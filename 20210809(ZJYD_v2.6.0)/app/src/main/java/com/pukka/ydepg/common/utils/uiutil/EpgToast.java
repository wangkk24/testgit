package com.pukka.ydepg.common.utils.uiutil;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.R;

/**
 * Tool class to show a toast.<br/>
 * <p/>
 *
 * @author zhouliyun n005689
 */
public final class EpgToast {

    private static final String TAG = EpgToast.class.getSimpleName();

    private static Toast toast = new Toast(OTTApplication.getContext());

    private EpgToast() { }

    public static void showToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void debugToast(String message){
        if(BuildConfig.DEBUG){
            showToast(OTTApplication.getContext(), message, Toast.LENGTH_SHORT);
        }
    }

    /**
     * show toast.
     * 1）not display when app is not active.
     * 2）if an old toast is showing when a new one will show, close the old one first.
     *
     * @param context  context
     * @param message  message to show
     * @param duration duration for show
     */
    private static void showToast(Context context, String message, int duration) {
        // Display the presence of toast, cancel the previous one
        //        toast.cancel();
        if (null != context) {
            // When the application is in the background, discard the message
            if (OTTApplication.isApplicationBroughtToBackground(context)) {
                SuperLog.debug(TAG, "Application is background,discard toast, message is " + message);
                return;
            }
            if (toast == null) {
                toast = new Toast(OTTApplication.getContext().getApplicationContext());
            }
            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflate.inflate(R.layout.toast_layout, null);
            TextView tv = v.findViewById(R.id.message);
            tv.setText(message);
            toast.setView(v);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            toast.setDuration(duration);
            toast.show();
            SuperLog.debug(TAG, "Toast message is: " + message);
        } else {
            SuperLog.error(TAG, "Context is null, can not show toast.");
        }
    }
}