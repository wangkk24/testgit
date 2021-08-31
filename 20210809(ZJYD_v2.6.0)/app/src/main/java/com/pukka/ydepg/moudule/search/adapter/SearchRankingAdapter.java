package com.pukka.ydepg.moudule.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 排行榜适配器
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.adapter.SearchRankingAdapter.java
 * @author:xj
 * @date: 2017-12-19 09:47
 */

public class SearchRankingAdapter extends RecyclerView.Adapter<SearchRankingAdapter.ViewHolder> {

    Context mContext;
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);
    private List<VOD> mRankingList;

    private int currentPosition = -1;

    public SearchRankingAdapter(Context mContext, List<VOD> mRankingList) {
        this.mContext = mContext;
        this.mRankingList = mRankingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_ranking_result_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VOD vod = mRankingList.get(position);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) holder.mLayout.getLayoutParams();
        if (position / 4 == 0) {
            params.setMargins(0, 0, 0, (int) mContext.getResources().getDimension(R.dimen.margin_10));
        } else {
            params.setMargins(0, (int) mContext.getResources().getDimension(R.dimen.margin_10), 0, (int) mContext.getResources().getDimension(R.dimen.margin_10));
        }
        holder.mLayout.setLayoutParams(params);

        if (null != vod) {
            if (!TextUtils.isEmpty(vod.getUserScore())) {
                holder.mScore.setText(vod.getUserScore());
            } else {
                holder.mScore.setText("");
            }

            if (!TextUtils.isEmpty(vod.getName())) {
                holder.mName.setText(vod.getName());
            }

            Picture picture = vod.getPicture();
            if (null != picture) {
                List<String> posters = picture.getPosters();
                if (null != posters && posters.size() > 0) {
                    String pictureUrl = posters.get(0);
                    RequestOptions options = new RequestOptions()
                            .placeholder(mContext.getResources().getDrawable(R.drawable.default_poster))
                            .error(R.drawable.default_poster);
                    Glide.with(mContext).load(pictureUrl).apply(options).into(holder.mResultImage);
                    //ImageLoadManager.loadImageFromUrl(holder.mResultImage,pictureUrl,R.drawable.default_poster);
                } else {
                    holder.mResultImage.setImageResource(R.drawable.default_poster);
                }
            } else {
                holder.mResultImage.setImageResource(R.drawable.default_poster);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (VodUtil.isMiguVod(vod)) {
                        MiguQRViewPopWindow miguQRViewPopWindow = new MiguQRViewPopWindow(mContext, vod.getCode(), MiguQRViewPopWindow.mSearchResultType);
                        miguQRViewPopWindow.showPopupWindow(v);
                    } else {
                        //如果是第三方apk
                        if (!TextUtils.isEmpty(vod.getCpId()) && CpRoute.isCp(vod.getCpId(),vod.getCustomFields())) {
                            CpRoute.goCp(vod.getCustomFields());
                        } else {
                            //儿童版进儿童详情,普通进普通详情
                            Intent intent;
                            if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                                intent = new Intent(mContext, ChildModeVodDetailActivity.class);
                            } else {
                                intent = new Intent(mContext, NewVodDetailActivity.class);
                            }
                            intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                            intent.putExtra(NewVodDetailActivity.ORGIN_VOD, vod);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });

            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (mFocusHighlight != null) {
                        mFocusHighlight.onItemFocused(v, hasFocus);
                    }
                    holder.mImageBg.setSelected(hasFocus);

                    if (hasFocus) {
                        currentPosition = position;
                        holder.mName.setSelected(true);
                    } else {
                        currentPosition = -1;
                        holder.mName.setSelected(false);
                    }
                }
            });

            //SuperScript
            String url = SuperScriptUtil.getInstance().getSuperScriptByVod(vod,false);
            if (!TextUtils.isEmpty(url)){
                GlideApp.with(mContext).load(url).into(holder.superScript);
                holder.superScript.setVisibility(View.VISIBLE);
            }else{
                holder.superScript.setVisibility(View.GONE);
            }

        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return null == mRankingList ? 0 : mRankingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_result_item_image)
        ImageView mResultImage;

        @BindView(R.id.search_result_item_hd)
        ImageView mHD;

        @BindView(R.id.search_result_item_score)
        TextView mScore;

        @BindView(R.id.search_result_item_name)
        TextView mName;

        @BindView(R.id.movies_list_item_img_bg)
        RelativeLayout mImageBg;

        @BindView(R.id.search_vod_bg)
        RelativeLayout mLayout;

        @BindView(R.id.vipimg)
        ImageView superScript;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
