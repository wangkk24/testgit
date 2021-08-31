package com.pukka.ydepg.common.upgrade.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.pukka.ydepg.R;

public class ConfirmDialog {

    public static void show(Context context,final OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("更新提醒");
        builder.setMessage("发现新版本，是否更新？");
        builder.setCancelable(false);            //点击对话框以外的区域是否让对话框消失

        //设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClickOk();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClickCancel();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        dialog.show();                              //显示对话框
    }

    public interface OnClickListener{
        void onClickOk();
        void onClickCancel();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
//        //拦截主页按键,防止用户跳过升级选择,打乱逻辑
//        if( ){
//            //只响应[左/右/确定]三个键
//            return super.onKeyDown(keyCode, event);
//        } else {
//            return true;
//        }
//    }
}

