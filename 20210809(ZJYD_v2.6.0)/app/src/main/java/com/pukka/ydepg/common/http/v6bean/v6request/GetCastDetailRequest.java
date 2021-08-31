package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;


/**
 * Created by liudo on 2018/12/10.
 */

public class GetCastDetailRequest
{

    /**
     * Cast ID列表
     */
    @SerializedName("castIds")
    private List<String>  castIds;


    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;



    public List<NamedParameter> getExtensionFields()
    {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields)
    {
        this.extensionFields = extensionFields;
    }

    public List<String> getCastIds()
    {
        return castIds;
    }

    public void setCastIds(List<String> castIds)
    {
        this.castIds = castIds;
    }
}
