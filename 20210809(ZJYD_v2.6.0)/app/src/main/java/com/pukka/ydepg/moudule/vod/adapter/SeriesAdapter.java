package com.pukka.ydepg.moudule.vod.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.BrotherSeasonVOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import java.util.List;

/**
 *
 *系列适配器
 * @author: ld
 * @date: 2017-12-19
 */
public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    private List<BrotherSeasonVOD> mBrotherSeasonVODList;
    private AlertDialog dialog;

    public SeriesAdapter(List<BrotherSeasonVOD> brotherSeasonVODs, AlertDialog dialog) {
        mBrotherSeasonVODList = brotherSeasonVODs;
        this.dialog = dialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_series_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BrotherSeasonVOD brotherSeasonVOD = mBrotherSeasonVODList.get(position);
        if (brotherSeasonVOD != null) {
            VOD vod = brotherSeasonVOD.getVOD();
            if (vod != null) {
                String name = vod.getName();
                if (!TextUtils.isEmpty(name)) {
                    holder.itemView.setTag(vod);
                    ((TextView) holder.itemView).setText(name);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBrotherSeasonVODList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VOD vod = (VOD) v.getTag();
                    if(VodUtil.isMiguVod(vod)){
                        MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(v.getContext(),vod.getCode(),MiguQRViewPopWindow.mSearchResultType);
                        popWindow.showPopupWindow(v);
                        return;
                    }
                    String id = vod.getID();
                    if (!TextUtils.isEmpty(id)) {
                        Intent intent = new Intent(v.getContext(), NewVodDetailActivity.class);
                        intent.putExtra(NewVodDetailActivity.VOD_ID, id);
                        intent.putExtra(NewVodDetailActivity.ORGIN_VOD,vod);
                        v.getContext().startActivity(intent);
                    }
                    dialog.dismiss();
                }
            });
        }
    }
}