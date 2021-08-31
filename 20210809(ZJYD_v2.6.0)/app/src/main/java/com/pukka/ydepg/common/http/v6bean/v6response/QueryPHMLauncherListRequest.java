package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.DesktopInfos;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;

import java.util.List;

public class QueryPHMLauncherListRequest {



    //O 在根据桌面ID获取桌面文件下载方式的时候需要填写该参数。如果未填写该参数则平台根据终端请求条件返回对应的桌面。
    @SerializedName("desktopIDs")
    private List<String> desktopIDs;

    //M 填写终端登陆认证请求Authenticate的入参authenticateDevice.deviceModel。
    @SerializedName("deviceModel")
    private String deviceModel;

    //M 该字段填写用户认证返回的userToken。目前PHS对该字段不校验
    @SerializedName("userToken")
    private String userToken;

    /*M 该字段填写用户LoginPHSRoute返回的userAttribute。
    当前支持的key如下：
    subnetId：用户归属的子网运营商ID信息。
    areaId：终端所属的区域信息。
    userGroupId：用户分组信息。
    spIds：用户所属的sp信息，多个sp在一个字符串中用逗号分隔。*/
    @SerializedName("userAttribute")
    private List<NamedParameter> userAttribute;

    //O 终端自定义版本号。
    @SerializedName("terminalVersion")
    private String terminalVersion;

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<String> getDesktopIDs() {
        return desktopIDs;
    }

    public void setDesktopIDs(List<String> desktopIDs) {
        this.desktopIDs = desktopIDs;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<NamedParameter> getUserAttribute() {
        return userAttribute;
    }

    public void setUserAttribute(List<NamedParameter> userAttribute) {
        this.userAttribute = userAttribute;
    }

    public String getTerminalVersion() {
        return terminalVersion;
    }

    public void setTerminalVersion(String terminalVersion) {
        this.terminalVersion = terminalVersion;
    }
}