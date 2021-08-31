package com.pukka.ydepg.customui.tv.autoscroll;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

import androidx.viewpager.widget.ViewPager;

import java.util.Timer;
import java.util.TimerTask;

public class AutoViewPager extends ViewPager {
    private static final String TAG = "AutoViewPager";
    private int currentItem;
    private boolean needStop = false;
    private long internalTime = 3*1000L;// 默认切换轮播时间，单位毫秒
    private Timer mTimer;
    private AutoTask mTimerTask;

    public AutoViewPager(Context context) {
        super(context);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(AutoViewPager viewPager, AutoScrollViewPagerAdapter adapter) {
        adapter.init(viewPager, adapter);
    }

    public void start() {
        needStop = false;
        onStop();
        if (null == mTimer) {
            mTimer = new Timer();
        }
        if (null == mTimerTask) {
            mTimerTask = new AutoTask();
        }
        mTimer.schedule(mTimerTask, internalTime, internalTime);

    }

    public void setInternalTime(Long time) {
        this.internalTime = time;
        start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isShown() || !needStop) {

                currentItem = getCurrentItem();
                if (null != getAdapter() && currentItem == getAdapter().getCount() - 1) {
                    currentItem = 0;
                } else {
                    currentItem++;
                }
                setCurrentItem(currentItem);
            } else {
                onStop();
            }
        }
    };
    private AutoHandler mHandler = new AutoHandler();

    public void updatePointView(int size) {
        if (getParent() instanceof AutoScrollViewPager) {
            AutoScrollViewPager pager = (AutoScrollViewPager) getParent();
            pager.initPointView(size);
        } else {
            Log.e(TAG, "parent view not be AutoScrollViewPager");
        }
    }

    public void onPageSelected(int position) {
        AutoScrollViewPager pager = (AutoScrollViewPager) getParent();
        pager.updatePointView(position);
    }

    private class AutoTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(runnable);
        }
    }

    private static final class AutoHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }

    public void onStop() {
        needStop = true;
        //先取消定时器
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (null != mTimerTask) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }


    public void onResume() {
        needStop = false;
        start();
    }

    public int getCurrentItem(){
        return currentItem;
    }

}
