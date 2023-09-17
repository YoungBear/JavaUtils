# 不要对正则表达式进行频繁重复预编译



## 背景

在频繁调用场景，如方法体内或者循环语句中，新定义Pattern会导致重复预编译正则表达式，降低程序执行效率。另外，在 JDK 中部分 入参为正则表达式格式的 API，如 `String.replaceAll`, `String.split` 等，也需要关注性能问题。



## 验证



**正例：**

将 Pattern 对象预编译，并在常量中声明。

```java
    private static final String IP_V4 = "^(((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$";
    // Pattern 常量
    private static final Pattern IP_V4_PATTERN = Pattern.compile(IP_V4);
    public static boolean isValidIpv4V2(String input) {
        if (input == null) {
            return false;
        }
        return IP_V4_PATTERN.matcher(input).matches();
    }
```



**反例：**

每次调用时才声明 Pattern。

```java
    private static final String IP_V4 = "^(((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$";
    public static boolean isValidIpv4V1(String input) {
        if (input == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(IP_V4);
        return pattern.matcher(input).matches();
    }
```



**测试代码：**

```java
package com.ysx.utils.pattern.performance;

import org.junit.jupiter.api.Test;

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

```



执行结果：

```
isValidIpv4V1, input1, consume: 232ms
isValidIpv4V1, input2, consume: 74ms
isValidIpv4V2, input1, consume: 24ms
isValidIpv4V2, input2, consume: 19ms
isValidIpv4V3, input1, consume: 20ms
isValidIpv4V3, input2, consume: 12ms
```

根据执行结果，可以明显看到，预编译正则表达式可以提升性能。



## 总结

- 通常情况下，正则表达式为常量，所以可以将其作为常量量，在类编译时预编译。 `private static final Pattern xxx_PATTERN = Pattern.compile("xxx");`
- 对于动态的正则表达式，可以将其缓存，即缓存其 Pattern 结果。（参考 `isValidIpv4V3` )。
- 另外，对于外部收入的正则表达式，一定要校验其安全性，防止 ReDos 攻击。
