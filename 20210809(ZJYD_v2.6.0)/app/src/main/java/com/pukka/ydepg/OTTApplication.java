package com.pukka.ydepg;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.multidex.MultiDexApplication;

import com.huawei.ott.sdk.encrypt.MsaSecurityStorage;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.utils.CrashHandler;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.UtilBase;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.mvp.contact.TabItemContact;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.ThreadManager;
import com.pukka.ydepg.moudule.multviewforstb.multiview.TVApplication;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.VRPlayerApplictaion;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

public class OTTApplication extends MultiDexApplication {

    private static final String TAG = OTTApplication.class.getSimpleName();

    @SuppressWarnings("StaticFieldLeak")
    private static OTTApplication app;

    private static Typeface typeFace;

    private MainActivity mainActivity;

    private WeakReference<Activity> mCurrentActivityWeakRef;

    private TabItemContact.ITabItemView view;

    private List<GroupElement> groupElements;

    private boolean needShowNetworkExceptionDialog = false;   // 网络检测对话框是否显示

    private boolean isLoginSuccess = false;

    private boolean isLoadLauncherSuccess = false;

    //是否刷新爱看界面数据
    private boolean isRefreshPbsEpg = false;

    /**是否是跳转到三方app
     * 解决二级页面配置了跳转三方app(和家相册)，在三方app按返回键，二级页面也相应了返回事件，焦点跑到了页面的最顶部
     * */
    private boolean isJumpToThirdApp = false;

    // 修复 试看页面营销内容图片 缓存机制错误问题 ADD START
    /**
     * 试看页面营销内容图片地址（需在每次开机时候重新获取）
     */
    private String videoTrialMarketingContentImageURL;

    //儿童模式下详情广告url
    private String childVodDetailAdvertisementURL;

    private String lastNavID;

    private String favoCatalogID; //添加收藏id;

    private boolean searchHasFocus = false; //Search当前是否获取了焦点

    private boolean isSupportMulticast = false;

    //第一次开机--解决开机焦点落焦到爱看界面
    private boolean isFirstPower = true;

    public boolean isSupportMulticast() {
        return isSupportMulticast;
    }

    public void setSupportMulticast(boolean supportMulticast) {
        SuperLog.info2SD(TAG,"Current device support multicast(组播) capability : " + supportMulticast);
        isSupportMulticast = supportMulticast;
    }

    public void setIsRefreshPbsEpg(boolean isRefreshPbsEpg){
        this.isRefreshPbsEpg = isRefreshPbsEpg;
    }
    public boolean isRefreshPbsEpg(){
        return isRefreshPbsEpg;
    }

    public void setIsFirstPower(boolean isFirstPower){
        this.isFirstPower = isFirstPower;
    }
    public boolean isFirstPower(){
        return isFirstPower;
    }

    public void setSearchFocus(boolean searchFocus){
        this.searchHasFocus = searchFocus;
    }

    public boolean getSearchFocus(){
        return searchHasFocus;
    }

    public static Typeface getTypeFace() {
        return typeFace;
    }

    public String getFavoCatalogID()
    {
        return favoCatalogID;
    }

    public void setFavoCatalogID(String favoCatalogID)
    {
        this.favoCatalogID = favoCatalogID;
    }

    public String getChildVodDetailAdvertisementURL()
    {
        return childVodDetailAdvertisementURL;
    }

    public void setChildVodDetailAdvertisementURL(String childVodDetailAdvertisementURL) {
        this.childVodDetailAdvertisementURL = childVodDetailAdvertisementURL;
    }

    public String getVideoTrialMarketingContentImageURL() {
        return videoTrialMarketingContentImageURL;
    }
    // 修复 试看页面营销内容图片 缓存机制错误问题 ADD END

    public void setVideoTrialMarketingContentImageURL(String videoTrialMarketingContentImageURL) {
        this.videoTrialMarketingContentImageURL = videoTrialMarketingContentImageURL;
    }

    public List<GroupElement> getGroupElements() {
        return groupElements;
    }

    public void setGroupElements(List<GroupElement> groupElements) {
        this.groupElements = groupElements;
    }

    public boolean isLoadLauncherSuccess() {
        return isLoadLauncherSuccess;
    }

    public void setLoadLauncherSuccess(boolean loadLauncherSuccess) {
        isLoadLauncherSuccess = loadLauncherSuccess;
    }

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    public boolean isNeedShowNetworkExceptionDialog() {
        return needShowNetworkExceptionDialog;
    }

    public void setNeedShowNetworkExceptionDialog(boolean needShowNetworkExceptionDialog) {
        this.needShowNetworkExceptionDialog = needShowNetworkExceptionDialog;
    }

    public TabItemContact.ITabItemView getView() {
        return view;
    }

    public void setView(TabItemContact.ITabItemView view) {
        this.view = view;
    }

    public String getLastNavID() {
        return lastNavID;
    }

    public void setLastNavID(String lastNavID) {
        this.lastNavID = lastNavID;
    }

    public void setIsJumpToThirdApp(boolean isJumpToThirdApp){
        this.isJumpToThirdApp = isJumpToThirdApp;
    }
    public boolean getIsJumpToThirdApp(){
        return isJumpToThirdApp;
    }




    public OTTApplication() {
        app = this;
    }

    public static OTTApplication getContext() {
        return app;
    }

    @Override
    public void onCreate() {
        UtilBase.init(this);
        AuthenticateManager.getInstance().createTokenManagerObject();
        super.onCreate();
        //初始化工具类（log等）
        getWidthPixel();
        //设置字体
        initActivityCallback();
        typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/hzgb2.ttf");
        initAppData();

        //初始化华为安全加解密工具
        MsaSecurityStorage.init(this);

        //获取User-Agent,必须在主线程执行
        HttpUtil.getSspUserAgent();
        TVApplication.getInstance().setContext(this);
        VRPlayerApplictaion.getInstance().setContext(this);
    }

    private void initAppData() {
        SuperLog.setEnable(BuildConfig.DEBUG_LOG, false);
        ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // Print application information
                showAppVersion();
                // Initialize CrashHandler
                CrashHandler.getInstance().init(getApplicationContext());
            }
        });
    }


    private void showAppVersion() {
        try {
            String packageName = this.getPackageName();
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
            StringBuilder version = new StringBuilder();
            version.append(" <<<<< ZJYD app has been started from now. App and device info : >>>>> ")
                   .append("\n*******************************************************************************")
                   .append("\n*\tPackageName : ").append(packageName)
                   .append("\n*\tVersionName : ").append(packageInfo.versionName)
                   .append("(code=").append(packageInfo.versionCode).append(")")
                   .append("\n*\tSVNLog      : " + BuildConfig.SVN_LOG)
                   .append("\n*\tJDK Version : ").append(System.getProperty("java.version"))
                   .append("\n*\tDevice Info : ").append(Build.MODEL)
                   .append("\n*\tManufacture : ").append(Build.MANUFACTURER)
                   .append("\n******************************************************************************");
            SuperLog.info2SD(TAG, version.toString());
            CommonUtil.printChinamobileAuthVersion();
        } catch (PackageManager.NameNotFoundException e) {
            SuperLog.error(TAG, e);
        }
    }

    public boolean isNeedLoadResource() {
        return false;
    }

    /**
     * Analyzing the current application is in the foreground or background
     */
    public static boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (tasks != null && !tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getWidthPixel() {
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
    }

    //获取应用缓存文件默认保存位置
    public static String getCachePath() {
        return UtilBase.getApplicationContext().getFilesDir().getPath() + File.separator;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (mCurrentActivityWeakRef != null) {
            currentActivity = mCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    private void initActivityCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) { }

            @Override public void onActivityStarted(Activity activity) { }

            @Override
            public void onActivityResumed(Activity activity) {
                OTTApplication.this.mCurrentActivityWeakRef = new WeakReference<>(activity);
            }

            @Override public void onActivityPaused(Activity activity) { }

            @Override public void onActivityStopped(Activity activity) { }

            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }

            @Override public void onActivityDestroyed(Activity activity) { }
        });
    }
}