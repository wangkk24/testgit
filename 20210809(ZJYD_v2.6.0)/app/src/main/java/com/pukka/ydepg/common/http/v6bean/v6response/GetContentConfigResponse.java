package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.Genre;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.ProduceZone;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: GetContentConfigResponse.java
 * @author: yh
 * @date: 2017-04-25 10:50
 */

public class GetContentConfigResponse extends BaseResponse{


    /**
     * result : Result
     * produceZones : [""]
     * genres : [""]
     * extensionFields : [""]
     */

    /**
     *内容的出品地信息。

     */
    @SerializedName("produceZones")
    private List<ProduceZone> produceZones;

    /**
     *内容的流派信息。

     */
    @SerializedName("genres")
    private List<Genre> genres;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<ProduceZone> getProduceZones() {
        return produceZones;
    }

    public void setProduceZones(List<ProduceZone> produceZones) {
        this.produceZones = produceZones;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
