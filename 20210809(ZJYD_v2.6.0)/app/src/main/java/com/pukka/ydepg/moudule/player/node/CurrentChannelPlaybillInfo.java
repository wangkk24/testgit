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
package com.pukka.ydepg.moudule.player.node;

/**
 * 当前频道当前节目单,基础实体bean
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: CurrentChannelPlaybillInfo
 * @Package com.pukka.ydepg.moudule.player.node
 * @date 2018/01/16 16:19
 */
public class CurrentChannelPlaybillInfo {
  /**
   * 节目单id
   */
  private String playbillId;
  /**
   * 频道id
   */
  private String channelId;
  /**
   * 频道号
   */
  private String channelNo;
  /**
   * 频道名称
   */
  private String channelName;
  /**
   * 频道节目单名称
   */
  private String channelProgramName;
  /**
   * 媒体ID
   */
  private String channelMediaId;

  /**
   * 节目单剩余时间
   */
  private long updateTime;

  /**
   * 刷新请求数据时间
   */
  private long refreshDataTime;

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public String getChannelNo() {
    return channelNo;
  }

  public void setChannelNo(String channelNo) {
    this.channelNo = channelNo;
  }

  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  public String getChannelProgramName() {
    return channelProgramName;
  }

  public void setChannelProgramName(String channelProgramName) {
    this.channelProgramName = channelProgramName;
  }

  public String getChannelMediaId() {
    return channelMediaId;
  }

  public void setChannelMediaId(String channelMediaId) {
    this.channelMediaId = channelMediaId;
  }

  public long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(long updateTime) {
    this.updateTime = updateTime;
  }

  public long getRefreshDataTime() {
    return refreshDataTime;
  }

  public void setRefreshDataTime(long refreshDataTime) {
    this.refreshDataTime = refreshDataTime;
  }

  public String getPlaybillId() {
    return playbillId;
  }

  public void setPlaybillId(String playbillId) {
    this.playbillId = playbillId;
  }
}