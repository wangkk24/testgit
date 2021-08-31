package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class UpgradeRequest {

    @SerializedName("TYPE")
    private String TYPE;

    @SerializedName("STBID")
    private String STBID;

    @SerializedName("MAC")
    private String MAC;

    @SerializedName("USER")
    private String USER;

    @SerializedName("VER")
    private String VER;

    @SerializedName("CHECKSUM")
    private String CHECKSUM;

    @SerializedName("RedirectType")
    private String RedirectType;



    @SerializedName("FILENAME")
    private String FILENAME;

    private Map<String, String> mapParam = new HashMap<>();

    public String getTYPE() {
        return mapParam.get("TYPE");
    }

    public void setTYPE(String TYPE) {
        mapParam.put("TYPE", TYPE);
    }

    public String getSTBID() {
        return mapParam.get("STBID");
    }

    public void setSTBID(String STBID) {
        mapParam.put("STBID", STBID);
    }

    public String getMAC() {
        return mapParam.get("MAC");
    }

    public void setMAC(String MAC) {
        mapParam.put("MAC",MAC);
    }

    public String getUSER() {
        return mapParam.get("USER");
    }

    public void setUSER(String USER) {
        mapParam.put("USER",USER);
    }

    public String getVER() {
        return mapParam.get("VER");
    }

    public void setVER(String VER) {
        mapParam.put("VER",VER);
    }

    public String getCHECKSUM() {
        return mapParam.get("CHECKSUM");
    }

    public void setCHECKSUM(String CHECKSUM) {
        mapParam.put("CHECKSUM",CHECKSUM);
    }

    public String getRedirectType() {
        return mapParam.get("RedirectType");
    }

    public void setRedirectType(String redirectType) {
        mapParam.put("RedirectType",redirectType);
    }

    public String getFILENAME() {
        return mapParam.get("FILENAME");
    }

    public void setFILENAME(String FILENAME) {
        mapParam.put("FILENAME",FILENAME);
    }

    public Map<String, String> getRequest() {
        return mapParam;
    }
}
