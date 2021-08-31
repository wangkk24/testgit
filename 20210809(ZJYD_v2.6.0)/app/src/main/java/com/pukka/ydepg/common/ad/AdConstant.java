package com.pukka.ydepg.common.ad;

public class AdConstant {

    public static final String TAG                 = "SSP_AD";

    //终端配置参数 SSP广告平台总开关,此开关关闭(0)则关闭一切SSP广告平台功能 不配或者配置为其他值为打开功能
           static final String SSP_SWITCH_ALL      = "ssp_switch_all";

    //终端配置参数 SSP广告功能子场景开关,此开关可分别控制launcher、屏保、前贴等各个子场景广告是否展示
    //MAP类型,key   :子场景类型 取值为@AdConstant.AdClassify  VIDEO/START/SCREEN
    //       value : SSP_SWITCH_ALL (0)为关闭,关闭当前子场景功能 不配或者配置为其他值为打开功能
           static final String SSP_SWITCH_SINGLE   = "ssp_switch_single";

    //终端配置参数 SSP广告功能子场景广告时长开关,此开关可分别控制launcher、屏保、前贴等各个子场景广告的总时长是否显示
    //MAP类型,key   :子场景类型 取值为@AdConstant.AdClassify  VIDEO/START/SCREEN
    //       value : SSP_SWITCH_ALL (0)为关闭,关闭当前子场景功能广告总时长展示, 不配或者配置为其他值为打开功能
           static final String SSP_TIME_SWITCH_ALL = "ssp_time_switch_all";

    //终端配置参数 SSP广告功能子场景跳过时长,此开关可分别定义launcher、屏保、前贴等各个子场景广告的可跳过播放时长
    //MAP类型,key   :子场景类型 取值为@AdConstant.AdClassify  VIDEO/START/SCREEN
    //       value : 可跳过播放时长,单位秒
           static final String SSP_SKIP_TIME_ALL   = "ssp_skip_time_all";

    //终端配置参数SSP_SWITCH_ALL的关闭值=>0:关闭此功能,否则(包括没配)为启用
           static final String SSP_CLOSE           = "0";

    //终端配置参数 SSP平台域名
           static final String ADVERT_URL_DOMAIN   = "advert_url_domain";

    //终端配置参数 SSP广告平台会员产品列表,用户如果订购了此产品列表中的产品,则认为是开通了SSP广告平台会员,具有直接跳过SSP广告的能力
           static final String SSP_MEMBER_LIST     = "ssp_member_list";

    //终端配置参数 SSP前贴广告STB黑名单列表,视频前贴广告涉及STB播放器的playlist能力。
    //此名单中机顶盒设备不支持贴片广告  [ALL]全部机顶盒不展示贴片广告
           static final String SSP_VIDEO_STB_BLACKLIST = "ssp_video_stb_blacklist";

    //广告的可跳过播放时长默认值
           static final int SSP_DEFAULT_SKIP_TIME   = 3;

    //终端配置参数 开机广告海报图片展示时间,单位秒
    public static final String BANNER_DURATION     = "start_pic_duration";

    //终端配置参数 开机广告是否展示广告倒计时
    public static final String BANNER_ISSHOW_COUNT = "banner_isshow_count";



    //广告平台url
    static final String ADVERT_DOMAIN     = "https://sspcloud.huawei.com";
    //广告平台路径
    static final String ADVERT_URL_PATH   = "/adex/v1.0/ad/";
    //华为SSP广告平台广告请求接口
    static final String QUERY             = "creatives";
    //华为SSP广告平台广告上报接口
    static final String REPORT     = "report";

    static final long NOT_STARTED  = 0L;

    public interface AdClassify{
        String START  = "START";  //开机广告
        String SCREEN = "SCREEN"; //屏保广告
        String VIDEO  = "VIDEO";  //视频前贴广告
        String DETAIL = "DETAIL"; //内容详情页广告
        String CORNER = "CORNER"; //播放页角标广告
    }

    public interface AdvertInfo{
        String TENANTID             = "8601";

        //开机广告
        String START_BANNER_CODE    = "8601_EPG_LAUNCHER_1"; //图片广告
        String START_VIDEO_CODE     = "8601_EPG_LAUNCHER_V"; //视频广告

        //视频前贴视频广告
        String VIDEO_VIDEO_CODE     = "8601_EPG_PLAY_PRE_1";

        //屏保广告位,5个图片轮播
        String SCREEN_BANNER_CODE_1 = "8601_EPG_SCREENSAVER_1";
        String SCREEN_BANNER_CODE_2 = "8601_EPG_SCREENSAVER_2";
        String SCREEN_BANNER_CODE_3 = "8601_EPG_SCREENSAVER_3";
        String SCREEN_BANNER_CODE_4 = "8601_EPG_SCREENSAVER_4";
        String SCREEN_BANNER_CODE_5 = "8601_EPG_SCREENSAVER_5";

        //内容详情页Banner广告
        String DETAIL_BANNER_CODE_1 = "8601_EPG_DETAIL_BANNER_1";

        //播放器内角标广广告
        String PLAY_CORNER_CODE_1   = "8601_EPG_PLAY_CORNER_1";
    }

    //上报report广告类型(展示的广告类型,如展示的是图片广告,就是BANNER)
    public interface AdType{
        String VIDEO        = "VIDEO"; //视频广告
        String NATIVE       = "NATIVE";//信息流广告
        String BANNER       = "BANNER";//图片广告
    }

    //广告类型上报
    public interface ReportActionType{
        String IMPRESSION    = "IMPRESSION";   //曝光/展示（或视频加载成功）
        String START         = "START";
        String CLICK         = "CLICK";
        String DURATION      = "IMPRESSIONDUR";//实际播放时长上报
    }
}