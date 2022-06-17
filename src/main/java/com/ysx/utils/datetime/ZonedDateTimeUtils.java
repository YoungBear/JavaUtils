package com.ysx.utils.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/15 21:58
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 带时区日期时间工具类
 */
public class ZonedDateTimeUtils {

    /**
     * 根据时间戳返回指定格式的时间日期格式字符串
     * 使用系统默认时区
     *
     * @param timestamp 时间戳 毫秒 如 1655301989483L
     * @param pattern   格式字符串 如 yyyy-MM-dd'T'HH:mm:ss.SSSxxx'['VV']'
     * @return 格式化后的字符串 如 2022-06-15T22:06:29.483+08:00[Asia/Shanghai]
     */
    public static String long2String(long timestamp, String pattern) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return zonedDateTime.format(dateTimeFormatter);
    }

    /**
     * 根据时间戳返回指定格式的时间日期格式字符串
     * 使用系统默认时区
     *
     * @param timestamp 时间戳 毫秒 如 1655301989483L
     * @param formatter formatter 如 DateTimeFormatter.ISO_ZONED_DATE_TIME
     * @return 格式化后的字符串 如 2022-06-15T22:06:29.483+08:00[Asia/Shanghai]
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
     * @param timestamp 时间戳 毫秒 如 1655301989483L
     * @param formatter formatter 如 DateTimeFormatter.ISO_ZONED_DATE_TIME
     * @param zoneId    时区信息 如 ZoneId.of("Europe/Moscow")
     * @return 格式化后的字符串 如 2022-06-15T17:06:29.483+03:00[Europe/Moscow]
     */
    public static String long2String(long timestamp, DateTimeFormatter formatter, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        return zonedDateTime.format(formatter);
    }


    /**
     * 时间日期格式字符串返回时间戳
     *
     * @param dateString 格式化后的字符串 如 2022-06-15T17:06:29.483+03:00[Europe/Moscow]
     * @param pattern    格式字符串 如 yyyy-MM-dd'T'HH:mm:ss.SSSxxx'['VV']'
     * @return 时间戳 毫秒 如 1655301989483L
     */
    public static long string2long(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter);
        return Instant.from(zonedDateTime).toEpochMilli();
    }

    /**
     * 时间日期格式字符串返回时间戳
     *
     * @param dateString 格式化后的字符串 如 2022-06-15T17:06:29.483+03:00[Europe/Moscow]
     * @param formatter  formatter 如 DateTimeFormatter.ISO_ZONED_DATE_TIME
     * @return 时间戳 毫秒 如 1655301989483L
     */
    public static long string2long(String dateString, DateTimeFormatter formatter) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter);
        return Instant.from(zonedDateTime).toEpochMilli();
    }

    /**
     * 时间日期格式字符串返回时间戳
     * 指定时区
     *
     * @param dateString 格式化后的字符串 如 2022-06-15T17:06:29.483+03:00[Europe/Moscow]
     * @param formatter  formatter 如 DateTimeFormatter.ISO_ZONED_DATE_TIME
     * @param zoneId     时区信息 如 ZoneId.of("Europe/Moscow")
     * @return 时间戳 毫秒 如 1655301989483L
     */
    public static long string2long(String dateString, DateTimeFormatter formatter, ZoneId zoneId) {
        formatter.withZone(zoneId);
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        return Instant.from(zonedDateTime).toEpochMilli();
    }
}
