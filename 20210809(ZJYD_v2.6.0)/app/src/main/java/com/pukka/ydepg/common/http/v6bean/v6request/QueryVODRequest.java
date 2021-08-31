package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class QueryVODRequest {

    @SerializedName("VODID")
    private String VODID;

    /*
     *编号的类型。
     *取值包括：
     *0: 内容内键，取值由VSP生成。
     *1: 内容Code，取值由第三方系统生成。
     */
    @SerializedName("IDType")
    private int IDType;

    /*
     *是否根据用户属性和终端能力进行过滤。
     *取值包括：
     *0: 过滤
     *1: 不过滤
     */
    @SerializedName("isFilter")
    private int isFilter;

    /*
     *是否返回所有媒资订购信息。
     *取值包括：
     *0: 不返回
     *1: 返回
     */
    @SerializedName("isReturnAllMediaSubscription")
    private int isReturnAllMediaSubscription;

    public String getVODID() {
        return VODID;
    }

    public void setVODID(String VODID) {
        this.VODID = VODID;
    }

    public int getIDType() {
        return IDType;
    }

    public void setIDType(int IDType) {
        this.IDType = IDType;
    }

    public int getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(int isFilter) {
        this.isFilter = isFilter;
    }

    public int getIsReturnAllMediaSubscription() {
        return isReturnAllMediaSubscription;
    }

    public void setIsReturnAllMediaSubscription(int isReturnAllMediaSubscription) {
        this.isReturnAllMediaSubscription = isReturnAllMediaSubscription;
    }
}
