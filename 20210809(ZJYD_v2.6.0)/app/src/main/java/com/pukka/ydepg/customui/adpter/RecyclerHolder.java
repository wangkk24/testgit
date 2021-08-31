package com.pukka.ydepg.customui.adpter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 *  Created by ld on 2018/1/22.
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;

    public RecyclerHolder(View itemView) {
        super(itemView);
        //一般不会超过8个吧
        this.mViews = new SparseArray<View>(8);
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     */
    public RecyclerHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }



    public RecyclerHolder setImageResource(int viewId, int drawableId){
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

}