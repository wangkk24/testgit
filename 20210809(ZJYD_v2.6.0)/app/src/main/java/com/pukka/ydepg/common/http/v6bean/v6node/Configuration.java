package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Configuration {
    /**
     * cfgType : 123
     * values : ["NamedParameter"]
     * STBLogServerURL :
     * STBLogUploadInterval :
     * playHeartBitInterval :
     * netrixPushServerURL :
     * skippingTimeBlock :
     * VODFavLimit :
     * channelFavLimit :
     * VASFavLimit :
     */

    /**
     *参数配置类型。

     取值范围：

     0：查询终端的配置参数
     1：查询EPG模板的配置参数
     2：查询VSP EPG参数
     3：查询第三方系统集成参数

     */
    @SerializedName("cfgType")
    private String cfgType;

    /**
     *STB日志服务器地址。

     */
    @SerializedName("STBLogServerURL")
    private String STBLogServerURL;

    /**
     *STB日志上载周期。

     单位：秒。

     */
    @SerializedName("STBLogUploadInterval")
    private String STBLogUploadInterval;

    /**
     *播放心跳周期，单位为秒。

     */
    @SerializedName("playHeartBitInterval")
    private String playHeartBitInterval;

    /**
     *Netrix设备信息上报URL。

     */
    @SerializedName("netrixPushServerURL")
    private String netrixPushServerURL;

    /**
     *Skip播放跳转时长跨度，单位是s。

     */
    @SerializedName("skippingTimeBlock")
    private String skippingTimeBlock;

    /**
     *VOD收藏上限。

     */
    @SerializedName("VODFavLimit")
    private String VODFavLimit;

    /**
     *频道收藏上限。

     */
    @SerializedName("channelFavLimit")
    private String channelFavLimit;

    /**
     *VAS收藏上限。

     */
    @SerializedName("VASFavLimit")
    private String VASFavLimit;

    /**
     *配置参数列表。

     */
    @SerializedName("values")
    private List<NamedParameter> values;
    /**
     * downloadedBufferLength :
     * SocialURL :
     * favouriteLimit :
     * bookmarkLimit :
     * lockLimit :
     * profileLimit :
     * SQMURL :
     * UserPwdMinLength : 123
     * UserPwdUpperCaseLetters : 123
     * UserPwdLowerCaseLetters : 123
     */

    /**
     *用户进行下载播放时的最小缓冲时长，单位为秒，默认30s。

     */
    @SerializedName("downloadedBufferLength")
    private String downloadedBufferLength;

    /**
     *社交平台的服务地址。

     */
    @SerializedName("SocialURL")
    private String SocialURL;

    /**
     *用户收藏上限。

     每个子用户的收藏个数上限单独控制。

     */
    @SerializedName("favouriteLimit")
    private String favouriteLimit;

    /**
     *书签上限。

     每个子用户的书签个数上限单独控制

     */
    @SerializedName("bookmarkLimit")
    private String bookmarkLimit;

    /**
     *订户的锁上限。

     */
    @SerializedName("lockLimit")
    private String lockLimit;

    /**
     *用户Profile个数上限。

     */
    @SerializedName("profileLimit")
    private String profileLimit;

    /**
     *对接的第三方业务质量管理系统地址。

     IP地址支持V4或V6。

     */
    @SerializedName("SQMURL")
    private String SQMURL;

    /**
     *密码最小长度。

     */
    @SerializedName("UserPwdMinLength")
    private String UserPwdMinLength;

    /**
     *是否一定要包含大写字母A-Z。

     0：不一定包含
     1：必须包含
     默认值为1。

     */
    @SerializedName("UserPwdUpperCaseLetters")
    private String UserPwdUpperCaseLetters;

    /**
     *是否一定要包含小写字母a-z。

     0：不一定包含
     1：必须包含
     默认值为1。

     */
    @SerializedName("UserPwdLowerCaseLetters")
    private String UserPwdLowerCaseLetters;
    /**
     * UserPwdNumbers : 123
     * UserPwdOthersLetters :
     * UserPwdSupportSpace : 123
     * BeginOffset : 12
     * EndOffset : 12
     * NPVRSpaceStrategy : 123
     * OTTIMPURLforIPv6 :
     * OTTIMPURL :
     * OTTIMPPort : 12
     * OTTIMPIPforIPv6 :
     */

    /**
     *是否一定要包含数字。

     0：不一定包含
     1：必须包含
     默认值为1。

     */
    @SerializedName("UserPwdNumbers")
    private String UserPwdNumbers;


    /**
     *特殊字符。

     如果为空，则表示不需要一定包含特殊字符。
     如果不为空，则表示一定要包含此字段值中的特殊字符中的一个或多个。
     */
    @SerializedName("UserPwdOthersLetters")
    private String UserPwdOthersLetters;


    /**
     *是否支持空格作为密码字符。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("UserPwdSupportSpace")
    private String UserPwdSupportSpace;


    /**
     *节目单录制提前启动时长，单位秒，默认0，取值范围是0~3600。

     */
    @SerializedName("BeginOffset")
    private String BeginOffset;


    /**
     *节目单录制延后录制时长，单位秒，默认0，取值范围是0~3600。

     */
    @SerializedName("EndOffset")
    private String EndOffset;


    /**
     *NPVR业务的空间运营模式，取值包括：

     1：按照网络容量计算
     2：按照录制时长计算
     默认值为2。

     */
    @SerializedName("NPVRSpaceStrategy")
    private String NPVRSpaceStrategy;


    /**
     *OTT领域的IM服务器BOSH服务IP V6 URL。

     */
    @SerializedName("OTTIMPURLforIPv6")
    private String OTTIMPURLforIPv6;


    /**
     *OTT领域的IM服务器BOSH服务IP V4 URL。

     */
    @SerializedName("OTTIMPURL")
    private String OTTIMPURL;


    /**
     *OTT领域的IM服务端口。

     */
    @SerializedName("OTTIMPPort")
    private String OTTIMPPort;


    /**
     *OTT领域的IM服务器IP V6地址。

     */
    @SerializedName("OTTIMPIPforIPv6")
    private String OTTIMPIPforIPv6;
    /**
     * UserIDMinLength :
     * UserIDMaxLength :
     * UserIDUpperCaseLetters :
     * UserIDLowerCaseLetters :
     * UserIDNumbers :
     * UserIDOthersLetters :
     * UserIDSupportSpace :
     * ProfileNameMinLength :
     * ProfileNameMaxLength :
     * ProfileNameUpperCaseLetters :
     * ProfileNameLowerCaseLetters :
     * ProfileNameNumbers :
     * ProfileNameOthersLetters :
     * ProfileNameSupportSpace :
     * DeviceNameMinLength :
     * DeviceNameMaxLength :
     * DeviceNameUpperCaseLetters :
     * DeviceNameLowerCaseLetters :
     * DeviceNameNumbers :
     * DeviceNameOthersLetters :
     * DeviceNameSupportSpace :
     * SearchKeyMinLength :
     * SearchKeyMaxLength :
     * SearchKeyUpperCaseLetters :
     * SearchKeyLowerCaseLetters :
     * SearchKeyNumbers :
     * SearchKeyOthersLetters :
     * SearchKeySupportSpace :
     * PlaylistNameMinLength :
     * PlaylistNameMaxLength :
     * PlaylistNameUpperCaseLetters :
     * PlaylistNameLowerCaseLetters :
     * PlaylistNameNumbers :
     * PlaylistNameOthersLetters :
     * PlaylistNameSupportSpace :
     * OTTIMPIP :
     * IPTVIMPURLforIPv6 :
     * IPTVIMPURL :
     * IPTVIMPPort :
     * IPTVIMPIPforIPv6 :
     * IPTVIMPIP :
     * IMDomain :
     * ImpAuthType :
     * PlaybillLen :
     * RecPlaybillLen :
     * MQMCURL :
     * ConcurrentCpvrTaskLimit :
     */

    /**
     *UserID最小长度。

     */
    @SerializedName("UserIDMinLength")
    private String UserIDMinLength;


    /**
     *UserID最大长度。

     */
    @SerializedName("UserIDMaxLength")
    private String UserIDMaxLength;

    /**
     *是否支持大写字母A-Z。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("UserIDUpperCaseLetters")
    private String UserIDUpperCaseLetters;

    /**
     *是否一定支持小写字母a-z。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("UserIDLowerCaseLetters")
    private String UserIDLowerCaseLetters;

    /**
     *是否一定支持数字。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("UserIDNumbers")
    private String UserIDNumbers;

    /**
     *特殊字符。

     如果为空，则表示不支持包含特殊字符。
     如果不为空，则表示支持包含此字段值中的特殊字符中的一个或多个。
     */
    @SerializedName("UserIDOthersLetters")
    private String UserIDOthersLetters;

    /**
     *是否支持空格。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("UserIDSupportSpace")
    private String UserIDSupportSpace;

    /**
     *ProfileName最小长度。

     */
    @SerializedName("ProfileNameMinLength")
    private String ProfileNameMinLength;

    /**
     *ProfileName最大长度。

     */
    @SerializedName("ProfileNameMaxLength")
    private String ProfileNameMaxLength;

    /**
     *是否支持大写字母A-Z。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("ProfileNameUpperCaseLetters")
    private String ProfileNameUpperCaseLetters;

    /**是否支持小写字母a-z。

     0：不支持
     1：支持
     默认值为1。

     *
     */
    @SerializedName("ProfileNameLowerCaseLetters")
    private String ProfileNameLowerCaseLetters;

    /**
     *是否支持数字。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("ProfileNameNumbers")
    private String ProfileNameNumbers;

    /**
     *特殊字符。

     如果为空，则表示不支持包含特殊字符。
     如果不为空，则表示支持包含此字段值中的特殊字符中的一个或多个。
     */
    @SerializedName("ProfileNameOthersLetters")
    private String ProfileNameOthersLetters;

    /**
     *是否支持空格。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("ProfileNameSupportSpace")
    private String ProfileNameSupportSpace;

    /**
     *DeviceName最小长度。

     */
    @SerializedName("DeviceNameMinLength")
    private String DeviceNameMinLength;

    /**
     *DeviceName最大长度。

     */
    @SerializedName("DeviceNameMaxLength")
    private String DeviceNameMaxLength;

    /**是否支持大写字母A-Z。

     0：不支持
     1：支持
     默认值为1。

     *
     */
    @SerializedName("DeviceNameUpperCaseLetters")
    private String DeviceNameUpperCaseLetters;

    /**
     *是否支持小写字母a-z。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("DeviceNameLowerCaseLetters")
    private String DeviceNameLowerCaseLetters;

    /**
     *是否支持数字。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("DeviceNameNumbers")
    private String DeviceNameNumbers;

    /**
     *特殊字符。

     如果为空，则表示不支持包含特殊字符。
     如果不为空，则表示支持包含此字段值中的特殊字符中的一个或多个。
     */
    @SerializedName("DeviceNameOthersLetters")
    private String DeviceNameOthersLetters;

    /**
     *是否支持空格。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("DeviceNameSupportSpace")
    private String DeviceNameSupportSpace;

    /**
     *SearchKey最小长度。

     */
    @SerializedName("SearchKeyMinLength")
    private String SearchKeyMinLength;

    /**
     *SearchKey最大长度。

     */
    @SerializedName("SearchKeyMaxLength")
    private String SearchKeyMaxLength;

    /**
     *是否支持大写字母A-Z。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("SearchKeyUpperCaseLetters")
    private String SearchKeyUpperCaseLetters;

    /**是否支持小写字母a-z。

     0：不支持
     1：支持
     默认值为1。

     *
     */
    @SerializedName("SearchKeyLowerCaseLetters")
    private String SearchKeyLowerCaseLetters;

    /**
     *是否支持数字。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("SearchKeyNumbers")
    private String SearchKeyNumbers;

    /**
     *特殊字符。

     如果为空，则表示不支持包含特殊字符。
     如果不为空，则表示支持包含此字段值中的特殊字符中的一个或多个。
     */
    @SerializedName("SearchKeyOthersLetters")
    private String SearchKeyOthersLetters;

    /**
     *是否支持空格。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("SearchKeySupportSpace")
    private String SearchKeySupportSpace;

    /**
     *PlaylistName最小长度。

     */
    @SerializedName("PlaylistNameMinLength")
    private String PlaylistNameMinLength;

    /**
     *PlaylistName最大长度。

     */
    @SerializedName("PlaylistNameMaxLength")
    private String PlaylistNameMaxLength;

    /**
     *是否支持大写字母A-Z。

     0：不支持
     1：支持
     默认值为1。
     */
    @SerializedName("PlaylistNameUpperCaseLetters")
    private String PlaylistNameUpperCaseLetters;

    /**
     *是否支持小写字母a-z。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("PlaylistNameLowerCaseLetters")
    private String PlaylistNameLowerCaseLetters;

    /**
     *是否支持数字。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("PlaylistNameNumbers")
    private String PlaylistNameNumbers;

    /**
     *特殊字符。

     如果为空，则表示不支持包含特殊字符。
     如果不为空，则表示支持包含此字段值中的特殊字符中的一个或多个。

     */
    @SerializedName("PlaylistNameOthersLetters")
    private String PlaylistNameOthersLetters;

    /**
     *是否支持空格。

     0：不支持
     1：支持
     默认值为1。

     */
    @SerializedName("PlaylistNameSupportSpace")
    private String PlaylistNameSupportSpace;

    /**
     *OTT领域的IM服务器IP V4地址。

     */
    @SerializedName("OTTIMPIP")
    private String OTTIMPIP;

    /**
     *IPTV领域的IM服务器BOSH服务IP V6 URL。

     */
    @SerializedName("IPTVIMPURLforIPv6")
    private String IPTVIMPURLforIPv6;

    /**
     *IPTV领域的IM服务器BOSH服务IP V4 URL。

     */
    @SerializedName("IPTVIMPURL")
    private String IPTVIMPURL;

    /**
     *IPTV领域的IM服务端口。

     */
    @SerializedName("IPTVIMPPort")
    private String IPTVIMPPort;

    /**
     *IPTV领域的IM服务器IP V6地址。

     */
    @SerializedName("IPTVIMPIPforIPv6")
    private String IPTVIMPIPforIPv6;

    /**
     *IPTV领域的IM服务器IP V4地址。

     */
    @SerializedName("IPTVIMPIP")
    private String IPTVIMPIP;

    /**
     *IM服务的域名。

     */
    @SerializedName("IMDomain")
    private String IMDomain;

    /**
     *IMP对客户端登录的鉴权方式。

     0：采用摘要校验
     1：采用会话TOKEN校验
     默认值为0。

     */
    @SerializedName("ImpAuthType")
    private String ImpAuthType;

    /**
     *节目单展示的最晚时间间隔，单位天，默认值7，表示终端展示最晚7天后的节目单

     */
    @SerializedName("PlaybillLen")
    private String PlaybillLen;

    /**
     *节目单展示的最早时间间隔，单位天，默认值7，表示终端展示最早7天前的节目单

     */
    @SerializedName("RecPlaybillLen")
    private String RecPlaybillLen;

    /**
     *第三方业务质量管理系统地址。

     IP地址支持V4或V6。

     */
    @SerializedName("MQMCURL")
    private String MQMCURL;

    /**
     *机顶盒支持的CPVR并发录制数

     */
    @SerializedName("ConcurrentCpvrTaskLimit")
    private String ConcurrentCpvrTaskLimit;

    public List<NamedParameter> getValues() {
        return values;
    }

    public void setValues(List<NamedParameter> values) {
        this.values = values;
    }

    /**
     * the configuration parameter,the item of key
     */
    public interface Key {
        /**
         * 返回value值为电视看点下面的回看节目单的父栏目ID
         */
        String TV_WATCH_SUBJECT_ID = "tvwatch_subject_id";

        /**
         * 是否展示频道名称，返回值为0或1，0表示不展示，1表示展示
         */
        String IS_SHOWCHANNEL_NAME="tvwatch_canshow_channelname";

        /**
         * 默认频道号
         */
        String PLAY_DEFAULT_CHANNEL = "default_channelNO";

        /**
         * 直播页面 infobar显示多少时间消失
         */
        String LIVE_TV_INFOBAR_DELAY_TIME="liveTV_menu_show_time";
        /**
         * 资讯模板轮播图滚动间隔
         */
        String BANNER_SWITCHING_TIME = "banner_switching_time";
        /**
         * 资讯模板垂直滚动文字滚动间隔
         */
        String SCROLLTEXT_SWITCHING_TIME = "scrollText_switching_time";
        /**
         * 不展示评分
         */
        String NOSCOREDISPLAYEDSUBJECTIDS = "noScoreDisplayedSubjectIDs";
        /**
         * 不展示评分的CMSType
         */
        String NOSCOREDISPLAYEDCMSTYPE = "noScoreDisplayedCmsType";
        /**
         *第三方支付有效时间,秒
         */
        String THIRD_PAY_EXPIRED_TIME_DURATION="thirdPayExpiredTime";
        /**
         * 是否打开时移
         */
        String OPEN_LIVE_TVTS="openLiveTVTS";

        /**
         * VOD详情支持展示的分辨率标识
         */
        String VODDETAIL_DEFINITION_DISPLAY = "voddetail_definition_display";

        /**
         * 跳过片头片尾开关
         */
        String VOD_SKIP_SWITCH = "vod_skip_switch";

        /**
         * 基本业务中用于查询图片url和跳转url的subjectID
         */
        String BASE_BUSINESS_SUBJCET_ID = "base_business_subject_id";

        /**
         * 订购二次确认弹框落焦
         */
        String ORDER_SECOND_CONFIRM_FOCUS = "order_second_confirm_focus";

        /**
         * 话费支付余额校验最低价格
         */
        String ORDER_PHONEBILL_PAY_MINIMUM_AMOUNT = "order_phonebill_pay_minimum_amount";

        /**
         * 订购产品包由互斥包时订购失败弹出的Toast提示语
         */
        String ORDER_PRODUCT_MUTEX_INFO = "order_product_mutex_info";

        /**
         * 我的订购记录页面不展示产品包id
         */
        String MY_ORDER_UNSHOW_LIST = "my_order_unshow_list";

        /*
         *我的订购记录退订时定制的产品包的营销活动的退订提示语
         */
        String UNSUBSCRIBE_TIPS = "unsubscribe_tips";

        /*
         *订购赠送营销活动的产品包id
         */
        String GIFT_PRODUCTID = "gift_productid";

        /*
         *订购赠送营销活动的产品包是否需要跳转H5
         */
        String GIFT_PRODUCTID_SWITCH = "gift_productid_switch";

        /*
         *是否需要跳转至H5订购页面
         */
        String NEED_JUMP_TO_H5_ORDER = "need_jump_to_h5";

        /*
         *重复订购的间隔时间
         */
        String AVOID_REPEAT_PAYMENT_TIME = "avoid_repeat_payment_time";

        /*
         *4K节目详情页提示
         */
        String VOD_4K_WARNNING_TIP = "vod_4k_warnning_tip";

        /*
         *不展示演职员列表的vod类型
         */
        String NOT_SHOW_CAST_LIST_CMSTYPE = "not_show_cast_list_cmstype";

        /*
         *vod详情页 预播放白名单
         */
        String VOD_DETAIL_SUPPORT_PREPLAY = "vod_detail_support_preplay";

        /*
         *智能推荐配置,用于配置详情页，订购过渡页，播放返回页智能推荐是否开启以及关闭之后展示人工推荐的栏目id
         */
        String RECOMMEND_CONFIG = "recommend_config";

        /*
         *详情页的最近热播栏目，人工推荐的栏目id
         */
        String VODDETAIL_RECOMMEND_RECENT = "voddetail_recommend_recent";

        /*
         *退订挽留页按钮的配置终端参数
         */
        String UNSUBSCRIBE_BUTTON_CONFIG = "unsubscribe_button_config";

        /*
         *详情页角标开关
         */
        String SUPERSCRIPT_SWIRCH = "superscript_switch";

        /*
         *倍速播放黑名单
         */
        String CAN_SET_SPEED_BLACK_LIST = "can_set_speed_black_list";

        /*
         *详情页推荐内容配置
         */
        String DETAIL_RECOM_CONFIG = "detail_recom_config";

        /*
         *青铜/白银包的产品包id
         */
        String CHOI_PRODUCT = "choiProduct25";

        /*
         *退出播放页的智能推荐配置
         */
        String PLAYBACK_RECOM_CONFIG = "playback_recom_config";
    }
}