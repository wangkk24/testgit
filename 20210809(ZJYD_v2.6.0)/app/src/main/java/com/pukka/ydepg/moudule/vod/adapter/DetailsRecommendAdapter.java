package com.pukka.ydepg.moudule.vod.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.utils.CornersTransform;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil;

import java.util.List;

/**
 *
 *推荐适配器
 * @author: ld
 * @date: 2017-12-19
 */
public class DetailsRecommendAdapter extends RecyclerView.Adapter<DetailsRecommendAdapter.ViewHolder> {
    private static final String TAG = "DetailsRecommendAdapter";

    private List<VOD> mList;

    private String recommendType;

    private DetailsRecommendListener detailsRecommendListener;

    public void setDetailsRecommendListener(DetailsRecommendListener detailsRecommendListener) {
        this.detailsRecommendListener = detailsRecommendListener;
    }

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(1);

    public DetailsRecommendAdapter(List<VOD> list,String recommendType) {
        mList = list;
        this.recommendType=recommendType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //item_child_movies_list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_movies_list, parent, false);
        return new ViewHolder(view);
    }

    public void addData(List<VOD> list,boolean needClear) {
        if (needClear) {
            mList.clear();
            notifyDataSetChanged();
        }
        mList.addAll(list);
        notifyItemRangeInserted(mList.size() - list.size(), list.size());
    }

    public void clean() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(mList.get(position));
        holder.nameText.setText(mList.get(position).getName());
        Picture picture = mList.get(position).getPicture();
        if (picture != null) {
            List<String> posters = picture.getPosters();
            if (posters != null) {
                if (posters.size() != 0) {
                    String poster = posters.get(0);
                    if (!TextUtils.isEmpty(poster)) {
                        RequestOptions options  = new RequestOptions()
                                .transform(new CornersTransform(8));

                        Glide.with(holder.itemView.getContext()).load(poster).apply(options).into(holder.posterImageView);
                    }
                }
            }
        }

        VOD vod = mList.get(position);
        if (null != vod){
            //superScript
            boolean isShow = SuperScriptUtil.getInstance().getSuperScriptSwitch(SuperScriptUtil.SUPERSCRIPT_CHILD);
            if (isShow){
                String superScriptUrl = SuperScriptUtil.getInstance().getSuperScriptByVod(vod,SuperScriptUtil.SCRIPT_ICON);
                if (!TextUtils.isEmpty(superScriptUrl)){
                    GlideApp.with(holder.itemView.getContext()).load(superScriptUrl).into(holder.superScript);
                    holder.superScript.setVisibility(View.VISIBLE);
                }else{
                    holder.superScript.setVisibility(View.GONE);
                }
            }else{
                holder.superScript.setVisibility(View.GONE);
            }
        }else{
            holder.superScript.setVisibility(View.GONE);
        }

        String score = mList.get(position).getAverageScore();
        holder.scoreText.setText(score);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImageView;
        private TextView scoreText;
        private TextView nameText;
        private ImageView superScript;

        public ViewHolder(final View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.movies_list_item_img);
            scoreText = (TextView) itemView.findViewById(R.id.movies_list_item_score);
            nameText = (TextView) itemView.findViewById(R.id.movies_list_item_name);
            superScript = (ImageView) itemView.findViewById(R.id.vipimg);
            itemView.setOnFocusChangeListener(mOnFocusChangeListener);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VOD vod = (VOD) v.getTag();
                    if(VodUtil.isMiguVod(vod)){
//                        EpgToast.showToast(itemView.getContext(),"请到咪咕爱看app进行观看!");
                        MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(itemView.getContext(),vod.getCode(),MiguQRViewPopWindow.mSearchResultType);
                        popWindow.showPopupWindow(v);
                        return;
                    }
                    Intent intent;
                    if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                        Activity current = OTTApplication.getContext().getCurrentActivity();
                        if(current instanceof ChildModeVodDetailActivity){
                                current.finish();
                        }
                        intent = new Intent(itemView.getContext(), ChildModeVodDetailActivity.class);
                    }else{
                        intent = new Intent(itemView.getContext(), NewVodDetailActivity.class);
                    }
                    intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                    intent.putExtra(NewVodDetailActivity.ORGIN_VOD,vod);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_APP_POINTED_ID, RemixRecommendUtil.APPPINEDID_CHILD);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_RECOMMEND_TYEP, UBDRecommendImpression.recommendType_child);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_SCENE_ID,       UBDRecommendImpression.sceneId_child);

                    if (null != vod.getFeedback()){
                        //传递九天播放上报url
                        if (!TextUtils.isEmpty(vod.getFeedback().getPlay_tracker())){
                            intent.putExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL, vod.getFeedback().getPlay_tracker());
                        }
                        if (!TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                            //九天点击上报
                            SuperLog.debug(TAG,"jiutian click");
                            JiutianService.reportClick(vod.getFeedback().getClick_tracker());
                        }
                        if (!TextUtils.isEmpty(vod.getItemid())){
                            intent.putExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID, vod.getItemid());
                        }
                    }

                    itemView.getContext().startActivity(intent);
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
            if(detailsRecommendListener != null){
                detailsRecommendListener.onFocus(hasFocus);
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

    public interface DetailsRecommendListener{
        void onFocus(boolean hasFocus);
    }
}
