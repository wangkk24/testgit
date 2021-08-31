package com.pukka.ydepg.common.toptool;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.LinearLayoutExt;
import com.pukka.ydepg.common.extview.MarqueeEpgTextView;
import com.pukka.ydepg.common.extview.RelativeLayoutExt;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.http.v6bean.v6response.PBSRemixRecommendResponse;
import com.pukka.ydepg.common.http.v6bean.v6response.Recommend;
import com.pukka.ydepg.common.report.ubd.extension.TopFunctionData;
import com.pukka.ydepg.common.report.ubd.scene.UBDTopFunction;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.Topic;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eason on 10-Jun-20.
 * EPG top
 */
public class EpgTopFunctionMenu {

    private static volatile EpgTopFunctionMenu instance;

    public static final String TOP_FOCUS_COLOR = "topFocusColor";
    public static final String NOTICE_FOCUS_COLOR = "noticeFocusColor";

    //刷新按钮布局
    private RelativeLayoutExt freshRl, freshRlChild;
    //
    private QueryEPGFunctionListener queryEPGFunctionListener = new QueryEPGFunctionListener();
    private PBSRemixRecommendListener queryRemixRecommendListener = new PBSRemixRecommendListener();

    private TabItemPresenter tabItemPresenter = new TabItemPresenter();

    private static String NAME = "displayName";
    private static String ACTIONURL = "actionUrl";
    private static String APKEXTRA = "apkExtra";
    private static String FOCUS_COLOR = "focusColor";

    private static String ACTIVITY = "activity";
    private static String NATIVE = "native";
    private static String H5 = "h5";

    private static final String TAG = "EpgTopFunctionMenu";
    private static final String EPG_FUNCTION_MENU = "epg_function_menu";
    public static final String EPG_PKG = "com.pukka.ydepg";

    private static final String EPG_SCROLL_ADS = "epg_scroll_ads";

    /**
     * 功能键集合
     * key = s , value = actionUrl
     * key = displayName , value = 功能键title
     */
    private List<Map<String, String>> mapList = new ArrayList<>();

    private String mScrollString = null;

    //跳转三方app或者内部页面所需扩展参数
    private Map<String, String> extraData = new HashMap<>();

    private String mDefaultColor = "#ffffff";
    private String mFocusColor = "#ffffff";
    ;

    //item group
    /**
     * 真导航栏：mlinearContent mTvEpgScrollAds
     */
    private LinearLayoutExt mlinearContent, mChildLinearContent = null;
    //private MarqueeTextView mTvEpgScrollAds = null;
    private MarqueeEpgTextView mTvEpgScrollAds = null;
    private Context mContext = null;

    private List<TextViewExt> functionMenuViews = new ArrayList<>();

    public void setMainView(LinearLayoutExt linearContent, MarqueeEpgTextView tvEpgScrollAds) {
        this.mlinearContent = linearContent;
        this.mTvEpgScrollAds = tvEpgScrollAds;
    }

    public void resetMapList() {
        mapList = new ArrayList<>();
        mScrollString = null;
    }

    //添加功能键 Item
    public void addFunctionMenuItem(Context context) {
        SuperLog.info2SD(TAG, "addFunctionMenuItem");
        this.mContext = context;
        if (null == mapList || mapList.size() == 0) {
            SuperLog.info2SD(TAG, "loadData(context)");
            loadData();
        } else {
            SuperLog.info2SD(TAG, "no need loadData(context) and mScrollString=" + mScrollString + ";mTvEpgScrollAds.getVisibility() == View.VISIBLE is " + (mTvEpgScrollAds.getVisibility() == View.VISIBLE));
            functionMenuViews = new ArrayList<>();
            if (null != mlinearContent) {
                mlinearContent.removeAllViews();
            }
            if (null != mChildLinearContent) {
                mChildLinearContent.removeAllViews();
            }

            List<Map<String, String>> mapListTem = new ArrayList<>();
            for (int i = 0; i < mapList.size(); i++){
                if (!TextUtils.isEmpty(mapList.get(i).get(NAME))) {
                    mapListTem.add(mapList.get(i));
                }
            }
            for (int i = 0; i < mapListTem.size(); i++) {
                Map<String, String> map = mapListTem.get(i);
                if (TextUtils.isEmpty(map.get(NAME))) {
                    continue;
                }
                if (!TextUtils.isEmpty(map.get(FOCUS_COLOR))){
                    mFocusColor = map.get(FOCUS_COLOR);
                }else if (!TextUtils.isEmpty(LauncherService.getInstance().getLauncher().getExtraData().get(TOP_FOCUS_COLOR))){
                    mFocusColor = LauncherService.getInstance().getLauncher().getExtraData().get(TOP_FOCUS_COLOR);
                }else{
                    mFocusColor = "#ffffff";
                }
                LinearLayoutExt inflate = (LinearLayoutExt) LayoutInflater.from(mContext).inflate(R.layout.function_item, null);
                LinearLayoutExt inflateChild = (LinearLayoutExt) LayoutInflater.from(mContext).inflate(R.layout.function_item, null);
                //刷新键
                if (map.get(NAME).equalsIgnoreCase(mContext.getResources().getString(R.string.account_time_out_refresh))) {
                    freshRl = inflate.findViewById(R.id.rl_main_refresh);
                    TextViewExt tv_main_refresh_btn = inflate.findViewById(R.id.tv_main_refresh_btn);
                    freshRlChild = inflateChild.findViewById(R.id.rl_main_refresh);
                    tv_main_refresh_btn.setTag(R.id.tag_epg_color,mFocusColor);
                    Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(mFocusColor,null),tv_main_refresh_btn,null);
                    freshRl.setVisibility(View.VISIBLE);
                    freshRlChild.setVisibility(View.VISIBLE);
                    freshRlChild.setFocusable(false);
                    if (i == 0 && TextUtils.isEmpty(mScrollString)) {
                        freshRl.findViewById(R.id.tv_main_refresh_btn).setNextFocusLeftId(R.id.tv_main_refresh_btn);
                    }
                    if (i == mapListTem.size()-1 &&  ((MainActivity) mContext).getProfile().getVisibility() == View.GONE) {
                        freshRl.findViewById(R.id.tv_main_refresh_btn).setNextFocusRightId(R.id.tv_main_refresh_btn);
                    }

                    Utils.setTopMenuBg(context,tv_main_refresh_btn,Utils.getShapeForColor(context,mFocusColor,8));

                    tv_main_refresh_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            tv_main_refresh_btn.setSelected(hasFocus);
                            if (hasFocus){
                                //tv_main_refresh_btn.setTextColor(ContextCompat.getColor(context, R.color.white_0));
                                Utils.setTextColorUseFocusColor(String.valueOf(tv_main_refresh_btn.getTag(R.id.tag_epg_color)),tv_main_refresh_btn,null);
                            }else{
                                //tv_main_refresh_btn.setTextColor(ContextCompat.getColor(context, R.color.color_364653));
                                Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(String.valueOf(tv_main_refresh_btn.getTag(R.id.tag_epg_color)),mDefaultColor),tv_main_refresh_btn,null);
                            }
                        }
                    });

                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).initFreshView(freshRl);
                    }
                } else {//普通功能键
                    TextViewExt tv = inflate.findViewById(R.id.tv_epg_function_item);
                    TextViewExt tvChild = inflateChild.findViewById(R.id.tv_epg_function_item);
                    tvChild.setText(map.get(NAME));
                    tvChild.setVisibility(View.VISIBLE);
                    tvChild.setFocusable(false);
                    Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(mFocusColor,mDefaultColor),tv,null);
                    tv.setText(map.get(NAME));
                    tv.setTag(R.id.tag_epg_first, map.get(ACTIONURL));
                    tv.setTag(R.id.tag_epg_second, map.get(APKEXTRA));
                    tv.setTag(R.id.tag_epg_color, mFocusColor);
                    tv.setOnClickListener(onClickListener);
                    tv.setVisibility(View.VISIBLE);
                    if (i == 0 && TextUtils.isEmpty(mScrollString)) {
                        tv.setNextFocusLeftId(R.id.tv_epg_function_item);
                    }
                    if (i == mapListTem.size()-1 && ((MainActivity) mContext).getProfile().getVisibility() == View.GONE) {
                        tv.setNextFocusRightId(R.id.tv_epg_function_item);
                    }

                    Utils.setTopMenuBg(context,tv,Utils.getShapeForColor(context,mFocusColor,8));

                    tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            tv.setSelected(hasFocus);
                            if (hasFocus){
                                //tv.setTextColor(ContextCompat.getColor(context, R.color.white_0));
                                Utils.setTextColorUseFocusColor(String.valueOf(tv.getTag(R.id.tag_epg_color)),tv,null);
                            }else{
                                //tv.setTextColor(ContextCompat.getColor(context, R.color.color_364653));
                                Utils.setTextColorUseFocusColor(Utils.getNoFocusColorString(String.valueOf(tv.getTag(R.id.tag_epg_color)),mDefaultColor),tv,null);
                            }
                        }
                    });


                    functionMenuViews.add(tv);
                }
                if (null != mlinearContent) {
                    mlinearContent.addView(inflate);
                }
                if (null != mChildLinearContent) {
                    mChildLinearContent.addView(inflateChild);
                }
            }
            if (mContext instanceof MainActivity) {
                ((MainActivity) mContext).addFunctionMenuViews(functionMenuViews);
            }

            setTvEpgScrollAdsText();
            setSwitchProfileBtnNextLeftFocus();
        }
    }

    //
    private void setSwitchProfileBtnNextLeftFocus(){
        if (mContext instanceof MainActivity && mlinearContent.getChildCount() == 0 && TextUtils.isEmpty(mScrollString)) {
            ((MainActivity) mContext).mProfileAliasTextBg.setNextFocusLeftId(R.id.tv_user_title_bg);
        }
    }

    public void setTvEpgScrollAdsText() {
        if (!TextUtils.isEmpty(mScrollString) && null != mTvEpgScrollAds) {
            isHideTvEpgScrollAd(false);
            mTvEpgScrollAds.post(new Runnable() {
                @Override
                public void run() {
                    SuperLog.debug(TAG,"setTvEpgScrollAdsText();mScrollString="+mScrollString);
                    mTvEpgScrollAds.setText(mScrollString, mTvEpgScrollAds.getMeasuredWidth(), mTvEpgScrollAds.getMeasuredHeight());
                }
            });
        }
    }

    public static EpgTopFunctionMenu getInstance() {
        if (null == instance) {
            synchronized (EpgTopFunctionMenu.class) {
                if (null == instance) {
                    instance = new EpgTopFunctionMenu();
                }
            }
        }
        return instance;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_epg_function_item) {
                //功能键点击事件,不包括刷新的点击事件
                String actionUrl = "";
                if (null != view.getTag(R.id.tag_epg_first)) {
                    actionUrl = String.valueOf(view.getTag(R.id.tag_epg_first));
                }
                Map<String, String> extraDataMap = null;
                if (null != view.getTag(R.id.tag_epg_second)) {
                    String extraString = String.valueOf(view.getTag(R.id.tag_epg_second));
                    extraDataMap = getApkExtraData(extraString);
                }
                SuperLog.debug(TAG, "name=" + ((TextViewExt) view).getText() + ";actionurl=" + actionUrl);
                ZJVRoute.route(mContext, ZJVRoute.LauncherElementDataType.STATIC_ITEM, actionUrl, ZJVRoute.LauncherElementActionType.TYPE_0, null, null, extraDataMap);
                //点击事件上报UBD
                UBDTopFunction.reportTopFunction(((TextView) view).getText().toString(), TopFunctionData.BTN_NORMAL);
            } else if (view.getId() == R.id.tv_epg_scroll_ads) {
                //跑马灯点击事件
                if (null != mTvEpgScrollAds) {
                    if (null != mTvEpgScrollAds.getTag()) {
                        SuperLog.debug(TAG,"ActionUrl="+String.valueOf(mTvEpgScrollAds.getTag()));
                        if (String.valueOf(mTvEpgScrollAds.getTag()).contains("ContentType=PAGE")){
                            ZJVRoute.route(mContext, ZJVRoute.LauncherElementDataType.STATIC_ITEM, String.valueOf(mTvEpgScrollAds.getTag()), ZJVRoute.LauncherElementActionType.TYPE_2, null, null, extraData);
                        }else{
                            ZJVRoute.route(mContext, ZJVRoute.LauncherElementDataType.STATIC_ITEM, String.valueOf(mTvEpgScrollAds.getTag()), ZJVRoute.LauncherElementActionType.TYPE_0, null, null, extraData);
                        }
                        //点击事件上报UBD
                        UBDTopFunction.reportTopFunction(null, TopFunctionData.BTN_LAMP);
                    }
                }
            }
        }
    };

    public void loadData() {
        if (null != tabItemPresenter) {
            if (null != LauncherService.getInstance().getLauncher().getExtraData() && !TextUtils.isEmpty(LauncherService.getInstance().getLauncher().getExtraData().get(EPG_SCROLL_ADS))) {
                tabItemPresenter.queryPBSRemixRecommend(null, queryRemixRecommendListener, LauncherService.getInstance().getLauncher().getExtraData().get(EPG_SCROLL_ADS),"6");
            } else {
                queryEpgTopMenu();
            }
        }
    }

    public void queryEpgTopMenu() {
        String functionSubjectId = "";
        if (null != LauncherService.getInstance().getLauncher().getExtraData() && !TextUtils.isEmpty(LauncherService.getInstance().getLauncher().getExtraData().get(EPG_FUNCTION_MENU))) {
            functionSubjectId = LauncherService.getInstance().getLauncher().getExtraData().get(EPG_FUNCTION_MENU);
        }
        tabItemPresenter.queryEpgTopFunctionMenuList(mContext, functionSubjectId, queryEPGFunctionListener);
    }

    //查询功能键
    private class QueryEPGFunctionListener implements OnQueryEPGFunctionListener {

        @Override
        public void getEPGFunctionData(List<SubjectVODList> subjectVODLists) {
            if (null != subjectVODLists) {
                mapList = new ArrayList<>();
                for (SubjectVODList subjects : subjectVODLists) {
                    if (null != subjects.getSubject().getCustomFields() && subjects.getSubject().getCustomFields().size() > 0) {
                        HashMap<String, String> mapConfigValue = new HashMap<>();
                        for (NamedParameter namedParameter : subjects.getSubject().getCustomFields()) {
                            if (namedParameter.getKey().equalsIgnoreCase(ACTIONURL) || namedParameter.getKey().equalsIgnoreCase(NAME) || namedParameter.getKey().equalsIgnoreCase(APKEXTRA) || namedParameter.getKey().equalsIgnoreCase(FOCUS_COLOR)) {
                                mapConfigValue.put(namedParameter.getKey(), namedParameter.getFistItemFromValue());
                            }
                        }
                        mapList.add(mapConfigValue);
                    }
                }
                if (null != mapList && mapList.size() > 0) {
                    addFunctionMenuItem(mContext);
                } else {
                    setTvEpgScrollAdsText();
                    setSwitchProfileBtnNextLeftFocus();
                }
            }
        }

        @Override
        public void getEPGFunctionDataFail() {
            setTvEpgScrollAdsText();
            setSwitchProfileBtnNextLeftFocus();
        }
    }

    //组织功能键跳转第三方APK的扩展参数
    private Map<String, String> getApkExtraData(String extraString) {
        Map<String, String> apkDxtraData = new HashMap<>();
        ;
        if (!TextUtils.isEmpty(extraString)) {
            for (String keyValue : extraString.split("&")) {
                String[] keyValues = keyValue.split("=");
                if (keyValues.length == 2) {
                    apkDxtraData.put(keyValues[0], keyValues[1]);
                }
            }
        }
        return apkDxtraData;
    }

    //查询跑马灯
    private class PBSRemixRecommendListener implements OnPBSRemixRecommendListener {

        @Override
        public void getRemixRecommendData(PBSRemixRecommendResponse response) {
            //查询成功 返回值notice及跳转连接
            if (null != mTvEpgScrollAds) {
                mTvEpgScrollAds.setOnClickListener(onClickListener);
                if (null != response && null != response.getRecommends() && response.getRecommends().size() > 0) {
                    /**
                     * 推荐类型（sceneType）：0 内容，1 专题，2 活动，3 apk（含自有和第三方） 根据FRS 暂不支持0 内容，仅支持apk 专题  h5专题
                     *  apks   sceneType  = 3
                     *  topics  sceneType  = 1 或者 2
                     * */
                    Recommend recommend = response.getRecommends().get(0);
                    if (TextUtils.isEmpty(recommend.getSceneType())) {
                        SuperLog.info2SD(TAG, "recommend.getSceneType()=null，跑马灯内容SceneType为空，不展示");
                        return;
                    }
                    if ("1".equalsIgnoreCase(recommend.getSceneType()) || "2".equalsIgnoreCase(recommend.getSceneType())) {
                        //处理H5页面
                        loadTopic(response);
                    } else if ("3".equalsIgnoreCase(recommend.getSceneType())) {
                        //处理APK页面
                        loadApk(response);
                    }
                } else {
                    SuperLog.info2SD(TAG, "recommend = null，跑马灯无推荐内容，不展示");
                    //isHideTvEpgScrollAd(true);
                }
            }
            queryEpgTopMenu();
        }

        @Override
        public void getRemixRecommendDataFail() {
            //isHideTvEpgScrollAd(true);
            queryEpgTopMenu();
        }
    }

    private void isHideTvEpgScrollAd(boolean isHide) {
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isHide) {
                    mTvEpgScrollAds.setVisibility(View.GONE);
                } else {
                    mTvEpgScrollAds.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadApk(PBSRemixRecommendResponse response) {
        if (null != response.getRecommends().get(0).getApks() && response.getRecommends().get(0).getApks().size() > 0) {
            //处理APK（内部页面跳转及三方app跳转）
            Apk apk = response.getRecommends().get(0).getApks().get(0);
            if (TextUtils.isEmpty(apk.getNotice())) {
                SuperLog.debug(TAG, "Notice=null,跑马灯内容为空，不展示");
                return;
            }
            String tag;
            if (apk.getPkg().equalsIgnoreCase(EPG_PKG)) {
                //内部页面跳转
                StringBuffer sb = new StringBuffer();
                /*sb.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE+"=");*/
                SuperLog.debug(TAG,"actionUrl_apk.getCls()="+apk.getCls());
                String actionUrl = getActionUrlForEpgInner(apk.getCls());
                SuperLog.debug(TAG,"actionUrl="+actionUrl);
                /*if (actionUrl.contains("ContentType=PAGE")){
                    mActionType = ZJVRoute.LauncherElementActionType.TYPE_2;
                }*/
                sb.append(actionUrl);
                tag = sb.toString();
            } else {
                //三方App跳转 AppPkg=com.syntc.mushroomjump&ContentType=APK&Version=6
                StringBuffer sb = new StringBuffer();
                sb.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE + "=");
                sb.append(ZJVRoute.LauncherElementContentType.APK);
                sb.append("&" + ZJVRoute.ActionUrlKeyType.APP_PKG + "=");
                sb.append(apk.getPkg());
                sb.append("&" + ZJVRoute.ActionUrlKeyType.APP_CLASS + "=");
                sb.append(apk.getCls());
                //set actionurl
                tag = sb.toString();
            }
            setScrollTvData(apk.getNotice(), tag);
            //组织扩展参数
            List<com.pukka.ydepg.launcher.bean.node.NamedParameter> extras = apk.getExtras();
            if (null != extras && extras.size() > 0) {
                extraData = new HashMap<>();
                for (com.pukka.ydepg.launcher.bean.node.NamedParameter namedParameter : extras) {
                    extraData.put(namedParameter.getKey(), namedParameter.getFistItemFromValue());
                }
            }
        }
    }

    private void loadTopic(PBSRemixRecommendResponse response) {
        if (null != response.getRecommends().get(0).getTopics() && response.getRecommends().get(0).getTopics().size() > 0) {
            /**
             * 处理H5页面
             * type=native原生专题；h5、activity、h5为H5专题.
             * */
            Topic topic = response.getRecommends().get(0).getTopics().get(0);
            if (TextUtils.isEmpty(topic.getNotice())) {
                SuperLog.debug(TAG, "Notice=null,跑马灯内容为空，不展示");
                return;
            }
            String tag = null;
            if (NATIVE.equalsIgnoreCase(topic.getType())) {
                StringBuffer sb = new StringBuffer();
                sb.append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE + "=");
                sb.append(ZJVRoute.LauncherElementContentType.VOD_CATEGORY);
                sb.append("&" + ZJVRoute.ActionUrlKeyType.CONTENT_ID + "=");
                sb.append(topic.getRelationSubjectId());
                sb.append("&" + ZJVRoute.ActionUrlKeyType.FOCUS_COTENTID + "=");
                sb.append(topic.getTopicStyleId());
                tag = sb.toString();
            } else if (ACTIVITY.equalsIgnoreCase(topic.getType()) || H5.equalsIgnoreCase(topic.getType())) {
                tag = topic.getTopicURL();
            }
            setScrollTvData(topic.getNotice(), tag);
        }
    }

    private void setScrollTvData(String txt, String tag) {
        mScrollString = txt;
        OTTApplication.getContext().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != mTvEpgScrollAds) {
                    //isHideTvEpgScrollAd(false);
                    //mTvEpgScrollAds.setText(txt, TextView.BufferType.EDITABLE);
                    mTvEpgScrollAds.setTag(tag);
                    //mTvEpgScrollAds.setTag(-1,txt);
                }
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

    public interface OnQueryEPGFunctionListener {
        void getEPGFunctionData(List<SubjectVODList> subjectVODLists);

        void getEPGFunctionDataFail();
    }

    public interface OnPBSRemixRecommendListener {
        void getRemixRecommendData(PBSRemixRecommendResponse pbsRemixRecommendResponse);

        void getRemixRecommendDataFail();
    }
}