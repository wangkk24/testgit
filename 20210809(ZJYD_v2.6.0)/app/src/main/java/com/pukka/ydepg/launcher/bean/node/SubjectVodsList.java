package com.pukka.ydepg.launcher.bean.node;



import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 2017/8/24.
 */

public class SubjectVodsList
{
    @SerializedName("subjectID")
    private String subjectID;

    @SerializedName("vodList")
    private List<VOD> vodList;

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public List<VOD> getVodList() {
        return vodList;
    }

    public void setVodList(List<VOD> vodList) {
        this.vodList = vodList;
    }

    @Override
    public String toString() {
        return "SubjectVodsList{" +
                "subjectID='" + subjectID + '\'' +
                ", vodList=" + vodList +
                '}';
    }
}
