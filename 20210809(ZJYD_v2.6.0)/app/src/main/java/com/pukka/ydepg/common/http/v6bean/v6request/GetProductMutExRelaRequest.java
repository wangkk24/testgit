package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.bean.node.PromSubInfo;
import com.pukka.ydepg.common.http.bean.node.SubProdInfo;

import java.util.Map;

public class GetProductMutExRelaRequest {
    @SerializedName("userID")
    private String userID;

    @SerializedName("subInfo")
    private SubProdInfo subInfo;

    @SerializedName("promSubInfo")
    private PromSubInfo promSubInfo;

    @SerializedName("extentionInfo")
    private Map<String,String> extentionInfo;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public SubProdInfo getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(SubProdInfo subInfo) {
        this.subInfo = subInfo;
    }

    public PromSubInfo getPromSubInfo() {
        return promSubInfo;
    }

    public void setPromSubInfo(PromSubInfo promSubInfo) {
        this.promSubInfo = promSubInfo;
    }

    public Map<String, String> getExtentionInfo() {
        return extentionInfo;
    }

    public void setExtentionInfo(Map<String, String> extentionInfo) {
        this.extentionInfo = extentionInfo;
    }
}
