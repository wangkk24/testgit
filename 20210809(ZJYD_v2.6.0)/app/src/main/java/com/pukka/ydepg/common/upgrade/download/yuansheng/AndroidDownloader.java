package com.pukka.ydepg.common.upgrade.download.yuansheng;

import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.v6request.UpgradeRequest;
import com.pukka.ydepg.common.upgrade.download.DownloaderFactory;
import com.pukka.ydepg.common.upgrade.download.IDownloadListener;
import com.pukka.ydepg.common.upgrade.download.IDownloader;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

public class AndroidDownloader implements IDownloader, DownloadStateReceiver.DownloadFinishListener {

    private static final String TAG = AndroidDownloader.class.getSimpleName();

    private Context context;

    //下载结束结果回调
    private IDownloadListener listener;

    //监听下载进度更新
    private DownloadObserver downloadObserver;

    //监听下载完成事件
    private DownloadStateReceiver myReceiver;

    private Handler handler;

    //下载任务id
    private long id;

    public AndroidDownloader(IDownloadListener listener,Context context) {
        this.context = context;
        this.listener = listener;
        this.handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                listener.onProgress(msg.arg1,msg.arg2);
            }
        };
    }

    //URL:下载文件地址,形如 http://117.148.130.74:33500/UPGRADE/jsp/ftp/guwangapk/ZJYD_v2.1.0.5.7_CODE374_20200311_release.apk
    @Override
    public void download(String url) {
        //注册下载完成事件广播,下载完成回调本类onProgress
        registerBroadcast();

        //创建下载request对象
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(url));

        //设置什么网络情况下可以下载
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        //设置通知栏的标题
        //request.setTitle("下载");

        //设置通知栏的message
        //request.setDescription("今日头条正在下载.....");

        //设置漫游状态下是否可以下载
        //request.setAllowedOverRoaming(false);

        String fileName = DownloaderFactory.getDownloadFileName(url);
        setDownloadPath(request,fileName);

        //获取系统服务
        android.app.DownloadManager downloadManager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //进行下载
        id = downloadManager.enqueue(request);
        SuperLog.info2SD(TAG,"Download task started, Task id="+id);
        //注册下载进度监听Observer
        registerDownloadObserver(id);
    }

    @Override
    public void download(String url, UpgradeRequest request) {
        //No implementation
        listener.onFail();
    }

    public void onComplete(String file) {
        //去注册下载进度Observer
        unregisterDownloadObserver();
        //去注册下载完成事件广播
        unregisterBroadcast();
        //回调下载事件订阅者
        listener.onComplete(file);
    }

    public void downloadFail() {
        //去注册下载进度Observer
        unregisterDownloadObserver();
        //去注册下载完成事件广播
        unregisterBroadcast();
        listener.onFail();
    }

    //注册下载完成事件广播
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        myReceiver = new DownloadStateReceiver(this);//传入回调接口
        context.registerReceiver(myReceiver, filter);
    }

    //去注册下载完成事件广播
    private void unregisterBroadcast() {
        //去注册下载进度监视对象
        if (downloadObserver != null) {
            context.getContentResolver().unregisterContentObserver(downloadObserver);
            downloadObserver = null;
        }
    }

    //注册下载进度Observer
    private void registerDownloadObserver(long downloadTaskID) {
        //创建下载进度监视对象
        downloadObserver = new DownloadObserver(context,handler,downloadTaskID);
        //注册下载进度监视对象,参数URI取值"content://downloads/my_downloads"不可修改,代表监控下载内容
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true,downloadObserver);
    }

    //去注册下载进度Observer
    private void unregisterDownloadObserver() {
        if (myReceiver != null) {
            context.unregisterReceiver(myReceiver);
            myReceiver = null;
        }
    }

    @Override
    public void onFinish(long id) {
        if (this.id != id) {
            //非升级功能完成的下载,不由本类处理
            return;
        }

        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        android.app.DownloadManager dm = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        query.setFilterById(id);
        Cursor c = dm.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndexOrThrow(android.app.DownloadManager.COLUMN_STATUS));
                    if(status == android.app.DownloadManager.STATUS_SUCCESSFUL){
                        //获取文件下载路径(filename是包含文件绝对路径的文件)
                        String filename = c.getString(c.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_FILENAME));
                        SuperLog.info2SD(TAG, "Download upgrade file finished: [" + filename + "], Download status=" + status + "(8:successful)");
                        onComplete(filename);
                    } else {
                        SuperLog.error(TAG, "Download upgrade file failed. Status=" + status);
                        downloadFail();
                    }
                }
            } catch (Exception e) {
                SuperLog.error(TAG, e);
                downloadFail();
            } finally {
                c.close();
            }
        }
    }

    //为兼容不同机顶盒读写权限不同
    private void setDownloadPath(android.app.DownloadManager.Request request,String fileName){
        //设置文件存放目录,应用专属目录,软件卸载后，下载的文件将随着卸载全部被删除
        //   /storage/emulated/0/Android/data/com.example.testdownload/files/Download/ZJYD.apk
        //request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "ZJYD.apk");
        switch (CommonUtil.getDeviceType()){
            case "ECM8v9":
                String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
                request.setDestinationUri(Uri.parse(downloadPath));
                break;
            default:
                //设置文件存放目录,软件卸载后,下载的文件会保留
                // CM201z : /storage/emulated/0/Download/ZJYD/ZJYD_v2.1.0.5.7_CODE374_20200311_release.apk
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+"/ZJYD",fileName);
                break;
        }
    }
}
