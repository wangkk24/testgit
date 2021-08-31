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
package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: QueryProfileRequest
 * @Package com.pukka.ydepg.common.http.v6bean.v6request
 * @date 2017/10/26 16:41
 */
public class QueryProfileRequest {
  @SerializedName("type")
  private String type;
  @SerializedName("condition")
  private String[] condition;
  @SerializedName("count")
  private String count;
  @SerializedName("offset")
  private String offset;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String[] getCondition() {
    return condition;
  }

  public void setCondition(String[] condition) {
    this.condition = condition;
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
}
