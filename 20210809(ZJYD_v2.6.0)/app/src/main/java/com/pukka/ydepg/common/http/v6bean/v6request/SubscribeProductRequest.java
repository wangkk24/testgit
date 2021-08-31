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

import com.pukka.ydepg.common.http.v6bean.v6node.Subscribe;
import com.google.gson.annotations.SerializedName;

/**
 * 产品订购
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: SubscribeProductRequest
 * @Package com.pukka.ydepg.common.http.v6bean.v6request
 * @date 2018/01/24 16:11
 */
public class SubscribeProductRequest {
  /**
   * 产品订购请求
   */
  @SerializedName("subscribe")
  private Subscribe subscribe;
  /**
   订购的验证类型。
   取值包括：
   1 ：主profile密码订购
   3 ：短信验证码订购
   */
  @SerializedName("type")
  private String type;

  public interface TYPE{
    String PROFILE_PWD="1";
    String SMS_CODE="3";
  }

  /**
   * 验证码
   */
  @SerializedName("correlator")
  private String correlator;

  public Subscribe getSubscribe() {
    return subscribe;
  }

  public void setSubscribe(Subscribe subscribe) {
    this.subscribe = subscribe;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCorrelator() {
    return correlator;
  }

  public void setCorrelator(String correlator) {
    this.correlator = correlator;
  }
}