package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlaybillFilter.java
 * @author: yh
 * @date: 2017-04-25 09:51
 */

public class PlaybillFilter implements Serializable {


    /**
     *对内容名称首字母进行过滤，取值对应用户语系的字母和0-9的10个数字。

     */
    @SerializedName("initials")
    private List<String> initials;

    /**
     *根据节目单的lifetimeID查询节目单。

     */
    @SerializedName("lifetimeIDs")
    private List<String> lifetimeIDs;

    /**
     *出品国家。

     */
    @SerializedName("countries")
    private List<String> countries;

    /**
     *流派ID。

     */
    @SerializedName("genres")
    private List<String> genres;

    public List<String> getInitials() {
        return initials;
    }

    public void setInitials(List<String> initials) {
        this.initials = initials;
    }

    public List<String> getLifetimeIDs() {
        return lifetimeIDs;
    }

    public void setLifetimeIDs(List<String> lifetimeIDs) {
        this.lifetimeIDs = lifetimeIDs;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
