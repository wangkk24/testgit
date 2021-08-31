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
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.errorcode.ErrorCode;
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.bean.request.OrderProductRequest;
import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6request.SendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SubscribeProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.UpdateUserRegInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.UpdateUserRegInfoResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayContract;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * 支付presenter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PayPresenter
 * @Package com.pukka.ydepg.moudule.mytv.presenter
 * @date 2018/01/23 15:59
 */
public class PayPresenter extends PayContract.Presenter {

  private static final String TAG="PayPresenter";

  /**
   * 返回产品信息
   *
   * @param productJson
   */
  @Override
  public void generateProductInfo(String productJson) {
    if (isViewAttached()) {
      getBaseView().onResultProductInfo(JsonParse.json2Object(productJson, Product.class));
    }
  }

  /**
   * 返回键盘数据
   */
  @Override
  public void generateKeyboard() {
    List<String> keyboardList = new ArrayList<>();
    for (int i = 1; i < 10; i++) {
      keyboardList.add(String.valueOf(i));
    }
    if (isViewAttached()) {
      getBaseView().onResultKeyBoardValue(keyboardList);
    }
  }

  /**
   * 倒计时
   *
   * @param totalTime 倒计时开始时间
   */
  @Override
  public void countDown(int totalTime) {
    Observable.interval(1, TimeUnit.SECONDS)
        .take(totalTime)
        .map(new Function<Long, Long>() {
          @Override public Long apply(@NonNull Long takeValue) throws Exception {
            return totalTime - takeValue;
          }
        })
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override public void accept(Disposable disposable) throws Exception {
            if (isViewAttached()) {
              getBaseView().onCountDownStart();
            }
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ResourceObserver<Long>() {
          @Override public void onComplete() {
            if (isViewAttached()) {
              getBaseView().onCountDownFinish();
            }
          }
          @Override public void onError(Throwable e) {
          }
          @Override public void onNext(Long value) {
            if (isViewAttached()) {
              getBaseView().onCountDownProgressUpdate(value);
            }
          }
        });
  }


  /**
   * 发送短信验证码
   *
   * @param request
   */
  @Override
  public void sendSMS(SendSmsRequest request, Context context) {
    HttpApi.getInstance().getService().sendSMS(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 +HttpConstant.SENDSMS, request)
        .compose(compose(getBaseView().bindToLife()))
        .subscribe(new RxCallBack<BaseResponse>(context) {
          @Override
          public void onSuccess(BaseResponse response) {
            if(null!=response && null!=response.getResult()){
              String returnCode=response.getResult().getRetCode();
              String returnMsg=response.getResult().getRetMsg();
              if(!TextUtils.isEmpty(returnCode) && !returnCode.equals(Result.RETCODE_OK)){
                SuperLog.error(TAG,"returnCode:"+returnCode+",returnMsg:"+returnMsg);
                if(returnCode.equals("157021051")){
                  //获取短信验证码次数达到上限。
                  EpgToast.showToast(OTTApplication.getContext(),
                      OTTApplication.getContext().getString(
                          R.string.sms_code_upper_limit));
                }else{
                  //发送短信失败。
                  EpgToast.showToast(OTTApplication.getContext(),
                      OTTApplication.getContext().getString(
                          R.string.sms_code_senderror));
                }
              }
            }
          }
          @Override public void onFail(@NonNull Throwable e) {
            SuperLog.error(TAG,"sendSMS error");
          }
        });
  }

  /**
   * 付款
   * @param request
   */
  @Override
  public void payment(SubscribeProductRequest request, Context context) {
      HttpApi.getInstance().getService().subscribe(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3 +HttpConstant.SUBSCRIBE_PRODUCT,request)
        .compose(compose(getBaseView().bindToLife()))
        .subscribe(new RxCallBack<SubscribeProductResponse>(HttpConstant.SUBSCRIBE_PRODUCT,context) {
          @Override public void onSuccess(SubscribeProductResponse response) {
            if(null!=response && null!=response.getResult() && isViewAttached()){
              String returnCode=response.getResult().getRetCode();
              String returnMsg=response.getResult().getRetMsg();
              if(!TextUtils.isEmpty(returnCode)){
                if(returnCode.equals(Result.RETCODE_OK)){
                  getBaseView().onPaymentSucc(response);
                }else{
                  SuperLog.error(TAG,"returnCode:"+returnCode+",returnMsg:"+returnMsg);
                  //添加判断是否是互斥产品包的逻辑
                    if (returnCode.equals(Result.RETCODE_MUTEX)){
                        String mutexStr = SessionService.getInstance().getSession().getTerminalConfigurationOrderProductMutexInfo();
                        if (null != mutexStr && mutexStr.length()>0){
                            EpgToast.showLongToast(OTTApplication.getContext(), mutexStr);
                        }else{
                            EpgToast.showLongToast(OTTApplication.getContext(), context.getResources().getString(R.string.big_small_product_mutex));
                        }
                        getBaseView().onPaymentFailed();
                        return;
                    }
                  EpgToast.showLongToast(OTTApplication.getContext(),
                      ErrorCode.findError(HttpConstant.SUBSCRIBE_PRODUCT,returnCode).getMessage());
                  getBaseView().onPaymentFailed();
                }
              }else{
                  EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.order_payment_failed));
                  getBaseView().onPaymentFailed();
              }
            }else{
                EpgToast.showToast(OTTApplication.getContext(),OTTApplication.getContext().getString(R.string.order_payment_failed));
                getBaseView().onPaymentFailed();
            }
          }

          @Override public void onFail(@NonNull Throwable e) {
            SuperLog.error(TAG,"payment error");
            EpgToast.showToast(OTTApplication.getContext(), OTTApplication.getContext().getString(R.string.order_payment_failed));
            if(isViewAttached()){
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
                                    //验证码的判断
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
                    EpgToast.showToast(OTTApplication.getContext(),
                            OTTApplication.getContext().getString(R.string.order_payment_failed));
                    SuperLog.error(TAG,"payment error");
                    if(isViewAttached()){
                        getBaseView().onPaymentFailed();
                    }
                }
            });
    }

    /**
   * 是否支持自动续订
   *
   * @param product 产品
   */
  public String isAutoExtend(Product product) {
    if (product == null) {
      return "0";
    }
    //产品类型 > 0：包周期; 1：按次
    //包周期需要自动续订......
    return product.getIsAutoExtend();
  }
}
