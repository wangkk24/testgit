package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryUserAttrsResponse;

import java.util.ArrayList;
import java.util.List;

public class ModifyUserAttrRequest {

    //用户号（浙江移动视频为手机号码）
    @SerializedName("userId")
    private String userId;

    /**
     * 变更的自定义用户属性
     * actionType=0时，如果attrValue为空则更新为空
     * actionType=1时，attrValue传参无效
     * */
    @SerializedName("modifyAttr")
    private QueryUserAttrsResponse.UserAttr modifyAttr;

    /**
     * 动作类型
     * 0：新增/变更
     * 1：删除
     * */
    @SerializedName("actionType")
    private String actionType;

    //操作流水号
    @SerializedName("sequenceID")
    private String sequenceID = "000011000000081709121026599609";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public QueryUserAttrsResponse.UserAttr getModifyAttr() {
        return modifyAttr;
    }

    public void setModifyAttr(QueryUserAttrsResponse.UserAttr modifyAttr) {
        this.modifyAttr = modifyAttr;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }
}
