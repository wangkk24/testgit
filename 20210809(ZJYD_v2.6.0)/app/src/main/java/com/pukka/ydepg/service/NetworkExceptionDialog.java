package com.pukka.ydepg.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.R;
import com.pukka.ydepg.common.CommonUtil;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;


/**
 * Created by ld on 2017/11/20.
 */

public class NetworkExceptionDialog extends Activity implements View.OnClickListener {

    private int data = 11;
    private TextView commText;
    private Button cancelButton;
    private Button settingButton;
    private Button retryButton;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (data <= 0) {
                    sendEmptyMessage(2);
                }
                String text = NetworkExceptionDialog.this.getResources().getString(R.string.network_error);
                text = String.format(text, data + "");
                commText.setText(text);
                data--;
                sendEmptyMessageDelayed(0, 1000);
            } else if (msg.what == 2) {
                if (hasMessages(0)) {
                    removeMessages(0);
                }
                showResult();
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    if (activeNetwork.isConnected()) {
                        if (mHandler.hasMessages(0)) {
                            mHandler.removeMessages(0);
                        }
                        NetworkExceptionDialog.this.finish();
                    }
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_exception);
        commText = (TextView) findViewById(R.id.common_content);
        cancelButton = (Button) findViewById(R.id.common_button);
        cancelButton.setOnClickListener(this);
        settingButton = (Button) findViewById(R.id.setting_button);
        settingButton.setOnClickListener(this);
        retryButton = (Button) findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);
        mHandler.sendEmptyMessage(0);
        this.registerReceiver(mBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void showResult() {
        commText.setText(getResources().getString(R.string.network_error2));
        settingButton.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);
        data = 11;
    }

    private void showData() {
        settingButton.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.common_button) {
            mHandler.sendEmptyMessage(2);
        } else if (v.getId() == R.id.setting_button) {
            CommonUtil.startSettingActivity(this);
        } else if (v.getId() == R.id.retry_button) {
            showData();
        } else {
            SuperLog.error("NetworkException","Unknown branch in NetworkExceptionDialog !");
        }
    }

    @Override
    public void onBackPressed() {
        if (mHandler.hasMessages(0)) {
            mHandler.removeMessages(0);
        }
        mHandler.sendEmptyMessage(2);
    }

    public static void show(Context context){
    	if(OTTApplication.getContext().isNeedShowNetworkExceptionDialog()){
			Intent intent = new Intent(context, NetworkExceptionDialog.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
}
