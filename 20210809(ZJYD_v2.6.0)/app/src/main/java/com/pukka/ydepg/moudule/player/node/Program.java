package com.pukka.ydepg.moudule.player.node;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jWX234536 on 2015/8/4.
 */
public class Program  implements Parcelable
{
    @SerializedName("ID")
    private String id;

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    /**
     * 节目是否可订购标识
     1：可订购
     0：不可订购
     */
    @SerializedName("canOrder")
    private String canOrder = "0";

    /**
     * 搜索码
     节目搜索码
     */
    @SerializedName("searchCode")
    private String searchCode;

    /**
     * 预留扩展位
      默认为0
     */
    @SerializedName("extend")
    private String extend;

    /**
     * 录播节目单状态
     0：未录制
     1：录制成功
     2：录制失败
     */
    @SerializedName("recordingState")
    private String recordingState = "0";

    @SerializedName("playUrl")
    private String playUrl;

    /**
     * TVODProgramListActivity中才使用的字段,其他场景该值为NULL
     */
    @SerializedName("channelID")
    private String channelID;

    protected Program(Parcel in) {
        id = in.readString();
        code = in.readString();
        name = in.readString();
        startDate = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        canOrder = in.readString();
        searchCode = in.readString();
        extend = in.readString();
        recordingState = in.readString();
        playUrl = in.readString();
        channelID=in.readString();
    }

    public static final Creator<Program> CREATOR = new Creator<Program>() {
        @Override
        public Program createFromParcel(Parcel in) {
            return new Program(in);
        }

        @Override
        public Program[] newArray(int size) {
            return new Program[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCanOrder() {
        return canOrder;
    }

    public void setCanOrder(String canOrder) {
        this.canOrder = canOrder;
    }

    public String getSearchCode() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = searchCode;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getRecordingState() {
        return recordingState;
    }

    public void setRecordingState(String recordingState) {
        this.recordingState = recordingState;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Program() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(startDate);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(canOrder);
        dest.writeString(searchCode);
        dest.writeString(extend);
        dest.writeString(recordingState);
        dest.writeString(playUrl);
        dest.writeString(channelID);
    }
}
