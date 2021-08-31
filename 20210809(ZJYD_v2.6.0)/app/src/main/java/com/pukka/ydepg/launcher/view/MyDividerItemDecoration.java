package com.pukka.ydepg.launcher.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.MyDividerItemDecoration.java
 * @date: 2018-03-22 10:32
 * @version: V1.0 描述当前版本功能
 */


public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) < (parent.getLayoutManager().getItemCount() -1 )) {
            SuperLog.debug("MyDividerItemDecoration" ,"32");
            outRect.right = ScreenUtil.getDimension(OTTApplication.getContext(), R.dimen.margin_16);
        }else{
            SuperLog.debug("MyDividerItemDecoration" ,"0");
            outRect.right = 0;
        }
    }
}
