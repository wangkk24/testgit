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

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: DeleteLockResponse
 * @Package com.pukka.ydepg.common.http.v6bean.v6response
 * @date 2017/11/02 11:13
 */
public class DeleteLockResponse extends BaseResponse{
  @SerializedName("version")
  private String version;
  @SerializedName("preVersion")
  private String preVersion;
  @SerializedName("extensionFields")
  private NamedParameter[] extensionFields;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getPreVersion() {
    return preVersion;
  }

  public void setPreVersion(String preVersion) {
    this.preVersion = preVersion;
  }

  public NamedParameter[] getExtensionFields() {
    return extensionFields;
  }

  public void setExtensionFields(NamedParameter[] extensionFields) {
    this.extensionFields = extensionFields;
  }
}
