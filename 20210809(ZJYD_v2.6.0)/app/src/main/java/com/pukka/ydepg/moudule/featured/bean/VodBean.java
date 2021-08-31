package com.pukka.ydepg.moudule.featured.bean;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Bookmark;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 公司:广州易杰数码
 * Created by xujie on 2016/11/3.
 * used:
 */
public class VodBean {
    /**
     * The vod id
     */
    private String id;


    public List<String> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<String> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public List<String> getHcsSlaveAddressList() {
        return hcsSlaveAddressList;
    }

    public void setHcsSlaveAddressList(List<String> hcsSlaveAddressList) {
        this.hcsSlaveAddressList = hcsSlaveAddressList;
    }

    private List<String> subjectIds;

    /**
     * The vod name
     */
    private String name;

    /**
     * The vod average score
     */
    private String rate;

    /**
     * The path of the vod's poster
     */
    private String poster;

    /**
     * Whether it is HD or not
     */
    private boolean isHD;


    private String tendency;

    private String cpId;
    /**
     * 20180318
     * bug 我的页面视频没有标志画质
     */
    private String definition;

    private List<String> hcsSlaveAddressList;

    /**
     *VOD的扩展属性，其中扩展属性的Key由局点CMS定制。

     */
    @SerializedName("customFields")
    protected List<NamedParameter> customFields;

    /**
     *VOD类型。

     取值范围：

     0：非电视剧
     1：普通连续剧
     2：季播剧父集
     3：季播剧
     */
    protected String VODType;

    //书签
    private Bookmark bookmark;

    public Bookmark getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean isHD() {
        return isHD;
    }

    public void setHD(boolean HD) {
        isHD = HD;
    }

    public String getTendency() {
        return tendency;
    }

    public void setTendency(String tendency) {
        this.tendency = tendency;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public List<String> getHCSSlaveAddressList() {
        return hcsSlaveAddressList;
    }

    public void setHCSSlaveAddressList(List<String> hcsSlaveAddressList) {
        this.hcsSlaveAddressList = hcsSlaveAddressList;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getVODType() {
        return VODType;
    }

    public void setVODType(String VODType) {
        this.VODType = VODType;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }
}
