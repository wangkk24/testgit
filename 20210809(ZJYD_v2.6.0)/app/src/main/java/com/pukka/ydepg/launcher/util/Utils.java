package com.pukka.ydepg.launcher.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pukka.ydepg.R;
import com.pukka.ydepg.common.extview.ImageViewExt;
import com.pukka.ydepg.common.extview.TextViewExt;
import com.pukka.ydepg.common.http.v6bean.v6node.ExtraData;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.ZJVRoute;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.uiutil.DensityUtil;
import com.pukka.ydepg.common.utils.uiutil.EpgToast;
import com.pukka.ydepg.launcher.Constant;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.bean.BodyContentBean;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在此写用途
 *
 * @author yangjunyan
 * @FileName: com.pukka.ydepg.launcher.util.Utils.java
 * @date: 2017-12-18 14:11
 * @version: V1.0 描述当前版本功能
 */
public class Utils {

    private static final String TAG = "Utils";
    private static final String TITLE_COLOR = "titleColor";
    public static final String FOCUS_COLOR = "focusColor";

    /**
     * Format rate null "" 0->0.0
     * 9.35->9.4
     * 7->7.0
     * 10->10
     *
     * @param rate
     * @return
     */
    public static String formatRate(String rate) {
        if (TextUtils.isEmpty(rate)) {
            return "0.0";
        } else {
            try {
                Float floatRate = Float.parseFloat(rate);
                if (floatRate >= 10) {
                    return "10";
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    return  decimalFormat.format(floatRate);
                }
            } catch (NumberFormatException e) {
                return "0.0";
            }
        }
    }

    //获取控件标题颜色
    public static void setGroupTitleColor(ExtraData extraData, TextView textView) {
        String titleColor = null;
        if (null != extraData && !TextUtils.isEmpty(extraData.getTitleColor())){
            titleColor = extraData.getTitleColor();
        }else if (null != LauncherService.getInstance().getLauncher()
            && null != LauncherService.getInstance().getLauncher().getExtraData()
            && !TextUtils.isEmpty(LauncherService.getInstance().getLauncher().getExtraData().get(TITLE_COLOR))){
            titleColor = LauncherService.getInstance().getLauncher().getExtraData().get(TITLE_COLOR);
        }
        if (!TextUtils.isEmpty(titleColor)){
            String[] split = titleColor.split("\\|");
            textView.setTextColor(Color.parseColor(split[0].replaceAll(" ", "")));
        }
    }

    //未落焦字体颜色色值
    public static String getNoFocusColorString(String focusColor,String defaultColor) {
        if (!TextUtils.isEmpty(focusColor) && focusColor.split("\\|").length > 0){
            String[] split = focusColor.split("\\|");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0].substring(0,1));
            sb.append("B3");
            sb.append(split[0].substring(1,split[0].length()));
            return sb.toString();
        }else if (!TextUtils.isEmpty(defaultColor)){
            StringBuilder sb = new StringBuilder();
            sb.append(defaultColor.substring(0,1));
            sb.append("B3");
            sb.append(defaultColor.substring(1,defaultColor.length()));
            return sb.toString();
        }
        return null;
    }

    //设置字体颜色
    public static void setTextColorUseFocusColor(String focusColor,TextView textView,String defaultColor) {
        if (!TextUtils.isEmpty(focusColor)){
            String[] split = focusColor.split("\\|");
            textView.setTextColor(Color.parseColor(split[0].replaceAll(" ", "")));
        }else if (!TextUtils.isEmpty(defaultColor)){
            textView.setTextColor(Color.parseColor(defaultColor));
        }
    }

    //获取导航栏字体落焦颜色
    public static void setTextBgColorUseFocusColor(String focusColor,TextView textView) {
        if (!TextUtils.isEmpty(focusColor)){
            String[] split = focusColor.split("\\|");
            textView.setTextColor(Color.parseColor(split[0].replaceAll(" ", "")));
        }
    }

    //获取导航栏字体落焦颜色
    public static int getNavTitleColor(int position) {
        List<Navigate> navigateList = LauncherService.getInstance().getLauncher().getNavigateList();
        if (position > -1 && position < navigateList.size()){
            Navigate navigate = navigateList.get(position);
            //#ffffff 文字颜色、#000000背景颜色
            Map<String, String> extraData = navigate.getExtraData();
            String focusColor;
            if (null != extraData && !TextUtils.isEmpty(extraData.get(FOCUS_COLOR))){
                focusColor = extraData.get(FOCUS_COLOR);
            }else{
                focusColor = "#ffffff|#1675ff";
            }
            if (!TextUtils.isEmpty(focusColor) && focusColor.split("\\|").length > 0){
                String[] split = focusColor.split("\\|");
                return Color.parseColor(split[0].replaceAll(" ", ""));
            }else {
                return -1;
            }
        }
        return -1;
    }
    //导航栏不落焦颜色设置80 未落焦时默认为该颜色+50%不透明度
    public static int getNavTitleColorNormal(int position) {
        List<Navigate> navigateList = LauncherService.getInstance().getLauncher().getNavigateList();
        if (position > -1 && position < navigateList.size()){
            Navigate navigate = navigateList.get(position);
            //#ffffff 文字颜色、#000000背景颜色
            Map<String, String> extraData = navigate.getExtraData();
            String focusColor;
            if (null != extraData && !TextUtils.isEmpty(extraData.get(FOCUS_COLOR))){
                focusColor = extraData.get(FOCUS_COLOR);
            }else{
                focusColor = "#ffffff|#1675ff";
            }
            if (!TextUtils.isEmpty(focusColor) && focusColor.split("\\|").length > 0){
                String[] split = focusColor.split("\\|");
                StringBuilder sb = new StringBuilder();
                sb.append(split[0].substring(0,1));
                sb.append("80");
                sb.append(split[0].substring(1,split[0].length()));
                return Color.parseColor(sb.toString().replaceAll(" ", ""));
            }else {
                return -1;
            }
        }
        return -1;
    }

    @SuppressLint("WrongConstant")
    public static Drawable getShapeForColor(Context context,String focusColor,int radiusFloat) {

        String startColor = "#1675ff",endColor = "#1675ff";
        GradientDrawable gd;

        if (!TextUtils.isEmpty(focusColor) && focusColor.split("\\|").length > 1){
            String[] split = focusColor.split("\\|");
            startColor = split[1];
            endColor = split[1];
        }

        SuperLog.info2SD(MainActivity.class.getSimpleName(),"startColor="+startColor+";endColor="+endColor);

        if (!TextUtils.isEmpty(startColor) && !TextUtils.isEmpty(endColor)){
            int colors[] = { Color.parseColor(startColor.replaceAll(" ", "")) , Color.parseColor(endColor.replaceAll(" ", "")) };//分别为开始颜色，中间夜色，结束颜色
            gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        }else{
            int colors[] = { Color.parseColor("#1675ff") , Color.parseColor("#1675ff") };//分别为开始颜色，中间夜色，结束颜色
            gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        }

        //int fillColor = Color.parseColor("#971417");//内部填充颜色
        //int radius = DensityUtil.dip2px(context, R.dimen.details_poster_height);
        //int radius = DensityUtil.dip2px(context, radiusFloat);

        gd.setCornerRadius(radiusFloat);

        return gd;
    }

    public static void setTopMenuBg(Context context, TextView tv, Drawable drawableSelect){
        StateListDrawable drawable =new StateListDrawable();
        //选中
        drawable.addState(new int[]{android.R.attr.state_selected},drawableSelect);
        //未选中
        if (tv.getId() == R.id.tv_change_user_btn){
            drawable.addState(new int[]{-android.R.attr.state_selected},context.getResources().getDrawable(R.drawable.shape_epg_change_no_focus));
        }else {
            drawable.addState(new int[]{-android.R.attr.state_selected},context.getResources().getDrawable(R.drawable.shape_epg_top_scroll_ads_no_focus));
        }
        tv.setBackground(drawable);;
    }

    public static void setTopMenuBg(Context context, RelativeLayout rl, Drawable drawableSelect){
        StateListDrawable drawable =new StateListDrawable();
        //选中
        drawable.addState(new int[]{android.R.attr.state_selected},drawableSelect);
        //未选中
        if (rl.getId() == R.id.tv_user_title_bg){
            drawable.addState(new int[]{-android.R.attr.state_selected},context.getResources().getDrawable(R.drawable.shape_epg_change_no_focus));
        }else{
            drawable.addState(new int[]{-android.R.attr.state_selected},context.getResources().getDrawable(R.drawable.shape_epg_top_scroll_ads_no_focus));
        }
        rl.setBackground(drawable);;
    }

    @SuppressLint("WrongConstant")
    public static Drawable getNavTitleShape(Context context,int position) {

        String startColor = "#1675ff",endColor = "#1675ff";
        GradientDrawable gd;

        List<Navigate> navigateList = LauncherService.getInstance().getLauncher().getNavigateList();
        if (position > -1 && position < navigateList.size()){
            Navigate navigate = navigateList.get(position);
            //#ffffff 文字颜色、#000000背景颜色
            //#ffffff 文字颜色、#000000背景颜色
            Map<String, String> extraData = navigate.getExtraData();
            String focusColor = null;
            if (null != extraData && !TextUtils.isEmpty(extraData.get(FOCUS_COLOR))){
                focusColor = extraData.get(FOCUS_COLOR);
            }
            if (!TextUtils.isEmpty(focusColor) && focusColor.split("\\|").length > 1){
                String[] split = focusColor.split("\\|");
                startColor = split[1];
                endColor = split[1];
            }
        }

        SuperLog.info2SD(MainActivity.class.getSimpleName(),"startColor="+startColor+";endColor="+endColor);

        if (!TextUtils.isEmpty(startColor) && !TextUtils.isEmpty(endColor)){
            int colors[] = { Color.parseColor(startColor.replaceAll(" ", "")) , Color.parseColor(endColor.replaceAll(" ", "")) };//分别为开始颜色，中间夜色，结束颜色
            gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        }else{
            int colors[] = { Color.parseColor("#1675ff") , Color.parseColor("#1675ff") };//分别为开始颜色，中间夜色，结束颜色
            gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        }

        //int fillColor = Color.parseColor("#971417");//内部填充颜色
        int radius = DensityUtil.dip2px(context, R.dimen.details_poster_height);

        gd.setCornerRadius(radius);

        return gd;
    }

    public static void setNavTitleBg(Context context, TextView tv, Drawable drawableSelect){
        StateListDrawable drawable =new StateListDrawable();
        //选中
        drawable.addState(new int[]{android.R.attr.state_selected},drawableSelect);
        //未选中
        drawable.addState(new int[]{-android.R.attr.state_selected},context.getResources().getDrawable(R.color.transparent));
        tv.setBackground(drawable);;
    }

    //判断爱看页面Vod的Custom Field中是否含有H5Url或者ContentType,含有的话表示跳转到EPG界面、apk
    public static boolean hasH5UrlOrContentTypeForCustomField(List<NamedParameter> customField){
        if (null != customField && customField.size() > 0){
            for (NamedParameter namedParameter : customField){
                if (namedParameter.getKey().equalsIgnoreCase(ZJVRoute.H5_URL) || namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE)){
                    SuperLog.debug(ZJVRoute.class.getSimpleName(),"profile vod custom fields have H5Url or ContentType, go to EPG child page");
                    return true;
                }
            }
        }
        SuperLog.debug(ZJVRoute.class.getSimpleName(),"profile vod custom fields don't have H5Url and ContentType, don't go to EPG child page");
        return false;
    }

    public static String getActionUrl(List<NamedParameter> customField) {
        StringBuilder sb = new StringBuilder();
        String contentType = null;
        String H5Url = null;
        int type = -1;//0:代表跳转h5网页；1：代表跳转非h5

        SuperLog.info2SDDebug(ZJVRoute.class.getSimpleName(), "customField=  "+JsonParse.listToJsonString(customField));

        /**
         * 下面根据contentType判断是否跳转二级页面，需拼接，
         * 所以先for循环取Content Type，
         * */
        for (NamedParameter namedParameter : customField){
            if (namedParameter.getKey().equalsIgnoreCase(ZJVRoute.H5_URL)){
                type = 0;
                H5Url = namedParameter.getFistItemFromValue();
            }
            if (namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE)){
                if (type == -1){
                    type = 1;
                }
                contentType = namedParameter.getFistItemFromValue();
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE).append("=").append(contentType);
            }
        }

        //根据首页静态资源位跳转逻辑优先判断是否跳转H5,此处页优先判断H5
        if (type == 0){
            if (!TextUtils.isEmpty(H5Url)){
                SuperLog.debug(ZJVRoute.class.getSimpleName(),"profile vod go to H5，H5Url = " + H5Url);
                return H5Url;
            }else{
                SuperLog.info2SD(ZJVRoute.class.getSimpleName(),"profile vod go to H5，but H5Url = null,return");
                return "";
            }
        }

        if (type == 1){
            if (TextUtils.isEmpty(contentType)){
                SuperLog.info2SD(ZJVRoute.class.getSimpleName(),"profile vod go to EPG page，contentType = null,return");
                return "";
            }
        }

        for (NamedParameter namedParameter : customField){
            String key = namedParameter.getKey();
            String value = namedParameter.getFistItemFromValue();
            //判断key是否为空字符串
            if (TextUtils.isEmpty(key)){
                continue;
            }
            /*if (key.equals(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE)){
                contentType = value;
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE).append("=").append(contentType);
            }*/
            if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.SUB_CONTENT_TYPE)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.SUB_CONTENT_TYPE).append("=").append(value);
            }else if (ZJVRoute.ActionUrlKeyType.CONTENT_ID.equalsIgnoreCase(key)){
                if (ZJVRoute.LauncherElementContentType.PAGE.equalsIgnoreCase(contentType) &&!TextUtils.isEmpty(value) && !value.contains("@")){
                    //处理跳往二级桌面，contentId为二级桌面的桌面id，需拼接首页桌面id和version，才能正确下载二级桌面json,(100363_103051@1614744750189)
                    sb.append("&")
                            .append(ZJVRoute.ActionUrlKeyType.CONTENT_ID)
                            .append("=")
                            .append(SharedPreferenceUtil.getInstance().getLauncherDeskTopIdForChild())
                            .append("_")
                            .append(value)
                            .append("@")
                            .append(SharedPreferenceUtil.getInstance().getLauncherVersionForChild());
                }else{
                    sb.append("&").append(ZJVRoute.ActionUrlKeyType.CONTENT_ID).append("=").append(value);
                }
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.SUBJECT_ID)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.SUBJECT_ID).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.FOCUS_COTENTID)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.FOCUS_COTENTID).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.APP_PKG)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.APP_PKG).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.APP_CLASS)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.APP_CLASS).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.VERSION)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.VERSION).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.APK_URL)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.APK_URL).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.ACTION)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.ACTION).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CONTENT_CODE)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.CONTENT_CODE).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CLASS_NAME)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.CLASS_NAME).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.FOURK_CONTENT_ID)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.FOURK_CONTENT_ID).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.VODID)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.VODID).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.TYPE)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.TYPE).append("=").append(value);
            }else if (key.equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.KEY)){
                sb.append("&").append(ZJVRoute.ActionUrlKeyType.KEY).append("=").append(value);
            }

        }
        SuperLog.info2SD(ZJVRoute.class.getSimpleName(),"profile vod go to EPG page，actionUrl = " + sb.toString());
        return sb.toString();
    }

    //不属于ActionUrlKeyType中的参数全部当成扩展参数
    public static Map<String, String> getExtraData(List<NamedParameter> customField) {

        StringBuilder sb = new StringBuilder();
        Map<String, String> extraData = new HashMap<>();
        for (NamedParameter namedParameter : customField){
            if (!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CONTENT_TYPE)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.SUB_CONTENT_TYPE)
                &&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CONTENT_ID)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.SUBJECT_ID)
                &&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.FOCUS_COTENTID)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.APP_PKG)
                &&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.APP_CLASS)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.VERSION)
                &&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.APK_URL)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.ACTION)
                &&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CONTENT_CODE)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CLASS_NAME)
                &&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.CHANNEL_MINI_TYPE)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.FOURK_CONTENT_ID)
                &&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.VODID)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.TYPE)
                &&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.ActionUrlKeyType.KEY)&&!namedParameter.getKey().equalsIgnoreCase(ZJVRoute.H5_URL)){
                extraData.put(namedParameter.getKey(),namedParameter.getFistItemFromValue());
                sb.append("&")
                        .append(namedParameter.getKey())
                        .append(":")
                        .append(namedParameter.getFistItemFromValue());
            }
        }
        SuperLog.info2SD(ZJVRoute.class.getSimpleName(),"profile vod go to EPG page，actionUrl = " + sb.toString());
        return extraData;
    }

}