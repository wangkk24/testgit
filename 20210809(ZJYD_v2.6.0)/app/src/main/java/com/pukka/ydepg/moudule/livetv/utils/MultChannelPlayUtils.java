package com.pukka.ydepg.moudule.livetv.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayMultiMediaVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.VRSQuerySubscriptionRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayChannelResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayMultiMediaVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VRSQuerySubscriptionResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;
import com.pukka.ydepg.moudule.search.presenter.BasePresenter;
import com.pukka.ydepg.moudule.vod.utils.MultViewUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class MultChannelPlayUtils extends BasePresenter {

    private static final String TAG = MultChannelPlayUtils.class.getSimpleName();
    private RxCallBack<PlayChannelResponse> mPlayChannelCallback;

    private PlayurlCallback mPlayurlCallback;

    public void setmPlayurlCallback(PlayurlCallback mPlayurlCallback) {
        this.mPlayurlCallback = mPlayurlCallback;
    }

    public void playChannel(PlayChannelRequest playChannelRequest,RxAppCompatActivity rxAppCompatActivity,Context context) {
        playChannel(playChannelRequest, compose(bindToLifecycle(rxAppCompatActivity)),context );
    }

    /**
     * 直播鉴权
     *
     * @param request     直播鉴权request
     * @param transformer transformer
     */
//    public synchronized void playChannel(PlayChannelRequest request, ObservableTransformer<PlayChannelResponse, PlayChannelResponse> transformer, Context context) {
    public synchronized void playChannel(PlayChannelRequest request, ObservableTransformer<PlayChannelResponse, PlayChannelResponse> transformer, Context context) {
        mPlayChannelCallback = new RxCallBack<PlayChannelResponse>(HttpConstant.PLAYCHANNEL, context) {
            @Override
            public void onSuccess(PlayChannelResponse response) {
                SuperLog.info2SD(TAG, "Time[ReceivePlayChannel]=" + System.currentTimeMillis());
                if (null != response && null != response.getResult()) {
                    Result result = response.getResult();
                    String returnCode = result.getRetCode();
                    if (!TextUtils.isEmpty(returnCode)) {
                        //鉴权结果
                        AuthorizeResult authorizeResult = response.getAuthorizeResult();
                        if (returnCode.equals(Result.RETCODE_OK)) {
                            String productId = null;
                            String url = response.getPlayURL();
                            //鉴权成功获取到的产品ID
                            if (!TextUtils.isEmpty(productId) || !TextUtils.isEmpty(url)) {
                                //播放地址
                                SuperLog.debug(TAG, "[playChannel] get playUrl success.");
                                if (null != mPlayurlCallback) {
//                                    mPlayurlCallback.getPlayUrl(request.getChannelID(), StringUtils.splicingPlayUrl(url), response.getBookmark(), StringUtils.splicingPlayUrl(response.getAttachedPlayURL()));
                                    mPlayurlCallback.getPlayUrlSuccess(url);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFail(@NonNull Throwable e) {
                if (null != mPlayChannelCallback) {
                    mPlayurlCallback.getPlayUrlError();
                }
            }
        };
        SuperLog.info2SD(TAG, "Time[SendPlayChannel]=" + System.currentTimeMillis());
        HttpApi.getInstance().getService().playChannel(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.PLAYCHANNEL, request)
                .compose(transformer)
                .subscribe(mPlayChannelCallback);
    }

    public interface PlayurlCallback
    {
        void getPlayUrlSuccess(String url);
        void getPlayUrlError();
    }
}
