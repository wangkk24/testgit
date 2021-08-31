package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/24.
 */

public class MultiRightsPlayready {

    /**
     * LA_URL :
     * LUI_URL :
     */

    /**
     * client请求获取license的地址
     */
    @SerializedName("LA_URL")
    private String LA_URL;

    /**
     * URL of the PlayReady portal. This parameter is not used currently.
     */
    @SerializedName("LUI_URL")
    private String LUI_URL;

    public String getLA_URL() {
        return LA_URL;
    }

    public void setLA_URL(String LA_URL) {
        this.LA_URL = LA_URL;
    }

    public String getLUI_URL() {
        return LUI_URL;
    }

    public void setLUI_URL(String LUI_URL) {
        this.LUI_URL = LUI_URL;
    }
}
