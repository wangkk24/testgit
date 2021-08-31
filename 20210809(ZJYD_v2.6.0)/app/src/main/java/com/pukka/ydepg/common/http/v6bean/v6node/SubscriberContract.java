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

/**
 * 订户的联系方式
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: SubscriberContract
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2018/02/13 10:08
 */
public class SubscriberContract {
  /**
   * 订户手机号
   */
  private String mobilePhone;

  /**
   * 订户Email地址
   */
  private String email;

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
