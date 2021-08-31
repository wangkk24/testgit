package com.pukka.ydepg.launcher.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.report.ubd.UBDTool;
import com.pukka.ydepg.common.utils.uiutil.Strings;
import com.pukka.ydepg.launcher.bean.node.AppInfo;
import com.pukka.ydepg.launcher.ui.adapter.AllAppInfoAdapter;
import com.pukka.ydepg.launcher.util.APPUtils;
import com.pukka.ydepg.moudule.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllAppInfoActivity extends BaseActivity implements AllAppInfoAdapter.OnFocusChangedCallBack{

    private List<AppInfo> appInfoList;

    private AllAppInfoAdapter mAdapter;

    @BindView(R.id.all_app)
    RecyclerView mAllApp;

    @BindView(R.id.all_app_title)
    TextView mAllAppTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_app);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView(){
        mAllApp.setLayoutManager(new GridLayoutManager(this, 6));
        mAdapter = new AllAppInfoAdapter(this, this);
        mAllApp.setAdapter(mAdapter);
    }

    private void initData(){
        appInfoList = APPUtils.getAllAppInfo(this);
        mAllAppTitle.setText(String.format(Strings.getInstance().getString(R.string.my_app_title), appInfoList.size()));
        AppInfo appInfoCache = null;
        if(appInfoList.size() > 0){
            for(AppInfo appInfo:appInfoList){
                if(APPUtils.APPSTORE_PACKAGE_NAME.equals(appInfo.getPackageName())){
                    appInfoCache = appInfo;
                    break;
                }
            }
            if(appInfoCache != null) {
                appInfoList.remove(appInfoCache);
                appInfoList.add(appInfoCache);
            }
        }
        mAdapter.setmData(appInfoList);
    }

    @Override
    public void onFocusChanged(int position) {
        mAllApp.smoothScrollToPosition(position);
    }
}
