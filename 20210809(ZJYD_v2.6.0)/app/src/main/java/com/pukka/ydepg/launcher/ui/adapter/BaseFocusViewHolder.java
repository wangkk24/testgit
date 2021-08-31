package com.pukka.ydepg.launcher.ui.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.adapter.BaseFocusViewHolder.java
 * @date: 2017-12-19 09:59
 * @version: V1.0 描述当前版本功能
 */
public abstract class BaseFocusViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseFocusViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }

    abstract void bind(T t,int position);
}