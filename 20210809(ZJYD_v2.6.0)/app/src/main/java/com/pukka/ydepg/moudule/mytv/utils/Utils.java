package com.pukka.ydepg.moudule.mytv.utils;

import java.util.Formatter;
import java.util.Locale;

public class Utils {

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public Utils() {
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }


    /**
     * 把毫秒转换成：01:20:30这里形式
     *
     * @param timeMs
     * @return
     */
    public String stringForTime(int timeMs) {
        int seconds = timeMs % 60;
        int minutes = (timeMs / 60) % 60;
        int hours = timeMs / 3600;
        mFormatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

}