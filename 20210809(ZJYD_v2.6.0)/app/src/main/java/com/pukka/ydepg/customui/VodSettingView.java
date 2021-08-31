package com.pukka.ydepg.customui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.GetVODDetailRequest;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.PlayerAttriUtil;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.Colors;
import com.pukka.ydepg.customui.adpter.CommonRecyclerAdapter;
import com.pukka.ydepg.customui.adpter.VodEpisodeAdapter;
import com.pukka.ydepg.customui.adpter.VodTotalEpisodeAdapter;
import com.pukka.ydepg.event.PlayerSkipChangeEvent;
import com.pukka.ydepg.event.PlayerSpeedChangeEvent;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LD on 2018/1/22.
 * VOD播放 选集View
 */
public class VodSettingView extends PopupWindow implements View.OnClickListener{
    private BaseActivity mContext;

    private TextView settingTitle;
    private TextView settingSkipTitle;
    private LinearLayout settingSkipLayout;
    private TextView skipStart;
    private TextView skipClose;
    private TextView settingSpeedTitle;
    private LinearLayout settingSpeedLayout;
    private TextView speed0;
    private TextView speed1;
    private TextView speed2;
    private TextView speed3;
    private TextView speed4;
    private boolean isskip;
    private int speed = 1;
    private boolean canSetSpeed = true;
    private List<Float> speedList = new ArrayList<>();
    //是否支持跳过片头片尾 =
    boolean canSetSkip = false;

    public VodSettingView(BaseActivity mContext, int speed, boolean canSetSpeed , boolean canSetSkip) {
        this.mContext = mContext;
        this.speed = speed;
//        this.canSetSpeed = canSetSpeed;
        this.canSetSpeed = canSetSpeed;
        this.canSetSkip = canSetSkip;
        initView();
        initSettingView();
    }

    private void initView() {
        speedList.clear();
        String speeds = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_speed_list");
        if(TextUtils.isEmpty(speeds)){
            speedList.add(0.8f);
            speedList.add(1.0f);
            speedList.add(1.25f);
            speedList.add(1.5f);
            speedList.add(2.0f);
        }
        else{
            try {
                String[] speedarray = speeds.split(",");
                if (speedarray != null && speedarray.length == 5) {
                    for(String speed:speedarray){
                        speedList.add(Float.parseFloat(speed));
                    }
                } else {
                    speedList.add(0.8f);
                    speedList.add(1.0f);
                    speedList.add(1.25f);
                    speedList.add(1.5f);
                    speedList.add(2.0f);
                }
            }catch (Exception e){
                speedList.add(0.8f);
                speedList.add(1.0f);
                speedList.add(1.25f);
                speedList.add(1.5f);
                speedList.add(2.0f);
            }
        }
        View vodEpisodesView = LayoutInflater.from(mContext).inflate(R.layout.window_pip_setting, null);
        settingTitle = (TextView) vodEpisodesView.findViewById(R.id.ondemand_setting_title);
        settingSkipTitle = (TextView) vodEpisodesView.findViewById(R.id.ondemand_setting_skip_title);
        settingSkipLayout = (LinearLayout) vodEpisodesView.findViewById(R.id.ondemand_setting_skip_layout);
        skipStart = (TextView) vodEpisodesView.findViewById(R.id.ondemand_skip_start);
        skipClose = (TextView) vodEpisodesView.findViewById(R.id.ondemand_skip_close);
        settingSpeedTitle = (TextView) vodEpisodesView.findViewById(R.id.ondemand_setting_speed_title);
        settingSpeedLayout = (LinearLayout) vodEpisodesView.findViewById(R.id.ondemand_setting_speed_layout);
        speed0 = (TextView) vodEpisodesView.findViewById(R.id.ondemand_speed_0);
        speed1 = (TextView) vodEpisodesView.findViewById(R.id.ondemand_speed_1);
        speed2 = (TextView) vodEpisodesView.findViewById(R.id.ondemand_speed_2);
        speed3 = (TextView) vodEpisodesView.findViewById(R.id.ondemand_speed_3);
        speed4 = (TextView) vodEpisodesView.findViewById(R.id.ondemand_speed_4);
        speed0.setText(speedList.get(0) + "");
        speed1.setText(speedList.get(1) + "");
        speed2.setText(speedList.get(2) + "");
        speed3.setText(speedList.get(3) + "");
        speed4.setText(speedList.get(4) + "");
        if(canSetSpeed){
            settingSpeedTitle.setVisibility(View.VISIBLE);
            settingSpeedLayout.setVisibility(View.VISIBLE);
        }
        else{
            settingSpeedTitle.setVisibility(View.INVISIBLE);
            settingSpeedLayout.setVisibility(View.INVISIBLE);
        }
        setContentView(vodEpisodesView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        settingSkipTitle.setVisibility(View.GONE);
        settingSkipLayout.setVisibility(View.GONE);
        settingSpeedTitle.setVisibility(View.GONE);
        settingSpeedLayout.setVisibility(View.GONE);
        if (canSetSkip){
            settingSkipTitle.setVisibility(View.VISIBLE);
            settingSkipLayout.setVisibility(View.VISIBLE);
        }
        if (canSetSpeed){
            settingSpeedTitle.setVisibility(View.VISIBLE);
            settingSpeedLayout.setVisibility(View.VISIBLE);
        }

        //解决关闭点击右焦点移动到倍率
        skipClose.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
                    return true;
                }
                return false;
            }
        });
    }

    private void chooseView(boolean isEpisodeChoose){
        if(isEpisodeChoose){
            settingSkipTitle.setVisibility(View.GONE);
            settingSkipLayout.setVisibility(View.GONE);
            settingSpeedTitle.setVisibility(View.GONE);
            settingSpeedLayout.setVisibility(View.GONE);
            settingTitle.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        }
        else{
            settingSkipTitle.setVisibility(View.VISIBLE);
            settingSkipLayout.setVisibility(View.VISIBLE);
            if(canSetSpeed) {
                settingSpeedTitle.setVisibility(View.VISIBLE);
                settingSpeedLayout.setVisibility(View.VISIBLE);
            }
            else{
                settingSpeedTitle.setVisibility(View.INVISIBLE);
                settingSpeedLayout.setVisibility(View.INVISIBLE);
            }
            settingTitle.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
        }
    }

    private void initSettingView(){
        isskip = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);
        settingTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    chooseView(false);
                }
            }
        });
        if(isskip){
            skipClose.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
            skipStart.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
        }
        else{
            skipStart.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
            skipClose.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
        }
        skipStart.setOnClickListener(this);
        skipClose.setOnClickListener(this);
        speed0.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed1.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed2.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed3.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed4.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed0.setOnClickListener(this);
        speed1.setOnClickListener(this);
        speed2.setOnClickListener(this);
        speed3.setOnClickListener(this);
        speed4.setOnClickListener(this);
        switch (speed){
            case 0:
                speed0.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            case 1:
                speed1.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            case 2:
                speed2.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            case 3:
                speed3.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            case 4:
                speed4.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            default:
                speed1.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ondemand_speed_0:
                changeSpeed(0);
                EventBus.getDefault().post(new PlayerSpeedChangeEvent(0, speedList));
                break;
            case R.id.ondemand_speed_1:
                changeSpeed(1);
                EventBus.getDefault().post(new PlayerSpeedChangeEvent(1, speedList));
                break;
            case R.id.ondemand_speed_2:
                changeSpeed(2);
                EventBus.getDefault().post(new PlayerSpeedChangeEvent(2, speedList));
                break;
            case R.id.ondemand_speed_3:
                changeSpeed(3);
                EventBus.getDefault().post(new PlayerSpeedChangeEvent(3, speedList));
                break;
            case R.id.ondemand_speed_4:
                changeSpeed(4);
                EventBus.getDefault().post(new PlayerSpeedChangeEvent(4, speedList));
                break;
            case R.id.ondemand_skip_close:
                SharedPreferenceUtil.getInstance().putBoolean(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", false);
                skipClose.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                skipStart.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
                EventBus.getDefault().post(new PlayerSkipChangeEvent());
                break;
            case R.id.ondemand_skip_start:
                SharedPreferenceUtil.getInstance().putBoolean(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);
                skipStart.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                skipClose.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
                EventBus.getDefault().post(new PlayerSkipChangeEvent());
                break;
        }
    }

    private void changeSpeed(int speed){
        speed0.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed1.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed2.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed3.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        speed4.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        switch (speed){
            case 0:
                speed0.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            case 1:
                speed1.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            case 2:
                speed2.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            case 3:
                speed3.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            case 4:
                speed4.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
            default:
                speed1.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
                break;
        }
    }
    public void showEpisodes(View parentView) {
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.RIGHT, 0, 0);
//        parentView.setFocusableInTouchMode(true);
    }
}