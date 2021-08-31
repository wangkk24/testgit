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
package com.pukka.ydepg.moudule.mytv.presenter.contract;

import android.content.Context;

import com.pukka.ydepg.common.http.bean.response.OrderProductResponse;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6request.SendSmsRequest;
import com.pukka.ydepg.common.http.v6bean.v6request.SubscribeProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;
import java.util.List;

/**
 * 支付contract
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PayContract
 * @Package com.pukka.ydepg.moudule.mytv.presenter.contract
 * @date 2018/01/23 15:59
 */
public interface PayContract {

  interface View extends BaseView{
    /**
     * 返回产品信息
     * @param product
     */
    void onResultProductInfo(Product product);

    /**
     * 返回键盘数据集合
     * @param keyboardList
     */
    void onResultKeyBoardValue(List<String> keyboardList);

    /**
     * 倒计时开始
     */
    void onCountDownStart();

    /**
     * 倒计时结束
     */
    void onCountDownFinish();

    /**
     * 倒计时数字更新
     * @param progress 当前倒计时进度
     */
    void onCountDownProgressUpdate(Long progress);

    /**
     * 付款成功
     */
    void onPaymentSucc(SubscribeProductResponse response);

      /**
       * 付款成功
       */
      void onPaymentSucc(OrderProductResponse response);

    /**
     * 付款失败
     */
    void onPaymentFailed();

      /**
       *
       */
    void updateUserRegInfoSucess();

      /**
       *
       */
    void updateUserRegInfoFail();
  }

  abstract class Presenter extends BasePresenter<View>{
    /**
     * 生成product
     * @param productJson
     */
    public abstract void generateProductInfo(String productJson);

    /**
     * 生成支付键盘数字
     */
    public abstract void generateKeyboard();

    /**
     * 倒计时
     * @param totalTime 倒计时时间
     */
    public abstract void countDown(int totalTime);

    /**
     * 发送短信验证码
     * @param request
     */
    public abstract void sendSMS(SendSmsRequest request, Context context);

    /**
     * 付款
     * @param request
     */
    public abstract void payment(SubscribeProductRequest request, Context context);

  }

}