package com.pukka.ydepg.launcher.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by jiaxing on 2017/8/10.
 */

public class MyVerticalScrollView extends FrameLayout {

    protected Scroller mScroller;
    private final Rect mTempRect = new Rect();

    public MyVerticalScrollView(Context context) {
        this(context, null);
    }

    public MyVerticalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVerticalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext(), new ScrollInterpolator());
    }

    public boolean isFinished() {
        return mScroller.isFinished();
    }

    public void scrollToChild(View child) {
        child.getDrawingRect(mTempRect);

        offsetDescendantRectToMyCoords(child, mTempRect);

        int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);

        if (scrollDelta == 0) {
            mScroller.startScroll(getScrollX(), getScrollY(), 0, child.getHeight(), 750);
        }else{
            mScroller.startScroll(getScrollX(), getScrollY(), 0, scrollDelta, 750);
        }
        invalidate();
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) return 0;

        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;

        int fadingEdge = getVerticalFadingEdgeLength();

        if (rect.top > 0) {
            screenTop += fadingEdge;
        }

        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }

        int scrollYDelta = 0;

        if (rect.bottom > screenBottom && rect.top > screenTop) {

            if (rect.height() > height) {
                scrollYDelta += (rect.top - screenTop);
            } else {
                scrollYDelta += (rect.bottom - screenBottom);
            }

            int bottom = getChildAt(0).getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

        } else if (rect.top < screenTop && rect.bottom < screenBottom) {

            if (rect.height() > height) {
                scrollYDelta -= (screenBottom - rect.bottom);
            } else {
                scrollYDelta -= (screenTop - rect.top);
            }

            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
        }
        return scrollYDelta;
    }


    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                final int range = getScrollRange();
                scrollBy(x - oldX, y - oldY);
            }
            postInvalidate();
        }
    }


    private static class ScrollInterpolator implements Interpolator {
        public ScrollInterpolator() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1;
        }
    }
}
