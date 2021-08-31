package com.pukka.ydepg.common.http;

/**
 * Created by liudo on 2018/4/18.
 */

import android.os.AsyncTask;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoaderStartMedia extends AsyncTask<Void, Integer, Long> {
    private final String TAG = this.getClass().getSimpleName();
    private URL mUrl;
    private File mFile;
    private FileOutputStream mOutputStream;
    private String startPicturePath;

    public DownLoaderStartMedia(String url, String startVideoDir, String startVideoName) {
        SuperLog.debug(TAG, "url is " + url);
        SuperLog.debug(TAG, "startVideoDir is " + startVideoDir);
        SuperLog.debug(TAG, "startVideoName is " + startVideoName);
        startPicturePath = startVideoDir + "/" + startVideoName + ".mp4";
        try {
            mUrl = new URL(url);
            mFile = new File(startPicturePath);
        } catch (MalformedURLException e) {
            SuperLog.error(TAG,e);
        }

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Long doInBackground(Void... params) {
        return download();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    @Override
    protected void onPostExecute(Long result) {
        if(mFile.exists() && result > 0){
            SuperLog.debug(TAG, "download media success");
            SuperLog.debug(TAG, FileUtil.getCanonicalPath(mFile));
        }
        else{
            SuperLog.debug(TAG, "download media failed");
        }
    }

    private long download() {
        HttpURLConnection connection = null;
        int bytesCopied = 0;
        try {
            connection = (HttpURLConnection) mUrl.openConnection();
            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 302) {
                if (mFile.exists()) {
                    if(!mFile.delete()){
                        SuperLog.error(TAG,"Delete [" + startPicturePath + "] file failed");
                    }
                    if(mFile.createNewFile()){
                        SuperLog.error(TAG,"Create [" + startPicturePath + "] file failed");
                    }
                }
                mOutputStream = new FileOutputStream(mFile);
                bytesCopied = copy(connection.getInputStream(), mOutputStream);
                mOutputStream.close();
            }
        } catch (IOException e) {
            SuperLog.error(TAG,e);
            if (null != mOutputStream) {
                try {
                    mOutputStream.close();
                } catch (IOException e1) {
                    SuperLog.error(TAG,e);
                }
            }
        }
        return bytesCopied;
    }

    private int copy(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024 * 8];
        BufferedInputStream in = new BufferedInputStream(input);
        BufferedOutputStream out = new BufferedOutputStream(output);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            SuperLog.error(TAG,e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                SuperLog.error(TAG,e);
            }
            try {
                in.close();
            } catch (IOException e) {
                SuperLog.error(TAG,e);
            }
        }
        return count;
    }

}