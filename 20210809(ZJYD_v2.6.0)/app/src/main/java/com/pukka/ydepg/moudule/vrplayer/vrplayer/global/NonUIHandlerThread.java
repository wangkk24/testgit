package com.pukka.ydepg.moudule.vrplayer.vrplayer.global;

import android.os.HandlerThread;
import android.os.Looper;

import com.pukka.ydepg.moudule.vrplayer.vrplayer.utils.LogUtil;


/**
 * 独立的用于处理非主线程消息的线程。封装了HandlerThread，对外提供单例，使得不同地方可以使用同一个非主线程消息循环。
 *
 * @author l00477311
 * @since 2020-07-22
 */
public class NonUIHandlerThread extends HandlerThread {
    private static final String TAG = "NonUIHandlerThread";

    private NonUIHandlerThread(String name) {
        super(name);
    }

    /**
     * 使用静态内部类创建单例，仅当内部静态方法、静态属性、构造方法等被访问时，
     * 才对静态内部类进行初始化,延迟初始化的同时保证线程安全
     */
    public static class NonUIHandlerThreadBuilder {
        private static NonUIHandlerThread nonUIHandlerThread = new NonUIHandlerThread("NonUIHandlerThread");
    }

    /**
     * 获取非主线程消息循环线程的单例对象
     *
     * @return NonUIHandlerThread  非主线程消息循环线程
     */
    private static NonUIHandlerThread getInstance() {
        LogUtil.logI(TAG, "Enter getInstance");

        NonUIHandlerThread handlerThread = NonUIHandlerThreadBuilder.nonUIHandlerThread;
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        return handlerThread;
    }

    /**
     * 获取非主线程消息循环的单例对象中的属性Looper
     *
     * @return Looper 非主线程消息循环Looper
     */
    public static Looper getNonUILooper() {
        return getInstance().getLooper();
    }
}
