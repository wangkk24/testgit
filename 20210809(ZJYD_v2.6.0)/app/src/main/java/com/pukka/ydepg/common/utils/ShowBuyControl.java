package com.pukka.ydepg.common.utils;

import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.HttpUtil;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.bean.response.QueryProductInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayMultiMediaVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryVODListBySubjectRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.VRSQuerySubscriptionRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayMultiMediaVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryVODListBySubjectResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VRSQuerySubscriptionResponse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.control.selfregister.ErrorCodeConstant;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class ShowBuyControl extends BasePresenter {

    private static final String TAG = "ShowBuyControl";

    private RxAppCompatActivity rxAppCompatActivity;

    private ShowBuyTagCallBack callBack;

    private ShowMultViewResponseCallBack mMultViewCallbak;

    //??????????????????????????????id?????????id????????????querySubscription
    private List<String> productIds = new ArrayList<>();

    //????????????
    private int count = 0;

    //????????????????????????????????????productid????????????????????????
    private boolean isContains = false;

    private VODDetail mVodDetail;

    public ShowBuyControl(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;

        productIds.clear();
        isContains = false;
        count = 0;

        String productIdStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.CHOI_PRODUCT);
//        productIdStr = "[\"600000706846\",\"600000706826\"]";

        Log.i(TAG, "ShowBuyControl: " + productIdStr);
        if (!TextUtils.isEmpty(productIdStr)) {
            try {
                productIds = JsonParse.jsonToStringList(productIdStr);
                count = productIds.size();
            } catch (Exception e) {
                SuperLog.error(TAG, e);
            }
        }
    }

    /*
     * 0 ????????????
     * 1 ?????????
     * 2 ???????????????
     */
    private int showBuy = -1;


    public void ShowBuyTagBySubscription(VODDetail vodDetail) {
        if (null == callBack) {
            return;
        }
        count = 0;
        isContains = false;
        this.mVodDetail = vodDetail;
        if (null != productIds && productIds.size() > 0) {
            for (int i = 0; i < productIds.size(); i++) {
                String productId = productIds.get(i);
                querySubscription(productId);
            }
        } else {
            //?????????????????????id????????????????????????
            ShowBuyTag(vodDetail);
        }
    }

    public void ShowBuyTag(VODDetail vodDetail) {

        if (null == vodDetail) {
            showBuy = 0;
            if (null != callBack) {
                callBack.showBuyTag(0);
            }
            return;
        }
        String price = vodDetail.getPrice();
        if (TextUtils.isEmpty(price)) {
            showBuy = 1;
        } else {
            double pricet = Double.parseDouble(price);
            if (0 == pricet) {
                showBuy = 1;
            } else {
                ShowBuyTagByAuthentication(vodDetail);
                return;
            }
        }
        if (null != callBack) {
            callBack.showBuyTag(showBuy);
        }
    }

    //???????????????????????????????????????????????????
    private void ShowBuyTagByAuthentication(VODDetail vodDetail) {
        String type = vodDetail.getVODType();
        if (type.equals("0")) {//????????????
            List<VODMediaFile> vodMediaFiles = vodDetail.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                //??????
                playVOD(vodDetail);
            }
        } else {//??????????????? ??????????????????????????????????????????
            List<Episode> episodes = vodDetail.getEpisodes();
//            playVOD(episodes);
            //????????????????????????????????????
            Episode episode = getFirstEpisodeWithPrice(episodes);
            if (null != episode) {
                //??????
                playVOD(episode);
            } else {
                //?????????????????????????????????,?????????????????????
                showBuy = 1;
                callBack.showBuyTag(showBuy);
            }
        }
    }

    //???????????? ????????????
    public void playVOD(VODDetail vodDetail) {
        List<VODMediaFile> vodMediaFiles = vodDetail.getMediaFiles();
        playVod(vodMediaFiles, vodDetail.getID());
    }

    //??????????????? ??????????????????????????????????????????
//    public void playVOD(List<Episode> episodes){
//        if (episodes != null && episodes.size() != 0) {
//            for (int i = episodes.size() - 1; i > 0 ; i --) {
//                Log.i(TAG, "playVOD: count "+ i);
//                VOD vod = episodes.get(i).getVOD();
//                String price = vod.getPrice();
//                if (!TextUtils.isEmpty(price) && Double.parseDouble(price) != 0){
//                    Log.i(TAG, "playVOD: ????????????????????????" + showBuy);
////                    Episode playEpisode = episodes.get(i);
////                    playVOD(playEpisode);
////                    return;
//                }
//            }
//            Log.i(TAG, "playVOD: ???????????????????????? "+ showBuy);
//        }
//    }

    //??????????????????????????????????????????????????????????????????????????????????????????????????????Null
    private Episode getFirstEpisodeWithPrice(List<Episode> episodes) {
        if (episodes != null && episodes.size() != 0) {
            for (int i = episodes.size() - 1; i >= 0; i--) {
                VOD vod = episodes.get(i).getVOD();
                String price = vod.getPrice();
                if (!TextUtils.isEmpty(price) && Double.parseDouble(price) != 0) {
                    return episodes.get(i);
                }
            }
            return null;
        }
        return null;
    }

    //????????????????????????????????????????????????
    public void playVOD(Episode episode) {
        VOD vod = episode.getVOD();
        if (vod != null) {
            playVod(vod.getMediaFiles(), vod.getID());
        }
    }

    //??????????????????
    private void playVod(List<VODMediaFile> vodMediaFiles, String id) {
        if (vodMediaFiles != null) {
            if (vodMediaFiles.size() != 0) {
                VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                PlayVODRequest mPlayVodRequest = new PlayVODRequest();
                mPlayVodRequest.setVODID(id);
                mPlayVodRequest.setMediaID(vodMediaFile.getID());
                mPlayVodRequest.setURLFormat("1");
                mPlayVodRequest.setIsReturnProduct("1");
                getVODPlayUrlEPG(mPlayVodRequest, compose(bindToLifecycle(rxAppCompatActivity)), vodMediaFile.getElapseTime());
            }
        }
    }

    public void playVod(PlayVODRequest playVODRequest) {
        getVODPlayUrlEPG(playVODRequest, compose(bindToLifecycle(rxAppCompatActivity)), "0");
    }

    //??????????????????
    public void playMultVod(PlayMultiMediaVODRequest playVODRequest) {
        getVODPlayUrlsEPG(playVODRequest, compose(bindToLifecycle(rxAppCompatActivity)), "0");
    }



    /**
     * ?????????????????????????????????
     *
     * @param playVODRequest
     */
    private void getVODPlayUrlEPG(PlayVODRequest playVODRequest, ObservableTransformer<PlayVODResponse, PlayVODResponse> transformer, String elapseTime) {
        HttpApi.getInstance().getService().playVOD(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.PLAYVOD, playVODRequest)
                .compose(transformer)
                .subscribe(new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, rxAppCompatActivity) {
                    @Override
                    public void onSuccess(PlayVODResponse playVODResponse) {
                        if (playVODResponse != null && playVODResponse.getResult() != null) {
                            Result result = playVODResponse.getResult();
                            if (result != null
                                    && !TextUtils.isEmpty(result.getRetCode())
                                    && result.getRetCode().equals(Result.RETCODE_OK)) {
                                getVODPlayUrlSuccess(StringUtils.splicingPlayUrl(playVODResponse.getPlayURL()), playVODResponse.getBookmark()
                                        , playVODResponse.getAuthorizeResult().getProductID(), elapseTime);
                                getVODPlayUrlSuccess(playVODResponse);
                            } else {
                                playFail(playVODResponse);
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        playFail();
                    }
                });
    }
    /**
     * ?????????????????????????????????
     *
     * @param playVODRequest
     */
    private void getVODPlayUrlsEPG(PlayMultiMediaVODRequest playVODRequest, ObservableTransformer<PlayMultiMediaVODResponse, PlayMultiMediaVODResponse> transformer, String elapseTime) {
        HttpApi.getInstance().getService().PlayMultiMediaVOD(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.PLAY_MULTI_MEDIA_VOD, playVODRequest)
                .compose(transformer)
                .subscribe(new RxCallBack<PlayMultiMediaVODResponse>(HttpConstant.PLAY_MULTI_MEDIA_VOD, rxAppCompatActivity) {
                    @Override
                    public void onSuccess(PlayMultiMediaVODResponse playVODResponse) {
                        if (playVODResponse != null && playVODResponse.getResult() != null) {
                            Result result = playVODResponse.getResult();
                            if (result != null
                                    && !TextUtils.isEmpty(result.getRetCode())
                                    && result.getRetCode().equals(Result.RETCODE_OK)) {
                                getMultVodUrlsSuccess(playVODResponse);

//                                getVODPlayUrlSuccess(StringUtils.splicingPlayUrl(playVODResponse.getPlayURL()), playVODResponse.getBookmark()
//                                        , playVODResponse.getMultiMediaAuthorizeResults().get(0).getProductID(), elapseTime);
//                                getVODPlayUrlSuccess(playVODResponse);
                            } else {
//                                playFail(playVODResponse);
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        playFail();
                    }
                });
    }

    private void getMultVodUrlsSuccess(PlayMultiMediaVODResponse playVODResponse) {
        if (null != mMultViewCallbak) {
            mMultViewCallbak.showBuyTags(playVODResponse);
        }
    }

    //??????????????????
    private void querySubscription(String productId) {
        if (TextUtils.isEmpty(productId)) {
            //?????????id?????????????????????????????????
            querySubscriptionCallback(false);
            return;
        }
        VRSQuerySubscriptionRequest request = new VRSQuerySubscriptionRequest();
        request.setMessageID(PaymentUtils.generateMessageID());

        request.setUserType("1");

        UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        request.setUserId(userInfo.getUserId());

        request.setProductId(productId);

        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + HttpConstant.VRS_QUERY_SUBSCRIPTION;

        RxCallBack<VRSQuerySubscriptionResponse> rxCallBack = new RxCallBack<VRSQuerySubscriptionResponse>(url, OTTApplication.getContext()) {
            @Override
            public void onSuccess(VRSQuerySubscriptionResponse response) {
                if (null != response) {
                    if (null != response.getSubscriptions() && response.getSubscriptions().size() > 0) {
                        querySubscriptionCallback(true);
                        return;
                    }
                }
                querySubscriptionCallback(false);
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                //??????????????????????????????
                querySubscriptionCallback(false);
            }
        };

        HttpApi.getInstance().getService().vrsQuerySubscription(url, request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxCallBack);
    }

    //????????????????????????  isContains?????????????????????productid???????????????
    private void querySubscriptionCallback(boolean isContains) {
        count++;
        if (isContains) {
            this.isContains = true;
        }
        if (count == productIds.size()) {
            if (this.isContains) {
                //???????????????/?????????????????????
                showBuy = 1;
                callBack.showBuyTag(showBuy);
            } else {
                //??????????????????/????????????????????????????????????
                ShowBuyTag(mVodDetail);
            }
        }
    }

    //??????????????????????????????
    private void getVODPlayUrlSuccess(String url, String bookmark, String productId, String elapseTime) {
        showBuy = 2;
        callBack.showBuyTag(showBuy);
    }

    //??????????????????????????????
    private void getVODPlayUrlSuccess(PlayVODResponse response) {
        if (null != mMultViewCallbak) {
            mMultViewCallbak.showBuyTag(response);
        }
    }

    //???????????????????????????
    private void playFail(PlayVODResponse playVODResponse) {
        showBuy = 0;
        callBack.showBuyTag(showBuy);
        if(null!=mMultViewCallbak)
        mMultViewCallbak.showBuyTag(playVODResponse);
    }

    //????????????????????????
    private void playFail() {
        showBuy = 1;
        callBack.showBuyTag(showBuy);
        if(null!=mMultViewCallbak)
        mMultViewCallbak.showBuyTag(null);
    }


    public interface ShowBuyTagCallBack {
        void showBuyTag(int showBuy);
    }

    public void setCallBack(ShowBuyTagCallBack callBack) {
        this.callBack = callBack;
    }

    public interface ShowMultViewResponseCallBack {
        void showBuyTag(PlayVODResponse response);
        void showBuyTags(PlayMultiMediaVODResponse response);
    }

    public void setCallBack(ShowMultViewResponseCallBack callBack) {
        this.mMultViewCallbak = callBack;
    }
}
