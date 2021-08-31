package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class QueryRecmContentResponse extends BaseResponse{

    /**
     * result :
     * recmActionID :
     * recmContents :
     * extensionFields :
     */

    /**
     * 推荐请求流水号。
     * <p>
     * 字段由推荐系统产生返回给VSP透传，用于唯一标记某个内容推荐请求操作，用于后续与用户推荐结果反馈关联
     */
    @SerializedName("recmActionID")
    private String recmActionID;

    /**
     * 推荐结果列表。
     * <p>
     * 说明：
     * 由于推荐接口支持1次批量查询多个推荐位列表，因此推荐结果数组数量、数组内数据顺序必须和推荐请求参数RecmScenario指定的推荐位参数相同并且一一对应。如果某个推荐请求对应的推荐结果内容没有数据则推荐结果数组中对应对象返回空
     */
    @SerializedName("recmContents")
    private List<RecmContents> recmContents;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getRecmActionID() {
        return recmActionID;
    }

    public void setRecmActionID(String recmActionID) {
        this.recmActionID = recmActionID;
    }

    public List<RecmContents> getRecmContents() {
        return recmContents;
    }

    public void setRecmContents(List<RecmContents> recmContents) {
        this.recmContents = recmContents;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
