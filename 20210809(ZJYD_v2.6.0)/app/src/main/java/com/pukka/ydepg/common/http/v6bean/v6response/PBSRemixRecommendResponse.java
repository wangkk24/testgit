package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Picture;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.launcher.bean.node.Apk;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;
import com.pukka.ydepg.launcher.bean.node.Topic;

import java.util.List;


public class PBSRemixRecommendResponse extends BaseResponse {
    /**
     * 终端所需要的开机画面的名称
     */
    @SerializedName("recommends")
    private List<Recommend> recommends;

    public List<Recommend> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<Recommend> recommends) {
        this.recommends = recommends;
    }
}



