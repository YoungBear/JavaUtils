package com.ysx.utils.datetime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/26 22:40
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class ConvertUtilsTest {

    @Test
    @DisplayName("ZonedDateTime test")
    public void testZonedDateTime() {
        String zonedDateTimeString = "2022-06-15T22:06:29.483+08:00[Asia/Shanghai]";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(zonedDateTimeString, formatter);

        // zoned2Local
        LocalDateTime localDateTime = ConvertUtils.zoned2Local(zonedDateTime);
        String localDateTimeString = "2022-06-15T22:06:29.483";
        Assertions.assertEquals(localDateTimeString, localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // zoned2Instant
        Instant instant = ConvertUtils.zoned2Instant(zonedDateTime);
        long timestamp = 1655301989483L;
        Assertions.assertEquals(timestamp, instant.toEpochMilli());
    }

    @Test
    @DisplayName("LocalDateTime test")
    public void testLocalDateTime() {
        String localDateTimeString = "2022-06-15T22:06:29.483";
        LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");

        // local2Zoned
        ZonedDateTime zonedDateTime = ConvertUtils.local2Zoned(localDateTime, zoneId);
        String zonedDateTimeString = "2022-06-15T22:06:29.483+08:00[Asia/Shanghai]";
        Assertions.assertEquals(zonedDateTimeString, zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        // local2Instant
        Instant instant = ConvertUtils.local2Instant(localDateTime, zoneId);
        long timestamp = 1655301989483L;
        Assertions.assertEquals(timestamp, instant.toEpochMilli());
    }

    @Test
    @DisplayName("Instant test")
    public void testInstant() {
        long timestamp = 1655301989483L;
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");

        // instant2local
        LocalDateTime localDateTime = ConvertUtils.instant2local(instant, zoneId);
        String localDateTimeString = "2022-06-15T22:06:29.483";
        Assertions.assertEquals(localDateTimeString, localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // instant2Zoned
        ZonedDateTime zonedDateTime = ConvertUtils.instant2Zoned(instant, zoneId);
        String zonedDateTimeString = "2022-06-15T22:06:29.483+08:00[Asia/Shanghai]";
        Assertions.assertEquals(zonedDateTimeString, zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

}
