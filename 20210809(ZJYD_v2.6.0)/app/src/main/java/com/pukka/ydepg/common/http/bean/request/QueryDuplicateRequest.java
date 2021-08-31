package com.pukka.ydepg.common.http.bean.request;

import java.util.List;

public class QueryDuplicateRequest {

    //内容推荐
    public final static String CONTENT_RECOMMEND = "201";
    //专题推荐
    public final static String SPECIAL_RECOMMEND = "203";
    //活动推荐
    public final static String EVENT_RECOMMEND = "204";

    //表示起始位置，可以不传
    private String offset;

    //表示每页内容数量，可以不传
    private String count;

    //表示场景类型，枚举值为202：内容推荐、203：专题推荐、204：活动推荐；
    private String sceneId;

    //表示需要去重的内容ID集合
    private List<String> contentIds;

    private String  vt;

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public List<String> getContentIds() {
        return contentIds;
    }

    public void setContentIds(List<String> contentIds) {
        this.contentIds = contentIds;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }
}
