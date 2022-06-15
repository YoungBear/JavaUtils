package com.ysx.utils.datetime;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/15 21:58
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 日期时间工具类
 */
public class DateTimeUtils {

    /**
     * 根据时间戳返回指定格式的时间日期格式字符串
     * 使用默认时区
     *
     * @param timestamp 时间戳 毫秒
     * @param pattern   格式字符串
     * @return 格式化后的字符串
     */
    public static String long2String(long timestamp, String pattern) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return zonedDateTime.format(dateTimeFormatter);
    }

    /**
     * 根据时间戳返回指定格式的时间日期格式字符串
     * 使用默认时区
     *
     * @param timestamp 时间戳 毫秒
     * @param formatter formatter
     * @return 格式化后的字符串
     */
    public static String long2String(long timestamp, DateTimeFormatter formatter) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return zonedDateTime.format(formatter);
    }
    /**
     * 根据时间戳返回指定格式的时间日期格式字符串
     * 指定时区
     *
     * @param timestamp 时间戳 毫秒
     * @param formatter formatter
     * @param zoneId 时区信息
     * @return 格式化后的字符串
     */
    public static String long2String(long timestamp, DateTimeFormatter formatter, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        return zonedDateTime.format(formatter);
    }



    /**
     * 时间日期格式字符串返回时间戳
     *
     * @param dateString 格式化后的字符串
     * @param pattern    格式字符串
     * @return 时间戳 毫秒
     */
    public static long string2long(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter);
        return Instant.from(zonedDateTime).toEpochMilli();
    }

    /**
     * 时间日期格式字符串返回时间戳
     *
     * @param dateString 格式化后的字符串
     * @param formatter  formatter
     * @return 时间戳 毫秒
     */
    public static long string2long(String dateString, DateTimeFormatter formatter) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter);
        return Instant.from(zonedDateTime).toEpochMilli();
    }

    /**
     * 时间日期格式字符串返回时间戳
     * 指定时区
     *
     * @param dateString 格式化后的字符串
     * @param formatter  formatter
     * @return 时间戳 毫秒
     */
    public static long string2long(String dateString, DateTimeFormatter formatter, ZoneId zoneId) {
        formatter.withZone(zoneId);
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
//        return localDateTime.toInstant(ZonedDateTime.of(localDateTime, zoneId).getOffset()).toEpochMilli();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        return Instant.from(zonedDateTime).toEpochMilli();
    }
}
