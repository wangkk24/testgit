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
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.SubscribeProductRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.SubscribeProductResponse;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;

/**
 * UniPayContract
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: UniPayContract
 * @Package com.pukka.ydepg.moudule.mytv.presenter.contract
 * @date 2018/02/13 15:30
 */
public interface UniPayContract {

  interface View extends BaseView{

    /**
     * 支付成功
     */
    void onPaymentSucc(SubscribeProductResponse response);

      /**
       * 支付成功
       */
      void onPaymentSucc(OrderProductResponse response);

    /**
     * 支付失败
     */
    void onPaymentFailed();
  }

  abstract class Presenter extends BasePresenter<View>{

    /**
     * 解析产品包,获取产品包海报
     * @param vodDetail vodDetail
     * @param product product
     */
    public abstract String resolveProductPoster(VODDetail vodDetail,Product product);

    /**
     * 支付
     * @param request SubscribeProductRequest
     */
    public abstract void payment(SubscribeProductRequest request, Context context);

  }
}
