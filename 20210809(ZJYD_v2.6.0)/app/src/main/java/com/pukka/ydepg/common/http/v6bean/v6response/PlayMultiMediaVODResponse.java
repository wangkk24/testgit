package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.MultiMediaAuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.PlaySession;

import java.util.List;

/**
 * 作者：panjw on 2021/6/10 10:32
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class PlayMultiMediaVODResponse extends BaseResponse {

    @SerializedName("multiMediaAuthorizeResults")
    private List<MultiMediaAuthorizeResult> multiMediaAuthorizeResults;
    @SerializedName("isLocked")
    private int isLocked;
    @SerializedName("isParentControl")
    private int isParentControl;
    @SerializedName("contentID")
    private String contentID;
    @SerializedName("contentType")
    private String contentType;
    @SerializedName("bookmark")
    private int bookmark;
    @SerializedName("playSessions")
    private List<PlaySession>playSessions;
    public List<MultiMediaAuthorizeResult> getMultiMediaAuthorizeResults() {
        return multiMediaAuthorizeResults;
    }

    public void setMultiMediaAuthorizeResults(List<MultiMediaAuthorizeResult> multiMediaAuthorizeResults) {
        this.multiMediaAuthorizeResults = multiMediaAuthorizeResults;
    }

    public int getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(int isLocked) {
        this.isLocked = isLocked;
    }

    public int getIsParentControl() {
        return isParentControl;
    }

    public void setIsParentControl(int isParentControl) {
        this.isParentControl = isParentControl;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public List<PlaySession> getPlaySessions() {
        return playSessions;
    }

    public void setPlaySessions(List<PlaySession> playSessions) {
        this.playSessions = playSessions;
    }


}
