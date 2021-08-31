package com.pukka.ydepg.moudule.multviewforstb.multiview;

import android.content.Context;
import android.content.Intent;

import com.pukka.ydepg.moudule.multviewforstb.multiview.module.main.MainActivity;


/**
 * 作者：panjw on 2021/5/27 16:04
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class multViewutils {
    public void intents(Context context) {
        Intent intent = new Intent(TVApplication.getContext(), MainActivity.class);
        context.startActivity(intent);
    }
}
