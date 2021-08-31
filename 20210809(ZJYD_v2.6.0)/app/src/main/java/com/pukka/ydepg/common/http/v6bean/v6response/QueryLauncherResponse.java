package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;

public class QueryLauncherResponse {
    @SerializedName("launcherLink")
    private String launcherLink;

    @SerializedName("version")
    private String version;

    @SerializedName("desktopID")
    private String desktopId;

    @SerializedName("interval")
    private String interval;

    @SerializedName("batchGetResStrategyDataURL")
    private String batchGetResStrategyDataURL;

    @SerializedName("getLatestResourcesURL")
    private String getLatestResourcesURL;

    @SerializedName("currentResourcesVersion")
    private String currentResourcesVersion;

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getCurrentResourcesVersion()
    {
        return currentResourcesVersion;
    }

    public void setCurrentResourcesVersion(String currentResourcesVersion) {
        this.currentResourcesVersion = currentResourcesVersion;
    }

    public String getBatchGetResStrategyDataURL()
    {
        return batchGetResStrategyDataURL;
    }

    public void setBatchGetResStrategyDataURL(String batchGetResStrategyDataURL) {
        this.batchGetResStrategyDataURL = batchGetResStrategyDataURL;
    }

    public String getGetLatestResourcesURL()
    {
        return getLatestResourcesURL;
    }

    public void setGetLatestResourcesURL(String getLatestResourcesURL) {
        this.getLatestResourcesURL = getLatestResourcesURL;
    }

    public String getLauncherLink()
    {
        return launcherLink;
    }

    public void setLauncherLink(String launcherLink)
    {
        this.launcherLink = launcherLink;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getDesktopID() {
        return desktopId;
    }

    public void setDesktopID(String desktopId)
    {
        this.desktopId = desktopId;
    }

    public String getInterval()
    {
        return interval;
    }

    public void setInterval(String interval)
    {
        this.interval = interval;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                  .append("\tResult                     = ").append(result)
                .append("\n\tInterval                   = ").append(interval)
                .append("\n\tDesktopID                  = ").append(desktopId)
                .append("\n\tGetLatestResourcesURL      = ").append(getLatestResourcesURL)
                .append("\n\tBatchGetResStrategyDataURL = ").append(batchGetResStrategyDataURL)
                .append("\n\tVersion                    = ").append(version)
                .append("\n\tCurrentResourcesVersion    = ").append(currentResourcesVersion)
                .append("\n\tLauncherLink               = ").append(launcherLink);
        return sb.toString();
    }
}