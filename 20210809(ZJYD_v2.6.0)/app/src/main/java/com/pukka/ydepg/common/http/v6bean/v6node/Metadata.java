package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hasee on 2017/5/10.
 */

public class Metadata implements Serializable {
    /**
     *id
     */
    @SerializedName("ID")
    protected String ID;

    /**
     *名称。
     */
    @SerializedName("name")
    protected String name;

    /**
     * 海报路径。
     参考“Picture”。
     */
    @SerializedName("picture")
    protected Picture picture;

    /**
     * 频道的流派信息。
     */
    @SerializedName("genres")
    protected List<Genre> genres;

    private ElementAction elementAction;

    public ElementAction getElementAction() {
        return elementAction;
    }

    public void setElementAction(ElementAction elementAction) {
        this.elementAction = elementAction;
    }


    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

}
