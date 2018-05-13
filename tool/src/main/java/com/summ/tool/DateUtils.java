package com.summ.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Project Name：Common
 * Package Name：com.summ.base.tools
 * @author:summ.Chen
 * Create：2017/8/28
 * Email： summ_summ@163.com
 * Version：1.0.0
 * <p>
 * Describe：时间的工具类
 */

public class DateUtils {


    /**
     * 默认格式：精确到秒
     */
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化到天
     */
    public static final String DAY_FORMAT = "yyyy-MM-dd";

    /**
     * 格式到毫秒
     */
    public static final String MILLIS_FORMAT = "yyyy-MM-dd HH:mm:ss:SSSS";

    /**
     * des：Date格式转为字符串
     *
     * @param date 时间
     * @return 格式化后时间字符串
     */
    public static String date2Str(Date date) {
        return date2Str(date, null);
    }

    /**
     * des：Date格式转为字符串
     *
     * @param time   时间
     * @param format yyyy-MM-dd HH:mm:ss
     * @return 格式化后时间字符串
     */
    public static String date2Str(long time, String format) {
        if (time == 0) {
            return null;
        }

        if (EmptyUtils.isEmpty(format)) {
            format = DEFAULT_FORMAT;
        }

        Date date = new Date(time);

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(date);
        return s;
    }


    /**
     * des：Date格式转为字符串
     *
     * @param date   时间
     * @param format yyyy-MM-dd HH:mm:ss
     * @return 格式化后时间字符串
     */
    public static String date2Str(Date date, String format) {
        if (date == null) {
            return null;
        }

        if (EmptyUtils.isEmpty(format)) {
            format = DEFAULT_FORMAT;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(date);
        return s;
    }


    /**
     * 字符串转换成日期
     *
     * @param str 字符串日期
     * @return date
     */
    public static Date strToDate(String str, String formatStr) {

        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前时间戳
     *
     * @return 毫秒数
     */
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 判断时间是否今天
     *
     * @param time 需要判断的时间
     * @return true: 今天   false:不是今天
     */
    public boolean isToday(long time) {
        return isSameDate(System.currentTimeMillis(), time);
    }

    /**
     * 判断时间是否同一天
     *
     * @param preDate  需要比较第一个时间
     * @param nextDate 需要比较第二个时间
     * @return true: 同一天   false:不是同一天
     */
    public boolean isSameDate(long preDate, long nextDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(preDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(nextDate);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

}
