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

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.UniPayInfo;
import java.util.List;

/**
 * QueryUniPayInfoResponse
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: QueryUniPayInfoResponse
 * @Package com.pukka.ydepg.common.http.v6bean.v6response
 * @date 2018/02/13 11:57
 */
public class QueryUniPayInfoResponse extends BaseResponse {
  /**
   * 支付类型。
   * 取值包括：
   * 0: 无支付关系
   * 1: 主号
   * 2: 副号
   */
  @SerializedName("payType")
  private String payType;

  /**
   * 统一支付成员信息列表
   */
  @SerializedName("uniPayList")
  private List<UniPayInfo> uniPayList;

  public String getPayType() {
    return payType;
  }

  public void setPayType(String payType) {
    this.payType = payType;
  }

  public List<UniPayInfo> getUniPayList() {
    return uniPayList;
  }

  public void setUniPayList(List<UniPayInfo> uniPayList) {
    this.uniPayList = uniPayList;
  }
}
