package com.pukka.ydepg.moudule.featured.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by hasee on 2017/3/27.
 */

public class LimitScrollViewPager extends ViewPager {
    public LimitScrollViewPager(Context context) {
        super(context);
    }

    public LimitScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private boolean isCanScroll = false;



    public void setScanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER){
            return super.dispatchKeyEvent(event);
        }
        return false;
    }
}
