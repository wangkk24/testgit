package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ProduceZone.java
 * @author: yh
 * @date: 2017-04-21 17:29
 */

public class ProduceZone implements Serializable {


    /**
     * ID :
     * name :
     */


    /**
     *出品国家或地区。

     如果支持指定具体国家，取值为ISO3166-1双字母缩写。
     如果指定地区，取值为从1开始编号的数字。

     */
    @SerializedName("ID")
    private String ID;

    /**
     *出品国家（或地区）名称。

     当前支持的语种包括：

     English（默认，如果用户语种不在这几种内，返回该语种对应的语种）
     Chinese
     Arabic
     Hungarian
     German
     说明：
     如果系统配置的是ISO3166-1双字母缩写国家码，则根据当前的用户语种，查询ISO标准编码表获取国家名称。

     */
    @SerializedName("name")
    private String name;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
