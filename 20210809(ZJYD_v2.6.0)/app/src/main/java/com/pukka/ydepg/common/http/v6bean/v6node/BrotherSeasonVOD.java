package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: BrotherSeasonVOD.java
 * @author: yh
 * @date: 2017-04-24 12:46
 */

public class BrotherSeasonVOD implements Serializable {


    /**
     * VOD : 支持格式化高亮折叠
     * sitcomNO : 123
     */

    /**
     * 季播剧。

     */
    @SerializedName("VOD")
    private VOD VOD;

    /**
     * 该季播剧是连续剧的第几季。

     */
    @SerializedName("sitcomNO")
    private String sitcomNO;

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
