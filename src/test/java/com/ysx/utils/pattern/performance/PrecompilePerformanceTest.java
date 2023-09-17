package com.ysx.utils.pattern.performance;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-09-17 8:31
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 正则表达式性能测试
 */
public class PrecompilePerformanceTest {

    private static final String IP_V4 = "^(((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$";
    // Pattern 常量
    private static final Pattern IP_V4_PATTERN = Pattern.compile(IP_V4);

    // 缓存
    private static final Map<String, Pattern> cacheCompilePatternMap = new ConcurrentHashMap<>();

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static void precompile() {
        isValidIpv4V1("192.168.12.13");
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static void not_precompile() {
        isValidIpv4V2("192.168.12.13");
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public static void cache_precompile() {
        isValidIpv4V3("192.168.12.13");
    }

//    public static void main(String[] args) throws Exception {
//        org.openjdk.jmh.Main.main(args);
//    }

    public static boolean isValidIpv4V1(String input) {
        if (input == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(IP_V4);
        return pattern.matcher(input).matches();
    }

    public static boolean isValidIpv4V2(String input) {
        if (input == null) {
            return false;
        }
        return IP_V4_PATTERN.matcher(input).matches();
    }

    public static boolean isValidIpv4V3(String input) {
        if (input == null) {
            return false;
        }
        if (!cacheCompilePatternMap.containsKey(IP_V4)) {
            cacheCompilePatternMap.put(IP_V4, Pattern.compile(IP_V4));
        }
        Pattern pattern = cacheCompilePatternMap.get(IP_V4);
        return pattern.matcher(input).matches();
    }

    @Test
    public void performanceTest() {
        String input1 = "192.168.12.13";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            isValidIpv4V1(input1);
        }
        long stop = System.currentTimeMillis();
        System.out.println("isValidIpv4V1, input1, consume: " + (stop - start) + "ms");

        String input2 = "192.168.12.13";
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            isValidIpv4V1(input2);
        }
        long stop2 = System.currentTimeMillis();
        System.out.println("isValidIpv4V1, input2, consume: " + (stop2 - start2) + "ms");
    }

    @Test
    public void performanceV2Test() {
        String input1 = "192.168.12.13";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            isValidIpv4V2(input1);
        }
        long stop = System.currentTimeMillis();
        System.out.println("isValidIpv4V2, input1, consume: " + (stop - start) + "ms");

        String input2 = "192.168.12.13";
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            isValidIpv4V2(input2);
        }
        long stop2 = System.currentTimeMillis();
        System.out.println("isValidIpv4V2, input2, consume: " + (stop2 - start2) + "ms");
    }

    @Test
    public void performanceV3Test() {
        String input1 = "192.168.12.13";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            isValidIpv4V3(input1);
        }
        long stop = System.currentTimeMillis();
        System.out.println("isValidIpv4V3, input1, consume: " + (stop - start) + "ms");

        String input2 = "192.168.12.13";
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            isValidIpv4V3(input2);
        }
        long stop2 = System.currentTimeMillis();
        System.out.println("isValidIpv4V3, input2, consume: " + (stop2 - start2) + "ms");
    }

}
