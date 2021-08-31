package com.pukka.ydepg.moudule.vrplayer.vrplayer.module.config;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.pukka.ydepg.R;

import java.util.ArrayList;
import java.util.List;

public class PickFileAdapter extends RecyclerView.Adapter<PickFileAdapter.PickFileViewHolder> {

    private List<FileBean> fileBeanList = new ArrayList<>();


    private Drawable fileDrawable;
    private Drawable fileDirDrawable;

    private FilePickClick filePickClick;

    public PickFileAdapter(Context context) {
        fileDrawable = ContextCompat.getDrawable(context, R.drawable.icon_file);
        fileDirDrawable = ContextCompat.getDrawable(context, R.drawable.icon_fileidr);
    }

    @NonNull
    @Override
    public PickFileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_filepick, viewGroup, false);
        return new PickFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PickFileViewHolder viewHolder, final int i) {
        final FileBean fileBean = fileBeanList.get(i);
        viewHolder.imageView.setImageDrawable(fileBean.isFile() ? fileDrawable : fileDirDrawable);
        viewHolder.name.setText(fileBean.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePickClick != null) {
                    filePickClick.onFileClick(fileBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileBeanList == null ? 0 : fileBeanList.size();
    }

    public void setFileBeanList(List<FileBean> fileBeanList) {
        this.fileBeanList = fileBeanList;
    }

    public void setFilePickClick(FilePickClick filePickClick) {
        this.filePickClick = filePickClick;
    }

    public class PickFileViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView name;

        public PickFileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
        }
    }

    public interface FilePickClick {
        void onFileClick(FileBean fileBean);
    }
}
