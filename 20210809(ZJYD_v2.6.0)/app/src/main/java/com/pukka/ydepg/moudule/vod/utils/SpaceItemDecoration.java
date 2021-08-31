package com.pukka.ydepg.moudule.vod.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "CustomItemDecoration";
    private Context mContext;
    private float space;


    public SpaceItemDecoration(Context context ,float space) {
        this.mContext = context;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0 || pos == 6){
            outRect.left = mContext.getResources().getDimensionPixelOffset(R.dimen.margin_7);
        }else{
            outRect.left = (int) space;
        }
        outRect.top = mContext.getResources().getDimensionPixelOffset(R.dimen.margin_7);
        outRect.bottom = mContext.getResources().getDimensionPixelOffset(R.dimen.margin_7);
        outRect.right = (int) space;
    }

}
