package com.pukka.ydepg.common.http.v6bean.v6node;


import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/3/14.
 */

public class Dialect
{
    @SerializedName("language")
    private String language;

    @SerializedName("value")
    private String value;

    @SerializedName("image")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
