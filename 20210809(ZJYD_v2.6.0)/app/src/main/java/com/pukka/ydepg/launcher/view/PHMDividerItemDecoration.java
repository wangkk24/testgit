package com.pukka.ydepg.launcher.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.launcher.ui.template.PHMTemplate;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.view.PHMDividerItemDecoration.java
 * @date: 2018-03-20 11:17
 * @version: V1.0 描述当前版本功能
 */


public class PHMDividerItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int positon = parent.getLayoutManager().getPosition(view);
        if (positon == 0) {
            if (view instanceof PHMTemplate) {
                PHMTemplate template = (PHMTemplate) view;
                if (template.hasTitle()) {
                    outRect.top = -16;
                } else {
                    outRect.top = 0;
                }
            }
        }
    }

}
