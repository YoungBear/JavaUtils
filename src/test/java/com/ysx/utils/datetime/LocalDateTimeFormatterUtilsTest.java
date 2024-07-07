package com.ysx.utils.datetime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-07 11:55
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description unit test for {@link LocalDateTimeFormatterUtils}
 */
public class LocalDateTimeFormatterUtilsTest {

    @Test
    @DisplayName("根据时间戳格式化为字符串 默认格式 默认时区")
    public void long2StringTest1() {
        long timestamp = 1655301989483L;
        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        try (MockedStatic<ZoneId> mockedZonedId = Mockito.mockStatic(ZoneId.class)) {
            mockedZonedId.when(ZoneId::systemDefault).thenReturn(zoneIdShanghai);
            assertEquals("2022-06-15 22:06:29", LocalDateTimeFormatterUtils.long2String(timestamp));
        }
    }

    @Test
    @DisplayName("根据时间戳格式化为字符串 指定格式 默认时区")
    public void long2StringTest2() {
        long timestamp = 1655301989483L;
        DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        try (MockedStatic<ZoneId> mockedZonedId = Mockito.mockStatic(ZoneId.class)) {
            mockedZonedId.when(ZoneId::systemDefault).thenReturn(zoneIdShanghai);
            assertEquals("2022-06-15T22:06:29.483", LocalDateTimeFormatterUtils.long2String(timestamp, formatter1));
            assertEquals("2022-06-15 22:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, formatter2));
        }
    }

    @Test
    @DisplayName("根据时间戳格式化为字符串 默认格式 指定时区")
    public void long2StringTest3() {
        long timestamp = 1655301989483L;

        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        ZoneId zoneIdMoscow = ZoneId.of("Europe/Moscow");
        ZoneId zoneIdParis = ZoneId.of("Europe/Paris");
        ZoneId zoneIdLos_Angeles = ZoneId.of("America/Los_Angeles");

        assertEquals("2022-06-15 22:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, zoneIdShanghai));
        assertEquals("2022-06-15 17:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, zoneIdMoscow));
        assertEquals("2022-06-15 16:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, zoneIdParis));
        assertEquals("2022-06-15 07:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, zoneIdLos_Angeles));
    }

    @Test
    @DisplayName("根据时间戳格式化为字符串 指定格式 指定时区")
    public void long2StringTest4() {
        long timestamp = 1655301989483L;
        DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        ZoneId zoneIdMoscow = ZoneId.of("Europe/Moscow");
        ZoneId zoneIdParis = ZoneId.of("Europe/Paris");
        ZoneId zoneIdLos_Angeles = ZoneId.of("America/Los_Angeles");

        assertEquals("2022-06-15T22:06:29.483", LocalDateTimeFormatterUtils.long2String(timestamp, formatter1, zoneIdShanghai));
        assertEquals("2022-06-15T17:06:29.483", LocalDateTimeFormatterUtils.long2String(timestamp, formatter1, zoneIdMoscow));
        assertEquals("2022-06-15T16:06:29.483", LocalDateTimeFormatterUtils.long2String(timestamp, formatter1, zoneIdParis));
        assertEquals("2022-06-15T07:06:29.483", LocalDateTimeFormatterUtils.long2String(timestamp, formatter1, zoneIdLos_Angeles));

        assertEquals("2022-06-15 22:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, formatter2, zoneIdShanghai));
        assertEquals("2022-06-15 17:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, formatter2, zoneIdMoscow));
        assertEquals("2022-06-15 16:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, formatter2, zoneIdParis));
        assertEquals("2022-06-15 07:06:29", LocalDateTimeFormatterUtils.long2String(timestamp, formatter2, zoneIdLos_Angeles));
    }

    @Test
    @DisplayName("根据字符串解析为时间戳 默认格式 默认时区")
    public void string2longTest1() {
        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        try (MockedStatic<ZoneId> mockedZonedId = Mockito.mockStatic(ZoneId.class)) {
            mockedZonedId.when(ZoneId::systemDefault).thenReturn(zoneIdShanghai);
            assertEquals(1655301989000L, LocalDateTimeFormatterUtils.string2long("2022-06-15 22:06:29"));
        }
    }

    @Test
    @DisplayName("根据字符串解析为时间戳 指定格式 默认时区")
    public void string2longTest2() {
        DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        long timestamp1 = 1655301989483L;
        long timestamp2 = 1655301989000L;
        String dateStringShanghai1 = "2022-06-15T22:06:29.483";

        String dateStringShanghai2 = "2022-06-15 22:06:29";
        try (MockedStatic<ZoneId> mockedZonedId = Mockito.mockStatic(ZoneId.class)) {
            mockedZonedId.when(ZoneId::systemDefault).thenReturn(zoneIdShanghai);
            assertEquals(timestamp1, LocalDateTimeFormatterUtils.string2long(dateStringShanghai1, formatter1));
            assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringShanghai2, formatter2));
        }
    }

    @Test
    @DisplayName("根据字符串解析为时间戳 默认格式 指定时区")
    public void string2longTest3() {
        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        ZoneId zoneIdMoscow = ZoneId.of("Europe/Moscow");
        ZoneId zoneIdParis = ZoneId.of("Europe/Paris");
        ZoneId zoneIdLos_Angeles = ZoneId.of("America/Los_Angeles");

        String dateStringShanghai2 = "2022-06-15 22:06:29";
        String dateStringMoscow2 = "2022-06-15 17:06:29";
        String dateStringParis2 = "2022-06-15 16:06:29";
        String dateStringLos_Angeles2 = "2022-06-15 07:06:29";

        long timestamp2 = 1655301989000L;

        assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringShanghai2, zoneIdShanghai));
        assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringMoscow2, zoneIdMoscow));
        assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringParis2, zoneIdParis));
        assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringLos_Angeles2, zoneIdLos_Angeles));
    }

    @Test
    @DisplayName("根据字符串解析为时间戳 指定格式 指定时区")
    public void string2longTest4() {
        DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        ZoneId zoneIdMoscow = ZoneId.of("Europe/Moscow");
        ZoneId zoneIdParis = ZoneId.of("Europe/Paris");
        ZoneId zoneIdLos_Angeles = ZoneId.of("America/Los_Angeles");

        String dateStringShanghai1 = "2022-06-15T22:06:29.483";
        String dateStringMoscow1 = "2022-06-15T17:06:29.483";
        String dateStringParis1 = "2022-06-15T16:06:29.483";
        String dateStringLos_Angeles1 = "2022-06-15T07:06:29.483";

        String dateStringShanghai2 = "2022-06-15 22:06:29";
        String dateStringMoscow2 = "2022-06-15 17:06:29";
        String dateStringParis2 = "2022-06-15 16:06:29";
        String dateStringLos_Angeles2 = "2022-06-15 07:06:29";

        long timestamp1 = 1655301989483L;
        long timestamp2 = 1655301989000L;

        assertEquals(timestamp1, LocalDateTimeFormatterUtils.string2long(dateStringShanghai1, formatter1, zoneIdShanghai));
        assertEquals(timestamp1, LocalDateTimeFormatterUtils.string2long(dateStringMoscow1, formatter1, zoneIdMoscow));
        assertEquals(timestamp1, LocalDateTimeFormatterUtils.string2long(dateStringParis1, formatter1, zoneIdParis));
        assertEquals(timestamp1, LocalDateTimeFormatterUtils.string2long(dateStringLos_Angeles1, formatter1, zoneIdLos_Angeles));

        assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringShanghai2, formatter2, zoneIdShanghai));
        assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringMoscow2, formatter2, zoneIdMoscow));
        assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringParis2, formatter2, zoneIdParis));
        assertEquals(timestamp2, LocalDateTimeFormatterUtils.string2long(dateStringLos_Angeles2, formatter2, zoneIdLos_Angeles));
    }
}
