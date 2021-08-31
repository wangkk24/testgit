package com.pukka.ydepg.launcher.util;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.common.http.vss.ResultCode;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;
import com.pukka.ydepg.common.http.vss.response.OrderProductResponse;
import com.pukka.ydepg.common.http.vss.response.VSSBaseResponse;
import com.pukka.ydepg.common.report.error.ErrorInfoReport;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.errorWindowUtil.OTTErrorWindowUtils;
import com.pukka.ydepg.launcher.mvp.contact.IBaseContact;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.ResourceObserver;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.util.RxCallBack.java
 * @date: 2018-02-02 22:52
 * @version: V1.0 描述当前版本功能
 */
public abstract class RxCallBack<T> extends ResourceObserver<T> {
    private static final String TAG = "RxCallBack";
    private IBaseContact.IBaseView mView;
    private String mInterfaceName;
    private Context mContext;

    public RxCallBack(IBaseContact.IBaseView view, String interfaceName, Context context) {
        this.mView = view;
        this.mInterfaceName = interfaceName;
        this.mContext = context;
    }

    public RxCallBack(String interfaceName, Context context) {
        this.mInterfaceName = interfaceName;
        this.mContext = context;
    }

    public RxCallBack(Context context) {
        this.mContext = context;
    }

    @Override
    public void onNext(@NonNull T t) {
        //SuperLog.info2SD(TAG,"onNext First " + mInterfaceName);
        if (t instanceof BaseResponse) {
            BaseResponse baseResponse = (BaseResponse)t;
            if (null != baseResponse.getResult()) {
                Result result = baseResponse.getResult();
                if (TextUtils.equals(result.getRetCode(),Result.RETCODE_OK)) {
                    //接口调用成功
                    onSuccess(t);
                } else {
                    //接口调用失败       首先上报错误码到探针中间件
                    ErrorInfoReport.getInstance().reportErrorCode(mInterfaceName,result.getRetCode());
                    if (CommonUtil.isSessionError(result.getRetCode())) {
                        //Session超时错误
                        ThreadManager.getInstance().getSingleThreadPool().execute(()-> {
                            SuperLog.error(TAG,mInterfaceName + " SessionTimeOut,begin to reportTokenStatus(UpdateToken)");
                            AuthenticateManager.getInstance().reportTokenStatus();
                            if (HttpConstant.QUERYEPGHOMEVOD.equals(mInterfaceName)){
                                onFail(new Throwable(HttpConstant.QUERYEPGHOMEVOD + " error:" + result.getRetCode()));
                            }
                        });
                    } else {
                        //其他错误(非Session超时错误)
                        if ( !TextUtils.isEmpty(mInterfaceName) && mView != null && !HttpConstant.QUERY_SUBSCRIBE_INFO.equals(mInterfaceName)) {
                            OTTErrorWindowUtils.getErrorInfoFromPbs(mInterfaceName,result.getRetCode(),mContext);
                            onFail(new Throwable(mInterfaceName + " error:" + result.getRetCode()));
                        }else{
                            //很多接口没有传mView，playVod接口没有传，需要不是RETCODE_OK的返回
                            onSuccess(t);
                        }
                    }
                }
            } else {
                onSuccess(t);
            }
        } else if (t instanceof VSSBaseResponse) {
            VSSBaseResponse response = (VSSBaseResponse) t;
            if (ResultCode.OK.code().equals(response.getCode())) {
                onSuccess(t);
            } else {
                String resultCode = response.getCode();
                showErrorInfo(resultCode);
            }
        } else if (t instanceof OrderProductResponse) {
            OrderProductResponse response = (OrderProductResponse) t;
            ResultInfo result = response.getResult();
            if (result != null) {
                String resultCode = result.getResultCode();
                //添加对互斥包返回码10030164的判断处理 2020.6.22
                if (ResultCode.OK.code().equals(resultCode) || Result.RETCODE_MUTEX.equals(resultCode) || Result.RETCODE_VERIFIED_WRONG.equals(resultCode)) {
                    onSuccess(t);
                } else {
                    showErrorInfo(resultCode);
                }
            }
        } else {
            //SuperLog.info2SD(TAG, mInterfaceName + " response is a unknown type. Go to onSuccess callback");
            onSuccess(t);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        SuperLog.error(TAG, "Interface[" + mInterfaceName + "] return error. Object=" + this);
        SuperLog.error(TAG,e);
        onFail(e);
        OTTErrorWindowUtils.getErrorInfoFromPbs(mInterfaceName,e.getMessage(),mContext);
    }

    @Override
    public void onComplete() {}

    public abstract void onSuccess(@NonNull T t);

    public abstract void onFail(@NonNull Throwable e);

    private void showErrorInfo(String errorCode) {
        //上报错误码到探针中间件
        ErrorInfoReport.getInstance().reportErrorCode(mInterfaceName,errorCode);
        onFail(new Throwable(mInterfaceName + " error:" + errorCode));
        OTTErrorWindowUtils.getErrorInfoFromPbs(mInterfaceName,errorCode,mContext);
    }
}