package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class PageTracker {

    /**
     * type :
     * appID :
     * appPassword :
     * isSupportedUserLogCollect :
     * pageTrackerServerURL :
     * extensionFields :
     */

    /**
     *表示接入应用UI。
     对华为UI，默认数值如：HW_STB_EPGUI、HW_PC、HW_iPhone、HW_iPad、HW_AndroidPhone、HW_AndroidPad；
     对第三方UI，由管理员在网管上分配，如Youpeng_EPGUI。
     */
    @SerializedName("type")
    private String type;
    /**
     *表示接入应用唯一标识，格式是由6位字母和数字组成。
     */
    @SerializedName("appID")
    private String appID;
    /**
     *表示接入应用鉴权码，格式为：识别码|验证码。识别码是终端每次登录认证成功之后系统会生成的唯一标识字符串。验证码：Base64(HMAC(sha256, 识别码,AppPWD))。
     */
    @SerializedName("appPassword")
    private String appPassword;
    /**
     *是否支持实时采集用户页面浏览轨迹，取值包括：
     0：不支持
     1：支持
     默认值为0。
     */
    @SerializedName("isSupportedUserLogCollect")
    private String isSupportedUserLogCollect;
    /**
     *用户浏览轨迹采集服务端URL，格式形如"IP:Port/VSPLogServer/LogTracker"。
     */
    @SerializedName("pageTrackerServerURL")
    private String pageTrackerServerURL;
    /**
     *扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public String getIsSupportedUserLogCollect() {
        return isSupportedUserLogCollect;
    }

    public void setIsSupportedUserLogCollect(String isSupportedUserLogCollect) {
        this.isSupportedUserLogCollect = isSupportedUserLogCollect;
    }

    public String getPageTrackerServerURL() {
        return pageTrackerServerURL;
    }

    public void setPageTrackerServerURL(String pageTrackerServerURL) {
        this.pageTrackerServerURL = pageTrackerServerURL;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
