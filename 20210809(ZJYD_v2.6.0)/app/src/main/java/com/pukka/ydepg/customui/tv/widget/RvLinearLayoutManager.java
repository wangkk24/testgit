package com.pukka.ydepg.customui.tv.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;


/**
 * support升级到27.1.1后，，recycleView 滚动问题
 * 2019.11.19
 */
public class RvLinearLayoutManager extends LinearLayoutManager {

    private RecyclerViewTV recyclerViewTV;

    public RvLinearLayoutManager(Context context,int orientation, boolean reverseLayout, RecyclerViewTV recyclerViewTV) {
        super(context, orientation, reverseLayout);
        this.recyclerViewTV = recyclerViewTV;
        setOrientation(orientation);
        setReverseLayout(reverseLayout);
    }
    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        if (recyclerViewTV.requestChildRectangleOnScreen(child,rect,immediate)){
            return true;
        }else{
            return super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private static class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }
    }
}
