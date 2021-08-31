package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.ContentScore;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class CreateContentScoreRequest {

    /**
     * scores :
     * extensionFields :
     */

    /**
     *用户待创建的评分
     */@SerializedName("scores")
    private List<ContentScore> scores;

    /**
     *扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<ContentScore> getScores() {
        return scores;
    }

    public void setScores(List<ContentScore> scores) {
        this.scores = scores;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
