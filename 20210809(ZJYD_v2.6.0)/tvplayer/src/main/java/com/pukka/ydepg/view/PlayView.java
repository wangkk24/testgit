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
package com.pukka.ydepg.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pukka.ydepg.AbstractMediaPlayer;
import com.pukka.ydepg.AndroidJMGMediaPlayer;
import com.pukka.ydepg.AndroidMediaPlayer;
import com.pukka.ydepg.CommonUtiltemp;
import com.pukka.ydepg.ExoMediaPlayer;
import com.pukka.ydepg.inf.IMediaPlayer;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.player.R;
import com.pukka.ydepg.util.PlayUtil;
import com.pukka.ydepg.view.loadingball.MonIndicator;

import java.lang.ref.WeakReference;

/**
 * 播放器View
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PlayView
 * @Package com.pukka.ydepg.view
 * @date 2018/02/07 13:46
 */
public class PlayView extends FrameLayout implements IMediaPlayer, IPlayListener {

    private final byte[] mMutex = new byte[0];

    /**
     * ExoMediaPlayer
     */
    public static final String TYPE_EXO = "TYPE_EXO";

    /**
     * MediaPlayer
     */
    private static final String TYPE_NATIVE = "TYPE_NATIVE";

    /**
     * 填充
     */
    public static final String RESIZE_MODE_FIT = "FIT";

    /**
     * 自适应
     */
    public static final String RESIZE_MODE_AUTO = "AUTO";

    /**
     * 播放器对象
     */
    protected AbstractMediaPlayer mPlayer;
    /**
     * 是否是小窗口
     */
    private boolean isWindow;

    /**
     * 是否是详情页的播放窗口，大小屏可切换
     */
    private boolean isVodDetail;
    public ShowSkipTipCallBack skipTipCallBack;

    /**
     * 是否自动播放
     */
    private boolean isAutoPlay;

    /**
     * 播放器类型
     */
    private String mPlayType = TYPE_NATIVE;

    /**
     * 视频显示模式
     */
    private String mResizeMode = RESIZE_MODE_FIT;

    /**
     * 播放区域背景drawable
     */
    private Drawable mControlViewDrawable;

    /**
     * 播放控制栏
     */
    protected PlayControlView mControlView;

    private LinearLayout mBackToStartLayout;
    private TextView mBackToStartTv;

    private TextView mSettingRecord;

    private TextView mRecord;
    private RelativeLayout mTopRecordLayout;
    private TextView mBottomRecord;
    private ImageView mTopRecordImg;

    private TextView mTitleUp;

    /**
     * Loading框
     */
//    protected ProgressBar mLoadingBar;
    protected MonIndicator mLoadingBar;
    private IPlayListener mOnPlayCallback;

    /**
     * 是否为儿童详情模式
     */
    private boolean isChildDetailMode;


    protected int videoType = PlayUtil.VideoType.VOD;


    protected long iptvlength;

    private boolean isFirstShowTopRecord = true;

    private boolean isNeedToSkip = false;

    private String vodTitle = "";

    private PlayViewHandler mHandler = new PlayViewHandler(this);


    public void setTryToSeeFlag(int tryToSeeFlag) {
        if (null != mControlView) {
            mControlView.setTryToSeeFlag(tryToSeeFlag);
        }
    }


    public void setTryToSeeTime(int tryToSeeTime) {
        if (null != mControlView) {
            mControlView.setTryToSeeTime(tryToSeeTime);
        }
    }

    public PlayView(@NonNull Context context) {
        this(context, null);
    }

    public PlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayView(@NonNull Context context, @Nullable AttributeSet attrs,
                    @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public String getPlayType() {
        return mPlayType;
    }

    public void setPlayType(String mPlayType) {
        this.mPlayType = mPlayType;
    }

    public String getResizeMode() {
        return mResizeMode;
    }

    public void setResizeMode(String resizeMode) {
        this.mResizeMode = resizeMode;
        if (null != mPlayer) {
            mPlayer.setResizeMode(resizeMode);
        }
    }

    /**
     * 初始化
     *
     * @param context context
     * @param attrs   attrs
     */
    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasierTvPlayView, 0, 0);
            isAutoPlay = a.getBoolean(R.styleable.EasierTvPlayView_AutoPlay, false);
            synchronized (mMutex) {
                isWindow = a.getBoolean(R.styleable.EasierTvPlayView_isLittle, false);
                isVodDetail = a.getBoolean(R.styleable.EasierTvPlayView_isVodDetail, false);
            }
            mPlayType = a.getString(R.styleable.EasierTvPlayView_play_type);
            mControlViewDrawable = a.getDrawable(R.styleable.EasierTvPlayView_ControlViewBackground);
            mResizeMode = a.getString(R.styleable.EasierTvPlayView_play_resize_mode);
            if (TextUtils.isEmpty(mResizeMode)) {
                //没有设置默认设置为全屏填充
                mResizeMode = RESIZE_MODE_FIT;
            }
            a.recycle();

        }
        initPlayer(mPlayType);
    }


    /**
     * 设置播放类型
     *
     * @param playType 播放类型
     */
    public void setVideoType(int playType) {
        this.videoType = playType;
        if (null != mPlayer) {
            mPlayer.setVideoType(playType);
        }

    }


    /**
     * 初始化播放器
     *
     * @param playType 播放器类型
     */
    public void initPlayer(String playType) {
        synchronized (mMutex) {
            View surfaceView;
            if (playType.equals(TYPE_EXO)) {
                LayoutInflater.from(getContext()).inflate(R.layout.player_exoview, this);
                mPlayer = new ExoMediaPlayer(getContext());
                surfaceView = findViewById(R.id.fm_player);
            } else {
                LayoutInflater.from(getContext()).inflate(R.layout.player_mediaview, this);

                if (CommonUtiltemp.isJMGODevice()) {
                    mPlayer = new AndroidJMGMediaPlayer(getContext());
                }else{
                    mPlayer = new AndroidMediaPlayer(getContext());
                }

                surfaceView = findViewById(R.id.fm_player);
            }
            mPlayer.setSurface(surfaceView);
            //设置视频适应模式
            mPlayer.setResizeMode(mResizeMode);
            //设置是否自动播放
            mPlayer.setShouldAutoPlay(isAutoPlay);
            //设置播放回调监听器
            mPlayer.setPlayListener(this);
            mLoadingBar = (MonIndicator) findViewById(R.id.pb_loading);
            mBackToStartLayout = (LinearLayout) findViewById(R.id.pb_back_to_start_ll);
            mBackToStartTv = (TextView) findViewById(R.id.pb_back_to_start_tv);

            mTopRecordLayout = (RelativeLayout) findViewById(R.id.player_top_record);
            mSettingRecord = (TextView) findViewById(R.id.tv_setting_record);
            mRecord = (TextView) findViewById(R.id.tv_record);
            mTitleUp = (TextView) findViewById(R.id.tv_name_up_end);
            mBottomRecord = (TextView) findViewById(R.id.tv_record_bottom);
            mTopRecordImg = (ImageView) findViewById(R.id.tv_setting_record_img);

            mControlView = (PlayControlView) findViewById(R.id.playback_control);
            mControlView.setVodSkipListener(new PlayControlView.VodSkipListener() {
                @Override
                public void onSkipEnd() {
                    onPlayCompleted();
                }

                @Override
                public void onWillSkipEnd() {
                    if (isNeedToSkip) {
                        if (mControlView.isSkipStart()) {
                            if (mPlayer.getWindosState() == 1){
                                //优化播控，改为toast提示
//                                mRecord.setText(R.string.player_skip_end);
                                showToast("即将为您跳过片尾",null);
                            }else{
                                if (null != skipTipCallBack){
                                    skipTipCallBack.showSkip();
                                }
                            }
                        } else {
//                            mRecord.setText(R.string.player_not_skip_end);
                        }
                        isFirstShowTopRecord = true;
                        mRecord.setVisibility(VISIBLE);
                        mRecord.setSelected(true);
                        //优化播控，不再展示右上角提示
//                        mSettingRecord.setText(R.string.player_setting_record);
//                        mSettingRecord.setVisibility(VISIBLE);
                        mTopRecordImg.setVisibility(VISIBLE);
                        if (mPlayer.getWindosState() == 1){
                            showControlView(true);
                        }
                    }
                }

                @Override
                public void onBackToVideo() {
                    mRecord.setVisibility(GONE);
                    mRecord.setSelected(false);
                    mSettingRecord.setText("");
                    mSettingRecord.setVisibility(GONE);
                    mTopRecordImg.setVisibility(GONE);
                }
            });
            if (!isWindow || isVodDetail) {
                mControlView.setVisibility(View.GONE);
                mControlView.setPlayer(mPlayer);
                //设置控制栏
                mPlayer.setPlayControlView(mControlView);
            } else {
                mControlView.setVisibility(GONE);
                if (null != mLoadingBar) {
                    mLoadingBar.setVisibility(View.GONE);
                }
                mPlayer.setPlayControlView(null);
            }
            mPlayer.setIsWindow(isWindow);
            mPlayer.setIsNewDetail(isVodDetail);


            //设置控制栏背景
//      if (null != mControlViewDrawable) {
//        mControlView.setBackground(mControlViewDrawable);
//      } else {
//        mControlView.setBackgroundColor(Color.TRANSPARENT);
//      }
        }
    }

    public void initVideoView() {
        synchronized (mMutex) {
            if (null == mPlayer) {
                return;
            }
            View surfaceView;
            surfaceView = findViewById(R.id.fm_player);
            mPlayer.setSurface(surfaceView);
            //设置视频适应模式
            mPlayer.setResizeMode(mResizeMode);
            //设置是否自动播放
            mPlayer.setShouldAutoPlay(isAutoPlay);
            //设置播放回调监听器
            mPlayer.setPlayListener(this);
            mLoadingBar = (MonIndicator) findViewById(R.id.pb_loading);
            mBackToStartLayout = (LinearLayout) findViewById(R.id.pb_back_to_start_ll);
            mBackToStartTv = (TextView) findViewById(R.id.pb_back_to_start_tv);

            mTopRecordLayout = (RelativeLayout) findViewById(R.id.player_top_record);
            mSettingRecord = (TextView) findViewById(R.id.tv_setting_record);
            mRecord = (TextView) findViewById(R.id.tv_record);
            mTitleUp = (TextView) findViewById(R.id.tv_name_up_end);
            mBottomRecord = (TextView) findViewById(R.id.tv_record_bottom);
            mTopRecordImg = (ImageView) findViewById(R.id.tv_setting_record_img);

            mControlView = (PlayControlView) findViewById(R.id.playback_control);
            mControlView.setVodSkipListener(new PlayControlView.VodSkipListener() {
                @Override
                public void onSkipEnd() {
                    onPlayCompleted();
                }

                @Override
                public void onWillSkipEnd() {
                    if (isNeedToSkip) {
                        if (mControlView.isSkipStart()) {
                            if (mPlayer.getWindosState() == 1){
                                //优化播控，改为toast提示
//                                mRecord.setText(R.string.player_skip_end);
                                showToast("即将为您跳过片尾",null);
                            }else{
                                if (null != skipTipCallBack){
                                    skipTipCallBack.showSkip();
                                }
                            }
                        } else {
//                            mRecord.setText(R.string.player_not_skip_end);
                        }
                        isFirstShowTopRecord = true;
                        mRecord.setVisibility(VISIBLE);
                        mRecord.setSelected(true);
                        //优化播控，不再展示右上角提示
//                        mSettingRecord.setText(R.string.player_setting_record);
//                        mSettingRecord.setVisibility(VISIBLE);
                        mTopRecordImg.setVisibility(VISIBLE);
                        if (mPlayer.getWindosState() == 1){
                            showControlView(true);
                        }
                    }
                }

                @Override
                public void onBackToVideo() {

                }
            });
            if (!isWindow || isVodDetail) {
                mControlView.setVisibility(View.GONE);
                mControlView.setPlayer(mPlayer);
                //设置控制栏
                mPlayer.setPlayControlView(mControlView);
            } else {
                mControlView.setVisibility(GONE);
                if (null != mLoadingBar) {
                    mLoadingBar.setVisibility(View.GONE);
                }
                mPlayer.setPlayControlView(null);
            }
            mPlayer.setIsWindow(isWindow);
            mPlayer.setIsNewDetail(isVodDetail);

        }
    }

    /**
     * 设置播放监听回调
     *
     * @param listener listener
     */
    public void setOnPlayCallback(IPlayListener listener) {
        this.mOnPlayCallback = listener;
    }

    /**
     * 播放
     *
     * @param url      播放地址
     * @param bookmark 书签
     */
    @Override
    public void startPlay(String url, long bookmark) {
        if (null != mPlayer) {
            //时移直播，要在播控中重启播放器
            if (null != mControlView) {
                mControlView.setCurrentUrl(url);
            }
            mPlayer.reInitPlayHandler();
            mPlayer.startPlay(url, bookmark);
        }
    }

    /**
     * 有前贴广告的播放
     *
     * @param url
     * @param advertUrl
     * @param bookmark
     */
    public void startPlay(String url, String advertUrl, long bookmark) {

        if (null != mPlayer) {
            //时移直播，要在播控中重启播放器
            if (null != mControlView) {
                mControlView.setCurrentUrl(url);
            }
            mPlayer.reInitPlayHandler();
            mPlayer.startPlay(url, advertUrl, bookmark);
        }

    }

    /**
     * 播放
     *
     * @param url 播放地址
     */
    @Override
    public void startPlay(String url) {
        if (null != mPlayer) {
            //时移直播，要在播控中重启播放器
            if (null != mControlView) {
                mControlView.setCurrentUrl(url);
            }
            mPlayer.startPlay(url);
        }
    }

    /**
     * 重新播放
     */
    @Override
    public void rePlay() {
        if (null != mPlayer) {
            mPlayer.rePlay();
        }
    }

    /**
     * 恢复播放
     */
    @Override
    public void resumePlay() {
        if (null != mPlayer) {
            mPlayer.resumePlay();
        }
    }

    /**
     * 暂停播放
     */
    @Override
    public void pausePlay() {
        if (null != mPlayer) {
            if (null != mControlView) {
                mControlView.resetCachePausePosition();
            }
            mPlayer.pausePlay();

        }
    }

    /**
     * 播放暂停
     */
    @Override
    public void playerOrPause() {
        if (null != mPlayer) {
            if (null != mControlView) {
                mControlView.resetCachePausePosition();
            }
            mPlayer.playerOrPause();
        }
    }

    /**
     * 释放播放器
     */
    @Override
    public void releasePlayer() {
        if (null != mPlayer) {
            mPlayer.releasePlayer();
        }
    }

    /**
     * 快速释放,重置mediaPlay
     */
    @Override
    public void fastReleasePrepare() {
        if (null != mPlayer) {

            mPlayer.fastReleasePrepare();
        }
    }

    /**
     * 快速起播
     *
     * @param playUrl 播放地址
     */
    @Override
    public void fastPlayVideo(String playUrl) {
        if (null != mPlayer) {
            //时移直播，要在播控中重启播放器
            if (null != mControlView) {
                mControlView.setCurrentUrl(playUrl);
            }
            mPlayer.fastPlayVideo(playUrl);
        }
    }


    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        return (null != mPlayer && mPlayer.isPlaying());
    }


    public boolean isPause() {

        return null != mPlayer && mPlayer.isPause();
    }

    /**
     * 设置海报
     *
     * @param bitmap 传入要设置在播放器上面的封面图
     */
    @Override
    public void setArtwork(Bitmap bitmap) {
        if (null != mPlayer) {
            mPlayer.setArtwork(bitmap);
        }
    }

    /**
     * 当前进度
     */
    @Override
    public long getCurrentPosition() {
        if (videoType == PlayUtil.VideoType.TSTV && null != mControlView) {
            return mControlView.getCurrentPosition();
        }
        return null != mPlayer ? mPlayer.getCurrentPosition() : 0;
    }

    /**
     * 总时长
     */
    @Override
    public long getDuration() {
        //return null != mPlayer ? mPlayer.getDuration() : 0;
        return null != mControlView ? mControlView.getDuration() : 0;
    }

    /**
     * 设置播放进度
     *
     * @param progress 进度值
     */
    @Override
    public void seekTo(long progress) {
        if (null != mPlayer) {
            mPlayer.seekTo(progress);
        }
    }

    /**
     * 前进
     */
    @Override
    public void fastForward() {
        if (null != mPlayer) {
            mPlayer.fastForward();
        }
    }

    /**
     * 前进
     *
     * @param forwardPosition 前进值
     */
    @Override
    public void fastForward(long forwardPosition) {
        if (null != mPlayer) {
            mPlayer.fastForward(forwardPosition);
        }
    }

    /**
     * 后退
     */
    @Override
    public void rewind() {
        if (null != mPlayer) {
            mPlayer.rewind();
        }
    }

    /**
     * 后退
     *
     * @param rewindPosition 后退进度值
     */
    @Override
    public void rewind(long rewindPosition) {
        if (null != mPlayer) {
            mPlayer.rewind(rewindPosition);
        }
    }

    /**
     * 设置是否自动播放
     *
     * @param shouldAutoPlay 是否自动播放
     */
    @Override
    public void setShouldAutoPlay(boolean shouldAutoPlay) {
        if (null != mPlayer) {
            mPlayer.setShouldAutoPlay(shouldAutoPlay);
        }
    }

    @Override
    public void dragProgress(int time) {
        if (null != mPlayer) {
            mPlayer.dragProgress(time);
        }
    }

    @Override
    public void restartUpdateProgress() {
        if (null != mPlayer) {
            mPlayer.restartUpdateProgress();
        }
    }

    @Override
    public void setIptvlength(long duration) {
        this.iptvlength = duration;
        if (null != mControlView && null != mPlayer) {
            mControlView.setmIPTVLength(iptvlength);
            mControlView.setVideoType(videoType);
            mPlayer.setVideoType(videoType);
            mPlayer.setIptvlength(iptvlength);
        }
    }

    /**
     * 播放状态回调
     *
     * @param playbackState playbackState
     */
    @Override
    public void onPlayState(int playbackState) {
        if (null != mOnPlayCallback) {
            mOnPlayCallback.onPlayState(playbackState);
        }
        if (null != mLoadingBar) {
            if (playbackState == IPlayState.PLAY_STATE_BUFFERING && videoType != PlayUtil.VideoType.TV && videoType != PlayUtil.VideoType.TSTV && !isChildDetailMode) {
                mLoadingBar.setVisibility(View.VISIBLE);
            } else {
                mLoadingBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPrepared(int Videotype) {
        if (null != mOnPlayCallback) {
            mOnPlayCallback.onPrepared(Videotype);
        }

    }

    @Override
    public void onRelease() {
        if (null != mOnPlayCallback) {
            mOnPlayCallback.onRelease();
        }
    }

    /**
     * 播放出错
     */
    @Override
    public void onPlayError(String msg, int errorCode, int playerType) {
        if (null != mOnPlayCallback) {
            mOnPlayCallback.onPlayError(msg, errorCode, playerType);
        }
    }

    /**
     * 播放完成
     */
    @Override
    public void onPlayCompleted() {
        if (null != mOnPlayCallback) {
            mOnPlayCallback.onPlayCompleted();
        }
    }

    @Override
    public void onDetached(long time) {

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onTryPlayForH5() {
        if (null != mOnPlayCallback) {
            mOnPlayCallback.onTryPlayForH5();
        }
    }

    @Override
    public void onAdVideoEnd() {
        if (null != mOnPlayCallback) {
            mOnPlayCallback.onAdVideoEnd();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null != mOnPlayCallback) {
            mOnPlayCallback.onAttached();
        }
    }

    /**
     * 视图销毁
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mOnPlayCallback != null) {
            mOnPlayCallback.onDetached(getCurrentPosition());
        }

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
//        releasePlayer();
    /*if(null!=mPlayer){
      mPlayer.onDestoryPlay();
    }*/
        if (null != mControlViewDrawable) {
            mControlViewDrawable.setCallback(null);
            if (null != mControlView) {
                mControlView.setBackground(null);
                mControlView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 显示底部控制栏，并在5秒之后消失
     *
     * @param isDelayHide 是否支持延时隐藏
     */
    public void showControlView(boolean isDelayHide) {
        setControllViewState(View.VISIBLE, isDelayHide);
    }

    //展示底部下拉箭头
    public void setDownArrowVisible(boolean visible){
        mControlView.setDownArrorVisible(visible);
    }

    //展示Toast
    public void showToast(String string, SpannableString spannableString){
        mControlView.showToast(string,spannableString);
    }

    //隐藏Toast
    public void hideToast(){
        mControlView.hideToast();
    };

    //设置跳过片头片尾的节点 0片头 1片尾 percent进度
    public void setNode(int position, float percent){
        mControlView.setNode(position,percent);
    }

    //隐藏节点
    public void hideNode(){
        mControlView.hideNode();
    }

    /**
     * 隐藏底部控制栏
     */
    public void hideControlView() {
        setControllViewState(View.GONE, true);
    }

    /**
     * 设置播放控制状态
     *
     * @param visibility  可见还是不可见
     * @param isDelayHide 只针对show
     */
    public void setControllViewState(int visibility, boolean isDelayHide) {
        if (null == mControlView) return;
        if (visibility == View.VISIBLE) {
            mControlView.show(isDelayHide);

            mTopRecordLayout.setVisibility(VISIBLE);
            if (isFirstShowTopRecord) {
                isFirstShowTopRecord = false;
                mRecord.setVisibility(VISIBLE);
                mRecord.setSelected(true);
                if (TextUtils.isEmpty(mSettingRecord.getText())) {
                    mSettingRecord.setVisibility(GONE);
                    mTopRecordImg.setVisibility(GONE);
//                    mBottomRecord.setText("");
                    mBottomRecord.setVisibility(VISIBLE);
                } else {
                    mSettingRecord.setVisibility(VISIBLE);
                    mTopRecordImg.setVisibility(VISIBLE);
                    mBottomRecord.setVisibility(VISIBLE);
                }
            } else {
//                mRecord.setVisibility(GONE);
                mRecord.setVisibility(VISIBLE);
                mRecord.setSelected(true);
//                mSettingRecord.setVisibility(GONE);
//                mTopRecordImg.setVisibility(GONE);
                mBottomRecord.setText("");
                mBottomRecord.setVisibility(VISIBLE);
            }
            if (isDelayHide) {

                mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0, PlayControlView.DEFAULT_SHOW_TIMEOUT_MS);

                mHandler.removeMessages(1);
                mHandler.sendEmptyMessageDelayed(1, PlayControlView.DEFAULT_SHOW_TIMEOUT_MS);
            }
        } else {
            mControlView.hide();
            mTopRecordLayout.setVisibility(GONE);
            mRecord.setVisibility(GONE);
//            if (!isNeedToSkip || isVodDetail) {
//                mRecord.setText(vodTitle);
//            } else {
//                mRecord.setText("");
//            }
            mRecord.setText(vodTitle);
            mSettingRecord.setText("");
//            mSettingRecord.setCompoundDrawables(null, null, null, null);
            mSettingRecord.setVisibility(GONE);
            mTopRecordImg.setVisibility(GONE);
            mBottomRecord.setText("");

            mControlView.hideToast();
            mControlView.hideNode();
        }
    }

    public void setPlayerNull() {
        if(mPlayer instanceof AndroidJMGMediaPlayer)
        {
            mPlayer.setNullPlayer();
        }
    }


    @SuppressLint("HandlerLeak")
    private class PlayViewHandler extends Handler {
        private WeakReference<PlayView> mReference;

        PlayViewHandler(PlayView playView) {
            mReference = new WeakReference<PlayView>(playView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != mReference && null != mReference.get()) {
                if (msg.what == 0) {//点击快进按钮，重置延时隐藏controlview
                    mReference.get().mRecord.setVisibility(GONE);
//                    if (!isNeedToSkip || isVodDetail) {
//                        mReference.get().mRecord.setText(mReference.get().vodTitle);
//                    } else {
//                        mReference.get().mRecord.setText("");
//                    }
                    mReference.get().mRecord.setText(mReference.get().vodTitle);
                } else if (msg.what == 1) {//延时隐藏其他
                    mReference.get().mTopRecordLayout.setVisibility(GONE);
                    //.setVisibility(GONE);
                    mReference.get().mSettingRecord.setText("");
//                        mSettingRecord.setCompoundDrawables(null, null, null, null);
                    mReference.get().mSettingRecord.setVisibility(GONE);
                    mReference.get().mTopRecordImg.setVisibility(GONE);
                    mReference.get().mBottomRecord.setText("");
                }
            }
        }
    }

    ;


    /**
     * 移除默认控制栏
     */
    public void removeDefaultPlayControlView() {
        setControllViewState(View.GONE, false);
        if (null != mControlView) {
            mControlView.setPlayer(null);
        }
    }

    public void onDestory() {
        if (null != mPlayer) {
            mPlayer.onDestoryPlay();
        }
    }

    public void setChildDetailMode(boolean childDetailMode) {
        isChildDetailMode = childDetailMode;
    }

    public void showBackToStartToast(int resId) {
        mBackToStartTv.setText(resId);
        mBackToStartLayout.setVisibility(VISIBLE);
    }

    public void hideBackToStartToast() {
        mBackToStartLayout.setVisibility(GONE);
    }

    @Override
    public void restartTSTVurl() {
        mPlayer.restartTSTVurl();
    }

    @Override
    public void resetMediaPlayer() {
        mPlayer.resetMediaPlayer();
    }

    public void setSpeed(float speed) {
        if (mPlayer != null) {
            Log.i("TAG", "setSpeed: "+speed);
            mPlayer.setSpeed(speed);
        }
    }

    public void setSkipStart(boolean skipStart) {
        if (mControlView != null) {
            mControlView.setSkipStart(skipStart);
        }
    }

    public void setEndTime(long endTime) {
        if (mControlView != null) {
            mControlView.setEndTime(endTime);
        }
    }

    public TextView getmRecord() {
        return mRecord;
    }

    public TextView getmTitleUp() {
        return mTitleUp;
    }

    public RelativeLayout getmTopRecordLayout() {
        return mTopRecordLayout;
    }

    public TextView getmSettingRecord() {
        return mSettingRecord;
    }

    public void setmBottomRecord() {
        mBottomRecord.setText(R.string.player_record_1);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomRecord.setText("");
            }
        }, 2000);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomRecord.setText(R.string.player_record_2);
            }
        }, 6000);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomRecord.setText("");
            }
        }, 8000);
    }

    public void setmBottomRecordText(String str){
        mBottomRecord.setText(str);
        mBottomRecord.setVisibility(VISIBLE);
    }

    public void setFirstShowTopRecord(boolean firstShowTopRecord) {
        isFirstShowTopRecord = firstShowTopRecord;
    }

    public void setNeedToSkip(boolean needToSkip) {
        isNeedToSkip = needToSkip;
    }

    public boolean canSetSpeed(float speed) {
        if (mPlayer != null) {
            return mPlayer.canSetSpeed(speed);
        }
        return false;
    }

    public void setVodTitle(String vodTitle) {
        this.vodTitle = vodTitle;
    }

    public boolean isCurrentPlayAdvert() {
        if (mPlayer != null) {
            return mPlayer.isCurrentPlayAdvert();
        }
        return false;
    }

    /***********************************暴露出surfaceView给详情页做大小屏切换************************/

    public void setSurfaceViewSize(float width, float height){
        mPlayer.setSurfaceViewSize(width,height);
    }

    //提供回调给小屏时提示跳过片尾
    public interface ShowSkipTipCallBack{
        void showSkip();
    }

    public void setWindowState(int state){
        mPlayer.setWindosState(state);
    }

    public void resetControlState(){
        mControlView.setHasShowToast(false);
    }
}