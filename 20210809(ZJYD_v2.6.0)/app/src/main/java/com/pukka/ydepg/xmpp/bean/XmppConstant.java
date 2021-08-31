package com.pukka.ydepg.xmpp.bean;

public interface XmppConstant {

    String XMPP_OPERATION_FINISH = "0";

    //认证方式
    public static final String SASLMECHANISM_TOKEN    = "1";   //采用token认证
    public static final String SASLMECHANISM_PASSWARD = "0";   //采用密码认证

    //XMPP消息类型
    interface MessageType {
        String CHAT     = "normal";     // 普通文本消息
        String SYS_CTRL = "sys-ctrl";   // 控制消息
    }

    interface ControlStatus{
        /**
         * -1 表示操作失败。
         *  0 表示成功
         */
        String FAILED = "-1";
        String SUCCESS = "0";

    }
    interface ConnectionStatus{
        /**
         * 当前连接的状态，当前可取值如下：
         *  1：连接成功。
         *  0：失去连接（已经过重试），等下次重连。
         * -1: 查询失败
         */
        String CONNECTIONSUCCESS = "1";
        String CONNECTIONFAIL    = "0";
        String UNKNOWN_STATE     = "-1";
    }

    //XMPP应用消息体中KEY
    interface MessageKey{
        String ACTION           = "action";
        String FUNCTION_TYPE    = "functionType";
        String TRICK_PLAY_MODE  = "trickPlayMode";
        String STATE_TYPE       = "stateType";
        String MEDIA_TYPE       = "mediaType";
        String MEDIA_CODE       = "mediaCode";
        String KEY_CODE         = "keyCode";
        String SEEK_POSITION    = "seekPosition";
        String PLAY_BY_BOOKMARK = "playByBookMark";
        String PLAY_BY_TIME     = "playByTime";
        String ACTION_SOURCE    = "actionSource";
        String DELAY            = "delay";
        String PLATFORM         = "platform";
        String PLAY_URL         = "playUrl";
        String EVENT_TYPE       = "eventType";
        String NEW_VOLUME       = "newVolume";
        String MSG_TYPE         = "msgType";
        String CONTENT_CODE     = "contentCode";
        String TRANSCTION_ID    = "transactionId";
        String VR               = "isVR";
        String VIDEO_TYPE       = "videoType";
    }

    //播放状态,仅供getAppState命令返回中playBackState参数使用
    interface PlayBackState{
        int NOT_PLAY = 0;
        int PLAY     = 1;
    }

    //媒体类型
    interface MediaType{
        String TYPE_CHANNEL = "1";
        String TYPE_VOD     = "2";
        String TYPE_TVOD    = "3";
        String TYPE_MUSIC   = "4";
        String TYPE_LOCAL   = "5";
        String TYPE_SWITCH  = "98";  //手机遥控切屏(普通版/老人版/儿童版)
        String TYPE_KDXF    = "99";  //科大讯飞宽带电视遥控命令
    }

    //XMPP应用消息中action字段取值
    interface Action{
        String FUNCTION_CALL = "functionCall";     //功能调用
        String GET_APP_STATE = "getAppState";      //查询机顶盒播放器状态
        String EVENT_INFO    = "eventInfo";        //设置机顶盒播放器音量
    }

    //XMPP应用消息中functionType取值
    interface FunctionType {
        String REMOTE_CONTROL     = "remoteControl";    //发送遥控器键值
        String START_PLAY         = "startPlay";        //启动播放操作
        String TRICK_PLAY_CONTROL = "trickPlayControl"; //控制机顶盒播放器
    }

    //XMPP应用消息中stateType取值
    interface StateType{
        String PLAYER_STATE  = "playerState";      //查询机顶盒播放器状态
        String PLAYER_VOLUME = "playerVolume";     //查询机顶盒音量
    }

    //XMPP应用消息中eventType取值
    interface EventType{
        String SET_VOLUME  = "setVolume";          //设置机顶盒音量
    }

    // XMPP应用消息中控制机顶盒上播放器行为参数[TrickPlayMode]取值
    // 调用功能类型functionType=trickPlayControl时需要携带此参数
    interface TrickPlayMode{
        int UN_DEFINED = -1;
        int PLAY       = 0;
        int PAUSE      = 1;
        int FAST_SPEED = 2;  //快进或快退
        int SEEK       = 3;
        int TO_BEGIN   = 4;  //一键到头
        int TO_END     = 5;  //一键到尾
        int STOP       = 10; //退出播放
    }

    interface Operation{
        int UN_DEFINED_OPERATION = -1;
        int PLAY_LIVE_TV     = 1;
        int PLAY_VOD         = 2;
        int KDXF_CONTROL     = 3;
        int GET_PLAYER_STATE = 4;
        int SEEK_CONTROL     = 5;
        int PLAY_CONTROL     = 6;
        int GET_VOLUME_STATE = 7;
        int SET_VOLUME       = 8;
        int PLAY_EXIT        = 9;
        int PULL_OPERATION   = 10;
        int PLAY_VR          = 11;
        int SWITCH_DESKTOP   = 12;
    }

    interface VideoType{
        int NORMAL = 0;//普通视频
        int VR     = 1;//VR视频
    }
}