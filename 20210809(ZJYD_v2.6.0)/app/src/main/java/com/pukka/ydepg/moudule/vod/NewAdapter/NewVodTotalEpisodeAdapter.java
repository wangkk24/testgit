package com.pukka.ydepg.moudule.vod.NewAdapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.TextUtil;
import com.pukka.ydepg.customui.adpter.VodEpisodeAdapter;
import com.pukka.ydepg.customui.adpter.VodTotalEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;

import java.util.List;
import java.util.Map;

public class NewVodTotalEpisodeAdapter extends RecyclerView.Adapter<NewVodTotalEpisodeAdapter.ViewHolder>{
    private List<List<String>> mEpisodeList;
    private NewVodEpisodeAdapter mChooseEpisodeAdapter;
    private View mSelectView;
    private String currentSitNum;
    private BrowseEpsiodesUtils utils;

    private boolean canMove = true;

    public NewVodTotalEpisodeAdapter(List<List<String>> episodes, BrowseEpsiodesUtils utils,NewVodEpisodeAdapter chooseAdapter, String sitNum) {
        this.utils = utils;
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
        List<String> episodes = mEpisodeList.get(position);
        holder.itemView.setTag(episodes.get(0));
        int startNum=-1;
        int endNum=-1;
        if(null!=mEpisodeList.get(position)&&mEpisodeList.get(position).size()>0){
            startNum=Integer.parseInt(mEpisodeList.get(position).get(0));
            endNum=Integer.parseInt(mEpisodeList.get(position).get(mEpisodeList.get(position).size()-1));
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

    public boolean isCanMove() {
        return canMove;
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

                    if (hasFocus){
                        canMove = false;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                canMove = true;
                            }
                        },300);
                    }

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
                        String index = (String) v.getTag();
                        int offset = Integer.valueOf(index) - 1;
                        if (!utils.isPositive()){
                            int total = utils.getTotal();
                            offset = total - offset;
                        }

                        Map<String,List<String>> map = utils.getMapForIndex();
                        List<String> indexList = map.get(index);
                        mChooseEpisodeAdapter.setCurrentSitNum(currentSitNum);
                        mChooseEpisodeAdapter.setDatas(indexList);
                        episodeTag = index;

                        utils.getEpisodeList(35, offset, new VoddetailEpsiodePresenter.GetEpisodeListCallback() {
                            @Override
                            public void getEpisodeListSuccess(int total, List<Episode> episodes) {
                                if (episodes.size() > 0 && episodeTag.equals(episodes.get(0).getSitcomNO())){
                                    mChooseEpisodeAdapter.setCurrentSitNum(currentSitNum);
                                    mChooseEpisodeAdapter.setEpisodes(episodes);
                                }
                            }

                            @Override
                            public void getEpisodeListFail() {

                            }
                        });
                    }else{
                        TextUtil.setNoSelectedMarqueen(false, (TextView) v);
                    }
                }
            });
        }
    }
    //用于标记当前请求到的数据，防止数据错位
    private String episodeTag = "";

    private View.OnKeyListener mOnKeyListener;

    public void setOnKeyListener(View.OnKeyListener listener) {
        mOnKeyListener = listener;
    }
}
