package com.pukka.ydepg.moudule.vod.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.MyHorizontalScrollView;
import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.SortFilterBean;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODFilter;
import com.pukka.ydepg.common.http.v6bean.v6node.YearFilterBean;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.search.SearchActivity;
import com.pukka.ydepg.moudule.vod.adapter.FilterAreaAdapter;
import com.pukka.ydepg.moudule.vod.adapter.FilterCatagoryAdapter;
import com.pukka.ydepg.moudule.vod.adapter.FilterSortAdapter;
import com.pukka.ydepg.moudule.vod.adapter.FilterYearAdapter;
import com.pukka.ydepg.moudule.vod.adapter.MoviesListCatagoryAdapter;
import com.pukka.ydepg.moudule.vod.adapter.MoviesListContentAdapter;
import com.pukka.ydepg.moudule.vod.presenter.MoviesListPresenter;
import com.pukka.ydepg.moudule.vod.view.FocusVerticalGridView;
import com.pukka.ydepg.moudule.vod.view.MoviesListDataView;

import java.util.ArrayList;
import java.util.List;

/**
 * ???????????????
 *
 * @FileName: VodMainActivity.java
 * @author: ld
 * @date: 2017-12-19
 */

public class VodMainActivity extends BaseActivity implements MoviesListDataView, FocusInterceptor {

    private static final String TAG = "VodMainActivity";

    public static final String CATEGORY_ID = "categoryId";

    public static final String FOCUS_CATEGORY_ID = "focusCategoryId";

    public static final String IS_CHILD_FILTER_MODE="is_child_filter_mode";

    //???????????????????????????
    TextView mTvCategoryName;
    //????????????
    TextView mTvCurrentNum;
    //?????????
    TextView mTvTotalNum;
    //????????????
    ImageView filterImg;
    //????????????tv
    TextView mTvFilterConditions;
    //????????????????????????????????????????????????
    LinearLayout mLinlayoutFilter;
    //vodlist??????????????????
    LinearLayout mNoContentLayout;
    //??????????????????
    LinearLayout mCategoryNoContent;
    //???????????????
    RelativeLayout mImgSearchRelayout;
    //????????????
    VerticalGridView mCatagoryList;
    //????????????????????????
    View moviesListLine;
    //vod??????
    FocusVerticalGridView mContentList;
    //???????????????
    MyHorizontalScrollView mHorizontalScrollView;
    //?????????
    ProgressBar moviesListProgress;
    //????????????????????????
    BrowseFrameLayout mBrowserFrameLayout;

    //?????????
    RelativeLayout progressrel;
    //    //??????????????????
//    private ProgressBarManager mProgressBarManager;
    //????????????
    private VerticalGridView mFilterSortList;
    //??????????????????
    private VerticalGridView mFilterCatagoryList;
    //??????????????????
    private VerticalGridView mFilterAreaList;
    //??????????????????
    private VerticalGridView mFilterYearList;
    //??????popupwindow
    private PopupWindow mFilterPopupWindow;
    //vod???????????????
    private MoviesListContentAdapter mContentAdapter;
    //?????????????????????
    private MoviesListPresenter mMoviesListPresenter;
    //????????????item
    private View mSortSelectedView;
    //????????????item
    private View mGenreIDSelectedView;
    //????????????item
    private View mYearSelectedView;
    //????????????item
    private View mAreaSelectedView;
    //??????list??????
    private List<SortFilterBean> mSortFilterBeanList;
    //??????list??????
    private List<Genre> mGenreList;
    //??????list??????
    private List<ProduceZone> mProduceZoneList;
    //??????list??????
    private List<YearFilterBean> mYearFilterBeanList;
    //???????????????
    private FilterAreaAdapter mFilterAreaAdapter;
    //????????????
    private List<Subject> mSubjects;
    //??????????????????????????????id
    private String baseCategoryID;
    //??????????????????????????????id
    private String focusCategoryID;
    //??????HorizontalScrollView???????????????????????????true?????????????????????????????????false????????????????????????????????????
    private boolean needBack = true;
    //??????????????????????????????subject??????
    private Subject mBaseSubject;
    //?????????????????????
    private int loadPageNum = 0;
    //vod??????
    private int totalNumber;
    //???????????????????????????
    private long mLastKeyDownTime = 0;
    //??????vod????????????????????????
    private VODFilter mVODFilter;
    //??????vod????????????????????????
    private String mSortType;
    //??????????????????????????????true,vod??????????????????????????????false???vod??????????????????
    private boolean isDoFilter = false;

    //???????????????????????????
    private boolean isCanTouch = true;
    //????????????
    private TextView filterName;

    //????????????????????????????????????
    private List<Subject>  baseSubjects;

    //???????????????????????????
    private boolean  isChildFilterMode;

    //????????????????????????
    private String baseSubjectsName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        mMoviesListPresenter = new MoviesListPresenter(this);
        mMoviesListPresenter.setMoviesListDataView(this);
        initViews();
        baseCategoryID = getIntent().getStringExtra(CATEGORY_ID);
        focusCategoryID = getIntent().getStringExtra(FOCUS_CATEGORY_ID);
        SuperLog.debug(TAG, " onCreate|categoryID:" + baseCategoryID + ",focusCategoryID:" + focusCategoryID);
        isChildFilterMode=getIntent().getBooleanExtra(IS_CHILD_FILTER_MODE,false);
        if(isChildFilterMode){
            baseSubjects= SessionService.getInstance().getSession().getTerminalConfigurationSettingFilterSubjects();
            baseSubjectsName=SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.CHILD_FILTER_BASE_CATEGORY_NAME);
            if(null!=baseSubjects&&baseSubjects.size()>0){
                showCatagorys(baseSubjects);
            }

            if(!TextUtils.isEmpty(baseSubjectsName)){
                mTvCategoryName.setText(baseSubjectsName);
            }
        }else{
            if (baseCategoryID != null) {
                mMoviesListPresenter.queryCatagoryDetail(baseCategoryID);
            }
        }
        mMoviesListPresenter.loadFilterContent();
        mContentList.setInterceptor(this);
    }

    private void initViews() {
        mTvCategoryName = (TextView) findViewById(R.id.root_catagory_string);
        mTvCurrentNum = (TextView) findViewById(R.id.page_num);
        mTvTotalNum = (TextView) findViewById(R.id.page_total);
        mTvFilterConditions = (TextView) findViewById(R.id.filter_conditions);
        filterName=(TextView)findViewById(R.id.filter_name);
        filterName.setFocusable(true);
        mLinlayoutFilter = (LinearLayout) findViewById(R.id.linlayout_filter);
        mNoContentLayout = (LinearLayout) findViewById(R.id.movies_list_no_content);
        mCategoryNoContent = (LinearLayout) findViewById(R.id.movies_category_no_content);
        mImgSearchRelayout = (RelativeLayout) findViewById(R.id.relayout_search);
        mCatagoryList = (VerticalGridView) findViewById(R.id.first_catagory_list);
        moviesListLine = findViewById(R.id.movies_list_line);
        mContentList = (FocusVerticalGridView) findViewById(R.id.movies_list);
        mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.horizontal_scroll_view);
        moviesListProgress = (ProgressBar) findViewById(R.id.movies_list_progress);
        mBrowserFrameLayout = (BrowseFrameLayout) findViewById(R.id.movies_list_root_view);
        progressrel = (RelativeLayout) findViewById(R.id.progressrel);
        filterImg= (ImageView) findViewById(R.id.filter_img);
        mImgSearchRelayout.setNextFocusRightId(R.id.relayout_search);
        mImgSearchRelayout.setNextFocusLeftId(R.id.relayout_search);
        mImgSearchRelayout.setFocusable(false);
        mTvCategoryName.setFocusable(true);
        mTvCategoryName.requestFocus();
        mCatagoryList.setFocusable(false);
//        initProgressBar();
        initContentList();
        initFilterPopupWindow();
        initListener();
    }

    private void initListener() {
        mImgSearchRelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????????????????
                VodMainActivity.this.startActivity(new Intent(VodMainActivity.this, SearchActivity.class));
            }
        });
        filterName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus){
                    filterImg.setImageDrawable(getResources().getDrawable(R.drawable.filter_click));
                    filterName.setTextColor(getResources().getColor(R.color.c21_color));
                    filterName.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelOffset(R.dimen.filter_name_size_big));
                }else{
                    filterImg.setImageDrawable(getResources().getDrawable(R.drawable.filter_normal));
                    filterName.setTextColor(getResources().getColor(R.color.c23_color));
                    filterName.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelOffset(R.dimen.filter_name_size_small));
                }
            }
        });
        filterName.setNextFocusLeftId(R.id.filter_name);
        filterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mFilterPopupWindow.isShowing()) {
                    mFilterPopupWindow.dismiss();
                }
                mFilterPopupWindow.showAtLocation(mBrowserFrameLayout, Gravity.CENTER, 0, 0);
            }
        });

        mBrowserFrameLayout.setOnFocusSearchListener(new BrowseFrameLayout.OnFocusSearchListener() {
            @Override
            public View onFocusSearch(View focused, int direction) {
                //??????????????????
                if (direction == View.FOCUS_RIGHT) {
                    //????????????????????????????????????????????????
                    if (focused != null && focused.getId() == R.id.relayout_search) {
                        return focused;
                    }
                    //????????????????????????????????????
                    if (focused != null && ((ViewGroup) focused.getParent()).getId() == R.id.first_catagory_list) {
                        if (!mHorizontalScrollView.isFinished()) {
                            return focused;
                        }
                        MoviesListCatagoryAdapter adapter = (MoviesListCatagoryAdapter) mCatagoryList.getAdapter();
                        String currentContentId = adapter.getCurrentContentId();
                        Subject subject = (Subject) focused.getTag();

                        if (!TextUtils.isEmpty(currentContentId)) {
                            if (!currentContentId.equals(subject.getID())) {
                                return focused;
                            }
                        }
                        //vod????????????????????????????????????
                        if (mContentList.getChildCount() == 0) {
                            return focused;
                        }
                        if (needBack) {
                            showOrHideCatagory(false);
                        }
                        if (mContentList != null && mContentList.getChildCount() != 0) {
                            if (mMoviesListPresenter.getOldContentFocusView() != null) {
                                return mMoviesListPresenter.getOldContentFocusView();
                            }
                            return mContentList.getChildAt(0);
                        }
                    }
                    //??????????????????
                } else if (direction == View.FOCUS_LEFT) {
                    //?????????????????????vod?????????
                    if (focused != null && ((ViewGroup) focused.getParent()).getId() == R.id.movies_list) {
                        if (!mHorizontalScrollView.isFinished()) {
                            return focused;
                        }
                        if (mCatagoryList.getChildCount() == 0) {
                            return focused;
                        }
                        SuperLog.debug(TAG, " FOCUS_LEFT  needBack:" + needBack);
                        if (!needBack&&isShowCatagoryList()) {
                            showOrHideCatagory(true);
                        }
                        return mCatagoryList;
                    }
                }
                return null;
            }
        });

        mContentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isFinishing()) {
                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                        //????????????????????????????????????pauseRequests()???????????????????????????????????????resumeRequests()????????????
                        if (!Glide.with(VodMainActivity.this).isPaused()) {
                            Glide.with(VodMainActivity.this).pauseRequests();
                        }
                        return;
                    }
                    Glide.with(VodMainActivity.this).resumeRequests();

                    //????????????
                    View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                    if (null != lastChildView) {
                        int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                        if ((totalNumber - loadPageNum * 24 > 0) && (loadPageNum * 24 - lastPosition - 1 <= 12)) {
                            SuperLog.debug(TAG, "onScrollStateChanged->loadMoviesContent" + loadPageNum);
                            if (mVODFilter == null && mSortType == null) {
                                mMoviesListPresenter.loadMoviesContent(loadPageNum * 24 + "");
                            } else {
                                mMoviesListPresenter.loadVODListBySubject(loadPageNum * 24 + "", "24", mVODFilter, mSortType);
                            }
                            loadPageNum++;
                        }
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    //??????????????????
    private View.OnKeyListener mOnkeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            SuperLog.debug("TAG", "popup windows onKeyDown--->" + keyCode);
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                    if (mFilterPopupWindow.isShowing()) {
                        mFilterPopupWindow.dismiss();
                    }
                    return false;
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if (mFilterPopupWindow.isShowing()) {
                        mFilterPopupWindow.dismiss();
                    }
                    return false;
                }
            }
            return false;
        }
    };

    private void initFilterPopupWindow() {
        View filterView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_list, null);
        mFilterPopupWindow = new PopupWindow(filterView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mFilterPopupWindow.setTouchable(true);
        mFilterPopupWindow.setOutsideTouchable(true);
        mFilterPopupWindow.setFocusable(true);
        mFilterPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDismiss() {
                SuperLog.debug(TAG, " mFilterPopupWindow onDismiss");
                //??????????????????????????????????????????????????????????????????????????????????????????
                if (mContentList.getChildCount() != 0) {
                    SuperLog.debug(TAG, "mFilterPopupWindow onDismiss->mContentList size not 0" + "isDoFilter:" + isDoFilter);
                    if (isDoFilter) {
                        mContentList.getChildAt(0).requestFocus();
                    }
                    isDoFilter = false;
                } else {
                    //??????????????????????????????????????????????????????
                    if (!needBack&&isShowCatagoryList()) {
                        showOrHideCatagory(true);
                    }
                }

            }
        });
        filterView.setFocusableInTouchMode(true);
        BrowseFrameLayout browseFrameLayout = (BrowseFrameLayout) filterView.findViewById(R.id.popupwindow_root_view);
        browseFrameLayout.setFocusable(false);
        browseFrameLayout.setOnFocusSearchListener(new BrowseFrameLayout.OnFocusSearchListener() {
            @Override
            public View onFocusSearch(View focused, int direction) {
                //????????????
                if (direction == View.FOCUS_RIGHT) {
                    //????????????????????????????????????????????????????????????
                    if (focused != null && ((ViewGroup) focused.getParent()).getId() == R.id.filter_sort_list) {
                        return mFilterCatagoryList;
                    }
                    //?????????????????????????????????????????????????????????????????????
                    if (focused != null && ((ViewGroup) focused.getParent()).getId() == R.id.filter_catagory_list) {
                        return mFilterAreaList;
                    }
                    //?????????????????????????????????????????????????????????
                    if (focused != null && ((ViewGroup) focused.getParent()).getId() == R.id.filter_area_list) {
                        return mFilterYearList;
                    }
                }
                //????????????
                if (direction == View.FOCUS_LEFT) {
                    //???????????????????????????????????????????????????????????????
                    if (focused != null && ((ViewGroup) focused.getParent()).getId() == R.id.filter_catagory_list) {
                        return mFilterSortList;
                    }
                    //???????????????????????????????????????????????????????????????
                    if (focused != null && ((ViewGroup) focused.getParent()).getId() == R.id.filter_area_list) {
                        return mFilterCatagoryList;
                    }
                    //???????????????????????????????????????????????????????????????
                    if (focused != null && ((ViewGroup) focused.getParent()).getId() == R.id.filter_year_list) {
                        return mFilterAreaList;
                    }
                }
                return null;
            }
        });
        mFilterSortList = (VerticalGridView) mFilterPopupWindow.getContentView().findViewById(R.id.filter_sort_list);
        mFilterCatagoryList = (VerticalGridView) mFilterPopupWindow.getContentView().findViewById(R.id.filter_catagory_list);
        mFilterAreaList = (VerticalGridView) mFilterPopupWindow.getContentView().findViewById(R.id.filter_area_list);
        mFilterYearList = (VerticalGridView) mFilterPopupWindow.getContentView().findViewById(R.id.filter_year_list);
    }

    @SuppressLint("RestrictedApi")
    private void initContentList() {
        mContentList.setFocusable(false);
        mContentList.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ITEM);
        mContentList.setNumColumns(6);
        mContentAdapter = new MoviesListContentAdapter(new ArrayList<VOD>(), mContentList, mTvCurrentNum, mMoviesListPresenter);
        mContentList.setAdapter(mContentAdapter);
    }

//    private void initProgressBar() {
//        mProgressBarManager = new ProgressBarManager();
//        mProgressBarManager.setProgressBarView(moviesListProgress);
//        mProgressBarManager.setInitialDelay(100);
//        mProgressBarManager.hide();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this != null && !this.isFinishing()) {
            mContentList.setAdapter(null);
        }
        SuperLog.debug(TAG,"onDestroy");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean onKeyDown = false;
        if (!isCanTouch && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            SuperLog.debug(TAG, "loading now");
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            View currentFocus = getCurrentFocus();
            if(!isShowCatagoryList()&&currentFocus!=filterName){
                if (mFilterPopupWindow.isShowing()) {
                    mFilterPopupWindow.dismiss();
                }
                mFilterPopupWindow.showAtLocation(mBrowserFrameLayout, Gravity.CENTER, 0, 0);
                return true;
            }
            if (currentFocus != null) {
                if (mContentList != null && mContentList.getChildCount() != 0) {
                    if (mContentList.getFocusedChild() == currentFocus) {
                        if (mFilterPopupWindow.isShowing()) {
                            mFilterPopupWindow.dismiss();
                        }
                        mFilterPopupWindow.showAtLocation(mBrowserFrameLayout, Gravity.CENTER, 0, 0);
                        onKeyDown = true;
                    }
                }
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBaseSubject != null) {
                if (!mBaseSubject.getHasChildren().equals("1")) {
                    needBack = true;
                }
                View focusView = getCurrentFocus();
                if (focusView == mImgSearchRelayout ||
                        (mCatagoryList != null && getCurrentFocus() == mCatagoryList.getFocusedChild())) {//?????????search??????????????????
                    focusCategoryID = mBaseSubject.getID();
                    if (isNeedBack()) {
                        return super.onKeyDown(keyCode, event);
                    }
                    mImgSearchRelayout.setFocusable(false);
                    onKeyDown = loadCatagoryDetail();
                } else if (mContentList != null && getCurrentFocus() == mContentList.getFocusedChild()) {//??????????????????
                    if (!needBack&&isShowCatagoryList()) {
                        showOrHideCatagory(true);
                        onKeyDown = true;
                    }
                }
            }
        }
        //???????????????????????????
        if (null != mContentList && mContentList.findFocus() != null && (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
            long current = System.currentTimeMillis();
            if (current - mLastKeyDownTime < 150) {
                //SuperLog.error(TAG, "keyCode isLoadMore or < 300 return true");
                return true;
            }
            mLastKeyDownTime = current;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            View currrentFocus = getCurrentFocus();
            if (currrentFocus != null && currrentFocus == mCatagoryList.getChildAt(0)) {
                mImgSearchRelayout.requestFocus();
            }
        }
        if (!onKeyDown) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public boolean isNeedBack(){
        if(isChildFilterMode){
            boolean needBack=true;
            if(null!=baseSubjects&&baseSubjects.size()>0&&null!=mBaseSubject){
                for(int i=0;i<baseSubjects.size();i++){
                    if(baseSubjects.get(i).getID().equals(mBaseSubject.getID())){
                        needBack=false;
                        break;
                    }
                }
            }
            return needBack;
        }else{
            return (!TextUtils.isEmpty(baseCategoryID))&&null!=mBaseSubject&&baseCategoryID.equals(mBaseSubject.getID());

        }
    }



    //??????????????????????????????
    private boolean loadCatagoryDetail() {
        boolean needLoad = false;
        if (mBaseSubject != null) {
                //????????????catagory
                if (isNeedBack()) {
                    needLoad = false;
                } else {
                    //??????????????????
                    mContentAdapter.clean();
                    String catagoryId = mBaseSubject.getParentSubjectID();
                    boolean isLoadChild=false;
                    if(null!=baseSubjects&&baseSubjects.size()>0){
                        for(int i=0;i<baseSubjects.size();i++){
                            if(baseSubjects.get(i).getID().equals(mBaseSubject.getID())){
                                isLoadChild=true;
                                break;
                            }
                        }
                    }
                    if(isChildFilterMode&&isLoadChild){
                        showCatagorys(baseSubjects);
                        if(!TextUtils.isEmpty(baseSubjectsName)){
                            mTvCategoryName.setText(baseSubjectsName);
                        }
                        mBaseSubject=null;
                    }else{
                        mMoviesListPresenter.queryCatagoryDetail(catagoryId);
                    }
                    needLoad = true;
                }
        }
        SuperLog.debug("TAG", "needLoad--->" + needLoad + ",baseCategoryID:" + baseCategoryID + "," + mBaseSubject);
        return needLoad;
    }

    //?????????????????????????????????
    public boolean isShowCatagoryList(){
        if(null==mBaseSubject||TextUtils.isEmpty(baseCategoryID)){
            return true;
        }
        if(baseCategoryID.equals(mBaseSubject.getID())&&!mBaseSubject.getHasChildren().equals("1")){
            return false;
        }
        return true;
    }


    //??????????????????????????????
    @SuppressLint("RestrictedApi")
    private void showOrHideCatagory(boolean needShow) {
        if (!mHorizontalScrollView.isFinished()) {
            return;
        }
        needBack = needShow;
        int x = 0;
        if (needShow) {
            x = -getResources().getDimensionPixelOffset(R.dimen.scroll_x);
            if (mTvTotalNum != null) {
                // ?????????????????????
                mTvTotalNum.setVisibility(View.INVISIBLE);
                mTvCurrentNum.setVisibility(View.INVISIBLE);
                mLinlayoutFilter.setVisibility(View.INVISIBLE);
                if (mCatagoryList != null && mCatagoryList.getChildCount() != 0) {//????????????
                    mCatagoryList.requestFocus();
                }
                if (mBaseSubject != null) {
                    mTvCategoryName.setText(mBaseSubject.getName());
                }
            }
            if (mContentList != null && mContentList.getChildCount() == 0) {
                mNoContentLayout.setVisibility(View.INVISIBLE);

                mCategoryNoContent.setVisibility(View.VISIBLE);
            }
        } else {
            x = getResources().getDimensionPixelOffset(R.dimen.scroll_x);
            if (mTvTotalNum != null) {
                //?????????????????????
                mTvTotalNum.setVisibility(View.VISIBLE);
                mTvCurrentNum.setVisibility(View.VISIBLE);
                mLinlayoutFilter.setVisibility(View.VISIBLE);

                if (mContentList != null && mContentList.getChildCount() != 0) {//????????????
                    mContentList.requestFocus();
                }
            }
            if (mSubjects != null && (mCatagoryList != null && mCatagoryList.getChildCount() != 0)) {
                mTvCategoryName.setText(mSubjects.get(mCatagoryList.getSelectedPosition()).getName());
            }
            mNoContentLayout.clearAnimation();
            if (mContentList != null && mContentList.getChildCount() != 0) {
                mNoContentLayout.setVisibility(View.INVISIBLE);
                mCategoryNoContent.setVisibility(View.INVISIBLE);
            }
        }
        mHorizontalScrollView.scrollToX(x, 0);//????????????
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showLoading() {
        isCanTouch = false;
//        progressrel.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        isCanTouch = true;
//        if(progressrel.getVisibility()==View.VISIBLE)
//        progressrel.setVisibility(View.GONE);
    }


    @Override
    public void showNoContent() {
        mTvCurrentNum.setVisibility(View.INVISIBLE);
        mTvTotalNum.setVisibility(View.INVISIBLE);
        mContentAdapter.clean();
        if (mNoContentLayout.getVisibility() != View.VISIBLE) {
            mNoContentLayout.setVisibility(View.VISIBLE);
            if (needBack) {
                mNoContentLayout.setVisibility(View.INVISIBLE);
                mCategoryNoContent.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showError(String message) {
    }

    //??????????????????
    @Override
    public void showCatagorys(List<Subject> subjects) {
        SuperLog.info(TAG, "showCatagorys:" + subjects.toString());
        mSubjects = subjects;
        String focusID = null;
        if (mBaseSubject != null && focusCategoryID != null) {
            focusID = focusCategoryID;
        }
        mCatagoryList.setAdapter(null);
        int focusPosition = -1;
        for (int i = 0; i < subjects.size(); i++) {
            String subjectId = subjects.get(i).getID();
            if (!TextUtils.isEmpty(subjectId)) {
                if (subjects.get(i).getID().equals(focusID)) {
                    focusPosition = i;
                }
            }
        }
        MoviesListCatagoryAdapter adapter = new MoviesListCatagoryAdapter(subjects, mMoviesListPresenter, focusID, mTvCategoryName, mCatagoryList,this);
        mCatagoryList.setAdapter(adapter);
        if (focusPosition != -1) {
            mCatagoryList.scrollToPosition(focusPosition);
        }
        adapter.setmMoviewListOnItemClickListener(new MoviesListCatagoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Subject subject) {
                if (subject.getHasChildren().equals("1")) {
                    SuperLog.info(TAG, "???????????????????????????????????????");
                    mImgSearchRelayout.setFocusable(false);
                    if (mContentList != null && mContentList.getChildCount() != 0) {
                        mContentAdapter.clean();
                    }
                    mBaseSubject = subject;
                    mTvCategoryName.setText(subject.getName());
                    if (mBaseSubject != null) {
                        mMoviesListPresenter.queryCatagoryDetail(mBaseSubject.getID());
                    }
                } else {
                    if (mContentList != null && mContentList.getChildCount() != 0) {
                        showOrHideCatagory(false);
                        mContentList.getChildAt(0).requestFocus();
                    }
                }
            }
        });
    }

    //??????vod??????
    @SuppressLint("RestrictedApi")
    @Override
    public void showMoviesContent(List<VOD> vodList,String subjectId) {
        boolean needclean = true;
        if (loadPageNum > 1) {
            needclean = false;
        } else {
            needclean = true;
        }
        mNoContentLayout.setVisibility(View.INVISIBLE);
        mCategoryNoContent.setVisibility(View.INVISIBLE);
        if (needclean) {
            if (mCatagoryList.getChildCount() == 0) {
                mContentList.setSelectedPosition(0);
            }
            if (isDoFilter) {
                mTvCurrentNum.setText("1");
            }
        }
        showPageNum();
        if (mNoContentLayout.getVisibility() == View.VISIBLE) {
            mNoContentLayout.setVisibility(View.INVISIBLE);
        }
        mContentAdapter.addData(vodList, needclean,subjectId);
        if(needclean&&mCatagoryList.getChildCount() == 0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mContentList != null&&mContentList.getChildCount() != 0) {
                            mContentList.getChildAt(0).requestFocus();
                        }
                    }
                },100);

        }


    }

    //??????????????????
    @Override
    public void showFilterContent(List<SortFilterBean> sortFilterBeanList, List<Genre> genres, List<ProduceZone> produceZones, List<YearFilterBean> yearList) {
        mSortFilterBeanList = sortFilterBeanList;
        FilterSortAdapter filterSortAdapter = new FilterSortAdapter(sortFilterBeanList);
        filterSortAdapter.setOnItemClickListener(mSortOnClickListener);
        filterSortAdapter.setOnKeyListener(mOnkeyListener);
        mFilterSortList.setAdapter(filterSortAdapter);

        mGenreList = genres;
        FilterCatagoryAdapter filterCatagoryAdapter = new FilterCatagoryAdapter(genres);
        filterCatagoryAdapter.setmOnItemClickListener(mGenreOnClickListener);
        filterCatagoryAdapter.setOnKeyListener(mOnkeyListener);
        mFilterCatagoryList.setAdapter(filterCatagoryAdapter);

        mProduceZoneList = produceZones;
        mFilterAreaList = (VerticalGridView) mFilterPopupWindow.getContentView().findViewById(R.id.filter_area_list);
        mFilterAreaAdapter = new FilterAreaAdapter(produceZones);
        mFilterAreaAdapter.setOnItemClickListener(mAreaOnClickListener);
        mFilterAreaAdapter.setOnKeyListener(mOnkeyListener);
        mFilterAreaList.setAdapter(mFilterAreaAdapter);

        mYearFilterBeanList = yearList;
        mFilterYearList = (VerticalGridView) mFilterPopupWindow.getContentView().findViewById(R.id.filter_year_list);
        FilterYearAdapter filterYearAdapter = new FilterYearAdapter(yearList);
        filterYearAdapter.setmOnItemClickListener(mYearOnClickListener);
        filterYearAdapter.setOnKeyListener(mOnkeyListener);
        mFilterYearList.setAdapter(filterYearAdapter);
    }

    @Override
    public void showTogalPage(int totalPage) {
        totalNumber = totalPage;
        if (totalNumber == 0) {
            mTvCurrentNum.setText("0");
        }
        mTvTotalNum.setText("/" + totalPage);
    }

    @Override
    public void hideCategory() {
        if (needBack) {
            showOrHideCatagory(false);
        }
    }

    @Override
    public void showCategoryName(Subject subject) {
        mBaseSubject = subject;
        mTvCategoryName.setText(subject.getName());
    }

    @Override
    public void hideFilter() {
        if (mTvFilterConditions == null) {
            return;
        }
        mTvFilterConditions.setText("");
        if (mTvFilterConditions.getVisibility() == View.VISIBLE) {
            mTvFilterConditions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SuperLog.debug(TAG,"onStop");
    }

    //?????????????????????
    @Override
    public void resetLoadNum() {
        loadPageNum = 1;
    }

    @Override
    public int getLoadNum() {
        return loadPageNum;
    }

    @Override
    public void setLoadNum() {
        loadPageNum = -1;
    }

    @Override
    public void showPageNum() {
        //needBack???false,???????????????
        if (!needBack) {
            if (mTvTotalNum.getVisibility() != View.VISIBLE) {
                mTvTotalNum.setVisibility(View.VISIBLE);
            }
            if (mTvCurrentNum.getVisibility() != View.VISIBLE) {
                mTvCurrentNum.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setSearchFocus() {
        mTvCategoryName.setFocusable(false);
        mImgSearchRelayout.setFocusable(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void resetContentStatus() {
        if (mContentList != null && mContentList.getChildCount() != 0) {
            mContentAdapter.clean();
            mContentList.setSelectedPosition(0);
        }
    }

    @Override
    public void hideLine() {
        moviesListLine.setVisibility(View.INVISIBLE);
    }

    @Override
    public void resetFilterField() {
        mVODFilter = null;
        mSortType = null;
    }

    private FilterSortAdapter.OnItemClickListener mSortOnClickListener = new FilterSortAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, View selectedView) {
            if (mSortSelectedView != null && mSortSelectedView == view) {
                return;
            }
            SortFilterBean sortFilterBean = mSortFilterBeanList.get(position);
            //String sortType = sortFilterBean.getSortType();
            if (selectedView != null) {
                selectedView.setSelected(false);
                selectedView.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.INVISIBLE);
            }
            if (mSortSelectedView != null) {
                mSortSelectedView.setSelected(false);
                mSortSelectedView.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.INVISIBLE);
            }
            mMoviesListPresenter.setSortType(sortFilterBean);
            view.setSelected(true);
            view.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.VISIBLE);
            mSortSelectedView = view;
            doFilter();
        }
    };

    private FilterCatagoryAdapter.OnItemClickListener mGenreOnClickListener = new FilterCatagoryAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, View selectedView) {
            if (mGenreIDSelectedView != null && mGenreIDSelectedView == view) {
                return;
            }
            Genre genre = mGenreList.get(position);
            List<Genre> genreList = new ArrayList<>();
            genreList.add(genre);
            if (!(genre.getGenreName().equals("??????"))) {
                mMoviesListPresenter.setmGenreIDList(genreList);
            } else {
                mMoviesListPresenter.setmGenreIDList(null);
            }
            if (selectedView != null) {
                selectedView.setSelected(false);
                selectedView.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.INVISIBLE);
            }

            if (mGenreIDSelectedView != null) {
                mGenreIDSelectedView.setSelected(false);
                mGenreIDSelectedView.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.INVISIBLE);
            }
            view.setSelected(true);
            view.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.VISIBLE);
            mGenreIDSelectedView = view;
            doFilter();
        }
    };

    private FilterAreaAdapter.OnItemClickListener mAreaOnClickListener = new FilterAreaAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, View selectedView) {
            if (mAreaSelectedView != null && mAreaSelectedView == view) {
                return;
            }
            SuperLog.info(TAG, "mAreaOnClickListener");
            ProduceZone produceZone = mProduceZoneList.get(position);
            List<ProduceZone> produceZoneList = new ArrayList<>();
            produceZoneList.add(produceZone);
            if (!(produceZone.getName().equals("??????"))) {
                mMoviesListPresenter.setmProduceZoneList(produceZoneList);
            } else {
                mMoviesListPresenter.setmProduceZoneList(null);
            }
            if (selectedView != null) {
                selectedView.setSelected(false);
                selectedView.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.INVISIBLE);
            }
            if (mAreaSelectedView != null) {
                SuperLog.info(TAG, "mAreaSelectedView?????????");
                mAreaSelectedView.setSelected(false);
                mAreaSelectedView.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.INVISIBLE);
            }
            view.setSelected(true);
            view.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.VISIBLE);
            mAreaSelectedView = view;
            mFilterAreaAdapter.setSelectedView(view);
            doFilter();
        }
    };

    private FilterYearAdapter.OnItemClickListener mYearOnClickListener = new FilterYearAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, View selectedView) {
            if (mYearSelectedView != null && mYearSelectedView == view) {
                return;
            }
            YearFilterBean yearFilterBean = mYearFilterBeanList.get(position);
            String year = yearFilterBean.getFilterType();
            List<String> yearList = new ArrayList<>();
            yearList.add(year);
            if (!(year.equals("??????"))) {
                mMoviesListPresenter.setmProduceYearList(yearList);
            } else {
                mMoviesListPresenter.setmProduceYearList(null);
            }
            if (selectedView != null) {
                selectedView.setSelected(false);
                selectedView.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.INVISIBLE);
            }
            if (mYearSelectedView != null) {
                mYearSelectedView.setSelected(false);
                mYearSelectedView.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.INVISIBLE);
            }
            view.setSelected(true);
            view.findViewById(R.id.movies_list_filter_item_img).setVisibility(View.VISIBLE);
            mYearSelectedView = view;
            doFilter();
        }
    };

    /**
     * ??????????????????
     */
    private void doFilter() {
        isDoFilter = true;
        String sortType = null;
        if (mMoviesListPresenter.getSortType() != null) {
            sortType = mMoviesListPresenter.getSortType().getSortType();
        }
        VODFilter vodFilter = new VODFilter();
        List<String> produceZoneList = new ArrayList<>();
        if (mMoviesListPresenter.getmProduceZoneList() != null && mMoviesListPresenter.getmProduceZoneList().size() != 0) {
            produceZoneList.add(mMoviesListPresenter.getmProduceZoneList().get(0).getID());
        }
        vodFilter.setProduceZoneIDs(produceZoneList);
        vodFilter.setProduceYears(mMoviesListPresenter.getmProduceYearList());
        List<String> genIDList = new ArrayList<>();
        if (mMoviesListPresenter.getmGenreIDList() != null && mMoviesListPresenter.getmGenreIDList().size() != 0) {
            genIDList.add(mMoviesListPresenter.getmGenreIDList().get(0).getGenreID());
        }
        vodFilter.setGenreIDs(genIDList);
        Subject subject = mMoviesListPresenter.getSubject();
        String strSort = "";
        String strGenre = "";
        String strArea = "";
        String strYear = "";
        if (sortType != null) {
            strSort = mMoviesListPresenter.getSortType().getName() + "|";
        } else {
            strSort = "??????" + "|";
        }
        if (mMoviesListPresenter.getmGenreIDList() != null && mMoviesListPresenter.getmGenreIDList().size() != 0) {
            strGenre = mMoviesListPresenter.getmGenreIDList().get(0).getGenreName() + "|";
        } else {
            strGenre = "????????????" + "|";
        }
        if (mMoviesListPresenter.getmProduceZoneList() != null && mMoviesListPresenter.getmProduceZoneList().size() != 0) {
            strArea = mMoviesListPresenter.getmProduceZoneList().get(0).getName() + "|";
        } else {
            strArea = "????????????" + "|";
        }
        if (mMoviesListPresenter.getmProduceYearList() != null && mMoviesListPresenter.getmProduceYearList().size() != 0) {
            strYear = mMoviesListPresenter.getmProduceYearList().get(0);
        } else {
            strYear = "????????????";
        }
        String content = strSort + strGenre + strArea + strYear;
        mTvFilterConditions.setText(content);
        mVODFilter = vodFilter;
        mSortType = sortType;
        mContentAdapter.clean();
        mMoviesListPresenter.loadVODListBySubject(subject, "0", "24", vodFilter, sortType);
        resetLoadNum();
        SuperLog.info(TAG, vodFilter.toString() + "->" + sortType);
    }

    @Override
    public boolean interceptFocus(KeyEvent event, View view) {
        int keycode = event.getKeyCode();
        if (keycode == KeyEvent.KEYCODE_DPAD_DOWN && mContentList.findFocus() != null && isBorder((ViewGroup) view, mContentList.findFocus(), View.FOCUS_DOWN)) {
            if (totalNumber != 0 && mContentList.getLayoutManager().getItemCount() == totalNumber) {
                //??????
                int remainder = totalNumber % 6;
                //?????????????????????????????????
                int rate = (totalNumber - remainder) / 6;
                if (rate != 0&&remainder!=0) {
                    //????????????????????????
                    int position = mContentList.getChildAdapterPosition(mContentList.findFocus());
                    //??????
                    int lastSecondPosition = rate * 6 - 1;
                    if (lastSecondPosition >= position && lastSecondPosition - 6 < position && ((position + 1) % 6 > remainder || (position + 1) % 6 == 0)) {
                        mContentList.getLayoutManager().scrollToPosition(totalNumber - 1);
                        View lastView = mContentList.getLayoutManager().findViewByPosition(totalNumber - 1);
                        if (null == lastView) {
                            if (null != mContentList.findViewHolderForLayoutPosition(totalNumber - 1)) {
                                lastView = mContentList.findViewHolderForLayoutPosition(totalNumber - 1).itemView;
                            }
                        }
                        if (null != lastView) {
                            lastView.setFocusable(true);
                            lastView.requestFocus();
                        }
                    }
                }
            }
        }
        if(keycode == KeyEvent.KEYCODE_DPAD_UP && mContentList.findFocus() != null && isBorder((ViewGroup) view, mContentList.findFocus(), View.FOCUS_UP)){
            int position = mContentList.getChildAdapterPosition(mContentList.findFocus());
            if(position<6)
            {
                filterName.setFocusable(true);
                filterName.requestFocus();
            }
        }
        if(keycode == KeyEvent.KEYCODE_DPAD_RIGHT && mContentList.findFocus() != null && isBorder((ViewGroup) view, mContentList.findFocus(), View.FOCUS_RIGHT)){
            mContentList.findFocus().setFocusable(true);
            mContentList.findFocus().requestFocus();
            return true;
        }
        return false;
    }


    /**
     * ?????????????????????
     *
     * @param root
     * @param focused
     * @param direction
     * @return
     */
    private boolean isBorder(ViewGroup root, View focused, int direction) {
        return FocusFinder.getInstance().findNextFocus(root, focused,
                direction) == null ? true : false;
    }
}
