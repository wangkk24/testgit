/*
 *Copyright (C) 2018 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.moudule.catchup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.http.v6bean.V6Constant;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthorizeResult;
import com.pukka.ydepg.common.http.v6bean.v6node.ChannelDetail;
import com.pukka.ydepg.common.http.v6bean.v6node.Content;
import com.pukka.ydepg.common.http.v6bean.v6node.PlayVodBean;
import com.pukka.ydepg.common.http.v6bean.v6request.PlayChannelRequest;
import com.pukka.ydepg.common.refresh.RefreshManager;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.timeutil.DateCalendarUtils;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.bean.node.VoiceBean;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.catchup.adapter.TVODProgramListAdapter;
import com.pukka.ydepg.moudule.catchup.adapter.TVODProgramMenuItemListAdapter;
import com.pukka.ydepg.moudule.catchup.common.TVODDataUtil;
import com.pukka.ydepg.moudule.catchup.presenter.TVODProgramListPresenter;
import com.pukka.ydepg.moudule.catchup.presenter.contract.TVODProgramListContract;
import com.pukka.ydepg.moudule.livetv.adapter.BaseAdapter;
import com.pukka.ydepg.moudule.livetv.utils.LiveDataHolder;
import com.pukka.ydepg.moudule.livetv.utils.LiveUtils;
import com.pukka.ydepg.moudule.player.node.Program;
import com.pukka.ydepg.moudule.player.node.Schedule;
import com.pukka.ydepg.moudule.player.ui.OnDemandVideoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 频道回看节目单列表界面
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: TVODProgramListActivity
 * @Package com.pukka.ydepg.moudule.catchup.activity
 * @date 2018/09/20 19:12
 */
public class TVODProgramListActivity extends BaseActivity<TVODProgramListContract.Presenter> implements BaseAdapter.OnItemListener, BaseAdapter.OnItemkeyListener, TVODProgramListContract.View {

    private static final String TAG = "TVODProgramListActivity";

    private static final long DELAY_INVIDATE = 40;

    @BindView(R.id.tv_column_name)
    TextView mChannelName;

    @BindView(R.id.list_column)
    VerticalGridView mDateList;

    @BindView(R.id.tv_program_count)
    TextView mProgramCount;

    @BindView(R.id.list_page_left)
    VerticalGridView mPageListLeft;

    @BindView(R.id.list_page_right)
    VerticalGridView mPageListRight;

    @BindView(R.id.tv_page_index)
    TextView mPageIndexValue;

    @BindView(R.id.rl_no_program_data)
    RelativeLayout mRlNoProgramData;

    @BindView(R.id.v_line)
    View mLine;

    private static final String TVOD_MENU_ITEM_TAG = "TVOD_MENU_ITEM_TAG";

    private static final String LEFT_PAGE_TAG = "LEFT_PAGE_TAG";

    private static final String RIGHT_PAGE_TAG = "RIGHT_PAGE_TAG";

    private TVODProgramMenuItemListAdapter mDateListAdapter;

    private TVODProgramListAdapter mLeftPageProgramListAdapter;

    private TVODProgramListAdapter mRightPageProgramListAdapter;

    private List<String> mOriginalList;

    private List<String> mChannelIds = new ArrayList<>();

    private String mCurrentSelectDateKey = "";

    private List<ArrayMap<Integer, List<Program>>> mAllProgramList;

    private int mCurrentPageIndex = 0;

    private String mVideoName;

    private String mTVODPlaybillID;

    /**
     * 最新item选中的时间戳
     */
    private long mLastItemSelectTime;

    /**
     * 最新一次Action_Down时间戳
     */
    private long mLastDownTime;

    /**
     * 总页数
     */
    private long mTotalPageSize;
    /**
     * 第一次获得焦点的日期
     */
    private String firstFocusDate;

    /**
     * 语音监听
     */
    private Scene mFocusScene;

    /**
     *  语音回调
     */
    private Feedback mFeedback;

    /**
     * 语音命令
     */
    private  List<String> keys = new ArrayList<>();

    /**
     * 语音命令
     */
    private  List<String> directives = new ArrayList<>();

    /**
     * 语音命令
     */
    private  List<String> fuzzys = new ArrayList<>();

    /**
     * 显示的天数
     */
    private List<String> showDateList;

    /**
     * 每页左界面单列表
     */
    private  List<Program> pageLeftProgramList;

    /**
     * 每页右界面单列表
     */
    private  List<Program> pageRightProgramList;

    private List<ChannelDetail>  allChannelDetails=new ArrayList<>();

    @Override
    protected void initPresenter() {
        presenter = new TVODProgramListPresenter();
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


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mFocusScene = null;
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        stopXiri();
    }


    /**
     * 释放语音命令
     */
    private void stopXiri()
    {
        //可能未注册，释放会报错
        try
        {
            if (null != mFocusScene)
                mFocusScene.release();
        }catch (Exception e){
            SuperLog.error(TAG,e.getMessage());
        }
    }

    /**
     * 启动语音
     */
    private void startXiri()
    {
        String queryStr=getVoiceCommand();
        Log.e(TAG,"startXiri->isResume:"+getIsResume()+"|queryStr:"+queryStr);
        mFocusScene = new Scene(this);
        mFocusScene.init(new ISceneListener()
        {
            @Override
            public String onQuery()
            {
                return queryStr;
            }

            @Override
            public void onExecute(Intent intent) {
                RefreshManager.getInstance().getScreenPresenter().exit();
                mFeedback.begin(intent);
                if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(Constant
                    .VoiceCommandScene.TVOD_PROGRAM))
                {
                    if (intent.hasExtra("_command"))
                    {
                        String command = intent.getStringExtra("_command");
                        if(command.equals("keyup")){
                            mFeedback.feedback("上一页", Feedback.EXECUTION);
                            if(null != getCurrentFocus() && getCurrentFocus().getParent()==mDateList)
                            {
                                mDateListAdapter.setIsSelect(true);
                            }

                            loadProgramData(true,LEFT_PAGE_TAG);
                            return;
                        }
                        if(command.equals("keydown")){
                            mFeedback.feedback("下一页", Feedback.EXECUTION);
                            if(null != getCurrentFocus() && getCurrentFocus().getParent()==mDateList)
                            {
                                mDateListAdapter.setIsSelect(true);
                            }
                            loadProgramData(false,LEFT_PAGE_TAG);
                            return;
                        }
                        //日期
                        if(null!=showDateList&&showDateList.size()>0)
                        {
                            for (int i = 0; i < showDateList.size();i++){
                                if(keys.get(i).equals(command)){
                                    mFeedback.feedback(fuzzys.get(i), Feedback.EXECUTION);
                                    mDateList.scrollToPosition(i);
                                    requestFocus(mDateList.getLayoutManager().findViewByPosition(i));
                                    mDateListAdapter.setIsSelect(true);
                                    return;
                                }

                            }
                            //左侧节目单
                            if(null!=pageLeftProgramList&&pageLeftProgramList.size()>0){
                                for (int i = showDateList.size(); i <showDateList.size()+pageLeftProgramList.size(); i++)
                                {
                                    if(keys.get(i).equals(command))
                                    {
                                        if(null != getCurrentFocus() && getCurrentFocus().getParent()==mDateList)
                                        {
                                            mDateListAdapter.setIsSelect(true);
                                        }
                                        mFeedback.feedback(fuzzys.get(i), Feedback.EXECUTION);
                                        requestFocus(mPageListLeft.getLayoutManager().findViewByPosition(i - showDateList.size()));
                                        onItemClickListener(LEFT_PAGE_TAG,i-showDateList.size());
                                        return;
                                    }

                                }
                            }
                            //右侧节目单
                            int m=pageLeftProgramList==null?0:pageLeftProgramList.size();
                            if(null!=pageRightProgramList&&pageRightProgramList.size()>0){
                                for (int i = showDateList.size()+m; i < showDateList.size()+m+pageRightProgramList.size(); i++)
                                {
                                    if(keys.get(i).equals(command))
                                    {
                                        if(null != getCurrentFocus() && getCurrentFocus().getParent()==mDateList)
                                        {

                                            mDateListAdapter.setIsSelect(true);
                                        }
                                        mFeedback.feedback(fuzzys.get(i), Feedback.EXECUTION);
                                        requestFocus(mPageListRight.getLayoutManager().findViewByPosition(i - showDateList.size()-m));
                                        onItemClickListener(RIGHT_PAGE_TAG,i-showDateList.size()-m);
                                        return;
                                    }

                                }
                            }
                            if(null!=allChannelDetails&&allChannelDetails.size()>0)
                            {
                                for(int i=0;i<allChannelDetails.size();i++){
                                    if(allChannelDetails.get(i).getName().equals(command)){
                                        mFeedback.feedback(allChannelDetails.get(i).getName().replace("加", "+"), Feedback.EXECUTION);
                                        switchmCatagoryList(allChannelDetails.get(i));
                                        break;
                                    }

                                }

                            }

                        }

                    }
                }
            }
        });
    }

    public void switchmCatagoryList(ChannelDetail mChannelDetail){
        if(null==mChannelDetail){
            return;
        }
        Intent intent = new Intent(TVODProgramListActivity.this, TVODProgramListActivity.class);
        intent.putExtra(CatchUpActivity.CHANNEL_ID, mChannelDetail.getID());
        intent.putExtra(CatchUpActivity.CHANNEL_NAME, mChannelDetail.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    public String getVoiceCommand(){
        keys.clear();
        directives.clear();
        fuzzys.clear();
        if (null != showDateList && !showDateList.isEmpty())
        {
            for (int i = 0; i < showDateList.size(); i++)
            {
                keys.add("key"+i);
                directives.add("date"+i);
                fuzzys.add(showDateList.get(i));

            }
        }
        if(null!=pageLeftProgramList&&pageLeftProgramList.size()>0 && null != showDateList){
            for (int i = 0; i < pageLeftProgramList.size(); i++)
            {
                int m=i+showDateList.size();
                keys.add("key"+m);
                directives.add("program"+m);
                fuzzys.add(DateCalendarUtils.formatDate(Long.parseLong(pageLeftProgramList.get(i).getStartTime()),"HH:mm")+" "+pageLeftProgramList.get(i).getName());
            }
        }

        if(null!=pageRightProgramList&&pageRightProgramList.size()>0 && null != showDateList){
            for (int i = 0; i < pageRightProgramList.size(); i++)
            {
                int m=i+showDateList.size()+(pageLeftProgramList==null?0:pageLeftProgramList.size());
                keys.add("key"+m);
                directives.add("program"+m);
                fuzzys.add(DateCalendarUtils.formatDate(Long.parseLong(pageRightProgramList.get(i).getStartTime()),"HH:mm")+" "+pageRightProgramList.get(i).getName());
            }
        }
        if(null!=allChannelDetails&&allChannelDetails.size()>0 && null != showDateList){
            for (int i = 0; i < allChannelDetails.size(); i++)
            {
                int m=i+showDateList.size()+(pageLeftProgramList==null?0:pageLeftProgramList.size())+(pageRightProgramList==null?0:pageRightProgramList.size());
                keys.add(allChannelDetails.get(i).getName());
                directives.add("channel"+m);
                fuzzys.add(allChannelDetails.get(i).getName().replace("+","加"));
            }
        }

        keys.add("keyup");
        directives.add("pageup");
        fuzzys.add("上一页");
        keys.add("keydown");
        directives.add("pagedown");
        fuzzys.add("下一页");
        return JsonParse.object2String(new VoiceBean(Constant.VoiceCommandScene.TVOD_PROGRAM,keys,directives,fuzzys));
    }
    /**
     * 根据位置获取recycleview,itemview
     * @param position 位置
     */
    private View findItemView(int position,VerticalGridView listView){
        RecyclerView.ViewHolder holder=listView.findViewHolderForLayoutPosition(position);
        View view;
        if(null!=holder){
            view=holder.itemView;
        }else{
            view = listView.getLayoutManager().findViewByPosition(position);
        }
        return view;
    }

    /**
     * 设置listView的itemView获取焦点
     * @param position 记忆的position
     * @param viewType 哪个listView
     */
    private void requestListItemViewFocus(int position,String viewType){
        if(viewType.equals(LEFT_PAGE_TAG)){
            requestFocus(findItemView(position,mPageListLeft));
        }else if(viewType.equals(RIGHT_PAGE_TAG)){
            requestFocus(findItemView(position,mPageListRight));
        }else if(viewType.equals(TVOD_MENU_ITEM_TAG)){
            requestFocus(findItemView(position,mDateList));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvod_program_list);
        ButterKnife.bind(this);
        mChannelName.setSelected(true);
        mFeedback = new Feedback(this);
        mChannelIds.add(getIntent().getStringExtra(CatchUpActivity.CHANNEL_ID));
        mChannelName.setText(getIntent().getStringExtra(CatchUpActivity.CHANNEL_NAME));
        firstFocusDate=getIntent().getStringExtra(CatchUpActivity.CHANNEL_DATE);
        mDateListAdapter = new TVODProgramMenuItemListAdapter(this, TVOD_MENU_ITEM_TAG, new ArrayList<>());
        mDateListAdapter.setOnItemListener(this);
        mDateList.setAdapter(mDateListAdapter);
        mLeftPageProgramListAdapter = new TVODProgramListAdapter(this, LEFT_PAGE_TAG, new ArrayList<>());
        mRightPageProgramListAdapter = new TVODProgramListAdapter(this, RIGHT_PAGE_TAG, new ArrayList<>());
        mPageListLeft.setAdapter(mLeftPageProgramListAdapter);
        mPageListRight.setAdapter(mRightPageProgramListAdapter);
        presenter.createTVODDateList();
        mLeftPageProgramListAdapter.setOnItemListener(this);
        mRightPageProgramListAdapter.setOnItemListener(this);
        mRightPageProgramListAdapter.setOnItemListener(this);
        mLeftPageProgramListAdapter.setOnItemKeyListener(this);
        mRightPageProgramListAdapter.setOnItemKeyListener(this);
        mDateListAdapter.setOnItemKeyListener(this);
        HashMap<String, List<ChannelDetail>> channelMap= TVODDataUtil.getInstance().getMapSubjectName2listChannel();
        if(null!=channelMap&&!channelMap.isEmpty()){
            for(String key:channelMap.keySet()){
                List<ChannelDetail> channelDetailList=channelMap.get(key);
                if(null!=channelDetailList&&!channelDetailList.isEmpty()){
                    allChannelDetails.addAll(channelDetailList);
                }
            }
        }

    }

    private void loadProgramData(boolean isKeyUp,String viewType){
        //添加对坚果投影仪的适配，设备是投影仪时不做这个保护
        if((System.currentTimeMillis()-mLastItemSelectTime<500
                || System.currentTimeMillis()-mLastDownTime<500 ) && !CommonUtil.isJMGODevice()){
            return;
        }
        if(isKeyUp){
            if(mCurrentPageIndex==0){
                if(RIGHT_PAGE_TAG.equals(viewType)&&mLeftPageProgramListAdapter.getItemCount()>0){
                    requestListItemViewFocus(mLeftPageProgramListAdapter.getItemCount()-1,LEFT_PAGE_TAG);
                }
                return;
            }
            --mCurrentPageIndex;
            if(mCurrentPageIndex<0){
                mCurrentPageIndex=0;
            }
        }else{
            int totalPage=mAllProgramList.size()-1;
            if(mCurrentPageIndex==totalPage){
                if(mCurrentPageIndex==0&&LEFT_PAGE_TAG.equals(viewType)&&mRightPageProgramListAdapter.getItemCount()!=0){
                    requestListItemViewFocus(0,RIGHT_PAGE_TAG);
                }
                return;
            }
            //加载下一页数据
            ++mCurrentPageIndex;
        }
        mLeftPageProgramListAdapter.clearAll();
        mRightPageProgramListAdapter.clearAll();
        mLeftPageProgramListAdapter.setRecordFocusPosition(0);
        mRightPageProgramListAdapter.setRecordFocusPosition(0);
        pageLeftProgramList=mAllProgramList.get(mCurrentPageIndex).get(0);
        pageRightProgramList=mAllProgramList.get(mCurrentPageIndex).get(1);
        mLeftPageProgramListAdapter.bindData(pageLeftProgramList);
        mRightPageProgramListAdapter.bindData(pageRightProgramList);
        if(viewType.equals(RIGHT_PAGE_TAG) && mRightPageProgramListAdapter.getItemCount()==0){
            mPageListLeft.setFocusable(true);
            mPageListLeft.requestFocus();
            //第二列节目单没有数据的时候
            requestListItemViewFocus(0,LEFT_PAGE_TAG);
        }else{
            requestListItemViewFocus(0,viewType);
            if(isKeyUp){
                selectShortestPath(0,viewType);
            }
        }
        stopXiri();

        startXiri();
    }

    @Override
    public void onTVODDateList(List<String> originalList, List<String> dateList,List<String> voiceDateList) {
        SuperLog.debug(TAG,"onTVODDateList->firstFocusDate:"+firstFocusDate);
        mOriginalList = originalList;
        mDateListAdapter.bindData(dateList);
        showDateList=voiceDateList;
        if(!TextUtils.isEmpty(firstFocusDate)&&mOriginalList.contains(firstFocusDate)){
            int index=mOriginalList.indexOf(firstFocusDate);
            new Handler().postDelayed(()-> {
                    SuperLog.error(TAG,"onTVODDateList->dateview is "+mDateList.getLayoutManager().findViewByPosition(index));
                    requestFocus(mDateList.getLayoutManager().findViewByPosition(index));

            },100);
        }else
        {
            requestFocus(mDateList.getLayoutManager().findViewByPosition(0));
        }
    }

    @Override
    public void onPlayBillListEmpty(String dateValueKey) {
        if (mCurrentSelectDateKey.equals(dateValueKey)) {
            mProgramCount.setText("");
            mPageIndexValue.setText("");
            mLine.setVisibility(View.INVISIBLE);
            mRlNoProgramData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onQueryPlayBillListSuccess(String dateValueKey, int programSize, List<ArrayMap<Integer, List<Program>>> allProgramList) {
        if (mCurrentSelectDateKey.equals(dateValueKey)) {
            mProgramCount.setText(String.format(getString(R.string.tvod_channel_program_size), programSize + ""));
            mRlNoProgramData.setVisibility(View.GONE);
            mLine.setVisibility(View.VISIBLE);
            mAllProgramList = allProgramList;
            mTotalPageSize=programSize;
            mCurrentPageIndex = 0;
            pageLeftProgramList=allProgramList.get(mCurrentPageIndex).get(0);
            pageRightProgramList=allProgramList.get(mCurrentPageIndex).get(1);
            mLeftPageProgramListAdapter.bindData(pageLeftProgramList);
            mRightPageProgramListAdapter.bindData(pageRightProgramList);
        }
        stopXiri();
        startXiri();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(null!=showDateList&&showDateList.size()>0){
            stopXiri();
            startXiri();
        }
    }

    @Override
    public void onQueryPlaybillListFailed() {

    }

    /**
     * 回看播放鉴权成功
     */
    @Override
    public void onPlayChannelUrlSuccess(String channelID, String url, String bookMark) {
        //启动点播视频播放
        Intent intent = new Intent(this, OnDemandVideoActivity.class);
        PlayVodBean playVodBean=new PlayVodBean();
        playVodBean.setPlayUrl(url);
        playVodBean.setVodId(mTVODPlaybillID);
        playVodBean.setVodName(mVideoName);
        playVodBean.setBookmark(bookMark);
        playVodBean.setVodType(Content.PROGRAM);
//        intent.putExtra(OnDemandVideoActivity.PLAY_VOD_BEAN, JsonParse.object2String(playVodBean));
        LiveDataHolder.get().setPlayVodBean(JsonParse.object2String(playVodBean));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 回看播放鉴权失败
     *
     * @param authorizeResult 认证结果集
     */
    @Override
    public void onPlayChannelUrlFailed(AuthorizeResult authorizeResult) {
        SuperLog.error(TAG, ">>>>>>>>这个回看视频需要订购.....");
    }

    /**
     * 网络异常或者authorizeResult==NULL
     */
    @Override
    public void onPlayChannelUrlError() {

    }

    @Override
    public void onItemSelect(String viewType, int position) {
        if (viewType.equals(TVOD_MENU_ITEM_TAG)) {
            String dateKey = mOriginalList.get(position);
            if (mCurrentSelectDateKey.equals(dateKey)) {
                SuperLog.debug(TAG, "currentSelectDateKey==dateKey");
                mPageIndexValue.setText("");
                return;
            }
            mRlNoProgramData.setVisibility(View.GONE);
            mLeftPageProgramListAdapter.clearAll();
            mRightPageProgramListAdapter.clearAll();
            mLeftPageProgramListAdapter.setRecordFocusPosition(0);
            mRightPageProgramListAdapter.setRecordFocusPosition(0);
            mProgramCount.setText("");
            mPageIndexValue.setText("");
            mLine.setVisibility(View.INVISIBLE);
            mCurrentSelectDateKey = dateKey;
            mCurrentPageIndex = 0;
            presenter.queryPlaybillList(this, mCurrentSelectDateKey, mChannelIds);
        }else {
            selectShortestPath(position,viewType);
            mLastItemSelectTime=System.currentTimeMillis();
        }
    }

    private void selectShortestPath(int position,String viewType){
        if(viewType.equals(LEFT_PAGE_TAG)){
            mLeftPageProgramListAdapter.setRecordFocusPosition(position);
            int rightPageSize=mRightPageProgramListAdapter.getItemCount()-1;
            if(position>rightPageSize){
                mRightPageProgramListAdapter.setRecordFocusPosition(rightPageSize);
            }else{
                mRightPageProgramListAdapter.setRecordFocusPosition(position);
            }
        }else if(viewType.equals(RIGHT_PAGE_TAG)){
            mRightPageProgramListAdapter.setRecordFocusPosition(position);
            mLeftPageProgramListAdapter.setRecordFocusPosition(position);
        }
        if(!viewType.equals(TVOD_MENU_ITEM_TAG)){
            //获取当前页码
            int currentPageIndex=mCurrentPageIndex*26;
            if(viewType.equals(LEFT_PAGE_TAG)){
                currentPageIndex+=(mLeftPageProgramListAdapter.getRecordFocusPosition()+1);
            }else{
                currentPageIndex+=13+(mRightPageProgramListAdapter.getRecordFocusPosition()+1);
            }
            //设置当前显示的页面索引位置
            mPageIndexValue.setText(String.valueOf(currentPageIndex+"/"+mTotalPageSize));
        }
    }

    @Override
    public void onItemClickListener(String viewType, int position) {
        String channelID= null;
        String mediaID = null;
        if (viewType.equals(LEFT_PAGE_TAG)) {
            Program program=mLeftPageProgramListAdapter.getItemPosition(position);
            channelID=program.getChannelID();
            mTVODPlaybillID=program.getId();
            //设置节目单名字,用于跳转到点播播放器中携带的电影名字
            mVideoName=program.getName();
            Schedule schedule= LiveUtils.findScheduleById(channelID);
            if(null!=schedule){
                mediaID=schedule.getMediaID();
            }
        } else if (viewType.equals(RIGHT_PAGE_TAG)) {
            Program program=mRightPageProgramListAdapter.getItemPosition(position);
            channelID=program.getChannelID();
            mTVODPlaybillID=program.getId();
            //设置节目单名字,用于跳转到点播播放器中携带的电影名字
            mVideoName=program.getName();
            Schedule schedule= LiveUtils.findScheduleById(channelID);
            if(null!=schedule){
                mediaID=schedule.getMediaID();
            }
        }
        if (!viewType.equals(TVOD_MENU_ITEM_TAG) && !TextUtils.isEmpty(mediaID)) {
            //回看鉴权request
            PlayChannelRequest playChannelRequest=new PlayChannelRequest();
            playChannelRequest.setChannelID(channelID);
            playChannelRequest.setPlaybillID(mTVODPlaybillID);
            playChannelRequest.setMediaID(mediaID);
            playChannelRequest.setBusinessType(V6Constant.ChannelURLType.CUTV);
            playChannelRequest.setURLFormat("1");
            presenter.playChannel(this,playChannelRequest);
        }
    }

    @Override
    public boolean onItemkeyListener(int keyCode, KeyEvent event, int position, String viewType) {
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            Log.i(TAG, "onItemkeyListener: onkeydonw");
            mLastDownTime=System.currentTimeMillis();
            if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
                if(viewType.equals(LEFT_PAGE_TAG)){
                    //左侧栏目获取焦点
                    requestListItemViewFocus(mDateListAdapter.getRecordFocusPosition(),TVOD_MENU_ITEM_TAG);
                    return true;
                }else if(viewType.equals(RIGHT_PAGE_TAG)){
                    //第一个节目单获取焦点
                    requestListItemViewFocus(mLeftPageProgramListAdapter.getRecordFocusPosition(),LEFT_PAGE_TAG);
                    return true;
                }
            }else  if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
                if(viewType.equals(LEFT_PAGE_TAG)){
                    //第二个节目单列表获取焦点
                    if(mRightPageProgramListAdapter.getItemCount()>0){
                        requestListItemViewFocus(mRightPageProgramListAdapter.getRecordFocusPosition(),RIGHT_PAGE_TAG);
                    }
                    return true;
                }else if(viewType.equals(TVOD_MENU_ITEM_TAG)){
                    if(mLeftPageProgramListAdapter.getItemCount()>0){
                        //设置左侧栏目字体高亮
                        mDateListAdapter.setIsSelect(true);
                        //第一个节目单列表获取焦点
                        requestListItemViewFocus(mLeftPageProgramListAdapter.getRecordFocusPosition(),LEFT_PAGE_TAG);
                        return true;
                    }
                    return true;
                }
            }
        }else if(event.getAction()==KeyEvent.ACTION_UP){
            if(System.currentTimeMillis()-mLastDownTime>150 ){
                mLastDownTime=0;
            }
            if (viewType.equals(LEFT_PAGE_TAG)|| viewType.equals(RIGHT_PAGE_TAG)) {
                if(keyCode==KeyEvent.KEYCODE_DPAD_UP && position==0){
                    loadProgramData(true,viewType);
                    return true;
                }else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
                    if ((viewType.equals(LEFT_PAGE_TAG) && position == mLeftPageProgramListAdapter.getItemCount() - 1)
                            || (viewType.equals(RIGHT_PAGE_TAG) && position == mRightPageProgramListAdapter.getItemCount() - 1)) {
                        loadProgramData(false,viewType);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}