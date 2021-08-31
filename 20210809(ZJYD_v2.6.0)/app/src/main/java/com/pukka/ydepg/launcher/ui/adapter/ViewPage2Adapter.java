package com.pukka.ydepg.launcher.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.ShimmerImageView;
import com.pukka.ydepg.common.http.v6bean.v6node.Dialect;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.ubd.extension.RecommendData;
import com.pukka.ydepg.common.toptool.EpgTopFunctionMenu;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.ui.activity.TopicActivity;
import com.pukka.ydepg.launcher.view.ReflectRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPage2Adapter extends RecyclerView.Adapter<ViewPage2Adapter.ViewHolder> {

    private final static String TAG = ViewPage2Adapter.class.getSimpleName();

    private Context mContext;
    private Group mGroup;
    private Element mElement;
    private List<Recommend> mRecommends;
    private ReflectRelativeLayout mRootView;

    //private Map<Integer,ReflectRelativeLayout> mReflecMap = new HashMap<>();

    public ViewPage2Adapter(List<Recommend> recommends, Group group,Element element, Context context, ReflectRelativeLayout rootView) {
        this.mContext = context;
        this.mRecommends = recommends;
        this.mGroup = group;
        this.mElement = element;
        this.mRootView = rootView;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewpage2_adapter_item, parent,false);
        //View view = LayoutInflater.from(mContext).inflate(R.layout.test_layout, parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //不复用、复用导致角标展示异常
        holder.setIsRecyclable(false);
        if (null != mRecommends && mRecommends.size() > 0){
            loadData(mRecommends.get(position%mRecommends.size()),(ReflectRelativeLayout) holder.itemView,position%mRecommends.size());
        }
        //loadData(mRecommends.get(position),(ReflectRelativeLayout) holder.itemView,position);

    }

    /*private void setReflecMap(int position,ReflectRelativeLayout reflectRelativeLayout){
        if (null == mReflecMap.get(position)){
            mReflecMap.put(position,reflectRelativeLayout);
        }
    }

    public ReflectRelativeLayout getItemView(int position){
        return mReflecMap.get(position);
    }*/

    @Override
    public int getItemCount() {
        return (null == mRecommends || mRecommends.size() < 1) ? 1 : Integer.MAX_VALUE;
        //return mRecommends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void loadData(Recommend recommend,ReflectRelativeLayout reflectRelativeLayout,int position){
        if ("0".equalsIgnoreCase(recommend.getSceneType())) {
            //内容
            if (null != recommend.getVODs() && recommend.getVODs().size() > 0) {
                loadVod(reflectRelativeLayout, recommend.getVODs().get(0),recommend.getSceneId(),mRecommends.indexOf(recommend),recommend.getIdentifyType(),recommend.getAppointedId());
            }
        } else if ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType())) {
            //处理H5/专题 页面
            if (null != recommend.getTopics() && recommend.getTopics().size() > 0) {
                loadH5AndTopic(reflectRelativeLayout, recommend.getTopics().get(0),recommend.getSceneId(),mRecommends.indexOf(recommend),recommend.getIdentifyType(),recommend.getAppointedId());
            }
        }else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
            //处理apk,跳转内部界面和三方app
            if (null != recommend.getApks() && recommend.getApks().size() > 0) {
                loadApk(reflectRelativeLayout, recommend.getApks().get(0),recommend.getSceneId(),mRecommends.indexOf(recommend),recommend.getIdentifyType(),recommend.getAppointedId());
            }
        }else if ("4".equalsIgnoreCase(recommend.getSceneType())) {
            //处理和彩云数据
            loadCloudTv(reflectRelativeLayout, recommend,mRecommends.indexOf(recommend));
        }
    }
    private void loadVod(ReflectRelativeLayout reflectRelativeLayout, VOD vod,String sceneId,int position,Integer identifyType,String appointedId) {
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != vod.getFeedback() && !TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(vod.getFeedback().getClick_tracker());
                }

                mElement.setRecommendType(String.valueOf(identifyType));
                mElement.setRecommendId(vod.getID());
                mElement.setSceneId(sceneId);
                mElement.setAppointedId(appointedId);

                reflectRelativeLayout.setDefaultData(false);
                reflectRelativeLayout.setElement(mElement);
                reflectRelativeLayout.setIsRecommend(true, sceneId, null == identifyType?null:String.valueOf(identifyType));
                reflectRelativeLayout.setGroup(mGroup);
                reflectRelativeLayout.parseVOD(null, vod);
            }
        });
    }

    private void loadApk(ReflectRelativeLayout reflectRelativeLayout, Apk apk,String sceneId,int position,Integer identifyType,String appointedId) {
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        //设置海报
        mElement.getElementDataList().get(0).setContentURL(apk.getPosterURL());
        //设置标题Title
        List<Dialect> nameDialect = new ArrayList<>();
        Dialect dialect = new Dialect();
        dialect.setLanguage("zh");
        dialect.setValue(apk.getName());
        nameDialect.add(dialect);
        mElement.getElementDataList().get(0).setNameDialect(nameDialect);
        String actionURL = mElement.getElementDataList().get(0).getElementAction().getActionURL();
        //H5专题和活动推荐 url设置给actionUrl即可
        if (apk.getPkg().equalsIgnoreCase(EpgTopFunctionMenu.EPG_PKG)){
            //内部页面跳转
            StringBuffer sb = new StringBuffer();
            //sb.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE+"=");
            SuperLog.debug(TAG,"actionUrl_apk.getCls()="+apk.getCls());
            String actionUrl = getActionUrlForEpgInner(apk.getCls());
            SuperLog.debug(TAG,"actionUrl="+actionUrl);
            if (actionUrl.contains("ContentType=PAGE")){
                mElement.getElementDataList().get(0).getElementAction().setActionType(ZJVRoute.LauncherElementActionType.TYPE_2);
            }
            sb.append(actionUrl);
            mElement.getElementDataList().get(0).getElementAction().setActionURL(sb.toString());
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
            mElement.getElementDataList().get(0).getElementAction().setActionURL(sb.toString());
            List<com.pukka.ydepg.launcher.bean.node.NamedParameter> extras = apk.getExtras();
            if (null != extras && extras.size() > 0){
                Map extraData = mElement.getExtraData();
                if (null == extraData){
                    extraData = new HashMap();
                }
                for (com.pukka.ydepg.launcher.bean.node.NamedParameter namedParameter : extras){
                    if (!extraData.containsKey(namedParameter.getKey())){
                        extraData.put(namedParameter.getKey(),namedParameter.getFistItemFromValue());
                    }
                }
                mElement.setExtraData(extraData);
            }
        }

        //set组织好的element
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != apk.getFeedback() && !TextUtils.isEmpty(apk.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(apk.getFeedback().getClick_tracker());
                }

                mElement.setRecommendType(String.valueOf(identifyType));
                mElement.setRecommendId(apk.getId());
                mElement.setSceneId(sceneId);
                mElement.setAppointedId(appointedId);

                reflectRelativeLayout.setDefaultData(true);
                reflectRelativeLayout.setIsRecommend(true,sceneId,null == identifyType?null:String.valueOf(identifyType));
                reflectRelativeLayout.setGroup(mGroup);
                reflectRelativeLayout.setElementData(mElement);
                mElement.getElementDataList().get(0).getElementAction().setActionURL(actionURL);
            }
        });
    }

    private void loadH5AndTopic(ReflectRelativeLayout reflectRelativeLayout, Topic topic,String sceneId,int position,Integer identifyType,String appointedId) {
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        //设置海报
        mElement.getElementDataList().get(0).setContentURL(topic.getPosterUrl());
        //设置标题Title
        List<Dialect> nameDialect = new ArrayList<>();
        Dialect dialect = new Dialect();
        dialect.setLanguage("zh");
        dialect.setValue(topic.getName());
        nameDialect.add(dialect);
        mElement.getElementDataList().get(0).setNameDialect(nameDialect);
        String actionURL = mElement.getElementDataList().get(0).getElementAction().getActionURL();
        //H5专题和活动推荐 url设置给actionUrl即可
        if (topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_H5) || topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_ACTIVITY)){
            mElement.getElementDataList().get(0).getElementAction().setActionURL(topic.getTopicURL());
        }else if (topic.getType().equalsIgnoreCase(TopicActivity.TOPIC_NATIVE)){//普通专题
            //native原生专题，用推荐接口请求回来的数据拼接actionUrl,set到Reflect中，后面走统一逻辑
            StringBuffer buffer = new StringBuffer();
            buffer.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE);
            buffer.append("=");
            buffer.append(ZJVRoute.LauncherElementContentType.VOD_CATEGORY);
            buffer.append("&");
            buffer.append(ZJVRoute.ActionUrlKeyType.CONTENT_ID);
            buffer.append("=");
            buffer.append(topic.getRelationSubjectId());//本地专题列表存储是以RelationSubjectId为key
            mElement.getElementDataList().get(0).getElementAction().setActionURL(buffer.toString());
        }

        //set组织好的element
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != topic.getFeedback() && !TextUtils.isEmpty(topic.getFeedback().getClick_tracker())){
                    reflectRelativeLayout.setClickTrackerUrl(topic.getFeedback().getClick_tracker());
                }

                mElement.setRecommendType(String.valueOf(identifyType));
                mElement.setRecommendId(topic.getId());
                mElement.setSceneId(sceneId);
                mElement.setAppointedId(appointedId);

                reflectRelativeLayout.setDefaultData(true);
                reflectRelativeLayout.setIsRecommend(true,sceneId,null == identifyType?null:String.valueOf(identifyType));
                reflectRelativeLayout.setGroup(mGroup);
                reflectRelativeLayout.setElementData(mElement);
                //setReflecMap(position,reflectRelativeLayout);
                mElement.getElementDataList().get(0).getElementAction().setActionURL(actionURL);
            }
        });
    }

    private void loadCloudTv(ReflectRelativeLayout reflectRelativeLayout, Recommend recommend,int position) {
        //使用智能推荐数据，将获取的数据转化成element，后面走同意流程
        //设置海报
        mElement.getElementDataList().get(0).setContentURL(recommend.getPosterURLCloudTv());

        /*mElement.getElementDataList().get(0).getElementAction().setActionURL("AppPkg=com.chinamobile.mcloudtv.helper&ClassName=com.chinamobile.mcloudtv.helper.login.AutoLoginService&ContentType=APK");
        Map<String,String> map = new HashMap<>();
        map.put("appName","和彩云");
        map.put("forceUpdate","1");
        map.put("appUrl","http://zj-mobile-ystzw-gamedownload.wasu.tv:8085/app/705/4.3.1/1617269359231FamilyAlbumTV_v4.3.1.20210322_ZheJiang_silence.apk");
        mElement.setExtraData(map);*/
        //set组织好的element
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                /*mRootView.setDefaultData(true);
                mRootView.setIsCloudTvData(true);
                mRootView.setIsRecommend(true,null,null);
                mRootView.setGroup(mGroup);
                mRootView.setElementData(mElement);*/

                reflectRelativeLayout.setDefaultData(true);
                reflectRelativeLayout.setIsCloudTvData(true);
                reflectRelativeLayout.setIsRecommend(true,null,null);
                reflectRelativeLayout.setGroup(mGroup);
                reflectRelativeLayout.setElementData(mElement);
            }
        });
    }

    private String getActionUrlForEpgInner(String cls) {
        if (cls.contains("ContentType=PAGE")){
            //ContentID=103051&ContentType=PAGE&Action=View
            String contentId = ZJVRoute.getContentValue(cls,ZJVRoute.ActionUrlKeyType.CONTENT_ID);
            if (!TextUtils.isEmpty(contentId)){
                StringBuilder sb = new StringBuilder();
                sb.append(SharedPreferenceUtil.getInstance().getLauncherDeskTopIdForChild())
                        .append("_")
                        .append(contentId)
                        .append("@")
                        .append(SharedPreferenceUtil.getInstance().getLauncherVersionForChild());
                return cls.replaceAll(contentId,sb.toString());
            }else {
                return cls;
            }
        }else{
            return cls;
        }
    }

}
