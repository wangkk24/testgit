package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Genre.java
 * @author: yh
 * @date: 2017-04-21 17:53
 */

public class Genre implements Serializable {


    /**
     * genreID : 11
     * genreType : 111
     * genreName :
     * extensionFields : [""]
     */

    /**
     *流派编号。

     */
    @SerializedName("genreID")
    private String genreID;

    /**
     *流派类型，取值包括：

     1：音乐类，供音频内容使用
     2：影视类，供视频内容使用
     6：信息类，供VAS内容使用
     7：频道节目类，供频道和节目单使用
     */
    @SerializedName("genreType")
    private String genreType;

    /**
     *流派名称。

     */
    @SerializedName("genreName")
    private String genreName;

    /**
     *局点定制的扩展字段。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getGenreID() {
        return genreID;
    }

    public void setGenreID(String genreID) {
        this.genreID = genreID;
    }

    public String getGenreType() {
        return genreType;
    }

    public void setGenreType(String genreType) {
        this.genreType = genreType;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
