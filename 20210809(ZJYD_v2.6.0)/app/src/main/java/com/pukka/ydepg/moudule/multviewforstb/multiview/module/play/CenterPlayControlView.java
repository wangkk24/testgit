package com.pukka.ydepg.moudule.multviewforstb.multiview.module.play;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;


public class CenterPlayControlView extends PlayControlView {

    View rootView;
    ImageView playState;
    TextView timeText;
    SeekBar seekBar;

    public CenterPlayControlView(@NonNull Context context) {
        super(context);
    }

    public CenterPlayControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterPlayControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(Context mContext) {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.play_control_center, this, true);
        playState = rootView.findViewById(R.id.play_state);
        seekBar = rootView.findViewById(R.id.seekbar);
        timeText = rootView.findViewById(R.id.time_text);
    }


    /**
     * 设置时间
     *
     * @param currentTime    当前时间 毫秒
     * @param duration       总时长  毫秒
     * @param bufferedLength 已缓存的时长+当前时长 毫秒
     */
    @Override
    public void setTime(long currentTime, long duration, int bufferedLength) {
        DebugLog.debug(TVPlayActivity.TAG, "[setTime] currentTime=" + currentTime + " duration=" + duration + " bufferedLength=" + (bufferedLength - currentTime));
        if (rootView != null) {

            seekBar.setMax((int) duration);
            seekBar.setSecondaryProgress(bufferedLength);
            seekBar.setProgress((int) currentTime);

            String time = parseSec((int) currentTime) + "/" + parseSec((int) duration);
            timeText.setText(time);
            DebugLog.debug(TVPlayActivity.TAG, "[setTime] " + time);
        } else {
            DebugLog.error(TVPlayActivity.TAG, "[setTime] rootView == null");
        }
    }

    @Override
    public void doPause(boolean pause) {
        playState.setSelected(pause);
    }
}
