package com.pukka.ydepg.xmpp.utils;

import android.content.Intent;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.customui.dialog.ParentSetCenterDialog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.xmpp.bean.CommonMessage;
import com.pukka.ydepg.xmpp.bean.XmppConstant;

import org.json.JSONObject;

import static com.pukka.ydepg.common.utils.ZJVRoute.LauncherElementContentType.SWITCH_EPG;

public class XmppUtil {

    private static final String TAG = XmppUtil.class.getSimpleName();

    //首次检查XMPP链接延时(单位ms)
    public final static long FIRST_CHECK_DELAY = 2*60*1000L;

    //默认检查XMPP链接状态时间间隔(单位s)  15分钟 测试阶段使用5分钟
    private final static long DEFAULT_CHECK_INTERVAL = 15*60L;

    //单位毫秒
    public static long getCheckInterval() {
        long interval;
        try{
            //配置参数,单位秒
            String time = SessionService.getInstance().getSession().getTerminalConfigurationValue("XMPP_CHECK_INTERVAL");
            interval = Long.parseLong(time);
        } catch (Exception e){
            SuperLog.error(TAG,e);
            interval = DEFAULT_CHECK_INTERVAL;
        }
        SuperLog.info2SD(TAG,"XMPP check interval is (unit:s)：" + interval);
        return interval*1000;
    }

    public static int getXmppOperation(JSONObject json){
        if( isPlayVR(json) ){
            SuperLog.info2SD(TAG,"Received [Seek] xmpp message.");
            return XmppConstant.Operation.PLAY_VR;
        } else if( isSeekCommand(json) ){
            SuperLog.info2SD(TAG,"Received [Seek] xmpp message.");
            return XmppConstant.Operation.SEEK_CONTROL;
        } else if ( isKDXFControlCommand(json)){
            SuperLog.info2SD(TAG,"Received [KDXF control] xmpp message.");
            return XmppConstant.Operation.KDXF_CONTROL;
        } else if ( isPlayVod(json) ){
            SuperLog.info2SD(TAG,"Received [PlayVOD] xmpp message.");
            return XmppConstant.Operation.PLAY_VOD;
        } else if ( isGetAppState(json )){
            if("0".equals(getString(json,XmppConstant.MessageKey.MSG_TYPE))){
                //msgType当前枚举值为0 表示TV频道及搜索结果页拉屏
                SuperLog.info2SD(TAG,"Received [Pull operation] xmpp message.");
                return XmppConstant.Operation.PULL_OPERATION;
            } else {
                //没有msgType表示获取播放状态
                SuperLog.info2SD(TAG,"Received [Get STB player state] xmpp message.");
                return XmppConstant.Operation.GET_PLAYER_STATE;
            }
        } else if ( isGetPlayerVolume(json )){
            SuperLog.info2SD(TAG,"Received [Get STB player volume] xmpp message.");
            return XmppConstant.Operation.GET_VOLUME_STATE;
        } else if ( isSetPlayerVolume(json )){
            SuperLog.info2SD(TAG,"Received [Set STB player volume] xmpp message.");
            return XmppConstant.Operation.SET_VOLUME;
        } else if ( isExitPlayer(json )){
            SuperLog.info2SD(TAG,"Received [Exit player] xmpp message.");
            return XmppConstant.Operation.PLAY_EXIT;
        } else if ( isPlayLiveTV(json)){
            SuperLog.info2SD(TAG,"Received [PlayLiveTV] xmpp message.");
            return XmppConstant.Operation.PLAY_LIVE_TV;
        } else if ( isPlayControlCommand(json)){
            SuperLog.info2SD(TAG,"Received [PlayControl] xmpp message.");
            return XmppConstant.Operation.PLAY_CONTROL;
        } else if ( isSwitchDesktop(json)){
            SuperLog.info2SD(TAG,"Received [Switch Desktop] xmpp message.");
            return XmppConstant.Operation.SWITCH_DESKTOP;
        }else {
            SuperLog.info2SD(TAG,"Received undefined XMPP message.");
            return XmppConstant.Operation.UN_DEFINED_OPERATION;
        }
//        //遥控 交给内置app
//        if (json.getString(MessageKey.ACTION).equalsIgnoreCase(Action.FUNCTION_CALL)
//                && json.getString(MessageKey.FUNCTION_TYPE).equalsIgnoreCase(FunctionType.REMOTE_CONTROL)) {
//            XmppManager.handleRemoteFunctionCall(json.getString(MessageKey.KEY_CODE));
//        }
    }

    private static boolean isPlayVR(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String funcType   = json.getString(XmppConstant.MessageKey.FUNCTION_TYPE);
            int isVR          = json.getInt(XmppConstant.MessageKey.VR);
            if( action.equalsIgnoreCase(XmppConstant.Action.FUNCTION_CALL)
                    && funcType.equalsIgnoreCase(XmppConstant.FunctionType.START_PLAY)
                    && isVR == XmppConstant.VideoType.VR ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isSeekCommand(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String funcType   = json.getString(XmppConstant.MessageKey.FUNCTION_TYPE);
            int trickPlayMode = json.getInt(XmppConstant.MessageKey.TRICK_PLAY_MODE);
            if( action.equalsIgnoreCase(XmppConstant.Action.FUNCTION_CALL)
                    && funcType.equalsIgnoreCase(XmppConstant.FunctionType.TRICK_PLAY_CONTROL)
                    && trickPlayMode == XmppConstant.TrickPlayMode.SEEK ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isPlayControlCommand(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String funcType   = json.getString(XmppConstant.MessageKey.FUNCTION_TYPE);
            int trickPlayMode = json.getInt(XmppConstant.MessageKey.TRICK_PLAY_MODE);
            if( action.equalsIgnoreCase(XmppConstant.Action.FUNCTION_CALL)
                    && funcType.equalsIgnoreCase(XmppConstant.FunctionType.TRICK_PLAY_CONTROL)
                    && (trickPlayMode == XmppConstant.TrickPlayMode.PLAY || trickPlayMode == XmppConstant.TrickPlayMode.PAUSE) ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isExitPlayer(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String funcType   = json.getString(XmppConstant.MessageKey.FUNCTION_TYPE);
            int trickPlayMode = json.getInt(XmppConstant.MessageKey.TRICK_PLAY_MODE);
            if( action.equalsIgnoreCase(XmppConstant.Action.FUNCTION_CALL)
                    && funcType.equalsIgnoreCase(XmppConstant.FunctionType.TRICK_PLAY_CONTROL)
                    && trickPlayMode == XmppConstant.TrickPlayMode.STOP ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isKDXFControlCommand(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String funcType   = json.getString(XmppConstant.MessageKey.FUNCTION_TYPE);
            String mediaType  = json.getString(XmppConstant.MessageKey.MEDIA_TYPE);
            if( action.equalsIgnoreCase(XmppConstant.Action.FUNCTION_CALL)
                    && funcType.equalsIgnoreCase(XmppConstant.FunctionType.START_PLAY)
                    && mediaType.equalsIgnoreCase(XmppConstant.MediaType.TYPE_KDXF) ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isPlayVod(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String funcType   = json.getString(XmppConstant.MessageKey.FUNCTION_TYPE);
            String mediaType  = json.getString(XmppConstant.MessageKey.MEDIA_TYPE);
            if( action.equalsIgnoreCase(XmppConstant.Action.FUNCTION_CALL)
                    && funcType.equalsIgnoreCase(XmppConstant.FunctionType.START_PLAY)
                    && mediaType.equalsIgnoreCase(XmppConstant.MediaType.TYPE_VOD) ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isPlayLiveTV(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String funcType   = json.getString(XmppConstant.MessageKey.FUNCTION_TYPE);
            String mediaType  = json.getString(XmppConstant.MessageKey.MEDIA_TYPE);
            if( action.equalsIgnoreCase(XmppConstant.Action.FUNCTION_CALL)
                    && funcType.equalsIgnoreCase(XmppConstant.FunctionType.START_PLAY)
                    && mediaType.equalsIgnoreCase(XmppConstant.MediaType.TYPE_CHANNEL) ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isGetAppState(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String stateType  = json.getString(XmppConstant.MessageKey.STATE_TYPE);
            if( action.equalsIgnoreCase(XmppConstant.Action.GET_APP_STATE)
                    && stateType.equalsIgnoreCase(XmppConstant.StateType.PLAYER_STATE) ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isGetPlayerVolume(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String stateType  = json.getString(XmppConstant.MessageKey.STATE_TYPE);
            if( action.equalsIgnoreCase(XmppConstant.Action.GET_APP_STATE)
                    && stateType.equalsIgnoreCase(XmppConstant.StateType.PLAYER_VOLUME) ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isSetPlayerVolume(JSONObject json){
        try{
            String action     = json.getString(XmppConstant.MessageKey.ACTION);
            String eventType  = json.getString(XmppConstant.MessageKey.EVENT_TYPE);
            if( action.equalsIgnoreCase(XmppConstant.Action.EVENT_INFO)
                    && eventType.equalsIgnoreCase(XmppConstant.EventType.SET_VOLUME) ){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    private static boolean isSwitchDesktop(JSONObject json){
        try{
            String mediaType  = json.getString(XmppConstant.MessageKey.MEDIA_TYPE);
            String playUrl    = json.getString(XmppConstant.MessageKey.PLAY_URL);
            if( mediaType.equalsIgnoreCase(XmppConstant.MediaType.TYPE_SWITCH)
                && playUrl.contains("EPGChange")){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return false;
        }
    }

    public static String getPushReturnMsg(String transactionId){
        CommonMessage msg = new CommonMessage();
        msg.setPlayResultCode("0"); //0:成功
        msg.setPlayResultMsg("operation success");
        msg.setTransactionId(transactionId);
        return JsonParse.object2String(msg);
    }

    public static String getString(JSONObject json,String name){
        try {
            return json.getString(name);
        } catch (Exception e){
            //SuperLog.error(TAG,e);
            return null;
        }
    }

    public static void loadNewDesktop(Constant.DesktopType desktopType){
        if(SharedPreferenceUtil.getInstance().getIsChildrenEpg() && desktopType != Constant.DesktopType.CHILD){
            //儿童版切屏需要拉起验证页面
            OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ParentSetCenterDialog dialog = new ParentSetCenterDialog(OTTApplication.getContext().getMainActivity(),6);
                    dialog.setTypeForToActivity(SWITCH_EPG);
                    dialog.setOnclickType(desktopType == Constant.DesktopType.NORMAL ? 1 : 2 );
                    dialog.show();
                    //dialog.unLock();
                }
            });
            return;
        }
        Intent intent = new Intent(OTTApplication.getContext(), MainActivity.class);
        intent.putExtra("EPGMode", desktopType);
        //不加此Flag抛异常
        // android.util.AndroidRuntimeException:
        // Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag.
        // Is this really what you want?
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //调用后会走MainActivity的onNewIntent回调方法
        OTTApplication.getContext().startActivity(intent);
    }
}