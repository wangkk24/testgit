package com.pukka.ydepg.common.pushmessage.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.pushmessage.presenter.PushMessagePresenter;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.event.MessageDialogIsShowEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Eason on 2018/5/14.
 * 我的消息Dialog
 */
public class MessageDialog extends Dialog{

    private MessageDialogManageView mMessageDialogManageView;

    public MessageDialog(Activity rxAppCompatActivity) {
        super(rxAppCompatActivity,R.style.message_dialog);
        View view = LayoutInflater.from(rxAppCompatActivity).inflate(R.layout.dialog_message,null,false);

        //commented by liuxia at 20200930这行代码会导致屏保无法展示
        //getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);

        mMessageDialogManageView = view.findViewById(R.id.root_view);
        mMessageDialogManageView.setRxAppCompatActivity(rxAppCompatActivity,this);
        setContentView(view);//设置View

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SuperLog.debug("MessageDialog","onDismiss dialog");
                PushMessagePresenter.hideMessageDialogAndToShow();
            }
        });
    }

    public final void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

        MessageDialogIsShowEvent event = new MessageDialogIsShowEvent();
        event.setShow(true);
        EventBus.getDefault().post(event);
    }

    //这是弹框展示信息
    public void setBody(String body){
        mMessageDialogManageView.setBody(body);
    }

    //销毁Dialog
    public interface DialogDismissListener {
        void onDismiss();
    }
}