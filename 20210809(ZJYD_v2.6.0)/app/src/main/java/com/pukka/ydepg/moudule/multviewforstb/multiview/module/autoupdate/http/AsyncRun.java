package com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.http;

import android.os.Handler;
import android.os.Looper;

import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.ResultBean;


public final class AsyncRun {
    private static final String TAG = AsyncRun.class.getSimpleName();

    public static void runSuccess(final ResultCallback callback,final ResultBean response){
        Handler handler = new Handler(Looper.getMainLooper());
        if(callback != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(response);
                }
            });
        }
    }

    public static void runError(final ResultCallback callback , final String error){
        Handler handler = new Handler(Looper.getMainLooper());
        if(callback != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(error);
                }
            });
        }
    }

    public static void runProgress(final ResultCallback callback,final long total ,final long downloadedSize){
        Handler handler = new Handler(Looper.getMainLooper());
        if(callback != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onDownloadProgress(total,downloadedSize);
                }
            });
        }
    }
}
