package com.ysx.utils.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRules;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/26 12:38
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 夏令时
 * 参考：http://www.timeofdate.com/
 * 美国的夏令时，从每年3月第2个星期天凌晨开始，到每年11月第1个星期天凌晨结束。以2017年为例，美国2017年夏令时从3月12日开始，到11月5日结束。
 * America/New_York PT1H -04:00
 * 参考：http://www.timeofdate.com/country/United%20States
 * 澳大利亚的夏令时：从每年10月的第1个周日凌晨开始，到每年4月第1个周日凌晨结束。以2017年为例，澳大利亚在4月2日结束夏令时，在10月1日重新开始夏令时
 * Australia/Sydney PT1H +11:00
 * http://www.timeofdate.com/country/Australia
 */
public class DSTUtils {

    /**
     * 是否是夏令时
     *
     * @param zoneIdString 时区id
     * @param instant 时间信息
     * @return 是否是夏令时
     */
    public static boolean isDST(String zoneIdString, Instant instant) {
        ZoneId zoneId = ZoneId.of(zoneIdString);
        ZoneRules rules = zoneId.getRules();
        return rules.isDaylightSavings(instant);
    }

    /**
     * 是否是夏令时
     *
     * @param zoneIdString 时区id，如Asia/Shanghai
     * @param localDateTime localDateTime
     * @return 是否是夏令时
     */
    public static boolean isDST(String zoneIdString, LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.of(zoneIdString);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return isDST(zoneIdString, instant);
    }

    /**
     * 是否是夏令时
     *
     * @param zoneIdString 时区id，如Asia/Shanghai
     * @param timestamp 时间戳 如1655301989483L
     * @return 是否是夏令时
     */
    public static boolean isDST(String zoneIdString, long timestamp) {
        return isDST(zoneIdString, Instant.ofEpochMilli(timestamp));
    }

    /**
     * 是否是夏令时
     *
     * @param zoneIdString 时区id，如Asia/Shanghai
     * @param isoLocalDateTime iso日期时间格式，如：2022-06-15T22:06:29.483
     * @return 是否是夏令时
     */
    public static boolean isDST(String zoneIdString, String isoLocalDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        ZoneId zoneId = ZoneId.of(zoneIdString);
        dateTimeFormatter.withZone(zoneId);
        LocalDateTime localDateTime = LocalDateTime.parse(isoLocalDateTime, dateTimeFormatter);
        return isDST(zoneIdString, localDateTime);
    }
}
