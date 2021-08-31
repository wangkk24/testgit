package com.pukka.ydepg.moudule.multviewforstb.multiview.module.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "RecyclerItemDecoration";


    /**
     * 一行有几个item
     */
    public static final int ITEM_NUM = 5;

    /**
     * 间隔
     */
    public static final int ITEM_INTERVAL = 26;

    public RecyclerItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        int position = parent.getResources().getDisplayMetrics().widthPixels;
        int position = parent.getChildAdapterPosition(view);
        if (parent.getChildCount() > 0) {
            if (position % ITEM_NUM == 0) {                  //最左边Item
                outRect.left = ITEM_INTERVAL / 2;
                outRect.right = ITEM_INTERVAL / 2;
            } else if (position % ITEM_NUM == (ITEM_NUM - 1)) { //最右边Item
                outRect.left = ITEM_INTERVAL / 2;
                outRect.right = ITEM_INTERVAL / 2;
            } else {                                        //中间Item
                outRect.left = ITEM_INTERVAL / 2;
                outRect.right = ITEM_INTERVAL / 2;
            }
        }
    }
}
