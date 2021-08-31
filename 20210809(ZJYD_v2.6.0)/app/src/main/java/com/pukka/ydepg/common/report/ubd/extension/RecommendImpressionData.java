package com.pukka.ydepg.common.report.ubd.extension;

public class RecommendImpressionData {

    //[通用]业务场景ID,业务场景固定
    private String appPointedId;

    //[通用]场景ID
    private String sceneId;

    //[通用]推荐内容ID
    private String contentIds;

    //[通用]推荐方式(智能推荐/手工推荐)
    private String recommendType;

    //[定制]产品包ID,产品过渡页推荐场景上报此字段
    private String packageId;

    //[定制]VOD内容ID,播放退出页面上报此字段
    private String contentId;



    public String getAppPointedId() {
        return appPointedId;
    }

    public void setAppPointedId(String appPointedId) {
        this.appPointedId = appPointedId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getContentIds() {
        return contentIds;
    }

    public void setContentIds(String contentIds) {
        this.contentIds = contentIds;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}