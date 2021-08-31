package com.pukka.ydepg.moudule.vrplayer.vrplayer.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.SeekBar;

import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.AreaSpecialProperties;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.MessageType;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.NonUIHandlerThread;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.module.VideoBean;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.CommonTools;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.LogUtil;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.view.MediaPlayerWrapper;

/**
 * 功能描述 播放控制器
 *
 * @author l00477311
 * @since 2020-08-06
 */
public class MediaController implements ILogic {
    private static final String TAG = "MediaController";

    private Context mContext;

    private MediaPlayerWrapper mMediaPlayerWrapper;

    private Handler mpCtrHandler = null;

    @Override
    public void init(Context context, Object object) {
        mContext = context;
        if (object instanceof MediaPlayerWrapper) {
            mMediaPlayerWrapper = (MediaPlayerWrapper) object;
        }
        mpCtrHandler = new Handler(NonUIHandlerThread.getNonUILooper(), new MpCtrCallBack());
    }

    @Override
    public void sendMessage(Message message) {
        mpCtrHandler.sendMessage(message);
    }

    @Override
    public void sendEmptyMessage(int what) {
        mpCtrHandler.sendEmptyMessage(what);
    }

    @Override
    public void sendMessageDelayed(Message message, long delay) {
        mpCtrHandler.sendMessageDelayed(message, delay);
    }

    @Override
    public void post(Runnable runnable) {
        mpCtrHandler.post(runnable);
    }

    @Override
    public void removeMessage(int what) {
        mpCtrHandler.removeMessages(what);
    }

    @Override
    public void removeCallbacks(Runnable runnable) {
        mpCtrHandler.removeCallbacks(runnable);
    }

    @Override
    public void removeAllMessage() {
        mpCtrHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void destroy() {
        this.mContext = null;
        removeAllMessage();
        mpCtrHandler = null;
        mMediaPlayerWrapper = null;
    }

    /**
     * 根据进度条上时间进行seek
     *
     * @param seekBar
     */
    public void seekBySeekBarTime(SeekBar seekBar) {
        LogUtil.logI(TAG, "Enter seekBySeekBarTime");
        int position = mMediaPlayerWrapper.getCurrentPosition();
        int duration = mMediaPlayerWrapper.getDuration();
        long seekBarPosition = seekBar.getProgress() * (long) duration / 1000;
        LogUtil.logI(TAG, "current position=" + position + " seekBarPosition = " + seekBarPosition);
        if (Math.abs((int) seekBarPosition - position) < (AreaSpecialProperties.getSingleSeekTime() / 2) * 1000) { // 2秒之内不用seek
            return;
        }
        Message msg = new Message();
        msg.what = MessageType.MPControlMessage.MESSAGE_MPCONTROL_SEEK_TO;
        msg.arg1 = (int) seekBarPosition;
        sendMessage(msg);
    }

    /**
     * 播放结束后操作
     *
     * @param videoBean
     */
    public void playNext(VideoBean videoBean) {
        if (TextUtils.isEmpty(videoBean.getReturnURL())) {
            return;
        }
        // 启动iptv加载页面

        Intent iptvIntent = new Intent();
        iptvIntent.setClassName("com.huawei.iptv.stb", "com.huawei.iptv.stb.ui.IPTVAPPActivity");
        iptvIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        iptvIntent.putExtra("jumpURL", videoBean.getReturnURL());
        CommonTools.startActivityByIntent(mContext, iptvIntent);
    }

    private class MpCtrCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MessageType.MPControlMessage.MESSAGE_MPCONTROL_SEEK_TO:
                    mMediaPlayerWrapper.getPlayer().seekTo(msg.arg1);
                    break;
            }
            return false;
        }
    }
}
