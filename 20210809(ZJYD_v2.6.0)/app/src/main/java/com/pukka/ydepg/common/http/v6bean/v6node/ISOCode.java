package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ISOCode.java
 * @author: yh
 * @date: 2017-04-21 18:00
 */

public class ISOCode implements Serializable {


    /**
     * shortName :
     * fullName :
     */

    /**
     *ISO编码。

     对ISO 639-1，为小写的双字母英文缩写；
     对于ISO 3166-1 alpha-2，为大写的双字母英文缩写。
     对于ISO 639-2或ISO 639-3，为小写的三字母英文缩写。
     */
    @SerializedName("shortName")
    private String shortName;


    /**
     *编码对应的语种或国家名称。

     */
    @SerializedName("fullName")
    private String fullName;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
