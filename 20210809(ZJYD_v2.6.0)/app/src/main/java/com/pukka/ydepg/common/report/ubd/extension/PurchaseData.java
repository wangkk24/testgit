package com.pukka.ydepg.common.report.ubd.extension;

public class PurchaseData {

    public final static String SUCCESS = "0";

    public final static String FAIL    = "1";

    public final static String SOURCE_EPG = "0";

    public final static String SOURCE_H5 = "1";


    //[序号1][必选][不可为空]TV用户的标识,TV用户的标识
    private String userID;

    //[序号2][必选][不可为空]内容ID,内容的标识,引发产品包订购的内容ID
    private String contentID;

    //[序号3][必选][不可为空]产品包ID,产品包的标识
    private String packageID;

    //[序号4][必选][不可为空]订购是否成功 0：成功 1：失败
    private String isSubscriptionSucceed;

    //[序号5][必选][不可为空]时间戳 YYYYMMDD HH24:MM:SS 即订购成功时的时间戳
    private String timestamp;

    //[序号6][可选][可以为空]扩展字段,普通话单0 特殊话单1
    private String extraOne = "0";

    //[序号7][必选]来源 0代表来自于EPG，1代表来自于H5
    private String source;

    //[序号8][可选]渠道吗，当来源是H5时需上报
    private String srcch;

    //[序号8][可选]页面名，当来源是H5时需上报
    private String pageName;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public String getIsSubscriptionSucceed() {
        return isSubscriptionSucceed;
    }

    public void setIsSubscriptionSucceed(String isSubscriptionSucceed) {
        this.isSubscriptionSucceed = isSubscriptionSucceed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getExtraOne() {
        return extraOne;
    }

    public void setExtraOne(String extraOne) {
        this.extraOne = extraOne;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSrcch() {
        return srcch;
    }

    public void setSrcch(String srcch) {
        this.srcch = srcch;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}