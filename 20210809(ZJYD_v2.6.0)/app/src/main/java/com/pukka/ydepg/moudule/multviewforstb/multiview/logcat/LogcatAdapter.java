package com.pukka.ydepg.moudule.multviewforstb.multiview.logcat;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.pukka.ydepg.R;

import java.util.ArrayList;
import java.util.List;

public class LogcatAdapter extends RecyclerView.Adapter<LogcatAdapter.LogcatViewHolder> {

    private static final String TAG = "LogcatAdapter";

    private List<LogcatItem> logcatList = new ArrayList<>();

    public LogcatAdapter() {
    }

    @NonNull
    @Override
    public LogcatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_logcat, viewGroup, false);
        return new LogcatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LogcatViewHolder viewHolder, final int position) {
        LogcatItem logcatItem = logcatList.get(position);
        viewHolder.logcatItem.setTextColor(logcatItem.isError() ? Color.RED : Color.WHITE);
        viewHolder.logcatItem.setText(logcatItem.getLogcat());
    }

    @Override
    public int getItemCount() {
        return (logcatList == null || logcatList.isEmpty()) ? 0 : logcatList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setLogcatList(List<LogcatItem> logcatList) {
        this.logcatList = logcatList;
    }

    public class LogcatViewHolder extends RecyclerView.ViewHolder {

        private TextView logcatItem;

        public LogcatViewHolder(@NonNull View itemView) {
            super(itemView);
            logcatItem = itemView.findViewById(R.id.item_logcat);
        }
    }
}
