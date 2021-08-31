package com.pukka.ydepg.moudule.vod.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.presenter.ActorListPresenter;

import java.util.List;

/**
 *
 *搜索内容适配器
 * @author: ld
 * @date: 2017-12-19
 */
public class SearchContentAdapter extends RecyclerView.Adapter<SearchContentAdapter.ViewHolder> {

    private List<Content> mList;
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_MEDIUM);
    private ActorListPresenter mPresenter;
    private HorizontalGridView mActorVodList;

    public SearchContentAdapter(List<Content> list, ActorListPresenter presenter, HorizontalGridView actorVodList) {
        mList = list;
        mPresenter = presenter;
        mActorVodList = actorVodList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actor_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Content content = mList.get(position);
        if(position==0){
            holder.firstLinetv.setVisibility(View.INVISIBLE);
            if(null!=content.getVOD()){
                holder.yearmarktv.setText(content.getVOD().getProduceDate().substring(0, 4));
                holder.yearmarktv.setVisibility(View.VISIBLE);
            }else{
                holder.yearmarktv.setVisibility(View.GONE);
            }
        }else{
            holder.firstLinetv.setVisibility(View.VISIBLE);
            Content contentFirst = mList.get(position-1);
            if(null!=contentFirst.getVOD()&&null!=content.getVOD()&&!TextUtils.isEmpty(contentFirst.getVOD().getProduceDate()) &&!TextUtils.isEmpty(content.getVOD().getProduceDate())&&!contentFirst.getVOD().getProduceDate().substring(0, 4).equals(content.getVOD().getProduceDate().substring(0, 4))){
                holder.yearmarktv.setVisibility(View.VISIBLE);
                holder.yearmarktv.setText(content.getVOD().getProduceDate().substring(0, 4));
            }else{
                holder.yearmarktv.setVisibility(View.GONE);
            }
        }
        if (content != null) {
            VOD vod = content.getVOD();
            if (vod != null) {
                holder.itemView.setTag(vod);
                holder.nameText.setText(vod.getName());
                Picture picture = vod.getPicture();
                if (picture != null) {
                    List<String> posters = picture.getPosters();
                    if (posters != null) {
                        if (posters.size() != 0) {
                            String poster = posters.get(0);
                            if (!TextUtils.isEmpty(poster)) {
                                Glide.with(holder.itemView.getContext()).load(poster).into(holder.posterImageView);
                            }
                        }
                    }
                }
                String score = vod.getAverageScore();
                if(score.equals("0.0")){
                    score = "7.0";
                }
                holder.scoreText.setText(score);

                //设置显示右上角清晰度  HD/4K
                List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
                if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                    VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                    String definition = vodMediaFile.getDefinition();
                    if ("2".equals(definition)) {
                        holder.rightView.setBackgroundResource(R.drawable.movies_list_right_4k_icon);//4K
                    } else {
                        holder.rightView.setBackground(null);
                    }

                }else{
                    holder.rightView.setBackground(null);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addContents(List<Content> con) {
        mList.addAll(con);
        notifyItemRangeInserted(mList.size() - con.size(), con.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImageView;
        private TextView scoreText;
        private TextView nameText;
        private View rightView;
        private RelativeLayout BgView;
        private TextView firstLinetv;
        private TextView yearmarktv;

        public ViewHolder(final View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.movies_list_item_img);
            scoreText = (TextView) itemView.findViewById(R.id.movies_list_item_score);
            nameText = (TextView) itemView.findViewById(R.id.movies_list_item_name);
            rightView = itemView.findViewById(R.id.movies_list_poster_right_view);
            BgView= (RelativeLayout) itemView.findViewById(R.id.movies_list_item_img_bg);
            firstLinetv= (TextView) itemView.findViewById(R.id.firstline);
            yearmarktv= (TextView) itemView.findViewById(R.id.year_mark);
            itemView.setOnFocusChangeListener(mOnFocusChangeListener);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VOD vod = (VOD) v.getTag();
                    if(VodUtil.isMiguVod(vod)){
                        MiguQRViewPopWindow popWindow  = new MiguQRViewPopWindow(itemView.getContext(),vod.getCode(),MiguQRViewPopWindow.mSearchResultType);
                        popWindow.showPopupWindow(v);
                        return;
                    }
                    Intent intent;
                    if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
                        intent = new Intent(itemView.getContext(), ChildModeVodDetailActivity.class);
                    }else{
                        intent = new Intent(itemView.getContext(), NewVodDetailActivity.class);
                    }
                    intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                    intent.putExtra(NewVodDetailActivity.ORGIN_VOD,vod);
                    itemView.getContext().startActivity(intent);
                }
            });
            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(BgView);
            }
        }
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (mFocusHighlight != null) {
                mFocusHighlight.onItemFocused(v.findViewById(R.id.movies_list_item_img_bg), hasFocus);
            }
            if (hasFocus) {
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(true);
                v.findViewById(R.id.movies_list_item_name).setSelected(true);
                int position = mActorVodList.getChildAdapterPosition(v);
                mPresenter.showFocusNum((position + 1) + "");
            } else {
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(false);
                v.findViewById(R.id.movies_list_item_name).setSelected(false);
            }
        }
    };
}