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

import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.pukka.ydepg.AbstractMediaPlayer;
import com.pukka.ydepg.CommonUtiltemp;
import com.pukka.ydepg.inf.IPlayListener;
import com.pukka.ydepg.inf.IPlayState;
import com.pukka.ydepg.player.R;
import com.pukka.ydepg.util.PlayUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * 播放器控制栏(定制版),参考:com.google.android.exoplayer2.ui.PlaybackControlView
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PlayBackControlView
 * @Package com.pukka.ydepg.view
 * @date 2018/02/07 12:45
 */
public class PlayControlView extends FrameLayout {

    /**
     * TAG
     */
    private static final String TAG = "PlayControlView";

    /**
     * 默认快进每次前进时间10秒
     */
    private static final int DEFAULT_FAST_FORWARD_MS = 10000;

    /**
     * 默认倒带快退时间10秒
     */
    public static final int DEFAULT_REWIND_MS = 10000;

    /**
     * 默认显示超时时间5秒
     */
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 8000;

    /**
     * 当前窗口是否销毁
     */
    private boolean isAttachedToWindow;

    /**
     * 显示当前播放时长的View
     */
    private TextView mCurrentPosition;

    /**
     * 显示当前总时长的View
     */
    private TextView mTotalPosition;

    /**
     * 播放按钮
     */
    private ImageView mPlayIcon;

    /**
     * 点播的名字
     */
    private TextView tv_subtitle;

    /**
     * 播放进度条
     */
    private SeekBar mProgressbar;
    private RelativeLayout seekBarLayout;

    //新增，底部下拉按钮
    private ImageView mDownArrow;
    //新增，进度条上方的toast
    private RelativeLayout mToastLayout;
    private TextView mToastTextView;

    /**
     * StringBuilder
     */
    private final StringBuilder mFormatBuilder;

    /**
     * Formatter
     */
    private final Formatter mFormatter;

    /**
     * 媒体播放器接口，定义了一些诸如播放暂停之类的操作
     */
    private AbstractMediaPlayer mPlayer;

    /**
     * 当前播放状态
     */
    private int mPlayState = IPlayState.PLAY_STATE_NOMEDIA;

    /**
     * 组件的listener
     */
    private ControlComponentListener mListener;

    /**
     * 是否在拖拽进度条
     */
    private boolean isDragProgress;


    //是否是试看状态
    private int tryToSeeFlag;

    private int tryToSeeTime = 5 * 60;


    private int videoType = PlayUtil.VideoType.VOD;
    /**
     * 当前时移进度条的位置
     */
    private long currentTSTVPositon;

    /**
     * 暂停时缓存当前播放时间戳
     */
    private long cachePausePosition;
    /**
     * 当前播放的url
     */
    private String currentUrl;

    /**
     * 时移偏移url
     */
    private long offset;

    //播控优化，增加跳过片头片尾的节点
    View headNodeView ;
    View tailNodeView ;


    /**
     * 快速时移
     */
    private long restartTSTVDelayTime = 500;

    private SimpleDateFormat sdf = new SimpleDateFormat(PlayUtil.TIME_FORMAT_N);

    private long endTime;
    private boolean isSkipStart = false;
    private VodSkipListener vodSkipListener;
    private int skipDelayTime = 5000;
    private boolean hasShowToast = false;


    public void resetCachePausePosition() {
        int duration = (int) getDuration();
        if (duration == 0) {
            return;
        }
        int currentPosition = (int) getCurrentPosition();
        if (PlayUtil.VideoType.TSTV == videoType) {
            int offset = duration - currentPosition;
            long nowTime = PlayUtil.getNtpTime();
            cachePausePosition = nowTime - offset;

        }
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public void setmIPTVLength(long mIPTVLength) {
        currentTSTVPositon = mIPTVLength;
    }


    public void setTryToSeeFlag(int tryToSeeFlag) {
        this.tryToSeeFlag = tryToSeeFlag;
    }


    public void setTryToSeeTime(int tryToSeeTime) {
        this.tryToSeeTime = tryToSeeTime;
    }

    /**
     * update progressValue Runnable
     */
    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };
    /**
     * hide Runnable
     */
    private final Runnable hideAction = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };


    private final Runnable restartTSTVAction = new Runnable() {

        @Override
        public void run() {
            restartTSTVurl(offset);
        }
    };

    public PlayControlView(@NonNull Context context) {
        this(context, null);
    }

    public PlayControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayControlView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mListener = new ControlComponentListener();
    }

    /**
     * 设置当前媒体播放控制
     *
     * @param player player
     */
    public void setPlayer(AbstractMediaPlayer player) {
        if (this.mPlayer == player) {
            return;
        }
        this.mPlayer = player;
        if (player != null) {
            //添加新的监听器
            player.setOnUpdateProgressListener(mListener);
        }
        //更新数据
        updateAll();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        updateAll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        //从消息队列中移除
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
    }

    /**
     * 布局填充完成
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCurrentPosition = (TextView) findViewById(R.id.tv_position);
        mTotalPosition = (TextView) findViewById(R.id.tv_duration);
        mPlayIcon = (ImageView) findViewById(R.id.iv_play);
        mProgressbar = (SeekBar) findViewById(R.id.sb_progress);
        seekBarLayout = findViewById(R.id.sb_progress_layout);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        //设置监听器
        mProgressbar.setOnSeekBarChangeListener(mListener);
        mProgressbar.setSecondaryProgress(0);

        mDownArrow = findViewById(R.id.controller_down_arrow);
        mToastLayout = findViewById(R.id.toast_parent_view);
        mToastTextView = findViewById(R.id.controller_toast_text);

        headNodeView = findViewById(R.id.node_left);
        tailNodeView = findViewById(R.id.node_right);
    }

    /**
     * 更新进度
     */
    public void updateProgress() {
        if (!isVisible() && videoType == PlayUtil.VideoType.TSTV && null != mPlayer && mPlayer.isTSTVPause() && isAttachedToWindow) {
            int duration = (int) getDuration();
            long nowTime = PlayUtil.getNtpTime();
            currentTSTVPositon = duration - (nowTime - cachePausePosition);
            if (currentTSTVPositon < 0) {
                currentTSTVPositon = 0;
            }
            sendMessageUpdate();
            return;

        }
        if (!isAttachedToWindow) {
            return;
        }
        showSlideSeekTime();
        removeCallbacks(updateProgressAction);
        if (null != mPlayer) {
            sendMessageUpdate();
        }
    }

    private void sendMessageUpdate() {
        if (mPlayState != IPlayState.PLAY_STATE_NOMEDIA && mPlayState != IPlayState.PALY_STETE_END) {
            long delayMs = 0;
            if (mPlayer.isPlaying()) {
                float playbackSpeed = 1.0f;
                long mediaTimeUpdatePeriodMs = 1000 / Math.max(1, Math.round(1 / playbackSpeed));
                long mediaTimeDelayMs = mediaTimeUpdatePeriodMs - (getCurrentPosition() %
                        mediaTimeUpdatePeriodMs);
                if (mediaTimeDelayMs < (mediaTimeUpdatePeriodMs / 5)) {
                    mediaTimeDelayMs += mediaTimeUpdatePeriodMs;
                }
                delayMs = mediaTimeDelayMs;
            }
            if (delayMs > 1000 || delayMs == 0) {
                delayMs = 1000;
            }
            postDelayed(updateProgressAction, delayMs);
        }
    }

    private void showSlideSeekTime() {
        int duration = (int) getDuration();

        if (duration == 0) {
            return;
        }
        boolean isPause = null != mPlayer && mPlayer.isTSTVPause();
        long currentPosition = getCurrentPosition();
        //不要和拖拽进度条时候冲突
        if (PlayUtil.VideoType.TSTV == videoType) {
            long nowTime = PlayUtil.getNtpTime();
            long playTime;
            //暂停时，暂停时间就是缓存时间，当前进度条位置=进度条时长-（当前时间-暂停时间）
            if (isPause) {
                playTime = cachePausePosition;
                currentPosition = duration - (nowTime - cachePausePosition);
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                currentTSTVPositon = currentPosition;
            } else {
                long offset = duration - currentPosition;
                playTime = nowTime - offset;
            }
            Log.i(TAG, "[showSlidSeekTime]  duration = " + duration + " --currentPosition" + " = " +
                    "" + " " + "" + currentPosition);
            Date nowDate = new Date(nowTime);
            Date playDate = new Date(playTime);

            mCurrentPosition.setText(PlayUtil.getFormatString("HH:mm:ss", playDate));
            mTotalPosition.setText(PlayUtil.getFormatString("HH:mm:ss", nowDate));
        } else {

            if (mCurrentPosition != null && !isDragProgress) {
                if (tryToSeeFlag == 1 && currentPosition >= tryToSeeTime) {
                    mCurrentPosition.setText(PlayUtil.getStringForTime(mFormatBuilder, mFormatter,
                            tryToSeeTime));
                } else {
                    mCurrentPosition.setText(PlayUtil.getStringForTime(mFormatBuilder, mFormatter,
                            currentPosition));
                }
            }

            if (mTotalPosition != null) {
                mTotalPosition.setText(PlayUtil.getStringForTime(mFormatBuilder, mFormatter, duration));
            }
        }
        if (null != mProgressbar && !isDragProgress) {
            mProgressbar.setMax(duration);
            //暂停的时候不更新进度条.....
            boolean isPlay = (null == mPlayer) || (mPlayer.isPlaying());
            if (isPlay) {
                mProgressbar.setProgress((int) currentPosition);
            } else {
                //解决暂停是进度条还是上次看到的进度，不更新的问题
                mProgressbar.setProgress(videoType == PlayUtil.VideoType.TSTV ? (int)
                        currentPosition :
                        (int) mPlayer.getCurrentPosition());
            }

        }


    }

    /**
     * 当前进度条是否可见
     *
     * @return
     */
    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    /**
     * 获取当前进度值
     */
    protected long getCurrentPosition() {
        long position = 0;
        if (null != mPlayer) {
            if (videoType == PlayUtil.VideoType.TSTV) {
                Log.e(TAG, "getCurrentPosition->currentTSTVPositon:" + currentTSTVPositon);
                return currentTSTVPositon;
            }
            position = mPlayer.getCurrentPosition();
        }
        return position;
    }

    /**
     * 获取总时长
     */
    public long getDuration() {
        long totalPosition = 0;
        if (null != mPlayer) {
            totalPosition = mPlayer.getDuration();
        }
        return totalPosition;
    }

    /**
     * 设置当前进度条数值
     *
     * @param progress progress
     */
    public void seekTo(long progress) {
        if (null != mPlayer) {
            mPlayer.seekTo(progress);
        } else {
            Log.e(TAG, "seekTo error,mPlayer=null");
        }
    }

    /**
     * 快进
     */
    public void fastForward() {
        fastForward(DEFAULT_FAST_FORWARD_MS);
    }

    /**
     * 前进
     *
     * @param forwardPosition 前进位置
     */
    public void fastForward(long forwardPosition) {
        long durationMs = getDuration();
        long seekPositionMs = getCurrentPosition() + forwardPosition;
        if (tryToSeeFlag == 1 && seekPositionMs >= tryToSeeTime) {
            seekPositionMs = tryToSeeTime;
        }
        if (durationMs != C.TIME_UNSET && durationMs > 0) {
            seekPositionMs = Math.min(seekPositionMs, durationMs);
        }
        if (videoType == PlayUtil.VideoType.TSTV) {
            int duration = (int) getDuration();
            if (duration == 0) {
                return;
            }
            offset = duration - currentTSTVPositon - forwardPosition;
            if (offset < 0) {
                offset = 0;
            }
            if (currentTSTVPositon != duration) {
                removeCallbacks(restartTSTVAction);
                postDelayed(restartTSTVAction, restartTSTVDelayTime);
//                restartTSTVurl(offset);
            }
            if (duration - currentTSTVPositon - forwardPosition <= 0) {
                currentTSTVPositon = duration;
            } else {
                currentTSTVPositon = seekPositionMs;
            }

        } else {
            seekTo((int) seekPositionMs);
        }
    }

    /**
     * 快退
     */
    public void rewind() {
        rewind(DEFAULT_REWIND_MS);
    }

    /**
     * 快退
     *
     * @param rewindPosition 快退位置
     */
    public void rewind(long rewindPosition) {
        if (videoType == PlayUtil.VideoType.TSTV) {
            int duration = (int) getDuration();
            if (duration == 0) {
                return;
            }
            offset = duration - currentTSTVPositon + rewindPosition;
            if (offset > duration) {
                offset = duration;
            }
            if (currentTSTVPositon != 0) {
                removeCallbacks(restartTSTVAction);
                postDelayed(restartTSTVAction, restartTSTVDelayTime);
//                restartTSTVurl(offset);
                currentTSTVPositon = Math.max(getCurrentPosition() - rewindPosition, 0);
            }
        } else {
            seekTo((int) Math.max(getCurrentPosition() - rewindPosition, 0));
        }
    }

    /**
     * @param offset
     */
    public void restartTSTVurl(long offset) {
        if (offset == 0) {
            mPlayer.fastReleasePrepare();
            mPlayer.releasePlayer();
            if(CommonUtiltemp.isJMGODevice()) {
                mPlayer.resetMediaPlayer();
            }
            mPlayer.startPlay(currentUrl);
            return;
        }
        String dateString = sdf.format(new Date(PlayUtil.getNtpTime() - offset));
        mPlayer.fastReleasePrepare();
        if (!TextUtils.isEmpty(PlayUtil.getIptvUrl()) && PlayUtil.getIptvUrl().contains(PlayUtil.REQUEST_PARAMETER)) {
            int startIndex = PlayUtil.getIptvUrl().indexOf(PlayUtil.REQUEST_PARAMETER);
            startIndex = startIndex + PlayUtil.REQUEST_PARAMETER.length() - 1;
            String startStr = PlayUtil.getIptvUrl().substring(0, startIndex + 1);
//            String endStr=currentUrl.substring(startIndex+"yyyyMMddHHmmss-".length()+1,currentUrl.length());
            mPlayer.releasePlayer();
            if(CommonUtiltemp.isJMGODevice()) {
                mPlayer.resetMediaPlayer();
            }
            mPlayer.startPlay(startStr + dateString);
//            mPlayer.fastPlayVideo(startStr+dateString);
        }

    }

    /**
     * 隐藏播放控制View
     */
    protected void hide() {
        if (null != tv_subtitle) {
            tv_subtitle.setSelected(false);
        }
        if (isVisible()) {
            setVisibility(GONE);
            if (!(videoType == PlayUtil.VideoType.TSTV && null != mPlayer && mPlayer.isTSTVPause() && isAttachedToWindow)) {
//                removeCallbacks(updateProgressAction);
            }
            removeCallbacks(hideAction);
        }
    }

    /**
     * 显示播放控制View
     */
    protected void show(boolean isDelayHide) {
        if (null != tv_subtitle) {
            tv_subtitle.setSelected(true);
        }
        if (!isVisible()) {
            setVisibility(VISIBLE);
            updateAll();
        }
        if (isDelayHide) {
            hideAfterTimeout();
        } else {
            removeCallbacks(hideAction);
        }
    }

    /**
     * 延时做隐藏控制栏
     */
    private void hideAfterTimeout() {
        removeCallbacks(hideAction);
        //if (isAttachedToWindow) {
        postDelayed(hideAction, DEFAULT_SHOW_TIMEOUT_MS);
        //}
    }

    /**
     * 播放状态和进度变化的监听器
     */
    private final class ControlComponentListener implements SeekBar.OnSeekBarChangeListener,
            IPlayListener {

        /**
         * 进度条进度改变
         *
         * @param seekBar  seekBar
         * @param progress 进度值
         * @param fromUser 是不是用户拖拽
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (mCurrentPosition != null) {
                if (videoType == PlayUtil.VideoType.TSTV) {
                    int offset = seekBar.getMax() - progress;
                    long nowTime = PlayUtil.getNtpTime();
                    long playTime = nowTime - offset;
                    Date nowDate = new Date(nowTime);
                    Date playDate = new Date(playTime);
                    mCurrentPosition.setText(PlayUtil.getFormatString("HH:mm:ss", playDate));
                    mTotalPosition.setText(PlayUtil.getFormatString("HH:mm:ss", nowDate));
                } else {
                    mCurrentPosition.setText(PlayUtil.getStringForTime(mFormatBuilder, mFormatter,
                            progress));
                    if (getCurrentPosition() + skipDelayTime > getDuration() - endTime && !hasShowToast) {
                        hasShowToast = true;
                        if (vodSkipListener != null) {
                            if (!mPlayer.isCurrentPlayAdvert()) {
                                vodSkipListener.onWillSkipEnd();
                            }
                        }
                    }
                    if (getCurrentPosition() > getDuration() - endTime && isSkipStart && endTime != 0) {
                        //TODO
                        if (vodSkipListener != null) {
                            vodSkipListener.onSkipEnd();
                        }
                        return;
                    }
                }
            }
        }

        /**
         * 开始拖拽进度条
         *
         * @param seekBar seekbar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            removeCallbacks(hideAction);
            isDragProgress = true;
        }

        /**
         * 停止拖拽进度条
         *
         * @param seekBar seekbar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isDragProgress = false;
            seekTo(seekBar.getProgress());
            hideAfterTimeout();
        }

        /**
         * 播放状态变更回调
         *
         * @param playbackState 播放状态
         */
        @Override
        public void onPlayState(int playbackState) {
            mPlayState = playbackState;
            updatePlayPauseButton();
            updateProgress();
        }

        @Override
        public void onPrepared(int Videotype) {

        }

        @Override
        public void onRelease() {

        }

        @Override
        public void onPlayError(String msg, int errorCode, int playerType) {
        }

        @Override
        public void onPlayCompleted() {
        }

        @Override
        public void onDetached(long time) {

        }

        @Override
        public void onAttached() {

        }

        @Override
        public void onTryPlayForH5() {

        }

        @Override
        public void onAdVideoEnd() {

        }
    }

    /**
     * 更新播放按钮状态
     */
    private void updatePlayPauseButton() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }
        boolean playing = null != mPlayer && mPlayer.isPlaying();
        boolean isPause = null != mPlayer && mPlayer.isTSTVPause();
        if (mPlayIcon != null) {
            if (videoType == PlayUtil.VideoType.TSTV) {
                if (isPause) {
                    mPlayIcon.setImageResource(R.drawable.play_48);
                } else {
                    mPlayIcon.setImageResource(R.drawable.pause_48);
                }

            } else {
                if (playing) {
                    Log.i("button state","pause_48");
                    //play icon
                    mPlayIcon.setImageResource(R.drawable.pause_48);
                } else {
                    //pause icon
                    Log.i("button state","play_48");
                    mPlayIcon.setImageResource(R.drawable.play_48);
                }
            }
        }
    }

    /**
     * 更新进度和按钮状态
     */
    private void updateAll() {
        updatePlayPauseButton();
        updateProgress();
    }

    /**
     * 长按拖拽进度条
     *
     * @param time 进度时间值
     */
    public void dragProgress(int time) {
        removeCallbacks(updateProgressAction);
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }
        int duration = (int) getDuration();
        if (null != mProgressbar && duration > 1) {
            //旧版本逻辑是:暂停的时候不可以拖拽进度条.
            //新版本逻辑是:暂停的时候可以拖拽进度条.
            //if(mPlayer.isPlaying()){
            mProgressbar.setProgress(time);
            //}
        }
    }

    /**
     * 重新恢复更新进度
     */
    public void restartUpdateProgress() {
        updateProgress();
    }

    public void setSkipStart(boolean skipStart) {

        isSkipStart = skipStart;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public interface VodSkipListener {
        void onSkipEnd();

        void onWillSkipEnd();

        void onBackToVideo();
    }

    public void setVodSkipListener(VodSkipListener vodSkipListener) {
        this.vodSkipListener = vodSkipListener;
    }

    public boolean isSkipStart() {
        return isSkipStart;
    }

    public void setHasShowToast(boolean hasShowToast) {
        this.hasShowToast = hasShowToast;
    }

    public void setDownArrorVisible(boolean visible){
        if (visible){
            mDownArrow.setVisibility(View.VISIBLE);
        }else{
            mDownArrow.setVisibility(View.GONE);
        }
    }

    public void showToast(String string, SpannableString spannableString){
        mToastLayout.setVisibility(View.VISIBLE);
        if (!"".equals(string)){
            mToastLayout.setVisibility(View.VISIBLE);
            mToastTextView.setText(string);
        }else if (null != spannableString){
            mToastLayout.setVisibility(View.VISIBLE);
            mToastTextView.setText(spannableString);
        }

        if (mToastLayout.getVisibility() == View.VISIBLE){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideToast();
                }
            },5000);
        }
    }

    public void hideToast(){
        if (mToastLayout.getVisibility() == View.VISIBLE){
            mToastLayout.setVisibility(View.GONE);
            mToastTextView.setText("");
        }
    }

    //设置跳过片头片尾的节点 0片头 1片尾 percent进度
    public void setNode(int position, float percent){
        Log.i(TAG, "setNode: "+percent);
        //容错
        if (position == 0){
            headNodeView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mProgressbar.getLayoutParams();

            final int startX = params.leftMargin;
            final float finalPercent = percent;

//            int totalWidth = mProgressbar.getProgressDrawable().getBounds().width();
//            final int layoutWidth = seekBarLayout.getWidth();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    int totalWidth = mProgressbar.getProgressDrawable().getBounds().width();
                    int layoutWidth = seekBarLayout.getWidth();

                    int measuredWidth = seekBarLayout.getMeasuredWidth();

                    Log.i(TAG, "run: getnode totalWidth:"+totalWidth + " layoutWidth:"+layoutWidth + " measuredWidth:"+measuredWidth);
                    float density = getContext().getResources().getDisplayMetrics().density;
                    float dpi = getContext().getResources().getDisplayMetrics().densityDpi;

                    //获取不到长度的保险措施
                    if (totalWidth == 0){
                        if (density > 0){
                            totalWidth = (int) ((int) getResources().getDimension(R.dimen.progressbar_width)/density);
                        }
                    }
                    if (layoutWidth == 0){
                        if (density > 0){
                            layoutWidth = (int) ((int) getResources().getDimension(R.dimen.progressbar_layout_width)/density);
                        }

                    }

                    Log.i(TAG, "run: setNode totalWidth: "+totalWidth + " layoutWidth:"+layoutWidth + " density:"+density+" dpi:"+dpi);
                    RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) headNodeView.getLayoutParams();
                    Log.i(TAG, "setNode run: marginleft "+ headParams.leftMargin);
                    headParams.leftMargin = (int) ((layoutWidth - totalWidth)/2 + startX + totalWidth * finalPercent /1000);
                    Log.i(TAG, "setNode run: "+totalWidth + " " + layoutWidth + " " + finalPercent + " "+ startX + " "+ headParams.leftMargin);
                    headNodeView.setLayoutParams(headParams);
                }
            });

        }else{
            tailNodeView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mProgressbar.getLayoutParams();

            final int startX = params.rightMargin;

            final int layoutWidth = seekBarLayout.getWidth();
            final float finalPercent = percent;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    int totalWidth = mProgressbar.getProgressDrawable().getBounds().width();
                    int layoutWidth = seekBarLayout.getWidth();

                    float density = getContext().getResources().getDisplayMetrics().density;
                    float dpi = getContext().getResources().getDisplayMetrics().densityDpi;

                    //获取不到长度的保险措施
                    if (totalWidth == 0){
                        if (density > 0){
                            totalWidth = (int) ((int) getResources().getDimensionPixelOffset(R.dimen.progressbar_width)/density);
                        }
                    }
                    if (layoutWidth == 0){
                        if (density > 0){
                            layoutWidth = (int) ((int) getResources().getDimensionPixelOffset(R.dimen.progressbar_layout_width)/density);
                        }

                    }
                    RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) tailNodeView.getLayoutParams();
                    headParams.rightMargin = (int) ((layoutWidth - totalWidth)/2 + startX + totalWidth * finalPercent /1000);
                    tailNodeView.setLayoutParams(headParams);
                }
            });
        }

    }

    //隐藏跳过片头片尾节点
    public void hideNode(){
        headNodeView.setVisibility(INVISIBLE);
        tailNodeView.setVisibility(INVISIBLE);
    }
}