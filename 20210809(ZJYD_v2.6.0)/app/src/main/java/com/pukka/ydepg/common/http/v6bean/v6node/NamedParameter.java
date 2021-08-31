package com.pukka.ydepg.common.http.v6bean.v6node;

import com.pukka.ydepg.common.utils.CollectionUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.huawei.chfjydvideo.common.http.v6bean.v6node.NamedParameter.java
 * @author: yh
 * @date: 2017-04-20 12:15
 */
public class NamedParameter implements Serializable{


    /**
     * key : key
     * values : ["1","2"]
     */

    @SerializedName("key")
    private String key;
    @SerializedName("values")
    private List<String> values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "NamedParameter{" +
                "key='" + key + '\'' +
                ", values=" + values +
                '}';
    }

    public NamedParameter(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public String getFistItemFromValue() {
        if (!CollectionUtil.isEmpty(values))
        {
            return values.get(0);
        }
        return null;
    }
}