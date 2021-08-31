package com.pukka.ydepg.launcher.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.launcher.ui.template.PHMTemplate;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.adapter.PHMViewHolder.java
 * @date: 2018-03-19 10:34
 * @version: V1.0 描述当前版本功能
 */
public class PHMViewHolder extends RecyclerView.ViewHolder {
    PHMTemplate itemView;

    public PHMViewHolder(PHMTemplate itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void setVisibility(boolean isVisible) {
        ViewGroup.LayoutParams param = itemView.getLayoutParams();
        if (null == param) {
            param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (isVisible) {
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        } else {
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        itemView.setLayoutParams(param);
    }
}