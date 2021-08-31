package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.huawei.chfjydvideo.common.http.v6bean.v6node.Subject.java
 * @author: yh
 * @date: 2017-04-21 16:54
 */

public class Subject extends Metadata  {


    /**
     * ID :
     * name :
     * contentType :
     * introduce :
     * picture : Picture
     * hasChildren : 12
     * isSubscribed : 12
     * parentSubjectID :
     * isLocked : 12
     * extensionFields : [""]
     */

    /**
     * 海报类型
     */
    int type;

//    /**
//     * 栏目ID。
//     */
//    @SerializedName("ID")
//    private String ID;
//
//    /**
//     * 栏目名称。
//     */
//    @SerializedName("name")
//    private String name;

    /**
     * 栏目下可以挂载的内容类型。
     * <p>
     * 取值范围：
     * <p>
     * VOD：点播
     * AUDIO_VOD：音频点播
     * VIDEO_VOD：视频点播
     * CHANNEL：频道
     * AUDIO_CHANNEL：音频频道
     * VIDEO_CHANNEL：视频频道
     * MIX：混合，任何内容都可以挂载
     * PROGRAM：节目单
     */
    @SerializedName("contentType")
    private String contentType;

    /**
     * 栏目简介。
     */
    @SerializedName("introduce")
    private String introduce;

//    /**
//     * 栏目海报信息。图片信息参考“Picture”。
//     */
//    @SerializedName("picture")
//    private Picture picture;

    /**
     * 是否有子栏目。
     * <p>
     * 取值范围：
     * <p>
     * -1：该栏目是叶子栏目
     * 0：有子栏目，但是子栏目不可用
     * 1：有可用的子栏目
     */
    @SerializedName("hasChildren")
    private String hasChildren;

    /**
     * 是否已经订购。
     * <p>
     * 取值范围：
     * <p>
     * 0：未订购
     * 1：已订购
     */
    @SerializedName("isSubscribed")
    private String isSubscribed;

    /**
     * 父栏目ID。
     * <p>
     * 如果id为-1，这个参数无效
     */
    @SerializedName("parentSubjectID")
    private String parentSubjectID;

    /**
     * 标识栏目是否被加锁。
     * <p>
     * 取值范围：
     * <p>
     * 1：已加锁
     * 0：未加锁
     */
    @SerializedName("isLocked")
    private String isLocked;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    @SerializedName("customFields")
    protected List<NamedParameter> customFields;

    public String getID() {
        return ID == null ? "" : ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }


    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Picture getPicture() {
        if(picture==null)
            return new Picture();
        return picture;
    }

    public String getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getParentSubjectID() {
        return parentSubjectID;
    }

    public void setParentSubjectID(String parentSubjectID) {
        this.parentSubjectID = parentSubjectID;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }
}
