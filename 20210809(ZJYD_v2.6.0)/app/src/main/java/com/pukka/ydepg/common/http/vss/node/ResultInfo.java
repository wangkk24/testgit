package com.pukka.ydepg.common.http.vss.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultInfo implements Serializable {

    private static final long serialVersionUID = 601521996754035314L;

    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("description")
    private String description;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
