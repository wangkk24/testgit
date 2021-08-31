package com.pukka.ydepg.common.utils.errorWindowUtil;

public class ErrorParams {
    private String token;
    private String code;
    private String desc;
    private String boxModel;
    private String boxSerial;
    private String licence;
    private String apkVersion;
    private String ip;
    private String port;
    private String ext1;
    private String ext2;
    private String ext3;

    public String getParams(){
        return "token=" + token + "&code=" + code + "&desc=" + desc
                + "&boxModel=" + boxModel + "&boxSerial=" + boxSerial
                + "&licence=" + licence + "&apkVersion=" + apkVersion + "&ip=" + ip
                + "&port=" + port + "&ext1=" + ext1 + "&ext2=" + ext2 + "&ext3=" + ext3;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token.replace("&", "\\x38");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code.replace("&", "\\x38");
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc.replace("&", "\\x38");
    }

    public String getBoxModel() {
        return boxModel;
    }

    public void setBoxModel(String boxModel) {
        this.boxModel = boxModel.replace("&", "\\x38");
    }

    public String getBoxSerial() {
        return boxSerial;
    }

    public void setBoxSerial(String boxSerial) {
        this.boxSerial = boxSerial.replace("&", "\\x38");
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence.replace("&", "\\x38");
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion.replace("&", "\\x38");
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip.replace("&", "\\x38");
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port.replace("&", "\\x38");
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1.replace("&", "\\x38");
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2.replace("&", "\\x38");
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3.replace("&", "\\x38");
    }
}
