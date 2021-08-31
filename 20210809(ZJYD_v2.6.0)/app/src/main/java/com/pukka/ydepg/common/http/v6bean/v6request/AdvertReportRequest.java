package com.pukka.ydepg.common.http.v6bean.v6request;


import androidx.annotation.NonNull;

import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertUser;

public class AdvertReportRequest {

    //事件上报消息的请求流水
    //格式：媒体编码_当前时间毫秒数_10随机数
    //EPG的媒体编码：OTT_EPG
    //举例：OTT_EPG_1551283200000_2736546789
    private String reqId;

    //浙江移动固定为8601
    private String tenantId;

    //用户信息，跟广告请求消息一致
    private AdvertUser user;

    //被曝光/点击的广告的编码（广告请求时返回的广告编码）
    private String adId;

    //上报的事件类型：
    //-	IMPRESSION-曝光/展示（或视频加载成功）
    //-	CLICK-点击
    //-	对于视频是否需要其他播放进度或时长，待定
    private String actionType;

    //上报时间，取当前时间的毫秒数；
    private Long startdate;

    //上报实际播放时长，单位为毫秒，actionType=IMPRESSIONDUR时必填
    private Long duration;

    //广告类型
    //-VIDEO：视频
    //-NATIVE：信息流
    //-BANNER：图片
    private String adType;

    //关联ID：在线广告请求的reqID；用于将事件上报请求跟此前的广告请求关联起来；后续会用于稽核，避免出现虚假的投放记录
    private String relationID;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public AdvertUser getUser() {
        return user;
    }

    public void setUser(AdvertUser user) {
        this.user = user;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Long getStartdate() {
        return startdate;
    }

    public void setStartdate(Long startdate) {
        this.startdate = startdate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }


    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
            .append("\n\treqId      = ").append(reqId)
            .append("\n\ttenantId   = ").append(tenantId)
            .append("\n\tuser       = ").append(user)
            .append("\n\tadId       = ").append(adId)
            .append("\n\tactionType = ").append(actionType)
            .append("\n\tstartDate  = ").append(startdate).append(" UTC")
            .append("\n\tduration   = ").append(duration).append(" ms")
            .append("\n\tadType     = ").append(adType)
            .append("\n\trelationID = ").append(relationID)
            .toString();
    }
}