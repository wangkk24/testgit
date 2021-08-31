package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Sitcom.java
 * @author: yh
 * @date: 2017-04-21 17:25
 */

public class Sitcom implements Serializable {

    /**
     * VODID :
     * sitcomNO : 2122
     */

    /**
     *父集VOD的ID。

     */
    @SerializedName("VODID")
    private String VODID;
    /**
     *该VOD在父集中的子集号。

     */
    @SerializedName("sitcomNO")
    private String sitcomNO;

    public String getVODID() {
        return VODID;
    }

    public void setVODID(String VODID) {
        this.VODID = VODID;
    }

    public String getSitcomNO() {
        return sitcomNO;
    }

    public void setSitcomNO(String sitcomNO) {
        this.sitcomNO = sitcomNO;
    }
}
