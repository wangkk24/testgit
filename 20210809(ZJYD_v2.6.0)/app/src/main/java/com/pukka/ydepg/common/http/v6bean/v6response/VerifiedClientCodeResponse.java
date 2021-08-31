package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;

public class VerifiedClientCodeResponse {
    //返回码
    @SerializedName("code")
    private String code;

    //返回码描述
    @SerializedName("description")
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionFromCode(String code){
        switch (code){
            case "1109040000":return "签到日期非法（浙江手机视频）";
            case "1100020002":return "系统内部错误";
            case "1100020003":return "参数校验失败";
            case "1100020004":return "验证码连续获取次数达到上限";
            case "1100020005":return "两次连续获取验证码的间隔时间过短";
            case "1100020006":return "用户已经存在（新推荐有礼场景下使用的错误码））";
            case "1100020007":return "验证码校验失败";
            case "1100020008":return "用户不存在，获取验证码失败";
            case "1100020999":return "未知错误";
            default:return "未知错误";
        }
    }
}
