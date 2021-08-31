package com.pukka.ydepg.moudule.player.node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jWX234536 on 2015/8/4.
 */
public class Still
{
    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("image")
    private String image;

    public String getThumbnail()
    {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

}
