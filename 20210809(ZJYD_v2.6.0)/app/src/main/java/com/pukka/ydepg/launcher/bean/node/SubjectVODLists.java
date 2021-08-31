package com.pukka.ydepg.launcher.bean.node;


import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;

import java.util.List;

public class SubjectVODLists
{
    @SerializedName("subject")
    private Subject subject;

    @SerializedName("VODs")
    private List<VOD> vodList;

    public List<VOD> getVodList() {
        return vodList;
    }

    public void setVodList(List<VOD> vodList) {
        this.vodList = vodList;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
