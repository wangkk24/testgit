package com.pukka.ydepg.common.viewpage2;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ViewPage2Scroller extends Scroller {
    private int mDuration = 1500;

    public ViewPage2Scroller(Context context) {
        super(context);
    }

    public ViewPage2Scroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setmDuration(int time) {
        mDuration = time;
    }

    public int getmDuration() {
        return mDuration;
    }

}
