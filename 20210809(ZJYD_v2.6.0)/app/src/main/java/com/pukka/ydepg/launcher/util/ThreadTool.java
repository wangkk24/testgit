package com.pukka.ydepg.launcher.util;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程调度器，把子线程的方法发到主线程
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.util.ThreadTool.java
 * @date: 2018-01-12 16:22
 * @version: V1.0 描述当前版本功能
 */


public class ThreadTool {
    public static void switchMainThread(Action action){
        Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(action).subscribe();
    }

    public static void switchNewThread(Action action){
        Observable.empty().observeOn(Schedulers.newThread()).doOnComplete(action).subscribe();

    }
}
