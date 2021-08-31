package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryVODListBySubjectResponse.java
 * @author: yh
 * @date: 2017-04-25 10:45
 */

public class QueryVODListBySubjectResponse extends BaseResponse{


    /**
     * result : Result
     * total : 12
     * VODs : [""]
     * extensionFields : [""]
     */

    /**
     *VOD总个数。

     */
    @SerializedName("total")
    private String total;

    /**
     *VOD列表信息，VOD属性请参见“VOD”类型。

     如果查询结果为空，不返回该参数。

     */
    @SerializedName("VODs")
    private List<VOD> VODs;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<VOD> getVODs() {
        return VODs;
    }

    public void setVODs(List<VOD> VODs) {
        this.VODs = VODs;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
