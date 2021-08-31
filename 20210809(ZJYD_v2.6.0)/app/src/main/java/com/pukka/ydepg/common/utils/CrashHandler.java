package com.pukka.ydepg.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.launcher.ui.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by f00194603 on 2015/8/18.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";

    /**
     * 单例
     */
    private static CrashHandler instance = new CrashHandler();

    /**
     * 换行符
     */
    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * 文件后缀名
     */
    private static final String SUFFIX = ".txt";

    /**
     * context
     */
    private static Context context;

    /**
     * 私有构造函数
     */
    private CrashHandler() {

    }

    /**
     * 获取实例
     *
     * @return 单例对象
     */
    public static CrashHandler getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(instance);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 这里只打印名字
        SuperLog.error(TAG, "Got uncaught exception.");

        if (null != ex) {
            SuperLog.error(TAG, ex);
            saveCrashInfoToFile(ex);
        }

//        handler.post(new MyThread());
        MainActivity activity = (MainActivity) OTTApplication.getContext().getMainActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStackImmediate(null, 1);
            activity.finish();
            SuperLog.debug(MainActivity.TAG, "finish");
        } else {
            SuperLog.debug(MainActivity.TAG, "activity = null");
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intent);
        OTTApplication.getContext().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void saveCrashInfoToFile(Throwable ex) {
        String logPath = SuperLog.getLogPath();

        // 拼装文件内容
        StringBuilder builder = new StringBuilder();
        String stackString = SuperLog.getStackString(ex);

        SuperLog.error(TAG, "Got uncaught exception = " + stackString);

        builder.append(getVersion()).append(NEW_LINE)
                .append(getTelModel()).append(NEW_LINE)
                .append("SDKVersion:" ).append(Build.VERSION.SDK_INT).append(NEW_LINE)
                .append("SVNLog:" + BuildConfig.SVN_LOG).append(NEW_LINE)
                .append(stackString);
        FileOutputStream outputStream = null;
        try {
            try {
                File logDir = new File(SuperLog.getLogPath());

                if (!logDir.exists()) {
                    if (!logDir.mkdirs()) {
                        SuperLog.warn(TAG, "Create log dir failed, stop print crash log, log dir " +
                                "is " + FileUtil.getCanonicalPath(logDir));
                        return;
                    }
                }
                String fileName = "Crash_" + new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss").format
                        (new Date()) + SUFFIX;

                // 通过拼接绝对路径构造File对象
                File outFile = new File(logPath + File.separator + fileName);

                // 输出文件
                outputStream = new FileOutputStream(outFile, false);
                outputStream.write(builder.toString().getBytes());
                outputStream.flush();
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        }
    }

    public String getVersion() {
        return  "AppVersion:" + CommonUtil.getVersionName();
    }

    private String getTelModel() {
        String user = Build.USER;
        if ("root".equalsIgnoreCase(user)) {
            return "TelModel:" + Build.MODEL;
        } else {
            return user  + ":" + Build.MODEL;
        }
    }
}