package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PlaySession;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayVODResponse.java
 * @author: yh
 * @date: 2017-04-25 10:52
 */

public class PlayVODResponse extends BaseResponse{

    /**
     * result :
     * authorizeResult :
     * playURL :
     * bookmark : 1
     * playSessions : [""]
     * extensionFields : [""]
     */

    /**
     *鉴权结果。

     */
    @SerializedName("authorizeResult")
    private AuthorizeResult authorizeResult;

    /**
     *如果鉴权成功，返回内容播放地址。

     */
    @SerializedName("playURL")
    private String playURL;

    /**
     *如果鉴权成功且VOD创建过书签，返回书签断点时间。

     单位是秒。

     */
    @SerializedName("bookmark")
    private String bookmark;

    /**
     *如果内容并发播放数超限，返回正在播放此内容的播放会话。

     */
    @SerializedName("playSessions")
    private List<PlaySession> playSessions;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public AuthorizeResult getAuthorizeResult() {
        if(null==authorizeResult){
            return new AuthorizeResult();
        }
        return authorizeResult;
    }

    public void setAuthorizeResult(AuthorizeResult authorizeResult) {
        this.authorizeResult = authorizeResult;
    }

    public String getPlayURL() {
        return playURL;
    }

    public void setPlayURL(String playURL) {
        this.playURL = playURL;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public List<PlaySession> getPlaySessions() {
        return playSessions;
    }

    public void setPlaySessions(List<PlaySession> playSessions) {
        this.playSessions = playSessions;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
