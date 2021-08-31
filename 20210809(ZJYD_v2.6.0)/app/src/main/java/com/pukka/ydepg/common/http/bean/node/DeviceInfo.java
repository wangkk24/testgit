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
package com.pukka.ydepg.common.http.bean.node;

import com.pukka.ydepg.common.http.v6bean.v6node.Device;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: DeviceInfo
 * @Package com.pukka.ydepg.common.http.bean.node
 * @date 2017/09/21 17:09
 */
public class DeviceInfo {

  @SerializedName("device")
  private Device device;
  @SerializedName("extensionFields")
  private List<NamedParameter> extensionFields;

  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }

  public List<NamedParameter> getExtensionFields() {
    return extensionFields;
  }

  public void setExtensionFields(List<NamedParameter> extensionFields) {
    this.extensionFields = extensionFields;
  }
  //public class Device{
  //  @SerializedName("ID")
  //  private String ID;
  //  @SerializedName("name")
  //  private String name;
  //  @SerializedName("deviceType")
  //  private String deviceType;
  //  @SerializedName("deviceModel")
  //  private String deviceModel;
  //  @SerializedName("status")
  //  private int status;
  //  @SerializedName("onlineState")
  //  private int onlineState;
  //  @SerializedName("physicalDeviceID")
  //  private String physicalDeviceID;
  //  @SerializedName("lastOfflineTime")
  //  private long lastOfflineTime;
  //  @SerializedName("CADeviceInfos")
  //  private CADeviceInfo[] CADeviceInfos;
  //  @SerializedName("channelNamespace")
  //  private String channelNamespace;
  //  @SerializedName("deviceToken")
  //  private String deviceToken;
  //  @SerializedName("terminalVendor")
  //  private String terminalVendor;
  //  @SerializedName("videoCodec")
  //  private String videoCodec;
  //  @SerializedName("definition")
  //  private String definition;
  //  @SerializedName("fps")
  //  private float fps;
  //  @SerializedName("extensionFields")
  //  private NamedParameter[] extensionFields;
  //  @SerializedName("customFields")
  //  private NamedParameter[] customFields;
  //
  //  public class CADeviceInfo{
  //    @SerializedName("CADeviceType")
  //    private String CADeviceType;
  //    @SerializedName("CADeviceID")
  //    private String CADeviceID;
  //    @SerializedName("CADeviceIDSignature")
  //    private String CADeviceIDSignature;
  //    @SerializedName("extensionFields")
  //    private NamedParameter[] extensionFields;
  //
  //    public String getCADeviceType() {
  //      return CADeviceType;
  //    }
  //
  //    public void setCADeviceType(String CADeviceType) {
  //      this.CADeviceType = CADeviceType;
  //    }
  //
  //    public String getCADeviceID() {
  //      return CADeviceID;
  //    }
  //
  //    public void setCADeviceID(String CADeviceID) {
  //      this.CADeviceID = CADeviceID;
  //    }
  //
  //    public String getCADeviceIDSignature() {
  //      return CADeviceIDSignature;
  //    }
  //
  //    public void setCADeviceIDSignature(String CADeviceIDSignature) {
  //      this.CADeviceIDSignature = CADeviceIDSignature;
  //    }
  //
  //    public NamedParameter[] getExtensionFields() {
  //      return extensionFields;
  //    }
  //
  //    public void setExtensionFields(NamedParameter[] extensionFields) {
  //      this.extensionFields = extensionFields;
  //    }
  //  }
  //
  //  public String getID() {
  //    return ID;
  //  }
  //
  //  public void setID(String ID) {
  //    this.ID = ID;
  //  }
  //
  //  public String getName() {
  //    return name;
  //  }
  //
  //  public void setName(String name) {
  //    this.name = name;
  //  }
  //
  //  public String getDeviceType() {
  //    return deviceType;
  //  }
  //
  //  public void setDeviceType(String deviceType) {
  //    this.deviceType = deviceType;
  //  }
  //
  //  public String getDeviceModel() {
  //    return deviceModel;
  //  }
  //
  //  public void setDeviceModel(String deviceModel) {
  //    this.deviceModel = deviceModel;
  //  }
  //
  //  public int getStatus() {
  //    return status;
  //  }
  //
  //  public void setStatus(int status) {
  //    this.status = status;
  //  }
  //
  //  public int getOnlineState() {
  //    return onlineState;
  //  }
  //
  //  public void setOnlineState(int onlineState) {
  //    this.onlineState = onlineState;
  //  }
  //
  //  public String getPhysicalDeviceID() {
  //    return physicalDeviceID;
  //  }
  //
  //  public void setPhysicalDeviceID(String physicalDeviceID) {
  //    this.physicalDeviceID = physicalDeviceID;
  //  }
  //
  //  public long getLastOfflineTime() {
  //    return lastOfflineTime;
  //  }
  //
  //  public void setLastOfflineTime(long lastOfflineTime) {
  //    this.lastOfflineTime = lastOfflineTime;
  //  }
  //
  //  public CADeviceInfo[] getCADeviceInfos() {
  //    return CADeviceInfos;
  //  }
  //
  //  public void setCADeviceInfos(CADeviceInfo[] CADeviceInfos) {
  //    this.CADeviceInfos = CADeviceInfos;
  //  }
  //
  //  public String getChannelNamespace() {
  //    return channelNamespace;
  //  }
  //
  //  public void setChannelNamespace(String channelNamespace) {
  //    this.channelNamespace = channelNamespace;
  //  }
  //
  //  public String getDeviceToken() {
  //    return deviceToken;
  //  }
  //
  //  public void setDeviceToken(String deviceToken) {
  //    this.deviceToken = deviceToken;
  //  }
  //
  //  public String getTerminalVendor() {
  //    return terminalVendor;
  //  }
  //
  //  public void setTerminalVendor(String terminalVendor) {
  //    this.terminalVendor = terminalVendor;
  //  }
  //
  //  public String getVideoCodec() {
  //    return videoCodec;
  //  }
  //
  //  public void setVideoCodec(String videoCodec) {
  //    this.videoCodec = videoCodec;
  //  }
  //
  //  public String getDefinition() {
  //    return definition;
  //  }
  //
  //  public void setDefinition(String definition) {
  //    this.definition = definition;
  //  }
  //
  //  public float getFps() {
  //    return fps;
  //  }
  //
  //  public void setFps(float fps) {
  //    this.fps = fps;
  //  }
  //
  //  public NamedParameter[] getExtensionFields() {
  //    return extensionFields;
  //  }
  //
  //  public void setExtensionFields(NamedParameter[] extensionFields) {
  //    this.extensionFields = extensionFields;
  //  }
  //
  //  public NamedParameter[] getCustomFields() {
  //    return customFields;
  //  }
  //
  //  public void setCustomFields(NamedParameter[] customFields) {
  //    this.customFields = customFields;
  //  }
  //}
}
