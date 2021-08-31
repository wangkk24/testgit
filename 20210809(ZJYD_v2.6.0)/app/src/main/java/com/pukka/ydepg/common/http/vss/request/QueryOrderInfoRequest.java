package com.pukka.ydepg.common.http.vss.request;

public class QueryOrderInfoRequest {

    /**
     * 用户ITV业务账号
     */
    private String userID;

    /**
     * 内容Code，按次产品存在值
     */
    private String contentID;


    /**
     * 产品ID，用户订购前根据userID和productID查询订单，判断是否存在重复订单
     * 如果为营销活动，则取queryPromotion扩展属性中的bundleID（浙江暂不涉及）
     */
    private String productID;

    /**
     * 浙江暂不涉及
     */
    private String promotionID;

    /**
     * 订单ID，与productID必填其一
     */
    private String orderID;

    /**
     * 订单状态，如果不填则全部查询，多值用英文,号分割，比如0,1,2
     * λ	0：草稿状态(一般不会出现，如果出现可能原因是VSS内部出现异常)
     * λ	1：已提交状态(支付超时时订单状态变更为该状态)
     * λ	2：支付中状态(调用VSS订购接口成功后的本地订单状态，此时VSS等待支付网关通知)
     * λ	3：已支付状态(支付网关通知VSS支付成功后但未开始履约的本地订单状态)
     * λ	4：已取消状态(外部调用VSS的cancelOrder接口后的本地订单状态)
     * λ	5：已完成状态(支付成功且订购关系履约成功的本地订单状态)
     * λ	6：履约中状态(正在保存订购关系)
     * λ	7：退订中状态(正在退订订购关系)
     * λ    8：已退订状态(订购关系已经退订)
     */
    private String status;

    /**
     * 分页页数, 第一页从1开始，如第5页则传入值为5，不传默认为1
     */
    private String pageNum;

    /**
     * 每页的结果数量，不传默认使用10，如果超过配置的最大值1000，则返回错误码
     */
    private String pageSize;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(String promotionID) {
        this.promotionID = promotionID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
