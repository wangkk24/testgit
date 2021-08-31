package com.pukka.ydepg.xmpp.bean;

public class CommonMessage {

    //标识此消息的具体动作，此处固定取值为"functionCall",具体的功能由参数"functionType"指定
    String action = "functionCall";

    //调用功能的类型。 当前定义如下： playResult：播放结果
    String functionType = "playResult";

    //消息来源通道 XMPP DLNA
    String msgChannel = "XMPP";

    //事务流水号. 格式如下:YYYYMMDDhhmmss+不大于18位的随机数
    String transactionId;

    //播放结果. 0:成功 其他:失败
    String playResultCode;

    //结果描述
    String playResultMsg;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getMsgChannel() {
        return msgChannel;
    }

    public void setMsgChannel(String msgChannel) {
        this.msgChannel = msgChannel;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPlayResultCode() {
        return playResultCode;
    }

    public void setPlayResultCode(String playResultCode) {
        this.playResultCode = playResultCode;
    }

    public String getPlayResultMsg() {
        return playResultMsg;
    }

    public void setPlayResultMsg(String playResultMsg) {
        this.playResultMsg = playResultMsg;
    }
}
