package com.pukka.ydepg.common.utils.LogUtil;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.common.utils.DeviceInfo;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.common.utils.fileutil.PathManager;
import com.pukka.ydepg.launcher.util.ThreadManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class SuperLog {

    private static final String TAG         = "OTT|";

    public  static final String NEW_LINE    = System.getProperty("line.separator");

    public  static final String NULL_TIPS   = "Log with null object";

    private static final String LOG_PATH    = PathManager.getLogPath();

    private static final String FILE_TYPE   = ".txt";

    public  static final int JSON_INDENT    = 4;

    private static final long ERR_LOG_SIZE  = 1024L * 1024;

    private static boolean needDebug        = true;

    private static boolean WRITE_SDCARD_LOG = false;

    private static volatile File logfile;

    public static final int V = 0x1;
    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;
    public static final int E = 0x5;
    public static final int A = 0x6;

    private SuperLog() {}

    public static void setEnable(boolean isEnableLog, boolean isWrite2SD) {
        needDebug = isEnableLog;
        WRITE_SDCARD_LOG = isWrite2SD;
    }

    public static boolean enabled() {
        return needDebug;
    }

    public static String getLogPath() {
        return LOG_PATH;
    }

    private static String buildMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return "log is null";
        }
        msg = formatMsg(msg, 800);
        StringBuilder buffer = new StringBuilder();
        Thread currentThread = Thread.currentThread();
        final StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
        buffer.append("[");
        buffer.append(currentThread.getName());
        buffer.append(" |");
        buffer.append(stackTraceElement.getFileName());
        buffer.append(" |");
        buffer.append(stackTraceElement.getMethodName());
        buffer.append("() |Line:");
        buffer.append(stackTraceElement.getLineNumber());
        buffer.append("] ");
        buffer.append(msg);
        buffer.append(NEW_LINE);
        return buffer.toString();
    }

    private static String buildMsg(String msg,String threadName,final StackTraceElement stackTraceElement) {
        if (TextUtils.isEmpty(msg)) {
            return "log is null";
        }

        msg = formatMsg(msg, 800);
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        buffer.append(threadName);
        buffer.append(" |");
        buffer.append(stackTraceElement.getFileName());
        buffer.append(" |");
        buffer.append(stackTraceElement.getMethodName());
        buffer.append("() |Line:");
        buffer.append(stackTraceElement.getLineNumber());
        buffer.append("] ");
        buffer.append(msg);
        buffer.append(NEW_LINE);
        return buffer.toString();
    }

    /**
     * 拆分日志 每行不超过size
     *
     * @param msg
     * @return
     */
    private static String formatMsg(String msg, int size) {
        if (TextUtils.isEmpty(msg) || msg.length() < size) {
            return msg;
        }
        StringBuilder formatMsg = new StringBuilder();
        while (true) {
            if (msg.length() >= size) {
                formatMsg.append(msg.substring(0, size)).append("\n");
                msg = msg.substring(size);
                continue;
            } else {
                formatMsg.append(msg).append("\n");
            }
            break;
        }

        return formatMsg.toString();
    }

    private static String getTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return format.format(date);
    }

    public static void verbose(String tag, String msg) {
        if (enabled()) {
            Log.v(TAG + tag, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (enabled()) {
            final Thread currentThread = Thread.currentThread();
            String threadName = currentThread.getName();
            StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
            ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String log = buildMsg(msg,threadName,stackTraceElement);
                    printDefault(D, TAG + tag, log);
                }
            });
        }
    }

    public static void info(String tag, String msg) {
        if (enabled()) {
            final Thread currentThread = Thread.currentThread();
            String threadName = currentThread.getName();
            StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
            ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String printMsg = buildMsg(msg,threadName,stackTraceElement);
                    printDefault(I, TAG + tag, printMsg);
                }
            });
        }
    }

    public static void info2SD(String tag, String msg) {
        final Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
        ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String log = buildMsg(msg,threadName,stackTraceElement);
                printDefault(I, TAG + tag, log);
                writeFileToSD(buildFileMsgWithTag(tag,log));
            }
        });
    }

    //调试时使用的文件日志,发布版本请注释掉或替换成debug日志
    public static void info2SDDebug(String tag, String msg) {
        if( !BuildConfig.DEBUG){
            //Release版本不打印
            return;
        }
        final Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
        ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String log = buildMsg(msg,threadName,stackTraceElement);
                printDefault(I, TAG + tag, log);
                writeFileToSD(buildFileMsgWithTag(tag,log));
            }
        });
    }

    public static void info2SDSecurity(String tag, String msg) {
        if( !BuildConfig.DEBUG){
            //[安全整改]Release版本不打印日志
            return;
        }
        final Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
        ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String log = buildMsg(msg,threadName,stackTraceElement);
                printDefault(I, TAG + tag, log);
                writeFileToSD(buildFileMsgWithTag(tag,log));
            }
        });
    }

    public static void warn(String tag, String msg) {
        if (enabled()) {
            final Thread currentThread = Thread.currentThread();
            String threadName = currentThread.getName();
            StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
            ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String log = buildMsg(msg,threadName,stackTraceElement);
                    printDefault(W, TAG + tag, log);
                    writeFileToSD(buildFileMsgWithTag(tag,log));
                }
            });
        }
    }

    public static void error(String tag, String msg) {
        final Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
        ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String log = buildMsg(msg,threadName,stackTraceElement);
                printDefault(E, TAG + tag,log);
                writeFileToSD(buildFileMsgWithTag(tag,log));
            }
        });
    }

    public static void errorSecurity(String tag, String msg) {
        if( !BuildConfig.DEBUG){
            //[安全整改]Release版本不打印日志
            return;
        }
        final Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        StackTraceElement stackTraceElement = currentThread.getStackTrace()[3];
        ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String log = buildMsg(msg,threadName,stackTraceElement);
                printDefault(E, TAG + tag,log);
                writeFileToSD(buildFileMsgWithTag(tag,log));
            }
        });
    }

    public static void error(String tag, Throwable e) {
        if (/*enabled()*/true) {
            ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    printDefault(E, TAG + tag, getStackString(e));
                    writeFileToSD(buildFileMsgWithTag(tag,getStackString(e)));
                }
            });
        }
    }

    public static void xmlPrint(String tag, String msg) {
        if (enabled()) {
            XmlLog.printXml(TAG + tag, msg, "This xml log");
            if (WRITE_SDCARD_LOG) {
                ThreadManager.getInstance().getSingleThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        writeFileToSD(buildMsg(msg));
                    }
                });
            }
        }
    }

    private static String printStackTraceAsCause(StackTraceElement[] causedTrace, Throwable e) {
        StackTraceElement[] trace = e.getStackTrace();
        int m = trace.length - 1, n = causedTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(e.toString());
        builder.append(NEW_LINE);
        for (int i = 0; i <= m; i++) {
            builder.append(trace[i].toString());
            builder.append(NEW_LINE);
        }
        String ret = builder.toString();

        Throwable ourCause = e.getCause();
        if (ourCause != null) {
            String temp = printStackTraceAsCause(trace, ourCause);
            ret = temp + ret;
        }
        return ret;
    }

    public static String getStackString(Throwable e) {
        StackTraceElement[] trace = e.getStackTrace();
        StringBuilder builder = new StringBuilder();
        builder.append(e.toString());
        builder.append(NEW_LINE);
        for (StackTraceElement temp : trace) {
            builder.append(temp.toString());
            builder.append(NEW_LINE);
        }

        String ret = builder.toString();
        Throwable ourCause = e.getCause();
        if (ourCause != null) {
            String child = printStackTraceAsCause(trace, ourCause);
            ret = child + ret;
        }
        return ret;
    }

    private static void writeFileToSD(String context) {
        if (isSdcardCanWrite()) {
            appendToFile(context, chooseFileName("_APP_LOG"));
        }
    }

    private static void appendToFile(String text, File file) {
        if (file == null) {
            return;
        }
        if (makeDirs(LOG_PATH)) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(FileUtil.getCanonicalPath(file), "rw");
                raf.seek(file.length());
                raf.write(text.getBytes());
            } catch (Exception e) {
                printException(e);
            } finally {
                if (null != raf) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        printException(e);
                    }
                }
            }
        }
    }

    private static void printException(Exception exception) {
        String ss = getStackString(exception);
        Log.e(TAG, ss);
    }

    private static boolean makeDirs(String path) {
        boolean isComplete = true;
        if (!isFileExists(path)) {
            File file = new File(path);
            isComplete = file.mkdirs();
        }
        return isComplete;
    }

    private static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return new File(path).exists();
    }

    private static boolean isSdcardCanWrite() {
        boolean ret;
        try{
            String sdStatus = Environment.getExternalStorageState();
            if ( DeviceInfo.isSkyworth() || DeviceInfo.isHisense() ) {
                //Log.d(TAG, "Device is Skyworth or Hisense. Consider sdcard is mounted.");
                ret = true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(sdStatus)) {
                Log.w(TAG, "Sdcard is readonly. File log will not be recorded.");
                ret = false;
            } else if (!Environment.MEDIA_MOUNTED.equals(sdStatus)){
                Log.e(TAG, "Sdcard is not mounted right now. File log will not be recorded.");
                ret = false;
            } else {
                ret = true;
            }
        } catch (Exception e){
            Log.e(TAG, "Unknown exception : ",e);
            ret = false;
        }
        return ret;
    }

    private static File chooseFileName(String type) {
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) +
                type + FILE_TYPE;
        if (logfile == null) {
            deleteUnusedFile(type + FILE_TYPE);
            logfile = new File(LOG_PATH + fileName);
        } else {
            if (logfile.length() > ERR_LOG_SIZE) {
                logfile = new File(LOG_PATH + fileName);
            }
        }
        return logfile;
    }

    private static void deleteUnusedFile(String simpleName) {
        List<String> rawList = new ArrayList<String>();
        File file = new File(LOG_PATH);
        String[] files = file.list();
        if (files != null) {
            rawList = Arrays.asList(files);
        }

        List<String> names = new ArrayList<String>();
        for (String fileTemp : rawList) {
            if (fileTemp.matches("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}.*" + simpleName)) {
                names.add(fileTemp);
            }
        }
        if (names.size() > 10) {
            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String arg1, String arg2) { //逆排序
                    String str1 = arg1.substring(0, 13);
                    String str2 = arg2.substring(0, 13);
                    return str2.compareTo(str1);
                }

            };
            Collections.sort(names, comparator);

            for (int i = 0; i < names.size(); i++) {
                if (i >= 10) {
                    File deletedFile = new File(LOG_PATH + names.get(i));
                    if (!deletedFile.delete()) {
                        SuperLog.error(TAG, "[deleteUnusedFile] , deletedFile delete failed");
                    }
                }
            }
        }
    }

    private static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int maxLength = 38000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case V:
                Log.v(tag, sub);
                break;
            case D:
                Log.d(tag, sub);
                break;
            case I:
                Log.i(tag, sub);
                break;
            case W:
                Log.w(tag, sub);
                break;
            case E:
                Log.e(tag, sub);
                break;
            case A:
                Log.wtf(tag, sub);
                break;
        }
    }

    private static String buildFileMsgWithTag(String tag,String msg){
        StringBuilder sb = new StringBuilder("[")
                .append(BuildConfig.BUILD_TYPE)
                .append("][")
                .append(getTime())
                .append("][")
                .append(tag)
                .append("]")
                .append(msg);
        return sb.toString();
    }
}