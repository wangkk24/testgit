package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * the Metadata of VAS
 *
 */
public class VAS
{
    /**
     * field:id
     */
    @SerializedName("ID")
    private String id;


    /**
     * field:name
     */
    @SerializedName("name")
    private String name;

    /**
     * field:vasPicture
     */
    @SerializedName("picture")
    private Picture picture;

    /**
     * field:favorite
     */
    @SerializedName("favorite")
    private Favorite favorite;

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

    public Picture getPicture()
    {
        return picture;
    }

    public void setPicture(Picture picture)
    {
        this.picture = picture;
    }

    public Favorite getFavorite()
    {
        return favorite;
    }

    public void setFavorite(Favorite favorite)
    {
        this.favorite = favorite;
    }
}