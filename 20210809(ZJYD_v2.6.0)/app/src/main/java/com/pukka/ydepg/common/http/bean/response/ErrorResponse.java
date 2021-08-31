package com.pukka.ydepg.common.http.bean.response;

import com.google.gson.annotations.SerializedName;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: ErrorResponse.java
 * @author: yh
 * @date: 2016-11-07 12:58
 */

public class ErrorResponse {

    private static final String TAG = "ErrorResponse";

    @SerializedName("error")
    private String error;

    @SerializedName("error_description")
    private String errorMsg;

    @SerializedName("errorCode")
    private String errorCode;

    @SerializedName("errorType")
    private String errorType;

    @SerializedName("errorName")
    private String errorName;

    @SerializedName("errorDesc")
    private String errorDesc;

    @SerializedName("errorResolve")
    private String errorResolve;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorResolve() {
        return errorResolve;
    }

    public void setErrorResolve(String errorResolve) {
        this.errorResolve = errorResolve;
    }

    public ErrorResponse() {
    }
}