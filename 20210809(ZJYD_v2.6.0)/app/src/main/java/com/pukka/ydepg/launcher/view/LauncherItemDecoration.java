package com.pukka.ydepg.launcher.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.LauncherItemDecoration.java
 * @date: 2018-01-22 9:20
 * @version: V1.0 描述当前版本功能
 */


public class LauncherItemDecoration extends RecyclerView.ItemDecoration {
    private int mMarginBottom;

    public LauncherItemDecoration(int marginBottom) {
        this.mMarginBottom = marginBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mMarginBottom;
    }
}
