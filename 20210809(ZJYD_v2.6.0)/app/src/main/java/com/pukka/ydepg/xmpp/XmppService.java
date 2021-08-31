package com.pukka.ydepg.xmpp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;

import com.huawei.stb.xmpp.IXMPPCallback;
import com.huawei.stb.xmpp.IXMPPService;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.pushmessage.presenter.PushMessagePresenter;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.report.ubd.extension.XmppData;
import com.pukka.ydepg.common.report.ubd.scene.UBDXmpp;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.node.Configuration;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.xmpp.bean.XmppConstant;
import com.pukka.ydepg.xmpp.bean.XmppConstant.MessageKey;
import com.pukka.ydepg.xmpp.bean.XmppConstant.MessageType;
import com.pukka.ydepg.xmpp.bean.XmppConstant.Operation;
import com.pukka.ydepg.xmpp.utils.XmppUtil;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.pukka.ydepg.xmpp.bean.XmppConstant.MessageKey.CONTENT_CODE;
import static com.pukka.ydepg.xmpp.bean.XmppConstant.SASLMECHANISM_TOKEN;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.xmpp.XmppService.java
 * @author:xj
 * @date: 2018-01-16 17:16
 */
public class XmppService implements PlayerControl.HandXMPPMessage {

    private static final String TAG = XmppService.class.getSimpleName();

    private static XmppService singleObject;

    private IXMPPService ixmppService;

    private String mHandler = null;

    private String mJID;

    private String mUserToken;

    private String connectStatus = XmppConstant.ConnectionStatus.CONNECTIONFAIL;

    private Timer timer;

    private KDXFControl   mKDXFControl   = new KDXFControl();

    private PlayerControl mPlayerControl = new PlayerControl(this);

    private XmppHandler   handler;

    private HandlerThread handlerThread;

    private long checkInterval = XmppUtil.getCheckInterval();

    @Override
    public void handleXmppMessage(int operation, String pullMessage, String handler, String jid) {
        try {
            switch (operation){
                case Operation.PULL_OPERATION:
                    SuperLog.info2SD(TAG,"sendXMPPMessage from [" + mJID + "] to [" + jid + "], [Pull]message:" + pullMessage);
                    ixmppService.sendXMPPMessage(handler,jid,pullMessage);
                    break;
            }
        }catch (Exception e){
            SuperLog.error(TAG,e);
        }

    }

    private static class XmppHandler extends Handler{
        WeakReference<XmppService> mReference;

        XmppHandler(Looper looper,XmppService service) {
            super(looper);
            mReference=new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(null!=mReference && null!=mReference.get()){
                if(msg.what==0){
                    try {
                        mReference.get().mPlayerControl.handleRemotePlayChannel(new JSONObject((String) msg.obj));
                    } catch (Exception e) {
                        SuperLog.error(TAG,e);
                    }
                }
            }
        }
    }

    private class XmppConnectionTask extends TimerTask {
        @Override
        public void run() {
            try{
                SuperLog.info2SDDebug(TAG,"XMPP connection check timer executed.");
                checkXmppConnection();
                timer.schedule(new XmppConnectionTask(),checkInterval);
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SuperLog.info2SD(TAG, " onServiceConnected,begin to initXmppConnection");
            handlerThread = new HandlerThread(TAG);
            handlerThread.start();

            handler = new XmppHandler(handlerThread.getLooper(), XmppService.this);


            timer = new Timer();
            timer.schedule(new XmppConnectionTask(),XmppUtil.FIRST_CHECK_DELAY);//开机2分钟第一次检查XMPP连接是否正常

            ixmppService = IXMPPService.Stub.asInterface(service);
            initXmppConnectionInChildThread();
        }

        //IDL解绑后,下一次平台心跳会触发UpdateToken进而重连
        @Override
        public void onServiceDisconnected(ComponentName name) {
            SuperLog.error(TAG, " ========== onServiceDisconnected[XmppService]. Destroy XMPP connection ========== ");
            connectStatus = XmppConstant.ConnectionStatus.CONNECTIONFAIL;
            ixmppService  = null;
            mHandler      = null;
            mUserToken    = null;

            if (null != handler) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }

            if ( null != handlerThread) {
                handlerThread.quit();
                handlerThread = null;
            }

            //计时器停止后,会由下一次平台心跳触发updateToken,进而触发检查XMPP链接然后重连
            if( null != timer ){
                timer.cancel();
                timer = null;
            }
        }
    };

    //XMPP回调
    private IXMPPCallback mXmppCallBack = new IXMPPCallback.Stub() {
        @Override
        public String xmppConnectionStatus(String handler, String connectionStatus) {
            SuperLog.info2SD(TAG, " ========== Received Callback [xmppConnectionStatus],handler=" + handler + " [CurrentStatus]=" + connectStatus + " [ReceivedStatus]=" + connectionStatus + " (success=1,failed=0) ========== ");

            if (TextUtils.equals(connectionStatus, XmppConstant.ConnectionStatus.CONNECTIONFAIL)) {
                //失去连接
                if (XmppConstant.ConnectionStatus.CONNECTIONSUCCESS.equals(connectStatus)) {
                    //当前状态为链接成功，此时XMPP_CLIENT上报断链则重连,否则不需要重连,用于过滤XMPPCLIENT建链过程中上报status
                    SuperLog.error(TAG, "XMPP connection failed, re-initXmppConnection");
                    reConnectXmpp();
                }
            } else {//链接成功
                //上报服务器
                SuperLog.info2SD(TAG, " <<<<<<<<<< Establish XMPP connection successfully. >>>>>>>>>>");
                //EpgToast.showLongToast(mContext,"XMPP连接建立成功");
                //SubmitDeviceInfoController.submitDeviceInfo();
            }
            connectStatus = connectionStatus;
            return XmppConstant.XMPP_OPERATION_FINISH;
        }

        @Override
        public String xmppMessageArrived(String handler, String jid, String subject, String body, String thread, String report, String delay){
            SuperLog.info2SD(TAG, " ========== Received Callback [xmppMessageArrived],handler=" + handler +
                    "\n\t[JID](from) = " + jid     +
                    "\n\t[subject]   = " + subject +
                    "\n\t[body]      = " + body    +
                    "\n\t[thread]    = " + thread  + "\t(sys-ctrl:控制消息 other:推送消息)" +
                    "\n\t[report]    = " + report  +
                    "\n\t[delay]     = " + delay);
            try {
                if (TextUtils.equals(thread, MessageType.SYS_CTRL)) {
                    //控制消息 thread=sys-ctrl
                    JSONObject json = new JSONObject(body);
                    //流水号
                    String transactionId = XmppUtil.getString(json,XmppConstant.MessageKey.TRANSCTION_ID);
                    switch (XmppUtil.getXmppOperation(json)){
                        case Operation.SEEK_CONTROL:
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            int position = json.getInt(MessageKey.SEEK_POSITION);
                            mPlayerControl.remoteSeek(position);
                            break;
                        case Operation.PLAY_CONTROL:
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            int trickPlayMode = json.getInt(MessageKey.TRICK_PLAY_MODE);
                            mPlayerControl.remoteControl(trickPlayMode);
                            break;
                        case Operation.GET_PLAYER_STATE:
                            String playerState = mPlayerControl.getPlayerState();
                            SuperLog.info2SD(TAG,"sendXMPPMessage from [" + mJID + "] to [" + jid + "], [GetPlayerState]message:" + playerState);
                            ixmppService.sendXMPPMessage(handler,jid,playerState);
                            break;
                        case Operation.PULL_OPERATION:
                            String contentCode = XmppUtil.getString(json,CONTENT_CODE);
                            mPlayerControl.handlePullMessage(contentCode,handler,jid);
                            break;
                        case Operation.GET_VOLUME_STATE:
                            String playerVolume = mPlayerControl.getPlayerVolume();
                            SuperLog.info2SD(TAG,"sendXMPPMessage from [" + mJID + "] to [" + jid + "], [GetVolume]message:" + playerVolume);
                            ixmppService.sendXMPPMessage(handler,jid,playerVolume);
                            break;
                        case Operation.SET_VOLUME:
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            mPlayerControl.setPlayerVolume(json);
                            break;
                        case Operation.PLAY_EXIT:
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            mPlayerControl.exitPlay();
                            break;
                        case Operation.KDXF_CONTROL:
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            mKDXFControl.sendMessageToKDXF(json.getString(XmppConstant.MessageKey.PLAY_URL));
                            UBDXmpp.recordXmppOperation(XmppData.OptType.VOICE,body,transactionId);
                            break;
                        case Operation.PLAY_LIVE_TV:
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            if(XmppService.this.handler.hasMessages(0)){
                                XmppService.this.handler.removeMessages(0);
                            }
                            Message msg= XmppService.this.handler.obtainMessage();
                            msg.obj=body;
                            msg.what=0;
                            XmppService.this.handler.sendMessageDelayed(msg,700);
                            UBDXmpp.recordXmppOperation(XmppData.OptType.PUSH_LIVE,body,transactionId);
                            ixmppService.sendXMPPMessage(handler,jid,XmppUtil.getPushReturnMsg(transactionId));
                            break;
                        case Operation.PLAY_VOD:
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            mPlayerControl.handleRemotePlayVOD(json);
                            UBDXmpp.recordXmppOperation(XmppData.OptType.PUSH_VOD,body,transactionId);
                            ixmppService.sendXMPPMessage(handler,jid,XmppUtil.getPushReturnMsg(transactionId));
                            break;
                        case Operation.PLAY_VR:
                            //VR视频操作;
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            mPlayerControl.playVR(json);
                            break;
                        case Operation.SWITCH_DESKTOP:
                            //切换桌面
                            RefreshManager.getInstance().getScreenPresenter().exit();
                            String playUrl = XmppUtil.getString(json, MessageKey.PLAY_URL);
                            XmppManager.switchDesktop(playUrl);
                            break;
                        default:
                            break;
                    }
                } else {
                    //推送消息
                    //XmppManager.handlePushMessage(body);
                    PushMessagePresenter.pushMessage(body);
                }
            } catch (Exception e) {
                SuperLog.error(TAG,e);
            }
            return null;
        }
    };

    private XmppService() {}

    public static XmppService getInstance() {
        if (null == singleObject) {
            singleObject = new XmppService();
        }
        return singleObject;
    }

    private void initXmppConnectionInChildThread(){
        if(Thread.currentThread().getId() == 1){
            //如果当前是主线程则起子线程执行
            new Thread() {
                @Override
                public void run() {
                    initXmpp();
                }
            }.start();
        } else {
            //如果当前是子线程则直接初始化XMPP,防止在子线程中再起子线程报异常java.lang.InternalError: Thread starting during runtime shutdown
            initXmpp();
        }
    }

    private void initXmpp(){
        Session session = SessionService.getInstance().getSession();
        Configuration configuration = session.getServerConfiguration();

        String packageName = "com.pukka.ydepg";
        String pushServerUrl="";
        if(null!=configuration) {
            String ottIMPIP = configuration.getOTTIMPIP();
            SuperLog.info2SDDebug(TAG, "ottIMPIP is " + ottIMPIP);
            if(ottIMPIP.contains(":")){
                if(!ottIMPIP.contains("[")){
                    ottIMPIP = "[" + ottIMPIP;
                }
                if(!ottIMPIP.contains("]")){
                    ottIMPIP = ottIMPIP + "]";
                }
                SuperLog.info2SDDebug(TAG, "IPV6 format ottIMPIP is " + ottIMPIP);
            }
            pushServerUrl= ottIMPIP + ":" + configuration.getOTTIMPPort();
            String deviceID = TextUtils.isEmpty(session.getDeviceId()) ? "" : ("/" + session.getDeviceId());
            //SuperLog.info2SD(TAG, "XMPP deviceID = " + deviceID);
            mJID = session.getUserId() + "@" + configuration.getIMDomain() + deviceID;
        }
        //如果使用deviceID会收到两次推送消息,原因不明,先改为不携带deviceID
        //mJID = session.getUserId() + "@" + configuration.getIMDomain();

        String sasIID = session.getUserId();
        String sasLPassword = "";
        mUserToken = SessionService.getInstance().getSession().getUserToken();

        try {
            StringBuffer sb = new StringBuffer("InitXmppConnection input param is as followed:>>>")
                    .append("\n\tpackageName  =").append(packageName)           //com.pukka.ydepg
                    .append("\n\tpushServerUrl=").append(pushServerUrl)         //117.148.130.134:6050(示例)
                    .append("\n\tmJID         =").append(mJID)                  //hwtest910@push.huawei.com/10001193205430(示例)
                    .append("\n\tsasIID       =").append(sasIID)                //hwtest910(示例)
                    .append("\n\tsasLPassword =").append(sasLPassword)          //为空 ""
                    .append("\n\tuserToken    =").append(mUserToken)            //7lox97lox96SkEfQlY9kZTqlwbmEOmNO(示例)
                    .append("\n\timpAuthType  =").append(SASLMECHANISM_TOKEN);  //1
            SuperLog.info2SDSecurity(TAG,sb.toString());
            if ( ixmppService == null || TextUtils.isEmpty(sasIID) || TextUtils.isEmpty(mUserToken) ) {
                SuperLog.error(TAG, "Input param[sasIID or userToken] is null,initXmpp failed.");
                return;
            }
            mHandler = ixmppService.initialXMPPConnection(
                    packageName,
                    pushServerUrl,
                    mJID,
                    sasIID,
                    sasLPassword,
                    mUserToken,
                    SASLMECHANISM_TOKEN);
            if (!isHandlerValid(mHandler)) {
                SuperLog.error(TAG, "Initialize XMPP connection failed. XMPP handler=" + mHandler);
                return;
            }

            String result = ixmppService.registerXMPPCallback(mHandler, mXmppCallBack);
            SuperLog.info2SD(TAG, "Register XMPP callback finished. Result=" + result + " Handler=" + mHandler);
            return;
        } catch (Exception e) {
            SuperLog.error(TAG,e);
            return;
        }
    }

    private void deInitialXMPPConnection() {
        if (null != ixmppService && isHandlerValid(mHandler)) {
            SuperLog.info2SD(TAG, "Begin to deInitialXMPPConnection. [XMPPHandler]=" + mHandler);
            try {
                String result = ixmppService.deInitialXMPPConnection(mHandler);
                SuperLog.info2SD(TAG, "deInitialXMPPConnection result=["+result+"]\t0:success -1:fail");
                if ( result.equals(XmppConstant.ControlStatus.SUCCESS)){
                    connectStatus = XmppConstant.ConnectionStatus.CONNECTIONFAIL;
                    mHandler = null;
                }
            } catch (RemoteException e) {
                SuperLog.error(TAG,e);
            }
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        //绑定服务端的service
        String name = "com.huawei.stb.xmpp";
        String act  = "com.huawei.stb.xmpp.XMPPService";
        intent.setAction(name);
        //intent.setPackage(name);
        //新版本（5.0后）必须显式intent启动 绑定服务
        intent.setComponent(new ComponentName(name, act));
        if(!OTTApplication.getContext().bindService(intent, conn, BIND_AUTO_CREATE)){
            SuperLog.error(TAG,"Bind XMPP service failed. Please see system logcat for details.");
        }
    }

    private boolean isHandlerValid(String handler) {
        if (TextUtils.isEmpty(handler) || handler.equals("-1")) {
            SuperLog.error(TAG, "handler is invalid.handler=" + handler);
            return false;
        } else {
            return true;
        }
    }

    // 调用场景 1:ZJLogin之后(即Session超时后重新登录) 2:心跳之后
    public void updateToken(String userToken) {
        // 如果XMPP连接状态不正常,则用重连替代更新token
        if( checkXmppConnection()){
            return;
        }

        if ( mUserToken.equals(userToken)) {
            SuperLog.info2SD(TAG, "Token is not change, no need to update XMPP token.");
            return;
        }

        SuperLog.info2SDSecurity(TAG, " <<<<<<<<<< Token updated, detail info is as followed: >>>>>>>>>> " +
                "\n\tCurrent Token = " + mUserToken +
                "\n\tNew     Token = " + userToken);

        // 连接正常则更新token，用于XMPP连接正常时，token过期后进行更新
        SuperLog.info2SD(TAG, "XMPP connection is normal, begin to update token.");
        if (userToken.equals(mUserToken)) {
            SuperLog.info2SD(TAG, "Token is not change, no need to update XMPP token.");
            return;
        }

        if (ixmppService == null || !isHandlerValid(mHandler) || TextUtils.isEmpty(mJID) ) {
            SuperLog.error(TAG, "Initialization param is null, can not update XMPP token.");
            return;
        }

        try {
            mUserToken = userToken;
            String result = ixmppService.updateAccessToken(mHandler, mJID, mUserToken);
            SuperLog.info2SD(TAG, "XMPP token updated. Result=" + result + "(0:Success)");
        } catch ( Exception e) {
            SuperLog.error(TAG,e);
        }
    }

    // 返回参数 connectionStatus 当前连接的状态， 当前可取值如下： 1：连接成功 0：失去连接  -1：查询失败
    // 当查询为 0/-1 时，业务APK需要销毁当前连接，进行重新初始化，保证 XMPP 服务可用。
    // 调用场景 1:内部使用  2:供H5使用
    public String checkConnection(){
        String status = XmppConstant.ConnectionStatus.UNKNOWN_STATE;
        try{
            if( null != ixmppService ){
                status = ixmppService.checkConnection(mHandler);
            } else {
                SuperLog.error(TAG,"XMPP AIDL object is null. Can not get xmpp connection status.");
                status = XmppConstant.ConnectionStatus.UNKNOWN_STATE;
            }
        } catch (Exception e){
            SuperLog.error(TAG,e);
            status = XmppConstant.ConnectionStatus.UNKNOWN_STATE;
        } finally {
            SuperLog.info2SD(TAG,"XMPP connection status : " + status + "\t[1:连接成功 0:失去连接 -1:查询失败]");
            return status;
        }
    }

    // 检查XMPP状态,如果不正常则重连
    // 调用场景 1:网络状态由断连到重连时 2:updateToken中(ZJLogin之后) 3:定时器 4:切换账号(强制重连重连force=true)
    public boolean checkXmppConnection(){
        String status = checkConnection();
        if(!XmppConstant.ConnectionStatus.CONNECTIONSUCCESS.equals(status)){
            reConnectXmpp();
            return true;
        } else {
            return false;
        }
    }

    //调用场景 1：在checkXmppConnection中  2:XMPP回调通知断联,且之前状态为链接成功 3:供H5使用
    public void reConnectXmpp() {
        try {
            if (ixmppService == null) {
                SuperLog.info2SD(TAG, "AIDL service object ixmppService is null, begin to bind XMPP service.");
                bindService();
            } else {
                if (isHandlerValid(mHandler)) {
                    deInitialXMPPConnection();
                }
                initXmppConnectionInChildThread();
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }
    }
}