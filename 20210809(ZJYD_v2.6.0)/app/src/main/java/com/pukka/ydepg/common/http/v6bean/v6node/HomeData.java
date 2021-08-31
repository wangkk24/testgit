package com.pukka.ydepg.common.http.v6bean.v6node;

import com.pukka.ydepg.common.utils.OTTFormat;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/5/8.
 */

public class HomeData extends ExtensionField {
    private static final String TAG = ChannelPlaybill.class.getSimpleName();

    @SerializedName("result")
    private Result result;

    @SerializedName("vodList")
    private List<VOD> vodList;

    @SerializedName("channelList")
    private List<Channel> channelList;

    @SerializedName("playbillList")
    private List<Playbill> playbillList;

    @SerializedName("playbillLiteList")
    private List<PlaybillLite> playbillLiteList;

    @SerializedName("subjectList")
    private List<Subject> subjectList;

    @SerializedName("channelDetailList")
    private List<ChannelDetail> channelDetailList;

    @SerializedName("subject")
    private Subject subject;

    @SerializedName("recmActionID")
    private String recmActionID;


    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<VOD> getVODList() {
        return vodList;
    }

    public void setVODList(List<VOD> vodList) {
        this.vodList = vodList;
    }

    public List<ChannelDetail> getChannelDetailList() {
        return channelDetailList;
    }

    public void setChannelDetailList(List<ChannelDetail> channelDetailList) {
        this.channelDetailList = channelDetailList;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getRecmActionId() {
        return recmActionID;
    }

    public void setRecmActionId(String recmActionID) {
        this.recmActionID = recmActionID;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public List<Playbill> getPlaybillList() {
        if (null != playbillList && playbillList.size() > 1) {
            for (int i = 0; i < playbillList.size() - 1; i++) {
                if (null != playbillList.get(i)) {
                    if (null != playbillList.get(i + 1)) {
                        long firstEndTime = OTTFormat.convertSimpleLong(playbillList.get(i)
                                .getEndTime());
                        long secondStartTime = OTTFormat.convertSimpleLong(playbillList.get(i +
                                1).getStartTime());
                        if (firstEndTime > secondStartTime) {
//                            DebugLog.error(TAG, DebugLog.Scenario.ERROR_CODE, ErrorCode.findError
//                                    (ErrorCode.Module.MSA, ErrorCode.Module.MSA, ErrorCode.MSA
//                                            .CLIENT_38029).toString());
                            break;
                        }
                        if (secondStartTime - firstEndTime > 1000) {
//                            DebugLog.error(TAG, DebugLog.Scenario.ERROR_CODE, ErrorCode.findError
//                                    (ErrorCode.Module.MSA, ErrorCode.Module.MSA, ErrorCode.MSA
//                                            .CLIENT_38030).toString());
                            break;
                        }
                    }
                }
            }
        }
        return playbillList;
    }

    public void setPlaybillList(List<Playbill> playbillList) {
        this.playbillList = playbillList;
    }

    public List<PlaybillLite> getPlaybillLiteList() {
        if (null != playbillLiteList && playbillLiteList.size() > 1) {
            for (int i = 0; i < playbillLiteList.size() - 1; i++) {
                if (null != playbillLiteList.get(i)) {
                    if (null != playbillLiteList.get(i + 1)) {
                        long firstEndTime = OTTFormat.convertSimpleLong(playbillLiteList.get(i)
                                .getEndTime());
                        long secondStartTime = OTTFormat.convertSimpleLong(playbillLiteList.get(i
                                + 1).getStartTime());
                        if (firstEndTime > secondStartTime) {
//                            DebugLog.error(TAG, DebugLog.Scenario.ERROR_CODE, ErrorCode.findError
//                                    (ErrorCode.Module.MSA, ErrorCode.Module.MSA, ErrorCode.MSA
//                                            .CLIENT_38029).toString());
                            break;
                        }
                        if (secondStartTime - firstEndTime > 1000) {
//                            DebugLog.error(TAG, DebugLog.Scenario.ERROR_CODE, ErrorCode.findError
//                                    (ErrorCode.Module.MSA, ErrorCode.Module.MSA, ErrorCode.MSA
//                                            .CLIENT_38030).toString());
                            break;
                        }
                    }
                }
            }
        }
        return playbillLiteList;
    }

    public void setPlaybillLiteList(List<PlaybillLite> playbillLiteList) {
        this.playbillLiteList = playbillLiteList;
    }

    @Override
    public String toString() {
        return "HomeData{" +
                "result=" + result +
                ", vodList=" + vodList +
                ", channelList=" + channelList +
                ", playbillList=" + playbillList +
                ", playbillLiteList=" + playbillLiteList +
                ", subjectList=" + subjectList +
                ", channelDetailList=" + channelDetailList +
                ", subject=" + subject +
                ", recmActionID='" + recmActionID + '\'' +
                '}';
    }
}