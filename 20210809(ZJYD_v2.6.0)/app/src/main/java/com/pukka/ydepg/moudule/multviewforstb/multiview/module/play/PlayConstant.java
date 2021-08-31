package com.pukka.ydepg.moudule.multviewforstb.multiview.module.play;

public interface PlayConstant {
    /**
     * 更新时间进度条 what
     */
    int WHAT_UPDATE_TIME = 0x0980;
    /**
     * 隐藏时间进度条 what
     */
    int WHAT_HIDE_TIME = 0x0981;

    /**
     * 更新下载速度
     */
    int WHAT_UPDATE_SPEED = 0x0983;

    /**
     * 更新zoom后的UI
     */
    int WHAT_DO_ZOOM = 0x0984;

    /**
     * 更新logcatView
     */
    int WHAT_UPDATE_LOGCAT = 0x0985;

    /**
     * 更新时间间隔 毫秒
     */
    int UPDATETIME_OFFSET = 1000;

    /**
     * 用于seek的偏移量，一次down，进行偏移量大小的seek
     */
    int SEEKOFFSET = 5000;

    /**
     * 发送机位切换指令
     */
    int WHAT_SET_CAMERAROTATION_TRACK = 0x1240;

    /**
     * 传输数据Flag
     */
    String PUSH_DATA = "allVideoConfig";

    String EXTRA = "extra";

    /**
     * 从EPG模板拉起apk，退出时要发送ReturnURL
     */
    String RETURN_URL = "ReturnURL";

    /**
     * 是否展示网速
     */
    String TAG_ISSHOWSPEED = "isShowSpeed";
    /**
     * 是否在UI上显示log
     */
    String TAG_SHOWLOGCAT = "showLogcat";
    /**
     * 是否展示
     */
    String TAG_SHOWCAMERAVIEW = "showCameraView";

    /**
     * 机顶盒类型上报事件
     * 0：非机顶盒，
     * 1：海思，
     * 2：非海思
     */
    int UPDATE_STB_TYPE = 817;

    /**
     * 通用接口，切换屏幕的延时时间，毫秒
     */
    int NOMAL_SWITCH_SCREEN_DELAY_TIME = 60;

}
