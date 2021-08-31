package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

public class UpgradeActivity extends Activity{
    private static final String TAG = UpgradeActivity.class.getSimpleName();
    private String loginRouteUrl = "http://101.91.202.229:8082/EDS/V3/LoginRoute";
    private static final int INSTALL_PACKAGES_REQUESTCODE = 2000;
//    private String loginRouteUrl = "http://10.164.180.209:18080/VSP/V3/Login";
    String filePath;
    ProgressDialog progressDialog ;
    LinearLayout view ;
    TextView bool;
    Gson gson = new Gson();
    UpdateApk updateApk = new UpdateApk(this);

    private static final int UPDATE = 12345;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        view = new LinearLayout(this);
//        view.setOrientation(LinearLayout.VERTICAL);
//        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        bool = new TextView(this);
//        bool.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        linearLayout.addView(bool);
//        view.addView(linearLayout);
//        setContentView(view);

    }

}
