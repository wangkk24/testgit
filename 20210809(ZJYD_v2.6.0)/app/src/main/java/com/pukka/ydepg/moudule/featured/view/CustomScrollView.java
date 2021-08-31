package com.pukka.ydepg.moudule.featured.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by hasee on 2016/12/5.
 */

public class CustomScrollView extends ScrollView {
    private OnScrollListener mListener;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mListener) {
            mListener.onScrolled(l, t, oldl, oldt);
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mListener = listener;
    }

    public interface OnScrollListener {
        void onScrolled(int l, int t, int oldl, int oldt);
    }
}
