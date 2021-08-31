package com.pukka.ydepg.common.http.vss.response;

import com.google.gson.annotations.SerializedName;

public class VSSBaseResponse {
    /**
     * 返回码
     */
    @SerializedName("code")
    protected String code;
    /**
     * 返回描述
     */
    @SerializedName("description")
    protected String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
