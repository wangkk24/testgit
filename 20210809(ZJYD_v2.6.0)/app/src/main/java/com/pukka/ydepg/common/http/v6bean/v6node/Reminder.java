package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Reminder.java
 * @author: yh
 * @date: 2017-04-21 17:14
 */

public class Reminder implements Serializable{


    /**
     * contentID :
     * contentType :
     * reminderTime : 423
     * extensionFields : [""]
     */

    /**
     *内容ID

     */
    @SerializedName("contentID")
    private String contentID;

    /**
     *内容类型,取值包括：

     PROGRAM

     */
    @SerializedName("contentType")
    private String contentType;

    /**
     *提醒时间，取值为距离1970年1月1号的毫秒数。

     如果新增提醒，对于VOD提醒必填，对于节目单提醒，如果不设置，系统会根据用户设置的提前偏移量确定提醒时间，其中用户设置的提前偏移量参见Profile对象的leadTimeForSendReminder属性。

     如果查询提醒，参数取值非空。

     */
    @SerializedName("reminderTime")
    private long reminderTime;

    /**
     *局点定制的扩展属性。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
