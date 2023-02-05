package com.ysx.utils.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-02-05 9:31
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 工具类
 */
public final class DateTimeUtils {

    public static void main(String[] args) {
        LocalDate min = LocalDate.MIN;
        System.out.println("LocalDate.MIN: " + LocalDate.MIN);

        LocalDate now = LocalDate.now();
        LocalDateTime startOfDay = now.atStartOfDay();
        System.out.println("startOfDay: " + startOfDay);
        int year = now.get(ChronoField.YEAR);
        System.out.println(year);
        LocalDate firstDayOfYear = now.with(TemporalAdjusters.firstDayOfYear());
        System.out.println("firstDayOfYear: " + firstDayOfYear);

    }

}
