package com.pukka.ydepg.moudule.vrplayer.vrplayer.view;

import android.media.MediaPlayer;
import android.view.Surface;


import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.LogUtil;

import java.io.IOException;

/**
 * 功能描述
 *
 * @since 2020-07-21
 */
public class MediaPlayerWrapper implements MediaPlayer.OnPreparedListener {
    private static final String TAG = "MediaPlayerWrapper";

    protected MediaPlayer mPlayer;
    private MediaPlayer.OnPreparedListener mPreparedListener;
    private static final int STATUS_IDLE = 0;
    private static final int STATUS_PREPARING = 1;
    private static final int STATUS_PREPARED = 2;
    private static final int STATUS_STARTED = 3;
    private static final int STATUS_PAUSED = 4;
    private static final int STATUS_STOPPED = 5;
    private int mStatus = STATUS_IDLE;

    private boolean prepared = false; // 视频流准备好
    private boolean isStart;
    private int headTime;

    /**
     * 初始化播放器
     */
    public void init() {
        mStatus = STATUS_IDLE;


        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
    }

    public void setSurface(Surface surface) {
        if (getPlayer() != null) {
            getPlayer().setSurface(surface);
        }
    }

    public void openRemoteFile(String url) {
        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    public boolean hasPrePared() {
        return prepared;
    }

    public void setHasPrepared() {
        prepared = true;
    }

    public void prepare() {
        if (mPlayer == null) {
            return;
        }
        if (mStatus == STATUS_IDLE || mStatus == STATUS_STOPPED) {
            prepared = false;
            mPlayer.prepareAsync();
            mStatus = STATUS_PREPARING;
        }
    }

    public void stop() {
        if (mPlayer == null) {
            return;
        }
        if (mStatus == STATUS_STARTED || mStatus == STATUS_PAUSED) {
            LogUtil.logI(TAG, "stop VRPlayer");
            mPlayer.stop();
            mStatus = STATUS_STOPPED;
        }
    }

    public void pause() {
        if (mPlayer == null) {
            return;
        }
        if (mPlayer.isPlaying() && mStatus == STATUS_STARTED) {
            LogUtil.logI(TAG, "pause VRPlayer");
            mPlayer.pause();
            mStatus = STATUS_PAUSED;
        }
    }

    private void start() {
        if (mPlayer == null) {
            return;
        }
        if (mStatus == STATUS_PREPARED || mStatus == STATUS_PAUSED) {
            LogUtil.logI(TAG, "resume VRPlayer");
            if (mStatus == STATUS_PREPARED && isStart) {
                mPlayer.seekTo(headTime);
                isStart = false;
            }
            mPlayer.start();
            mStatus = STATUS_STARTED;
        }
    }

    public void setPreparedListener(MediaPlayer.OnPreparedListener preparedListener) {
        this.mPreparedListener = preparedListener;
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener errorListener) {
        mPlayer.setOnErrorListener(errorListener);
    }

    public void setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener videoSizeChangedListener) {
        mPlayer.setOnVideoSizeChangedListener(videoSizeChangedListener);
    }

    public void setOnInfoListener(MediaPlayer.OnInfoListener infoListener) {
        mPlayer.setOnInfoListener(infoListener);
    }

    public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener) {
        mPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener completionListener) {
        mPlayer.setOnCompletionListener(completionListener);
    }

    public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener seekCompleteListener) {
        mPlayer.setOnSeekCompleteListener(seekCompleteListener);
    }

    public int getCurrentPosition() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getCurrentPosition();
    }

    public int getDuration() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getDuration();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mStatus = STATUS_PREPARED;
        start();
        if (mPreparedListener != null) {
            mPreparedListener.onPrepared(mp);
        }
    }

    public void resume() {
        start();
    }

    public void destroy() {
        stop();
        if (mPlayer != null) {
            mPlayer.setSurface(null);
            mPlayer.release();
        }
        mPlayer = null;
    }

    public void reset() {
        stop();
        if (mPlayer != null) {
            mPlayer.reset();
            mStatus = STATUS_IDLE;
        }
    }

    public void seek(int endtime) {
        if (mPlayer != null) {
            mPlayer.seekTo(endtime);
        }
    }

    public void setSeekHead(int i) {
        isStart = true;
        headTime = i;
    }

    public interface startSeek {
        void seek();
    }
}
