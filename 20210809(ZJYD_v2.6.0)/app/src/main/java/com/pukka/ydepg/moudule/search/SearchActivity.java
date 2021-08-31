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
 * 搜索界面
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.SearchActivity.java
 * @author:xj
 * @date: 2017-12-15 10:19
 */
public class SearchActivity extends BaseActivity implements
        SearchDataView,
        SuggestKeywordCallBack,
        SearchKeyBoardView.OnKeyBoardClickListener,  //键盘回调
        SearchActorAdapter.OnItemClickListener,      //影人回调
        SearchSuggestAdapter.OnItemClickListener,    //关键词联想回调
        SearchSubjectAdapter.OnItemClickListener,    //热搜视频回调
        EveryoneSearchAdapter.OnItemClickListener{   //大家都在搜回调

    private static final String TAG = SearchActivity.class.getSimpleName();

    public static boolean isIsFromSearchActivity() {
        return isFromSearchActivity;
    }

    public static void setIsFromSearchActivity(boolean isFromSearchActivity) {
        SearchActivity.isFromSearchActivity = isFromSearchActivity;
    }

    //是否来自搜索页面,用于返回首页后导航栏落焦到默认落焦栏位
    private static boolean isFromSearchActivity = false;

    private static final int  Max_Search_Length = 29;

    private static final long MIN_CLICK_INTERVAL = 350L;

    //搜索结果总数
    @BindView(R.id.search_result_num)
    TextView mSearchResultNum;

    //搜索全部
    @BindView(R.id.tv_search_all)
    TextView mSearchAll;

    // 当前行数/总行数 [当前行数]
    @BindView(R.id.search_current_line)
    TextView mSearchCurrentLine;

    // 当前行数/总行数 [分隔符"/"]
    @BindView(R.id.search_line_separator)
    TextView mSearchLineSeparator;

    // 当前行数/总行数 [总行数]
    @BindView(R.id.search_total_line)
    TextView mSearchTotalLine;

    @BindView(R.id.search_result_viewPager)
    ViewPager mResultViewPager;

    //搜索提示(键盘下边的引导图片),和搜索联想词同位置,二者之展示其一
    @BindView(R.id.search_tip)
    ImageView mSearchTip;

    // 搜索联想词布局,和搜索提示同位置,二者之展示其一
    @BindView(R.id.search_association)
    LinearLayout mSearchAssociationLayout;

    // 搜索联想词标题
    @BindView(R.id.search_suggest_title)
    TextView mSuggestTitle;

    // [你是不是在找]关键联想词RecycleView
    @BindView(R.id.search_association_recycle)
    RecyclerView mSuggestKeywordRecycleView;

    //首次搜索引导
    @BindView(R.id.search_first_guide)
    ImageView mSearchFirstGuide;

    //输入框
    @BindView(R.id.search_edit)
    EditText mEditText;

    //大家都在搜(热词)
    @BindView(R.id.search_everyone_search_list)
    RecyclerView mEveryoneSearch;

    //键盘
    @BindView(R.id.search_keyboard)
    SearchKeyBoardView mKeyBoardView;

    //影人标题
    @BindView(R.id.search_actor_title)
    TextView mActorTitle;

    //影人
    @BindView(R.id.search_actor_layout)
    RecyclerView mActorRecyclerView;

    //热搜视频(无搜索结果时展示)
    @BindView(R.id.search_vod_ranking_list)
    RecyclerView searchVodRankingList;

    //右边【热词和排行榜】布局(和【搜索结果整体】同时只显示一个)
    @BindView(R.id.search_default_layout)
    View mSearchDefaultLayout;

    //右边【搜索结果整体】布局(和【热词和排行榜】同时只显示一个)
    @BindView(R.id.search_result_layout)
    View mSearchResultLayout;

    //右边上部栏目导航栏布局(该布局在mSearchResultLayout中)
    @BindView(R.id.tv_search_subject_recycleview)
    RecyclerViewTV mSearchSubjectRecycle;

    //搜索页面总布局(包在R.id.search_main中)
    @BindView(R.id.ll_container)
    FocusLinearLayout mContainerLayout;

    //搜索页面总布局
    @BindView(R.id.search_main)
    BrowseFrameLayout mSearchMainLayout;

    private View mLastFocusView;

    //[影人]适配器
    SearchActorAdapter mActorAdapter;

    //[分类栏目]适配器
    SearchSubjectAdapter mSearchSubjectAdapter;

    //[热搜视频]适配器
    SearchRankingAdapter mSearchRankingAdapter;

    //[联想词]适配器
    SearchSuggestAdapter mSearchSuggestAdapter;

    //[大家都在搜]热词适配器
    EveryoneSearchAdapter mEveryoneSearchAdapter;

    //搜索结果Fragment适配器(用于ViewPager)
    ResultFragmentPagerAdapter mResultFragmentPagerAdapter;

    //排行列表
    List<VOD> mRankingList    = new ArrayList<>();

    //热词(大家都在搜)列表
    List<String> mHotKeyList  = new ArrayList<>();

    //演员列表
    List<SearchActorBean> mActorList = new ArrayList<>();

    //热搜视频栏目ID(用于展示热搜视频和键盘底部的图片)
    private String mHotSubjectID;

    private List<SearchSubjectBean> mSubjectBeans;

    //是否向平台请求[你是不是在找](关键词联想)
    private boolean mIsRequestSuggestKey = true;

    private boolean isLoading        = false;

    private int  searchResultGridNumber = 5;

    private int  selectPosition;

    //最新的key值，校验是否可以向右点击
    private String   newKey;

    //搜索关键词
    private String   mSearchKey;

    private Handler  mHandler = new Handler();

    private SearchPresenter     mSearchPresenter;

    private MoviesListPresenter mMoviesListPresent;

    private VODListCallBack mVodListCallBack = new SearchVodSubjectCallBack();

    //规避频繁联想
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
        //查询热搜栏目的图片,用于展示在搜索键盘底部
        mMoviesListPresent.queryCatagoryDetail(mHotSubjectID, mVodListCallBack);
        //查询热搜视频
        mMoviesListPresent.loadMoviesContent(mHotSubjectID, "0", "50", mVodListCallBack);
    }

    private void initData() {
        //设置快速点击时间间隔,防止浏览搜索结果时点击方向键下过快导致的焦点移动错误
        ClickUtil.setInterval(TAG, MIN_CLICK_INTERVAL);
        //Presenter初始化
        mMoviesListPresent = new MoviesListPresenter(this);
        mSearchPresenter   = new SearchPresenter(this);
        mSearchPresenter.setmSearchDataView(this);
        isFromSearchActivity = true;
        mSubjectBeans      = SearchUtils.initSubjectData();         //栏目数据初始化
        mHotKeyList        = SearchUtils.initEveryoneSearchData();  //[大家都在搜]数据初始化
        mActorList         = SearchUtils.initActorData();           //[影人]数据初始化
        mHotSubjectID      = SearchUtils.initHotSubject();          //[热搜视频]栏目ID初始化
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
            //儿童版使用动画大咖代替影人
            mActorTitle.setText(R.string.search_actor_child);

            boolean isFirstSearch = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.FIRST_TIME_USE_SEARCH.toString(),true);
            if( isFirstSearch ){
                //儿童版首次进入展示引导页
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
                //儿童版非首次进入不展示引导页
                mSearchFirstGuide.setVisibility(View.GONE);
                //键盘获取进入搜索后的默认落焦
                mKeyBoardView.requestFirstFocus();
            }
        } else {
            //普通版进入搜索直接获取默认落焦
            mKeyBoardView.requestFirstFocus();
        }

        setTotal(0,true);

        mSearchResultNum.setVisibility(View.GONE);
        mKeyBoardView.setOnKeyBoardClickListener(this);

        //[影人]
        mActorAdapter = new SearchActorAdapter(this, this);
        mActorAdapter.setOnItemClickListener(this);
        mActorAdapter.setmData(mActorList);
        mActorRecyclerView.setAdapter(mActorAdapter);
        mActorRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        //搜索分类栏目
        mSearchSubjectAdapter = new SearchSubjectAdapter(this,mSubjectBeans);
        mSearchSubjectAdapter.setOnItemClickListener(this);
        mSearchSubjectRecycle.setLayoutManager(new RvLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false,mSearchSubjectRecycle));
        mSearchSubjectRecycle.setAdapter(mSearchSubjectAdapter);

        //[热搜视频]
        mSearchRankingAdapter = new SearchRankingAdapter(this, mRankingList);
        searchVodRankingList.setAdapter(mSearchRankingAdapter);
        searchVodRankingList.setLayoutManager(new GridLayoutManager(this, 4));

        //[你是不是在找]关键词联想
        mSearchSuggestAdapter = new SearchSuggestAdapter(this, this);
        mSearchSuggestAdapter.setOnItemClickListener(this);
        mSuggestKeywordRecycleView.setAdapter(mSearchSuggestAdapter);
        mSuggestKeywordRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setSuggestTitle();

        //[大家都在搜]
        mEveryoneSearchAdapter = new EveryoneSearchAdapter(this, this);
        mEveryoneSearchAdapter.setmData(mHotKeyList);
        mEveryoneSearch.setAdapter(mEveryoneSearchAdapter);
        mEveryoneSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //搜索输入框布局初始化设置
        String hint = Strings.getInstance().getString(R.string.et_search_hint_text);
        SpannableStringBuilder builder = new SpannableStringBuilder(hint);
        builder.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.c21_color)), 8, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.c21_color)), 12, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.T31_C21_Light_size), false), 0 , hint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEditText.setHint(builder);
        mEditText.addTextChangedListener(new SearchTextWatcher());

        //搜索结果布局
        mResultViewPager.setOffscreenPageLimit(4);

        //搜索页面总布局焦点切换监听设置
        mSearchMainLayout.setOnFocusSearchListener(new OnSearchFocusSearchListener());
        mSearchMainLayout.setOnChildFocusListener(new OnSearchChildFocusListener());
    }

    private boolean isBorder(ViewGroup root, View focused, int direction) {
        return FocusFinder.getInstance().findNextFocus(root, focused, direction) == null;
    }

    private void querySuggestLists(String key) {
        mSearchPresenter.suggestKeyWords(key, this);
    }

    //键盘回调(全键盘/T9键盘/数字键盘 任意字符按下时触发)
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
     * 删除或者增加输入框字符
     *
     * @param isAdd
     * @param key
     */
    private void changeEditText(boolean isAdd, String key) {
        if (null == mEditText) {
            return;
        }
        Editable text = mEditText.getText();
        if (isAdd) {//输入
            if (text.length() >= Max_Search_Length) {
                EpgToast.showLongToast(this, "输入的字符数已达到上限。");
                return;
            }
            if (text.length() > 0) {
                String newText = text.toString() + key;
                mEditText.setText(newText);
                //设置光标位置
                mEditText.setSelection(newText.length());
            } else {
                mEditText.setText(key);
                //设置光标位置
                mEditText.setSelection(key.length());
            }
        } else {//删除
            if (null != text && text.length() > 0) {
                String subText = text.toString().substring(0, text.length() - 1);
                mEditText.setText(subText);
                //设置光标位置
                mEditText.setSelection(subText.length());
            }
        }
    }

    //搜索热词回调
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

    //搜索排行榜回调
    @Override
    public void queryHotContentListSuccess(List<Content> contents) { }

    @Override
    public void queryHotContentListFailed() { }

    //查询联想词[你是不是在找]网络请求响应成功回调
    @Override
    public void suggestKeywordSuccess(List<String> suggests) {
        if ( null != suggests && suggests.size()>0 ) {
            mSearchSuggestAdapter.setmData(suggests);
            mSuggestKeywordRecycleView.scrollToPosition(0);
            showSuggestKeyword(true);
        } else {
            //不存在联想词,不展示联想词
            showSuggestKeyword(false);
        }
    }

    //查询联想词[你是不是在找]网络请求响应失败回调
    @Override
    public void suggestKeywordFail() {
        //联想词查找失败,不展示联想词
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

    //点击[你是不是在找](搜索关键词联想)中任意项时回调此方法
    @Override
    public void onAssociationItemClick(String key) {
        mSearchSubjectAdapter.setFocusSubject(0);//第一个栏目获取焦点
        mIsRequestSuggestKey = false;//点击[你是不是在找]不需要请求关键字联想
        mEditText.setText(key);
        showSuggestKeyword(false);//点击任意项后隐藏联想词显示区域
    }

    //点击[大家都在搜]中任意项时回调此方法
    @Override
    public void onEveryoneSearchItemClick(String key) {
        mSearchSubjectAdapter.setFocusSubject(0);//第一个栏目获取焦点
        mIsRequestSuggestKey = false;//点击[大家都在搜]不需要请求关键字联想
        mEditText.setText(key);
    }

    //点击[影人](普通版/简版)/[动画大咖](儿童版)中任意项时回调此方法
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
                mSearchSubjectAdapter.setFocusSubject(0);//第一个栏目获取焦点
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

    //点击搜索
    private void gotoSearch(String key) {
        //UBD记录搜索行为
        UBDService.recordSearchKey(key);
        mSearchKey = key;
        ChineseUtil.setSearchKey(mSearchKey);
        if (StringUtils.containSymbol(key)) {
            EpgToast.showLongToast(SearchActivity.this, "包含特殊字符");
        }
        //搜索时展示结果布局
        mSearchDefaultLayout.setVisibility(View.GONE);
        mSearchResultLayout.setVisibility(View.VISIBLE);
        isLoading=true;
        newKey=key;

        if("咪咕爱看".equals(mSubjectBeans.get(0).getSubjectName())){
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
            mResultViewPager.setCurrentItem(0);       //每次搜索后展示第一个栏目下的内容
            EventBus.getDefault().post(new SearchKeyEvent(key));
        }

        mSearchSubjectRecycle.scrollToPosition(0);    //完成新的搜索后跳到第一个栏目
        mSearchSubjectAdapter.setSelectSubject(0);    //让第一个栏目获取选中效果(Adapter中onBindViewHolder方法)
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

    //返回搜索首页(没有搜索结果，有影人/热词/推荐视频)
    private void gotoSearchHome(){
        mSearchTip.setVisibility(View.VISIBLE);
        if (mSearchDefaultLayout.getVisibility() != View.VISIBLE) {
            mSearchDefaultLayout.setVisibility(View.VISIBLE);
        }
        if (mSearchResultLayout.getVisibility() != View.GONE) {
            mSearchResultLayout.setVisibility(View.GONE);
        }
        //默认位置落焦(字母M)
        mKeyBoardView.requestFirstFocus();
    }

    private class SearchTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            //触发变化时 默认消失之前结果布局 展示默认布局
            selectPosition = 0;
            mSearchSuggestAdapter.resetCurrentPosition();
            if (!TextUtils.isEmpty(s)){
                mSearchKey = s.toString();
            }
            if (null != s && s.length() > 0) {
                //有搜索关键字
                if (mSearchDefaultLayout.getVisibility() != View.GONE) {
                    mSearchDefaultLayout.setVisibility(View.GONE);
                }
                if (mSearchResultLayout.getVisibility() != View.VISIBLE) {
                    mSearchResultLayout.setVisibility(View.VISIBLE);
                }
                gotoSearch(s.toString());
                //DEBUG版本测试
//                if(BuildConfig.DEBUG){
//                    HecaiCloudService.login(new HecaiCloudService.OnResponse() {
//                        @Override
//                        public void loginCallback(String result) {
//                            SuperLog.info2SD(TAG,"Get Hecaiyun login response successfully,result="+result);
//                        }
//                    });
//                }
            } else {
                //无搜索关键字
                gotoSearchHome();
            }

            //检查关键字联想功能是否开启
            if (SearchSuggestAdapter.isKeySuggestOpen()) {
                if ( null != s && s.length() > 0 ) {
                    if (mIsRequestSuggestKey) {
                        if (null != mSuggestKeywordRunnable){
                            mHandler.removeCallbacks(mSuggestKeywordRunnable);
                        }
                        mHandler.postDelayed(mSuggestKeywordRunnable, 500);
                    } else {
                        //点击[大家都在搜]不触发关键词联想
                        mIsRequestSuggestKey = true;
                    }
                } else {
                    //不存在搜索内容,不展示联想词
                    if (null != mSuggestKeywordRunnable){
                        //用户快速删除关键字时,防止出现联想词
                        mHandler.removeCallbacks(mSuggestKeywordRunnable);
                    }
                    showSuggestKeyword(false);
                }
            }
        }
    }

    private class OnSearchFocusSearchListener implements BrowseFrameLayout.OnFocusSearchListener {
        /**
         * 在两个ChildView之间发生焦点移动时回调此方法,在一个ChildView内部发生焦点移动不回调此方法
         *
         * @param focused    当前落焦View
         * @param direction  焦点即将移动的方向
         *
         * @return 下一个获得焦点的View
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
                            //从选择键盘按键(全键盘/T9键盘/123按下时,如果没有联想词则焦点不动)
                            return focused;
                        }
                    } else if (focused.getId() == R.id.keyboard_delete_btn &&isLoading){
                        //防止加载时候焦点消失
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
                        //【搜索栏目标题焦点记忆】从哪个栏目标题移到结果布局,则从结果布局返回时就落到哪个栏目标题
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

                    if ( focused.getId() == R.id.keyboard_delete_btn      //焦点在退格按键
                        || focused.getId() == R.id.switch_number_keyboard //焦点在切换数字键盘(123)按键
                        || focused.getId() == R.id.item_t9_layout         //焦点在T9键盘按键
                        || focused.getId() == R.id.keyboard_item_text )   //焦点在字母或数字键盘按键
                    {
                        //当[大家都在搜]可见时 大家都在搜的第一排获得焦点
                        if(mSearchDefaultLayout.getVisibility() == View.VISIBLE
                            && mEveryoneSearchAdapter != null && mEveryoneSearchAdapter.getItemCount() > 0){
                            return mEveryoneSearch.getChildAt(0);
                        }

                        //右侧无搜索结果，栏目标题"全部"获取焦点
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

            //防止加载时候焦点消失
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

                //ZJYDEPGAPP-364 在搜索界面，输入搜索内容"a"，移动焦点到右侧内容列表界面，焦点落在非最右侧海报上时，再点击右键，出现无响应的情况
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
         * 在【一个ChildView内部】/【两个ChildView之间】焦点移动时,回调此方法
         *
         * @param child    尚不清楚
         * @param focused  发生焦点移动时,即将获得焦点的View
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
        //加载【热搜栏目的图片(展示在搜索键盘底部)】
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
        //加载【热搜视频】
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

        //DEBUG版本测试
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

        //展示引导页时屏蔽所有按键,按返回键可以返回首页
        if ( mSearchFirstGuide.getVisibility() == View.VISIBLE && keyCode != KeyEvent.KEYCODE_BACK ){
            return true;
        }

        //凡在搜索结果页点击返回键都是回到搜索首页,焦点在键盘的M上
        if ( mSearchResultLayout.getVisibility() == View.VISIBLE && keyCode == KeyEvent.KEYCODE_BACK){
            if( getCurrentFocus() !=null && getCurrentFocus().getId() == R.id.search_subject_name_text){
                //焦点落在栏目上时按返回键回到首页默认落焦导航栏,此为新需求
                return super.onKeyDown(keyCode, event);
            }

            //如果当前键盘布局不是全键盘,需要先切换到全键盘
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
                //如果按数字键时焦点不在键盘上则让搜索栏目第一个栏目标题落焦,否则焦点位置不动
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

    //设置关键词联想标题
    private void setSuggestTitle(){
        String title = CommonUtil.getConfigValue(SEARCH_SUGGEST_TITLE);
        if(!TextUtils.isEmpty(title)){
            mSuggestTitle.setText(title);
        }
    }
}