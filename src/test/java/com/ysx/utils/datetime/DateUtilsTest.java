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
