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
package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: QueryCustomizeConfig
 * @Package com.pukka.ydepg.common.http.v6bean.v6node
 * @date 2017/09/11 17:59
 */
public class QueryCustomizeConfig {
  @SerializedName("key")
  private String key;
  @SerializedName("queryType")
  private String queryType;
  @SerializedName("isFuzzyQuery")
  private int isFuzzyQuery;
  @SerializedName("deviceModel")
  private String deviceModel;
  @SerializedName("language")
  private String language;
  @SerializedName("standard")
  private String standard;
  @SerializedName("extensionFields")
  private NamedParameter[] extensionFields;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getQueryType() {
    return queryType;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }

  public int getIsFuzzyQuery() {
    return isFuzzyQuery;
  }

  public void setIsFuzzyQuery(int isFuzzyQuery) {
    this.isFuzzyQuery = isFuzzyQuery;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getStandard() {
    return standard;
  }

  public void setStandard(String standard) {
    this.standard = standard;
  }

  public NamedParameter[] getExtensionFields() {
    return extensionFields;
  }

  public void setExtensionFields(NamedParameter[] extensionFields) {
    this.extensionFields = extensionFields;
  }
}
