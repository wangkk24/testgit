package com.pukka.ydepg.launcher.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;


public class FocusLayout extends RelativeLayoutExt{
    private LayoutParams mFocusLayoutParams;
    private View mFocusView;

    public FocusLayout(Context context) {
        super(context);
        init(context);
    }

    public FocusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public View getFocusView() {
        return mFocusView;
    }

    private void init(Context context) {
        this.mFocusLayoutParams = new RelativeLayout.LayoutParams(0, 0);
        this.mFocusView = new View(context);
        this.mFocusView.setBackgroundResource(R.drawable.white_focus);
        this.addView(this.mFocusView, this.mFocusLayoutParams);
    }


    /**
     * 由于getGlobalVisibleRect获取的位置是相对于全屏的,所以需要减去FocusLayout本身的左与上距离,变成相对于FocusLayout的
     * @param rect
     */
    public void correctLocation(Rect rect) {
        Rect layoutRect = new Rect();
        this.getGlobalVisibleRect(layoutRect);
        rect.left -= layoutRect.left;
        rect.right -= layoutRect.left;
        rect.top -= layoutRect.top;
        rect.bottom -= layoutRect.top;
    }

    /**
     * 设置焦点view的位置,计算焦点框的大小
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setFocusLocation(int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;

        this.mFocusLayoutParams.width = width;
        this.mFocusLayoutParams.height = height;
        this.mFocusLayoutParams.leftMargin = left;
        this.mFocusLayoutParams.topMargin = top;
        this.mFocusView.layout(left, top, right, bottom);
    }
}
