package com.ysx.utils.datetime;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/26 22:57
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 格式化与解析
 */
public class FormatterUtils {

    /**
     * 使用默认格式格式化本地日期时间
     * 默认格式为ISO_LOCAL_DATE_TIME，即 yyyy-MM-dd'T'HH:mm:ss.SSS，如 2022-06-15T22:06:29.483
     *
     * @param localDateTime 本地日期时间
     * @return 格式化字符串
     */
    public static String local2String(LocalDateTime localDateTime) {
        return local2String(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    }

    /**
     * 指定格式格式化本地日期时间
     *
     * @param localDateTime 本地日期时间
     * @param pattern 格式：如 yyyy-MM-dd'T'HH:mm:ss.SSS
     * @return 格式化字符串
     */
    public static String local2String(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return local2String(localDateTime, dateTimeFormatter);
    }

    /**
     * 指定 DateTimeFormatter 格式化本地日期时间
     *
     * @param localDateTime 本地日期时间
     * @param dateTimeFormatter DateTimeFormatter 如 DateTimeFormatter.ISO_LOCAL_DATE_TIME
     * @return 格式化字符串
     */
    public static String local2String(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 使用默认格式解析本地日期时间
     *
     * @param localDateTimeString 格式化字符串 如 2022-06-15T22:06:29.483
     * @return 本地日期时间
     */
    public static LocalDateTime string2Local(String localDateTimeString) {
        return string2Local(localDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    }

    /**
     * 指定格式解析本地日期时间
     *
     * @param localDateTimeString 格式化字符串 如 2022-06-15T22:06:29.483
     * @param pattern 格式：如 yyyy-MM-dd'T'HH:mm:ss.SSS
     * @return 本地日期时间
     */
    public static LocalDateTime string2Local(String localDateTimeString, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return string2Local(localDateTimeString, dateTimeFormatter);
    }

    /**
     * 指定 DateTimeFormatter 解析本地日期时间
     *
     * @param localDateTimeString 格式化字符串 如 2022-06-15T22:06:29.483
     * @param dateTimeFormatter DateTimeFormatter 如 DateTimeFormatter.ISO_LOCAL_DATE_TIME
     * @return 本地日期时间
     */
    public static LocalDateTime string2Local(String localDateTimeString, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(localDateTimeString, dateTimeFormatter);
    }

    /**
     * 使用默认格式格式化带时区的日期时间
     *
     * @param zonedDateTime 带时区的日期时间
     * @return 格式化带时区的日期时间 格式为：yyyy-MM-dd'T'HH:mm:ss.SSSxxx'['VV']' 如 2022-06-15T22:06:29.483+08:00[Asia/Shanghai]
     */
    public static String zoned2String(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);

    }

    /**
     * 使用默认格式解析带时区的日期时间
     *
     * @param zonedDateTimeString 格式化带时区的日期时间 格式为：yyyy-MM-dd'T'HH:mm:ss.SSSxxx'['VV']' 如 2022-06-15T22:06:29.483+08:00[Asia/Shanghai]
     * @return 带时区的日期时间
     */
    public static ZonedDateTime string2Zoned(String zonedDateTimeString) {
        return ZonedDateTime.parse(zonedDateTimeString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }


}
