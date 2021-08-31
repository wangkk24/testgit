package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean;

import android.text.TextUtils;

import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.VRPlayerApplictaion;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.util.ApplicationUtil;


/**
 * Created by yanhao
 * Time:2019/9/24
 * Description:升级返回的数据
 */
public class UpdateBean {

    private static final String TAG = "UpdateBean";

    private String version;
    private String fileName;
    private String forceUpdate;
    private String lastForceUpgradeVersion;
    private String appStoreUrl;

    public String getVersion() {
        return version;
    }

    public String getFileName() {
        return fileName;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public String getLastForceUpgradeVersion() {
        return lastForceUpgradeVersion;
    }

    public String getAppStoreUrl() {
        return appStoreUrl;
    }

    /**
     * 转换数据为bean
     *
     * @param responseBody [UPDATEAPP]
     *                     Version=1668
     *                     FileName=VR_V600R020C00_Pico_20190729_zh.apk
     *                     LastForceUpgradeVersion=0
     *                     ForceUpdate=0
     *                     AppStoreUrl=http://101.91.206.13:33500/UPGRADE/jsp/upgrade.jsp
     * @return
     */
    public static UpdateBean getDataBean(String responseBody) {
        UpdateBean updateBean = new UpdateBean();
        String[] split = responseBody.split("\r\n");
        for (int i = 0; i < split.length; i++) {
            String data = split[i];
            int index = data.indexOf('=');
            if (index >= 0) {
                String key = data.substring(0, index);
                String value = data.substring(index + 1);
                if (key.equals("LastForceUpgradeVersion")) {
                    updateBean.lastForceUpgradeVersion = value;
                } else if (key.equals("Version")) {
                    updateBean.version = value;
                } else if (key.equals("FileName")) {
                    updateBean.fileName = value;
                } else if (key.equals("ForceUpdate")) {
                    updateBean.forceUpdate = value;
                } else if (key.equals("AppStoreUrl")) {
                    updateBean.appStoreUrl = value;
                }
            }
        }
        return updateBean;
    }

    public boolean hasUpdate() {
        if (null != VRPlayerApplictaion.getApplication().getUpdateBean()) {
            String version = VRPlayerApplictaion.getApplication().getUpdateBean().getVersion().trim();
            try {
                int serverVer = Integer.parseInt(version);
                if (serverVer > ApplicationUtil
                        .getCurrentAppVersionCode()) {
                    //代表有更新
                    return true;
                }
            } catch (Exception e) {
                DebugLog.error(TAG, "[refreshUpdate] Exception=" + e);
            }
        }
        return false;
    }

    /**
     * 是否是强制升级
     *
     * @return
     */
    public boolean isForceUpdate() {

        // Gets whether mandatory upgrade configuration items; "true" or "false"
        // And IOS consistent and aligned and po + 3, this parameter should be configured to
        // "0" or "1"
        boolean force = "1".equals(forceUpdate);
        //基线的逻辑
        if (!force && !TextUtils.isEmpty(lastForceUpgradeVersion) && Integer
                .parseInt(lastForceUpgradeVersion) > ApplicationUtil
                .getCurrentAppVersionCode()) {
            force = true;
        }
        return force;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "version='" + version + '\'' +
                ", fileName='" + fileName + '\'' +
                ", forceUpdate='" + forceUpdate + '\'' +
                ", lastForceUpgradeVersion='" + lastForceUpgradeVersion + '\'' +
                ", appStoreUrl='" + appStoreUrl + '\'' +
                '}';
    }
}
