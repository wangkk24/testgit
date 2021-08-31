package com.pukka.ydepg.moudule.mytv.bean;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 消息体
 * @FileName: com.pukka.ydepg.moudule.mytv.bean.BodyContentBean.java
 * content类型为Object
 * 5：命令通知消息
 */

public class BodyContentBean implements Comparable<BodyContentBean>  {

    /*{"mode":"5","domain":"1","type":"0","content":{"Command":"appmessage","actionType":"0","contentCode":"640649872","contentId":"60106081",
            "contentType":"VOD","extensionInfo":"{\"postURL\":\"www.baidu.com\",\"cancelBtnText\":\"cancelBtnText\",\"confirmBtnText\":\"cancelBtnText\"}",
            "messageIntroduce":"输出","messageTitle":"输出","pushType":"TEXT","summary":"输出"},"validTime":"20180525045800","mediaCode":"60106081"}*/



    @SerializedName("type")
    private String type;
    @SerializedName("mode")
    private String mode;
    @SerializedName("domain")
    private String domain;
    @SerializedName("content")
    private Content content;
    @SerializedName("validTime")
    private String validTime;
    @SerializedName("mediaCode")
    private String mediaCode;

    @SerializedName("receivingTime")
    private String receivingTime;

    public String getReceivingTime() {
        return receivingTime;
    }

    public void setReceivingTime(String receivingTime) {
        this.receivingTime = receivingTime;
    }

    public String getMediaCode() {
        return mediaCode;
    }

    public void setMediaCode(String mediaCode) {
        this.mediaCode = mediaCode;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
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

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }


    @Override
    public int compareTo(@NonNull BodyContentBean o) {
        // o.getValidTime().compareTo(this.validTime);
        return o.getValidTime().compareTo(this.validTime);
    }

    public static class Content implements Serializable{

        @SerializedName("command")
        private String Command;
        @SerializedName("actionType")
        private String actionType;
        @SerializedName("contentCode")
        private String contentCode;
        @SerializedName("contentId")
        private String contentId;
        @SerializedName("contentType")
        private String contentType;
        @SerializedName("messageIntroduce")
        private String messageIntroduce;
        @SerializedName("messageTitle")
        private String messageTitle;
        @SerializedName("pushType")
        private String pushType;
        @SerializedName("summary")
        private String summary;
        @SerializedName("extensionInfo")
        private String extensionInfo;
        @SerializedName("linkURL")
        private String linkURL;
        @SerializedName("categoryId")
        private String categoryId;
        @SerializedName("sendTime")
        private String sendTime;

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getLinkURL() {
            return linkURL;
        }

        public void setLinkURL(String linkURL) {
            this.linkURL = linkURL;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getExtensionInfo() {
            return extensionInfo;
        }

        public void setExtensionInfo(String extensionInfo) {
            this.extensionInfo = extensionInfo;
        }

        public String getCommand() {
            return Command;
        }

        public void setCommand(String command) {
            Command = command;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getContentCode() {
            return contentCode;
        }

        public void setContentCode(String contentCode) {
            this.contentCode = contentCode;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getMessageIntroduce() {
            return messageIntroduce;
        }

        public void setMessageIntroduce(String messageIntroduce) {
            this.messageIntroduce = messageIntroduce;
        }

        public String getMessageTitle() {
            return messageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
        }

        public String getPushType() {
            return pushType;
        }

        public void setPushType(String pushType) {
            this.pushType = pushType;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

    }

    public static class ExtensionInfo implements Serializable{

        @SerializedName("postURL")
        private String postURL;
        @SerializedName("cancelBtnText")
        private String cancelBtnText;
        @SerializedName("messageId")
        private String messageId;
        @SerializedName("confirmBtnText")
        private String confirmBtnText;
        @SerializedName("sendTime")
        private String sendTime;
        //枚举值：1:原生弹窗，2:H5弹窗
        @SerializedName("messagetype")
        private String messagetype = "1";
        //传入中文，当有多个标签时以“|”隔开。
        @SerializedName("Messagelabel")
        private String Messagelabel;
        //枚举值：1:左上，2:右上，3:左下，4:右下，5:中间
        @SerializedName("location")
        private String location = "5";
        //传入数字100-20，前端自动转换为百分比
        @SerializedName("opaque")
        private String opaque = "100";
        //枚举值：1:15S、2:30S、3:45S、4:60S、5:始终
        @SerializedName("theretime")
        private String theretime = "1";
        //传入数字，最小单位为1次
        @SerializedName("messagenum")
        private String messagenum = "1";
        //跳转内部界面的链接
        @SerializedName("actionUrl")
        private String actionUrl;

        @SerializedName("actionType")
        private String actionType;

        @SerializedName("ContentType")
        private String ContentType;

        @SerializedName("SubContentType")
        private String SubContentType;

        @SerializedName("ContentID")
        private String ContentID;

        @SerializedName("SubjectID")
        private String SubjectID;

        @SerializedName("FocusContentID")
        private String FocusContentID;

        @SerializedName("AppPkg")
        private String AppPkg;

        @SerializedName("AppClass")
        private String AppClass;

        @SerializedName("Version")
        private String Version;

        @SerializedName("ApkUrl")
        private String ApkUrl;

        @SerializedName("Action")
        private String Action;

        @SerializedName("ContentCode")
        private String ContentCode;

        @SerializedName("ClassName")
        private String ClassName;

        @SerializedName("ChannelMiniType")
        private String ChannelMiniType;

        @SerializedName("4kContentID")
        private String fourKContentID;

        @SerializedName("VODId")
        private String VODId;

        @SerializedName("type")
        private String type;

        @SerializedName("key")
        private String key;

        @SerializedName("extraData")
        private String extraData;

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getContentType() {
            return ContentType;
        }

        public void setContentType(String contentType) {
            ContentType = contentType;
        }

        public String getSubContentType() {
            return SubContentType;
        }

        public void setSubContentType(String subContentType) {
            SubContentType = subContentType;
        }

        public String getContentID() {
            return ContentID;
        }

        public void setContentID(String contentID) {
            ContentID = contentID;
        }

        public String getSubjectID() {
            return SubjectID;
        }

        public void setSubjectID(String subjectID) {
            SubjectID = subjectID;
        }

        public String getFocusContentID() {
            return FocusContentID;
        }

        public void setFocusContentID(String focusContentID) {
            FocusContentID = focusContentID;
        }

        public String getAppPkg() {
            return AppPkg;
        }

        public void setAppPkg(String appPkg) {
            AppPkg = appPkg;
        }

        public String getAppClass() {
            return AppClass;
        }

        public void setAppClass(String appClass) {
            AppClass = appClass;
        }

        public String getVersion() {
            return Version;
        }

        public void setVersion(String version) {
            Version = version;
        }

        public String getApkUrl() {
            return ApkUrl;
        }

        public void setApkUrl(String apkUrl) {
            ApkUrl = apkUrl;
        }

        public String getAction() {
            return Action;
        }

        public void setAction(String action) {
            Action = action;
        }

        public String getContentCode() {
            return ContentCode;
        }

        public void setContentCode(String contentCode) {
            ContentCode = contentCode;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }

        public String getChannelMiniType() {
            return ChannelMiniType;
        }

        public void setChannelMiniType(String channelMiniType) {
            ChannelMiniType = channelMiniType;
        }

        public String getFourKContentID() {
            return fourKContentID;
        }

        public void setFourKContentID(String fourKContentID) {
            this.fourKContentID = fourKContentID;
        }

        public String getVODId() {
            return VODId;
        }

        public void setVODId(String VODId) {
            this.VODId = VODId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getExtraData() {
            return extraData;
        }

        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }

        public String getActionUrl() {
            return actionUrl;
        }

        public void setActionUrl(String actionUrl) {
            this.actionUrl = actionUrl;
        }

        public String getPostURL() {
            return postURL;
        }

        public void setPostURL(String postURL) {
            this.postURL = postURL;
        }

        public String getCancelBtnText() {
            return cancelBtnText;
        }

        public void setCancelBtnText(String cancelBtnText) {
            this.cancelBtnText = cancelBtnText;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getConfirmBtnText() {
            return confirmBtnText;
        }

        public void setConfirmBtnText(String confirmBtnText) {
            this.confirmBtnText = confirmBtnText;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getMessagetype() {
            return messagetype;
        }

        public void setMessagetype(String messagetype) {
            this.messagetype = messagetype;
        }

        public String getMessagelabel() {
            return Messagelabel;
        }

        public void setMessagelabel(String messagelabel) {
            Messagelabel = messagelabel;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getOpaque() {
            return opaque;
        }

        public void setOpaque(String opaque) {
            this.opaque = opaque;
        }

        public String getTheretime() {
            return theretime;
        }

        public void setTheretime(String theretime) {
            this.theretime = theretime;
        }

        public String getMessagenum() {
            return messagenum;
        }

        public void setMessagenum(String messagenum) {
            this.messagenum = messagenum;
        }

    }
}
