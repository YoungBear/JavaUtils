package com.ysx.utils.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/26 22:22
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description LocalDateTime ZonedDateTime Instant 的相互转换
 */
public class ConvertUtils {

    /**
     * 将 ZonedDateTime 转换为 LocalDateTime
     *
     * @param zonedDateTime 带时区的日期时间
     * @return 本地日期时间
     */
    public static LocalDateTime zoned2Local(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * 将 LocalDateTime 转换为 ZonedDateTime
     *
     * @param localDateTime 本地日期时间
     * @param zoneId 时区
     * @return 带时区的日期时间
     */
    public static ZonedDateTime local2Zoned(LocalDateTime localDateTime, ZoneId zoneId) {
        return ZonedDateTime.of(localDateTime, zoneId);
    }

    /**
     * 将 Instant 转换为 LocalDateTime
     *
     * @param instant 时间戳
     * @param zoneId 时区
     * @return 本地日期时间
     */
    public static LocalDateTime instant2local(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 将 LocalDateTime 转换为 Instant
     *
     * @param localDateTime 本地日期时间
     * @param zoneId 时区
     * @return 时间戳
     */
    public static Instant local2Instant(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId).toInstant();
    }


    /**
     * 将 Instant 转换为 ZonedDateTime
     *
     * @param instant 时间戳
     * @param zoneId 时区
     * @return 带时区的日期时间
     */
    public static ZonedDateTime instant2Zoned(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId);
    }

    /**
     * 将 ZonedDateTime 转换为 Instant
     *
     * @param zonedDateTime 带时区的日期时间
     * @return 时间戳
     */
    public static Instant zoned2Instant(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant();
    }
}
