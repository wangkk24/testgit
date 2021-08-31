package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.QueryDynamicRecmContent;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: GetVODDetailRequest.java
 * @author: yh
 * @date: 2017-04-25 10:52
 */

public class GetVODDetailRequest {
/**
 * VODID :
 * IDType : 1
 * filterType : 1
 * queryDynamicRecmContent :
 * extensionFields : [""]
 */

    /**
     *
     VOD 编号
     */
    @SerializedName("VODID")
    private String VODID;

    /**
     *内容编号的类型，取值包括：

     0：内容内键，取值由VSP生成。
     1：内容Code，取值由第三方系统生成。
     默认值为0。
     */
    @SerializedName("IDType")
    private String IDType;

    /**
     *内容的过滤类型，取值包括：

     0：根据用户属性和终端能力进行过滤
     1：不做任何条件过滤
     默认值为0。

     说明：
     若接口要获取推荐内容，该参数也决定VSP是否对获取到的推荐内容做过滤。如果filterType=0，VSP会对推荐系统返回的推荐内容，根据用户的属性过滤。否则不过滤，如果对内容不做过滤，有可能播放失败。所以，建议终端在filterType为0时，才获取推荐内容。

     */
    @SerializedName("filterType")
    private String filterType;

    /**
     *该参数非空时，根据该参数获取对应的推荐内容。

     */
    @SerializedName("queryDynamicRecmContent")
    private QueryDynamicRecmContent queryDynamicRecmContent;


    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getVODID() {
        return VODID;
    }

    public void setVODID(String VODID) {
        this.VODID = VODID;
    }

    public String getIDType() {
        return IDType;
    }

    public void setIDType(String IDType) {
        this.IDType = IDType;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public QueryDynamicRecmContent getQueryDynamicRecmContent() {
        return queryDynamicRecmContent;
    }

    public void setQueryDynamicRecmContent(QueryDynamicRecmContent queryDynamicRecmContent) {
        this.queryDynamicRecmContent = queryDynamicRecmContent;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
