package com.pukka.ydepg.launcher.ui;

import android.app.Instrumentation;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

public class MainActivityUtil {

    //模拟按键调用，使系统推出TouchMode模式，解决开机后Launcher默认无焦点，按键后焦点会出现，但不是在想要的位置上
    //https://blog.csdn.net/w1070216393/article/details/84531477
    //https://blog.csdn.net/johnWcheung/article/details/76922352
    static void simulateKeystroke(final int KeyCode) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    SuperLog.error(MainActivity.TAG,e);
                }
            }
        }).start();
    }
}