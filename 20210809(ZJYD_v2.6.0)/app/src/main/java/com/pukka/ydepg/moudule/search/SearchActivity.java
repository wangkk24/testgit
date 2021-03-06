package com.pukka.ydepg.moudule.search;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.SuggestKeywordCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODListCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.ubd.UBDService;
import com.pukka.ydepg.common.utils.ClickUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.common.utils.uiutil.Strings;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.RvLinearLayoutManager;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.http.hecaiyun.HecaiyunServiceImpl;
import com.pukka.ydepg.launcher.http.hecaiyun.data.BoothCarouselCont;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.launcher.view.topic.TopicViewManager;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.browse.VodSubjectCallBack;
import com.pukka.ydepg.moudule.featured.view.LimitScrollViewPager;
import com.pukka.ydepg.moudule.featured.view.TopicService;
import com.pukka.ydepg.moudule.search.adapter.EveryoneSearchAdapter;
import com.pukka.ydepg.moudule.search.adapter.ResultFragmentPagerAdapter;
import com.pukka.ydepg.moudule.search.adapter.SearchActorAdapter;
import com.pukka.ydepg.moudule.search.adapter.SearchRankingAdapter;
import com.pukka.ydepg.moudule.search.adapter.SearchSubjectAdapter;
import com.pukka.ydepg.moudule.search.adapter.SearchSuggestAdapter;
import com.pukka.ydepg.moudule.search.bean.SearchActorBean;
import com.pukka.ydepg.moudule.search.bean.SearchSubjectBean;
import com.pukka.ydepg.moudule.search.event.LoadFinishEvent;
import com.pukka.ydepg.moudule.search.event.SearchKeyEvent;
import com.pukka.ydepg.moudule.search.presenter.SearchPresenter;
import com.pukka.ydepg.moudule.search.utils.ChineseUtil;
import com.pukka.ydepg.moudule.search.utils.SearchUtils;
import com.pukka.ydepg.moudule.search.view.FocusLinearLayout;
import com.pukka.ydepg.moudule.search.view.SearchDataView;
import com.pukka.ydepg.moudule.search.view.SearchKeyBoardView;
import com.pukka.ydepg.moudule.vod.presenter.MoviesListPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pukka.ydepg.launcher.Constant.SEARCH_SUGGEST_TITLE;

/**
 * ????????????
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.SearchActivity.java
 * @author:xj
 * @date: 2017-12-15 10:19
 */
public class SearchActivity extends BaseActivity implements
        SearchDataView,
        SuggestKeywordCallBack,
        SearchKeyBoardView.OnKeyBoardClickListener,  //????????????
        SearchActorAdapter.OnItemClickListener,      //????????????
        SearchSuggestAdapter.OnItemClickListener,    //?????????????????????
        SearchSubjectAdapter.OnItemClickListener,    //??????????????????
        EveryoneSearchAdapter.OnItemClickListener{   //?????????????????????

    private static final String TAG = SearchActivity.class.getSimpleName();

    public static boolean isIsFromSearchActivity() {
        return isFromSearchActivity;
    }

    public static void setIsFromSearchActivity(boolean isFromSearchActivity) {
        SearchActivity.isFromSearchActivity = isFromSearchActivity;
    }

    //????????????????????????,?????????????????????????????????????????????????????????
    private static boolean isFromSearchActivity = false;

    private static final int  Max_Search_Length = 29;

    private static final long MIN_CLICK_INTERVAL = 350L;

    //??????????????????
    @BindView(R.id.search_result_num)
    TextView mSearchResultNum;

    //????????????
    @BindView(R.id.tv_search_all)
    TextView mSearchAll;

    // ????????????/????????? [????????????]
    @BindView(R.id.search_current_line)
    TextView mSearchCurrentLine;

    // ????????????/????????? [?????????"/"]
    @BindView(R.id.search_line_separator)
    TextView mSearchLineSeparator;

    // ????????????/????????? [?????????]
    @BindView(R.id.search_total_line)
    TextView mSearchTotalLine;

    @BindView(R.id.search_result_viewPager)
    ViewPager mResultViewPager;

    //????????????(???????????????????????????),???????????????????????????,?????????????????????
    @BindView(R.id.search_tip)
    ImageView mSearchTip;

    // ?????????????????????,????????????????????????,?????????????????????
    @BindView(R.id.search_association)
    LinearLayout mSearchAssociationLayout;

    // ?????????????????????
    @BindView(R.id.search_suggest_title)
    TextView mSuggestTitle;

    // [??????????????????]???????????????RecycleView
    @BindView(R.id.search_association_recycle)
    RecyclerView mSuggestKeywordRecycleView;

    //??????????????????
    @BindView(R.id.search_first_guide)
    ImageView mSearchFirstGuide;

    //?????????
    @BindView(R.id.search_edit)
    EditText mEditText;

    //???????????????(??????)
    @BindView(R.id.search_everyone_search_list)
    RecyclerView mEveryoneSearch;

    //??????
    @BindView(R.id.search_keyboard)
    SearchKeyBoardView mKeyBoardView;

    //????????????
    @BindView(R.id.search_actor_title)
    TextView mActorTitle;

    //??????
    @BindView(R.id.search_actor_layout)
    RecyclerView mActorRecyclerView;

    //????????????(????????????????????????)
    @BindView(R.id.search_vod_ranking_list)
    RecyclerView searchVodRankingList;

    //????????????????????????????????????(????????????????????????????????????????????????)
    @BindView(R.id.search_default_layout)
    View mSearchDefaultLayout;

    //????????????????????????????????????(????????????????????????????????????????????????)
    @BindView(R.id.search_result_layout)
    View mSearchResultLayout;

    //?????????????????????????????????(????????????mSearchResultLayout???)
    @BindView(R.id.tv_search_subject_recycleview)
    RecyclerViewTV mSearchSubjectRecycle;

    //?????????????????????(??????R.id.search_main???)
    @BindView(R.id.ll_container)
    FocusLinearLayout mContainerLayout;

    //?????????????????????
    @BindView(R.id.search_main)
    BrowseFrameLayout mSearchMainLayout;

    private View mLastFocusView;

    //[??????]?????????
    SearchActorAdapter mActorAdapter;

    //[????????????]?????????
    SearchSubjectAdapter mSearchSubjectAdapter;

    //[????????????]?????????
    SearchRankingAdapter mSearchRankingAdapter;

    //[?????????]?????????
    SearchSuggestAdapter mSearchSuggestAdapter;

    //[???????????????]???????????????
    EveryoneSearchAdapter mEveryoneSearchAdapter;

    //????????????Fragment?????????(??????ViewPager)
    ResultFragmentPagerAdapter mResultFragmentPagerAdapter;

    //????????????
    List<VOD> mRankingList    = new ArrayList<>();

    //??????(???????????????)??????
    List<String> mHotKeyList  = new ArrayList<>();

    //????????????
    List<SearchActorBean> mActorList = new ArrayList<>();

    //??????????????????ID(????????????????????????????????????????????????)
    private String mHotSubjectID;

    private List<SearchSubjectBean> mSubjectBeans;

    //?????????????????????[??????????????????](???????????????)
    private boolean mIsRequestSuggestKey = true;

    private boolean isLoading        = false;

    private int  searchResultGridNumber = 5;

    private int  selectPosition;

    //?????????key????????????????????????????????????
    private String   newKey;

    //???????????????
    private String   mSearchKey;

    private Handler  mHandler = new Handler();

    private SearchPresenter     mSearchPresenter;

    private MoviesListPresenter mMoviesListPresent;

    private VODListCallBack mVodListCallBack = new SearchVodSubjectCallBack();

    //??????????????????
    private Runnable mSuggestKeywordRunnable = new Runnable() {
        @Override
        public void run() {
            querySuggestLists(mSearchKey);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setmUnBinder(ButterKnife.bind(this));
        initData();
        initView();
        //???????????????????????????,?????????????????????????????????
        mMoviesListPresent.queryCatagoryDetail(mHotSubjectID, mVodListCallBack);
        //??????????????????
        mMoviesListPresent.loadMoviesContent(mHotSubjectID, "0", "50", mVodListCallBack);
    }

    private void initData() {
        //??????????????????????????????,??????????????????????????????????????????????????????????????????????????????
        ClickUtil.setInterval(TAG, MIN_CLICK_INTERVAL);
        //Presenter?????????
        mMoviesListPresent = new MoviesListPresenter(this);
        mSearchPresenter   = new SearchPresenter(this);
        mSearchPresenter.setmSearchDataView(this);
        isFromSearchActivity = true;
        mSubjectBeans      = SearchUtils.initSubjectData();         //?????????????????????
        mHotKeyList        = SearchUtils.initEveryoneSearchData();  //[???????????????]???????????????
        mActorList         = SearchUtils.initActorData();           //[??????]???????????????
        mHotSubjectID      = SearchUtils.initHotSubject();          //[????????????]??????ID?????????
    }

    private static final int MIN_CLICK_DELAY   = 300;
    private long lastClickTime;
    private boolean canClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY) {
            flag = true;
            lastClickTime = curClickTime;
        }
        return flag;
    }

    private void initView() {
        mContainerLayout.setInterceptor(new FocusInterceptor() {
            @Override
            public boolean interceptFocus(KeyEvent event, View view) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && mSearchResultLayout.findFocus() != null) {
                    //return ClickUtil.isFastDoubleClick(TAG);
                    return !canClick();
                } if (mSearchSubjectRecycle.hasFocus() && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT ||event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT)){
                    int childLayoutPosition = mSearchSubjectRecycle.getChildLayoutPosition(mSearchSubjectRecycle.getFocusedChild());
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && childLayoutPosition > -1 && childLayoutPosition < mSearchSubjectAdapter.getItemCount()-1
                     && null != mSearchSubjectRecycle.findViewHolderForAdapterPosition(childLayoutPosition+1)){
                        ((SearchSubjectAdapter.SearchSubjectViewHolder)(mSearchSubjectRecycle.findViewHolderForAdapterPosition(childLayoutPosition+1))).tv.requestFocus();
                        return true;
                    }else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && childLayoutPosition > 0 && null != mSearchSubjectRecycle.findViewHolderForAdapterPosition(childLayoutPosition-1)){
                        ((SearchSubjectAdapter.SearchSubjectViewHolder)(mSearchSubjectRecycle.findViewHolderForAdapterPosition(childLayoutPosition-1))).tv.requestFocus();
                        return true;
                    }
                    return false;
                }else {
                    return false;
                }
            }
        });

        if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
            //???????????????????????????????????????
            mActorTitle.setText(R.string.search_actor_child);

            boolean isFirstSearch = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.FIRST_TIME_USE_SEARCH.toString(),true);
            if( isFirstSearch ){
                //????????????????????????????????????
                mSearchFirstGuide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mKeyBoardView.requestFirstFocus();
                        v.setVisibility(View.GONE);
                    }
                });
                mSearchFirstGuide.setVisibility(View.VISIBLE);
                mSearchFirstGuide.requestFocus();
                SharedPreferenceUtil.getInstance().putBoolean(SharedPreferenceUtil.Key.FIRST_TIME_USE_SEARCH.toString(),false);
            } else {
                //??????????????????????????????????????????
                mSearchFirstGuide.setVisibility(View.GONE);
                //??????????????????????????????????????????
                mKeyBoardView.requestFirstFocus();
            }
        } else {
            //?????????????????????????????????????????????
            mKeyBoardView.requestFirstFocus();
        }

        setTotal(0,true);

        mSearchResultNum.setVisibility(View.GONE);
        mKeyBoardView.setOnKeyBoardClickListener(this);

        //[??????]
        mActorAdapter = new SearchActorAdapter(this, this);
        mActorAdapter.setOnItemClickListener(this);
        mActorAdapter.setmData(mActorList);
        mActorRecyclerView.setAdapter(mActorAdapter);
        mActorRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        //??????????????????
        mSearchSubjectAdapter = new SearchSubjectAdapter(this,mSubjectBeans);
        mSearchSubjectAdapter.setOnItemClickListener(this);
        mSearchSubjectRecycle.setLayoutManager(new RvLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false,mSearchSubjectRecycle));
        mSearchSubjectRecycle.setAdapter(mSearchSubjectAdapter);

        //[????????????]
        mSearchRankingAdapter = new SearchRankingAdapter(this, mRankingList);
        searchVodRankingList.setAdapter(mSearchRankingAdapter);
        searchVodRankingList.setLayoutManager(new GridLayoutManager(this, 4));

        //[??????????????????]???????????????
        mSearchSuggestAdapter = new SearchSuggestAdapter(this, this);
        mSearchSuggestAdapter.setOnItemClickListener(this);
        mSuggestKeywordRecycleView.setAdapter(mSearchSuggestAdapter);
        mSuggestKeywordRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setSuggestTitle();

        //[???????????????]
        mEveryoneSearchAdapter = new EveryoneSearchAdapter(this, this);
        mEveryoneSearchAdapter.setmData(mHotKeyList);
        mEveryoneSearch.setAdapter(mEveryoneSearchAdapter);
        mEveryoneSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //????????????????????????????????????
        String hint = Strings.getInstance().getString(R.string.et_search_hint_text);
        SpannableStringBuilder builder = new SpannableStringBuilder(hint);
        builder.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.c21_color)), 8, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.c21_color)), 12, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.T31_C21_Light_size), false), 0 , hint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEditText.setHint(builder);
        mEditText.addTextChangedListener(new SearchTextWatcher());

        //??????????????????
        mResultViewPager.setOffscreenPageLimit(4);

        //?????????????????????????????????????????????
        mSearchMainLayout.setOnFocusSearchListener(new OnSearchFocusSearchListener());
        mSearchMainLayout.setOnChildFocusListener(new OnSearchChildFocusListener());
    }

    private boolean isBorder(ViewGroup root, View focused, int direction) {
        return FocusFinder.getInstance().findNextFocus(root, focused, direction) == null;
    }

    private void querySuggestLists(String key) {
        mSearchPresenter.suggestKeyWords(key, this);
    }

    //????????????(?????????/T9??????/???????????? ???????????????????????????)
    @Override
    public void onKeyBoardClick(String key) {
        changeEditText(true, key);
    }

    @Override
    public void onDelete() {
        changeEditText(false, "");
    }

    @Override
    public void onDeleteAll() {
        if (null != mEditText) {
            mEditText.setText("");
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param isAdd
     * @param key
     */
    private void changeEditText(boolean isAdd, String key) {
        if (null == mEditText) {
            return;
        }
        Editable text = mEditText.getText();
        if (isAdd) {//??????
            if (text.length() >= Max_Search_Length) {
                EpgToast.showLongToast(this, "????????????????????????????????????");
                return;
            }
            if (text.length() > 0) {
                String newText = text.toString() + key;
                mEditText.setText(newText);
                //??????????????????
                mEditText.setSelection(newText.length());
            } else {
                mEditText.setText(key);
                //??????????????????
                mEditText.setSelection(key.length());
            }
        } else {//??????
            if (null != text && text.length() > 0) {
                String subText = text.toString().substring(0, text.length() - 1);
                mEditText.setText(subText);
                //??????????????????
                mEditText.setSelection(subText.length());
            }
        }
    }

    //??????????????????
    @Override
    public void searchHotSuccess(int total, List<String> hotKeys) {
        if (null != hotKeys && !hotKeys.isEmpty()) {
            mHotKeyList.clear();
            mHotKeyList.addAll(hotKeys);
            mEveryoneSearchAdapter.setmData(mHotKeyList);
        }
    }

    @Override
    public void searchHotKeyFail() { }

    //?????????????????????
    @Override
    public void queryHotContentListSuccess(List<Content> contents) { }

    @Override
    public void queryHotContentListFailed() { }

    //???????????????[??????????????????]??????????????????????????????
    @Override
    public void suggestKeywordSuccess(List<String> suggests) {
        if ( null != suggests && suggests.size()>0 ) {
            mSearchSuggestAdapter.setmData(suggests);
            mSuggestKeywordRecycleView.scrollToPosition(0);
            showSuggestKeyword(true);
        } else {
            //??????????????????,??????????????????
            showSuggestKeyword(false);
        }
    }

    //???????????????[??????????????????]??????????????????????????????
    @Override
    public void suggestKeywordFail() {
        //?????????????????????,??????????????????
        showSuggestKeyword(false);
    }

    private void showSuggestKeyword(boolean show){
        if(show){
            mSearchAssociationLayout.setVisibility(View.VISIBLE);
            mSearchTip.setVisibility(View.GONE);
        }else{
            mSearchAssociationLayout.setVisibility(View.GONE);
            mSearchTip.setVisibility(View.VISIBLE);
        }
    }

    //??????[??????????????????](?????????????????????)??????????????????????????????
    @Override
    public void onAssociationItemClick(String key) {
        mSearchSubjectAdapter.setFocusSubject(0);//???????????????????????????
        mIsRequestSuggestKey = false;//??????[??????????????????]??????????????????????????????
        mEditText.setText(key);
        showSuggestKeyword(false);//?????????????????????????????????????????????
    }

    //??????[???????????????]??????????????????????????????
    @Override
    public void onEveryoneSearchItemClick(String key) {
        mSearchSubjectAdapter.setFocusSubject(0);//???????????????????????????
        mIsRequestSuggestKey = false;//??????[???????????????]??????????????????????????????
        mEditText.setText(key);
    }

    //??????[??????](?????????/??????)/[????????????](?????????)??????????????????????????????
    @Override
    public void onSearchActorItemClick(SearchActorBean actorBean) {
        switch ( SearchUtils.getActorType(actorBean)){
            case WEB: {
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", actorBean.getUrl());
                startActivity(intent);
                break;
            }
            case TOPIC: {
                Topic topic = TopicService.getInstance().getTopicMap().get(actorBean.getCategoryID());
                if (topic != null && TopicViewManager.isStyleIdValid(topic.getTopicStyleId())) {
                    Intent intent = new Intent(this, TopicActivity.class);
                    intent.putExtra(TopicActivity.TOPIC_OBJECT,topic);
                    startActivity(intent);
                }
                break;
            }
            case SEARCH: {
                mSearchSubjectAdapter.setFocusSubject(0);//???????????????????????????
                mEditText.setText(actorBean.getName());
                //gotoSearch(actorBean.getName());
                break;
            }
            case UNKNOWN:
            default: {
                SuperLog.error(TAG,"Search hot actor parameter[search_hot_character] configured incorrectly.");
                break;
            }
        }
    }

    //????????????
    private void gotoSearch(String key) {
        //UBD??????????????????
        UBDService.recordSearchKey(key);
        mSearchKey = key;
        ChineseUtil.setSearchKey(mSearchKey);
        if (StringUtils.containSymbol(key)) {
            EpgToast.showLongToast(SearchActivity.this, "??????????????????");
        }
        //???????????????????????????
        mSearchDefaultLayout.setVisibility(View.GONE);
        mSearchResultLayout.setVisibility(View.VISIBLE);
        isLoading=true;
        newKey=key;

        if("????????????".equals(mSubjectBeans.get(0).getSubjectName())){
            searchResultGridNumber = 4;
        } else {
            searchResultGridNumber = 5;
        }

        if (null == mResultFragmentPagerAdapter) {
            mResultViewPager.setEnabled(false);
            mResultViewPager.setAdapter(mResultFragmentPagerAdapter = new ResultFragmentPagerAdapter(getSupportFragmentManager(), mSubjectBeans));
            mSearchAll.setTextColor(ContextCompat.getColor(this, R.color.c30_color));
        } else {
            mSearchAll.setTextColor(ContextCompat.getColor(this, R.color.c30_color));
            selectPosition = 0;
            mResultViewPager.setCurrentItem(0);       //????????????????????????????????????????????????
            EventBus.getDefault().post(new SearchKeyEvent(key));
        }

        mSearchSubjectRecycle.scrollToPosition(0);    //??????????????????????????????????????????
        mSearchSubjectAdapter.setSelectSubject(0);    //????????????????????????????????????(Adapter???onBindViewHolder??????)
        mSearchSubjectAdapter.notifyDataSetChanged();
    }

    public void setTotal(Integer total,boolean isNeedReload) {
        if ( !isNeedReload ) {
            mSearchResultNum.setVisibility(View.VISIBLE);
            mSearchResultNum.setText(String.format(getString(R.string.search_result_num), total.toString()));
        }

        int totalResultLine = (total==0) ? 0 : (total - 1)/searchResultGridNumber + 1;
        mSearchTotalLine.setText(String.valueOf(totalResultLine));

        if (0==total.intValue()) {
            mSearchCurrentLine.setVisibility(View.GONE);
            mSearchLineSeparator.setVisibility(View.GONE);
            mSearchTotalLine.setVisibility(View.GONE);
        } else {
            mSearchCurrentLine.setVisibility(View.VISIBLE);
            mSearchLineSeparator.setVisibility(View.VISIBLE);
            mSearchTotalLine.setVisibility(View.VISIBLE);
        }
    }

    public void setCustomPage(String customPage) {
        mSearchCurrentLine.setText(customPage + "");
    }

    public void setFocusView() {
        if (null != mLastFocusView) {
            mLastFocusView.requestFocus();
        }
    }

    @Subscribe
    public void onLoadFinshEvent(LoadFinishEvent event){
        String requestKey = event.getKey();
        SuperLog.debug(TAG,"LoadFinishEvent->newKey:"+newKey+" | requestKey:"+requestKey);
        if(TextUtils.isEmpty(newKey)){
            isLoading = false;
        }else {
            if (newKey.equals(requestKey))
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                    }
                }, 100);
        }
    }

    @Override
    public void onSubjectItemClick(String key, int position, int searchResultGridNumber) {
        if (null != mResultViewPager) {
            //mResultViewPager.getChildAt(selectPosition ).setVisibility(View.GONE);
            this.searchResultGridNumber = searchResultGridNumber;
            mResultViewPager.setCurrentItem(position,false);
            selectPosition = position;
            //mResultViewPager.getChildAt(selectPosition ).setVisibility(View.VISIBLE);
        }
    }

    //??????????????????(??????????????????????????????/??????/????????????)
    private void gotoSearchHome(){
        mSearchTip.setVisibility(View.VISIBLE);
        if (mSearchDefaultLayout.getVisibility() != View.VISIBLE) {
            mSearchDefaultLayout.setVisibility(View.VISIBLE);
        }
        if (mSearchResultLayout.getVisibility() != View.GONE) {
            mSearchResultLayout.setVisibility(View.GONE);
        }
        //??????????????????(??????M)
        mKeyBoardView.requestFirstFocus();
    }

    private class SearchTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            //??????????????? ?????????????????????????????? ??????????????????
            selectPosition = 0;
            mSearchSuggestAdapter.resetCurrentPosition();
            if (!TextUtils.isEmpty(s)){
                mSearchKey = s.toString();
            }
            if (null != s && s.length() > 0) {
                //??????????????????
                if (mSearchDefaultLayout.getVisibility() != View.GONE) {
                    mSearchDefaultLayout.setVisibility(View.GONE);
                }
                if (mSearchResultLayout.getVisibility() != View.VISIBLE) {
                    mSearchResultLayout.setVisibility(View.VISIBLE);
                }
                gotoSearch(s.toString());
                //DEBUG????????????
//                if(BuildConfig.DEBUG){
//                    HecaiCloudService.login(new HecaiCloudService.OnResponse() {
//                        @Override
//                        public void loginCallback(String result) {
//                            SuperLog.info2SD(TAG,"Get Hecaiyun login response successfully,result="+result);
//                        }
//                    });
//                }
            } else {
                //??????????????????
                gotoSearchHome();
            }

            //???????????????????????????????????????
            if (SearchSuggestAdapter.isKeySuggestOpen()) {
                if ( null != s && s.length() > 0 ) {
                    if (mIsRequestSuggestKey) {
                        if (null != mSuggestKeywordRunnable){
                            mHandler.removeCallbacks(mSuggestKeywordRunnable);
                        }
                        mHandler.postDelayed(mSuggestKeywordRunnable, 500);
                    } else {
                        //??????[???????????????]????????????????????????
                        mIsRequestSuggestKey = true;
                    }
                } else {
                    //?????????????????????,??????????????????
                    if (null != mSuggestKeywordRunnable){
                        //??????????????????????????????,?????????????????????
                        mHandler.removeCallbacks(mSuggestKeywordRunnable);
                    }
                    showSuggestKeyword(false);
                }
            }
        }
    }

    private class OnSearchFocusSearchListener implements BrowseFrameLayout.OnFocusSearchListener {
        /**
         * ?????????ChildView??????????????????????????????????????????,?????????ChildView??????????????????????????????????????????
         *
         * @param focused    ????????????View
         * @param direction  ???????????????????????????
         *
         * @return ????????????????????????View
         */
        @Override
        public View onFocusSearch(View focused, int direction) {
            SuperLog.debug(TAG, "onFocusSearch");
            mLastFocusView = focused;
            switch (direction) {
                case View.FOCUS_DOWN:
                {
                    if (focused.getId() == R.id.tv_search_Variety || focused.getId() == R.id.tv_search_tvActor || focused.getId() == R.id.tv_search_miplay) {
                        RecyclerView parent = mResultViewPager.getChildAt(selectPosition ).findViewById(R.id.search_fragment_result);
                        if (parent != null) {
                            int allcount = parent.getLayoutManager().getItemCount();
                            if (allcount == 1) {
                                return parent.getLayoutManager().findViewByPosition(0);
                            } else if (allcount == 2) {
                                return parent.getLayoutManager().findViewByPosition(1);
                            }
                        }
                    } else if (focused.getId() == R.id.actor_item_layout){
                        if( mActorAdapter != null && mActorAdapter.getItemCount() > 0
                                && searchVodRankingList != null && mSearchRankingAdapter != null
                                && mSearchRankingAdapter.getItemCount() > 0){
                            if(mSearchRankingAdapter.getItemCount() > (mActorAdapter.getCurrentPosition() % 5)) {
                                return searchVodRankingList.getChildAt(mActorAdapter.getCurrentPosition() % 5 > 3 ? 3:(mActorAdapter.getCurrentPosition() % 5));
                            }else{
                                return searchVodRankingList.getChildAt(mSearchRankingAdapter.getItemCount() - 1);
                            }
                        }
                    } else if (focused.getId() == R.id.association_item_layout){
                        if(mEveryoneSearchAdapter != null && mEveryoneSearchAdapter.getCurrentPosition() == (mEveryoneSearchAdapter.getItemCount() - 1)){
                            return mEveryoneSearch.getChildAt(mEveryoneSearchAdapter.getCurrentPosition() - ((LinearLayoutManager)mEveryoneSearch.getLayoutManager()).findFirstVisibleItemPosition());
                        }
                    } else if (focused.getId() == R.id.search_suggest_item_layout){
                        if(mSearchSuggestAdapter != null && mSearchSuggestAdapter.getCurrentPosition() == (mSearchSuggestAdapter.getItemCount() - 1)){
                            return focused;
                        }
                    } else if(focused.getId() == R.id.search_vod_bg && mSearchDefaultLayout.getVisibility() == View.GONE){
                        RecyclerView parent=null;
                        if (null != mResultViewPager.findViewWithTag(mSubjectBeans.get(selectPosition).getId()) && null != mResultViewPager.findViewWithTag(mSubjectBeans.get(selectPosition).getId()).findViewById(R.id.search_fragment_result)){
                            parent = mResultViewPager.findViewWithTag(mSubjectBeans.get(selectPosition).getId()).findViewById(R.id.search_fragment_result);
                         }
                        if (parent != null) {
                            int position = parent.getLayoutManager().getPosition(focused);
                            int itemCount = parent.getLayoutManager().getItemCount();
                            SuperLog.debug(TAG, "focused position is " + position);
                            SuperLog.debug(TAG, "focused itemCount is " + itemCount);
                            if (position / searchResultGridNumber == ((itemCount - 1) / searchResultGridNumber)){
                                return focused;
                            } else {
                                View lastView = parent.getLayoutManager().findViewByPosition(position + searchResultGridNumber);
                                SuperLog.debug(TAG + "focus", "lastView is " + (lastView));
                                if (null == lastView) {
                                    SuperLog.debug(TAG + "focus", "lastView is " + (parent.findViewHolderForLayoutPosition(position + searchResultGridNumber)));
                                    if (null != parent.findViewHolderForLayoutPosition(position + searchResultGridNumber)) {
                                        lastView = parent.findViewHolderForLayoutPosition(position + searchResultGridNumber).itemView;
                                    }
                                    SuperLog.debug(TAG + "focus", "lastView is " + (lastView));
                                }
                                SuperLog.debug(TAG + "focus", "lastView is " + (lastView));
                                if(lastView == null){
                                    return focused;
                                } else {
                                    return lastView;
                                }
                            }
                        }
                    } else if (focused.getId() == R.id.search_vod_bg && mSearchDefaultLayout.getVisibility() == View.VISIBLE){
                        int position = searchVodRankingList.getLayoutManager().getPosition(focused);
                        int itemCount = searchVodRankingList.getLayoutManager().getItemCount();
                        SuperLog.debug(TAG, "focused position is " + position);
                        SuperLog.debug(TAG, "focused itemCount is " + itemCount);
                        if (position / 4 == ((itemCount - 1) / 4)) {
                            return focused;
                        } else {
                            View lastView = searchVodRankingList.getLayoutManager().findViewByPosition(position + 4);
                            SuperLog.debug(TAG + "focus", "lastView is " + (lastView));
                            if (null == lastView) {
                                SuperLog.debug(TAG + "focus", "lastView is " + (searchVodRankingList.findViewHolderForLayoutPosition(position + 4)));
                                if (null != searchVodRankingList.findViewHolderForLayoutPosition(position + 4)) {
                                    lastView = searchVodRankingList.findViewHolderForLayoutPosition(position + 4).itemView;
                                }
                                SuperLog.debug(TAG + "focus", "lastView is " + (lastView));
                            }
                            SuperLog.debug(TAG + "focus", "lastView is " + (lastView));
                            if (lastView == null) {
                                return focused;
                            } else {
                                return lastView;
                            }
                        }
                    } else if (focused.getId() == R.id.switch_char_keyboard
                        || focused.getId() == R.id.switch_number_keyboard
                        || focused.getId() == R.id.switch_t9_keyboard){
                        if(mSearchTip.getVisibility() == View.VISIBLE){
                            //?????????????????????(?????????/T9??????/123?????????,????????????????????????????????????)
                            return focused;
                        }
                    } else if (focused.getId() == R.id.keyboard_delete_btn &&isLoading){
                        //??????????????????????????????
                        return focused;
                    }
                    break;
                }
                case View.FOCUS_UP:
                {
                    if(focused.getId() == R.id.search_vod_bg && mSearchDefaultLayout.getVisibility() == View.VISIBLE){
                        if(mSearchRankingAdapter != null && mSearchRankingAdapter.getItemCount() > 0
                                && mSearchRankingAdapter.getCurrentPosition() / 4 == 0 && mActorRecyclerView != null
                                && mActorAdapter != null && mActorAdapter.getItemCount() > 0){
                            if(mActorAdapter.getItemCount() > mSearchRankingAdapter.getCurrentPosition()){
                                return mActorRecyclerView.getChildAt(mSearchRankingAdapter.getCurrentPosition() + mActorAdapter.getItemCount() / 5 * 5 - ((LinearLayoutManager) mActorRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
                            } else {
                                return mActorRecyclerView.getChildAt(mActorAdapter.getItemCount() - 1 - ((LinearLayoutManager) mActorRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
                            }
                        } else {
                            return focused;
                        }
                    }

                    if(focused.getId() == R.id.search_vod_bg && mSearchResultLayout.getVisibility() == View.VISIBLE&&null!=mSearchSubjectRecycle.findViewHolderForAdapterPosition(selectPosition)){
                        //???????????????????????????????????????????????????????????????????????????,??????????????????????????????????????????????????????
                        return ((SearchSubjectAdapter.SearchSubjectViewHolder)(mSearchSubjectRecycle.findViewHolderForAdapterPosition(selectPosition))).tv;
                    }

                    if (focused.getId() == R.id.keyboard_item_text && mKeyBoardView.getCurrentLine() == 0){
                        return focused;
                    }
                    break;
                }
                case View.FOCUS_RIGHT:
                {
                    if(focused.getId() == R.id.association_item_layout && mSearchDefaultLayout.getVisibility() == View.VISIBLE){
                        if(mActorAdapter.getItemCount() == 0){
                            if(mSearchRankingAdapter.getItemCount() == 0) {
                                return focused;
                            } else {
                                return searchVodRankingList.getChildAt(0);
                            }
                        }
                    }

                    if ( focused.getId() == R.id.keyboard_delete_btn      //?????????????????????
                        || focused.getId() == R.id.switch_number_keyboard //???????????????????????????(123)??????
                        || focused.getId() == R.id.item_t9_layout         //?????????T9????????????
                        || focused.getId() == R.id.keyboard_item_text )   //????????????????????????????????????
                    {
                        //???[???????????????]????????? ???????????????????????????????????????
                        if(mSearchDefaultLayout.getVisibility() == View.VISIBLE
                            && mEveryoneSearchAdapter != null && mEveryoneSearchAdapter.getItemCount() > 0){
                            return mEveryoneSearch.getChildAt(0);
                        }

                        //????????????????????????????????????"??????"????????????
                        /*if(mResultViewPager.getChildCount() > 0 && mResultViewPager.getChildAt(0).findViewById(R.id.no_search_result).getVisibility() == View.VISIBLE){
                            //return mSearchSubjectRecycle.getChildAt(0);
                            return ((SearchSubjectAdapter.SearchSubjectViewHolder)(mSearchSubjectRecycle.findViewHolderForAdapterPosition(0))).tv;
                        }*/
                        if (null!=mSearchSubjectRecycle.findViewHolderForAdapterPosition(0)){
                            return ((SearchSubjectAdapter.SearchSubjectViewHolder)(mSearchSubjectRecycle.findViewHolderForAdapterPosition(0))).tv;
                        }
                    }
                    break;
                }
                case View.FOCUS_LEFT:
                {
                    if( mSearchDefaultLayout.getVisibility() == View.VISIBLE){
                        if(focused.getId() == R.id.actor_item_layout || focused.getId() == R.id.search_vod_bg){
                            if(mActorAdapter != null && mActorAdapter.getItemCount() > 0 && mActorAdapter.getCurrentPosition() % 4 == 0){
                                if(mEveryoneSearchAdapter != null && mEveryoneSearchAdapter.getItemCount() > 0){
                                    return mEveryoneSearch.getChildAt(0);
                                }
                            }
                            if(mSearchRankingAdapter != null && mSearchRankingAdapter.getItemCount() > 0 && mSearchRankingAdapter.getCurrentPosition() % 4 == 0){
                                if(mEveryoneSearchAdapter != null && mEveryoneSearchAdapter.getItemCount() > 0){
                                    return mEveryoneSearch.getChildAt(0);
                                }
                            }
                        }
                    }
                    if (focused.getId() == R.id.keyboard_clear_btn||focused.getId() == R.id.switch_char_keyboard) {
                        return focused;
                    }
                    break;
                }
                default:
                    break;
            }

            //??????????????????????????????
            if(focused.getId() == R.id.keyboard_item_layout && isLoading){
                RecyclerView parent = (RecyclerView) focused.getParent();
                boolean border = isBorder(parent, focused, View.FOCUS_RIGHT);
                if(border&&direction==View.FOCUS_RIGHT){
                    return focused;
                }
            }

            if(focused.getId() == R.id.search_vod_bg) {
                RecyclerView parent = (RecyclerView) focused.getParent();
                boolean border = isBorder(parent, focused, View.FOCUS_LEFT);
                if (border && direction == View.FOCUS_LEFT) {
                    return mKeyBoardView.requestDeleteFocus();
                }

                //ZJYDEPGAPP-364 ????????????????????????????????????"a"??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                boolean borderRight = isBorder(parent, focused, View.FOCUS_RIGHT);
                if (!borderRight && direction == View.FOCUS_RIGHT) {
                    int position = parent.getChildAdapterPosition(focused);
                    int allCount = parent.getLayoutManager().getItemCount();
                    if (allCount - 1 > position && (position + 1) % searchResultGridNumber != 0) {
                        View lastView = parent.getLayoutManager().findViewByPosition(position + 1);
                        if (null == lastView) {
                            if (null != parent.findViewHolderForLayoutPosition(position + 1)) {
                                lastView = parent.findViewHolderForLayoutPosition(position + 1).itemView;
                            }
                        }
                        return lastView;
                    }
                }
            }
            return null;
        }
    }

    private class OnSearchChildFocusListener implements BrowseFrameLayout.OnChildFocusListener {
        @Override
        public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
            return false;
        }
        /**
         * ????????????ChildView?????????/?????????ChildView????????????????????????,???????????????
         *
         * @param child    ????????????
         * @param focused  ?????????????????????,?????????????????????View
         */
        @Override
        public void onRequestChildFocus(View child, View focused) {
            if (focused instanceof LimitScrollViewPager) {
                mLastFocusView.requestFocus();
            }
        }
    }

    private class SearchVodSubjectCallBack extends VodSubjectCallBack {
        @Override
        public void queryVODSubjectListSuccess(int total, List<Subject> subjects) { }

        @Override
        public void queryVODSubjectListFailed() { }
        //??????????????????????????????(???????????????????????????)???
        @Override
        public void querySubjectDetailSuccess(int total, List<Subject> subjects) {
            if(null != subjects && subjects.size() > 0 && null != subjects.get(0).getPicture()
                    && null != subjects.get(0).getPicture().getPosters()
                    && subjects.get(0).getPicture().getPosters().size() > 0){
                RequestOptions options  = new RequestOptions()
                        .placeholder(getResources().getDrawable(R.drawable.default_poster))
                        .error(R.drawable.search_voice_guide)
                        .error(R.drawable.search_voice_guide);
                Glide.with(SearchActivity.this).load(subjects.get(0).getPicture().getPosters().get(0)).apply(options).into(mSearchTip);
            }
        }

        @Override
        public void queryPSBRecommendSuccess(int total, List<VOD> vodDetails) {
            //No need to implement this interface
        }

        @Override
        public void queryPSBRecommendFail() {
            //No need to implement this interface
        }

        @Override
        public void onError() { }
        //????????????????????????
        @Override
        public void queryVODListBySubjectSuccess(int total, List<VOD> vodList, String subjectId) {
            if (null != vodList && !vodList.isEmpty() && getIsResume() ) {
                searchVodRankingList.setVisibility(View.VISIBLE);
                mRankingList.clear();
                mRankingList.addAll(vodList);
                mSearchRankingAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ClickUtil.isFastDoubleClick(TAG)){
            return true;
        }

        //DEBUG????????????
//        if (BuildConfig.DEBUG && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//            HecaiyunServiceImpl hcy = new HecaiyunServiceImpl();
//            hcy.queryPic(new HecaiyunServiceImpl.OnResponse() {
//                @Override
//                public void onQueryListener(List<BoothCarouselCont> listBoothCarouselCon) {
//                    SuperLog.info2SD(TAG, "Get Hecaiyun pic finished");
//                }
//            });
//            //RefreshManager.getInstance().getScreenPresenter().start();
//        }

        //????????????????????????????????????,??????????????????????????????
        if ( mSearchFirstGuide.getVisibility() == View.VISIBLE && keyCode != KeyEvent.KEYCODE_BACK ){
            return true;
        }

        //????????????????????????????????????????????????????????????,??????????????????M???
        if ( mSearchResultLayout.getVisibility() == View.VISIBLE && keyCode == KeyEvent.KEYCODE_BACK){
            if( getCurrentFocus() !=null && getCurrentFocus().getId() == R.id.search_subject_name_text){
                //?????????????????????????????????????????????????????????????????????,???????????????
                return super.onKeyDown(keyCode, event);
            }

            //???????????????????????????????????????,???????????????????????????
            if(!mKeyBoardView.isCharKeyboard()){
                mKeyBoardView.switchKeyBoard(0);
            }
            mEditText.setText(null);
            return true;
        }

//        if(mSearchSuggestAdapter.isOnItemClick()) {
//            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && (mSearchSubjectAdapter.getmCurrentPosition() == 0 || (null != mResultFragmentPagerAdapter.getCurrentSearchResultFragment(selectPosition) && mResultFragmentPagerAdapter.getCurrentSearchResultFragment(selectPosition).getmFocusPosition() == 0))){
//                mSuggestKeywordRecycleView.getChildAt(mSearchSuggestAdapter.getClickPosition() - ((LinearLayoutManager) mSuggestKeywordRecycleView.getLayoutManager()).findFirstVisibleItemPosition()).findViewById(R.id.search_suggest_item_layout).requestFocus();
//                return true;
//            }
//            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && mSearchSuggestAdapter.getClickPosition() == mSearchSuggestAdapter.getCurrentPosition() && mSearchSuggestAdapter.getClickPosition() != -1){
//                mSearchSuggestAdapter.resetCurrentPosition();
//                mSearchSubjectRecycle.scrollToPosition(0);
//                mSearchSubjectRecycle.getChildAt(0).requestFocus();
//                return true;
//            }
//        }

        if( keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1
                || keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3
                || keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5
                || keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7
                || keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9){
            if(null != getCurrentFocus() && getCurrentFocus().getId() != R.id.keyboard_item_layout ){
                //???????????????????????????????????????????????????????????????????????????????????????,????????????????????????
                mSearchSubjectAdapter.setFocusSubject(0);
            }
            onKeyBoardClick(keyCode - 7 + "");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getmSearchKey() {
        return mSearchKey;
    }

    //???????????????????????????
    private void setSuggestTitle(){
        String title = CommonUtil.getConfigValue(SEARCH_SUGGEST_TITLE);
        if(!TextUtils.isEmpty(title)){
            mSuggestTitle.setText(title);
        }
    }
}