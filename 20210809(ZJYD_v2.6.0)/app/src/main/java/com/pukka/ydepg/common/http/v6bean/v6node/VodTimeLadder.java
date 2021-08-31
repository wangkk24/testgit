package com.pukka.ydepg.common.http.v6bean.v6node;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VodTimeLadder implements Serializable {
    @SerializedName("elapseTime")
    private String elapseTime;

    @SerializedName("previewduration")
    private String previewduration;

    public int getElapseTime() {
        if(!TextUtils.isEmpty(elapseTime)){
            return Integer.parseInt(elapseTime);
        }
        return 0;
    }

    public void setElapseTime(String elapseTime) {
        this.elapseTime = elapseTime;
    }

    public int getPreviewduration() {
        if(!TextUtils.isEmpty(previewduration)){
            return Integer.parseInt(previewduration);
        }
        return 0;
    }

    public void setPreviewduration(String previewduration) {
        this.previewduration = previewduration;
    }
}
