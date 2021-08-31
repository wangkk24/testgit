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
package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * 支付信息
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PaymentInfo
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2018/01/24 16:15
 */
public class PaymentInfo {
  /**
   * 促销码编号，当用户使用促销码订购时才有效。
   */
  @SerializedName("promoCodeID")
  private String promoCodeID;

  /**
   * 如果通过手机支付，传入支付的手机号。
   */
  @SerializedName("mobilePhone")
  private String mobilePhone;

  /**
   * 如果通过手机支付，输入支付验证码
   */
  @SerializedName("verifyCode")
  private String verifyCode;

  /**
   * 产品试算价格
   */
  @SerializedName("discountFee")
  private String discountFee;

  /**
   * 账户的信用额度是否可以超额
   */
  @SerializedName("canExceedQuota")
  private String canExceedQuota;

  /**
   * 该支付交易的唯一流水号，由终端生成并传递到平台
   */
  @SerializedName("merchantTranID")
  private String merchantTranID;

  /**
   * 终实际扣费的价格，产品的试算价格减去Voucher面额
   */
  @SerializedName("finalFee")
  private String finalFee;

  public String getPromoCodeID() {
    return promoCodeID;
  }

  public void setPromoCodeID(String promoCodeID) {
    this.promoCodeID = promoCodeID;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getVerifyCode() {
    return verifyCode;
  }

  public void setVerifyCode(String verifyCode) {
    this.verifyCode = verifyCode;
  }

  public String getDiscountFee() {
    return discountFee;
  }

  public void setDiscountFee(String discountFee) {
    this.discountFee = discountFee;
  }

  public String getCanExceedQuota() {
    return canExceedQuota;
  }

  public void setCanExceedQuota(String canExceedQuota) {
    this.canExceedQuota = canExceedQuota;
  }

  public String getMerchantTranID() {
    return merchantTranID;
  }

  public void setMerchantTranID(String merchantTranID) {
    this.merchantTranID = merchantTranID;
  }

  public String getFinalFee() {
    return finalFee;
  }

  public void setFinalFee(String finalFee) {
    this.finalFee = finalFee;
  }
}
