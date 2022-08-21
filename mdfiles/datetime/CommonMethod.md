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





## 2. 最大最小时间

使用`java.time.temporal.Temporal#with(java.time.temporal.TemporalAdjuster)` 可以计算最大最小等时间。

```java
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
```

打印结果为：

```
nowZonedDateTime: 2022-08-21T22:16:50.012+08:00[Asia/Shanghai], epochMilli: 1661091410012
nowZonedDateTimeWithMinLocalTime: 2022-08-21T00:00+08:00[Asia/Shanghai], epochMilli: 1661011200000
nowZonedDateTimeWithMaxLocalTime: 2022-08-21T23:59:59.999999999+08:00[Asia/Shanghai], epochMilli: 1661097599999
nowLocalDate: 2022-08-21
nowLocalDateWithFirstDayOfMonth: 2022-08-01
nowLocalDateWithFirstDayOfYear: 2022-01-01
nowLocalDateWithLastDayOfMonth: 2022-08-31
nowLocalDateWithLastDayOfYear: 2022-12-31
```

# 相关文章

## [1. LocalDateTime ZonedDateTime Instant 的相互转换](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/ConvertUtils.md)

## [2. 日期时间格式化与解析](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/FormatterUtils.md)

## [3. 带时区时间日期 ZonedDateTime](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/ZonedDateTimeUtils.md)

## [4. 夏令时](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/dst.md)

## [5. SimpleDateFormat 多线程存在的问题及解决方案](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/SimpleDateFormat.md)

## [6. 常用日期时间方法](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/CommonMethod.md)



# [源代码地址](https://github.com/YoungBear/JavaUtils)
