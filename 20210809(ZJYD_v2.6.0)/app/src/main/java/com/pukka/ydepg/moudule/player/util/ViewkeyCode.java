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
package com.pukka.ydepg.moudule.player.util;

/**
 * Viewkeycode
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ViewkeyCode
 * @Package com.pukka.ydepg.moudule.player.util
 * @date 2018/01/11 12:39
 */
public interface ViewkeyCode {
  /**
   * 继续播放
   */
  int VIEW_KEY_CONTINUE_PLAY=0x12;
  /**
   * 重新播放
   */
  int VIEW_KEY_REPLAY=0x13;
  /**
   * 详情
   */
  int VIEW_KEY_DETAIL_PLAY=0x14;
  /**
   * 返回电视看点
   */
  int VIEW_KEY_BACK_TV_PLAY=0x15;
  /**
   * 退出
   */
  int VIEW_KEY_EXIT=0x16;

  /**
   * 下一集
   */
  int NEXT_PLAY = 11112;
}
