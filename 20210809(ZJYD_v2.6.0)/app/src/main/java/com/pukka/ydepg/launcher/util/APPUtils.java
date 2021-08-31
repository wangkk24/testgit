package com.pukka.ydepg.launcher.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.node.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/28.
 */

public class APPUtils {
    private static final String TAG = "APPUtils";

    public  static final String APPSTORE_PACKAGE_NAME = "com.istv.appstore";

    private static final String APPSTORE_START_CLASS  = "com.istv.ui.app.appsquare.AppSquareActivity";

    private static final String APPSTORE_ACTION       = "com.istv.ystframework.apkmanager.service.Service";

    private static final String APPSTORE_PLAY_SERVICE = "com.istv.ystframework.services.monitor.MonitorService";

    private APPUtils() { }

    /**
     * 根据包名判断是否已经安装该apk
     * */
    //根据包名判断app是否已经存在
    public static boolean isAvilible(Context mContext, String packageName) {
        PackageManager manager = mContext.getPackageManager();
        List<PackageInfo> infoList = manager.getInstalledPackages(0);
        for (int i = 0; i < infoList.size(); i++) {
            SuperLog.debug(TAG ,"isAvilible:"+"应用包名："+infoList.get(i).packageName);
            if (infoList.get(i).packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 跳转到应用
     * **/
    public static Intent startAPP(Context context,String pkgName,String className ,String version,String apkUrl) {
        Intent mIntent = null;
        // 判断应用有没有，没有通过应用商店下载安全
        if (!isAvilible(context,pkgName))
        {
            boolean isAppstoreInstall = isAvilible(context, APPSTORE_PACKAGE_NAME);
            SuperLog.debug(TAG, "startAPP_download isAppstoreInstall:" + isAppstoreInstall);
            if (isAppstoreInstall)
            {
                // 下载并安装
                mIntent = downloadAndInstallAPP("", pkgName, className);
                mIntent.putExtra("KEY_IS_START_SERVICE", "true");
                return mIntent;
            }
        }
        // 该应用存在且应用是应用商店，打开应用商店
        else if (APPSTORE_PACKAGE_NAME.equals(pkgName)) {
            //检查商城是否存在
            SuperLog.debug(TAG,"跳转到应用商城");
            return startAppStore();
        }
        // 该应用存在且非应用商店，打开该应用
        else
            {
            //除了apk，其他apk
                SuperLog.debug(TAG, "跳转到其他 pkgName:" + pkgName);
            //其他apk，存在，则跳转到其他apk首页
            PackageManager manager = context.getPackageManager();
            mIntent = manager.getLaunchIntentForPackage(pkgName);
            if (mIntent != null) {
                return mIntent;
            } else {
                SuperLog.debug("APPUtil", "应用程序错误");
            }
        }
        return mIntent;
    }


    /**
     * 下载安装并启动apk，如下载咪咕音乐，并打开首页
     */
    public static Intent downloadAndInstallAPP(String appAction, String appPkgName, String appLaunchClass)
    {
        SuperLog.debug(TAG, "appAction:" + appAction + "; appPkgName:" + appPkgName + "; appLaunchClass");
        // 创建启动一个app的intent
        Intent startIntent = new Intent();
        //setAction跟setComponent必须填一个
        if (!TextUtils.isEmpty(appAction))
        {
            startIntent.setAction(appAction);
        }
        else
        {
            startIntent.setComponent(new ComponentName(appPkgName, appLaunchClass));
        }
        // 创建启动Appstore的intent
        Intent mIntent = new Intent(APPSTORE_ACTION);
        mIntent.setComponent(new ComponentName(APPSTORE_PACKAGE_NAME, APPSTORE_PLAY_SERVICE));
        mIntent.putExtra("startIntent", startIntent);
        mIntent.putExtra("packageName", appPkgName); //apk 包名不能为空
//        startService(mIntent);
        return mIntent;
    }

    /**
     * 启动应用商店
     * @return
     */
    public static Intent startAppStore()
    {
        Intent mIntent = new Intent();
        mIntent.setComponent(new ComponentName(APPSTORE_PACKAGE_NAME, APPSTORE_START_CLASS));
        return mIntent;
    }


    public static List<AppInfo> getAllAppInfo(Context context){
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据　

        StringBuffer logInfo = new StringBuffer();

        PackageManager pm = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mResolveInfo = pm.queryIntentActivities(mainIntent, 0);
        AppInfo appStore = null;
        for(ResolveInfo info : mResolveInfo){
            String packName = info.activityInfo.packageName;
            if(packName.equals(context.getPackageName())){
                continue;
            }
            if(packName.equals(APPSTORE_PACKAGE_NAME)){
                appStore = new AppInfo();
                appStore.setAppIcon(info.activityInfo.applicationInfo.loadIcon(pm));
                appStore.setAppName(info.activityInfo.applicationInfo.loadLabel(pm).toString());
                appStore.setPackageName(packName);
                continue;
            }

            try {
                PackageInfo packageInfo = pm.getPackageInfo(packName, 0);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    //第三方应用
                    AppInfo tmpInfo =new AppInfo();
                    tmpInfo.setAppIcon(info.activityInfo.applicationInfo.loadIcon(pm));
                    tmpInfo.setAppName(info.activityInfo.applicationInfo.loadLabel(pm).toString());
                    tmpInfo.setPackageName(packName);

                    try {
                        tmpInfo.setVersion(pm.getPackageInfo(packName,0).versionCode);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    logInfo.append(tmpInfo.getAppName()).append(tmpInfo.getVersion()).append(";");
                    appList.add(tmpInfo);
                } else {
                    //系统应用 根据需求不展示
                    continue;
                }
            } catch (PackageManager.NameNotFoundException e) {
                SuperLog.error(TAG,e);
            }
        }

        if(null != appStore){
            appList.add(appStore);
        }
        SuperLog.info2SD(TAG, "apkversion="+logInfo.toString());
        return appList;
    }
}
