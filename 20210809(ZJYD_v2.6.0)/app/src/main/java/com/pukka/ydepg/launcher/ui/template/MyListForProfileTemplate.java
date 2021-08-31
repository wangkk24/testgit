package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
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
import com.pukka.ydepg.common.utils.IsTV;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.VodUtil;
import com.pukka.ydepg.common.utils.uiutil.CpRoute;
import com.pukka.ydepg.customui.MiguQRViewPopWindow;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.ReflectItemView;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.adapter.ItemEventListener;
import com.pukka.ydepg.launcher.ui.adapter.ProfileBookMarMyAdapter;
import com.pukka.ydepg.launcher.ui.adapter.ProfileBookMarkAdapter;
import com.pukka.ydepg.launcher.ui.fragment.MyPHMFragment;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.view.MyDividerItemDecoration;
import com.pukka.ydepg.moudule.featured.bean.VodBean;
import com.pukka.ydepg.moudule.mytv.NewMyMovieActivity;
import com.pukka.ydepg.moudule.mytv.adapter.CollectionAdapter;
import com.pukka.ydepg.moudule.player.util.OffScreenUtils;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 多Profile的观看记录Template
 */
public class MyListForProfileTemplate extends PHMTemplate implements ItemEventListener {
    public static final String TYPE_BOOKMARK = "bookmark";
    public static final String TYPE_FAVORITE = "favorite";
    private static final String PROFILE = "0";
    private static final String CHILDPROFILE = "1";
    RecyclerViewTV recyclerViewTV;
    RecyclerViewTV recyclerViewMyTV;
    ProfileBookMarkAdapter mAdapter;
    ProfileBookMarMyAdapter mMyAdapter;
    TextView tvTitle;
    TextViewExt tvMoreText;
    ReflectItemView rlMore;
    private List<VodBean> mVodBeans = new ArrayList<>();
    private List<VOD> mVods = new ArrayList<>();
    private String type = "";
    private Map<String, String> cpApkMap;

    private Group mGroup;
    private Element mElement;

    private Context mContext;

    //0：是主账号；1：不是主账号  1使用"我的"页面观看历史效果；0使用两行效果
    private String primaryAccount = "0";

    public MyListForProfileTemplate(Context context) {
        super(context);
    }

    public MyListForProfileTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
        this.mContext = context;
    }

    //primaryAccount = 0：是主账号；1：不是主账号  1使用"我的"页面观看历史效果；0使用两行效果
    private void initView(Context context,String primaryAccount) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pannel_poster_my_list, this, true);
        recyclerViewTV = (RecyclerViewTV) findViewById(R.id.rv_viewPlayRecord);
        recyclerViewMyTV = (RecyclerViewTV) findViewById(R.id.rv_viewPlayRecord_my);
        rlMore = (ReflectItemView) findViewById(R.id.rt_moreItem);
        tvMoreText = (TextViewExt) findViewById(R.id.tv_moreText);
        tvTitle = findViewById(R.id.tv_home_title);
        cpApkMap = SessionService.getInstance().getSession().getTerminalConfigurationCPAPKINFO();
        if (primaryAccount.equalsIgnoreCase(CHILDPROFILE)){
            recyclerViewMyTV.setVisibility(VISIBLE);
            recyclerViewTV.setVisibility(GONE);
            mMyAdapter = new ProfileBookMarMyAdapter(context, this);//
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewMyTV.setLayoutManager(layoutManager);
            recyclerViewMyTV.addItemDecoration(new MyDividerItemDecoration());
            recyclerViewMyTV.setAdapter(mMyAdapter);
        }else if (primaryAccount.equalsIgnoreCase(PROFILE)){
            recyclerViewMyTV.setVisibility(GONE);
            recyclerViewTV.setVisibility(VISIBLE);
            mAdapter = new ProfileBookMarkAdapter(context, this);//
            GridLayoutManager layoutManager = new GridLayoutManager(context,6);
            recyclerViewTV.setLayoutManager(layoutManager);
            recyclerViewTV.addItemDecoration(new MyDividerItemDecoration());
            recyclerViewTV.setAdapter(mAdapter);
            recyclerViewTV.addItemDecoration(new SpaceItemDecoration(context));
        }

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
    }

    @Override
    public View getFirstView() {
        if (null != recyclerViewTV && recyclerViewTV.getVisibility() == VISIBLE && null != recyclerViewTV.getChildAt(0)) {
            return recyclerViewTV.getChildAt(0);
        }else if (null != recyclerViewMyTV && recyclerViewMyTV.getVisibility() == VISIBLE && null != recyclerViewMyTV.getChildAt(0)) {
            return recyclerViewMyTV.getChildAt(0);
        }
        return super.getFirstView();
    }

    private void setVisibility(boolean isVisible) {
        ViewGroup.LayoutParams param = getLayoutParams();
        if (null == param) {
            param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (isVisible) {
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        setLayoutParams(param);
    }

    public void loadBookmarkData(List<VOD> vods) {
        //添加VR的过滤
        List<VOD> vodsTem = new ArrayList<>();
        for (int i = 0; i < vods.size(); i++) {
            if (IsTV.isNotVRBM(vods.get(i))){
                vodsTem.add(vods.get(i));
            }
        }
        if (vodsTem.size() == 0){
            setVisibility(false);
            return;
        }

        vods = vodsTem;

        if (vods.size() > 0 && !TextUtils.isEmpty(vods.get(0).getPrimaryAccount())){
            primaryAccount = vods.get(0).getPrimaryAccount();
        }

        if (primaryAccount.equalsIgnoreCase(CHILDPROFILE)){
            if (null != recyclerViewMyTV){
                recyclerViewMyTV.setVisibility(VISIBLE);
                recyclerViewTV.setVisibility(GONE);
            }
            if (null == mMyAdapter){
                initView(mContext,primaryAccount);
            }
        }else if(primaryAccount.equalsIgnoreCase(PROFILE)) {
            if (null != recyclerViewMyTV){
                recyclerViewMyTV.setVisibility(GONE);
                recyclerViewTV.setVisibility(VISIBLE);
            }
            if (null == mAdapter){
                initView(mContext,primaryAccount);
            }
        }

        List<VOD> vodList = new ArrayList<>();
        if (primaryAccount.equalsIgnoreCase(CHILDPROFILE)) {
            for (int i = 0; i < 5; i++) {
                if (null != vods && vods.size() > 0 && i < vods.size())
                    vodList.add(vods.get(i));
            }
        }else if (primaryAccount.equalsIgnoreCase(PROFILE)){
            if (null != vods && vods.size() > 12){
                for (int i = 0;i < 12;i++){
                    vodList.add(vods.get(i));
                }
            }else if (null != vods && vods.size()>0){
                vodList.addAll(vods);
            }
        }

        List<VodBean> vodBeans = MyPHMFragment.convert2SearchVODBean(vodList);
        type = TYPE_BOOKMARK;
        if (CollectionUtil.isEmpty(vodBeans)) {
            return;
        }
        mVodBeans = vodBeans;
        mVods = vods;
        if (null != mGroup && null != mGroup.getNameDialect() && mGroup.getNameDialect().size() > 0 && !TextUtils.isEmpty(mGroup.getNameDialect().get(0).getValue())){
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(mGroup.getNameDialect().get(0).getValue());
        }

        tvMoreText.setText(getResources().getString(R.string.launcher_my_more_history));
        if (primaryAccount.equalsIgnoreCase(CHILDPROFILE)) {
            if (vodBeans.size() < 5) {
                rlMore.setVisibility(GONE);
            } else {
                rlMore.setVisibility(VISIBLE);
            }
            mMyAdapter.setDatas(mVodBeans);
        }else if (primaryAccount.equalsIgnoreCase(PROFILE)){
            rlMore.setVisibility(GONE);
            mAdapter.setDatas(vodList);
        }
    }

    public void setElementData(GroupElement groupElement){
        if (null != groupElement && null != groupElement.getElement() && groupElement.getElement().size() > 0){
            this.mElement = groupElement.getElement().get(0);
        }
        this.mGroup = groupElement.getGroup();
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
                MiguQRViewPopWindow popWindow = new MiguQRViewPopWindow(context, vod.getCode(), MiguQRViewPopWindow.mHistoryResultType);
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
        if(null!=vod&&!TextUtils.isEmpty(vod.getCpId())) {
            VodBean finalVodBean = vodBean;
            VOD finalVod = vod;
            OffScreenUtils.getSPVodDetail(finalVod.getID(), new VODListController((RxAppCompatActivity) context), (RxAppCompatActivity) context, new VODDetailCallBack() {
                @Override
                public void getVODDetailSuccess(VODDetail vodDetail, String recmActionID, List<RecmContents> recmContents) {
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

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private static final String TAG = "CustomItemDecoration";
        private final Context mContext;

        public SpaceItemDecoration(Context context) {
            this.mContext =context;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            SuperLog.debug(TAG, "itemCount>>" +itemCount + ";Position>>" + pos);

            if (pos >= 6){
                outRect.bottom = 0;
            }else{
                outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.margin_20);
            }
        }
    }
}