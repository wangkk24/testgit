package com.pukka.ydepg.common.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.json.JsonSanitizer;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: JsonParse.java
 * @author: yh
 * @date: 2016-10-31 10:10
 */
public class JsonParse {

    private static final String TAG = JsonParse.class.getSimpleName();

    private static Gson gson;

    static {
        gson = new GsonBuilder().create();
    }

    /**
     *
     * @param json
     * @return
     */
    public static String object2String(final Object json) {
        try {
            return gson.toJson(json);
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            return null;
        }
    }

    /**
     * object2String
     * @param json
     * @return
     */
    public static String object2String(final Object json,Type type) {
        try {
            return gson.toJson(json,type);
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            return null;
        }
    }

    /**
     * List换json
     * @param list
     * @return
     */
    public static String listToJsonString(List list) {
        return gson.toJson(list);
    }

    /**
     * List<Object>换json
     * @param list
     * @param elementClasses
     * @return
     */
    public static String classListToJson(List list,Class<?> elementClasses){
        try{
            return gson.toJson(list);
        }catch(Exception e){
            SuperLog.error(TAG,e);
            return "";
        }
    }






    /**
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String json, Class<T> classOfT){
        try {
            String secureJson = JsonSanitizer.sanitize(json);
            return gson.fromJson(secureJson,classOfT);
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            return null;
        }
    }

    /**
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String json, Type type){
        try {
            String secureJson = JsonSanitizer.sanitize(json);
            return gson.fromJson(secureJson,type);
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            return null;
        }
    }

    /**
     * json转 List<Object>
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> jsonToClassList(String json,Class<T> cls){
        if(!TextUtils.isEmpty(json)){
            try {
                JsonArray array = new JsonParser().parse(json).getAsJsonArray();
                if(null!=array && array.size()>0){
                    List<T> list = new ArrayList<>();
                    for(final JsonElement elem : array){
                        list.add(gson.fromJson(elem, cls));
                    }
                    return list;
                }
            }catch (IllegalStateException e){
                SuperLog.error(TAG,e);
            }
        }
        return null;
    }

    /**
     * json转List<String>
     * @param json
     * @return
     */
    public static List<String>  jsonToStringList(String json) {
        String secureJson = JsonSanitizer.sanitize(json);
        List<String> strList = gson.fromJson(secureJson, new TypeToken<List<String>>(){}.getType());
        return strList;
    }

    public static Map<String, String> jsonToMap(String jsonStr) {
        HashMap<String, String> map;
        try {
            String secureJson = JsonSanitizer.sanitize(jsonStr);
            map = gson.fromJson(secureJson, new TypeToken<HashMap<String, String>>() {}.getType());
        } catch (Exception e) {
            SuperLog.error(TAG,e);
            map = new HashMap<>();
        }
        return map;
    }
}