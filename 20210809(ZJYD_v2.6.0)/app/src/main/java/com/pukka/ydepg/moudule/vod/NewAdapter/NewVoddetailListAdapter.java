package com.pukka.ydepg.moudule.vod.NewAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.GlideApp;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.SuperScriptUtil;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.customui.tv.widget.FocusHighlight;
import com.pukka.ydepg.customui.tv.widget.FocusHighlightHandler;
import com.pukka.ydepg.customui.tv.widget.FocusScaleHelper;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.pukka.ydepg.moudule.mytv.utils.VODMining.getPicture;

public class NewVoddetailListAdapter extends RecyclerView.Adapter <NewVoddetailListAdapter.ViewHolder>{

    private static final String TAG = "NewVoddetailListAdapter";

    private Context context;

    //猜你喜欢的数据
    private List<VOD> list;

    //主创相关的数据
    private List<Content> contentList;

    private onClickListenr listenr;

    //防止快速移动焦点错乱
    private boolean canMove = true;

    //当前页面是否有子集列表
    private boolean hasEpisodes = false;

    int index;

    private FocusHighlightHandler mFocusHighlight = new FocusScaleHelper.ItemFocusScale(FocusHighlight.ZOOM_FACTOR_XSMALL);

    public void setListenr(onClickListenr listenr) {
        this.listenr = listenr;
    }

    public NewVoddetailListAdapter(Context context, List<VOD> list, List<Content> ContentList, int index, boolean hasEpisodes) {
        this.context = context;
        this.list = list;
        this.contentList = ContentList;
        this.hasEpisodes = hasEpisodes;
        this.index = index;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voddetail_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (null != list && list.size() > 0) {
            VOD vod = list.get(position);
            holder.title.setText(vod.getName());
            Log.i(TAG, "onBindViewHolder: list"+vod.getName());

            String url = "";
            Picture picture = vod.getPicture();
            if (null != picture){

                if (VodUtil.isMiguVod(vod)) {// 判断是否是别的终端添加
                    url = getMiguPoster(vod);
                } else {
                    List<String> posterList = picture.getPosters();
                    if (null != posterList && posterList.size() > 0) {
                        url = posterList.get(0);

                    }
                }
            }

            if (null != context && null != holder.bg){
                RequestOptions options = new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.default_poster_bg));
                GlideApp.with(context).load(url).apply(options).into(holder.bg);
            }

            RequestOptions options = new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.default_poster_bg));
            GlideApp.with(context).load(url).apply(options).into(holder.bg);

            boolean isShow = SuperScriptUtil.getInstance().getSuperScriptSwitch(SuperScriptUtil.SUPERSCRIPT_GUESS);
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

        } else if (null != contentList && contentList.size() > 0) {
            Content content = contentList.get(position);
            String url = "";
            if (null != content && null != content.getVOD()){
                VOD vod = content.getVOD();

                holder.title.setText(vod.getName());
                Log.i(TAG, "onBindViewHolder: contentList"+vod.getName());

                Picture picture = vod.getPicture();
                if (null != picture){
                    List<String> list = picture.getPosters();
                    if (null != list && list.size() >0){
                        url = list.get(0);
                    }
                }

                boolean isShow = SuperScriptUtil.getInstance().getSuperScriptSwitch(SuperScriptUtil.SUPERSCRIPT_ACTOR);
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

            if (null != context && null != holder.bg){
                RequestOptions options = new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.default_poster));
                GlideApp.with(context).load(url).apply(options).into(holder.bg);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null != list && list.size()>0){
            return list.size();
        }else if (null != contentList && contentList.size()>0){
            return contentList.size();
        }else{
            return 0;
        }
    }

    public static String getMiguPoster(VOD vod) {
        Picture picture = getPicture(vod);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;

        ImageView bg;

        TextView title;

        ImageView superScript;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_voddetail_bg_layout);
            bg = itemView.findViewById(R.id.item_voddetail_bg);
            title = itemView.findViewById(R.id.item_voddetail_title);
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
                    itemView.setSelected(hasFocus);
                    layout.setSelected(hasFocus);
                    title.setSelected(hasFocus);

                    if (hasFocus && index != 0){
                        EventBus.getDefault().post(new VoddetailScrollEvent(index));
                    }else if (hasFocus && index == 0){
                        EventBus.getDefault().post(new VoddetailScrollEvent(10));
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int)v.getTag();

                    if (null != list && list.size()>0){
                        if (list.size() >= position){
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
                            intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_APP_POINTED_ID, RemixRecommendUtil.APPPINEDID_VOD);
                            intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_RECOMMEND_TYEP, UBDRecommendImpression.recommendType_vod);
                            intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_SCENE_ID,       UBDRecommendImpression.sceneId_vod);
                            context.startActivity(intent);

                            if (null != listenr){
                                boolean ismigu = VodUtil.isMiguVod(vod);
                                listenr.onClick(ismigu);
                            }
                        }
                    }else if (null !=contentList && contentList.size()>0){
                        if (contentList.size() >= position){
                            Content content = contentList.get(position);
                            VOD vod = content.getVOD();

                            if (VodUtil.isMiguVod(vod)) {
                                if (null != listenr){
                                    boolean ismigu = VodUtil.isMiguVod(vod);
                                    listenr.onClick(ismigu);
                                }
                                return;
                            }

                            if (null != vod && !TextUtils.isEmpty(vod.getID())){
                                Intent intent = new Intent(context, NewVodDetailActivity.class);
                                intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                                context.startActivity(intent);

                                if (null != listenr){
                                    boolean ismigu = VodUtil.isMiguVod(vod);
                                    listenr.onClick(ismigu);
                                }
                            }
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
                        if (null != list){
                            if (position == list.size() - 1){
                                return true;
                            }
                        }else if (null != contentList){
                            if (position == contentList.size() - 1){
                                return true;
                            }
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

    public interface onClickListenr{
        void onClick(boolean ismigu);
    }

}
