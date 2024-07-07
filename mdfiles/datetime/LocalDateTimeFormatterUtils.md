# LocalDateTime 字符串与时间戳的相互转换


主要介绍LocalDateTime的格式化字符串与时间戳的相互转换

常见带日期时间格式：

| 字段名        | 字段值                                |
| ------------- | ------------------------------------- |
| api格式       | DateTimeFormatter.ISO_LOCAL_DATE_TIME |
| 字符串pattern | yyyy-MM-dd'T'HH:mm:ss.SSS'            |
| 示例          | 2022-06-15T22:06:29.483               |
| 字符串pattern | yyyy-MM-dd HH:mm:ss                   |
| 示例          | 2022-06-15 22:06:29                   |



## 1. 使用默认格式和默认时区

```java
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

}

```



对应单元测试

```java
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
    @DisplayName("根据字符串解析为时间戳 默认格式 默认时区")
    public void string2longTest1() {
        ZoneId zoneIdShanghai = ZoneId.of("Asia/Shanghai");
        try (MockedStatic<ZoneId> mockedZonedId = Mockito.mockStatic(ZoneId.class)) {
            mockedZonedId.when(ZoneId::systemDefault).thenReturn(zoneIdShanghai);
            assertEquals(1655301989000L, LocalDateTimeFormatterUtils.string2long("2022-06-15 22:06:29"));
        }
    }

}

```





## 2. 更多方法(指定格式和指定时区)

```java
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

```



对应单元测试

```java
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

```



## [源码地址-github](https://github.com/YoungBear/JavaUtils)

## [源码地址-gitee](https://gitee.com/YoungBear2023/JavaUtils)

## [更多文章](https://blog.csdn.net/next_second)