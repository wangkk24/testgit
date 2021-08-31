package com.pukka.ydepg.launcher.mvp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.request.DSVQuerySubscription;
import com.pukka.ydepg.common.http.v6bean.v6node.BookmarkItem;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.http.v6bean.v6request.BatchSendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ChangeUserOrderingSwitchRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CheckClientVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.CheckVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.ModifyUserAttrRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryBookmarkRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryFavoriteRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUserAttrsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUserOrderingSwitchRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SendVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateSubscriberRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateUserRegInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.BatchSendSmsResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.ModifyUserAttrResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUniPayInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUserAttrsResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UpdateSubscriberResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UpdateUserRegInfoResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VerifiedClientCodeResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VerifiedCodeResponse;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.bean.UserInfo;
import com.pukka.ydepg.launcher.mvp.contact.MyContact;
import com.pukka.ydepg.launcher.mvp.contact.SwitchDialogContact;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.moudule.featured.bean.VODMining;
import com.pukka.ydepg.moudule.featured.bean.VodBean;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的页面业务类
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.mvp.presenter.MyPresenter.java
 * @date: 2017-12-16 17:04
 * @version: V1.0 描述当前版本功能
 */
public class SwitchDialogPresenter extends BasePresenter<SwitchDialogContact.SwitchView>{
    private static final String TAG = SwitchDialogPresenter.class.getSimpleName();

    public SwitchDialogPresenter() {}

    @SuppressLint("CheckResult")
    public void queryUserAttrs() {
        QueryUserAttrsRequest request = new QueryUserAttrsRequest();
        UserInfo userInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        request.setUserId(userInfo.getUserId());
        List<String> attrNames = new ArrayList<>();
        attrNames.add(com.pukka.ydepg.launcher.Constant.PARENTS_PWD);
        request.setAttrNames(attrNames);
        String interfaceName = HttpConstant.QUERY_USER_ATTRS;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        if (null == mView) {
            return;
        }
        HttpApi.getInstance().getService().queryUserAttrs(url, request).compose(onCompose(mView.bindToLife())).subscribe(queryUserAttrsResponse -> {
            int returnCode = queryUserAttrsResponse.getReturnCode();
            if (returnCode == Integer.parseInt(Result.RETCODE_OK_TWO)) {
                List<QueryUserAttrsResponse.UserAttr> attrs = queryUserAttrsResponse.getAttrs();
                if (mView != null) {
                    if (null != attrs && attrs.size() > 0 && attrs.get(0).getAttrName().equalsIgnoreCase(com.pukka.ydepg.launcher.Constant.PARENTS_PWD)
                            && !TextUtils.isEmpty(attrs.get(0).getAttrValue())){
                        mView.queryUserAttrsSuccess(attrs.get(0).getAttrValue());
                    }else{
                        mView.queryUserAttrsFail();
                    }
                }
            }else{
                if (mView != null) {
                    mView.queryUserAttrsFail();
                }
            }
        }, throwable -> {
            if (mView != null) {
                mView.queryUserAttrsFail();
            }
        });
    }

    /**
     * 发送短信验证码
     *
     * @param request
     */
    public void sendSMS(SendSmsRequest request, Context context) {
        HttpApi.getInstance().getService().sendSMS(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 + HttpConstant.SENDSMS, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<BaseResponse>(HttpConstant.SENDSMS,context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (null != response && null != response.getResult()) {
                            String returnCode = response.getResult().getRetCode();
                            String returnMsg = response.getResult().getRetMsg();
                            if (!TextUtils.isEmpty(returnCode) && !returnCode.equals(Result.RETCODE_OK)) {
                                SuperLog.error(TAG, "returnCode:" + returnCode + ",returnMsg:" + returnMsg);
                                if (returnCode.equals("157021051")) {
                                    //获取短信验证码次数达到上限。
                                    EpgToast.showToast(OTTApplication.getContext(),
                                            OTTApplication.getContext().getString(
                                                    R.string.sms_code_upper_limit));
                                } else {
                                    //发送短信失败。
                                    EpgToast.showToast(OTTApplication.getContext(),
                                            OTTApplication.getContext().getString(
                                                    R.string.sms_code_senderror));
                                }
                            }else if (!TextUtils.isEmpty(returnCode) && returnCode.equals(Result.RETCODE_OK)){
                                EpgToast.showToast(OTTApplication.getContext(),
                                        OTTApplication.getContext().getString(
                                                R.string.sms_code_sendsuccess));
                            }
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG, "sendSMS error");
                    }
                });
    }

    public void updateUserRegInfo(UpdateUserRegInfoRequest request, Context context){
        HttpApi.getInstance().getService().updateUserRegInfo(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.UPDATE_USER_REGINFO, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<UpdateUserRegInfoResponse>(HttpConstant.UPDATE_USER_REGINFO, context) {
                    @Override
                    public void onSuccess(@NonNull UpdateUserRegInfoResponse response) {
                        if(null!=response && null!=response.getResult()){
                            String returnCode=response.getResult().getRetCode();
                            String resultMsg=response.getResult().getRetMsg();
                            if(!TextUtils.isEmpty(returnCode) && returnCode.equals(Result.RETCODE_OK)){
                                if(null!=mView){
                                    mView.checkVerifiedCodeSuccess();
                                }
                            }else{
                                SuperLog.error(TAG,"returnCode:"+returnCode+",resultMsg:"+resultMsg);
                                mView.checkVerifiedCodeFail();
                            }
                        }
                    }
                    @Override public void onFail(@NonNull Throwable e) {
                        mView.checkVerifiedCodeFail();
                    }
                });
    }


    //VRS发送验证码接口
    public void sendVerifiedCode(String mobileNum, Context context){

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
        HttpApi.getInstance().getService().sendVerifiedCode(url,request).compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<VerifiedCodeResponse>(HttpConstant.SEND_VERIFIED_CODE,context) {
            @Override
            public void onSuccess(VerifiedCodeResponse verifiedCodeResponse) {

                if (null != verifiedCodeResponse && !TextUtils.isEmpty(verifiedCodeResponse.getCode())){
                    if (verifiedCodeResponse.getCode().equals("0")){
                        //发送短信成功
                        EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.sms_code_sendsuccess));
                    }else {
                        //发送短信失败。
                        EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.sms_code_senderror));
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, "sendSMS error");
            }
        });
    }

    //VRS验证验证码接口
    public void checkVerifiedCode(String verifiedCode, String number, Context context){

        CheckVerifiedCodeRequest request = new CheckVerifiedCodeRequest();
        request.setMessageID(PaymentUtils.generateMessageID());
        request.setVerifiedCode(verifiedCode);

        request.setUserId(number);
        request.setUserType("1");
        request.setSceneType("2");

        String interfaceName = HttpConstant.CHECK_VERIFIED_CODE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().checkVerifiedCode(url,request).compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<VerifiedCodeResponse>(HttpConstant.CHECK_VERIFIED_CODE,context) {
            @Override
            public void onSuccess(VerifiedCodeResponse verifiedCodeResponse) {
                if (null != verifiedCodeResponse && !TextUtils.isEmpty(verifiedCodeResponse.getCode())){
                    if (verifiedCodeResponse.getCode().equals("0")){
                        //验证码验证成功
                        if (null != mView){
                            mView.checkVerifiedCodeSuccess();
                        }
                    }else{
                        //验证码验证失败
                        if (null != mView){
                            mView.checkVerifiedCodeFail();
                        }
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                mView.checkVerifiedCodeFail();
                SuperLog.info2SD(TAG,e.getMessage());
            }
        });
    }

    //VRS发送验证码接口
    public void sendBatchSendSms(String mobileNum, Context context){

        BatchSendSmsRequest request = new BatchSendSmsRequest();
        request.setFrom("10086");
        request.setTo(mobileNum);
        request.setBody("您正在重置浙江移动宽带电视家长密码，验证码{checkCode}，5分钟内有效，请勿泄露或者转发他人。");

        String interfaceName = HttpConstant.BATCH_SEND_SMS;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().sendBatchSendSms(url,request).compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<BatchSendSmsResponse>(HttpConstant.SEND_VERIFIED_CODE,context) {
            @Override
            public void onSuccess(BatchSendSmsResponse verifiedCodeResponse) {
                if (null != verifiedCodeResponse){

                }
                /*if (null != verifiedCodeResponse && !TextUtils.isEmpty(verifiedCodeResponse.getCode())){
                    if (verifiedCodeResponse.getCode().equals("0")){
                        //发送短信成功
                        EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.sms_code_sendsuccess));
                    }else {
                        //发送短信失败。
                        EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.sms_code_senderror));
                    }
                }*/
            }

            @Override
            public void onFail(Throwable e) {
                SuperLog.error(TAG, "sendSMS error");
            }
        });
    }

    //VRS验证验证码接口
    public void checkClientVerifiedCode(String verifiedCode, String number, Context context){

        CheckClientVerifiedCodeRequest request = new CheckClientVerifiedCodeRequest();
        request.setMessageID(PaymentUtils.generateMessageID());
        request.setVerifiedCode(verifiedCode);
        request.setAccount(number);
        request.setVerifiedCodeType("4");

        String interfaceName = HttpConstant.CHECK_VERIFIED_CLIENT_CODE;
        String url = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP + interfaceName;
        HttpApi.getInstance().getService().checkClientVerifiedCode(url,request).compose(onCompose(mView.bindToLife())).subscribe(new RxCallBack<VerifiedClientCodeResponse>(HttpConstant.CHECK_VERIFIED_CODE,context) {
            @Override
            public void onSuccess(VerifiedClientCodeResponse verifiedCodeResponse) {
                if (null != verifiedCodeResponse && !TextUtils.isEmpty(verifiedCodeResponse.getCode())){
                    if (verifiedCodeResponse.getCode().equals("0")){
                        //验证码验证成功
                        if (null != mView){
                            mView.checkVerifiedCodeSuccess();
                        }
                    }else{
                        //验证码验证失败
                        if (null != mView){
                            mView.checkVerifiedCodeFail();
                        }
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                mView.checkVerifiedCodeFail();
                SuperLog.info2SD(TAG,e.getMessage());
            }
        });
    }

    /**
     * 查询统一支付开通情况
     * @param request request
     */
  public void queryUniPayInfo(QueryUniInfoRequest request, Context context) {
        HttpApi.getInstance().getService().queryUniPayInfo(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QUERY_UNIPAY_INFO, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<QueryUniPayInfoResponse>(HttpConstant.QUERY_UNIPAY_INFO,context) {
                    @Override
                    public void onSuccess(@NonNull QueryUniPayInfoResponse response) {
                        if(null!=response && null!=response.getResult()){
                            String returnCode=response.getResult().getRetCode();
                            String resultMsg=response.getResult().getRetMsg();
                            if(!TextUtils.isEmpty(returnCode) && returnCode.equals(Result.RETCODE_OK)){
                                if(null!=mView){
                                    mView.queryUniPayInfoSuccess(response);
                                }
                            }else{
                                SuperLog.error(TAG,"returnCode:"+returnCode+",resultMsg:"+resultMsg);
                                if (mView != null) {
                                    mView.queryUniPayInfoFail();
                                }
                            }
                        }
                    }
                    @Override public void onFail(@NonNull Throwable e) {
                        if (mView != null) {
                            mView.queryUniPayInfoFail();
                        }
                    }
                });
    }

    /**
     * 自定义用户属性变更接口
     */
    public void modifyUserAttr(ModifyUserAttrRequest request, Context context) {
        HttpApi.getInstance().getService().modifyUserAttr(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP+HttpConstant.MODIFY_USER_ATTR, request)
                .compose(onCompose(mView.bindToLife()))
                .subscribe(new RxCallBack<ModifyUserAttrResponse>(HttpConstant.MODIFY_USER_ATTR, context) {
                    @Override
                    public void onSuccess(@NonNull ModifyUserAttrResponse response) {
                        if(null!=response && null!=response.getReturnCode()){
                            Integer returnCode = response.getReturnCode();
                            String returnMessage = response.getReturnMessage();
                            if(returnCode == Integer.parseInt(Result.RETCODE_OK_TWO)){
                                if(null!=mView){
                                    mView.modifyUserAttrSuccess();
                                }
                            }else{
                                SuperLog.error(TAG,"returnCode:"+returnCode+",resultMsg:"+returnMessage);
                                mView.modifyUserAttrFail();
                            }
                        }
                    }
                    @Override public void onFail(@NonNull Throwable e) {
                        mView.modifyUserAttrFail();
                    }
                });
    }

    /**
     * 查询订户信息
     * @param request request
     */
    public void querySubscriberInfo(QueryUniInfoRequest request, Context context) {
        SuperLog.info2SD(TAG,"querySubscriberInfo");
        HttpApi.getInstance().getService().querySubscriberInfo(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QUERY_SUBSCRIBE_INFO,request)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxCallBack<QuerySubscriberResponse>(HttpConstant.QUERY_SUBSCRIBE_INFO,context) {
                    @Override public void onSuccess(@NonNull QuerySubscriberResponse response) {
                        if(null != mView && null!=response && null!=response.getSubscriber()){
                            mView.querySubscriberInfoSucc(response);
                        }
                    }
                    @Override public void onFail(@NonNull Throwable e) {
                        SuperLog.error(TAG,"onError:"+e.getMessage());
                        if(null != mView){
                            mView.querySubscriberInfoError();
                        }
                    }
                });
    }

}