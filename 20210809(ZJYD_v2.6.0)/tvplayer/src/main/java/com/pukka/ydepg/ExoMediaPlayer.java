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
import android.graphics.Bitmap;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.pukka.ydepg.bean.PlayerConstant;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.util.EventLogger;
import com.pukka.ydepg.util.PlayUtil;
import com.pukka.ydepg.view.PlayView;

import java.lang.reflect.Method;

/**
 * 基于GOOGLE ExoPlayer播放器的实现
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: ExoMediaPlayer
 * @Package com.pukka.ydepg
 * @date 2018/02/07 09:59
 */
public class ExoMediaPlayer extends AbstractMediaPlayer {

    private static final String TAG = ExoMediaPlayer.class.getSimpleName();

    public static void setIsReleaseSurface(boolean isReleaseSurface) {
        ExoMediaPlayer.isReleaseSurface = isReleaseSurface;
    }

    //只有老人桌面，为true，防止surfaceView没有释放，出现1/4屏幕
    private static boolean isReleaseSurface = false;

    private Context mContext;
    /**
     * 当前正在播放的视频地址
     */
    private String mCurrentPlayUrl;

    /**
     * 初始化Handler,指定handler在主线程创建
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 带宽评估
     */
    private final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    /**
     * 默认音轨跟踪器
     */
    private DefaultTrackSelector mTrackSelector;

    /**
     * 媒体加载数据流
     */
    private DataSource.Factory mMediaDataSourceFactory;

    /**
     * 播放事件回调监听:此处用于监听播放状态的变化
     */
    private PlayerEventListener mEventListener;

    /**
     * 播放事件logger
     */
    private EventLogger mEventLogger;

    /**
     * SimpleExoPlayer渲染组件
     */
    private SimpleExoPlayer mPlayer;

    /**
     * 播放器播放失败标志
     */
    private boolean inErrorState;

    /**
     * 当前播放的位置
     */
    private long mCurrentPosition;

    private PlayerView mSurfaceView;

    private FrameLayout container;

    private long bookmarkValue;

    private int videoType= PlayUtil.VideoType.VOD;

    private String mResizeMode=PlayView.RESIZE_MODE_AUTO;

    public ExoMediaPlayer(Context context) {
        this.mContext = context;
    }

    /**
     * 启动播放器
     *
     * @param url      播放地址
     * @param bookmark 书签
     */
    @Override
    public void startPlay(String url, long bookmark) {
        initPlayer(mContext);
        mCurrentPlayUrl = url;
        bookmarkValue = bookmark;
        if (null == mPlayer) {
            initPlayer(mContext);
            mCurrentPosition = 0;
        }
        MediaSource videoSource = PlayUtil.buildMediaSource( mMediaDataSourceFactory,
                Uri.parse(mCurrentPlayUrl));
        seekTo(bookmarkValue);
        mPlayer.prepare(videoSource, false, false);
        mPlayer.setPlayWhenReady(isAutoPlay);
        inErrorState = false;
    }

    @Override
    public void startPlay(String url, String advertUrl, long bookmark) {
        initPlayer(mContext);
        mCurrentPlayUrl = url;
        bookmarkValue = bookmark;
        if (null == mPlayer) {
            initPlayer(mContext);
            mCurrentPosition = 0;
        }
        MediaSource videoSource = PlayUtil.buildMediaSource( mMediaDataSourceFactory,
                Uri.parse(mCurrentPlayUrl));
        seekTo(bookmarkValue);
        mPlayer.prepare(videoSource, false, false);
        mPlayer.setPlayWhenReady(isAutoPlay);
        inErrorState = false;
    }

    /**
     * 启动播放器
     *
     * @param url 播放地址
     */
    @Override
    public void startPlay(String url) {
        initPlayer(mContext);
        mCurrentPlayUrl = url;
        bookmarkValue = 0;
        if (null == mPlayer) {
            initPlayer(mContext);
            mCurrentPosition = 0;
        }
        final MediaSource videoSource = PlayUtil.buildMediaSource(mMediaDataSourceFactory,
                Uri.parse(mCurrentPlayUrl));
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != mPlayer) {
                    seekTo(mCurrentPosition);
                    mPlayer.prepare(videoSource, false, false);
                    mPlayer.setPlayWhenReady(isAutoPlay);
                    inErrorState = false;
                }
            }
        });
    }

    /**
     * 重新播放
     */
    @Override
    public void rePlay() {
        //fixed: prepare(MediaSource mediaSource,boolean resetPosition,boolean resetState) >> bug
        releasePlayer();
        startPlay(mCurrentPlayUrl);
    }

    /**
     * 恢复播放
     */
    @Override
    public void resumePlay() {
        if (null != mPlayer) {
            mPlayer.setPlayWhenReady(true);
        } else {
            if (!TextUtils.isEmpty(mCurrentPlayUrl)) {
                if (null != mSurfaceView) {
                    mSurfaceView.setPlayer(null);
                }
                //要设置下,视频加载完成准备好之后,自动播放,否则需要手动触发
                isAutoPlay = true;
                startPlay(mCurrentPlayUrl);
            }
        }
    }

    /**
     * 暂停播放
     */
    @Override
    public void pausePlay() {
        if (null != mPlayer) {
            mPlayer.setPlayWhenReady(false);
        }
        mCurrentPosition = Math.max(0, getCurrentPosition());
    }

    /**
     * 播放暂停 Toggle
     */
    @Override
    public void playerOrPause() {
        if (null != mPlayer) {
            mPlayer.setPlayWhenReady(!mPlayer.getPlayWhenReady());
        }
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        //有媒体流同时是播放状态是播放
        return null != mPlayer && mPlayer.getPlayWhenReady();//hasMedia(mPlayer.getPlaybackState()) &&
    }

    /**
     * 设置封面
     *
     * @param bitmap 传入要设置在播放器上面的封面图
     */
    @Override
    public void setArtwork(Bitmap bitmap) { }

    /**
     * 当前播放时长进度
     */
    @Override
    public long getCurrentPosition() {
        return (null != mPlayer) ? mPlayer.getCurrentPosition() : 0;
    }

    /**
     * 视频总进度时长
     */
    @Override
    public long getDuration() {
        return (null != mPlayer) ? mPlayer.getDuration() : 0;
    }

    /**
     * 释放播放器
     */
    @Override
    public void releasePlayer() {
        if (null != mPlayer) {
            mPlayer.release();
            mPlayer.removeListener(mEventListener);
            mPlayer.removeListener(mEventLogger);
            mPlayer.removeAudioDebugListener(mEventLogger);
            mPlayer.removeVideoDebugListener(mEventLogger);
            mPlayer.removeMetadataOutput(mEventLogger);
        }
        if (null != mSurfaceView) {
            mSurfaceView.setPlayer(null);
            Log.d(TAG,"isReleaseSurface : " + isReleaseSurface);
            if(isReleaseSurface)
            {
                //必须要手动释放surfaceview,不然会影响其他播放器
                mSurfaceView.removeAllViews();
            }
            if (null != mPlayListener) {
                mPlayListener.onRelease();
            }
        }
        mCurrentPosition = 0;
        mPlayer = null;
        mTrackSelector = null;
        mEventLogger = null;
        mEventListener = null;
    }

    @Override
    public void fastReleasePrepare() { }

    @Override
    public void fastPlayVideo(String playUrl) { }

    /**
     * 初始化播放器
     *
     * @param context context
     */
    @Override
    void initPlayer(Context context) {
        if(null!=container)
        {
            container.removeAllViews();
            mSurfaceView=new PlayerView(context);
            if(PlayView.RESIZE_MODE_FIT.equals(mResizeMode)){
                mSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            }else{
                mSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            }
            mSurfaceView.setFocusable(false);
            mSurfaceView.setUseController(false);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(mSurfaceView, lp);
            mSurfaceView.setPlayer(mPlayer);
        }
        if (null == mEventListener) {
            mEventListener = new PlayerEventListener();
        }
        if (null == mMediaDataSourceFactory) {
            mMediaDataSourceFactory = PlayUtil.buildDataSourceFactory(context, BANDWIDTH_METER);
        }
        if (null == mTrackSelector) {
            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.
                    Factory(BANDWIDTH_METER);
            mTrackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        }
        if (null == mEventLogger) {
            mEventLogger = new EventLogger(mTrackSelector);
        }
        if (null == mPlayer) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(context, mTrackSelector);
            //添加监听器
            mPlayer.addListener(mEventListener);
            mPlayer.addListener(mEventLogger);
            mPlayer.addMetadataOutput(mEventLogger);
            //调试信息
            mPlayer.addAudioDebugListener(mEventLogger);
            mPlayer.addVideoDebugListener(mEventLogger);
        }
        //初始化新的player对象之后要设置好最新的player
        if (null != mSurfaceView && null == mSurfaceView.getPlayer()) {
            mSurfaceView.setPlayer(mPlayer);
        }
    }

    @Override
    public void setSurface(View view) {
        container = (FrameLayout) view;
    }

    @Override
    public void setZOrderOnTop(boolean isOnTop) {
    }

    /**
     * RESIZE_MODE_FIT 表示通过减少视频的宽度或者高度，来达到想要的视频宽高比。
     * RESIZE_MODE_FIXED_WIDTH 表示宽度是固定的，通过减少或者增加高度的值来实现想要的宽高比。
     * RESIZE_MODE_FIXED_HEIGHT 表示高度是固定的，通过减少或者增加宽度的值来实现想要的宽高比。
     * RESIZE_MODE_FILL 表示不考虑指定的宽高比。
     * RESIZE_MODE_ZOOM 表示通过增加宽度或者高度，来达到想要的视频宽高比。
     * */

    @Override
    public void setResizeMode(String resizeMode) {
        mResizeMode=resizeMode;
    }

    @Override
    public void onDestoryPlay() {
        releasePlayer();
    }

    /**
     * 是不是处在暂停状态
     * @return
     */
    @Override
    public boolean isPause(){
        return null != mPlayer&&!mPlayer.getPlayWhenReady();
    }

    @Override
    public void setIsWindow(boolean isLittle) { }

    @Override
    public void setIsNewDetail(boolean isNewDetail) {

    }

    @Override
    public void setVideoType(int videoType) {
        this.videoType=videoType;
    }

    /**
     * 播放器的事件监听器
     */
    private class PlayerEventListener extends Player.DefaultEventListener {
        /**
         * 播放状态变化回调
         *
         * @param playWhenReady 当前是可以自动播放
         * @param playbackState 当前播放的状态
         */
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);
            Log.i(TAG, "[onPlayerStateChanged]:" + EventLogger.getStateString(playbackState));
            //用于回调控制栏定时更新控制栏
            if (null != mUpdateProgressListener) {
                mUpdateProgressListener.onPlayState(playbackState);
            }
            //播放状态回调监听
            if (null != mPlayListener) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        mPlayListener.onPlayState(IPlayState.PLAY_STATE_BUFFERING);
                        break;
                    case Player.STATE_ENDED:
                        //ExoPlayer播放器释放也会走.......
                        if (playWhenReady) {
                            mPlayListener.onPlayCompleted();
                            mPlayListener.onPlayState(IPlayState.PALY_STETE_END);
                        }
                        break;
                    case Player.STATE_READY:
                        if (getCurrentPosition() == 0) {
                            seekTo(bookmarkValue);
                        }
                        mPlayListener.onPlayState(IPlayState.PLAY_STATE_HASMEDIA);
                        break;
                    case Player.STATE_IDLE:
                        mPlayListener.onPlayState(IPlayState.PLAY_STATE_NOMEDIA);
                        break;
                }
            }
        }

        /**
         * 位置不连续原因如网络,seekTo等
         *
         * @param reason reason
         */
        @Override
        public void onPositionDiscontinuity(int reason) {
            super.onPositionDiscontinuity(reason);
            if (null != mUpdateProgressListener) {
                mUpdateProgressListener.onPlayState(IPlayState.PLAY_STATE_BUFFERING);
            }
            if (inErrorState) {
                //onPlayError回调之后
                mCurrentPosition = Math.max(0, getCurrentPosition());
            }
        }

        /**
         * 播放失败回调
         *
         * @param error exception
         */
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            super.onPlayerError(error);
            RuntimeException runtimeException = null;
            try {
                runtimeException = error.getUnexpectedException();
            } catch (Exception e) {
                //ingore
            }
            boolean hasRetryPlay = !TextUtils.isEmpty(mCurrentPlayUrl)
                    && ((null != runtimeException)
                    && (runtimeException instanceof IllegalStateException));
            if (hasRetryPlay) {
                Log.i(TAG, "An error occurred, you can try to replay in the current position.");
            } else {
                Log.i(TAG, "Can not try again.");
            }
            inErrorState = true;
            if (PlayUtil.isBehindLiveWindow(error)) {
                hasRetryPlay = true;
                startPlay(mCurrentPlayUrl);
            } else {
                if (hasRetryPlay) {
                    //Internal runtime error....
                    mCurrentPosition = Math.max(0, getCurrentPosition());
                    startPlay(mCurrentPlayUrl);
                }
            }
            if (null != mPlayListener && !hasRetryPlay) {
                mPlayListener.onPlayError("播放出错", error.type, PlayerConstant.PlayerType.EXOMEDIAPLAYER);
            }
        }
    }

    /**
     * 视频播放位置进度调整
     *
     * @param progress 进度
     */
    @Override
    public void seekTo(long progress) {
        if (null != mPlayer) {
            mPlayer.seekTo(progress);
        }
    }

    @Override
    public void setIptvlength(long duration)
    {
        this.mTSTVLength=duration;
    }

    @Override
    public void resetMediaPlayer() {

    }

    @Override
    public void reInitPlayHandler() { }

    @Override
    public void setSurfaceViewSize(float width, float height) {

    }

    @Override
    public boolean isTSTVPause()
    {
        return false;
    }

    @Override
    public void setSpeed(float speed) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams playbackParams = new PlaybackParams();
            playbackParams.setSpeed(speed);
            playbackParams.setPitch(mPlayer.getPlaybackParameters().pitch);
            mPlayer.setPlaybackParams(playbackParams);
        }
        else{
            //TODO
            try {
                Method method = SimpleExoPlayer.class.getMethod("setSpeed", float.class);
                method.invoke(mPlayer, speed);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean canSetSpeed(float speed) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        else{
            //TODO
            try {
                Method method = SimpleExoPlayer.class.getMethod("setSpeed", float.class);
                method.invoke(mPlayer, speed);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public void setNullPlayer() {

    }
}