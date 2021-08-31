package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.Product;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.event.ProductTransitionListFocusEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OrderTransitionListAdapter extends RecyclerView.Adapter <OrderTransitionListAdapter.ViewHolder>{

    private List<VOD> recommendVodList;

    private Context mContext;

    //点击回调
    private OnitemClick onitemClick;

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);

    public OrderTransitionListAdapter(List<VOD> recommendVodList, Context mContext) {
        this.recommendVodList = recommendVodList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_transition_list, null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VOD vod = recommendVodList.get(position);

        //vod名称
        if ( null!= vod){
            if (TextUtils.isEmpty(vod.getName())){
                holder.shadow.setVisibility(View.GONE);
                holder.title.setText("");
            }else{
                holder.shadow.setVisibility(View.VISIBLE);
                holder.title.setText(vod.getName());
            }

            //SuperScript
            String superScripturl = SuperScriptUtil.getInstance().getSuperScriptByVod(vod,false);
            if (!TextUtils.isEmpty(superScripturl)){
                GlideApp.with(holder.itemView.getContext()).load(superScripturl).into(holder.superScript);
                holder.superScript.setVisibility(View.VISIBLE);
            }else{
                holder.superScript.setVisibility(View.GONE);
            }
        }

        //vod封面海报
        Picture picture = null;
        if (null != vod){
            picture = vod.getPicture();
        }
        if (picture != null) {
            List<String> posters = picture.getPosters();
            if (posters != null) {
                if (posters.size() != 0) {
                    String poster = posters.get(0);
                    if (!TextUtils.isEmpty(poster)) {
                        RequestOptions options  = new RequestOptions()
                                .skipMemoryCache(true)
                                .placeholder(R.drawable.default_poster);

                        Glide.with(holder.itemView.getContext()).load(poster).apply(options).into(holder.cover);
                    }
                }
            }
        }

        //绑定点击事件
        if (onitemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onitemClick.onItemClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return null != recommendVodList ? recommendVodList.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout coverLayout;

        ImageView cover;

        TextView title;

        ImageView superScript;

        View shadow;

        public ViewHolder(View itemView) {

            super(itemView);
            coverLayout = (RelativeLayout) itemView.findViewById(R.id.order_transition_list_item_layout);
            cover = (ImageView) itemView.findViewById(R.id.order_transition_list_item_cover);
            title = (TextView) itemView.findViewById(R.id.order_transition_list_item_title);
            shadow = itemView.findViewById(R.id.shadow_layout);
            superScript = itemView.findViewById(R.id.vipimg);

            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        EventBus.getDefault().post(new ProductTransitionListFocusEvent());
                    }

                    if (mFocusHighlight != null) {
                        mFocusHighlight.onItemFocused(v, hasFocus);
                    }

                    coverLayout.setSelected(hasFocus);
                }
            });
        }
    }

    public void setOnitemClickLintener (OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    //点击事件的接口
    public interface OnitemClick {
        void onItemClick(int position);
    }
}
