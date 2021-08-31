package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.DesktopInfos;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;

import java.util.List;
import java.util.Map;

public class QueryPHMLauncherListResponse {



    //终端周期性到PHMServer获刷最新桌面布局配置文件的最短时间间隔，单位为秒。如果终端不需要周期到平台获取最新的桌面布局配置文件，则不需要使用该参数。
    @SerializedName("interval")
    private Integer interval;

    //桌面配置文件下载等信息。
    @SerializedName("desktopInfos")
    private List<DesktopInfos> desktopInfos;

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public List<DesktopInfos> getDesktopInfos() {
        return desktopInfos;
    }

    public void setDesktopInfos(List<DesktopInfos> desktopInfos) {
        this.desktopInfos = desktopInfos;
    }
}