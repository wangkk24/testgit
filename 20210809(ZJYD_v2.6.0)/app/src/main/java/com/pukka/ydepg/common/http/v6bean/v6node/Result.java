package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/24.
 */

public class Result {


    public static final String RETCODE_OK = "000000000";
    public static final String RETCODE_OK_TWO = "0";
    public static final String RETCODE_LOCK = "146021007";//频道加锁
    public static final String RETCODE_PROGRAM_LOCK = "146021008";//节目单加锁
    public static final String RETCODE_PASSWARD_FAIL = "157021009";//解锁密码错误
    public static final String RETCODE_VOD_CONTROL = "144020011";//VOD被受控
    public static final String RETCODE_CHANNEL_CONTROL = "146021007";//频道被受控
    public static final String RETCODE_MUTEX = "10030164";//订购产品包存在互斥
    public static final String RETCODE_VERIFIED_WRONG = "10030156";//订购验证码错误

    /**
     * retCode :
     * retMsg :
     */

    @SerializedName("retCode")
    private String retCode;

    @SerializedName("retMsg")
    private String retMsg;

    @SerializedName("returnCode")
    private String returnCode;

    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("description")
    private String description;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "retCode='" + retCode + '\'' +
                ", retMsg='" + retMsg + '\'' +
                '}';
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
