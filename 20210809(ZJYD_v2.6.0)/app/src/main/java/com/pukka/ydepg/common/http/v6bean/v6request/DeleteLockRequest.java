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
import java.util.List;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: DeleteLockRequest
 * @Package com.pukka.ydepg.common.http.v6bean.v6request
 * @date 2017/11/02 10:55
 */
public class DeleteLockRequest {
  @SerializedName("profileID")
  private String profileID;
  @SerializedName("lockTypes")
  private List<String> lockTypes;
  @SerializedName("itemIDs")
  private List<String> itemIDs;
  @SerializedName("deleteType")
  private int deleteType;

  public String getProfileID() {
    return profileID;
  }

  public void setProfileID(String profileID) {
    this.profileID = profileID;
  }

  public List<String> getLockTypes() {
    return lockTypes;
  }

  public void setLockTypes(List<String> lockTypes) {
    this.lockTypes = lockTypes;
  }

  public List<String> getItemIDs() {
    return itemIDs;
  }

  public void setItemIDs(List<String> itemIDs) {
    this.itemIDs = itemIDs;
  }

  public int getDeleteType() {
    return deleteType;
  }

  public void setDeleteType(int deleteType) {
    this.deleteType = deleteType;
  }
}