package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

public class CustomGroup {

    /**
     *标签（客户群）名称
     */
    @SerializedName("labelName")
    private String labelName;

    /**
     *客户群归属
     * 1代表属于该客户群，0代表不属于该客户群
     */
    @SerializedName("labelValue")
    private String labelValue;

    /**
     *客户群id
     */
    @SerializedName("groupId")
    private String groupId;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
