package com.ysx.utils.datetime;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/26 23:15
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class FormatterUtilsTest {

    @Test
    public void localFormatTest() {
        String localDateTimeString = "2022-06-15T22:06:29.483";
        LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String localDateTimeString1 = FormatterUtils.local2String(localDateTime);
        String localDateTimeString2 = FormatterUtils.local2String(localDateTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
        String localDateTimeString3 = FormatterUtils.local2String(localDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Assertions.assertEquals(localDateTimeString, localDateTimeString1);
        Assertions.assertEquals(localDateTimeString, localDateTimeString2);
        Assertions.assertEquals(localDateTimeString, localDateTimeString3);

    }

    @Test
    public void localParseTest() {
        String localDateTimeString = "2022-06-15T22:06:29.483";
        LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDateTime localDateTime1 = FormatterUtils.string2Local(localDateTimeString);
        LocalDateTime localDateTime2 = FormatterUtils.string2Local(localDateTimeString, "yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime localDateTime3 = FormatterUtils.string2Local(localDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Assertions.assertEquals(localDateTime, localDateTime1);
        Assertions.assertEquals(localDateTime, localDateTime2);
        Assertions.assertEquals(localDateTime, localDateTime3);
    }

    @Test
    public void zonedFormatTest() {
        String zonedDateTimeString = "2022-06-15T22:06:29.483+08:00[Asia/Shanghai]";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(zonedDateTimeString, formatter);
        String zonedDateTimeString1 = FormatterUtils.zoned2String(zonedDateTime);
        Assertions.assertEquals(zonedDateTimeString, zonedDateTimeString1);
    }

    @Test
    public void zonedParseTest() {
        String zonedDateTimeString = "2022-06-15T22:06:29.483+08:00[Asia/Shanghai]";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(zonedDateTimeString, formatter);
        ZonedDateTime zonedDateTime1 = FormatterUtils.string2Zoned(zonedDateTimeString);
        Assertions.assertEquals(zonedDateTime, zonedDateTime1);
    }
}
