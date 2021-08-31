package com.pukka.ydepg.common.utils;

import android.content.Context;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.example.retrofit.utils.UtilBase.java
 * @author: yh
 * @date: 2016-10-26 14:44
 */

public class UtilBase {

    private static final String TAG = "UtilBase";

    //the context of application
    private static Context applicationContext;

    public static void init(Context context) {
        applicationContext = context.getApplicationContext();
    }


    public static Context getApplicationContext()
    {
        if(null == applicationContext) {
            SuperLog.error(TAG,"The OTT SDK application context is null, you need to init OTTSDK by OTTSDK.init(context) before you use it");
        }
        return applicationContext;
    }
}