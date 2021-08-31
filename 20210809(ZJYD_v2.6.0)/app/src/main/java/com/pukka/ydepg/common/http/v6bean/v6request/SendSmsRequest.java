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
package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

/**
 * 发送短信验证码
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: SendSmsRequest
 * @Package com.pukka.ydepg.common.http.v6bean.v6request
 * @date 2018/01/24 09:43
 */
public class SendSmsRequest {
  /**
   * profile的loginName；
   * 如果是自注册场景，是注册手机号。
   */
  @SerializedName("loginName")
  private String loginName;

  /**
   * 短信发送的目标手机号;
   * 如果没传，则使用loginName对应的profile的手机号。
   */
  @SerializedName("destMobilePhone")
  private String destMobilePhone;

  /**
   * 认证类型
   * 取值包括：
   * 1：短信验证码
   * 2：email注册（预留）
   * 3：短信验证码订购
   */
  @SerializedName("msgType")
  private String msgType;

  public interface MsgType{
    /**
     * 短信验证码
     */
    String SMS_CODE="1";
    /**
     * email注册
     */
    String EMAIL_CODE="2";
    /**
     * 短信验证码订购
     */
    String SMS_SUBSCRIBE_CODE="3";

    /**
     * 修改手机号
     */
    String SMS_RESET_PHONE_CODE="4";

    //儿童版 家长设置密码 发送手机验证码
    String SMS_PHONE_CODE="99";
  }

  /**
   * 当前终端页面显示内容的语种属性。
   * 如果语种是订户共享的且语种还未设置，VSP将终端上报的locale作为订户语种。
   * 采用统一为ISO639-1缩写，如en。
   */
  @SerializedName("lang")
  private String lang;
  /**
   * 子网ID
   */
  @SerializedName("subnetId")
  private String subnetId;
  /**
   * 扩展信息
   */
  @SerializedName("extensionFields")
  private NamedParameter extensionFields;

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getDestMobilePhone() {
    return destMobilePhone;
  }

  public void setDestMobilePhone(String destMobilePhone) {
    this.destMobilePhone = destMobilePhone;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getSubnetId() {
    return subnetId;
  }

  public void setSubnetId(String subnetId) {
    this.subnetId = subnetId;
  }

  public NamedParameter getExtensionFields() {
    return extensionFields;
  }

  public void setExtensionFields(NamedParameter extensionFields) {
    this.extensionFields = extensionFields;
  }
}