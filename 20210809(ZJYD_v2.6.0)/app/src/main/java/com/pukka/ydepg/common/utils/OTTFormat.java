package com.pukka.ydepg.common.utils;

import android.text.TextUtils;

/**
 * Created by hasee on 2017/5/8.
 */

public final class OTTFormat {

    public static long convertLong(String value) {
        if(TextUtils.isEmpty(value)) {
            return 0;
        } else {
            try {
                return Long.valueOf(Long.parseLong(value));
            } catch (NumberFormatException var2) {
                return 0;
            }
        }
    }

    public static long convertSimpleLong(String value) {
        if(TextUtils.isEmpty(value)) {
            return 0L;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException var2) {
                return 0L;
            }
        }
    }

    public static int convertInt(String value) {
        if(TextUtils.isEmpty(value)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException var2) {
                return 0;
            }
        }
    }
}