package com.pukka.ydepg.common.http.v6bean.v6node;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: SubjectVODList.java
 * @author: yh
 * @date: 2017-04-24 11:27
 */

public class SubjectVODList  implements Parcelable {

    /**
     * subject :
     * VODs : [""]
     */

    /**
     * 栏目信息。

     */
    @SerializedName("subject")
    private Subject subject;

    /**
     * 栏目下VOD信息列表。

     */
    @SerializedName("VODs")
    private List<VOD> VODs;
    private int type;

    protected SubjectVODList(Parcel in) {
        type = in.readInt();
        subject = (Subject) in.readSerializable();
    }

    public static final Creator<SubjectVODList> CREATOR = new Creator<SubjectVODList>() {
        @Override
        public SubjectVODList createFromParcel(Parcel in) {
            return new SubjectVODList(in);
        }

        @Override
        public SubjectVODList[] newArray(int size) {
            return new SubjectVODList[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<VOD> getVODs() {
        return VODs;
    }

    public void setVODs(List<VOD> VODs) {
        this.VODs = VODs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeSerializable(subject);
    }

}
