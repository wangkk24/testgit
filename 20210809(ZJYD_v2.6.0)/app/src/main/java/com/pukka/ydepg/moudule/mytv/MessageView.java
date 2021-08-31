package com.pukka.ydepg.moudule.mytv;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.VerticalGridView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.RxApiManager;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.event.MessageEvent;
import com.pukka.ydepg.moudule.mytv.adapter.MessageAdapter;
import com.pukka.ydepg.common.pushmessage.view.MessageDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eason on 2018/5/14.
 * 我的消息列表---view
 */

public class MessageView extends RelativeLayout implements MessageAdapter.MessageOnclickListener, MessageDialog.DialogDismissListener {


    private String test1 = "{\"mode\":\"6\",\"domain\":\"0\",\"type\":\"0\",\"content\":\"黄初三年，余朝京师，还济洛川。古人有言，斯水之神，名曰宓妃\",\"validTime\":\"20180613094151\"}";


    public static final String TAG = "MessageView";

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHss");

    public MessageView(@NonNull Context context) {
        super(context);
    }

    public MessageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
    * 消息记录
    * */
    @BindView(R.id.activity_mytv_movie_history)
    TextView mHistory;

    /*
    * 消息设置
    * */
    @BindView(R.id.activity_mytv_movie_collection)
    TextView mHistorySet;

    /*
    * 内容列表
    * */
    @BindView(R.id.history_list)
    VerticalGridView mHistoryList;

    /*
    * 无内容背景
    * */
    @BindView(R.id.background_rela)
    RelativeLayout mBackgroundRela;

    /*
    * 消息记录Adapter
    * */
    private MessageAdapter mMessageAdapter;

    private RxAppCompatActivity mRxAppCompatActivity;
    /*
    * 記錄焦点所在位置
    * 1：记录历史
    * 2：设置
    * */
    private int mFocusInt = 0;

    /*
    * 消息Dialog
    * */
    private MessageDialog mMessageDialog;

    /*
    * 视图从容器中移除
    * */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /*
    * 视图依附到容器
    * */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initData();
        initListener();
    }

    /*
    * 点击事件
    * */
    private void initListener() {
        mHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    //xml映射完成
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    /*
    * 初始化VIew
    * */
    private void initView() {

    }

    private void initData() {
        mHistory.requestFocus();

        mMessageAdapter = new MessageAdapter(mRxAppCompatActivity, this);
        mHistoryList.setAdapter(mMessageAdapter);

        mHistory.setOnFocusChangeListener(mOnFocusChangeListener);
        mHistorySet.setOnFocusChangeListener(mOnFocusChangeListener);

        /*
        * 获取本地缓存的推送的消息
        * */
        List<String> notifitionList = SharedPreferenceUtil.getInstance().getNotifitionList();

        /*
        * 测试使用
        * */
        /*if (notifitionList != null && notifitionList.size() == 0) {
            notifitionList.add(test1);
            notifitionList.add(test2);

        }*/
        /*notifitionList.add(test3);
        notifitionList.add(test4);*/
        /*
        * adapter绑定数据
        * */
        if (notifitionList != null && notifitionList.size() > 0) {

            /*
            * 过滤掉过期日期以后的推送消息List
            * */
            List<String> notifitionListNew = new ArrayList<>();
            /*
            * 过滤掉过期的推送消息
            * 过期消息不展示
            * */
            for (String message : notifitionList) {
                try {
                    JSONObject object = new JSONObject(message);
                    if (!TextUtils.isEmpty(object.getString("validTime"))) {
                        String time = object.getString("validTime");
                        Date date = mFormat.parse(time.substring(0, time.length() - 2));

                        /*
                        * UTC时间转北京时间
                        * */
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
                        date = calendar.getTime();

                        Date nowDate = new Date();
                        long nowTime = nowDate.getTime();
                        long dateTime = date.getTime();
                        if (dateTime - nowTime >= 0) {
                            notifitionListNew.add(message);
                        }
                        SuperLog.debug(TAG, "validTime==" + (dateTime - nowTime) + ";nowTime==" + nowTime + ";dateTime==" + dateTime);
                    }
                } catch (Exception e) {
                    SuperLog.error(TAG,e);
                }
            }
            Collections.reverse(notifitionListNew);
            mMessageAdapter.setData(notifitionListNew);
        }

    }

    /*
    * 控制焦点变化
    * */
    private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v == mHistory) {
                if (hasFocus) {
                    mFocusInt = 1;
                    setFocusView(mFocusInt);
                }
            } else if (v == mHistorySet) {
                if (hasFocus) {
                    mFocusInt = 2;
                    setFocusView(mFocusInt);
                }
            }
        }
    };

    public void setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.mRxAppCompatActivity = rxAppCompatActivity;
    }

    /*
    * 监听向左事件、控制焦点
    * */
    public void setOnKeyDownLeft() {
        setFocusView(mFocusInt);
    }

    /*
    * 监听向右事件、控制焦点
    * */
    public void setOnkeyDownRight() {
        if (mHistory.hasFocus()) {
            mFocusInt = 1;
            mHistory.setSelected(true);
        } else if (mHistorySet.hasFocus()) {
            mFocusInt = 2;
            mHistorySet.setSelected(true);
        }
    }

    /*
        * 设置View焦点
        * */
    private void setFocusView(int flagInt) {
        if (flagInt == 1) {//消息記錄
            mHistory.requestFocus();
            mHistorySet.clearFocus();
            mHistorySet.setSelected(false);
        } else if (flagInt == 2) {//消息设置
            mHistory.clearFocus();
            mHistorySet.requestFocus();
            mHistory.setSelected(false);
        }
    }

    /*
    * Adapter 点击事件
    * */
    @Override
    public void onItemClick(String body, int position) {

        if (mMessageDialog != null && !mMessageDialog.isShowing()) {
            mMessageDialog.setBody(body);
            mMessageDialog.show();
        } else {
            showDialog(body);
        }

    }

    /*
    * 消息浏览Dialog显示
    * */
    private void showDialog(String body) {
        mMessageDialog = new MessageDialog(mRxAppCompatActivity);
        mMessageDialog.setBody(body);
        mMessageDialog.show();
    }

    @Override
    public void onDismiss() {
        mMessageDialog = null;
        RxApiManager.get().cancelAll();
    }

    @Subscribe
    public void onEvent(MessageEvent event) {
        if (mMessageDialog != null && mMessageDialog.isShowing()) {
            mMessageDialog.dismiss();
        }
    }

}
