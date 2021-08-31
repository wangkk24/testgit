package com.pukka.ydepg.common.constant;

/**
 * 简介：广播事件发出的action值的静态字段
 * 功能：心跳逆向通知UI，多个UI页面之间相互影响，需要发送广播通知，实现页面的被动更新，本类定义了所有的广播action字段
 * 注意事项：
 * 一、Action字段定义规则：
 * 1.以String命名
 * 2.名称key以：“COM_HUAWEI_OTT_模块/功能_动作”为依据
 * 3.值value以：“com.pukka.ydepg.模块/功能.动作”为依据
 * 4.此处类名，以发出广播的类为依据
 */
public interface BroadCastConstant {
    interface Session {
        String COM_HUAWEI_OTT_SESSION_TIMEOUT_PERMISSION = "com.pukka.ydepg.session.TIMEOUT";
    }

    //Unified Network exception
    interface NetWorkException {
        String COM_HUAWEI_OTT_NETWORK_EXCEPTION = "com.pukka.ydepg.network.exception";
    }

    //心跳管理模块广播action
    interface Heartbit {
        String COM_HUAWEI_OTT_HEARTBIT_CHANNEL_UPDATE   = "com.pukka.ydepg.heartbit.channel.update";   //心跳维护的channel更新
        String COM_HUAWEI_OTT_HEARTBIT_CHANNELNO_UPDATE = "com.pukka.ydepg.heartbit.channelno.update"; //心跳维护的channelNO更新
        String COM_HUAWEI_OTT_START_HEARTBIT            = "com.pukka.ydepg.start.heartbit";            //开始心跳
        String COM_HUAWEI_OTT_STOP_HEARTBIT             = "com.pukka.ydepg.stop.heartbit";             //停止心跳
    }

    //----------------------APP级别广播action-----------------------------------------------------
    String COM_HUAWEI_OTT_APP_EXIT = "com.pukka.ydepg.app.exit";//退出App应用
}