package com.pukka.ydepg.common.report.ubd;

public class UBDConstant {

    //UBD日志TAG
    public  static final String TAG = "UBD";

    //UBD功能开状态
    static final String UBD_OPEN = "1";

    //UBD功能总开关终端配置参数key
    static final String UBD_SWITCH_KEY = "UBD_switch";

    //设备类型 1代表机顶盒
    public static final String DEVICE_STB = "1";

    public static final String MANUAL_RECOMMEND_TYPE = "-1";

    public interface Function{
        String SMS_VERIFY         = "SMSVerify";
        String COLLECT            = "Collect";
        String SWITCH_SOURCE      = "SwitchSource";
        String RECOMMEND          = "recommendpush";
    }

    public interface ActionType{
        String VERSION               = "Event_EpgVersion";          //上报版本号动作
        String UPGRADE               = "Event_EpgUpgrade";          //上报升级动作
        String SUBSCRIBE             = "Event_Subscribe";           //上报订购动作
        String SCREEN_IMPRESSION     = "Event_ScreenImpression";    //上报屏保广告曝光
        String SCREEN_CLICK          = "Event_ScreenClick";         //上报屏保广告点击
        String VOD_BANNER_IMPRESSION = "Event_VodBannerImpression"; //上报Vod广告曝光
        String PURCHASE_BANNER_IMPRESSION = "Event_PurchaseBannerImpression"; //上报退订挽留页广告曝光
        String TOP_FUNCTION          = "Event_TopFunction";         //上报头部功能键点击
        String RECOMMEND_IMPRESSION  = "Event_RecommendImpression"; //上报推送消息到达
        String MESSAGE_ARRIVED       = "Event_MessageArrived";      //上报推送消息到达
        String MESSAGE_IMPRESSION    = "Event_MessageImpression";   //上报推送消息曝光
        String MESSAGE_CONVERT       = "Event_MessageConvert";      //上报推送消息转化
        String DESKTOP_TYPE          = "Event_DesktopType";         //上报桌面类型
    }

    public interface ACTION {
        String FOCUS_MOVE         = "000"; //焦点移动(已废弃)
        String START              = "001"; //开机后从广告页结束到首页动作

        //点击事件
        String PLAY_PREVIEW       = "100"; //播放(试看)
        String PLAY_NORMAL        = "101"; //播放
        String SUBSCRIBE          = "102"; //订购(已经废弃)
        String FAVORITE           = "103"; //收藏
        String SEARCH             = "104"; //搜索
        String VIEW_VOD_DETAIL    = "105"; //点击跳转详情
        String LEAD_SUBSCRIBE     = "112"; //订购引流信息(位置信息)
        String ADVERT_PURCHASE    = "120"; //点击[退订挽留页]广告

        //其他
        String COMMON_CLICK       = "150"; //通用点击(跳转)行为
        String PLAY_AUTO          = "201"; //自动播放下一集
    }

    public interface OptType{
        String PULL               = "0";   //拉屏
        String PUSH_LIVE          = "1";   //直播甩屏
        String VOICE              = "10";  //语音搜索
        String PUSH_VOD           = "13";  //点播甩屏
        String SWITCH             = "14";  //EPG模板切换
        String VIDEO_CALL         = "15";  //视频通话(当前无此场景)
        String BIND               = "16";  //绑定
        String UNBIND             = "17";  //解绑
        String OPEN_SMS           = "20";  //开启短信验证
        String CLOSE_SMS          = "21";  //关闭短信验证
        String SMS_VERIFIY        = "22";  //发送短信验证
        String VODDETAIL_SHOW     = "20";  //详情页的推荐曝光(前需求为30，变更为20)
        String SELECT_VOD_SHOW    = "31";  //精选页活动推荐   31: 精选频道活动推荐204
        String SELECT_TOPIC_SHOW  = "32";  //精选页专题推荐   32: 精选频道专题推荐203
        String MOVIE_CONTENT_SHOW = "41";  //电影频道内容曝光 41: 电影频道内容曝光201
    }

    public interface ContentType{
        String VOD                = "vod";
        String CHANNEL            = "channel";
    }

    public interface BillType{
        String NORMAL             = "0";
        String SOURCE             = "1";
    }

    public interface NavPosition {
        int SIMPLE = -1;
        int CHILD  = -2;
    }
}