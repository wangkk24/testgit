package com.pukka.ydepg.common.report.ubd.extension;

public class PushMessageData {
    //[序号1][不可为空]TV用户的标识,TV用户的标识
    String userID;

    //[序号2]
    String messageId;

    //[序号3]
    String pageTo;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPageTo() {
        return pageTo;
    }

    public void setPageTo(String pageTo) {
        this.pageTo = pageTo;
    }
}
