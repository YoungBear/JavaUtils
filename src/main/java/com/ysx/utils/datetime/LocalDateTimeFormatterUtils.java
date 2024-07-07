package com.ysx.utils.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime 字符串与时间戳的相互转换
 *
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-07 11:38
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class LocalDateTimeFormatterUtils {

    /**
     * 默认时间日期格式字符串
     * yyyy-MM-dd HH:mm:ss
     * eg.
     * 2022-06-15 22:06:29
     */
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 根据时间戳格式化为字符串
     * 指定格式的时间日期格式
     * 使用系统默认时区
     *
     * @param timestamp 时间戳 毫秒 如 1655301989483L
     * @return 格式化后的字符串 如 2022-06-15T22:06:29.483
     */
    public static String long2String(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DEFAULT_FORMATTER);
    }

    /**
     * 根据时间戳格式化为字符串
     * 指定格式的时间日期格式
     * 使用系统默认时区
     *
     * @param timestamp 时间戳 毫秒 如 1655301989483L
     * @param formatter formatter 如 DateTimeFormatter.ISO_LOCAL_DATE_TIME
     * @return 格式化后的字符串 如 2022-06-15T17:06:29.483
     */
    public static String long2String(long timestamp, DateTimeFormatter formatter) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter);
    }

    /**
     * 根据时间戳格式化为字符串
     * 默认格式的时间日期格式
     * 指定时区
     *
     * @param timestamp 时间戳 毫秒 如 1655301989483L
     * @param zoneId 时区信息 如 ZoneId.of("Europe/Moscow")
     * @return 格式化后的字符串 如 2022-06-15T17:06:29.483
     */
    public static String long2String(long timestamp, ZoneId zoneId) {
        return Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDateTime().format(DEFAULT_FORMATTER);
    }


    /**
     * 根据时间戳格式化为字符串
     * 指定格式的时间日期格式
     * 指定时区
     *
     * @param timestamp 时间戳 毫秒 如 1655301989483L
     * @param formatter formatter 如 DateTimeFormatter.ISO_LOCAL_DATE_TIME
     * @param zoneId 时区信息 如 ZoneId.of("Europe/Moscow")
     * @return 格式化后的字符串 如 2022-06-15T17:06:29.483
     */
    public static String long2String(long timestamp, DateTimeFormatter formatter, ZoneId zoneId) {
        return Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDateTime().format(formatter);
    }

    /**
     * 根据字符串解析时间戳
     * 默认格式的时间日期格式
     * 默认时区
     *
     * @param dateString 格式化后的字符串 如 2022-06-15T17:06:29.483
     * @return 时间戳 毫秒 如 1655301989483L
     */
    public static long string2long(String dateString) {
        return LocalDateTime.parse(dateString, DEFAULT_FORMATTER).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 根据字符串解析时间戳
     * 指定格式的时间日期格式
     * 默认时区
     *
     * @param dateString 格式化后的字符串 如 2022-06-15T17:06:29.483
     * @param formatter formatter 如 DateTimeFormatter.ISO_LOCAL_DATE_TIME
     * @return 时间戳 毫秒 如 1655301989483L
     */
    public static long string2long(String dateString, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateString, formatter).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 根据字符串解析时间戳
     * 默认格式的时间日期格式
     * 指定时区
     *
     * @param dateString 格式化后的字符串 如 2022-06-15T17:06:29.483
     * @param zoneId 时区信息 如 ZoneId.of("Europe/Moscow")
     * @return 时间戳 毫秒 如 1655301989483L
     */
    public static long string2long(String dateString, ZoneId zoneId) {
        return LocalDateTime.parse(dateString, DEFAULT_FORMATTER).atZone(zoneId).toInstant().toEpochMilli();
    }

    /**
     * 根据字符串解析时间戳
     * 指定格式的时间日期格式
     * 指定时区
     *
     * @param dateString 格式化后的字符串 如 2022-06-15T17:06:29.483
     * @param formatter formatter 如 DateTimeFormatter.ISO_LOCAL_DATE_TIME
     * @param zoneId 时区信息 如 ZoneId.of("Europe/Moscow")
     * @return 时间戳 毫秒 如 1655301989483L
     */
    public static long string2long(String dateString, DateTimeFormatter formatter, ZoneId zoneId) {
        return LocalDateTime.parse(dateString, formatter).atZone(zoneId).toInstant().toEpochMilli();
    }

}
