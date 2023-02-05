package com.ysx.utils.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/8/21 21:58
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 日期时间常用方法
 */
public class CommonMethod {

    public static void main(String[] args) {
//        // 时间计算：plus
//        plusUsage();
//        // 最大最小时间
//        maxUsage();

        localDataWithUsage();

    }

    /**
     * 使用plus方法获取n天/月/年后的时间
     */
    public static void plusUsage() {
        LocalDate nowLocalDate = LocalDate.now();
        System.out.println("nowLocalDate: " + nowLocalDate);

        LocalDate nowLocalDatePlus1Day = nowLocalDate.plusDays(1);
        System.out.println("nowLocalDatePlus1Day: " + nowLocalDatePlus1Day);

        LocalDate nowLocalDatePlus1Month = nowLocalDate.plusMonths(1);
        System.out.println("nowLocalDatePlus1Month: " + nowLocalDatePlus1Month);

        LocalDate nowLocalDatePlus1Year = nowLocalDate.plusYears(1);
        System.out.println("nowLocalDatePlus1Year: " + nowLocalDatePlus1Year);

        // 指定单位
        LocalDate nowLocalDatePlus1Day2 = nowLocalDate.plus(1, ChronoUnit.DAYS);
        System.out.println("nowLocalDatePlus1Day2: " + nowLocalDatePlus1Day2);

        // 使用ZonedDateTime获取时间戳及时间计算
        ZonedDateTime nowZonedDateTime = ZonedDateTime.now();
        System.out.println("nowZonedDateTime: " + nowZonedDateTime
                + ", epochMilli: " + nowZonedDateTime.toInstant().toEpochMilli());

        // 获取1年后的时间戳（常用于设置有效期）
        ZonedDateTime nowZonedDateTimePlus1Year = nowZonedDateTime.plus(1, ChronoUnit.YEARS);
        System.out.println("nowZonedDateTimePlus1Year: " + nowZonedDateTimePlus1Year
                + ", epochMilli: " + nowZonedDateTimePlus1Year.toInstant().toEpochMilli());

    }

    /**
     * 当天最小最大时间
     * 当月/当年第一天最后一天
     */
    public static void maxUsage() {
        // 当前时间
        ZonedDateTime nowZonedDateTime = ZonedDateTime.now();
        System.out.println("nowZonedDateTime: " + nowZonedDateTime
                + ", epochMilli: " + nowZonedDateTime.toInstant().toEpochMilli());

        // 当天最早时间 00:00:00
        ZonedDateTime nowZonedDateTimeWithMinLocalTime = nowZonedDateTime.with(LocalTime.MIN);
        System.out.println("nowZonedDateTimeWithMinLocalTime: " + nowZonedDateTimeWithMinLocalTime
                + ", epochMilli: " + nowZonedDateTimeWithMinLocalTime.toInstant().toEpochMilli());

        // 当天最晚时间 23:59:59
        ZonedDateTime nowZonedDateTimeWithMaxLocalTime = nowZonedDateTime.with(LocalTime.MAX);
        System.out.println("nowZonedDateTimeWithMaxLocalTime: " + nowZonedDateTimeWithMaxLocalTime
                + ", epochMilli: " + nowZonedDateTimeWithMaxLocalTime.toInstant().toEpochMilli());


        // 本月或本年的第一天 最后一天
        LocalDate nowLocalDate = LocalDate.now();
        LocalDate nowLocalDateWithFirstDayOfMonth = nowLocalDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate nowLocalDateWithFirstDayOfYear = nowLocalDate.with(TemporalAdjusters.firstDayOfYear());
        LocalDate nowLocalDateWithLastDayOfMonth = nowLocalDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate nowLocalDateWithLastDayOfYear = nowLocalDate.with(TemporalAdjusters.lastDayOfYear());

        System.out.println("nowLocalDate: " + nowLocalDate);
        System.out.println("nowLocalDateWithFirstDayOfMonth: " + nowLocalDateWithFirstDayOfMonth);
        System.out.println("nowLocalDateWithFirstDayOfYear: " + nowLocalDateWithFirstDayOfYear);
        System.out.println("nowLocalDateWithLastDayOfMonth: " + nowLocalDateWithLastDayOfMonth);
        System.out.println("nowLocalDateWithLastDayOfYear: " + nowLocalDateWithLastDayOfYear);

    }

    /**
     * localDate with 常见用法
     */
    public static void localDataWithUsage() {
        // 当前时间 2023-02-05
        LocalDate nowLocalDate = LocalDate.now();
        // dayOfWeekInMonth 表示同一个月的第几个星期几
        // 比如 第三个星期日 2023-02-19
        LocalDate thirdSundayOfMonth = nowLocalDate.with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.SUNDAY));
        // 当前月的第一天 2023-02-01
        LocalDate firstDayOfMonth = nowLocalDate.with(TemporalAdjusters.firstDayOfMonth());
        // 下个月的第一天 2023-03-01
        LocalDate firstDayOfNextMonth = nowLocalDate.with(TemporalAdjusters.firstDayOfNextMonth());
        // 当前年的第一天 2023-01-01
        LocalDate firstDayOfYear = nowLocalDate.with(TemporalAdjusters.firstDayOfYear());
        // 第二年的第一天 2024-01-01
        LocalDate firstDayOfNextYear = nowLocalDate.with(TemporalAdjusters.firstDayOfNextYear());
        // firstInMonth 当前月的第一个星期几 即 dayOfWeekInMonth(1, DayOfWeek)
        // 第一个星期日 2023-02-05
        LocalDate firstSundayOfMonth = nowLocalDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY));
        // 当前月的最后一天 2023-02-28
        LocalDate lastDayOfMonth = nowLocalDate.with(TemporalAdjusters.lastDayOfMonth());
        // 当前年的最后一天 2023-12-31
        LocalDate lastDayOfYear = nowLocalDate.with(TemporalAdjusters.lastDayOfYear());
        // lastInMonth 当前月的最后一个星期几
        // 最后一个星期日 2023-02-26
        LocalDate lastSundayOfMonth = nowLocalDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY));
        // 下一个星期几 (可以跨月或者年)
        // 下一个星期日 2023-02-12
        LocalDate nextSunday = nowLocalDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        // 下一个或者相同的星期几(如果和当前相同)
        // 当前是 2023-02-05 星期日 则 nextOfSameSunday 为 2023-02-05
        LocalDate nextOfSameSunday = nowLocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 当前是 2023-02-05 星期日 则 nextOfSameMonday 为 2023-02-06
        LocalDate nextOfSameMonday = nowLocalDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        // 上一个星期日 2023-01-29
        LocalDate previousSunday = nowLocalDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        // 上一个或者相同的星期日 2023-02-05
        LocalDate previousOrSameSunday = nowLocalDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        // 上一个或者相同的星期六 2023-01-30
        LocalDate previousOrSameMonday = nowLocalDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // 自定义
        // 加两天 2023-02-07
        LocalDate plus2Days = nowLocalDate.with(TemporalAdjusters.ofDateAdjuster(date -> date.plus(2, ChronoUnit.DAYS)));


        System.out.println("nowLocalDate: " + nowLocalDate);
        System.out.println("thirdSundayOfMonth: " + thirdSundayOfMonth);
        System.out.println("firstDayOfMonth: " + firstDayOfMonth);
        System.out.println("firstDayOfNextMonth: " + firstDayOfNextMonth);
        System.out.println("firstDayOfYear: " + firstDayOfYear);
        System.out.println("firstDayOfNextYear: " + firstDayOfNextYear);
        System.out.println("firstSundayOfMonth: " + firstSundayOfMonth);
        System.out.println("lastDayOfMonth: " + lastDayOfMonth);
        System.out.println("lastDayOfYear: " + lastDayOfYear);
        System.out.println("lastSundayOfMonth: " + lastSundayOfMonth);
        System.out.println("nextSunday: " + nextSunday);
        System.out.println("nextOfSameSunday: " + nextOfSameSunday);
        System.out.println("nextOfSameMonday: " + nextOfSameMonday);
        System.out.println("previousSunday: " + previousSunday);
        System.out.println("previousOrSameSunday: " + previousOrSameSunday);
        System.out.println("previousOrSameMonday: " + previousOrSameMonday);
        System.out.println("plus2Days: " + plus2Days);
    }

}
