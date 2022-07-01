# SimpleDateFormat 多线程存在的问题及解决方案

## 背景：

通常情况下，项目中存在将日期时间格式化成字符串或者将格式化的字符串解析成时期时间，如果频繁的new SimpleDateFormat对象，则会造成内存消耗增大，所以，通常将一个不变的SimpleDateFormat声明为static final的成员变量。

但是，由于SimpleDateFormat是线程不安全的，在并发场景会出现错误或异常。本文主要讲解SimpleDateFormat多线程异常的Demo代码及常用的解决方案。

## 1. format问题



### 1. 多线程格式化出错

如下程序，模拟多线程情况下，使用SimpleDateFormat格式化日期时间：

```java
    // 2022-07-01 23:53:00
    private static final long BASE_TIMESTAMP = 1656690780000L;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static void formatTest1() {
        for (int i = 0; i < 10; i++) {
            int finalIndex = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long timestamp = BASE_TIMESTAMP + finalIndex * 1000;
                    Date date = new Date(timestamp);
                    String format = SIMPLE_DATE_FORMAT.format(date);
                    System.out.println(Thread.currentThread().getName() + ", " + timestamp + ", " + format);
                }
            });
            thread.start();
        }
    }
```

期望输出：(多线程运行，打印顺序可能不同)

```java
Thread-1, 1656690781000, 2022-07-01T23:53:01
Thread-0, 1656690780000, 2022-07-01T23:53:00
Thread-2, 1656690782000, 2022-07-01T23:53:02
Thread-3, 1656690783000, 2022-07-01T23:53:03
Thread-4, 1656690784000, 2022-07-01T23:53:04
Thread-5, 1656690785000, 2022-07-01T23:53:05
Thread-6, 1656690786000, 2022-07-01T23:53:06
Thread-7, 1656690787000, 2022-07-01T23:53:07
Thread-9, 1656690789000, 2022-07-01T23:53:09
Thread-8, 1656690788000, 2022-07-01T23:53:08
```

实际输出结果：

```java
Thread-2, 1656690782000, 2022-07-01T23:53:03
Thread-4, 1656690784000, 2022-07-01T23:53:04
Thread-1, 1656690781000, 2022-07-01T23:53:01
Thread-3, 1656690783000, 2022-07-01T23:53:03
Thread-0, 1656690780000, 2022-07-01T23:53:00
Thread-6, 1656690786000, 2022-07-01T23:53:06
Thread-5, 1656690785000, 2022-07-01T23:53:05
Thread-7, 1656690787000, 2022-07-01T23:53:09
Thread-8, 1656690788000, 2022-07-01T23:53:09
Thread-9, 1656690789000, 2022-07-01T23:53:09
```



问题根因，可查看SimpleDateFormat源代码。

### 1.2 解决方案1 使用ThreadLocal

```java
    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_SIMPLE_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }
    };
    public static void formatTest2() {
        for (int i = 0; i < 10; i++) {
            int finalIndex = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long timestamp = BASE_TIMESTAMP + finalIndex * 1000;
                    Date date = new Date(timestamp);
                    String format = THREAD_LOCAL_SIMPLE_DATE_FORMAT.get().format(date);
                    System.out.println(Thread.currentThread().getName() + ", " + timestamp + ", " + format);
                }
            });
            thread.start();
        }
    }
```

### 1.3 解决方案2 使用 DateTimeFormatter

```java
    private static final List<String> DATETIME_STRING_LIST = Arrays.asList(
            "2022-07-01T23:53:00",
            "2022-07-01T23:53:01",
            "2022-07-01T23:53:02",
            "2022-07-01T23:53:03",
            "2022-07-01T23:53:04",
            "2022-07-01T23:53:05",
            "2022-07-01T23:53:06",
            "2022-07-01T23:53:07",
            "2022-07-01T23:53:08",
            "2022-07-01T23:53:09"
    );
```



使用DateTimeFormatter 格式化：

```java
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static void formatTest3() {
        for (int i = 0; i < 10; i++) {
            int finalIndex = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long timestamp = BASE_TIMESTAMP + finalIndex * 1000;
                    LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
                    String format = localDateTime.format(DATE_TIME_FORMATTER);
                    System.out.println(Thread.currentThread().getName() + ", " + timestamp + ", " + format);
                }
            });
            thread.start();
        }
    }
```



## 2. parse问题

### 1. 多线程解析异常

    private static final List<String> DATETIME_STRING_LIST = Arrays.asList(
            "2022-07-01T23:53:00",
            "2022-07-01T23:53:01",
            "2022-07-01T23:53:02",
            "2022-07-01T23:53:03",
            "2022-07-01T23:53:04",
            "2022-07-01T23:53:05",
            "2022-07-01T23:53:06",
            "2022-07-01T23:53:07",
            "2022-07-01T23:53:08",
            "2022-07-01T23:53:09"
    );

使用SimpleDateFormat解析：

```
    public static void parseTest1() {
        for (int i = 0; i < 10; i++) {
            int finalIndex = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String dateTimeString = DATETIME_STRING_LIST.get(finalIndex);
                    try {
                        Date date = SIMPLE_DATE_FORMAT.parse(dateTimeString);
                        System.out.println(Thread.currentThread().getName() + ", " + date.getTime() + ", " + dateTimeString);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        }
    }
```



期望：（多线程运行，打印顺序可能不同）

```
Thread-0, 1656690780000, 2022-07-01T23:53:00
Thread-1, 1656690781000, 2022-07-01T23:53:01
Thread-2, 1656690782000, 2022-07-01T23:53:02
Thread-3, 1656690783000, 2022-07-01T23:53:03
Thread-4, 1656690784000, 2022-07-01T23:53:04
Thread-6, 1656690786000, 2022-07-01T23:53:06
Thread-9, 1656690789000, 2022-07-01T23:53:09
Thread-7, 1656690787000, 2022-07-01T23:53:07
Thread-5, 1656690785000, 2022-07-01T23:53:05
Thread-8, 1656690788000, 2022-07-01T23:53:08
```



实际：可能出现异常

```java
Exception in thread "Thread-2" Exception in thread "Thread-1" Exception in thread "Thread-0" Exception in thread "Thread-3" java.lang.NumberFormatException: multiple points
	at sun.misc.FloatingDecimal.readJavaFormatString(FloatingDecimal.java:1890)
	at sun.misc.FloatingDecimal.parseDouble(FloatingDecimal.java:110)
	at java.lang.Double.parseDouble(Double.java:538)
	at java.text.DigitList.getDouble(DigitList.java:169)
	at java.text.DecimalFormat.parse(DecimalFormat.java:2056)
	at java.text.SimpleDateFormat.subParse(SimpleDateFormat.java:1869)
	at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1514)
	at java.text.DateFormat.parse(DateFormat.java:364)
	at com.ysx.utils.datetime.old.SimpleDateFormatDemo$2.run(SimpleDateFormatDemo.java:72)
	at java.lang.Thread.run(Thread.java:748)
java.lang.NumberFormatException: multiple points
	at sun.misc.FloatingDecimal.readJavaFormatString(FloatingDecimal.java:1890)
	at sun.misc.FloatingDecimal.parseDouble(FloatingDecimal.java:110)
	at java.lang.Double.parseDouble(Double.java:538)
	at java.text.DigitList.getDouble(DigitList.java:169)
	at java.text.DecimalFormat.parse(DecimalFormat.java:2056)
	at java.text.SimpleDateFormat.subParse(SimpleDateFormat.java:1869)
	at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1514)
	at java.text.DateFormat.parse(DateFormat.java:364)
	at com.ysx.utils.datetime.old.SimpleDateFormatDemo$2.run(SimpleDateFormatDemo.java:72)
	at java.lang.Thread.run(Thread.java:748)
java.lang.NumberFormatException: For input string: ".1012E"
	at sun.misc.FloatingDecimal.readJavaFormatString(FloatingDecimal.java:2043)
	at sun.misc.FloatingDecimal.parseDouble(FloatingDecimal.java:110)
	at java.lang.Double.parseDouble(Double.java:538)
	at java.text.DigitList.getDouble(DigitList.java:169)
	at java.text.DecimalFormat.parse(DecimalFormat.java:2056)
	at java.text.SimpleDateFormat.subParse(SimpleDateFormat.java:2162)
	at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1514)
	at java.text.DateFormat.parse(DateFormat.java:364)
	at com.ysx.utils.datetime.old.SimpleDateFormatDemo$2.run(SimpleDateFormatDemo.java:72)
	at java.lang.Thread.run(Thread.java:748)
java.lang.NumberFormatException: For input string: ".2022E42022"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Long.parseLong(Long.java:578)
	at java.lang.Long.parseLong(Long.java:631)
	at java.text.DigitList.getLong(DigitList.java:195)
	at java.text.DecimalFormat.parse(DecimalFormat.java:2051)
	at java.text.SimpleDateFormat.subParse(SimpleDateFormat.java:1869)
	at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1514)
	at java.text.DateFormat.parse(DateFormat.java:364)
	at com.ysx.utils.datetime.old.SimpleDateFormatDemo$2.run(SimpleDateFormatDemo.java:72)
	at java.lang.Thread.run(Thread.java:748)
Thread-4, 1656690784000, 2022-07-01T23:53:04
Thread-7, 1656690787000, 2022-07-01T23:53:07
Thread-5, 1656690787000, 2022-07-01T23:53:05
Thread-8, 1656690788000, 2022-07-01T23:53:08
Thread-9, 1656690789000, 2022-07-01T23:53:09
Thread-6, 1656690786000, 2022-07-01T23:53:06

Process finished with exit code 0

```

### 1.2 解决方案1 使用ThreadLocal

```java
    public static void parseTest2() {
        for (int i = 0; i < 10; i++) {
            int finalIndex = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String dateTimeString = DATETIME_STRING_LIST.get(finalIndex);
                    try {
                        Date date = THREAD_LOCAL_SIMPLE_DATE_FORMAT.get().parse(dateTimeString);
                        System.out.println(Thread.currentThread().getName() + ", " + date.getTime() + ", " + dateTimeString);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        }
    }
```





### 2.3 解决方案2 使用 DateTimeFormatter

```java
    public static void parseTest3() {
        for (int i = 0; i < 10; i++) {
            int finalIndex = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String dateTimeString = DATETIME_STRING_LIST.get(finalIndex);
                    LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
                    long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    System.out.println(Thread.currentThread().getName() + ", " + timestamp + ", " + dateTimeString);
                }
            });
            thread.start();
        }
    }
```



# 参考：

## [1. Java8官方文档 Java SE 8 Date and Time](https://www.oracle.com/technical-resources/articles/java/jf14-date-time.html)

## [2. Java8官方文档：DateTimeFormatter](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)

## [3. 阿里巴巴 Java开发手册(黄山版).pdf](https://github.com/alibaba/p3c)





# 相关文章

## [1. LocalDateTime ZonedDateTime Instant 的相互转换](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/ConvertUtils.md)

## [2. 日期时间格式化与解析](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/FormatterUtils.md)

## [3. 带时区时间日期 ZonedDateTime](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/ZonedDateTimeUtils.md)

## [4. 夏令时](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/dst.md)

## [5. SimpleDateFormat 多线程存在的问题及解决方案](https://github.com/YoungBear/JavaUtils/blob/master/mdfiles/datetime/SimpleDateFormat.md)



# [源代码地址](https://github.com/YoungBear/JavaUtils)

