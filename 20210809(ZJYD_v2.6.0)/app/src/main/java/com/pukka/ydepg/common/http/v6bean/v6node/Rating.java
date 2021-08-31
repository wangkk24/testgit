package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Rating.java
 * @author: yh
 * @date: 2017-04-21 18:21
 */

public class Rating implements Serializable{


    /**
     * ID : 123
     * code :
     * name :
     */

    /**
     * 级别ID，一般按照Age定义。

     */
    @SerializedName("ID")
    private String ID;

    /**
     * 级别外键。

     */
    @SerializedName("code")
    private String code;

    /**
     * 级别名称。

     */
    @SerializedName("name")
    private String name;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
