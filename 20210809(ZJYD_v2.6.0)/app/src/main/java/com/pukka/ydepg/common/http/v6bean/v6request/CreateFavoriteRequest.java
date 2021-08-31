package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Favorite;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class CreateFavoriteRequest {

    /**
     * favorites :
     * autoCover :
     * extensionFields :
     */

    /**
     * 用户待创建的收藏内容
     */
    @SerializedName("favorites")
    private List<Favorite> favorites;

    /**
     * 当添加收藏到达上限，是否支持自动覆盖最老的收藏，取值包括：
     * <p>
     * 0：不支持
     * 1：支持
     * 默认值为1
     */
    @SerializedName("autoCover")
    private String autoCover;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public String getAutoCover() {
        return autoCover;
    }

    public void setAutoCover(String autoCover) {
        this.autoCover = autoCover;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
