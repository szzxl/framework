/**************************************************************************************** 
 Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.hbasesoft.framework.common.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年11月6日 <br>
 * @see com.hbasesoft.framework.core.utils <br>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    /** yyyyMMdd */
    public static final String DATE_FORMAT_8 = "yyyyMMdd";

    /** yyyy年MM月dd日 */
    public static final String DATE_FORMAT_11 = "yyyy年MM月dd日";

    /** yyyy-MM-dd */
    public static final String DATE_FORMAT_10 = "yyyy-MM-dd";

    /** yyyy/MM/dd */
    public static final String DATE_FORMAT_10_2 = "yyyy/MM/dd";

    /** yyyyMMddHHmmss */
    public static final String DATETIME_FORMAT_14 = "yyyyMMddHHmmss";

    /** yyyyMMddHHmmssSSS */
    public static final String DATETIME_FORMAT_17 = "yyyyMMddHHmmssSSS";

    /** yyyy-MM-dd HH:mm:ss */
    public static final String DATETIME_FORMAT_19 = "yyyy-MM-dd HH:mm:ss";

    /** yyyy-MM-dd HH:mm:ss */
    public static final String DATETIME_FORMAT_19_2 = "yyyy/MM/dd HH:mm:ss";

    /** yyyy年MM月dd日 HH时mm分ss秒 */
    public static final String DATETIME_FORMAT_21 = "yyyy年MM月dd日 HH时mm分ss秒";

    /** yyyy-MM-dd HH:mm:ss.SSS */
    public static final String DATETIME_FORMAT_23 = "yyyy-MM-dd HH:mm:ss.SSS";

    /** yyyy/MM/dd HH:mm:ss.SSS */
    public static final String DATETIME_FORMAT_23_2 = "yyyy/MM/dd HH:mm:ss.SSS";

    /** ONE_DAY_MILISECOND */
    private static final int ONE_DAY_MILISECOND = 1000 * 3600 * 24;

    /** DATE LENGTH */
    private static final int DATE_LENGTH_8 = 8;

    /** DATE LENGTH */
    private static final int DATE_LENGTH_10 = 10;

    /** DATE LENGTH */
    private static final int DATE_LENGTH_11 = 11;

    /** DATE LENGTH */
    private static final int DATE_LENGTH_14 = 14;

    /** DATE LENGTH */
    private static final int DATE_LENGTH_17 = 17;

    /** DATE LENGTH */
    private static final int DATE_LENGTH_19 = 19;

    /** DATE LENGTH */
    private static final int DATE_LENGTH_21 = 21;

    /** DATE LENGTH */
    private static final int DATE_LENGTH_23 = 23;

    /**
     * Description: <br>
     * 
     * @author yang.zhipeng <br>
     * @taskId <br>
     * @param ds <br>
     * @return <br>
     */
    public static Date format(final String ds) {
        if (StringUtils.isEmpty(ds)) {
            return null;
        }
        String dateStr = ds.trim();
        Date date = null;
        switch (dateStr.length()) {
            case DATE_LENGTH_8:
                date = format(dateStr, DateUtil.DATE_FORMAT_8);
                break;
            case DATE_LENGTH_10:
                date = format(dateStr,
                    dateStr.indexOf("/") == -1 ? DateUtil.DATE_FORMAT_10 : DateUtil.DATE_FORMAT_10_2);
                break;
            case DATE_LENGTH_11:
                date = format(dateStr, DateUtil.DATE_FORMAT_11);
                break;
            case DATE_LENGTH_14:
                date = format(dateStr, DateUtil.DATETIME_FORMAT_14);
                break;
            case DATE_LENGTH_17:
                date = format(dateStr, DateUtil.DATETIME_FORMAT_17);
                break;
            case DATE_LENGTH_19:
                date = format(dateStr,
                    dateStr.indexOf("/") == -1 ? DateUtil.DATETIME_FORMAT_19 : DateUtil.DATETIME_FORMAT_19_2);
                break;
            case DATE_LENGTH_21:
                date = format(dateStr, DateUtil.DATETIME_FORMAT_21);
                break;
            case DATE_LENGTH_23:
                date = format(dateStr,
                    dateStr.indexOf("/") == -1 ? DateUtil.DATETIME_FORMAT_23 : DateUtil.DATETIME_FORMAT_23_2);
                break;
            default:
                throw new IllegalArgumentException(dateStr + "不支持的时间格式");
        }
        return date;
    }

    /**
     * Description: <br>
     * 
     * @author yang.zhipeng <br>
     * @taskId <br>
     * @param date <br>
     * @param format <br>
     * @return <br>
     */
    public static Date format(final String date, final String format) {
        if (StringUtils.isEmpty(format)) {
            throw new IllegalArgumentException("the date format string is null!");
        }
        DateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date.trim());
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("the date string " + date + " is not matching format: " + format, e);
        }
    }

    /**
     * Description: <br>
     * 
     * @author yang.zhipeng <br>
     * @taskId <br>
     * @param date <br>
     * @return <br>
     */
    public static String format(final Date date) {
        return format(date, DateUtil.DATETIME_FORMAT_19);
    }

    /**
     * Description: <br>
     * 
     * @author yang.zhipeng <br>
     * @taskId <br>
     * @param date <br>
     * @param format <br>
     * @return <br>
     */
    public static String format(final Date date, final String format) {
        String result = null;
        if (date != null) {
            DateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        }
        return result;
    }

    /**
     * Description: getCurrentTimestamp<br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @return <br>
     */
    public static String getCurrentTimestamp() {
        return format(getCurrentDate(), DateUtil.DATETIME_FORMAT_14);
    }

    /**
     * Description: getCurrentTime<br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @return <br>
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Description: <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @return <br>
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取开始日期到今天的间隔天数
     * 
     * @param startDate 开始时间
     * @return 相差天数
     */
    public static int daysBetween(final Date startDate) {
        return daysBetween(startDate, getCurrentDate());
    }

    /**
     * Description: 获取月份最后一天<br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @param d
     * @return <br>
     */
    public static Date getYrMonthLastDay(final Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MONTH, 1);
        Date nextMonthFirstDay = DateUtil.getYrMonthFirstDay(calendar.getTime());
        calendar.setTime(nextMonthFirstDay);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * Description:获取月份第一天 <br>
     * 
     * @author 王伟<br>
     * @taskId <br>
     * @param d
     * @return <br>
     */
    public static Date getYrMonthFirstDay(final Date d) {
        String yrMonth = DateUtil.format(d, "yyyyMM");
        String date = yrMonth + "01";
        return DateUtil.format(date);
    }

    /**
     * 获取一年内，两个日期之间间隔的天数
     * 
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 相差天数
     */
    public static int daysBetween(final Date startDate, final Date endDate) {
        long s1 = startDate.getTime();
        long s2 = endDate.getTime();
        long c = s1 - s2;
        if (c <= ONE_DAY_MILISECOND) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int d1 = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(endDate);
            int d2 = calendar.get(Calendar.DAY_OF_YEAR);
            return d1 == d2 ? 0 : 1;
        }
        return Math.abs(Double.valueOf(Math.floor((s2 - s1) / ONE_DAY_MILISECOND)).intValue());
    }
}
