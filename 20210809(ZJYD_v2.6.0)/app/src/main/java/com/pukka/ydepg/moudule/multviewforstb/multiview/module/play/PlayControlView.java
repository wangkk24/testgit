package com.pukka.ydepg.moudule.multviewforstb.multiview.module.play;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class PlayControlView extends FrameLayout {

    public PlayControlView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PlayControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PlayControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected abstract void initView(Context mContext);
    /**
     * 设置时间
     * @param currentTime 当前时间 毫秒
     * @param duration    总时长  毫秒
     * @param bufferedLength 已缓存的时长+当前时长 毫秒
     */
    public abstract void setTime(long currentTime, long duration, int bufferedLength);
    public abstract void doPause(boolean pause);



    /**
     * 格式化时间
     * @param timeMs 毫秒
     * @return
     */
    protected String parseSec(int timeMs) {
        int totalSeconds = timeMs % 1000 >= 500 ? (timeMs / 1000) + 1 : timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
