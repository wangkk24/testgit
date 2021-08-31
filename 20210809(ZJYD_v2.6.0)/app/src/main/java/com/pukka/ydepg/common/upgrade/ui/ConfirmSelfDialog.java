package com.pukka.ydepg.common.upgrade.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.pukka.ydepg.R;


public class ConfirmSelfDialog extends Dialog {

    private ConfirmSelfDialogListener listener;

    private ConfirmSelfDialog(@NonNull Context context, @NonNull ConfirmSelfDialogListener listener) {
        super(context, R.style.download_dialog);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_confirm);

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

        ImageView btnCancel = findViewById(R.id.upgrade_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickCancel();
                dismiss();
            }
        });

        ImageView btnOk = findViewById(R.id.upgrade_btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickOk();
                dismiss();
            }
        });
        btnOk.requestFocus();

    }

    public interface ConfirmSelfDialogListener{
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

    public static void show(Context context,final ConfirmSelfDialogListener listener) {
        ConfirmSelfDialog dialog = new ConfirmSelfDialog(context,listener);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
