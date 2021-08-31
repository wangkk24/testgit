package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: VODExcluder.java
 * @author: yh
 * @date: 2017-04-21 16:37
 */

public class VODExcluder {


    /**
     * produceZoneIDs : [""]
     * subscriptionTypes : [123]
     * produceYears : [""]
     * genreIDs : [222]
     * supersetType : 123
     */

    /**
     *出产国家或者地区。

     */
    @SerializedName("supersetType")
    private String supersetType;

	public String getCpId()
	{
		return cpId;
	}

	public void setCpId(String cpId)
	{
		this.cpId = cpId;
	}

	/**
     * VOD CPID
     */
    @SerializedName("cpId")
    private String cpId;

    /**
     *订购方式，取值如下：

     -1：所有
     0：包周期订购
     1：按次订购
     默认值是-1。

     */
    @SerializedName("produceZoneIDs")
    private List<String> produceZoneIDs;

    /**
     *发布年份。

     例如：produceYear传入了2011,2010，即查询出品日期是2011和2010的VOD。

     */
    @SerializedName("subscriptionTypes")
    private List<String> subscriptionTypes;

    /**
     *流派ID。

     */
    @SerializedName("produceYears")
    private List<String> produceYears;

    /**
     *VOD父集类型。

     1：普通连续剧父集
     2：季播剧父集
     如果不传，表示不做过滤。

     */
    @SerializedName("genreIDs")
    private List<String> genreIDs;

    public String getSupersetType() {
        return supersetType;
    }

    public void setSupersetType(String supersetType) {
        this.supersetType = supersetType;
    }

    public List<String> getProduceZoneIDs() {
        return produceZoneIDs;
    }

    public void setProduceZoneIDs(List<String> produceZoneIDs) {
        this.produceZoneIDs = produceZoneIDs;
    }

    public List<String> getSubscriptionTypes() {
        return subscriptionTypes;
    }

    public void setSubscriptionTypes(List<String> subscriptionTypes) {
        this.subscriptionTypes = subscriptionTypes;
    }

    public List<String> getProduceYears() {
        return produceYears;
    }

    public void setProduceYears(List<String> produceYears) {
        this.produceYears = produceYears;
    }

    public List<String> getGenreIDs() {
        return genreIDs;
    }

    public void setGenreIDs(List<String> genreIDs) {
        this.genreIDs = genreIDs;
    }
}
