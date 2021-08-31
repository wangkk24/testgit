package com.pukka.ydepg.common.upgrade.download.yuansheng;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import static com.pukka.ydepg.common.upgrade.UpgradeManager.COMMON_TAG;

public class DownloadStateReceiver extends BroadcastReceiver {

    DownloadFinishListener listener;

    public DownloadStateReceiver(DownloadFinishListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SuperLog.info2SD(COMMON_TAG,"Received download complete broadcast.");
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            listener.onFinish(id);
        }
    }

    public interface DownloadFinishListener {
        void onFinish(long id);
    }
}
