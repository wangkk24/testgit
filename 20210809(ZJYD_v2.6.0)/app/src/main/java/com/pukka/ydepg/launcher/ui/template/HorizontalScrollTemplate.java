package com.pukka.ydepg.launcher.ui.template;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.adapter.CommonAdapter;
import com.pukka.ydepg.common.adapter.base.ViewHolder;
import com.pukka.ydepg.common.http.v6bean.v6node.Dialect;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
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
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.customui.tv.widget.RecyclerViewTV;
import com.pukka.ydepg.customui.tv.widget.RvLinearLayoutManager;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.ui.fragment.PHMFragment;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 横向滑动模板
 * 四种类型处理
 */
public class HorizontalScrollTemplate extends PHMTemplate {
    private static final String TAG = "HorizontalScrollTemplate";
    private RecyclerViewTV recyclerViewTV;

    private List<VOD> vods = new ArrayList<>();

    private int itemLayuotId;

    private int navIndex = 0;

    private String appointId;

    private List<Recommend> recommends = new ArrayList<>();
    private Map<Integer,Recommend> recommendMap = new HashMap<>();

    private TabItemPresenter tabItemPresenter = new TabItemPresenter();
    private PBSRemixRecommendListener pbsRemixRecommendListener = new PBSRemixRecommendListener();

    private List<Element> mElementList;
    private Group mGroup;
    private int mLayoutId;

    private String display_tracker;
    private StringBuilder sbRecommendId = new StringBuilder();
    private StringBuilder sbRecommendItemId = new StringBuilder();
    private StringBuilder sbAppointedId = new StringBuilder();
    private StringBuilder sbScenId = new StringBuilder();
    private StringBuilder sbrecommendType = new StringBuilder();

    private static final String APPOINTEDID = "appointedId";

    public HorizontalScrollTemplate(Context context, int layoutId, PHMFragment fragment, OnFocusChangeListener focusChangeListener) {
        super(context, layoutId, fragment, focusChangeListener);
        this.mLayoutId = layoutId;
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
    @Override
    protected void initView(Context context, int layoutId) {
        super.initView(context, layoutId);
        recyclerViewTV = (RecyclerViewTV) findViewById(R.id.rv_horizontal_scro);
        recyclerViewTV.setLayoutManager(new RvLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false,recyclerViewTV));
        recyclerViewTV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getLayoutManager().getPosition(view);
                if (position > 0) {
                    outRect.left = (int) getResources().getDimension(R.dimen.pan_common_item_marginLeft);
                }
            }
        });
        setVisibility(false);
    }

    public void setDatas(List<Element> elementList, List<VOD> vodList, Group group,int navIndex) {
        this.mGroup = group;
        this.vods = vodList;
        this.navIndex = navIndex;

        if (null != elementList && elementList.size() > 10 ){
            mElementList = new ArrayList<>();
            for (int i = 0;i < 10;i++){
                mElementList.add(elementList.get(i));
            }
        }else{
            this.mElementList = elementList;
        }

        itemLayuotId = R.layout.item_horizontal_scroll;
        if (mLayoutId == R.layout.pannel_poster_horizontal_z_16){
            itemLayuotId = R.layout.item_horizontal_scroll;
        }else if (mLayoutId == R.layout.pannel_poster_horizontal_16){
            itemLayuotId = R.layout.item_horizontal_scroll_square;
        }else if (mLayoutId == R.layout.pannel_poster_horizontal_nor_16){
            itemLayuotId = R.layout.item_horizontal_scroll_one;
        }else if (mLayoutId == R.layout.pannel_poster_horizontal_sm_16){
            itemLayuotId = R.layout.item_horizontal_scroll_two;
        }

        appointId = getApponitId(elementList,group);
        if (!TextUtils.isEmpty(appointId)){
            if (null != tabItemPresenter){
                tabItemPresenter.queryPBSRemixRecommend(fragment.getContentIdList(),pbsRemixRecommendListener,appointId,"10");
            }
        }else{
            SuperLog.debug(TAG,"混合推荐appointID为空，加载默认数据");
            loadDefaultData();
            setVisibility(true);
        }
    }

    //无推荐数据，加载默认的数据
    private void loadDefaultData(){
        realIndex = 0;
        //item_horizontal_scroll:264*207 1.275
        CommonAdapter adapter = new CommonAdapter<Element>(context, itemLayuotId, mElementList) {
            @Override
            protected void convert(ViewHolder holder, Element element, int position) {
                //防止复用导致资源位内容错乱
                holder.setIsRecyclable(false);
                ReflectRelativeLayout relativeLayout = (ReflectRelativeLayout) holder.itemView;
                relativeLayout.setGroup(mGroup);
                relativeLayout.setRadius(0.0f);
                relativeLayout.setElement(element);
                relativeLayout.setIsRecommend(false,null,RecommendData.RecommendType.HAND_TYPE);
                relativeLayout.setOnFocusChangeListener(onFocusChangeListener);
                if (TextUtils.equals(element.getForceDefaultData(), "true")) {
                    relativeLayout.setDefaultData(true);
                    relativeLayout.setElementData(element);
                } else {
                    relativeLayout.setDefaultData(false);
                    if (null != vods && vods.size() > realIndex) {
                        relativeLayout.parseVOD(new TypeThreeLoader(), vods.get(realIndex));
                        realIndex++;
                    }else if (null != vods && vods.size() > position){
                        relativeLayout.parseVOD(new TypeThreeLoader(), vods.get(position));
                    }
                }
            }
        };
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerViewTV.setAdapter(adapter);
            }
        });
    }

    @Override
    public View getFirstView() {
        return recyclerViewTV;
    }

    public void scrollToTop() {
        recyclerViewTV.getLayoutManager().scrollToPosition(0);
    }

    private Map<String,List<Recommend>> mRecomMap = new HashMap<>();

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
                        //setRecomMap(recommendTem);
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
                        //setRecomMap(recommendTem);
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
        for (int i = 0; i < mElementList.size(); i++) {
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
                for (int i = 0;i < mElementList.size();i++){
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

                loadAdapterData();
                recordMainRecommend(sbRecommendId.toString());
                recordMainRecommendJiuTian(sbRecommendItemId.toString());
                /* else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                    //处理三方app/页面内部  跳转
                    loadApk(response);
                }*/
            } else {
                //loadDefaultData();
            }
        }

        @Override
        public void getRemixRecommendDataFail() {
            //loadDefaultData();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        recordMainRecommend(sbRecommendId.toString());
        recordMainRecommendJiuTian(sbRecommendItemId.toString());
    }

    //获取单个资源位支持智能推荐的数据
    private Recommend getElementRecom(Element element){
        if (null != element.getExtraData() && !TextUtils.isEmpty(element.getExtraData().get(APPOINTEDID)) && mRecomMap.containsKey(element.getExtraData().get(APPOINTEDID))){
            List<Recommend> recommends = mRecomMap.get(element.getExtraData().get(APPOINTEDID));
            if (null != recommends && recommends.size() > 0){
                return recommends.get(0);
            }
        }
        return null;
    }

    private int recomGroupIndex=-1;
    private Recommend getGroupRecom(){
        if (null != mGroup.getExtraData() && !TextUtils.isEmpty(mGroup.getExtraData().getAppointedId()) && mRecomMap.containsKey(mGroup.getExtraData().getAppointedId())){
            List<Recommend> recommends = mRecomMap.get(mGroup.getExtraData().getAppointedId());
            if (null != recommends && recommends.size() > 0){
                recomGroupIndex++;
                if (recomGroupIndex < recommends.size()){
                    return recommends.get(recomGroupIndex);
                }
            }
        }
        return null;
    }

    //加载混合推荐的数据
    private void loadAdapterData() {
        CommonAdapter adapter = new CommonAdapter<Element>(context, itemLayuotId, mElementList) {
            @Override
            protected void convert(ViewHolder holder, Element element, int position) {
                //防止复用导致资源位内容错乱
                holder.setIsRecyclable(false);
                ReflectRelativeLayout relativeLayout = (ReflectRelativeLayout) holder.itemView;
                //每个recommend可能有多个VOd topic   把recommends的数据整理到一个集合之中，还是以recommend为单位
                Recommend recommend = null;
                if (null != recommendMap && recommendMap.containsKey(position)){
                    recommend = recommendMap.get(position);
                }
                //mRecomMap
                //先判断element是否是单独推荐的数据
                /*recommend = getElementRecom(element);
                //如果单个element不是单独推荐，再取group推荐
                if (null == recommend) {
                    recommend = getGroupRecom();
                }*/
                if (null != recommend && "0".equalsIgnoreCase(recommend.getSceneType()) && null != recommend.getVODs() && recommend.getVODs().size() > 0) {
                    loadVod(relativeLayout, position, recommend.getVODs().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
                } else if (null != recommend && ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType()))
                        && null != recommend.getTopics() && recommend.getTopics().size() > 0) {
                    loadH5AndTopic(relativeLayout, position, recommend.getTopics().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
                }else if (null != recommend && "3".equalsIgnoreCase(recommend.getSceneType())
                        && null != recommend.getApks() && recommend.getApks().size() > 0) {
                    loadApk(relativeLayout, position, recommend.getApks().get(0),recommend.getSceneId(),getIdentifyType(recommend.getIdentifyType()),recommend.getAppointedId());
                }else{
                    //加载默认数据
                    relativeLayout.setIsRecommend(false,null,RecommendData.RecommendType.HAND_TYPE);
                    relativeLayout.setGroup(mGroup);
                    relativeLayout.setElement(element);
                    relativeLayout.setRadius(0.0f);
                    relativeLayout.setOnFocusChangeListener(onFocusChangeListener);
                    if (TextUtils.equals(element.getForceDefaultData(), "true")) {
                        relativeLayout.setDefaultData(true);
                        relativeLayout.setElementData(element);
                    } else {
                        relativeLayout.setDefaultData(false);
                        if (null != vods && vods.size() > position) {
                            relativeLayout.parseVOD(new TypeThreeLoader(), vods.get(position));
                        } else if (null != vods && vods.size() > position) {
                            relativeLayout.parseVOD(new TypeThreeLoader(), vods.get(position));
                        }
                    }
                }
            }
        };
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerViewTV.setAdapter(adapter);
                setVisibility(true);
            }
        });
    }

    private int realIndex = 0;

    private void loadVod(ReflectRelativeLayout reflectRelativeLayout,int index,VOD vod,String sceneId,String identifyType,String appointedId) {
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != vod.getFeedback() && !TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(vod.getFeedback().getClick_tracker());
                }

                mElementList.get(index).setRecommendType(identifyType);
                mElementList.get(index).setRecommendId(vod.getID());
                mElementList.get(index).setSceneId(sceneId);
                mElementList.get(index).setAppointedId(appointedId);

                reflectRelativeLayout.setIsRecommend(true,sceneId,identifyType);
                reflectRelativeLayout.setAppointedId(appointedId);
                reflectRelativeLayout.setDefaultData(false);
                reflectRelativeLayout.setElement(mElementList.get(index));
                reflectRelativeLayout.setGroup(mGroup, getNavIndex());
                reflectRelativeLayout.parseVOD(null, vod);
            }
        });
    }
    private void loadApk(ReflectRelativeLayout reflectRelativeLayout, int realIndex, Apk apk,String sceneId,String identifyType,String appointedId) {
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        Element element = mElementList.get(realIndex);
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
                reflectRelativeLayout.setGroup(mGroup, navIndex);
                reflectRelativeLayout.setElementData(element);
                element.getElementDataList().get(0).getElementAction().setActionURL(actionURL);
            }
        });
    }
    private void loadH5AndTopic(ReflectRelativeLayout reflectRelativeLayout, int index, Topic topic,String sceneId,String identifyType,String appointedId) {
        reflectRelativeLayout.setDefaultData(true);
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        Element element = mElementList.get(index);
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

                reflectRelativeLayout.setIsRecommend(true,"",identifyType);
                reflectRelativeLayout.setAppointedId(appointedId);
                reflectRelativeLayout.setGroup(mGroup, getNavIndex());
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
}