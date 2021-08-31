package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

public class Event {

    //跟踪事件类型：IMPRESSION-第三方监测曝光监测URL  CLICK-第三方监测点击监测URL
    private String type;

    //不需要使用这个字段
    private String method;

    //追踪API,基于一些业界标准的，遵循IAB定义
    private String api;

    //第三方监测的URL，包含宏定义，需要按照第3章的要求填写；
    private String url;

    //跟踪参数,可以给跟踪代码增加json格式的参数:
    private String cdata;

    //扩展信息
    private String ext;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCdata() {
        return cdata;
    }

    public void setCdata(String cdata) {
        this.cdata = cdata;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}