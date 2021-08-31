package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObject;
import com.pukka.ydepg.common.http.v6bean.v6node.PriceObjectForVSS;

import java.util.List;
import java.util.Map;

/**
 * 产品订购信息
 * Created by liudo on 2018/9/14.
 */

public class SubProdInfo
{
    /**
     * 1.一次性产品
     * 2.包周期产品
     */
    @SerializedName("type")
    private String type;

    @SerializedName("productId")
    private String productId;

    /**
     * 订购数量
     */
    @SerializedName("amount")
    private String amount;
    /**
     * 自动续订标识
     * 0：非自动续订
     * 1或null 自动续订
     */
    @SerializedName("renewFlag")
    private String renewFlag;

    @SerializedName("object")
    private PriceObjectForVSS object;

    @SerializedName("extentionInfo")
    private Map<String,String> extensionInfo;

    public interface SubProdInfoType{
        String ONCE_PRODUCT="1";

        String PERIOD_PRODUCT="2";

    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getRenewFlag()
    {
        return renewFlag;
    }

    public void setRenewFlag(String renewFlag)
    {
        this.renewFlag = renewFlag;
    }

    public PriceObjectForVSS getObject()
    {
        return object;
    }

    public void setObject(PriceObjectForVSS object)
    {
        this.object = object;
    }

    public Map<String,String> getExtensionInfo()
    {
        return extensionInfo;
    }

    public void setExtensionInfo(Map<String,String> extensionInfo)
    {
        this.extensionInfo = extensionInfo;
    }
}
