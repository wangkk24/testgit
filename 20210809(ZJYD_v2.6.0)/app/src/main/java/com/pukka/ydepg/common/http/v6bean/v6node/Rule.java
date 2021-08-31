package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Rule.java
 * @author: yh
 * @date: 2017-04-21 17:31
 */

public class Rule {


    /**
     * type : 1423
     * values : [""]
     */

    /**
     *规则类型，取值包括：

     0：包含
     1：排他
     */
    @SerializedName("type")
    private String type;

    /**
     *规则取值。

     */
    @SerializedName("values")
    private List<String> values;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
