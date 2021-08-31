package com.pukka.ydepg.common.upgrade.download.yuansheng;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

public class DownloadObserver extends ContentObserver {

    private Context context;

    private long downloadTaskId;

    private Handler handler;

    public DownloadObserver(Context context,Handler handler,long downloadTaskId) {
        super(handler);
        this.context        = context;
        this.downloadTaskId = downloadTaskId;
        this.handler        = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Message msg = Message.obtain();
        msg.arg1 = getProgress(getBytesAndStatus(downloadTaskId));
        handler.sendMessage(msg);
    }

    //downloadBytes[0]:已经下载文件大小
    //downloadBytes[1]:下载文件的总大小
    //downloadBytes[2]:下载状态
    private int getProgress(int[] downloadBytes){
        float progress = (float) downloadBytes[0] / (float) downloadBytes[1] * 100f;
        return (int)progress;
    }

    private int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }
}