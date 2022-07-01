package com.ysx.utils.datetime.old;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/7/1 23:50
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description SimpleDateFormat 是线程不安全的
 */
public class SimpleDateFormatDemo {

    // 2022-07-01 23:53:00
    private static final long BASE_TIMESTAMP = 1656690780000L;

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

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * 使用ThreadLocal规避SimpleDateFormat并发问题
     */
    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_SIMPLE_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }
    };

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static void main(String[] args) {
//        formatTest1();
//        formatTest2();
//        formatTest3();

//        parseTest1();
//        parseTest2();
        parseTest3();

    }

    /**
     * 模拟多线程 SimpleDateFormat format 场景
     */
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

    /**
     * 模拟多线程 SimpleDateFormat parse 场景
     */
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
}
