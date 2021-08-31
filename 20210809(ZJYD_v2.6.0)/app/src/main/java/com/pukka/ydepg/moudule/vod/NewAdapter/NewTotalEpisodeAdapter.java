package com.pukka.ydepg.moudule.vod.NewAdapter;

import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.moudule.vod.adapter.EpisodeAdapter;

import java.util.List;

public class NewTotalEpisodeAdapter extends RecyclerView.Adapter<NewTotalEpisodeAdapter.ViewHolder> {
    private static final String TAG = "NewTotalEpisodeAdapter";

    private List<List<String>> mEpisodeList;
    private EpisodeAdapter mChooseEpisodeAdapter;
    public View mSelectView;
    private RecyclerView episodeRecycle;

    private int  selectPosition=0;

    private int rate = 20;

    private int total;

    private View.OnKeyListener mOnKeyListener;

    //防止快速移动焦点错乱
    boolean canMove = true;

    public void setOnkeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public NewTotalEpisodeAdapter(List<List<String>> episodes, EpisodeAdapter chooseAdapter, int rate, RecyclerView recyclerView) {
        mEpisodeList = episodes;
        mChooseEpisodeAdapter = chooseAdapter;
        episodeRecycle = recyclerView;
        this.rate = rate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_total_epsiode_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: color 1");
        ((TextView) holder.itemView).setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white_40));
        ((TextView) holder.itemView).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        if(selectPosition == position){
            mSelectView = holder.itemView;
        }
        List<String> episodes = mEpisodeList.get(position);
        holder.itemView.setTag(episodes);
        holder.itemView.setTag(R.id.voddetail_total_position,position);
        if (rate == 20) {
            int startNum=-1;
            int endNum=-1;
            if(null!=mEpisodeList.get(position)&&mEpisodeList.get(position).size()>0){
                startNum=Integer.parseInt(mEpisodeList.get(position).get(0));
                endNum=Integer.parseInt(mEpisodeList.get(position).get(mEpisodeList.get(position).size()-1));
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
                startNum=Integer.parseInt(mEpisodeList.get(position).get(0));
                endNum=Integer.parseInt(mEpisodeList.get(position).get(mEpisodeList.get(position).size()-1));
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
        if (mSelectView != null && !mSelectView.isFocused() && selectPosition == position) {
            Log.i(TAG, "onBindViewHolder: color 2");
            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.white_0));
            ((TextView) mSelectView).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    public void setSelectPosition(int position){
        Log.i(TAG, "setSelectPosition: "+position);
        this.selectPosition=position;
        notifyDataSetChanged();
    }

    //展示下一组数据
    //返回值为是否有下一组
    public boolean showNext(){
        Log.i(TAG, "showNext:  "+ selectPosition);
        if ( selectPosition >=  (mEpisodeList.size() - 1)){
            //回到第一组
            Log.i(TAG, "showNext: show 0");
            List<String> episodes = mEpisodeList.get(0);
            mChooseEpisodeAdapter.setDataEpisodesSource(episodes);

            Log.i(TAG, "onBindViewHolder: color 3`1");
            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.white_40));
            ((TextView) mSelectView).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            setSelectPosition(0);

            return true;
        }else{

            List<String> episodes = mEpisodeList.get(selectPosition + 1);

            mChooseEpisodeAdapter.setDataEpisodesSource(episodes);

            Log.i(TAG, "onBindViewHolder: color 3");
            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.white_40));
            ((TextView) mSelectView).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));


            setSelectPosition(selectPosition + 1);

            return true;
        }
    }

    //展示上一组数据
    //返回值为是否有下一组
    public boolean showPre(){
        Log.i(TAG, "showPre:  "+ selectPosition);
        if ( selectPosition == 0){
            //展示最后一组
            Log.i(TAG, "showPre: show 0");
            List<String> episodes = mEpisodeList.get(mEpisodeList.size() - 1);
            mChooseEpisodeAdapter.setDataEpisodesSource(episodes);

            Log.i(TAG, "onBindViewHolder: color 3`2");
            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.white_40));
            ((TextView) mSelectView).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            setSelectPosition(mEpisodeList.size() - 1);

            return true;
        }else{

            List<String> episodes = mEpisodeList.get(selectPosition - 1);

            mChooseEpisodeAdapter.setDataEpisodesSource(episodes);

            Log.i(TAG, "onBindViewHolder: color 3`2");
            ((TextView) mSelectView).setTextColor(mSelectView.getContext().getResources().getColor(R.color.white_40));
            ((TextView) mSelectView).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));


            setSelectPosition(selectPosition - 1);

            return true;
        }
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
                    if (hasFocus) {

                        canMove = false;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                canMove = true;
                            }
                        },300);

                        if (mSelectView != null) {
                            mSelectView.setSelected(false);
                        }
                        ((TextView) mSelectView).setTextColor(v.getContext().getResources().getColor(R.color.white_40));
                        ((TextView) mSelectView).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                        v.setSelected(true);

                        mSelectView = v;
                        if (v.isSelected()) {
                            Log.i(TAG, "onBindViewHolder: color 4");
                            ((TextView) v).setTextColor(v.getContext().getResources().getColor(R.color.white_0));
                            ((TextView) v).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        }
                        List<String> episodes = (List<String>) v.getTag();
                        Log.i(TAG, "VoddetailEpsiodesUtils:  "+ JsonParse.object2String(episodes));
                        TextUtil.setMarqueen(true, (TextView) v);


                        mChooseEpisodeAdapter.setDataEpisodesSource(episodes);
                        selectPosition = (int) itemView.getTag(R.id.voddetail_total_position);
                    }else{
                        Log.i(TAG, "onBindViewHolder: color 5");
//                        ((TextView )v).setTextColor(mSelectView.getContext().getResources().getColor(R.color.white_60));
//                        ((TextView) v).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        TextUtil.setMarqueen(false, (TextView) v);
                    }
                }
            });
        }
    }

    public boolean isCanMove() {
        return canMove;
    }
}
