package com.pukka.ydepg.moudule.mytv;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.BrowseFrameLayout;

import com.google.gson.reflect.TypeToken;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.http.v6bean.v6response.QueryFavoriteResponse;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.event.HistoryListRequestFocusEvent;
import com.pukka.ydepg.moudule.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;

public class NewMyMovieActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener, View.OnKeyListener{

    private static final String TAG = "NewMyMovieActivity";

    //第一次进收藏和观看记录，显示提示信息guide
    ImageView mIvChildFirstGuide;

    BrowseFrameLayout frameLayout;

    /**
     * 历史
     */
    TextView mHistory;
    View mHistroyBg;

    /**
     * 收藏
     */
    TextView mCollection;
    Fragment mFragment;

    View mMenu;
    View mLine;

    private boolean mHasJumpOut;
    NewHistoryFragment mHistoryListFragment;
    CollectionListFragment mCollectionListFragment;

    private boolean isFirstMyMoview = SharedPreferenceUtil.getBoolData(SharedPreferenceUtil.Key.FIRST_TIME_USE_COLLECTANDHISTORY.toString(),true);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_moive_new);

        initView();
        initData();
    }

    @Override
    public void onBackPressed() {

        if (mFragment == mHistoryListFragment){

            NewHistoryListFragment fragment = (NewHistoryListFragment) mHistoryListFragment.getmFragment();

            if (fragment.isEditing()){
                mHistoryListFragment.quitEdit();
                return;
            }
        }

        super.onBackPressed();
    }

    /**
     * 1 历史选中 其余收藏选中
     */
    private String viewType = "1";
    private void initData() {

        Intent intent = getIntent();
        if (null != intent) {
            viewType = intent.getStringExtra("id");
            if (!isFirstMyMoview || !SharedPreferenceUtil.getInstance().getIsChildrenEpg()) {
                if (TextUtils.equals(viewType, "1")) {
                    mHistory.requestFocus();
                    mHistory.setSelected(true);
                    mHistroyBg.setSelected(true);
                } else {
                    mCollection.requestFocus();
                    mCollection.setSelected(true);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHasJumpOut = false;
    }



    private void initView() {
        mHistoryListFragment =  new NewHistoryFragment();
        mCollectionListFragment = new CollectionListFragment();
        frameLayout = (BrowseFrameLayout) findViewById(R.id.activity_mytv);
        mLine = findViewById(R.id.line);
        mIvChildFirstGuide = (ImageView) findViewById(R.id.iv_child_first_guide);
        mHistory = (TextView) findViewById(R.id.activity_mytv_movie_history);
        mHistroyBg = findViewById(R.id.activity_mytv_movie_history_bg);
        mCollection = (TextView) findViewById(R.id.activity_mytv_movie_collection);
        mMenu = findViewById(R.id.activity_my_movie_menu);
        mHistory.setOnFocusChangeListener(this);
        mHistory.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mCollection.setOnFocusChangeListener(this);
        mCollection.setOnKeyListener(this);
        mHistory.setOnKeyListener(this);
        mHistory.setOnClickListener(this);

        //观看记录向右，落焦到列表第一项
        frameLayout.setOnFocusSearchListener(new BrowseFrameLayout.OnFocusSearchListener() {
            @Override
            public View onFocusSearch(View focused, int direction) {
                if (focused == mHistory && direction == View.FOCUS_RIGHT){
                    NewHistoryListFragment fragment = (NewHistoryListFragment) mHistoryListFragment.getmFragment();
                    if (fragment.hasData()){
                        View FirstItemView = fragment.getFirstItem();
                        if (null != FirstItemView){
                            return FirstItemView;
                        }
                    }else{
                        return mHistoryListFragment.getNowSelectedText();
                    }
                    return mHistory;
                }
                return null;
            }
        });

        showFirstGuide();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (mFragment == mHistoryListFragment){

            NewHistoryListFragment fragment = (NewHistoryListFragment) mHistoryListFragment.getmFragment();

            if (null != fragment && !fragment.showFocus){
                return;
            }
        }

        Fragment to = null;
        switch (v.getId()) {
            case R.id.activity_mytv_movie_history:

                if (hasFocus){
                    to = mHistoryListFragment;
                    mHistory.setSelected(true);
                    mHistroyBg.setSelected(true);
                    mCollection.setSelected(false);

                }else{
                    mHistroyBg.setSelected(false);
                }

                break;
            case R.id.activity_mytv_movie_collection:
                to = mCollectionListFragment;
                mCollection.setSelected(true);
                mHistory.setSelected(false);
                break;
        }
        if (null != to){
            switchContent(to);
        }
        if (to == mHistoryListFragment){
            //列表滚动到最上方
            NewHistoryListFragment fragment = (NewHistoryListFragment) mHistoryListFragment.getmFragment();
            if (null != fragment){
                fragment.scrollToTop();
            }
        }
    }

    /**
     *      * Modify the contents of the display will not reload
     */
    protected void switchContent(Fragment to) {
        if (mFragment != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_my_movie_content,to).commitAllowingStateLoss();
            mFragment = to;
        }
    }
    @Override
    public void onClick(View v) {
        jumpActivity(v);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_RIGHT://向右
                    SuperLog.error("jamie", "－－－－－向右－－－－－");

                    if (v == mCollection){
                        if (!mHasJumpOut) {
                            jumpActivity(v);
                            mHasJumpOut = true;
                        }
                    }

                    if (v == mHistory){
                        return false;
                    }

                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (v == mHistory){
                        return true;
                    }
                    break;
                default:
                    break;
            }
        return false;
    }

    /**
     * 跳转历史记录页面 或者收藏页面
     * @param view
     */
    private void jumpActivity(View view) {
        switch (view.getId()) {
            case R.id.activity_mytv_movie_history:
                break;
            case R.id.activity_mytv_movie_collection:
                Intent collection = new Intent(NewMyMovieActivity.this, CollectionActivity.class);
                QueryFavoriteResponse response = mCollectionListFragment.getFragmentData();
                if (null != response) {
                    String object2String = JsonParse.object2String(response, new TypeToken<QueryFavoriteResponse>() {
                    }.getType());
                    if (!TextUtils.isEmpty(object2String)) {
                        collection.putExtra("collection", object2String);
                    }
                }
                startActivity(collection);
                //设置切换动画，从右边进入，左边退出
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
        }
    }

    //观看记录获得焦点
    @Subscribe
    public void onEvent(HistoryListRequestFocusEvent event) {
        if (event.getViewNeedFocus().equals(HistoryListRequestFocusEvent.REQUEST_FOCUS_ACTIVITY) ){
            Log.i(TAG, "onEvent: 收到事件");
            mHistory.requestFocus();
        }
    }

    //用户在儿童版第一次进入收藏观看历史展示提示信息页
    private void showFirstGuide(){
        if(isFirstMyMoview && SharedPreferenceUtil.getInstance().getIsChildrenEpg()){
            mIvChildFirstGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    if (TextUtils.equals(viewType, "1")) {
                        mHistory.requestFocus();
                        mHistory.setSelected(true);
                    } else {
                        mCollection.requestFocus();
                        mCollection.setSelected(true);
                    }
                }
            });
            mIvChildFirstGuide.setVisibility(View.VISIBLE);
            mIvChildFirstGuide.requestFocus();
            SharedPreferenceUtil.getInstance().putBoolean(SharedPreferenceUtil.Key.FIRST_TIME_USE_COLLECTANDHISTORY.toString(),false);
        } else {
            mIvChildFirstGuide.setVisibility(View.GONE);
        }
    }
}
