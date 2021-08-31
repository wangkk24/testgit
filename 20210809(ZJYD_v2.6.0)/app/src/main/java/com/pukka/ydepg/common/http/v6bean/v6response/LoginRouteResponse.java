package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class LoginRouteResponse {

    /**
     * result :
     * vspURL :
     * vspHttpsURL :
     * extensionFields :
     */

    /**
     * 返回结果
     */
    @SerializedName("result")
    private Result result;

    /**
     * 如果调度成功，返回VSP服务器的HTTP访问地址，格式为http://<VSPIP|Domain>:<VSPHttpPort>。
     * 如果地址是IP形式，IP地址支持V4或V6，VSP平台(EDS)根据终端接入的网络地址类型返回对应的VSP地址。
     */
    @SerializedName("vspURL")
    private String vspURL;

    /**
     * 如果调度成功，返回VSP服务器的HTTPS地址，当用户网关(VSP)服务器支持HTTPS协议才返回，
     * 格式为https://<VSPIP|Domain >:<VSPHttpsPort>。
     * 如果地址是IP形式，IP地址支持V4或V6，VSP平台(EDS)根据终端接入的网络地址类型返回对应的用户网关(VSP)地址。
     */
    @SerializedName("vspHttpsURL")
    private String vspHttpsURL;

    @SerializedName("backupVspURLs")
    private List<String> backupVspURLs;

    @SerializedName("backupVspHttpsURLs")
    private List<String> backupVspHttpsURLs;

    public List<String> getBackupVspURLs() {
        return backupVspURLs;
    }

    public void setBackupVspURLs(List<String> backupVspURLs) {
        this.backupVspURLs = backupVspURLs;
    }

    public List<String> getBackupVspHttpsURLs() {
        return backupVspHttpsURLs;
    }

    public void setBackupVspHttpsURLs(List<String> backupVspHttpsURLs) {
        this.backupVspHttpsURLs = backupVspHttpsURLs;
    }

    /**
     * 扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getVspURL() {
        return vspURL;
    }

    public void setVspURL(String vspURL) {
        this.vspURL = vspURL;
    }

    public String getVspHttpsURL() {
        return vspHttpsURL;
    }

    public void setVspHttpsURL(String vspHttpsURL) {
        this.vspHttpsURL = vspHttpsURL;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

}
