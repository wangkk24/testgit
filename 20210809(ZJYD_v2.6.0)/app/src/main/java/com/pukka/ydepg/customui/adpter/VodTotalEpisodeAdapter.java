package com.pukka.ydepg.customui.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.TextUtil;

import java.util.List;

/**
 * Created by liudong on 2018/1/22.
 * 剧集adapter
 */

public class VodTotalEpisodeAdapter extends RecyclerView.Adapter<VodTotalEpisodeAdapter.ViewHolder> {

    private List<List<Episode>> mEpisodeList;
    private VodEpisodeAdapter mChooseEpisodeAdapter;
    private View mSelectView;
    private String currentSitNum;

    public VodTotalEpisodeAdapter(List<List<Episode>> episodes, VodEpisodeAdapter chooseAdapter, String sitNum) {
        mEpisodeList = episodes;
        mChooseEpisodeAdapter = chooseAdapter;
        currentSitNum = sitNum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_episode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<Episode> episodes = mEpisodeList.get(position);
        holder.itemView.setTag(episodes);
        int startNum=-1;
        int endNum=-1;
        if(null!=mEpisodeList.get(position)&&mEpisodeList.get(position).size()>0){
            startNum=Integer.parseInt(mEpisodeList.get(position).get(0).getSitcomNO());
            endNum=Integer.parseInt(mEpisodeList.get(position).get(mEpisodeList.get(position).size()-1).getSitcomNO());
        }
        if(startNum==-1||endNum==-1) {
            startNum = position * 35 + 1;
            endNum = startNum + episodes.size() - 1;
        }
        ((TextView) holder.itemView).setText((startNum == endNum) ? startNum+"" : startNum + "-" + endNum);
        if (mSelectView != null) {
            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.c21_color));
        }
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
                    if (hasFocus && !v.isSelected()) {
                        if (mSelectView != null) {
                            mSelectView.setSelected(false);
                            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.c24_color));
                        }
                        TextUtil.setNoSelectedMarqueen(true, (TextView) v);
                        v.setSelected(true);
                        mSelectView = v;
                        if (v.isSelected()) {
                            ((TextView) v).setTextColor(v.getContext().getResources().getColor(R.color.c21_color));
                        }
                        List<Episode> episodes = (List<Episode>) v.getTag();
                        mChooseEpisodeAdapter.setCurrentSitNum(currentSitNum);
                        mChooseEpisodeAdapter.setDatas(episodes);
                    }else{
                        TextUtil.setNoSelectedMarqueen(false, (TextView) v);
                    }
                }
            });
        }
    }
    private View.OnKeyListener mOnKeyListener;

    public void setOnKeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }
}
