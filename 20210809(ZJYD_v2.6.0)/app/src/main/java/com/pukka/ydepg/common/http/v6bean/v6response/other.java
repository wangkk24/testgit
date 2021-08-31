package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：panjw on 2021/5/7 10:22
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class other {
    @SerializedName("stringURL")
    private String stringURL;

    public String getStringURL() {
        return stringURL;
    }

    public void setStringURL(String stringURL) {
        this.stringURL = stringURL;
    }
}
