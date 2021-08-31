package com.pukka.ydepg.common.constant;

/**
 * Created by wgy on 2017/4/19.
 */
public class HttpConstant {
    //推荐url
    public static final String PBS_URL="http://aikanvod.miguvideo.com:8858/pvideo/p/stb_CviRecommend.jsp";

    //用户群（标签库客户画像）查询url
    public static final String GET_LABELS_BY_USER_URL="http://aikanvod.miguvideo.com:8858/pvideo/p/stb_getLabelsByUserId.jsp";

    //智能推荐url
    public static final String PBS_Duplicate_URL="http://aikanvod.miguvideo.com:8858/pvideo/p/stb_CviDuplicate.jsp";

    /**
     * http://aikanvod.miguvideo.com:8858/pvideo/p/stb_remixRecommend.jsp?offset=0&count=6&appointedIds=recom001&contentIds=ID1,ID2,ID3,ID4,ID5&vt=9
     * 混合智能推荐接口
     * */
    public static final String PBS_RemixRecommend_URL="http://aikanvod.miguvideo.com:8858/pvideo/p/stb_remixRecommend.jsp";

    //查询角标
    public static final String PBS_GetSuperScriptInfo="http://aikanvod.miguvideo.com:8858/pvideo/p/stb_getSuperScriptInfo.jsp?vt=9";

    //查询首页轮播推荐数据
    public static final String PBS_QueryCarouselsById_URL="http://aikanvod.miguvideo.com:8858/pvideo/p/stb_queryCarouselsById.jsp";

    // VSS平台认证appKey
    public static final String AppKey="00000000000000000004000000000000";
    // VSS平台认证appSecret
    public static final String AppSecret ="4a747f28cb32100b";

    public static final String HTTPS_VSP_IP_VSP_PORT_VSP_V3 = "/VSP/V3/";

    public static final String HTTPS_VSP_IP_VSS_PORT_VSP    = "/VSP/VSS/";

    /**
     * 在线心跳
     * https://VSP IP:VSP Port/VSP/V3/OnLineHeartbeat
     */
    public static final String ONLINEHEARTBEAT = "OnLineHeartbeat";

    /**
     * 获取频道栏目列表
     * https://VSP IP:VSP Port/VSP/V3/QueryChannelSubjectList
     */
    public static final String QUERYCHANNELSUBJECTLIST = "QueryChannelSubjectList";

    /**
     * 获取栏目下频道列表
     * https://VSP IP:VSP Port/VSP/V3/QueryChannelStcPropsBySubject
     */
    public static final String QUERYCHANNELSTCPROPSBYSUBJECT = "QueryChannelStcPropsBySubject";

    /**
     * 获取频道下节目单列表
     * https://VSP IP:VSP Port/VSP/V3/QueryPlaybillList
     */
    public static final String QUERYPLAYBILLLIST = "QueryPlaybillList";
    public static final String QUERYPLAYBILLCONTEXT = "QueryPlaybillContext";

    /**
     * 直播播放鉴权
     * https://VSP IP:VSP Port/VSP/V3/PlayChannel
     */
    public static final String PLAYCHANNEL = "PlayChannel";

    /**
     * 上报频道行为
     * https://VSP IP:VSP Port/VSP/V3/ReportChannel
     */
    public static final String REPORTCHANNEL = "ReportChannel";

    /**
     * 获取VOD栏目列表
     * https://VSP IP: VSP Port/VSP/V3/QueryVODSubjectList
     */
    public static final String QUERYVODSUBJECTLIST = "QueryVODSubjectList";

    /**
     * 获取栏目下VOD列表 (Not Recommended。如果不需要根据是否已订购过滤,请使用QueryVODListStcPropsBySubject接口）
     * https://VSP IP: VSP Port/VSP/V3/QueryVODListBySubject
     */
    public static final String QUERYVODLISTBYSUBJECT = "QueryVODListBySubject";

    /**
     * 获取栏目下VOD列表（更换过之后的）
     * https://VSP IP: VSP Port/VSP/V3/QueryVODListBySubject
     */
    public static final String QUERYVODLISTSTCPROPSBYSUBJECT = "QueryVODListStcPropsBySubject";

    /**
     * 获取栏目下VOD列表
     * https://VSP IP: VSP Port/VSP/V3/QueryVODListBySubject
     */
    public static final String QUERYCHANNELLISTBYSUBJECT = "BatchQueryChannelListBySubject";

    /**
     * 获取栏目下子栏目及子栏目中VOD列表(Deprecated,使用接口QueryVODSubjectList和QueryVODListStcPropsBySubject替换当前接口)
     * https://VSP IP: VSP Port/VSP/V3/QuerySubjectVODBySubjectID
     */
    public static final String QUERYSUBJECTVODBYSUBJECTID = "QuerySubjectVODBySubjectID";

    /**
     * 获取内容配置
     * https://VSP IP: VSP Port/VSP/V3/GetContentConfig
     */
    public static final String GETCONTENTCONFIG = "GetContentConfig";

    /**
     * 查询VOD详情
     * https://VSP IP: VSP Port/VSP/V3/GetVODDetail
     */
    public static final String GETVODDETAIL = "GetVODDetail";

    /**
     * 点播播放鉴权
     * https://VSP IP: VSP Port/VSP/V3/PlayVOD
     */
    public static final String PLAYVOD = "PlayVOD";

    /**
     * 点播下载鉴权
     * https://VSP IP: VSP Port/VSP/V3/DownloadVOD
     */
    public static final String DOWNLOADVOD = "DownloadVOD";

    /**
     * 上报点播行为
     * https://VSP IP: VSP Port/VSP/V3/ReportVOD
     */
    public static final String REPORTVOD = "ReportVOD";

    /**
     * 搜索内容
     * https://VSP IP: VSP Port/VSP/V3/SearchContent
     */
    public static final String SEARCHCONTENT = "SearchContent";

    /**
     * 搜索关键词联想
     * https://VSP IP:VSP Port/VSP/V3/SuggestKeyword
     */
    public static final String SUGGESTKEYWORD = "SuggestKeyword";

    /**
     * 新增收藏
     * https://VSP IP: VSP Port/VSP/V3/CreateFavorite
     */
    public static final String CREATEFAVORITE = "CreateFavorite";

    /**
     * 删除收藏
     * https://VSP IP: VSP Port/VSP/V3/DeleteFavorite
     */
    public static final String DELETEFAVORITE = "DeleteFavorite";

    /**
     * 查询收藏
     * https://VSP IP: VSP Port/VSP/V3/QueryFavorite
     */
    public static final String QUERYFAVORITE = "QueryFavorite";

    /**
     * 新增收藏夹
     * https://VSP IP: VSP Port/VSP/V3/AddFavoCatalog
     */
    public static final String ADDFAVOCATALOG = "AddFavoCatalog";

    /**
     * 删除收藏夹
     * https://VSP IP: VSP Port/VSP/V3/DeleteFavoCatalog
     */
    public static final String DELETEFAVOCATALOG = "DeleteFavoCatalog";

    /**
     * 查询收藏夹
     * https://VSP IP: VSP Port/VSP/V3/QueryFavoCatalog
     */
    public static final String QUERYFAVOCATALOG = "QueryFavoCatalog";

    /**
     * 新增书签
     * https://VSP IP: VSP Port/VSP/V3/CreateBookmark
     */
    public static final String CREATEBOOKMARK = "CreateBookmark";

    /**
     * 删除书签
     * https://VSP IP: VSP Port/VSP/V3/DeleteBookmark
     */
    public static final String DELETEBOOKMARK = "DeleteBookmark";

    /**
     * 查询书签
     * https://VSP IP: VSP Port/VSP/V3/QueryBookmark
     */
    public static final String QUERYBOOKMARK = "QueryBookmark";

    /**
     * 新增评分
     * https://VSP IP: VSP Port/VSP/V3/CreateContentScore
     */
    public static final String CREATECONTENTSCORE = "CreateContentScore";

    /**
     * 动态桌面
     * https://VSP IP: VSP Port/VSP/V3/QueryLauncher
     */
    public static final String QUERYLAUNCHER = "QueryLauncher";

    /**
     * 终端在登录完成后访问智能桌面配置。
     * https://VSP IP:VSP Port/VSP/V3/LoginPHSRoute
     */
    public static final String LOGIN_PHS_ROUTE = "LoginPHSRoute";

    /**
     * 此接口提供给终端使用，终端通过访问该接口获取桌面布局配置文件，进行本地解析、渲染、业务数据获取，最终生成终端首页展示页面。
     * https://phmserver.huawei.com/video/phs/QueryPHMLauncherList
     */
    public static final String QUERYPHMLAUNCHERLIST = "/video/phs/QueryPHMLauncherList";

    /**
     * 获取频道列表静态属性（Java）
     * https://VSP IP:VSP Port/VSP/V3/QueryAllChannel?userFilter=XXX

     */
    public static final String QUERYALLCHANNEL = "QueryAllChannel";

    /**
     * 获取频道列表动态属性（Java）
     * https://VSP IP:VSP Port/VSP/V3/QueryAllChannelDynamicProperties
     */
    public static final String QUERYALLCHANNELDYNAMICPROPERTIES = "QueryAllChannelDynamicProperties";

    /**
     *查询配置参数接口
     *https://VSP IP:VSP Port/VSP/V3/QueryCustomizeConfig
     */
    public static final String QUERYCUSTOMIZECONFIG="QueryCustomizeConfig";

    /**
     * DSV v6相关新增/替换的请求接口
     */
    public static final String QUERY_SUBSCRIPTION="QuerySubscription";

    /**
     * 产品订购SubscribeProduct
     */
    public static final String SUBSCRIBE_PRODUCT="SubscribeProduct";
    /**
     * 产品退订CancelSubscribe
     */
    public static final String CANCEL_SUBSCRIBE="CancelSubscribe";
    /**
     * 查询Profile
     */
    public static final String QUERY_PROFILE="QueryProfile";
    /**
     * 修改profile
     */
    public static final String MODIFY_PROFILE="ModifyProfile";
    /**
     * 添加Profile
     */
    public static final String ADD_PROFILE="AddProfile";

    /**
     * 用户名密码认证登录
     */
    public static final String AUTHENTICATE="Authenticate";

    /**
     * 获得栏目的详情
     */
    public static final String QUERYSUBJECT_DETAIL="QuerySubjectDetail";

    /**
     * 生成二维码并发起二维码认证
     */
    public static final String QRCODEAUTHENTICATE = "QrCodeAuthenticate";

    /**
     * 查询二维码认证状态
     */
    public static final String QUERYQRCODEAUTHENTICATESTATUS = "QueryQrCodeAuthenticateStatus";

    /**
     * 固移绑定帐号查询(浙江/福建移动)
     */
    public static final String QUERYBINDEDSUBSCRIBER = "QueryBindedSubscriber";

    /**
     *固移帐号解绑(浙江移动)
     */
    public static final String UNBINDSUBSCRIBER = "UnBindSubscriber";

    /**
     *退出二维码认证状态
     */
    public static final String QUITQRCODEAUTHENTICATE = "QuitQrCodeAuthenticate";


    /**
     *获取首页VOD列表
     */
    public static final String QUERYEPGHOMEVOD = "QueryEpgHomeVod";

    /**
     *获取多个栏目下VOD列表
     */
    public static final String BATCH_QUERY_VODLIST_BYSUBJECT = "BatchQueryVODListBySubject";

    /**
     *设置当前二维码认证用户
     */
    public static final String SETQRCODEAUTHENTICATEDSUBSCRIBER = "SetQrCodeAuthenticatedSubscriber";
    /**
     * 发送验证码
     */
    public static final String SENDSMS = "SendSMS";

    /**
     * 终端上报设备信息
     */
    public static final String SUBMITDEVICEINFO = "SubmitDeviceInfo";

    /**
     * 查询订户信息
     */
    public static final String QUERY_SUBSCRIBE_INFO="QuerySubscriber";

    /**
     * VRS自定义用户属性查询接口
     */
    public static final String QUERY_USER_ATTRS="UserManagementServices/queryUserAttrs/v1";

    /**
     * VRS自定义用户属性查询接口
     */
    public static final String MODIFY_USER_ATTR="UserManagementServices/modifyUserAttr/v1";

    /**
     * 查询用户统一支付开通情况
     */
    public static final String QUERY_UNIPAY_INFO="QueryUniPayInfo";
    /**
     * 查询专题信息
     */
    public static final String QUERY_GET_TOPICS="GetTopics";

    /**
     * 获得开机图片
     */
    public static final String GET_START_PICTURE="GetStartPicture";

    /**
     * 用户在需要查询产品信息时调用此接口。
     */
    public static final String QUERY_PRODUCT="QueryProduct";
    /**
     * 更新订户信息
     */

    public static final String UPDATE_SUBSCRIBER="UpdateSubscriber";
    /**
     * 验证短信信息
     */
    public static final String UPDATE_USER_REGINFO="UpdateUserRegInfo";

    /**
     * 订购
     */
    public static final String ORDER_PRODUCT="vss/subProduct/orderProduct/v1";
    /**v1
     * 查询用户信息
     */
    public static final String QUERY_MULTIQRY="BossProductSaleServices/queryMultiqry/v1";

    /**
     * 查询精简的用户信息
     */
    public static final String QUERY_MULTI_USER_INFO ="BossProductSaleServices/queryMultiUserInfo/v1";

    /**
     * 查询订单信息
     */
    public static final String QUERY_ORDER_INFO = "queryOrderInfo/v1";

    /**
     * 取消订单
     */
    public static final String CANCEL_ORDER         = "ProductSaleServices/cancelOrder/v1";


    //BOSS超级接口
    public static final String QUERY_RESULT_BAL     = "BossProductSaleServices/generalBossQuery/v1";

    /*演员详情接口*/
    public static final String GET_CAST_DETAIL      = "GetCastDetail";
    /*查询固移融合关联内容接口*/
    public static final String GET_RELATED_CONTENT  = "GetRelatedContent";

    //VRS发送验证码接口
    public static final String SEND_VERIFIED_CODE = "SystemManagement/sendVerifiedCode/v1";

    //VRS验证验证码接口
    public static final String CHECK_VERIFIED_CODE = "SystemManagement/checkVerifiedCode/v1";

    public static final String CHECK_VERIFIED_CLIENT_CODE = "SystemManager_Client/checkVerifiedCode/v1";
    public static final String BATCH_SEND_SMS = "dic/sms/batchSendSms/v1";

    //VRS积分换算接口
    public static final String CONVERT_FEE_TO_SCORE = "ProductSaleServices/convertFeeToScore/v1";

    //获取互斥关系接口
    public static final String GET_PRODUCT_MUT_EX_RELA = "ProductSaleServices/ckProductMutExRela/v1";

    //VRS当前童锁状态查询功能
    public static final String QUERY_USER_ORDER_SIWTCH = "UserManager_Cn_Zjmobile/queryUserOrderingSwitch/v1";

    //VRS用户童锁状态变更
    public static final String CHANGE_USER_ORDER_SWITCH = "UserManager_Cn_Zjmobile/changeUserOrderingSwitch/v1";

    //查询VOD信息（不包含子集和推荐信息）
    public static final String QUERY_VOD = "QueryVOD";

    //查询连续剧简要子集信息
    public static final String QUREY_EPISODE_BRIEF_INFO = "QueryEpisodeBriefInfo";

    //查询VOD子集列表
    public static final String QUERY_EPISODE_LIST = "QueryEpisodeList";

    //获取自选包订购关系自选内容剩余数和自选内容状态
    public static final String GET_ALACARTE_CHOOSED_CONTENTS = "vss/subProduct/getAlacarteChoosedContents/v1";

    //添加自选包订购关系自选内容和扣减内容余额
    public static final String ADD_ALACARTE_CHOOSED_CONTENTS = "vss/subProduct/addAlacarteChoosedContent/v1";

    //VRS查询订购关系
    public static final String VRS_QUERY_SUBSCRIPTION ="ProductSaleServices/querySubscription/v1";

    /**
     * 点播播放鉴权
     * https://VSP IP: VSP Port/VSP/V3/PlayVOD
     */
    public static final String PLAY_MULTI_MEDIA_VOD = "PlayMultiMediaVOD";
    /**
     * 筛选页
     */
    public static final String PBS_QUERY_VOD_URL="http://aikanvod.miguvideo.com:8858/pvideo/p/stb_selectContent.jsp";

    //PBS订购信息查询接口
    public static final String STB_ORDERINFO = "http://aikanvod.miguvideo.com:8858/pvideo/p/stb_orderInfo.jsp";

    //PBS内容关联的产品包角标查询接口
    public static final String STB_GET_SINGLE_MARKS = "http://aikanvod.miguvideo.com:8858/pvideo/p/stb_getSingleMarks.jsp";

}