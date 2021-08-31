package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PlaySession;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: PlayChannelResponse.java
 * @author: yh
 * @date: 2017-04-25 10:11
 */

public class PlayChannelResponse extends BaseResponse{

    /**
     * result :
     * authorizeResult :
     * AuthorizeResult :
     * playURL :
     * attachedPlayURL :
     * bookmark :
     * playSessions :
     * extensionFields :
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
     *由于直播和网络时移业务，用户可以自由切换，所以为了提升用户体验，当终端在请求直播或网络时移的播放地址时，平台可能返回网络时移或直播的播放地址，原则如下：

     如果businessType是BTV且频道支持网络时移且用户可以使用网络时移业务，返回网络时移的播放地址。
     如果businessType是PLTV且用户可以使用直播业务，返回直播的播放地址。
     */
    @SerializedName("attachedPlayURL")
    private String attachedPlayURL;

    /**
     *如果鉴权成功且回看节目单创建过书签，返回书签断点时间。

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

    public String getAttachedPlayURL() {
        return attachedPlayURL;
    }

    public void setAttachedPlayURL(String attachedPlayURL) {
        this.attachedPlayURL = attachedPlayURL;
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
