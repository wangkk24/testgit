package com.pukka.ydepg.moudule.vrplayer.vrplayer;

import android.content.Context;
import android.content.SharedPreferences;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean.UpdateBean;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.global.AreaSpecialProperties;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.LogUtil;

/**
 * 功能描述
 *ac
 * @author l00477311
 * @since 2020-08-08
 */
public class VRPlayerApplictaion{
    private static final String TAG = "VRPlayerApplictaion";
    public static final String LOG_FILE_PATH = "sdcard/Alarms/tvdemoLog/";
    public static final String LOG_LEVEL = "LOG_LEVEL";
    private static Context context;
    private static VRPlayerApplictaion application;
    private boolean hasCheckUpdate = false;

    private UpdateBean updateBean;

    private SharedPreferences sp;

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        sp = getSharedPreferences("360vr", Context.MODE_PRIVATE); //私有数据
//        // 初始化配置文件
//        AreaSpecialProperties.initResource(getApplicationContext());
//        LogUtil.logI(TAG, "singleSeekTime = " + AreaSpecialProperties.getSingleSeekTime());
//        LogUtil.logI(TAG, "longSeekSpeed = " + AreaSpecialProperties.getLongSeekSpeed());
//        LogUtil.logI(TAG, "menuShowTime = " + AreaSpecialProperties.getMenuShowTime());
//
//        int logLevel = sp.getInt(LOG_LEVEL, DebugLog.DEBUG);
//
//
//        application = this;
//        context = this;
//        DebugLog.initLogcatLevel(logLevel);
//        DebugLog.initLogFileLevel(logLevel,LOG_FILE_PATH);
//    }

    public static VRPlayerApplictaion getInstance() {
        if (application == null) {
            synchronized (VRPlayerApplictaion.class) {
                if (application == null) {
                    application = new VRPlayerApplictaion();
                }
            }
        }
        return application;
    }


    public boolean isHasCheckUpdate() {

        return hasCheckUpdate;
    }

    public void setHasCheckUpdate(boolean hasCheckUpdate) {
        this.hasCheckUpdate = hasCheckUpdate;
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public UpdateBean getUpdateBean() {
        return updateBean;
    }

    public void setUpdateBean(UpdateBean updateBean) {
        this.updateBean = updateBean;
    }
    public static Context getContext() {
        return context;
    }

    public static VRPlayerApplictaion getApplication() {
        return application;
    }

    public void setContext(Context context) {
        this.context=context;
        sp = context.getSharedPreferences("360vr", Context.MODE_PRIVATE); //私有数据
        // 初始化配置文件
        AreaSpecialProperties.initResource(context);
        LogUtil.logI(TAG, "singleSeekTime = " + AreaSpecialProperties.getSingleSeekTime());
        LogUtil.logI(TAG, "longSeekSpeed = " + AreaSpecialProperties.getLongSeekSpeed());
        LogUtil.logI(TAG, "menuShowTime = " + AreaSpecialProperties.getMenuShowTime());

        int logLevel = sp.getInt(LOG_LEVEL, DebugLog.DEBUG);
        DebugLog.initLogcatLevel(logLevel);
        //DebugLog.initLogFileLevel(logLevel,LOG_FILE_PATH);
    }
}
