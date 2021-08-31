package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QueryUserAttrsRequest {

    //用户号（浙江移动视频为手机号码）
    @SerializedName("userId")
    private String userId;

    //操作流水号
    @SerializedName("sequenceID")
    private String sequenceID = "000011000000081709121026599609";

    //LIST为0时查询该ID下所有的attr，最大查询5个attrName
    @SerializedName("attrNames")
    private List<String> attrNames = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }

    public List<String> getAttrNames() {
        return attrNames;
    }

    public void setAttrNames(List<String> attrNames) {
        this.attrNames = attrNames;
    }
}
