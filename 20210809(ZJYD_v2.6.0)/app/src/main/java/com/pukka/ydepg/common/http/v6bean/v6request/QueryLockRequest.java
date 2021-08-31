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
 * @Title: QueryLockRequest
 * @Package com.pukka.ydepg.common.http.v6bean.v6request
 * @date 2017/10/31 16:02
 */
public class QueryLockRequest {
  @SerializedName("profileID")
  private String profileID;
  @SerializedName("isShare")
  private String isShare;
  @SerializedName("lockTypes")
  private String[] lockTypes;
  @SerializedName("count")
  private String count;
  @SerializedName("offset")
  private String offset;
  @SerializedName("sortType")
  private String sortType;

  public String getProfileID() {
    return profileID;
  }

  public void setProfileID(String profileID) {
    this.profileID = profileID;
  }

  public String getIsShare() {
    return isShare;
  }

  public void setIsShare(String isShare) {
    this.isShare = isShare;
  }

  public String[] getLockTypes() {
    return lockTypes;
  }

  public void setLockTypes(String[] lockTypes) {
    this.lockTypes = lockTypes;
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

  public String getSortType() {
    return sortType;
  }

  public void setSortType(String sortType) {
    this.sortType = sortType;
  }

  public interface IsShare
  {
    /**
     * 0: Subscriber lock (all family members to take effect)
     */
    String SUBSCRIBER_LOCK = "0";
    /**
     * 1: exclusive lock (only Admin Profile in force)
     */
    String ADMIN_LOCK = "1";
    /**
     * -1: all lock
     */
    String ALL_LOCK = "-1";
  }
  public interface SortType
  {
    /**
     * Ascending order by creation time
     */
    String CREATE_TIME_ASC = "CREATE_TIME:ASC";
    /**
     * Descending order by creation time
     */
    String CREATE_TIME_DESC = "CREATE_TIME:DESC";
    /**
     * Arranged in ascending channel number
     */
    String CHAN_NO_ASC = "CHAN_NO:ASC";
    /**
     * Sorted by channel number descending
     */
    String CHAN_NO_DESC = "CHAN_NO:DESC";
  }

}
