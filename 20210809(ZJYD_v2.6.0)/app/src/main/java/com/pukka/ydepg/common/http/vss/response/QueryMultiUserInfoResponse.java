package com.pukka.ydepg.common.http.vss.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QueryMultiUserInfoResponse implements Serializable {

    private static final long serialVersionUID = -735441193091502522L;

    /**
     * 返回码
     */
    @SerializedName("code")
    protected String code;
    /**
     * 返回描述
     */
    @SerializedName("description")
    protected String description;

    /**
     * 是否在网： true/在网；false/不在网
     */
    @SerializedName("isOnline")
    private String isOnline;

    /**
     * 用户编号
     */
    @SerializedName("userID")
    private String userID;

    /**
     * 用户状态：1/在用；0/暂停
     */
    @SerializedName("userState")
    private String userState;

    /**
     * 停开状态
     * 用户停开机状态按字符存储不同的停开
     * 机类型。每一位取值0表示开，1表示
     * 停。每一位的含义为：1.管理停开、2.营
     * 业停开、3.帐务停开、4.营业连带停开、
     * 5.呼出限制停开、6.帐务连带停开、7.管
     * 理连带停开、8.呼出限制连带停开、9.营
     * 业预销、10.帐务预销、11.管理预销、12.
     * 有效期停、13.无消费停机用户停开机、
     * 14. 非实名呼限、 15. 非实名停机
     */
    @SerializedName("osStatus")
    private String osStatus;

    /**
     * 停状态描述
     * OS_STATUS每一位停状态值的描述，按
     * 空格分隔
     */
    @SerializedName("osStatusDesc")
    private String osStatusDesc;

    /**
     * 实名认证状态
     * 10:非实名 11: 实名 12:待确认
     */
    @SerializedName("realNameFlag")
    private String realNameFlag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getOsStatus() {
        return osStatus;
    }

    public void setOsStatus(String osStatus) {
        this.osStatus = osStatus;
    }

    public String getOsStatusDesc() {
        return osStatusDesc;
    }

    public void setOsStatusDesc(String osStatusDesc) {
        this.osStatusDesc = osStatusDesc;
    }

    public String getRealNameFlag() {
        return realNameFlag;
    }

    public void setRealNameFlag(String realNameFlag) {
        this.realNameFlag = realNameFlag;
    }
}
