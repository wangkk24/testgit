package com.pukka.ydepg.common.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.lang.reflect.Method;
import java.util.List;

public class DeviceInfo {

    private static final String TAG = DeviceInfo.class.getSimpleName();

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
            SuperLog.error(TAG,ignored);
        }
        return serialnum;
    }

    public static  boolean isForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSkyworth(){
        if ( Constant.SKYWORTH.equals(Build.MANUFACTURER) ) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isM301H(){
        if(CommonUtil.getDeviceType().contains("M301H")){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isHisense(){
        if ( Build.MANUFACTURER != null && Constant.HISENSE.equals(Build.MANUFACTURER.toLowerCase()) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * MGV2000 简版大屏、小屏多次切换，黑屏黑问题
     * 原因进大屏时小屏release不及时
     * So判断是MGV2000的盒子第一次进大屏延时500ms start play
     * */
    public static boolean isMGV2000(){
        String systemInfo = getSystemInfo(com.pukka.ydepg.launcher.Constant.DEVICE_RAW);
        if (TextUtils.isEmpty(systemInfo) || "unknown".equals(systemInfo)) {
            systemInfo = Build.MODEL;
        }
        if (systemInfo.contains(Constant.MGV2000)) {
            return true;
        } else {
            return false;
        }
    }
}