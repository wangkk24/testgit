package com.pukka.ydepg.common.report.problem;

import androidx.annotation.NonNull;

public class PbsErrorInfoResponse {

    private String interfaceName;

    private String errorCode;

    //错误描述
    private String errorDesc;

    //是否弹出故障二维码界面 枚举值：0 否,1 是
    private String isPop;

    //(isPop=1时有效)错误建议
    private String suggest;

    //(isPop=1时有效)界面操作入口 枚举值：0 返回,1 立即重启,2 网络设置 支持返回多项用逗号分隔
    private String operate;




    public String getIsPop() {
        return isPop;
    }

    public void setIsPop(String isPop) {
        this.isPop = isPop;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append("\n\tinterfaceName = ").append(interfaceName)
                .append("\n\terrorCode     = ").append(errorCode)
                .append("\n\terrorDesc     = ").append(errorDesc)
                .append("\n\tisPop         = ").append(isPop)       //枚举值：0 否,1 是
                .append("\n\toperate       = ").append(operate)     //界面操作入口 枚举值：0 返回,1 立即重启,2 网络设置 支持返回多项，用逗号分隔
                .append("\n\tsuggest       = ").append(suggest)
                .toString();
    }
}
