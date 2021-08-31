package com.pukka.ydepg.moudule.mytv;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.moudule.base.BaseActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Eason on 2018/5/14.
 * 我的消息列表
 */
public class MessageActivity extends BaseActivity {

    private MessageView mMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mMessageView = (MessageView) findViewById(R.id.message_view);
        mMessageView.setRxAppCompatActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        /*
        * 向左
        * */
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            mMessageView.setOnKeyDownLeft();
        }

        /*
        * 向右
        * */
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

            /*
            * 获取本地缓存的推送的消息
            * */
            List<String> notificationList = SharedPreferenceUtil.getInstance().getNotifitionList();
            if (notificationList != null && notificationList.size() > 0) {
                List<String> strings = setData(notificationList);
                if (strings != null && strings.size() > 0) {
                    mMessageView.setOnkeyDownRight();
                }else{
                    return true;
                }

            } else {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHss");

    private List<String> setData(List<String> notificationList) {
        List<String> notificationListNew = new ArrayList<>();
            /*
            * 过滤掉过期的推送消息
            * 过期消息不展示
            * */
        for (String message : notificationList) {
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
                        notificationListNew.add(message);
                    }
                }
            } catch (Exception e) {
                SuperLog.error("MessageActivity",e);
            }
        }
        return notificationListNew;
    }

}
