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
package com.pukka.ydepg.event;

/**
 * 关闭TVGUIDE页消息事件
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVGuideEvent
 * @Package com.pukka.ydepg.event
 * @date 2018/01/26 15:59
 */
public class TVGuideEvent {
  /**
   * true表示展示popwindow;
   * false表示销毁popwindow
   */
  private boolean isShow;

  public TVGuideEvent(boolean isShow){
    this.isShow=isShow;
  }

  public boolean isShow(){
    return this.isShow;
  }
}
