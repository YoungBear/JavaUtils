# 常用正则表达式-MAC 地址

## MAC地址的定义

**物理地址**（通常称为 **MAC地址**，Media Access Control Address）是网络设备在数据链路层（如以太网、Wi-Fi）的唯一标识符。它由设备的网络接口卡（NIC）固化在硬件中，用于在局域网（LAN）中精确寻址设备。MAC地址长度为 **48位**（6字节），通常以十六进制表示，每组2个字符，共6组，每组可以用冒号或者中划线分隔，即格式为 `XX:XX:XX:XX:XX:XX` 或 `XX-XX-XX-XX-XX-XX`，例如 `00:1A:2B:3C:4D:5E`， 也可以不用分隔符，如`001A2B3C4D5E`。

在MAC地址中，前24位（即前三个八位组或前六个十六进制数字）称为组织唯一标识符（Organizationally Unique Identifier, OUI）。这部分由IEEE（电气和电子工程师协会）分配给各个硬件制造商，用于唯一标识网络设备的生产厂商。这意味着，通过查看MAC地址的前24位，可以识别出制造该设备的厂商。

剩下的24位（即后三个八位组或后六个十六进制数字）则由厂商自行分配，用来标识具体的设备，确保每个设备在全球范围内都是唯一的。

## MAC地址的正则表达式

根据定义，可以给出正则表达式：

```shell
# MAC地址正则表达式（支持冒号、中划线分隔或无分隔符）
^([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}|([0-9a-fA-F]{2}-){5}[0-9a-fA-F]{2}|[0-9a-fA-F]{12}$
```

转换为java代码：

```java
String MAC_ADDRESS = "^([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}|([0-9a-fA-F]{2}-){5}[0-9a-fA-F]{2}|[0-9a-fA-F]{12}$";
```



## 对应完整java代码

```java
package com.ysx.utils.pattern;

import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-03-30 23:24
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description Mac 地址
 */
public class MacAddressValidator {
    // 支持冒号、中划线分隔或无分隔符
    private static final String MAC_ADDRESS = "^([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}|([0-9a-fA-F]{2}-){5}[0-9a-fA-F]{2}|[0-9a-fA-F]{12}$";
    // 预编译提高性能
    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile(MAC_ADDRESS);

    /**
     * 是否是有效的Mac地址
     *
     * @param input 字符串
     * @return 是否是有效的Mac地址
     */
    public static boolean isValidMacAddress(String input) {
        if (input == null) {
            return false;
        }
        return MAC_ADDRESS_PATTERN.matcher(input).matches();
    }
}
```





## 单元测试

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
 * @date 2025-03-30 23:25
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description test for {@link MacAddressValidator}
 */
public class MacAddressValidatorTest {
    private static Stream<String> validMacAddressProvider() {
        return Stream.of(
                "00:1A:2B:3C:4D:5E", // 冒号分隔
                "00-1A-2B-3C-4D-5E", // 中划线分隔
                "001A2B3C4D5E", // 无分隔符
                "00:1a:2B:3c:4D:5e"); // 大小写混合
    }

    private static Stream<String> invalidMacAddressProvider() {
        return Stream.of(
                null, // null
                "", // empty
                "00:1G:2B:3C:4D:5E", // 无效字符（例如'G'）
                "00:1A-2B:3C-4D:5E", // 分隔符不一致（冒号和连字符混合）
                "00:1A:2B:3C:4D",    // 长度不足
                "00:1A:2B:3C:4D:5E:FF", // 多余字符
                "00 1A 2B 3C 4D 5E");  // 无效分隔符（例如空格）
    }

    @ParameterizedTest(name = "#{index} - Run test with MacAddress = {0}")
    @MethodSource("validMacAddressProvider")
    void test_mac_address_regex_valid(String input) {
        assertTrue(MacAddressValidator.isValidMacAddress(input));
    }

    @ParameterizedTest(name = "#{index} - Run test with MacAddress = {0}")
    @MethodSource("invalidMacAddressProvider")
    void test_mac_address_regex_invalid(String input) {
        assertFalse(MacAddressValidator.isValidMacAddress(input));
    }
}

```



## [源代码地址](https://github.com/YoungBear/JavaUtils)
