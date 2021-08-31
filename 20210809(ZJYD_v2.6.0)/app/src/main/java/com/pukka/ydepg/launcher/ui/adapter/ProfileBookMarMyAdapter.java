package com.pukka.ydepg.launcher.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.ShimmerImageView;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.CornersTransform;
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.ScoreControl;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.moudule.featured.bean.VodBean;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * MyFragment历史收藏的adapter
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.adapter.MyAdapter.java
 * @date: 2017-12-18 11:30
 * @version: V1.0 描述当前版本功能
 */
public class ProfileBookMarMyAdapter extends BaseFocusAdapter<VodBean, BaseFocusViewHolder> {

    public ProfileBookMarMyAdapter(Context context, ItemEventListener itemEventListener) {
        super(context, itemEventListener);
    }

    @Override
    public BaseFocusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseFocusViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected MyViewHolder createViewHolder(View view) {
        return new MyViewHolder(view);
    }

    public void setDatas(List<VodBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public int resourceId() {
        return R.layout.item_profile_my_fragment_layout;
    }

    public class MyViewHolder extends BaseFocusViewHolder<VodBean> {
        @BindView(R.id.movies_list_item_img_hd)
        ImageViewExt mHdImg;
        @BindView(R.id.movies_list_item_score)
        TextViewExt mScoreTx;
        @BindView(R.id.movies_list_item_name)
        TextViewExt mNameTx;
        @BindView(R.id.movies_list_item_img)
        ShimmerImageView mPosterIv;
        @BindView(R.id.rl_item_profile_bookmark_list)
        RelativeLayoutExt mMoviesView;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(VodBean vodBean, int position) {
            if (itemEventListener != null) {
                mMoviesView.setOnFocusChangeListener((view, focused) -> {
                    mNameTx.setSelected(focused);
                    view.setSelected(focused);
                });
                mMoviesView.setOnClickListener((v) -> {
                    itemEventListener.onItemClick(v, position);
                });
            }
            mMoviesView.setTag(position);
            mNameTx.setText(vodBean.getName());

            //显示右上角标签图片 1 HD   2 4
            if (vodBean.getDefinition().equals("1")) {
                mHdImg.setVisibility(View.GONE);
            } else if (vodBean.getDefinition().equals("2")) {
                mHdImg.setVisibility(View.VISIBLE);
                mHdImg.setBackgroundResource(R.drawable.details_right_4k_icon);
            } else {
                mHdImg.setVisibility(View.GONE);
            }

            String score = vodBean.getRate();

            if (!TextUtils.isEmpty(score) && score.equals("0.0")) {
                score = "7.0";
            }

            if(ScoreControl.newNeedShowScore(vodBean)){
                mScoreTx.setText(score);
                mScoreTx.setVisibility(View.VISIBLE);
            }else {
                mScoreTx.setVisibility(View.GONE);
            }

            //解决收藏和播放历史无海报时也有闪光效果
            mPosterIv.setCanStartShimmer(!TextUtils.isEmpty(vodBean.getPoster()));
            RequestOptions options  = new RequestOptions().transform(new CornersTransform(ScreenUtil.getDimensionF(context, R.dimen.margin_3_75))).placeholder(R.drawable.default_poster);
            GlideApp.with(context).load(vodBean.getPoster()).apply(options).into(mPosterIv);
            VOD vod = new VOD();
            vod.setCustomFields(vodBean.getCustomFields());
            addLeftCorner(mMoviesView, SuperScriptUtil.getInstance().getSuperScriptForCollectionHistory(vod));
        }
    }

    public void addLeftCorner(RelativeLayout relativeLayout, String scriptUrl) {
        //先移除
        if (relativeLayout.getChildAt(relativeLayout.getChildCount()-1).getId() == R.id.left_corner){
            relativeLayout.removeView(relativeLayout.getChildAt(relativeLayout.getChildCount()-1));
        }
        if (!TextUtils.isEmpty(scriptUrl)){
            ImageView ImgScript = new ImageView(context);
            ImgScript.setId(R.id.left_corner);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_30));
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(OTTApplication.getContext().getResources().getDimensionPixelSize(R.dimen.margin_10),0,0,0);
            ImgScript.setLayoutParams(params);
            ImgScript.setScaleType(ImageView.ScaleType.FIT_START);
            relativeLayout.addView(ImgScript);
            Glide.with(context).load(scriptUrl).into(ImgScript);
        }
    }
}