/*
 *Copyright (C) 2017 广州易杰科技, Inc.
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
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: DsvProduct
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2017/10/13 16:39
 */
public class DsvProduct {
  @SerializedName("subscribeType")
  private String subscribeType;
  @SerializedName("productID")
  private String productID;
  @SerializedName("priceObject")
  private PriceObject priceObject;
  @SerializedName("payment")
  private Object payment;
  @SerializedName("isAutoExtend")
  private String isAutoExtend;
  @SerializedName("deviceID")
  private String deviceID;
  @SerializedName("reliantMediaID")
  private String reliantMediaID;
  @SerializedName("effectiveTime")
  private long effectiveTime;
  @SerializedName("approvalVersions")
  private String[] approvalVersions;

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

  public PriceObject getPriceObject() {
    return priceObject;
  }

  public void setPriceObject(PriceObject priceObject) {
    this.priceObject = priceObject;
  }

  public Object getPayment() {
    return payment;
  }

  public void setPayment(Object payment) {
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

  public String getReliantMediaID() {
    return reliantMediaID;
  }

  public void setReliantMediaID(String reliantMediaID) {
    this.reliantMediaID = reliantMediaID;
  }

  public long getEffectiveTime() {
    return effectiveTime;
  }

  public void setEffectiveTime(long effectiveTime) {
    this.effectiveTime = effectiveTime;
  }

  public String[] getApprovalVersions() {
    return approvalVersions;
  }

  public void setApprovalVersions(String[] approvalVersions) {
    this.approvalVersions = approvalVersions;
  }
}
