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
import com.pukka.ydepg.common.http.v6bean.v6node.Subscriber;

import java.util.List;

public class QueryUserAttrsResponse extends BaseResponse {

  /**
   * VRS 查询组播开关状态
   */
  /**
   * •0：成功
   * •其它：失败
   * */
  @SerializedName("returnCode")
  private Integer returnCode;

  //失败时返回错误信息
  @SerializedName("returnMessage")
  private Integer returnMessage;

  //成功后返回自定义用户属性
  @SerializedName("attrs")
  private List<UserAttr> attrs;

  public Integer getReturnMessage() {
    return returnMessage;
  }

  public void setReturnMessage(Integer returnMessage) {
    this.returnMessage = returnMessage;
  }

  public Integer getReturnCode() {
    return returnCode;
  }

  public void setReturnCode(Integer returnCode) {
    this.returnCode = returnCode;
  }

  public List<UserAttr> getAttrs() {
    return attrs;
  }

  public void setAttrs(List<UserAttr> attrs) {
    this.attrs = attrs;
  }

  public static class UserAttr {

    @SerializedName("attrName")
    private String attrName;

    @SerializedName("attrValue")
    private String attrValue;

    public String getAttrName() {
      return attrName;
    }

    public void setAttrName(String attrName) {
      this.attrName = attrName;
    }

    public String getAttrValue() {
      return attrValue;
    }

    public void setAttrValue(String attrValue) {
      this.attrValue = attrValue;
    }
  }

}
