package com.pukka.ydepg.moudule.base;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.iflytek.xiri.Feedback;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.BroadCastConstant;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.pushmessage.presenter.PushMessagePresenter;
import com.pukka.ydepg.common.pushmessage.view.MessageDialog;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SettingLanguageUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.event.MessageDialogEvent;
import com.pukka.ydepg.event.MessageDialogIsShowEvent;
import com.pukka.ydepg.event.OpenVodEvent;
import com.pukka.ydepg.event.SwitchEpgEvent;
import com.pukka.ydepg.event.TVGuideEvent;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.AuthenticateActivity;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.reminder.ReminderDialog;
import com.pukka.ydepg.launcher.ui.reminder.beans.ReminderMessage;
import com.pukka.ydepg.launcher.util.ThreadTool;
import com.pukka.ydepg.moudule.catchup.activity.CatchUpActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.featured.view.TopicService;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.moudule.player.ui.LiveTVActivity;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;
import com.pukka.ydepg.moudule.player.util.RemoteKeyEvent;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.VodMainActivity;
import com.pukka.ydepg.moudule.voice.VoiceEvent;
import com.pukka.ydepg.moudule.voice.VoiceVodListener;
import com.pukka.ydepg.moudule.voice.XiriVoiceVodUtil;
import com.pukka.ydepg.service.BaseBroadcastReceiver;
import com.pukka.ydepg.service.HeartBeatService;
import com.pukka.ydepg.service.NtpTimeService;
import com.pukka.ydepg.test.Tester;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Unbinder;
import io.reactivex.functions.Action;

import static com.pukka.ydepg.common.report.ubd.UBDConstant.ACTION.ADVERT_PURCHASE;
import static com.pukka.ydepg.common.utils.JsonParse.json2Object;

public abstract class BaseActivity<T extends IBaseContact.IBasePresenter> extends AuthenticateActivity<T> {

    private final String TAG = getClass().getSimpleName();

    protected BaseBroadcastReceiver baseBroadcastReceiver = null;

    protected int isLauncher = 0;//第一次加载  1 加载中 2 加载完成

    private Unbinder mUnBinder;

    private HeartBeatService mHeartBeatService = null;

    private ServiceConnection mHeartBeatConnection;

    private ServiceConnection mNTPConnection;

    private BaseActivity.HomeKeyReceiver mHomeReceiver;

    private IBinder mNtpBinder;

    private HeartBeatReceiver mHeartBeatReceiver;

    /*
     * 推送消息Dialog
     * */
    private MessageDialog mMessageDialog = null;

    /*
     * 用于标记推送消息Dialog正在展示
     * 默认false ，
     * */
    private boolean mIsShowing = false;

    /**
     * 当前activity是否处于可见状态
     */
    private boolean isResume;

    private static final String REMINDER_TYPE = "REMINDER_TYPE";

    private XiriVoiceVodUtil mXiriVoiceVodUtil;

    /**
     * 心跳Connection
     */
    private class HeartBitOTTConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SuperLog.debug(TAG, "onServiceConnected()");
            mHeartBeatService = ((HeartBeatService.HeartBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            SuperLog.error(TAG, "onServiceDisconnected()");
        }
    }

    /**
     * NTPService Connection
     */
    private class NtpOTTConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mNtpBinder = service;
            startNTPService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    private void registerHomeKeyReceiver() {
        mHomeReceiver = new BaseActivity.HomeKeyReceiver();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeReceiver, homeFilter);
    }


    @Override
    public void startService() {
        SuperLog.info2SD(TAG, "Begin to Start core service.");
        startHeartBitService();
        //启动NTP服务
        if (null == mNtpBinder) {
            bindNTPService();
        } else {
            startNTPService();
        }

        //开机ZJLogin之后的XMPP更新Token动作会启动XMPP初始化,因此无需手动启动XMPP
//        //启动xmpp
//        try {
//            SuperLog.info2SD(TAG, "Bind XMPP service when start");
//            XmppService.getInstance().bindService(OTTApplication.getContext());
//        } catch (Exception e) {
//            SuperLog.error(TAG,e);
//        }
    }

    //启动心跳
    public void startHeartBitService() {
        if (mHeartBeatService == null) {
            SuperLog.debug(TAG, "bindHeartBitService");
            bindHeartBitService();
        } else {
            SuperLog.debug(TAG, "startHeartBit");
            stopHeartBit();
            startHeartBit();
        }
    }

    /**
     * 绑定心跳服务,Launcher页绑定
     */
    private void bindHeartBitService() {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(this, HeartBeatService.class);
        mHeartBeatConnection = new HeartBitOTTConnection();
        bindService(serviceIntent, mHeartBeatConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 绑定NTP服务
     */
    private void bindNTPService() {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(this, NtpTimeService.class);
        mNTPConnection = new NtpOTTConnection();
        bindService(serviceIntent, mNTPConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 启动NTP服务
     */
    private void startNTPService() {
        if (null != mNtpBinder && mNtpBinder instanceof NtpTimeService.NtpBinder
                && null != ((NtpTimeService.NtpBinder) mNtpBinder).getService()) {
            SuperLog.debug(TAG, "[startNTPService] >> doGetNtpTime");
            try {
                ((NtpTimeService.NtpBinder) mNtpBinder).getService().doGetNtpTime();
            } catch (Exception e) {
                SuperLog.error(TAG, "[startNTPService] error=" + e.toString());
            }
        }
    }

    /**
     * 接受命令：启动心跳
     */
    private void startHeartBit() {
        if (mHeartBeatService != null && mHeartBeatService.isHeartBitStop()) {
            mHeartBeatService.startHeartBit();
        }
    }

    /**
     * 接受命令：停止心跳
     */
    private void stopHeartBit() {
        if (mHeartBeatService != null) {
            mHeartBeatService.stopHeartBit();
        }
    }

    /**
     * 接受命令:刷新频道信息,当前心跳查询的频道和频道动态参数接口失败时候;
     */
    private void refreshChannelInfo() {
        if (mHeartBeatService != null) {
            mHeartBeatService.refreshChannelInfo();
        }
    }

    /**
     * 刷新频道动态参数属性,channelNO
     */
    private void refreshChannelDynamicProtites() {
        if (mHeartBeatService != null) {
            mHeartBeatService.refreshChannelDynamicProtites();
        }
    }

    /**
     * 注册心跳执行命令广播接收器
     */
    private void registerHeartbeatReceiver() {
        mHeartBeatReceiver = new HeartBeatReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_START_HEARTBIT);
        filter.addAction(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_STOP_HEARTBIT);
        filter.addAction(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_HEARTBIT_CHANNEL_UPDATE);
        filter.addAction(BroadCastConstant.Heartbit.COM_HUAWEI_OTT_HEARTBIT_CHANNELNO_UPDATE);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHeartBeatReceiver, filter);
    }

    /**
     * 心跳命令接收器
     */
    class HeartBeatReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            SuperLog.info2SD(TAG, "Received heartbeat broadcast, intent action=" + intent.getAction());
            if (BroadCastConstant.Heartbit.COM_HUAWEI_OTT_START_HEARTBIT.equals(intent.getAction())) {
                stopHeartBit();
                startHeartBit();
                LauncherService.getInstance().stopCheckUpdate();
                LauncherService.getInstance().checkAndUpdate(BaseActivity.this, -1);
                TopicService.getInstance().stopCheckUpdate();
                TopicService.getInstance().checkAndUpdate(BaseActivity.this);
            } else if (BroadCastConstant.Heartbit.COM_HUAWEI_OTT_STOP_HEARTBIT.equals(intent.getAction())) {
                stopHeartBit();
                LauncherService.getInstance().stopCheckUpdate();
                TopicService.getInstance().stopCheckUpdate();
            } else if (BroadCastConstant.Heartbit.COM_HUAWEI_OTT_HEARTBIT_CHANNEL_UPDATE.equals(intent.getAction())) {
                refreshChannelInfo();
            } else if (BroadCastConstant.Heartbit.COM_HUAWEI_OTT_HEARTBIT_CHANNELNO_UPDATE.equals(intent.getAction())) {
                refreshChannelDynamicProtites();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBroadcast();
        if (getClass().getName().equals(MainActivity.class.getName())) {
            registerHeartbeatReceiver();
        }
        mXiriVoiceVodUtil = new XiriVoiceVodUtil(this, new VoiceVodListener() {
            @Override
            public void play() {

            }

            @Override
            public void pause() {

            }

            @Override
            public void forWard(long time) {

            }

            @Override
            public void backForward(long time) {

            }

            @Override
            public void finish() {
                OTTApplication.getContext().getCurrentActivity().finish();
            }

            @Override
            public void nextPlay() {

            }

            @Override
            public void prevPlay() {

            }

            @Override
            public void indexPlay(int index) {

            }

            @Override
            public void seekTo(int position) {

            }

            @Override
            public void rePlay() {

            }

            @Override
            public void doSkipHistory() {

            }

            @Override
            public void playLastEpisode() {

            }
        }, new Feedback(this));
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        SettingLanguageUtil.setUserSelectedLanguage();
    }

    protected void initBroadcast() {
        if (baseBroadcastReceiver == null) {
            // Register destruction broadcasting
            baseBroadcastReceiver = new BaseBroadcastReceiver(this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(BroadCastConstant.NetWorkException.COM_HUAWEI_OTT_NETWORK_EXCEPTION);
            filter.addAction(BroadCastConstant.COM_HUAWEI_OTT_APP_EXIT);
            //加入权限 COM_HUAWEI_OTT_SESSION_TIMEOUT_PERMISSION
            registerReceiver(baseBroadcastReceiver, filter, BroadCastConstant.Session.COM_HUAWEI_OTT_SESSION_TIMEOUT_PERMISSION, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isResume = true;
        registerHomeKeyReceiver();
        if (!getClass().getName().equals(NewVodDetailActivity.class.getName())) {

            mXiriVoiceVodUtil.startXiri();
        }

        //检测是否含有未展示的推送消息
        if (MessageDataHolder.get().getIsComplete()){
            PushMessagePresenter.showXmppMessage();
        }
        //进入Activity时上报UBD跳转

        //是否是点击广告进入此页面
        boolean isAdvert = getIntent().getBooleanExtra("isAdvert",false);
        if(isAdvert){
            UBDSwitch.getInstance().report(ADVERT_PURCHASE,this.getClass().getSimpleName());
        } else {
            UBDSwitch.getInstance().reportInBaseActivity(this.getClass().getSimpleName());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
        if (mHomeReceiver != null) {
            unregisterReceiver(mHomeReceiver);
        }

        if (null != mXiriVoiceVodUtil) {
            mXiriVoiceVodUtil.stopXiri();
        }

        //离开Activity时记录UBD From数据
        UBDSwitch.getInstance().setFromActivity(this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SuperLog.info2SD(TAG,TAG + "[HOME] destroyed.");
        if (null != mUnBinder) {
            mUnBinder.unbind();
        }
        if (null != mHeartBeatConnection) {
            unbindService(mHeartBeatConnection);
        }
        if (null != mNTPConnection) {
            unbindService(mNTPConnection);
        }
        if (null != baseBroadcastReceiver) {
            unregisterReceiver(baseBroadcastReceiver);
            baseBroadcastReceiver = null;
        }
        if (null != mHeartBeatReceiver && (getClass().getName().equals(MainActivity.class.getName()))) {
            unregisterReceiver(mHeartBeatReceiver);
            mHeartBeatReceiver = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //SuperLog.info2SD(this.getClass().getSimpleName(), "[onConfigurationChanged]");
        SettingLanguageUtil.setUserSelectedLanguage();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //SuperLog.info2SD(this.getClass().getSimpleName(), "onActivityResult");
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
                SuperLog.warn(this.getClass().getSimpleName(), "Activity result fragment index out of range: 0x" + Integer
                        .toHexString(requestCode));
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
                SuperLog.warn(this.getClass().getSimpleName(), "Activity result no fragment exists for index: 0x" + Integer
                        .toHexString(requestCode));
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
        }
    }

    /**
     * Recursive call, take effect for all sub Fragement
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        @SuppressLint("RestrictedApi") List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SuperLog.debug(TAG, "keyCode:" + keyCode);
        Tester.test(keyCode);
        /*
         * 推送弹框显示状态
         * */
            if (mIsShowing) {
            if (keyCode == KeyEvent.KEYCODE_BACK && MainActivity.TAG.equalsIgnoreCase(TAG)) {
                hideMessageDialogAndToShow();
            }
        } else {

            String className = getClass().getName();
            int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
            //首页需要等待launcher加载出来才可以,首页控制直播和回看跳转就不在base中添加了
            if (!className.equals(LiveTVActivity.class.getName()) && !className.equals(MainActivity.class.getName())) {
                if (codeValue == RemoteKeyEvent.BTV && OTTApplication.getContext().isLoginSuccess() && OTTApplication.getContext().isLoadLauncherSuccess()) {
                    //直播
                    switchLiveTV(false);
                } else if (codeValue == RemoteKeyEvent.TVOD && OTTApplication.getContext().isLoginSuccess() && OTTApplication.getContext().isLoadLauncherSuccess()) {
                    //回看(点播)
                    switchLiveTV(true);
                }
            }
            if (codeValue == RemoteKeyEvent.VOD) {
                //点播影片列表
                switchVodMainList();
            }
        }


        return super.onKeyDown(keyCode, event);
    }

    /**
     * 切换直播频道页界面
     *
     * @param isPlayback 是不是回看
     */
    protected void switchLiveTV(boolean isPlayback) {
        if (getClass().getName().equals(OnDemandVideoActivity.class.getName())) {
            finish();
        }
        if (!DeviceInfo.isForeground(OTTApplication.getContext())) {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
        }
        Intent intent = new Intent();
        if (isPlayback) {
            intent.setClass(this, CatchUpActivity.class);
            intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_TVOD);
        } else {
            intent.setClass(this, LiveTVActivity.class);
            intent.putExtra(LiveTVActivity.VIDEO_TYPE, LiveTVActivity.VIDEO_TYPE_LIVETV);
        }
        startActivity(intent);
    }

    /**
     * 切换vod页界面
     */
    public void switchVodMainList() {
        Intent intent = new Intent(this, VodMainActivity.class);
        String catagoryId = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_subject_id");
        SuperLog.debug(TAG, "switchVodMainList:" + catagoryId);
        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
            List<Subject> subjects = SessionService.getInstance().getSession().getTerminalConfigurationSettingFilterSubjects();
            if (null == subjects || subjects.isEmpty()) {
                return;
            }
            intent.putExtra(VodMainActivity.IS_CHILD_FILTER_MODE, true);
        } else {
            if (!TextUtils.isEmpty(catagoryId)) {
                intent.putExtra(VodMainActivity.CATEGORY_ID, catagoryId);
            } else {
                intent.putExtra(VodMainActivity.CATEGORY_ID, "catauto2000011029");
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(OpenVodEvent vodEvent) {
        switchVodMainList();
    }

    /**
     * 语音接受命令对直播做相关操作
     *
     * @param event VoiceEvent
     */
    @Subscribe
    public void onEvent(VoiceEvent event) {
        switch (event.getAction()) {
            case VoiceEvent.SELECT:
                String className = getClass().getName();
                //从语音启动直播之后,会传递过来channelID,mediaID,如果当前在直播界面
                //先干掉本身,那么进来进行鉴权的时候就是通过传递的channelID,mediaID进行playChannel鉴权
                if (className.equals(LiveTVActivity.class.getName())) {
                    finish();
                }
                //启动直播......
                switchLiveTV(false);
                break;
            case VoiceEvent.NEXT:
                //下一个频道
                if (!isResume) return;
                EventBus.getDefault().post(new TVGuideEvent(false));
                dispatchKeyCommand(KeyEvent.KEYCODE_DPAD_DOWN);
                break;
            case VoiceEvent.PREV:
                //上一个频道
                if (!isResume) return;
                EventBus.getDefault().post(new TVGuideEvent(false));
                dispatchKeyCommand(KeyEvent.KEYCODE_DPAD_UP);
                break;
        }
    }

    /**
     * 语音分发keycode操作命令
     *
     * @param keycode
     */
    private void dispatchKeyCommand(int keycode) {
        ThreadTool.switchNewThread(new Action() {
            @Override
            public void run() {
                try {
                    //必须在子线程中调用
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keycode);
                } catch (Exception e) {
                    if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        SuperLog.error(TAG, "[VoiceEvent.NEXT]: Instrumentation ERROR");
                    } else {
                        SuperLog.error(TAG, "[VoiceEvent.PREV]: Instrumentation ERROR");
                    }
                }
            }
        });
    }

//    private class HomeKeyReceiver extends BroadcastReceiver {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            SuperLog.debug(TAG, "HomeKeyReceiver received action:" + action);
//            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
//                String reason = intent.getStringExtra("reason");
//                if ("homekey".equals(reason) /*&& !isPlayingAd*/) {
//                    if ( DeviceInfo.isSkyworth() ) {
//                        //创维电视关闭其他Activity MainActivity由系统拉起
//                        if (!(BaseActivity.this instanceof MainActivity)) {
//                            SuperLog.debug(TAG, BaseActivity.this.getClass().getSimpleName() + " has finished when home pressed.");
//                            BaseActivity.this.finish();
//                        }
//                    } else {
//                        //非创维电视直接启动MainActivity
//                        if (!(BaseActivity.this instanceof MainActivity)) {
//                            SuperLog.debug(TAG, "MainActivity started.");
//                            Intent intentHome = new Intent(context, MainActivity.class);
//                            context.startActivity(intentHome);
//                        }
//                    }
//                }
//            }
//        }
//    }

    private class HomeKeyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            SuperLog.debug(TAG, "HomeKeyReceiver action:" + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if ("homekey".equals(reason) /*&& !isPlayingAd*/) {
                    RefreshManager.getInstance().getScreenPresenter().exit();//如果有屏保需要关闭屏保
                    if (!(BaseActivity.this instanceof MainActivity)) {
                        // 当前页面不是MainActivity时首页(MainActivity)
                        SuperLog.debug(TAG, "Begin to Start MainActivity.");
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
            }
        }
    }

    @Subscribe
    public void onEvent(SwitchEpgEvent epgEvent) {
        //切换桌面与当前桌面相同直接退出
        if ((epgEvent.getType() == Constant.DesktopType.NORMAL && (!SharedPreferenceUtil.getInstance().getIsChildrenEpg() && !SharedPreferenceUtil.getInstance().getIsSimpleEpg()))
                || (epgEvent.getType() == Constant.DesktopType.SIMPLE && SharedPreferenceUtil.getInstance().getIsSimpleEpg())
                || (epgEvent.getType() == Constant.DesktopType.CHILD && SharedPreferenceUtil.getInstance().getIsChildrenEpg())) {
            return;
        }

        OTTApplication.getContext().getMainActivity().switchLauncher(epgEvent.getType());
        if (!OTTApplication.getContext().getCurrentActivity().getLocalClassName().contains(MainActivity.TAG)) {
            OTTApplication.getContext().getCurrentActivity().finish();
        }
    }

    /*
     * 从消息列表点击的弹框
     * 是否正在显示
     * */
    @Subscribe
    public void onEvent(MessageDialogIsShowEvent event) {
        SuperLog.info2SD(TAG, "mIsShowing = " + event.isShow());
        mIsShowing = event.isShow();
    }

    /*
     * 控制推送消息Dialog显示与否
     * */
    @Subscribe
    public void onEvent(MessageDialogEvent messageDialogEvent) {
        SuperLog.info2SD(TAG, "EventBus receive [MessageDialogEvent] event. object=" + messageDialogEvent);
        if (!(this == OTTApplication.getContext().getCurrentActivity())) {
            //由于消息在基类注册,因此Activity栈内都可收到,只有当前展示的Activity才处理推送消息
            SuperLog.info2SD(TAG, "This activity is not foreground. object=" + this);
            return;
        }

        //处于加载默认图片  不弹框
        /*是否在刚开机加载界面
        如果在则不显示，跳到MainActivity再弹框显示*/
        if (!MessageDataHolder.get().getIsComplete()) {
            SuperLog.info2SD(TAG, "Ad is showing, push message will be delay.");
            return;
        }

        //消息转化事件，跳到了其他界面，关闭dialog
        if (messageDialogEvent.isToOtherPage()) {
            hideMessageDialog();
            SuperLog.info2SD(TAG, "Jump to other page, close dialog.");
            return;
        }

        /*
         * 防止多个推送弹框弹出
         * */
        if (!mIsShowing && messageDialogEvent.isShow()) {
            if (!TextUtils.isEmpty(messageDialogEvent.getBody())) {
                SuperLog.info2SD(TAG, "messageDialogEvent.getBody() != null");
                showMessageDialogBase(messageDialogEvent.getBody());
            } else if (MessageDataHolder.get().getMessageAllList().size() > 0) {
                SuperLog.info2SD(TAG, "mMessageDataHolder.get().getMessageAllList().size() > 0");
                showMessageDialogBase(MessageDataHolder.get().getMessageList());
            }
        } else if (mIsShowing && messageDialogEvent.isShow()) {
            //开机展示手机移动端书签
            SuperLog.info2SD(TAG, "开机展示手机移动端书签.");
            if (!TextUtils.isEmpty(messageDialogEvent.getBody())) {
                MessageDataHolder.get().setMessageMobileAndEpg(messageDialogEvent.getBody());
            }
        } else {
            SuperLog.info2SD(TAG, "hideMessageDialogAndToShow().");
            hideMessageDialogAndToShow();
        }
    }

    /*
     * 推送消息浏览Dialog显示
     * */
    public void showMessageDialogBase(String body) {
        SuperLog.info2SD(TAG, "mIsShowing = true");
        RefreshManager.getInstance().getScreenPresenter().exit();//如果有屏保需要关闭屏保
        mIsShowing = true;
        mMessageDialog = new MessageDialog(BaseActivity.this);
        if (isReminderType(body)) {
            ReminderMessage reminder = json2Object(body, ReminderMessage.class);
            if (reminder != null) {
                showReminderDialog(reminder);
            } else {
                SuperLog.error(TAG, "Reminder Body is null");
            }
        } else {
            mMessageDialog.setBody(body);
            mMessageDialog.show();
        }
    }

    public void hideMessageDialogAndToShow() {
        //先隐藏
        hideMessageDialog();

        //在判断是否还有待展示的消息
        String messageString = MessageDataHolder.get().getMessageList();
        if (!TextUtils.isEmpty(messageString)) {
            showMessageDialogBase(messageString);
        }
    }

    private void hideMessageDialog() {
        mIsShowing = false;
        if (mMessageDialog != null && mMessageDialog.isShowing()) {
            mMessageDialog.dismiss();
            mMessageDialog = null;
            mIsShowing = false;
        }
    }

    /**
     * 是否是提醒类型
     *
     * @param body 消息体
     * @return boolean
     */
    private boolean isReminderType(String body) {
        return body.contains(REMINDER_TYPE);
    }

    /**
     * 展示开机提醒弹窗
     */
    private void showReminderDialog(ReminderMessage reminder) {
        ReminderDialog reminderDialog = new ReminderDialog(this, reminder);
        reminderDialog.show();
    }

    @Override
    public void loadAdvertContentSuccess(List<AdvertContent> listAdvertContent) {
    }

    @Override
    public void loadAdvertFail() {
    }

    public boolean getIsResume() {
        return isResume;
    }

    public void setmUnBinder(Unbinder mUnBinder) {
        this.mUnBinder = mUnBinder;
    }

    //用于判断用户是否有操作,如果用户有操作,就是调用此方法
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //记录用户操作行为
        RefreshManager.getInstance().updateUserAction(true, RefreshManager.TimerType.ALL);
        return super.dispatchKeyEvent(event);
    }
}