package com.pukka.ydepg.common.upgrade.data;

import java.io.Serializable;

public class UpgradeConfig implements Serializable {

    private String version;

    private String fileName;

    private String downloadAddress;

    private String forceUpgrade;

    private String desc;

    private String lastForceUpdateVersion;



    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public String getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(String forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLastForceUpdateVersion() {
        return lastForceUpdateVersion;
    }

    public void setLastForceUpdateVersion(String lastForceUpdateVersion) {
        this.lastForceUpdateVersion = lastForceUpdateVersion;
    }
}
