package com.pukka.ydepg.moudule.catchup.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.customui.focusView.RelativeLayoutFocusView;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.adapter.BaseHolder;

import java.util.List;

import butterknife.BindView;

/**
 * 回看：频道适配器
 */
public class CatchUpContentAdapter extends BaseAdapter<ChannelDetail, CatchUpContentAdapter.ViewHolder> {
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale
            (FocusHighlight.ZOOM_FACTOR_SMALL);

    private OnHasFocusChangePageNumListener mOnHasFocusChangePageNumListener;

    public void setmOnHasFocusChangePageNumListener(OnHasFocusChangePageNumListener listener) {
        this.mOnHasFocusChangePageNumListener = listener;
    }

    public CatchUpContentAdapter(Context context, List<ChannelDetail> list) {
        super(context, list);
    }

    @Override
    protected CatchUpContentAdapter.ViewHolder createViewHolder(View view) {
        return new CatchUpContentAdapter.ViewHolder(view);
    }

    @Override
    protected int getAdapterLayoutId() {
        return R.layout.item_catchup_channel_list;
    }


    public class ViewHolder extends BaseHolder<ChannelDetail> {
        @BindView(R.id.movies_list_item_img)
        ImageView posterImageView;
        @BindView(R.id.movies_list_item_name)
        TextView nameText;
        @BindView(R.id.rela_channel)
        RelativeLayoutFocusView mItemFocusView;

        public ViewHolder(View itemView) {
            super(itemView);
            if (mFocusHighlight != null)
            {
                mFocusHighlight.onInitializeView(mItemFocusView);
            }
        }

        @Override
        public void bindView(ChannelDetail channelDetail, int position) {
            if (null != channelDetail){
                nameText.setText(channelDetail.getName());
                //加载台标
                String logUrl;
                if (null != channelDetail.getPicture() && null != channelDetail.getPicture().getPosters()
                        && channelDetail.getPicture().getPosters().size() > 0){
                    logUrl = channelDetail.getPicture().getPosters().get(0);
                    RequestOptions options  = new RequestOptions()
                            .placeholder(R.drawable.channel_default_logo)
                            .error(R.drawable.channel_default_logo);

                    Glide.with(itemView.getContext()).load(logUrl).apply(options).into(posterImageView);
                }else{
                    posterImageView.setImageResource(R.drawable.channel_default_logo);
                }
            }
            mItemFocusView.setTag(R.id.tag_first, position);
            mItemFocusView.setOnFocusListener(mOnFocusChangeListener);
        }
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            if (mFocusHighlight != null) {
                mFocusHighlight.onItemFocused(v, hasFocus);
            }
            v.setSelected(hasFocus);
            if (hasFocus) {
                if(null!=v.getTag(R.id.tag_first))
                mOnHasFocusChangePageNumListener.onItemFocusChangeNum((int)v.getTag(R.id
                    .tag_first) );
            }
        }
    };

    public interface OnHasFocusChangePageNumListener {
        void onItemFocusChangeNum(int position);
    }
}