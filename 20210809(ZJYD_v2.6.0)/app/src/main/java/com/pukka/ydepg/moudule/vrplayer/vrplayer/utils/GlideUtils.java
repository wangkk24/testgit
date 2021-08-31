package com.pukka.ydepg.moudule.vrplayer.vrplayer.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;

import java.io.File;


public class GlideUtils {

    /**
     * @param context     当前Activity的上下文对象
     * @param path        加载的路径
     * @param imageView   需要加载的图片
     * @param placeholder 加载的占位图
     * @param errorImage  加载失败的图片
     */
    public static void showImageView(Context context, String path, @DrawableRes int errorImage, @DrawableRes int placeholder, ImageView imageView ) {
        Object obj;
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                obj = file;
            } else {
                obj = path;
            }
        } else {
            obj = "";
        }
        Glide.with(context).load(obj).error(errorImage).placeholder(placeholder).into(imageView);
    }
}
