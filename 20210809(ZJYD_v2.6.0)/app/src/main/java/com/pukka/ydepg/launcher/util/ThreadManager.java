package com.pukka.ydepg.launcher.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.util.ThreadManager.java
 * @date: 2018-03-08 16:49
 * @version: V1.0 描述当前版本功能
 */

public class ThreadManager {

    private ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    private ThreadManager() { }

    public ExecutorService getSingleThreadPool() {
        return singleThreadPool;
    }



    private static volatile ThreadManager sInstance;

    public static ThreadManager getInstance() {
        if (sInstance == null) {
            synchronized (ThreadManager.class) {
                if (sInstance == null)
                    sInstance = new ThreadManager();
            }
        }
        return sInstance;
    }
}