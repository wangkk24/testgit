package com.pukka.ydepg.launcher.bean.node;

import com.google.gson.annotations.SerializedName;


/**
 * the Metadata of Result
 * all the response contains the object of Result,can extends it
 *
 */
public class Result extends Metadata
{
     @SerializedName("retCode")
    private String returnCode;

     @SerializedName("retMsg")
    private String returnMessage;

    public interface ReturnCode
    {
        /**
         *field success == 0
         */
        int SUCCESS = 0;
    }

    public String getReturnCode()
    {
        return returnCode;
    }

    public void setReturnCode(String returnCode)
    {
        this.returnCode = returnCode;
    }

    public String getReturnMessage()
    {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage)
    {
        this.returnMessage = returnMessage;
    }

    @Override
    public String toString()
    {
        return "Result{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMessage='" + returnMessage + '\'' +
                '}';
    }
}