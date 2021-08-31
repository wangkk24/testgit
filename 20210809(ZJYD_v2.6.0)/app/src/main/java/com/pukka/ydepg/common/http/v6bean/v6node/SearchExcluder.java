package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: SearchExcluder.java
 * @author: yh
 * @date: 2017-04-21 17:45
 */

public class SearchExcluder {

    /**
     * genreName :
     * country :
     * ratingID :
     * subjectID : [""]
     */

    /**
     *流派名称。

     */
    @SerializedName("genreName")
    private String genreName;

    /**
     *出品国家。

     */
    @SerializedName("country")
    private String country;

    /**
     *父母字级别ID。

     */
    @SerializedName("ratingID")
    private String ratingID;

	public String[] getListCpId()
	{
		return listCpId;
	}

	public void setListCpId(String[] listCpId)
	{
		this.listCpId = listCpId;
	}

	/**
     * 内容CPID
     */
    @SerializedName("cpIDList")
    private String[] listCpId;

    /**
     *栏目ID。

     */
    @SerializedName("subjectID")
    private List<String> subjectID;

    private int subSitcomSearchFlag;

    public int getSubSitcomSearchFlag() {
        return subSitcomSearchFlag;
    }

    public void setSubSitcomSearchFlag(int subSitcomSearchFlag) {
        this.subSitcomSearchFlag = subSitcomSearchFlag;
    }

    public SearchExcluder() {
    }


    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }

    public List<String> getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(List<String> subjectID) {
        this.subjectID = subjectID;
    }
}
