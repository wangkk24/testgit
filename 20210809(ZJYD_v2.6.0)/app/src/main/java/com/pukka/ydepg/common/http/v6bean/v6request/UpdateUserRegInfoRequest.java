package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liudo on 2018/5/15.
 */

public class UpdateUserRegInfoRequest {

    @SerializedName("loginName")
    private String loginName;

    @SerializedName("verfiyCode")
    private String verifiyCode;

    @SerializedName("msgType")
    private int msgType;

    @SerializedName("newValue")
    private String newValue;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getVerifiyCode() {
        return verifiyCode;
    }

    public void setVerifiyCode(String verifiyCode) {
        this.verifiyCode = verifiyCode;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public interface MsgType{
        int RESET_EMAIL=3;
        int RESET_PHONE=4;
    }
}
