package com.pukka.ydepg.common.http;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.HttpConstant;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.StartPicture;
import com.pukka.ydepg.common.http.v6bean.v6node.StartPictureConfig;
import com.pukka.ydepg.common.http.v6bean.v6request.DownloadVODRequest;
import com.pukka.ydepg.common.http.v6bean.v6response.DownloadVODResponse;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
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
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DownloaderStartSource extends AsyncTask<Void, Integer, Long> {

    private static final String TAG = DownloaderStartSource.class.getSimpleName();
    public  static final String START_CONFIG = "startConfig.json";
    public  static final String STB_BOOT_FILE_DIR = "/data/local";
    public  static final String STB_BOOT_FILE_NAME = "bootanimation.zip";
    public  static final String STB_BOOT_FILE = STB_BOOT_FILE_DIR + "/" + STB_BOOT_FILE_NAME;

    private URL mNewStartResourceUrl;
    private File mNewStartResourceFile;
    private FileOutputStream mOutputStream;
    private String savedStartResourcePath;
    private String startResourceDir;
    private String preStartResourceDir;
    private String savedPreStartResourcePath;
    private String resourceName;

    public DownloaderStartSource(String url, String resourceName) {
        preStartResourceDir       = OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource();
        savedPreStartResourcePath = OTTApplication.getCachePath() + SharedPreferenceUtil.getInstance().getPreStartResource() + ".zip";
        startResourceDir          = OTTApplication.getCachePath() + resourceName;
        savedStartResourcePath    = OTTApplication.getCachePath() + resourceName + ".zip";
        StringBuilder sb = new StringBuilder()
                .append("\n\tpreStartResourceDir       = ").append(preStartResourceDir)
                .append("\n\tsavedPreStartResourcePath = ").append(savedPreStartResourcePath)
                .append("\n\tstartResourceDir          = ").append(startResourceDir)
                .append("\n\tsavedStartResourcePath    = ").append(savedStartResourcePath);
        SuperLog.info2SD(TAG,sb.toString());
        this.resourceName = resourceName;
        try {
            mNewStartResourceUrl  = new URL(url);
            mNewStartResourceFile = new File(savedStartResourcePath);
        } catch (MalformedURLException e) {
            SuperLog.error(TAG,e);
        }
    }

    @Override
    protected void onPreExecute() { }

    @Override
    protected Long doInBackground(Void... params) {
        return download();
    }

    @Override
    protected void onProgressUpdate(Integer... values) { }

    @Override
    protected void onPostExecute(Long result) {
        SuperLog.info2SD(TAG,"Download start resource finished. Begin to replace local resource by the downloaded resource.");
        //判断目录文件是否存在
        if(!TextUtils.isEmpty(SharedPreferenceUtil.getInstance().getPreStartResource())) {
            File file = new File(preStartResourceDir);
            if (file.exists()) {
                SuperLog.info2SD(TAG, "need delete file, path = " + preStartResourceDir);
                FileUtil.deleteDir(preStartResourceDir);
            }
            //判断目录文件是否存在
            File zipFile = new File(savedPreStartResourcePath);
            if (zipFile.exists() && !resourceName.equals(SharedPreferenceUtil.getInstance().getPreStartResource())) {
                SuperLog.info2SD(TAG, "need delete file, path = " + savedPreStartResourcePath);
                FileUtil.deleteDir(savedPreStartResourcePath);
            }
        }
        //解压开机资源
        if (mNewStartResourceFile.exists()) {
            boolean unzipStartResourceResult = FileUtil.unZip(savedStartResourcePath, startResourceDir);
            SharedPreferenceUtil.getInstance().saveUnZipStartResource(unzipStartResourceResult);
            StringBuilder log = new StringBuilder("Unzip [").append(savedStartResourcePath)
                    .append("] to [" ).append(startResourceDir)
                    .append("] result = ").append(unzipStartResourceResult);
            SuperLog.info2SD(TAG, log.toString());
            if(unzipStartResourceResult){
                downloadStartVideo();
                String newBootFilePath = startResourceDir + "/" + STB_BOOT_FILE_NAME;
                File newBootFile = new File(newBootFilePath);
                if(newBootFile.exists()){
                    //当平台下发的开机资源中存在boot资源才去解压boot资源,否则无此动作
                    File bootFile = new File(STB_BOOT_FILE);
                    SuperLog.debug(TAG, "Local bootFile.exists() is " + bootFile.exists());

                    if(bootFile.exists() && newBootFile.exists()){
                        SuperLog.info2SDSecurity(TAG, "need delete file, path = " + STB_BOOT_FILE);
                        FileUtil.deleteDir(STB_BOOT_FILE);
                    }
                    boolean unzipBootResourceResult = FileUtil.unZip(newBootFilePath, STB_BOOT_FILE_DIR);
                    StringBuilder log1 = new StringBuilder("Unzip [").append(newBootFilePath)
                            .append("] to [" ).append(STB_BOOT_FILE_DIR)
                            .append("] result = ").append(unzipBootResourceResult);
                    SuperLog.info2SD(TAG, log1.toString());
                    if(unzipBootResourceResult){
                        //替换boot资源成功后才更新当前资源包名,防止替换boot资源失败后下次不再替换
                        SharedPreferenceUtil.getInstance().savePreStartResource(SharedPreferenceUtil.getInstance().getStartResource());
                        SharedPreferenceUtil.getInstance().saveStartResource(resourceName);
                    }
                } else {
                    //新的开机资源包中不包含机顶盒boot资源
                    SuperLog.debug(TAG, "The new start resource["+resourceName+"] does not contain boot resource.");
                    SharedPreferenceUtil.getInstance().savePreStartResource(SharedPreferenceUtil.getInstance().getStartResource());
                    SharedPreferenceUtil.getInstance().saveStartResource(resourceName);
                }
            } else {
                //下载开机资源成功,解压开机资源失败,下次启动后[本地资源包名]==[网络资源包名],不下载但是需要重新解压
                SharedPreferenceUtil.getInstance().savePreStartResource(SharedPreferenceUtil.getInstance().getStartResource());
                SharedPreferenceUtil.getInstance().saveStartResource(resourceName);
            }
        } else{
            //[网络资源包]下载失败,不保存任何数据,下次启动后[本地资源包名]!=[网络资源包名],需要重新下载
            SuperLog.info2SD(TAG, "Start resource not exit. Maybe download step failed, please check it.");
        }

        StringBuilder log2 = new StringBuilder("After download and process start resource, the cache info is as followed:>>>")
                .append("\n\tUnZipStartResource result = ").append(SharedPreferenceUtil.getInstance().getUnZipStartResource())
                .append("\n\tPreStartResource          =" ).append(SharedPreferenceUtil.getInstance().getPreStartResource())
                .append("\n\tCurrentStartResource      =" ).append(SharedPreferenceUtil.getInstance().getStartResource());
        SuperLog.info2SD(TAG, log2.toString());
    }

    private long download() {
        SuperLog.info2SDSecurity(TAG,"Begin to download start source, url="+ mNewStartResourceUrl.getPath());
        HttpURLConnection connection;
        int bytesCopied = 0;
        try {
            connection = (HttpURLConnection) mNewStartResourceUrl.openConnection();
            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 302) {
                if (mNewStartResourceFile.exists()) {
                    if(!mNewStartResourceFile.delete()){
                        SuperLog.error(TAG,"Delete [" + savedStartResourcePath + "] file failed");
                    }
                    if(!mNewStartResourceFile.createNewFile()){
                        SuperLog.error(TAG,"Create [" + savedStartResourcePath + "] file failed");
                    }
                }
                mOutputStream = new FileOutputStream(mNewStartResourceFile);
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
        int count = 0;
        int n;
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

    private void downloadStartVideo(){
        SuperLog.debug("gwpzjyd", "downloadStartVideo");
        String startConfigPath = startResourceDir + "/" + START_CONFIG;
        String startConfigJson = FileUtil.getJsonStrFromUrl(startConfigPath);
        SuperLog.debug(TAG, "startConfigJson is " + startConfigJson);
        StartPictureConfig startPictureConfig = JsonParse.json2Object(startConfigJson, StartPictureConfig.class);
        if (startPictureConfig != null && startPictureConfig.getPicture() != null && startPictureConfig.getPicture().size() > 0) {
            List<String> vodIDs = new ArrayList<>();
            for(StartPicture startPicture:startPictureConfig.getPicture()){
                File mediaFile = new File(startResourceDir +startPicture.getVodID() + ".mp4");
                SuperLog.debug("gwpzjyd", "START_RESOURCE_DIR +startPicture.getVodID() is " + startResourceDir +startPicture.getVodID());
                SuperLog.debug("gwpzjyd", "mediaFile.exists() is " + mediaFile.exists());

                if("1".equals(startPicture.getType()) && !vodIDs.contains(startPicture.getVodID()) && !mediaFile.exists()){
                    vodIDs.add(startPicture.getVodID());
                    DownloadVODRequest downloadVODRequest = new DownloadVODRequest();
                    downloadVODRequest.setVODID(startPicture.getVodID());
                    List<String> mediaIDs = new ArrayList<>();
                    mediaIDs.add(startPicture.getMediaID());
                    downloadVODRequest.setMediaIDs(mediaIDs);
                    getVODPlayUrlEPGAndDownload(downloadVODRequest, startPicture.getVodID());
                }
            }
        }
    }

    private void getVODPlayUrlEPGAndDownload(DownloadVODRequest downloadVODRequest, String vodID) {
        SuperLog.debug(TAG, "getVODPlayUrlEPGAndDownload");

        HttpApi.getInstance().getService().downloadVOD(
                ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + HttpConstant.HTTPS_VSP_IP_VSP_PORT_VSP_V3+HttpConstant.DOWNLOADVOD, downloadVODRequest)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadVODResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(DownloadVODResponse downloadVODResponse) {
                        SuperLog.debug(TAG,"getVODPlayUrlEPG onSuccess");
                        if (downloadVODResponse != null
                                && downloadVODResponse.getResult() != null) {

                            Result result = downloadVODResponse.getResult();
                            if (result != null
                                    && !TextUtils.isEmpty(result.getRetCode())
                                    && result.getRetCode().equals(Result.RETCODE_OK)
                                    && downloadVODResponse.getDownloadURLs() != null && downloadVODResponse.getDownloadURLs().size() > 0) {

                                DownLoaderStartMedia task = new DownLoaderStartMedia(downloadVODResponse.getDownloadURLs().get(0), startResourceDir, vodID);
                                task.execute();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }
}