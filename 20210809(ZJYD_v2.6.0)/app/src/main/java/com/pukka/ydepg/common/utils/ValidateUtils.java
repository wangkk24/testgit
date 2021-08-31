package com.pukka.ydepg.common.utils;

import java.util.regex.Pattern;

/**
 * Created by wgy on 2017/1/6.
 */
public class ValidateUtils {
    public static boolean isNumbers(String value){
        return Pattern.compile("[0-9]+").matcher(value).find();
    }
}