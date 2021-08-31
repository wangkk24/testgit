package com.pukka.ydepg.moudule.children.report;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Eason on 08-Apr-19.
 */

public class ChildrenConstant {

    public static LinkedHashMap<String ,String> mSingleDurationMap = new LinkedHashMap<>();

    public static LinkedHashMap<String,String>  mAllDurationMap    = new LinkedHashMap<>();

    static {
        mSingleDurationMap.put("-1","不限制");
        mSingleDurationMap.put("15","15 分钟");
        mSingleDurationMap.put("30","30 分钟");
        mSingleDurationMap.put("45","45 分钟");
        mSingleDurationMap.put("60","60 分钟");
        mSingleDurationMap.put("90","90 分钟");

        mAllDurationMap.put("-1","不限制");
        mAllDurationMap.put("30","30 分钟");
        mAllDurationMap.put("60","60 分钟");
        mAllDurationMap.put("90","90 分钟");
        mAllDurationMap.put("120","120 分钟");
        mAllDurationMap.put("180","180 分钟");

    }

    public static Object getKey(Map map, Object value){
        //List<Object> keyList = new ArrayList<>();
        for(Object key: map.keySet()){
            if(map.get(key).equals(value)){
                //keyList.add(key);
                return key;
            }
        }
//        if(keyList.size()>0){
//            return keyList.get(0);
//        }
        return "";
    }

    /**
     * viewType:
     * 1:单次时长
     * 2:总时长
     * 3:生日
     * 4:性别
     * 5:切换界面
     * 6:unlock解锁界面数学题
     */
    public interface VIEWTYPE{
        Integer SINGLETIME = 1;
        Integer ALLTIME    = 2;
        Integer BIRTHDAY   = 3;
        Integer GENDER     = 4;
        Integer SWITCHEPG  = 5;
        Integer UNLOCK     = 6;
    }
}