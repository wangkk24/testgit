package com.pukka.ydepg.moudule.mytv.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.node.User;
import com.pukka.ydepg.common.http.bean.request.OrderProductRequest;
import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6request.CheckVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ConvertFeeToScoreRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.ConvertFeeToScoreResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VerifiedCodeResponse;
import com.pukka.ydepg.common.http.vss.node.BusiInfo;
import com.pukka.ydepg.common.http.vss.node.RespParam;
import com.pukka.ydepg.common.http.vss.node.ResultInfo;
import com.pukka.ydepg.common.http.vss.request.BodyofGeneralBossQueryRequest;
import com.pukka.ydepg.common.http.vss.request.QueryResultRequest;
import com.pukka.ydepg.common.http.vss.response.BodyofGeneralBossQueryResponse;
import com.pukka.ydepg.common.http.vss.response.QueryResultBalResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;


import io.reactivex.annotations.NonNull;

public class PointPayPresenter extends BasePresenter {

    private static final String TAG = "PointPayPresenter";


    //发送验证码接口
    public void sendVerifiedCode(String mobileNum, Context context,SendVerifiedCodeCallBack callBack){

        SendVerifiedCodeRequest request = new SendVerifiedCodeRequest();
        request.setMessageID(PaymentUtils.generateMessageID());
        request.setMobileNum(mobileNum);

        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            request.setUserId(mUserInfo.getUserId());
        }
        request.setUserType(2);
        request.setSceneType(2);

        String interfaceName = HttpConstant.SEND_VERIFIED_CODE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().sendVerifiedCode(url,request).compose(compose(getBaseView().bindToLife())).subscribe(new RxCallBack<VerifiedCodeResponse>(HttpConstant.SEND_VERIFIED_CODE,context) {
            @Override
            public void onSuccess(VerifiedCodeResponse verifiedCodeResponse) {

                if (null != verifiedCodeResponse && !TextUtils.isEmpty(verifiedCodeResponse.getCode())){
                    if (verifiedCodeResponse.getCode().equals("0")){
                        if (null != callBack){
                            callBack.sendVerifiedCodeSuccsee();
                            return;
                        }
                    }else{
                        if (null != callBack){
                            callBack.sendVerifiedCodeFail(verifiedCodeResponse.getDescriptionFromCode(verifiedCodeResponse.getCode()));
                            return;
                        }
                    }
                }
                if (null != callBack){
                    callBack.sendVerifiedCodeFail("发送验证码失败");
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (null != callBack){
                    callBack.sendVerifiedCodeFail("发送验证码失败");
                }
            }
        });
    }

    //验证验证码接口
    public void checkVerifiedCode(String verifiedCode, String number,Context context,CheckVerifiedCodeCallBack callBack){

        CheckVerifiedCodeRequest request = new CheckVerifiedCodeRequest();
        request.setMessageID(PaymentUtils.generateMessageID());
        request.setVerifiedCode(verifiedCode);

        request.setUserId(number);
        request.setUserType("1");

        request.setSceneType("2");

        String interfaceName = HttpConstant.CHECK_VERIFIED_CODE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().checkVerifiedCode(url,request).compose(compose(getBaseView().bindToLife())).subscribe(new RxCallBack<VerifiedCodeResponse>(HttpConstant.CHECK_VERIFIED_CODE,context) {
            @Override
            public void onSuccess(VerifiedCodeResponse verifiedCodeResponse) {
                if (null != verifiedCodeResponse && !TextUtils.isEmpty(verifiedCodeResponse.getCode())){
                    if (verifiedCodeResponse.getCode().equals("0")){
                        if (null != callBack){
                            callBack.checkVerifiedCodeSuccsee();
                            return;
                        }
                    }else{
                        if (null != callBack){
                            callBack.checkVerifiedCodeFail(verifiedCodeResponse.getDescriptionFromCode(verifiedCodeResponse.getCode()));
                            return;
                        }
                    }
                }
                if (null != callBack){
                    callBack.checkVerifiedCodeFail("发送验证码失败");
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (null != callBack){
                    callBack.checkVerifiedCodeFail(e.getMessage());
                }
            }
        });
    }

    //积分换算接口
    public void convertFeeToScore(String fee, Context context, ConvertFeeToScoreCallBack callBack){

        ConvertFeeToScoreRequest request = new ConvertFeeToScoreRequest();
        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            User mUser = new User();
            mUser.setId(mUserInfo.getUserId());
            request.setUserId(mUser);
        }
        request.setCurrency("CNY");
        request.setFee(fee);
        String interfaceName = HttpConstant.CONVERT_FEE_TO_SCORE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().convertFeeToScore(url,request).compose(compose(getBaseView().bindToLife())).subscribe(new RxCallBack<ConvertFeeToScoreResponse>(HttpConstant.CONVERT_FEE_TO_SCORE,context) {
            @Override
            public void onSuccess(ConvertFeeToScoreResponse convertFeeToScoreResponse) {

                ResultInfo resultInfo = convertFeeToScoreResponse.getResult();
                if (null != resultInfo && null != resultInfo.getResultCode()){
                    if (resultInfo.getResultCode().equals("0") && !TextUtils.isEmpty(convertFeeToScoreResponse.getScore())){
                        if (null != callBack){
                            callBack.convertFeeToScoreSuccsee(convertFeeToScoreResponse.getScore());
                            return;
                        }
                    }
                }
                if (null != resultInfo){
                    if (null != callBack){
                        callBack.convertFeeToScoreFail(resultInfo.getDescription());
                    }
                }else{
                    if (null != callBack){
                        callBack.convertFeeToScoreFail("");
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (null != callBack){
                    callBack.convertFeeToScoreFail(e.getMessage());
                }
            }
        });
    }

    //查询积分详情接口
    public void queryScore(String billId, Context context , QueryPointsCallBack callback) {
        QueryResultRequest resultRequest = new QueryResultRequest();
        BusiInfo busiInfo = new BusiInfo();
        busiInfo.setBillId(billId);
        BodyofGeneralBossQueryRequest body = new BodyofGeneralBossQueryRequest();
        body.setBusiInfo(busiInfo);
        resultRequest.setBodyofGeneralBossQueryRequest(body);
        resultRequest.setAddressCode("addressCode1");
        resultRequest.setHeadofGeneralBossQueryRequest("");
        resultRequest.setInterfaceName("ESB_CS_SCORE_SCORE_QRY_002");
        resultRequest.setMessageID(PaymentUtils.generateMessageID());
        String interfaceName = HttpConstant.QUERY_RESULT_BAL;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().queryResultBal(url, resultRequest).compose(compose(getBaseView().bindToLife()))
                .subscribe(new RxCallBack<QueryResultBalResponse>(HttpConstant.QUERY_RESULT_BAL, context) {
                    @Override
                    public void onSuccess(QueryResultBalResponse resultBalResponse) {
                        if (null != resultBalResponse){
                            SuperLog.debug(TAG,resultBalResponse.getDescription());
                            BodyofGeneralBossQueryResponse body = resultBalResponse.getBodyofGeneralBossQueryResponse();
                            if (null != body){
                                RespParam param = body.getRESP_PARAM();
                                if (null != param){
                                    if (null != callback){
                                        callback.queryPointSuccess(param);
                                        return;
                                    }
                                }
                            }
                        }
                        if (null != callback){
                            callback.queryPointFail();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        SuperLog.error(TAG,e);
                        if (null != callback){
                            callback.queryPointFail();
                        }
                    }
                });
    }

    //积分订购
    public void ScorePay(OrderProductRequest request , Context context , QueryPointsCallBack callBack){
        HttpApi.getInstance().getService().orderProduct(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP +HttpConstant.ORDER_PRODUCT,request)
                .compose(compose(getBaseView().bindToLife()))
                .subscribe(new RxCallBack<OrderProductResponse>(HttpConstant.ORDER_PRODUCT,context) {
                    @Override public void onSuccess(OrderProductResponse response) {
                        if(null!=response && null!=response.getResult()){
                            String returnCode=response.getResult().getRetCode();
                            String returnMsg=response.getResult().getRetMsg();
                            if(!TextUtils.isEmpty(returnCode)){
                                if(returnCode.equals(Result.RETCODE_OK)||returnCode.equals(Result.RETCODE_OK_TWO)){
                                    if (null != callBack ){
                                        callBack.pointPaySuccess(response);
                                    }
                                    return;
                                }else{
                                    SuperLog.error(TAG,"returnCode:"+returnCode+",returnMsg:"+returnMsg);
                                    //添加判断是互斥产品包的逻辑
                                    if (returnCode.equals(Result.RETCODE_MUTEX)){
                                        String mutexStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderProductMutexInfo();
                                        if (null != mutexStr && mutexStr.length()>0){
                                            EpgToast.showLongToast(OTTApplication.getContext(),
                                                    mutexStr);
                                        }else{
                                            EpgToast.showLongToast(OTTApplication.getContext(),
                                                    context.getResources().getString(R.string.big_small_product_mutex));
                                        }
                                        if (null != callBack){
                                            callBack.pointPayFail();
                                        }
                                        return;
                                    }
                                    EpgToast.showLongToast(OTTApplication.getContext(),
                                            ErrorCode.findError(HttpConstant.ORDER_PRODUCT,returnCode).getMessage());
                                    if (null != callBack){
                                        callBack.pointPayFail();
                                    }
                                    return;
                                }
                            }
                        }
                        EpgToast.showToast(OTTApplication.getContext(),
                                OTTApplication.getContext().getString(R.string.order_payment_failed));
                        if (null != callBack){
                            callBack.pointPayFail();
                        }
                    }

                    @Override public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG,"payment error");
                        EpgToast.showToast(OTTApplication.getContext(),
                                OTTApplication.getContext().getString(R.string.order_payment_failed));
                        if (null != callBack){
                            callBack.pointPayFail();
                        }
                    }
                });
    }

    public interface SendVerifiedCodeCallBack{
        void sendVerifiedCodeSuccsee();
        void sendVerifiedCodeFail(String description);
    }

    public interface CheckVerifiedCodeCallBack{
        void checkVerifiedCodeSuccsee();
        void checkVerifiedCodeFail(String description);
    }

    public interface ConvertFeeToScoreCallBack{
        void convertFeeToScoreSuccsee(String score);
        void convertFeeToScoreFail(String description);
    }

    public interface QueryPointsCallBack{
        void queryPointSuccess(RespParam param);
        void queryPointFail();
        void pointPaySuccess(OrderProductResponse response);
        void pointPayFail();
    }
}
