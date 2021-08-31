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
package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.LockItem;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: QueryLockResponse
 * @Package com.pukka.ydepg.common.http.v6bean.v6response
 * @date 2017/10/31 16:29
 */
public class QueryLockResponse extends BaseResponse {
  /**
   * The total number of records
   */
  @SerializedName("total") private String total;

  /**
   * PPersonalized lock messages.
   */
  @SerializedName("locks") private List<LockItem> lockList;

  @SerializedName("version") private String version;

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public List<LockItem> getLockList() {
    return lockList;
  }

  public void setLockList(List<LockItem> lockList) {
    this.lockList = lockList;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override public String toString() {
    return "QueryLockResponse{"
        + "total='"
        + total
        + '\''
        + ", lockList="
        + lockList
        + ", version='"
        + version
        + '\''
        + '}';
  }
}