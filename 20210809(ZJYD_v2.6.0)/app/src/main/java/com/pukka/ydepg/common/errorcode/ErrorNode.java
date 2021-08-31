package com.pukka.ydepg.common.errorcode;

import com.google.gson.annotations.SerializedName;

/**
 * error code data structure
 *
 * @author wangqing 00188401
 */
public class ErrorNode
{
    /**
     * 接口名称
     */
    @SerializedName("interfaceName")
    private String interfaceName;

    /**
     * 错误码
     */
    @SerializedName("code")
    private String code;
    /**
     * 第一语言提示语
     */
    @SerializedName("first_language")
    private String firstLanguage;

    public ErrorNode(String interfaceName, String code, String firstLanguage)
    {
        this.interfaceName = interfaceName;
        this.code = code;
        this.firstLanguage = firstLanguage;
    }

    public String getInterfaceName()
    {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getFirstLanguage()
    {
        return firstLanguage;
    }

    public void setFirstLanguage(String firstLanguage)
    {
        this.firstLanguage = firstLanguage;
    }
}