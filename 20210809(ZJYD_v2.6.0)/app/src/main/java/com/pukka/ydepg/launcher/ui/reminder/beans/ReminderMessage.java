package com.pukka.ydepg.launcher.ui.reminder.beans;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/9.
 * ------------------
 */

public class ReminderMessage {
    private String contentID;

    private String reminderType;

    private String vodName;

    private String sitcomNO;

    public ReminderMessage(String contentID, String vodName, String sitcomNO) {
        this.contentID = contentID;
        this.reminderType = "REMINDER_TYPE";
        this.vodName = vodName;
        this.sitcomNO = sitcomNO;
    }

    public String getSitcomNO() {
        return sitcomNO;
    }

    public void setSitcomNO(String sitcomNO) {
        this.sitcomNO = sitcomNO;
    }

    public String getVodName() {
        return vodName;
    }

    public void setVodName(String vodName) {
        this.vodName = vodName;
    }

    public ReminderMessage() {
        this.reminderType = "REMINDER_TYPE";
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    @Override
    public String toString() {
        return "ReminderMessage{" +
                "contentID='" + contentID + '\'' +
                ", reminderType='" + reminderType + '\'' +
                ", vodName='" + vodName + '\'' +
                '}';
    }
}
