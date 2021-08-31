package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.ShimmerImageView;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.http.v6bean.v6node.Dialect;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.ElementData;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaConstant;
import com.pukka.ydepg.common.report.ubd.pbs.PbsUaService;
import com.pukka.ydepg.common.report.ubd.pbs.scene.Desktop;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommend;
import com.pukka.ydepg.common.report.ubd.scene.UBDSwitch;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.customui.tv.autoscroll.AutoScrollViewPager;
import com.pukka.ydepg.customui.tv.autoscroll.AutoScrollViewPagerAdapter;
import com.pukka.ydepg.event.AutoScrollViewEvent;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.home.view.MarqueeText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoScrollTemplate extends PHMTemplate {
    int realIndex = 0;
    private int viewPosition = 0;
    AutoScrollViewPager viewPager;
    private ShimmerImageView shimmerImageView;
    private RelativeLayoutExt rootView;
    private int dataIndex;
    private Group mGroup;
    private List<Element> mElements;
    private GroupElement mGroupElement;

    private String appointId;
    private TabItemPresenter tabItemPresenter = new TabItemPresenter();
    private PBSRemixRecommendListener pbsRemixRecommendListener = new PBSRemixRecommendListener();
    private List<Recommend> recommends = new ArrayList<>();
    private Map<Integer,Recommend> recommendMap = new HashMap<>();

    private StringBuilder sbAppointedId = new StringBuilder();
    private StringBuilder sbrecommendType = new StringBuilder();

    private ImageView bgImageV;

    private List<VOD> mDataList = new ArrayList<>();

    public AutoScrollTemplate(Context context) {
        super(context);
    }

    public AutoScrollTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context);
        this.fragment = fragment;
        this.onFocusChangeListener = focusChangeListener;
        this.layoutId = layoutId;
        setFocusable(false);
        initView(context, layoutId);
        setElements(fragment);
    }

    public AutoScrollTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void initView(Context context, int layoutId) {
        //注释掉是因为影响了滚动Banner的标题，会和内容重叠
        //super.initView(context, layoutId);
        this.context = context;
        //SuperLog.debug(TAG, "layoutId:" + layoutId);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(layoutId, this, true);
        setClipChildren(false);
        rootView = findViewById(R.id.rl_container_auto_scroll_tem);
        mTitleTv = (TextView) findViewById(R.id.tv_home_title);
        marqueeText = (MarqueeText) findViewById(R.id.tv_marquee_title);
        viewPager = (AutoScrollViewPager) findViewById(R.id.auto_scroll_viewpager);
        shimmerImageView = (ShimmerImageView) findViewById(R.id.simple_epg_shimmer_imageview);
        addFocusEffect();
        rootView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (null != bgImageV){
                    bgImageV.bringToFront();
                    bgImageV.setSelected(hasFocus);
                    bgImageV.setFocusable(hasFocus);
                }
            }
        });
    }

    public void addFocusEffect() {
        bgImageV = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        bgImageV.setLayoutParams(params);
        bgImageV.setImageResource(R.drawable.select_ref_main);
        rootView.addView(bgImageV);
    }

    @Override
    public void setElements(PHMFragment fragment) {
        int childCount = getChildCount();
//        View firstView = getChildAt(0);
//        firstView.setOnFocusChangeListener(onFocusChangeListener);
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof ReflectRelativeLayout) {
                ((ReflectRelativeLayout) view).setFragment(fragment);
                if (onFocusChangeListener != null) {
                    view.setOnFocusChangeListener(onFocusChangeListener);
                }
                elementViews.add((ReflectRelativeLayout) view);
            }
        }
    }

    //解决Banner放在页面第一排资源位焦点无法下移
    public int getNavIndex(){
        return viewPosition;
    }

    public void setNavIndex(int viewPosition){
        this.viewPosition = viewPosition;
    }

    public void bindSubjectVOD(List<VOD> dataList, DataLoaderAdapter adapter, List<Element> elements, int viewPosition) {
        this.viewPosition = viewPosition;
        if (mDataList.size() == 0){
            this.mDataList.addAll(dataList);
        }
        fragment.addFunctionTemplate(viewPosition, AutoScrollTemplate.this);
        if (CollectionUtil.isEmpty(elements) && !TextUtils.isEmpty(appointId)) {
            return;
        }

        loadOtherElements(mElements);

        realIndex = 0;
        loadFirstViewData(mDataList, mElements);
        for (int i = 0; i < elementViews.size(); i++) {
            ReflectRelativeLayout reflectRelativeLayout = elementViews.get(i);
            reflectRelativeLayout.setGroup(mGroupElement.getGroup(), getNavIndex());
            if (reflectRelativeLayout.isDefaultData()) {
                SuperLog.debug(TAG, "i:" + i);
                // 目前仅112135模板右下角element支持滚动文字
                if (i == 5 && mElements.size() > 9 && mElements.get(9).getType().equals("4")) {
                    OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setScrollTexts(reflectRelativeLayout, reflectRelativeLayout.getExtraData());
                        }
                    });
                    if (!CollectionUtil.isEmpty(mElements.get(9).getElementDataList()) && null != mElements.get(9).getElementDataList().get(0) && !CollectionUtil.isEmpty(mElements.get(9).getElementDataList().get(0).getNameDialect())) {
                        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                reflectRelativeLayout.getPosterTitleTv().setText(mElements.get(9).getElementDataList().get(0).getNameDialect().get(0).getValue());
                            }
                        });
                    }
                }
                continue;
            }
            if (!CollectionUtil.isEmpty(mDataList) && realIndex < mDataList.size()) {
                OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reflectRelativeLayout.parseVOD(adapter, mDataList.get(realIndex));
                    }
                });
                realIndex++;
            }
        }
    }

    private void loadOtherElements(List<Element> elements) {
        for (int i = 4; i < elements.size(); i++) {  //如果是本地数据，设置本地显示
            if (i - 4 < elementViews.size()) { //防止数组越界
                ReflectRelativeLayout reflectRelativeLayout = elementViews.get(i - 4);
                reflectRelativeLayout.setGroup(mGroupElement.getGroup(), getNavIndex());
                if (TextUtils.equals(elements.get(i).getForceDefaultData(), "true")) {
                    reflectRelativeLayout.setDefaultData(true);
                    if (!CollectionUtil.isEmpty(elements.get(i).getElementDataList())) {
                        int finalI = i;
                        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                reflectRelativeLayout.setElementData(elements.get(finalI));
                            }
                        });
                    }
                } else {
                    reflectRelativeLayout.setDefaultData(false);
                    int finalI1 = i;
                    OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reflectRelativeLayout.setElementData(elements.get(finalI1));
                        }
                    });
                }
            }
        }
    }

    @Override
    public View getLastView() {
        if (CollectionUtil.isEmpty(elementViews)) {
            return getChildAt(0);
        } else {
            if (lastView == null && elementViews.size() > 0) {
                lastView = elementViews.get(elementViews.size() - 1);
            }
            return lastView;
        }
    }

    //海报光效开关
    private boolean isOpenShimmer = false;
    private  int navIndex = 0;

    @Override
    public void setElementData(GroupElement groupElement, int navIndex) {
        this.mElements = groupElement.getElement();
        this.dataIndex = groupElement.getDataIndex();
        this.mGroup = groupElement.getGroup();
        this.mGroupElement = groupElement;
        this.navIndex = navIndex;

        //获取扩展参数中海报光效的开关
        Map<String,String> extraData = null;
        if (null != mElements && mElements.size() > 0 && null != mElements.get(0).getExtraData()){
            extraData = mElements.get(0).getExtraData();
        }
        if (null != extraData && !TextUtils.isEmpty(extraData.get("Is_Shimmer"))
                && extraData.get("Is_Shimmer").equalsIgnoreCase("true")){
            isOpenShimmer = true;
        }

        /*if (dataIndex != -1) {
            return;
        }*/


        appointId = getApponitId(mElements,mGroup);
        /*if (!TextUtils.isEmpty(appointId)){
            //appointId = "recom007,recom001,recom002,recom003,recom004,recom005,recom006";
            appointId = "recom002,recom004";
        }*/
        if (!TextUtils.isEmpty(appointId)){

            if (null != recommendMap && recommendMap.size() > 0){
                loadRecommendData();
                return;
            }

            if (null != tabItemPresenter){
                tabItemPresenter.queryPBSRemixRecommend(fragment.getContentIdList(),pbsRemixRecommendListener,appointId,"6");
            }
        }else if (dataIndex == -1){
            SuperLog.debug(TAG,"混合推荐appointID为空，加载默认数据");
            //添加element,资源位及设置位置
            loadDefaultElementData();
        }
    }

    //加载默认静态数据
    private void loadDefaultElementData(){
        if (dataIndex != -1) {
            return;
        }
        if (null != mElements){
            loadFirstViewData(null, mElements);
            loadOtherElements(mElements);
            for (int i = 0; i < elementViews.size(); i++) {
                ReflectRelativeLayout reflectRelativeLayout = elementViews.get(i);
                if (reflectRelativeLayout.isDefaultData()) {
                    SuperLog.debug(TAG, "i:" + i);
                    // 目前仅112135模板右下角element支持滚动文字
                    if (i == 5 && mElements.size() > 9 && mElements.get(9).getType().equals("4")) {
                        setScrollTexts(reflectRelativeLayout, reflectRelativeLayout.getExtraData());
                        if (!CollectionUtil.isEmpty(mElements.get(9).getElementDataList()) && null != mElements.get(9).getElementDataList().get(0) && !CollectionUtil.isEmpty(mElements.get(9).getElementDataList().get(0).getNameDialect())) {
                            reflectRelativeLayout.getPosterTitleTv().setText(mElements.get(9).getElementDataList().get(0).getNameDialect().get(0).getValue());
                        }
                    }
                    continue;
                }
            }
        }
    }

    @Override
    public View getFirstView() {
        if (CollectionUtil.isEmpty(elementViews)) {
            return null;
        } else {
            //findViewById(R.id.rl_topItem_container01);
            return rootView;
        }
    }

    private void loadFirstViewData(List<VOD> dataList, List<Element> elements) {

        if (null != dataList && null != elements && elements.size() > 0){
            for (int i = 0; i < elements.size(); i++){
                if (i < 4 && elements.get(i).getForceDefaultData().equalsIgnoreCase("false")){
                    realIndex++;
                }
            }
        }
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(new AutoScrollViewPagerAdapter(context, rootView, onAutoViewPagerItemClickListener, elements, mGroup,dataList, elements.size() > 4 ? 4 : elements.size(), R.layout.item_first_scroll_content_layout));
                String time = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.BANNER_SWITCHING_TIME);
                if (!TextUtils.isEmpty(time)) {
                    long newTime = Long.parseLong(time) * 1000;
                    viewPager.setTime(newTime);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(AutoScrollViewEvent event){
        if (event.isStartShimmer() && isOpenShimmer){
            shimmerImageView.startShimmer();
        }else{
            shimmerImageView.stopShimmer();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        fragment.addFunctionTemplate(viewPosition, this);
        if (null != viewPager) {
            viewPager.onResume();
        }
        recordMainRecommend(sbRecommendId.toString());
        recordMainRecommendJiuTian(sbRecommendItemId.toString());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        fragment.removeVideoTemplate(viewPosition);
        if (null != viewPager) {
            viewPager.onDestroy();
        }
    }

    @Override
    public void onFragmentPause() {
        super.onFragmentPause();
        if (null != viewPager) {
            viewPager.onDestroy();
        }
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        if (fragment.getUserVisibleHint() && null != viewPager) {
            viewPager.onResume();
        }
    }

    private AutoScrollViewPagerAdapter.OnAutoViewPagerItemClickListener onAutoViewPagerItemClickListener = new AutoScrollViewPagerAdapter.OnAutoViewPagerItemClickListener() {
        @Override
        public void onItemClick(Element element, VOD vod, boolean forceDefaultData) {
            String mActionUrl = "";
            Element mElement = element;
            if (forceDefaultData) {
                ElementData elementData = element.getElementDataList().get(0);
                if (!TextUtils.isEmpty(elementData.getElementAction().getActionURL())) {
                    mActionUrl = elementData.getElementAction().getActionURL();
                    String mActionType = elementData.getElementAction().getActionType();
                    ZJVRoute.route(context, ZJVRoute.LauncherElementDataType.STATIC_ITEM, mActionUrl, mActionType, "", null, element.getExtraData());
                }
            } else {
                if (dataIndex == -1 || null == vod) {
                    return;
                }
                ZJVRoute.route(context, ZJVRoute.LauncherElementDataType.VOD, null, null, vod.getID(), vod, element.getExtraData());
            }

            UBDSwitch.getInstance().getMainExtensionField().setRecommendType(mElement.getRecommendType());
            UBDSwitch.getInstance().getMainExtensionField().setSceneId(mElement.getSceneId());
            UBDSwitch.getInstance().getMainExtensionField().setAppointedId(mElement.getAppointedId());
            //UBDSwitch.getInstance().getMainExtensionField().setOptType(UBDRecommend.getOptType(sceneId));

            UBDSwitch.getInstance().recordMainOnclick(mActionUrl,mElement,mGroup,vod,null);
            if (null != recommendMap && recommendMap.size() > 0){
                if (!TextUtils.isEmpty(mElement.getClickTrackerUrl())){
                    JiutianService.reportClick(mElement.getClickTrackerUrl());
                }
                PbsUaService.report(Desktop.getRecommendData(PbsUaConstant.ActionType.CLICK,mElement.getRecommendId(),mElement.getAppointedId(),
                        mElement.getRecommendType(),mElement.getSceneId(),mElement.getRecommendId(),mElement.getRecommendType()));
            }
        }
    };

    private class PBSRemixRecommendListener implements EpgTopFunctionMenu.OnPBSRemixRecommendListener {

        @Override
        public void getRemixRecommendData(PBSRemixRecommendResponse response) {
            //查询成功 返回值notice及跳转连接
            /**
             * 推荐类型（sceneType）：0 内容，1 专题，2 活动，3 apk（含自有和第三方）
             * */
            if (null != response && null != response.getRecommends() && response.getRecommends().size() > 0) {
                recommendMap = new HashMap<>();
                recommends = new ArrayList<>();
                sbRecommendId = new StringBuilder();
                sbRecommendItemId = new StringBuilder();
                sbScenId = new StringBuilder();
                sbAppointedId = new StringBuilder();
                sbrecommendType = new StringBuilder();
                for (int i = 0; i < response.getRecommends().size(); i++) {
                    Recommend recommend = response.getRecommends().get(i);
                    if (TextUtils.isEmpty(recommend.getSceneType())) {
                        SuperLog.info2SD(TAG, "智能推荐 SceneType()=null，无法判断智能推荐类型，当成无推荐数据处理");
                        continue;
                    }
                    if ("0".equalsIgnoreCase(recommend.getSceneType())) {
                        //内容
                        if (null != recommend.getVODs() && recommend.getVODs().size() > 0) {
                            for (int j = 0; j < recommend.getVODs().size(); j++) {
                                Recommend recommendTem = new Recommend();
                                recommendTem.setSceneId(recommend.getSceneId());
                                recommendTem.setAppointedId(recommend.getAppointedId());
                                recommendTem.setSceneType(recommend.getSceneType());
                                recommendTem.setIdentifyType(recommend.getIdentifyType());
                                List<VOD> vodList = new ArrayList<>();
                                vodList.add(recommend.getVODs().get(j));
                                recommendTem.setVODs(vodList);
                                recommends.add(recommendTem);
                            }
                        }
                    } else if ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType())) {
                        //处理H5/专题 页面
                        if (null != recommend.getTopics() && recommend.getTopics().size() > 0) {
                            for (int j = 0; j < recommend.getTopics().size(); j++) {
                                Recommend recommendTem = new Recommend();
                                recommendTem.setSceneId(recommend.getSceneId());
                                recommendTem.setAppointedId(recommend.getAppointedId());
                                recommendTem.setSceneType(recommend.getSceneType());
                                recommendTem.setIdentifyType(recommend.getIdentifyType());
                                List<Topic> topics = new ArrayList<>();
                                topics.add(recommend.getTopics().get(j));
                                recommendTem.setTopics(topics);
                                recommends.add(recommendTem);
                            }
                        }
                    } else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                        //处理H5/专题 页面
                        if (null != recommend.getApks() && recommend.getApks().size() > 0) {
                            for (int j = 0; j < recommend.getApks().size(); j++) {
                                Recommend recommendTem = new Recommend();
                                recommendTem.setSceneId(recommend.getSceneId());
                                recommendTem.setAppointedId(recommend.getAppointedId());
                                recommendTem.setSceneType(recommend.getSceneType());
                                recommendTem.setIdentifyType(recommend.getIdentifyType());
                                List<Apk> apks = new ArrayList<>();
                                apks.add(recommend.getApks().get(j));
                                recommendTem.setApks(apks);
                                recommends.add(recommendTem);
                            }
                        }
                    }
                }

                setRecommendMap();

                loadRecommendData();

                recordMainRecommend(sbRecommendId.toString());
                recordMainRecommendJiuTian(sbRecommendItemId.toString());
                /* else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                    //处理三方app/页面内部  跳转
                    loadApk(response);
                }*/
            } else {
                loadDefaultElementData();
                bindSubjectVOD(mDataList,null,mElements,viewPosition);
            }
        }
        @Override
        public void getRemixRecommendDataFail() {
            loadDefaultElementData();
            bindSubjectVOD(mDataList,null,mElements,viewPosition);
        }
    }

    private void setRecommendMap(){
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < mGroupElement.getElement().size(); i++) {
            Map<Integer, String> elementAppMap = getElementAppMap();
            if (null != elementAppMap && elementAppMap.containsKey(i)) {
                for (int j = 0;j < recommends.size();j++) {
                    if (recommends.get(j).getAppointedId().equalsIgnoreCase(elementAppMap.get(i))){
                        stringList.add(elementAppMap.get(i));
                        recommendMap.put(i, recommends.get(j));
                        break;
                    }
                }
            }
        }
        for (int j = 0;j < recommends.size();j++) {
            if (!stringList.contains(recommends.get(j).getAppointedId())){
                for (int i = 0;i < mGroupElement.getElement().size();i++){
                    if (!recommendMap.containsKey(i)){
                        recommendMap.put(i, recommends.get(j));
                        break;
                    }
                }
            }
        }
        for (Recommend recommend : recommendMap.values()){
            display_tracker = recommend.getDisplay_tracker();
            setAppointedId(recommend.getAppointedId());
            setRecommendType(getIdentifyType(recommend.getIdentifyType()));
            if ("0".equalsIgnoreCase(recommend.getSceneType()) && null != recommend.getVODs() && recommend.getVODs().size() > 0){
                setRecommendId(recommend.getVODs().get(0).getID());
                setRecommendItemId(recommend.getVODs().get(0).getItemid());
                setScenId(recommend.getSceneId());
            }else if (("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType()))
                    && null != recommend.getTopics() && recommend.getTopics().size() > 0){
                if (TopicActivity.TOPIC_H5.equalsIgnoreCase(recommend.getTopics().get(0).getType()) || TopicActivity.TOPIC_ACTIVITY.equalsIgnoreCase(recommend.getTopics().get(0).getType())){
                    setRecommendId(recommend.getTopics().get(0).getTopicURL());
                    setScenId(recommend.getSceneId());
                }else if (recommend.getTopics().get(0).getType().equalsIgnoreCase(TopicActivity.TOPIC_NATIVE)){
                    setRecommendId(recommend.getTopics().get(0).getRelationSubjectId());
                    setScenId(recommend.getSceneId());
                }
            }else if ("3".equalsIgnoreCase(recommend.getSceneType()) && null != recommend.getApks() && recommend.getApks().size() > 0){
                setRecommendId(recommend.getApks().get(0).getId());
                setScenId(recommend.getSceneId());
            }
        }
    }

    //加载混合推荐的数据
    private void loadRecommendData() {
        int vodIndex = 0;
        if (null == mElements && mElements.size() == 0) {
            return;
        }
        for (int position = 0; position < mElements.size(); position++) {
            Recommend recommend = null;
            if (recommendMap.containsKey(position)) {
                recommend = recommendMap.get(position);
            }
            if (null != recommend && position < 4){
                mElements.get(position).setSceneId(recommend.getSceneId());
                mElements.get(position).setAppointedId(recommend.getAppointedId());
                mElements.get(position).setRecommendType(String.valueOf(recommend.getIdentifyType()));
            }
            if (null != recommend && "0".equalsIgnoreCase(recommend.getSceneType()) && null != recommend.getVODs() && recommend.getVODs().size() > 0) {
                if (position < 4) {
                    mElements.get(position).setForceDefaultData("false");
                    mDataList.add(vodIndex, recommend.getVODs().get(0));
                    vodIndex++;
                } else if (position - 4 < elementViews.size()) { //防止数组越界
                    ReflectRelativeLayout reflectRelativeLayout = elementViews.get(position - 4);
                    loadVod(reflectRelativeLayout, position, recommend.getVODs().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
                }
            } else if (null != recommend && ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType()))
                    && null != recommend.getTopics() && recommend.getTopics().size() > 0) {
                ReflectRelativeLayout reflectRelativeLayout = null;
                if (position >= 4) {
                    reflectRelativeLayout = elementViews.get(position - 4);
                }
                loadH5AndTopic(reflectRelativeLayout, position, recommend.getTopics().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());

            } else if (null != recommend && "3".equalsIgnoreCase(recommend.getSceneType()) && null != recommend.getApks() && recommend.getApks().size() > 0) {
                ReflectRelativeLayout reflectRelativeLayout = null;
                if (position >= 4) {
                    reflectRelativeLayout = elementViews.get(position - 4);
                }
                loadApk(reflectRelativeLayout, position, recommend.getApks().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
            }else if (position >= 4 && position - 4 < elementViews.size()) {
                ReflectRelativeLayout reflectRelativeLayout = elementViews.get(position - 4);
                loadDefaultVod(reflectRelativeLayout, position,vodIndex);
                vodIndex++;
            }
        }
        loadFirstViewData(mDataList, mElements);
        //bindSubjectVOD(mDataList, null, mElements, viewPosition);
    }

    private void loadVod(ReflectRelativeLayout reflectRelativeLayout,int index,VOD vod,String sceneId,String identifyType,String appointedId) {
        reflectRelativeLayout.setDefaultData(false);
        Element element = mElements.get(index);
        element.setForceDefaultData("false");
        reflectRelativeLayout.setElement(element);
        /*reflectRelativeLayout.setIsRecommend(true, sceneId, RecommendData.RecommendType.CVI_TYPE);
        setRecommendId(vod.getID());*/
        reflectRelativeLayout.setGroup(mGroupElement.getGroup(), getNavIndex());
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != vod.getFeedback() && !TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(vod.getFeedback().getClick_tracker());
                    element.setClickTrackerUrl(vod.getFeedback().getClick_tracker());
                }

                element.setRecommendType(identifyType);
                element.setRecommendId(vod.getID());
                element.setSceneId(sceneId);
                element.setAppointedId(appointedId);

                reflectRelativeLayout.setIsRecommend(true,sceneId, identifyType);
                reflectRelativeLayout.setAppointedId(appointedId);
                reflectRelativeLayout.parseVOD(null, vod);
            }
        });
    }
    private void loadDefaultVod(ReflectRelativeLayout reflectRelativeLayout,int index,int vodIndex) {
        Element element = mElements.get(index);
        reflectRelativeLayout.setElement(element);
        /*reflectRelativeLayout.setIsRecommend(true, sceneId, RecommendData.RecommendType.CVI_TYPE);
        setRecommendId(vod.getID());*/
        reflectRelativeLayout.setGroup(mGroupElement.getGroup(), getNavIndex());
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (vodIndex < mDataList.size()-1){
                    reflectRelativeLayout.setIsRecommend(false,null, RecommendData.RecommendType.HAND_TYPE);
                    reflectRelativeLayout.parseVOD(null, mDataList.get(vodIndex));
                }
            }
        });
    }

    private void loadApk(ReflectRelativeLayout reflectRelativeLayout, int realIndex, Apk apk,String sceneId,String identifyType,String appointedId) {
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        Element element = mElements.get(realIndex);
        element.setForceDefaultData("true");
        //设置海报
        element.getElementDataList().get(0).setContentURL(apk.getPosterURL());
        //设置标题Title
        List<Dialect> nameDialect = new ArrayList<>();
        Dialect dialect = new Dialect();
        dialect.setLanguage("zh");
        dialect.setValue(apk.getName());
        nameDialect.add(dialect);
        element.getElementDataList().get(0).setNameDialect(nameDialect);
        String actionURL = element.getElementDataList().get(0).getElementAction().getActionURL();
        //H5专题和活动推荐 url设置给actionUrl即可
        if (apk.getPkg().equalsIgnoreCase(EpgTopFunctionMenu.EPG_PKG)){
            //内部页面跳转
            StringBuffer sb = new StringBuffer();
            //sb.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE+"=");
            SuperLog.debug(TAG,"actionUrl_apk.getCls()="+apk.getCls());
            String actionUrl = getActionUrlForEpgInner(apk.getCls());
            SuperLog.debug(TAG,"actionUrl="+actionUrl);
            if (actionUrl.contains("ContentType=PAGE")){
                element.getElementDataList().get(0).getElementAction().setActionType(ZJVRoute.LauncherElementActionType.TYPE_2);
            }
            sb.append(actionUrl);
            element.getElementDataList().get(0).getElementAction().setActionURL(sb.toString());
            //setRecommendId(topic.getTopicURL());
        }else {
            //三方App跳转 AppPkg=com.syntc.mushroomjump&ContentType=APK&Version=6
            StringBuffer sb = new StringBuffer();
            sb.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE+"=");
            sb.append(ZJVRoute.LauncherElementContentType.APK);
            sb.append("&"+ZJVRoute.ActionUrlKeyType.APP_PKG+"=");
            sb.append(apk.getPkg());
            sb.append("&"+ZJVRoute.ActionUrlKeyType.APP_CLASS+"=");
            sb.append(apk.getCls());
            //set actionurl
            element.getElementDataList().get(0).getElementAction().setActionURL(sb.toString());
            List<com.pukka.ydepg.launcher.bean.node.NamedParameter> extras = apk.getExtras();
            if (null != extras && extras.size() > 0){
                Map extraData = element.getExtraData();
                if (null == extraData){
                    extraData = new HashMap();
                }
                for (com.pukka.ydepg.launcher.bean.node.NamedParameter namedParameter : extras){
                    if (!extraData.containsKey(namedParameter.getKey())){
                        extraData.put(namedParameter.getKey(),namedParameter.getFistItemFromValue());
                    }
                }
                element.setExtraData(extraData);
            }
            //setRecommendId(topic.getId());
        }
        //set组织好的element
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (realIndex >= 4){
                    if (null == reflectRelativeLayout){
                        if (null != apk.getFeedback() && !TextUtils.isEmpty(apk.getFeedback().getClick_tracker())){
                            reflectRelativeLayout.setClickTrackerUrl(apk.getFeedback().getClick_tracker());
                            element.setClickTrackerUrl(apk.getFeedback().getClick_tracker());
                        }

                        element.setRecommendType(identifyType);
                        element.setRecommendId(apk.getId());
                        element.setSceneId(sceneId);
                        element.setAppointedId(appointedId);

                        reflectRelativeLayout.setDefaultData(true);
                        reflectRelativeLayout.setIsRecommend(true,sceneId, identifyType);
                        reflectRelativeLayout.setAppointedId(appointedId);
                        reflectRelativeLayout.setGroup(mGroup, navIndex);
                        reflectRelativeLayout.setElementData(element);
                    }
                }
            }
        });
    }

    private void loadH5AndTopic(ReflectRelativeLayout reflectRelativeLayout, int index, Topic topic,String sceneId,String identifyType,String appointedId) {
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        //setRecommendId(topic.getId());
        if (null == mElements && index >= mElements.size()){
            return;
        }
        Element element = mElements.get(index);
        //设置海报
        element.setForceDefaultData("true");
        element.getElementDataList().get(0).setContentURL(topic.getPosterUrl());
        //设置标题Title
        List<Dialect> nameDialect = new ArrayList<>();
        Dialect dialect = new Dialect();
        dialect.setLanguage("zh");
        dialect.setValue(topic.getName());
        nameDialect.add(dialect);
        element.getElementDataList().get(0).setNameDialect(nameDialect);
        //H5专题和活动推荐 url设置给actionUrl即可
        if (topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_H5) || topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_ACTIVITY)){
            element.getElementDataList().get(0).getElementAction().setActionURL(topic.getTopicURL());
        }else if (topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_NATIVE)){//普通专题
            //native原生专题，用推荐接口请求回来的数据拼接actionUrl,set到Reflect中，后面走统一逻辑
            StringBuffer buffer = new StringBuffer();
            buffer.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
            buffer.append("=");
            buffer.append(ZJVRoute.LauncherElementContentType.VOD_CATEGORY);
            buffer.append("&");
            buffer.append(ZJVRoute.ActionUrlKeyType.CONTENT_ID);
            buffer.append("=");
            buffer.append(topic.getRelationSubjectId());
            element.getElementDataList().get(0).getElementAction().setActionURL(buffer.toString());
        }

        //reflectRelativeLayout.setIsRecommend(isRecommend,sceneId,RecommendData.RecommendType.CVI_TYPE);
        //set组织好的element
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (index >= 4){
                    if (null == reflectRelativeLayout){
                        if (null != topic.getFeedback() && !TextUtils.isEmpty(topic.getFeedback().getClick_tracker())){
                            reflectRelativeLayout.setClickTrackerUrl(topic.getFeedback().getClick_tracker());
                            element.setClickTrackerUrl(topic.getFeedback().getClick_tracker());
                        }

                        element.setRecommendType(identifyType);
                        element.setRecommendId(topic.getId());
                        element.setSceneId(sceneId);
                        element.setAppointedId(appointedId);

                        reflectRelativeLayout.setIsRecommend(true,sceneId, identifyType);
                        reflectRelativeLayout.setAppointedId(appointedId);
                        reflectRelativeLayout.setDefaultData(true);
                        reflectRelativeLayout.setGroup(mGroupElement.getGroup(), getNavIndex());
                        reflectRelativeLayout.setElementData(element);
                    }
                }
            }
        });
    }

    private String display_tracker;
    private StringBuilder sbRecommendId = new StringBuilder();
    private StringBuilder sbRecommendItemId = new StringBuilder();
    private StringBuilder sbScenId = new StringBuilder();

    private void recordMainRecommend(String recommendId) {
        if (!TextUtils.isEmpty(recommendId)) {
            UBDRecommend.recordMainRecommend(recommendId, sbrecommendType.toString(), sbScenId.toString(),sbAppointedId.toString());
            PbsUaService.report(Desktop.getRecommendData(PbsUaConstant.ActionType.IMPRESSION,null,sbAppointedId.toString(),
                    sbrecommendType.toString(),sbScenId.toString(),recommendId,sbrecommendType.toString()));
        }
    }
    private void recordMainRecommendJiuTian(String itemId) {
        if (!TextUtils.isEmpty(itemId) && !TextUtils.isEmpty(display_tracker)){
            JiutianService.reportDisplay(display_tracker,itemId);
        }
    }

    private void setRecommendId(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbRecommendId.toString())){
            sbRecommendId.append(id);
        }else{
            sbRecommendId.append(";");
            sbRecommendId.append(id);
        }
    }

    private void setRecommendItemId(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbRecommendItemId.toString())){
            sbRecommendItemId.append(id);
        }else{
            sbRecommendItemId.append(";");
            sbRecommendItemId.append(id);
        }
    }

    private void setScenId(String id){
        if (TextUtils.isEmpty(id)){
            id = " ";
        }
        if (TextUtils.isEmpty(sbScenId.toString())){
            sbScenId.append(id);
        }else{
            sbScenId.append(";");
            sbScenId.append(id);
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
}
