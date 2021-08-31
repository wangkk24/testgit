package com.pukka.ydepg.common.utils;


import com.pukka.ydepg.common.http.v6bean.v6node.CacheBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaxing on 2017/9/3.
 */

public class CacheUtils {
    private static final long INVALID_TIME = 90 * 1000 * 60L;

    private static Map<String, List<String>> mCategoryDetailMap = new HashMap<>();

    private static Map<String, List<String>> mCategoryListMap = new HashMap<>();

    public static void saveCategoryDetail(String catagoryId, long time, String detail) {
        List<String> list = mCategoryDetailMap.get(catagoryId);
        if (list != null && list.size() != 0) {
            mCategoryDetailMap.remove(catagoryId);
        }
        List<String> saveList = new ArrayList<>();
        saveList.add(time + "");
        saveList.add(detail);
        mCategoryDetailMap.put(catagoryId, saveList);
    }

    public static boolean getCategoryDetail(String catagoryId, long time, CacheBean cacheBean) {
        List<String> list = mCategoryDetailMap.get(catagoryId);
        if (list != null && list.size() != 0) {
            cacheBean.setCategoryDetail(list.get(1));
            long saveTime = Long.parseLong(list.get(0));
            if (time - saveTime > INVALID_TIME) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static void saveCategoryList(String catagoryId, long time, String info) {
        List<String> list = mCategoryListMap.get(catagoryId);
        if (list != null && list.size() != 0) {
            mCategoryListMap.remove(catagoryId);
        }
        List<String> saveList = new ArrayList<>();
        saveList.add(time + "");
        saveList.add(info);
        mCategoryListMap.put(catagoryId, saveList);
    }

    public static boolean getCategorList(String catagoryId, long time, CacheBean cacheBean) {
        List<String> list = mCategoryListMap.get(catagoryId);
        if (list != null && list.size() != 0) {
            cacheBean.setCategoryList(list.get(1));
            long saveTime = Long.parseLong(list.get(0));
            if (time - saveTime > INVALID_TIME) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
