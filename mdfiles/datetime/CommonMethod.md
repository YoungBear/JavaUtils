# 常用日期时间方法



## 1. 使用plus方法获取n天/月/年后的时间

使用plus方法可以计算n小时/天/月/年后的时间。根据API的不同，所支持的单位也不同。

```java
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
```

打印记录为：

```
nowLocalDate: 2022-08-21
nowLocalDatePlus1Day: 2022-08-22
nowLocalDatePlus1Month: 2022-09-21
nowLocalDatePlus1Year: 2023-08-21
nowLocalDatePlus1Day2: 2022-08-22
nowZonedDateTime: 2022-08-21T22:16:50.011+08:00[Asia/Shanghai], epochMilli: 1661091410011
nowZonedDateTimePlus1Year: 2023-08-21T22:16:50.011+08:00[Asia/Shanghai], epochMilli: 1692627410011

```

LocalDate仅表示日期，所以不支持Hours等操作。同样，LocalTime仅表示时间，则不支持Day等操作。

与plus操作相对应，minus表示减去对应的单位。

常用API及支持的单位：

| 类名           | API描述            | plus支持的单位                                           |
| -------------- | ------------------ | -------------------------------------------------------- |
| LocalDate      | 本地日期           | DAYS WEEKS MONTHS YEARS DECADES CENTURIES MILLENNIA      |
| LocalTime      | 本地时间           | NANOS MICROS MILLIS SECONDS MINUTES HOURS HALF_DAYS      |
| LocalDateTime  | 本地日期时间       | LocalDate和LocalTime的合集                               |
| Instant        | 时间戳             | NANOS MICROS MILLIS SECONDS MINUTES HOURS HALF_DAYS DAYS |
| ZonedDateTime  | 带时区的日期时间   | LocalDate和LocalTime的合集                               |
| OffsetDateTime | 带偏移量的日期时间 | LocalDate和LocalTime的合集                               |





## 2. 常见日期时间计算

使用`java.time.temporal.Temporal#with(java.time.temporal.TemporalAdjuster)` 可以计算最大最小等日期计算。

#### TemporalAdjusters 常用函数

| 函数名                                                     | 描述                                   |
| ---------------------------------------------------------- | -------------------------------------- |
| dayOfWeekInMonth(int ordinal,DayOfWeek dayOfWeek)          | 获取当前月的第几个星期几               |
| firstDayOfMonth()                                          | 当前月的第一天                         |
| firstDayOfNextMonth()                                      | 下个月的第一天                         |
| firstDayOfYear()                                           | 当前年的第一天                         |
| firstDayOfNextYear()                                       | 第二年的第一天                         |
| firstInMonth(DayOfWeek dayOfWeek)                          | 当前月的第一个星期几                   |
| lastDayOfMonth()                                           | 当前月的最后一天                       |
| lastDayOfYear()                                            | 当前年的最后一天                       |
| lastInMonth(DayOfWeek dayOfWeek)                           | 当前月的最后一个星期几                 |
| next(DayOfWeek dayOfWeek)                                  | 下一个星期几(可以跨月或者年)           |
| nextOrSame(DayOfWeek dayOfWeek)                            | 下一个或者相同的星期几(如果和当前相同) |
| previous(DayOfWeek dayOfWeek)                              | 上一个星期几                           |
| previousOrSame(DayOfWeek dayOfWeek)                        | 上一个或者相同的星期几                 |
| ofDateAdjuster(UnaryOperator<LocalDate> dateBasedAdjuster) | 自定义                                 |



参考文档：

https://docs.oracle.com/javase/8/docs/api/java/time/temporal/TemporalAdjusters.html

eg.

```java
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
```



#### 根据LocalDate获取具体Time或者时间戳

可以通过LocalDate.atTime()获取具体的时间。如

- LocalDate.atTime(LocalTime.MIN) 获取当天最小时间 00:00:00
- LocalDate.atTime(LocalTime.MAX) 获取当天最大时间 23:59:59
- LocalDate.atTime(LocalTime.NOON) 获取当天中午时间 12:00



eg.

```java
    public static void dateAndTimeUsage() {
        LocalDate nowLocalDate = LocalDate.now();
        // 2023-02-05
        System.out.println("nowLocalDate: " + nowLocalDate);
        LocalDateTime minLocalDateTime = nowLocalDate.atTime(LocalTime.MIN);
        // 2023-02-05T00:00
        System.out.println("minLocalDateTime: " + minLocalDateTime);
        LocalDateTime maxLocalDateTime = nowLocalDate.atTime(LocalTime.MAX);
        // 2023-02-05T23:59:59.999999999
        System.out.println("maxLocalDateTime: " + maxLocalDateTime);
        LocalDateTime noonLocalDateTime = nowLocalDate.atTime(LocalTime.NOON);
        // 2023-02-05T12:00
        System.out.println("noonLocalDateTime: " + noonLocalDateTime);
        // 获取时间戳
        // 1675526400000
        long minTimeStamp = minLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(minTimeStamp);
        // 1675612799999
        long maxTimeStamp = maxLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(maxTimeStamp);
        // 1675569600000
        long noonTimeStamp = noonLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(noonTimeStamp);
    }
```



结合LocalDate的with常用函数，就可以获取常用时间的时间戳：

eg. 

- 某天的0点的时间戳
- 某天的23:59:59的时间戳
- 某周/月/年第一天的0点的时间戳
- 某周/月/年最后一天23:59:59的时间戳

eg.

```java
    public static void commonTimestamp() {
        LocalDate nowLocalDate = LocalDate.now();
        // 2023-02-05
        System.out.println("nowLocalDate: " + nowLocalDate);
        // 1675526400000
        long minTimeStamp = nowLocalDate.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // 1675612799999
        long maxTimeStamp = nowLocalDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // 1675180800000
        long firstDayOfMonthMinTimeStamp = nowLocalDate.with(TemporalAdjusters.firstDayOfMonth()).atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // 1675267199999
        long firstDayOfMonthMaxTimeStamp = nowLocalDate.with(TemporalAdjusters.firstDayOfMonth()).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println("minTimeStamp: " + minTimeStamp);
        System.out.println("maxTimeStamp: " + maxTimeStamp);
        System.out.println("firstDayOfMonthMinTimeStamp: " + firstDayOfMonthMinTimeStamp);
        System.out.println("firstDayOfMonthMaxTimeStamp: " + firstDayOfMonthMaxTimeStamp);
    }
```







# 相关文章

## [1. LocalDateTime ZonedDateTime Instant 的相互转换](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/ConvertUtils.md)

## [2. 日期时间格式化与解析](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/FormatterUtils.md)

## [3. 带时区时间日期 ZonedDateTime](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/ZonedDateTimeUtils.md)

## [4. 夏令时](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/dst.md)

## [5. SimpleDateFormat 多线程存在的问题及解决方案](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/SimpleDateFormat.md)

## [6. 常用日期时间方法](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/CommonMethod.md)



# [源代码地址](https://github.com/YoungBear/JavaUtils)
