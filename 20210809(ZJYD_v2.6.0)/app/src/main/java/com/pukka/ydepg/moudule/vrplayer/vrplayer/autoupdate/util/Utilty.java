package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.VRPlayerApplictaion;

import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Utilty {
    private static final String TAG = Utilty.class.getSimpleName();


    static String[] formatUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            DebugLog.error(TAG, new String[]{"url is null"});
            return null;
        } else {
            int index = url.indexOf("?");
            if (-1 != index) {
                String[] result = new String[]{url.substring(0, index), url.substring(index + 1)};
                return result;
            } else {
                return null;
            }
        }
    }

    public static String normalizeUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        } else {
            try {
                URL myurl = new URL(url);
                URI uri = new URI(myurl.getPath());
                String normalUri = uri.normalize().toString();
                String oldUri = uri.toString();
                if (normalUri.compareTo(oldUri) == 0) {
                    return url;
                } else {
                    DebugLog.error(TAG, new String[]{"oldUri = " + oldUri + ", securityUri =" + normalUri});
                    return url.replaceFirst(oldUri, normalUri);
                }
            } catch (Exception var5) {
                DebugLog.error(TAG, new String[]{"normalizeUrl Exception =" + var5});
                return "";
            }
        }
    }

    static String createRequestURL(String domain, String fileName) {
        StringBuilder urlBuilder = new StringBuilder(domain);
        if (!domain.endsWith("/jsp/upgrade.jsp")) {
            urlBuilder.append("/jsp/upgrade.jsp");
        }

        urlBuilder.append("?TYPE=" + UpgradeHelper.getDeviceType());
        urlBuilder.append("&VER=").append(getVersionCode());
        if (UpgradeHelper.getUser() != null) {
            urlBuilder.append("&USER=" + UpgradeHelper.getUser());
        }

        if (fileName != null) {
            urlBuilder.append("&FILENAME=").append(fileName);
        }

        if (!urlBuilder.toString().contains("/EDS/")) {
            urlBuilder.append("&CHECKSUM=").append(getAsciiTotalValue(urlBuilder.toString()));
        }

        return urlBuilder.toString();
    }

    private static int getAsciiTotalValue(String value) {
        if (null != value && value.length() != 0) {
            int totalValue = 0;
            char[] charValue = value.toCharArray();
            char[] var3 = charValue;
            int var4 = charValue.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                char assicNum = var3[var5];
                totalValue += assicNum;
            }

            return totalValue + 1;
        } else {
            return 1;
        }
    }

    /**
     * 忽略https的安全证书验证
     */
//    public static void handleSSLHandshake() {
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                }
//            }};
//
//            SSLContext sc = SSLContext.getInstance("TLS");
//            // trustAllCerts信任所有的证书
//            sc.init(null, trustAllCerts, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return hostname.equalsIgnoreCase(session.getPeerHost());
//                }
//            });
//        } catch (Exception ignored) {
//        }
//    }

    /**
     * 获取当前版本号
     * @return
     */
    public static String getVersionCode() {
        PackageManager manager = VRPlayerApplictaion.getContext().getApplicationContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(VRPlayerApplictaion.getContext().getPackageName(), 0);
            return String.valueOf(info.versionCode);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

}
