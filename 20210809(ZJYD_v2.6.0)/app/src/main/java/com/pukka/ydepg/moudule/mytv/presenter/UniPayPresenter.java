/*
 *Copyright (C) 2018 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.moudule.mytv.presenter;

import android.content.Context;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.request.OrderProductRequest;
import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.CustomGroup;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.SendVerifiedCodeRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SubscribeProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.VerifiedCodeResponse;
import com.pukka.ydepg.common.http.vss.node.BusiInfo;
import com.pukka.ydepg.common.http.vss.node.RespParam;
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
import com.pukka.ydepg.moudule.mytv.presenter.contract.UniPayContract;
import com.pukka.ydepg.moudule.mytv.utils.PaymentUtils;


import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * UniPayPresenter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: UniPayPresenter
 * @Package com.pukka.ydepg.moudule.mytv.presenter
 * @date 2018/02/13 15:31
 */
public class UniPayPresenter extends UniPayContract.Presenter {

  private static final String TAG = "UniPayPresenter";

    //统一支付一般模式
    public static final String unipayment =              "10001";
    //统一支付童锁模式
    public static final String unipayment_child =        "10002";
    //其他手机号支付
    public static final String phonepay =                "10003";

  /**
   * 解析海报URL
   *
   * @param vodDetail vodDetail
   * @param product product
   */
  @Override public String resolveProductPoster(VODDetail vodDetail, Product product) {
    String posterUrl = "";
//    if ("1".equals(product.getProductType())) {
//      //按次
//      if (null != vodDetail && null != vodDetail.getPicture() && null != vodDetail.getPicture()
//          .getPosters() && vodDetail.getPicture().getPosters().size() > 0) {
//        posterUrl = vodDetail.getPicture().getPosters().get(0);
//      }
//    } else {
      //按周期
      if (null!=product&&product.getPicture() != null
          && product.getPicture().getPosters() != null
          && product.getPicture().getPosters().size() > 0) {
        posterUrl = product.getPicture().getPosters().get(0);
      }
//    }
    return posterUrl;
  }

  /**
   * 支付
   *
   * @param request SubscribeProductRequest
   */
  @Override public void payment(SubscribeProductRequest request, Context context) {
    HttpApi.getInstance().getService().subscribe(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
        + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.SUBSCRIBE_PRODUCT, request)
        .compose(compose(getBaseView().bindToLife()))
        .subscribe(new RxCallBack<SubscribeProductResponse>(HttpConstant.SUBSCRIBE_PRODUCT, context) {
          @Override public void onSuccess(SubscribeProductResponse response) {
            if (null != response && null != response.getResult() && isViewAttached()) {
              String returnCode = response.getResult().getRetCode();
              String returnMsg = response.getResult().getRetMsg();
              if (!TextUtils.isEmpty(returnCode)) {
                if (returnCode.equals(Result.RETCODE_OK)) {
                  if(isViewAttached()){
                    getBaseView().onPaymentSucc(response);
                  }
                } else {
                  SuperLog.error(TAG, "returnCode:" + returnCode + ",returnMsg:" + returnMsg);
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
                        getBaseView().onPaymentFailed();
                        return;
                    }
                  showPaymentErrorMsg(returnCode);
                  if(isViewAttached()){
                    getBaseView().onPaymentFailed();
                  }
                }
              }
            }
          }

          @Override public void onFail(@NonNull Throwable e) {
            SuperLog.error(TAG, "payment error");
            EpgToast.showToast(OTTApplication.getContext(),
                OTTApplication.getContext().getString(R.string.order_payment_failed));
            if (isViewAttached()) {
              getBaseView().onPaymentFailed();
            }
          }
        });
  }


    public void paymentByVSS(OrderProductRequest request, Context context) {

        HttpApi.getInstance().getService().orderProduct(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP +HttpConstant.ORDER_PRODUCT,request)
            .compose(compose(getBaseView().bindToLife()))
            .subscribe(new RxCallBack<OrderProductResponse>(HttpConstant.ORDER_PRODUCT,context) {
                @Override public void onSuccess(OrderProductResponse response) {
                    if(null!=response && null!=response.getResult() && isViewAttached()){
                        String returnCode=response.getResult().getRetCode();
                        String returnMsg=response.getResult().getRetMsg();
                        if(!TextUtils.isEmpty(returnCode)){
                            if(returnCode.equals(Result.RETCODE_OK)||returnCode.equals(Result.RETCODE_OK_TWO)){
                                getBaseView().onPaymentSucc(response);
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
                                    getBaseView().onPaymentFailed();
                                    return;
                                }else if (returnCode.equals(Result.RETCODE_VERIFIED_WRONG)){
                                    EpgToast.showLongToast(OTTApplication.getContext(),
                                            "验证码错误");
                                    getBaseView().onPaymentFailed();
                                    return;
                                }

                                EpgToast.showLongToast(OTTApplication.getContext(),
                                    ErrorCode.findError(HttpConstant.ORDER_PRODUCT,returnCode).getMessage());
                                getBaseView().onPaymentFailed();
                            }
                        }else{
                            EpgToast.showToast(OTTApplication.getContext(),
                                    OTTApplication.getContext().getString(R.string.order_payment_failed));
                            getBaseView().onPaymentFailed();
                        }
                    }else{
                        EpgToast.showToast(OTTApplication.getContext(),
                                OTTApplication.getContext().getString(R.string.order_payment_failed));
                        getBaseView().onPaymentFailed();
                    }
                }

                @Override public void onFail(@NonNull Throwable e) {
                    SuperLog.error(TAG,"payment error");
                    EpgToast.showToast(OTTApplication.getContext(),
                            OTTApplication.getContext().getString(R.string.order_payment_failed));
                    if(isViewAttached()){
                        getBaseView().onPaymentFailed();
                    }
                }
            });
    }

  /**
   * 统一支付错误码,提示消息
   *
   * @param errorMsg errorMsg
   */
  private void showPaymentErrorMsg(String errorMsg) {
    SuperLog.debug(TAG, "订购失败" + errorMsg);
    if (!TextUtils.isEmpty(errorMsg)) {
      String toastMsg = "";
      OTTApplication application = OTTApplication.getContext();
      switch (errorMsg) {
        case "1":
          toastMsg = application.getString(R.string.orrder_error_code_1);
          break;
        case "2":
          toastMsg = application.getString(R.string.orrder_error_code_2);
          break;
        case "3":
          toastMsg = application.getString(R.string.orrder_error_code_3);
          break;
        case "4":
          toastMsg = application.getString(R.string.orrder_error_code_4);
          break;
        case "5":
          toastMsg = application.getString(R.string.orrder_error_code_5);
          break;
        case "100":
          toastMsg = application.getString(R.string.orrder_error_code_100);
          break;
        case "101":
          toastMsg = application.getString(R.string.orrder_error_code_101);
          break;
        case "102":
          toastMsg = application.getString(R.string.orrder_error_code_102);
          break;
        case "103":
          toastMsg = application.getString(R.string.orrder_error_code_103);
          break;
        case "104":
          toastMsg = application.getString(R.string.orrder_error_code_104);
          break;
        case "105":
          toastMsg = application.getString(R.string.orrder_error_code_105);
          break;
        case "106":
          toastMsg = application.getString(R.string.orrder_error_code_106);
          break;
        case "107":
          toastMsg = application.getString(R.string.orrder_error_code_107);
          break;
        case "108":
          toastMsg = application.getString(R.string.orrder_error_code_108);
          break;
        case "109":
          toastMsg = application.getString(R.string.orrder_error_code_109);
          break;
        case "201":
          toastMsg = application.getString(R.string.orrder_error_code_201);
          break;
        case "202":
          toastMsg = application.getString(R.string.orrder_error_code_202);
          break;
        case "203":
          toastMsg = application.getString(R.string.orrder_error_code_203);
          break;
        case "204":
          toastMsg = application.getString(R.string.orrder_error_code_204);
          break;
        case "500":
          toastMsg = application.getString(R.string.orrder_error_code_500);
          break;
        case "501":
          toastMsg = application.getString(R.string.orrder_error_code_501);
          break;
        case "502":
          toastMsg = application.getString(R.string.orrder_error_code_502);
          break;
        case "503":
          toastMsg = application.getString(R.string.orrder_error_code_503);
          break;
        case "504":
          toastMsg = application.getString(R.string.orrder_error_code_504);
          break;
        case "505":
          toastMsg = application.getString(R.string.orrder_error_code_505);
          break;
        case "10030050":
          toastMsg = application.getString(R.string.order_error_code_10030050);
          break;
        default:
          toastMsg = application.getString(R.string.orrder_error_code_unknown);
          break;
      }
      EpgToast.showToast(application, toastMsg);
    }
  }

  /**
   * 是否支持自动续订
   * @param product 产品
   */
  public boolean isAutoExtend(Product product) {
    if(product == null){
      return false;
    }
    //产品类型 > 0：包周期; 1：按次
    //包周期需要自动续订......
    return  "1".equals(product.getIsAutoExtend());
  }



  //查询话费余额
    public void queryResultBal(String billId, Context context ,QueryResultBalCallback callback ,String comeFrom) {
        QueryResultRequest resultRequest = new QueryResultRequest();
        BusiInfo busiInfo = new BusiInfo();
        busiInfo.setBillId(billId);
        BodyofGeneralBossQueryRequest body = new BodyofGeneralBossQueryRequest();
        body.setBusiInfo(busiInfo);
        resultRequest.setBodyofGeneralBossQueryRequest(body);
        resultRequest.setAddressCode("addressCode1");
        resultRequest.setHeadofGeneralBossQueryRequest("");
        resultRequest.setInterfaceName("ESB_CS_QRY_RESULT_BAL_001");
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
                                        callback.QueryResultBalSuccess(param,comeFrom);
                                        return;
                                    }
                                }
                            }
                        }
                        if (null != callback){
                            callback.QueryResultBalFail(comeFrom);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        SuperLog.error(TAG,e);
                        if (null != callback){
                            callback.QueryResultBalFail(comeFrom);
                        }
                    }
                });
    }


    public static int SCENE_TYPE_PHONE = 3;
    public static int SCENE_TYPE_UNI   = 4;
    //新的发送验证码接口
    public void sendVerifiedCode(String mobileNum, Context context, int sceneType, SendVerifiedCodeCallBack callBack){

        SendVerifiedCodeRequest request = new SendVerifiedCodeRequest();
        request.setMessageID(PaymentUtils.generateMessageID());
        request.setMobileNum(mobileNum);

        UserInfo mUserInfo = AuthenticateManager.getInstance().getCurrentUserInfo();
        if (null != mUserInfo)
        {
            request.setUserId(mUserInfo.getUserId());
        }
        request.setUserType(1);
        request.setSceneType(sceneType);

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

    public interface SendVerifiedCodeCallBack{
        void sendVerifiedCodeSuccsee();
        void sendVerifiedCodeFail(String description);
    }

    public interface QueryResultBalCallback{
      void QueryResultBalSuccess(RespParam param,String comeFrom);
      void QueryResultBalFail(String comeFrom);
    }
}