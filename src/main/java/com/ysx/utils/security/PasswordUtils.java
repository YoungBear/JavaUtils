package com.ysx.utils.security;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-05-06 18:27
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 密码工具类
 */
public class PasswordUtils {

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

    // "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\Q`~!@#$%^&*()-_=+[]{}\\|;:'\",.<>/? \\E]).{8,16}$"
    private static final Pattern PATTERN_VALID_PASSWORD = Pattern.compile("^" +
            "(?=.*[A-Z])" +
            "(?=.*[a-z])" +
            "(?=.*\\d)" +
            "(?=.*[" + Pattern.quote(SPECIAL_CHARS) + "])" +
            ".{" + MIN_LENGTH + "," + MAX_LENGTH + "}"
            + "$");

    private static final SecureRandom random = new SecureRandom();

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
}
