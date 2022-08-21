package com.ysx.utils.datetime;

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
        // 时间计算：plus
        plusUsage();
        // 最大最小时间
        maxUsage();

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
}
