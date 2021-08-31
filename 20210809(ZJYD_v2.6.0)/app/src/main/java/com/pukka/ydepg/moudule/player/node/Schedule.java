package com.pukka.ydepg.moudule.player.node;


import android.os.Parcel;
import android.os.Parcelable;

import com.pukka.ydepg.common.http.v6bean.v6node.PlaybillLite;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule implements Parcelable {

    private String fatherId;

    @SerializedName("fatherbame")
    private String fatherName;

    @SerializedName("ID")
    private String id;

    @SerializedName("name")
    private String name;

    private boolean isSupportBtv;

    /**
     * 第三方系统分配的内容Code
     */
    @SerializedName("code")
    private String code;

    @SerializedName("number")
    private int number;

    @SerializedName("picture")
    private String picture;

    private String poster;

    private String channelLogo;

    private String channelNo;

    @SerializedName("ad")
    private String ad;

    @SerializedName("title")
    private String title;

    /**
     * 是否收藏(1：收藏、0：未收藏)
     */
    private boolean isFavorite;

    @SerializedName("lockStatus")
    private int lockStatus;

    /**
     * 时移时长 s秒
     */
    @SerializedName("PLTVLength")
    private int pltvLength;

    /**
     * 录播时长 s秒
     */
    @SerializedName("recordLength")
    private int recordLength;

    /**
     * 是否支持录播(1：支持、0：不支持)
     */
    @SerializedName("isTVOD")
    private int isTVOD;

    @SerializedName("isAudio")
    private int isAudio;

    /**
     * 高清标识(1：高清、2：标清)
     */
    @SerializedName("definition")
    private int definition;

    @SerializedName("mediaID")
    private String mediaID;

    /**
     * 频道类型(1：视频、2：音频、3：页面频道)
     */
    @SerializedName("channelType")
    private int channelType;

    @SerializedName("programList")
    private List<Program> programList;

    /**
     * 是否已订购(1：订购、0：未订购)
     */
    @SerializedName("issubscribed")
    private int issubscribed;

    /**
     * 是否支持时移(1：支持、0：不支持)
     */
    @SerializedName("ispltv")
    private int ispltv = 0;

    /**
     * 当前直播URL
     */
    @SerializedName("url")
    private String url;

    /**
     * 时移URl
     */
    @SerializedName("timeshifturl")
    private String timeshifturl;

    private int parentPosition;
    private String parentId;

    private String isLocked;

    private PlaybillLite currentPlaybillLite;

    public Schedule() {

    }


    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
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

    public boolean isSupportBtv() {
        return isSupportBtv;
    }

    public void setSupportBtv(boolean supportBtv) {
        isSupportBtv = supportBtv;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getChannelLogo() {
        return channelLogo;
    }

    public void setChannelLogo(String channelLogo) {
        this.channelLogo = channelLogo;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }

    public int getPltvLength() {
        return pltvLength;
    }

    public void setPltvLength(int pltvLength) {
        this.pltvLength = pltvLength;
    }

    public int getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    public int getIsTVOD() {
        return isTVOD;
    }

    public void setIsTVOD(int isTVOD) {
        this.isTVOD = isTVOD;
    }

    public int getIsAudio() {
        return isAudio;
    }

    public void setIsAudio(int isAudio) {
        this.isAudio = isAudio;
    }

    public int getDefinition() {
        return definition;
    }

    public void setDefinition(int definition) {
        this.definition = definition;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public List<Program> getProgramList() {
        return programList;
    }

    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }

    public int getIssubscribed() {
        return issubscribed;
    }

    public void setIssubscribed(int issubscribed) {
        this.issubscribed = issubscribed;
    }

    public int getIspltv() {
        return ispltv;
    }

    public void setIspltv(int ispltv) {
        this.ispltv = ispltv;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimeshifturl() {
        return timeshifturl;
    }

    public void setTimeshifturl(String timeshifturl) {
        this.timeshifturl = timeshifturl;
    }

    public int getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsLocked() {
        return isLocked;
    }
    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public PlaybillLite getCurrentPlaybillLite() {
        return currentPlaybillLite;
    }

    public void setCurrentPlaybillLite(PlaybillLite currentPlaybillLite) {
        this.currentPlaybillLite = currentPlaybillLite;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fatherId);
        dest.writeString(this.fatherName);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.isSupportBtv ? (byte) 1 : (byte) 0);
        dest.writeString(this.code);
        dest.writeInt(this.number);
        dest.writeString(this.picture);
        dest.writeString(this.poster);
        dest.writeString(this.channelLogo);
        dest.writeString(this.channelNo);
        dest.writeString(this.ad);
        dest.writeString(this.title);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeInt(this.lockStatus);
        dest.writeInt(this.pltvLength);
        dest.writeInt(this.recordLength);
        dest.writeInt(this.isTVOD);
        dest.writeInt(this.isAudio);
        dest.writeInt(this.definition);
        dest.writeString(this.mediaID);
        dest.writeInt(this.channelType);
        dest.writeTypedList(this.programList);
        dest.writeInt(this.issubscribed);
        dest.writeInt(this.ispltv);
        dest.writeString(this.url);
        dest.writeString(this.timeshifturl);
        dest.writeInt(this.parentPosition);
        dest.writeString(this.parentId);
        dest.writeString(this.isLocked);
        dest.writeSerializable(this.currentPlaybillLite);
    }

    protected Schedule(Parcel in) {
        this.fatherId = in.readString();
        this.fatherName = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.isSupportBtv = in.readByte() != 0;
        this.code = in.readString();
        this.number = in.readInt();
        this.picture = in.readString();
        this.poster = in.readString();
        this.channelLogo = in.readString();
        this.channelNo = in.readString();
        this.ad = in.readString();
        this.title = in.readString();
        this.isFavorite = in.readByte() != 0;
        this.lockStatus = in.readInt();
        this.pltvLength = in.readInt();
        this.recordLength = in.readInt();
        this.isTVOD = in.readInt();
        this.isAudio = in.readInt();
        this.definition = in.readInt();
        this.mediaID = in.readString();
        this.channelType = in.readInt();
        this.programList = in.createTypedArrayList(Program.CREATOR);
        this.issubscribed = in.readInt();
        this.ispltv = in.readInt();
        this.url = in.readString();
        this.timeshifturl = in.readString();
        this.parentPosition = in.readInt();
        this.parentId = in.readString();
        this.isLocked = in.readString();
        this.currentPlaybillLite = (PlaybillLite) in.readSerializable();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
