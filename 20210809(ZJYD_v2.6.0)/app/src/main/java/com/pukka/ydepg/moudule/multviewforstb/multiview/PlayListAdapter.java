package com.pukka.ydepg.moudule.multviewforstb.multiview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.config.KPIConstant;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllVideoConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.PlayConstant;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.TVPlayActivity;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.play.freecamera.FreeCameraPlayActivity;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.RecyclerItemDecoration;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.focus.FocusBorder;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>{

    private static final String TAG = "PlayListAdapter";

    private AllConfig allConfig;

    private Context context;

    /**
     * 屏幕宽度
     */
    protected int phoneWidth;

   /**
     * 当前的RecyclerView滑动状态
     */
    private boolean isScrolling = false;

    private final float SCALEN = 1.0f;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            DebugLog.error(TAG,"newState="+newState);
            if (newState ==RecyclerView.SCROLL_STATE_IDLE){
                isScrolling = false;
                onMoveFocusBorder(currentFocusView,SCALEN);
            }
            if (newState==RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING){
                isScrolling = true;
            }
        }
    };

    /**
     * 焦点框
     */
    private FocusBorder mFocusBorder;

    /**
     * 当前获取焦点的View
     */
    private View currentFocusView;

    public PlayListAdapter(Context context) {
        this.context = context;
        mFocusBorder = new FocusBorder.Builder()
                .asColor()
                .borderColorRes(R.color.borderColorRes)
                .borderWidth(TypedValue.COMPLEX_UNIT_DIP, 2.2f)
                .shadowColorRes(R.color.shadowColorRes)
                .shadowWidth(TypedValue.COMPLEX_UNIT_DIP, 10f)
                .build((Activity) context);
    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_play_list, viewGroup, false);
        return new PlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayListViewHolder viewHolder, final int position) {

        int realPosition = position;
//        int realPosition = 0;
//
//        if (allVideoConfigs.size()!=0){
//            realPosition = position%allVideoConfigs.size();
//        }

        if(phoneWidth > 0){
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.width = (phoneWidth - RecyclerItemDecoration.ITEM_NUM * RecyclerItemDecoration.ITEM_INTERVAL) / RecyclerItemDecoration.ITEM_NUM;
            layoutParams.height = layoutParams.width * 9 / 16;
            viewHolder.itemView.setLayoutParams(layoutParams);
        }

        final AllVideoConfig allVideoConfig = allConfig.getAllVideoConfig().get(realPosition);

        boolean isFree = allVideoConfig.getResourcePlayURL().size() == 1;
        viewHolder.iconFree.setVisibility(isFree ? View.VISIBLE:View.GONE);
        viewHolder.videoName.setText(allVideoConfig.getResourceName());
        if (allVideoConfig.getPicture() != null) {
            Glide.with(context)
                    .load(allVideoConfig.getPicture().getPosterUrl())
                    .dontAnimate()
                    .thumbnail(0.8f)
                    .placeholder(R.drawable.cover)
                    .error(R.drawable.cover)
                    .into(viewHolder.ivPlay);
        }

        viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    currentFocusView = viewHolder.itemView;
                    onMoveFocusBorder(viewHolder.itemView,SCALEN);
                }

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isMulti = allVideoConfig.getResourcePlayURL().size() > 1;
                DebugLog.info(TAG, (isMulti ? KPIConstant.KPI_MULTIVIEW:KPIConstant.KPI_FREEVIEW)+"onClick"+(allVideoConfig.getMediaType() == 1 ?" LIVE":" VOD"));
                Intent intent = new Intent();
                Gson gson = new Gson();
                intent.setClass(context, isMulti ? TVPlayActivity.class : FreeCameraPlayActivity.class);
                intent.putExtra(PlayConstant.EXTRA, gson.toJson(allVideoConfig));

                intent.putExtra(PlayConstant.TAG_SHOWLOGCAT, allConfig.isShowLogcat());
                intent.putExtra(PlayConstant.TAG_ISSHOWSPEED, allConfig.isShowSpeed());
                intent.putExtra(PlayConstant.TAG_SHOWCAMERAVIEW, allConfig.isShowCameraView());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
//        return (allVideoConfigs == null || allVideoConfigs.isEmpty()) ? 0 : Integer.MAX_VALUE;
        return (allConfig == null || allConfig.getAllVideoConfig().isEmpty()) ? 0 : allConfig.getAllVideoConfig().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(AllConfig allConfig) {
        this.allConfig = allConfig;
    }

    public class PlayListViewHolder extends RecyclerView.ViewHolder {

        private TextView videoName;
        private ImageView iconFree;
        private ImageView ivPlay;

        public PlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            videoName = itemView.findViewById(R.id.tv_video_name);
            iconFree = itemView.findViewById(R.id.icon_free);
            ivPlay = itemView.findViewById(R.id.iv_play);
        }
    }

    protected void onMoveFocusBorder(View focusedView, float scale) {
        if(null != mFocusBorder && !isScrolling) {
            mFocusBorder.onFocus(focusedView, FocusBorder.OptionsFactory.get(scale, scale));
        }
    }

    public RecyclerView.OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setPhoneWidth(int phoneWidth) {
        this.phoneWidth = phoneWidth;
    }
}
