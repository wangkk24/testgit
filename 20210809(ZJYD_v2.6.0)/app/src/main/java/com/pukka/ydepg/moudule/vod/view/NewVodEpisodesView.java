package com.pukka.ydepg.moudule.vod.view;

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

import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6CallBack.VODDetailCallBack;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.RecmContents;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.http.v6bean.v6request.GetVODDetailRequest;
import com.pukka.ydepg.common.utils.JsonParse;
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
import com.pukka.ydepg.moudule.vod.NewAdapter.NewVodEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.NewAdapter.NewVodTotalEpisodeAdapter;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.presenter.DetailPresenter;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewVodEpisodesView extends PopupWindow implements View.OnClickListener, CommonRecyclerAdapter.OnKeyDownListener{

    private static final String TAG = "NewVodEpisodesView";
    private BaseActivity mContext;
    private HorizontalGridView horizontalGridView;
    private View episodesLine;
    private RecyclerView chooseEpisdeList;
    private String vodId;
    private DetailPresenter detailPresenter;
    private NewVodEpisodeAdapter onDemandEpisodeAdapter;

    private String sitcomNO;
    private Handler handler;

    private String fatherPrice;
    private TextView episodeTitle;
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
    BrowseEpsiodesUtils utils;


    public NewVodEpisodesView(BaseActivity mContext, BrowseEpsiodesUtils utils, String vodId, Handler handler, String sitcomNO, String fatherPrice, int speed , boolean canSetSpeed , boolean canSetSkip) {
        this.mContext = mContext;
        this.utils = utils;
        this.vodId = vodId;
        this.handler = handler;
        this.sitcomNO = sitcomNO;
        this.fatherPrice=fatherPrice;
        this.speed = speed;
        this.canSetSpeed = canSetSpeed;
        this.canSetSkip = canSetSkip;
        initView();
        initEpisodesDatas();
        initSettingView();
        initLstener();
    }

    private void initView() {
        Log.e("VodEpisodesView", "canSetSpeed is " + canSetSpeed);
        Log.e("VodEpisodesView", "canSetSkip is " + canSetSkip);
        speedList.clear();
        String speeds = SessionService.getInstance().getSession().getTerminalConfigurationValue("vod_speed_list");
        if(TextUtils.isEmpty(speeds)){
            Log.e("VodEpisodesView", "TextUtils.isEmpty(speeds)");
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
                    Log.e("VodEpisodesView", "speedarray != null && speedarray.length == 5");
                    for(String speed:speedarray){
                        speedList.add(Float.parseFloat(speed));
                    }
                } else {
                    Log.e("VodEpisodesView", "speedarray != null && speedarray.length == 5 !!!");
                    speedList.add(0.8f);
                    speedList.add(1.0f);
                    speedList.add(1.25f);
                    speedList.add(1.5f);
                    speedList.add(2.0f);
                }
            }catch (Exception e){
                Log.e("VodEpisodesView", e.getMessage());
                speedList.add(0.8f);
                speedList.add(1.0f);
                speedList.add(1.25f);
                speedList.add(1.5f);
                speedList.add(2.0f);
            }
        }
        View vodEpisodesView = LayoutInflater.from(mContext).inflate(R.layout.window_pip_episodes, null);
        horizontalGridView = (HorizontalGridView) vodEpisodesView.findViewById(R.id.ondemand_total_episodes);
        episodesLine = vodEpisodesView.findViewById(R.id.ondemand_episodes_line);
        chooseEpisdeList = (RecyclerView) vodEpisodesView.findViewById(R.id.ondemand_choose_episodes);
        chooseEpisdeList.setLayoutManager(new GridLayoutManager(mContext, 5, GridLayoutManager.VERTICAL, false));
        episodeTitle = (TextView) vodEpisodesView.findViewById(R.id.ondemand_choose_episodes_title);
        settingTitle = (TextView) vodEpisodesView.findViewById(R.id.ondemand_setting_title);
        //默认设为false,ZJYDEPGAPP-2191解决这个问题，只有在选集上右键才可以给设置落焦
        settingTitle.setFocusable(false);
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
        setContentView(vodEpisodesView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        detailPresenter = new DetailPresenter(mContext);
    }

    private void initEpisodesDatas() {
        episodeTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
                    if (settingTitle.getVisibility() == View.VISIBLE){
                        return false;
                    }else{
                        return true;
                    }
                }else if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                {
                    //默认设为false,ZJYDEPGAPP-2191解决这个问题，只有在选集上右键才可以给设置落焦
                    settingTitle.setFocusable(false);
                }
                return false;
            }
        });
        episodeTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //默认设为false,ZJYDEPGAPP-2191解决这个问题，只有在选集上右键才可以给设置落焦
                    settingTitle.setFocusable(true);
                    chooseView(true);
                }
            }
        });

        //解决关闭点击右边焦点上移
        skipClose.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
                    return true;
                }
                return false;
            }
        });
        //解决倍速点击上焦点移到选集
        View.OnKeyListener listener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP ){
                    if (canSetSkip){
                        //上方有支持跳过片头片尾焦点移到开启关闭
                        return false;
                    }else{
                        //焦点移动到设置
                        settingTitle.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        };
        speed0.setOnKeyListener(listener);
        speed1.setOnKeyListener(listener);
        speed2.setOnKeyListener(listener);

        //解决设置点击右焦点移到倍率
        settingTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
                    return true;
                }
                return false;
            }
        });

        Log.i("whd","initEpisodesDatas");
        if (null != utils.getmEpisodesCount() && utils.getmEpisodesCount().size() > 0 && null != utils.getMarkEpisodes() && utils.getMarkEpisodes().size()>0) {
            initAdapter();
        } else {
            if (!PlayerAttriUtil.isEmpty(vodId)) {
                getVODDetail(vodId);
            }
        }
    }

    private void initLstener() {
        Log.i("whd","initLstener");
        if (onDemandEpisodeAdapter == null)
        {
            Log.w("whd","initLstener onDemandEpisodeAdapter == null");
            return;
        }
        onDemandEpisodeAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                Message message = Message.obtain();
                message.what = BrowseTVPlayFragment.EPISODE_PLAY;
                Bundle bundle = new Bundle();
                String sitNum = (String) view.getTag();
                utils.getEpisode(sitNum, new BrowseEpsiodesUtils.GetEpisodeCallback() {
                    @Override
                    public void getEpisode(List<Episode> episodes, Episode episode) {
                        bundle.putString("SitcomNO", episode.getSitcomNO());
                        bundle.putString("EpisodesId", episode.getVOD().getID());
                        bundle.putString("MediaId", episode.getVOD().getMediaFiles().get(0).getID());
                        bundle.putString("elapseTime", episode.getVOD().getMediaFiles().get(0).getElapseTime());
                        bundle.putSerializable("episodeVod",episode.getVOD());
                        message.setData(bundle);
                        handler.sendMessage(message);
                        dismiss();
                    }

                    @Override
                    public void getEpisodeFail() {

                    }
                });

            }
        });
    }


    public void getVODDetail(String vodId) {
        Log.i("whd","getVODDetail" + vodId);
        utils.getSimpleVod(vodId, new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
            @Override
            public void getSimpleVodSuccess(VODDetail vodDetail) {
                detailPresenter.setVODDetail(vodDetail);
                initAdapter();
                initLstener();
            }

            @Override
            public void getSimpleVodFail() {

            }
        });
//        GetVODDetailRequest getVODDetailRequest = new GetVODDetailRequest();
//        getVODDetailRequest.setVODID(vodId);
//        detailPresenter.getVODDetail(vodId,this);
    }

    private void initAdapter() {
        Log.e("whd","initAdapter");
        if (utils.getTotal() != 0) {
            Log.e("whd","initAdapter episodes" + utils.getTotal());
            if (utils.getTotal() <= 35) {
                horizontalGridView.setVisibility(View.GONE);
                episodesLine.setVisibility(View.GONE);
                utils.getSitcomNos();
                Map<String,List<String>> map = utils.getMapForIndex();
                List<Episode> episodes = utils.getMarkEpisodes();
                String index = episodes.get(0).getSitcomNO();
                List<String> episodeIndex = map.get(index);
                onDemandEpisodeAdapter = new NewVodEpisodeAdapter(chooseEpisdeList, episodeIndex,fatherPrice);
                onDemandEpisodeAdapter.setOnKeyDownListener(this);
                onDemandEpisodeAdapter.setCurrentSitNum(sitcomNO);
                onDemandEpisodeAdapter.setEpisodes(episodes);
                chooseEpisdeList.setAdapter(onDemandEpisodeAdapter);
                mHandler.sendEmptyMessageDelayed(1,20);
            } else {
                List<List<String>> totalEpisodes = utils.getSitcomNos();
                Map<String,List<String>> map = utils.getMapForIndex();
                List<Episode> episodes = utils.getMarkEpisodes();
                String index = episodes.get(0).getSitcomNO();
                List<String> episodeIndex = map.get(index);
                onDemandEpisodeAdapter = new NewVodEpisodeAdapter(chooseEpisdeList, episodeIndex,fatherPrice);
                onDemandEpisodeAdapter.setOnKeyDownListener(this);
                chooseEpisdeList.setAdapter(onDemandEpisodeAdapter);
                NewVodTotalEpisodeAdapter adapter1 = new NewVodTotalEpisodeAdapter(totalEpisodes,utils, onDemandEpisodeAdapter, sitcomNO);
                adapter1.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (!adapter1.isCanMove()){
                            return true;
                        }

                        if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_UP){
                            if (episodeTitle.isFocusable()){
                                episodeTitle.requestFocus();
                            }
                            return true;
                        }
                        return false;
                    }
                });
                horizontalGridView.setAdapter(adapter1);
                mHandler.sendEmptyMessageDelayed(1,50);
            }



        }
    }
    private Handler mHandler = new Handler() {
        @SuppressLint("RestrictedApi")
        @Override
        public void handleMessage(Message msg) {
            if (utils.getTotal() > 0) {
                int position = 0;
                if (!PlayerAttriUtil.isEmpty(sitcomNO)) {
                    for (int i = 0; i < utils.querySitcomNos().size(); i++) {
                        List<String> list = utils.querySitcomNos().get(i);
                        for (int j = 0; j < list.size(); j++) {
                            if (sitcomNO.equals(list.get(j))){
                                position = i;
                                break;
                            }
                        }
                    }
                }

                horizontalGridView.setSelectedPosition(position);
            }
        }
    };

    private Handler mFocusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(chooseEpisdeList.getLayoutManager().findViewByPosition(msg.what % 35) != null){
                chooseEpisdeList.getLayoutManager().findViewByPosition(msg.what % 35)
                        .requestFocus();
            }else{
                sendEmptyMessageDelayed(msg.what,20);
            }
        }
    };
    public void showEpisodes(View parentView) {
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.RIGHT, 0, 0);
        initLstener();
//        parentView.setFocusableInTouchMode(true);
        parentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                SuperLog.debug("onKey","onKey");
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
            chooseEpisdeList.setVisibility(View.VISIBLE);
            if(utils.getTotal() > 35){
                episodesLine.setVisibility(View.VISIBLE);
                horizontalGridView.setVisibility(View.VISIBLE);
            }
            else{
                episodesLine.setVisibility(View.GONE);
                horizontalGridView.setVisibility(View.GONE);
            }
            settingTitle.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
            episodeTitle.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
        }
        else{
            if(canSetSpeed) {
                settingSpeedTitle.setVisibility(View.VISIBLE);
                settingSpeedLayout.setVisibility(View.VISIBLE);
            }
            else{
                settingSpeedTitle.setVisibility(View.INVISIBLE);
                settingSpeedLayout.setVisibility(View.INVISIBLE);
            }

            if (canSetSkip){
                //支持设置跳过片头片尾的时候显示
                settingSkipTitle.setVisibility(View.VISIBLE);
                settingSkipLayout.setVisibility(View.VISIBLE);
            }
            else{
                settingSkipTitle.setVisibility(View.GONE);
                settingSkipLayout.setVisibility(View.GONE);
            }
            chooseEpisdeList.setVisibility(View.GONE);
            episodesLine.setVisibility(View.GONE);
            horizontalGridView.setVisibility(View.GONE);

            settingTitle.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c21_color));
            episodeTitle.setTextColor(Colors.getInstance().getColor(OTTApplication.getContext().getResources(), R.color.c57_color));
        }
    }

    private void initSettingView(){
        //既不支持设置倍速播放，也不支持跳过片头片尾的时候，不展示设置
        if (!canSetSpeed && !canSetSkip){
            settingTitle.setVisibility(View.GONE);
        }

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

    @Override
    public boolean onKeyDown(View v, int keyCode, KeyEvent event, int position) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_DPAD_UP){
            if (null == horizontalGridView || horizontalGridView.getVisibility() != View.VISIBLE){
                if(position % 35 < 5){
                    episodeTitle.requestFocus();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onKeyUp(View v, int keyCode, KeyEvent event, int position) {

        return false;
    }
}
