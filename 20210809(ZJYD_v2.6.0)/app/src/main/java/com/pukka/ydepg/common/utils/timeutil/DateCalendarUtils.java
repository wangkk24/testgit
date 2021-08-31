package com.pukka.ydepg.common.utils.timeutil;

import android.text.TextUtils;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public final class DateCalendarUtils {
    public static final String TAG = "DateCalendarUtils";

    private static long startDSTMs = 0;

    private static long endDSTMs = 0;

    public static final String PATTERN_A ="yyyyMMdd HH:mm:ss.SSS";

    public static final String PATTERN_B ="yyyyMMddHHmmss";

    public static final String PATTERN_C ="yyyyMMdd HH:mm:ss";

    //对应实际时间样例 20191025T145700000+0000
    public static final String PATTERN_VIP_VALID_TIME ="yyyyMMdd'T'HHmmssSSSZ";

    private static String timeZone = TimeZone.getDefault().getID();

    private static DateCalendarUtils gInstance = new DateCalendarUtils();

    private final SimpleDateFormat dateFormatCompact = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    private final SimpleDateFormat zoneFormatCompact = new SimpleDateFormat("yyyyMMddHHmmss Z", Locale.US);

    private DateCalendarUtils() {}

    public static boolean isTimeValid(String startTime, String endTime, String format) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat(format);
            Date startDate = format1.parse(startTime);
            Date endDate = format1.parse(endTime);
            Date nowDate = new Date();
            return (startDate.before(nowDate) && nowDate.before(endDate));
        } catch (ParseException e) {
            SuperLog.error(TAG,e);
            return false;
        }
    }

    /**
     * @param startTime 开始时间戳
     * @param endTime   结束时间戳
     * @return
     */
    public static boolean isTimeValid(long startTime, long endTime) {
        Date nowDate = new Date();
        Date start = new Date(startTime);
        Date end = new Date(endTime);
        if (nowDate.before(end) && nowDate.after(start)) {
            return true;
        }
        return false;
    }

    public static Date parseCompact(String dateString) {
        if (dateString == null) {
            return new Date();
        }
        synchronized (gInstance) {
            try {
                if (dateString.length() > 19) {
                    dateString = dateString.replace("UTC", "GMT");
                    return gInstance.zoneFormatCompact.parse(dateString);
                } else {
                    return gInstance.dateFormatCompact.parse(dateString);
                }
            } catch (ParseException e) {
                SuperLog.error(TAG, e);
            }
        }
        return new Date();
    }

    private static SimpleDateFormat getStandFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
        return format;
    }


    /**
     * UTC时间转换为本地时间。
     *
     * @param time yyyyMMddHHmmss。
     * @return 本地时间。
     */
    public static String convertUtcToLocalDate(String time) {
        String converttime = null;
        long resultTime = 0;
        if (time == null) {
            return null;
        }
        Date date = null;
        String id = getTimeZone();
        TimeZone timezone = TimeZone.getTimeZone(id);

        if (timezone.useDaylightTime()) {
            SimpleDateFormat format = getStandFormat();
            try {
                date = format.parse(time);
                resultTime = date.getTime() + timezone.getRawOffset();
                if (isInDST(date)) {
                    resultTime = resultTime + timezone.getDSTSavings();
                    converttime = format.format(resultTime);
                } else {
                    converttime = format.format(resultTime);
                }
            } catch (Exception e) {
                SuperLog.error(TAG, e);
            }
        } else {
            converttime = convertUtcToLocal(time, timezone);
        }
        return converttime;
    }

    private static String convertUtcToLocal(String srcTime, TimeZone timezone) {
        String convertTime;
        Date resultDate;
        long resultTime = 0;
        SimpleDateFormat format = getStandFormat();
        try {
            format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
            resultDate = format.parse(srcTime);
            resultTime = resultDate.getTime();
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            return null;
        }

        format.setTimeZone(timezone);
        convertTime = format.format(resultTime);

        return convertTime;
    }

    /**
     * 将毫秒数转为UTC时间，格式为yyyyMMddHHmmss
     *
     * @param timeMillis 毫秒数，注意为实际偏移量，不需要做时区的偏移修正
     * @return UTC时间
     */
    public static String convertToUTC(long timeMillis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
        return format.format(timeMillis);
    }

    public static String formatDate(long time, String format) {
        Calendar mCalendar = new GregorianCalendar();
        mCalendar.setTimeInMillis(time);
        mCalendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format,Locale.getDefault());
        return simpleDateFormat.format(mCalendar.getTime());
    }

    public static boolean isInDST(Date date) {
        boolean isInDst = false;
        if (date == null) {
            return false;
        }
        if ((date.getTime() >= startDSTMs) && (date.getTime() <= endDSTMs - 1)) {
            isInDst = true;
        }
        return isInDst;
    }

    public static String getTimeZone() {
        return timeZone;
    }

    private static SimpleDateFormat getSimpleFormat(String formatStr, Locale local) {
        SimpleDateFormat format;
        if (local == null) {
            format = new SimpleDateFormat(formatStr);
        } else {
            format = new SimpleDateFormat(formatStr, local);
        }
        format.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        return format;
    }

    public static String getDatetime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(time));
        SimpleDateFormat format = getSimpleFormat("d MMM, yyyy", Locale.US);
        String timeZone = getTimeZone();
        if (TextUtils.isEmpty(timeZone)) {
            timeZone = "Asia/Shanghai";
        }
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        String strTime = format.format(cal.getTime());
        return strTime;
    }

    /**
     * convert timeMillis to specified format
     *
     * @param time         timeMillis
     * @param formartValue format such as "yyyyMMdd"
     * @return date string
     */
    public static String formatDateTime(long time, String formartValue) {
        TimeZone timeZone = TimeZone.getDefault();
        SimpleDateFormat format = new SimpleDateFormat(formartValue, Locale.CHINA);
        format.setTimeZone(timeZone);
        String strTime = format.format(time);

        //To support daylight-saving time, when out of daylight saving one hour ago (note part
        // time zone transition adjustment time instead of 1 hour, used here timeZone
        // .getDSTSavings ()) time, add DST
        if (formartValue.contains("HH:mm") && timeZone.useDaylightTime() && timeZone.inDaylightTime
                (new Date(time)) && !timeZone.inDaylightTime(new Date(time + timeZone
                .getDSTSavings()))) {
            strTime = strTime + " DST";
        }
        return strTime;
    }

    /**
     * get TimeMillis of yyyyMMdd 00:00:00
     *
     * @param date yyyyMMdd
     * @return TimeMillis
     */
    public static long getStartTimeOfDay(String date) {
        String pattern = "yyyyMMdd";
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        format.setTimeZone(TimeZone.getDefault());
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            SuperLog.error(TAG, "Date is not the yyyyMMdd format. date: " + date);
            SuperLog.error(TAG, e);
        }

        return 0L;
    }

    /**
     * get TimeMillis of yyyyMMdd 23:59:59
     *
     * @param date yyyyMMdd
     * @return TimeMillis
     */
    public static long getEndTimeOfDay(String date) {
        long startTime = getStartTimeOfDay(date);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(startTime);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * 日期转时间戳
     *
     * @param timeString 日期
     * @return
     */
    public static long getTime(String timeString) {
        long time = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(timeString);
            time = date.getTime();
        } catch (ParseException e) {
            SuperLog.error(TAG,e);
        }
        return time;
    }

    /**
     * 时间戳转日期(UBD时间戳)
     */
    public static String  getTime(Date date,String pattern) {
        String time = "";
        if (null != date){
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            time = sdf.format(date);
        }
        return time;
    }
}