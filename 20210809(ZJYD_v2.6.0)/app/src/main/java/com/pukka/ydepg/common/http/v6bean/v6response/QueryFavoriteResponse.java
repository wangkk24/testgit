package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class QueryFavoriteResponse extends BaseResponse{

    /**
     * result :
     * total :
     * version :
     * favorites :
     * extensionFields :
     */

    /**
     * 如果查询成功，返回条件匹配的总记录数
     */
    @SerializedName("total")
    private String total;

    /**
     * 如果查询成功，返回收藏版本
     */
    @SerializedName("version")
    private String version;

    /**
     * 如果查询成功，返回个性化收藏信息
     */
    @SerializedName("favorites")
    private List<Content> favorites;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Content> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Content> favorites) {
        this.favorites = favorites;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
