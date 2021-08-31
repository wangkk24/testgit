package com.pukka.ydepg.common.report.ubd.pbs;

public class PbsUaConstant {
    public final static String TAG = "PBS_USER_ACTION";

    final static String PBS_UBD_URL     = "http://aikanvod.miguvideo.com:8858/pvideo/p/useraction.jsp";

    final static String PBS_DESKTOP_URL = "http://aikanvod.miguvideo.com:8858/pvideo/p/stb_getEPGInfo.jsp";

    //需要上报的猜你爱看的sceneId值，平台返回或者是配置的subjectId
    public static String sceneId_guess;

    //需要上报的相关推荐的sceneId值，平台返回或者是配置的subjectId
    public static String sceneId_rec;

    public interface ActionType{
        String IMPRESSION      = "1";
        String CLICK           = "2";
        String NORMAL_PLAY     = "4";
        String RECOMMEND_PLAY  = "5";
    }

    public interface ActionChannel{
        String DETAIL     = "00_01";  //详情页
        String PLAY       = "00_02";  //播放页面
        String PURCHASE   = "00_03";  //订购页面
        String LIVE       = "00_04";  //直播页面
        String DESTOP     = "00_00";  //首页launcher
    }

    //0级渠道
    public interface ActionChannel_0 {
        String EPG      = "00";  //EPG
        String THIRD    = "01";  //第三方APK,此类型不会在EPG中产生
        String EPG_TVMS = "02";  //EPG弹窗
    }

    //1级渠道
    public interface ActionChannel_1 {
        String DETAIL    = "01";    //详情页
        String PLAY      = "02";    //播放页,由于播放页和详情页合并,因此不会上报
        String ORDER_CENTER = "03";//订购中心
        String LIVE      = "04";   //直播页面
        String PBS       = "05";   //H5页面
        String OTHER     = "06";   //其他原生EPG页面
    }

    public interface AppointedId{
        String RELATION   = "relatedReco25";  //相关推荐
        String GUESS      = "guessReco25";    //猜你爱看
    }

    public interface SceneId{
        String RELATION   = "215";    //相关推荐
        String GUESS      = "705";    //猜你爱看
    }
}