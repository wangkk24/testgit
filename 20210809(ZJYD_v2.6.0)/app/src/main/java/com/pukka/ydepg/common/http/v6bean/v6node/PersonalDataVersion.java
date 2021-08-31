package com.pukka.ydepg.common.http.v6bean.v6node;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */
public class PersonalDataVersion {
    /**
     * 频道版本号。
     * 频道版本号格式如下：“channelversion|subscribeversion”。
     * 其中， “channelversion”为频道元数据版本号，当CP操作员修改频道元数据时会修改；
     * “subscribeversion”为用户订购关系版本号，当用户订购关系变化时会修改。
     */
    @SerializedName("channel")
    private String channel;

    /**
     * 用户书签版本号，目前使用14位的时间表示，格式类似yyyyMMddHHmmss。
     */
    @SerializedName("bookmark")
    private String bookmark;

    /**
     * 用户收藏版本号，目前使用14位的时间表示，格式类似yyyyMMddHHmmss。
     */
    @SerializedName("favorite")
    private String favorite;

    /**
     * 用户童锁版本号，目前使用14位的时间表示，格式类似yyyyMMddHHmmss。
     */
    @SerializedName("lock")
    private String lock;

    /**
     * 用户提醒版本号，目前使用14位的时间表示，格式类似yyyyMMddHHmmss。
     */
    @SerializedName("reminder")
    private String reminder;

    /**
     * profile版本号，目前使用Long整数表示。
     */
    @SerializedName("profile")
    private String profile;

    /**
     * 机顶盒PVR录制文件版本号
     */
    @SerializedName("stbFileVersion")
    private String stbFileVersion;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getStbFileVersion() {
        return stbFileVersion;
    }

    public void setStbFileVersion(String stbFileVersion) {
        this.stbFileVersion = stbFileVersion;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    @Override
    public String toString() {
        return "PersonalDataVersion{" +
                "\n\t\tchannel         = " + channel        +
                "\n\t\tbookmark        = " + bookmark       +
                "\n\t\tfavorite        = " + favorite       +
                "\n\t\tlock            = " + lock           +
                "\n\t\treminder        = " + reminder       +
                "\n\t\tprofile         = " + profile        +
                "\n\t\tstbFileVersion  = " + stbFileVersion +
                "\n\t\textensionFields = " + extensionFields;
    }

    public String getChannelVersion(){
        if (TextUtils.isEmpty(channel)){
            return "0";
        }
        String[] split = channel.split("\\|");
        if (split.length > 0){
           return split[0];
        }
        return "0";
    }

    public String getSubscribeVersion(){
        if (TextUtils.isEmpty(channel)){
            return "0";
        }
        String[] split = channel.split("\\|");
        if (split.length > 1){
            return split[1];
        }
        return "0";
    }
}