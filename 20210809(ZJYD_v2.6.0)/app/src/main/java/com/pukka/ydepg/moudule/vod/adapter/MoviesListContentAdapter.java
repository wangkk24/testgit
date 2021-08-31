package com.pukka.ydepg.moudule.vod.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.utils.ScoreControl;
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
import com.pukka.ydepg.moudule.vod.presenter.MoviesListPresenter;

import java.util.List;

/**
 * 点播vod列表适配器
 *
 * @author: ld
 * @date: 2017-12-19
 */
public class MoviesListContentAdapter extends RecyclerView.Adapter<MoviesListContentAdapter.ViewHolder> {

    private List<VOD> mList;
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_MEDIUM);
    private VerticalGridView mVerticalGridView;
    private TextView mTvCurrentNum;
    private MoviesListPresenter mPresenter;

    private String subjectId;

    public MoviesListContentAdapter(List<VOD> list, VerticalGridView verticalGridView, TextView tv, MoviesListPresenter presenter) {
        mList = list;
        this.mVerticalGridView = verticalGridView;
        this.mTvCurrentNum = tv;
        this.mPresenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies_list, parent, false);
        return new ViewHolder(view);
    }

    public void addData(List<VOD> list, boolean needClear, String subjectId) {
        int lastIndex = 0;
        this.subjectId = subjectId;
        if (needClear) {
            mList.clear();
            notifyDataSetChanged();
        }
        lastIndex = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(lastIndex, list.size());
    }

    public void clean() {
        if (mList.size() != 0) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VOD vod = mList.get(position);
        holder.posterImageView.setImageResource(R.drawable.default_poster);
        Picture picture = null;
        if (null != vod) {
            holder.itemView.setTag(vod);
            holder.nameText.setText(vod.getName());
            picture = vod.getPicture();

            //superScript
            String superScriptUrl = SuperScriptUtil.getInstance().getSuperScriptByVod(vod,false);
            if (!TextUtils.isEmpty(superScriptUrl)){
                GlideApp.with(holder.itemView.getContext()).load(superScriptUrl).into(holder.superScript);
                holder.superScript.setVisibility(View.VISIBLE);
            }else{
                holder.superScript.setVisibility(View.GONE);
            }

        }
        if (picture != null) {
            List<String> posters = picture.getPosters();
            if (posters != null) {
                if (posters.size() != 0) {
                    String poster = posters.get(0);
                    if (!TextUtils.isEmpty(poster)) {
                        RequestOptions options = new RequestOptions()
                                .skipMemoryCache(true)
                                .placeholder(R.drawable.default_poster);

                        Glide.with(holder.itemView.getContext()).load(poster).apply(options).into(holder.posterImageView);
                    }
                }
            }
        }

        holder.rightIcon.setVisibility(View.INVISIBLE);
        holder.rightIcon.setBackground(null);
        //String type = vod.getVODType();
        holder.rightIcon.setVisibility(View.VISIBLE);
        List<VODMediaFile> vodMediaFiles = null;
        if (null != vod) {
            vodMediaFiles = vod.getMediaFiles();
        }
        if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
            VODMediaFile vodMediaFile = vodMediaFiles.get(0);
            String definition = vodMediaFile.getDefinition();
            if ("2".equals(definition)) {
                holder.rightIcon.setBackgroundResource(R.drawable.movies_list_right_4k_icon);//4K
            }
        }

        if (null != vod) {
            if (ScoreControl.newNeedShowScore(vod)) {
                String score = mList.get(position).getAverageScore();
                if (!TextUtils.isEmpty(score) && score.equals("0.0")) {
                    score = "7.0";
                }
                holder.scoreText.setText(score);
                holder.scoreText.setVisibility(View.VISIBLE);
            } else {
                holder.scoreText.setVisibility(View.GONE);
            }
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
        private ImageView posterImageView;
        private TextView scoreText;
        private TextView nameText;
        private View rightIcon;
        private ImageView superScript;

        public ViewHolder(final View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.movies_list_item_img);
            scoreText = (TextView) itemView.findViewById(R.id.movies_list_item_score);
            nameText = (TextView) itemView.findViewById(R.id.movies_list_item_name);
            rightIcon = itemView.findViewById(R.id.movies_list_poster_right_view);
            superScript = itemView.findViewById(R.id.vipimg);
            itemView.setOnFocusChangeListener(mOnFocusChangeListener);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VOD vod = (VOD) v.getTag();
                    if (CpRoute.isCp(vod.getCpId(),vod.getCustomFields())) {
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
                        if (!TextUtils.isEmpty(subjectId))
                            intent.putExtra(NewVodDetailActivity.SUBJECT_ID, subjectId);
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
                mPresenter.setOldContentFocusView(v);
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(true);
                v.findViewById(R.id.movies_list_item_name).setSelected(true);
                int position = mVerticalGridView.getChildAdapterPosition(v);
                mTvCurrentNum.setText((position + 1) + "");
            } else {
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(false);
                v.findViewById(R.id.movies_list_item_name).setSelected(false);
            }
        }
    };
}