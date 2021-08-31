package com.pukka.ydepg.moudule.children.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.pukka.ydepg.R;
import com.pukka.ydepg.moudule.base.BaseActivity;
import com.pukka.ydepg.moudule.children.view.ParentSetCenterManagerView;

import butterknife.ButterKnife;

/**
 * Created by Eason on 01-Apr-19.
 * 家长设置中心
 */

public class ParentSetCenterActivity extends BaseActivity {

    private ParentSetCenterManagerView parentSetCenterManagerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_parent_set_center, null);
        setContentView(view);
        ButterKnife.bind(this);
        parentSetCenterManagerView = (ParentSetCenterManagerView) view.findViewById(R.id.parent_set_center_managerview);
        parentSetCenterManagerView.setContext(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
            return parentSetCenterManagerView.onKeyDown();
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        parentSetCenterManagerView.onDestroy();
    }
}
