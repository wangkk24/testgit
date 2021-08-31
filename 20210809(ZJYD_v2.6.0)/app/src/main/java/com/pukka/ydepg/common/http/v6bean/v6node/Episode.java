package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Episode.java
 * @author: yh
 * @date: 2017-04-21 17:34
 */

public class Episode implements Serializable {

    /**
     * VOD : VOD
     * sitcomNO : 12
     */

    /**
     * 子集VOD信息。

     */
    @SerializedName("VOD")
    private VOD VOD;

    /**
     * 表示该子集是连续剧的第几集。

     */
    @SerializedName("sitcomNO")
    private String sitcomNO;

    public Episode() {
    }

    public com.pukka.ydepg.common.http.v6bean.v6node.VOD getVOD() {
        return VOD;
    }

    public void setVOD(com.pukka.ydepg.common.http.v6bean.v6node.VOD VOD) {
        this.VOD = VOD;
    }

    public String getSitcomNO() {
        return sitcomNO;
    }

    public void setSitcomNO(String sitcomNO) {
        this.sitcomNO = sitcomNO;
    }
}


