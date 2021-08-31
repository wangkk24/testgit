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
 * 统一支付成员信息列表
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: UniPayInfo
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2018/02/13 11:58
 */
public class UniPayInfo {
  /**
   * 角色。
   * 取值包括：
   * 1: 户主
   * 2: 成员
   */
  @SerializedName("roleID")
  private String roleID;

  /**
   * 户主或成员手机号码
   */
  @SerializedName("billID")
  private String billID;

  /**
   * 户主或成员姓名
   */
  @SerializedName("name")
  private String name;

  /**
   * 生效时间
   */
  @SerializedName("effTime")
  private String effTime;

  /**
   * 失效时间
   */
  @SerializedName("expTime")
  private String expTime;

  /**
   * 统一支付状态。
   * 取值包括：
   * 1: 统一支付状态
   * 2: 统一支付预变更状态
   * 3: 统一支付成员邀请状态
   */
  @SerializedName("payState")
  private String payState;

  public String getRoleID() {
    return roleID;
  }

  public void setRoleID(String roleID) {
    this.roleID = roleID;
  }

  public String getBillID() {
    return billID;
  }

  public void setBillID(String billID) {
    this.billID = billID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEffTime() {
    return effTime;
  }

  public void setEffTime(String effTime) {
    this.effTime = effTime;
  }

  public String getExpTime() {
    return expTime;
  }

  public void setExpTime(String expTime) {
    this.expTime = expTime;
  }

  public String getPayState() {
    return payState;
  }

  public void setPayState(String payState) {
    this.payState = payState;
  }
}


