package com.pukka.ydepg.launcher.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chinamobile.middleware.auth.TokenManager;
import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.ad.AdConstant;
import com.pukka.ydepg.common.ad.AdManager;
import com.pukka.ydepg.common.ad.ui.WelcomeRelativeLayout;
import com.pukka.ydepg.common.adapter.CommonAdapter;
import com.pukka.ydepg.common.adapter.MultiItemTypeAdapter;
import com.pukka.ydepg.common.adapter.base.ViewHolder;
import com.pukka.ydepg.common.constant.Constant.DesktopType;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.LinearLayoutExt;
import com.pukka.ydepg.common.extview.MarqueeEpgTextView;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.RelativeLayoutNoFocus;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.http.v6bean.v6node.Page;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.profile.ProfileManager;
import com.pukka.ydepg.common.profile.adapter.ConvertDataFromPbsToEpg;
import com.pukka.ydepg.common.pushmessage.presenter.PushMessagePresenter;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.ubd.UBDConstant;
import com.pukka.ydepg.common.report.ubd.extension.TopFunctionData;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Desktop;
import com.pukka.ydepg.common.report.ubd.scene.UBDDesktopType;
import com.pukka.ydepg.common.report.ubd.scene.UBDPurchase;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.report.ubd.scene.UBDTopFunction;
import com.pukka.ydepg.common.toptool.EpgGuideTool;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.upgrade.IUpgradeListener;
import com.pukka.ydepg.common.upgrade.UpgradeManager;
import com.pukka.ydepg.common.utils.ActivityStackControlUtil;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.GlideUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.customui.dialog.ManualRefreshDialog;
import com.pukka.ydepg.customui.tv.widget.MainUpView;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.ReflectItemView;
import com.pukka.ydepg.customui.tv.widget.RvLinearLayoutManager;
import com.pukka.ydepg.event.NavFocusEvent;
import com.pukka.ydepg.event.RefreshLauncherEvent;
import com.pukka.ydepg.event.ShimmerCloseEvent;
import com.pukka.ydepg.event.ShowRefreshNotifyEvent;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.bean.node.ForceLifecycleEvent;
import com.pukka.ydepg.launcher.mvp.contact.LauncherContact;
import com.pukka.ydepg.launcher.mvp.presenter.LauncherPresenter;
import com.pukka.ydepg.launcher.receiver.TokenReceiver;
import com.pukka.ydepg.launcher.ui.adapter.PHMFragmentAdapter;
import com.pukka.ydepg.launcher.ui.fragment.MyPHMFragment;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.ui.fragment.SimpleEpgFragment;
import com.pukka.ydepg.launcher.ui.reminder.services.ReminderService;
import com.pukka.ydepg.launcher.util.APPUtils;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.SwitchProfileEvent;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.launcher.view.FocusRelativeLayoutExt;
import com.pukka.ydepg.launcher.view.ViewPagerFocusExt;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.home.mvp.contact.StartHeartBitEvent;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.search.SearchActivity;
import com.pukka.ydepg.service.NetworkReceiver;
import com.pukka.ydepg.view.loadingball.MonIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pukka.ydepg.common.profile.ProfileManager.PROFILE_REQUEST_CODE;
import static com.pukka.ydepg.common.profile.ProfileManager.PROFILE_RESULT_CODE;
import static com.pukka.ydepg.common.profile.ProfileManager.URL_PROFILE_MODIFY;
import static com.pukka.ydepg.common.profile.ProfileManager.URL_PROFILE_SELECT;
import static com.pukka.ydepg.launcher.ui.MainActivityUtil.simulateKeystroke;

public class MainActivity extends BaseActivity<LauncherPresenter> implements
        View.OnClickListener,
        LauncherContact.ILauncherView,
        TokenReceiver.TokenStateListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String HOME_TV_LOG_LEFT        = "home_tv_logo_left";  //首页左上角图片logo
    public static final String HOME_TV_LOG_RIGHT       = "home_tv_logo_right"; //首页右左上角图片logo
    public static final String SUB_HOME_TV_LEFT_LOGO   = "sub_home_left_logo"; //二级桌面图片logo
    public static final String SUB_HOME_TV_RIGHT_LOGO  = "sub_home_right_logo"; //二级桌面图片logoS
    public static final String SUB_HOME_TV_MIDDLE_LOGO = "sub_home_middle_logo"; //二级桌面图片logo

    private String mDefaultColor = "#ffffff";
    private String mTopFocusColor = "#ffffff";
    private String mNoticeFocusColor = "#ffffff";

    RelativeLayoutNoFocus mRlContainer;
    RelativeLayout mSimpleEpgContainer;
    TextClock mTextClock;
    TextClock mTextClockForTime;

    RecyclerViewTV mNavRecyclerView;
    RelativeLayout mRelaNavForTop;
    ImageViewExt mIvSearch;
    ImageViewExt mIvLogo1,mIvLogo2;
    ImageViewExt mIvNetworkStatus;
    ReflectItemView mRiSearch;

    MainUpView        mMainUpView;
    BrowseFrameLayout mContainerLayout;
    FocusRelativeLayoutExt mMainLayout;

    ImageView         mBackgroundImageView;//首页背景
    TextViewExt       mManualRefreshBtn;//首页手动刷新按钮
    ImageViewExt      mManualRefreshIm;//首页手动刷新按钮
    RelativeLayoutExt mManualRefreshRl;//首页手动刷新按钮

    MarqueeEpgTextView mTvEpgScrollAds;//首页顶部滚动广告tv

    LinearLayoutExt mLinearContent;//首页功能键content group

    TextViewExt mSwitchProfileBtn;//切换Profile按钮
    private ImageView   mProfileIcon;//Profile头像
    private TextViewExt mProfileAliasText;//Profile昵称
    public RelativeLayout mProfileAliasTextBg;//Profile昵称背景
    RelativeLayout mProfile;//Profile整体UI

    boolean searchIvHasFocus = false;

    protected MonIndicator mLoadingBar;
    private boolean needShowStartPic;

    /*是否是滚动导航栏模式*/
    public final static boolean IS_SCROLL_NAVIGATION_MODE = true;
    /*顶部导航栏View布局*/
    private RelativeLayout mTopView;
    private int mWelcomePlayViewCurrentPosition = 0;
    private boolean isVideoStart;
    private int mFirstIndex;




    PHMFragment mFragment;//当前tag页所在的fragment，用于-1屏切换页面视频播放窗口未走onpause方法，未release播放器

    public PHMFragment getCurrentFragment(){
        return mFragment;
    }

    PHMFragmentAdapter mFragmentAdapter;
    ViewPagerFocusExt mViewpager;





    SimpleEpgFragment simpleEpgFragment;
    PHMFragment childrenEpgFragment;

    List<Navigate> mNavigates = new ArrayList<>();
    private LauncherFocusHelper mLauncherFocusHelper;
    protected int currentPosition;  //当前的position

    protected int currentPositionTemple;  //临时记录调用QueryEpgHome的tab position
    protected int mNavFoucsKeyEvent;  //临时记录调用QueryEpgHome的tab position

    //是否是导航栏由隐藏到显示后第一次获取焦点
    private boolean isFirstShowFocus = false;

    static final int LOAD_COMPLETE                         = 200;
    private static final int LAUNCHER_UPDATE               = 400;
    private static final int LOAD_SSP_AD_BANNER_COMPLETION = 300;

    private static final int CLOCK_SWITCH       = 600;

    protected NetworkReceiver networkReceiver;
    private TokenReceiver mTokenReceiver;
    private boolean disableKeyCode = true;

    private String mDefaultLauncherBgUrl;
    protected boolean isLoadLauncherSuccess = false;

    //广告是否加载结束
    private boolean isLoadAdvertCompleted;

    private boolean isStartPicFinished = false;//开机动画是否结束

    private String mBgUrl;  //当前的背景图地址

    //广告布局
    public WelcomeRelativeLayout mWelcomeRelativeLayiout;

    //是否是从选择多profile页面返回，解决从多profile页面返回，会执行一次onKeyUp中的onback事件，焦点会落焦到默认焦点上
    private boolean isBackFromProfile = false;

    public boolean isStartPicFinished() {
        return isStartPicFinished;
    }
    public int getCurrentPosition(){
        return currentPosition;
    }

    Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            if (mWelcomeRelativeLayiout.getPlayView() != null && mWelcomeRelativeLayiout.getPlayView().getCurrentPosition() > 0) {
                isVideoStart = true;
                mBackgroundImageView.setVisibility(View.VISIBLE);
                mWelcomeRelativeLayiout.getPlayView().setBackgroundColor(Color.TRANSPARENT);
                mWelcomeRelativeLayiout.getPlayView().setBackground(getResources().getDrawable(R.drawable.transparent_drawable));
                mWelcomeRelativeLayiout.loadDrawable(getResources().getDrawable(R.drawable.transparent_drawable));
                mWelcomeRelativeLayiout.frontGone();
            }
            if (isVideoStart) {
                playRunnable = null;
            } else {
                mainHandler.postDelayed(playRunnable, 100);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_SSP_AD_BANNER_COMPLETION:
                    SuperLog.info2SD(TAG,"SSP ad load and play finished. Startup flow will be continued.");
                    isLoadAdvertCompleted=true;
                    switchLauncher();
                    break;
                case LAUNCHER_UPDATE:
                    setFocus();
                    disableKeyCode = false;
                    break;
                case LOAD_COMPLETE:
                    SuperLog.debug(TAG, "Main handler received [LOAD_COMPLETE] Message");

                    //开机广告页结束，上报UBD开机启动消息From MainActivity_AD to MainActivity_navID
                    UBDSwitch.getInstance().reportStartupInMainActivity(LauncherService.getInstance().getFirstIndex());

                    if(!SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                        ReminderService.getmInstance().checkBootStrapAlertReminder(MainActivity.this);
                    }

                    //解决海信电视首次开机无焦点问题，退出touch mode模式
                    simulateKeystroke(KeyEvent.KEYCODE_SLASH);

                    //解决新增导航页焦点问题
                    setFocus();

                    LiveDataHolder.get().setIsShowingSkip(false);

                    //延时300ms，为了解决：setFocus()获取焦点得时候也有300ms,用户可肉眼看到跳焦
                    new Handler().postDelayed(()->{
                        mContainerLayout.setVisibility(View.VISIBLE);
                        mBackgroundImageView.setVisibility(View.VISIBLE);
                    }, 300);

                    isStartPicFinished = true;
                    if (null != simpleEpgFragment) {
                        simpleEpgFragment.setIsStartPicFinished(isStartPicFinished);
                    }

                    if (null != childrenEpgFragment) {
                        childrenEpgFragment.setIsStartPicFinished(isStartPicFinished);
                    }

                    //解决当老人桌面被开机广告图覆盖时，轮播无法自动滚动的问题
                    if (null != simpleEpgFragment) {
                        simpleEpgFragment.resume();
                    }
                    //隐藏开机图片,展示首页
                    //mWelcomeRelativeLayiout.setVisibility(View.GONE);

                    //展示开机引导图片
                    new EpgGuideTool().showGuidePicture(MainActivity.this,()->{
                        if(ProfileManager.isChangeUser){
                            //开机/切换账号走此分支   执行图片展示结束后,进入Profile选择H5页面
                            ProfileManager.getStartupProfileStatus(MainActivity.this);
                            //H5页面选择完Profile后,回到本类的onActivityResult继续执行逻辑
                        } else {
                            onProfileUIFinished(null);
                        }
                    });
                    break;
                case CLOCK_SWITCH:{
                    //每隔5秒切换时钟
                    if (mTextClock.getVisibility() == View.VISIBLE && mTextClockForTime.getVisibility() == View.GONE){
                        mTextClock.setVisibility(View.GONE);
                        mTextClockForTime.setVisibility(View.VISIBLE);
                    }else{
                        mTextClock.setVisibility(View.VISIBLE);
                        mTextClockForTime.setVisibility(View.GONE);
                    }

                    sendEmptyMessageDelayed(CLOCK_SWITCH,5000);
                    break;
                }
                default:
                    SuperLog.error(TAG,"Unknown message");
                    break;
            }
        }
    };

    //H5页面选择完Profile后,回到此方法继续执行逻辑,同时会先调用此方法,再调用onResume()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PROFILE_REQUEST_CODE && resultCode == PROFILE_RESULT_CODE){
            isBackFromProfile = true;
            SuperLog.info2SD(ProfileManager.TAG,"Profile WebActivity closed and back to MainActivity.");
            //更新Profile昵称
            ProfileManager.updateProfileAlias(mProfileAliasText,mProfileIcon);
            //获取子Profile的爱看栏目页面数据
            ProfileManager.getSubProfileSubjectData(refreshListener,false);
            onProfileUIFinished(true);
        }
    }

    // 统一出口,开机后Profile相关逻辑结束后调用此方法,此方法内逻辑需保证只在开机后执行一次
    // 1 开机后展示Profile相关页面,关闭后进入
    // 2 开机后没展示过Profile相关相面,直接进入
    public void onProfileUIFinished(Boolean isShowProfile){
        //跑马灯改为此处加载，防止宽度计算有误
        initFunctionMenu();
        //关闭开机等待页面,此时关闭将减少黑屏时间,不可随意移动此行代码
        mWelcomeRelativeLayiout.setVisibility(View.GONE);
        if(!ProfileManager.isProfileFinished && isShowProfile!=null){//切换账号后由于isProfileFinished)为true,将不会执行if之内逻辑
            SuperLog.info2SD(TAG,"onProfileUIFinished started!(This method should only be executed once when STB startup)");
            findViewById(R.id.profile).setVisibility(isShowProfile?View.VISIBLE:View.GONE);
            ProfileManager.isProfileFinished = true;
            //开机如果有推送消息没展示开机海报隐藏后再显示推送消息
            MessageDataHolder.get().setIsComplete(true);
            PushMessagePresenter.showXmppMessage();
            //启动自动刷新桌面定时器,启动后 屏保功能/自动刷新桌面功能启动
            RefreshManager.getInstance().startRefreshTimer();
            SuperLog.info2SD(TAG,"Profile step finished. All startup flow finished.");
        }

        //上报开机是否成功
        PbsUaService.reportDesktop(Desktop.getLauncherData(Desktop.getDownloadState(),Desktop.getDownloadTime(),Desktop.getAnalyseState()));
    }

    //PBS的获取Profile状态接口返回4时,不展示管理页面,但需要展示Profile信息UI,调用此方法
    public void dismissProfilePage(){
        onActivityResult(PROFILE_REQUEST_CODE,PROFILE_RESULT_CODE,null);
    }

    public void requestFocusDefault(){
        mIvLogo1.setFocusable(true);
        mIvLogo1.requestFocus();
    }

    public void scrollToItemForNavRecycleView(int position){
        requestFocusDefault();
        (mNavRecyclerView.getLayoutManager()).smoothScrollToPosition(mNavRecyclerView,new RecyclerView.State(),position);
    }

    public void setFocus() {
        if (!SharedPreferenceUtil.getInstance().getIsSimpleEpg() && !SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            //设置精选焦点放到数据加载完成之后，防止导航栏过长时精选抢夺焦点
            mFirstIndex = LauncherService.getInstance().getFirstIndex();

            //焦点位置大于简版EPG位置，简版EPG位置位隐藏状态，所以在获取焦点时-1才能正常获取
            if (LauncherService.getInstance().getSimpleEpgIndex() != -1 && mFirstIndex > LauncherService.getInstance().getSimpleEpgIndex()) {
                mFirstIndex = mFirstIndex - 1;
            }

            setNavFocus();
        }
    }

    private void setNavFocus(){
        if (mFirstIndex > 7 || currentPosition > 7) {
            mNavRecyclerView.scrollToPosition(mFirstIndex);
            scrollToItemForNavRecycleView(mFirstIndex);
        }
        if (null != mNavRecyclerView && null != mNavRecyclerView.getLayoutManager()
                .findViewByPosition(mFirstIndex)) {
            mIvSearch.setFocusable(true);
            currentPosition = mFirstIndex;
            boolean result = mNavRecyclerView.getLayoutManager().findViewByPosition(mFirstIndex).requestFocus();
            SuperLog.info2SD(TAG,"1st index request focus result="+result);
            mViewpager.setCurrentItem(mFirstIndex);
            loadFragmentData();
        }else if (null != mNavRecyclerView){
            mNavRecyclerView.postDelayed(this::setNavFocus, 500);
        }
    }

    private void loadFragmentData(){
        //如果默认落焦栏目在第一个栏目则第一次加载数据需要手动调用,否则从onPageScrollStateChanged方法中调用
        if (0 == mFirstIndex || currentPosition == mFirstIndex) {
            if (mFragmentAdapter .size() > 0) {
                mFragment = mFragmentAdapter.get(mFirstIndex);
            }
            if (null == childrenEpgFragment){
                loadBackGroup(getPageBgUrl());
            }
        }
    }

    /**
     * 加载开机图片
     */
    public void initStartupAdView() {
        SuperLog.debug(TAG, "Begin to initStartupAdView");

        isStartPicFinished = false;
        if (null != simpleEpgFragment) {
            simpleEpgFragment.setIsStartPicFinished(isStartPicFinished);
        }
        if (null != childrenEpgFragment) {
            childrenEpgFragment.setIsStartPicFinished(isStartPicFinished);
        }

        mLoadingBar         = findViewById(com.pukka.ydepg.player.R.id.pb_loading);

        // mWelcomeRelativeLayout初始化过程中加载开机启动图片
        mWelcomeRelativeLayiout = findViewById(R.id.ll_welcome);
        mWelcomeRelativeLayiout.setVisibility(View.VISIBLE);
        mWelcomeRelativeLayiout.setListener(new WelcomeRelativeLayout.CompleteListener() {
            @Override
            public void complete() {
                mBackgroundImageView.setVisibility(View.VISIBLE);
                mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
            }
        });

        mWelcomeRelativeLayiout.setPlayListener(new WelcomeRelativeLayout.PlayerListener() {
            @Override
            public void onComplete() {
                mBackgroundImageView.setVisibility(View.VISIBLE);
                mWelcomeRelativeLayiout.getPlayLayout().setVisibility(View.GONE);
                mWelcomeRelativeLayiout.getPlayView().setBackground(getResources().getDrawable(R.drawable.transparent_drawable));
                mWelcomeRelativeLayiout.complete();
                mWelcomeRelativeLayiout.getPlayView().setAlpha(0);
                mWelcomeRelativeLayiout.getPlayLayout().setAlpha(0);
                mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
            }

            @Override
            public boolean onError() {
                SuperLog.info2SD(TAG, "mWelcomePlayView onError");
                mBackgroundImageView.setVisibility(View.VISIBLE);
                mWelcomeRelativeLayiout.getPlayView().setBackground(getResources().getDrawable(R.drawable.transparent_drawable));
                mWelcomeRelativeLayiout.loadDrawable(getResources().getDrawable(R.drawable.transparent_drawable));
                mWelcomeRelativeLayiout.getPlayView().setAlpha(0);
                mWelcomeRelativeLayiout.getPlayLayout().setAlpha(0);
                mWelcomeRelativeLayiout.getPlayLayout().setVisibility(View.GONE);
                mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
                return false;
            }

            @Override
            public void onPrepare() {
                isVideoStart = false;
                mainHandler.post(playRunnable);
            }

            @Override
            public void playComplete() {
                mBackgroundImageView.setVisibility(View.VISIBLE);
                mWelcomeRelativeLayiout.getPlayLayout().setVisibility(View.GONE);
                mWelcomeRelativeLayiout.getPlayView().setBackground(getResources().getDrawable(R.drawable.transparent_drawable));
                mWelcomeRelativeLayiout.complete();
                mWelcomeRelativeLayiout.getPlayView().setAlpha(0);
                mWelcomeRelativeLayiout.getPlayLayout().setAlpha(0);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SuperLog.debug(TAG, "onCreate start, object="+this+" task id="+this.getTaskId());
        OTTApplication.getContext().setMainActivity(this);

        //开始认证流程
        presenter.loginAndLoadLauncher(this);
        setContentView(R.layout.activity_launcher);

        //初始化View和Data
        initView();
        initData();
    }

    private void initView(){
        initStartupAdView();

        mBackgroundImageView = (ImageView) findViewById(R.id.iv_main_bg);
        mRlContainer     = (RelativeLayoutNoFocus) findViewById(R.id.rl_container);
        mTextClock       = (TextClock) findViewById(R.id.tc_textClock);
        mTextClockForTime= (TextClock) findViewById(R.id.tc_textClock_time);
        mRelaNavForTop = findViewById(R.id.rela_nav_for_top);
        mNavRecyclerView = (RecyclerViewTV) findViewById(R.id.rv_nav);
        mIvLogo1         = (ImageViewExt) findViewById(R.id.iv_logo1);
        mIvLogo2         = (ImageViewExt) findViewById(R.id.iv_logo2);
        mIvSearch        = (ImageViewExt) findViewById(R.id.iv_search);
        mIvNetworkStatus = (ImageViewExt) findViewById(R.id.iv_NetworkStatus);
        mRiSearch        = (ReflectItemView) findViewById(R.id.ri_search);

        mViewpager       = (ViewPagerFocusExt) findViewById(R.id.vp_viewpager);
        mViewpager.setFocusable(false);//防止viewpager获取焦点
        mTopView         = (RelativeLayout) findViewById(R.id.top_view);
        if (IS_SCROLL_NAVIGATION_MODE) {
            ((FocusRelativeLayoutExt.LayoutParams) mViewpager.getLayoutParams()).topMargin = 0;
        }
        mMainUpView      = (MainUpView) findViewById(R.id.mp_mainUpView);
        mContainerLayout = (BrowseFrameLayout) findViewById(R.id.bl_container);
        mMainLayout      = (FocusRelativeLayoutExt) findViewById(R.id.rl_main_lay);
        mSimpleEpgContainer = (RelativeLayout) findViewById(R.id.rl_simple_epg_content);

        //右上角Profile整体UI
        mProfile = findViewById(R.id.profile);
        //Profile头像
        mProfileIcon = findViewById(R.id.iv_user_head_icon);
        //切换Profile按键
        mSwitchProfileBtn = findViewById(R.id.tv_change_user_btn);

        /*Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(mTopFocusColor,mDefaultColor),mSwitchProfileBtn,null);
        Utils.setTopMenuBg(this,mSwitchProfileBtn,Utils.getShapeForColor(this,mTopFocusColor,300));*/

        mSwitchProfileBtn.setOnClickListener(view->{
            ProfileManager.startProfilePage(this,URL_PROFILE_MODIFY,true);
            UBDTopFunction.reportTopFunction(mSwitchProfileBtn.getText().toString(), TopFunctionData.BTN_NORMAL);
        });
        mSwitchProfileBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mSwitchProfileBtn.setSelected(hasFocus);
                if (hasFocus){
                    Utils.setTextColorUseFocusColor(mTopFocusColor,mSwitchProfileBtn,null);
                }else{
                    Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(mTopFocusColor,mDefaultColor),mSwitchProfileBtn,null);
                }
            }
        });

        //Profile昵称
        mProfileAliasText = findViewById(R.id.tv_user_title);
        mProfileAliasTextBg = findViewById(R.id.tv_user_title_bg);

        /*Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(mTopFocusColor,mDefaultColor),mProfileAliasText,null);
        Utils.setTopMenuBg(this,mProfileAliasTextBg,Utils.getShapeForColor(this,mTopFocusColor,300));*/

        mProfileAliasTextBg.setOnClickListener(view->{
            ProfileManager.startProfilePage(this,URL_PROFILE_SELECT,true);
            UBDTopFunction.reportTopFunction(mProfileAliasText.getText().toString(), TopFunctionData.BTN_NORMAL);
        });
        mProfileAliasTextBg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mProfileAliasTextBg.setSelected(hasFocus);
                if (hasFocus){
                    Utils.setTextColorUseFocusColor(mTopFocusColor,mProfileAliasText,null);
                }else{
                    Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(mTopFocusColor,mDefaultColor),mProfileAliasText,null);
                }
            }
        });

        
        mLinearContent   = findViewById(R.id.linear_content_group_main);
        mTvEpgScrollAds  = findViewById(R.id.tv_epg_scroll_ads);

        mTvEpgScrollAds.setSingleLine();
        mTvEpgScrollAds.setMarqueeRepeatLimit(-1);
        mTvEpgScrollAds.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        //mTvEpgScrollAds.setSelected(true);

        /*Utils.setTextColorUseFocusColor(mNoticeFocusColor,mTvEpgScrollAds,null);
        Utils.setTopMenuBg(this,mTvEpgScrollAds,Utils.getShapeForColor(this,mNoticeFocusColor,8));*/

        mTvEpgScrollAds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mTvEpgScrollAds.setSelected(hasFocus);
            }
        });

        setEpgTopFocusColor();

        //实例化焦点管理对象
        mLauncherFocusHelper = new LauncherFocusHelper(this);
        mNavRecyclerView.setLayoutManager(new RvLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false,mNavRecyclerView));

        setNetworkStatusIcon();  //设置网络状态图标
        //设置按键拦截器拦截焦点
        if (mMainLayout != null) {
            mMainLayout.setInterceptor(mLauncherFocusHelper);
        }

        //initClickListener
        mIvSearch.setOnClickListener(this);

        //首页手动刷新 获取焦点
        Drawable mManuslRefreshFocusDrawable = getResources().getDrawable(R.drawable.manual_refresh_focus_icon);
        Drawable mManuslRefreshNoFocusDrawable = getResources().getDrawable(R.drawable.manual_refresh_nofocus_icon);
        mManuslRefreshFocusDrawable.setBounds(0, 0, mManuslRefreshFocusDrawable.getMinimumWidth(), mManuslRefreshFocusDrawable.getMinimumHeight());
        mManuslRefreshNoFocusDrawable.setBounds(0, 0, mManuslRefreshNoFocusDrawable.getMinimumWidth(), mManuslRefreshNoFocusDrawable.getMinimumHeight());

        mIvSearch.setOnFocusChangeListener((v, hasFocus) -> {
            OTTApplication.getContext().setSearchFocus(hasFocus);
            if (hasFocus) {
                mRiSearch.setBackgroundResource(R.drawable.top_focus);
            } else {
                mRiSearch.setBackgroundResource(0);
            }
        });

        mIvLogo1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    mIvLogo1.setFocusable(false);
                }
            }
        });

        mTextClock.setVisibility(View.VISIBLE);
        mTextClockForTime.setVisibility(View.GONE);
        mainHandler.sendEmptyMessageDelayed(CLOCK_SWITCH,5000);
    }

    public void initFunctionMenu(){
        EpgTopFunctionMenu.getInstance().setMainView(mLinearContent,mTvEpgScrollAds);
        EpgTopFunctionMenu.getInstance().addFunctionMenuItem(this);
    }

    public RelativeLayout getProfile(){
        return mProfile;
    }

    public void initFreshView(RelativeLayoutExt freshRl){
        mManualRefreshBtn= freshRl.findViewById(R.id.tv_main_refresh_btn);
        mManualRefreshIm = freshRl.findViewById(R.id.im_main_refresh_btn);
        mManualRefreshRl = freshRl.findViewById(R.id.rl_main_refresh);
        mManualRefreshBtn.setOnClickListener(this);

    }

    private List<TextViewExt> mFunctionMenuViews = new ArrayList<>();
    public void addFunctionMenuViews(List<TextViewExt> functionMenuViews){
        mFunctionMenuViews = new ArrayList<>();
        mFunctionMenuViews = functionMenuViews;
        if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
            new Handler().postDelayed(()-> {
                focusManageEpgTop(true);
            },1500);
        }
    }

    /**
     * 防止焦点在导航栏上按下键,抢焦点,设置跑马灯 功能键 可获取焦点
     * 焦点在导航栏上按上键 设置可获取焦点  焦点在导航栏上按下键 设置不可获取焦点
     * */
    public void focusManageEpgTop(boolean isFocus){
        if (null != mTvEpgScrollAds && mTvEpgScrollAds.getLocalVisibleRect(new Rect())){
            mTvEpgScrollAds.setFocusable(isFocus);
            mTvEpgScrollAds.setFocusableInTouchMode(isFocus);
        }
        if (null != mManualRefreshBtn && mManualRefreshBtn.getLocalVisibleRect(new Rect())){
            mManualRefreshBtn.setFocusable(isFocus);
            mManualRefreshBtn.setFocusableInTouchMode(isFocus);
        }
        if (null != mFunctionMenuViews && mFunctionMenuViews.size() > 0){
            for (TextViewExt textViewExt : mFunctionMenuViews){
                if(textViewExt.getLocalVisibleRect(new Rect())){
                    textViewExt.setFocusable(isFocus);
                    textViewExt.setFocusableInTouchMode(isFocus);
                }
            }
        }
        if (null != mProfileAliasTextBg){
            mProfileAliasTextBg.setFocusable(isFocus);
            mProfileAliasTextBg.setFocusableInTouchMode(isFocus);
        }
        if (null != mSwitchProfileBtn){
            mSwitchProfileBtn.setFocusable(isFocus);
            mSwitchProfileBtn.setFocusableInTouchMode(isFocus);
        }
    }

    private void initData(){
        registerNetWorkListener();
        registerTokenReceiver();

        //xmpp H5消息仅开机展示一次
        PushMessagePresenter.setHasShowH5Message(false);

        //组织本地APP信息
        MessageDataHolder.get().setAppInfoList(APPUtils.getAllAppInfo(this));
    }

    /**
     * 存在多用户爱看界面，切在二级页面播放过点播，刷新爱看界面的观看历史
     * 1、是否点击过播放 2、是否添加过爱看界面 3、当前界面是否在爱看界面
     * */

    private boolean mIsChangeTab = false;
    private boolean isFreshingPbs = false;//是否正在刷新爱看界面

    public void setIsChangeTab(boolean isChangeTab){
        mIsChangeTab =isChangeTab;
    }
    public void setIsFreshingPbs(boolean isFreshingPbs){
        this.isFreshingPbs = isFreshingPbs;
    }

    public void refreshPbsEpg(boolean isChangeTab){
        mIsChangeTab = isChangeTab;
        if (OTTApplication.getContext().isRefreshPbsEpg() && ConvertDataFromPbsToEpg.isHadAddPbsEpg() && currentPosition >= 0 && currentPosition < mNavigates.size() && mNavigates.get(currentPosition).getId().equalsIgnoreCase(ConvertDataFromPbsToEpg.AIKAN_NAV_ID)){
            setIsFreshingPbs(true);
            OTTApplication.getContext().setIsRefreshPbsEpg(false);
            ProfileManager.getRefreshProfiljectData(refreshListener,true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SuperLog.info2SD(TAG, "MainActivity onResume");

        disableKeyCode = false;

        refreshPbsEpg(false);

        //解决九联401H盒子简版开机获取token失败后卡登陆界面问题
        if(isLoadLauncherSuccess){

            if (RefreshManager.getInstance().needRefresh()) { //如果首页需要更新
                SuperLog.info2SD(TAG, "need to update launcher!! ");
                launcherUpdate(false);
            }
        }

        //清除掉UBDTool用于H5订购上报的Srcch
        UBDPurchase.clearH5Info();

        //将跳转H5订购单例类中的是否正在订购标志位还原
        JumpToH5OrderUtils.getInstance().setDoingOrder(false);
    }

    private void updateFocusColor(){

        mTopFocusColor = "#ffffff";
        mNoticeFocusColor = "#ffffff";

        if (!TextUtils.isEmpty(LauncherService.getInstance().getLauncher().getExtraData().get(EpgTopFunctionMenu.TOP_FOCUS_COLOR))){
            mTopFocusColor = LauncherService.getInstance().getLauncher().getExtraData().get(EpgTopFunctionMenu.TOP_FOCUS_COLOR);
        }
        if (!TextUtils.isEmpty(LauncherService.getInstance().getLauncher().getExtraData().get(EpgTopFunctionMenu.NOTICE_FOCUS_COLOR))){
            mNoticeFocusColor = LauncherService.getInstance().getLauncher().getExtraData().get(EpgTopFunctionMenu.NOTICE_FOCUS_COLOR);
        }
    }

    private void setEpgTopFocusColor(){
        Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(mTopFocusColor,mDefaultColor),mSwitchProfileBtn,null);
        Utils.setTopMenuBg(this,mSwitchProfileBtn,Utils.getShapeForColor(this,mTopFocusColor,300));

        Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(mTopFocusColor,mDefaultColor),mProfileAliasText,null);
        Utils.setTopMenuBg(this,mProfileAliasTextBg,Utils.getShapeForColor(this,mTopFocusColor,300));

        Utils.setTextColorUseFocusColor(mNoticeFocusColor,mTvEpgScrollAds,null);
        Utils.setTopMenuBg(this,mTvEpgScrollAds,Utils.getShapeForColor(this,mNoticeFocusColor,8));
    }

    /**
     * 切换账号后，发送广播，更新launcher
     *
     * @param event
     */
    @Subscribe
    public void event(SwitchProfileEvent event) {
        SuperLog.debug(TAG, "Received SwitchProfileEvent event");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isLoadAdvertCompleted = true;// //切换账号不需要播放广告，直接设置为true
                launcherUpdate(true);
            }
        });
    }

    private void launcherUpdate(boolean isSwitchProfile) {

        EpgTopFunctionMenu.getInstance().resetMapList();
        if (isSwitchProfile){
            //切换账号重置顶部跑马灯功能键map
            ConvertDataFromPbsToEpg.removeOldPbsEpgData(LauncherService.getInstance().getLauncher().getNavigateList(),LauncherService.getInstance().getGroupElements());
            resetTopScrollAdsAndFunctionMenu();
        }else{
            //小红点刷新，焦点落焦到精选，而非爱看
            OTTApplication.getContext().setIsFirstPower(true);
            ConvertDataFromPbsToEpg.convertDataFromPbsToEpg(ConvertDataFromPbsToEpg.mProfileCustomization,refreshListener,false);
        }

        focusManageEpgTop(false);
        mIvSearch.setFocusable(false);

        updateFocusColor();
        setEpgTopFocusColor();

        //开始刷新桌面launcher时再存储launcherLink地址
        updateLauncherLink();
        setChildrenAndSimpleNav(LauncherService.getInstance().getLauncher().getNavigateList());

        mLauncherFocusHelper.clearFocusEffect();
        SuperLog.debug(MainActivity.TAG, "onResume() + 需要更新launcher");
        disableKeyCode = true; //更新首页期间拦截按键
        SuperLog.info2SD(TAG, "start load launcherData ");
        if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()) {
            loadSimpleEpgData(isSwitchProfile);
        } else if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            loadChildrenEpgData(isSwitchProfile);
        } else {
            mRelaNavForTop.setVisibility(View.VISIBLE);

            loadHomeTvLogo();
            /*if (null != mManualRefreshBtn && !mManualRefreshBtn.hasFocus()){
                mIvSearch.requestFocus();  //设置焦点为搜索
            }*/
            mDefaultLauncherBgUrl = GlideUtil.getUrl(LauncherService.getInstance().getLauncher().getDefaultBackground());
            initNav(LauncherService.getInstance().getLauncher().getNavigateList());
            initFragments(LauncherService.getInstance().getLauncher().getNavigateList());
            mainHandler.sendEmptyMessage(LAUNCHER_UPDATE);
            needShowStartPic=isSwitchProfile;
            if (isSwitchProfile) {
                loadBackGroup(mDefaultLauncherBgUrl);
                startHeartBitService();
                switchLauncher();
            }
        }
        SharedPreferenceUtil.getInstance().saveLauncherUpdate(false);
        if (null != mManualRefreshIm){
            mManualRefreshIm.setVisibility(View.GONE);
        }
    }

    //根据网络状态设置网络图标
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
        SuperLog.error(TAG,"MainActivity onDestroy called!");
        try {
            super.onDestroy();
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }
        mainHandler.removeCallbacksAndMessages(null);
        unRegister();
    }

    //加载动态桌面和导航数据
    private void initFragments(List<Navigate> navigates) {
        List<Navigate> navigatesTem = new ArrayList<>(navigates);
        EventBus.getDefault().post(new ForceLifecycleEvent(ForceLifecycleEvent.RESUME));
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragmentAdapter = new PHMFragmentAdapter(fragmentManager,mViewpager,navigatesTem,"");
        mViewpager.setVisibility(View.VISIBLE);
        mViewpager.setAdapter(mFragmentAdapter);
        mViewpager.setOffscreenPageLimit(1);
        mViewpager.addOnPageChangeListener(pageListener);
    }

    ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            //切换导航栏时如果有桌面文件更新则需要加载新桌面
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(RefreshManager.getInstance().needRefresh() && !RefreshManager.getInstance().isFirstSelect()){
                        onEvent(new RefreshLauncherEvent(true));
                    }
                }
            },1000);

            currentPosition = position;
            if (mFragmentAdapter.size() > position){
                mFragment = mFragmentAdapter.get(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE && currentPositionTemple != currentPosition) {
                //滚动完成恢复图片加载
                Glide.with(MainActivity.this).resumeRequests();
                mLauncherFocusHelper.onPageChange();   //切换导航栏时变化标签选中效果
                loadBackGroup(getPageBgUrl());
                //viewpager切换完成触发加载数据
                if (currentPosition >= 0 && currentPosition < mFragmentAdapter.size()) {
                    currentPositionTemple = currentPosition;
                }
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                //滚动中暂停图片加载
                if (!mNavRecyclerView.hasFocus()){
                    Glide.with(MainActivity.this).pauseRequests();
                }
                if (currentPosition < 0){
                    currentPosition = 0;
                }
                if (currentPosition < mFragmentAdapter.size()) {
                    PHMFragment phmFragment = mFragmentAdapter.get(currentPosition);
                    if (phmFragment != null) {
                        phmFragment.scrollToTop();
                    }
                }
            }
        }
    };

    /**
     * @param isFirstLoad 是否是开机加载桌面
     */
    private void loadSimpleEpgData(boolean isFirstLoad) {
        UBDDesktopType.record("simple");
        UBDSwitch.getInstance().setNavPosition(UBDConstant.NavPosition.SIMPLE);
        SharedPreferenceUtil.getInstance().setIsChildrenEpg(false);
        SharedPreferenceUtil.getInstance().setIsSimpleEpg(true);

        //如果当前加载过少儿动漫epg先移除,然后置空
        if (null != childrenEpgFragment) {
            getSupportFragmentManager().beginTransaction().remove(childrenEpgFragment).commitAllowingStateLoss();
            childrenEpgFragment = null;
        }

        if (null != mFragmentAdapter) {
            mFragmentAdapter.notifyDataSetChanged();
            mViewpager.setVisibility(View.GONE);
        }

        mNavRecyclerView.setVisibility(View.GONE);
        mRiSearch.setVisibility(View.GONE);

        if (null != mFragment){
            getSupportFragmentManager().beginTransaction().remove(mFragment).commitAllowingStateLoss();
            mFragment = null;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //重新实例话之前，停止播放
        if(null!=simpleEpgFragment){
            simpleEpgFragment.setRestartNew(true);
            simpleEpgFragment=null;
        }
        //每次切换简版epg重新实例化fragment，保证数据重新加载一次
        simpleEpgFragment = new SimpleEpgFragment();
        simpleEpgFragment.setIsStartPicFinished(isStartPicFinished);
        findViewById(R.id.simple_epg_container).setFocusable(false);
        transaction.replace(R.id.simple_epg_container, simpleEpgFragment).commitAllowingStateLoss();
        disableKeyCode = false;
        loadBackGroup(LauncherService.getInstance().getSimpleEpgBg());
        loadLauncherSuccess(true);
        needShowStartPic = isFirstLoad;
        switchLauncher();
    }

    /**
     * @param isFirstLoad 是否是开机加载桌面
     */
    private void loadChildrenEpgData(boolean isFirstLoad) {
        UBDDesktopType.record("child");
        UBDSwitch.getInstance().setNavPosition(UBDConstant.NavPosition.CHILD);
        SharedPreferenceUtil.getInstance().setIsChildrenEpg(true);
        SharedPreferenceUtil.getInstance().setIsSimpleEpg(false);

        if (null != simpleEpgFragment) {
            getSupportFragmentManager().beginTransaction().remove(simpleEpgFragment).commitAllowingStateLoss();
            simpleEpgFragment = null;
        }

        if (null != mFragmentAdapter) {
            mFragmentAdapter.notifyDataSetChanged();
            mViewpager.setVisibility(View.GONE);
        }

        mRelaNavForTop.setVisibility(View.GONE);

        if (null != mFragment){
            getSupportFragmentManager().beginTransaction().remove(mFragment).commitAllowingStateLoss();
            mFragment = null;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //每次切换简版epg重新实例化fragment，保证数据重新加载一次
        childrenEpgFragment = new PHMFragment();
        childrenEpgFragment.setMainActivity(this);
        childrenEpgFragment.setIsChildrenEpg(true);
        childrenEpgFragment.setIsStartPicFinished(isStartPicFinished);
        findViewById(R.id.simple_epg_container).setFocusable(false);
        transaction.replace(R.id.simple_epg_container, childrenEpgFragment).commitAllowingStateLoss();
        childrenEpgFragment.setNavId(LauncherService.getInstance().getNavIdChildrenEpg());
        childrenEpgFragment.setGroupElementList(LauncherService.getInstance().getChildrenEpgData());
        disableKeyCode = false;
        loadBackGroup(LauncherService.getInstance().getChildrenEpgBg());
        loadLauncherSuccess(true);
        needShowStartPic = isFirstLoad;
        switchLauncher();
    }

    //返回按键操作
    private void onBack(){
        resetMarginForTopView(-1);
        mLauncherFocusHelper.onKeyBack();
    }

    /**
     * 点击切换桌面
     *
     * @param type=0:EPG;=1:简版EPG；=2：少儿动漫
     */
    public void switchLauncher(DesktopType type) {
        //实例化焦点管理对象
        mLauncherFocusHelper = new LauncherFocusHelper(this);
        //解决从简版切到普通版，按左键到搜索再按左键到导航栏最后一个tab,没有加载数据的问题
        currentPositionTemple = -1;
        onBack();
        focusManageEpgTop(false);
        switch (type){
            case NORMAL:
                if(!SharedPreferenceUtil.getInstance().getIsChildrenEpg() && !SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
                    return;
                }
                loadLauncherSuccess(false);
                SharedPreferenceUtil.getInstance().setIsChildrenEpg(false);
                SharedPreferenceUtil.getInstance().setIsSimpleEpg(false);
                showTopScrollAdsAndFunctionMenu();
                loadLauncherData(LauncherService.getInstance().getLauncher().getNavigateList(), false);
                break;
            case SIMPLE:
                if(SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
                    return;
                }
                loadLauncherSuccess(false);
                loadSimpleEpgData(false);
                break;
            case CHILD:
                if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                    return;
                }
                loadChildrenEpgData(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadLauncherSuccess(boolean success) {
        isLoadLauncherSuccess = success;
        UpgradeManager.upgrade(this);//已经有新的升级方法,为保证升级能力,保留老的升级方法
        OTTApplication.getContext().setLoadLauncherSuccess(success);
        setNetworkStatusIcon();
        setFocus();
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLauncher = 1;
            }
        }, 1000);
    }

    //加载首页左右上角logo
    private void loadHomeTvLogo() {
        initImageUrl();
        if (null != mIvLogo1){
            mIvLogo1.setBackgroundResource(0);
            mIvLogo1.setImageResource(0);
        }
        if (null != mIvLogo2){
            mIvLogo2.setBackgroundResource(0);
            mIvLogo2.setImageResource(0);
        }
        String imgUrlLeft   = GlideUtil.getUrl(LauncherService.getInstance().getAdditionElementMap().get(HOME_TV_LOG_LEFT));
        String imgUrlRight  = GlideUtil.getUrl(LauncherService.getInstance().getAdditionElementMap().get(HOME_TV_LOG_RIGHT));
        SuperLog.info2SDSecurity(TAG,"HomeUrlLeft="+mImageUrl + imgUrlLeft+"\tHomeUrlRight="+mImageUrl + imgUrlRight);
        if(null != mIvLogo1) {
            if (!TextUtils.isEmpty(imgUrlLeft)) {
                mIvLogo1.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(mImageUrl + imgUrlLeft)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mIvLogo1.setImageResource(R.drawable.logo1);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(mIvLogo1);
            } else {
                mIvLogo1.setVisibility(View.GONE);
            }
        }
        if (!TextUtils.isEmpty(imgUrlRight) && null != mIvLogo2) {
            Glide.with(this)
                    .load(mImageUrl + imgUrlRight)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //mIvLogo2.setBackgroundResource(R.drawable.logo2);
                            mIvLogo2.setVisibility(View.GONE);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mIvLogo2.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(mIvLogo2);
        }else if (null != mIvLogo2){
            mIvLogo2.setVisibility(View.GONE);
        }
    }

    //优先使用Launcher栏目独立背景(imgUrl),没有则使用Launcher统一背景(mDefaultLauncherBgUrl),都没有则使用APK内置背景(R.drawable.default_detail_bg1)
    @SuppressLint("CheckResult")
    public void loadBackGroup(String imgUrl) {
        if( TextUtils.isEmpty(imgUrl) && TextUtils.isEmpty(mDefaultLauncherBgUrl)){
            //既没有栏目特定背景(imgUrl),也没有统一桌面背景(mDefaultLauncherBgUrl)都没有则使用项目里内置默认背景(本地资源)
            SuperLog.info2SDDebug(TAG,"loadBackBg___loadBackGroup()__加载默认内置图片");
            mBgUrl = "1";
            GlideUtil.loadForResourceReady(this,R.drawable.default_detail_bg1, mBackgroundImageView,-1,null);
        } else {
            String backgroundUrl = LauncherService.getInstance().getLauncherPictureLink();
            if(!TextUtils.isEmpty(imgUrl)){
                //栏目有单独配置的背景(imgUrl)优先使用
                backgroundUrl = imgUrl.contains("http") ? imgUrl : backgroundUrl + imgUrl;
            } else {
                //没有单独配置的栏目背景则使用统一桌面背景(mDefaultLauncherBgUrl)(之前已经判断过两者都为空的情况)
                backgroundUrl = backgroundUrl + mDefaultLauncherBgUrl;
            }
            mBgUrl = backgroundUrl;
            SuperLog.info2SDDebug(TAG,"loadBackBg___loadBackGroup()__imgUrl="+backgroundUrl);
            GlideUtil.loadForResourceReady(this,backgroundUrl, mBackgroundImageView,-1,null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SuperLog.debug(TAG, "onStart");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver, intentFilter);
        networkReceiver.setNetWorkStateListener(new NetworkReceiver.NetWorkStateListener() {
            @Override
            public void changeToWire() {
                mIvNetworkStatus.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.wire));
            }

            @Override
            public void changeToWifi() {
                mIvNetworkStatus.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.wifi));
            }

            @Override
            public void changeToError() {
                mIvNetworkStatus.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.wire_error));
            }
        });
    }

    private String mImageUrl = "";
    private void initImageUrl(){
        String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
        if (!TextUtils.isEmpty(launcherLink) && null != AuthenticateManager.getInstance().getUserInfo()) {
            mImageUrl = "http://" + AuthenticateManager.getInstance().getUserInfo
                    ().getIP() + ":" + AuthenticateManager.getInstance().getUserInfo()
                    .getPort() + launcherLink;
        }
    }
    public String getImageUrl(){
        return mImageUrl;
    }

    //profileType=0：删除已有的爱看界面；profileType=1：新增一个爱看界面；profileType=2：刷新已有的爱看界面。
    public void notifyDataSetChanged(int profileType){
        //如果是在儿童版或者简版不操作
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg() || SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
            return;
        }
        if (profileType == 2){
            if (null != mFragment && !mFragment.getNavId().equalsIgnoreCase(ConvertDataFromPbsToEpg.AIKAN_NAV_ID)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (OTTApplication.getContext().isFirstPower()) {
                            //小红点刷新，焦点落焦到精选，而非爱看
                            OTTApplication.getContext().setIsFirstPower(false);
                            mLauncherFocusHelper.navItemGetFocus(LauncherService.getInstance().getFirstIndexForNormal());
                        }else{
                            mLauncherFocusHelper.navItemGetFocus(LauncherService.getInstance().getFirstIndexForNormal() + 1);
                        }
                    }
                }, 300);
            }else{
                mLauncherFocusHelper.navItemGetFocus(LauncherService.getInstance().getFirstIndexForNormal() + 1);
            }
            if (ProfileManager.isAiKanFragment()){
                loadBackGroup(getPageBgUrl());
            }
        }else{
            //焦点index大于默认落焦的index,新增或删除爱看焦点要相应的+-1.
            if (currentPosition > LauncherService.getInstance().getFirstIndexForNormal()){
                if (profileType == 0){
                    currentPosition -= 1;
                }
            }

            reFreshViewPageData();

            if (null != mNavAdapter){
                mNavigates.clear();
                mNavigates.addAll(LauncherService.getInstance().getLauncher().getNavigateList());
                mNavAdapter.notifyDataSetChanged();
                //loadBackGroup(getPageBgUrl());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (profileType == 1) {
                            if (OTTApplication.getContext().isFirstPower()) {
                                OTTApplication.getContext().setIsFirstPower(false);
                                if (LauncherService.getInstance().getFirstIndex() >= 0) {//儿童版子账号开机焦点问题
                                    mLauncherFocusHelper.navItemGetFocus(LauncherService.getInstance().getFirstIndexForNormal());
                                    reFreshFragmentData(LauncherService.getInstance().getFirstIndexForNormal());
                                }
                            } else {
                                mLauncherFocusHelper.navItemGetFocus(LauncherService.getInstance().getFirstIndexForNormal() + 1);
                                reFreshFragmentData(LauncherService.getInstance().getFirstIndexForNormal() + 1);
                            }
                        } else if (profileType == 0) {
                            mLauncherFocusHelper.navItemGetFocus(currentPosition);
                            reFreshFragmentData(currentPosition);
                        }
                    }
                },300);
            }
        }

    }
    public void reFreshViewPageData(){
        if (null != mViewpager) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            List<Navigate> navigatesTem = new ArrayList<>(LauncherService.getInstance().getLauncher().getNavigateList());
            mFragmentAdapter = new PHMFragmentAdapter(fragmentManager,mViewpager,navigatesTem,"");
            mViewpager.setAdapter(mFragmentAdapter);
            mViewpager.setOffscreenPageLimit(1);
            mViewpager.addOnPageChangeListener(pageListener);
        }
    }
    private void reFreshFragmentData(int position){
        for (int i = 0;i < 3;i++){
            if (i == 0){
                position--;
            }else if (i == 2){
                position++;
            }
            if (position > -1 && null != mFragmentAdapter && position < mFragmentAdapter.getCount() && null != mFragmentAdapter.get(position)){
                PHMFragment fragmentTem = mFragmentAdapter.get(position);
                fragmentTem.resetPHMAdapter();
                fragmentTem.setGroupElementList(null);
                fragmentTem.setIsLoadData(false);
                fragmentTem.loadData();
            }
        }
    }

    /**
     * 加载顶部导航
     *
     * @param navigateList
     */
    private CommonAdapter mNavAdapter;
    private void initNav(final List<Navigate> navigateList) {
        if (mTopView.getVisibility() != View.VISIBLE) {
            mTopView.setVisibility(View.VISIBLE);
        }
        currentPosition = LauncherService.getInstance().getFirstIndex();
        showManualRefresh();
        mNavigates.clear();
        //mNavigates = new ArrayList<>(navigateList);
        mNavigates.addAll(navigateList);
        mNavAdapter = new CommonAdapter<Navigate>(this, R.layout.item_launcher_nav, navigateList) {
            @Override
            protected void convert(ViewHolder holder, Navigate navigate, int position) {

                holder.setIsRecyclable(false);

                TextView tvTitle = holder.getView(R.id.tv_nav_title);
                ImageView imageView = holder.getView(R.id.im_nav_title);
                RelativeLayout rlNav = holder.getView(R.id.rl_nav);
                LinearLayoutExt llNavTitleLine = holder.getView(R.id.ll_nav_title_line);
                ImageViewExt imNavTitleLine = holder.getView(R.id.im_nav_title_line);

                //有设置tab的背景图片，则不显示tab title,只显示背景图片
                if (!TextUtils.isEmpty(navigate.getImage()) && !TextUtils.isEmpty(navigate.getFocusImage()) && !TextUtils.isEmpty(navigate.getSecondaryTitleImg())) {
                    tvTitle.setVisibility(View.INVISIBLE);
                    if (LauncherService.getInstance().getFirstIndex() == currentPosition && currentPosition == position && mNavRecyclerView.hasFocus()) {
                        mLauncherFocusHelper.loadNavImgBg(position, imageView, tvTitle, 1,rlNav);
                    } else {
                        mLauncherFocusHelper.loadNavImgBg(position, imageView, tvTitle, 0,rlNav);
                    }
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                }
                if (null != navigate.getNameDialect() && navigate.getNameDialect().size() > 0) {
                    tvTitle.setText(navigate.getNameDialect().get(0).getValue());

                    tvTitle.setTextColor(Utils.getNavTitleColorNormal(position));

                    //设置导航栏文字下划线
                    Paint paint = new Paint();
                    paint.setTextSize(tvTitle.getTextSize());
                    float size = paint.measureText(tvTitle.getText().toString());
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llNavTitleLine.getLayoutParams();
                    layoutParams.width = (int) size;
                    llNavTitleLine.setLayoutParams(layoutParams);

                    //设置导航栏问题背景色
                    imNavTitleLine.setBackground(Utils.getNavTitleShape(mContext,position));
                    Utils.setNavTitleBg(mContext,tvTitle,Utils.getNavTitleShape(mContext,position));
                }
            }
        };

        mNavAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mWelcomeRelativeLayiout.getVisibility() != View.VISIBLE) {
                    //栏目跳转到列表页
                    String actionUrl = mNavigates.get(position).getActionURL();
                    if (TextUtils.isEmpty(actionUrl)) {
                        return;
                    }
                    String type = ZJVRoute.getContentValue(actionUrl,ZJVRoute.ActionUrlKeyType.TYPE);
                    String key = ZJVRoute.getContentValue(actionUrl,ZJVRoute.ActionUrlKeyType.KEY);
                    Map<String, Element> elementMap = LauncherService.getInstance().getnavApkExtraDataMap();
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase(ZJVRoute.LauncherElementContentType.APK)
                            && !TextUtils.isEmpty(key) && null != elementMap.get(key)){
                        if (null != elementMap.get(key).getElementDataList() && elementMap.get(key).getElementDataList().size() > 0
                                && !TextUtils.isEmpty(elementMap.get(key).getElementDataList().get(0).getElementAction().getActionURL())){
                            ZJVRoute.route(MainActivity.this, ZJVRoute.LauncherElementDataType
                                            .STATIC_ITEM, elementMap.get(key).getElementDataList().get(0).getElementAction().getActionURL(),
                                    null, null, null, elementMap.get(key).getExtraData());
                        }
                    }else{
                        ZJVRoute.route(MainActivity.this, ZJVRoute.LauncherElementDataType
                                .STATIC_ITEM, actionUrl, null, null, null, null);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mNavRecyclerView.setVisibility(View.VISIBLE);
        mNavRecyclerView.setAdapter(mNavAdapter);
    }

    private void setChildrenAndSimpleNav(List<Navigate> navigateList) {

        Navigate simpleEpgNav=null;
        Navigate childEpgNav=null;
        Navigate fourKNav=null;

        //for循环导航栏navigateList，定位出简版、儿童版、4K专区
        if (null != navigateList && navigateList.size() > 0){
            for (int i = 0; i < navigateList.size(); i++){
                String id = navigateList.get(i).getId();
                if (!TextUtils.isEmpty(id)){
                    if (id.equalsIgnoreCase(LauncherService.getInstance().getNavIdSimpleEpg())){
                        simpleEpgNav = navigateList.get(i);
                    }else if (id.equalsIgnoreCase(LauncherService.getInstance().getNavIdChildrenEpg())){
                        childEpgNav = navigateList.get(i);
                    }else if (id.equalsIgnoreCase(LauncherService.getInstance().getNavId4K()) && !VodUtil.isDeviceSupport4K()){
                        fourKNav = navigateList.get(i);
                    }
                }
            }
            //移除Tab导航栏中简版epg
            if (null != simpleEpgNav){
                navigateList.remove(simpleEpgNav);
                LauncherService.getInstance().setNavSimpleEpg(simpleEpgNav);
            }
            //移除Tab导航栏中儿童版epg
            if (null != childEpgNav){
                navigateList.remove(childEpgNav);
                LauncherService.getInstance().setNavChildrenEpg(childEpgNav);
            }
            //设备不支持4K，移除Tab导航栏中4K专区页面
            if (null != fourKNav){
                navigateList.remove(fourKNav);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (mWelcomeRelativeLayiout.getVisibility() != View.VISIBLE) {
            switch (view.getId()) {
                case R.id.iv_search:
                case R.id.iv_simple_search://搜索按钮
                    //从首页进入搜索上报由搜索页面处理
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_main_refresh_btn://刷新按钮
                    if (null != mManualRefreshIm){
                        mManualRefreshIm.setVisibility(View.GONE);
                    }
                    UBDTopFunction.reportTopFunction(mManualRefreshBtn.getText().toString(), TopFunctionData.BTN_NORMAL);
                    //focusManageEpgTop(false);//解决点击刷新按钮，焦点会先移到跑马灯再移动到精选页
                    onEvent(new RefreshLauncherEvent(true));
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SuperLog.debug(TAG, "onPause");
        disableKeyCode = true;
        UBDSwitch.getInstance().reportToOtherApk();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWelcomeRelativeLayiout.getPlayLayout().getVisibility() == View.VISIBLE) {
            mWelcomePlayViewCurrentPosition = mWelcomeRelativeLayiout.getPlayView().getCurrentPosition();
            mWelcomeRelativeLayiout.getPlayView().stopPlayback();
        }
    }

    /**
     * 从presenter返回的launcher.json的数据模板
     * queryLauncher成功后回调此方法,通知MainActivity登录成功,可以开始渲染主页面
     *
     * @param navigates
     * @param isFirstLoad
     */
    @Override
    public void loadLauncherData(List<Navigate> navigates, boolean isFirstLoad) {
        SuperLog.info2SD(TAG, "<<<<<<<<<< loginAndLoadLauncher from server successfully >>>>>>>>>> begin to load UI data locally");
        UBDSwitch.getInstance().setNavPosition(LauncherService.getInstance().getFirstIndex());
        if (isLoadLauncherSuccess) {
            return;
        }

        updateLauncherLink();
        updateFocusColor();
        setEpgTopFocusColor();

        if (navigates == null) {
            navigates = new ArrayList<>();
        }
        //加载背景
        mDefaultLauncherBgUrl = GlideUtil.getUrl(LauncherService.getInstance().getLauncher().getDefaultBackground());
        loadHomeTvLogo();
        mRelaNavForTop.setVisibility(View.VISIBLE);
        //先展示launcher默认背景
        loadBackGroup(mDefaultLauncherBgUrl);
        setChildrenAndSimpleNav(navigates);
        //如果处于老人桌面模式，开始加载老人桌面的内容
        if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()) {
            SuperLog.info2SD(TAG, "last saved launcher is simpleEpg, begin to load data");
            loadSimpleEpgData(true);
            return;
        }
        //如果处于少儿动漫桌面模式，开始加载少儿动漫的内容
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            SuperLog.info2SD(TAG, "last saved launcher is childrenEpg, begin to load data");
            loadChildrenEpgData(true);
            return;
        }
        UBDDesktopType.record("normal");
        mRelaNavForTop.setVisibility(View.VISIBLE);
        mRiSearch.setVisibility(View.VISIBLE);
        //如果当前加载过简版epg先移除,然后置空
        if (null != simpleEpgFragment) {
            getSupportFragmentManager().beginTransaction().remove(simpleEpgFragment).commitAllowingStateLoss();
            simpleEpgFragment = null;
        }
        //如果当前加载过少儿动漫epg先移除,然后置空
        if (null != childrenEpgFragment) {
            getSupportFragmentManager().beginTransaction().remove(childrenEpgFragment).commitAllowingStateLoss();
            childrenEpgFragment = null;
        }
        initNav(navigates);
        initFragments(navigates);
        needShowStartPic = isFirstLoad;
        switchLauncher();
        //设置加载成功标志位
        loadLauncherSuccess(true);
        SuperLog.info2SD(TAG, "[LOGIN-13(Maybe in switchLauncher)]loadLauncher finished. Set flag [isLoadLauncherSuccess] = success");
        SuperLog.info2SD(TAG, "[(===End===)LOGIN-14(Maybe in switchLauncher)]Login process finished. But UI will be loaded later when you see the first <queryEpgHomeVod> response because of it is a asynchronous action.");
    }

    public void resetMarginForTopView(int dy){
        try {
            if (null != mFragmentAdapter && currentPosition < mFragmentAdapter.size()){
                PHMFragment tabItemFragment = mFragmentAdapter.get(currentPosition);
                if (dy == 0 && tabItemFragment.getmRvScrollY() == -1){
                    tabItemFragment.setmRvScrollY(dy);
                }else if (dy == -1){
                    tabItemFragment.setmRvScrollY(dy);
                }
            }
        }catch (Exception e){
            SuperLog.error(TAG, e);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  //返回键时焦点向上
            if (isBackFromProfile){
                return true;
            }
            //本地播放，跳第三方软件，我们APP也会响应返回事件，得屏蔽掉
            if (OTTApplication.isApplicationBroughtToBackground(getApplicationContext()) || OTTApplication.getContext().getIsJumpToThirdApp()) {
                OTTApplication.getContext().setIsJumpToThirdApp(false);
                return true;
            }
            //在老人桌面不处理返回按键
            if (null != simpleEpgFragment) {
                return true;
            }
            //在少儿动漫桌面不处理返回按键
            if (null != childrenEpgFragment) {
                return true;
            }
            if (!hasFocusTvEpgScrollAds() && !hasFocusLinearContent() && !hasFocusEpgTopProfile()){
                focusManageEpgTop(false);
            }
            onBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private int keyCode=KeyEvent.KEYCODE_1;
    public int getKeyCode(){
        return keyCode;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.keyCode = keyCode;
        if (isBackFromProfile){
            isBackFromProfile = false;
        }

        if (isFreshingPbs){
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && mProfileAliasTextBg.hasFocus()){
            //解决从首页进入到profileH5选择界面再返回，焦点在用户昵称上按左键焦点越过功能键落焦到跑马灯
            focusManageEpgTop(true);
        }

        resetMarginForTopView(0);
        mNavFoucsKeyEvent = keyCode;
        if (disableKeyCode || mWelcomeRelativeLayiout.getVisibility() == View.VISIBLE) {

            //摁返回键时
            if (keyCode == KeyEvent.KEYCODE_BACK){
                //如果在正在加载广告
                if ( mWelcomeRelativeLayiout.isLoadAd() && mWelcomeRelativeLayiout.isCanSkip()){
                    //如果是图片广告
                    if (mWelcomeRelativeLayiout.getShowType().equals(mWelcomeRelativeLayiout.TYPE_DISPLAY)){
                        mWelcomeRelativeLayiout.complete();
                        mWelcomeRelativeLayiout.clearUI();
                    }
                    //如果是视频广告
                    else if (mWelcomeRelativeLayiout.getPlayView().getVisibility() == View.VISIBLE &&mWelcomeRelativeLayiout.getShowType().equals(mWelcomeRelativeLayiout.TYPE_VIDEO))
                    {
                        mBackgroundImageView.setVisibility(View.VISIBLE);
                        mWelcomeRelativeLayiout.getPlayView().setBackground(getResources().getDrawable(R.drawable.transparent_drawable));
                        mWelcomeRelativeLayiout.loadDrawable(getResources().getDrawable(R.drawable.transparent_drawable));
                        mWelcomeRelativeLayiout.getPlayView().setVisibility(View.GONE);
                        mWelcomeRelativeLayiout.getPlayView().setAlpha(0);
                        mWelcomeRelativeLayiout.getPlayLayout().setAlpha(0);
                        mWelcomeRelativeLayiout.getPlayView().stopPlayback();
                        mWelcomeRelativeLayiout.clearUI();

                        mainHandler.sendEmptyMessage(LOAD_COMPLETE);
                        if (null != simpleEpgFragment && isStartPicFinished) {
                            simpleEpgFragment.resume();
                        }
                    }
                }else{
                    return true;
                }
            }
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            boolean isWelcomeLayoutGone = mWelcomeRelativeLayiout.getVisibility() == View.GONE;
            if (isWelcomeLayoutGone) {
                ManualRefreshDialog manualRefreshDialog = new ManualRefreshDialog(this);
                manualRefreshDialog.show();
                EventBus.getDefault().post(new ShimmerCloseEvent());
            }
        }

        //首页手动刷新和TAB栏丢失和获取焦点处理
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && !hasFocusTvEpgScrollAds() && !hasFocusLinearContent()  && (mNavRecyclerView.hasFocus() || mIvSearch.hasFocus())) {
            if (mIvSearch.hasFocus()){
                currentPosition = -1;
            }
            focusManageEpgTop(true);
        }

        //【0720】【跑马灯】落焦到简版、儿童版跑马灯，按上键跳转到切换账号
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && (hasFocusEpgTopProfile() || hasFocusLinearContent() || hasFocusTvEpgScrollAds())){
            return true;
        }

        //BTV/TVOD等快捷键事件
        //TODO 没有考虑一切特殊页面需要屏蔽这些案件的处理 如节点指引页面,广告页面等
        int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
        if (codeValue == RemoteKeyEvent.BTV && OTTApplication.getContext().isLoginSuccess() &&
                OTTApplication.getContext().isLoadLauncherSuccess()) {
            //需要等待launcher渲染出来之后才允许点击
            //直播
            if (isLauncher == 1) {
                switchLiveTV(false);
            }
        } else if (codeValue == RemoteKeyEvent.TVOD && OTTApplication.getContext().isLoginSuccess()
                && OTTApplication.getContext().isLoadLauncherSuccess()) {
            //需要等待launcher渲染出来之后才允许点击
            //回看(点播)
            if (isLauncher == 1) {
                switchLiveTV(true);
            }
        }

        //儿童版焦点下移后再上移按上键无法落焦到功能键跑马灯
        if (null != childrenEpgFragment && keyCode == KeyEvent.KEYCODE_DPAD_UP && mTopView.getVisibility() == View.VISIBLE){
            View nextFocusView = FocusFinder.getInstance().findNextFocus(mContainerLayout, childrenEpgFragment.getRecyclerViewTV().findFocus(), View.FOCUS_UP);
            if (null != nextFocusView){
                nextFocusView.requestFocus();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private String getPageBgUrl() {
        String backGroundUrl = null;
        if (!CollectionUtil.isEmpty(mNavigates) && currentPosition < mNavigates.size() && currentPosition >= 0) {
            List<Page> pages = mNavigates.get(currentPosition).getPageList();
            if (!CollectionUtil.isEmpty(pages)) {
                backGroundUrl = pages.get(0).getBackground();
            }
        }
        SuperLog.info2SDDebug(TAG, "currentPosition:" + currentPosition + ", backGroundUrl:" + backGroundUrl);
        return backGroundUrl;
    }

    public String getBgUrl(){
        return mBgUrl;
    }

    @Subscribe
    public void eventDispatch(String s) {
        switch (s) {
            case Constant.SCROLL_COMPLETE:
                mLauncherFocusHelper.drawFocusEffect();
                break;
        }
    }

    @Subscribe
    public void eventStartHeart(StartHeartBitEvent startHeartBitEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startHeartBitService();
            }
        });

    }

    @Subscribe
    public void onEvent(ShowRefreshNotifyEvent event){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != mManualRefreshIm){
                    mManualRefreshIm.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(RefreshLauncherEvent event) {
        if (!event.isMainActivity()) return;
        if(BuildConfig.DEBUG){
            EpgToast.showLongToast(this, RefreshManager.TAG + " | Update Desktop now......");
        }
        currentPositionTemple = -1;

        if (RefreshManager.getInstance().needRefresh()) { //如果首页需要更新
            SuperLog.info2SD(TAG, "need to update launcher!! ");
            focusManageEpgTop(false);
            mIvSearch.setFocusable(false);
            launcherUpdate(false);
        }else{
            if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()) {
                LauncherService.getInstance().refreshLauncher(this,refreshListener,true);
            } else if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                //清除焦点效果
                mLauncherFocusHelper.clearFocusEffect();
                LauncherService.getInstance().refreshLauncher(this,refreshListener,true);
            } else {
                if (currentPosition < 0){
                    currentPosition = 0;
                }
                //mViewpager.setCurrentItem(currentPosition);
                if (currentPosition < mFragmentAdapter.size()) {
                    PHMFragment tabItemFragment = mFragmentAdapter.get(currentPosition);
                    //3个小时内没有加载过数据才重新请求
                    if (tabItemFragment instanceof MyPHMFragment) {
                        //我的页面每次切换都需要重新请求收藏和历史
                        if (currentPosition == 0) ((MyPHMFragment) tabItemFragment).scrollToTop();
                    } else {
                        if (currentPosition == 0) tabItemFragment.scrollToTop();
                    }
                    LauncherService.getInstance().refreshLauncher(this,refreshListener,true);
                }
            }
        }
    }

    private final RefreshListener refreshListener = new RefreshListener();

    private class RefreshListener implements LauncherService.OnQueryLauncherListener {
        @Override
        public void onVersionChange(boolean change,boolean isOnlyRefreshPbsEpg,boolean isEpgRefresh) {
            setIsFreshingPbs(false);
            if (isOnlyRefreshPbsEpg){
                if (null != mFragment && ProfileManager.isAiKanFragment()){
                    //当前如果是在爱看界面，即执行刷新爱看界面，，将刷新爱看的flag设置false
                    OTTApplication.getContext().setIsRefreshPbsEpg(false);
                    //只刷新Pbs爱看界面
                    mFragment.clearData();
                    if (!mIsChangeTab){
                        mFragment.scrollToTop();
                        onBack();
                    }
                }
            }else if (isEpgRefresh && ProfileManager.isAiKanFragment()){
                //在爱看页面点击刷新，重查爱看界面的数据
                OTTApplication.getContext().setIsRefreshPbsEpg(true);
                refreshPbsEpg(mNavRecyclerView.hasFocus() ? true : false);
            }else if (isEpgRefresh && (null != mFragment || null != childrenEpgFragment) && !change){
                refreshEpgNoNeedUpdateLauncher();
            }else if(!change) {
                SuperLog.debug(TAG,"[refreshLauncher] version no change, load local json");
                //无需下载，重新加载一次本地json
                if (!SharedPreferenceUtil.getInstance().getIsChildrenEpg() && !SharedPreferenceUtil.getInstance().getIsSimpleEpg()) {
                    //TODO
                    if (currentPosition < 0){
                        currentPosition = 0;
                    }
                    if (mFragmentAdapter.size() > 0){
                        PHMFragment tabItemFragment = mFragmentAdapter.get(currentPosition);
                        if (!(tabItemFragment instanceof MyPHMFragment)) {
                            if (null != tabItemFragment.getPresenter()) {
                                tabItemFragment.getPresenter().resetOffSet();
                            }
                        }
                    }
                }
                setIsLoadLauncherSuccess(false);
                loadLauncherData(LauncherService.getInstance().getLauncher().getNavigateList(), false);
            }
        }
    }

    @Override
    protected void initPresenter() {
        presenter = new LauncherPresenter();
    }


    @Override
    /**
     * 重新认证
     */ public void reAuthenticate() {
        SuperLog.info2SD(TAG, "[MainActivity]Begin to reAuthenticate");
        if (isLoadLauncherSuccess) {
            SuperLog.info2SD(TAG, "isLoadLauncherSuccess=true, reAuthenticate()");
            super.reAuthenticate();
        } else {
            SuperLog.info2SD(TAG, "isLoadLauncherSuccess=false, loginAndLoadLauncher()");
            presenter.loginAndLoadLauncher(this);
        }
    }

    /**
     * 注册网络状态监听
     */
    private void registerNetWorkListener() {
        networkReceiver = new NetworkReceiver();
    }


    /**
     * 注册tokenReceiver
     */
    private void registerTokenReceiver() {
        if (mTokenReceiver == null) {
            mTokenReceiver = new TokenReceiver(this);
        }
        IntentFilter intentFilter = new IntentFilter();
        // 为BroadcastReceiver指定action，即要监听的消息名字。
        intentFilter.addAction(TokenManager.TOKEN_ACTION);
        registerReceiver(mTokenReceiver, intentFilter);
    }

    /**
     * 注销广播等资源
     */
    private void unRegister() {
        SuperLog.debug(TAG, "unRegister");
        if (mTokenReceiver != null) {
            unregisterReceiver(mTokenReceiver);
        }
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }

    //接收到chinamobile的Token更新广播
    @Override
    public void onStateChange(String action, Intent intent) {
        //接收第三方中间件广播后，重新认证
        String status = intent.getStringExtra("status");
        SuperLog.info2SD(TAG, "Token updated, begin to re-Authenticate. Status=" + status);
        //收到广播后不需要继续定时请求Token
        AuthenticateManager.getInstance().cancelReportToken();
        if ("online".equals(status)) {
            UserInfo oldUserInfo = AuthenticateManager.getInstance().getUserInfo();
            UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
            if (null != oldUserInfo && null != userInfo && !oldUserInfo.getUserId().equals(userInfo.getUserId())) {
                //切换账号并且换用户重新登陆
                SuperLog.info2SD(TAG, "UserID changed after switch user.");
                //解决切换账号，我的页面没有走onPause方法，导致一直在播放的情况
                if (null != mViewpager) {
                    EventBus.getDefault().post(new ForceLifecycleEvent(ForceLifecycleEvent.PAUSE));
                    mFragmentAdapter = new PHMFragmentAdapter(getSupportFragmentManager(), mViewpager, new ArrayList<Navigate>(),  "");
                    mViewpager.setAdapter(mFragmentAdapter);
                }
                SharedPreferenceUtil.getInstance().clearCache();
                OTTApplication.getContext().setFavoCatalogID(null);
                OTTApplication.getContext().setChildVodDetailAdvertisementURL(null);
                mContainerLayout.setVisibility(View.INVISIBLE);
                mBackgroundImageView.setVisibility(View.INVISIBLE);
                initStartupAdView();
                isLoadLauncherSuccess=false;
                isLoadAdvertCompleted=false;
                needShowStartPic=false;
                reAuthenticateAfterSwitch();//原值isReport=false
            } else {
                //1.未做操作收到广播
                //2.切换账号但是没有换用户重新登陆
                //3.开机直接获取TOKEN失败,重新请求Token
                reAuthenticate();
            }
        } else {
            //弹出认证界面时不弹出网络断开提示对话框，防止EPGAPK的界面覆盖认证登录页面导致用户无法输入用户名密码
            OTTApplication.getContext().setNeedShowNetworkExceptionDialog(false);
            SuperLog.error(TAG, "LoginStatus=offline. Begin to open CMCC [login] UI.");
            intent = new Intent("com.chinamobile.middleware.auth.loginui");
            intent.putExtra("type", "jar");
            startActivity(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SuperLog.debug(TAG, "onRestart");
        if (mWelcomeRelativeLayiout.getPlayLayout().getVisibility() == View.VISIBLE && mWelcomeRelativeLayiout.getVisibility() == View.VISIBLE) {
            mWelcomeRelativeLayiout.getPlayView().setVisibility(View.VISIBLE);
            mWelcomeRelativeLayiout.getPlayView().seekTo(mWelcomePlayViewCurrentPosition);
            mWelcomeRelativeLayiout.getPlayView().start();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SuperLog.debug(TAG, "onNewIntent");
        //双堆栈的问题
        if (this != OTTApplication.getContext().getMainActivity()) {
            SuperLog.info2SD(TAG, "删除当前mainActivity[双首页] MainActivity has been finished.");
            this.finish();
            Activity oldMainActivity = OTTApplication.getContext().getMainActivity();
            if (null != oldMainActivity ) {
                SuperLog.info2SD(TAG, oldMainActivity + " MainActivity has been finished.");
                oldMainActivity.finish();
                ActivityStackControlUtil.finishProgram();
            }
        }

        //是加载桌面请求
        if(intent.getBooleanExtra(RefreshManager.TAG,false)){
            onEvent(new RefreshLauncherEvent(true));
            return;
        }

        DesktopType type = (DesktopType) intent.getSerializableExtra("EPGMode");
        if(type != null){
            switchLauncher(type);
            return;
        }
    }

    @Subscribe
    public void onEvent(NavFocusEvent event) {
        if (currentPosition == event.getIndex()) {
            mLauncherFocusHelper.navItemGetFocus(event.getIndex());
        }
    }

    /**
     * 获取首页导航栏数据
     *
     * @return 导航栏集合
     */
    public List<Navigate> getmNavigates() {
        return mNavigates;
    }

    public void setIsFirstShowFocus(boolean isFirstShowFocus){
        this.isFirstShowFocus = isFirstShowFocus;
    }
    public boolean getIsFirstShowFocus(){
        return this.isFirstShowFocus;
    }

    //点击资源位跳转到相应的导航栏
    public void navItemGetFocus(String navId){
        int idIndexForNavs = getNavIdIndexForNavs(navId);
        if (idIndexForNavs != -1){
            if (null != mFragment){
                mFragment.scrollToTop();
            }
            resetMarginForTopView(-1);
            mLauncherFocusHelper.navItemGetFocus(idIndexForNavs);
        }
    }

    public void requestNavItemFocus(){
        View childAt = mNavRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
        if (childAt != null){
            childAt.requestFocus();
        }
    }

    private int getNavIdIndexForNavs(String navId){
        for (int i = 0;i < mNavigates.size();i++){
            if (mNavigates.get(i).getId().equalsIgnoreCase(navId)){
                return i;
            }
        }
        SuperLog.debug(TAG,"NavIdIndexForNavs = -1,navId="+navId);
        return -1;
    }

    private void updateLauncherLink() {
        SuperLog.info2SD(TAG, "updateLauncherLink--->  LauncherNewLink=" + SharedPreferenceUtil.getInstance().getLauncherNewLink() + ";LauncherOldLink=" + SharedPreferenceUtil.getInstance().getLauncherLink());
        if (!SharedPreferenceUtil.getInstance().getLauncherNewLink().equals(SharedPreferenceUtil.getInstance().PHM_LAUNCHER_LINK)
                && !SharedPreferenceUtil.getInstance().getLauncherNewLink().equals(SharedPreferenceUtil.getInstance().getLauncherLink()))
            SharedPreferenceUtil.getInstance().saveLauncherLink(SharedPreferenceUtil.getInstance().getLauncherNewLink());
    }

    @Override
    public void loadAdvertContentSuccess( List<AdvertContent> advertContent ) {

        //防止重复播放广告
        if (AdManager.getInstance().isPlayComplete()){
            mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
            SuperLog.debug(AdConstant.TAG, "Play SSP AD already complete");
            return;
        }

        //先进行升级的流程
        UpgradeManager.upgrade(MainActivity.this, new IUpgradeListener() {
            @Override
            public void onFinish() {
                SuperLog.info2SD(TAG,"Upgrade operation finished. Back to MainActivity and continue");
                if(CollectionUtil.isEmpty(advertContent)){
                    mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
                }else{
                    if (AdManager.getInstance().isPlayComplete()){
                        mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
                    }else{
                        //防止重复播放广告
                        if (AdManager.getInstance().isPlayComplete()){
                            mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
                            SuperLog.debug(AdConstant.TAG, "Play SSP AD already complete");
                        } else {
                            //加载广告(视频/图片)
                            mWelcomeRelativeLayiout.loadAd(advertContent);
                        }
                    }
                }
            }

            @Override
            public void onOptionalUpgrade() { }
        });
    }

    @Override
    public void loadAdvertFail() {
        //先进行升级的流程
        UpgradeManager.upgrade(MainActivity.this, new IUpgradeListener() {
            @Override
            public void onFinish() {
                SuperLog.info2SD(TAG,"Upgrade operation finished. Back to MainActivity and continue");
                mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
            }

            @Override
            public void onOptionalUpgrade() { }
        });
    }

    //启动界面切换到首页
    public void switchLauncher(){
        SuperLog.info2SD(TAG,">>>Login and get launcher flow has finished : " + needShowStartPic + "    >>>SSP Advert flow : " + isLoadAdvertCompleted);
        if (needShowStartPic&&isLoadAdvertCompleted) {
            isLoadAdvertCompleted=false;
            needShowStartPic=false;
            mainHandler.sendEmptyMessage(LOAD_COMPLETE);
        }
    }

    public LauncherFocusHelper getLauncherFocusHelper(){
        return mLauncherFocusHelper;
    }

    //修复二级界面按首页，java.lang.IllegalStateException:crash问题
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) { }

    //显示手动刷新按钮
    private void showManualRefresh(){
        if (null == mManualRefreshBtn || null == mManualRefreshRl || null == mManualRefreshIm){
            return;
        }
        mManualRefreshRl.setVisibility(View.VISIBLE);
        mManualRefreshBtn.setVisibility(View.VISIBLE);
        if (SharedPreferenceUtil.getInstance().getLauncherUpdate() && isStartPicFinished){
            mManualRefreshIm.setVisibility(View.VISIBLE);
        }else{
            mManualRefreshIm.setVisibility(View.GONE);
        }
    }

    //隐藏顶部跑马灯及功能键
    private void resetTopScrollAdsAndFunctionMenu(){
        if (null != mLinearContent){
            mLinearContent.removeAllViews();
        }
        if (null != mTvEpgScrollAds){
            mTvEpgScrollAds.setVisibility(View.GONE);
        }
    }

    private void showTopScrollAdsAndFunctionMenu(){
        if (null != mLinearContent){
            mLinearContent.setVisibility(View.VISIBLE);
        }
        if (null != mTvEpgScrollAds && !TextUtils.isEmpty(mTvEpgScrollAds.getText())){
            mTvEpgScrollAds.setVisibility(View.VISIBLE);
        }
    }

    public void setIsLoadLauncherSuccess(boolean isLoadLauncherSuccess){
        this.isLoadLauncherSuccess = isLoadLauncherSuccess;
    }

    public boolean hasFocusTvEpgScrollAds() {
        return (null != mTvEpgScrollAds && mTvEpgScrollAds.hasFocus());
    }
    public boolean hasFocusLinearContent() {
        return (null != mLinearContent && mLinearContent.hasFocus());
    }

    public boolean hasFocusEpgTopProfile() {
        if (null != mSwitchProfileBtn && mSwitchProfileBtn.hasFocus()){
            return true;
        }else if (null != mProfileAliasTextBg && mProfileAliasTextBg.hasFocus()){
            return true;
        }
        return false;
    }

    public RelativeLayout getTopView(){
        return mTopView;
    }

    public boolean isMain(){
        return (null == childrenEpgFragment && null == simpleEpgFragment);
    }

    public boolean isChildrenEpgFragment(){
        return null == childrenEpgFragment ? false : true;
    }

    //当前界面显示区域是否含有视频播放窗口
    public boolean isShowVideoView(){
        if (null != childrenEpgFragment){
            return childrenEpgFragment.isShowVideoView();
        }else if (null != mFragment){
            return mFragment.isShowVideoView();
        }
        return false;
    }

    private void refreshEpgNoNeedUpdateLauncher(){
        if (!mNavRecyclerView.hasFocus()){
            onBack();
        }

        if (null != mFragment){
            mFragment.setRecycleViewRefresh();
        }else if (null != childrenEpgFragment){
            childrenEpgFragment.setRecycleViewRefresh();
        }
    }
}