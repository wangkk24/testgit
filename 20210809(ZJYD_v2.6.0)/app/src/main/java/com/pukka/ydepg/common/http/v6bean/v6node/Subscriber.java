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
 * 订户信息
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: Subscriber
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2018/02/13 10:05
 */
public class Subscriber {

  /**
   * 订户编号
   */
  @SerializedName("subscriberId")
  private String subscriberId;

  /**
   * 订户键值
   */
  @SerializedName("subscriberSn")
  private String subscriberSn;

  /**
   * 订户名称
   */
  @SerializedName("subscriberName")
  private String subscriberName;

  /**
   * 订户归属的子网运营商ID
   */
  @SerializedName("subnetId")
  private String subnetId;

  /**
   * BOSS编号，表示由该BOSS开的此订户
   */
  @SerializedName("bossId")
  private String bossId;

  /**
   * 订户的付费类型。
   * 取值包括：
   * AFTERPAID: 后付费用户
   * PREPAID: 预付费用户
   */
  @SerializedName("payType")
  private String payType;

  /**
   * 表示用户是否同意平台采集自己的行为数据。
   * 取值包括：
   * OPTIN: opt in
   * OPTOUT: opt out
   */
  @SerializedName("opt")
  private String opt;

  /**
   * 订户的联系方式
   */
  @SerializedName("subscriberContract")
  private SubscriberContract subscriberContract;

  /**
   * 透传的扩展字段
   */
  @SerializedName("customFields")
  private List<NamedParameter> customFields;

  public String getSubscriberId() {
    return subscriberId;
  }

  public void setSubscriberId(String subscriberId) {
    this.subscriberId = subscriberId;
  }

  public String getSubscriberSn() {
    return subscriberSn;
  }

  public void setSubscriberSn(String subscriberSn) {
    this.subscriberSn = subscriberSn;
  }

  public String getSubscriberName() {
    return subscriberName;
  }

  public void setSubscriberName(String subscriberName) {
    this.subscriberName = subscriberName;
  }

  public String getSubnetId() {
    return subnetId;
  }

  public void setSubnetId(String subnetId) {
    this.subnetId = subnetId;
  }

  public String getBossId() {
    return bossId;
  }

  public void setBossId(String bossId) {
    this.bossId = bossId;
  }

  public String getPayType() {
    return payType;
  }

  public void setPayType(String payType) {
    this.payType = payType;
  }

  public String getOpt() {
    return opt;
  }

  public void setOpt(String opt) {
    this.opt = opt;
  }

  public SubscriberContract getSubscriberContract() {
    return subscriberContract;
  }

  public void setSubscriberContract(SubscriberContract subscriberContract) {
    this.subscriberContract = subscriberContract;
  }

  public List<NamedParameter> getCustomFields() {
    return customFields;
  }

  public void setCustomFields(List<NamedParameter> customFields) {
    this.customFields = customFields;
  }
}