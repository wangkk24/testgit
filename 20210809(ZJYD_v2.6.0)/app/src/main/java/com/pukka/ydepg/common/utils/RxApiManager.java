package com.pukka.ydepg.common.utils;

import android.util.ArrayMap;

import com.pukka.ydepg.launcher.util.RxCallBack;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by liudo on 2018/3/21.
 */

public class RxApiManager {
    private static RxApiManager sInstance = null;

    private ArrayMap<String, RxCallBack> maps;

    public static synchronized RxApiManager get() {
        if (sInstance == null) {
            sInstance = new RxApiManager();
        }
        return sInstance;
    }

    private RxApiManager() {
        maps = new ArrayMap<>();
    }

    public void add(String url, RxCallBack reCallBack) {
        String key = url + generateSequence();
        maps.put(key, reCallBack);
    }

    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, RxCallBack>> it = maps.entrySet().iterator();
        if (null != it) {
            while (it.hasNext()) {
                Map.Entry<String, RxCallBack> entry = it.next();
                if (null!=entry.getValue()&&!entry.getValue().isDisposed()) {
                    entry.getValue().dispose();
                }
                it.remove();
            }
        }
    }

    private String generateSequence() {
        long current = System.currentTimeMillis();
        return String.valueOf(current);
    }
}