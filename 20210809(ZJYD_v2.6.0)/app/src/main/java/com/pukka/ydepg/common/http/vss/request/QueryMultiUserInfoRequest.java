package com.pukka.ydepg.common.http.vss.request;

import com.pukka.ydepg.common.http.vss.node.NamedParameter;

import java.util.List;

public class QueryMultiUserInfoRequest {
    /**
     * 消息唯一标识, 生成规则:
     * appKey(6位)+deviceID(8位) + YYMMDDHHMMSS + 4位序列号
     * 本地时间.
     * appKey: 获取对应的后6位值.
     * deviceID: 预留给Partner标识服务器节点或者
     * 终端编号, 建议数字和字母组合使用.
     * YYMMDDHHMMSS: 本地时间戳
     * 4位序列号: 0000 – 9999
     */
    private String messageID;

    /**
     * 用户号码
     */
    private String billID;

    /**
     * 扩展信息
     */
    private List<NamedParameter> namedParameterList;

    public QueryMultiUserInfoRequest() {
    }

    public QueryMultiUserInfoRequest(String messageID, String billID) {
        this.messageID = messageID;
        this.billID = billID;
    }

    public QueryMultiUserInfoRequest(String messageID, String billID, List<NamedParameter> namedParameterList) {
        this.messageID = messageID;
        this.billID = billID;
        this.namedParameterList = namedParameterList;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public List<NamedParameter> getNamedParameterList() {
        return namedParameterList;
    }

    public void setNamedParameterList(List<NamedParameter> namedParameterList) {
        this.namedParameterList = namedParameterList;
    }
}
