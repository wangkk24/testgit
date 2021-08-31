package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

import com.google.gson.annotations.SerializedName;

public class UserIdentification {

    public interface  IdType{
        String USERID = "USERID";
        String IMEI   = "IMEI";
        String TELNUM = "TELNUM";
        String TVID   = "TVID";
    }

    @SerializedName("idType")
    private String idType;

    @SerializedName("idValue")
    private String idValue;

    public String getIdType()
    {
        return idType;
    }

    public void setIdType(String idType)
    {
        this.idType = idType;
    }

    public String getIdValue()
    {
        return idValue;
    }

    public void setIdValue(String idValue)
    {
        this.idValue = idValue;
    }
}