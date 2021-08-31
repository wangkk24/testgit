package com.pukka.ydepg.customui.tv.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;


/**
 * 当你使用滚动窗口焦点错乱的时候，就可以使用这个控件.
 * <p/>
 * 使用方法和滚动窗口是一样的，具体查看DEMO吧.
 * <p/>
 * 如果想改变滚动的系数，R.dimen.fading_edge
 * <p/>
 *
 * @author hailongqiu
 */
public class SmoothVorizontalScrollView extends ScrollView {
    private static final String TAG = SmoothVorizontalScrollView.class.getSimpleName();
    private boolean isScrollable;
    private int mScrollYDelta;
    private int mKeyCode;
    public static final int IDLE_STATE = 100;

    public static final int SCROLL_STATE = 100;
    private int preY;



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        mKeyCode = event.getKeyCode();
        return super.dispatchKeyEvent(event);
    }


    public SmoothVorizontalScrollView(Context context) {
        super(context, null, 0);
    }

    private int mFadingEdge;
    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public SmoothVorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SmoothVorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFadingEdge(int fadingEdge) {
        this.mFadingEdge = fadingEdge;
    }

    /**
     * 判断焦点是否是底部，是的话此不再滚动
     *
     * @param child
     * @param focused
     */
    @Override
    public void requestChildFocus(View child, View focused) {


        //涉及需求变动,暂时注释
//        if (focused instanceof ReflectRelativeLayout && ((ReflectRelativeLayout) focused).isFirstLine() && mKeyCode != KeyEvent.KEYCODE_DPAD_UP) {
//            if (getScrollY() > 0) {
//                scrollTo(0, 0);
//            }
//            isScrollable = false;
//        } else {
//            isScrollable = true;
//        }
//        if(mKeyCode == KeyEvent.KEYCODE_DPAD_DOWN && focused.getId() == R.id.rl_layoutOrder){ //如果焦点是是my的订购
//            isScrollable = false;
//        }else {
//            isScrollable = true;
//        }
        super.requestChildFocus(child, focused);
    }


    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
//        if (!isScrollable) {
//            return 0;
//        }
        if (getChildCount() == 0)
            return 0;
        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        int fadingEdge = mFadingEdge > 0 ? mFadingEdge : this.getResources().getDimensionPixelSize(R.dimen.fading_edge);
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        //
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
        mScrollYDelta = scrollYDelta;
        return scrollYDelta;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}
