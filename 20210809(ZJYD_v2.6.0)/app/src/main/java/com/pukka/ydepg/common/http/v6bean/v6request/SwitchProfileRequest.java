package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class SwitchProfileRequest {

    /**
     * profileID :
     * password :
     * forceLogin :
     * asDefaultProfile :
     * lang :
     * deviceID :
     * extensionFields :
     */

    /**
     *待认证的Profile ID
     */
    @SerializedName("profileID")
    private String profileID;

    /**
     *Profile密码，此处是明文。

     如果当前登录设备绑定了默认Profile且默认Profile就是待切换的ProfileID，以及主Profile切换到子Profile，终端可以不上报密码

     */
    @SerializedName("password")
    private String password;

    /**
     *Profile是否强制登录。

     取值范围：

     0：不强制
     1：强制
     默认为1。如果一个Profile已经在一个终端A上登录，该Profile尝试又在另一终端B上登录，当forceLogin=0，系统会提示返回“登录失败，Profile已登录”的错误码，当foreLogin=1，系统自动断开最早的会话

     */
    @SerializedName("forceLogin")
    private String forceLogin;

    /**
     *是否将该profile设置为默认profile。

     0：否
     1：是
     如果不传该字段，则表示保持当前设备上的默认profile不变化

     */
    @SerializedName("asDefaultProfile")
    private String asDefaultProfile;

    /**
     *当前客户端页面显示内容的语种属性。

     采用统一为ISO639-1缩写，如en

     */
    @SerializedName("lang")
    private String lang;

    /**
     *当前设备对应的逻辑设备ID。

     如果asDefaultProfile有值，则需要上报该字段

     */
    @SerializedName("deviceID")
    private String deviceID;

    /**
     *扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForceLogin() {
        return forceLogin;
    }

    public void setForceLogin(String forceLogin) {
        this.forceLogin = forceLogin;
    }

    public String getAsDefaultProfile() {
        return asDefaultProfile;
    }

    public void setAsDefaultProfile(String asDefaultProfile) {
        this.asDefaultProfile = asDefaultProfile;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
