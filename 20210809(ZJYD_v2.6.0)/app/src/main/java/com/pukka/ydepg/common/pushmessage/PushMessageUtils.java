package com.pukka.ydepg.common.pushmessage;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class PushMessageUtils {

    //组织功能键跳转第三方APK的扩展参数
    public static void setExtraData(String extraString,Map<String, String> extraData) {
        if (!TextUtils.isEmpty(extraString)) {
            for (String keyValue : extraString.split("&")) {
                String[] keyValues = keyValue.split("=");
                if (keyValues.length == 2) {
                    extraData.put(keyValues[0], keyValues[1]);
                }
            }
        }
    }

}
