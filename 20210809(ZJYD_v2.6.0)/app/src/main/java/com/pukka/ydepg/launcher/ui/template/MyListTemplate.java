package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6Controller.VODListController;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.ReflectItemView;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.adapter.ItemEventListener;
import com.pukka.ydepg.launcher.ui.adapter.MyAdapter;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.view.MyDividerItemDecoration;
import com.pukka.ydepg.moudule.featured.bean.VodBean;
import com.pukka.ydepg.moudule.mytv.NewMyMovieActivity;
import com.pukka.ydepg.moudule.mytv.adapter.CollectionAdapter;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;
import com.pukka.ydepg.moudule.player.util.OffScreenUtils;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的页面 收藏和播放历史模板
 *
 * @FileName: com.pukka.ydepg.launcher.ui.template.MineListTemplate.java
 * @author: luwm
 * @data: 2018-06-25 17:49
 * @Version V1.0 <描述当前版本功能>
 */
public class MyListTemplate extends PHMTemplate implements ItemEventListener {
    public static final String TYPE_BOOKMARK = "bookmark";
    public static final String TYPE_FAVORITE = "favorite";
    RecyclerViewTV recyclerViewTV;
    MyAdapter myAdapter;
    TextViewExt tvTitle;
    TextViewExt tvMoreText;
    private MiguQRViewPopWindow popWindow;
    ReflectItemView rlMore;
    private ImageViewExt ivFocusBg;
    private List<VodBean> mVodBeans = new ArrayList<>();
    private List<VOD> mVods = new ArrayList<>();
    private String type = "";
    private Map<String, String> cpApkMap;

    private Group mGroup;
    private Element mElement;

    public MyListTemplate(Context context) {
        super(context);
    }

    public MyListTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pannel_poster_my_list, this, true);
        recyclerViewTV = (RecyclerViewTV) findViewById(R.id.rv_viewPlayRecord);
        rlMore = (ReflectItemView) findViewById(R.id.rt_moreItem);
        ivFocusBg = (ImageViewExt) findViewById(R.id.iv_moreImage_focus_bg);
        tvMoreText = (TextViewExt) findViewById(R.id.tv_moreText);
        tvTitle = (TextViewExt) findViewById(R.id.tv_mine_list_title);
        cpApkMap = SessionService.getInstance().getSession().getTerminalConfigurationCPAPKINFO();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTV.setLayoutManager(layoutManager);
        recyclerViewTV.addItemDecoration(new MyDividerItemDecoration());
        rlMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (TextUtils.equals(type, TYPE_BOOKMARK)) {
                    intent = new Intent(context, NewMyMovieActivity.class).putExtra("id", "1");
                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, NewMyMovieActivity.class).putExtra("id", "2");
                    context.startActivity(intent);
                }
                //静态资源位点击事件上报UBD
                UBDSwitch.getInstance().recordMainOnclick(null,null,mGroup,null,null);
            }
        });
        rlMore.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ivFocusBg.setSelected(hasFocus);
            }
        });
    }

    @Override
    public View getFirstView() {
        if (null != recyclerViewTV && null != recyclerViewTV.getChildAt(0)) {
            return recyclerViewTV.getChildAt(0);
        }
        return super.getFirstView();
    }

    public void loadFavoriteData(List<VodBean> vodBeans, List<VOD> vods) {
        type = TYPE_FAVORITE;
        if (CollectionUtil.isEmpty(vodBeans)) {
            return;
        }
        mVodBeans = vodBeans;
        mVods = vods;
        tvTitle.setText(context.getString(R.string.launcher_my_collect));
        tvMoreText.setText(getResources().getString(R.string.mytv_movie_title));
        if (vodBeans.size() < 5) {
            rlMore.setVisibility(GONE);
        } else {
            rlMore.setVisibility(VISIBLE);
        }
        myAdapter = new MyAdapter(context, this);
        recyclerViewTV.setAdapter(myAdapter);
        myAdapter.setDatas(vodBeans);
    }

    public void loadBookmarkData(List<VodBean> vodBeans, List<VOD> vods) {
        type = TYPE_BOOKMARK;
        if (CollectionUtil.isEmpty(vodBeans)) {
            return;
        }
        mVodBeans = vodBeans;
        mVods = vods;
        tvTitle.setText(context.getString(R.string.launcher_my_play_history));
        tvMoreText.setText(getResources().getString(R.string.launcher_my_more_history));
        if (vodBeans.size() < 5) {
            rlMore.setVisibility(GONE);
        } else {
            rlMore.setVisibility(VISIBLE);
        }
        myAdapter = new MyAdapter(context, this);
        recyclerViewTV.setAdapter(myAdapter);
        myAdapter.setDatas(vodBeans);
    }

    //返回当前的标题内容，确定是历史还是收藏界面
    public String getTitleContent(){
        if (!TextUtils.isEmpty(tvTitle.getText())){
            return tvTitle.getText().toString();
        }
        return "";
    }

    public void dismissMiguQRViewPopWindow() {
        if (null != popWindow && popWindow.isShowing()) {
            popWindow.dismiss();
        }
    }

    public void setElementData(Element element){
        if (null != element && null != element.getExtraData()){
            this.mElement = element;
        }
    }

    //1:播放历史；2：收藏
    public void setExtraData(Map<String, String> extraData,int type){
        if (null != extraData && !TextUtils.isEmpty(extraData.get("Is_Shimmer")) && extraData.get("Is_Shimmer").equals("true")){
            if (type == 1){
                MessageDataHolder.get().setIsShimmerHistory(true);
            }else{
                MessageDataHolder.get().setIsShimmerCollect(true);
            }
        }else{
            if (type == 1){
                MessageDataHolder.get().setIsShimmerHistory(false);
            }else{
                MessageDataHolder.get().setIsShimmerCollect(false);
            }
        }
    }

    public void setGroupAndNavIndex(Group group){
        this.mGroup = group;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intentVod = new Intent(context, NewVodDetailActivity.class);
        VodBean vodBean = null;
        VOD vod = null;
        if (position < mVodBeans.size()) {
            vodBean = mVodBeans.get(position);
            vod = mVods.get(position);
            if (VodUtil.isMiguVod(vodBean)) {
                popWindow = new MiguQRViewPopWindow(context, vod.getCode(), MiguQRViewPopWindow.mHistoryResultType);
                popWindow.showPopupWindow(view);
                return;
            }
            if (null != cpApkMap && cpApkMap.containsKey(vod.getCpId())) {
                PackageManager manager = context.getPackageManager();
                Intent mIntent = manager.getLaunchIntentForPackage(cpApkMap.get(vod.getCpId()));
                mIntent.putExtra(Constant.CONTENTCODE, vod.getCode());
                mIntent.putExtra(Constant.ACTION, "VOD");
                context.startActivity(mIntent);
                return;
            }
        }

        //Added by liuxia at 20180315 for vodBean为空导致crash
        if (vodBean == null) {
            return;
        }
        if(null!=vod&&!TextUtils.isEmpty(vod.getCpId()))
        {
            VodBean finalVodBean = vodBean;
            VOD finalVod = vod;
            OffScreenUtils.getSPVodDetail(finalVod.getID(), new VODListController((RxAppCompatActivity) context), (RxAppCompatActivity) context, new VODDetailCallBack() {
                @Override
                public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
                    vodDetail.getCpId();

                    if (!TextUtils.isEmpty(vodDetail.getCpId()) && CpRoute.isCp(vodDetail.getCpId(), vodDetail.getCustomFields())) {
                        CpRoute.goCp(vodDetail.getCustomFields());
                    } else {
                        //普通点击事件 跳转详情页
                        intentVod.putExtra(NewVodDetailActivity.VOD_ID, finalVodBean.getId());
                        intentVod.putExtra(NewVodDetailActivity.ORGIN_VOD, finalVod);
                        context.startActivity(intentVod);

                        UBDSwitch.getInstance().recordMainOnclick("", mElement, mGroup, finalVod,null);
                    }
                }

                @Override
                public void getVODDetailFailed() {
                    SuperLog.info2SD(CollectionAdapter.class.getSimpleName(), "get cpId failed");
                    //普通点击事件 跳转详情页
                    intentVod.putExtra(NewVodDetailActivity.VOD_ID, finalVodBean.getId());
                    intentVod.putExtra(NewVodDetailActivity.ORGIN_VOD, finalVod);
                    context.startActivity(intentVod);

                    UBDSwitch.getInstance().recordMainOnclick("", mElement, mGroup, finalVod,null);
                }

                @Override
                public void onError() {
                    //普通点击事件 跳转详情页
                    intentVod.putExtra(NewVodDetailActivity.VOD_ID, finalVodBean.getId());
                    intentVod.putExtra(NewVodDetailActivity.ORGIN_VOD, finalVod);
                    context.startActivity(intentVod);

                    UBDSwitch.getInstance().recordMainOnclick("", mElement, mGroup, finalVod,null);
                }
            });
        }else {

            intentVod.putExtra(NewVodDetailActivity.VOD_ID, vodBean.getId());
            intentVod.putExtra(NewVodDetailActivity.ORGIN_VOD, vod);
            context.startActivity(intentVod);

            UBDSwitch.getInstance().recordMainOnclick("", mElement, mGroup, vod,null);
        }
    }
}