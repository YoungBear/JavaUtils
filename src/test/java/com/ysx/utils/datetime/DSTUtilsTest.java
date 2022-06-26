package com.ysx.utils.datetime;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/26 13:07
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description DSTUtilsTest
 */
public class DSTUtilsTest {
    public static void main(String[] args) {
        // 获取时间戳
        String dateString = "2022-01-15T22:06:29.483+08:00[Asia/Shanghai]";
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        long timestamp = Instant.from(zonedDateTime).toEpochMilli();
        System.out.println(timestamp);
    }

    @Test
    public void instantTest1() {
        // 2022-06-15T22:06:29.483+08:00[Asia/Shanghai] 北半球可能有夏令时
        long timestamp = 1655301989483L;
        Instant instant = Instant.ofEpochSecond(timestamp);
        // 2022-06-15 伦敦有夏令时
        String zoneIdString1 = "Europe/London";
        Assert.assertTrue(DSTUtils.isDST(zoneIdString1, instant));
        // 2022-06-15 巴黎有夏令时
        String zoneIdString2 = "Europe/Paris";
        Assert.assertTrue(DSTUtils.isDST(zoneIdString2, instant));
        // 2022-06-15 柏林有夏令时
        String zoneIdString3 = "Europe/Berlin";
        Assert.assertTrue(DSTUtils.isDST(zoneIdString3, instant));
        // 2022-06-15 莫斯科无夏令时
        String zoneIdString4 = "Europe/Moscow";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString4, instant));
        // 2022-06-15 上海无夏令时
        String zoneIdString5 = "Asia/Shanghai";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString5, instant));
        // 2022-06-15 悉尼无夏令时
        String zoneIdString6 = "Australia/Sydney";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString6, instant));
        // 2022-06-15 纽约有夏令时
        String zoneIdString7 = "America/New_York";
        Assert.assertTrue(DSTUtils.isDST(zoneIdString7, instant));
    }

    @Test
    public void instantTest2() {
        // 2022-01-15T22:06:29.483+08:00[Asia/Shanghai] 南半球可能有夏令时
        long timestamp = 1642255589483L;
        Instant instant = Instant.ofEpochSecond(timestamp);
        // 2022-06-15 上海无夏令时
        String zoneIdString5 = "Asia/Shanghai";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString5, instant));
        // 2022-01-15 悉尼有夏令时
        String zoneIdString6 = "Australia/Sydney";
        Assert.assertTrue(DSTUtils.isDST(zoneIdString6, instant));
        // 2022-01-15 纽约无夏令时
        String zoneIdString7 = "America/New_York";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString7, instant));
    }

    @Test
    public void isoLocalDateTimeTest1() {
        String str = "2022-06-15T22:06:29.483";
        // 2022-06-15 上海无夏令时
        String zoneIdString5 = "Asia/Shanghai";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString5, str));
        // 2022-06-15 悉尼有夏令时
        String zoneIdString6 = "Australia/Sydney";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString6, str));
        // 2022-06-15 纽约无夏令时
        String zoneIdString7 = "America/New_York";
        Assert.assertTrue(DSTUtils.isDST(zoneIdString7, str));

    }

    @Test
    public void isoLocalDateTimeTest2() {
        String str = "2022-01-15T22:06:29.483";
        // 2022-01-15 上海无夏令时
        String zoneIdString5 = "Asia/Shanghai";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString5, str));
        // 2022-01-15 悉尼有夏令时
        String zoneIdString6 = "Australia/Sydney";
        Assert.assertTrue(DSTUtils.isDST(zoneIdString6, str));
        // 2022-01-15 纽约无夏令时
        String zoneIdString7 = "America/New_York";
        Assert.assertFalse(DSTUtils.isDST(zoneIdString7, str));

    }
}
