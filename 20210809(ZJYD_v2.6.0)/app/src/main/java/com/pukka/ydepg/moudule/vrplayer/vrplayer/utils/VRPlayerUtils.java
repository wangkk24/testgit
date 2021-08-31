package com.pukka.ydepg.moudule.vrplayer.vrplayer.utils;

import android.content.Context;
import android.content.Intent;

import com.pukka.ydepg.moudule.vrplayer.vrplayer.MainActivity;


/**
 * 作者：panjw on 2021/6/1 11:10
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class VRPlayerUtils {
    public void initVRPlayer(Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
