package com.pukka.ydepg.common.http.bean.response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.bean.node.OfferInfo;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;

import java.util.List;

/**
 * Created by liudo on 2018/9/17.
 */

public class QueryMultiqryResponse
{

    @SerializedName("code")
    private String code;

    @SerializedName("description")
    private String description;

    @SerializedName("addressID")
    private  String addressID;

    @SerializedName("countyCode")
    private  String countyCode;

    @SerializedName("userID")
    private  String userID;

    @SerializedName("osStatus")
    private  String osStatus;

    @SerializedName("planID")
    private  String planID;


    @SerializedName("tradeMarkID")
    private String tradeMarkID;

    @SerializedName("userType")
    private String userType;

    @SerializedName("orgID")
    private  String orgID;

    @SerializedName("planName")
    private  String planName;

    @SerializedName("custID")
    private  String custID;

    @SerializedName("urgeStopFlag")
    private  String urgeStopFlag;

    @SerializedName("lastTransDate")
    private  String lastTransDate;

    @SerializedName("tradeMarkName")
    private  String tardMarkName;


    @SerializedName("accID")
    private  String accID;


    @SerializedName("doneCode")
    private  String doneCode;

    @SerializedName("createDate")
    private  String createDate;

    @SerializedName("regionCode")
    private String regionCode;

    @SerializedName("userStatus")
    private  String userStatus;

    @SerializedName("contractNo")
    private  String contractNo;

    @SerializedName("offerList")
    private List<OfferInfo> offerList;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getAddressID()
    {
        return addressID;
    }

    public void setAddressID(String addressID)
    {
        this.addressID = addressID;
    }

    public String getCountyCode()
    {
        return countyCode;
    }

    public void setCountyCode(String countyCode)
    {
        this.countyCode = countyCode;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getOsStatus()
    {
        return osStatus;
    }

    public void setOsStatus(String osStatus)
    {
        this.osStatus = osStatus;
    }

    public String getPlanID()
    {
        return planID;
    }

    public void setPlanID(String planID)
    {
        this.planID = planID;
    }

    public String getTradeMarkID()
    {
        return tradeMarkID;
    }

    public void setTradeMarkID(String tradeMarkID)
    {
        this.tradeMarkID = tradeMarkID;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getOrgID()
    {
        return orgID;
    }

    public void setOrgID(String orgID)
    {
        this.orgID = orgID;
    }

    public String getPlanName()
    {
        return planName;
    }

    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public String getCustID()
    {
        return custID;
    }

    public void setCustID(String custID)
    {
        this.custID = custID;
    }

    public String getUrgeStopFlag()
    {
        return urgeStopFlag;
    }

    public void setUrgeStopFlag(String urgeStopFlag)
    {
        this.urgeStopFlag = urgeStopFlag;
    }

    public String getLastTransDate()
    {
        return lastTransDate;
    }

    public void setLastTransDate(String lastTransDate)
    {
        this.lastTransDate = lastTransDate;
    }

    public String getTardMarkName()
    {
        return tardMarkName;
    }

    public void setTardMarkName(String tardMarkName)
    {
        this.tardMarkName = tardMarkName;
    }

    public String getAccID()
    {
        return accID;
    }

    public void setAccID(String accID)
    {
        this.accID = accID;
    }

    public String getDoneCode()
    {
        return doneCode;
    }

    public void setDoneCode(String doneCode)
    {
        this.doneCode = doneCode;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getRegionCode()
    {
        return regionCode;
    }

    public void setRegionCode(String regionCode)
    {
        this.regionCode = regionCode;
    }

    public String getUserStatus()
    {
        return userStatus;
    }

    public void setUserStatus(String userStatus)
    {
        this.userStatus = userStatus;
    }

    public String getContractNo()
    {
        return contractNo;
    }

    public void setContractNo(String contractNo)
    {
        this.contractNo = contractNo;
    }

    public List<OfferInfo> getOfferList()
    {
        return offerList;
    }

    public void setOfferList(List<OfferInfo> offerList)
    {
        this.offerList = offerList;
    }
}
