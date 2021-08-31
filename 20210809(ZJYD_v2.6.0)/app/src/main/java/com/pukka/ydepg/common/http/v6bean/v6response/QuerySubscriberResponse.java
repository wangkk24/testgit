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

import com.pukka.ydepg.common.http.v6bean.v6node.Subscriber;


/**
 * QuerySubscriberResponse
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: QuerySubscriberResponse
 * @Package com.pukka.ydepg.common.http.v6bean.v6response
 * @date 2018/02/13 10:04
 */
public class QuerySubscriberResponse extends BaseResponse {

  /**
   * 订户信息
   */
  private Subscriber subscriber;

  public Subscriber getSubscriber() {
    return subscriber;
  }

  public void setSubscriber(Subscriber subscriber) {
    this.subscriber = subscriber;
  }
}
