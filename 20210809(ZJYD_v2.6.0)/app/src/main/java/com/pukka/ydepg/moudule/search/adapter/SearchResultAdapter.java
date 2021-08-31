package com.pukka.ydepg.moudule.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODMediaFile;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ScoreControl;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.search.utils.ChineseUtil;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 排行榜适配器
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.module.search.adapter.SearchRankingAdapter.java
 * @author:xj
 * @date: 2017-12-19 09:47
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    Context mContext;
    private List<Content> mResultList;
    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_SMALL);
    private OnItemFoucsListener mOnItemFoucsListener;

    private Map<String,String> map;

    public SearchResultAdapter(Context mContext, List<Content> mRankingList) {
        this.mContext = mContext;
        this.mResultList = mRankingList;
        map=SessionService.getInstance().getSession().getTerminalConfigurationCPAPKINFO();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Content content = mResultList.get(position);
        VOD vod = content.getVOD();
        holder.mHD.setVisibility(View.INVISIBLE);
        if (null != vod) {
            if(ScoreControl.newNeedShowScore(vod)){
                holder.mScore.setText(formatRate(vod.getAverageScore()));
                holder.mScore.setVisibility(View.VISIBLE);
            }else{
                holder.mScore.setVisibility(View.GONE);
            }


            //设置显示右上角清晰度  HD/4K
            List<VODMediaFile> vodMediaFiles = vod.getMediaFiles();
            if (vodMediaFiles != null && vodMediaFiles.size() != 0) {
                VODMediaFile vodMediaFile = vodMediaFiles.get(0);
                String definition = vodMediaFile.getDefinition();
                if ("1".equals(definition)) {
//                    holder.mHD.setBackgroundResource(R.drawable.details_right_hd_icon);//HD
                    holder.mHD.setVisibility(View.GONE);
                } else if ("2".equals(definition)) {
                    holder.mHD.setBackgroundResource(R.drawable.details_right_4k_icon);//4K
                    holder.mHD.setVisibility(View.VISIBLE);
                }

            }

            //SuperScript
            String url = SuperScriptUtil.getInstance().getSuperScriptByVod(vod,false);
            if (!TextUtils.isEmpty(url)){
                GlideApp.with(mContext).load(url).into(holder.superScript);
                holder.superScript.setVisibility(View.VISIBLE);
            }else{
                holder.superScript.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(vod.getName())) {
                //holder.mName.setText(vod.getName());
                holder.mName.setText(ChineseUtil.getHighLightBuilder(vod.getName()));
            }
            Picture picture = vod.getPicture();
            if (null != picture) {
                RequestOptions options  = new RequestOptions()
                        .placeholder(mContext.getResources().getDrawable(R.drawable.default_poster))
                        .error(R.drawable.default_poster);
                if (VodUtil.isMiguVod(vod)) {
                    String miguPictureUrl = getMiguPoster(picture);
                    if (!TextUtils.isEmpty(miguPictureUrl)) {
                        Glide.with(mContext).load(miguPictureUrl).apply(options).into(holder.mResultImage);
                        //ImageLoadManager.loadImageFromUrl(holder.mResultImage, miguPictureUrl, R.drawable.default_poster);
                    } else {
                        holder.mResultImage.setImageResource(R.drawable.default_poster);
                    }
                } else {
                    List<String> posters = picture.getPosters();
                    if (null != posters && posters.size() > 0) {
                        String pictureUrl = posters.get(0);
                        Glide.with(mContext).load(pictureUrl).apply(options).into(holder.mResultImage);
                        //ImageLoadManager.loadImageFromUrl(holder.mResultImage, pictureUrl, R.drawable.default_poster);
                    } else {
                        holder.mResultImage.setImageResource(R.drawable.default_poster);
                    }
                }
            } else {
                holder.mResultImage.setImageResource(R.drawable.default_poster);
            }
            // 设置onkeylistener无效，暂时解决
            if (position == getItemCount() - 1) {
                holder.itemView.setNextFocusRightId(holder.itemView.getId());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (VodUtil.isMiguVod(vod)) {
                        //移网咪咕的VOD资源不能进详情,弹出二维码Dialog提示用户
                        MiguQRViewPopWindow miguQRViewPopWindow = new MiguQRViewPopWindow(mContext, vod.getCode(), MiguQRViewPopWindow.mSearchResultType);
                        miguQRViewPopWindow.showPopupWindow(v);
                        return;
                    }
                    //如果是第三方apk
                    if(!TextUtils.isEmpty(vod.getCpId())&&CpRoute.isCp(vod.getCpId(),vod.getCustomFields())){
                        CpRoute.goCp(vod.getCustomFields());
                    }else {
                    if (null != map && map.containsKey(vod.getCpId())) {
                        PackageManager manager = mContext.getPackageManager();
                        Intent mIntent = manager.getLaunchIntentForPackage(map.get(vod.getCpId()));
                        mIntent.putExtra(Constant.CONTENTCODE, vod.getCode());
                        mIntent.putExtra(Constant.ACTION, "VOD");
                        mContext.startActivity(mIntent);
                        return;
                    } else {
                        //儿童版进儿童详情,普通进普通详情
                        Intent intent;
                        if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                            intent = new Intent(mContext, ChildModeVodDetailActivity.class);
                        } else {
                            intent = new Intent(mContext, NewVodDetailActivity.class);
                        }
                        intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                        intent.putExtra(NewVodDetailActivity.ORGIN_VOD, vod);
                        mContext.startActivity(intent);
                    }
                }
                }
            });
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return null == mResultList ? 0 : mResultList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_result_item_image)
        ImageView mResultImage;

        @BindView(R.id.search_result_item_hd)
        ImageView mHD;

        @BindView(R.id.search_result_item_score)
        TextView mScore;

        @BindView(R.id.search_result_item_name)
        TextView mName;

        @BindView(R.id.vipimg)
        ImageView superScript;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mFocusHighlight != null) {
                mFocusHighlight.onInitializeView(itemView);
            }
            itemView.setOnFocusChangeListener(mOnFocusChangeListener);
        }
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (mFocusHighlight != null) {
                mFocusHighlight.onItemFocused(v, hasFocus);
            }
            if (null != mOnItemFoucsListener) {
                mOnItemFoucsListener.onFocus(hasFocus, (Integer) v.getTag());
            }
            if (hasFocus) {

                v.findViewById(R.id.search_result_item_name).setSelected(true);
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(true);
            } else {
                v.findViewById(R.id.movies_list_item_img_bg).setSelected(false);
                v.findViewById(R.id.search_result_item_name).setSelected(false);
            }
        }
    };

    public interface OnItemFoucsListener {
        void onFocus(boolean hasFoucus, int position);
    }

    public void setOnItemFoucsListener(OnItemFoucsListener onItemFoucsListener) {
        mOnItemFoucsListener = onItemFoucsListener;
    }

    /**
     * Format rate null "" 0->0.0
     * 9.35->9.4
     * 7->7.0
     * 10->10
     *
     * @param rate 评分
     */
    private static String formatRate(String rate) {
        if (TextUtils.isEmpty(rate)) {
            return "0.0";
        } else {
            try {
                Float floatRate = Float.parseFloat(rate);
                if (floatRate >= 10) {
                    return "10";
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    String rateStr = decimalFormat.format(floatRate);
                    if (!TextUtils.isEmpty(rateStr) && rateStr.equals("0.0")) {
                        rateStr = "7.0";
                    }
                    return rateStr;
                }
            } catch (NumberFormatException e) {
                return "0.0";
            }
        }
    }

    public String getMiguPoster(Picture picture) {
        if (null == picture) {
            return null;
        }
        List<String> iconList = picture.getIcons();
        if (!CollectionUtil.isEmpty(iconList)) {
            return iconList.get(0);
        } else {
            List<String> draftList = picture.getDrafts();
            if (!CollectionUtil.isEmpty(draftList)) {
                return draftList.get(0);
            } else {
                List<String> titleList = picture.getTitles();
                if (!CollectionUtil.isEmpty(titleList)) {
                    return titleList.get(0);
                }
            }
        }
        return null;
    }
}
