package com.pukka.ydepg.launcher.ui.template;

import android.text.TextUtils;

/**
 * 处理各种groupType的数据
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.ui.template.DataLoaderFactory.java
 * @date: 2018-02-01 13:04
 * @version: V1.0 描述当前版本功能
 */


public class DataLoaderFactory {
    private static volatile DataLoaderFactory sInstance;

    public static DataLoaderFactory getInstance() {
        if (sInstance == null) {
            synchronized (DataLoaderFactory.class) {
                if (sInstance == null)
                    sInstance = new DataLoaderFactory();
            }
        }
        return sInstance;
    }

    private TypeThreeLoader mTypeThreeLoader;

    public DataLoaderAdapter getDataLoaderAdapter(String type) {
        if (TextUtils.isEmpty(type)){
            type = "3";
        }
        switch (type) {
            case "3"://生成groupType类型为3的数据加载器。
                if (mTypeThreeLoader == null) {
                    return new TypeThreeLoader();
                }
                return mTypeThreeLoader;

        }
        return null;
    }
}
