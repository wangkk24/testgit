package com.pukka.ydepg.common.http.bean.request;

import java.util.List;

public class QueryRemixRecommendRequest {

    //表示起始位置，可以不传
    private String offset;

    //表示每页内容数量，可以不传
    private String count;

    //pbs与epg约定的推荐id，用于确认采用哪组推荐策略
    private String appointedId;

    //内容详情场景下的内容id
    private String vodId;

    //产品相关场景的产品id
    private String productId;

    //表示需要去重的内容ID集合
    private List<String> contentIds;

    //请求PBS返回的数据格式，传9表示以json格式返回
    private String  vt;

    public String getAppointedId() {
        return appointedId;
    }

    public void setAppointedId(String appointedId) {
        this.appointedId = appointedId;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

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
