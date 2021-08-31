package com.pukka.ydepg.moudule.vod.playerController;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;

import java.util.ArrayList;
import java.util.List;

public class ControllerListItemAdapter extends RecyclerView.Adapter<ControllerListItemAdapter.ViewHolder> implements ControllerEpisodeAdapter{
    private static final String TAG = "ControllerListItemAdapt";

    private Context context;

    //是设置倍速的楼层
    private boolean isSetSpeed;

    //是设置跳过片头片尾的楼层
    private boolean isSetSikp;

    private View.OnKeyListener onKeyListener;

    //点击事件
    private View.OnClickListener onClickListener;

    //倍速列表
    private List<Float> speedList = new ArrayList<>();

    //片头片尾列表
    private List<String> skipList = new ArrayList<>();

    private float nowSpeed = 1f;

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);
    public final static String SkipOpen  = "开启";
    public final static String SkipClose = "关闭";

    public ControllerListItemAdapter(Context context, boolean isSetSpeed, boolean isSetSikp,float nowSpeed) {
        this.context = context;
        this.isSetSpeed = isSetSpeed;
        this.isSetSikp = isSetSikp;
        this.nowSpeed = nowSpeed;

        if (isSetSpeed){
            initSpeedList();
        }else if (isSetSikp){
            initSkip();
        }
    }

    public void setOnKeyListener(View.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_window_controller_item_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.playingIcon.setVisibility(View.GONE);
        holder.markIcon.setVisibility(View.GONE);

        if (isSetSpeed){
            holder.title.setText(speedList.get(position)+"");

            if (speedList.get(position) == nowSpeed){
                holder.title.setTextColor(context.getResources().getColor(R.color.controller_item_title_color_selected));
                holder.markIcon.setVisibility(View.VISIBLE);
            }else{
                holder.title.setTextColor(context.getResources().getColor(R.color.white_0));
                holder.markIcon.setVisibility(View.GONE);
            }

            Log.i(TAG, "onBindViewHolder: 设置倍速tag "+ position);
            holder.bg.setTag(position);
        }else if (isSetSikp){
            holder.title.setText(skipList.get(position));
            holder.bg.setTag(skipList.get(position));

            boolean skipOpen = SharedPreferenceUtil.getInstance().getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "",true);

            if (skipOpen && skipList.get(position).equals(SkipOpen)){
                holder.title.setTextColor(context.getResources().getColor(R.color.controller_item_title_color_selected));
                holder.markIcon.setVisibility(View.VISIBLE);
            }else if (!skipOpen && skipList.get(position).equals(SkipClose)){
                holder.title.setTextColor(context.getResources().getColor(R.color.controller_item_title_color_selected));
                holder.markIcon.setVisibility(View.VISIBLE);
            }else{
                holder.title.setTextColor(context.getResources().getColor(R.color.white_0));
                holder.markIcon.setVisibility(View.GONE);
            }
        }

//        if (null != onClickListener){
//            Log.i(TAG, "onBindViewHolder: bg设置click "+(null == onClickListener));
//            holder.bg.setOnClickListener(onClickListener);
//        }
    }

    @Override
    public int getItemCount() {
        if (isSetSpeed && speedList.size() > 0){
            return speedList.size();
        }else if (isSetSikp && skipList.size() > 0){
            return skipList.size();
        }else{
            return 0;
        }
    }

    //初始化倍速
    private void initSpeedList(){
        speedList.clear();
        String speeds = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_speed_list");
        if(TextUtils.isEmpty(speeds)){
            Log.e("VodEpisodesView", "TextUtils.isEmpty(speeds)");
            speedList.add(0.8f);
            speedList.add(1.0f);
            speedList.add(1.25f);
            speedList.add(1.5f);
            speedList.add(2.0f);
        }
        else{
            try {
                String[] speedarray = speeds.split(",");
                if (speedarray != null && speedarray.length == 5) {
                    Log.e("VodEpisodesView", "speedarray != null && speedarray.length == 5");
                    for(String speed:speedarray){
                        speedList.add(Float.parseFloat(speed));
                    }
                } else {
                    Log.e("VodEpisodesView", "speedarray != null && speedarray.length == 5 !!!");
                    speedList.add(0.8f);
                    speedList.add(1.0f);
                    speedList.add(1.25f);
                    speedList.add(1.5f);
                    speedList.add(2.0f);
                }
            }catch (Exception e){
                Log.e("VodEpisodesView", e.getMessage());
                speedList.add(0.8f);
                speedList.add(1.0f);
                speedList.add(1.25f);
                speedList.add(1.5f);
                speedList.add(2.0f);
            }
        }
    }

    //初始化跳过片头片尾
    private void initSkip(){
        skipList.clear();
        skipList.add(SkipOpen);
        skipList.add(SkipClose);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView playingIcon ;
        ImageView bg;
        TextView  title;
        ImageView markIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playingIcon = itemView.findViewById(R.id.controller_playing_icon);
            bg          = itemView.findViewById(R.id.controller_item_bg);
            title       = itemView.findViewById(R.id.controller_item_text);
            markIcon    = itemView.findViewById(R.id.markimg);
            bg.setOnKeyListener(onKeyListener);

            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }

            bg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.i(TAG, "bg onFocusChange: "+hasFocus);
                    if (mFocusHighlight != null) {
                        mFocusHighlight.onItemFocused(itemView, hasFocus);
                    }

                    markIcon.setSelected(hasFocus);


                    if (isSetSikp){
                        boolean skipOpen = SharedPreferenceUtil.getInstance().getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "",true);
                        if (hasFocus){
                            if (skipOpen && title.getText().equals(SkipOpen)){
                                title.setTextColor(context.getResources().getColor(R.color.white_0));
                            }else if (!skipOpen && title.getText().equals(SkipClose)){
                                title.setTextColor(context.getResources().getColor(R.color.white_0));
                            }
                        }else{
                            if (skipOpen && title.getText().equals(SkipOpen)){
                                title.setTextColor(context.getResources().getColor(R.color.controller_item_title_color_selected));
                            }else if (!skipOpen && title.getText().equals(SkipClose)){
                                title.setTextColor(context.getResources().getColor(R.color.controller_item_title_color_selected));
                            }
                        }
                    }else{
                        if (hasFocus){
                            if (Float.valueOf((String) title.getText()) == nowSpeed){
                                title.setTextColor(context.getResources().getColor(R.color.white_0));
                            }
                        }else{
                            if (Float.valueOf((String) title.getText()) == nowSpeed){
                                title.setTextColor(context.getResources().getColor(R.color.controller_item_title_color_selected));
                            }
                        }
                    }
                }
            });

        }
    }

    public void setItemOnClickListener(View.OnClickListener onClickListener) {
        Log.i(TAG, "setOnClickListener: bg设置click "+(null == onClickListener));
        this.onClickListener = onClickListener;
    }

    @Override
    public void setDataEpisodesSource(List<String> episodes) {

    }

    @Override
    public void setDataSource(List<Episode> episodes) {

    }

    @Override
    public void setSelectEpisode(Episode episode) {

    }
}
