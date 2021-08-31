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
import com.pukka.ydepg.common.http.HttpApi;
import com.pukka.ydepg.common.http.v6bean.v6request.QueryUniInfoRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.QuerySubscriberResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.node.Configuration;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.util.RxCallBack;
import com.pukka.ydepg.moudule.mytv.presenter.contract.PayQrCodeLinkContract;
import com.pukka.ydepg.moudule.mytv.utils.QRCodeGenerator;

import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PayQrCodeLinkPresenter
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PayQrCodeLinkPresenter
 * @Package com.pukka.ydepg.moudule.mytv.presenter
 * @date 2018/02/12 18:04
 */
public class PayQrCodeLinkPresenter extends PayQrCodeLinkContract.Presenter {

  private static final String TAG="PayQrCodeLinkPresenter";

  /**
   * 生成统一支付连接二维码
   * @param defaultQrCodeUrl defaultQrCodeUrl
   * @param userId userId
   * @param productName productName
   */
  @Override
  public void generateQrCode(String defaultQrCodeUrl, String userId, String productName) {
    SuperLog.info2SD(TAG,"generateQrCode");
    Session session = SessionService.getInstance().getSession();
    String qrCodeUrl = session.getTerminalConfigurationValue("epg_bill_qrcode_url");
    if (TextUtils.isEmpty(qrCodeUrl)) {
      qrCodeUrl = defaultQrCodeUrl;
    }
    String deviceId=session.getDeviceId();
    long timestamp = System.currentTimeMillis();
    String orderID = deviceId + timestamp;
    String keyContent = userId + productName + orderID + "4" + timestamp + "ZJMobile!15112017@OTT";
    String key = sha256(keyContent);
    String qrcodeContent = qrCodeUrl + "?USERID=" + userId + "&PROD=" + productName
        + "&ORDID=" + orderID + "&TIME=" + timestamp + "&KEY=" + key + "&PLAT=4";
    if(isViewAttached()){
      int width= OTTApplication.getContext().getResources()
          .getDimensionPixelSize(R.dimen.paylink_qrcode_imageview_width);
      getBaseView().onResultQRCode(QRCodeGenerator.genQrCode(qrcodeContent,width,width));
    }
  }

  /**
   * 查询订户信息
   * @param request request
   */
  @Override public void querySubscriberInfo(QueryUniInfoRequest request, Context context) {
    SuperLog.info2SD(TAG,"querySubscriberInfo");
    HttpApi.getInstance().getService().querySubscriberInfo(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
        + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.QUERY_SUBSCRIBE_INFO,request)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .subscribe(new RxCallBack<QuerySubscriberResponse>(HttpConstant.QUERY_SUBSCRIBE_INFO,context) {
      @Override public void onSuccess(@NonNull QuerySubscriberResponse response) {
        if(isViewAttached() && null!=response && null!=response.getSubscriber()){
          getBaseView().onQuerySubscriberInfoSucc(response);
        }
      }
      @Override public void onFail(@NonNull Throwable e) {
        SuperLog.error(TAG,"onError:"+e.getMessage());
        if(isViewAttached()){
          getBaseView().onQuerySubscriberInfoError();
        }
      }
    });
  }

  private String sha256(final String plainText) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(plainText.getBytes("UTF-8"));
      byte byteBuffer[] = messageDigest.digest();

      StringBuilder strHexString = new StringBuilder();
      for (byte aByteBuffer : byteBuffer) {
        String hex = Integer.toHexString(0xff & aByteBuffer);
        if (hex.length() == 1) {
          strHexString.append('0');
        }
        strHexString.append(hex);
      }
      return strHexString.toString();
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      SuperLog.error(TAG,e.getMessage());
    }
    return null;
  }
}
