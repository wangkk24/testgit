package com.pukka.ydepg.moudule.mytv.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.vod.activity.ChildModeVodDetailActivity;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;

import java.util.List;

public class UnSubscribeRecommendAdapter extends RecyclerView.Adapter<UnSubscribeRecommendAdapter.ViewHolder> {

    private List<VOD> mList;

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(1);

    private Activity activity;

    private PopupWindow PersuadeDialog;

    public UnSubscribeRecommendAdapter(List<VOD> list, Activity activity, PopupWindow PersuadeDialog) {
        mList = list;
        this.activity = activity;
        this.PersuadeDialog = PersuadeDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unsubscribe_recommend, parent, false);
        return new UnSubscribeRecommendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(mList.get(position));
        holder.nameText.setText(Html.fromHtml(resetName(position, mList.get(position).getName())));
        Picture picture = mList.get(position).getPicture();
        if (picture != null) {
            List<String> posters = picture.getPosters();
            if (posters != null) {
                if (posters.size() != 0) {
                    String poster = posters.get(0);
                    if (!TextUtils.isEmpty(poster)) {
                        Glide.with(holder.itemView.getContext()).load(poster).into(holder.posterImageView);
                    }
                }
            }
        }

        VOD vod = mList.get(position);
        if (null != vod){
            //SuperScript
            String superScripturl = SuperScriptUtil.getInstance().getSuperScriptByVod(vod,false);
            if (!TextUtils.isEmpty(superScripturl)){
                GlideApp.with(holder.itemView.getContext()).load(superScripturl).into(holder.superScript);
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
        return mList.size();
    }

    public void addData(List<VOD> list, boolean needClear) {
        if (needClear) {
            mList.clear();
            notifyDataSetChanged();
        }
        mList.addAll(list);
        notifyItemRangeInserted(mList.size() - list.size(), list.size());
    }


    public String resetName(int position, String name) {
        StringBuffer sb = new StringBuffer();
        switch (position) {
            case 0:
                sb.append("亲!最新最热的");
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append("大片\"");
                sb.append(name);
                sb.append("\"");
                sb.append("</font></strong>");
                sb.append(",你还没看哦。 ");
                break;
            case 1:
                sb.append("广受好评的");
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append("大作\"");
                sb.append(name);
                sb.append("\"");
                sb.append("</font></strong>");
                sb.append("，向你含泪吐血推荐。 ");
                break;
            case 2:
                sb.append("为你精挑细选的");
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append("大片\"");
                sb.append(name);
                sb.append("\"");
                sb.append("</font></strong>");
                sb.append("，你忍心不看了吗? ");
                break;
            case 3:
                sb.append("期盼已久的");
                sb.append("<strong><font color=\"#FFFFFF\">");
                sb.append("大片\"");
                sb.append(name);
                sb.append("\"");
                sb.append("</font></strong>");
                sb.append(",你忍心错过机会吗? ");
                break;
            default:
                sb.append(name);
        }
        return sb.toString();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImageView;

        private TextView nameText;

        ImageView superScript;

        public ViewHolder(final View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.movies_list_item_img);
            nameText = (TextView) itemView.findViewById(R.id.movies_list_item_name);
            superScript = itemView.findViewById(R.id.vipimg);
            itemView.setOnFocusChangeListener(mOnFocusChangeListener);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    VOD vod = (VOD) v.getTag();
//                    if(VodUtil.isMiguVod(vod)){
//                        MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(itemView.getContext(),vod.getCode(),MiguQRViewPopWindow.mSearchResultType);
//                        popWindow.showPopupWindow(v);
//                        return;
//                    }
//                    Intent intent;
//                    if(SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
//                        WeakReference<Activity> current = OTTApplication.getContext().getmCurrentActivity();
//                        if(null!=current&&current.get()!=null&&current.get() instanceof ChildModeVodDetailActivity){
//                            current.get().finish();
//                        }
//                        intent = new Intent(itemView.getContext(), ChildModeVodDetailActivity.class);
//                    }else{
//                        intent = new Intent(itemView.getContext(), VodDetailActivity.class);
//                    }
//                    intent.putExtra(VodDetailActivity.VOD_ID, vod.getID());
//                    intent.putExtra(VodDetailActivity.ORGIN_VOD,vod);
//                    itemView.getContext().startActivity(intent);
//                    PersuadeDialog.dismiss();
//                    activity.finish();
//                }
//            });

            itemView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            PersuadeDialog.dismiss();
                            return true;
                        }
                        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                            VOD vod = (VOD) v.getTag();
                            if (!TextUtils.isEmpty(vod.getCpId()) && CpRoute.isCp(vod.getCpId(),vod.getCustomFields())) {
                                CpRoute.goCp(vod.getCustomFields());
                            } else {
                                if (VodUtil.isMiguVod(vod)) {
                                    MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(itemView.getContext(), vod.getCode(), MiguQRViewPopWindow.mSearchResultType);
                                    popWindow.showPopupWindow(v);
                                    return true;
                                }
                                Intent intent;
                                if (SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                                    Activity current = OTTApplication.getContext().getCurrentActivity();
                                    if (current instanceof ChildModeVodDetailActivity) {
                                        current.finish();
                                    }
                                    intent = new Intent(itemView.getContext(), ChildModeVodDetailActivity.class);
                                } else {
                                    intent = new Intent(itemView.getContext(), NewVodDetailActivity.class);
                                }
                                intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                                intent.putExtra(NewVodDetailActivity.ORGIN_VOD, vod);
                                itemView.getContext().startActivity(intent);
                                //延时关闭弹框和我的订购页面，避免视觉上感觉先关闭再跳转
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (null != PersuadeDialog){
                                            PersuadeDialog.dismiss();
                                        }
                                        if (null != activity){
                                            activity.finish();
                                        }
                                    }
                                },1500);
                            }
                            return true;
                        }
                    }
                    return false;
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
