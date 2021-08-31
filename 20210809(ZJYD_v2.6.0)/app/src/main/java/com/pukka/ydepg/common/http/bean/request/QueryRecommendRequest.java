package com.pukka.ydepg.common.http.bean.request;

public class QueryRecommendRequest {

   public  interface  SecenarizedType{
        //产品退订
        String PRODUCT_UNSUBSCRIBE="0";
        //内容详情
        String CONTENT_DETAIL="1";
        //猜你爱看
        String GUESS_YOU_LIKE="2";
        //精彩推荐
        String RECOMMEND="3";

    }



    private String offset;

    private String count;

    private String scenarizedType;

    private String proudctId;

    private String vodId;

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

    public String getScenarizedType() {
        return scenarizedType;
    }

    public void setScenarizedType(String scenarizedType) {
        this.scenarizedType = scenarizedType;
    }

    public String getProudctId() {
        return proudctId;
    }

    public void setProudctId(String proudctId) {
        this.proudctId = proudctId;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }
}
