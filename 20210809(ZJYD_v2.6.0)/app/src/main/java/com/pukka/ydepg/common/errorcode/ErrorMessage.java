package com.pukka.ydepg.common.errorcode;

/**
 * face to UI 's object
 *
 * @author wangqing 00188401
 */
public class ErrorMessage
{
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误提示语，已区分语言
     */
    private String message;

    public ErrorMessage(String code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}