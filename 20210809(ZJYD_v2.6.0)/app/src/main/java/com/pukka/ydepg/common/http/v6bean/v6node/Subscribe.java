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
import java.util.List;

/**
 * Subscribe产品订购请求
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: Subscribe
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2018/01/24 16:13
 */
public class Subscribe {
  /**
   订购类型。
   取值包括：
   0 ：订购
   1 ：预约订购(预留枚举值，不使用)
   */
  @SerializedName("subscribeType")
  private String subscribeType;

  public interface SubscribeType{
    String SUBSCRIBE="0";
    String REMIND_SUBSCRIBE="1";
  }
  /**
   * 待订购的产品ID
   */
  @SerializedName("productID")
  private String productID;
  /**
   如果订购按次产品或者包周期限次产品，需要传入待订购的定价对象，
   定价对象可以是内容、媒资、内容或媒资的业务特性，目前只支持内容。
   */
  @SerializedName("priceObjects")
  private List<PriceObject> priceObjects;
  /**
   * 支付信息
   */
  @SerializedName("payment")
  private PaymentInfo payment;

  /**
   * 订购包周期产品，是否支持自动续订。
   * 取值包括：
   * 0 ：不续订
   * 1 ：续订
   */
  @SerializedName("isAutoExtend")
  private String isAutoExtend;

  public interface IsAutoExtend{
    /**
     * 不续订
     */
    String NO_RENEW="0";
    /**
     * 续订
     */
    String RENEW="1";
  }

  /**
   * 如果待订购的产品是独享产品，产品的订购关系将绑定到此逻辑设备上，
   * 如果不指定，默认绑定到当前登录的逻辑设备上
   */
  @SerializedName("deviceID")
  private String deviceID;

  /**
   * 订购关系的生效时间，取值为距离1970年1月1号的毫秒数，
   * 如果不指定，VSP根据产品策略自动计算生效时间。
   */
  @SerializedName("effectiveTime")
  private String effectiveTime;

  /**
   * 付费账号ID
   */
  @SerializedName("paidUserID")
  private String paidUserID;

  public String getSubscribeType() {
    return subscribeType;
  }

  public void setSubscribeType(String subscribeType) {
    this.subscribeType = subscribeType;
  }

  public String getProductID() {
    return productID;
  }

  public void setProductID(String productID) {
    this.productID = productID;
  }

  public List<PriceObject> getPriceObjects() {
    return priceObjects;
  }

  public void setPriceObjects(List<PriceObject> priceObjects) {
    this.priceObjects = priceObjects;
  }

  public PaymentInfo getPayment() {
    return payment;
  }

  public void setPayment(PaymentInfo payment) {
    this.payment = payment;
  }

  public String getIsAutoExtend() {
    return isAutoExtend;
  }

  public void setIsAutoExtend(String isAutoExtend) {
    this.isAutoExtend = isAutoExtend;
  }

  public String getDeviceID() {
    return deviceID;
  }

  public void setDeviceID(String deviceID) {
    this.deviceID = deviceID;
  }

  public String getEffectiveTime() {
    return effectiveTime;
  }

  public void setEffectiveTime(String effectiveTime) {
    this.effectiveTime = effectiveTime;
  }

  public String getPaidUserID() {
    return paidUserID;
  }

  public void setPaidUserID(String paidUserID) {
    this.paidUserID = paidUserID;
  }
}
