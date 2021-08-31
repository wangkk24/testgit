package com.pukka.ydepg.launcher.ui.reminder.beans;

import com.google.gson.annotations.SerializedName;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/10.
 * ------------------
 */

public class RelatedContentVOD {
    @SerializedName("id")
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public RelatedContentVOD() {
    }

    public RelatedContentVOD(String ID) {
        this.ID = ID;
    }
}
