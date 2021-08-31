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
import android.graphics.Bitmap;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.launcher.bean.request.BaseRequest;
import com.pukka.ydepg.moudule.livetv.presenter.base.BasePresenter;
import com.pukka.ydepg.moudule.livetv.presenter.base.BaseView;

/**
 * PayQrCodeLinkContract
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PayQrCodeLinkContract
 * @Package com.pukka.ydepg.moudule.mytv.presenter.contract
 * @date 2018/02/12 18:03
 */
public interface PayQrCodeLinkContract {

  interface View extends BaseView {
    /**
     * 返回QRCODE的bitmap对象
     * @param resultBitmap resultBitmap
     */
    void onResultQRCode(Bitmap resultBitmap);

    /**
     * 查询订户信息成功
     * @param response response
     */
    void onQuerySubscriberInfoSucc(QuerySubscriberResponse response);

    /**
     * 查询订户信息失败
     */
    void onQuerySubscriberInfoError();
  }

  abstract class Presenter extends BasePresenter<View> {

    /**
     * 生成统一支付连接二维码
     * @param defaultQrCodeUrl defaultQrCodeUrl
     * @param userId userId
     * @param productName productName
     */
    public abstract void generateQrCode(String defaultQrCodeUrl, String userId, String productName);

    /**
     * 查询订户信息
     * @param request request
     */
    public abstract void querySubscriberInfo(QueryUniInfoRequest request, Context context);

  }
}