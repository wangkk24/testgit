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
package com.pukka.ydepg.common.http.bean.request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: DSVQuerySubscription
 * @Package com.pukka.ydepg.common.http.bean.request
 * @date 2017/09/15 10:56
 */
public class DSVQuerySubscription {

  @SerializedName("profileID")
  private String profileID;
  @SerializedName("fromDate")
  private String fromDate;
  @SerializedName("toDate")
  private String toDate;
  @SerializedName("count")
  private String count;
  @SerializedName("offset")
  private String offset;
  @SerializedName("productType")
  private String productType;
  @SerializedName("deviceID")
  private String deviceID;
  @SerializedName("isMain")
  private String isMain;
  @SerializedName("subscriptionKey")
  private String subscriptionKey;
  @SerializedName("sortType")
  private String sortType;
  @SerializedName("queryType")
  private int queryType;
  @SerializedName("extensionFields")
  private List<NamedParameter> extensionFields;

  public String getProfileID() {
    return profileID;
  }

  public void setProfileID(String profileID) {
    this.profileID = profileID;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public String getCount() {
    return count;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public String getOffset() {
    return offset;
  }

  public void setOffset(String offset) {
    this.offset = offset;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getDeviceID() {
    return deviceID;
  }

  public void setDeviceID(String deviceID) {
    this.deviceID = deviceID;
  }

  public String getIsMain() {
    return isMain;
  }

  public void setIsMain(String isMain) {
    this.isMain = isMain;
  }

  public String getSubscriptionKey() {
    return subscriptionKey;
  }

  public void setSubscriptionKey(String subscriptionKey) {
    this.subscriptionKey = subscriptionKey;
  }

  public String getSortType() {
    return sortType;
  }

  public void setSortType(String sortType) {
    this.sortType = sortType;
  }

    public int getQueryType() {
    return queryType;
  }

    public void setQueryType(int queryType) {
    this.queryType = queryType;
  }

  public List<NamedParameter> getExtensionFields() {
    return extensionFields;
  }

  public void setExtensionFields(List<NamedParameter> extensionFields) {
    this.extensionFields = extensionFields;
  }
}
