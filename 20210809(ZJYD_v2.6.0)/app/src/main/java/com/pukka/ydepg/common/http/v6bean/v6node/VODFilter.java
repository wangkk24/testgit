package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: VODFilter.java
 * @author: yh
 * @date: 2017-04-21 17:19
 */

public class VODFilter {


    /**
     * produceZoneIDs : [""]
     * subscriptionTypes : [1212]
     * produceYears : [""]
     * genreIDs : [11]
     * initials : [""]
     * languages : [""]
     * cpId :
     */

    /**
     *点播内容归属的CP ID,多个cpId以","分割

     */
    @SerializedName("cpId")
    private String cpId;

    /**
     *出产国家或者地区。
     。

     */
    @SerializedName("produceZoneIDs")
    private List<String> produceZoneIDs;

    /**
     *订购方式，取值如下：

     -1：所有
     0：已订购且被定价为包周期产品
     1：已订购且按定价为按次产品
     2：未订购
     3：未订购且被定价为按次产品
     4：未订购且被定价为包周期产品
     默认值是-1。

     */
    @SerializedName("subscriptionTypes")
    private List<String> subscriptionTypes;

    /**
     *发布年份。

     例如：produceYear传入了2011,2010，即查询出品日期是2011和2010的VOD。

     */
    @SerializedName("produceYears")
    private List<String> produceYears;

    /**
     *流派ID。

     */
    @SerializedName("genreIDs")
    private List<String> genreIDs;

    /**
     *对内容名称首字母进行过滤，取值对应用户语系的字母和0-9的10个数字。

     */
    @SerializedName("initials")
    private List<String> initials;

    /**
     *VOD支持的语种，为语言的ISO 639-1双字节缩写。比如en、zh

     */
    @SerializedName("languages")
    private List<String> languages;

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
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

    public List<String> getInitials() {
        return initials;
    }

    public void setInitials(List<String> initials) {
        this.initials = initials;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
