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
 * @description 时期工具类
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
