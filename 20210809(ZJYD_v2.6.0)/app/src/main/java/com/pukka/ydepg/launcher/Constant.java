package com.pukka.ydepg.launcher;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.Constant.java
 * @date: 2017-12-30 09:58
 * @version: V1.0 描述当前版本功能
 */
public class Constant {
    public static final String SCROLL_COMPLETE = "scroll_complete";
    public static final String CHILDREN_LOCK_HINT = "children_lock_hint";
    public static final String CHILDREN_LOCK_ONPEN_HINT = "children_lock_onpen_hint";
    public static final String PPVORDER_SHOW_TIME = "ppvorder_show_time";
    public static final String AUTO_PLAY = "auto_play";
    public static final String PLAY_MODE = "play_mode";
    public static final String RESOURCE_TYPE = "resource_type";
    public static final String SHOW_SUBSCRIBERD_MARK = "showSubscribedMark";
    public static final String SHOW_EPSIODE_VIP_MARK = "showEpsiodeVIPMark";
    public static final String DEVICE_STBID = "ro.product.stb.stbid";
    public static final String DEVICE_RAW = "ro.product.model.RAW";

    //launcher扩展参数key
    public static final String NAV_MINE = "nav_mine";//导航我的页面
    public static final String NAV_FOCUS = "nav_focus";//首次落焦的导航
    public static final String NAV_SIMPLE_EPG = "nav_simpleEpg";//导航简版epg
    public static final String NAV_CHILDREN_EPG = "nav_childrenEpg";//儿童版
    public static final String NAV_4K_EPG = "nav_4k";//儿童版

    public static final String PREPOSE_PRODUCT_LIST = "prepose_product_list";
    public static final String POSTPOSITION_PRODUCT_LIST = "postposition_product_list";
    public static final String SECOND_CONFIRM_PRODUCT_LIST = "second_confirm_product_list";
    public static final String UNSUPPORT_4K_DEVICE = "unsupport_4K_devices";

    public static final String PRODUCT_PACKAGE_RELATIONSHIP = "product_package_relationship";  //大小包互斥关系
    public static final String CHILD_SETTING_FILTER_CATEGORY = "child_setting_filter_category";
    public static final String CHILD_FILTER_BASE_CATEGORY_NAME = "child_filter_base_category_name";
    public static final String BOOKMARK_CHILD_MODE = "book_child_mode";
    public static final String PLAY_FROM_TERMINAL = "playfromterminal";
    public static final String CHILD_MODE_RECOMMEND_SUBJECT = "child_mode_recommend_subject_list";
    public static final String USE_CVI_RECOMMEND = "use_cvi_recommend";
    public static final String USE_VERIFICATION_CODE_WHITELIST = "verification_code_pay_whitelist";

    //搜索
    public static final String SEARCH_HOT_KEY = "search_everyone_search";
    public static final String SEARCH_HOT_ACTORS = "search_hot_character";
    public static final String SEARCH_HOT_CATEGORY = "search_hot_subject";
    public static final String SEARCH_SUGGEST_SWITCH = "search_suggest_switch";
    public static final String SEARCH_SUGGEST_TITLE = "search_suggest_title";
    public static final String SEARCH_SUBJECT_CLASSIFY = "search_subject_classify";
    public static final String SEARCH_SUBJECT_All = "search_all";//获取默认"全部"栏目的SubjectID

    public static final String CP_APK_INFO = "CP_APK_Info";
    public static final String CONTENTCODE = "ContentCode";
    public static final String ACTION = "Action";
    public static final String MARKETING_INFO = "marketing_info";
    public static final String ORDER_CENTER_PRODUCT_INFOS = "order_center_product_infos";
    public static final String VIDEO_TRIAL_MARKETING_CONTENT_SUBJECT_ID = "video_trial_marketing_content_subject_id";

    //第三方支付
    public static final String CHECK_ORDER_STATUS_INTERVAL = "check_order_status_interval"; //第三方支付轮询订单状态时间间隔终端参数KEY
    public static final String CHECK_ORDER_STATUS_AFTER_CALLBACK_INTERVAL = "check_order_status_after_callback_interval";//第三方支付收到第三方回调后轮询订单状态时间间隔终端参数KEY
    public static final String THIRD_PART_PAYMENT_URL = "third_part_payment_url";      //第三方支付URL地址终端参数KEY
    public static final String THIRD_PART_PAYMENT_APP_ID = "third_part_payment_app_id";   //第三方支付调用支付网关的AppID

    public static final String BLANK_STARING = "";    //空字符串
    public static final String USER_STATE_IN_USE = "1";   //用户状态-在用
    public static final String CHANNEL_TYPE_THIRD_PART_PAYMENT = "100";

    //详情
    public static final String VOD_DETAIL_AdVERT_SUBJECT_ID = "vod_detail_advertisement_subject_id";
    public static final String CHILD_FAVROTIE_FOLDER = "zjyd_child_favorite_folder";
    public static final String CHILD_FAVROITE_FOLDER_ID = "zjydchildfavoritefolder";

    public static final String DETAIL_ZJ_BOOKMARK = "ZJsitcomNO";

    //直播单播获取栏目列表所需SubjectID
    public static final String LIVE_SUBJECT_ID = "channelClassCategoryID";

    //直播组播获取栏目列表所需SubjectID
    public static final String LIVE_SUBJECT_ID_MULTICAST = "channelClassCategoryID4Multicast";

    //查询组播开关状态的key,value=1:组播开关打开；=0：组播开关关闭
    public static final String TVMULTICASTSWITCH = "tvmulticastSwitch";

    //查询儿童版家长设置的密码
    public static final String PARENTS_PWD = "parentsPwd";

    //推送消息频次控制功能
    public static final String MESSAGE_FREQUENCY_CONTROL = "message_frequency_control";

    //首页窗口视频轮播=1:落焦播放；=2:自动播放，如果没有配置，默认落焦播放
    public static final String MAIN_VIDEO_PLAY_TYPE = "main_video_play_type";

    //xmpp接收--显示时间配置(推送消息延时显示时间)
    public static final String XMPP_RECEIVE_SHOW_TIME = "xmpp_receive_show_time";

    /**
     * 1  代表使用PHS部署，调用QueryPHMLauncherList
     * 0或者未配置   代表不使用PHS部署，直接执行原有逻辑
     */
    public static final String QUERY_PHM_LAUNCHER_LIST = "query_phm_launcherlist";

    //首页资费类角标开关,配置终端参数，枚举值：0资费类角标；1导读类角标   默认为1
    public  static final String EPG_MAIN_IS_USE_POSTAGE_SCRIPT = "epg_main_is_use_postage_script";

    /*
     *收藏和播放记录开关，配置0展示资费，配置1展示导读，不配置或配置其他值默认为导读
     */
    public  static final String SUPERSCRIPT_SWIRCH_HISTORY_COLLECTION = "superscript_switch_history_collection";

    //FCC检测地址
    public static final String PLAY_FCC_URL = "test_fcc_url";
    //FCC播放黑名单
    public static final String BLACKLIST_FOR_FCC = "black_list_for_multicast";
    //FCC播放白名单
    public static final String WHITELIST_FOR_FCC = "white_list_for_multicast";

    public static final String NAV_TITLE_BG_START_COLOR = "nav_title_bg_start_color";
    public static final String NAV_TITLE_BG_END_COLOR = "nav_title_bg_end_color";

    //用于拼接PBS接口返回的角标URL
    public static final String PBS_SCRIPT_URL="http://aikanvod.miguvideo.com:8858";

    public interface URL {
        String ZJ_LOGIN = "/VSP/V3/ZJLogin";
        String CUSTOMSIZE_CONFIG = "/VSP/V3/QueryCustomizeConfig";
    }

    public interface SortType {
        String UPDATE_TIME_DESC = "UPDATE_TIME:DESC"; //历史顺序
        String SORTTYPE_FAVO_TIME_DESC = "FAVO_TIME:DESC";   //收藏顺序
    }

    //the range of field queryType
    public interface QueryType {
        String TERMINAL_CONFIG = "0"; //The config of terminal
        String TEMPLE_CONFIG = "1"; //The config of temple
        String EPG_CONFIG = "2"; //The config of EPG
        String THE_THIRD_SYSTEM_CONFIG = "3"; //The config of third system
        String CONTENT_LEVEL = "4"; //The level of content
        String USABLE_LANGUAGE = "5"; //Usable language
        String ISO_CODE = "6"; //Iso code
        String CURRENCY_CODE = "7"; //Currency code
    }

    public interface VoiceCommandScene {
        String CATCHUP_ACTIVITY = "com.pukka.ydepg.CatchUpActivity";
        String TVOD_PROGRAM = "com.pukka.ydepg.TVODProgramListActivity";
    }

    //广告角标
    public interface AdCornerMarker {
        //间隔显示广告时间
        String INTERVAL_PLAY_TIME = "INTERVAL_PLAY_TIME";
        //最大播放次数
        String MAX_PLAY_COUNT = "MAX_PLAY_COUNT";
        //关闭时间
        String AD_CLOSE_TIME = "AD_CLOSE_TIME";
        //首次展示广告的时间
        String FIRST_SHOW_AD_TIME ="CORNER_ADS_FIRST_PLAY_TIME";
    }

    //直播订购
    public interface LiveOrder {
        //终端配置参数
        //直播订购subjectid
        String LIVE_ORDER_SUBJECTID = "live_order_subjectId";//需要订购的直播列表id
        //liveorder
        //直播订购默认落焦0：取消 1：订购
        String  LIVE_ORDER_DEFAULT_FOCUS="liveOrderDefaultFocus";
        //直播订购文案提示
        String  LIVE_ORDER_DESCRIPTION="liveOrderDescription";

    }
    //支持空间视频列表
    public static final String SUPPORT_EDSPATIAL_VIDEO_DEVICES = "SupportedSpatialVideoDevices";
    //甩屏直播需求
    public static final String CHANNEL_CODE="ZJMUchannelCode";
    //语音直播需求
    public static final String CHANNEL_ID="ZJMUchannelID";
}