package com.pukka.ydepg.moudule.multviewforstb.multiview;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.GlideModule;

public class PictureGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

//    @Override
//    public void registerComponents(Context context, Glide glide) {
//
//    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
