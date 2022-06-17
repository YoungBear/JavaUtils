package com.ysx.utils.datetime;

import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/15 22:05
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 参考：
 * DateTimeFormatter：https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
 * ZonedDateTime https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html
 */
public class ZonedDateTimeUtilsTest {

    @Test
    public void long2StringTest() {
        long timestamp = 1655301989483L;

        String dateStr1 = "2022-06-15T22:06:29.483+08:00[Asia/Shanghai]";
        Assert.assertEquals(dateStr1, ZonedDateTimeUtils.long2String(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME));
        String dateStr2 = "2022-06-15T22:06:29.483+08:00";
        Assert.assertEquals(dateStr2, ZonedDateTimeUtils.long2String(timestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        String dateStr3 = "2022-06-15T22:06:29.483";
        Assert.assertEquals(dateStr3, ZonedDateTimeUtils.long2String(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // 分别对应 pattern 字符串如下
        String pattern1 = "yyyy-MM-dd'T'HH:mm:ss.SSSxxx'['VV']'";
        Assert.assertEquals(dateStr1, ZonedDateTimeUtils.long2String(timestamp, pattern1));
        String pattern2 = "yyyy-MM-dd'T'HH:mm:ss.SSSxxx";
        Assert.assertEquals(dateStr2, ZonedDateTimeUtils.long2String(timestamp, pattern2));
        String pattern3 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        Assert.assertEquals(dateStr3, ZonedDateTimeUtils.long2String(timestamp, pattern3));

    }

    @Test
    public void string2longTest() {
        String str1 = "2022-06-15T22:06:29.483+08:00[Asia/Shanghai]";
        long timestamp = 1655301989483L;
        long timestamp1 = ZonedDateTimeUtils.string2long(str1, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        Assert.assertEquals(timestamp, timestamp1);
        long timestamp11 = ZonedDateTimeUtils.string2long(str1, "yyyy-MM-dd'T'HH:mm:ss.SSSxxx'['VV']'");
        Assert.assertEquals(timestamp, timestamp11);

        String str2 = "2022-06-15T22:06:29.483+08:00";
        long timestamp2 = ZonedDateTimeUtils.string2long(str2, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        Assert.assertEquals(timestamp, timestamp2);
        long timestamp22 = ZonedDateTimeUtils.string2long(str2, "yyyy-MM-dd'T'HH:mm:ss.SSSxxx");
        Assert.assertEquals(timestamp, timestamp22);

        // 失败，因为没有带时区信息
//        String str3 = "2022-06-15T22:06:29.483";
//        long timestamp3 = DateTimeUtils.string2long(str3, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        Assert.assertEquals(timestamp, timestamp3);
    }

    @Test
    public void long2StringWithZoneIdTest() {
        long timestamp = 1655301989483L;
        // 默认时区 Asia/Shanghai 2022-06-15T22:06:29.483+08:00[Asia/Shanghai]
        String dateStringShanghai = "2022-06-15T22:06:29.483+08:00[Asia/Shanghai]";
        Assert.assertEquals(dateStringShanghai, ZonedDateTimeUtils.long2String(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME));
        // 指定时区 Europe/Moscow 2022-06-15T17:06:29.483+03:00[Europe/Moscow]
        ZoneId zoneIdMoscow = ZoneId.of("Europe/Moscow");
        String dateStringMoscow = "2022-06-15T17:06:29.483+03:00[Europe/Moscow]";
        Assert.assertEquals(dateStringMoscow, ZonedDateTimeUtils.long2String(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME, zoneIdMoscow));
        // 指定时区 Europe/Paris 2022-06-15T16:06:29.483+02:00[Europe/Paris]
        ZoneId zoneIdParis = ZoneId.of("Europe/Paris");
        String dateStringParis = "2022-06-15T16:06:29.483+02:00[Europe/Paris]";
        Assert.assertEquals(dateStringParis, ZonedDateTimeUtils.long2String(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME, zoneIdParis));
        // 指定时区 America/Los_Angeles 2022-06-15T07:06:29.483-07:00[America/Los_Angeles]
        ZoneId zoneIdLos_Angeles = ZoneId.of("America/Los_Angeles");
        String dateStringLos_Angeles = "2022-06-15T07:06:29.483-07:00[America/Los_Angeles]";
        Assert.assertEquals(dateStringLos_Angeles, ZonedDateTimeUtils.long2String(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME, zoneIdLos_Angeles));
    }

    @Test
    public void string2longWithZoneIdTest() {
        long timestamp = 1655301989483L;
        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        String str3 = "2022-06-15T22:06:29.483";
        long timestamp3 = ZonedDateTimeUtils.string2long(str3, DateTimeFormatter.ISO_LOCAL_DATE_TIME, zoneIdShanghai);
        Assert.assertEquals(timestamp, timestamp3);
    }
}
