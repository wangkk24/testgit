package com.pukka.ydepg.moudule.multviewforstb.multiview.util;

import android.content.Context;

import com.google.gson.Gson;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllConfig;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static String getFromAssets(Context context, String fileName) {
        DebugLog.info(TAG, "[getFromAssets]");
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
            DebugLog.error(TAG, e.getMessage());
        } finally {
            closeQuietly(in);
        }
        return result;
    }

    public static String getFromSDCard(String filePath) {
        DebugLog.info(TAG, "[getFromSDCard]");
        String result = "";
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            DebugLog.info(TAG, "[getFromSDCard] "+filePath);
            in = new FileInputStream(file);
            //获取文件的字节数
            int length = in.available();
            //创建byte[]
            byte[] buffer = new byte[length];
            //将文件数据读取到byte[]
            in.read(buffer);
            result = new String(buffer, "utf-8");
        } catch (IOException e) {
            DebugLog.error(TAG, e.getMessage());
        } finally {
            closeQuietly(in);
        }
        return result;
    }

    public static AllConfig getAllConfig(String json) {
        AllConfig allConfig = null;
        try {
            Gson gson = new Gson();
            allConfig = gson.fromJson(json, AllConfig.class);
        } catch (Exception e) {
            DebugLog.error(TAG, "[getAllConfig] Exception:"+e.getMessage());
        }

        return allConfig;
    }


    /**
     * 关闭输入输出流
     *
     * @param c 输入输出流
     */
    private static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                DebugLog.info(TAG, e.toString());
            }
        }
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
        DebugLog.info(TAG, "[copyFile]:  oldPath:"+oldPath$Name);
        DebugLog.info(TAG, "[copyFile]:  newPath:"+newPath$Name);
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                DebugLog.error(TAG, "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                DebugLog.error(TAG, "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                DebugLog.error(TAG, "copyFile:  oldFile cannot read.");
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
            DebugLog.error(TAG, "exception:"+e.toString());
            return false;
        }
    }

}
