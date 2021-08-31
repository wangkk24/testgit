package com.pukka.ydepg.launcher.http.hecaiyun.data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="root")
public class LoginData {
    @Element(name = "return")
    String resultCode;
    @Element
    String imspwd;
    @Element(required = false)
    String sbc;
    @Element(required = false)
    String domain;
    @Element(required = false)
    String svnlist;
    @Element(required = false)
    String svnuser;
    @Element(required = false)
    String svnpwd;
    @Element(required = false)
    String htslist;
    @Element
    String userType;
    @Element
    String userid;

    @Element
    String loginid;
    @Element
    String heartime;
    @Element
    String funcId;
    @Element
    String token;
    @Element
    String expiretime;

    @Element
    String authToken;
    @Element
    String atExpiretime;
    @Element
    String deviceid;
    @Element
    String account;

    @Element
    String expiryDate;
    @Element
    String areaCode;
    @Element
    String provCode;
    @Element
    String srvInfoVer;

    @Element
    ServerInfo serverinfo;
    @Element
    UserExtInfo userExtInfo;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getImspwd() {
        return imspwd;
    }

    public void setImspwd(String imspwd) {
        this.imspwd = imspwd;
    }

    public String getSbc() {
        return sbc;
    }

    public void setSbc(String sbc) {
        this.sbc = sbc;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSvnlist() {
        return svnlist;
    }

    public void setSvnlist(String svnlist) {
        this.svnlist = svnlist;
    }

    public String getSvnuser() {
        return svnuser;
    }

    public void setSvnuser(String svnuser) {
        this.svnuser = svnuser;
    }

    public String getSvnpwd() {
        return svnpwd;
    }

    public void setSvnpwd(String svnpwd) {
        this.svnpwd = svnpwd;
    }

    public String getHtslist() {
        return htslist;
    }

    public void setHtslist(String htslist) {
        this.htslist = htslist;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getHeartime() {
        return heartime;
    }

    public void setHeartime(String heartime) {
        this.heartime = heartime;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAtExpiretime() {
        return atExpiretime;
    }

    public void setAtExpiretime(String atExpiretime) {
        this.atExpiretime = atExpiretime;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getSrvInfoVer() {
        return srvInfoVer;
    }

    public void setSrvInfoVer(String srvInfoVer) {
        this.srvInfoVer = srvInfoVer;
    }

    public ServerInfo getServerinfo() {
        return serverinfo;
    }

    public void setServerinfo(ServerInfo serverinfo) {
        this.serverinfo = serverinfo;
    }

    public UserExtInfo getUserExtInfo() {
        return userExtInfo;
    }

    public void setUserExtInfo(UserExtInfo userExtInfo) {
        this.userExtInfo = userExtInfo;
    }
}















