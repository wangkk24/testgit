package com.pukka.ydepg.common.utils.uiutil;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CpRoute {

    private static final String TAG = CpRoute.class.getSimpleName();


    public static void main(String[] args) {
        String mapStr = "{\"cpId\":\"2\",\"albumId\":\"1051629\",\"chnId\":\"1000001\"}";

        Type jsonType1 = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> llla = JsonParse.json2Object(mapStr, jsonType1);
        Iterator<String> iter = llla.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = llla.get(key);
            System.out.println(key + " " + value);
        }
//        llla.get(0).entrySet();

        HashMap<String, String> map = new HashMap<>();
//        for (NamedParameter namedParameter : list) {
//            mapConfigValue.put(namedParameter.getKey(), namedParameter.getFistItemFromValue());
//        }
        String str = " [\"600000501108\",\"600000501077\",\"600000526411\",\"600000591411\",\"600000591415\",\"600000586942\"]";
        Type jsonType = new TypeToken<List<String>>() {
        }.getType();
        List<String> lll = JsonParse.json2Object(str, jsonType);
        if (lll.contains("600000501108")) {

        }
    }

    public static boolean isCp(String cpid) {
        //jumpExternalCpIdListKey跳转第三方的终端参数key
        if (null != SessionService.getInstance().getSession().getTerminalConfigurationValue("jumpExternalCpIdListKey")) {
            String str = SessionService.getInstance().getSession().getTerminalConfigurationValue("jumpExternalCpIdListKey");
            if (!TextUtils.isEmpty(str)) {
                Type jsonType = new TypeToken<List<String>>() {
                }.getType();
                List<String> cpIdList = JsonParse.json2Object(str, jsonType);
                if (!CollectionUtil.isEmpty(cpIdList) && !TextUtils.isEmpty(cpid)) {
                    if (cpIdList.contains(cpid) && !TextUtils.isEmpty(cpid)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isCp(String cpid, List<NamedParameter> namedParameters) {
        //jumpExternalCpIdListKey跳转第三方的终端参数key
        if (CollectionUtil.isEmpty(namedParameters)) {
            return false;
        }
        HashMap<String, String> mapConfigValue = new HashMap<>();
        for (NamedParameter namedParameter : namedParameters) {
            mapConfigValue.put(namedParameter.getKey(), namedParameter.getFistItemFromValue());
        }
        String pkgName = mapConfigValue.get("zjapkpkg");
        SuperLog.debug(TAG, "pkgName=" + pkgName);

        String str = SessionService.getInstance().getSession().getTerminalConfigurationValue("jumpExternalCpIdListKey");
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(pkgName)) {
            Type jsonType = new TypeToken<List<String>>() {
            }.getType();
            List<String> cpIdList = JsonParse.json2Object(str, jsonType);
            if (!CollectionUtil.isEmpty(cpIdList)) {
                return cpIdList.contains(cpid);
            }
        }
        return false;
    }

    //Extra格式样例 {"cpId":"2","albumId":"1051629","chnId":"1000001"}
    public static void goCp(List<NamedParameter> namedParameters) {
        HashMap<String, String> mapConfigValue = new HashMap<>();
        for (NamedParameter namedParameter : namedParameters) {
            mapConfigValue.put(namedParameter.getKey(), namedParameter.getFistItemFromValue());
        }
        String pkgName = mapConfigValue.get("zjapkpkg");
        String className = mapConfigValue.get("zjapkcls");
        String extra = mapConfigValue.get("zjapkextra");
        startApk(pkgName, className, extra);
    }

    public static void startApk(String pkgName, String className, String extra) {
        if (TextUtils.isEmpty(pkgName)) {
            SuperLog.info2SD(TAG, "pkgName is empty");
            return;
        }
        Intent startIntent = new Intent();//startintent为启动应用二级界面的intent
        if (!TextUtils.isEmpty(className)) {
            ComponentName comp = new ComponentName(pkgName, className);//1.package name 2.class name
            startIntent.setComponent(comp);
        } else {
            if (isApkExist(pkgName)) {
                PackageManager manager = OTTApplication.getContext().getPackageManager();
                startIntent = manager.getLaunchIntentForPackage(pkgName);
            } else {
                startIntent.setComponent(new ComponentName(pkgName, ""));
            }
        }
        if (!TextUtils.isEmpty(extra)) {
            Type jsonType1 = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> llla = JsonParse.json2Object(extra, jsonType1);
            if (null != llla) {
                Iterator<String> iter = llla.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String value = llla.get(key);
                    startIntent.putExtra(key, value);
                }
            }
        }
        if (isApkExist(pkgName)) {
            //如果apk存在则直接跳转
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                OTTApplication.getContext().startActivity(startIntent);
            } catch (Exception e) {
                EpgToast.showLongToast(OTTApplication.getContext(), "Can not start the activity, pkg=" + pkgName + " cls=" + className);
                SuperLog.error(TAG, e);
            }

        } else {
            //apk不存在先下载在拉起（拉起的操作由应用商城完成）
            Intent mIntent = new Intent("com.istv.ystframework.apkmanager.service.Service");
            mIntent.setComponent(new ComponentName("com.istv.appstore", "com.istv.ystframework.services.monitor.MonitorService"));
            mIntent.putExtra("startIntent", startIntent);
            mIntent.putExtra("packageName", pkgName); //apk 包名不能为空
            //按照长顺的意思是平台配置下面这几个参数，我们来解析put保证每个都有值。
            if (!TextUtils.isEmpty(extra)) {
                Type jsonType1 = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> llla = JsonParse.json2Object(extra, jsonType1);
                if (null != llla) {
                    Iterator<String> iter = llla.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        String value = llla.get(key);
                        mIntent.putExtra(key, value);
                        SuperLog.info2SD(TAG, "key=" + key + "---value=" + value);
                    }
                }
            }

//            mIntent.putExtra("appName", "蘑菇跳跳"); //apk 名称，可以为空，下载时显示用
//            mIntent.putExtra("appVersion", "6"); //apk 版本号可以为空
//            mIntent.putExtra("forceUpdate", 1);//可为空，传1代表强制下载，0代表非强制下载，不传默认为0，传空或其他值，按1处理
//            mIntent.putExtra("appUrl", "http://zj-mobile-ystzw-gamedownload.wasu.tv:8085/app/672/1/153682106914015032179703871439375614913281_MushroomJump_YSTen-2.apk"); //apk 下载地址可以为空
            OTTApplication.getContext().startService(mIntent);
        }
    }

    private static boolean isApkExist(String pkgName) {
        PackageInfo isPackageExit = null;
        try {
            isPackageExit = OTTApplication.getContext().getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            SuperLog.info2SD(TAG, "isApkExist e=" + e.getMessage());
            return false;
        }

        if (null != isPackageExit) {
            return true;
        }
        return false;
    }
}
