package com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.tools;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.launcher.session.SessionService;

import java.util.Map;

//用于控制退订挽留页 页面按钮落焦，文字的工具类
public class UnsubscribeButtonConfigUtils {

    //退订挽留页面
    public static final String UNSUBSCRIBE_BUTTON = "unsubscribe";

    //退订挽留确认页面
    public static final String UNSUBSCRIBE_CONFIRM_BUTTON = "unsubscribe_confirm";

    /*配置落焦 配置0 落在取消按钮，配置1 落在确认按钮
     *不配置时默认取消按钮
     */
    private static final String FOCUS_CONFIG = "focus";

    /*配置取消按钮文本
     *不配置时退订挽留页面默认"我要保留"，退订挽留确认页面"我再想想"
     */
    public static final String BUTTON_TEXT_CONFIG_CANCEL = "cancel";

    /*配置确认按钮文本
     *不配置时退订挽留页面默认"残忍退订"，退订挽留确认页面"继续退订"
     */
    public static final String BUTTON_TEXT_CONFIG_CONFIRM = "confirm";

    //退订挽留页 取消按钮默认文本
    public static final String UNSUBSCRIBE_BUTTON_TEXT_CANCEL = "我要保留";

    //退订挽留页 确认按钮默认文本
    public static final String UNSUBSCRIBE_BUTTON_TEXT_CONFIRM = "残忍退订";

    //退订挽留确认页 取消按钮默认文本
    public static final String UNSUBSCRIBE_CONFIRM_BUTTON_TEXT_CANCEL = "我再想想";

    //退订挽留确认页 确认按钮默认文本
    public static final String UNSUBSCRIBE_CONFIRM_BUTTON_TEXT_CONFIRM = "继续退订";

    //落在取消
    public static final String CANCEL = "0";

    //落在确认
    public static final String CONFIRM = "1";

    /*
     *获取落焦位置 0落在取消，1落在确认
     * scene 场景
     */
    public static String getFocusConfig(String scene){
        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationUnsubscribeButtonConfig();
        Map<String,Map<String,String>> map = JsonParse.json2Object(configStr,new TypeToken<Map<String,Map<String,String>>>() {}.getType());

        if (TextUtils.isEmpty(configStr) || null == map){
            return CANCEL;
        }

        if (scene.equals(UNSUBSCRIBE_BUTTON)){
            Map<String, String> mapForUnsubscribe = map.get(UNSUBSCRIBE_BUTTON);
            if (null != mapForUnsubscribe && !TextUtils.isEmpty(mapForUnsubscribe.get(FOCUS_CONFIG))){
                return mapForUnsubscribe.get(FOCUS_CONFIG);
            }

        }else if (scene.equals(UNSUBSCRIBE_CONFIRM_BUTTON)){
            Map<String, String> mapForUnsubscribeConfirm = map.get(UNSUBSCRIBE_CONFIRM_BUTTON);
            if (null != mapForUnsubscribeConfirm && !TextUtils.isEmpty(mapForUnsubscribeConfirm.get(FOCUS_CONFIG))){
                return mapForUnsubscribeConfirm.get(FOCUS_CONFIG);
            }
        }

        return CANCEL;
    }

    /*
     *获取文本信息
     * scene 场景
     * loaction 位置 左侧还是右侧
     * 异常情况返回""
     */
    public static String getButtonText(String scene,String location){
        String configStr = SessionService.getInstance().getSession().getTerminalConfigurationUnsubscribeButtonConfig();
        Map<String,Map<String,String>> map = JsonParse.json2Object(configStr,new TypeToken<Map<String,Map<String,String>>>() {}.getType());

        if (TextUtils.isEmpty(configStr) || null == map){
            if (scene.equals(UNSUBSCRIBE_BUTTON)){
                if (location.equals(BUTTON_TEXT_CONFIG_CANCEL)){
                    return UNSUBSCRIBE_BUTTON_TEXT_CANCEL;
                }else if (location.equals(BUTTON_TEXT_CONFIG_CONFIRM)){
                    return UNSUBSCRIBE_BUTTON_TEXT_CONFIRM;
                }
            }else if (scene.equals(UNSUBSCRIBE_CONFIRM_BUTTON)){
                if (location.equals(BUTTON_TEXT_CONFIG_CANCEL)){
                    return UNSUBSCRIBE_CONFIRM_BUTTON_TEXT_CANCEL;
                }else if (location.equals(BUTTON_TEXT_CONFIG_CONFIRM)){
                    return UNSUBSCRIBE_CONFIRM_BUTTON_TEXT_CONFIRM;
                }
            }
            return "";
        }

        if (scene.equals(UNSUBSCRIBE_BUTTON)){
            Map<String, String> mapForUnsubscribe = map.get(UNSUBSCRIBE_BUTTON);
            if (location.equals(BUTTON_TEXT_CONFIG_CANCEL)){
                if (null != mapForUnsubscribe && !TextUtils.isEmpty(mapForUnsubscribe.get(BUTTON_TEXT_CONFIG_CANCEL))){
                    return mapForUnsubscribe.get(BUTTON_TEXT_CONFIG_CANCEL);
                }
                return UNSUBSCRIBE_BUTTON_TEXT_CANCEL;
            }else if (location.equals(BUTTON_TEXT_CONFIG_CONFIRM)){
                if (null != mapForUnsubscribe && !TextUtils.isEmpty(mapForUnsubscribe.get(BUTTON_TEXT_CONFIG_CONFIRM))){
                    return mapForUnsubscribe.get(BUTTON_TEXT_CONFIG_CONFIRM);
                }
                return UNSUBSCRIBE_BUTTON_TEXT_CONFIRM;
            }
            return "";


        }else if (scene.equals(UNSUBSCRIBE_CONFIRM_BUTTON)){
            Map<String, String> mapForUnsubscribeConfirm = map.get(UNSUBSCRIBE_CONFIRM_BUTTON);
            if (location.equals(BUTTON_TEXT_CONFIG_CANCEL)){
                if (null != mapForUnsubscribeConfirm && !TextUtils.isEmpty(mapForUnsubscribeConfirm.get(BUTTON_TEXT_CONFIG_CANCEL))){
                    return mapForUnsubscribeConfirm.get(BUTTON_TEXT_CONFIG_CANCEL);
                }
                return UNSUBSCRIBE_CONFIRM_BUTTON_TEXT_CANCEL;
            }else if (location.equals(BUTTON_TEXT_CONFIG_CONFIRM)){
                if (null != mapForUnsubscribeConfirm && !TextUtils.isEmpty(mapForUnsubscribeConfirm.get(BUTTON_TEXT_CONFIG_CONFIRM))){
                    return mapForUnsubscribeConfirm.get(BUTTON_TEXT_CONFIG_CONFIRM);
                }
                return UNSUBSCRIBE_CONFIRM_BUTTON_TEXT_CONFIRM;
            }
            return "";
        }
        return "";

    }

}
