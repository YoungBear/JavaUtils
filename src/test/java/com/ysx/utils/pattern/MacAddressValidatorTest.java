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
