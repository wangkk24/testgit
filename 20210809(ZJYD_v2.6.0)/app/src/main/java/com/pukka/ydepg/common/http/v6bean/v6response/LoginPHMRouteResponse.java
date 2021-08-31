package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;

import java.util.List;
import java.util.Map;

public class LoginPHMRouteResponse {

    /*如果调度成功，返回PHMServer中PHS桌面服务的HTTP访问地址，格式为http://<PHSIP|Domain>:<PHSHttpPort>。
    如果地址是IP形式，IP地址支持V4或V6，VSP平台根据终端接入条件返回对应的PHS地址。
    如果VSC服务器信息上同时配置有域名和IP，则优先返回域名。*/
    @SerializedName("phsURLs")
    private List<String> phsURLs;

    /*如果调度成功，返回PHMServer中PHS桌面服务的HTTPS地址，格式为https://<PHSIP|Domain >:<PHSHttpsPort>。
    如果地址是IP形式，IP地址支持V4或V6，VSP平台根据终端接入的网络地址类型返回对应的PHS地址。
    如果VSC服务器信息上同时配置有域名和IP，则优先返回域名。*/
    @SerializedName("phsHttpsURLs")
    private List<String> phsHttpsURLs;

    /*用户归属的PHS属性，终端不需要理解，直接将该参数通过QueryPHMLauncherList传递到PHS。
    当前支持的key如下：
    subnetId：用户归属的子网运营商ID信息。
    areaId：终端所属的区域信息。
    userGroupId：用户分组信息。
    spIds：用户所属的sp信息，多个sp在一个字符串中用逗号分隔。*/
    @SerializedName("userAttribute")
    private List<NamedParameter> userAttribute;

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<String> getPhsURLs() {
        return phsURLs;
    }

    public void setPhsURLs(List<String> phsURLs) {
        this.phsURLs = phsURLs;
    }

    public List<String> getPhsHttpsURLs() {
        return phsHttpsURLs;
    }

    public void setPhsHttpsURLs(List<String> phsHttpsURLs) {
        this.phsHttpsURLs = phsHttpsURLs;
    }

    public List<NamedParameter> getUserAttribute() {
        return userAttribute;
    }

    public void setUserAttribute(List<NamedParameter> userAttribute) {
        this.userAttribute = userAttribute;
    }
}