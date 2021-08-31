package com.pukka.ydepg.moudule.multviewforstb.multiview.module.main;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayListItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 55, view.getContext().getResources().getDisplayMetrics());
        if (position == 0) {
            left = 0;
        }
        outRect.set(left, 0, 0, 0);
    }
}
