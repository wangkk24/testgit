package com.pukka.ydepg.common.utils;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6response.SuperScript;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.utils.EPGMainDataHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperScriptUtil {
    /**
     * 导读类角标id和名称对应关系
     */
    private static Map<String, String> homeSuperScript = new HashMap<>();
    /**
     * 资费类角标id和名称对应关系
     */
    private static Map<String, String> detailSuperScript = new HashMap<>();
    /**
     * 角标优先级和id对应关系
     */
    private static Map<String, Integer> homePriorityMap = new HashMap<>();
    private static Map<String, Integer> detailPriorityMap = new HashMap<>();
    private static SuperScriptUtil mInstance;
    private static final String COMMING_SOON = "Subscript_Comming_Soon";
    private static final String THEME = "Subscript_Theme";

    //静态资源位--剧集--更新第几集
    private static final String STATIC_EPISODE_NUM = "Static_Spisode_Num";
    //静态资源位--剧集--全集
    private static final String STATIC_EPISODE_NUM_MAX = "Static_Spisode_Num_Max";
    //series：集；period:期。   第几期/第几集
    private static final String STATIC_EPISODE_TYPE = "Static_Spisode_Type";

    static {
        homeSuperScript.put("有更新", "Subscript_Updated");
        homeSuperScript.put("限时免费", "Subscript_Limited_Free");
        homeSuperScript.put("新剧开播", "Subscript_New");
        homeSuperScript.put("敬请期待", "Subscript_Comming_Soon");
        homeSuperScript.put("专题", "Subscript_Theme");
        homeSuperScript.put("首播", "Subscript_First_Broadcast");
        homeSuperScript.put("预告片", "Subscript_Trailers");
        homeSuperScript.put("片花", "Subscript_Teaser");
        homeSuperScript.put("全网独播", "Subscript_Exclusive_Broadcast");
        homeSuperScript.put("大结局", "Subscript_Series_Ending");
        homeSuperScript.put("收官", "Subscript_Final");
        detailSuperScript.put("VIP", "Subscript_Vip_Content");
        detailSuperScript.put("付费", "Subscript_Paid_Content");
        detailSuperScript.put("限时免费", "Subscript_Limited_Free_Pay");
        homePriorityMap.put("有更新", 1);
        homePriorityMap.put("限时免费", 2);
        homePriorityMap.put("新剧开播", 3);
        homePriorityMap.put("敬请期待", 4);
        homePriorityMap.put("专题", 5);
        homePriorityMap.put("首播", 6);
        homePriorityMap.put("预告片", 7);
        homePriorityMap.put("片花", 8);
        homePriorityMap.put("全网独播", 9);
        homePriorityMap.put("大结局", 10);
        homePriorityMap.put("收官", 11);
        detailPriorityMap.put("VIP", 12);
        detailPriorityMap.put("付费", 13);
        detailPriorityMap.put("限时免费", 14);
    }

    private SuperScriptUtil() {

    }

    public static synchronized SuperScriptUtil getInstance() {
        if (null == mInstance) {
            mInstance = new SuperScriptUtil();
        }
        return mInstance;
    }

    /**
     * 获取vod动态角标地址
     *
     * @param data
     * @param isHome true 首页角标，false 详情页角标
     * @return
     */
    public String getSuperScriptByVod(VOD data, boolean isHome) {
        List<String> superScriptList=getSuperScriptList(data);
        if (isHome) {
            String scriptType = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.EPG_MAIN_IS_USE_POSTAGE_SCRIPT);
            if (TextUtils.isEmpty(scriptType)){
                scriptType = "1";
            }
            List<SuperScript> superScripts = EPGMainDataHolder.getSuperScript();
            if (!CollectionUtil.isEmpty(superScriptList) && !CollectionUtil.isEmpty(superScripts)){
                for (int j = 0; j < superScripts.size();j++) {
                    for (int i = 0; i < superScriptList.size(); i++) {
                        if (superScriptList.get(i).equalsIgnoreCase(superScripts.get(j).getName()) && superScripts.get(j).getType() == Integer.parseInt(scriptType)) {
                            if (!TextUtils.isEmpty(superScripts.get(j).getIcon())){
                                return Constant.PBS_SCRIPT_URL + superScripts.get(j).getIcon();
                            }
                            return "";
                        }
                    }
                }
            }
            //return GlideUtil.getUrl(scriptUrl);
            return "";
        } else {
            //开关打开时，展示角标
            if (getSuperScriptSwitch(SUPERSCRIPT_Other)){
                List<SuperScript> superScripts = EPGMainDataHolder.getSuperScript();
                if (!CollectionUtil.isEmpty(superScriptList) && !CollectionUtil.isEmpty(superScripts)){
                    for (int j = 0; j < superScripts.size();j++) {
                        for (int i = 0; i < superScriptList.size(); i++) {
                            if (superScriptList.get(i).equalsIgnoreCase(superScripts.get(j).getName()) && superScripts.get(j).getType() == 0) {
                                if (!TextUtils.isEmpty(superScripts.get(j).getIcon())){
                                    return Constant.PBS_SCRIPT_URL + superScripts.get(j).getIcon();
                                }
                                return "";
                            }
                        }
                    }
                }
            }

            //return GlideUtil.getUrl(scriptUrl);
            return "";
        }
    }

    //详情页角标，包括详情页角标，详情页子集角标，播控子集角标，儿童版子集角标等
    public String getSuperScriptByVod(VOD data, String type){
        List<String> superScriptList=getSuperScriptList(data);
        String scriptUrl = "";
        List<SuperScript> superScripts = EPGMainDataHolder.getSuperScript();

        scriptUrl = getUrlByLoop(superScripts,superScriptList,type,0);

        if (!TextUtils.isEmpty(scriptUrl)){
            scriptUrl = Constant.PBS_SCRIPT_URL + scriptUrl;
        }
        return scriptUrl;
    }

    private String getUrlByLoop(List<SuperScript> superScripts ,List<String> superScriptList,String type,int superScriptsType){
        if (!CollectionUtil.isEmpty(superScriptList) && !CollectionUtil.isEmpty(superScripts)){
            for (int j = 0; j < superScripts.size();j++) {
                for (int i = 0; i < superScriptList.size(); i++) {
                    Log.i(TAG, "getUrlByLoop: "+superScripts.get(j).getType()+" "+ superScriptsType + superScriptList.get(i) + " "+ superScripts.get(j).getName());
                    if (superScriptList.get(i).equalsIgnoreCase(superScripts.get(j).getName()) && superScripts.get(j).getType() == superScriptsType) {
                        return getSuperScriptByType(superScripts.get(j),type);
                    }
                }
            }
        }

        return "";
    }

    public static final String SCRIPT_ICON = "script_icon";
    //详情页标题旁边角标
    public static final String SCRIPT_DETAIL_ICON = "script_detail_icon";
    //详情页子集角标
    public static final String SCRIPT_SUB_ICON = "script_sub_icon";
    //儿童版详情页子集角标
    public static final String SCRIPT_CHILD_SUB_ICON = "script_child_sub_icon";
    //播放页子集角标
    public static final String SCRIPT_PLAY_SUB_ICON = "script_play_sub_icon";

    //根据类型获取字段
    private String getSuperScriptByType(SuperScript superScript, String type){
        switch (type){
            case SCRIPT_DETAIL_ICON:{
                return superScript.getDetailIcon();
            }
            case SCRIPT_ICON:{
                return superScript.getIcon();
            }
            case SCRIPT_SUB_ICON:{
                return superScript.getSubIcon();
            }
            case SCRIPT_CHILD_SUB_ICON:{
                return superScript.getSubChildIcon();
            }
            case SCRIPT_PLAY_SUB_ICON:{
                return superScript.getSubPlayIcon();
            }
        }
        return "";
    }


    public boolean isShowVipMark(VOD data, String fatherPrice){
        List<String> superScriptList=getSuperScriptList(data, fatherPrice);
        return  superScriptList.contains("VIP");

    }

    public List<String>  getSuperScriptList(VOD data){
        List<String> superScriptList = new ArrayList<>();
        List<NamedParameter> namedParameters = data.getCustomFields();
        if (CollectionUtil.isEmpty(namedParameters)) {
            return superScriptList;
        }
        String superScriptStrs = "";
        String superScriptValidTimeStrs = "";
        String[] superScripts;
        String[] superScriptValidTimes;
        for (NamedParameter namedParameter : namedParameters) {
            if (VOD.KEY_SUPERSCRIPT.equals(namedParameter.getKey())) {
                superScriptStrs = namedParameter.getFistItemFromValue();
            }
            if (VOD.KEY_SUPERSCRIPT_VALID_TIME.equals(namedParameter.getKey())) {
                superScriptValidTimeStrs = namedParameter.getFistItemFromValue();
            }
        }
        if (null == superScriptStrs){
            superScriptStrs = "";
        }
        if (null == superScriptValidTimeStrs){
            superScriptValidTimeStrs = "";
        }
        superScripts = superScriptStrs.split("\\|");
        superScriptValidTimes = superScriptValidTimeStrs.split("\\|");
        /*if(superScripts.length != superScriptValidTimes.length){
            //防止出现配置错误,如何角标数量和时间数量不一致,直接返回
            return superScriptList;
        }*/
        for (int i = 0; i < superScripts.length; i++) {
            String[] times = new String[0];
            if (i < superScriptValidTimes.length){
                times = superScriptValidTimes[i].split("-");
            }
            //2.4角标优化，如果没有配置有效期，则一直显示
            if (times.length > 1) {
                String startTime = times[0];
                String endTime = times[1];
                //判别有效期
                if (DateCalendarUtils.isTimeValid(startTime, endTime, DateCalendarUtils.PATTERN_VIP_VALID_TIME)) {
                    superScriptList.add(superScripts[i]);
                }
            }else{
                superScriptList.add(superScripts[i]);
            }
        }
        return superScriptList;
    }

    public List<String>  getSuperScriptList(VOD data, String fatherPrice){
        List<String> superScriptList = new ArrayList<>();
        List<NamedParameter> namedParameters = data.getCustomFields();
        if (CollectionUtil.isEmpty(namedParameters)) {
            SuperLog.info2SDDebug("EpisodeVIP", "namedParameters is null");
            if(isEpisodeShowVip(data, fatherPrice)){
                superScriptList.add("VIP");
            }
            return superScriptList;
        }
        String superScriptStrs = "";
        String superScriptValidTimeStrs = "";
        String[] superScripts;
        String[] superScriptValidTimes;
        for (NamedParameter namedParameter : namedParameters) {
            if (VOD.KEY_SUPERSCRIPT.equals(namedParameter.getKey())) {
                superScriptStrs = namedParameter.getFistItemFromValue();
            }
            if (VOD.KEY_SUPERSCRIPT_VALID_TIME.equals(namedParameter.getKey())) {
                superScriptValidTimeStrs = namedParameter.getFistItemFromValue();
            }
        }
        SuperLog.info2SDDebug("EpisodeVIP", "superScriptStrs is " + superScriptStrs);
        SuperLog.info2SDDebug("EpisodeVIP", "superScriptValidTimeStrs is " + superScriptValidTimeStrs);
        if((TextUtils.isEmpty(superScriptStrs) || !superScriptStrs.contains("VIP")) && isEpisodeShowVip(data, fatherPrice)){
            superScriptList.add("VIP");
        }
        if (null == superScriptStrs){
            superScriptStrs = "";
        }
        if (null == superScriptValidTimeStrs){
            superScriptValidTimeStrs = "";
        }
        superScripts = superScriptStrs.split("\\|");
        superScriptValidTimes = superScriptValidTimeStrs.split("\\|");
        //SuperLog.info2SDDebug("EpisodeVIP", "superScripts.length is" + superScripts.length);
        //SuperLog.info2SDDebug("EpisodeVIP", "superScriptValidTimes.length is" + superScriptValidTimes.length);
        for (int i = 0; i < superScripts.length; i++) {
            String[] times = superScriptValidTimes[i].split("-");
            SuperLog.info2SDDebug("EpisodeVIP", "superScripts.length is" + superScripts.length);
            if (null != times && times.length > 1) {
                String startTime = times[0];
                String endTime = times[1];
                SuperLog.info2SDDebug("EpisodeVIP", "startTime is" + startTime);
                SuperLog.info2SDDebug("EpisodeVIP", "endTime is" + endTime);
                //判别有效期
                if (DateCalendarUtils.isTimeValid(startTime, endTime, DateCalendarUtils.PATTERN_VIP_VALID_TIME)) {
                    SuperLog.info2SDDebug("EpisodeVIP", "added superScripts is" + superScripts[i]);
                    superScriptList.add(superScripts[i]);
                }
            }
        }
        return superScriptList;
    }

    public boolean isEpisodeShowVip(VOD vod, String fatherPrice) {
        if (null == vod) {
            return false;
        }
        String price = vod.getPrice();
        //SuperLog.info2SDDebug("EpisodeVIP", "price is " + price);
        //SuperLog.info2SDDebug("EpisodeVIP", "fatherPrice is " + fatherPrice);
        //空使用父级价格
        if (TextUtils.isEmpty(price)) {
            price = fatherPrice;
        } else {
            double pricet = Double.parseDouble(price);
            //不等于0使用父级价格
            if (pricet != 0) {
                price = fatherPrice;
            }
        }
        if (TextUtils.isEmpty(price)) {
            return false;
        } else {
            double pricet = Double.parseDouble(price);
            if (0 == pricet) {
                return false;
            } else {
                return true;
            }
        }

    }


    /**
     * 获取静态角标地址
     *
     * @param extraData element扩展参数
     * @return 角标对应的地址
     */
    public String getSuperScriptByElement(Map<String, String> extraData) {
        String mSuperScriptId = "";
        if (null != extraData) {
            if (extraData.containsKey(COMMING_SOON)) {
                mSuperScriptId = extraData.get(COMMING_SOON);
            } else if (extraData.containsKey(THEME)) {
                mSuperScriptId = extraData.get(THEME);
            }
        }
        if (TextUtils.isEmpty(mSuperScriptId)){
            return null;
        }
        String scriptType = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.EPG_MAIN_IS_USE_POSTAGE_SCRIPT);
        if (TextUtils.isEmpty(scriptType)){
            scriptType = "1";
        }
        List<SuperScript> superScripts = EPGMainDataHolder.getSuperScript();
        String scriptUrl = null;
        if (!CollectionUtil.isEmpty(superScripts)) {
            for (int j = 0; j < superScripts.size(); j++) {
                if (superScripts.get(j).getName().equalsIgnoreCase(mSuperScriptId) && superScripts.get(j).getType()==Integer.parseInt(scriptType)) {
                    scriptUrl = superScripts.get(j).getIcon();
                    break;
                }
            }
        }
        //return GlideUtil.getUrl(LauncherService.getInstance().getAdditionElementMap().get(mSuperScriptId));
        if (!TextUtils.isEmpty(scriptUrl)){
            scriptUrl = Constant.PBS_SCRIPT_URL + scriptUrl;
        }
        return scriptUrl;
    }

    //静态资源位剧集更新第几集
    public void setStaticEpisodeNum(Map<String, String> extraData, TextView textView) {
        if (textView == null)return;
        String mStaticEpisodeNum = "";
        String mStaticEpisodeType = "";
        String mStaticEpisodeMax = "";
        String tx = "";
            if (extraData.containsKey(STATIC_EPISODE_NUM_MAX)) {
                mStaticEpisodeMax = extraData.get(STATIC_EPISODE_NUM_MAX);
                if (extraData.containsKey(STATIC_EPISODE_TYPE)) {
                    mStaticEpisodeType = extraData.get(STATIC_EPISODE_TYPE);
                    if ("series".equals(mStaticEpisodeType)) {
                        tx = "集";
                    } else {
                        tx = "期";
                    }
                } else {
                    tx = "集";
                }

                if (extraData.containsKey(STATIC_EPISODE_NUM)) {
                    mStaticEpisodeNum = extraData.get(STATIC_EPISODE_NUM);
                    //最新子集的集号
                    int vodNum = Integer.valueOf(mStaticEpisodeNum);
                    //总集数
                    int maxSitcomNO = Integer.valueOf(mStaticEpisodeMax);
                    if (vodNum >= maxSitcomNO) {
                        textView.setText("全" + maxSitcomNO + tx);
                    } else {
                        textView.setText("更新至第" + vodNum + tx);
                    }
                    textView.setVisibility(View.VISIBLE);
                }


            } else {
                textView.setVisibility(View.GONE);
            }
    }

    //详情页场景
    //详情页最近热播
    public final static String SUPERSCRIPT_RECENT = "recent";
    //详情页猜你喜欢
    public final static String SUPERSCRIPT_GUESS = "guess";
    //详情页主创相关
    public final static String SUPERSCRIPT_ACTOR = "actor";
    //儿童版详情页
    public final static String SUPERSCRIPT_CHILD = "child";
    //其他场景（筛选等）
    public final static String SUPERSCRIPT_Other = "other";
    //详情页点击主创进入影人页面
    public final static String SUPERSCRIPT_FILMMAKER = "filmmaker";
    //子集场景，0走兼容逻辑，1走新逻辑，默认走新逻辑
    public final static String SUPERSCRIPT_SUB = "sub";

    private static final String TAG = "SuperScriptUtil";
    //子集角标，展示逻辑   true:新逻辑   false:兼容逻辑
    public boolean getSuperScriptLogic(VOD date){
        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationSuperScriptSwitch();
        Map<String,String> map = JsonParse.json2Object(configStr,new TypeToken<Map<String,String>>() {}.getType());
        if (null != map){
            String switchStr = map.get(SUPERSCRIPT_SUB);
            if (!TextUtils.isEmpty(switchStr) && "0".equals(switchStr)){
                return false;
            }
        }

        List<String> list = getSuperScriptList(date);
        Log.i(TAG, "onBindViewHolder getSuperScriptLogic: "+JsonParse.object2String(list));
        if (null != list && list.size() > 0 && !TextUtils.isEmpty(list.get(0))){
            return true;
        }else{
            return false;
        }
    }

    //角标开关
    public boolean getSuperScriptSwitch(String type){
        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationSuperScriptSwitch();
        Map<String,String> map = JsonParse.json2Object(configStr,new TypeToken<Map<String,String>>() {}.getType());
        if (null != map){
            String switchStr = map.get(type);
            if (!TextUtils.isEmpty(switchStr) && "0".equals(switchStr)){
                return false;
            }
        }
        return true;
    }

    //收藏和观看记录的角标开关
    public boolean getSuperScriptSwitchForHistoryAndCollection(){
        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Constant.SUPERSCRIPT_SWIRCH_HISTORY_COLLECTION);
        if (!TextUtils.isEmpty(configStr) && configStr.equals("0")){
            return false;
        }
        return true;
    }

    //获取收藏和观看记录的角标Url
    public String getSuperScriptForCollectionHistory(VOD data){
        int type = 0;
        if (getSuperScriptSwitchForHistoryAndCollection()){
            type = 1;
        }

        List<String> superScriptList=getSuperScriptList(data);
        String scriptUrl = "";
        List<SuperScript> superScripts = EPGMainDataHolder.getSuperScript();

        scriptUrl = getUrlByLoop(superScripts,superScriptList,SCRIPT_ICON,type);

        if (!TextUtils.isEmpty(scriptUrl)){
            scriptUrl = Constant.PBS_SCRIPT_URL + scriptUrl;
        }
        return scriptUrl;
    }
}
