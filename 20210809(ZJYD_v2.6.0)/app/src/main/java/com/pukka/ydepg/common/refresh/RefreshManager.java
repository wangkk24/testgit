package com.pukka.ydepg.common.refresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.screensaver.ScreenContract;
import com.pukka.ydepg.common.screensaver.presenter.ScreenPresenter;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.ui.ChildLauncherActivity;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.WebActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewMyPayModeActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewOrderCenterActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.ProductTransitionActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools.JumpToH5OrderUtils;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import java.util.Timer;
import java.util.TimerTask;

public class RefreshManager {

    public final static String TAG = RefreshManager.class.getSimpleName();

    //[终端配置参数]自动加载更新桌面文件的时间间隔,单位为秒,默认无,即关闭
    private final String AUTO_LOAD_TIME = "auto_load_time";

    //[终端配置参数]触发用户没有操作定时任务的时间,单位为秒
    private final String TIME_USER_NOT_INTERACT = "time_user_not_interact";

    //[终端配置参数]触发用户没有操作定时任务的时间,单位为秒
    private final String TIME_USER_NOT_INTERACT_FOR_SCREENSAVER = "time_user_not_interact_screensaver";

    // 用户没有操作定时任务的默认时间 5min
    private final long DEFAULT_TIME_USER_NOT_INTERACT = 5 * 60 * 1000L;

    //用户没有操作的毫秒数,默认5分钟
    private final long UNDEFINED_TIME_USER_NOT_INTERACT = -1;

    //用户没有操作的毫秒数,默认5分钟
    private long timeUserNotInteract = UNDEFINED_TIME_USER_NOT_INTERACT;

    //用户没有操作触发屏保的时间间隔毫秒数,默认5分钟
    private long timeUserNotInteractForScreensaver = UNDEFINED_TIME_USER_NOT_INTERACT;

    private long timeAutoRefresh     = UNDEFINED_TIME_USER_NOT_INTERACT;

    private final Runnable intervalRunnable = new IntervalRefreshRunnable();

    private final Runnable screensaverRunnable = new IntervalScreensaverRunnable();

    //防止多个任务重复更新,第一个任务启动更新后设为true,执行结束后置为false
    private boolean needRefresh = false;

    private Handler userActHandler = null;

    public ScreenContract.IPresenter getScreenPresenter() {
        return screenPresenter;
    }

    private final ScreenContract.IPresenter screenPresenter = new ScreenPresenter();

    private RefreshManager() {}

    private static RefreshManager refreshManager = null;

    public static RefreshManager getInstance() {
        if (refreshManager == null) {
            refreshManager = new RefreshManager();
        }
        return refreshManager;
    }

    private void init() {
        userActHandler = new Handler(Looper.getMainLooper());
        timeUserNotInteract = getTimeUserNotInteract();
        timeUserNotInteractForScreensaver = getTimeUserNotInteractForScreensaver();
        userActHandler.postDelayed(intervalRunnable, timeUserNotInteract);
        userActHandler.postDelayed(screensaverRunnable,timeUserNotInteractForScreensaver);
    }

    //获取自动更新桌面的时间间隔,单位毫秒
    private long getRefreshPeriod() {
        long period;
        try {
            String sPeriod = CommonUtil.getConfigValue(AUTO_LOAD_TIME);
            period = Long.parseLong(sPeriod) * 1000L;//转换成毫秒
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            period = 0;
        }
        SuperLog.info2SD(TAG, "Auto refresh desktop timer started, period(unit:s, 0s means shutdown this timer) = " + period / 1000);
        return period;
        //return 10*1000L; //TODO 测试临时改成10s
    }

    //获取自动更新桌面的用户无操作时间间隔,单位毫秒
    private long getTimeUserNotInteract() {
        long period;
        try {
            String sPeriod = CommonUtil.getConfigValue(TIME_USER_NOT_INTERACT);
            period = Long.parseLong(sPeriod) * 1000L;//转换成毫秒
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            period = DEFAULT_TIME_USER_NOT_INTERACT;
        }
        //period = 5 * 1000L; //TODO 测试临时改成5s
        SuperLog.info2SD(TAG, "User not interact task period(unit:s) = " + period / 1000);
        return period;
    }

    //获取触发屏保的用户无操作的时间间隔,单位毫秒
    private long getTimeUserNotInteractForScreensaver() {
        long period;
        try {
            String sPeriod = CommonUtil.getConfigValue(TIME_USER_NOT_INTERACT_FOR_SCREENSAVER);
            period = Long.parseLong(sPeriod) * 1000L;//转换成毫秒
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            period = DEFAULT_TIME_USER_NOT_INTERACT;
        }
        //period = 30 * 1000L; //TODO 测试临时改成30s
        SuperLog.info2SD(TAG, "User not interact for screensaver task period(unit:s) = " + period / 1000);
        return period;
    }

    //定时检查桌面是否需要更新的任务,配置定时自动刷新周期后根据配置的周期执行此任务
    private class RefreshAutoCheckTask extends TimerTask {
        @Override
        public void run() {
            SuperLog.info2SD(TAG, "[AutoRefreshRunnable]AutoRefresh timer triggered for " + timeAutoRefresh / 1000 + "s");
            if(OTTApplication.getContext().getCurrentActivity() instanceof ChildLauncherActivity ){
                //当前在二级Launcher页面则刷新二级页面launcher
                SuperLog.info2SD(TAG,"Refresh timer triggered and current activity is ChildLauncherActivity, force to refresh.");
                ((ChildLauncherActivity) OTTApplication.getContext().getCurrentActivity()).refreshLauncher();
            } else if(OTTApplication.getContext().getCurrentActivity() instanceof TopicActivity){
                //当前在专题页面则刷新TopicActivity
                SuperLog.info2SD(TAG,"Refresh timer triggered and current activity is TopicActivity, force to refresh.");
                ((TopicActivity)OTTApplication.getContext().getCurrentActivity()).refreshTopic();
            } else {
                //当前在其他页面
                if (needRefresh()) {
                    if (needRefresh) {
                        //需要更新,但是已经在执行更新动作了,结束
                        SuperLog.info2SD(TAG, "Refresh timer triggered and now is refreshing desktop. No need operate again.");
                    } else {
                        //需要更新(用户五分钟无操作或者没有看视频)
                        SuperLog.info2SD(TAG, "Refresh timer triggered and need to refresh desktop. New desktop will be loaded after user not interact for " + timeUserNotInteract / 1000 + "s.");
                        needRefresh = true;
                        //计时5分钟(终端配置参数可配),计时时间内用户无操作则触发[无操作任务]
                        updateUserAction(false,TimerType.REFRESH_DESKTOP);
                    }
                } else {
                    SuperLog.info2SD(TAG, "Refresh timer triggered and no need to refresh desktop, wait for next time");
                }
            }
        }
    }

    //判断当前是否在播放(页面) true:不展示屏保 false:可以展示屏保
    private boolean isPlaying() {
        Activity   currentActivity = OTTApplication.getContext().getCurrentActivity();
//        if(currentActivity instanceof NewVodDetailActivity){
//            return ((NewVodDetailActivity) currentActivity).isPlaying();
//        }

        //H5订购页
        if (currentActivity instanceof WebActivity){
            return JumpToH5OrderUtils.getInstance().isDoingOrder();
        }
        return     currentActivity instanceof OnDemandVideoActivity      //点播播放页
                || currentActivity instanceof LiveTVActivity             //直播播放页
                || currentActivity instanceof ChildModeVodDetailActivity //儿童版详情
                || ( currentActivity instanceof MainActivity
                        &&  (  SharedPreferenceUtil.getInstance().getIsSimpleEpg()                        //简版EPG
                            || OTTApplication.getContext().getMainActivity().isShowVideoView()))         //有播放窗口
                || currentActivity instanceof NewVodDetailActivity    //新版详情页
                || currentActivity instanceof NewOrderCenterActivity  //订购中心
                || currentActivity instanceof NewProductOrderActivity //产品包选择页面
                || currentActivity instanceof ProductTransitionActivity //订购过渡页
                || currentActivity instanceof NewMyPayModeActivity; //订购支付页
    }

    //用户[timeUserNotInteract]秒无操作触发一次,由主线程Handler处理
    private class IntervalRefreshRunnable implements Runnable {
        @Override
        public void run() {
            SuperLog.info2SD(TAG, "[IntervalRefreshRunnable]User does not interact for " + timeUserNotInteract / 1000 + "s");
            if (isPlaying()) {
                //在播放,不做处理
                SuperLog.info2SD(TAG, "No need to do anything because user is playing video now. Remove intervalRunnable and post a new one.");
            } else if (!CommonUtil.isApkForeground()) {
                //EPG应用在后台不做处理
                SuperLog.info2SD(TAG, "EPG apk is background running now. Remove intervalRunnable and post a new one.");
            } else {
                //EPG应用在前台
                if (needRefresh ){
                    //有桌面需要刷新
                    if(screenPresenter.isShowing()){
                        //屏保正在展示 屏保结束时立即刷新
                        SuperLog.info2SD(TAG,"Desktop will be updated immediately when Screensaver finished.");
                    } else {
                        //屏保没展示时 立即刷新桌面
                        loadNewDesktop();
                    }
                }
            }
            updateUserAction(false, TimerType.REFRESH_DESKTOP);
        }
    }

    //用户[timeUserNotInteractForScreensaver]秒无无操作触发一次,由主线程Handler处理
    private class IntervalScreensaverRunnable implements Runnable {
        @Override
        public void run() {
            SuperLog.info2SD(TAG, "[IntervalScreensaverRunnable]User does not interact for " + timeUserNotInteractForScreensaver / 1000 + "s");
            if (isPlaying()) {
                //在播放,不需要展示屏保
                SuperLog.info2SD(TAG, "No need to do anything because user is playing video now. Remove intervalRunnable and post a new one.");
            } else if (!CommonUtil.isApkForeground()) {
                //EPG应用在后台,不需要展示屏保
                SuperLog.info2SD(TAG, "EPG apk is background running now. Remove intervalRunnable and post a new one.");
            } else {
                //EPG应用在前台
                screenPresenter.start();
            }
            updateUserAction(false,TimerType.SCREENSAVER);
        }
    }

    //退出屏保时执行,如果有更新立即更新
    public void checkDesktopAfterScreensaver(){
        userActHandler.post(()->{
            //检查是否有更新
            SuperLog.info2SD(TAG,"Check if need update desktop after exit ScreenSaver");
            if ( needRefresh ) {
                //需要刷新(屏保没展示时)优先刷新,下次再展示屏保
                loadNewDesktop();
            }
            //启动定时器
            updateUserAction(true,TimerType.SCREENSAVER);
        });
    }

    private void loadNewDesktop() {
        SuperLog.info2SD(TAG, "Begin to load new desktop.");
        Intent intent = new Intent(OTTApplication.getContext(), MainActivity.class);
        intent.putExtra(TAG, true);
        //不加此Flag抛异常
        // android.util.AndroidRuntimeException:
        // Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag.
        // Is this really what you want?
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //调用后会走MainActivity的onNewIntent回调方法
        OTTApplication.getContext().startActivity(intent);
        needRefresh = false;      //更新结束,更新标志位还原
    }

    //本地存在新下载的Launcher.json文件时,为需要更新桌面
    public boolean needRefresh() {
        return SharedPreferenceUtil.getInstance().getLauncherUpdate() && null != LauncherService.getInstance().getLauncher();
    }

    public void startRefreshTimer() {
        init();
        timeAutoRefresh = getRefreshPeriod();
        if (timeAutoRefresh <= 0) {
            SuperLog.info2SD(TAG, "Refresh timer period configured incorrectly or not configured, no need to auto refresh desktop.");
        } else {
            Timer timer = new Timer();
            timer.schedule(new RefreshAutoCheckTask(), timeAutoRefresh, timeAutoRefresh);//单位是毫秒
        }
        //启动用户无操作时间间隔定时器
        updateUserAction(false,TimerType.ALL);
    }

    public interface TimerType {
        int ALL             = 0;
        int SCREENSAVER     = 1;
        int REFRESH_DESKTOP = 2;
    }

    //用户发生操作行为时调用 type类型为  interface TimerType
    public void updateUserAction(boolean userAct,int type) {
        if (userActHandler != null) {
            if(userAct){
                SuperLog.debug(TAG, "User interact. Remove intervalRunnable and post a new one.");
            } else {
                SuperLog.debug(TAG, "Remove intervalRunnable and post a new one.");
            }

            //TODO 已经移除的[intervalRunnable]或者消息队列里没有[intervalRunnable] 再移除是否会报错???
            switch (type){
                case TimerType.REFRESH_DESKTOP:
                    //计时5分钟(终端配置参数可配),计时时间内用户无操作则触发[无操作任务]
                    userActHandler.removeCallbacks(intervalRunnable);
                    userActHandler.postDelayed(intervalRunnable, timeUserNotInteract);
                    break;
                case TimerType.SCREENSAVER:
                    //计时5分钟(终端配置参数可配),计时时间内用户无操作则触发[无操作任务]
                    userActHandler.removeCallbacks(screensaverRunnable);
                    userActHandler.postDelayed(screensaverRunnable, timeUserNotInteractForScreensaver);
                    break;
                case TimerType.ALL:
                    userActHandler.removeCallbacks(intervalRunnable);
                    userActHandler.postDelayed(intervalRunnable, timeUserNotInteract);
                    userActHandler.removeCallbacks(screensaverRunnable);
                    userActHandler.postDelayed(screensaverRunnable, timeUserNotInteractForScreensaver);
                    break;
                default:
                    break;
            }

        }
    }

    private int firstSelect = 0;

    public boolean isFirstSelect() {
        if (firstSelect <= 2) {
            firstSelect++;
            return true;
        } else {
            return false;
        }
    }

    public void stopWatchScreensaverUserInteraction(){
        userActHandler.removeCallbacks(screensaverRunnable);
    }
}