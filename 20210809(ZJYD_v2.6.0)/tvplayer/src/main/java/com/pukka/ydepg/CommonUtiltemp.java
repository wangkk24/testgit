package com.pukka.ydepg;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

public class CommonUtiltemp {
    //获取设备型号,如CM201z
    public static String getDeviceType(){
        String systemModel = getSystemInfo("ro.product.model.RAW");
        if (TextUtils.isEmpty(systemModel) || "unknown".equals(systemModel)) {
            systemModel = Build.MODEL;
        }
        return systemModel;
    }

    /**
     * 获取机顶盒设备信息
     * @param id
     * @return
     */
    public static String getSystemInfo(String id) {
        String serialnum = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, id, "unknown"));
        } catch (Exception ignored) {
            Log.e("CommonUtiltemp",ignored.getMessage());
        }
        return serialnum;
    }
    public static boolean IsVoice()
    {
        if(getDeviceType().equals("LDY-A"))
        {
            return true;
        }
        return false;
    }

    public static boolean isJMGODevice(){
        return getDeviceType().contains("JmGO");
    }
}