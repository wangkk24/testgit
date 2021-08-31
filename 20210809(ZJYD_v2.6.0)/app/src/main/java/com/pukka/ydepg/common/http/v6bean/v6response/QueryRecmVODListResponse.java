package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2017/5/23.
 */

public class QueryRecmVODListResponse extends BaseResponse{

    /**
     * 推荐VOD的总个数。
     */
    @SerializedName("total")
    private String total;
    /**
     * VOD列表信息，如果查询结果为空，不返回该参数。
     */
    @SerializedName("VODs")
    private List<VOD> VODs;
    /**
     * 扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public List<VOD> getVODs() {
        return VODs;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setVODs(List<VOD> VODs) {
        this.VODs = VODs;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
