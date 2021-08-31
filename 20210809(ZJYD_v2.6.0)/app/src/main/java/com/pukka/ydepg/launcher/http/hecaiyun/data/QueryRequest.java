package com.pukka.ydepg.launcher.http.hecaiyun.data;

public class QueryRequest {

    private CommonAccountInfo commonAccountInfo;

    public QueryRequest(String account) {
        this.commonAccountInfo = new CommonAccountInfo(account,"1");
    }

    public CommonAccountInfo getCommonAccountInfo() {
        return commonAccountInfo;
    }

    public void setCommonAccountInfo(CommonAccountInfo commonAccountInfo) {
        this.commonAccountInfo = commonAccountInfo;
    }
}
