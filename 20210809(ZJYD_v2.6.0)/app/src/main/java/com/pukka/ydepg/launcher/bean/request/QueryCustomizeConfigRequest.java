package com.pukka.ydepg.launcher.bean.request;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;


/**
 * QueryCustomizeConfig request body
 */
public class QueryCustomizeConfigRequest {
    public QueryCustomizeConfigRequest() {

    }

    public QueryCustomizeConfigRequest(String... queryType) {
        if (null != queryType) {
            StringBuffer buffer = new StringBuffer();
            for (String type : queryType) {
                buffer.append(type);
                buffer.append(",");
            }
            String bufferString = buffer.toString();
            if (!TextUtils.isEmpty(bufferString)) {
                this.queryType = bufferString.substring(0, bufferString.length() - 1);
            }
        }
    }

    /**
     * field:key
     */
    @SerializedName("key")
    private String key;

    /**
     * field:key
     */
    @SerializedName("queryType")
    private String queryType;

    /**
     * field:isFuzzyQuery
     */
    @SerializedName("isFuzzyQuery")
    private String isFuzzyQuery;

    /**
     * field:deviceModel
     */
    @SerializedName("deviceModel")
    private String deviceModel;

    /**
     * field:deviceModel
     */
    @SerializedName("language")
    private String language;



    /**
     * field:deviceModel
     */
    @SerializedName("standard")
    private String standard;


    public String getKey() {
        return key;
    }

    public QueryCustomizeConfigRequest setKey(String key) {
        this.key = key;
        return this;
    }

    public String getQueryType() {
        return queryType;
    }

    public QueryCustomizeConfigRequest setQueryType(String queryType) {
        this.queryType = queryType;
        return this;
    }

    public String getIsFuzzyQuery() {
        return isFuzzyQuery;
    }

    public QueryCustomizeConfigRequest setIsFuzzyQuery(String isFuzzyQuery) {
        this.isFuzzyQuery = isFuzzyQuery;
        return this;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public QueryCustomizeConfigRequest setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public QueryCustomizeConfigRequest setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getStandard() {
        return standard;
    }

    public QueryCustomizeConfigRequest setStandard(String standard) {
        this.standard = standard;
        return this;
    }


    @Override
    public String toString() {
        return "QueryCustomizeConfigRequest{" +
                "key='" + key + '\'' +
                ", queryType='" + queryType + '\'' +
                ", isFuzzyQuery='" + isFuzzyQuery + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", language='" + language + '\'' +
                ", standard='" + standard + '\'' +
                '}';
    }
}