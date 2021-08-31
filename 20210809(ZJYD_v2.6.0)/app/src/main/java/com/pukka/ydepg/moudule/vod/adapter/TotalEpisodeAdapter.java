package com.pukka.ydepg.moudule.vod.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.TextUtil;

import java.util.List;

/**
 * 电视剧总集数概括适配器
 *
 * @author: ld
 * @date: 2017-12-19
 */

public class TotalEpisodeAdapter extends RecyclerView.Adapter<TotalEpisodeAdapter.ViewHolder> {

    private List<List<Episode>> mEpisodeList;
    private EpisodeAdapter mChooseEpisodeAdapter;
    private View mSelectView;
    private RecyclerView episodeRecycle;

    private int  selectPosition=-1;

    private boolean isChildMode;

    private int rate = 20;

    private int total;

    private View.OnKeyListener mOnKeyListener;


    public void setOnkeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public TotalEpisodeAdapter(List<List<Episode>> episodes, EpisodeAdapter chooseAdapter, int rate,boolean isChildMode, RecyclerView recyclerView) {
        mEpisodeList = episodes;
        mChooseEpisodeAdapter = chooseAdapter;
        episodeRecycle = recyclerView;
        this.rate = rate;
        this.isChildMode=isChildMode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(isChildMode?R.layout.item_child_detail_total_epsiode:R.layout.item_detail_total_epsiode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.itemView).setTextColor(holder.itemView.getContext().getResources().getColor(R.color.c24_color));
        if(selectPosition == position){
            mSelectView = holder.itemView;
        }
        List<Episode> episodes = mEpisodeList.get(position);
        holder.itemView.setTag(episodes);
        if (rate == 20) {
            int startNum=-1;
            int endNum=-1;
            if(null!=mEpisodeList.get(position)&&mEpisodeList.get(position).size()>0){
                startNum=Integer.parseInt(mEpisodeList.get(position).get(0).getSitcomNO());
                endNum=Integer.parseInt(mEpisodeList.get(position).get(mEpisodeList.get(position).size()-1).getSitcomNO());
            }
            if(startNum==-1||endNum==-1){
                startNum = position * rate + 1;
                endNum = startNum + episodes.size() - 1;
            }
            ((TextView) holder.itemView).setText((startNum == endNum) ? startNum+"" : startNum + "-" + endNum);
        } else {
            int startNum=-1;
            int endNum=-1;
            if(null!=mEpisodeList.get(position)&&mEpisodeList.get(position).size()>0){
                startNum=Integer.parseInt(mEpisodeList.get(position).get(0).getSitcomNO());
                endNum=Integer.parseInt(mEpisodeList.get(position).get(mEpisodeList.get(position).size()-1).getSitcomNO());
            }
            if(startNum==-1||endNum==-1) {
                startNum = total - position * rate;
                endNum = total - (position + 1) * rate + 1;
                if (endNum <= 0) {
                    endNum = 1;
                }
            }
            ((TextView) holder.itemView).setText((startNum == endNum) ? startNum+"" : startNum + "-" + endNum);
        }
        if (mSelectView != null&&!isChildMode) {
            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.c21_color));
        }
        if(isChildMode)
        {
            ((TextView) holder.itemView).setTextSize(TypedValue.COMPLEX_UNIT_PX,holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.T31_C21_Light_size));

            if (selectPosition == position)
            {
                ((TextView) holder.itemView).setTextColor(holder.itemView.getContext().getResources().getColor(R.color.c27_color));

            }
            else
            {
                ((TextView) holder.itemView).setTextColor(holder.itemView.getContext().getResources().getColor(R.color.c24_color));
            }
        }
    }

    public void setSelectPosition(int position){
        this.selectPosition=position;
        notifyDataSetChanged();
    }

    public int getSelectPosition(){
        return selectPosition;
    }

    @Override
    public int getItemCount() {
        return mEpisodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            if (mOnKeyListener != null) {
                itemView.setOnKeyListener(mOnKeyListener);
            }
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    selectPosition=-1;
                    if (hasFocus) {
                        if (mSelectView != null) {
                            mSelectView.setSelected(false);
                            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.c24_color));
                        }
                        v.setSelected(true);
                        mSelectView = v;
                        if (v.isSelected()) {
                            ((TextView) v).setTextColor(v.getContext().getResources().getColor(R.color.c21_color));
                        }
                        if(isChildMode){
                            v.setBackground(v.getContext().getResources().getDrawable(R.drawable.episodes_shape_selected));
                        }
                        List<Episode> episodes = (List<Episode>) v.getTag();
                        TextUtil.setMarqueen(true, (TextView) v);
                        if(!isChildMode){
                            if(rate == 20){
                                ViewGroup.LayoutParams layoutParams = episodeRecycle.getLayoutParams();
                                if (episodes.size() > 10) {
                                    layoutParams.height = OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.featured_detail_100dp);
                                } else {
                                    layoutParams.height = OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                                }
                                episodeRecycle.setLayoutParams(layoutParams);
                            }
                            else{
                                ViewGroup.LayoutParams pm = episodeRecycle.getLayoutParams();
                                if (episodes.size() <= 3) {
                                    pm.height = OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.featured_detail_55dp);
                                } else if (6 >= episodes.size() && episodes.size() > 3) {
                                    pm.height = OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.featured_detail_120dp);
                                } else {
                                    pm.height = OTTApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.details_varietys_view_height);
                                }
                                episodeRecycle.setLayoutParams(pm);
                            }
                        }
                        mChooseEpisodeAdapter.setDataSource(episodes);
                    }else{
                        if(isChildMode&&v.isSelected()){
                            ((TextView) v).setTextColor(v.getContext().getResources().getColor(R.color.c27_color));
                            v.setBackground(v.getContext().getResources().getDrawable(R.drawable.transparent_drawable));
                        }
                        TextUtil.setMarqueen(false, (TextView) v);
                    }
                }
            });
        }
    }
}
