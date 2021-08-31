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

import android.graphics.Bitmap;

/**
 * 媒体播放器提供的接口
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: IMediaPlayer
 * @Package com.pukka.ydepg.inf
 * @date 2018/02/07 10:12
 */
public interface IMediaPlayer {

  /**
   * 播放视频
   * @param url 播放地址
   * @param bookmark 书签
   */
  void startPlay(String url, long bookmark);

  /**
   * 播放前贴广告和视频
   * @param url 播放地址
   * @param advertUrl 广告地址，为空时走普通播放流程
   * @param bookmark  书签
   */
  void startPlay(String url,String advertUrl, long bookmark);

  /**
   * 播放视频
   * @param url 播放地址
   */
  void startPlay(String url);

  /**
   * 重新播放
   */
  void rePlay();

  /**
   * 恢复播放
   */
  void resumePlay();

  /**
   * 暂停播放,便于下次恢复
   */
  void pausePlay();

  /**
   * 播放暂停控制 Toggle
   */
  void playerOrPause();

  /**
   * 释放播放器
   */
  void releasePlayer();

  /**
   * 快速释放,重置mediaPlay
   */
  void fastReleasePrepare();

  /**
   * 快速起播
   * @param playUrl 播放地址
   */
  void fastPlayVideo(String playUrl);

  /**
   * 是否正在播放状态:play状态,不是pause状态
   */
  boolean isPlaying();

  /**
   * 设置播放器封面图
   * @param bitmap 传入要设置在播放器上面的封面图
   */
  void setArtwork(Bitmap bitmap);

  /**
   * 获取当前播放的进度
   * @return
   */
  long getCurrentPosition();

  /**
   * 获取视频总时长
   * @return
   */
  long getDuration();

  /**
   * 进度条设置进度
   */
  void seekTo(long progress);

  /**
   * 快进
   */
  void fastForward();

  /**
   * 快进,前进位置
   * @param forwardPosition
   */
  void fastForward(long forwardPosition);

  /**
   * 倒带
   */
  void rewind();

  /**
   * 倒带,倒带位置
   * @param rewindPosition
   */
  void rewind(long rewindPosition);

  /**
   * 设置是否可以自动播放
   * @param shouldAutoPlay
   */
  void setShouldAutoPlay(boolean shouldAutoPlay);


  /**
   * 设置是否可以自动播放
   * @param time
   *
   */
  void dragProgress(int time);


  /**
   * 重新启动定时更新进度
   *
   */
  void restartUpdateProgress();

    /**
     * 设置直播时移时长
     * @param duration
     */

  void setIptvlength(long duration);

  void restartTSTVurl();

  void resetMediaPlayer();
}