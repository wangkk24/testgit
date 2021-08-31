package com.pukka.ydepg.common.http;


import com.google.gson.JsonObject;
import com.pukka.ydepg.annotation.RetrofitBuilder;
import com.pukka.ydepg.common.http.bean.node.DeviceInfo;
import com.pukka.ydepg.common.http.bean.request.CancelSubscribeRequest;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.request.OrderProductRequest;
import com.pukka.ydepg.common.http.bean.request.QueryBatchVODListBySubjectRequest;
import com.pukka.ydepg.common.http.bean.request.QueryEpgHomeVodRequest;
import com.pukka.ydepg.common.http.bean.request.QueryMultiqryRequest;
import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.bean.response.QueryBatchVODListBySubjectResponse;
import com.pukka.ydepg.common.http.bean.response.QueryEpgHomeVodResponse;
import com.pukka.ydepg.common.http.bean.response.QueryMultiqryResponse;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.bean.response.QueryRecommendResponse;
import com.pukka.ydepg.common.http.bean.response.SubscribeDeleteResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryCustomizeConfig;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.QueryAdvertContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.AddAlacarteChoosedContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.AddFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.AdvertReportRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.AuthenticateRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.BatchGetResStrategyDataRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.BatchQueryChannelListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.BatchSendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ChangeUserOrderingSwitchRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CheckClientVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CheckVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ConvertFeeToScoreRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateContentScoreRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CreateLockRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteLockRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DeleteProfileRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.DownloadVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetAlacarteChoosedContentsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetCastDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetContentConfigRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetLatestResourcesRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetProductMutExRelaRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetRelatedContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetStartPictureRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetVODDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.LoginRouteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.LogoutRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ModifyProfileRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ModifyUserAttrRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelHeartbeatRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayMultiMediaVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODHeartbeatRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QrCodeAuthenticateRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryAdvertTokenRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryAllChannelDynamicPropertiesRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryAllChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryAllHomeDataRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBindedSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelStcPropsBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryChannelSubjectListRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryCustomizeConfigRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryDeviceListRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryEpisodeBriedInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryEpisodeListReuqest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoCatalogRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryLauncherRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryLocationRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryLockRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryOTTLiveTVHomeDataRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryPlaybillContextRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryPlaybillListByChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryPlaybillListRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryProfileRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryQrCodeStatusRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryRecmContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryRecmVODListRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuerySubjectVODBySubjectIDRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUserAttrsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUserOrderingSwitchRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODSubjectListRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuitQrCodeAuthenticateRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReplaceDeviceRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportQrCodeStateRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SearchContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SearchContentsListRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SearchHotKeyRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SetGlobalFilterCondRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SetQrCodeSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SubmitDeviceInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SubscribeProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SuggestKeywordRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SwitchProfileRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UnBindSubsrciberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateUserRegInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.VRSQuerySubscriptionRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.AddAlacarteChoosedContentResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.AddFavoCatalogResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.AddProfileResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.AdvertReportResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.AuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.BatchGetResStrategyDataResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.BatchQueryChannelListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.BatchSendSmsResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ConvertFeeToScoreResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.CreateBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.CreateContentScoreResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.CreateFavoriteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.CreateLockResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.DeleteBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.DeleteFavoCatalogResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.DeleteFavoriteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.DeleteLockResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.DownloadVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetAlacarteChoosedContentsResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetCastDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetContentConfigResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetLatestResourcesResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetProductMutExRelaResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetRelatedContentResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetStartPictureResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.LoginPHMRouteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.LoginRouteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.LogoutResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ModifyUserAttrResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.OnLineHeartbeatResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelHeartbeatResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayMultiMediaVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODHeartbeatResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QrCodeAuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryAdvertContentResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryAdvertTokenResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryAllChannelDynamicPropertiesResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryAllChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryAllHomeDataResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBookmarkResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelStcPropsBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryChannelSubjectListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryCustomizeConfigResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryDeviceListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryEpisodeBriefInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryEpisodeListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoCatalogResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoriteResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryLauncherResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryLocationResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryLockResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryOTTLiveTVHomeDataResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPlaybillContextResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryPlaybillListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryProfileResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryRecmContentResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryRecmVODListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubjectDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubjectVODBySubjectIDResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUserAttrsResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUserOrderingSwitchResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODSubjectListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuitQrCodeAuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ReplaceDeviceResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ReportChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ReportQrCodeStateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ReportVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SearchContentResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SearchContentsListResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SearchHotKeyResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SetGlobalFilterCondResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SetQrCodeSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SubmitDeviceResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SuggestKeywordResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SwitchProfileResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UnBindSubsrciberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UpdateSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UpdateUserRegInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VRSQuerySubscriptionResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VerifiedClientCodeResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VerifiedCodeResponse;
import com.pukka.ydepg.common.http.vss.request.CancelOrderRequest;
import com.pukka.ydepg.common.http.vss.request.QueryMultiUserInfoRequest;
import com.pukka.ydepg.common.http.vss.request.QueryOrderInfoRequest;
import com.pukka.ydepg.common.http.vss.request.QueryResultRequest;
import com.pukka.ydepg.common.http.vss.response.CancelOrderResponse;
import com.pukka.ydepg.common.http.vss.response.QueryMultiUserInfoResponse;
import com.pukka.ydepg.common.http.vss.response.QueryOrderInfoResponse;
import com.pukka.ydepg.common.http.vss.response.QueryResultBalResponse;
import com.pukka.ydepg.launcher.bean.request.TopicRequest;
import com.pukka.ydepg.launcher.http.hecaiyun.data.QueryRequest;
import com.pukka.ydepg.launcher.http.hecaiyun.data.QueryResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * service统一接口数据
 */
@RetrofitBuilder(httpAPiName = HttpApi.class)
public interface HttpService {

    /**
     * * 5.27	APP查询已订购/未订购产品信息接口
     *
     * @param url
     * @param querySubscription
     * @return
     */
    @POST()
    Observable<QueryProductInfoResponse> queryProductInfo(@Url String url, @Body
        DSVQuerySubscription querySubscription);

    /**
     * 登录调度（Java）
     *
     * @param url
     * @param loginRouteRequest
     * @return
     */
    @POST()
    Observable<LoginRouteResponse> loginRoute(@Url String url, @Body LoginRouteRequest loginRouteRequest);

    /**
     * 登录认证
     *
     * @param url
     * @param
     * @return
     */
    @POST()
    Observable<AuthenticateResponse> authenticate(@Url String url, @Body AuthenticateRequest authenticateRequest);

    /**
     * 调用QueryCustomizeConfig获取终端配置参数。VSP平台在认证响应中返回Verimatrix、容灾等参数信息，客户端需要缓存这些数据
     * @param url
     * @param queryCustomizeConfig
     * @return
     */
    @POST()
    Observable<QueryCustomizeConfigResponse> queryCustomizeConfig(@Url String url, @Body QueryCustomizeConfig queryCustomizeConfig);

    /**
     * 帐号切换接口
     *
     * @param url
     * @param switchProfileRequest
     * @return
     */
    @POST()
    Observable<SwitchProfileResponse> SwitchProfile(@Url String url, @Body SwitchProfileRequest switchProfileRequest);

    /**
     * 在线心跳
     *
     * @param url
     * @return
     */
    @POST()
    Observable<OnLineHeartbeatResponse> onLineHeartbeat(@Url String url);

    /**
     * 退出登录
     *
     * @param url
     * @param logoutRequest
     * @return
     */
    @POST()
    Observable<LogoutResponse> logout(@Url String url, @Body LogoutRequest logoutRequest);

    /**
     * 获取设备列表
     *
     * @param url
     * @param queryDeviceListRequest
     * @return
     */
    @POST()
    Observable<QueryDeviceListResponse> queryDeviceList(@Url String url, @Body QueryDeviceListRequest queryDeviceListRequest);

    /**
     * 替换设备
     *
     * @param url
     * @param replaceDeviceRequest
     * @return
     */
    @POST()
    Observable<ReplaceDeviceResponse> replaceDevice(@Url String url, @Body ReplaceDeviceRequest replaceDeviceRequest);

    /**
     * 获取频道栏目列表
     *
     * @param url
     * @param queryChannelSubjectListRequest
     * @return
     */
    @POST()
    Observable<QueryChannelSubjectListResponse> queryChannelSubjectList(@Url String url, @Body QueryChannelSubjectListRequest queryChannelSubjectListRequest);

    /**
     * 获取栏目下频道列表
     *
     * @param url
     * @param queryChannelListBySubjectRequest
     * @return
     */
    @POST()
    Observable<QueryChannelListBySubjectResponse> queryChannelListBySubject(@Url String url, @Body QueryChannelListBySubjectRequest queryChannelListBySubjectRequest);

    /**
     * 获取频道下节目单列表
     *
     * @param url
     * @param queryPlaybillListRequest
     * @return
     */
    @POST()
    Observable<QueryPlaybillListResponse> queryPlaybillList(@Url String url, @Body QueryPlaybillListRequest queryPlaybillListRequest);

    /**
     * 获取频道节目单上下（Java）
     *
     * @param url
     * @param queryPlaybillContextRequest
     * @return
     */
    @POST()
    Observable<QueryPlaybillContextResponse> queryPlaybillContext(@Url String url, @Body QueryPlaybillContextRequest queryPlaybillContextRequest);

    /**
     * 根据频道上下文获取频道下节目单列表
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryPlaybillListResponse> queryPlaybillListByChannel(@Url String url, @Body QueryPlaybillListByChannelRequest request);

    /**
     * 直播播放鉴权
     *
     * @param url
     * @param playChannelRequest
     * @return
     */
    @POST()
    Observable<PlayChannelResponse> playChannel(@Url String url, @Body PlayChannelRequest playChannelRequest);

    /**
     * 上报频道行为
     *
     * @param url
     * @param reportChannelRequest
     * @return
     */
    @POST()
    Observable<ReportChannelResponse> reportChannel(@Url String url, @Body ReportChannelRequest reportChannelRequest);

    /**
     * 频道播放心跳
     *
     * @param url
     * @param playChannelHeartbeatRequest
     * @return
     */
    @POST()
    Observable<PlayChannelHeartbeatResponse> playChannelHeartbeat(@Url String url, @Body PlayChannelHeartbeatRequest playChannelHeartbeatRequest);

    /**
     * 获取VOD栏目列表
     *
     * @param url
     * @param queryVODSubjectListRequest
     * @return
     */
    @POST()
    Observable<QueryVODSubjectListResponse> queryVODSubjectList(@Url String url, @Body QueryVODSubjectListRequest queryVODSubjectListRequest);

    /**
     * 获取栏目下VOD列表
     *
     * @param url
     * @param queryVODListBySubjectRequest
     * @return
     */
    @POST()
    Observable<QueryVODListBySubjectResponse> queryVODListBySubject(@Url String url, @Body QueryVODListBySubjectRequest queryVODListBySubjectRequest);

    /**
     * 获取栏目下子栏目及子栏目中VOD列表
     *
     * @param url
     * @param querySubjectVODBySubjectIDRequest
     * @return
     */
    @POST()
    Observable<QuerySubjectVODBySubjectIDResponse> querySubjectVODBySubjectID(@Url String url, @Body QuerySubjectVODBySubjectIDRequest querySubjectVODBySubjectIDRequest);

    /**
     * 获取内容配置
     *
     * @param url
     * @param getContentConfigRequest
     * @return
     */
    @POST()
    Observable<GetContentConfigResponse> getContentConfig(@Url String url, @Body GetContentConfigRequest getContentConfigRequest);

    /**
     * 查询VOD详情
     *
     * @param url
     * @param getVODDetailRequest
     * @return
     */
    @POST()
    Observable<GetVODDetailResponse> getVODDetail(@Url String url, @Body GetVODDetailRequest getVODDetailRequest);

    /**
     * 点播播放鉴权
     *
     * @param url
     * @param playVODRequest
     * @return
     */
    @POST()
    Observable<PlayVODResponse> playVOD(@Url String url, @Body PlayVODRequest playVODRequest);

    /**
     * 点播下载鉴权
     *
     * @param url
     * @param downloadVODRequest
     * @return
     */
    @POST()
    Observable<DownloadVODResponse> downloadVOD(@Url String url, @Body DownloadVODRequest downloadVODRequest);

    /**
     * 上报点播行为
     *
     * @param url
     * @param reportVODRequest
     * @return
     */
    @POST()
    Observable<ReportVODResponse> reportVOD(@Url String url, @Body ReportVODRequest reportVODRequest);

    /**
     * 点播播放心跳
     *
     * @param url
     * @param playVODHeartbeatRequest
     * @return
     */
    @POST()
    Observable<PlayVODHeartbeatResponse> playVODHeartbeat(@Url String url, @Body PlayVODHeartbeatRequest playVODHeartbeatRequest);

    /**
     * 搜索热词
     *
     * @param url
     * @param searchHotKeyRequest
     * @return
     */
    @POST()
    Observable<SearchHotKeyResponse> searchHotKey(@Url String url, @Body SearchHotKeyRequest searchHotKeyRequest);

    /**
     * 热搜内容查询
     *
     * @param url
     * @param searchContentsListRequest
     * @return
     */
    @POST()
    Observable<SearchContentsListResponse> qeryHotSearchContentsList(@Url String url, @Body SearchContentsListRequest searchContentsListRequest);

    /**
     * 搜索内容
     *
     * @param url
     * @param searchContentRequest
     * @return
     */
    @POST()
    Observable<SearchContentResponse> searchContent(@Url String url, @Body SearchContentRequest searchContentRequest);

    /**
     * 搜索关键词联想
     *
     * @param url
     * @param suggestKeywordRequest
     * @return
     */
    @POST()
    Observable<SuggestKeywordResponse> suggestKeyword(@Url String url, @Body SuggestKeywordRequest suggestKeywordRequest);

    /**
     * 推荐内容
     *
     * @param url
     * @param queryRecmContentRequest
     * @return
     */
    @POST()
    Observable<QueryRecmContentResponse> queryRecmContent(@Url String url, @Body QueryRecmContentRequest queryRecmContentRequest);

    /**
     * 新增收藏
     *
     * @param url
     * @param createFavoriteRequest
     * @return
     */
    @POST()
    Observable<CreateFavoriteResponse> createFavorite(@Url String url, @Body CreateFavoriteRequest createFavoriteRequest);

    /**
     * 删除收藏
     *
     * @param url
     * @param deleteFavoriteRequest
     * @return
     */
    @POST()
    Observable<DeleteFavoriteResponse> deleteFavorite(@Url String url, @Body DeleteFavoriteRequest deleteFavoriteRequest);

    /**
     * 查询收藏
     *
     * @param url
     * @param queryFavoriteRequest
     * @return
     */
    @POST()
    Observable<QueryFavoriteResponse> queryFavorite(@Url String url, @Body QueryFavoriteRequest queryFavoriteRequest);

    /**
     * 新增收藏夹
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<AddFavoCatalogResponse> addFavoCatalog(@Url String url, @Body AddFavoCatalogRequest request);

    /**
     * 删除收藏夹
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<DeleteFavoCatalogResponse> deleteFavoCatalog(@Url String url, @Body DeleteFavoCatalogRequest request);

    /**
     * 查询收藏夹
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryFavoCatalogResponse> queryFavoCatalog(@Url String url, @Body QueryFavoCatalogRequest request);

    /**
     * 新增书签
     *
     * @param url
     * @param createBookmarkRequest
     * @return
     */
    @POST()
    Observable<CreateBookmarkResponse> createBookmark(@Url String url, @Body CreateBookmarkRequest createBookmarkRequest);

    /**
     * 删除书签
     *
     * @param url
     * @param deleteBookmarkRequest
     * @return
     */
    @POST()
    Observable<DeleteBookmarkResponse> deleteBookmark(@Url String url, @Body DeleteBookmarkRequest deleteBookmarkRequest);

    /**
     * 查询书签
     *
     * @param url
     * @param queryBookmarkRequest
     * @return
     */
    @POST()
    Observable<QueryBookmarkResponse> queryBookmark(@Url String url, @Body QueryBookmarkRequest queryBookmarkRequest);

    /**
     * 新增评分
     *
     * @param url
     * @param createContentScoreRequest
     * @return
     */
    @POST()
    Observable<CreateContentScoreResponse> createContentScore(@Url String url, @Body CreateContentScoreRequest createContentScoreRequest);

    /**
     * 动态桌面
     *
     * @param url
     * @param queryLauncherRequest
     * @return
     */
    @POST()
    Observable<QueryLauncherResponse> queryLauncher(@Url String url, @Body QueryLauncherRequest queryLauncherRequest);

    /**
     *动态桌面数据
     *
     * @param url
     * @param queryAllHomeDataRequest
     * @return
     */
    @POST()
    Observable<QueryAllHomeDataResponse> queryAllHomeData(@Url String url, @Body QueryAllHomeDataRequest queryAllHomeDataRequest);


    /**
     *设置全局内容过滤条件
     * @param url
     * @param setGlobalFilterCondRequest
     * @return
     */
    @POST()
    Observable<SetGlobalFilterCondResponse> setGlobalFilterCond(@Url String url, @Body SetGlobalFilterCondRequest setGlobalFilterCondRequest);


    /**
     *VOD排行榜
     * @param url
     * @param queryRecmVODListRequest
     * @return
     */
    @POST()
    Observable<QueryRecmVODListResponse> QueryRecmVODList(@Url String url, @Body QueryRecmVODListRequest queryRecmVODListRequest);

    /**
     *直播首页数据
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryOTTLiveTVHomeDataResponse> queryOTTLiveTVHomeData(@Url String url, @Body QueryOTTLiveTVHomeDataRequest request);

    /*
    * 上报二维码认证状态
    *
    *
    */
    @POST()
    Observable<ReportQrCodeStateResponse> reportQrCodeState(@Url String url, @Body ReportQrCodeStateRequest request);

    /**
     * 修改设备信息
     * @param url
     * @param info
     * @return
     */
    @POST()
    Observable<BaseResponse> modifyDeviceInfo(@Url String url,@Body DeviceInfo info);
    /**
     *VSP提供地理位置查询接口，通过该接口获取终端所在的地理位置信息。
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryLocationResponse> queryLocation(@Url String url, @Body
        QueryLocationRequest request);

    /**
     *查询配置参数接口
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryCustomizeConfigResponse> queryCustomizeConfig(@Url String url, @Body
        QueryCustomizeConfigRequest request);

    /**
     * 订购
     * @param url
     * @param subscribeRequest
     * @return
     */
    @POST()
    Observable<SubscribeProductResponse> subscribe(@Url String url,@Body SubscribeProductRequest subscribeRequest);
    /**
     * 退订
     * @param url
     * @param unsubRequest
     * @return
     */
    @POST()
    Observable<SubscribeDeleteResponse> unsubscribe(@Url String url,@Body CancelSubscribeRequest unsubRequest);
    /**
     * 查询Profile
     */
    @POST()
    Observable<QueryProfileResponse> queryProfileInfo(@Url String url,@Body QueryProfileRequest request);
    /**
     * 添加,修改profile
     */
    @POST()
    Observable<AddProfileResponse> modifyProfile(@Url String url,@Body ModifyProfileRequest request);
    /**
     * 删除profile
     */
    @POST()
    Observable<BaseResponse> removeProfile(@Url String url,@Body DeleteProfileRequest request);

    /**
     * 获取频道列表静态属性（Java）
     */
    @POST()
    Observable<QueryAllChannelResponse> queryAllChannel(@Url String url, @Body QueryAllChannelRequest request);

    /**
     * 获取频道列表静态属性（Java）
     */
    @POST()
    Observable<QueryAllChannelDynamicPropertiesResponse> queryAllChannelDynamicProperties(@Url String url, @Body QueryAllChannelDynamicPropertiesRequest request);

    /**
     * 查询频道锁
     */
    @POST()
    Observable<QueryLockResponse> queryLock(@Url String url,@Body QueryLockRequest request);

    /**
     * 删除频道锁
     * @param url
     * @return
     */
    @POST()
    Observable<DeleteLockResponse> deleteLock(@Url String url,@Body DeleteLockRequest request);

    /**
     * 新增锁
     * @param url
     * @return
     */
    @POST()
    Observable<CreateLockResponse> createLock(@Url String url, @Body CreateLockRequest request);

    /**
     * 升级请求调度
     * @param url
     * @param request
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> upgrade(@Url String url,@FieldMap Map<String,String> request);


    /**
     * 通用的HTTP GET请求
     * @param url 请求URL
     * @return
     */
    @GET()
    Observable<ResponseBody> sendGetRequest(@Url String url);

    /**
     * 获得栏目详情
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QuerySubjectDetailResponse> QuerySubjectDetail(@Url String url, @Body QuerySubjectDetailRequest request);


    /**
     *
     生成二维码并发起二维码认证
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QrCodeAuthenticateResponse> qrCodeAuthenticate(@Url String url, @Body QrCodeAuthenticateRequest request);

    /**
     *
     查询二维码认证状态
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryQrCodeStatusResponse> queryQrCodeAuthenticateStatus(@Url String url, @Body QueryQrCodeStatusRequest request);



    /**
     *
     *固移绑定帐号查询(浙江/福建移动)
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryBindedSubscriberResponse> qeryBindedSubscriber(@Url String url, @Body QueryBindedSubscriberRequest request);

    /**
     *解绑
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<UnBindSubsrciberResponse> unBindSubsrciber(@Url String url, @Body UnBindSubsrciberRequest request);


    /**
     *解绑
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QuitQrCodeAuthenticateResponse> quitQrCodeAuthentiCate(@Url String url, @Body QuitQrCodeAuthenticateRequest request);


    /**
     * 获取首页VOD列表接口
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryEpgHomeVodResponse> queryEpgHomeVod(@Url String url, @Body QueryEpgHomeVodRequest request);

    /**
     * 获取首页VOD列表接口
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryBatchVODListBySubjectResponse> queryBatchVODListBySubject(@Url String url, @Body QueryBatchVODListBySubjectRequest request);


    /**
     *解绑
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<SetQrCodeSubscriberResponse> setQrCodeAuthenticatedSubscriber(@Url String url, @Body SetQrCodeSubscriberRequest request);

    /**
     * 发送短信验证码
     * @param url
     * @param request
     */
    @POST()
    Observable<BaseResponse> sendSMS(@Url String url,@Body SendSmsRequest request);

    /**
     * 获取用户产品信息
     * @param url
     * @param request
     */
    @POST()
    Observable<QueryProductInfoResponse> querySubscription(@Url String url,@Body DSVQuerySubscription request);

    /**
     * 上报
     * @param url
     * @param request
     */
    @POST()
    Observable<SubmitDeviceResponse> submitDeviceInfo(@Url String url,@Body SubmitDeviceInfoRequest request);

    /**
     * 下载launcher
     */
    @GET()
    Observable<ResponseBody> downloadLauncher(@Url String url);

    /**
     * 查询订户信息 c40 接口
     * @param url url
     * @param request request
     */
    @POST()
    Observable<QuerySubscriberResponse> querySubscriberInfo(@Url String url,@Body QueryUniInfoRequest request);

    /**
     * 查询订户信息 c40 接口
     * @param url url
     * @param request request
     */
    @POST()
    Observable<QueryUserAttrsResponse> queryUserAttrs(@Url String url,@Body QueryUserAttrsRequest request);

    /**
     * 查询订户信息 c40 接口
     * @param url url
     * @param request request
     */
    @POST()
    Observable<ModifyUserAttrResponse> modifyUserAttr(@Url String url,@Body ModifyUserAttrRequest request);

    /**
     * 查询用户的统一支付开通情况
     * 目前此接口仅在浙江移动使用
     * @param url url
     * @param request request
     */
    @POST()
    Observable<QueryUniPayInfoResponse> queryUniPayInfo(@Url String url,@Body
        QueryUniInfoRequest request);

    /**
     * 查询Topic信息
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<JsonObject> queryTopic(@Url String url, @Body
            TopicRequest request);

    /**
     *
     * 查询开机动画
     * @param url url
     * @param request request
     */
    @POST()
    Observable<GetStartPictureResponse> getStartPicture(@Url String url, @Body
            GetStartPictureRequest request);

    /**
     *
     * 查询订购产品详情
     * @param url url
     * @param request request
     */
    @POST()
    Observable<QueryProductResponse> queryProduct(@Url String url, @Body
            QueryProductRequest request);


    /**
     *
     * 更新订户信息
     * @param url url
     * @param request request
     */
    @POST()
    Observable<UpdateSubscriberResponse> updateSubscriber(@Url String url, @Body
            UpdateSubscriberRequest request);


    /**
     *
     * 校验验证码
     * @param url url
     * @param request request
     */
    @POST()
    Observable<UpdateUserRegInfoResponse> updateUserRegInfo(@Url String url, @Body
            UpdateUserRegInfoRequest request);

    /**
     * Launcher检查更新资源位数据接口
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<GetLatestResourcesResponse> getLatestResources(@Url String url, @Body GetLatestResourcesRequest request);

    /**
     * 批量获取终端的资源位数据接口
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<BatchGetResStrategyDataResponse> batchGetResStrategyData(@Url String url, @Body BatchGetResStrategyDataRequest request);

    /**
     * 订购接口
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<OrderProductResponse> orderProduct(@Url String url,@Body OrderProductRequest request);

    /**
     * 查询用户订购记录接口
     * @param url
     * @param request
     * @return
     */

    @POST
    Observable<QueryMultiqryResponse> queryMultiqry(@Url String url,@Body QueryMultiqryRequest request);

    /**
     * 精简的用户信息查询
     * 直接透传南向BOSS接口: BOSS. ESB_CS_QRY_MULTI_USERINFO_002，APIGW无任何逻辑，只做流控
     * @param url
     * @param request
     * @return
     */
    @POST
    Observable<QueryMultiUserInfoResponse> queryMultiUserInfo(@Url String url, @Body QueryMultiUserInfoRequest request);

    /**
     * 取消在途订单
     * 该接口用来取消订单状态为已提交或支付中的订单
     * @param url interface URL
     * @param request request
     * @return Observable<CancelOrderResponse>
     */
    @POST
    Observable<CancelOrderResponse> vssCancelOrder(@Url String url, @Body CancelOrderRequest request);

    /**
     * 查询订单信息
     * 根据用户业务账号、内容Code（按次）、产品ID或订单号查询订单信息
     * @param url
     * @param request
     * @return
     */
    @POST
    Observable<QueryOrderInfoResponse> vssQueryOrderInfo(@Url String url, @Body QueryOrderInfoRequest request);

    /**
     * 产品订购
     * @param url
     * @return
     */
    @POST
    Observable<com.pukka.ydepg.common.http.vss.response.OrderProductResponse> vssOrderProduct(@Url String url
            , @Body com.pukka.ydepg.common.http.vss.request.OrderProductRequest request);

    /**
     * 演员详情接口
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<GetCastDetailResponse> GetCastDetail(@Url String url,@Body GetCastDetailRequest request);

    /**
     * 查询固移融合关联内容接口
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<GetRelatedContentResponse> GetRelatedContent(@Url String url,@Body GetRelatedContentRequest request);

    /**
     * 获取栏目下频道列表
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryChannelStcPropsBySubjectResponse> queryChannelStcPropsBySubject(@Url String url,@Body QueryChannelStcPropsBySubjectRequest request);

    /**
     * 获取平台广告token
     * @param url
     * @param request
     * @return
     */

    @POST()
    Observable<QueryAdvertTokenResponse> queryAdvertToken(@Url String url,@Body QueryAdvertTokenRequest request);

    /**
     * 获取平台广告内容
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryAdvertContentResponse> queryAdvertContent(@Url String url,@Body QueryAdvertContentRequest request);


    /**
     * 广告平台上报
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<AdvertReportResponse> advertReport(@Url String url,@Body AdvertReportRequest request);

    /**
     *
     *
     */

    @GET()
    Observable<QueryRecommendResponse> queryRecommend(@Url String url);

    @POST()
    Observable<LoginPHMRouteResponse> loginPHMRoute(@Url String url);

    /**
     *VRS的超级接口generalBossQuery，接口调用BOSS的余额查询接口（ESB_CS_QRY_RESULT_BAL_001）查询手机话费实际可用余额（BALANCE）
     * 接口调用BOSS的积分查询接口（ESB_CS_SCORE_SCORE_QRY_001）查询手机积分实际可用余额
     */
    @POST
    Observable<QueryResultBalResponse> queryResultBal(@Url String url, @Body QueryResultRequest request);

    @POST
    Observable<BatchQueryChannelListBySubjectResponse> queryChannelListBySubjectIDs(@Url String url, @Body BatchQueryChannelListBySubjectRequest request);

    //VRS发送验证码接口
    @POST
    Observable<VerifiedCodeResponse> sendVerifiedCode(@Url String url, @Body SendVerifiedCodeRequest request);

    //VRS发送验证码接口
    @POST
    Observable<BatchSendSmsResponse> sendBatchSendSms(@Url String url, @Body BatchSendSmsRequest request);

    //VRS验证验证码接口
    @POST
    Observable<VerifiedCodeResponse> checkVerifiedCode(@Url String url, @Body CheckVerifiedCodeRequest request);

    //VRS验证验证码接口
    @POST
    Observable<VerifiedClientCodeResponse> checkClientVerifiedCode(@Url String url, @Body CheckClientVerifiedCodeRequest request);

    //VRS积分换算接口
    @POST
    Observable<ConvertFeeToScoreResponse> convertFeeToScore(@Url String url, @Body ConvertFeeToScoreRequest request);

    //获取产品包互斥关系接口
    @POST
    Observable<GetProductMutExRelaResponse> getProductMutExRela(@Url String url, @Body GetProductMutExRelaRequest relaRequest);

    //VRS查询童锁状态
    @POST
    Observable<QueryUserOrderingSwitchResponse> queryUserOrderingSwitch(@Url String url, @Body QueryUserOrderingSwitchRequest relaRequest);

    //VRS变更童锁状态
    @POST
    Observable<BaseResponse> changeUserOrderingSwitch(@Url String url, @Body ChangeUserOrderingSwitchRequest relaRequest);

    //查询VOD信息（不包含子集和推荐信息）
    @POST
    Observable<QueryVODResponse> queryVOD(@Url String url, @Body QueryVODRequest request);

    //查询连续剧简要子集信息
    @POST
    Observable<QueryEpisodeBriefInfoResponse>queryEpisodeBriefInfo(@Url String url,@Body QueryEpisodeBriedInfoRequest request);

    //查询VOD子集列表
    @POST
    Observable<QueryEpisodeListResponse>queryEpisodeList(@Url String url,@Body QueryEpisodeListReuqest reuqest);

    //获取自选包订购关系自选内容剩余数和自选内容状态
    @POST
    Observable<GetAlacarteChoosedContentsResponse>getAlacarteChoosedContents(@Url String url,@Body GetAlacarteChoosedContentsRequest request);

    //添加自选包订购关系自选内容和扣减内容余额
    @POST
    Observable<AddAlacarteChoosedContentResponse>addAlacarteChoosedContent(@Url String url,@Body AddAlacarteChoosedContentRequest request);

    //VRS查询订购关系
    @POST
    Observable<VRSQuerySubscriptionResponse>vrsQuerySubscription(@Url String url, @Body VRSQuerySubscriptionRequest request);

    //批量鉴权接口
    @POST
    Observable<PlayMultiMediaVODResponse>PlayMultiMediaVOD(@Url String url, @Body PlayMultiMediaVODRequest request);

    //查询和彩云图片
    @POST
    Observable<QueryResponse>queryHcyPic(@Url String url, @Body QueryRequest request);
}