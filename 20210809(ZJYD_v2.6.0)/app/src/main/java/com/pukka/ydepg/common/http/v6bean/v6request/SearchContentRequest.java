package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.SearchExcluder;
import com.pukka.ydepg.common.http.v6bean.v6node.SearchFilter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchContentRequest {

    /**
     * searchKey :
     * contentTypes :
     * searchScopes :
     * subjectID :
     * filter :
     * excluder :
     * count :
     * offset :
     * sortType :
     * correction :
     * extensionFields :
     */

    /**
     * 搜索关键字。如果搜索关键字中包括特殊字符，VSP会将特殊字符替换成空格符，特殊字符包括：
     * <p>
     * ASCII码0~31和127，以及如下字符: “~,!,^,(,),[,],\\,{,},:,\,@,#,$,%,&,*,-,=,_,+,|,;,\',英文逗号,.,/,<,>,?”。
     */
    @SerializedName("searchKey")
    private String searchKey;

    /**
     * 搜索的内容类型，取值范围：
     * <p>
     * VOD：点播内容，包括音频和视频内容
     * AUDIO_VOD：点播内容，仅限于音频内容
     * VIDEO_VOD：点播内容，仅限于视频内容
     * CHANNEL：直播频道
     * AUDIO_CHANNEL：直播频道，仅限于音频
     * VIDEO_CHANNEL：直播频道，仅限于视频
     * FILECAST_CHANNEL：[字段预留]
     * PROGRAM：正在播放和未开始的直播节目单
     * TVOD：录制成功的过期节目单
     * VAS：增值业务
     */
    @SerializedName("contentTypes")
    private List<String> contentTypes;

    /**
     * 搜索范围，取值范围：
     * <p>
     * CONTENT_NAME：内容名称
     * GENRE_NAME：流派名称，仅VOD、节目单和VAS支持
     * TAGS：点播标签
     * KEYWORD：关键字，仅VOD、频道和节目单支持
     * ACTOR：演员名称
     * DIRECTOR：导演名称
     * WRITTER：作者名称
     * SINGER：演唱者名称
     * PRODUCER：制片人名称
     * ADAPTOR：编剧名称
     * OTHER_CAST：其他演职员名称
     * CAST_ID：按照演职员ID精确搜索相关的VOD，节目单内容，不与以上字段组合使用，searchKey必须为准确演职员ID。
     * ALL：所有以上字段
     */
    @SerializedName("searchScopes")
    private List<String> searchScopes;

    /**
     * 栏目ID，在指定栏目下搜索内容，栏目可以是父栏目
     */

    /**
     * 过滤条件00
     */
    @SerializedName("filter")
    private SearchFilter filter;

    /**
     * 排他条件
     */
    @SerializedName("excluder")
    private SearchExcluder excluder;

    /**
     * 一次查询的总条数
     */
    @SerializedName("count")
    private String count;

    /**
     * 查询的起始位置。
     */
    @SerializedName("offset")
    private String offset;

    /**
     * 排序方式，取值范围：
     * <p>
     * RELEVANCE：关键词与内容相关性降序
     * CONTENT_NAME:ASC：内容名称升序
     * CONTENT_NAME:DESC：内容名称降序
     * CONTENT_TYPE:ASC：内容类型升序
     * CONTENT_TYPE:DESC：内容类型降序
     * START_TIME:ASC：内容生效时间/节目单开始时间升序
     * START_TIME:DESC：内容生效时间/节目单开始时间降序
     * END_TIME:ASC：内容失效时间/节目单结束时间升序
     * END_TIME:DESC：内容失效时间/节目单结束时间降序
     * PLAY_TIMES:ASC：播放次数升序
     * PLAY_TIMES:DESC：播放次数降序
     * AVG_SCORE:ASC：评分均值升序
     * AVG_SCORE:DESC：评分均值降序
     * GENRE_NAME:ASC：流派名称升序
     * GENRE_NAME:DESC：流派名称降序
     * PERSONAL：个性化排序
     * 如果传入多个排序方式，将按照传入顺序依次排序。默认按相关性排序。
     * <p>
     * 约束：个性化排序方式不支持与其它排序方式叠加，不支持修改升降序
     */
    @SerializedName("sortType")
    private List<String> sortType;

    /**
     * 是否使用搜索关键词纠错功能，取值包括：
     * <p>
     * 1：使用关键词纠错
     * 0：使用原词搜索
     * 默认值为1
     */
    @SerializedName("correction")
    private String correction;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    @SerializedName("subjectID")
    private String subjectId;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<String> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public List<String> getSearchScopes() {
        return searchScopes;
    }

    public void setSearchScopes(List<String> searchScopes) {
        this.searchScopes = searchScopes;
    }


    public SearchFilter getFilter() {
        return filter;
    }

    public void setFilter(SearchFilter filter) {
        this.filter = filter;
    }

    public SearchExcluder getExcluder() {
        return excluder;
    }

    public void setExcluder(SearchExcluder excluder) {
        this.excluder = excluder;
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

    public List<String> getSortType() {
        return sortType;
    }

    public void setSortType(List<String> sortType) {
        this.sortType = sortType;
    }

    public String getCorrection() {
        return correction;
    }

    public void setCorrection(String correction) {
        this.correction = correction;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public interface SearchScope {
        String CONTENT_NAME = "CONTENT_NAME";
        String GENRE_NAME   = "GENRE_NAME";
        String TAGS         = "TAGS";
        String KEYWORD      = "KEYWORD";
        String ACTOR        = "ACTOR";
        String DIRECTOR     = "DIRECTOR";
        String WRITTER      = "WRITTER";
        String SINGER       = "SINGER";
        String PRODUCER     = "PRODUCER";
        String ADAPTOR      = "ADAPTOR";
        String OTHER_CAST   = "OTHER_CAST";
        String CAST_ID      = "CAST_ID";
        String ALL          = "ALL";
    }

    public interface SortType {
        String RELEVANCE         = "RELEVANCE";
        String CONTENT_NAME_ASC  = "CONTENT_NAME:ASC";
        String CONTENT_NAME_DESC = "CONTENT_NAME:DESC";
        String CONTENT_TYPE_ASC  = "CONTENT_TYPE:ASC";
        String CONTENT_TYPE_DESC = "CONTENT_TYPE:DESC";
        String START_TIME_ASC    = "START_TIME:ASC";
        String START_TIME_DESC   = "START_TIME:DESC";
        String END_TIME_ASC      = "END_TIME:ASC";
        String END_TIME_DESC     = "END_TIME:DESC";
        String PLAY_TIMES_ASC    = "PLAY_TIMES:ASC";
        String PLAY_TIMES_DESC   = "PLAY_TIMES:DESC";
        String AVG_SCORE_ASC     = "AVG_SCORE:ASC";
        String AVG_SCORE_DESC    = "AVG_SCORE:DESC";
        String GENRE_NAME_ASC    = "GENRE_NAME:ASC";
        String GENRE_NAME_DESC   = "GENRE_NAME:DESC";
        String PRODUCE_DATE_DESC = "PRODUCE_DATE:DESC";//内容出品时间降序，仅对VOD有效
        String CREATE_TIME_AES   = "CREATETIME:ASC";   //根据内容创建时间升序，仅对VOD有效
        String CREATE_TIME_DESC  = "CREATETIME:DESC";  //根据内容创建时间降序，仅对VOD有效
        String PERSONAL          = "PERSONAL";
    }
}