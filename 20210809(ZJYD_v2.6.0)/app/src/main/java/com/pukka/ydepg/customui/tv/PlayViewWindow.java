package com.pukka.ydepg.customui.tv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pukka.ydepg.view.PlayView;

public class PlayViewWindow extends PlayView {

    public PlayViewWindow(@NonNull Context context) {
        super(context);
    }

    public PlayViewWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayViewWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //mLoadingBar.setVisibility(View.GONE);
        if (null != mControlView){
            mControlView.setFocusable(false);
            mControlView.setVisibility(GONE);
        }
        if (null != mPlayer){
            mPlayer.setPlayControlView(null);
        }
        setResizeMode(PlayView.RESIZE_MODE_FIT);
    }

    public void setZOrderOnTop(boolean isOnTop){
        mPlayer.setZOrderOnTop(isOnTop);
    }

}

