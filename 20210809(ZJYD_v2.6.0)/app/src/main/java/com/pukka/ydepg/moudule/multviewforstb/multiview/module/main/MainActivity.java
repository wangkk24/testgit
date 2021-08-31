package com.pukka.ydepg.moudule.multviewforstb.multiview.module.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.BuildConfig;
import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.multviewforstb.multiview.PlayListAdapter;
import com.pukka.ydepg.moudule.multviewforstb.multiview.TVApplication;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.UpdateApk;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.config.PickFileActivity;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.bean.AllConfig;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.view.RecyclerItemDecoration;
import com.pukka.ydepg.moudule.multviewforstb.multiview.util.FileUtils;
import com.pukka.ydepg.moudule.multviewforstb.multiview.util.ScreenUtil;
import com.pukka.ydepg.moudule.multviewforstb.multiview.util.UriUtil;

import java.io.File;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    /**
     * 备份的外置存储url列表文件
     */
    private String externalFilePath = "";

    /**
     * 获取文件回调
     */
    private final int FLAG_TAG = 0x8907;

    private RecyclerView rvPlayList;
    private PlayListAdapter adapter;
    /**
     * 版本号显示
     */
    private TextView playerVersion;
    /**
     * 屏幕信息显示
     */
    private TextView screenSize;
    /**
     * 主页背景view
     */
    private ImageView mainBG;
    /**
     * 配置信息
     */
    private AllConfig allConfig;
    /**
     * 更新APK实体
     */
    private UpdateApk updateApk;
    private boolean hasCheckUpgrade = false;
    /**
     * 隐藏功能，快速连续点击3次,进入config界面
     */
    private int configNum;

    /**
     * 屏幕高度
     */
    protected int phoneHeight;
    /**
     * 屏幕宽度
     */
    protected int phoneWidth;

    private Handler configHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (configNum == 3) {
               /* final String[] items = {getString(R.string.setting_choice_json), getString(R.string.setting_choice_loglevel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.setting_choice)).setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            go2PickFile();
                        } else {*/
                            showLogChoice();
                       /* }
                        dialog.dismiss();
                    }
                });
                builder.create().show();*/
            }
            configNum = 0;
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DebugLog.info(TAG, "[onCreate] BUIL_TIME=" + BuildConfig.BUIL_TIME);
        externalFilePath = Environment.getExternalStorageDirectory() + File.separator + "multiView.json";
        initView();
        hasCheckUpgrade = false;
        initPlayerVersion();
    }

    private void initPlayerVersion() {
//        String version = HuaweiPlayerFactory.getVersion();
//        if (TextUtils.isEmpty(version)) {
        String  version = "playerVersion:none";
//        }

        int logLevel = TVApplication.getInstance().getSp().getInt(TVApplication.LOG_LEVEL, DebugLog.INFO);

        String logDes = "";
        if (logLevel == DebugLog.DEBUG) {
            logDes += "Debug";
        } else if (logLevel == DebugLog.INFO) {
            logDes += "Info";
        } else if (logLevel == DebugLog.ERROR) {
            logDes += "Error";
        } else {
            logDes += "OFF";
        }

        playerVersion.setText(version + " " + BuildConfig.BUIL_TIME + " " + logDes + " " + BuildConfig.APP_DESC);
        DebugLog.info(TAG, "[initPlayerVersion] " + version + " AppBuildTime:" + BuildConfig.BUIL_TIME + " " + logDes + " " + BuildConfig.APP_DESC);
    }

    private void initView() {
        playerVersion = findViewById(R.id.player_version);
        screenSize = findViewById(R.id.screen_size);
        rvPlayList = findViewById(R.id.rv_play_list);
        mainBG = findViewById(R.id.cl_main_bg);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPlayList.setLayoutManager(manager);
        rvPlayList.addItemDecoration(new RecyclerItemDecoration());
//        rvPlayList.setHasFixedSize(true);
//        rvPlayList.addItemDecoration(new PlayListItemDecoration());
        adapter = new PlayListAdapter(this);
        rvPlayList.setAdapter(adapter);
        rvPlayList.addOnScrollListener(adapter.getOnScrollListener());
        initConfig();
    }

    private void initConfig() {
        long time1 = System.currentTimeMillis();
        File file = new File(externalFilePath);
        String urlJson = null;
        if (file.exists()) {
            urlJson = FileUtils.getFromSDCard(externalFilePath);
            long time2 = System.currentTimeMillis();
            DebugLog.info(TAG, "[initConfig] from File time:"+(time2-time1)+"ms");
            DebugLog.info(TAG, "[initConfig] from File:"+urlJson);
        }

        if (TextUtils.isEmpty(urlJson)) {
            urlJson = FileUtils.getFromAssets(this, "5GVideoUrl.json");
            DebugLog.info(TAG, "[initConfig] from Assets:"+urlJson);
        }

        if (!TextUtils.isEmpty(urlJson)) {
            allConfig = FileUtils.getAllConfig(urlJson);
            long time3 = System.currentTimeMillis();
            DebugLog.info(TAG, "[initConfig] getAllConfig:"+(time3-time1)+"ms");
            if(allConfig == null){
                showFinishDialog(MainActivity.this,getString(R.string.json_set_error_tip));
                return;
            }
            adapter.setData(allConfig);
            adapter.setPhoneWidth(phoneWidth);
            adapter.notifyDataSetChanged();
            refreshBg();
            screenSize.setVisibility(allConfig.isShowPlayVersion() ? View.VISIBLE : View.GONE);
            playerVersion.setVisibility(allConfig.isShowPlayVersion() ? View.VISIBLE : View.GONE);
        } else {
            showFinishDialog(MainActivity.this,getString(R.string.update_success_tip));
        }
    }

    public void showFinishDialog(Context context,String msg) {
        AlertDialog.Builder finishDialog = new AlertDialog.Builder(context);
        finishDialog.setCancelable(false);
        finishDialog.setMessage(msg);
        finishDialog.setNegativeButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        finishDialog.show();
    }

    /**
     * 刷新背景图片
     */
    private void refreshBg() {
        DebugLog.info(TAG,"[refreshBg] phoneWidth:"+phoneWidth);
        if(null == allConfig || null == mainBG){
            DebugLog.error(TAG,"[refreshBg] null == allConfig || null == mainBG");
            return;
        }
        if(phoneWidth == 0){
            return;
        }
        Glide.with(this)
                .load(!TextUtils.isEmpty(allConfig.getBackground()) ? allConfig.getBackground() : R.drawable.background)
                .thumbnail(0.8f)
                .override(phoneWidth, phoneHeight)
                .error(R.drawable.background)
                .into(mainBG);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && null != screenSize) {
            screenSize.setText(getString(R.string.current_screen) + ScreenUtil.getScreenInfo(this));
        }

        if (hasFocus && phoneWidth == 0) {
            int screenHeight = ScreenUtil.getScreenHeigt(this);
            int screenWidth = ScreenUtil.getScreenWidth(this);
            phoneWidth = Math.max(screenHeight, screenWidth);
            phoneHeight = Math.min(screenHeight, screenWidth);
            if(null != adapter){
                adapter.setPhoneWidth(phoneWidth);
                adapter.notifyDataSetChanged();
            }
            refreshBg();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasCheckUpgrade) {
            if (allConfig != null && !TextUtils.isEmpty(allConfig.getLoginRouteForUpgrade())) {
                updateApk = new UpdateApk(this);
                updateApk.startLogin(allConfig.getLoginRouteForUpgrade());
                hasCheckUpgrade = true;
//                TVApplication.getApplication().setHasCheckUpdate(true);
            } else {
                DebugLog.debug(TAG, getString(R.string.update_tip_nointface));
            }
        } else {
            DebugLog.debug(TAG, "Upgrade check has been done");
        }
    }

    private void go2PickFile() {
        DebugLog.info(TAG, "[go2PickFile]");
//        try {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("file/*");//设置类型
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(intent, FLAG_TAG);
//        } catch (ActivityNotFoundException exception) {
        startActivityForResult(new Intent(MainActivity.this, PickFileActivity.class), FLAG_TAG);
//        }
    }

    private void showLogChoice() {
        DebugLog.info(TAG, "[showLogChoice]");
        final String[] items = {"Debug", "Info", "Error", "CloseLog"};
        int checkedItem = 0;
        if (DebugLog.logcatEnable(DebugLog.DEBUG)) {
        } else if (DebugLog.logcatEnable(DebugLog.INFO)) {
            checkedItem = 1;
        } else if (DebugLog.logcatEnable(DebugLog.ERROR)) {
            checkedItem = 2;
        } else {
            checkedItem = 3;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.setting_choice_loglevel_title)).setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DebugLog.error(TAG, "[showLogChoice] = " + which);

                int debugLogLevel = DebugLog.DEBUG;
                switch (which) {
                    case 0:
                        debugLogLevel = DebugLog.DEBUG;
                        break;
                    case 1:
                        debugLogLevel = DebugLog.INFO;
                        break;
                    case 2:
                        debugLogLevel = DebugLog.ERROR;
                        break;
                    case 3:
                        debugLogLevel = DebugLog.OFF;
                        break;
                }

                DebugLog.initLogcatLevel(debugLogLevel);
                //DebugLog.initLogFileLevel(debugLogLevel, TVApplication.LOG_FILE_PATH);

                SharedPreferences.Editor editor = TVApplication.getInstance().getSp().edit();//获取编辑器
                editor.putInt(TVApplication.LOG_LEVEL, debugLogLevel);
                editor.commit();

                initPlayerVersion();

                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != FLAG_TAG || data == null) {
            return;
        }

        String filePath = data.getStringExtra("filePath");

        if (TextUtils.isEmpty(filePath)) {
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            if (uri == null) {
                return;
            }
            if (!uri.toString().endsWith("json")) {
                Toast.makeText(MainActivity.this, getString(R.string.pick_file_tip_jsonfile), Toast.LENGTH_SHORT).show();
                return;
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                filePath = uri.getPath();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                filePath = UriUtil.getPath(this, uri);
            } else {//4.4以下下系统调用方法
                filePath = UriUtil.getRealPathFromURI(MainActivity.this, uri);
            }
        }

        DebugLog.debug(TAG, "getRealPathFromUri:" + filePath);
        if (TextUtils.isEmpty(filePath)) {
            Toast.makeText(MainActivity.this, getString(R.string.pick_file_tip_filepath_fail), Toast.LENGTH_SHORT).show();
            return;
        }
        String readString = "";
        //使用apk内置url
        if ("assets".equals(filePath)) {
            readString = FileUtils.getFromAssets(this, "6GVideoUrl.json");
            DebugLog.info(TAG, "[initConfig] from Assets");
            new File(externalFilePath).delete();
        } else {
            readString = FileUtils.getFromSDCard(filePath);
        }

        DebugLog.debug(TAG, "readJson:" + readString);

        AllConfig allConfig = FileUtils.getAllConfig(readString);
        if (allConfig == null) {
            Toast.makeText(MainActivity.this, getString(R.string.pick_file_tip_file_fail), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!"assets".equals(filePath)) {
            FileUtils.copyFile(filePath, externalFilePath);
        }
        adapter.setData(allConfig);
        adapter.notifyDataSetChanged();
        //scrollToDefaultPosPosition();
        Toast.makeText(MainActivity.this, getString(R.string.pick_file_tip_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                configNum++;
                configHandler.removeMessages(0);
                configHandler.sendEmptyMessageDelayed(0, 400);
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugLog.info(TAG,"[onDestroy]");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DebugLog.info(TAG,"[onBackPressed]");
        //调用系统API结束进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * RecyclerView 滑动到初始位置
     */
    private void scrollToDefaultPosPosition(){
        if(allConfig == null){
            return;
        }
        if(allConfig.getAllVideoConfig() == null){
            return;
        }

        if(allConfig.getAllVideoConfig().isEmpty()){
            return;
        }
        int defaultPos = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % allConfig.getAllVideoConfig().size() + 1;
        rvPlayList.scrollToPosition(defaultPos);
        LinearLayoutManager mLayoutManager =
                (LinearLayoutManager) rvPlayList.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(defaultPos, 0);
    }
}
