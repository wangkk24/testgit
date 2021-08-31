package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlaybillExcluder.java
 * @author: yh
 * @date: 2017-04-25 09:53
 */

public class PlaybillExcluder implements Serializable {

    /**
     *出品国家。

     */
    @SerializedName("countries")
    private List<String> countries;


    /**
     *节目单ID。

     */
    @SerializedName("playbillIDs")
    private List<String> playbillIDs;

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getPlaybillIDs() {
        return playbillIDs;
    }

    public void setPlaybillIDs(List<String> playbillIDs) {
        this.playbillIDs = playbillIDs;
    }
}
