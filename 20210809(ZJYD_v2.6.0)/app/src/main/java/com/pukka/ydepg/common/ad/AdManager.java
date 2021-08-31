package com.pukka.ydepg.common.ad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.App;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.DistributionChannel;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.QueryAdvertContentRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.AdvertReportRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.GetVODDetailRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.GetVODDetailResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryAdvertContentResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.MessageWebViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class AdManager {

    private final static AdManager instance = new AdManager();

    //视频广告的ID,用于用于[广告事件上报]上报对应的广告ID
    private String adID;

    private boolean playComplete = false;

    //private IADListener listener;

    private final Map<String,IADListener> mapListener = new HashMap<>();

    //private List<AdvertContent> listAdvertContent;

    private final Map<String,List<AdvertContent>> mapClassify2ListAdvertContent = new HashMap<>();

    private AdManager() {}

    //查询VOD详情,用于请求视频广告时根据SSP平台返回的VOD外部code查询到对应的媒资ID(mediaID)
    private void getMediaID(String adClassify,String code,IADListener listener) {
        GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
        getVODDetailRequest.setVODID(code);
        getVODDetailRequest.setIDType("1"); //vodID是外部code
        RxCallBack<GetVODDetailResponse> rxCallBack = new RxCallBack<GetVODDetailResponse>(HttpConstant.GETVODDETAIL, OTTApplication.getContext()) {
            @Override
            public void onSuccess(GetVODDetailResponse getVODDetailResponse) {
                try {
                    String mediaID = getVODDetailResponse.getVODDetail().getMediaFiles().get(0).getID();
                    String id = getVODDetailResponse.getVODDetail().getID();
                    SuperLog.debug(AdConstant.TAG, "[GetVODDetail]Get mediaID of Video AD successfully. MediaID=" + mediaID + " ID=" + id);
                    getVideoAdUrl(adClassify,id, mediaID,listener);
                } catch (Exception e) {
                    SuperLog.error(AdConstant.TAG, e);
                    SuperLog.error(AdConstant.TAG, "[GetVODDetail]Get mediaID of Video AD exception.");
                    listener.onFail();
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                SuperLog.error(AdConstant.TAG, e);
                SuperLog.error(AdConstant.TAG, "[GetVODDetail]Get mediaID of Video AD exception.");
                listener.onFail();
            }
        };
        HttpApi.getInstance().getService().getVODDetail(HttpUtil.getVspUrl(HttpConstant.GETVODDETAIL), getVODDetailRequest)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//最后回调主线程
                .subscribe(rxCallBack);
    }

    //获取视频广告的播放URL(鉴权)
    private void getVideoAdUrl(String adClassify,String id, String mediaID,IADListener listener) {
        PlayVODRequest playVODRequest = new PlayVODRequest();
        playVODRequest.setVODID(id);
        playVODRequest.setMediaID(mediaID);
        playVODRequest.setURLFormat("1");

        HttpApi.getInstance().getService().playVOD(HttpUtil.getVspUrl(HttpConstant.PLAYVOD), playVODRequest)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//最后回调主线程
                .subscribe(new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, OTTApplication.getContext()) {
                    @Override
                    public void onSuccess(PlayVODResponse playVODResponse) {
                        SuperLog.debug(AdConstant.TAG, "[PlayVOD]Get video AD play url successfully.");
                        String playUrl = playVODResponse.getPlayURL();
                        if (TextUtils.isEmpty(playUrl)) {
                            SuperLog.error(AdConstant.TAG, "[PlayVOD]Get video AD play url failed.");
                            listener.onFail();
                        } else {
                            SuperLog.debug(AdConstant.TAG, "Video AD play url = " + playUrl);
                            List<AdvertContent> listAdvertContent = mapClassify2ListAdvertContent.get(adClassify);
                            listAdvertContent.get(0).getVideo().setCurl(playUrl);
                            //广告曝光上报
                            AdManager.getInstance().reportAdvert(listAdvertContent.get(0), AdConstant.AdType.VIDEO,AdConstant.ReportActionType.IMPRESSION);
                            listener.onSuccess(listAdvertContent);
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(AdConstant.TAG, e);
                        SuperLog.error(AdConstant.TAG, "[PlayVOD]Get video AD play url exception.");
                        listener.onFail();
                    }
                });
    }

    //随机播放开机广告(图片/视频)
    private List<AdvertContent> getRandomAdContent(List<AdvertContent> listAllAdvertContent) {
        List<AdvertContent> listBannerContent = new ArrayList<>();
        List<AdvertContent> listVideoContent = new ArrayList<>();
        for (AdvertContent advertContent : listAllAdvertContent) {
            if (AdvertContent.AdvertType.DISPLAY.equals(advertContent.getAdType())) {
                listBannerContent.add(advertContent);
            } else if (AdvertContent.AdvertType.VIDEO.equals(advertContent.getAdType())) {
                listVideoContent.add(advertContent);
            }
        }

        if (CollectionUtil.isEmpty(listBannerContent) && !CollectionUtil.isEmpty(listVideoContent)) {
            SuperLog.info2SD(AdConstant.TAG, "Confirm play video ad");
            return listVideoContent;
        } else if (!CollectionUtil.isEmpty(listBannerContent) && CollectionUtil.isEmpty(listVideoContent)) {
            SuperLog.info2SD(AdConstant.TAG, "Confirm play banner ad");
            return listBannerContent;
        } else if (!CollectionUtil.isEmpty(listBannerContent) && !CollectionUtil.isEmpty(listVideoContent)) {
            //随机播放
            if (AdConstant.AdType.VIDEO.equals(AdUtil.showBannerOrVideo())) {
                SuperLog.info2SD(AdConstant.TAG, "Random play video ad");
                return listVideoContent;
            } else {
                SuperLog.info2SD(AdConstant.TAG, "Random Play banner ad");
                return listBannerContent;
            }
        } else {
            return null;
        }
    }

    private QueryAdvertContentRequest createAdvertRequest(String adClassify, Object object) {
        QueryAdvertContentRequest request = new QueryAdvertContentRequest();

        request.setReqId(AdUtil.getRequestID());
        request.setTenantId(AdConstant.AdvertInfo.TENANTID);

        List<String> placementCodes = new ArrayList<>();
        DistributionChannel channel = AdUtil.getChannel();
        switch (adClassify) {
            case AdConstant.AdClassify.START:
                //开机广告channel只有基础信息,没有额外信息
                placementCodes.add(AdConstant.AdvertInfo.START_BANNER_CODE);
                placementCodes.add(AdConstant.AdvertInfo.START_VIDEO_CODE);
                break;
            case AdConstant.AdClassify.VIDEO:
            case AdConstant.AdClassify.CORNER:
            case AdConstant.AdClassify.DETAIL:
                Content content = new Content();
                try {
                    VOD vod = (VOD) object;
                    if(AdConstant.AdClassify.VIDEO.equalsIgnoreCase(adClassify)){
                        placementCodes.add(AdConstant.AdvertInfo.VIDEO_VIDEO_CODE);
                    } else if(AdConstant.AdClassify.CORNER.equalsIgnoreCase(adClassify)){
                        placementCodes.add(AdConstant.AdvertInfo.PLAY_CORNER_CODE_1);
                    } else if(AdConstant.AdClassify.DETAIL.equalsIgnoreCase(adClassify)){
                        placementCodes.add(AdConstant.AdvertInfo.DETAIL_BANNER_CODE_1);
                    }

                    channel.setApp(new App(vod.getCmsType()));
                    content.setId(vod.getCode());
                    content.setTitle(vod.getName());
                    if(!CollectionUtil.isEmpty(vod.getMediaFiles())
                            && vod.getMediaFiles().get(0)!=null
                            && !TextUtils.isEmpty(vod.getMediaFiles().get(0).getElapseTime())){
                        content.setLen(Integer.parseInt(vod.getMediaFiles().get(0).getElapseTime()));
                    }
                    if(!CollectionUtil.isEmpty(vod.getGenres())
                            && vod.getGenres().get(0)!=null
                            && !TextUtils.isEmpty(vod.getGenres().get(0).getGenreName())){
                        content.setGenre(vod.getGenres().get(0).getGenreName());
                    }
                } catch (Exception e) {
                    SuperLog.error(AdConstant.TAG, e);
                } finally {
                    channel.setContent(content);
                }
                break;
            case AdConstant.AdClassify.SCREEN:
                //屏保广告channel只有基础信息,没有额外信息
                placementCodes.add(AdConstant.AdvertInfo.SCREEN_BANNER_CODE_1);
                placementCodes.add(AdConstant.AdvertInfo.SCREEN_BANNER_CODE_2);
                placementCodes.add(AdConstant.AdvertInfo.SCREEN_BANNER_CODE_3);
                placementCodes.add(AdConstant.AdvertInfo.SCREEN_BANNER_CODE_4);
                placementCodes.add(AdConstant.AdvertInfo.SCREEN_BANNER_CODE_5);
                break;
            default:
                SuperLog.error(AdConstant.TAG, "Unknown adClassify in createAdvertRequest.");
                return request;
        }

        request.setPlacementCodes(placementCodes);
        request.setChannel(channel);
        request.setUser(AdUtil.getUser());
        request.setDevice(AdUtil.getDevice());
        request.setTimestamp(System.currentTimeMillis());

        return request;
    }

    private void handleQueryAdvertResponse(String adClassify, QueryAdvertContentResponse response, String requestID) {
        SuperLog.info2SD(AdConstant.TAG, "query AdvertContent success, response is : \n\t" + JsonParse.object2String(response));
        mapClassify2ListAdvertContent.put(adClassify,response.getAdvertContents());
        List<AdvertContent> listAdvertContent = mapClassify2ListAdvertContent.get(adClassify);
        if (CollectionUtil.isEmpty(listAdvertContent)) {
            SuperLog.error(AdConstant.TAG, "Get AdvertContent response failed, skip to load ssp AD, AdType="+adClassify);
            mapListener.get(adClassify).onFail();
            return;
        }

        //将请求ID保存在广告数据中,用于上报话单
        for (AdvertContent content : listAdvertContent) {
            content.setRelationID(requestID);
        }

        switch (adClassify) {
            case AdConstant.AdClassify.START:
                //决定随机播放内容
                listAdvertContent = getRandomAdContent(listAdvertContent);
                mapClassify2ListAdvertContent.put(adClassify,listAdvertContent);
                if (CollectionUtil.isEmpty(listAdvertContent)) {
                    SuperLog.error(AdConstant.TAG, "Get START AdvertContent failed, skip to load startup AD");
                    mapListener.get(adClassify).onFail();
                } else {
                    SuperLog.info2SD(AdConstant.TAG, "Parse SSP AD response finished. AD number = " + listAdvertContent.size());
                    if (null != listAdvertContent.get(0).getVideo()) {
                        //开机视频广告 继续请求视频播放URL
                        getMediaID(adClassify,listAdvertContent.get(0).getVideo().getCurl(),mapListener.get(adClassify));
                    } else {
                        //开机图片广告 直接回调应用侧
                        mapListener.get(adClassify).onSuccess(listAdvertContent);
                    }
                }
                break;
            case AdConstant.AdClassify.VIDEO:
                //贴片视频广告
                SuperLog.info2SD(AdConstant.TAG, "Get Screen advert successfully. AD number = " + listAdvertContent.size());
                getMediaID(adClassify,listAdvertContent.get(0).getVideo().getCurl(),mapListener.get(adClassify));
                break;
            case AdConstant.AdClassify.SCREEN:
                //屏保图片广告
                SuperLog.info2SD(AdConstant.TAG, "Get Screen advert successfully.");
                mapListener.get(adClassify).onSuccess(response.getAdvertContents());
                break;
            case AdConstant.AdClassify.DETAIL:
                //VOD详情页广告
                SuperLog.info2SD(AdConstant.TAG, "Get Detail advert successfully.");
                mapListener.get(adClassify).onSuccess(response.getAdvertContents());
                break;
            case AdConstant.AdClassify.CORNER:
                //VOD详情页广告
                SuperLog.info2SD(AdConstant.TAG, "Get Corner advert successfully.");
                mapListener.get(adClassify).onSuccess(response.getAdvertContents());
                break;
            default:
                SuperLog.error(AdConstant.TAG, "Unknown adClassify in handleQueryAdvertResponse.");
                mapListener.get(adClassify).onFail();
        }
    }


    //==================== 公有方法区 ====================
    public static AdManager getInstance() {
        return instance;
    }

    public boolean isPlayComplete() {
        return playComplete;
    }

    public void setPlayComplete(boolean playComplete) {
        this.playComplete = playComplete;
    }

    //启动海报广告对应的广告内容页面
    public void startAdPage(Context context, AdvertContent content, String adType, String url) {
        Intent intent = new Intent(context, MessageWebViewActivity.class);
        intent.putExtra(MessageWebViewActivity.LINKURL, url);
        context.startActivity(intent);
        //上报海报广告点击
        AdManager.getInstance().reportAdvert(content, adType, AdConstant.ReportActionType.CLICK);
    }

    //Object参数对于贴片广告是VOD对象,对于屏保广告是List<String>对象(PlacementCode)
    @SuppressLint("CheckResult")
    public void queryAdvertContent(String adClassify, IADListener listener, Object object){
        if (listener == null) {
            return;
        }
        mapListener.put(adClassify,listener);
        //this.listener = mapListener.get(adClassify);

        //SSP广告总开关未打开或SSP子场景开关未打开
        if (!AdUtil.enableSsp(adClassify)) {
            //SSP能力不具备,详细原因在AdUtil.enableSsp中会打印,回调失败方法,结束流程
            listener.onFail();
            return;
        }

        if ((AdConstant.AdClassify.VIDEO.equals(adClassify) || AdConstant.AdClassify.DETAIL.equals(adClassify) || AdConstant.AdClassify.CORNER.equals(adClassify))
                && !(object instanceof VOD)) {
            //请求类型为[视频贴片广告/详情页BANNER广告/播放页角标广告]且【VOD对象为空】,回调失败方法,结束流程
            SuperLog.error(AdConstant.TAG, "Input param[VOD] is null for [VIDEO] AD, skip [VIDEO] AD.");
            listener.onFail();
            return;
        }

        String advertUrl = AdUtil.getSspAdvertUrl(AdConstant.QUERY);
        QueryAdvertContentRequest request = createAdvertRequest(adClassify, object);
        SuperLog.info2SD(AdConstant.TAG, "Advert[Query] request is : " + request.toString());
        HttpApi.getInstance().getService().queryAdvertContent(advertUrl, request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    handleQueryAdvertResponse(adClassify, response, request.getReqId());
                }, throwable -> {
                    SuperLog.error(AdConstant.TAG, "Get AdvertContent response exception, skip to load [" + adClassify +"] AD");
                    SuperLog.error(AdConstant.TAG, throwable);
                    listener.onFail();
                });
    }

    //广告曝光/点击上报,,在广告曝光或者点击时调用
    public void reportAdvert(AdvertContent content, String adType, String actionType) {
        if (AdConstant.AdType.VIDEO.equals(adType) && AdConstant.ReportActionType.IMPRESSION.equals(actionType)) {
            //当上报视频广告曝光时记录视频开始播放时间,用于计算视频广告实际播放时间
//            startTime = System.currentTimeMillis();
            adID = content.getAdId();
//            SuperLog.info2SD(AdConstant.TAG, "Advert impression time = [" + new Date(startTime) + "]\t adID=" + adID);
            SuperLog.info2SD(AdConstant.TAG, "Advert impression adID=" + adID);
        }
        AdvertReportRequest reportRequest = new AdvertReportRequest();
        reportRequest.setReqId(AdUtil.getRequestID());
        reportRequest.setTenantId(AdConstant.AdvertInfo.TENANTID);
        reportRequest.setUser(AdUtil.getUser());
        reportRequest.setAdId(content.getAdId());
        reportRequest.setActionType(actionType);
        reportRequest.setStartdate(System.currentTimeMillis());
        reportRequest.setAdType(adType);
        reportRequest.setRelationID(content.getRelationID());
        String sspReportUrl = AdUtil.getSspAdvertUrl(AdConstant.REPORT);
        SuperLog.info2SD(AdConstant.TAG, "Advert[Impression][Report to SSP] request is : " + reportRequest.toString());
        //上报SSP平台
        HttpApi.getInstance().getService().advertReport(sspReportUrl, reportRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        advertReportResponse -> SuperLog.info2SD(AdConstant.TAG, "Report advert[Impression] to SSP successfully, response is : \n\t" + JsonParse.object2String(advertReportResponse)),
                        throwable -> SuperLog.error(AdConstant.TAG, throwable));

        //上报第三方平台
        String thirdReportUrl = AdUtil.getThirdReportUrl(content, adType, actionType);
        if (null != thirdReportUrl) {
            thirdReportUrl = AdUtil.setReportInfo(thirdReportUrl);
            SuperLog.info2SD(AdConstant.TAG, "Advert[Impression][Report to 3rd] request is : " + thirdReportUrl);
            HttpApi.getInstance().getService().sendGetRequest(thirdReportUrl)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                            advertReportResponse -> SuperLog.info2SD(AdConstant.TAG, "Report advert[Impression] to 3rd successfully, response is : \n\t" + JsonParse.object2String(advertReportResponse)),
                            throwable -> SuperLog.error(AdConstant.TAG, throwable));
        }
    }

    //视频广告实际播放时间上报,在视频广告播放结束时调用
    public void reportAdvertDuration(String relationID, long adPlayTime) {
        if (/*startTime == NOT_STARTED || */ TextUtils.isEmpty(adID)) {
            //未记录过视频起播时间,说明没有播放视频,无需上报广告持续时间
            return;
        }
        AdvertReportRequest reportRequest = new AdvertReportRequest();
        reportRequest.setReqId(AdUtil.getRequestID());                    //生成全新
        reportRequest.setTenantId(AdConstant.AdvertInfo.TENANTID);        //浙江移动固定为8601
        reportRequest.setUser(AdUtil.getUser());                          //固定值
        reportRequest.setAdId(adID);                                      //上报事件对应的广告ID
        reportRequest.setActionType(AdConstant.ReportActionType.DURATION);//上报的事件类型:实际的播放时长
        long endTime = System.currentTimeMillis();
        reportRequest.setStartdate(endTime);           //当前时间毫秒数
//        reportRequest.setDuration(endTime - startTime);  //上报实际播放时长，单位为毫秒
        reportRequest.setDuration(adPlayTime);  //上报实际播放时长，单位为毫秒
        reportRequest.setAdType(AdConstant.AdType.VIDEO);                 //上报事件类型:广告实际播放时间
        reportRequest.setRelationID(relationID);                           //与请求广告时的requestID对应一致

        String sspReportUrl = AdUtil.getSspAdvertUrl(AdConstant.REPORT);
        SuperLog.info2SD(AdConstant.TAG, "Advert[Duration][Report to SSP] request is : " + reportRequest.toString());
        //上报SSP平台
        HttpApi.getInstance().getService().advertReport(sspReportUrl, reportRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        advertReportResponse -> SuperLog.info2SD(AdConstant.TAG, "Report advert[Duration] to SSP successfully, response is : \n\t" + JsonParse.object2String(advertReportResponse)),
                        throwable -> SuperLog.error(AdConstant.TAG, throwable));
    }
}