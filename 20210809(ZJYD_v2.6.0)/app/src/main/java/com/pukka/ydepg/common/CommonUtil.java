package com.pukka.ydepg.common;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.session.Session;
import com.pukka.ydepg.launcher.session.SessionService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.pukka.ydepg.launcher.Constant.DEVICE_RAW;
import static com.pukka.ydepg.launcher.Constant.DEVICE_STBID;

public class CommonUtil {
    private static final String TAG = CommonUtil.class.getSimpleName();

    private CommonUtil(){}

    public static Session getSession(){
        return SessionService.getInstance().getSession();
    }

    //认证方式 true为自认证 false为白盒认证
    public static boolean isSelfAuthenticate(){
        return false;
    }

    public static String getConfigValue(String key){
        try{
            return SessionService.getInstance().getSession().getTerminalConfigurationValue(key);
        }catch (Exception e){
            SuperLog.error(TAG,e);
            return null;
        }
    }

    public static Map<String,String> getMapConfigValue(String key){
        return JsonParse.jsonToMap(SessionService.getInstance().getSession().getTerminalConfigurationValue(key));
    }

    public static List<String> getListConfigValue(String key){
        List<String> list = new ArrayList<>();
        try{
            String value = SessionService.getInstance().getSession().getTerminalConfigurationValue(key);
            if(!TextUtils.isEmpty(value)){
                list= JsonParse.jsonToStringList(value);
            }
        }catch (Exception e){
            SuperLog.error(TAG,e);
        }

        return  list;
    }

    public static String getMac(){
        String mac;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
        } else {
            mac = getMacFromHardware();
        }
        return null == mac ? "" : mac;
    }

    public static String getAndroidID(){
        return Settings.Secure.getString(OTTApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static String getMacDefault() {
        String mac;
        WifiManager wifi = (WifiManager) OTTApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }

        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    private static String getMacAddress() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            while (null != str) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();//去空格
                    break;
                }
            }
        } catch (IOException e) {
            // 赋予默认值
            SuperLog.error(TAG,e);
        }
        return macSerial;
    }


    private static String getMacFromHardware() {
        try {
            ArrayList<NetworkInterface>  all  = null;
            if (null != NetworkInterface.getNetworkInterfaces()){
                all  = Collections.list(NetworkInterface.getNetworkInterfaces());
            }
            if (null != all && all.size() > 0) {
                for (int i = 0; i < all.size(); i++) {
                    NetworkInterface nif = all.get(0);
                    if (!nif.getName().equals("wlan0"))
                        continue;
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) return "";
                    StringBuilder res1 = new StringBuilder();
                    for (Byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }
                    if (!TextUtils.isEmpty(res1)) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (SocketException e) {
            SuperLog.error(TAG,e);
        }
        return "";
    }

    //关联手机号支付是否展示数字订购码
    public static boolean showVerifyCode(){
        //用户所在地区码,取ZJLogin接口返回的前三位
        String userAreaCode = CommonUtil.getSession().getUserAreaCode().substring(0,3);
        //地市白名单(支持单个开，多个开，全部开，关)
        List<String> whileList = CommonUtil.getListConfigValue(Constant.USE_VERIFICATION_CODE_WHITELIST);
        //20190920 根据新需求 增加白名单后去掉总开关
        if( whileList.contains(userAreaCode)){
            return true;
        } else {
            return false;
        }
    }

    public static void startSettingActivity(Context context){
        Intent mIntent = new Intent();

        //action
        mIntent.setAction("android.intent.action.VIEW");

        //comp
        ComponentName comp = new ComponentName("tv.icntv.vendor","tv.icntv.vendor.Main");
        mIntent.setComponent(comp);

        //data
        Bundle data = new Bundle();
        data.putString("action", "settings");//action为settings
        data.putString("data", "");
        mIntent.putExtras(data);

        //CM211-2,菜单键点击设置无响应
        try {
            context.startActivity(mIntent);
        }catch (Exception e){
            SuperLog.info2SDDebug(TAG,e+"");
            if (e.toString().contains("ActivityNotFoundException")){
                context.startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        }
    }

    public static List<String> getCustomNamedParameterByKey(List<NamedParameter> listNp,String key){
        if(!CollectionUtil.isEmpty(listNp) && !TextUtils.isEmpty(key)){
            for(NamedParameter np:listNp){
                if(key.equals(np.getKey())){
                    return np.getValues();
                }
            }
        }
        return null;
    }

    public static String getCustomField(List<NamedParameter> listNp, String key){
        if(!CollectionUtil.isEmpty(listNp) && !TextUtils.isEmpty(key)){
            for(NamedParameter np:listNp){
                if(key.equals(np.getKey())){
                    if(!CollectionUtil.isEmpty(np.getValues())){
                        return np.getValues().get(0);
                    }
                }
            }
        }
        return null;
    }

    public static String getVersionName(){
        String version = "UnknownVersion";
        try {
            PackageInfo pi = OTTApplication.getContext().getPackageManager().getPackageInfo(OTTApplication.getContext().getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            SuperLog.error(TAG,e);
        }
        return version;
    }

    //Debug版本去除Debug后缀
    public static String getVersionNameExcludeDebug(){
        String versionName = "";
        try {
            versionName = CommonUtil.getVersionName();
            if(BuildConfig.DEBUG){
                versionName = versionName.substring(0,versionName.indexOf(" "));
            }
        } catch (Exception e) {
            SuperLog.error(TAG,e);
        }
        return versionName;
    }

    public static int getVersionCode(){
        int version = 0;
        try {
            PackageInfo pi = OTTApplication.getContext().getPackageManager().getPackageInfo(OTTApplication.getContext().getPackageName(), 0);
            version = pi.versionCode;
            SuperLog.info2SD(TAG,"Current apk version code is : "+version);
        } catch (PackageManager.NameNotFoundException e) {
            SuperLog.error(TAG,e);
        }
        return version;
    }

    //获取设备型号,如CM201z
    public static String getDeviceType(){
        String systemModel = DeviceInfo.getSystemInfo(DEVICE_RAW);
        if (TextUtils.isEmpty(systemModel) || "unknown".equals(systemModel)) {
            systemModel = Build.MODEL;
        }
        return systemModel;
    }

    //是否是坚果投影设备
    public static boolean isJMGODevice(){
        return getDeviceType().contains("JmGO");
    }

    //
    public static boolean isE900V22EDevice(){
        return getDeviceType().contains("E900V22E");
    }

    //获取STBID
    public static String getSTBID(){
        String stbId = DeviceInfo.getSystemInfo(DEVICE_STBID);
        if (TextUtils.isEmpty(stbId) || "unknown".equals(stbId)) {
            stbId = Build.SERIAL;
        }
        return stbId;
    }

    //获取STBID
    public static String getTerminalVersion(String versionName,String systemModel){
//        //用于模拟器设备类型不对无法获取到launcher
//        if(CommonUtil.isSelfAuthenticate()){
//            return versionName+"_"+"CM201z";
//        }
        if (!TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(systemModel)){
            return versionName+"_"+systemModel;
        }else{
            return "-1";
        }
    }

    //判断activity是否在前台
    private static boolean isActivityForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (cpn.getClassName().equals(className)) {
                return true;
            }
        }

        return false;
    }

    //判断activity是否在前台,解决进入设置页面/拉起别的APK后 屏幕逻辑仍在执行的问题
    public static boolean isApkForeground() {
        //顶部activity在前台则应用在前台
        return isActivityForeground(OTTApplication.getContext(),OTTApplication.getContext().getCurrentActivity().getClass().getName());
    }

    //打印认证APK版本号,用于判断本地认证APK版本
    public static void printChinamobileAuthVersion() {
        if(CommonUtil.isSelfAuthenticate()){
            return;
        }
        String authApk = "com.chinamobile.middleware.auth";
        try{
            PackageInfo pi = OTTApplication.getContext().getPackageManager().getPackageInfo(authApk, PackageManager.GET_ACTIVITIES);
            SuperLog.info2SD(TAG,"Auth apk["+authApk+"] versionCode="+pi.versionCode+" versionName="+pi.versionName);
        } catch (Exception e){
            SuperLog.error(TAG,"Can not find auth apk, package="+authApk);
        }
    }


    private static final List<String> listSessionErrorCode = new ArrayList<>();

    public static boolean isSessionError(String errorCode){
        if(listSessionErrorCode.isEmpty()){
            listSessionErrorCode.add("125023001");
            listSessionErrorCode.add("125023015");
            listSessionErrorCode.add("125023018");
            listSessionErrorCode.add("125023020");
            listSessionErrorCode.add("125023022");
        }
        return listSessionErrorCode.contains(errorCode);
    }
}