package com.pukka.ydepg.launcher.ui.adapter.topic;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.CollectionUtil;
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

/**
 * Created by jiaxing on 2017/8/18.
 */
public class TopicThreeAdapter extends RecyclerView.Adapter<TopicThreeAdapter.ViewHolder> {

    private List<VOD> mList;
    private Context context;
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_MEDIUM);

    public TopicThreeAdapter(List<VOD> list, Context context) {
        mList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_style_three_item_layout, parent, false);
        return new ViewHolder(view);
    }

    public void addData(List<VOD> list,boolean needClear) {
        int lastIndex ;
        if (needClear) {
            mList.clear();
            notifyDataSetChanged();
        }
        lastIndex = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(lastIndex, list.size());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VOD vod = mList.get(position);
        holder.itemView.setTag(vod);
        holder.nameText.setText(vod.getName());
        holder.posterImageView.setImageResource(R.drawable.default_poster);
        Picture picture = vod.getPicture();
        if (picture != null) {
            List<String> posters = picture.getTitles();
            if (CollectionUtil.isEmpty(posters)) {
                posters = picture.getPosters();
            }
            if (posters != null) {
                if (posters.size() != 0) {
                    String poster = posters.get(0);
                    if (!TextUtils.isEmpty(poster)) {
                        RequestOptions options  = new RequestOptions()
                                .skipMemoryCache(true)
                                .placeholder(R.drawable.default_poster);

                        Glide.with(holder.itemView.getContext()).load(poster).apply(options).into(holder.posterImageView);
                    }
                }
            }
        }

        String score = mList.get(position).getAverageScore();
        if(!TextUtils.isEmpty(score) && score.equals("0.0")){
            score = "7.0";
        }
        holder.scoreText.setText(score);

        //addLeftCorner(holder.relaFocusBg,SuperScriptUtil.getInstance().getSuperScriptByVod(vod, false));
    }

    public void addLeftCorner(RelativeLayout relativeLayout,String scriptUrl) {
        if (!TextUtils.isEmpty(scriptUrl)){
            ImageView ImgScript = new ImageView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                    .LayoutParams.WRAP_CONTENT, OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_30));
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_10),0,0,0);
            ImgScript.setLayoutParams(params);
            ImgScript.setScaleType(ImageView.ScaleType.FIT_START);
            relativeLayout.addView(ImgScript);
            Glide.with(context).load(scriptUrl).into(ImgScript);
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relaFocusBg;
        private ImageView posterImageView;
        private TextView scoreText;
        private TextView nameText;
        public ViewHolder(final View itemView) {
            super(itemView);
            relaFocusBg = (RelativeLayout) itemView.findViewById(R.id.movies_list_item_img_bg);
            posterImageView = (ImageView) itemView.findViewById(R.id.movies_list_item_img);
            scoreText = (TextView) itemView.findViewById(R.id.movies_list_item_score);
            nameText = (TextView) itemView.findViewById(R.id.movies_list_item_name);
            itemView.setOnFocusChangeListener(mOnFocusChangeListener);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VOD vod = (VOD) v.getTag();
                    //第三方apk
                    if (!TextUtils.isEmpty(vod.getCpId()) && CpRoute.isCp(vod.getCpId(),vod.getCustomFields())) {
                        CpRoute.goCp(vod.getCustomFields());
                    } else {
                        if (VodUtil.isMiguVod(vod)) {
                            MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(itemView.getContext(), vod.getCode(), MiguQRViewPopWindow.mSearchResultType);
                            popWindow.showPopupWindow(v);
                            return;
                        }

                        Intent intent;
                        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                            intent = new Intent(itemView.getContext(), ChildModeVodDetailActivity.class);
                        } else {
                            intent = new Intent(itemView.getContext(), NewVodDetailActivity.class);
                        }
                        intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                        intent.putExtra(NewVodDetailActivity.ORGIN_VOD, vod);
                        intent.putExtra("vod_exit", true);
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }
        }
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (mFocusHighlight != null) {
                mFocusHighlight.onItemFocused(v, hasFocus);
            }
            if (hasFocus) {
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(true);
                v.findViewById(R.id.movies_list_item_name).setSelected(true);
            } else {
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(false);
                v.findViewById(R.id.movies_list_item_name).setSelected(false);
            }
        }
    };
}