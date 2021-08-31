package com.pukka.ydepg.launcher.ui.adapter.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import java.util.List;

/**
 * Created by jiaxing on 2017/8/18.
 */
public class TopicOneAdapter extends RecyclerView.Adapter<TopicOneAdapter.ViewHolder> {

    private List<VOD> mList;
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_MEDIUM);
    private VerticalGridView mVerticalGridView;
    private int columnNum = 0;

    public TopicOneAdapter(List<VOD> list, VerticalGridView verticalGridView) {
        mList = list;
        this.mVerticalGridView = verticalGridView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_style_one_item_layout, parent, false);
        return new ViewHolder(view);
    }

    public void setColumnNum(int num) {
        this.columnNum = num;
    }

    public void addData(List<VOD> list) {
        int lastIndex = 0;
        lastIndex = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(lastIndex, list.size());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VOD vod = mList.get(position);
        holder.itemView.setTag(vod);
        holder.nameText.setText(vod.getName());
        holder.posterImageView.setImageResource(R.drawable.default_poster);
        Picture picture = vod.getPicture();
        List<String> posters = null;
        if (picture != null) {
            posters = picture.getTitles();
            if (CollectionUtil.isEmpty(posters)) {
                posters = picture.getPosters();
            }
            if (!CollectionUtil.isEmpty(posters)) {
                String poster = posters.get(0);
                if (!TextUtils.isEmpty(poster)) {
                    //holder.posterImageView.setImageResource(R.drawable.default_poster);
                    RequestOptions options = new RequestOptions()
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.default_poster);

                    Glide.with(holder.itemView.getContext()).load(poster).apply(options).into(holder.posterImageView);
                }

            }
        }
        holder.rightIcon.setVisibility(View.INVISIBLE);
        holder.rightIcon.setBackground(null);
        String type = vod.getVODType();
        holder.rightIcon.setVisibility(View.VISIBLE);
        List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
        if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
            VODMediaFile vodMediaFile = vodMediaFiles.get(0);
            String definition = vodMediaFile.getDefinition();
            if ("2".equals(definition)) {
                holder.rightIcon.setBackgroundResource(R.drawable.movies_list_right_4k_icon);//4K
            }
        }
        holder.itemView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //解决最后一行直接return true
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (null == FocusFinder.getInstance().findNextFocus(mVerticalGridView, v, View.FOCUS_DOWN) && isLastLine(v)) {
                        return true;
                    }else if (isLastSecondLine(v)) {
                        //解决从倒数第二行向下按，如果下方不存在内容，无响应的问题
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (null == FocusFinder.getInstance().findNextFocus(mVerticalGridView, v, View.FOCUS_DOWN)){
                                    View nextFocusView = mVerticalGridView.getChildAt(mVerticalGridView.getLayoutManager().getChildCount() - 1);
                                    nextFocusView.requestFocus();
                                }
                            }
                        }, 500);
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private boolean isLastLine(View v) {
        int position = mVerticalGridView.getChildAdapterPosition(v);
        return position >= getItemCount() - (getItemCount() % columnNum == 0 ? columnNum : getItemCount() % columnNum);
    }

    private boolean isLastSecondLine(View v) {
        int position = mVerticalGridView.getChildAdapterPosition(v);
        return getItemCount() - (getItemCount() % columnNum + columnNum) <= position && position < getItemCount() - getItemCount() % columnNum;
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
        private RelativeLayout posterLayout;
        private ImageView posterImageView;
        private TextView nameText;
        private View rightIcon;

        public ViewHolder(final View itemView) {
            super(itemView);
            posterLayout = (RelativeLayout) itemView.findViewById(R.id.movies_list_item_img_bg);
            posterImageView = (ImageView) itemView.findViewById(R.id.movies_list_item_img);
            nameText = (TextView) itemView.findViewById(R.id.movies_list_item_name);
            rightIcon = itemView.findViewById(R.id.movies_list_poster_right_view);
            itemView.setOnFocusChangeListener(mOnFocusChangeListener);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VOD vod = (VOD) v.getTag();
                    //第三方apk
                    if (!TextUtils.isEmpty(vod.getCpId()) && CpRoute.isCp(vod.getCpId(),vod.getCustomFields())) {
                        CpRoute.goCp(vod.getCustomFields());
                    } else {
                        if (VodUtil.isMiguVod(vod)) {
//                        EpgToast.showToast(itemView.getContext(), "请到咪咕爱看app进行观看!");
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
                        intent.putExtra("vod_exit", true);
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
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(true);
                v.findViewById(R.id.movies_list_item_name).setSelected(true);
            } else {
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(false);
                v.findViewById(R.id.movies_list_item_name).setSelected(false);
            }
        }
    };
}