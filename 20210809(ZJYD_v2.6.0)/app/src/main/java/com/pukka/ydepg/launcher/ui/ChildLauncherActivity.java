package com.pukka.ydepg.launcher.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.adapter.CommonAdapter;
import com.pukka.ydepg.common.adapter.MultiItemTypeAdapter;
import com.pukka.ydepg.common.adapter.base.ViewHolder;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.RelativeLayoutNoFocus;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.Launcher;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.http.v6bean.v6node.Page;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.customui.dialog.ManualRefreshDialog;
import com.pukka.ydepg.customui.tv.widget.MainUpView;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.ReflectItemView;
import com.pukka.ydepg.customui.tv.widget.RvLinearLayoutManager;
import com.pukka.ydepg.event.RefreshLauncherEvent;
import com.pukka.ydepg.event.ShimmerCloseEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.mvp.contact.ChildLauncherContact;
import com.pukka.ydepg.launcher.mvp.presenter.ChildLauncherPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.adapter.PHMFragmentAdapter;
import com.pukka.ydepg.launcher.ui.fragment.FocusObserver;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.view.FocusRelativeLayoutExt;
import com.pukka.ydepg.launcher.view.ViewPagerFocusExt;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.search.SearchActivity;
import com.pukka.ydepg.service.NetworkReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 二级桌面
 *
 * @author luwm
 * @FileName: com.pukka.ydepg.moudule.home.ui.ChildLauncherActivity.java
 * @date: 2018-05-24 18:03
 * @version: V1.0 描述当前版本功能
 */
public class ChildLauncherActivity extends BaseActivity<ChildLauncherPresenter> implements ChildLauncherContact.IChildLauncherView, View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = ChildLauncherActivity.class.getSimpleName();
    public static final String KEY_PAGE_ID = "PageId";

    //终端参数 针对FRS：判断用户无播放或无操作5分钟后，触发自动刷新，如果没有配置，默认5分钟
    public static final String REFRESH_CHILD_LAUNCHER = "refresh_child_launcher";
    RelativeLayoutNoFocus mRlContainer;
    TextClock mTextClock;
    RecyclerViewTV mNavRecyclerView;
    ImageViewExt mIvSearch;
    ImageViewExt mIvNetworkStatus;
    ReflectItemView mRiSearch;
    ViewPagerFocusExt mViewpager;
    MainUpView mMainUpView;
    BrowseFrameLayout mContainerLayout;
    RelativeLayout rlNavs;
    FocusRelativeLayoutExt mMainLayout;
    ImageView mMainImageView;

    PHMFragment tabItemFragment;

    //返回键置顶提示
    TextViewExt mTvBackTopHint;

    /**
     * 是否所有导航栏为空
     */
    boolean isAllNavEmpty = true;
    private ChildLauncherFocusHelper mLauncherFocusHelper;
    protected int currentPosition;  //当前的position
    protected PHMFragmentAdapter mFragmentAdapter;
    private boolean firstLoad = true;
    private static final int SET_FOCUS = 100;
    private static final int LAUNCHER_UPDATE = 400;
    protected NetworkReceiver networkReceiver;
    private boolean disableKeyCode = true;
    private String pageId;
    private String mBgUrl;
    protected boolean isLoadLauncherSuccess = false;

    //2.3刷新机制
    private TimeHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SuperLog.debug(ChildLauncherActivity.TAG, "onCreate");
        setContentView(R.layout.activity_child_launcher);
        mMainImageView = (ImageView) findViewById(R.id.iv_main_bg);
        mRlContainer = (RelativeLayoutNoFocus) findViewById(R.id.rl_container);
        mTextClock = (TextClock) findViewById(R.id.tc_textClock);
        mNavRecyclerView = (RecyclerViewTV) findViewById(R.id.rv_nav);
        mIvSearch = (ImageViewExt) findViewById(R.id.iv_search);
        mIvNetworkStatus = (ImageViewExt) findViewById(R.id.iv_NetworkStatus);
        mRiSearch = (ReflectItemView) findViewById(R.id.ri_search);
        mViewpager = (ViewPagerFocusExt) findViewById(R.id.vp_viewpager);
        rlNavs = (RelativeLayout) findViewById(R.id.rl_navs);
        mMainUpView = (MainUpView) findViewById(R.id.mp_mainUpView);
        mContainerLayout = (BrowseFrameLayout) findViewById(R.id.bl_container);
        mMainLayout = (FocusRelativeLayoutExt) findViewById(R.id.rl_main_lay);
        mTvBackTopHint = findViewById(R.id.tv_child_back_top_hint);
        pageId = getIntent().getStringExtra(KEY_PAGE_ID);
        mNavRecyclerView.setLayoutManager(new RvLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false,mNavRecyclerView));
        presenter.setPageId(pageId);
        if (!TextUtils.isEmpty(LauncherService.getInstance().getQueryPhmLauncher()) && LauncherService.getInstance().getQueryPhmLauncher().equalsIgnoreCase("1")){
            presenter.queryLauncher(pageId, true,this,true);
        }else if (isNeedUpdate()) {
            SharedPreferenceUtil.getInstance().saveChildLauncherUpdate(false);
            presenter.queryLauncher(pageId, true,this,true);
        } else {
            presenter.loadLauncher();
        }
        setNetworkStatusIcon();  //设置网络状态图标
        initReceiver();
        initClickListener();
        initFreshLauncher();
        mIvSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mRiSearch.setBackgroundResource(R.drawable.top_focus);
                } else {
                    mRiSearch.setBackgroundResource(0);
                }
            }
        });
    }
    private void initFreshLauncher(){
        handler = new TimeHandler(this);
        if (null == presenter){
            presenter = new ChildLauncherPresenter();
        }
        LauncherService.getInstance().setRefreshChild(pageId,presenter);
    }

    private boolean isNeedUpdate() {
        return ((!presenter.isFileExist()) || SharedPreferenceUtil.getInstance().getLauncherUpdate() || SharedPreferenceUtil.getInstance().getChildLauncherUpdate());
    }

    private void initClickListener() {
        mIvSearch.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_FOCUS:
                    loadBackGroup(getPageBgUrl());
                    //实例化焦点管理对象
                    mLauncherFocusHelper = new ChildLauncherFocusHelper(ChildLauncherActivity.this);
                    break;
                case LAUNCHER_UPDATE:
                    disableKeyCode = false;
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (firstLoad) {
            firstLoad = false;
        }
        disableKeyCode = false;

        //startTimeRefresh();
        //从二级桌面的二级页面返回二级桌面，检查是否要刷新使用最新的jason
        upData();

    }

    public void updateLauncher() {
        runOnUiThread(()->{
            if (null != presenter.getLauncher()){
                initNav(presenter.getLauncher().getNavigateList());
                initFragments(presenter.getLauncher().getNavigateList());
                mainHandler.sendEmptyMessage(LAUNCHER_UPDATE);
            }
        });
    }

    private boolean isDownload = false;

    @Override
    public void downLoadLauncher() {
        //已下载launcher
        isDownload = true;
    }

    /**
     * 设置按键拦截器拦截焦点
     *
     * @param focusObserver 实现focusObserver接口的viewGroup
     */
    public void registerFocusInter(FocusObserver focusObserver) {
        if (focusObserver != null) {
            focusObserver.setInterceptor(mLauncherFocusHelper);
        }
    }


    /**
     * 根据网络状态设置网络图标
     */
    private void setNetworkStatusIcon() {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                mIvNetworkStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.wifi));
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                mIvNetworkStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.wire));
            }
        } else {
            mIvNetworkStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.wire_error));
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
        mainHandler.removeCallbacksAndMessages(null);
        unRegister();
        LauncherService.getInstance().setRefreshChild(null,null);
    }


    /**
     * 加载动态桌面和导航数据
     *
     * @param navigates
     */
    private void initFragments(List<Navigate> navigates) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Navigate> navigatesTem = new ArrayList<>(navigates);
        mFragmentAdapter = new PHMFragmentAdapter(fragmentManager, mViewpager, navigatesTem,  pageId);
        mViewpager.setAdapter(mFragmentAdapter);
        //注册mViewpager监听
        mViewpager.addOnPageChangeListener(pageListener);
        /**
         *   切换账号，这个方法重新调用会报，放到下面可能没用
         *   java.lang.IllegalStateException: The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged!
         *
         */
        mViewpager.setOffscreenPageLimit(1);
        mainHandler.sendEmptyMessage(SET_FOCUS);
        if (null == tabItemFragment && currentPosition < mFragmentAdapter.size()) {
            tabItemFragment = (PHMFragment) mFragmentAdapter.get(currentPosition);
        }
        if (null != tabItemFragment){
            tabItemFragment.setChildActivity(this);
        }
    }

    public void showBackTopHint(boolean isToShow){
        if (!isToShow){
        //if (isTop()){
            //已在顶部
            mTvBackTopHint.setVisibility(View.GONE);
        }else{
            mTvBackTopHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) { }

    /**
     * 从presenter返回的launcher.json的数据模板
     *
     * @param navigates
     * @param groupElementsList
     */
    @Override
    public void loadLauncherData(List<Navigate> navigates, ArrayList<List<GroupElement>> groupElementsList) {
        if (null == navigates || navigates.size() == 0){
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isLoadLauncherSuccess) {
                    //加载背景
                    Launcher launcher = presenter.getLauncher();
                    if (null != launcher){
                        mBgUrl = launcher.getDefaultBackground();
                        loadBackGroup(mBgUrl);
                    }
                    mTvBackTopHint.setVisibility(View.GONE);
                    initNav(navigates);
                    initFragments(navigates);
                    loadLauncherSuccess(true);
                }
            }
        });

    }

    @SuppressLint("CheckResult")
    public void loadBackGroup(String imgUrl) {
        /**
         * example：imgUrl 为单独配置的桌面背景，mBgUrl为统一桌面背景
         *现需求为：有背景默认使用背景，没有则使用统一桌面背景，都没有则使用项目里内置默认背景
         */
        String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
        try {
            RequestOptions options  = new RequestOptions();

            if (!TextUtils.isEmpty(imgUrl)) {
                if (!TextUtils.isEmpty(launcherLink)) {
                    String contentURL = "http://" + AuthenticateManager.getInstance().getUserInfo().getIP() + ":" + AuthenticateManager.getInstance().getUserInfo().getPort() + launcherLink + GlideUtil.getUrl(imgUrl);
                    SuperLog.debug(TAG, "contentURL" + contentURL);
                    if (null != mMainImageView.getDrawable()) {
                        options.placeholder(mMainImageView.getDrawable());
                        Glide.with(this).load(contentURL)
                                .apply(options).into(mMainImageView);
                    } else {
                        options.placeholder(R.drawable.default_detail_bg1);
                        Glide.with(this).load(contentURL)
                                .apply(options).into(mMainImageView);
                    }
                }
            } else {
                String background = "http://" + AuthenticateManager.getInstance().getUserInfo().getIP() + ":" + AuthenticateManager.getInstance().getUserInfo().getPort() + launcherLink + mBgUrl;
                if (background.contains("null")){
                    background = "";
                }
                if (!TextUtils.isEmpty(mBgUrl)) {
                    if (null != mMainImageView.getDrawable()) {
                        options.placeholder(mMainImageView.getDrawable());
                        Glide.with(this).load(background).apply(options).into(mMainImageView);
                    } else {
                        options.placeholder(R.drawable.default_detail_bg1);
                        Glide.with(this).load(background)
                                .apply(options).into(mMainImageView);
                    }
                } else {
                    options.placeholder(R.drawable.default_detail_bg1);
                    Glide.with(this).load(background)
                            .apply(options).into(mMainImageView);
                }
            }
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver, intentFilter);
        networkReceiver.setNetWorkStateListener(new NetworkReceiver.NetWorkStateListener() {
            /**
             * 网络状态为有线
             */
            @Override
            public void changeToWire() {
                mIvNetworkStatus.setBackground(ContextCompat.getDrawable(ChildLauncherActivity.this, R.drawable.wire));
            }

            /**
             * 网络状态为无线
             */
            @Override
            public void changeToWifi() {
                mIvNetworkStatus.setBackground(ContextCompat.getDrawable(ChildLauncherActivity.this, R.drawable.wifi));
            }

            /**
             * 网络状态为无连接
             */
            @Override
            public void changeToError() {
                mIvNetworkStatus.setBackground(ContextCompat.getDrawable(ChildLauncherActivity.this, R.drawable.wire_error));
            }
        });
    }

    /**
     * 加载顶部导航
     *
     * @param navigateList
     */
    private void initNav(final List<Navigate> navigateList) {
        CommonAdapter<Navigate> mNavAdapter = new CommonAdapter<Navigate>(this, R.layout.item_launcher_nav, navigateList) {
            @Override
            protected void convert(ViewHolder holder, Navigate navigate, int position) {
                if (!CollectionUtil.isEmpty(navigate.getNameDialect()) && null != navigate.getNameDialect().get(0).getValue()) {
                    holder.setText(R.id.tv_nav_title, navigate.getNameDialect().get(0).getValue());
                } else {
                    holder.setText(R.id.tv_nav_title, "");
                }
            }
        };

        mNavAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //栏目跳转到列表页
                String actionUrl = navigateList.get(position).getActionURL();
                if (TextUtils.isEmpty(actionUrl)) {
                    return;
                }
                ZJVRoute.route(ChildLauncherActivity.this, ZJVRoute.LauncherElementDataType.STATIC_ITEM, actionUrl, null, null, null, null);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mNavRecyclerView.setAdapter(mNavAdapter);
        if (isAllNavEmpty(navigateList)) {
            rlNavs.setVisibility(View.GONE);
        }
    }

    /**
     * 用来判断是否所有的导航栏目都没有配置名称
     *
     * @param navigateList
     * @return
     */
    private boolean isAllNavEmpty(List<Navigate> navigateList) {
        if (CollectionUtil.isEmpty(navigateList)){
            isAllNavEmpty = true;
            return true;
        }
        for (Navigate navigate : navigateList) {
            if (!CollectionUtil.isEmpty(navigate.getNameDialect()) && !TextUtils.isEmpty(navigate.getNameDialect().get(0).getValue())) {
                isAllNavEmpty = false;
                return false;
            }
        }
        isAllNavEmpty = true;
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:  //搜索按钮
                Intent intent = new Intent(ChildLauncherActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableKeyCode = true;
        //stopTimeRefresh();
    }

    //定时刷新launcher
    public void refreshLauncher(){
        isLoadLauncherSuccess = false;
        presenter.queryLauncher(pageId, true,this,true);
    }

    public View getFocusView(){
        if (null != mLauncherFocusHelper){
            return mLauncherFocusHelper.getFocusView();
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //startTimeRefresh();
        if (null != mLauncherFocusHelper){
            mLauncherFocusHelper.setIsFirs();
        }
        if (disableKeyCode) {
            return true;
        }
        int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
        if (codeValue == RemoteKeyEvent.BTV
                && OTTApplication.getContext().isLoginSuccess()
                && OTTApplication.getContext().isLoadLauncherSuccess()) {
            //需要等待launcher渲染出来之后才允许点击
            //直播
            if (isLauncher == 1) {
                switchLiveTV(false);
            }
        } else if (codeValue == RemoteKeyEvent.TVOD
                && OTTApplication.getContext().isLoginSuccess()
                && OTTApplication.getContext().isLoadLauncherSuccess()) {
            //需要等待launcher渲染出来之后才允许点击
            //回看(点播)
            if (isLauncher == 1) {
                switchLiveTV(true);
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            EventBus.getDefault().post(new ShimmerCloseEvent());
            //与1.8版本保持一致，点击菜单键不显示-1屏
            /*if (null == manualRefreshDialog) {
                ManualRefreshDialog manualRefreshDialog = new ManualRefreshDialog(this);
                manualRefreshDialog.setIsMainActivity(false);
                manualRefreshDialog.show();
            }else if (!manualRefreshDialog.isShowing()){
                manualRefreshDialog.show();
            }*/
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  //返回键时焦点向上
            //本地播放，跳第三方软件，我们APP也会响应返回事件，得屏蔽掉
            if (OTTApplication.isApplicationBroughtToBackground(getApplicationContext()) || OTTApplication.getContext().getIsJumpToThirdApp()) {
                OTTApplication.getContext().setIsJumpToThirdApp(false);
                return true;
            }

            if (isTop()){
                //已在顶部
                if (isAllNavEmpty) {
                    this.finish();
                    return true;
                }
            }else{
                //返回到顶部
                mLauncherFocusHelper.onKeyBack();
                setDefaultFocus(tabItemFragment);
                mTvBackTopHint.setVisibility(View.GONE);
                return true;
            }

        }
        return super.onKeyUp(keyCode, event);
    }

    private View firstView = null;
    private boolean isTop(){
        if (null != mTvBackTopHint && mTvBackTopHint.getVisibility() == View.GONE){
            return true;
        }
        return false;
    }

    ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                Glide.with(ChildLauncherActivity.this).resumeRequests();
                mLauncherFocusHelper.onPageChange();   //切换导航栏时变化标签选中效果
                loadBackGroup(getPageBgUrl());
                //加载数据
                if (currentPosition < mFragmentAdapter.size()) {
                    tabItemFragment = (PHMFragment) mFragmentAdapter.get(currentPosition);
                    /*//实例化焦点管理对象
                    mLauncherFocusHelper = new ChildLauncherFocusHelper(ChildLauncherActivity.this);*/
                    registerFocusInter(mMainLayout);
                }
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                Glide.with(ChildLauncherActivity.this).pauseRequests();
                if (currentPosition != 0 && currentPosition < mFragmentAdapter.size()) {
                    PHMFragment phmFragment = (PHMFragment) mFragmentAdapter.get(currentPosition);
                    if (phmFragment != null) {
                        phmFragment.scrollToTop();
                    }
                }
            }
        }
    };

    private void upData(){
        if (isNeedUpdate()) {
            SharedPreferenceUtil.getInstance().saveChildLauncherUpdate(false);
            if (isDownload){
                presenter.loadLauncher();
            }else{
                presenter.queryLauncher(pageId, false,this,true);
            }
        }

    }

    @Subscribe
    public void onEvent(RefreshLauncherEvent event) {

        if (event.isMainActivity())return;

        if (isNeedUpdate()) {
            SharedPreferenceUtil.getInstance().saveChildLauncherUpdate(false);
            presenter.queryLauncher(pageId, true,this,true);
        } else {
            presenter.loadLauncher();
        }

    }

    private String getPageBgUrl() {
        String backGroundUrl = null;
        Launcher launcher = presenter.getLauncher();
        if (null != launcher && currentPosition < launcher.getNavigateList().size()) {
            List<Page> pages = launcher.getNavigateList().get(currentPosition).getPageList();
            if (!CollectionUtil.isEmpty(pages)) {
                backGroundUrl = pages.get(0).getBackground();
            }
        }
        SuperLog.debug(TAG, "currentPosition" + currentPosition + "backGroundUrl" + backGroundUrl);
        return backGroundUrl;
    }

    private void setDefaultFocus(PHMFragment tabItemFragment) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != tabItemFragment && null != tabItemFragment.getFirstFocusView()) {
                                tabItemFragment.getFirstFocusView().requestFocus();
                                timer.cancel();
                            }

                        }
                    });
            }
        }, 500, 100);
    }

    @Subscribe
    public void eventDispatch(String s) {
        switch (s) {
            case Constant.SCROLL_COMPLETE:
                mLauncherFocusHelper.drawFocusEffect();
                break;
        }
    }


    @Override
    protected void initPresenter() {
        presenter = new ChildLauncherPresenter();
    }


    @Override
    /**
     * 重新认证
     */
    public void reAuthenticate() {
    }


    /**
     * 注册网络状态监听
     */
    private void registerNetWorkListener() {
        networkReceiver = new NetworkReceiver();
    }


    /**
     * 注销广播等资源
     */
    private void unRegister() {
        SuperLog.debug(TAG, "unRegister");
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }


    private void initReceiver() {
        registerNetWorkListener();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    public void loadLauncherSuccess(boolean success) {
        isLoadLauncherSuccess = success;
        OTTApplication.getContext().setLoadLauncherSuccess(success);
        setNetworkStatusIcon();
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLauncher = 1;
            }
        }, 1000);
    }

    private static class TimeHandler extends Handler{
        private WeakReference<ChildLauncherActivity> weakReference;
        private String mRefreshTime;
        private ChildLauncherActivity activity;

        public TimeHandler(ChildLauncherActivity activity){
            this.weakReference = new WeakReference<>(activity);
            this.activity = activity;

            mRefreshTime = SessionService.getInstance().getSession().getTerminalConfigurationValue(REFRESH_CHILD_LAUNCHER);
            if (TextUtils.isEmpty(mRefreshTime)){
                mRefreshTime = "5";
            }
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != weakReference.get()){
                switch (msg.what) {
                    case 0:
                        if (hasMessages(1)){
                            removeMessages(1);
                        }
                        sendEmptyMessageDelayed(1,Integer.parseInt(mRefreshTime) * 60 * 1000);
                        break;
                    case 1:
                        activity.upData();
                        break;
                    default: break;
                }
            }
        }
    }

    private void startTimeRefresh() {
        if (null == handler) {
            handler = new TimeHandler(this);
        }
        if (handler.hasMessages(0)) {
            handler.removeMessages(0);
        }
        handler.sendEmptyMessageDelayed(0, 30 * 1000);

    }
    private void stopTimeRefresh(){
        if (null != handler){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}