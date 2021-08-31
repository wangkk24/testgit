package com.pukka.ydepg.common.screensaver;

public class ScreenConstant {

    public static final String TAG = "ScreenSaver";

    public static final long   DEFAULT_ADVERT_INTERVAL       = 5000L;

    //SSP屏保广告播放时间默认时长,单位为秒,播放到时长后就只播放运营配置广告
    public static final long   DEFAULT_SSP_ADVERT_DURATION   = 50;

    //终端配置参数
    public static final String TERMINAL_PARAM_ADVERT_SUBJECT = "screen_advert_subject_id";//屏保广告栏目
    public static final String TERMINAL_PARAM_ADVERT_TIME    = "screen_advert_interval";  //单张屏保广告展示时间,单位为秒
    public static final String TERMINAL_PARAM_SSP_ADVERT_DURATION = "max_ssp_screensaver_duration";  //SSP屏保广告播放时间,单位为秒

    //VOD对象扩展参数
    public static final String CUSTOM_FIELD_ADVERT_TYPE      = "type";        //广告类型  0：运营配置广告 1:ssp广告
    public static final String CUSTOM_FIELD_PLACEMENT_CODE   = "placement";   //SSP广告的placement,只有SSP广告需要配置
    public static final String CUSTOM_FIELD_CLICK_URL        = "clickURL";    //广告的H5跳转链接,只有运营配置广告需要配置
    public static final String CUSTOM_APK_PACKAGE            = "zjapkpkg";    //跳转自有/其他应用的包名
    public static final String CUSTOM_APK_CLASS              = "zjapkcls";    //跳转自有/其他应用的类名
    public static final String CUSTOM_APK_EXTRA              = "zjapkextra";  //跳转自有/其他应用的扩展参数
    //空间视频
    public static final String IFIDOL              = "ifIdol";  //多视角视频
    public static final String IFFREED              = "ifFreeD";  //自由视角视频
    //多机位
    public static final String CAMERADEGREE = "cameraDegree";  //当前片源可旋转度数
    public static final String MAINCAMERAID = "mainCameraId";  //主机位id
    public static final String CAMERANUM = "cameraNum";  //当前片源机位总数
}
