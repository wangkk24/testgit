package com.pukka.ydepg.common.utils;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final String TAG = DateUtil.class.getSimpleName();

    //日期字符串格式转换
    public static String formatDate(String strDate, String formatstyle) throws Exception {
        Date cdate = ConverToDate(strDate);
        SimpleDateFormat sdf = new SimpleDateFormat(formatstyle);
        return sdf.format(cdate);
    }

    //把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 获取某月的最后一天
     *
     * @throws
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month  1~12
     * @param:@return
     * @return:String
     */
    public static long getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份,0是1月 1是2月，以此类推，此处用下一个月第一天然后减一天求上个月最后一天

        //设置日历中月份的最大天数
        if (month != 2) {
            //获取某月最大天数
            cal.set(Calendar.MONTH, month - 1);
            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, lastDay+1);
            cal.set(Calendar.HOUR,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
        } else {
            //2月份使用上边的逻辑不正确，只能单独处理
            //获取上个月第一天，然后-1天为当月最后一天
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DATE, -1);
        }

        long timeInMillis = cal.getTimeInMillis();
        return timeInMillis;
    }

    /**
     * 获取某年月的第一天的UNIX时间戳
     *
     * @throws
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String 指定年月的第一天时刻00:00:00的UNIX时间戳，单位毫秒
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, 1,0,0,0);
        calendar.set(Calendar.MILLISECOND,0);
        return String.valueOf(calendar.getTimeInMillis());
    }

    /**
     * 获取前后天日期字符串,day>0是后day天,day<0是前day天
     *
     * @param day
     * @return
     */
    public static String getBeoreAfterDateValue(int day) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + day);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            SuperLog.error(TAG,e);
        }
        if (null != endDate){
            return dft.format(endDate);
        }else{
            return null;
        }
    }

    /**
     * 返回MM-dd的时间格式
     * 转换时带上年份，之后把年份截掉，以便区分闰年
     */
    public static String getMothAndDayValue(int day){
        SimpleDateFormat dft = new SimpleDateFormat("yyyy#MM-dd", Locale.getDefault());
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + day);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            SuperLog.error(TAG,e);
        }
        if (null != endDate){
            String str = dft.format(endDate);
            String tempStr = str.substring(str.indexOf("#")+ 1);
            return tempStr;
        }else{
            return null;
        }
    }

    public static String getChineseMothAndDayValue(int day){
        SimpleDateFormat dft = new SimpleDateFormat("y年M月d号", Locale.getDefault());
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + day);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            SuperLog.error(TAG,e);
        }
        if (null != endDate){
            String str = dft.format(endDate);
            String tempStr = str.substring(str.indexOf("年")+ 1);
            return tempStr;
        }else{
            return null;
        }
    }

    public static String getVoiceMothAndDayValue(int day,String patten){
        SimpleDateFormat dft = new SimpleDateFormat(patten, Locale.getDefault());
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + day);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            SuperLog.error(TAG,e);
        }
        if (null != endDate){
            return dft.format(endDate);
        }else{
            return null;
        }
    }
}

