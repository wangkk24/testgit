package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.VRPlayerApplictaion;


/**
 * the util of APP level
 * eg:the app is foreground,the top activity name,and so on
 */
public class ApplicationUtil
{
    /**
     * log tag
     */
    private static final String TAG = ApplicationUtil.class.getSimpleName();

    /**
     * get app version code
     *
     * @return app version code
     */
    public static int getCurrentAppVersionCode()
    {
        final int DEFAULT_VERSION = 1;
        try
        {
            PackageInfo packageInfo = getCurrentAppPackageInfo();
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            DebugLog.error(TAG, e);
        }
        return DEFAULT_VERSION;
    }

    /**
     * get package info
     *
     * @return package info
     * @throws PackageManager.NameNotFoundException
     */
    public static PackageInfo getCurrentAppPackageInfo() throws PackageManager.NameNotFoundException
    {
        PackageManager packageManager = VRPlayerApplictaion.getContext().getPackageManager();
        if (null == packageManager)
        {
            DebugLog.error(TAG, "null == packageManager");
            throw new PackageManager.NameNotFoundException();
        }
        else
        {
            PackageInfo packageInfo = packageManager.getPackageInfo(VRPlayerApplictaion.getContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo;
        }
    }
}