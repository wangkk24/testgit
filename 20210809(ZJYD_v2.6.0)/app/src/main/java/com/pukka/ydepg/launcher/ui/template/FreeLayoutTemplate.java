package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.VerticalScrollTextView;
import com.pukka.ydepg.common.http.v6bean.v6node.Dialect;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.ExtraData;
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
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.util.AuthenticateManager;
import com.pukka.ydepg.launcher.util.ComparatorElementIndex;
import com.pukka.ydepg.launcher.util.ComparatorElementLeft;
import com.pukka.ydepg.launcher.util.ComparatorIndex;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;
import com.pukka.ydepg.moudule.home.view.MarqueeText;
import com.pukka.ydepg.moudule.search.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eason on 26-May-20.
 * 自由编排桌面 Template
 */

public class FreeLayoutTemplate extends PHMTemplate{

    private Context mContext;
    private RelativeLayout mRelaContent;

    private String appointId;
    private TabItemPresenter tabItemPresenter = new TabItemPresenter();
    private PBSRemixRecommendListener pbsRemixRecommendListener = new PBSRemixRecommendListener();
    private List<Recommend> recommends = new ArrayList<>();

    private GroupElement mGroupElement;
    private List<VOD> vods;

    private PHMFragment mFragment;

    private String display_tracker;
    private StringBuilder sbRecommendId = new StringBuilder();
    private StringBuilder sbRecommendItemId = new StringBuilder();
    private StringBuilder sbScenId = new StringBuilder();
    private StringBuilder sbAppointedId = new StringBuilder();
    private StringBuilder sbrecommendType = new StringBuilder();
    private Map<Integer,Recommend> recommendMap = new HashMap<>();

    private int navIndex = 0;

    private ReflectRelativeLayout mFirstView = null;

    //private RelativeLayout.LayoutParams mElementParams;
    //private RelativeLayout.LayoutParams mGroupParams;

    public FreeLayoutTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
        this.mContext = context;
    }

    @Override
    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);
        mRelaContent = findViewById(R.id.rela_group_content);
    }

    private boolean isHasIndexEveryElement(List<Element> elements) {
        for (Element element : elements) {
            if (TextUtils.isEmpty(element.getIndex())) {
                return false;
            }
        }
        return true;
    }

    public void setDatas(GroupElement groupElement,List<VOD> vods,int navIndex,PHMFragment phmFragment){
        this.mGroupElement = groupElement;
        this.vods = vods;
        this.navIndex = navIndex;
        this.mFragment = phmFragment;

        //设置控件高度
        String groupHeight = groupElement.getGroup().getGroupHeight();
        if (!TextUtils.isEmpty(groupHeight)){
            RelativeLayout.LayoutParams mElementParams = new RelativeLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels, resetData(Integer.parseInt(groupHeight)));
            mElementParams.addRule(RelativeLayout.BELOW,R.id.rv_title);
            mElementParams.setMargins((int)context.getResources().getDimension(R.dimen.pan_common_group_marginLeft),(int)context.getResources().getDimension(R.dimen.margin_8),0,(int)context.getResources().getDimension(R.dimen.margin_10));
            mRelaContent.setLayoutParams(mElementParams);
        }

        if (null != mGroupElement.getElement() && mGroupElement.getElement().size() > 0){
            if (isHasIndexEveryElement(mGroupElement.getElement())){
                Collections.sort(mGroupElement.getElement(), new ComparatorElementIndex());
            }else{
                Collections.sort(mGroupElement.getElement(), new ComparatorElementLeft());
            }
        }
        //数据为null,不处理
        if (null == mGroupElement || null == mGroupElement || CollectionUtil.isEmpty(mGroupElement.getElement())){
            return;
        }

        appointId = getApponitId(mGroupElement.getElement(),mGroupElement.getGroup());
        /*if (TextUtils.isEmpty(appointId)){
            appointId = "recom007,recom001,recom002,recom003,recom004,recom005,recom006";
        }*/
        if (!TextUtils.isEmpty(appointId)){
            if (null != tabItemPresenter){
                tabItemPresenter.queryPBSRemixRecommend(fragment.getContentIdList(),pbsRemixRecommendListener,appointId,"6");
            }
        }else{
            SuperLog.debug(TAG,"混合推荐appointID为空，加载默认数据");
            //添加element,资源位及设置位置
            loadDefaultData(mGroupElement,vods);
        }
    }

    public int getNavIndex(){
        return navIndex;
    }

    public void setNavIndex(int navIndex){
        this.navIndex = navIndex;
    }

    //适配DPI==240,x1.5
    public int resetData(int value) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return value*dm.widthPixels/1920;
    }
    //加载自由模板默认数据
    private void loadDefaultData(GroupElement groupElement,List<VOD> vods) {
        int realIndex = 0;
        List<Element> elements = groupElement.getElement();
        Group group = groupElement.getGroup();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            ReflectRelativeLayout mReflectRelativeLayout = (ReflectRelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.fragment_content_item, null);
            RelativeLayout.LayoutParams mElementParams = new RelativeLayout.LayoutParams(resetData(Integer.parseInt(element.getWidth())), resetData((Integer.parseInt(element.getHeight()))));
            mElementParams.setMargins(resetData(Integer.parseInt(element.getLeft())), resetData(Integer.parseInt(element.getTop())), 0, 0);
            mReflectRelativeLayout.setLayoutParams(mElementParams);
            mReflectRelativeLayout.setFragment(mFragment);
            mReflectRelativeLayout.setIsRecommend(false, null, RecommendData.RecommendType.HAND_TYPE);
            mReflectRelativeLayout.setOnFocusChangeListener(onFocusChangeListener);
            mReflectRelativeLayout.setGroup(group,getNavIndex());
            mReflectRelativeLayout.setElement(element);
            if (TextUtils.equals(element.getForceDefaultData(), "true")) {
                mReflectRelativeLayout.setDefaultData(true);
                OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mReflectRelativeLayout.setElementData(element);
                    }
                });
            } else {
                mReflectRelativeLayout.setDefaultData(false);
                if (null != vods && vods.size() > realIndex) {
                    int finalRealIndex = realIndex;
                    OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReflectRelativeLayout.parseVOD(new TypeThreeLoader(), vods.get(finalRealIndex));
                        }
                    });
                    realIndex++;
                } else if (null != vods && vods.size() > i) {
                    int finalI = i;
                    OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReflectRelativeLayout.parseVOD(new TypeThreeLoader(), vods.get(finalI));
                        }
                    });
                }
            }
            if (i == 0){
                mFirstView = mReflectRelativeLayout;
            }
            OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRelaContent.addView(mReflectRelativeLayout);
                }
            });
            elementViews.add(mReflectRelativeLayout);
        }

        //设置模板背景
        setExpendGroupParams(groupElement.getGroup().getExtraData(), groupElement.getGroup().getBackgroud());

    }

    public View getFirstView(){
        /*if(mRelaContent.getChildCount() > 0){
            return mRelaContent.getChildAt(0);
        }*/
        return mFirstView;
    }

    private boolean isNeedShowNotice(int width,int height){
        if (width > 408 && height > 264){
            return true;
        }
        return false;
    }
    //先加载混合推荐数据，再加载默认数据
    private void loadRecommendData(){
        int realIndex = 0;
        for (int position = 0; position < mGroupElement.getElement().size(); position++) {
            Element element = mGroupElement.getElement().get(position);
            final Integer width = resetData(Integer.parseInt(element.getWidth()));
            final Integer height = resetData(Integer.parseInt(element.getHeight()));

            ReflectRelativeLayout mReflectRelativeLayout = (ReflectRelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.fragment_content_item, null);
            RelativeLayout.LayoutParams mElementParams = new RelativeLayout.LayoutParams(width, height);
            mElementParams.setMargins(resetData(Integer.parseInt(element.getLeft())), resetData(Integer.parseInt(element.getTop())), 0, 0);
            SuperLog.debug(TAG, "element.getWidth()=" + element.getWidth() + ";element.getHeight()=" + element.getHeight() + ";element.getLeft()=" + element.getLeft() + ";element.getTop()=" + element.getTop());
            mReflectRelativeLayout.setLayoutParams(mElementParams);
            mReflectRelativeLayout.setFragment(mFragment);
            mReflectRelativeLayout.setOnFocusChangeListener(onFocusChangeListener);
            mReflectRelativeLayout.setGroup(mGroupElement.getGroup(),getNavIndex());

            boolean isNeedShowNotice = isNeedShowNotice(width, height);

            Recommend recommend = null;
            if (recommendMap.containsKey(position)) {
                recommend = recommendMap.get(position);
            }
            if (null != recommend && "0".equalsIgnoreCase(recommend.getSceneType()) && null != recommend.getVODs() && recommend.getVODs().size() > 0) {
                loadVod(mReflectRelativeLayout, position, recommend.getVODs().get(0), isNeedShowNotice,recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
            } else if (null != recommend && ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType()))
                    && null != recommend.getTopics() && recommend.getTopics().size() > 0) {
                loadH5AndTopic(mReflectRelativeLayout, position, recommend.getTopics().get(0), isNeedShowNotice,recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
            } else if (null != recommend && "3".equalsIgnoreCase(recommend.getSceneType())
                    && null != recommend.getApks() && recommend.getApks().size() > 0) {
                loadApk(mReflectRelativeLayout, position, recommend.getApks().get(0), isNeedShowNotice,recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
            } else if (TextUtils.equals(element.getForceDefaultData(), "true")) {
                mReflectRelativeLayout.setDefaultData(true);
                OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mReflectRelativeLayout.setElementData(element);
                    }
                });
            } else {
                mReflectRelativeLayout.setDefaultData(false);
                if (null != vods && vods.size() > realIndex) {
                    int finalRealIndex = realIndex;
                    OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReflectRelativeLayout.parseVOD(new TypeThreeLoader(), vods.get(finalRealIndex));
                        }
                    });
                    realIndex++;
                } else if (null != vods && vods.size() > position) {
                    int finalPosition = position;
                    OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReflectRelativeLayout.parseVOD(new TypeThreeLoader(), vods.get(finalPosition));
                        }
                    });
                }
            }
            if (position == 0){
                mFirstView = mReflectRelativeLayout;
            }
            OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRelaContent.addView(mReflectRelativeLayout);
                    elementViews.add(mReflectRelativeLayout);
                }
            });
        }

        //设置模板背景
        setExpendGroupParams(mGroupElement.getGroup().getExtraData(), mGroupElement.getGroup().getBackgroud());

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        recordMainRecommend(sbRecommendId.toString());
        recordMainRecommendJiuTian(sbRecommendItemId.toString());
    }

    @Override
    public void onFragmentPause() {
        super.onFragmentPause();
        if (!CollectionUtil.isEmpty(elementViews)){
            for (ReflectRelativeLayout relativeLayout : elementViews){
                relativeLayout.onPause();
            }
        }
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        if (!CollectionUtil.isEmpty(elementViews)){
            for (ReflectRelativeLayout relativeLayout : elementViews){
                relativeLayout.onResume();
            }
        }
    }

    private void setRecommends(PBSRemixRecommendResponse response){
        recommends = new ArrayList<>();
        recommendMap = new HashMap<>();
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
                        //setRecommendId(recommend.getVODs().get(j).getID());
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
                        /*if (TopicActivity.TOPIC_H5.equalsIgnoreCase(recommend.getTopics().get(j).getType()) || TopicActivity.TOPIC_ACTIVITY.equalsIgnoreCase(recommend.getTopics().get(j).getType())){
                            setRecommendId(recommend.getTopics().get(j).getTopicURL());
                        }else if (recommend.getTopics().get(j).getType().equalsIgnoreCase(TopicActivity.TOPIC_NATIVE)){
                            setRecommendId(recommend.getTopics().get(j).getId());
                        }*/
                    }
                }
            } else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                //apk
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
                        //setRecomMap(recommendTem);
                        recommends.add(recommendTem);
                        //setRecommendId(recommend.getApks().get(j).getId());
                    }
                }
            }
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

    private class PBSRemixRecommendListener implements EpgTopFunctionMenu.OnPBSRemixRecommendListener {

        @Override
        public void getRemixRecommendData(PBSRemixRecommendResponse response) {
            //查询成功 返回值notice及跳转连接
            /**
             * 推荐类型（sceneType）：0 内容，1 专题，2 活动，3 apk（含自有和第三方）
             * */
            if (null != response && null != response.getRecommends() && response.getRecommends().size() > 0) {

                setRecommends(response);
                setRecommendMap();

                OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadRecommendData();
                    }
                });
                recordMainRecommend(sbRecommendId.toString());
                recordMainRecommendJiuTian(sbRecommendItemId.toString());
                /* else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                    //处理三方app/页面内部  跳转
                    loadApk(response);
                }*/
            } else {
                OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadDefaultData(mGroupElement,vods);
                    }
                });
            }
        }

        @Override
        public void getRemixRecommendDataFail() {
            OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadDefaultData(mGroupElement,vods);
                }
            });
        }
    }

    private void loadVod(ReflectRelativeLayout reflectRelativeLayout,int index,VOD vod,boolean isNeedShowNotice,String sceneId,String identifyType,String appointedId) {
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isNeedShowNotice){
                    mGroupElement.getElement().get(index).setNotice(vod.getNotice());
                }
                if (null != vod.getFeedback() && !TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(vod.getFeedback().getClick_tracker());
                }

                mGroupElement.getElement().get(index).setRecommendType(identifyType);
                mGroupElement.getElement().get(index).setRecommendId(vod.getID());
                mGroupElement.getElement().get(index).setSceneId(sceneId);
                mGroupElement.getElement().get(index).setAppointedId(appointedId);

                reflectRelativeLayout.setIsRecommend(true, sceneId, identifyType);
                reflectRelativeLayout.setAppointedId(appointedId);
                reflectRelativeLayout.setDefaultData(false);
                reflectRelativeLayout.setElement(mGroupElement.getElement().get(index));
                reflectRelativeLayout.setGroup(mGroupElement.getGroup(), getNavIndex());
                reflectRelativeLayout.parseVOD(null, vod);
            }
        });
    }
    private void loadH5AndTopic(ReflectRelativeLayout reflectRelativeLayout, int index, Topic topic,boolean isNeedShowNotice,String sceneId,String identifyType,String appointedId) {
        Element element = mGroupElement.getElement().get(index);

        if (isNeedShowNotice){
            element.setNotice(topic.getNotice());
        }
        //设置海报
        element.getElementDataList().get(0).setContentURL(topic.getPosterUrl());
        //设置标题Title
        List<Dialect> nameDialect = new ArrayList<>();
        Dialect dialect = new Dialect();
        dialect.setLanguage("zh");
        dialect.setValue(topic.getName());
        nameDialect.add(dialect);
        element.getElementDataList().get(0).setNameDialect(nameDialect);
        String actionURL = element.getElementDataList().get(0).getElementAction().getActionURL();
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
        //set组织好的element
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != topic.getFeedback() && !TextUtils.isEmpty(topic.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(topic.getFeedback().getClick_tracker());
                }

                element.setRecommendType(identifyType);
                element.setRecommendId(topic.getId());
                element.setSceneId(sceneId);
                element.setAppointedId(appointedId);

                reflectRelativeLayout.setIsRecommend(true, sceneId, identifyType);
                reflectRelativeLayout.setAppointedId(appointedId);
                reflectRelativeLayout.setDefaultData(true);
                reflectRelativeLayout.setGroup(mGroupElement.getGroup(), getNavIndex());
                reflectRelativeLayout.setElementData(element);
                element.getElementDataList().get(0).getElementAction().setActionURL(actionURL);
            }
        });
    }

    private void loadApk(ReflectRelativeLayout reflectRelativeLayout, int realIndex, Apk apk,boolean isNeedShowNotice,String sceneId,String identifyType,String appointedId) {
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        Element element = mGroupElement.getElement().get(realIndex);

        if (isNeedShowNotice){
            element.setNotice(apk.getNotice());
        }
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
                if (null != apk.getFeedback() && !TextUtils.isEmpty(apk.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(apk.getFeedback().getClick_tracker());
                }

                element.setRecommendType(identifyType);
                element.setRecommendId(apk.getId());
                element.setSceneId(sceneId);
                element.setAppointedId(appointedId);

                reflectRelativeLayout.setDefaultData(true);
                reflectRelativeLayout.setIsRecommend(true,sceneId,identifyType);
                reflectRelativeLayout.setAppointedId(appointedId);
                reflectRelativeLayout.setGroup(mGroupElement.getGroup(), navIndex);
                reflectRelativeLayout.setElementData(element);
                element.getElementDataList().get(0).getElementAction().setActionURL(actionURL);
            }
        });
    }

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
        }else if (!sbRecommendId.toString().contains(id)){
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

    public void setExpendGroupParams(ExtraData extraData, String bgroud) {
        // 设置背景图
        if (!TextUtils.isEmpty(bgroud)) {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            this.measure(w, h);
            int width = context.getResources().getDisplayMetrics().widthPixels;
            int height = this.getMeasuredHeight();
            String launcherLink = SharedPreferenceUtil.getInstance().getLauncherLink();
            // String launcherLink = "/PHS/11055";
            if (!TextUtils.isEmpty(bgroud) && !TextUtils.isEmpty(launcherLink)) {
                bgroud = "http://" + AuthenticateManager.getInstance().getUserInfo().getIP() + ":" + AuthenticateManager.getInstance().getUserInfo().getPort() + launcherLink + bgroud;
                SuperLog.debug(TAG, bgroud);
                if (null == ivBackgroud) {
                    ivBackgroud = new ImageView(context);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
                    ivBackgroud.setLayoutParams(params);
                    ivBackgroud.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivBackgroud.setFocusable(false);
                    addView(ivBackgroud, 0);
                }
                if (fragment != null) {
                    RequestOptions options  = new RequestOptions()
                            .override(width, height);

                    Glide.with(fragment).load(bgroud).apply(options).into(ivBackgroud);
                }
            }
        } else {
            if (null != ivBackgroud) {
                ivBackgroud.setImageDrawable(null);
                removeView(ivBackgroud);
                ivBackgroud = null;
            }
        }

        // 设置跑马灯，只有标题显示的时候才显示跑马灯
        if (null != mTitleTv && mTitleTv.getVisibility() == VISIBLE && null != extraData) {
            MarqueeText mTextView = (MarqueeText) findViewById(R.id.tv_marquee_title);
            String marqueeStr = extraData.getMarquee();
            if (!TextUtils.isEmpty(marqueeStr)) {
                //跑马灯不为空
                if (null != mTextView) {
                    mTextView.setVisibility(View.VISIBLE);
                    mTextView.setText(marqueeStr);
                }
            } else {
                mTextView.setVisibility(View.GONE);
            }
        }
    }

}
