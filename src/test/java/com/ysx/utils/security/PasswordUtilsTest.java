package com.ysx.utils.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-05-06 18:41
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description test for {@link PasswordUtils}
 */
class PasswordUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUtilsTest.class);

    @Test
    void generatePasswordTestForRandomLength() {
        String password = PasswordUtils.generatePassword();
        LOGGER.info("password: {}, length: {}.", password, password.length());
        assertTrue(password.length() >= 8);
        assertTrue(password.length() <= 16);
    }

    @Test
    void generatePasswordTestForSpecifiedLength() {
        int length = 10;

        String password = PasswordUtils.generatePassword(length);
        LOGGER.info("password: {}, length: {}.", password, password.length());
        assertEquals(length, password.length());
    }


    private static Stream<String> validPasswordProvider() {
        return Stream.of(
                "Abc@2025",
                "2025~xyZ",
                "(2020#Abc",
                " Abc2025",
                "~!@#$%Aa0",
                "2020$123$abc$ABC",
                "Aa0`2025@2026^Xy");
    }

    private static Stream<String> inValidPasswordProvider() {
        return Stream.of(
                null,
                "",
                "2025~xZ",
                "2025~xZ12ab34CD56",
                "(2020#abc",
                "(2020#ABC",
                "2020Abcd");
    }

    @ParameterizedTest(name = "#{index} - Run test with isValid = {0}")
    @MethodSource("validPasswordProvider")
    void test_isValid_input_valid(String input) {
        assertTrue(PasswordUtils.isValid(input));
    }

    @ParameterizedTest(name = "#{index} - Run test with isValid = {0}")
    @MethodSource("inValidPasswordProvider")
    void test_isValid_input_inValid(String input) {
        assertFalse(PasswordUtils.isValid(input));
    }
}