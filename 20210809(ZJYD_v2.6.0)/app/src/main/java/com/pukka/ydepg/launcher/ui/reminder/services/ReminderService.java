package com.pukka.ydepg.launcher.ui.reminder.services;

import com.pukka.ydepg.launcher.ui.reminder.presenter.ReminderPresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * ----Created By----
 * User: Fang Liang.
 * Date: 2019/1/10.
 * ------------------
 */

public class ReminderService {

    private static ReminderService mInstance;

    private ReminderPresenter mReminderPresenter;
    /*默认获取最新一条数据*/
    private static final int COUNT = 1;
    /*默认不偏移*/
    private static final int OFFSET = 0;

    private static boolean hasChecked = false;

    public static ReminderService getmInstance() {
        if (mInstance == null) {
            mInstance = new ReminderService();
        }
        return mInstance;
    }

    /**
     * 浙江移动视频项目因业务运营需要， 固网支持开机时固移关联内容的续播提醒功能。
     * 客户需求单号：【R0341】（华为）电视机开机时，检测到最新的播放记录为手机端，且已做过移固关联，则在电视上弹出提示框，用户点击后可直接进行续播。
     * 1、背景：丰富固移关联场景，举例：手机端用户在路上用手机点播内容，回到家中可以在电视上继续观看。
     * 2、实现场景：
     * 1）电视开机时提示，其他场景不提示；
     * 2）仅在检测到最新的播放记录为手机端，且已做过移固关联提示；
     * 3）在电视上右下角弹出提示框，提示语“您正在手机上观看《XXXX》，是否要继续观看”，两个按钮“确定”和“取消”；
     * 4）用户点击确认，直接跳转到播放页面；点击取消，关闭提示框；
     * 5）未点击按钮，等20秒提示框消失。
     */
    public void checkBootStrapAlertReminder(RxAppCompatActivity rxAppCompatActivity) {
        if(hasChecked){
            return;
        }
        mReminderPresenter = new ReminderPresenter(rxAppCompatActivity);
        mReminderPresenter.checkReminderRelatedContent(COUNT, OFFSET);
        hasChecked = true;
    }

    public void playBookmark(String contentID) {
        if (mReminderPresenter != null) {
            mReminderPresenter.playBookmark(contentID);
        }
    }
}
