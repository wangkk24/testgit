package com.pukka.ydepg.common.report.ubd.extension;

public class ScreenAdClickData {

    private String id;

    private String name;

    //跳转H5 url ,与PKG/CLS/EXTRA只能同时存在一个
    private String url;

    //跳转自有/第三方的页面信息,与url只能同时存在一个
    private String pkg;
    private String cls;
    private String extra;

    //[序号5][必选][不可为空]时间戳 YYYYMMDD HH24:MM:SS 即订购成功时的时间戳
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
