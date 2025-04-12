# IP地址

## 1. IPv4地址

### 1. IPv4地址定义

参考[百度百科](https://baike.baidu.com/item/IPv4/422599?fr=aladdin)

IPv4使用32位（4字节）地址，因此地址空间中只有4,294,967,296（即2的32次方）个地址。

通常使用点分十进制进行表示方法，如0.0.0.0-255.255.255.255。



### 2. IPv4地址的正则表达式



一个合法的IPv4地址由4组数字组成，每组数字之间以`.`隔开，数字的取值范围为0-255。每组数字的总结如下：

| 范围    | 描述                         | 正则表达式 |
| ------- | ---------------------------- | ---------- |
| 0-9     | 1位数字                      | `\d`       |
| 10-99   | 2位数字                      | `[1-9]\d`  |
| 100-199 | 以1开头的3位数字             | `1\d{2}`   |
| 200-249 | 以2开头，第2位是0-4的3位数字 | `2[0-4]\d` |
| 250-255 | 25开头，第3位是0-5的3位数字  | `25[0-5]`  |

所以每组数字的正则表达式为：

```shell
\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5]
```

其中， ```\d``` 和 ```[1-9]\d``` 可以合并为 ```[1-9]?\d``` 。再加上点号之后，加上起始和结尾的标记保证全匹配，则IPv4的完整正则表达式为:

```shell
^((\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.){3}(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])$
```

java代码中斜杠需要转移，则对应java代码的正则为：

```java
String IP_V4 = "^(((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$"
```

### 3. 对应java代码

IPValidator.java

```java
package com.ysx.utils.pattern;

import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-03-30 17:54
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description IP校验
 */
public class IPValidator {

    private static final String IP_V4 = "^((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])$";
    private static final Pattern IP_V4_PATTERN = Pattern.compile(IP_V4);

    /**
     * 是否是有效的ipv4地址
     *
     * @param input 字符串
     * @return 是否是有效的ipv4地址
     */
    public static boolean isValidIpv4(String input) {
        if (input == null) {
            return false;
        }
        return IP_V4_PATTERN.matcher(input).matches();
    }
}

```



使用junit5单元测试：IPValidatorTest.java

```java
package com.ysx.utils.pattern;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-03-30 17:58
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class IPValidatorTest {

    private static Stream<String> validIPv4Provider() {
        return Stream.of(
                "0.0.0.0",
                "0.0.0.1",
                "127.0.0.1",
                "1.2.3.4",              // 0-9
                "11.1.1.0",             // 10-99
                "101.1.1.0",            // 100-199
                "201.1.1.0",            // 200-249
                "255.255.255.255",      // 250-255
                "192.168.1.1",
                "192.168.1.255",
                "100.100.100.100");
    }

    private static Stream<String> invalidIPv4Provider() {
        return Stream.of(
                null,
                "000.000.000.000",          // leading 0
                "00.00.00.00",              // leading 0
                "1.2.3.04",                 // leading 0
                "1.02.03.4",                // leading 0
                "1.2",                      // 1 dot
                "1.2.3",                    // 2 dots
                "1.2.3.4.5",                // 4 dots
                "192.168.1.1.1",            // 4 dots
                "256.1.1.1",                // 256
                "1.256.1.1",                // 256
                "1.1.256.1",                // 256
                "1.1.1.256",                // 256
                "-100.1.1.1",               // -100
                "1.-100.1.1",               // -100
                "1.1.-100.1",               // -100
                "1.1.1.-100",               // -100
                "1...1",                    // empty between .
                "1..1",                     // empty between .
                "1.1.1.1.",                 // last .
                "");                        // empty
    }

    @ParameterizedTest(name = "#{index} - Run test with IPv4 = {0}")
    @MethodSource("validIPv4Provider")
    void test_ipv4_regex_valid(String ipv4) {
        assertTrue(IPValidator.isValidIpv4(ipv4));
    }

    @ParameterizedTest(name = "#{index} - Run test with IPv4 = {0}")
    @MethodSource("invalidIPv4Provider")
    void test_ipv4_regex_invalid(String ipv4) {
        assertFalse(IPValidator.isValidIpv4(ipv4));
    }
}

```



## 源代码地址

- [github](https://github.com/YoungBear/JavaUtils)
- [gitee](https://gitee.com/YoungBear2023/JavaUtils)



# 参考

[1. Java IP Address (IPv4) regex examples](https://mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/)



## 其他的ipv4地址的正则及问题



### 有问题的

```java
// https://juejin.cn/post/6844903768232820749
// 有问题 00.00.00.00 认为是有效的
String IP_V4 = "^(((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$";

// http://www.java2s.com/example/java/java.util.regex/is-ipv4-address-by-regex.html
// -- 有问题 000.000.000.000 认为是合法的
String IPV4_PATTERN ="^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$"
// https://mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
String IPV4_PATTERN ="^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$"
```



### 正确的：

```java
// https://mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/  
  // 25[0-5]        = 250-255
  // (2[0-4])[0-9]  = 200-249
  // (1[0-9])[0-9]  = 100-199
  // ([1-9])[0-9]   = 10-99
  // [0-9]          = 0-9
  // (\.(?!$))      = can't end with a dot
  private static final String IPV4_PATTERN_SHORTEST =
          "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$";      
```

