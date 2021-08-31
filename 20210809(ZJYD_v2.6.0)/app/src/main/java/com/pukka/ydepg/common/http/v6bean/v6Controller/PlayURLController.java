package com.pukka.ydepg.common.http.v6bean.v6Controller;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.PlayURLCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ReportVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.PlayVODResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ReportVODResponse;
import com.pukka.ydepg.common.report.error.ErrorInfoReport;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.StringUtils;
import com.pukka.ydepg.launcher.util.RxCallBack;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayURLController.java
 * @author: yh
 * @date: 2017-04-25 16:52
 */

public class PlayURLController extends BaseController {

    private static final String TAG = "PlayURLController";


    private PlayURLCallBack playURLCallBack;

    //是否需要展示自选集包弹框
    public boolean needShowAlacarteChoosePopWindow = false;


    public PlayURLController(final RxAppCompatActivity rxAppCompatActivity, final PlayURLCallBack playURLCallBack) {
        this.rxAppCompatActivity = rxAppCompatActivity;
        this.playURLCallBack = playURLCallBack;
    }

    public PlayURLController(final RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public void setPlayURLCallBack(PlayURLCallBack playURLCallBack){
        this.playURLCallBack=playURLCallBack;
    }

    /**
     * 获取点播相关的播放地址
     *
     * @param playVODRequest
     */
    public void getVODPlayUrlEPG(PlayVODRequest playVODRequest, ObservableTransformer<PlayVODResponse, PlayVODResponse> transformer,String elapseTime) {
        HttpApi.getInstance().getService().playVOD(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.PLAYVOD, playVODRequest)
                .compose(transformer)
                .subscribe(new RxCallBack<PlayVODResponse>(HttpConstant.PLAYVOD, this.rxAppCompatActivity) {
                    @Override
                    public void onSuccess(PlayVODResponse playVODResponse) {
                        SuperLog.debug(TAG,"getVODPlayUrlEPG onSuccess");

                        Log.i(TAG, "zixuanji onSuccess: "+ JsonParse.object2String(playVODResponse.getResult()) + " extensionFields : "+ JsonParse.object2String(playVODResponse.getExtensionFields()));
                        Log.i(TAG, "zixuanji onSuccess: "+JsonParse.object2String(playVODResponse.getAuthorizeResult()));


                        if (playVODResponse != null && playVODResponse.getResult() != null) {
                            Result result = playVODResponse.getResult();
                            if (result != null
                                    && !TextUtils.isEmpty(result.getRetCode())
                                    && result.getRetCode().equals(Result.RETCODE_OK)) {
                                if (playURLCallBack != null)
                                    playURLCallBack.getVODPlayUrlSuccess(StringUtils.splicingPlayUrl(playVODResponse.getPlayURL()), playVODResponse.getBookmark()
                                            , playVODResponse.getAuthorizeResult().getProductID(),elapseTime);
                            } else {
                                //上报错误码到探针中间件
                                if(result != null){
                                    ErrorInfoReport.getInstance().reportErrorCode(HttpConstant.PLAYVOD,result.getRetCode());
                                }

                                if (playURLCallBack != null) {
                                    if(!TextUtils.isEmpty(playVODResponse.getPlayURL())){
                                        playVODResponse.setPlayURL(StringUtils.splicingPlayUrl(playVODResponse.getPlayURL()));
                                    }
                                    playURLCallBack.playFail(playVODResponse);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        if (playURLCallBack != null) {
                            playURLCallBack.playFail();
                        }
                    }
                });

    }


    @SuppressLint("CheckResult")
    public void reportVod(ReportVODRequest request, ObservableTransformer<ReportVODResponse, ReportVODResponse> transformer){
        HttpApi.getInstance().getService().reportVOD(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.REPORTVOD,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reportVODResponse->{
                    if(null!=reportVODResponse&&null!=reportVODResponse.getResult()&&Result.RETCODE_OK.equals(reportVODResponse.getResult().getRetCode())){
                        SuperLog.debug(TAG,"reportVod:"+request.getVODID()+" reportVod Success");
                    }else{
                        SuperLog.debug(TAG,"reportVod:"+request.getVODID()+" reportVod fail");
                    }

                },throwable -> {
                    SuperLog.debug(TAG,"reportVod:"+request.getVODID()+" reportVod fail");
                });
    }


}
