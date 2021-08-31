package com.pukka.ydepg.moudule.vod.NewAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.launcher.view.CustomRoundImageView;

import java.util.List;

import butterknife.BindView;

public class PlayBackRecycleViewAdapter extends RecyclerView.Adapter <PlayBackRecycleViewAdapter.ViewHolder>{

    private List<VOD> vods;

    private Context mContext;

    //点击回调
    private OnitemClick onitemClick;

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_XSMALL);

    public PlayBackRecycleViewAdapter(List<VOD> vods, Context mContext) {
        this.vods = vods;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playback_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VOD vod = vods.get(position);
        holder.itemView.setTag(vod);

        String url = "";
        Picture picture = vod.getPicture();
        if (null != picture){
            List<String> list = picture.getPosters();
            if (null != list && list.size() >0){
                url = list.get(0);
            }
        }

        RequestOptions options = new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.default_poster_bg));
        GlideApp.with(mContext).load(url).apply(options).into(holder.poster);

        if (TextUtils.isEmpty(vod.getName())){
            holder.mantle.setVisibility(View.GONE);
            holder.name.setText("");
        }else{
            holder.mantle.setVisibility(View.VISIBLE);
            holder.name.setText(vod.getName());
        }

        //绑定点击事件
        if (onitemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onitemClick.onItemClick(holder.itemView);
                }
            });
        }

        //SuperScript
        String superScripturl = SuperScriptUtil.getInstance().getSuperScriptByVod(vod,false);
        if (!TextUtils.isEmpty(superScripturl)){
            GlideApp.with(mContext).load(superScripturl).into(holder.superScript);
            holder.superScript.setVisibility(View.VISIBLE);
        }else{
            holder.superScript.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return null == vods ? 0:vods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomRoundImageView poster;

        TextView name;

        ImageView superScript;

        View mantle;


        public ViewHolder(View itemView) {
            super(itemView);

            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }

            poster = itemView.findViewById(R.id.playback_list_item_poster);
            name = itemView.findViewById(R.id.playback_list_item_name);
            superScript = itemView.findViewById(R.id.vipimg);
            mantle = itemView.findViewById(R.id.playback_list_item_mantle);


            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mFocusHighlight != null) {
                        mFocusHighlight.onItemFocused(v, hasFocus);
                    }

                    itemView.setSelected(hasFocus);
                }
            });
        }
    }

    //点击事件的接口
    public interface OnitemClick {
        void onItemClick(View itemView);
    }

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
