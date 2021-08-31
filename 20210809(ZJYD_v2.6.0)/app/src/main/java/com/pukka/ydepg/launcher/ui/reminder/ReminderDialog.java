package com.pukka.ydepg.launcher.ui.reminder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.customui.tv.bridge.RecyclerViewBridge;
import com.pukka.ydepg.customui.tv.widget.MainUpView;
import com.pukka.ydepg.launcher.ui.reminder.beans.ReminderMessage;
import com.pukka.ydepg.launcher.ui.reminder.services.ReminderService;
import com.pukka.ydepg.launcher.util.FocusEffectWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/9.
 * ------------------
 * 浙江移动视频项目因业务运营需要， 固网支持开机时固移关联内容的续播提醒功能。
 * 客户需求单号：【R0341】（华为）电视机开机时，检测到最新的播放记录为手机端，且已做过移固关联，则在电视上弹出提示框，用户点击后可直接进行续播。
 * 1、背景：丰富固移关联场景，举例：手机端用户在路上用手机点播内容，回到家中可以在电视上继续观看。
 * 2、实现场景：
 * 1）电视开机时提示，其他场景不提示；
 * 2）仅在检测到最新的播放记录为手机端，且已做过移固关联提示；
 * 3）在电视上右下角弹出提示框，提示语“您正在手机上观看《XXXX》，是否要继续观看”，两个按钮“确定”和“取消”；
 * 4）用户点击确认，直播跳转到播放页面；点击取消，关闭提示框；
 * 5）未点击按钮，等20秒提示框消失。
 */

public class ReminderDialog extends Dialog {

    private static final String TAG = ReminderDialog.class.getName();

    private Context context;

    private ReminderMessage reminderMessage;

    private static final int CLOSE_DIALOG = 20 * 1000;

    @BindView(R.id.reminder_dialog)
    ViewGroup viewGroup;

    @BindView(R.id.tv_continue)
    TextView tvContinue;

    @BindView(R.id.vod_name)
    TextView vodName;

    public ReminderDialog(@NonNull Context context, ReminderMessage reminderMessage) {
        super(context, R.style.dialog);
        this.context = context;
        this.reminderMessage = reminderMessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置全屏无标题头*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reminder_dialog);
        ButterKnife.bind(this);
        if (getWindow() != null) {
        /*设置宽度全屏*/
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
            getWindow().setAttributes(layoutParams);
            getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent));
        /*设置首页焦点 Start*/
        }
        MainUpView mainUpView = new MainUpView(context);
        mainUpView.attach2View(viewGroup);
        /*焦点效果*/
        FocusEffectWrapper mFocusEffectWrapper = new FocusEffectWrapper.FocusEffectBuilder().effectNoDrawBridge(new RecyclerViewBridge()).mainUpView(mainUpView).build();
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener((View oldFocus, View newFocus) -> mFocusEffectWrapper.drawFocusEffect(oldFocus, newFocus));
        /*设置首页焦点 End*/
        vodName.setText(getVodName(reminderMessage));
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.reminder_dialog);
        viewGroup.startAnimation(animation);
        /*设置默认焦点*/
        new Handler().postDelayed(() -> {
            tvContinue.setFocusable(true);
            tvContinue.requestFocus();
        }, 200);
        new Handler().postDelayed(this::dismiss, CLOSE_DIALOG);
    }

    private String getVodName(ReminderMessage reminderMessage) {
        if (reminderMessage.getSitcomNO() != null) {
            return reminderMessage.getVodName() + " 第" + reminderMessage.getSitcomNO() + "集";
        }
        return reminderMessage.getVodName();
    }

    /**
     * 点击继续观看、稍后提醒
     */
    @OnClick({R.id.tv_continue, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_continue:
                ReminderService.getmInstance().playBookmark(reminderMessage.getContentID());
                dismiss();
                SuperLog.debug(TAG, reminderMessage.toString());
                break;
            case R.id.tv_back:
                dismiss();
                break;
            default:
                SuperLog.error(TAG,"Unknown view clicked");
        }
    }
}
