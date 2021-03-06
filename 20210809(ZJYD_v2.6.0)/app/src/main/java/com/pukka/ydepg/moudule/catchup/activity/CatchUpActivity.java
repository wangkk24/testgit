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
 * ๅ็้ฆ้กต
 */
public class CatchUpActivity extends BaseActivity implements FocusInterceptor, TVODContract.View,
        BaseAdapter.OnItemListener, CatchUpContentAdapter.OnHasFocusChangePageNumListener {

    private static final String TAG = CatchUpActivity.class.getName();
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "channel_name";
    public static final String CHANNEL_DATE = "channel_date";
    private static final long DELAY_INVIDATE = 40;

    private VerticalGridView mCatagoryList;

    //ๆ?็ฎName
    private TextView mTvClumnName;

    //้ข้ๆปไธชๆฐ
    private TextView mTvTotalNum;

    //ๅบ้จๆพ็คบ็้กตๆฐ
    private TextView mTvPageNum;

    //ๆ?ๆฐๆฎ
    private RelativeLayout mNoContentRela;

    //vodๅ่กจ
    private FocusVerticalGridView mContentList;

    //้ข้้้ๅจ
    private CatchUpContentAdapter mContentAdapter;

    //้ข้ๆปๆฐ
    private int mChannePagelNum = 0;

    //ๅฝๅๆๅจ้กตๆฐ
    private int mPagelNum = 1;

    //ๆ?็ฎ้ไธญ็index
    private int mSelectCloumnIndex = -1;

    //ๆ?็ฎ้้ๅจ
    private TVODProgramMenuItemListAdapter mCloumnAdapter;

    //ๆฐๆฎ๏ผHashMap,key=ๆ?็ฎid,value=่ฏฅๆ?็ฎไธ็้ข้้ๅ
    private HashMap<String, List<ChannelDetail>> mDateHashMap = new HashMap<>();
    //ๆ?็ฎ้ๅ
    private List<String> mCloumnNamelList;
    //ๆ?็ฎ้ๅ
    private List<Subject> mSubjectList;

    //่ฏญ้ณ็ๅฌ
    private Scene mFocusScene;

    //่ฏญ้ณๅ่ฐ
    private Feedback mFeedback;

    //่ฏญ้ณๅฝไปค
    private List<String> keys = new ArrayList<>();

    //่ฏญ้ณๅฝไปค
    private List<String> directives = new ArrayList<>();

    //่ฏญ้ณๅฝไปค
    private List<String> fuzzys = new ArrayList<>();

    //ๆๆ้ข้ๅ่กจ
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
            //็กฎไฟ็ฌฌไธๆฌก่ฟๅฅๅ็ไผ่ฐ็จๆฅๅฃ
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
        //ๅฏ่ฝๆชๆณจๅ๏ผ้ๆพไผๆฅ้
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
                                        mFeedback.feedback(allChannelDetails.get(i).getName().replace("ๅ?", "+"), Feedback.EXECUTION);
                                        switchmCatagoryList(allChannelDetails.get(i));
                                        break;
                                    }
                                }
                            }
                            if (("key" + mCloumnNamelList.size()).equals(command) || ("key" + (mCloumnNamelList.size() + 1)).equals(command)) {
                                mFeedback.feedback("ๆญๆพ่ฎฐๅฝ", Feedback.EXECUTION);
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
            fuzzys.add("ๆญๆพ่ฎฐๅฝ");
            keys.add("key" + (mCloumnNamelList.size() + 1));
            directives.add("history1");
            fuzzys.add("่ง็่ฎฐๅฝ");
            keys.add("key" + (mCloumnNamelList.size() + 2));
            directives.add("quit");
            fuzzys.add("้ๅบ");


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
                fuzzys.add(allChannelDetails.get(i).getName().replace("+", "ๅ?"));
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

        //่ฎพ็ฝฎๅๅฎนRecycle View Adapter
        mContentList.setFocusable(false);
        mContentList.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ITEM);
        mContentList.setNumColumns(4);

        //่ฎพ็ฝฎๆ?็ฎRecycle View Adapter
        mCloumnAdapter = new TVODProgramMenuItemListAdapter(this, "MENU_ITEM_TAG", new ArrayList<>());
        mCloumnAdapter.setOnItemListener(this);
        mCatagoryList.setAdapter(mCloumnAdapter);
        //mCatagoryList.addItemDecoration(new ProgramListItemDecoration());
        requestFocus(mCatagoryList.getLayoutManager().findViewByPosition(0));

        //่ฎพ็ฝฎๅๅฎนRecycle View Adapter
        mContentAdapter = new CatchUpContentAdapter(this, new ArrayList<>());
        mContentAdapter.setmOnHasFocusChangePageNumListener(this);
        mContentAdapter.setOnItemKeyListener((keyCode, event, position, viewType) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                //่ทณๅพ้ข้ๅ็่็ฎๅๅ่กจ็้ข
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

        //ๅค็็ฆ็นๅจๆ?็ฎๆไธๅๆไธๆถ็ๆไฝ
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && mCatagoryList.hasFocus() && mSelectCloumnIndex == 0) {
            mCatagoryList.smoothScrollToPosition(mCloumnNamelList.size() - 1);
            mCatagoryList.setSelectedPosition(mCloumnNamelList.size() - 1);
            return true;
        }

        //ๅค็๏ผ็ฆ็นๅจๆ?็ฎไธ๏ผๆๅณ้ฎ๏ผ่ฎฉๆ?็ฎๅญไฝ้ซไบฎ
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

            //ๆฒกๆๆฐๆฎๆถไธ็งปๅจ็ฆ็น
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
            //่งฃๅณๆๅบ้จ้ข้็ฆ็นไธไธ็งป้ฎ้ข
            if (mContentList.hasFocus()) {
                int focusPosition = mContentAdapter.getRecordFocusPosition();
                if (mContentAdapter.getItemCount() > 4
                        && mContentAdapter.getItemCount() % 4 != 0
                        && focusPosition + 5 > mContentAdapter.getItemCount()) {
                    requestFocus(mContentList.getLayoutManager().findViewByPosition(mContentAdapter.getItemCount() - 1));
                }
            } else if (mCatagoryList.hasFocus() && mSelectCloumnIndex == mCloumnNamelList.size() - 1) {
                //่งฃๅณๆ?็ฎๅ่กจๅพช็ฏ่ฝ็ฆ
                mCatagoryList.smoothScrollToPosition(0);
                mCatagoryList.setSelectedPosition(0);
                return true;
            }
        }

        //็ฆ็นๅจ้ข้ๅๅฎนไธ๏ผ็นๅป่ฟๅๆ้ฎ๏ผ็ฆ็น่ฝๅฐๆ?็ฎไธ
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
     * ๅผบๅถ่ฎฉView่ทๅ็ฆ็น
     */
    private void requestFocus(View view) {
        if (null != view) {
            view.setFocusable(true);
            view.postDelayed(view::requestFocus, DELAY_INVIDATE);
        }
    }


    //่ฎพ็ฝฎDataๅฐAdapter
    @SuppressLint("SetTextI18n")
    private void setData() {
        mCloumnNamelList = TVODDataUtil.getInstance().getListSubjectName();
        mSubjectList = TVODDataUtil.getInstance().getListSubject();
        mDateHashMap = TVODDataUtil.getInstance().getMapSubjectName2listChannel();
        mCloumnAdapter.bindData(mCloumnNamelList);
        //setDataOnSelect(0);
    }

    //่ฎพ็ฝฎ้ข้Adapterๅๅฎน
    @SuppressLint("SetTextI18n")
    private void setContentAdapterData(List<ChannelDetail> list) {
        //่ฎพ็ฝฎๅทฆไธ่งๆป้ข้ๆฐ็ฎ
        mTvTotalNum.setText(String.format(getString(R.string.tvod_channel_all_num), list.size() + ""));
        //ๆป้กตๆฐ
        mChannePagelNum = getPageNum(list.size());
        //่ฎพ็ฝฎๅบ้จๆพ็คบๅฝๅๆๅจ็้กต็?
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
        //่ฎพ็ฝฎๅทฆไธ่งๆป้ข้ๆฐ็ฎ
        mTvTotalNum.setText(String.format(getString(R.string.tvod_channel_all_num), list.size() + ""));
        //ๆป้กตๆฐ
        mChannePagelNum = getPageNum(list.size());
        //่ฎพ็ฝฎๅบ้จๆพ็คบๅฝๅๆๅจ็้กต็?
        if (mChannePagelNum > 0) {
            mTvPageNum.setText(mPagelNum + "/" + mChannePagelNum);
        } else {
            mTvPageNum.setText("");
        }

        //้ๆฐๅๅงๅmContentAdapter,ไฝฟ่กจๆ?ผๆปๅฐๆไธๆน
        mContentAdapter = null;
        mContentAdapter = new CatchUpContentAdapter(this, new ArrayList<>());
        mContentAdapter.setmOnHasFocusChangePageNumListener(this);
        mContentAdapter.setOnItemKeyListener((keyCode, event, position, viewType) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                //่ทณๅพ้ข้ๅ็่็ฎๅๅ่กจ็้ข
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


    //่ฟๅ้ข้้กตๆฐ
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

    //ๆ?นๆฎๆ?็ฎIDๆฅ่ฏข้ข้
    @Override
    public void onQueryChannelStcPropsBySubjectSuccess(List<ChannelDetail> channelDetails, int position) {
        setContentViewIsVisibility(channelDetails);
        mDateHashMap.put(mCloumnNamelList.get(position), channelDetails);
        TVODDataUtil.getInstance().setMapSubjectName2listChannel(mCloumnNamelList.get(position), channelDetails);
        setContentAdapterDataWithRefresh(channelDetails);
    }

    //ๆ?นๆฎๆ?็ฎIDๆฅ่ฏข้ข้ๅคฑ่ดฅ
    @Override
    public void onQueryChannelStcPropsBySubjectFailed() {
        setContentViewIsVisibility(null);
    }

    //ๆ?็ฎ้ไธญไบไปถ
    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelect(String viewType, int position) {
        setDataOnSelect(position);
    }

    //ๆ?็ฎ็นๅปไบไปถ
    @Override
    public void onItemClickListener(String viewType, int position) {
    }

    //่ฎพ็ฝฎๅบ้จๅฝๅๆๅจ็้กตๆฐๆพ็คบ
    @SuppressLint("SetTextI18n")
    @Override
    public void onItemFocusChangeNum(int position) {
        mPagelNum = getPageNum(position + 1);
        mTvPageNum.setText(mPagelNum + "/" + mChannePagelNum);
    }

    //็งปๅจ็ฆ็น ๅค็็งปๅจไบไปถ๏ผ้ฆๆฌก้ป่ฎคpositionไธบ0
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