package com.pukka.ydepg.moudule.vod.NewAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NewVoddetailListWithoutTitleAdapter extends RecyclerView.Adapter <NewVoddetailListWithoutTitleAdapter.ViewHolder> {

    private List<VOD> list;

    private Context context;

    private int index;

    private NewVoddetailListAdapter.onClickListenr listenr;

    //当前页面是否有子集列表
    private boolean hasEpisodes = false;

    //防止快速移动焦点错乱
    private boolean canMove = true;

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_XSMALL);

    public NewVoddetailListWithoutTitleAdapter(Context context, List<VOD> list,int index ,boolean hasEpisodes) {
        this.context = context;
        this.list = list;
        this.index = index;
        this.hasEpisodes = hasEpisodes;
    }

    public void setListenr(NewVoddetailListAdapter.onClickListenr listenr) {
        this.listenr = listenr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voddetail_without_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        VOD vod = list.get(position);
        String url = "";
        Picture picture = vod.getPicture();
        if (null != picture){
            List<String> list = picture.getChannelBlackWhites();
            if (null != list && list.size() >0){
                url = list.get(0);
            }
        }

        RequestOptions options = new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.default_poster_bg));
        GlideApp.with(context).load(url).apply(options).into(holder.bg);

        boolean isShow = SuperScriptUtil.getInstance().getSuperScriptSwitch(SuperScriptUtil.SUPERSCRIPT_RECENT);
        if (isShow){
            String superScriptUrl = SuperScriptUtil.getInstance().getSuperScriptByVod(vod,SuperScriptUtil.SCRIPT_ICON);
            if (!TextUtils.isEmpty(superScriptUrl)){
                GlideApp.with(context).load(superScriptUrl).into(holder.superScript);
                holder.superScript.setVisibility(View.VISIBLE);
            }else{
                holder.superScript.setVisibility(View.GONE);
            }
        }else{
            holder.superScript.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return null == list?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        ImageView bg;
        ImageView superScript;

        public ViewHolder(View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.item_bg);
            layout = itemView.findViewById(R.id.item_bg_layout);
            superScript = itemView.findViewById(R.id.vipimg);

            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
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

                    if (mFocusHighlight != null) {
                        mFocusHighlight.onItemFocused(v, hasFocus);
                    }
                    layout.setSelected(hasFocus);

                    if (hasFocus && index != 0){
                        EventBus.getDefault().post(new VoddetailScrollEvent(index));
                    }

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int)v.getTag();
                    if (list.size()>=position){
                        VOD vod = list.get(position);

                        if (VodUtil.isMiguVod(vod)) {
                            if (null != listenr){
                                boolean ismigu = VodUtil.isMiguVod(vod);
                                listenr.onClick(ismigu);
                            }
                            return;
                        }

                        Intent intent = new Intent(context, NewVodDetailActivity.class);
                        intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                        context.startActivity(intent);

                        if (null != listenr){
                            boolean ismigu = VodUtil.isMiguVod(vod);
                            listenr.onClick(ismigu);
                        }
                    }

                }
            });

            itemView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (!canMove){
                        return true;
                    }

                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP && index == 0) {
                        EventBus.getDefault().post(new VoddetailScrollEvent(index));
                        return true;
                    }

                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        int position = (int) v.getTag();
                        if (position == list.size() - 1){
                            return true;
                        }
                    }

                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        int position = (int) v.getTag();
                        if (position == 0){
                            return true;
                        }
                    }

                    //除子集列表以外，第一个列表向上落焦到子集列表
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP && index == 1 && hasEpisodes){
                        EventBus.getDefault().post(new VoddetailScrollEvent(VoddetailScrollEvent.EpisodeListRequesetFocus));
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
