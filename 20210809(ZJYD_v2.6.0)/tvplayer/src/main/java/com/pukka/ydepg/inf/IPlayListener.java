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
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: IPlayListener
 * @Package com.pukka.ydepg.inf
 * @date 2018/02/07 10:47
 */
public interface IPlayListener {

    /**
     * 回调当前播放器状态
     *
     * @param playbackState
     */
    void onPlayState(int playbackState);

    /**
     * 准备
     */
    void onPrepared(int Videotype);

    /**
     * 播放器释放
     */
    void onRelease();

    /**
     * 播放失败,重置界面中某些状态
     */
    void onPlayError(String msg, int errorCode, int playerType);

    /**
     * 播放结束
     */
    void onPlayCompleted();

    /**
     * 对用户不可见
     */
    void onDetached(long time);

    /**
     * 对用户可见
     */
    void onAttached();

    /**
     * H5界面播放失败重试
     */
    void onTryPlayForH5();

    /**
     * 广告播放第一帧
     */
    void onAdVideoEnd();
}
