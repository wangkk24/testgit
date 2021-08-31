package com.pukka.ydepg.moudule.mytv.bean;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * 消息体
 * @version V1.0
 * @FileName: com.pukka.ydepg.moudule.mytv.bean.BodyBean.java
 * @author:xj
 * @date: 2017-12-25 16:33
 */

public class BodyBean implements Comparable<BodyBean>  {


    /*"type":"<消息任务类型>","mode":"<消息展现模式>","domain":"<消息领域>",
    "content":"<消息内容或URL>","contentType":"<content的类型>",
    "sendTime":"<任务发送时间>","validTime":"<消息有效时间>",
     "mediaCode":"<关联的内容的code>","IPTVURL":"<消息页面/文件在IPTV领域的URL>",
     "lang":"<语种，2位字母>","reserve":"<保留的扩展字段>"*/

    /**
     * type : 0
     * mode : 6
     * domain : 0
     * content : HelloWorld!
     * contentType : 0
     * sendTime : 20160420110000
     * validTime : 20160420110000
     * lang : en
     */
    /*
    * 扩展字段
    * title:标题
    * */

    @SerializedName("type")
    private String type;
    @SerializedName("mode")
    private String mode;
    @SerializedName("domain")
    private String domain;
    @SerializedName("content")
    private String content;
    @SerializedName("contentType")
    private String contentType;
    @SerializedName("sendTime")
    private String sendTime;
    @SerializedName("validTime")
    private String validTime;
    @SerializedName("lang")
    private String lang;
    @SerializedName("title")
    private String title;
    @SerializedName("receivingTime")
    private String receivingTime;

    public String getReceivingTime() {
        return receivingTime;
    }

    public void setReceivingTime(String receivingTime) {
        this.receivingTime = receivingTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int compareTo(@NonNull BodyBean o) {
      // o.getValidTime().compareTo(this.validTime);
        return o.getValidTime().compareTo(this.validTime);
    }
}
