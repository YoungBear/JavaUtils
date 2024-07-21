# Mockito 中的 Answer 参数

在使用Mockito中的mock方法时，有一个重载方法，支持传入Answer类型的参数，表示没有打桩的方法的处理策略(default answer for unstubbed methods)。常见的选项如下：

| 参数值              | 描述                                                         |
| ------------------- | ------------------------------------------------------------ |
| CALLS_REAL_METHODS  | 调用真实方法                                                 |
| RETURNS_DEEP_STUBS  | 允许深度模拟。即允许在mock对象中使用链式调用                 |
| RETURNS_DEFAULTS    | 默认值。表示返回默认值，如0，空集合，null等。                |
| RETURNS_MOCKS       | first tries to return ordinary values (zeros, empty collections, empty string, etc.) then it tries to return mocks. If the return type cannot be mocked (e.g. is final) then plain null is returned. |
| RETURNS_SELF        | 在build模式下返回本身的方法，直接返回自身。                  |
| RETURNS_SMART_NULLS | 智能null，不会报空指针                                       |



## CALLS_REAL_METHODS

参考代码：

```java
package com.ysx.utils.datetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-21 23:27
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 日期工具类
 */
public class DateUtils {

    /**
     * 默认格式
     */
    private static final DateTimeFormatter YEAR_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取前一天的起始时间 00:00:00
     * 如 2024-07-21 00:00:00
     *
     * @return 格式化日期时间
     */
    public static String getLastDayStartTimePattern() {
        return LocalDate.now().minusDays(1).atTime(LocalTime.MIN).format(YEAR_DATE_TIME_FORMATTER);

    }
}

```



单元测试：

```Java
package com.ysx.utils.datetime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-21 23:34
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description unit test for {@link DateUtils}
 */
public class DateUtilsTest {

    @Test
    @DisplayName("不跨月 getLastDayStartTimePattern test")
    public void getLastDayStartTimePatternTest1() {
        LocalDate mockedNow = LocalDate.of(2024, 7, 21);
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(mockedNow);
            Assertions.assertEquals("2024-07-20 00:00:00", DateUtils.getLastDayStartTimePattern());
        }
    }

    @Test
    @DisplayName("跨月 getLastDayStartTimePattern test")
    public void getLastDayStartTimePatternTest2() {
        LocalDate mockedNow = LocalDate.of(2024, 7, 1);
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(mockedNow);
            Assertions.assertEquals("2024-06-30 00:00:00", DateUtils.getLastDayStartTimePattern());
        }

    }

    @Test
    @DisplayName("跨年 getLastDayStartTimePattern test")
    public void getLastDayStartTimePatternTest3() {
        LocalDate mockedNow = LocalDate.of(2024, 1, 1);
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(mockedNow);
            Assertions.assertEquals("2023-12-31 00:00:00", DateUtils.getLastDayStartTimePattern());
        }
    }
}

```



**说明：**

这里仅mock静态方法LocalDate::now，所以对于minusDays等其他非静态方法，会调用其真实方法。因为minusDays内部也会调用静态方法LocalDate::ofEpochDay，所以如果不加CALLS_REAL_METHODS参数的话，就会报空指针异常。



## 参考

https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#static_mocks

https://javadoc.io/static/org.mockito/mockito-core/5.12.0/org/mockito/Mockito.html#CALLS_REAL_METHODS



## 源代码

https://github.com/YoungBear/JavaUtils

