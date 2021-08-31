package com.pukka.ydepg.common.report.ubd.extension;

public class ScreenAdImpressionData {

    private String id;

    private String name;

    private String fromActivity;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromActivity() {
        return fromActivity;
    }

    public void setFromActivity(String fromActivity) {
        this.fromActivity = fromActivity;
    }
}
