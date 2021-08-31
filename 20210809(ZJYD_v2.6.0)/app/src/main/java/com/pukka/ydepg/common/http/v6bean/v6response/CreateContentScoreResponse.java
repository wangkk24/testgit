package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class CreateContentScoreResponse extends BaseResponse{
    /**
     * result :
     * newScores:
     * extensionFields :
     */

    /**
     * 如果评分添加成功，返回内容最新的评分均值，支持小数，返回小数点后一位小数
     */
    @SerializedName("newScores")
    private List<String> newScores;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<String> getNewScores() {
        return newScores;
    }

    public void setNewScores(List<String> newScores) {
        this.newScores = newScores;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
