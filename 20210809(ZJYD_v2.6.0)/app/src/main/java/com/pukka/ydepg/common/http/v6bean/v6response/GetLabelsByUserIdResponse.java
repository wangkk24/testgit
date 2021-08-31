package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.CustomGroup;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V2.2 <描述当前版本功能>
 * @FileName: GetLabelsByUserIdResponse.java
 * @author: weicy
 * @date: 2020-03-20 15:05
 */

public class GetLabelsByUserIdResponse {

    @SerializedName("customGroup")
    private List<CustomGroup> customGroups;

    /**
     *客户群
     */
    public List<CustomGroup> getCustomGroups() {
        return customGroups;
    }

    public void setCustomGroups(List<CustomGroup> customGroups) {
        this.customGroups = customGroups;
    }
}
