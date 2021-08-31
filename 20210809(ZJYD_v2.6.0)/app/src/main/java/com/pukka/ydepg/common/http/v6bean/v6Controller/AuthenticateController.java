package com.pukka.ydepg.common.http.v6bean.v6Controller;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.listener.HttpOnNextListener;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QrCodeCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryBindedSubscriberCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QueryQrCodeStatusCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.QuitQrCodeCallBack;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.UnBindSubsrciberCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Entity.QueryBindedSubscriberEntity;
import com.pukka.ydepg.common.http.v6bean.v6Entity.QueryQrCodeAuthenticateStatusEntity;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6request.QrCodeAuthenticateRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBindedSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryQrCodeStatusRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QuitQrCodeAuthenticateRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SetQrCodeSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UnBindSubsrciberRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QrCodeAuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryBindedSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryQrCodeStatusResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuitQrCodeAuthenticateResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SetQrCodeSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UnBindSubsrciberResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.ObservableTransformer;



/**
 * 接口绑定相关控制
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6Controller.AuthenticateController.java
 * @author:xj
 * @date: 2018-01-09 15:02
 */
public class AuthenticateController extends  BaseController {
    private QrCodeCallBack mQrCodeCallBack;
    private QueryQrCodeStatusCallBack mQueryQrCodeStatusCallBack;
    private QueryQrCodeAuthenticateStatusEntity qrCodeAuthenticateStatusEntity;
    private QueryBindedSubscriberEntity queryBindedSubscriberEntity;
    private QueryBindedSubscriberCallBack queryBindedSubscriberCallBack;

    public AuthenticateController(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity=rxAppCompatActivity;

        qrCodeAuthenticateStatusEntity = new QueryQrCodeAuthenticateStatusEntity(new HttpOnNextListener<QueryQrCodeStatusResponse>() {
            @Override
            public void onNext(QueryQrCodeStatusResponse queryQrCodeStatusResponse) {
                if (null != queryQrCodeStatusResponse) {
                    if (null != mQueryQrCodeStatusCallBack) {
                        Result result = queryQrCodeStatusResponse.getResult();
                        if (null != result){
                            String retCode = result.getRetCode();
                            if (!TextUtils.isEmpty(retCode)){
                                if (TextUtils.equals(retCode,Result.RETCODE_OK)){
                                    mQueryQrCodeStatusCallBack.queryQrCodeStatusSuccess(queryQrCodeStatusResponse);
                                }else {
                                    mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(ErrorCode.findError(qrCodeAuthenticateStatusEntity.getInterfaceName(),retCode));
                                }
                            }else {
                                mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
                            }
                        }else {
                            mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
                        }
                        mQueryQrCodeStatusCallBack.queryQrCodeStatusSuccess(queryQrCodeStatusResponse);
                    }
                } else {
                    if (null != mQueryQrCodeStatusCallBack)
                        mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
                }

            }

            @Override
            public void onError(Throwable e) {
                if (null != mQueryQrCodeStatusCallBack)
                    mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
            }

            @Override
            public void onCancel() {
                if (null != mQueryQrCodeStatusCallBack)
                    mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
            }
        }, rxAppCompatActivity);


        queryBindedSubscriberEntity = new QueryBindedSubscriberEntity(new HttpOnNextListener<QueryBindedSubscriberResponse>() {
            @Override
            public void onNext(QueryBindedSubscriberResponse queryBindedSubscriberResponse) {
                if (null != queryBindedSubscriberResponse){
                    if (queryBindedSubscriberResponse.isSuccess()){
                       if (null != queryBindedSubscriberCallBack){
                           queryBindedSubscriberCallBack.qeryBindedSubscriberSuccess(queryBindedSubscriberResponse);
                       }
                    }else {
                        if (null != queryBindedSubscriberCallBack)
                            queryBindedSubscriberCallBack.qeryBindedSubscriberFailed(ErrorCode.findError(queryBindedSubscriberEntity.getInterfaceName(),queryBindedSubscriberResponse.getResult().getRetCode()));
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                if (null != queryBindedSubscriberCallBack)
                    queryBindedSubscriberCallBack.qeryBindedSubscriberFailed(null);
            }

            @Override
            public void onCancel() {
                if (null != queryBindedSubscriberCallBack)
                    queryBindedSubscriberCallBack.qeryBindedSubscriberFailed(null);
            }
        },rxAppCompatActivity);
    }

    /**
     * 二维码生成
     * @param qrCodeCallBack
     */
    @SuppressLint("CheckResult")
    public void qrCodeAuthenticate(QrCodeCallBack qrCodeCallBack,
                                   ObservableTransformer<QrCodeAuthenticateResponse,QrCodeAuthenticateResponse> transformer) {
        mQrCodeCallBack = qrCodeCallBack;
        QrCodeAuthenticateRequest request = new QrCodeAuthenticateRequest();
        HttpApi.getInstance().getService().qrCodeAuthenticate(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QRCODEAUTHENTICATE,request)
                .compose(transformer)
                .subscribe(qrCodeAuthenticateResponse ->{
                    if (null != qrCodeAuthenticateResponse && null != qrCodeAuthenticateResponse.getResult()
                            && TextUtils.equals(qrCodeAuthenticateResponse.getResult().getRetCode(),Result.RETCODE_OK) ) {
                        if (null != mQrCodeCallBack) {
                            mQrCodeCallBack.qrCodeCallBackSuccess(qrCodeAuthenticateResponse);
                        }
                    } else if(null != qrCodeAuthenticateResponse && null != qrCodeAuthenticateResponse.getResult()
                            && !TextUtils.equals(qrCodeAuthenticateResponse.getResult().getRetCode(),Result.RETCODE_OK) ){
                       handleError(qrCodeAuthenticateResponse.getResult().getRetCode(),HttpConstant.QRCODEAUTHENTICATE);
                    } else {
                        if (null != mQrCodeCallBack)
                            mQrCodeCallBack.qrCodeCallBackFailed();
                    }
                },throwable -> {
                    if (null != mQrCodeCallBack)
                        mQrCodeCallBack.qrCodeCallBackFailed();
                });
    }
    /**
     * 二维码状态检查
     * @param qrCodeStatusCallBack
     */
    @SuppressLint("CheckResult")
    public void queryQrCodeStatus(QueryQrCodeStatusCallBack qrCodeStatusCallBack,
                                  ObservableTransformer<QueryQrCodeStatusResponse,QueryQrCodeStatusResponse> transformer){
        mQueryQrCodeStatusCallBack = qrCodeStatusCallBack;
        QueryQrCodeStatusRequest request = new QueryQrCodeStatusRequest();
        HttpApi.getInstance().getService().queryQrCodeAuthenticateStatus(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QUERYQRCODEAUTHENTICATESTATUS,request)
                .compose(transformer)
                .subscribe(queryQrCodeStatusResponse ->{
                    if (null != queryQrCodeStatusResponse) {
                        if (null != mQueryQrCodeStatusCallBack) {
                            Result result = queryQrCodeStatusResponse.getResult();
                            if (null != result){
                                String retCode = result.getRetCode();
                                if (!TextUtils.isEmpty(retCode)){
                                    if (TextUtils.equals(retCode,Result.RETCODE_OK)){
                                        mQueryQrCodeStatusCallBack.queryQrCodeStatusSuccess(queryQrCodeStatusResponse);
                                    }else {
                                        handleError(retCode);
                                        mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(ErrorCode.findError(qrCodeAuthenticateStatusEntity.getInterfaceName(),retCode));
                                    }
                                }else {
                                    mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
                                }
                            }else {
                                mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
                            }
                            mQueryQrCodeStatusCallBack.queryQrCodeStatusSuccess(queryQrCodeStatusResponse);
                        }
                    } else {
                        if (null != mQueryQrCodeStatusCallBack)
                            mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
                    }

                },throwable -> {
                    if (null != mQueryQrCodeStatusCallBack)
                        mQueryQrCodeStatusCallBack.queryQrCodeStatusFailed(null);
                });


    }

    /**
     * 查询绑定情况
     * @param queryBindedSubscriberCallBack
     */
    @SuppressLint("CheckResult")
    public void queryBindedSubscriber(QueryBindedSubscriberCallBack queryBindedSubscriberCallBack
            , ObservableTransformer<QueryBindedSubscriberResponse,QueryBindedSubscriberResponse> transformer){
        this.queryBindedSubscriberCallBack = queryBindedSubscriberCallBack;
        QueryBindedSubscriberRequest request = new QueryBindedSubscriberRequest();
        HttpApi.getInstance().getService().qeryBindedSubscriber(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QUERYBINDEDSUBSCRIBER,request)
                .compose(transformer)
                .subscribe(queryBindedSubscriberResponse ->{
                    if (null != queryBindedSubscriberResponse){
                        if (queryBindedSubscriberResponse.isSuccess()){
                            if (null != queryBindedSubscriberCallBack){
                                queryBindedSubscriberCallBack.qeryBindedSubscriberSuccess(queryBindedSubscriberResponse);
                            }
                        }else {
                            handleError(queryBindedSubscriberResponse.getResult().getRetCode(),queryBindedSubscriberEntity.getInterfaceName());
                            if (null != queryBindedSubscriberCallBack)
                                queryBindedSubscriberCallBack.qeryBindedSubscriberFailed(ErrorCode.findError(queryBindedSubscriberEntity.getInterfaceName(),queryBindedSubscriberResponse.getResult().getRetCode()));
                        }
                    }

                },throwable -> {
                    if (null != queryBindedSubscriberCallBack)
                        queryBindedSubscriberCallBack.qeryBindedSubscriberFailed(null);

                });

    }

    /**
     * 解绑
     * @param subscriberId
     * @param unBindSubscriberCallBack
     */
    @SuppressLint("CheckResult")
    public void unBindSubscriber(String subscriberId, UnBindSubsrciberCallBack unBindSubscriberCallBack
            , ObservableTransformer<UnBindSubsrciberResponse,UnBindSubsrciberResponse> transformer){
        UnBindSubsrciberRequest request = new UnBindSubsrciberRequest();
        request.setTargetSubscriberId(subscriberId);
        HttpApi.getInstance().getService().unBindSubsrciber(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.UNBINDSUBSCRIBER,request)
                .compose(transformer)
                .subscribe(unBindSubscriberResponse ->{
                    if (null != unBindSubscriberResponse){
                        if (unBindSubscriberResponse.isSuccess()){
                            if (null != unBindSubscriberCallBack)
                                unBindSubscriberCallBack.unBindSubsrciberSuccess();
                        }else {
                            handleError(unBindSubscriberResponse.getResult().getRetCode());
                            if (null != unBindSubscriberCallBack)
                                unBindSubscriberCallBack.unBindSubsrciberFailed();
                        }

                    }else {
                        if (null != unBindSubscriberCallBack)
                            unBindSubscriberCallBack.unBindSubsrciberFailed();
                    }
                },throwable -> {
                    if (null != unBindSubscriberCallBack)
                        unBindSubscriberCallBack.unBindSubsrciberFailed();
                });
    }

    @SuppressLint("CheckResult")
    public void quitQrCodeAuthenticate(QuitQrCodeCallBack quitQrCodeCallBack
            , ObservableTransformer<QuitQrCodeAuthenticateResponse,QuitQrCodeAuthenticateResponse> transformer){
        QuitQrCodeAuthenticateRequest request = new QuitQrCodeAuthenticateRequest();
        HttpApi.getInstance().getService().quitQrCodeAuthentiCate(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QUITQRCODEAUTHENTICATE,request).compose(transformer)
                .subscribe(quitQrCodeAuthenticateResponse -> {
                    if (null != quitQrCodeAuthenticateResponse){
                        if (quitQrCodeAuthenticateResponse.isSuccess()){
                            if (null != quitQrCodeCallBack)
                                quitQrCodeCallBack.quitQrCodeSuccess();

                        }else {
                            handleError(quitQrCodeAuthenticateResponse.getResult().getRetCode());
                            if (null != quitQrCodeCallBack)
                                quitQrCodeCallBack.quitQrCodeFailed();
                        }
                    }else {
                        if (null != quitQrCodeCallBack)
                            quitQrCodeCallBack.quitQrCodeFailed();
                    }
                },throwable -> {
                    if (null != quitQrCodeCallBack)
                        quitQrCodeCallBack.quitQrCodeFailed();
                });
    }

    @SuppressLint("CheckResult")
    public void setQrCodeAuthenticatedSubscriber(String subscriberId
            , ObservableTransformer<SetQrCodeSubscriberResponse,SetQrCodeSubscriberResponse> transformer){
        SetQrCodeSubscriberRequest request = new SetQrCodeSubscriberRequest();
        request.setQrCodeAuthenticatedSubscriberId(subscriberId);
        HttpApi.getInstance().getService().setQrCodeAuthenticatedSubscriber(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.SETQRCODEAUTHENTICATEDSUBSCRIBER,request).compose(transformer)
                .subscribe(setQrCodeSubscriberResponse -> {
                });
    }
}