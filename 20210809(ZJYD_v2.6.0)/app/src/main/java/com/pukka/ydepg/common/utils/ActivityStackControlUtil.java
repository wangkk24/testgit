package com.pukka.ydepg.common.utils;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ActivityStackControlUtil {
    public static List<WeakReference<Activity>> getActivityList() {
        return activityList;
    }

    //activity list
    private final static List<WeakReference<Activity>> activityList = new ArrayList<WeakReference<Activity>>();

    //添加一个activity
    public static void add(WeakReference<Activity> activity) {
        activityList.add(activity);
    }

    //关闭程序
    public static void finishProgram() {
        for (WeakReference<Activity> activity : activityList) {
            if(null!=activity.get()){
                activity.get().finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //删除一个activity
    public static void remove(WeakReference<Activity> activity) {
        activityList.remove(activity);
    }
}