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
package com.pukka.ydepg;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

import com.pukka.ydepg.inf.IMediaPlayer;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.util.PlayUtil;
import com.pukka.ydepg.view.PlayControlView;

/**
 * 媒体播放器抽象基类
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: AbstractMediaPlayer
 * @Package com.pukka.ydepg
 * @date 2018/02/07 10:50
 */
public abstract class AbstractMediaPlayer implements IMediaPlayer {

    /**
     * 播放器回调Listener
     */
    IPlayListener mPlayListener;

    /**
     * 用于控制栏进度更新
     */
    IPlayListener mUpdateProgressListener;

    /**
     * 是否自动播放标志
     */
    boolean isAutoPlay;

    /**
     * 播放控制栏
     */
    private PlayControlView mPlayControlView;


    /**
     * 直播时移进度
     */
    protected long mTSTVLength;

    /**
     * 当前是否播放的广告
     */
    protected boolean isCurrentPlayAdvert;

    /*
     *用于新版详情页，大屏还是小屏
     * 0小屏，1大屏
     */
    protected int windosState = 0;

    public void setWindosState(int windosState) {
        this.windosState = windosState;
    }

    public int getWindosState() {
        return windosState;
    }

    /**
   * 当前是否在播放广告
   * @return true 播放广告
   */
  public boolean isCurrentPlayAdvert() {
        return isCurrentPlayAdvert;
    }

  /**
   * 切换广告到内容
   */
  public void switchAdvertToContentPlayer() {

    }

    protected int videoType = PlayUtil.VideoType.VOD;

    public int setVideoType() {
        return videoType;
    }

    /**
     * 设置播放器状态回调
     *
     * @param listener listener
     */
    public final void setPlayListener(IPlayListener listener) {
        this.mPlayListener = listener;
    }

    /**
     * 设置播放器进度Delayed
     *
     * @param listener listener
     */
    public final void setOnUpdateProgressListener(IPlayListener listener) {
        this.mUpdateProgressListener = listener;
    }

    /**
     * 当前有媒体流
     *
     * @param playState 播放状态
     */
    final boolean hasMedia(int playState) {
        return playState != IPlayState.PLAY_STATE_NOMEDIA;
    }

    /**
     * 设置是否自动播放
     *
     * @param shouldAutoPlay true自动播放;false不自动播放
     */
    @Override
    public final void setShouldAutoPlay(boolean shouldAutoPlay) {
        this.isAutoPlay = shouldAutoPlay;
    }

    /**
     * 设置控制栏View
     *
     * @param controlView controlView
     */
    public void setPlayControlView(PlayControlView controlView) {
        this.mPlayControlView = controlView;
    }

    /**
     * 前进
     */
    @Override
    public void fastForward() {
        if (hasForwardRewind()) {
            mPlayControlView.fastForward();
        }
    }

    /**
     * 前进
     *
     * @param forwardPosition 设置任意前进位置
     */
    @Override
    public void fastForward(long forwardPosition) {
        if (hasForwardRewind()) {
            mPlayControlView.fastForward(forwardPosition);
        }
    }

    /**
     * 后退
     */
    @Override
    public void rewind() {
        if (hasForwardRewind()) {
            mPlayControlView.rewind();
        }
    }

    /**
     * 后退
     *
     * @param rewindPosition 设置任意后退位置
     */
    @Override
    public void rewind(long rewindPosition) {
        if (hasForwardRewind()) {
            mPlayControlView.rewind(rewindPosition);
        }
    }

    @Override
    public void dragProgress(int time) {
        mPlayControlView.dragProgress(time);
    }

    @Override
    public void restartUpdateProgress() {
        mPlayControlView.restartUpdateProgress();
    }

    /**
     * 是否可以快进快退? true:可以快进快退;false:不可以快进快退;
     */
    private boolean hasForwardRewind() {
        //需求只要有总时长就可以快进快退,暂停状态也可以快进快退
        return null != mPlayControlView && mPlayControlView.getDuration() > 0;//&& isPlaying()&&getDuration()>0;
    }

    /**
     * 初始化播放器
     *
     * @param context context
     */
    abstract void initPlayer(Context context);

    /**
     * 设置surfaceView
     *
     * @param view view
     */
    public abstract void setSurface(View view);

    public abstract void setZOrderOnTop(boolean isOnTop);

    /**
     * 设置视频自适应模式
     *
     * @param resizeMode resizeMode
     */
    public abstract void setResizeMode(String resizeMode);

    /**
     * 彻底销毁释放播放器相关资源
     */
    public abstract void onDestoryPlay();

    /**
     * 判断播放器是否在暂停状态
     *
     * @return
     */

    public abstract boolean isPause();

    public abstract void setIsWindow(boolean isLittle);

    //是否是新版详情页
    public abstract void setIsNewDetail(boolean isNewDetail);

    public abstract void setVideoType(int videoType);

    public abstract void reInitPlayHandler();

    public abstract void setSurfaceViewSize(float width, float height);

    /**
     * 判断时移频道是否在暂停
     *
     * @return
     */
    public abstract boolean isTSTVPause();

    @Override
    public void restartTSTVurl() {
        mPlayControlView.restartTSTVurl(1);
    }

    public abstract void setSpeed(float speed);

    public abstract boolean canSetSpeed(float speed);
    public abstract void setNullPlayer();
}