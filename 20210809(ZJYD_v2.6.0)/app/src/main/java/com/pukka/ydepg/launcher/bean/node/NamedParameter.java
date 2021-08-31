package com.pukka.ydepg.launcher.bean.node;

import com.pukka.ydepg.common.utils.CollectionUtil;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * the Metadata of NamedParameter
 *
 */
public class NamedParameter extends Metadata {

    @SerializedName("key")
    private String key;

    @SerializedName("values")
    private List<String> valueList;

    public NamedParameter() { }

    public NamedParameter(String key, List<String> valueList) {
        this.key = key;
        this.valueList = valueList;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public List<String> getValueList()
    {
        return valueList;
    }

    public void setValueList(List<String> valueList)
    {
        this.valueList = valueList;
    }

    public String getFistItemFromValue() {
        if (!CollectionUtil.isEmpty(valueList)) {
            return valueList.get(0);
        }
        return null;
    }

    @Override
    public String toString() {
        return "MemNamedParameter{" +
                "key='" + key + '\'' +
                ", value='" + valueList.toString() + '\'' +
                '}';
    }
}