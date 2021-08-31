package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.UserLoginHistoryInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class SwitchProfileResponse {

    /**
     * result :
     * defaultProfileID :
     * userToken :
     * isFirstLogin :
     * hasCollectUserPreference :
     * userLoginHistoryInfo :
     * extensionFields :
     */

    /**
     *返回结果
     */
    @SerializedName("result")
    private Result result;

    /**
     *默认profile处理（设置/取消设置）成功后，返回当前profileID。

     如果没有设置请求或设置失败，则不返回

     */
    @SerializedName("defaultProfileID")
    private String defaultProfileID;

    /**
     *当前登录用户的令牌，由VSP在User Profile认证成功后自动生成
     */
    @SerializedName("userToken")
    private String userToken;

    /**
     *Profile是否是首次登录Envision Video 系统。

     取值范围：

     0：非首次登录
     1：首次登录
     默认值为0

     */
    @SerializedName("isFirstLogin")
    private String isFirstLogin;

    /**
     *是否反馈用户初始偏好，取值范围：

     0：未反馈
     1：反馈
     默认值为0

     */
    @SerializedName("hasCollectUserPreference")
    private String hasCollectUserPreference;

    /**
     *本次认证以前的认证日志信息。

     说明：
     安全要求，在认证的时候，需要提示用户最近登录的IP/登录结果（成功/失败），终端可以根据的业务需要向用户提示。类似的还有提示用户当前是不是在另外一个设备上有登录

     */
    @SerializedName("userLoginHistoryInfo")
    private UserLoginHistoryInfo userLoginHistoryInfo;

    /**
     *扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getDefaultProfileID() {
        return defaultProfileID;
    }

    public void setDefaultProfileID(String defaultProfileID) {
        this.defaultProfileID = defaultProfileID;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getHasCollectUserPreference() {
        return hasCollectUserPreference;
    }

    public void setHasCollectUserPreference(String hasCollectUserPreference) {
        this.hasCollectUserPreference = hasCollectUserPreference;
    }

    public UserLoginHistoryInfo getUserLoginHistoryInfo() {
        return userLoginHistoryInfo;
    }

    public void setUserLoginHistoryInfo(UserLoginHistoryInfo userLoginHistoryInfo) {
        this.userLoginHistoryInfo = userLoginHistoryInfo;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
