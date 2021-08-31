package com.pukka.ydepg.moudule.vod.playerController;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.Episode;
import com.pukka.ydepg.common.http.v6bean.v6node.Topic;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.http.v6bean.v6node.VODDetail;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.event.PlayerSkipChangeEvent;
import com.pukka.ydepg.event.PlayerSpeedChangeEvent;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.mytv.ordercenter.newOrderCenter.NewProductOrderActivity;
import com.pukka.ydepg.moudule.vod.activity.BrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.activity.NewBrowseTVPlayFragment;
import com.pukka.ydepg.moudule.vod.presenter.VoddetailEpsiodePresenter;
import com.pukka.ydepg.moudule.vod.utils.BrowseEpsiodesUtils;
import com.pukka.ydepg.moudule.vod.utils.VoddetailEpsiodesUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class VodPlayerControllerView extends PopupWindow {
    private static final String TAG = "VodPlayerControllerView";

    private Context mContext;

    private VerticalGridView recyclerView;

    private ControllerListAdapter adapter;

    private VODDetail mVodDetail;

    private BrowseEpsiodesUtils utils;

    //是否可以设置倍速
    private boolean canSetSpeed;

    //是否可以设置跳过片头片尾
    private boolean canSetSkip;

    private Handler handler;

    private String fatherPrice;

    float nowSpeed;

    //自动隐藏倒计时
    public int countdown = 8;
    private static final int COUNT_DOWN_FLAG = 1000099;
    private CountDownHandler mCountDownHandler = new CountDownHandler(this);

    public static class CountDownHandler extends Handler {
        private WeakReference<VodPlayerControllerView> mReference;

        CountDownHandler(VodPlayerControllerView view) {
            this.mReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what ==  COUNT_DOWN_FLAG){
                mReference.get().countdown -- ;
                if (mReference.get().countdown > 0 && mReference.get().isShowing()){
                    sendEmptyMessageDelayed(COUNT_DOWN_FLAG,1000);
                }else{
                    removeMessages(COUNT_DOWN_FLAG);
                    mReference.get().dismiss();
                }
            }
        }
    }

    //倍速列表
    private List<Float> speedList = new ArrayList<>();

    public VodPlayerControllerView(Context context , boolean canSetSpeed, boolean canSetSkip , VODDetail vodDetail, BrowseEpsiodesUtils utils,Handler handler,String fatherPrice,float nowSpeed) {
        mContext = context;

        this.canSetSkip = canSetSkip;

        this.canSetSpeed = canSetSpeed;

        this.mVodDetail = vodDetail;

        this.utils = utils;

        this.handler = handler;

        this.fatherPrice = fatherPrice;

        this.nowSpeed = nowSpeed;

        if (null == utils){
            this.utils = new BrowseEpsiodesUtils(vodDetail.getID());
            this.utils.getSimpleVod(vodDetail.getID(), new VoddetailEpsiodePresenter.GetSimpleVodCallback() {
                @Override
                public void getSimpleVodSuccess(VODDetail vodDetail) {
                    initView();

                    initList();

                    initListener();
                }

                @Override
                public void getSimpleVodFail() {

                }
            });


        }else{
            initView();

            initList();

            initListener();
        }


    }

    private void initView(){
        Log.i(TAG, "showList: initView");
        initSpeedList();
        View view = LayoutInflater.from(mContext).inflate(R.layout.window_pip_controller, null);
        recyclerView = view.findViewById(R.id.controller_recyclerview);
        recyclerView.setNumColumns(1);
        recyclerView.addItemDecoration(new SpaceItemDecoration(mContext,0));
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initList(){
        Log.i(TAG, "showList: initList");
        adapter = new ControllerListAdapter(mVodDetail,canSetSpeed,canSetSkip,mContext,utils,recyclerView,fatherPrice,nowSpeed);
        adapter.setCountDownCallback(new HideCountDownCallback() {
            @Override
            public void onkey() {
                countdown = 8;
            }
        });


    }


    private void initListener(){
        //点击剧集，切集
        adapter.setEpisodeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message message = Message.obtain();
                message.what = BrowseTVPlayFragment.EPISODE_PLAY;
                Bundle bundle = new Bundle();
                String sitNum = (String) v.getTag();
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

        //点击跳过片头片尾
        adapter.setSkipClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = (String) v.getTag();
                if (str.equals(ControllerListItemAdapter.SkipOpen)){
                    //打开跳过片头片尾开关
                    SharedPreferenceUtil.getInstance().putBoolean(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", true);

                }else if (str.equals(ControllerListItemAdapter.SkipClose)){
                    //关闭跳过片头片尾开关
                    SharedPreferenceUtil.getInstance().putBoolean(SharedPreferenceUtil.Key.ONDEMAND_SKIP + "", false);

                }

                EventBus.getDefault().post(new PlayerSkipChangeEvent());
                dismiss();
            }
        });
        Log.i(TAG, "initListener: 设置倍速点击事件");

        //点击倍速
        adapter.setSpeedClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取倍速
                int speed = (int) v.getTag();
                Log.i(TAG, "onClick: 倍速点击事件"+speed + " "+ JsonParse.object2String(speedList));

                EventBus.getDefault().post(new PlayerSpeedChangeEvent(speed, speedList));

                dismiss();
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mCountDownHandler.sendEmptyMessageDelayed(COUNT_DOWN_FLAG,1000);
    }

    public void showList(View parentView){
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.RIGHT, 0, 0);
    }


    //recycleview的间距
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private static final String TAG = "CustomItemDecoration";

        private int mSpace;

        private Context mContext;

        /**
         * @param space 传入的值，其单位视为dp
         */
        public SpaceItemDecoration(Context context, float space) {
            SuperLog.debug(TAG, "space is " + space);
            this.mContext = context;
            this.mSpace = (int) space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            SuperLog.debug(TAG, "itemCount>>" + itemCount + ";Position>>" + pos);

            if (pos == 0){
                outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.margin_70);
            }else{
                outRect.top = 0;
            }
            outRect.left = 0;
//            outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
            outRect.bottom = 0;

            outRect.right = mSpace / 2;
            outRect.left = mSpace / 2;
        }
    }

    //初始化倍速
    private void initSpeedList(){
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
        Log.i(TAG, "initSpeedList: "+ JsonParse.object2String(speedList));
    }

    public interface HideCountDownCallback {

        void onkey();

    }

    @Override
    public void dismiss() {
        mCountDownHandler.removeMessages(COUNT_DOWN_FLAG);
        super.dismiss();
    }
}
