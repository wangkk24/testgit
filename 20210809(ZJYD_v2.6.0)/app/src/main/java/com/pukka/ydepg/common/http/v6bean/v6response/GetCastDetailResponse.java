package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.CastDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by liudo on 2018/12/10.
 */

public class GetCastDetailResponse extends BaseResponse
{

    /**
     *演职员详情。
     */
    private List<CastDetail> castDetails;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<CastDetail> getCastDetails()
    {
        return castDetails;
    }

    public void setCastDetails(List<CastDetail> castDetails)
    {
        this.castDetails = castDetails;
    }

    public List<NamedParameter> getExtensionFields()
    {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields)
    {
        this.extensionFields = extensionFields;
    }
}
