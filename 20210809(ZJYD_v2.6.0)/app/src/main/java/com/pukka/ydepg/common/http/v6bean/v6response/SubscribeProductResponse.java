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
package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.Subscription;
import com.google.gson.annotations.SerializedName;

/**
 * 产品订购信息响应体
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: SubscribeProductResponse
 * @Package com.pukka.ydepg.common.http.v6bean.v6response
 * @date 2018/01/24 16:29
 */
public class SubscribeProductResponse extends BaseResponse{

  /**
   * 第三方系统返回的信息
   */
  @SerializedName("originInfo")
  private NamedParameter[] originInfo;

  /**
   * 如果订购成功或者重复订购，返回新生成或者重复的订购关系。
   * 如果订购时，首先到第三方平台（如VSS、BOSS等）进行订购，
   * 再由第三方平台同步到EVP，这种场景下订购成功时，不返回订购关系。
   */
  @SerializedName("subscription")
  private Subscription subscription;

  /**
   * 当前信用额度周期内剩余的信用额度
   */
  @SerializedName("remanentCredit")
  private String remanentCredit;

  /**
   * 当前信用额度周期的剩余时间，单位：分钟
   *
   * 如果User Profile的信用帐期额度不足、订户的信用帐期额度不足、
   * 订户的周期信用金额额度不足或订户的周期信用次数额度不足，返回账户的信用额度信息
   */
  @SerializedName("remanentCreditDuration")
  private String remanentCreditDuration;

  /**
   * 单个信用额度周期的总信用额度
   *
   * 如果User Profile的信用帐期额度不足、订户的信用帐期额度不足、
   * 订户的周期信用金额额度不足或订户的周期信用次数额度不足，返回账户的信用额度信息
   */
  @SerializedName("totalCredit")
  private String totalCredit;

  /**
   * 信用额度周期，表示信用额度持续可用时间，单位：分钟
   *
   * 如果User Profile的信用帐期额度不足、订户的信用帐期额度不足、
   * 订户的周期信用金额额度不足或订户的周期信用次数额度不足，返回账户的信用额度信息
   */
  @SerializedName("totalCreditDuration")
  private String totalCreditDuration;

  /**
   * 如果产品依赖检查失败，返回依赖的产品信息
   */
  @SerializedName("reliantProducts")
  private Product[] reliantProducts;

  /**
   * 扩展信息
   */
  @SerializedName("extensionFields")
  private NamedParameter[] extensionFields;

  public NamedParameter[] getOriginInfo() {
    return originInfo;
  }

  public void setOriginInfo(NamedParameter[] originInfo) {
    this.originInfo = originInfo;
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public void setSubscription(Subscription subscription) {
    this.subscription = subscription;
  }

  public String getRemanentCredit() {
    return remanentCredit;
  }

  public void setRemanentCredit(String remanentCredit) {
    this.remanentCredit = remanentCredit;
  }

  public String getRemanentCreditDuration() {
    return remanentCreditDuration;
  }

  public void setRemanentCreditDuration(String remanentCreditDuration) {
    this.remanentCreditDuration = remanentCreditDuration;
  }

  public String getTotalCredit() {
    return totalCredit;
  }

  public void setTotalCredit(String totalCredit) {
    this.totalCredit = totalCredit;
  }

  public String getTotalCreditDuration() {
    return totalCreditDuration;
  }

  public void setTotalCreditDuration(String totalCreditDuration) {
    this.totalCreditDuration = totalCreditDuration;
  }

  public Product[] getReliantProducts() {
    return reliantProducts;
  }

  public void setReliantProducts(Product[] reliantProducts) {
    this.reliantProducts = reliantProducts;
  }

  public NamedParameter[] getExtensionFields() {
    return extensionFields;
  }

  public void setExtensionFields(NamedParameter[] extensionFields) {
    this.extensionFields = extensionFields;
  }
}
