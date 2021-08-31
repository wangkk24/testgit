package com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.util;

import android.content.Intent;
import android.net.Uri;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.multviewforstb.multiview.TVApplication;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.ResultBean;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.http.BaseResultCall;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.http.HttpConnection;


import java.io.File;

public class UpgradeHelper {
    private static final String TAG = "[UpgradeHelper]";
    private static String deviceType = "STB_ZYSJ";
    private static String user = null;
    private static IUpgrade upgradeDelegate = null;
    private static String upgradeApkName = "Upgrade.apk";
    private static String configFileName = "UpgradeConfig.ini";
    private static String serverUrl = "http://101.91.206.13:33500/UPGRADE";

    public static void setUser(String aUser) {
        user = aUser;
    }

    public static String getUser() {
        return user;
    }

    private enum DownloadType {
        config,
        apk
    }

    public static String getDeviceType() {
        return deviceType;
    }

    public static void setDeviceType(String sdeviceType) {
        deviceType = sdeviceType;
    }

    public static void setUpgradeDelegate(IUpgrade upgradeDelegate) {
        UpgradeHelper.upgradeDelegate = upgradeDelegate;
    }

    public static IUpgrade getUpgradeDelegate() {
        return upgradeDelegate;
    }

    /**
     * 安装apk
     *
     * @param filePath
     */
    public static void installApk(String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        TVApplication.getContext().startActivity(intent);
        System.exit(0);
    }


    /**
     * 拼接config文件的下载地址
     *
     * @param domain
     */
    public static void startUpgrade(String domain) {
        String attributedUrl = Utilty.createRequestURL(Utilty.normalizeUrl(domain), (String) null);
        DebugLog.debug(TAG, new String[]{"startUpgrade domain = " + domain + ", remote filepath =" + attributedUrl});
        String[] formatResult = Utilty.formatUrl(attributedUrl);
        if (null != formatResult && formatResult.length >= 2) {
            String requestURL = formatResult[0];
            String requestBody = formatResult[1];
            if (requestURL.endsWith("/EDS/jsp/upgrade.jsp")) {
                doEdsRedirect(requestURL, requestBody);
            } else {
                downloadConfigFile(attributedUrl);
            }
        } else {
            DebugLog.debug(TAG, new String[]{"apk download url is null."});
        }
    }

    /**
     * 重定向
     *
     * @param requestURL
     * @param requestBody
     */
    private static void doEdsRedirect(String requestURL, String requestBody) {
        DebugLog.debug(TAG, new String[]{"doEdsRedirect:requestURL:" + requestURL + "  requestBody:" + requestBody});
        HttpConnection.postRequest(requestURL, requestBody, new BaseResultCall() {
            @Override
            public void onSuccess(ResultBean response) {
                super.onSuccess(response);
                // 确认是重定向的返回结果 并且 重定向的url不为空
                if (response != null && (response.getStateCode() == 302 || response.getStateCode() == 301) && response.getBody() != null) {
                    UpgradeHelper.downloadConfigFile(Utilty.createRequestURL(Utilty.normalizeUrl(response.getBody()), null));
                } else {
                    DebugLog.error(TAG, new String[]{"redirectURL is empty, so stop the service"});
                }
            }
        });
    }

    /**
     * 下载config
     */
    public static void downloadConfigFile(String url) {
        File apkFile = new File(TVApplication.getContext().getExternalFilesDir((String) null), configFileName);
        if (apkFile.exists()) {
            apkFile.delete();
        }

        String filePath = null;

        try {
            filePath = apkFile.getCanonicalPath();
        } catch (Exception var4) {
            return;
        }

        download(url, filePath, DownloadType.config);
    }

    /**
     * 下载 apk
     *
     * @param fileName
     */
    public static void downloadApk(String fileName) {
        File apkFile = new File(TVApplication.getContext().getExternalFilesDir((String) null), upgradeApkName);
        if (apkFile.exists()) {
            apkFile.delete();
        }

        String filePath = null;

        try {
            filePath = apkFile.getCanonicalPath();
        } catch (Exception var4) {
            return;
        }

        String url = Utilty.createRequestURL(Utilty.normalizeUrl(serverUrl), fileName);
        download(url, filePath, DownloadType.apk);
    }


    /**
     * 下载config 文件/apk文件
     *
     * @param url
     * @param filePath
     * @param type
     */
    private static void download(String url, final String filePath, final DownloadType type) {
        DebugLog.debug(TAG, new String[]{"download url =" + url + ", filePath=" + filePath + ", type =" + type});
        String[] formatResult = Utilty.formatUrl(url);
        if (null != formatResult && formatResult.length >= 2) {
            final String requestURL = formatResult[0];
            String requestBody = formatResult[1];
            HttpConnection.getRequest(
                    requestURL,
                    requestBody
//                    "?TYPE=STB_ZYSJ"
                    , filePath, new BaseResultCall() {
                        @Override
                        public void onSuccess(ResultBean response) {
                            super.onSuccess(response);
                            switch (type) {
                                case apk:
                                    upgradeDelegate.onApkDownloadFinished(response.getBody());
                                    break;
                                case config:
                                    UpgradeHelper.serverUrl = requestURL;
                                    upgradeDelegate.onConfigFileDownloadFinished(response.getBody());
                                    break;
                            }
                        }

                        @Override
                        public void onError(String error) {
                            super.onError(error);
                            upgradeDelegate.onRequestException(error);
                        }

                        @Override
                        public void onDownloadProgress(long totalSize, long downloadedSize) {
                            super.onDownloadProgress(totalSize, downloadedSize);
                            if (totalSize > 0L) {
                                switch (type) {
                                    case apk:
                                        upgradeDelegate.onDownloadProgress(totalSize, downloadedSize);
                                    case config:
                                }
                            }
                        }
                    });
        } else {
            DebugLog.debug(TAG, new String[]{"apk download url is null."});
            switch (type) {
                case apk:
                    upgradeDelegate.onApkDownloadFinished("");
                    break;
                case config:
                    upgradeDelegate.onConfigFileDownloadFinished("");
            }
        }
    }


    public interface IUpgrade {
        void onConfigFileDownloadFinished(String var1);

        void onApkDownloadFinished(String var1);

        void onDownloadProgress(long totalSize, long downloadSize);

        void onRequestException(String log);

    }

}
