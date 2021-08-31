package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;
import com.pukka.ydepg.launcher.bean.node.Topic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eason on 19-Jun-20.
 */

public class Recommend implements Serializable{
    @SerializedName("appointedId")
    private String appointedId;

    @SerializedName("totalCount")
    private Integer totalCount;

    @SerializedName("count")
    private Integer count;

    @SerializedName("offset")
    private Integer offset;

    @SerializedName("identifyType")
    private Integer identifyType;

    @SerializedName("sceneId")
    private String sceneId;

    @SerializedName("posterURLCloudTv")
    private String posterURLCloudTv;

    @SerializedName("jtdataType")
    private String jtdataType;

    @SerializedName("display_tracker")
    private String display_tracker;

    @SerializedName("VODs")
    private List<VOD> VODs;

    @SerializedName("topics")
    private List<Topic> topics;

    @SerializedName("apks")
    private List<Apk> apks;

    /**
     * =0:VOD
     * =1/2:Topic H5/专题
     * =3：Apk EPG内部界面和三方apk
     * =4:和彩云
     * */
    @SerializedName("sceneType")
    private String sceneType;

    @SerializedName("contentType")
    private String contentType;

    /**
     * 开机动画的图片下载地址
     */
    @SerializedName("url")
    private String url;
    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;



    /**
     *
     * 栏目id
     */
    @SerializedName("others")
    private List<other> other;

    public String getJtdataType() {
        return jtdataType;
    }

    public void setJtdataType(String jtdataType) {
        this.jtdataType = jtdataType;
    }

    public String getDisplay_tracker() {
        return display_tracker;
    }

    public void setDisplay_tracker(String display_tracker) {
        this.display_tracker = display_tracker;
    }

    public String getPosterURLCloudTv() {
        return posterURLCloudTv;
    }

    public void setPosterURLCloudTv(String posterURLCloudTv) {
        this.posterURLCloudTv = posterURLCloudTv;
    }

    public List<com.pukka.ydepg.common.http.v6bean.v6response.other> getOther() {
        return other;
    }

    public void setOther(List<com.pukka.ydepg.common.http.v6bean.v6response.other> other) {
        this.other = other;
    }
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public String getAppointedId() {
        return appointedId;
    }

    public void setAppointedId(String appointedId) {
        this.appointedId = appointedId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(Integer identifyType) {
        this.identifyType = identifyType;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public List<VOD> getVODs() {
        return VODs;
    }

    public void setVODs(List<VOD> VODs) {
        this.VODs = VODs;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Apk> getApks() {
        return apks;
    }

    public void setApks(List<Apk> apks) {
        this.apks = apks;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
