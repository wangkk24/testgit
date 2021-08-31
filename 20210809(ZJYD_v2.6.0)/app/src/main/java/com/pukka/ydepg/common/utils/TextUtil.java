package com.pukka.ydepg.common.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;

import java.lang.reflect.Field;

/**
 * TextUtil工具
 *
 * @author fuqiang
 * @version 1.0
 * @date 2017/12/25 下午5:24
 */
public class TextUtil {
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 检查手机号码是否合法
     */
    public static boolean isIllegalPhoneNumber(String str) {
        if (str.length() == 11 && str.matches("^1.*")) {
            return true;
        }
        return false;
    }

    /**
     * 获取EditText最大长度
     *
     * @param et
     * @return
     */
    public static int getEditTextMaxLength(EditText et) {
        int length = 0;
        try {
            InputFilter[] inputFilters = et.getFilters();
            for (InputFilter filter : inputFilters) {
                Class<?> c = filter.getClass();
                if (c.isAssignableFrom(InputFilter.LengthFilter.class)) {
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            SuperLog.error("getEditTextMaxLength",e);
        }
        return length;
    }

    /**
     * 设置EditText过滤规则
     *
     * @param editText
     * @param length
     */
    public static void setEditTextFilter(final EditText editText, final int length) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                       int dend) {
                if ("+".equals(source.toString())) {
                    return "0";
                }
                if (!ValidateUtils.isNumbers(source.toString())) {
                    //0-9之间的数字才可以输入
                    if (!TextUtils.isEmpty(source.toString())) {
                        EpgToast.showToast(editText.getContext(), editText.getContext().getString(R.string.input_phonenumber_range));
                    }
                    return "";
                } else if (source.length() + dest.length() > length) {
                    //输入的字符达到上限
                    EpgToast.showToast(editText.getContext(), editText.getContext().getString(R.string.input_charset_maxlimit));
                    return "";
                }
                return source;
            }
        };
        InputFilter lengthFilter = new InputFilter.LengthFilter(length);
        editText.setFilters(new InputFilter[]{filter, lengthFilter});
    }
    /**
     * 设置跑马灯
     * @param isMarqueen
     * @param textView
     */
    public static void setMarqueen(boolean isMarqueen,TextView... textView){
        for (TextView marqueenTextView:textView){
            if(null!=marqueenTextView){
                if(!isMarqueen){
                    marqueenTextView.setEllipsize(TextUtils.TruncateAt.END);
                }else{
                    marqueenTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    marqueenTextView.setMarqueeRepeatLimit(-1);
                }
                marqueenTextView.setSelected(isMarqueen);
            }
        }
    }

    /**
     * 设置跑马灯
     * @param isMarqueen
     * @param textView
     */
    public static void setNoSelectedMarqueen(boolean isMarqueen,TextView... textView){
        for (TextView marqueenTextView:textView){
            if(null!=marqueenTextView){
                if(!isMarqueen){
                    marqueenTextView.setEllipsize(TextUtils.TruncateAt.END);
                }else{
                    marqueenTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    marqueenTextView.setMarqueeRepeatLimit(-1);
                }

            }
        }
    }
}