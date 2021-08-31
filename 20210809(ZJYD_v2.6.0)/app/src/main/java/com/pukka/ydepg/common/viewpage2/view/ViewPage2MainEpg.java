package com.pukka.ydepg.common.viewpage2.view;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Desktop;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommend;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.viewpage2.ViewPage2Constant;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.http.hecaiyun.HecaiyunServiceImpl;
import com.pukka.ydepg.launcher.http.hecaiyun.data.BoothCarouselCont;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.ui.ChildLauncherActivity;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.adapter.ViewPage2Adapter;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.search.utils.ViewPage2MainEpgUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ViewPage2MainEpg {

    private final static String TAG = ViewPage2MainEpg.class.getSimpleName();

    private ViewPager2 mViewPager2;
    private int currentItem = 0;
    private boolean needStop = false;
    //private long internalTime = 3*1000L;// 默认切换轮播时间，单位毫秒
    private Timer mTimer;
    private AutoTask mTimerTask;
    private LinearLayout mPointGroupLinear;
    private Context mContext;
    private ReflectRelativeLayout mRootView;
    private Element mElement;
    private Group mGroup;
    private ViewPage2Adapter viewPage2Adapter;
    private TabItemPresenter presenter;
    private PBSRemixRecommendListener pbsRemixRecommendListener = new PBSRemixRecommendListener();

    //仅有一条推荐数据，不滚动，不显示右下角的小点点
    private boolean isOnlyData = false;
    //是否是点击轮播资源位进入详情页
    private boolean isOnClick = false;

    private final static String ORIENTATION_VERTICAL = "1";
    private final static String ORIENTATION_HORIZONTAL = "0";

    //滑动方向：0 水平，1 垂直,默认为0
    private String direction = "0";

    //轮播间隔，单位：毫秒,默认3s
    private Long interval = 3000L;

    private String cloudTv;

    public void initViewPage(ViewPager2 viewPager2, LinearLayout pointGroupLinear, Context context, ReflectRelativeLayout rootView, Group group, Element element){
        if (null != viewPager2){
            mViewPager2 = viewPager2;
            mRootView = rootView;
            mPointGroupLinear = pointGroupLinear;
            mContext = context;
            mElement = element;
            mGroup = group;
            initData();
        }
    }

    private void initData() {
        cloudTv = "";
        if (!TextUtils.isEmpty(ViewPage2MainEpgUtil.getElementExtra(mElement,ViewPage2Constant.DIRECTION))){
            direction = ViewPage2MainEpgUtil.getElementExtra(mElement,ViewPage2Constant.DIRECTION);
        }
        if (!TextUtils.isEmpty(ViewPage2MainEpgUtil.getElementExtra(mElement,ViewPage2Constant.INTERVAL))){
            interval = Long.parseLong(String.valueOf(Integer.parseInt(ViewPage2MainEpgUtil.getElementExtra(mElement,ViewPage2Constant.INTERVAL)) * 1000));
        }

        //String CarouselId = "recom001,recom002,recom003,recom004,recom005,recom006,recom007";
        String CarouselId = ViewPage2MainEpgUtil.getElementExtra(mElement, ViewPage2Constant.CAROUSELID);;
        cloudTv = ViewPage2MainEpgUtil.getElementExtra(mElement, ViewPage2Constant.CLOUD_TV);;

        if (!TextUtils.isEmpty(CarouselId)){
            //CarouselId = "recom001,recom002,recom003,recom004,recom005,recom006,recom007";
            if (null == presenter){
                presenter = new TabItemPresenter();
            }
            presenter.queryPBSRemixRecommend(ViewPage2MainEpgUtil.getPageStaticContentIdList(mRootView), pbsRemixRecommendListener,CarouselId,"10");

            //TODO Test
            //loadCloudTvData();

        }else if (!TextUtils.isEmpty(cloudTv) && "1".equals(cloudTv)){

            loadCloudTvData();
            
        }else{
            mRootView.isHideNotBelongToViewPage(false);
        }
    }

    //处理和彩云数据
    public void loadCloudTvData() {
        HecaiyunServiceImpl hecaiyunService = new HecaiyunServiceImpl();
        hecaiyunService.queryPic(new HecaiyunServiceImpl.OnResponse() {
            @Override
            public void onQueryListener(List<BoothCarouselCont> listBoothCarouselCont) {
                if (null != listBoothCarouselCont && listBoothCarouselCont.size() > 0){
                    SuperLog.debug(TAG,"HeCaiYunService.QueryPic Response = " + JsonParse.listToJsonString(listBoothCarouselCont));
                    recommends = new ArrayList<>();
                    sbRecommendId = new StringBuilder();
                    for (BoothCarouselCont boothCarouselCont : listBoothCarouselCont){
                        if (!TextUtils.isEmpty(boothCarouselCont.getCloudContent().getBigthumbnailURL()) && recommends.size() < 5){
                            Recommend recommend = new Recommend();
                            recommend.setSceneType("4");
                            recommend.setPosterURLCloudTv(boothCarouselCont.getCloudContent().getBigthumbnailURL());
                            recommends.add(recommend);
                        }
                    }
                    if (recommends.size() > 0){
                        SuperLog.debug(TAG,"Hecaiyun data is no null and to show,pic size = "+ recommends.size());
                        loadData(recommends);
                    }else{
                        //TODO 加载默认数据
                        SuperLog.debug(TAG,"Hecaiyun pic is null,show default data");
                        mRootView.isHideNotBelongToViewPage(false);
                    }
                }else{
                    //TODO 加载默认数据
                    SuperLog.debug(TAG,"Hecaiyun no data,show default data");
                    mRootView.isHideNotBelongToViewPage(false);
                }
            }
        });
    }

    //
    private void init(List<Recommend> recommend) {

        currentItem = 0;
        if (direction.equalsIgnoreCase(ORIENTATION_VERTICAL)){
            mViewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        }else{
            mViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        }
        viewPage2Adapter = new ViewPage2Adapter(recommends,mGroup,mElement,mContext,mRootView);
        if (null != recommends && recommends.size() == 1){
            isOnlyData = true;
            onStop();
        }
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewPager2.setAdapter(viewPage2Adapter);
                if (!mViewPager2.isFakeDragging()){
                    mViewPager2.setCurrentItem(0);
                }
                initViewPagerScrollProxy();
                initPointView(recommends.size());
            }
        });

        mViewPager2.registerOnPageChangeCallback(new OnPageChangeCallback(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                SuperLog.debug(TAG,"onPageSelected(position)="+position+"------currentItem="+currentItem);
                // 页面被选中
                // 设置当前页面选中
                if (currentItem == 0 && mPointGroupLinear.getVisibility() == View.VISIBLE){
                    setPointImageViewEffectNew(currentItem);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View childAt = Objects.requireNonNull(((RecyclerView) mViewPager2.getChildAt(0)).getLayoutManager()).findViewByPosition(currentItem);
                if (childAt instanceof ReflectRelativeLayout){
                    isOnClick = true;
                    ((ReflectRelativeLayout)childAt).onClickForViewPage(String.valueOf(currentItem%recommends.size()));
                }
            }
        });
    }

    public String getCloudTvType(){
        return cloudTv;
    }

    //开始定时轮换图片
    public void start() {
        if (isOnlyData || null != mTimer){
            return;
        }
        if (!isOnClick) {
            mViewPager2.setAdapter(viewPage2Adapter);
        }else{
            isOnClick = false;
        }
        SuperLog.debug(TAG, "start()");
        needStop = false;
        //onStop();
        if (null == mTimer) {
            mTimer = new Timer();
        }
        if (null == mTimerTask) {
            mTimerTask = new AutoTask();
        }
        mTimer.schedule(mTimerTask, interval, interval);
    }

    public void onStop() {
        if (isOnlyData && null == mTimer){
            return;
        }
        SuperLog.debug(TAG,"onStop()");
        if (!isOnClick) {
            currentItem = 0;
            //((MainActivity) mContext).runOnUiThread
        }
        needStop = true;
        //先取消定时器
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (null != mTimerTask) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    //设置轮换间隔时间
    public void setInternalTime(Long time) {
        interval = time;
        start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mContext instanceof MainActivity){
                ((MainActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != mViewPager2 && (mViewPager2.isShown() || !needStop)) {
                            currentItem++;
                            SuperLog.debug(TAG,"currentItem="+currentItem);
                            if (!mViewPager2.isFakeDragging()){
                                SuperLog.debug(TAG,"setCurrentItem_____currentItem="+currentItem);
                                mViewPager2.setCurrentItem(currentItem);
                                if (mPointGroupLinear.getVisibility() == View.VISIBLE){
                                    if (recommends.size() > 0){
                                        setPointImageViewEffectNew(currentItem%recommends.size());
                                    }
                                }
                            }
                        } else {
                            onStop();
                        }
                    }
                });
            }else if (mContext instanceof ChildLauncherActivity){
                ((ChildLauncherActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != mViewPager2 && (mViewPager2.isShown() || !needStop)) {
                            currentItem++;
                            SuperLog.debug(TAG,"currentItem="+currentItem);
                            if (!mViewPager2.isFakeDragging()){
                                SuperLog.debug(TAG,"setCurrentItem_____currentItem="+currentItem);
                                mViewPager2.setCurrentItem(currentItem);
                                if (mPointGroupLinear.getVisibility() == View.VISIBLE){
                                    if (recommends.size() > 0){
                                        setPointImageViewEffectNew(currentItem%recommends.size());
                                    }
                                }
                            }
                        } else {
                            onStop();
                        }
                    }
                });
            }
        }
    };
    private AutoHandler mHandler = new AutoHandler();

    private class AutoTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(runnable);
        }
    }

    private static final class AutoHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    public void onResume() {
        needStop = false;
        start();
    }

    public int getCurrentItem(){
        return currentItem;
    }

    //初始化小圆点
    public void initPointView(int size) {

        if (isOnlyData){
            mPointGroupLinear.setVisibility(View.GONE);
            return;
        }
        mPointGroupLinear.setVisibility(View.VISIBLE);
        mPointGroupLinear.removeAllViews();
        for (int i = 0; i < size; i++) {
            // 制作底部小圆点
            ImageView pointImage = new ImageView(mContext);
            pointImage.setImageResource(R.drawable.viewpage2_point_selector);

            // 设置小圆点的布局参数
            int PointSize = mContext.getResources().getDimensionPixelSize(R.dimen.margin_6);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PointSize, PointSize);

            if (i > 0) {
                params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
                pointImage.setSelected(false);
            }else{
                params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
                pointImage.setSelected(true);
            }
            pointImage.setLayoutParams(params);

            // 添加到容器里
            mPointGroupLinear.addView(pointImage);
        }
    }

    private void setPointImageViewEffectNew(int position){
        for (int i = 0;i<mPointGroupLinear.getChildCount();i++){
            if (i == position){
                mPointGroupLinear.getChildAt(i).setSelected(true);
                setPointImageViewEffect((ImageView) mPointGroupLinear.getChildAt(i),true);
            }else{
                mPointGroupLinear.getChildAt(i).setSelected(false);
                setPointImageViewEffect((ImageView) mPointGroupLinear.getChildAt(i),false);
            }
        }
    }

    private void setPointImageViewEffect(ImageView pointImage,boolean isSelect){
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pointImage.getLayoutParams();

        if (isSelect){
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.margin_8);
            layoutParams.width = mContext.getResources().getDimensionPixelSize(R.dimen.margin_8);
        }else{
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.margin_6);
            layoutParams.width = mContext.getResources().getDimensionPixelSize(R.dimen.margin_6);
        }

        pointImage.setLayoutParams(layoutParams);
    }

    //查询跑马灯
    private class PBSRemixRecommendListener implements EpgTopFunctionMenu.OnPBSRemixRecommendListener {

        @Override
        public void getRemixRecommendData(PBSRemixRecommendResponse response) {
            if (null != response && null != response.getRecommends() && response.getRecommends().size() > 0){
                setRecommends(response);
                loadData(recommends);
            }else{
                //加载默认数据
                mRootView.isHideNotBelongToViewPage(false);
            }
        }

        @Override
        public void getRemixRecommendDataFail() {
            //加载默认数据
            mRootView.isHideNotBelongToViewPage(false);
        }
    }

    private void loadData(List<Recommend> recommend){
        if (mContext instanceof MainActivity) {
            ((MainActivity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    init(recommend);
                    start();
                }
            });
        }else if (mContext instanceof ChildLauncherActivity) {
            ((ChildLauncherActivity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    init(recommend);
                    start();
                }
            });
        }
        recordMainRecommend(sbRecommendId.toString());
    }

    private List<Recommend> recommends = new ArrayList<>();
    private StringBuilder sbRecommendId;
    private StringBuilder sbScenId = new StringBuilder();
    private StringBuilder sbrecommendType = new StringBuilder();
    private StringBuilder sbAppointedId = new StringBuilder();

    private void setRecommends(PBSRemixRecommendResponse response){
        recommends = new ArrayList<>();
        //recommendMap = new HashMap<>();
        sbRecommendId = new StringBuilder();
        sbScenId = new StringBuilder();
        sbrecommendType = new StringBuilder();
        sbAppointedId = new StringBuilder();
        for (int i = 0; i < response.getRecommends().size(); i++) {
            Recommend recommend = response.getRecommends().get(i);
            if (TextUtils.isEmpty(recommend.getSceneType())) {
                SuperLog.info2SD(TAG, "智能推荐 SceneType()=null，无法判断智能推荐类型，当成无推荐数据处理");
                continue;
            }
            setScenId(recommend.getSceneId());
            setAppointedId(recommend.getAppointedId());
            setRecommendType(getIdentifyType(recommend.getIdentifyType()));
            if ("0".equalsIgnoreCase(recommend.getSceneType())) {
                //内容
                if (null != recommend.getVODs() && recommend.getVODs().size() > 0) {
                    for (int j = 0; j < recommend.getVODs().size(); j++) {
                        if (recommends.size() < 10){
                            Recommend recommendTem = new Recommend();
                            recommendTem.setIdentifyType(recommend.getIdentifyType());
                            recommendTem.setSceneId(recommend.getSceneId());
                            recommendTem.setAppointedId(recommend.getAppointedId());
                            recommendTem.setSceneType(recommend.getSceneType());
                            List<VOD> vodList = new ArrayList<>();
                            vodList.add(recommend.getVODs().get(j));
                            recommendTem.setVODs(vodList);
                            //setRecomMap(recommendTem);
                            recommends.add(recommendTem);
                            setRecommendId(recommend.getVODs().get(j).getID());
                        }
                    }
                }
            } else if ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType())) {
                //处理H5/专题 页面
                if (null != recommend.getTopics() && recommend.getTopics().size() > 0) {
                    for (int j = 0; j < recommend.getTopics().size(); j++) {
                        if (recommends.size() < 10){
                            Recommend recommendTem = new Recommend();
                            recommendTem.setIdentifyType(recommend.getIdentifyType());
                            recommendTem.setSceneId(recommend.getSceneId());
                            recommendTem.setAppointedId(recommend.getAppointedId());
                            recommendTem.setSceneType(recommend.getSceneType());
                            List<Topic> topics = new ArrayList<>();
                            topics.add(recommend.getTopics().get(j));
                            recommendTem.setTopics(topics);
                            //setRecomMap(recommendTem);
                            recommends.add(recommendTem);
                            if (TopicActivity.TOPIC_H5.equalsIgnoreCase(recommend.getTopics().get(j).getType()) || TopicActivity.TOPIC_ACTIVITY.equalsIgnoreCase(recommend.getTopics().get(j).getType())){
                                setRecommendId(recommend.getTopics().get(j).getTopicURL());
                            }else if (recommend.getTopics().get(j).getType().equalsIgnoreCase(TopicActivity.TOPIC_NATIVE)){
                                setRecommendId(recommend.getTopics().get(j).getId());
                            }
                        }
                    }
                }
            } else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                //apk
                if (null != recommend.getApks() && recommend.getApks().size() > 0) {
                    for (int j = 0; j < recommend.getApks().size(); j++) {
                        if (recommends.size() < 10){
                            Recommend recommendTem = new Recommend();
                            recommendTem.setIdentifyType(recommend.getIdentifyType());
                            recommendTem.setSceneId(recommend.getSceneId());
                            recommendTem.setAppointedId(recommend.getAppointedId());
                            recommendTem.setSceneType(recommend.getSceneType());
                            List<Apk> apks = new ArrayList<>();
                            apks.add(recommend.getApks().get(j));
                            recommendTem.setApks(apks);
                            //setRecomMap(recommendTem);
                            recommends.add(recommendTem);
                            setRecommendId(recommend.getApks().get(j).getId());
                        }
                    }
                }
            }
        }
    }

    private void recordMainRecommend(String recommendId) {
        if (!TextUtils.isEmpty(recommendId)) {
            UBDRecommend.recordMainRecommend(recommendId, sbrecommendType.toString(), sbScenId.toString(),sbAppointedId.toString());
            PbsUaService.report(Desktop.getRecommendData(PbsUaConstant.ActionType.IMPRESSION,null,sbAppointedId.toString(),
                    sbrecommendType.toString(),sbScenId.toString(),recommendId,sbrecommendType.toString()));
        }
    }

    private void setRecommendId(String id){
        if (!TextUtils.isEmpty(id)){
            if (TextUtils.isEmpty(sbRecommendId.toString())){
                sbRecommendId.append(id);
            }else{
                sbRecommendId.append(";");
                sbRecommendId.append(id);
            }
        }
    }
    private void setScenId(String id){
        if (!TextUtils.isEmpty(id)){
            if (TextUtils.isEmpty(sbScenId.toString())){
                sbScenId.append(id);
            }else{
                sbScenId.append(";");
                sbScenId.append(id);
            }
        }
    }

    private void setRecommendType(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbrecommendType.toString())){
            sbrecommendType.append(id);
        }else{
            sbrecommendType.append(";");
            sbrecommendType.append(id);
        }
    }
    public String getIdentifyType(Integer identifyType){
        if (null != identifyType){
            return String.valueOf(identifyType);
        }else{
            return " ";
        }
    }

    private void setAppointedId(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbAppointedId.toString())){
            sbAppointedId.append(id);
        }else{
            sbAppointedId.append(";");
            sbAppointedId.append(id);
        }
    }


    private void initViewPagerScrollProxy() {
        try {
            //控制切换速度，采用反射方。法方法只会调用一次，替换掉内部的RecyclerView的LinearLayoutManager
            RecyclerView recyclerView = (RecyclerView) mViewPager2.getChildAt(0);
            recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            ProxyLayoutManger proxyLayoutManger = new ProxyLayoutManger(mContext, mViewPager2.getOrientation());
            recyclerView.setLayoutManager(proxyLayoutManger);

            Field LayoutMangerField = ViewPager2.class.getDeclaredField("mLayoutManager");
            LayoutMangerField.setAccessible(true);
            LayoutMangerField.set(mViewPager2, proxyLayoutManger);

            Field pageTransformerAdapterField = ViewPager2.class.getDeclaredField("mPageTransformerAdapter");
            pageTransformerAdapterField.setAccessible(true);
            Object mPageTransformerAdapter = pageTransformerAdapterField.get(mViewPager2);
            if (mPageTransformerAdapter != null) {
                Class<?> aClass = mPageTransformerAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mPageTransformerAdapter, proxyLayoutManger);
            }
            Field scrollEventAdapterField = ViewPager2.class.getDeclaredField("mScrollEventAdapter");
            scrollEventAdapterField.setAccessible(true);
            Object mScrollEventAdapter = scrollEventAdapterField.get(mViewPager2);
            if (mScrollEventAdapter != null) {
                Class<?> aClass = mScrollEventAdapter.getClass();
                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
                layoutManager.setAccessible(true);
                layoutManager.set(mScrollEventAdapter, proxyLayoutManger);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ProxyLayoutManger extends LinearLayoutManager {

        ProxyLayoutManger(Context context, int orientation) {
            super(context, orientation, false);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected int calculateTimeForDeceleration(int dx) {
                    return (int) (1000 * (1 - .3356));
                }
            };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

}
