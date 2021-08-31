package com.pukka.ydepg.moudule.vrplayer.vrplayer.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.pukka.ydepg.moudule.vrplayer.vrplayer.module.VideoListConfig;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-08-05
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    public static String path = Environment.getExternalStorageDirectory() + File.separator + "vrconfig" + File.separator;//根目录下的vrconfig

    public static String pathData = Environment.getDataDirectory().getPath() + File.separator + "vrconfig" + File.separator;//内部data根目录

    /**
     * 读取assets目录的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        InputStream in = null;
        try {
            in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int length = in.available();
            //创建byte[]
            byte[] buffer = new byte[length];
            //将文件数据读取到byte[]
            in.read(buffer);
            result = new String(buffer, "utf-8");
        } catch (IOException e) {
            LogUtil.logE(TAG, e.getMessage());
        } finally {
            closeQuietly(in);
        }
        return result;
    }

    public static String getFromSDCard(String filePath) {
        LogUtil.logI(TAG, "[getFromSDCard]");
        String result = "";
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            LogUtil.logI(TAG, "[getFromSDCard] "+filePath);
            in = new FileInputStream(file);
            //获取文件的字节数
            int length = in.available();
            //创建byte[]
            byte[] buffer = new byte[length];
            //将文件数据读取到byte[]
            in.read(buffer);
            result = new String(buffer, "utf-8");
        } catch (IOException e) {
            LogUtil.logE(TAG, e.getMessage());
        } finally {
            closeQuietly(in);
        }
        return result;
    }

    /**
     * @param fileName
     * @return
     */
    public static String getFromData(String fileName) {
        String result = "";
        FileInputStream in = null;
        try {
            File file = new File(pathData, fileName);
            LogUtil.logI("vrconfig path=", pathData);
            in = new FileInputStream(file);
            //获取文件的字节数
            int length = in.available();
            //创建byte[]
            byte[] buffer = new byte[length];
            //将文件数据读取到byte[]
            in.read(buffer);
            result = new String(buffer, "utf-8");
        } catch (IOException e) {
            LogUtil.logE(TAG, e.getMessage());
        } finally {
            closeQuietly(in);
        }
        return result;
    }

    /**
     * 关闭输入输出流
     *
     * @param c 输入输出流
     */
    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                LogUtil.logE(TAG, e.toString());
            }
        }
    }

    /**
     * 从json字符串中获取视频列表
     *
     * @param json
     * @return
     */
    public static VideoListConfig getVideListConfig(String json) {
        VideoListConfig videoListConfig = null;
        try {
            Gson gson = new Gson();
            videoListConfig = gson.fromJson(json, VideoListConfig.class);
        } catch (Exception e) {
            LogUtil.logE(TAG, e.getMessage());
        }
        return videoListConfig;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     *         <code>false</code> otherwise
     */
    public static boolean copyFile(String oldPath$Name, String newPath$Name) {
        Log.i(TAG, "[copyFile]:  oldPath:"+oldPath$Name);
        Log.i(TAG, "[copyFile]:  newPath:"+newPath$Name);
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e(TAG, "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e(TAG, "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e(TAG, "copyFile:  oldFile cannot read.");
                return false;
            }

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "exception:"+e.toString());
            return false;
        }
    }
}
