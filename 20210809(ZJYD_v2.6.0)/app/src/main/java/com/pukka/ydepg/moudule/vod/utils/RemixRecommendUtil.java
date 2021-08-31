package com.pukka.ydepg.moudule.vod.utils;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.common.http.v6bean.v6node.Configuration;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.vod.presenter.VodDetailRecPresenter;

import java.util.List;
import java.util.Map;

public class RemixRecommendUtil {
    private static final String TAG = "RemixRecommendUtil";
    //用于获取混合智能推荐配置的工具类
    //详情页的混合智能推荐
    public static final String RemixRec_Voddetail = "VODDETAIL";
    //播放返回的混合智能推荐
    public static final String RemixRec_Playvod   = "PLAYVOD";
    //订购过渡的混合智能推荐
    public static final String RemixRec_Order     = "ORDER";

    //开关
    private static final String RemixRec_Config_switch    = "switch";
    //开关关闭时人工推荐的栏目id
    private static final String RemixRec_Config_subjectId = "subjectid";

    //订购过渡智能推荐id
    public static final String APPPINEDID_ORDER = "REC_BUY";

    //详情页智能推荐id
    public static final String APPPINEDID_VOD   = "REC_VOD";

    //儿童版详情页智能推荐id
    public static final String APPPINEDID_CHILD = "vodDetail001";

    //播放返回页智能推荐id
    public static final String APPPINEDID_PLAY  = "REC_PLAY";

    //获取智能推荐开关,没有配置时默认为打开，配置0时关闭，其他值打开
    public static boolean getRemixRecommendSwitch(String type){
        String remixRecommendStr = SessionService.getInstance().getSession().getTerminalConfigurationRecommendConfig();

        if (!TextUtils.isEmpty(remixRecommendStr)){
            Map<String,Map<String,String>> map = JsonParse.json2Object(remixRecommendStr,new TypeToken<Map<String,Map<String,String>>>() {}.getType());
            if (null != map){
                Map<String,String> mapForType = map.get(type);

                if (null != mapForType){
                    String switchStr = mapForType.get(RemixRec_Config_switch);
                    if (!TextUtils.isEmpty(switchStr) && "0".equals(switchStr)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //获取智能推荐开关关闭时，用于查询人工推荐的subjectid
    public static String getRemixRecommendSubjectid(String type){
        String remixRecommendStr = SessionService.getInstance().getSession().getTerminalConfigurationRecommendConfig();

        if (!TextUtils.isEmpty(remixRecommendStr)){
            Map<String,Map<String,String>> map = JsonParse.json2Object(remixRecommendStr,new TypeToken<Map<String,Map<String,String>>>() {}.getType());
            if (null != map){
                Map<String,String> mapForType = map.get(type);

                if (null != mapForType){
                    String subjectid = mapForType.get(RemixRec_Config_subjectId);
                    if (!TextUtils.isEmpty(subjectid)){
                        return subjectid;
                    }
                }
            }
        }
        return "";
    }

    //退出播放页的展示开关
    public static boolean getRemixRecommendSwitch(){
        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.PLAYBACK_RECOM_CONFIG);
//        configStr = "{\"switch\":\"0\",\"subjectId\":{\"sitcom\":\"catauto2000011091\",\"variety\":\"catauto2000011092\",\"other\":\"catauto2000011093\"}}";

        if (!TextUtils.isEmpty(configStr)){
            try {
                Map map = JsonParse.json2Object(configStr,Map.class);
                if (null != map){
                    String switchStr = (String) map.get(RemixRec_Config_switch);
                    if (!TextUtils.isEmpty(switchStr) && "0".equals(switchStr)){
                        return false;
                    }
                }

                return true;
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }

        }

        return true;
    }

    //退出播放页的人工推荐栏目id
    public static Map<String,String> getRemixRecommendSubjectid(){
        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationValue(Configuration.Key.PLAYBACK_RECOM_CONFIG);

//        configStr = "{\"switch\":\"0\",\"subjectId\":{\"sitcom\":\"catauto2000011091\",\"variety\":\"catauto2000011092\",\"other\":\"catauto2000011093\"}}";

        if (!TextUtils.isEmpty(configStr)){
            try {
                Map map = JsonParse.json2Object(configStr,Map.class);
                if (null != map){
                    return  (Map)map.get(VodDetailRecPresenter.SUBJECTID);
                }
                return null;
            }catch (Exception e){
                SuperLog.error(TAG,e);
            }

        }
        return null;
    }
}
