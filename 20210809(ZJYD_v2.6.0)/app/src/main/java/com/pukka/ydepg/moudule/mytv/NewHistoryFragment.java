package com.pukka.ydepg.moudule.mytv;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.BrowseFrameLayout;

import com.pukka.ydepg.R;
import com.pukka.ydepg.event.HistoryListRequestFocusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 新的观看记录界面
 *
 * @version V2.1
 * @FileName: com.pukka.ydepg.moudule.mytv.NewHistoryFragment.java
 * @author:weicy
 * @date: 2019-10-30 11:32
 */

public class NewHistoryFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {

    private static final String TAG = "NewHistoryFragment";

    //整体布局
    BrowseFrameLayout history_fragment;

    //宽带电视
    TextView mTvTextView;
    View mTvTextViewBg;

    //咪咕爱看
    TextView mPhoneTextView;
    View mPhoneTextViewBg;

    //tv播放记录展示Fragment
    private NewHistoryListFragment mTvHistoryListFragment;

    //手机播放记录展示Fragment
    private NewHistoryListFragment mPhoneHistoryListFragment;

    //当前展示的fragment
    private Fragment mFragment;

    private FrameLayout frameLayout;

    /*当前选中
      1为宽带电视选中，其余为咪咕爱看选中
     */
    private String nowSelecet = "1";


    /*************************************生命周期********************************************/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mytv_history_list_new,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*************************************自定义方法********************************************/

    private void initData() {

    }

    private void initView(View view) {

        history_fragment = (BrowseFrameLayout) view.findViewById(R.id.history_fragment);

        mTvTextView = (TextView) view.findViewById(R.id.history_list_tv);
        mTvTextViewBg = view.findViewById(R.id.history_list_tv_bg);
        mPhoneTextView = (TextView) view.findViewById(R.id.history_list_phone);
        mPhoneTextViewBg = view.findViewById(R.id.history_list_phone_bg);
        frameLayout = (FrameLayout) view.findViewById(R.id.history_List_content);

        mTvHistoryListFragment = NewHistoryListFragment.newInstance(NewHistoryListFragment.VIEW_TYPE_TV);
        mPhoneHistoryListFragment = NewHistoryListFragment.newInstance(NewHistoryListFragment.VIEW_TYPE_PHONE);


        mTvTextView.setOnFocusChangeListener(this);
        mPhoneTextView.setOnFocusChangeListener(this);
        //首次进来切换至电视显示
        switchContent(mTvHistoryListFragment);
        mTvTextView.setSelected(true);
        mPhoneTextView.setSelected(false);

        history_fragment.setOnFocusSearchListener(new BrowseFrameLayout.OnFocusSearchListener() {
            @Override
            public View onFocusSearch(View focused, int direction) {
                if (focused == mTvTextView && direction == View.FOCUS_DOWN){
                    if (mTvHistoryListFragment.mDeleteBtn.getVisibility() == View.VISIBLE){
                        if (null != mTvHistoryListFragment.getFirstItem()){
                            return mTvHistoryListFragment.getFirstItem();
                        }
                    }
                    return mTvTextView;

                }else if (focused == mPhoneTextView && direction == View.FOCUS_DOWN){
                    if (mPhoneHistoryListFragment.mDeleteBtn.getVisibility() == View.VISIBLE){
                        if (null != mPhoneHistoryListFragment.getFirstItem()){
                            return mPhoneHistoryListFragment.getFirstItem();
                        }
                    }
                    return mPhoneTextView;

                }else if (focused == mTvTextView && direction == View.FOCUS_RIGHT){
                    return mPhoneTextView;
                }else if (focused == mPhoneTextView && direction == View.FOCUS_LEFT){
                    return mTvTextView;
                }else if (focused == mPhoneTextView && direction == View.FOCUS_RIGHT){
                    return mPhoneTextView;
                }else{
                    return null;
                }
            }
        });
    }

    //切换显示的fragment
    protected void switchContent(Fragment to) {
        if (mFragment != to) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.history_List_content,to).commitAllowingStateLoss();
            mFragment = to;
        }
    }

    //退出编辑模式
    public void quitEdit(){
        if (mFragment == mTvHistoryListFragment){
            mTvHistoryListFragment.quieEdit();
        }else{
            mPhoneHistoryListFragment.quieEdit();
        }
    }


    /*************************************按键回调********************************************/

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        Fragment to = null;
        switch (v.getId()) {
            case R.id.history_list_tv:
            {
                to = mTvHistoryListFragment;

                if (hasFocus){
                    mTvTextView.setSelected(true);
                    mTvTextViewBg.setSelected(true);
                    mPhoneTextView.setSelected(false);
                    mPhoneTextViewBg.setSelected(false);
                    nowSelecet = "1";
                }else {
                    mTvTextViewBg.setSelected(false);
                }
                break;
            }
            case R.id.history_list_phone:
                {
                    to = mPhoneHistoryListFragment;

                    if (hasFocus){
                        mPhoneTextView.setSelected(true);
                        mPhoneTextViewBg.setSelected(true);
                        mTvTextView.setSelected(false);
                        mTvTextViewBg.setSelected(false);
                        nowSelecet = "0";

                    }else {
                        mPhoneTextViewBg.setSelected(false);
                    }
                    break;
                }
        }
        if (null != to){
            switchContent(to);
        }
        if (to == mTvHistoryListFragment){
            mTvHistoryListFragment.scrollToTop();
        }else if (to == mPhoneHistoryListFragment){
            mPhoneHistoryListFragment.scrollToTop();
        }

    }

    /*************************************EventBus方法********************************************/

    @Subscribe
    public void onEvent(HistoryListRequestFocusEvent event) {
        if (event.getViewNeedFocus().equals(HistoryListRequestFocusEvent.REQUEST_FOCUS_FRAGMENT) ){
            if (mFragment == mTvHistoryListFragment){
                mTvTextView.requestFocus();
                mTvHistoryListFragment.scrollToTop();
            }else if (mFragment == mPhoneHistoryListFragment){
                mPhoneTextView.requestFocus();
                mPhoneHistoryListFragment.scrollToTop();
            }
        }
    }

    /*************************************get方法********************************************/

    public TextView getmTvTextView() {
        return mTvTextView;
    }

    public Fragment getmFragment() {
        return mFragment;
    }

    public TextView getNowSelectedText(){
        if (mTvTextView.isSelected()){
            return mTvTextView;
        }else if (mPhoneTextView.isSelected()){
            return mPhoneTextView;
        }
        return mTvTextView;
    }
}
