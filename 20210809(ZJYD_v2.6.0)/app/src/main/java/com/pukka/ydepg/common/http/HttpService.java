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
 * service??????????????????
 */
@RetrofitBuilder(httpAPiName = HttpApi.class)
public interface HttpService {

    /**
     * * 5.27	APP???????????????/???????????????????????????
     *
     * @param url
     * @param querySubscription
     * @return
     */
    @POST()
    Observable<QueryProductInfoResponse> queryProductInfo(@Url String url, @Body
        DSVQuerySubscription querySubscription);

    /**
     * ???????????????Java???
     *
     * @param url
     * @param loginRouteRequest
     * @return
     */
    @POST()
    Observable<LoginRouteResponse> loginRoute(@Url String url, @Body LoginRouteRequest loginRouteRequest);

    /**
     * ????????????
     *
     * @param url
     * @param
     * @return
     */
    @POST()
    Observable<AuthenticateResponse> authenticate(@Url String url, @Body AuthenticateRequest authenticateRequest);

    /**
     * ??????QueryCustomizeConfig???????????????????????????VSP??????????????????????????????Verimatrix????????????????????????????????????????????????????????????
     * @param url
     * @param queryCustomizeConfig
     * @return
     */
    @POST()
    Observable<QueryCustomizeConfigResponse> queryCustomizeConfig(@Url String url, @Body QueryCustomizeConfig queryCustomizeConfig);

    /**
     * ??????????????????
     *
     * @param url
     * @param switchProfileRequest
     * @return
     */
    @POST()
    Observable<SwitchProfileResponse> SwitchProfile(@Url String url, @Body SwitchProfileRequest switchProfileRequest);

    /**
     * ????????????
     *
     * @param url
     * @return
     */
    @POST()
    Observable<OnLineHeartbeatResponse> onLineHeartbeat(@Url String url);

    /**
     * ????????????
     *
     * @param url
     * @param logoutRequest
     * @return
     */
    @POST()
    Observable<LogoutResponse> logout(@Url String url, @Body LogoutRequest logoutRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param queryDeviceListRequest
     * @return
     */
    @POST()
    Observable<QueryDeviceListResponse> queryDeviceList(@Url String url, @Body QueryDeviceListRequest queryDeviceListRequest);

    /**
     * ????????????
     *
     * @param url
     * @param replaceDeviceRequest
     * @return
     */
    @POST()
    Observable<ReplaceDeviceResponse> replaceDevice(@Url String url, @Body ReplaceDeviceRequest replaceDeviceRequest);

    /**
     * ????????????????????????
     *
     * @param url
     * @param queryChannelSubjectListRequest
     * @return
     */
    @POST()
    Observable<QueryChannelSubjectListResponse> queryChannelSubjectList(@Url String url, @Body QueryChannelSubjectListRequest queryChannelSubjectListRequest);

    /**
     * ???????????????????????????
     *
     * @param url
     * @param queryChannelListBySubjectRequest
     * @return
     */
    @POST()
    Observable<QueryChannelListBySubjectResponse> queryChannelListBySubject(@Url String url, @Body QueryChannelListBySubjectRequest queryChannelListBySubjectRequest);

    /**
     * ??????????????????????????????
     *
     * @param url
     * @param queryPlaybillListRequest
     * @return
     */
    @POST()
    Observable<QueryPlaybillListResponse> queryPlaybillList(@Url String url, @Body QueryPlaybillListRequest queryPlaybillListRequest);

    /**
     * ??????????????????????????????Java???
     *
     * @param url
     * @param queryPlaybillContextRequest
     * @return
     */
    @POST()
    Observable<QueryPlaybillContextResponse> queryPlaybillContext(@Url String url, @Body QueryPlaybillContextRequest queryPlaybillContextRequest);

    /**
     * ???????????????????????????????????????????????????
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryPlaybillListResponse> queryPlaybillListByChannel(@Url String url, @Body QueryPlaybillListByChannelRequest request);

    /**
     * ??????????????????
     *
     * @param url
     * @param playChannelRequest
     * @return
     */
    @POST()
    Observable<PlayChannelResponse> playChannel(@Url String url, @Body PlayChannelRequest playChannelRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param reportChannelRequest
     * @return
     */
    @POST()
    Observable<ReportChannelResponse> reportChannel(@Url String url, @Body ReportChannelRequest reportChannelRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param playChannelHeartbeatRequest
     * @return
     */
    @POST()
    Observable<PlayChannelHeartbeatResponse> playChannelHeartbeat(@Url String url, @Body PlayChannelHeartbeatRequest playChannelHeartbeatRequest);

    /**
     * ??????VOD????????????
     *
     * @param url
     * @param queryVODSubjectListRequest
     * @return
     */
    @POST()
    Observable<QueryVODSubjectListResponse> queryVODSubjectList(@Url String url, @Body QueryVODSubjectListRequest queryVODSubjectListRequest);

    /**
     * ???????????????VOD??????
     *
     * @param url
     * @param queryVODListBySubjectRequest
     * @return
     */
    @POST()
    Observable<QueryVODListBySubjectResponse> queryVODListBySubject(@Url String url, @Body QueryVODListBySubjectRequest queryVODListBySubjectRequest);

    /**
     * ???????????????????????????????????????VOD??????
     *
     * @param url
     * @param querySubjectVODBySubjectIDRequest
     * @return
     */
    @POST()
    Observable<QuerySubjectVODBySubjectIDResponse> querySubjectVODBySubjectID(@Url String url, @Body QuerySubjectVODBySubjectIDRequest querySubjectVODBySubjectIDRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param getContentConfigRequest
     * @return
     */
    @POST()
    Observable<GetContentConfigResponse> getContentConfig(@Url String url, @Body GetContentConfigRequest getContentConfigRequest);

    /**
     * ??????VOD??????
     *
     * @param url
     * @param getVODDetailRequest
     * @return
     */
    @POST()
    Observable<GetVODDetailResponse> getVODDetail(@Url String url, @Body GetVODDetailRequest getVODDetailRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param playVODRequest
     * @return
     */
    @POST()
    Observable<PlayVODResponse> playVOD(@Url String url, @Body PlayVODRequest playVODRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param downloadVODRequest
     * @return
     */
    @POST()
    Observable<DownloadVODResponse> downloadVOD(@Url String url, @Body DownloadVODRequest downloadVODRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param reportVODRequest
     * @return
     */
    @POST()
    Observable<ReportVODResponse> reportVOD(@Url String url, @Body ReportVODRequest reportVODRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param playVODHeartbeatRequest
     * @return
     */
    @POST()
    Observable<PlayVODHeartbeatResponse> playVODHeartbeat(@Url String url, @Body PlayVODHeartbeatRequest playVODHeartbeatRequest);

    /**
     * ????????????
     *
     * @param url
     * @param searchHotKeyRequest
     * @return
     */
    @POST()
    Observable<SearchHotKeyResponse> searchHotKey(@Url String url, @Body SearchHotKeyRequest searchHotKeyRequest);

    /**
     * ??????????????????
     *
     * @param url
     * @param searchContentsListRequest
     * @return
     */
    @POST()
    Observable<SearchContentsListResponse> qeryHotSearchContentsList(@Url String url, @Body SearchContentsListRequest searchContentsListRequest);

    /**
     * ????????????
     *
     * @param url
     * @param searchContentRequest
     * @return
     */
    @POST()
    Observable<SearchContentResponse> searchContent(@Url String url, @Body SearchContentRequest searchContentRequest);

    /**
     * ?????????????????????
     *
     * @param url
     * @param suggestKeywordRequest
     * @return
     */
    @POST()
    Observable<SuggestKeywordResponse> suggestKeyword(@Url String url, @Body SuggestKeywordRequest suggestKeywordRequest);

    /**
     * ????????????
     *
     * @param url
     * @param queryRecmContentRequest
     * @return
     */
    @POST()
    Observable<QueryRecmContentResponse> queryRecmContent(@Url String url, @Body QueryRecmContentRequest queryRecmContentRequest);

    /**
     * ????????????
     *
     * @param url
     * @param createFavoriteRequest
     * @return
     */
    @POST()
    Observable<CreateFavoriteResponse> createFavorite(@Url String url, @Body CreateFavoriteRequest createFavoriteRequest);

    /**
     * ????????????
     *
     * @param url
     * @param deleteFavoriteRequest
     * @return
     */
    @POST()
    Observable<DeleteFavoriteResponse> deleteFavorite(@Url String url, @Body DeleteFavoriteRequest deleteFavoriteRequest);

    /**
     * ????????????
     *
     * @param url
     * @param queryFavoriteRequest
     * @return
     */
    @POST()
    Observable<QueryFavoriteResponse> queryFavorite(@Url String url, @Body QueryFavoriteRequest queryFavoriteRequest);

    /**
     * ???????????????
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<AddFavoCatalogResponse> addFavoCatalog(@Url String url, @Body AddFavoCatalogRequest request);

    /**
     * ???????????????
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<DeleteFavoCatalogResponse> deleteFavoCatalog(@Url String url, @Body DeleteFavoCatalogRequest request);

    /**
     * ???????????????
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryFavoCatalogResponse> queryFavoCatalog(@Url String url, @Body QueryFavoCatalogRequest request);

    /**
     * ????????????
     *
     * @param url
     * @param createBookmarkRequest
     * @return
     */
    @POST()
    Observable<CreateBookmarkResponse> createBookmark(@Url String url, @Body CreateBookmarkRequest createBookmarkRequest);

    /**
     * ????????????
     *
     * @param url
     * @param deleteBookmarkRequest
     * @return
     */
    @POST()
    Observable<DeleteBookmarkResponse> deleteBookmark(@Url String url, @Body DeleteBookmarkRequest deleteBookmarkRequest);

    /**
     * ????????????
     *
     * @param url
     * @param queryBookmarkRequest
     * @return
     */
    @POST()
    Observable<QueryBookmarkResponse> queryBookmark(@Url String url, @Body QueryBookmarkRequest queryBookmarkRequest);

    /**
     * ????????????
     *
     * @param url
     * @param createContentScoreRequest
     * @return
     */
    @POST()
    Observable<CreateContentScoreResponse> createContentScore(@Url String url, @Body CreateContentScoreRequest createContentScoreRequest);

    /**
     * ????????????
     *
     * @param url
     * @param queryLauncherRequest
     * @return
     */
    @POST()
    Observable<QueryLauncherResponse> queryLauncher(@Url String url, @Body QueryLauncherRequest queryLauncherRequest);

    /**
     *??????????????????
     *
     * @param url
     * @param queryAllHomeDataRequest
     * @return
     */
    @POST()
    Observable<QueryAllHomeDataResponse> queryAllHomeData(@Url String url, @Body QueryAllHomeDataRequest queryAllHomeDataRequest);


    /**
     *??????????????????????????????
     * @param url
     * @param setGlobalFilterCondRequest
     * @return
     */
    @POST()
    Observable<SetGlobalFilterCondResponse> setGlobalFilterCond(@Url String url, @Body SetGlobalFilterCondRequest setGlobalFilterCondRequest);


    /**
     *VOD?????????
     * @param url
     * @param queryRecmVODListRequest
     * @return
     */
    @POST()
    Observable<QueryRecmVODListResponse> QueryRecmVODList(@Url String url, @Body QueryRecmVODListRequest queryRecmVODListRequest);

    /**
     *??????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryOTTLiveTVHomeDataResponse> queryOTTLiveTVHomeData(@Url String url, @Body QueryOTTLiveTVHomeDataRequest request);

    /*
    * ???????????????????????????
    *
    *
    */
    @POST()
    Observable<ReportQrCodeStateResponse> reportQrCodeState(@Url String url, @Body ReportQrCodeStateRequest request);

    /**
     * ??????????????????
     * @param url
     * @param info
     * @return
     */
    @POST()
    Observable<BaseResponse> modifyDeviceInfo(@Url String url,@Body DeviceInfo info);
    /**
     *VSP??????????????????????????????????????????????????????????????????????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryLocationResponse> queryLocation(@Url String url, @Body
        QueryLocationRequest request);

    /**
     *????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryCustomizeConfigResponse> queryCustomizeConfig(@Url String url, @Body
        QueryCustomizeConfigRequest request);

    /**
     * ??????
     * @param url
     * @param subscribeRequest
     * @return
     */
    @POST()
    Observable<SubscribeProductResponse> subscribe(@Url String url,@Body SubscribeProductRequest subscribeRequest);
    /**
     * ??????
     * @param url
     * @param unsubRequest
     * @return
     */
    @POST()
    Observable<SubscribeDeleteResponse> unsubscribe(@Url String url,@Body CancelSubscribeRequest unsubRequest);
    /**
     * ??????Profile
     */
    @POST()
    Observable<QueryProfileResponse> queryProfileInfo(@Url String url,@Body QueryProfileRequest request);
    /**
     * ??????,??????profile
     */
    @POST()
    Observable<AddProfileResponse> modifyProfile(@Url String url,@Body ModifyProfileRequest request);
    /**
     * ??????profile
     */
    @POST()
    Observable<BaseResponse> removeProfile(@Url String url,@Body DeleteProfileRequest request);

    /**
     * ?????????????????????????????????Java???
     */
    @POST()
    Observable<QueryAllChannelResponse> queryAllChannel(@Url String url, @Body QueryAllChannelRequest request);

    /**
     * ?????????????????????????????????Java???
     */
    @POST()
    Observable<QueryAllChannelDynamicPropertiesResponse> queryAllChannelDynamicProperties(@Url String url, @Body QueryAllChannelDynamicPropertiesRequest request);

    /**
     * ???????????????
     */
    @POST()
    Observable<QueryLockResponse> queryLock(@Url String url,@Body QueryLockRequest request);

    /**
     * ???????????????
     * @param url
     * @return
     */
    @POST()
    Observable<DeleteLockResponse> deleteLock(@Url String url,@Body DeleteLockRequest request);

    /**
     * ?????????
     * @param url
     * @return
     */
    @POST()
    Observable<CreateLockResponse> createLock(@Url String url, @Body CreateLockRequest request);

    /**
     * ??????????????????
     * @param url
     * @param request
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> upgrade(@Url String url,@FieldMap Map<String,String> request);


    /**
     * ?????????HTTP GET??????
     * @param url ??????URL
     * @return
     */
    @GET()
    Observable<ResponseBody> sendGetRequest(@Url String url);

    /**
     * ??????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QuerySubjectDetailResponse> QuerySubjectDetail(@Url String url, @Body QuerySubjectDetailRequest request);


    /**
     *
     ???????????????????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QrCodeAuthenticateResponse> qrCodeAuthenticate(@Url String url, @Body QrCodeAuthenticateRequest request);

    /**
     *
     ???????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryQrCodeStatusResponse> queryQrCodeAuthenticateStatus(@Url String url, @Body QueryQrCodeStatusRequest request);



    /**
     *
     *????????????????????????(??????/????????????)
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryBindedSubscriberResponse> qeryBindedSubscriber(@Url String url, @Body QueryBindedSubscriberRequest request);

    /**
     *??????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<UnBindSubsrciberResponse> unBindSubsrciber(@Url String url, @Body UnBindSubsrciberRequest request);


    /**
     *??????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QuitQrCodeAuthenticateResponse> quitQrCodeAuthentiCate(@Url String url, @Body QuitQrCodeAuthenticateRequest request);


    /**
     * ????????????VOD????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryEpgHomeVodResponse> queryEpgHomeVod(@Url String url, @Body QueryEpgHomeVodRequest request);

    /**
     * ????????????VOD????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryBatchVODListBySubjectResponse> queryBatchVODListBySubject(@Url String url, @Body QueryBatchVODListBySubjectRequest request);


    /**
     *??????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<SetQrCodeSubscriberResponse> setQrCodeAuthenticatedSubscriber(@Url String url, @Body SetQrCodeSubscriberRequest request);

    /**
     * ?????????????????????
     * @param url
     * @param request
     */
    @POST()
    Observable<BaseResponse> sendSMS(@Url String url,@Body SendSmsRequest request);

    /**
     * ????????????????????????
     * @param url
     * @param request
     */
    @POST()
    Observable<QueryProductInfoResponse> querySubscription(@Url String url,@Body DSVQuerySubscription request);

    /**
     * ??????
     * @param url
     * @param request
     */
    @POST()
    Observable<SubmitDeviceResponse> submitDeviceInfo(@Url String url,@Body SubmitDeviceInfoRequest request);

    /**
     * ??????launcher
     */
    @GET()
    Observable<ResponseBody> downloadLauncher(@Url String url);

    /**
     * ?????????????????? c40 ??????
     * @param url url
     * @param request request
     */
    @POST()
    Observable<QuerySubscriberResponse> querySubscriberInfo(@Url String url,@Body QueryUniInfoRequest request);

    /**
     * ?????????????????? c40 ??????
     * @param url url
     * @param request request
     */
    @POST()
    Observable<QueryUserAttrsResponse> queryUserAttrs(@Url String url,@Body QueryUserAttrsRequest request);

    /**
     * ?????????????????? c40 ??????
     * @param url url
     * @param request request
     */
    @POST()
    Observable<ModifyUserAttrResponse> modifyUserAttr(@Url String url,@Body ModifyUserAttrRequest request);

    /**
     * ???????????????????????????????????????
     * ???????????????????????????????????????
     * @param url url
     * @param request request
     */
    @POST()
    Observable<QueryUniPayInfoResponse> queryUniPayInfo(@Url String url,@Body
        QueryUniInfoRequest request);

    /**
     * ??????Topic??????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<JsonObject> queryTopic(@Url String url, @Body
            TopicRequest request);

    /**
     *
     * ??????????????????
     * @param url url
     * @param request request
     */
    @POST()
    Observable<GetStartPictureResponse> getStartPicture(@Url String url, @Body
            GetStartPictureRequest request);

    /**
     *
     * ????????????????????????
     * @param url url
     * @param request request
     */
    @POST()
    Observable<QueryProductResponse> queryProduct(@Url String url, @Body
            QueryProductRequest request);


    /**
     *
     * ??????????????????
     * @param url url
     * @param request request
     */
    @POST()
    Observable<UpdateSubscriberResponse> updateSubscriber(@Url String url, @Body
            UpdateSubscriberRequest request);


    /**
     *
     * ???????????????
     * @param url url
     * @param request request
     */
    @POST()
    Observable<UpdateUserRegInfoResponse> updateUserRegInfo(@Url String url, @Body
            UpdateUserRegInfoRequest request);

    /**
     * Launcher?????????????????????????????????
     *
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<GetLatestResourcesResponse> getLatestResources(@Url String url, @Body GetLatestResourcesRequest request);

    /**
     * ??????????????????????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<BatchGetResStrategyDataResponse> batchGetResStrategyData(@Url String url, @Body BatchGetResStrategyDataRequest request);

    /**
     * ????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<OrderProductResponse> orderProduct(@Url String url,@Body OrderProductRequest request);

    /**
     * ??????????????????????????????
     * @param url
     * @param request
     * @return
     */

    @POST
    Observable<QueryMultiqryResponse> queryMultiqry(@Url String url,@Body QueryMultiqryRequest request);

    /**
     * ???????????????????????????
     * ??????????????????BOSS??????: BOSS. ESB_CS_QRY_MULTI_USERINFO_002???APIGW??????????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST
    Observable<QueryMultiUserInfoResponse> queryMultiUserInfo(@Url String url, @Body QueryMultiUserInfoRequest request);

    /**
     * ??????????????????
     * ??????????????????????????????????????????????????????????????????
     * @param url interface URL
     * @param request request
     * @return Observable<CancelOrderResponse>
     */
    @POST
    Observable<CancelOrderResponse> vssCancelOrder(@Url String url, @Body CancelOrderRequest request);

    /**
     * ??????????????????
     * ?????????????????????????????????Code?????????????????????ID??????????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST
    Observable<QueryOrderInfoResponse> vssQueryOrderInfo(@Url String url, @Body QueryOrderInfoRequest request);

    /**
     * ????????????
     * @param url
     * @return
     */
    @POST
    Observable<com.pukka.ydepg.common.http.vss.response.OrderProductResponse> vssOrderProduct(@Url String url
            , @Body com.pukka.ydepg.common.http.vss.request.OrderProductRequest request);

    /**
     * ??????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<GetCastDetailResponse> GetCastDetail(@Url String url,@Body GetCastDetailRequest request);

    /**
     * ????????????????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<GetRelatedContentResponse> GetRelatedContent(@Url String url,@Body GetRelatedContentRequest request);

    /**
     * ???????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryChannelStcPropsBySubjectResponse> queryChannelStcPropsBySubject(@Url String url,@Body QueryChannelStcPropsBySubjectRequest request);

    /**
     * ??????????????????token
     * @param url
     * @param request
     * @return
     */

    @POST()
    Observable<QueryAdvertTokenResponse> queryAdvertToken(@Url String url,@Body QueryAdvertTokenRequest request);

    /**
     * ????????????????????????
     * @param url
     * @param request
     * @return
     */
    @POST()
    Observable<QueryAdvertContentResponse> queryAdvertContent(@Url String url,@Body QueryAdvertContentRequest request);


    /**
     * ??????????????????
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
     *VRS???????????????generalBossQuery???????????????BOSS????????????????????????ESB_CS_QRY_RESULT_BAL_001??????????????????????????????????????????BALANCE???
     * ????????????BOSS????????????????????????ESB_CS_SCORE_SCORE_QRY_001???????????????????????????????????????
     */
    @POST
    Observable<QueryResultBalResponse> queryResultBal(@Url String url, @Body QueryResultRequest request);

    @POST
    Observable<BatchQueryChannelListBySubjectResponse> queryChannelListBySubjectIDs(@Url String url, @Body BatchQueryChannelListBySubjectRequest request);

    //VRS?????????????????????
    @POST
    Observable<VerifiedCodeResponse> sendVerifiedCode(@Url String url, @Body SendVerifiedCodeRequest request);

    //VRS?????????????????????
    @POST
    Observable<BatchSendSmsResponse> sendBatchSendSms(@Url String url, @Body BatchSendSmsRequest request);

    //VRS?????????????????????
    @POST
    Observable<VerifiedCodeResponse> checkVerifiedCode(@Url String url, @Body CheckVerifiedCodeRequest request);

    //VRS?????????????????????
    @POST
    Observable<VerifiedClientCodeResponse> checkClientVerifiedCode(@Url String url, @Body CheckClientVerifiedCodeRequest request);

    //VRS??????????????????
    @POST
    Observable<ConvertFeeToScoreResponse> convertFeeToScore(@Url String url, @Body ConvertFeeToScoreRequest request);

    //?????????????????????????????????
    @POST
    Observable<GetProductMutExRelaResponse> getProductMutExRela(@Url String url, @Body GetProductMutExRelaRequest relaRequest);

    //VRS??????????????????
    @POST
    Observable<QueryUserOrderingSwitchResponse> queryUserOrderingSwitch(@Url String url, @Body QueryUserOrderingSwitchRequest relaRequest);

    //VRS??????????????????
    @POST
    Observable<BaseResponse> changeUserOrderingSwitch(@Url String url, @Body ChangeUserOrderingSwitchRequest relaRequest);

    //??????VOD??????????????????????????????????????????
    @POST
    Observable<QueryVODResponse> queryVOD(@Url String url, @Body QueryVODRequest request);

    //?????????????????????????????????
    @POST
    Observable<QueryEpisodeBriefInfoResponse>queryEpisodeBriefInfo(@Url String url,@Body QueryEpisodeBriedInfoRequest request);

    //??????VOD????????????
    @POST
    Observable<QueryEpisodeListResponse>queryEpisodeList(@Url String url,@Body QueryEpisodeListReuqest reuqest);

    //?????????????????????????????????????????????????????????????????????
    @POST
    Observable<GetAlacarteChoosedContentsResponse>getAlacarteChoosedContents(@Url String url,@Body GetAlacarteChoosedContentsRequest request);

    //????????????????????????????????????????????????????????????
    @POST
    Observable<AddAlacarteChoosedContentResponse>addAlacarteChoosedContent(@Url String url,@Body AddAlacarteChoosedContentRequest request);

    //VRS??????????????????
    @POST
    Observable<VRSQuerySubscriptionResponse>vrsQuerySubscription(@Url String url, @Body VRSQuerySubscriptionRequest request);

    //??????????????????
    @POST
    Observable<PlayMultiMediaVODResponse>PlayMultiMediaVOD(@Url String url, @Body PlayMultiMediaVODRequest request);

    //?????????????????????
    @POST
    Observable<QueryResponse>queryHcyPic(@Url String url, @Body QueryRequest request);
}