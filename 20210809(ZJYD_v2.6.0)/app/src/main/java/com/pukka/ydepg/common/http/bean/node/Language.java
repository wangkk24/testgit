package com.pukka.ydepg.common.http.bean.node;


import com.google.gson.annotations.SerializedName;

/**
 * Created by jWX234536 on 2015/8/4.
 */
public class Language
{
    @SerializedName("ID")
    private String id;

    @SerializedName("name")
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String ID)
    {
        this.id = ID;
    }

}
