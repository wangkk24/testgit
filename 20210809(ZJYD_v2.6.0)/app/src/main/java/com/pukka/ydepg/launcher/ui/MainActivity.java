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

    public static final String HOME_TV_LOG_LEFT        = "home_tv_logo_left";  //?????????????????????logo
    public static final String HOME_TV_LOG_RIGHT       = "home_tv_logo_right"; //????????????????????????logo
    public static final String SUB_HOME_TV_LEFT_LOGO   = "sub_home_left_logo"; //??????????????????logo
    public static final String SUB_HOME_TV_RIGHT_LOGO  = "sub_home_right_logo"; //??????????????????logoS
    public static final String SUB_HOME_TV_MIDDLE_LOGO = "sub_home_middle_logo"; //??????????????????logo

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

    ImageView         mBackgroundImageView;//????????????
    TextViewExt       mManualRefreshBtn;//????????????????????????
    ImageViewExt      mManualRefreshIm;//????????????????????????
    RelativeLayoutExt mManualRefreshRl;//????????????????????????

    MarqueeEpgTextView mTvEpgScrollAds;//????????????????????????tv

    LinearLayoutExt mLinearContent;//???????????????content group

    TextViewExt mSwitchProfileBtn;//??????Profile??????
    private ImageView   mProfileIcon;//Profile??????
    private TextViewExt mProfileAliasText;//Profile??????
    public RelativeLayout mProfileAliasTextBg;//Profile????????????
    RelativeLayout mProfile;//Profile??????UI

    boolean searchIvHasFocus = false;

    protected MonIndicator mLoadingBar;
    private boolean needShowStartPic;

    /*??????????????????????????????*/
    public final static boolean IS_SCROLL_NAVIGATION_MODE = true;
    /*???????????????View??????*/
    private RelativeLayout mTopView;
    private int mWelcomePlayViewCurrentPosition = 0;
    private boolean isVideoStart;
    private int mFirstIndex;




    PHMFragment mFragment;//??????tag????????????fragment?????????-1???????????????????????????????????????onpause????????????release?????????

    public PHMFragment getCurrentFragment(){
        return mFragment;
    }

    PHMFragmentAdapter mFragmentAdapter;
    ViewPagerFocusExt mViewpager;





    SimpleEpgFragment simpleEpgFragment;
    PHMFragment childrenEpgFragment;

    List<Navigate> mNavigates = new ArrayList<>();
    private LauncherFocusHelper mLauncherFocusHelper;
    protected int currentPosition;  //?????????position

    protected int currentPositionTemple;  //??????????????????QueryEpgHome???tab position
    protected int mNavFoucsKeyEvent;  //??????????????????QueryEpgHome???tab position

    //????????????????????????????????????????????????????????????
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

    //????????????????????????
    private boolean isLoadAdvertCompleted;

    private boolean isStartPicFinished = false;//????????????????????????

    private String mBgUrl;  //????????????????????????

    //????????????
    public WelcomeRelativeLayout mWelcomeRelativeLayiout;

    //?????????????????????profile???????????????????????????profile??????????????????????????????onKeyUp??????onback??????????????????????????????????????????
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

                    //??????????????????????????????UBD??????????????????From MainActivity_AD to MainActivity_navID
                    UBDSwitch.getInstance().reportStartupInMainActivity(LauncherService.getInstance().getFirstIndex());

                    if(!SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                        ReminderService.getmInstance().checkBootStrapAlertReminder(MainActivity.this);
                    }

                    //??????????????????????????????????????????????????????touch mode??????
                    simulateKeystroke(KeyEvent.KEYCODE_SLASH);

                    //?????????????????????????????????
                    setFocus();

                    LiveDataHolder.get().setIsShowingSkip(false);

                    //??????300ms??????????????????setFocus()???????????????????????????300ms,???????????????????????????
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

                    //????????????????????????????????????????????????????????????????????????????????????
                    if (null != simpleEpgFragment) {
                        simpleEpgFragment.resume();
                    }
                    //??????????????????,????????????
                    //mWelcomeRelativeLayiout.setVisibility(View.GONE);

                    //????????????????????????
                    new EpgGuideTool().showGuidePicture(MainActivity.this,()->{
                        if(ProfileManager.isChangeUser){
                            //??????/????????????????????????   ???????????????????????????,??????Profile??????H5??????
                            ProfileManager.getStartupProfileStatus(MainActivity.this);
                            //H5???????????????Profile???,???????????????onActivityResult??????????????????
                        } else {
                            onProfileUIFinished(null);
                        }
                    });
                    break;
                case CLOCK_SWITCH:{
                    //??????5???????????????
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

    //H5???????????????Profile???,?????????????????????????????????,???????????????????????????,?????????onResume()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PROFILE_REQUEST_CODE && resultCode == PROFILE_RESULT_CODE){
            isBackFromProfile = true;
            SuperLog.info2SD(ProfileManager.TAG,"Profile WebActivity closed and back to MainActivity.");
            //??????Profile??????
            ProfileManager.updateProfileAlias(mProfileAliasText,mProfileIcon);
            //?????????Profile???????????????????????????
            ProfileManager.getSubProfileSubjectData(refreshListener,false);
            onProfileUIFinished(true);
        }
    }

    // ????????????,?????????Profile????????????????????????????????????,??????????????????????????????????????????????????????
    // 1 ???????????????Profile????????????,???????????????
    // 2 ?????????????????????Profile????????????,????????????
    public void onProfileUIFinished(Boolean isShowProfile){
        //??????????????????????????????????????????????????????
        initFunctionMenu();
        //????????????????????????,?????????????????????????????????,??????????????????????????????
        mWelcomeRelativeLayiout.setVisibility(View.GONE);
        if(!ProfileManager.isProfileFinished && isShowProfile!=null){//?????????????????????isProfileFinished)???true,???????????????if????????????
            SuperLog.info2SD(TAG,"onProfileUIFinished started!(This method should only be executed once when STB startup)");
            findViewById(R.id.profile).setVisibility(isShowProfile?View.VISIBLE:View.GONE);
            ProfileManager.isProfileFinished = true;
            //??????????????????????????????????????????????????????????????????????????????
            MessageDataHolder.get().setIsComplete(true);
            PushMessagePresenter.showXmppMessage();
            //?????????????????????????????????,????????? ????????????/??????????????????????????????
            RefreshManager.getInstance().startRefreshTimer();
            SuperLog.info2SD(TAG,"Profile step finished. All startup flow finished.");
        }

        //????????????????????????
        PbsUaService.reportDesktop(Desktop.getLauncherData(Desktop.getDownloadState(),Desktop.getDownloadTime(),Desktop.getAnalyseState()));
    }

    //PBS?????????Profile??????????????????4???,?????????????????????,???????????????Profile??????UI,???????????????
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
            //?????????????????????????????????????????????????????????????????????????????????????????????
            mFirstIndex = LauncherService.getInstance().getFirstIndex();

            //????????????????????????EPG???????????????EPG????????????????????????????????????????????????-1??????????????????
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
        //????????????????????????????????????????????????????????????????????????????????????,?????????onPageScrollStateChanged???????????????
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
     * ??????????????????
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

        // mWelcomeRelativeLayout??????????????????????????????????????????
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

        //??????????????????
        presenter.loginAndLoadLauncher(this);
        setContentView(R.layout.activity_launcher);

        //?????????View???Data
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
        mViewpager.setFocusable(false);//??????viewpager????????????
        mTopView         = (RelativeLayout) findViewById(R.id.top_view);
        if (IS_SCROLL_NAVIGATION_MODE) {
            ((FocusRelativeLayoutExt.LayoutParams) mViewpager.getLayoutParams()).topMargin = 0;
        }
        mMainUpView      = (MainUpView) findViewById(R.id.mp_mainUpView);
        mContainerLayout = (BrowseFrameLayout) findViewById(R.id.bl_container);
        mMainLayout      = (FocusRelativeLayoutExt) findViewById(R.id.rl_main_lay);
        mSimpleEpgContainer = (RelativeLayout) findViewById(R.id.rl_simple_epg_content);

        //?????????Profile??????UI
        mProfile = findViewById(R.id.profile);
        //Profile??????
        mProfileIcon = findViewById(R.id.iv_user_head_icon);
        //??????Profile??????
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

        //Profile??????
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

        //???????????????????????????
        mLauncherFocusHelper = new LauncherFocusHelper(this);
        mNavRecyclerView.setLayoutManager(new RvLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false,mNavRecyclerView));

        setNetworkStatusIcon();  //????????????????????????
        //?????????????????????????????????
        if (mMainLayout != null) {
            mMainLayout.setInterceptor(mLauncherFocusHelper);
        }

        //initClickListener
        mIvSearch.setOnClickListener(this);

        //?????????????????? ????????????
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
     * ????????????????????????????????????,?????????,??????????????? ????????? ???????????????
     * ?????????????????????????????? ?????????????????????  ?????????????????????????????? ????????????????????????
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

        //xmpp H5???????????????????????????
        PushMessagePresenter.setHasShowH5Message(false);

        //????????????APP??????
        MessageDataHolder.get().setAppInfoList(APPUtils.getAllAppInfo(this));
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????
     * 1???????????????????????? 2?????????????????????????????? 3????????????????????????????????????
     * */

    private boolean mIsChangeTab = false;
    private boolean isFreshingPbs = false;//??????????????????????????????

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

        //????????????401H????????????????????????token??????????????????????????????
        if(isLoadLauncherSuccess){

            if (RefreshManager.getInstance().needRefresh()) { //????????????????????????
                SuperLog.info2SD(TAG, "need to update launcher!! ");
                launcherUpdate(false);
            }
        }

        //?????????UBDTool??????H5???????????????Srcch
        UBDPurchase.clearH5Info();

        //?????????H5??????????????????????????????????????????????????????
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
     * ???????????????????????????????????????launcher
     *
     * @param event
     */
    @Subscribe
    public void event(SwitchProfileEvent event) {
        SuperLog.debug(TAG, "Received SwitchProfileEvent event");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isLoadAdvertCompleted = true;// //???????????????????????????????????????????????????true
                launcherUpdate(true);
            }
        });
    }

    private void launcherUpdate(boolean isSwitchProfile) {

        EpgTopFunctionMenu.getInstance().resetMapList();
        if (isSwitchProfile){
            //??????????????????????????????????????????map
            ConvertDataFromPbsToEpg.removeOldPbsEpgData(LauncherService.getInstance().getLauncher().getNavigateList(),LauncherService.getInstance().getGroupElements());
            resetTopScrollAdsAndFunctionMenu();
        }else{
            //??????????????????????????????????????????????????????
            OTTApplication.getContext().setIsFirstPower(true);
            ConvertDataFromPbsToEpg.convertDataFromPbsToEpg(ConvertDataFromPbsToEpg.mProfileCustomization,refreshListener,false);
        }

        focusManageEpgTop(false);
        mIvSearch.setFocusable(false);

        updateFocusColor();
        setEpgTopFocusColor();

        //??????????????????launcher????????????launcherLink??????
        updateLauncherLink();
        setChildrenAndSimpleNav(LauncherService.getInstance().getLauncher().getNavigateList());

        mLauncherFocusHelper.clearFocusEffect();
        SuperLog.debug(MainActivity.TAG, "onResume() + ????????????launcher");
        disableKeyCode = true; //??????????????????????????????
        SuperLog.info2SD(TAG, "start load launcherData ");
        if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()) {
            loadSimpleEpgData(isSwitchProfile);
        } else if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            loadChildrenEpgData(isSwitchProfile);
        } else {
            mRelaNavForTop.setVisibility(View.VISIBLE);

            loadHomeTvLogo();
            /*if (null != mManualRefreshBtn && !mManualRefreshBtn.hasFocus()){
                mIvSearch.requestFocus();  //?????????????????????
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

    //????????????????????????????????????
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

    //?????????????????????????????????
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
            //?????????????????????????????????????????????????????????????????????
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
                //??????????????????????????????
                Glide.with(MainActivity.this).resumeRequests();
                mLauncherFocusHelper.onPageChange();   //??????????????????????????????????????????
                loadBackGroup(getPageBgUrl());
                //viewpager??????????????????????????????
                if (currentPosition >= 0 && currentPosition < mFragmentAdapter.size()) {
                    currentPositionTemple = currentPosition;
                }
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                //???????????????????????????
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
     * @param isFirstLoad ???????????????????????????
     */
    private void loadSimpleEpgData(boolean isFirstLoad) {
        UBDDesktopType.record("simple");
        UBDSwitch.getInstance().setNavPosition(UBDConstant.NavPosition.SIMPLE);
        SharedPreferenceUtil.getInstance().setIsChildrenEpg(false);
        SharedPreferenceUtil.getInstance().setIsSimpleEpg(true);

        //?????????????????????????????????epg?????????,????????????
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
        //????????????????????????????????????
        if(null!=simpleEpgFragment){
            simpleEpgFragment.setRestartNew(true);
            simpleEpgFragment=null;
        }
        //??????????????????epg???????????????fragment?????????????????????????????????
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
     * @param isFirstLoad ???????????????????????????
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
        //??????????????????epg???????????????fragment?????????????????????????????????
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

    //??????????????????
    private void onBack(){
        resetMarginForTopView(-1);
        mLauncherFocusHelper.onKeyBack();
    }

    /**
     * ??????????????????
     *
     * @param type=0:EPG;=1:??????EPG???=2???????????????
     */
    public void switchLauncher(DesktopType type) {
        //???????????????????????????
        mLauncherFocusHelper = new LauncherFocusHelper(this);
        //???????????????????????????????????????????????????????????????????????????????????????tab,???????????????????????????
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
        UpgradeManager.upgrade(this);//???????????????????????????,?????????????????????,????????????????????????
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

    //????????????????????????logo
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

    //????????????Launcher??????????????????(imgUrl),???????????????Launcher????????????(mDefaultLauncherBgUrl),??????????????????APK????????????(R.drawable.default_detail_bg1)
    @SuppressLint("CheckResult")
    public void loadBackGroup(String imgUrl) {
        if( TextUtils.isEmpty(imgUrl) && TextUtils.isEmpty(mDefaultLauncherBgUrl)){
            //???????????????????????????(imgUrl),???????????????????????????(mDefaultLauncherBgUrl)?????????????????????????????????????????????(????????????)
            SuperLog.info2SDDebug(TAG,"loadBackBg___loadBackGroup()__????????????????????????");
            mBgUrl = "1";
            GlideUtil.loadForResourceReady(this,R.drawable.default_detail_bg1, mBackgroundImageView,-1,null);
        } else {
            String backgroundUrl = LauncherService.getInstance().getLauncherPictureLink();
            if(!TextUtils.isEmpty(imgUrl)){
                //??????????????????????????????(imgUrl)????????????
                backgroundUrl = imgUrl.contains("http") ? imgUrl : backgroundUrl + imgUrl;
            } else {
                //????????????????????????????????????????????????????????????(mDefaultLauncherBgUrl)(?????????????????????????????????????????????)
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

    //profileType=0?????????????????????????????????profileType=1??????????????????????????????profileType=2?????????????????????????????????
    public void notifyDataSetChanged(int profileType){
        //??????????????????????????????????????????
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg() || SharedPreferenceUtil.getInstance().getIsSimpleEpg()){
            return;
        }
        if (profileType == 2){
            if (null != mFragment && !mFragment.getNavId().equalsIgnoreCase(ConvertDataFromPbsToEpg.AIKAN_NAV_ID)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (OTTApplication.getContext().isFirstPower()) {
                            //??????????????????????????????????????????????????????
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
            //??????index?????????????????????index,???????????????????????????????????????+-1.
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
                                if (LauncherService.getInstance().getFirstIndex() >= 0) {//????????????????????????????????????
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
     * ??????????????????
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

                //?????????tab??????????????????????????????tab title,?????????????????????
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

                    //??????????????????????????????
                    Paint paint = new Paint();
                    paint.setTextSize(tvTitle.getTextSize());
                    float size = paint.measureText(tvTitle.getText().toString());
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llNavTitleLine.getLayoutParams();
                    layoutParams.width = (int) size;
                    llNavTitleLine.setLayoutParams(layoutParams);

                    //??????????????????????????????
                    imNavTitleLine.setBackground(Utils.getNavTitleShape(mContext,position));
                    Utils.setNavTitleBg(mContext,tvTitle,Utils.getNavTitleShape(mContext,position));
                }
            }
        };

        mNavAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mWelcomeRelativeLayiout.getVisibility() != View.VISIBLE) {
                    //????????????????????????
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

        //for???????????????navigateList?????????????????????????????????4K??????
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
            //??????Tab??????????????????epg
            if (null != simpleEpgNav){
                navigateList.remove(simpleEpgNav);
                LauncherService.getInstance().setNavSimpleEpg(simpleEpgNav);
            }
            //??????Tab?????????????????????epg
            if (null != childEpgNav){
                navigateList.remove(childEpgNav);
                LauncherService.getInstance().setNavChildrenEpg(childEpgNav);
            }
            //???????????????4K?????????Tab????????????4K????????????
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
                case R.id.iv_simple_search://????????????
                    //????????????????????????????????????????????????
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_main_refresh_btn://????????????
                    if (null != mManualRefreshIm){
                        mManualRefreshIm.setVisibility(View.GONE);
                    }
                    UBDTopFunction.reportTopFunction(mManualRefreshBtn.getText().toString(), TopFunctionData.BTN_NORMAL);
                    //focusManageEpgTop(false);//???????????????????????????????????????????????????????????????????????????
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
     * ???presenter?????????launcher.json???????????????
     * queryLauncher????????????????????????,??????MainActivity????????????,???????????????????????????
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
        //????????????
        mDefaultLauncherBgUrl = GlideUtil.getUrl(LauncherService.getInstance().getLauncher().getDefaultBackground());
        loadHomeTvLogo();
        mRelaNavForTop.setVisibility(View.VISIBLE);
        //?????????launcher????????????
        loadBackGroup(mDefaultLauncherBgUrl);
        setChildrenAndSimpleNav(navigates);
        //??????????????????????????????????????????????????????????????????
        if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()) {
            SuperLog.info2SD(TAG, "last saved launcher is simpleEpg, begin to load data");
            loadSimpleEpgData(true);
            return;
        }
        //????????????????????????????????????????????????????????????????????????
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            SuperLog.info2SD(TAG, "last saved launcher is childrenEpg, begin to load data");
            loadChildrenEpgData(true);
            return;
        }
        UBDDesktopType.record("normal");
        mRelaNavForTop.setVisibility(View.VISIBLE);
        mRiSearch.setVisibility(View.VISIBLE);
        //???????????????????????????epg?????????,????????????
        if (null != simpleEpgFragment) {
            getSupportFragmentManager().beginTransaction().remove(simpleEpgFragment).commitAllowingStateLoss();
            simpleEpgFragment = null;
        }
        //?????????????????????????????????epg?????????,????????????
        if (null != childrenEpgFragment) {
            getSupportFragmentManager().beginTransaction().remove(childrenEpgFragment).commitAllowingStateLoss();
            childrenEpgFragment = null;
        }
        initNav(navigates);
        initFragments(navigates);
        needShowStartPic = isFirstLoad;
        switchLauncher();
        //???????????????????????????
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {  //????????????????????????
            if (isBackFromProfile){
                return true;
            }
            //??????????????????????????????????????????APP???????????????????????????????????????
            if (OTTApplication.isApplicationBroughtToBackground(getApplicationContext()) || OTTApplication.getContext().getIsJumpToThirdApp()) {
                OTTApplication.getContext().setIsJumpToThirdApp(false);
                return true;
            }
            //????????????????????????????????????
            if (null != simpleEpgFragment) {
                return true;
            }
            //??????????????????????????????????????????
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
            //????????????????????????profileH5????????????????????????????????????????????????????????????????????????????????????????????????
            focusManageEpgTop(true);
        }

        resetMarginForTopView(0);
        mNavFoucsKeyEvent = keyCode;
        if (disableKeyCode || mWelcomeRelativeLayiout.getVisibility() == View.VISIBLE) {

            //???????????????
            if (keyCode == KeyEvent.KEYCODE_BACK){
                //???????????????????????????
                if ( mWelcomeRelativeLayiout.isLoadAd() && mWelcomeRelativeLayiout.isCanSkip()){
                    //?????????????????????
                    if (mWelcomeRelativeLayiout.getShowType().equals(mWelcomeRelativeLayiout.TYPE_DISPLAY)){
                        mWelcomeRelativeLayiout.complete();
                        mWelcomeRelativeLayiout.clearUI();
                    }
                    //?????????????????????
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

        //?????????????????????TAB??????????????????????????????
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && !hasFocusTvEpgScrollAds() && !hasFocusLinearContent()  && (mNavRecyclerView.hasFocus() || mIvSearch.hasFocus())) {
            if (mIvSearch.hasFocus()){
                currentPosition = -1;
            }
            focusManageEpgTop(true);
        }

        //???0720???????????????????????????????????????????????????????????????????????????????????????
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && (hasFocusEpgTopProfile() || hasFocusLinearContent() || hasFocusTvEpgScrollAds())){
            return true;
        }

        //BTV/TVOD??????????????????
        //TODO ??????????????????????????????????????????????????????????????? ?????????????????????,???????????????
        int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
        if (codeValue == RemoteKeyEvent.BTV && OTTApplication.getContext().isLoginSuccess() &&
                OTTApplication.getContext().isLoadLauncherSuccess()) {
            //????????????launcher?????????????????????????????????
            //??????
            if (isLauncher == 1) {
                switchLiveTV(false);
            }
        } else if (codeValue == RemoteKeyEvent.TVOD && OTTApplication.getContext().isLoginSuccess()
                && OTTApplication.getContext().isLoadLauncherSuccess()) {
            //????????????launcher?????????????????????????????????
            //??????(??????)
            if (isLauncher == 1) {
                switchLiveTV(true);
            }
        }

        //???????????????????????????????????????????????????????????????????????????
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

        if (RefreshManager.getInstance().needRefresh()) { //????????????????????????
            SuperLog.info2SD(TAG, "need to update launcher!! ");
            focusManageEpgTop(false);
            mIvSearch.setFocusable(false);
            launcherUpdate(false);
        }else{
            if (SharedPreferenceUtil.getInstance().getIsSimpleEpg()) {
                LauncherService.getInstance().refreshLauncher(this,refreshListener,true);
            } else if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                //??????????????????
                mLauncherFocusHelper.clearFocusEffect();
                LauncherService.getInstance().refreshLauncher(this,refreshListener,true);
            } else {
                if (currentPosition < 0){
                    currentPosition = 0;
                }
                //mViewpager.setCurrentItem(currentPosition);
                if (currentPosition < mFragmentAdapter.size()) {
                    PHMFragment tabItemFragment = mFragmentAdapter.get(currentPosition);
                    //3????????????????????????????????????????????????
                    if (tabItemFragment instanceof MyPHMFragment) {
                        //????????????????????????????????????????????????????????????
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
                    //????????????????????????????????????????????????????????????????????????????????????flag??????false
                    OTTApplication.getContext().setIsRefreshPbsEpg(false);
                    //?????????Pbs????????????
                    mFragment.clearData();
                    if (!mIsChangeTab){
                        mFragment.scrollToTop();
                        onBack();
                    }
                }
            }else if (isEpgRefresh && ProfileManager.isAiKanFragment()){
                //?????????????????????????????????????????????????????????
                OTTApplication.getContext().setIsRefreshPbsEpg(true);
                refreshPbsEpg(mNavRecyclerView.hasFocus() ? true : false);
            }else if (isEpgRefresh && (null != mFragment || null != childrenEpgFragment) && !change){
                refreshEpgNoNeedUpdateLauncher();
            }else if(!change) {
                SuperLog.debug(TAG,"[refreshLauncher] version no change, load local json");
                //???????????????????????????????????????json
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
     * ????????????
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
     * ????????????????????????
     */
    private void registerNetWorkListener() {
        networkReceiver = new NetworkReceiver();
    }


    /**
     * ??????tokenReceiver
     */
    private void registerTokenReceiver() {
        if (mTokenReceiver == null) {
            mTokenReceiver = new TokenReceiver(this);
        }
        IntentFilter intentFilter = new IntentFilter();
        // ???BroadcastReceiver??????action?????????????????????????????????
        intentFilter.addAction(TokenManager.TOKEN_ACTION);
        registerReceiver(mTokenReceiver, intentFilter);
    }

    /**
     * ?????????????????????
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

    //?????????chinamobile???Token????????????
    @Override
    public void onStateChange(String action, Intent intent) {
        //????????????????????????????????????????????????
        String status = intent.getStringExtra("status");
        SuperLog.info2SD(TAG, "Token updated, begin to re-Authenticate. Status=" + status);
        //??????????????????????????????????????????Token
        AuthenticateManager.getInstance().cancelReportToken();
        if ("online".equals(status)) {
            UserInfo oldUserInfo = AuthenticateManager.getInstance().getUserInfo();
            UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
            if (null != oldUserInfo && null != userInfo && !oldUserInfo.getUserId().equals(userInfo.getUserId())) {
                //???????????????????????????????????????
                SuperLog.info2SD(TAG, "UserID changed after switch user.");
                //??????????????????????????????????????????onPause???????????????????????????????????????
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
                reAuthenticateAfterSwitch();//??????isReport=false
            } else {
                //1.????????????????????????
                //2.?????????????????????????????????????????????
                //3.??????????????????TOKEN??????,????????????Token
                reAuthenticate();
            }
        } else {
            //??????????????????????????????????????????????????????????????????EPGAPK????????????????????????????????????????????????????????????????????????
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
        //??????????????????
        if (this != OTTApplication.getContext().getMainActivity()) {
            SuperLog.info2SD(TAG, "????????????mainActivity[?????????] MainActivity has been finished.");
            this.finish();
            Activity oldMainActivity = OTTApplication.getContext().getMainActivity();
            if (null != oldMainActivity ) {
                SuperLog.info2SD(TAG, oldMainActivity + " MainActivity has been finished.");
                oldMainActivity.finish();
                ActivityStackControlUtil.finishProgram();
            }
        }

        //?????????????????????
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
     * ???????????????????????????
     *
     * @return ???????????????
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

    //??????????????????????????????????????????
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

        //????????????????????????
        if (AdManager.getInstance().isPlayComplete()){
            mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
            SuperLog.debug(AdConstant.TAG, "Play SSP AD already complete");
            return;
        }

        //????????????????????????
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
                        //????????????????????????
                        if (AdManager.getInstance().isPlayComplete()){
                            mainHandler.sendEmptyMessage(LOAD_SSP_AD_BANNER_COMPLETION);
                            SuperLog.debug(AdConstant.TAG, "Play SSP AD already complete");
                        } else {
                            //????????????(??????/??????)
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
        //????????????????????????
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

    //???????????????????????????
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

    //??????????????????????????????java.lang.IllegalStateException:crash??????
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) { }

    //????????????????????????
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

    //?????????????????????????????????
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

    //??????????????????????????????????????????????????????
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