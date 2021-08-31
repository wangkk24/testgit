package com.pukka.ydepg.moudule.vod.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.report.jiutian.JiutianService;
import com.pukka.ydepg.common.report.ubd.scene.UBDRecommendImpression;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.moudule.player.util.ViewkeyCode;
import com.pukka.ydepg.moudule.vod.NewAdapter.PlayBackRecycleViewAdapter;
import com.pukka.ydepg.moudule.vod.activity.NewVodDetailActivity;
import com.pukka.ydepg.moudule.vod.presenter.PlayBackListPresenter;
import com.pukka.ydepg.moudule.vod.utils.RemixRecommendUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 遥控器操作监听到下面的KeyCode时候,显示当前popupwindow;
 *
 *  keyCode == KeyEvent.KEYCODE_DPAD_CENTER
 *
 *  keyCode == KeyEvent.KEYCODE_ENTER
 *
 *  keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
 *
 *  keyCode == KeyEvent.KEYCODE_BACK
 **/

public class NewPlayBackViewPopwindow extends PopupWindow implements
        View.OnClickListener,View.OnKeyListener{
    private static final String TAG = "NewPlayBackViewPopwindo";

    private Context mContext;
    private Handler mHandler;

    private Button nextBtn;
    private Button exitBtn;
    private Button continueBtn;
    private HorizontalGridView listView;
    private List<VOD> vods = new ArrayList<>();
    private PlayBackRecycleViewAdapter adapter;

    private String vodid = "";
    private VOD vod;
    private boolean hasNext;

    private NewPlayBackViewPopwindow.OnKeyBackDismissListener mListener;

    /**
     * 用来标记是不是用户手动触发:
     * true是用户取消的,false不是用户手动操作取消的;
     */
    private boolean fromUserDismiss;

    /**
     * description: 构造方法
     * @author fuqiang
     * @date 2017/12/20 下午5:34
     * @version 1.0
     * @param context
     * @param handler
     */
    public NewPlayBackViewPopwindow(Context context, Handler handler, boolean hasNext) {
        this.mContext = context;
        this.mHandler = handler;
        this.hasNext = hasNext;
        initView();
        initOnKeyListener();
        initClickFocusListener();
        initRecycleView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.window_pip_playback_new, null);
        nextBtn = view.findViewById(R.id.button_next);
        continueBtn = view.findViewById(R.id.button_countie);
        exitBtn = view.findViewById(R.id.button_exit);
        listView = view.findViewById(R.id.playback_recommand_list);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        if (!hasNext) {
            nextBtn.setVisibility(View.GONE);
        }
    }

    private void initOnKeyListener() {
        nextBtn.setOnKeyListener(this);
        continueBtn.setOnKeyListener(this);
        exitBtn.setOnKeyListener(this);
    }

    private void initClickFocusListener() {
        nextBtn.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
    }

    private void initRecycleView(){
        listView.setNumRows(1);
        listView.addItemDecoration(new SpaceItemDecoration(mContext, mContext.getResources().getDimensionPixelSize(R.dimen.margin_16)));

        listView.setWindowAlignment(BaseGridView.WINDOW_ALIGN_LOW_EDGE);
        listView.setWindowAlignmentOffsetPercent(88f);

        adapter = new PlayBackRecycleViewAdapter(vods,mContext);

        adapter.setOnitemClick(new PlayBackRecycleViewAdapter.OnitemClick() {
            @Override
            public void onItemClick(View itemView) {
                if (itemView.getTag() instanceof VOD){
                    VOD vod = (VOD) itemView.getTag();

                    Intent intent = new Intent(mContext, NewVodDetailActivity.class);
                    intent.putExtra(NewVodDetailActivity.VOD_ID, vod.getID());
                    intent.putExtra(NewVodDetailActivity.ORGIN_VOD, vod);
                    //详情页UBD上报用数据
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_APP_POINTED_ID,RemixRecommendUtil.APPPINEDID_PLAY);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_RECOMMEND_TYEP,UBDRecommendImpression.recommendType_play);
                    intent.putExtra(NewVodDetailActivity.BUNDLE_UBD_SCENE_ID,      UBDRecommendImpression.sceneId_play);

                    if (null != vod.getFeedback()){
                        //传递九天播放上报url
                        if (!TextUtils.isEmpty(vod.getFeedback().getPlay_tracker())){
                            intent.putExtra(NewVodDetailActivity.JIUTIAN_TRACKER_URL, vod.getFeedback().getPlay_tracker());
                        }
                        if (!TextUtils.isEmpty(vod.getItemid())){
                            intent.putExtra(NewVodDetailActivity.JIUTIAN_ITEM_ID, vod.getItemid());
                        }
                        if (!TextUtils.isEmpty(vod.getFeedback().getClick_tracker())){
                            //九天点击上报
                            SuperLog.debug(TAG,"jiutian click");
                            JiutianService.reportClick(vod.getFeedback().getClick_tracker());
                        }
                    }

                    if (mContext instanceof RxAppCompatActivity){
                        ((RxAppCompatActivity) mContext).finish();
                    }
                    mContext.startActivity(intent);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    },500);


                }

            }
        });

        listView.setAdapter(adapter);
    }

    //加载推荐数据
    private void initList(final String contentId,VOD vod,String jiutianItemId){
        PlayBackListPresenter presenter = new PlayBackListPresenter();
        presenter.getRecommendList(vod, new PlayBackListPresenter.PlayBackCallback() {
            @Override
            public void getListSuccess(List<VOD> list,String tracker) {
                vods.addAll(list);
                Log.i(TAG, "getListSuccess: "+vods.size());
                if (mContext instanceof RxAppCompatActivity){
                    ((RxAppCompatActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null!= vods && null != adapter){
                                listView.setFocusable(true);
                                adapter.notifyDataSetChanged();
                                if (!TextUtils.isEmpty(tracker)){
                                    //九天上报
                                    SuperLog.debug(TAG,"jiutian display playback");
                                    JiutianService.reportDisplayInDetail(tracker,JiutianService.getJiutianRecommendContentIDs(list),jiutianItemId);
                                }

                                if(!CollectionUtil.isEmpty(vods)){
                                    String recommendContentIDs = UBDRecommendImpression.getRecommendContentIDs(vods);
                                    UBDRecommendImpression.record(RemixRecommendUtil.APPPINEDID_PLAY,UBDRecommendImpression.sceneId_play,recommendContentIDs,UBDRecommendImpression.recommendType_play,null,contentId);
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void getListFail() {
                vods.clear();
                Log.i(TAG, "getListFail: ");
            }
        });


    }

    /**
     * description: Pip界面子view响应的click事件
     * @author fuqiang
     * @date 2017/12/20 下午5:34
     * @version 1.0
     * @param v
     */
    @Override public void onClick(View v) {
        fromUserDismiss=true;
        dismiss();
        switch (v.getId()) {
            case R.id.button_countie:
                mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_CONTINUE_PLAY);
                break;
            case R.id.button_exit:
                mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_BACK_TV_PLAY);
                break;
            case R.id.rl_detail:
                mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_DETAIL_PLAY);
                break;
            case R.id.button_next:
                mHandler.sendEmptyMessage(ViewkeyCode.NEXT_PLAY);
                break;
        }
    }
    /**
     * description: 窗口销毁
     * @author fuqiang
     * @date 2017/12/20 下午5:34
     * @version 1.0
     */
    @Override
    public void dismiss() {
        super.dismiss();
        //不是用户手动dismiss的话,回调到当前fragment/activity中
        //防止重复操作,如:PopWindow销毁恢复播放这个操作
        if(null!=mListener && !fromUserDismiss){
            mListener.onDismiss();
        }
        //清空列表数据
        if (null != vods){
            vods.clear();
        }
        if (null != adapter){
            adapter.notifyDataSetChanged();
        }
    }

    //contentId为UBD上报使用
    public void showPlayBack(View parentView, boolean hasNext, OnKeyBackDismissListener listener,VOD vod,String jiutianItemId) {
        listView.setFocusable(false);
        this.vod = vod;
        vodid = vod.getID();
        initList(vod.getID(),vod,jiutianItemId);
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        this.hasNext=hasNext;
        this.mListener=listener;
        fromUserDismiss=false;
        if(!hasNext){
            nextBtn.setVisibility(View.GONE);
        }else {
            nextBtn.setVisibility(View.VISIBLE);
        }
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

        initOnKeyListener();
        initClickFocusListener();
        initFocus(exitBtn);
    }

    /**
     * description: 初始化获取焦点设置
     * @author fuqiang
     * @date 2017/12/20 下午5:33
     * @version 1.0
     * @param focusView
     */
    private void initFocus(View focusView) {
        focusView.setFocusable(true);
        focusView.requestFocus();
    }
    /**
     * description: onKey事件,View设置监听遥控器按键
     * @author fuqiang
     * @date 2017/12/20 下午5:33
     * @version 1.0
     * @param v
     * @param keyCode
     * @param event
     */
    @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int id=v.getId();
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if(id==R.id.button_next){
                    initFocus(exitBtn);
                }else if(id==R.id.button_exit){
                    initFocus(continueBtn);
                }else if(id==R.id.button_countie){
                    initFocus(continueBtn);
                }
                return true;
            }else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
                if(id==R.id.button_next){
                    initFocus(nextBtn);
                }else if(id==R.id.button_exit){
                    if (nextBtn.getVisibility() == View.VISIBLE){
                        initFocus(nextBtn);
                    }else{
                        initFocus(exitBtn);
                    }
                }else if(id==R.id.button_countie){
                    initFocus(exitBtn);
                }
                return true;
            }else if (keyCode==KeyEvent.KEYCODE_DPAD_UP){
                if (vods.size() == 0){
                   return true;
                }
            }
            if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                Log.i(TAG, "onKey: KEYCODE_MEDIA_PLAY_PAUSE");
                mHandler.sendEmptyMessage(ViewkeyCode.VIEW_KEY_CONTINUE_PLAY);
                fromUserDismiss=true;
                dismiss();
            }
        }
        return false;
    }

    public interface OnKeyBackDismissListener{
        void onDismiss();
    }

    public void setOnKeyBackDismissListener(OnKeyBackDismissListener listener){
        this.mListener = listener;
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

            outRect.left = 0;
            outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
            outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);

            outRect.right = mSpace / 2;
            outRect.left = mSpace / 2;

            if (pos == 0){
                outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.margin_76_6);
            }
        }
    }



}
