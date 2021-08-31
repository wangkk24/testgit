/*
 *Copyright (C) 2017 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.util;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: PlayUtil
 * @Package com.pukka.ydepg.util
 * @date 2017/12/17 17:24
 */
public class PlayUtil {
    private static final String TAG = "PlayUtil";

    private static long currentNtpTime;

    private static long lastElapsedRealtime;

    /**
     * 直播时间格式
     */
    public static final String TIME_FORMAT_N = "yyyyMMddHHmmss'-'";
    /**
     * 直播时移添加参数
     */
    public static final String REQUEST_PARAMETER = "playseek=";


    private static String iptvUrl = "";

    private PlayUtil() {

    }

    public static String getIptvUrl() {
        return iptvUrl;
    }

    public static void setIptvUrl(String iptvUrl) {
        PlayUtil.iptvUrl = iptvUrl;
    }


    public static DataSource.Factory buildDataSourceFactory(Context context,
                                                            DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(context, bandwidthMeter,
                buildHttpDataSourceFactory(context, bandwidthMeter));
    }

    private static HttpDataSource.Factory buildHttpDataSourceFactory(Context context,
                                                                     DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(context, "OTTApplication"),
                bandwidthMeter);
    }

    /**
     * 根据uri动态选择MediaSource
     *
     * @param uri: Uri.parse(playUrl)
     */
    public static MediaSource buildMediaSource(DataSource.Factory dataSource, Uri uri) {
        @C.ContentType
        int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_HLS:
                //Faster HLS preparation
                return new HlsMediaSource.Factory(dataSource)
                        .setAllowChunklessPreparation(true)
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(dataSource).setExtractorsFactory(new DefaultExtractorsFactory())
                        .createMediaSource(uri);
            default:
                Log.e(TAG, "Unsupported type: " + type);
                return null;
        }
    }

    /**
     * ExoPlayer判断是不是离屏错误
     *
     * @param e exception
     */
    public static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 格式化时间,根据传入的毫秒值格式化成00:00:00的时间
     */
    public static String getStringForTime(StringBuilder builder, Formatter formatter, long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        builder.setLength(0);
        //%02d:如果第二位没有值则补0显示
        return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    public static class VideoType {

        private VideoType() {

        }

        public static final int VOD = 0;
        public static final int TV = 1;
        public static final int TSTV = 2;
        public static final int NPVR = 3;
        public static final int OTHER = 4;
        public static final int ADVERT=5;


    }

    public static String getFormatString(String formatStr, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.US);
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date);
    }

    public static long getNtpTime() {
        if (currentNtpTime == 0) {
            return System.currentTimeMillis();
        } else {
            long offsetTime = SystemClock.elapsedRealtime() - lastElapsedRealtime;
            return currentNtpTime + offsetTime;
        }
    }

    public static void setNtpTime(long ntpTime) {
        currentNtpTime = ntpTime;
    }

    public static void setLastElapsedRealtime(long lastElapsedRealtime) {
        PlayUtil.lastElapsedRealtime = lastElapsedRealtime;
    }
}