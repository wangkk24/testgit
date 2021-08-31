package com.pukka.ydepg.launcher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;

import java.util.List;

/**
 * 焦点控制adapter
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.adapter.BaseFocusAdapter.java
 * @date: 2017-12-19 09:33
 * @version: V1.0 描述当前版本功能
 */
public abstract class BaseFocusAdapter<T, R extends BaseFocusViewHolder> extends RecyclerView.Adapter<R>  {
    protected List<T> datas;
    protected Context context;
    protected FocusHighlightHandler focusHighlight = new FocusScaleHelper.ItemFocusScale(itemFocusScaleType());
    protected ItemEventListener itemEventListener;

    public BaseFocusAdapter(Context context) {
        this.context = context;
    }

    public BaseFocusAdapter(Context context, ItemEventListener itemEventListener) {
        this(context);
        this.itemEventListener = itemEventListener;
    }

    @NonNull
    @Override
    public R onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceId(), parent, false);
        if (focusHighlight != null) {
            focusHighlight.onInitializeView(view);
        }
        return createViewHolder(view);
    }

    protected abstract R createViewHolder(View view);

    @Override
    public void onBindViewHolder(R holder, int position) {
        holder.bind(datas.get(position), position);
    }

    /**
     * item焦点缩放类型，如果要改变效果重写这个方法
     *
     * @return
     */
    public int itemFocusScaleType() {
        return FocusHighlight.ZOOM_FACTOR_SMALL;
    }

    public abstract int resourceId();

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }
}