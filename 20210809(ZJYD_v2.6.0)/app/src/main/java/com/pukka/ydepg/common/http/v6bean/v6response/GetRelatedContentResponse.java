package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.launcher.ui.reminder.beans.RelatedContent;

import java.util.List;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/9.
 * ------------------
 */

public class GetRelatedContentResponse {
    /*返回结果*/
    @SerializedName("result")
    private Result result;
    /*固移融合关联查询出的内容信息，当前仅支持返回VOD内容，同时为了兼容现网性能要求，VOD对象中默认只返回VOD的ID属性*/
    @SerializedName("contents")
    private RelatedContent[] contents;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public GetRelatedContentResponse(Result result, RelatedContent[] contents, List<NamedParameter> extensionFields) {
        this.result = result;
        this.contents = contents;
        this.extensionFields = extensionFields;
    }

    public GetRelatedContentResponse() {
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public RelatedContent[] getContents() {
        return contents;
    }

    public void setContents(RelatedContent[] contents) {
        this.contents = contents;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
