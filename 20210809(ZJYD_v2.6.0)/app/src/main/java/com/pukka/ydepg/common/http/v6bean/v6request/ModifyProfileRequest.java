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

import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.google.gson.annotations.SerializedName;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ModifyProfileRequest
 * @Package com.pukka.ydepg.common.http.v6bean.v6request
 * @date 2017/10/27 16:19
 */
public class ModifyProfileRequest {

  @SerializedName("profile")
  private Profile profile;

  public ModifyProfileRequest(Profile profile) {
    this.profile = profile;
  }

  public Profile getProfile(){
    return profile;
  }

}
