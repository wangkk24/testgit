package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * the Metadata of LockSeries
 *
 */
public class LockSeries{
    /**
     * field :ID
     */
    @SerializedName("ID")
    private String id;

    /**
     * field :name
     */
    @SerializedName("name")
    private String name;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "LockSeries{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}