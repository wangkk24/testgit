package com.pukka.ydepg.moudule.catchup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.leanback.widget.VerticalGridView;

import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.node.VoiceBean;
import com.pukka.ydepg.launcher.ui.fragment.FocusInterceptor;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.catchup.adapter.CatchUpContentAdapter;
import com.pukka.ydepg.moudule.catchup.adapter.TVODProgramMenuItemListAdapter;
import com.pukka.ydepg.moudule.catchup.common.TVODDataUtil;
import com.pukka.ydepg.moudule.catchup.presenter.TVODContract;
import com.pukka.ydepg.moudule.catchup.presenter.TVODPresenter;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.mytv.NewMyMovieActivity;
import com.pukka.ydepg.moudule.vod.view.FocusVerticalGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 回看首页
 */
public class CatchUpActivity extends BaseActivity implements FocusInterceptor, TVODContract.View,
        BaseAdapter.OnItemListener, CatchUpContentAdapter.OnHasFocusChangePageNumListener {

    private static final String TAG = CatchUpActivity.class.getName();
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "channel_name";
    public static final String CHANNEL_DATE = "channel_date";
    private static final long DELAY_INVIDATE = 40;

    private VerticalGridView mCatagoryList;

    //栏目Name
    private TextView mTvClumnName;

    //频道总个数
    private TextView mTvTotalNum;

    //底部显示的页数
    private TextView mTvPageNum;

    //无数据
    private RelativeLayout mNoContentRela;

    //vod列表
    private FocusVerticalGridView mContentList;

    //频道适配器
    private CatchUpContentAdapter mContentAdapter;

    //频道总数
    private int mChannePagelNum = 0;

    //当前所在页数
    private int mPagelNum = 1;

    //栏目选中的index
    private int mSelectCloumnIndex = -1;

    //栏目适配器
    private TVODProgramMenuItemListAdapter mCloumnAdapter;

    //数据，HashMap,key=栏目id,value=该栏目下的频道集合
    private HashMap<String, List<ChannelDetail>> mDateHashMap = new HashMap<>();
    //栏目集合
    private List<String> mCloumnNamelList;
    //栏目集合
    private List<Subject> mSubjectList;

    //语音监听
    private Scene mFocusScene;

    //语音回调
    private Feedback mFeedback;

    //语音命令
    private List<String> keys = new ArrayList<>();

    //语音命令
    private List<String> directives = new ArrayList<>();

    //语音命令
    private List<String> fuzzys = new ArrayList<>();

    //所有频道列表
    private List<ChannelDetail> allChannelDetails = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_up);
        TVODDataUtil.getInstance().createChannelMapData();
        initViews();
        mFeedback = new Feedback(this);
        mContentList.setInterceptor(this);
        if (TVODDataUtil.getInstance().isFristIntoCatchupTv()) {
            //确保第一次进入回看会调用接口
            TVODDataUtil.getInstance().setListSubject(null);
            TVODDataUtil.getInstance().setFristIntoCatchupTv(false);
        }
        if (null == TVODDataUtil.getInstance().getListSubject()) {
            SuperLog.info2SD(TAG, "Begin to queryChannelSubjectList for TVOD.");
            ((TVODPresenter) presenter).queryChannelSubjectList();
        } else {
            SuperLog.info2SD(TAG, "Use cached channelSubjectList for TVOD.");
            setData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFocusScene = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopXiri();
    }

    private void stopXiri() {
        //可能未注册，释放会报错
        try {
            if (null != mFocusScene)
                mFocusScene.release();
        } catch (Exception e) {
            SuperLog.error(TAG, e.getMessage());
        }
    }

    private void startXiri() {
        String queryStr = getVoiceCommand();
        SuperLog.debug(TAG, "startXiri->isResume:" + getIsResume() + "|queryStr:" + queryStr);
        mFocusScene = new Scene(this);
        mFocusScene.init(new ISceneListener() {
            @Override
            public String onQuery() {
                return queryStr;
            }

            @Override
            public void onExecute(Intent intent) {
                RefreshManager.getInstance().getScreenPresenter().exit();
                mFeedback.begin(intent);
                if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(Constant.VoiceCommandScene.CATCHUP_ACTIVITY)) {
                    if (intent.hasExtra("_command")) {
                        String command = intent.getStringExtra("_command");
                        if (null != mCloumnNamelList && mCloumnNamelList.size() > 0) {
                            for (int i = 0; i < mCloumnNamelList.size(); i++) {
                                if (keys.get(i).equals(command)) {
                                    mFeedback.feedback(fuzzys.get(i), Feedback.EXECUTION);
                                    mCatagoryList.scrollToPosition(i);
                                    requestFocus(mCatagoryList.getLayoutManager().findViewByPosition(i));
                                    mCloumnAdapter.setIsSelect(true);
                                    return;
                                }

                            }
                            if (null != allChannelDetails && allChannelDetails.size() > 0) {
                                for (int i = 0; i < allChannelDetails.size(); i++) {
                                    if (allChannelDetails.get(i).getName().equals(command)) {
                                        mFeedback.feedback(allChannelDetails.get(i).getName().replace("加", "+"), Feedback.EXECUTION);
                                        switchmCatagoryList(allChannelDetails.get(i));
                                        break;
                                    }
                                }
                            }
                            if (("key" + mCloumnNamelList.size()).equals(command) || ("key" + (mCloumnNamelList.size() + 1)).equals(command)) {
                                mFeedback.feedback("播放记录", Feedback.EXECUTION);
                                Intent intent1 = new Intent(CatchUpActivity.this, NewMyMovieActivity.class);
                                intent1.putExtra("id", "1");
                                startActivity(intent1);
                            }
                            if (("key" + (mCloumnNamelList.size() + 2)).equals(command)) {
                                CatchUpActivity.this.finish();
                            }
                        }
                    }
                }
            }
        });
    }

    public void switchmCatagoryList(int position) {
        Intent intent = new Intent(CatchUpActivity.this, TVODProgramListActivity.class);
        intent.putExtra(CatchUpActivity.CHANNEL_ID, mContentAdapter.getItemPosition
                (position).getID());
        intent.putExtra(CatchUpActivity.CHANNEL_NAME, mContentAdapter.getItemPosition
                (position).getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void switchmCatagoryList(ChannelDetail mChannelDetail) {
        if (null == mChannelDetail) {
            return;
        }
        Intent intent = new Intent(CatchUpActivity.this, TVODProgramListActivity.class);
        intent.putExtra(CatchUpActivity.CHANNEL_ID, mChannelDetail.getID());
        intent.putExtra(CatchUpActivity.CHANNEL_NAME, mChannelDetail.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public String getVoiceCommand() {
        keys.clear();
        directives.clear();
        fuzzys.clear();
        if (null != mCloumnNamelList && !mCloumnNamelList.isEmpty()) {
            for (int i = 0; i < mCloumnNamelList.size(); i++) {
                keys.add("key" + i);
                directives.add("subject" + i);
                fuzzys.add(mCloumnNamelList.get(i));

            }
            keys.add("key" + mCloumnNamelList.size());
            directives.add("history");
            fuzzys.add("播放记录");
            keys.add("key" + (mCloumnNamelList.size() + 1));
            directives.add("history1");
            fuzzys.add("观看记录");
            keys.add("key" + (mCloumnNamelList.size() + 2));
            directives.add("quit");
            fuzzys.add("退出");


        }
        HashMap<String, List<ChannelDetail>> channelMap = TVODDataUtil.getInstance().getMapSubjectName2listChannel();
        if (null != channelMap && !channelMap.isEmpty()) {
            for (String key : channelMap.keySet()) {
                List<ChannelDetail> channelDetailList = channelMap.get(key);
                if (null != channelDetailList && !channelDetailList.isEmpty()) {
                    allChannelDetails.addAll(channelDetailList);
                }
            }
        }
        if (null != allChannelDetails && allChannelDetails.size() > 0) {
            for (int i = 0; i < allChannelDetails.size(); i++) {
                int m = i + (mCloumnNamelList == null ? 0 : mCloumnNamelList.size());
                keys.add(allChannelDetails.get(i).getName());
                directives.add("channel" + m);
                fuzzys.add(allChannelDetails.get(i).getName().replace("+", "加"));
            }
        }

        return JsonParse.object2String(new VoiceBean(Constant.VoiceCommandScene.CATCHUP_ACTIVITY, keys, directives, fuzzys));
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        presenter = new TVODPresenter(this);
    }

    private void initViews() {
        mCatagoryList = (VerticalGridView) findViewById(R.id.list_column);
        mContentList = (FocusVerticalGridView) findViewById(R.id.movies_list);
        mTvClumnName = (TextView) findViewById(R.id.tv_column_name);
        mTvTotalNum = (TextView) findViewById(R.id.tv_num);
        mTvPageNum = (TextView) findViewById(R.id.tv_page_num);
        mNoContentRela = (RelativeLayout) findViewById(R.id.no_content_rela);
        initContentList();
    }

    @SuppressLint("RestrictedApi")
    private void initContentList() {
        mTvClumnName.setText(getResources().getString(R.string.tvod_cloumn_name));

        //设置内容Recycle View Adapter
        mContentList.setFocusable(false);
        mContentList.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ITEM);
        mContentList.setNumColumns(4);

        //设置栏目Recycle View Adapter
        mCloumnAdapter = new TVODProgramMenuItemListAdapter(this, "MENU_ITEM_TAG", new ArrayList<>());
        mCloumnAdapter.setOnItemListener(this);
        mCatagoryList.setAdapter(mCloumnAdapter);
        //mCatagoryList.addItemDecoration(new ProgramListItemDecoration());
        requestFocus(mCatagoryList.getLayoutManager().findViewByPosition(0));

        //设置内容Recycle View Adapter
        mContentAdapter = new CatchUpContentAdapter(this, new ArrayList<>());
        mContentAdapter.setmOnHasFocusChangePageNumListener(this);
        mContentAdapter.setOnItemKeyListener((keyCode, event, position, viewType) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                //跳往频道回看节目单列表界面
                switchmCatagoryList(position);
                return true;
            }
            return false;
        });
        mContentList.setAdapter(mContentAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TVODDataUtil.getInstance().clearTVODProgramList();
        if (getIsResume() && null != mCloumnNamelList && mCloumnNamelList.size() > 0) {
            startXiri();
        }

    }

    @Override
    public boolean interceptFocus(KeyEvent event, View view) {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //处理焦点在栏目最上和最下时的操作
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && mCatagoryList.hasFocus() && mSelectCloumnIndex == 0) {
            mCatagoryList.smoothScrollToPosition(mCloumnNamelList.size() - 1);
            mCatagoryList.setSelectedPosition(mCloumnNamelList.size() - 1);
            return true;
        }

        //处理：焦点在栏目上，按右键，让栏目字体高亮
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

            //没有数据时不移动焦点
            if (mContentAdapter.getItemCount() == 0) {
                return true;
            }

            if (mCatagoryList.hasFocus()) {
                mCloumnAdapter.setIsSelect(true);
                if (-1 == mContentAdapter.getRecordFocusPosition()) {
                    requestFocus(mContentList.getLayoutManager().findViewByPosition(0));
                } else {
                    requestFocus(mContentList.getLayoutManager().findViewByPosition(mContentAdapter.getRecordFocusPosition()));
                }
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            //解决最底部频道焦点不下移问题
            if (mContentList.hasFocus()) {
                int focusPosition = mContentAdapter.getRecordFocusPosition();
                if (mContentAdapter.getItemCount() > 4
                        && mContentAdapter.getItemCount() % 4 != 0
                        && focusPosition + 5 > mContentAdapter.getItemCount()) {
                    requestFocus(mContentList.getLayoutManager().findViewByPosition(mContentAdapter.getItemCount() - 1));
                }
            } else if (mCatagoryList.hasFocus() && mSelectCloumnIndex == mCloumnNamelList.size() - 1) {
                //解决栏目列表循环落焦
                mCatagoryList.smoothScrollToPosition(0);
                mCatagoryList.setSelectedPosition(0);
                return true;
            }
        }

        //焦点在频道内容上，点击返回按钮，焦点落到栏目上
        if (keyCode == KeyEvent.KEYCODE_BACK && mContentList.hasFocus() && null != mCatagoryList
                .getLayoutManager().findViewByPosition(mCloumnAdapter.getRecordFocusPosition())) {
            mCatagoryList.getLayoutManager().findViewByPosition(mCloumnAdapter.getRecordFocusPosition()).requestFocus();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onQueryChannelSubjectListSuccess() {
        setData();
    }

    /**
     * 强制让View获取焦点
     */
    private void requestFocus(View view) {
        if (null != view) {
            view.setFocusable(true);
            view.postDelayed(view::requestFocus, DELAY_INVIDATE);
        }
    }


    //设置Data到Adapter
    @SuppressLint("SetTextI18n")
    private void setData() {
        mCloumnNamelList = TVODDataUtil.getInstance().getListSubjectName();
        mSubjectList = TVODDataUtil.getInstance().getListSubject();
        mDateHashMap = TVODDataUtil.getInstance().getMapSubjectName2listChannel();
        mCloumnAdapter.bindData(mCloumnNamelList);
        //setDataOnSelect(0);
    }

    //设置频道Adapter内容
    @SuppressLint("SetTextI18n")
    private void setContentAdapterData(List<ChannelDetail> list) {
        //设置左上角总频道数目
        mTvTotalNum.setText(String.format(getString(R.string.tvod_channel_all_num), list.size() + ""));
        //总页数
        mChannePagelNum = getPageNum(list.size());
        //设置底部显示当前所在的页码
        if (mChannePagelNum > 0) {
            mTvPageNum.setText(mPagelNum + "/" + mChannePagelNum);
        } else {
            mTvPageNum.setText("");
        }

        mContentAdapter.clearAll();
        mContentAdapter.bindData(list);
        if (getIsResume() && null != mCloumnNamelList && mCloumnNamelList.size() > 0) {
            stopXiri();
            startXiri();
        }
        if (list.size() > 0) {
            setContentViewIsVisibility(list);
        } else {
            setContentViewIsVisibility(null);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setContentAdapterDataWithRefresh(List<ChannelDetail> list) {
        mPagelNum = 1;
        //设置左上角总频道数目
        mTvTotalNum.setText(String.format(getString(R.string.tvod_channel_all_num), list.size() + ""));
        //总页数
        mChannePagelNum = getPageNum(list.size());
        //设置底部显示当前所在的页码
        if (mChannePagelNum > 0) {
            mTvPageNum.setText(mPagelNum + "/" + mChannePagelNum);
        } else {
            mTvPageNum.setText("");
        }

        //重新初始化mContentAdapter,使表格滚到最上方
        mContentAdapter = null;
        mContentAdapter = new CatchUpContentAdapter(this, new ArrayList<>());
        mContentAdapter.setmOnHasFocusChangePageNumListener(this);
        mContentAdapter.setOnItemKeyListener((keyCode, event, position, viewType) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                //跳往频道回看节目单列表界面
                switchmCatagoryList(position);
                return true;
            }
            return false;
        });
        mContentAdapter.setRecordFocusPosition(0);
        mContentList.setAdapter(mContentAdapter);

//        mContentAdapter.clearAll();
        mContentAdapter.bindData(list);
        if (getIsResume() && null != mCloumnNamelList && mCloumnNamelList.size() > 0) {
            stopXiri();
            startXiri();
        }
        if (list.size() > 0) {
            setContentViewIsVisibility(list);
        } else {
            setContentViewIsVisibility(null);
        }
    }


    //返回频道页数
    private int getPageNum(int num) {
        if (num % 16 > 0) {
            return num / 16 + 1;
        } else {
            return num / 16;
        }
    }

    @Override
    public void onQueryChannelSubjectListFailed() {
    }

    //根据栏目ID查询频道
    @Override
    public void onQueryChannelStcPropsBySubjectSuccess(List<ChannelDetail> channelDetails, int position) {
        setContentViewIsVisibility(channelDetails);
        mDateHashMap.put(mCloumnNamelList.get(position), channelDetails);
        TVODDataUtil.getInstance().setMapSubjectName2listChannel(mCloumnNamelList.get(position), channelDetails);
        setContentAdapterDataWithRefresh(channelDetails);
    }

    //根据栏目ID查询频道失败
    @Override
    public void onQueryChannelStcPropsBySubjectFailed() {
        setContentViewIsVisibility(null);
    }

    //栏目选中事件
    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelect(String viewType, int position) {
        setDataOnSelect(position);
    }

    //栏目点击事件
    @Override
    public void onItemClickListener(String viewType, int position) {
    }

    //设置底部当前所在的页数显示
    @SuppressLint("SetTextI18n")
    @Override
    public void onItemFocusChangeNum(int position) {
        mPagelNum = getPageNum(position + 1);
        mTvPageNum.setText(mPagelNum + "/" + mChannePagelNum);
    }

    //移动焦点 处理移动事件，首次默认position为0
    private void setDataOnSelect(int position) {
        if (null != mDateHashMap && mDateHashMap.size() > 0 && null != mDateHashMap.get(mCloumnNamelList.get(position))) {
            if (mSelectCloumnIndex != position) {
                mContentAdapter.setRecordFocusPosition(0);
                mContentList.scrollToPosition(0);
                mSelectCloumnIndex = position;
                mPagelNum = 1;
                setContentAdapterDataWithRefresh(mDateHashMap.get(mCloumnNamelList.get(position)));
            } else {
                setContentAdapterData(mDateHashMap.get(mCloumnNamelList.get(position)));
            }
        } else {
            mSelectCloumnIndex = position;
            mContentAdapter.clearAll();
            mContentAdapter.notifyDataSetChanged();
            mTvPageNum.setText("");
            ((TVODPresenter) presenter).queryChannelStcPropsBySubject(mSubjectList.get(position).getID(), position);
        }
    }

    private void setContentViewIsVisibility(List<ChannelDetail> channelDetails) {
        if (null == channelDetails || channelDetails.size() == 0) {
            if (mNoContentRela.getVisibility() == View.GONE)
                mNoContentRela.setVisibility(View.VISIBLE);
            if (mContentList.getVisibility() == View.VISIBLE) mContentList.setVisibility(View.GONE);
        } else {
            if (mNoContentRela.getVisibility() == View.VISIBLE)
                mNoContentRela.setVisibility(View.GONE);
            if (mContentList.getVisibility() == View.GONE) mContentList.setVisibility(View.VISIBLE);
        }
    }
}