package com.pukka.ydepg.common.utils;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.util.HashMap;

public class PayUtil {

    public class AreaInfo {
        private String areacode;
        private String foreignsn;
        private String areaname;

        public String getAreacode() {
            return areacode;
        }

        public void setAreacode(String areacode) {
            this.areacode = areacode;
        }

        public String getForeignsn() {
            return foreignsn;
        }

        public void setForeignsn(String foreignsn) {
            this.foreignsn = foreignsn;
        }

        public String getAreaname() {
            return areaname;
        }

        public void setAreaname(String areaname) {
            this.areaname = areaname;
        }
    }

    private static final String TAG = "PayUtil";

    private static HashMap<String,AreaInfo> mapAreacode2Areainfo = new HashMap<>();

    public static void getAreaCodeJsonObject(){
        try{
            String areaCodeJsonString = ConfigUtil.getPayAreaCodeFromAssets(OTTApplication.getContext());
            mapAreacode2Areainfo = JsonParse.json2Object(areaCodeJsonString, new TypeToken<HashMap<String,AreaInfo>>() {}.getType());
        }catch (Exception e){
            SuperLog.error(TAG,e);
        }
    }

    /*
     * areaCode : ZJLogin返回结果的areaCode字段(用户区域的外部编号，认证成功后返回。用户认证成功后需要写入终端)
     */
    public static AreaInfo getAreaInfo(String areaCode){
        return mapAreacode2Areainfo.get(areaCode);
    }
}
