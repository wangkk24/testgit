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

    protected int isLauncher = 0;//???????????????  1 ????????? 2 ????????????

    private Unbinder mUnBinder;

    private HeartBeatService mHeartBeatService = null;

    private ServiceConnection mHeartBeatConnection;

    private ServiceConnection mNTPConnection;

    private BaseActivity.HomeKeyReceiver mHomeReceiver;

    private IBinder mNtpBinder;

    private HeartBeatReceiver mHeartBeatReceiver;

    /*
     * ????????????Dialog
     * */
    private MessageDialog mMessageDialog = null;

    /*
     * ????????????????????????Dialog????????????
     * ??????false ???
     * */
    private boolean mIsShowing = false;

    /**
     * ??????activity????????????????????????
     */
    private boolean isResume;

    private static final String REMINDER_TYPE = "REMINDER_TYPE";

    private XiriVoiceVodUtil mXiriVoiceVodUtil;

    /**
     * ??????Connection
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
        //??????NTP??????
        if (null == mNtpBinder) {
            bindNTPService();
        } else {
            startNTPService();
        }

        //??????ZJLogin?????????XMPP??????Token???????????????XMPP?????????,????????????????????????XMPP
//        //??????xmpp
//        try {
//            SuperLog.info2SD(TAG, "Bind XMPP service when start");
//            XmppService.getInstance().bindService(OTTApplication.getContext());
//        } catch (Exception e) {
//            SuperLog.error(TAG,e);
//        }
    }

    //????????????
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
     * ??????????????????,Launcher?????????
     */
    private void bindHeartBitService() {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(this, HeartBeatService.class);
        mHeartBeatConnection = new HeartBitOTTConnection();
        bindService(serviceIntent, mHeartBeatConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * ??????NTP??????
     */
    private void bindNTPService() {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(this, NtpTimeService.class);
        mNTPConnection = new NtpOTTConnection();
        bindService(serviceIntent, mNTPConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * ??????NTP??????
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
     * ???????????????????????????
     */
    private void startHeartBit() {
        if (mHeartBeatService != null && mHeartBeatService.isHeartBitStop()) {
            mHeartBeatService.startHeartBit();
        }
    }

    /**
     * ???????????????????????????
     */
    private void stopHeartBit() {
        if (mHeartBeatService != null) {
            mHeartBeatService.stopHeartBit();
        }
    }

    /**
     * ????????????:??????????????????,??????????????????????????????????????????????????????????????????;
     */
    private void refreshChannelInfo() {
        if (mHeartBeatService != null) {
            mHeartBeatService.refreshChannelInfo();
        }
    }

    /**
     * ??????????????????????????????,channelNO
     */
    private void refreshChannelDynamicProtites() {
        if (mHeartBeatService != null) {
            mHeartBeatService.refreshChannelDynamicProtites();
        }
    }

    /**
     * ???????????????????????????????????????
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
     * ?????????????????????
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
            //???????????? COM_HUAWEI_OTT_SESSION_TIMEOUT_PERMISSION
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

        //??????????????????????????????????????????
        if (MessageDataHolder.get().getIsComplete()){
            PushMessagePresenter.showXmppMessage();
        }
        //??????Activity?????????UBD??????

        //????????????????????????????????????
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

        //??????Activity?????????UBD From??????
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
         * ????????????????????????
         * */
            if (mIsShowing) {
            if (keyCode == KeyEvent.KEYCODE_BACK && MainActivity.TAG.equalsIgnoreCase(TAG)) {
                hideMessageDialogAndToShow();
            }
        } else {

            String className = getClass().getName();
            int codeValue = RemoteKeyEvent.getInstance().getKeyCodeValue(keyCode);
            //??????????????????launcher?????????????????????,??????????????????????????????????????????base????????????
            if (!className.equals(LiveTVActivity.class.getName()) && !className.equals(MainActivity.class.getName())) {
                if (codeValue == RemoteKeyEvent.BTV && OTTApplication.getContext().isLoginSuccess() && OTTApplication.getContext().isLoadLauncherSuccess()) {
                    //??????
                    switchLiveTV(false);
                } else if (codeValue == RemoteKeyEvent.TVOD && OTTApplication.getContext().isLoginSuccess() && OTTApplication.getContext().isLoadLauncherSuccess()) {
                    //??????(??????)
                    switchLiveTV(true);
                }
            }
            if (codeValue == RemoteKeyEvent.VOD) {
                //??????????????????
                switchVodMainList();
            }
        }


        return super.onKeyDown(keyCode, event);
    }

    /**
     * ???????????????????????????
     *
     * @param isPlayback ???????????????
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
     * ??????vod?????????
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
     * ??????????????????????????????????????????
     *
     * @param event VoiceEvent
     */
    @Subscribe
    public void onEvent(VoiceEvent event) {
        switch (event.getAction()) {
            case VoiceEvent.SELECT:
                String className = getClass().getName();
                //???????????????????????????,???????????????channelID,mediaID,???????????????????????????
                //???????????????,??????????????????????????????????????????????????????channelID,mediaID??????playChannel??????
                if (className.equals(LiveTVActivity.class.getName())) {
                    finish();
                }
                //????????????......
                switchLiveTV(false);
                break;
            case VoiceEvent.NEXT:
                //???????????????
                if (!isResume) return;
                EventBus.getDefault().post(new TVGuideEvent(false));
                dispatchKeyCommand(KeyEvent.KEYCODE_DPAD_DOWN);
                break;
            case VoiceEvent.PREV:
                //???????????????
                if (!isResume) return;
                EventBus.getDefault().post(new TVGuideEvent(false));
                dispatchKeyCommand(KeyEvent.KEYCODE_DPAD_UP);
                break;
        }
    }

    /**
     * ????????????keycode????????????
     *
     * @param keycode
     */
    private void dispatchKeyCommand(int keycode) {
        ThreadTool.switchNewThread(new Action() {
            @Override
            public void run() {
                try {
                    //???????????????????????????
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
//                        //????????????????????????Activity MainActivity???????????????
//                        if (!(BaseActivity.this instanceof MainActivity)) {
//                            SuperLog.debug(TAG, BaseActivity.this.getClass().getSimpleName() + " has finished when home pressed.");
//                            BaseActivity.this.finish();
//                        }
//                    } else {
//                        //???????????????????????????MainActivity
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
                    RefreshManager.getInstance().getScreenPresenter().exit();//?????????????????????????????????
                    if (!(BaseActivity.this instanceof MainActivity)) {
                        // ??????????????????MainActivity?????????(MainActivity)
                        SuperLog.debug(TAG, "Begin to Start MainActivity.");
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
            }
        }
    }

    @Subscribe
    public void onEvent(SwitchEpgEvent epgEvent) {
        //?????????????????????????????????????????????
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
     * ??????????????????????????????
     * ??????????????????
     * */
    @Subscribe
    public void onEvent(MessageDialogIsShowEvent event) {
        SuperLog.info2SD(TAG, "mIsShowing = " + event.isShow());
        mIsShowing = event.isShow();
    }

    /*
     * ??????????????????Dialog????????????
     * */
    @Subscribe
    public void onEvent(MessageDialogEvent messageDialogEvent) {
        SuperLog.info2SD(TAG, "EventBus receive [MessageDialogEvent] event. object=" + messageDialogEvent);
        if (!(this == OTTApplication.getContext().getCurrentActivity())) {
            //???????????????????????????,??????Activity??????????????????,?????????????????????Activity?????????????????????
            SuperLog.info2SD(TAG, "This activity is not foreground. object=" + this);
            return;
        }

        //????????????????????????  ?????????
        /*??????????????????????????????
        ??????????????????????????????MainActivity???????????????*/
        if (!MessageDataHolder.get().getIsComplete()) {
            SuperLog.info2SD(TAG, "Ad is showing, push message will be delay.");
            return;
        }

        //???????????????????????????????????????????????????dialog
        if (messageDialogEvent.isToOtherPage()) {
            hideMessageDialog();
            SuperLog.info2SD(TAG, "Jump to other page, close dialog.");
            return;
        }

        /*
         * ??????????????????????????????
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
            //?????????????????????????????????
            SuperLog.info2SD(TAG, "?????????????????????????????????.");
            if (!TextUtils.isEmpty(messageDialogEvent.getBody())) {
                MessageDataHolder.get().setMessageMobileAndEpg(messageDialogEvent.getBody());
            }
        } else {
            SuperLog.info2SD(TAG, "hideMessageDialogAndToShow().");
            hideMessageDialogAndToShow();
        }
    }

    /*
     * ??????????????????Dialog??????
     * */
    public void showMessageDialogBase(String body) {
        SuperLog.info2SD(TAG, "mIsShowing = true");
        RefreshManager.getInstance().getScreenPresenter().exit();//?????????????????????????????????
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
        //?????????
        hideMessageDialog();

        //???????????????????????????????????????
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
     * ?????????????????????
     *
     * @param body ?????????
     * @return boolean
     */
    private boolean isReminderType(String body) {
        return body.contains(REMINDER_TYPE);
    }

    /**
     * ????????????????????????
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

    //?????????????????????????????????,?????????????????????,?????????????????????
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //????????????????????????
        RefreshManager.getInstance().updateUserAction(true, RefreshManager.TimerType.ALL);
        return super.dispatchKeyEvent(event);
    }
}