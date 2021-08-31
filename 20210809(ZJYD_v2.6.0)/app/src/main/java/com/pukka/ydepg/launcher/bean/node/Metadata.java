package com.pukka.ydepg.launcher.bean.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * vsp platform metadata
 * it is the base of all vsp metadata
 *
 */

public class Metadata implements Serializable
{
    @SerializedName("extendsParam")
    private HashMap<String, Object> extendsParam = new HashMap<>();

    public Object getExtendsParam(String key)
    {
        return extendsParam.get(key);
    }

    public void putExtendsParam(String key, Object value)
    {
        this.extendsParam.put(key, value);
    }

    public void removeExtendsParam(String key)
    {
        this.extendsParam.remove(key);
    }

    @Override
    public String toString()
    {
        return "Metadata{" +
                "extendsParam=" + extendsParam +
                '}';
    }
}