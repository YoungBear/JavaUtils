# 密码工具类



## 生成随机密码



符合密码强度的密码要求：

- 至少有一个大写字母
- 至少有一个小写字母
- 至少有一个数字
- 至少有一个特殊字符
- 长度满足要求（通常为8-16位）



```java
    // 大写字母
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // 小写字母
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    // 数字
    private static final String DIGITS = "0123456789";
    // 特殊字符(支持空格-下发密码明文时需要关注空格)
    private static final String SPECIAL_CHARS = "`~!@#$%^&*()-_=+[]{}\\|;:'\",.<>/? ";

    // 最小长度
    private static final int MIN_LENGTH = 8;

    // 最大长度
    private static final int MAX_LENGTH = 16;

    private static final SecureRandom random = new SecureRandom();
    /**
     * 生成随机密码
     * 满足强度要求
     * 长度随机
     *
     * @return 生成的密码
     */
    public static String generatePassword() {
        int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH);
        return generatePassword(length);
    }

    /**
     * 生成符合强密码要求的随机密码
     *
     * @param length 密码长度
     * @return 生成的密码
     * @throws IllegalArgumentException 长度不满足要求
     */
    public static String generatePassword(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new IllegalArgumentException("invalid password length.");
        }

        List<Character> passwordChars = new ArrayList<>();

        // 确保每类字符至少出现一次
        addRandomChar(passwordChars, UPPERCASE);
        addRandomChar(passwordChars, LOWERCASE);
        addRandomChar(passwordChars, DIGITS);
        addRandomChar(passwordChars, SPECIAL_CHARS);

        // 填充剩余字符
        String allChars = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARS;
        for (int i = 4; i < length; i++) {
            addRandomChar(passwordChars, allChars);
        }

        // 打乱字符顺序
        Collections.shuffle(passwordChars, random);

        // 构建密码字符串
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }

    /**
     * 从指定字符串中随机选取一个字符并添加到密码列表中
     */
    private static void addRandomChar(List<Character> list, String charSet) {
        int index = random.nextInt(charSet.length());
        list.add(charSet.charAt(index));
    }
```





## 校验密码强度有效性



```java
    // "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\Q`~!@#$%^&*()-_=+[]{}\\|;:'\",.<>/? \\E]).{8,16}$"
    private static final Pattern PATTERN_VALID_PASSWORD = Pattern.compile("^" +
            "(?=.*[A-Z])" +
            "(?=.*[a-z])" +
            "(?=.*\\d)" +
            "(?=.*[" + Pattern.quote(SPECIAL_CHARS) + "])" +
            ".{" + MIN_LENGTH + "," + MAX_LENGTH + "}"
            + "$");

    /**
     * 判断密码强度是否满足要求
     * 长度为8-16位(可按照实际修改)
     * 必须包含大写字母，小写字母，数字，特殊字符
     *
     * @param input 带验证的密码明文
     * @return 是否满足强度要求
     */
    public static boolean isValid(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return PATTERN_VALID_PASSWORD.matcher(input).matches();
    }
```



## 单元测试



```java
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
```



## 源代码地址

- [github](https://github.com/YoungBear/JavaUtils)
- [gitee](https://gitee.com/YoungBear2023/JavaUtils)
