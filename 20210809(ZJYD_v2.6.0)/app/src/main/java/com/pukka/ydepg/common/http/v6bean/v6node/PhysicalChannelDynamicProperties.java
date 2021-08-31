package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hasee on 2017/11/6.
 */

public class PhysicalChannelDynamicProperties {

    @SerializedName("ID")
    private String ID;

    @SerializedName("btvCR")
    private ContentRight btvCR;

    @SerializedName("pltvCR")
    private ContentRight pltvCR;

    @SerializedName("cpltvCR")
    private ContentRight cpltvCR;

    @SerializedName("cutvCR")
    private ContentRight cutvCR;

    @SerializedName("npvrRecordCR")
    private ContentRight npvrRecordCR;

    @SerializedName("cpvrRecordCR")
    private ContentRight cpvrRecordCR;

    @SerializedName("irCR")
    private ContentRight irCR;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ContentRight getBtvCR() {
        return btvCR;
    }

    public void setBtvCR(ContentRight btvCR) {
        this.btvCR = btvCR;
    }

    public ContentRight getPltvCR() {
        return pltvCR;
    }

    public void setPltvCR(ContentRight pltvCR) {
        this.pltvCR = pltvCR;
    }

    public ContentRight getCpltvCR() {
        return cpltvCR;
    }

    public void setCpltvCR(ContentRight cpltvCR) {
        this.cpltvCR = cpltvCR;
    }

    public ContentRight getCutvCR() {
        return cutvCR;
    }

    public void setCutvCR(ContentRight cutvCR) {
        this.cutvCR = cutvCR;
    }

    public ContentRight getNpvrRecordCR() {
        return npvrRecordCR;
    }

    public void setNpvrRecordCR(ContentRight npvrRecordCR) {
        this.npvrRecordCR = npvrRecordCR;
    }

    public ContentRight getCpvrRecordCR() {
        return cpvrRecordCR;
    }

    public void setCpvrRecordCR(ContentRight cpvrRecordCR) {
        this.cpvrRecordCR = cpvrRecordCR;
    }

    public ContentRight getIrCR() {
        return irCR;
    }

    public void setIrCR(ContentRight irCR) {
        this.irCR = irCR;
    }
}
