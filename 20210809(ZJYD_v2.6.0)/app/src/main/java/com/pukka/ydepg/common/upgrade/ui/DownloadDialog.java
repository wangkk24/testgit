package com.pukka.ydepg.common.upgrade.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pukka.ydepg.R;

import java.lang.ref.WeakReference;

public class DownloadDialog extends Dialog {

    private ProgressBar progressBar;

    private TextView progressText;

    private ProgressHandler handler;

    private Runnable progressRunnable = new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            setProgress(progressBar.getProgress()+1);
            //自动更新进度到80%停止自动跟新进度,等待真实进度
            if( progressBar.getProgress() < 79 ){
                handler.postDelayed(this, 2000);
            }
        }
    };

    private DownloadDialog(@NonNull Context context) {
        super(context,R.style.download_dialog);
        this.handler = new ProgressHandler(context.getMainLooper(),this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_progress);

//        //对话框外区域灰色遮罩
//        //等效于style中设置   <item name="android:backgroundDimEnabled">true</item>     <!--模糊-->
//        //                  <item name="android:backgroundDimAmount">0.6</item>       <!--背景透明度-->
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.6f; //值越大越透明，即不暗
//        getWindow().setAttributes(lp);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

//        //去除对话框背景图片圆角周围的直角黑色部分,方法：设置透明色背景
//        //等效于style中设置   <item name="android:windowBackground">@android:color/transparent</item> <!--背景透明,可以去除背景圆角周围的黑边-->
//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressBar  = findViewById(R.id.downloadProgress);
        progressText = findViewById(R.id.progressText);
        progressBar.setMax(100);
        setProgress(0);
        //不管有没有实际下载进度,启动进度自动更新,每秒1%提升用户体验
        handler.postDelayed(progressRunnable, 1000);
    }

    //使用Handler保证在主线程更新UI
    private static class ProgressHandler extends Handler {

        private WeakReference<DownloadDialog> dialog;

        public ProgressHandler(Looper looper, DownloadDialog dialog) {
            super(looper);
            this.dialog = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            if(dialog.get() != null){
                dialog.get().setProgress(msg.arg1);
            }
        }
    }


    private void setProgress(int progress){
        progressBar.setProgress(progress);
        progressText.setText(String.valueOf(progress));
    }






    public void close(boolean close) {
        if (close) {
            //升级完成后会自动重启,因此无需关闭对话框
            if (isShowing()) {
                dismiss();
            }
        }
    }

    public void updateProgress(int progress){
        //Log.i(COMMON_TAG,"下载进度：" + progress + "%" );
//        if( progress > progressBar.getProgress() ){
//            Message msg = Message.obtain();
//            msg.arg1 = progress;
//            handler.sendMessage(msg);

        setProgress(progress);
//        }
    }

    public void updateProgress2(int progress,Context context){
        //Log.i(COMMON_TAG,"下载进度：" + progress + "%" );
        if(progress>progressBar.getProgress()){
            if(context instanceof Activity){
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //真实下载进度大于自动展示进度后,关闭自动更新的进度,使用真实进度展示
                        handler.removeCallbacks(progressRunnable);
                        setProgress(progress);
                    }
                });
            }
        }
    }

    public static DownloadDialog buildDialog(Context context){
        //创建下载对话框
        DownloadDialog dialog = new DownloadDialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}