package com.pukka.ydepg.common.report.error;

import com.google.gson.annotations.SerializedName;

public class ErrorInfo
{
    /**
     * 接口名称
     */
    @SerializedName("interfaceName")
    private String interfaceName;

    /**
     * 内部错误码
     */
    @SerializedName("internalErrorCode")
    private String internalErrorCode;

    /**
     * 外部错误码
     */
    @SerializedName("externalErrorCode")
    private String externalErrorCode;


    public String getInterfaceName()
    {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName;
    }

    public String getInternalErrorCode()
    {
        return internalErrorCode;
    }

    public void setInternalErrorCode(String internalErrorCode) {
        this.internalErrorCode = internalErrorCode;
    }

    public String getExternalErrorCode() {
        return externalErrorCode;
    }

    public void setExternalErrorCode(String externalErrorCode) {
        this.externalErrorCode = externalErrorCode;
    }
}
