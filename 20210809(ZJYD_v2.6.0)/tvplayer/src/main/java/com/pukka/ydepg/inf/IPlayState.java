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
package com.pukka.ydepg.inf;

/**
 * 播放状态
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: IPlayState
 * @Package com.pukka.ydepg.inf
 * @date 2018/02/07 11:02
 */
public interface IPlayState {
  /**
   * 播放出错或者没有可播放的媒体
   */
  int PLAY_STATE_NOMEDIA=1;

  /**
   * 当前媒体正在缓冲
   */
  int PLAY_STATE_BUFFERING=2;

  /**
   * 当前媒体是可以播放的状态随时可以play和pause
   */
  int PLAY_STATE_HASMEDIA=3;

  /**
   * 当前媒体播放结束
   */
  int PALY_STETE_END=4;
}
