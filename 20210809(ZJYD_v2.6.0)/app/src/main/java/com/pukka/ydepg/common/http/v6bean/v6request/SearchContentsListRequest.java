package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 热搜内容查询
 *
 * @version V1.0
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.SearchContentsListRequest.java
 * @author:xj
 * @date: 2017-12-18 14:31
 */

public class SearchContentsListRequest {

    /**
     * 一次查询的总条数
     */
    @SerializedName("count")
    private String count;

    /**
     * 查询的起始位，0表示第一个
     */
    @SerializedName("offset")
    private String offset;

    /**
     *要查询的热门搜索内容排行榜类型，后续支持扩展
     *取值包括：
     *•ALL: 总排行榜，用来呈现热搜内容名称
     *•Series: VOD电视剧
     *•Movies: VOD电影
     *•Program: 节目单
     */
    @SerializedName("boardType")
    private String boardType;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public interface BoardType{
        String ALL = "ALL";
        String SERIES = "Series";
        String MOVIES = "Movies";
        String PROGRAM = "Program";
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
