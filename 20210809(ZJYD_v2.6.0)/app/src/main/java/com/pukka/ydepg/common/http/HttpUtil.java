package com.pukka.ydepg.common.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.webkit.WebView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.session.SessionService;

public class HttpUtil {
    public static String getVspUrl(String interfaceName){
        return ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3
                + interfaceName;
    }

    public static String getVssUrl(String interfaceName){
        return ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL()
                + HttpConstant.HTTPS_VSP_IP_VSS_PORT_VSP
                + interfaceName;
    }

    public static String addUserVODListFilter(){
        String userVODListFilter = SessionService.getInstance().getSession().getUserVODListFilter();
        if(TextUtils.isEmpty(userVODListFilter)){
            return "";
        } else {
            return "?userVODListFilter="+userVODListFilter;
        }
    }

    //0:无网络连接 1：WIFI 2:WIRED
    public static int getNetworkStatus(){
        ConnectivityManager manager = (ConnectivityManager) OTTApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    //SuperLog.info2SD(TAG, "WiFi network connected.");
                    return 1;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    //SuperLog.info2SD(TAG, "Wired network connected.");
                    return 2;
                } else {
                    //SuperLog.info2SD(TAG, "Neither wired nor wifi network connected.");
                    return 0;
                }
            } else {
                SuperLog.error(Constant.TAG, "Network is disconnected!");
                return 0;
            }

        } else {   // not connected to the internet
            SuperLog.error(Constant.TAG, "NetworkInfo is null, can not get network status.");
            return 0;
        }
    }

    private static String SSP_USER_AGENT = null;
    //获取向SSP平台发送请求时的Http头中User-Agent,样例如下
    //Mozilla/5.0 (Linux; Android 5.1.1; CM201z Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Safari/537.36
    //https://blog.csdn.net/zheng_weichao/article/details/79242125
    public static String getSspUserAgent(){
        if(SSP_USER_AGENT == null){
            SSP_USER_AGENT = new WebView(OTTApplication.getContext()).getSettings().getUserAgentString();
            if(TextUtils.isEmpty(SSP_USER_AGENT)){
                SSP_USER_AGENT = "okHttp/AndroidSTB";
            }
        }
        return SSP_USER_AGENT;
    }
    public static String getCookie()
    {
        if (!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getSeesionId())) {
        String jsessionid = SharedPreferenceUtil.getInstance().getSeesionId();

        if (!jsessionid.contains("JSESSIONID=")) {
            jsessionid = "JSESSIONID=" + jsessionid;
        }

        String csrfsession = SessionService.getInstance().getSession().getCookie();
        if (!TextUtils.isEmpty(csrfsession)) {
            jsessionid = jsessionid + "; " + csrfsession;
        }

        String xCSRFToken = SessionService.getInstance().getSession().getCSRFToken();
        if (!TextUtils.isEmpty(xCSRFToken)) {
            jsessionid = jsessionid + "; " + xCSRFToken;
        }
        return jsessionid;
    }
        return "";
    }
}
