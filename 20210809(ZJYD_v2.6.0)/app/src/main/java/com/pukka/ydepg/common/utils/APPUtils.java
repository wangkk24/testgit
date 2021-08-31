package com.pukka.ydepg.common.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.mytv.utils.MessageDataHolder;

import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.common.utils.uiutil.APPUtils.java
 * @date: 2017-12-18 17:18
 * @version: V1.0 描述当前版本功能
 */

public class APPUtils {
    private static final String TAG = "APPUtils";

    public static final String KEY_IS_START_SERVICE = "KEY_IS_START_SERVICE";

    private static final String APPSTORE_PACKAGE_NAME = "com.istv.appstore";

    private static final String APPSTORE_START_CLASS = "com.istv.ui.app.appsquare.AppSquareActivity";

    private static final String APPSTORE_ACTION = "com.istv.ystframework.apkmanager.service.Service";

    private static final String APPSTORE_PLAY_SERVICE = "com.istv.ystframework.services.monitor.MonitorService";

    /**
     * 根据包名判断是否已经安装该apk
     */
    //根据包名判断app是否已经存在
    public static boolean isAvilible(Context mContext, String packageName) {
        PackageManager manager = mContext.getPackageManager();
        List<PackageInfo> infoList = manager.getInstalledPackages(0);
        for (int i = 0; i < infoList.size(); i++) {
            SuperLog.debug("isAvilible", "应用包名：" + infoList.get(i).packageName);
            if (infoList.get(i).packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 跳转到应用
     **/
    static Intent startAPP(Context context, String pkgName, String className, String version, String apkUrl, String action, String vodId, Map<String, String> extraData) {
        Intent mIntent = null;
        // 判断应用有没有，没有通过应用商店下载安全
        if (!isAvilible(context, pkgName)) {
            boolean isAppstoreInstall = isAvilible(context, APPSTORE_PACKAGE_NAME);
            SuperLog.debug(TAG, "startAPP_download isAppstoreInstall:" + isAppstoreInstall);
            if (isAppstoreInstall) {
                // 下载并安装
                mIntent = downloadAndInstallAPP(vodId,action, pkgName, className,extraData);
                mIntent.putExtra("KEY_IS_START_SERVICE", "true");
                return mIntent;
            }
        } else if (APPSTORE_PACKAGE_NAME.equals(pkgName)) {
            // 该应用存在且应用是应用商店，打开应用商店
            //检查商城是否存在
            SuperLog.debug(TAG, "跳转到应用商城");
            return startAppStore();
        } else {
            // 该应用存在且非应用商店，打开该应用
            //除了apk，其他apk
            SuperLog.debug(TAG, "跳转到其他 pkgName:" + pkgName);
            if (!TextUtils.isEmpty(className)) {
                //修改为支持跳转到已安装应用指定页面 --by lwm
                mIntent = new Intent();
                ComponentName componentName = new ComponentName(pkgName, className);
                mIntent.setComponent(componentName);
            } else {
                //其他apk，存在，则跳转到其他apk首页
                PackageManager manager = context.getPackageManager();
                try {
                    int versionCode = manager.getPackageInfo(pkgName,0).versionCode;
                    if (!TextUtils.isEmpty(version) && Integer.parseInt(version) > versionCode){
                        if (isAvilible(context, APPSTORE_PACKAGE_NAME)) {//判断应用商店是是否有
                            //去掉第三方更新角标
                            MessageDataHolder.get().setAppInfoVersion(pkgName,Integer.parseInt(version));
                            // 下载并安装
                            mIntent = downloadAndInstallAPP(vodId,action, pkgName, className,extraData);
                            mIntent.putExtra("KEY_IS_START_SERVICE", "true");
                            mIntent.putExtra("appVersion", version);
                            return mIntent;
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                mIntent = manager.getLaunchIntentForPackage(pkgName);
            }

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
    public static Intent downloadAndInstallAPP(String vodId,String appAction, String appPkgName, String appLaunchClass, Map<String, String> extraData) {
        SuperLog.debug(TAG, "appAction:" + appAction + "; appPkgName:" + appPkgName + "; appLaunchClass");
        // 创建启动一个app的intent
        Intent startIntent = new Intent();
        //setAction跟setComponent必须填一个
        if (!TextUtils.isEmpty(appAction)) {
            startIntent.setAction(appAction);
        } else {
            startIntent.setComponent(new ComponentName(appPkgName, appLaunchClass));
        }
        if (!TextUtils.isEmpty(vodId)){
            startIntent.putExtra("VODId", vodId);
        }

        //第三方app首次安装打开指定二级页面需透传配置的参数
        if (null != extraData) {
            for (String key : extraData.keySet()) {
                if (TextUtils.isEmpty(key)){
                    continue;
                }
                startIntent.putExtra(key, extraData.get(key));
            }
        }

        // 创建启动Appstore的intent
        Intent mIntent = new Intent(APPSTORE_ACTION);
        mIntent.setComponent(new ComponentName(APPSTORE_PACKAGE_NAME, APPSTORE_PLAY_SERVICE));
        mIntent.putExtra("startIntent", startIntent);
        mIntent.putExtra("packageName", appPkgName); //apk 包名不能为空
        return mIntent;
    }

    /**
     * 启动应用商店
     *
     * @return
     */
    public static Intent startAppStore() {
        Intent mIntent = new Intent();
        mIntent.setComponent(new ComponentName(APPSTORE_PACKAGE_NAME, APPSTORE_START_CLASS));
        return mIntent;
    }
}