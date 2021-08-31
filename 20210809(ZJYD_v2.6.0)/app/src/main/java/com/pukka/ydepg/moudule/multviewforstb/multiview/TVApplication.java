package com.pukka.ydepg.moudule.multviewforstb.multiview;

import android.content.Context;
import android.content.SharedPreferences;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.UpdateBean;

public class TVApplication{

    public static final String LOG_FILE_PATH = "sdcard/Alarms/Logs/";
    public static final String LOG_LEVEL = "LOG_LEVEL";
    private static Context context;
    private static TVApplication application;
    private UpdateBean updateBean;
    private boolean hasCheckUpdate = false;

    private SharedPreferences sp;

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        sp = getSharedPreferences("multi", Context.MODE_PRIVATE); //私有数据
//        initAppLog();
//        context = this.getApplicationContext();
//        application = this;
//    }

    public static TVApplication getInstance() {
        if (application == null) {
            synchronized (TVApplication.class) {
                if (application == null) {
                    application = new TVApplication();
                }
            }
        }
        return application;
    }
    public void setContext(Context context)
    {
        sp = context.getSharedPreferences("multi", Context.MODE_PRIVATE); //私有数据
        initAppLog();
        this.context= context;
        application = this;
    }
    /**
     * 初始化APP日志级别
     */
    private void initAppLog() {
        int logLevel = getLogLevel();
        DebugLog.initLogcatLevel(logLevel);
        //DebugLog.initLogFileLevel(logLevel,LOG_FILE_PATH);
    }

    /**
     * 获取本地存储的日志级别
     * @return
     */
    public int getLogLevel(){
        return sp.getInt(LOG_LEVEL, BuildConfig.LOG_ENABLE ? DebugLog.INFO : DebugLog.OFF);
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

    public static TVApplication getApplication() {
        return application;
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public boolean isHasCheckUpdate() {
        return hasCheckUpdate;
    }

    public void setHasCheckUpdate(boolean hasCheckUpdate) {
        this.hasCheckUpdate = hasCheckUpdate;
    }

//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        if(level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
//            //内存低时，清理缓存，降低APP被杀死的风险。
//            Glide.get(this).clearMemory();
//        }
//        Glide.get(this).trimMemory(level);
//    }

//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        //内存低时，清理缓存，降低APP被杀死的风险。
//        Glide.get(this).clearMemory();
//    }
}
