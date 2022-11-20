package com.ysx.utils.pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-11-20 22:25
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 版本号校验 单元测试
 * 参考：<a href="https://regex101.com/r/vkijKf/1/">...</a>
 */
public class VersionUtilsTest {

    private static Stream<String> validVersionProvider() {
        return Stream.of(
                "0.0.4",
                "1.2.3",
                "10.20.30",
                "1.0.0",
                "2.0.0",
                "1.1.7",
                "99999999999999999999999.999999999999999999.99999999999999999"
        );
    }

    private static Stream<String> invalidVersionProvider() {
        return Stream.of(
                "1",
                "1.2",
                "1.2.3-0123",
                "+invalid",
                "invalid",
                "alpha",
                "01.1.1",
                "1.01.1",
                "1.1.01",
                "1.2-SNAPSHOT",
                "1.2-RC-SNAPSHOT"
        );
    }

    @ParameterizedTest(name = "#{index} - Run test with Version = {0}")
    @MethodSource("validVersionProvider")
    void test_version_regex_valid(String inputVersion) {
        assertTrue(VersionUtils.isValidVersion(inputVersion));
    }

    @ParameterizedTest(name = "#{index} - Run test with Version = {0}")
    @MethodSource("invalidVersionProvider")
    void test_version_regex_invalid(String inputVersion) {
        assertFalse(VersionUtils.isValidVersion(inputVersion));
    }

    @Test
    @DisplayName("version compare return 1")
    public void test_version_compare_return_1() {
        String version1 = "1.1.1";
        String version2 = "1.1.0";
        Assertions.assertEquals(1, VersionUtils.compare(version1, version2));
        version1 = "2.1.1";
        version2 = "1.2.0";
        Assertions.assertEquals(1, VersionUtils.compare(version1, version2));
        version1 = "10.1.1";
        version2 = "2.2.0";
        Assertions.assertEquals(1, VersionUtils.compare(version1, version2));
    }

    @Test
    @DisplayName("version compare return -1")
    public void test_version_compare_return__1() {
        String version1 = "1.1.1";
        String version2 = "1.1.2";
        Assertions.assertEquals(-1, VersionUtils.compare(version1, version2));
        version1 = "2.1.1";
        version2 = "2.2.0";
        Assertions.assertEquals(-1, VersionUtils.compare(version1, version2));
        version1 = "10.1.1";
        version2 = "11.2.0";
        Assertions.assertEquals(-1, VersionUtils.compare(version1, version2));
    }

    @Test
    @DisplayName("version compare return 0")
    public void test_version_compare_return_0() {
        String version1 = "1.1.1";
        String version2 = "1.1.1";
        Assertions.assertEquals(0, VersionUtils.compare(version1, version2));
    }

}
