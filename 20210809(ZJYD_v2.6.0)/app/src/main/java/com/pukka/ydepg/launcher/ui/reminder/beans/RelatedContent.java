package com.pukka.ydepg.launcher.ui.reminder.beans;

import com.google.gson.annotations.SerializedName;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/10.
 * ------------------
 */

public class RelatedContent {
    @SerializedName("VOD")
    private RelatedContentVOD VOD;

    public RelatedContentVOD getVOD() {
        return VOD;
    }

    public void setVOD(RelatedContentVOD VOD) {
        this.VOD = VOD;
    }

    public RelatedContent(RelatedContentVOD VOD) {
        this.VOD = VOD;
    }

    public RelatedContent() {
    }
}
