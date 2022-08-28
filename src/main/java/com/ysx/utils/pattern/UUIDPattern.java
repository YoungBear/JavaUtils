package com.ysx.utils.pattern;

import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-08-28 12:10
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class UUIDPattern {

    private static final Pattern UUID_PATTERN = Pattern.compile(PatternConstant.UUID);

    private static final Pattern UUID_WITH_NO_HYPHEN_PATTERN = Pattern.compile(PatternConstant.UUID_WITH_NO_HYPHEN);


    /**
     * 判断是否是有效的UUID（忽略大小写）
     *
     * @param input 入参字符串
     * @return 是否是有效的UUID
     */
    public static boolean isValidUUID(String input) {
        if (input == null) {
            return false;
        }
        return UUID_PATTERN.matcher(input.toLowerCase(Locale.ROOT)).matches();
    }

    /**
     * 判断是否是有效的32位UUID（忽略大小写）
     *
     * @param input 入参字符串
     * @return 是否是有效的32位UUID
     */
    public static boolean isValidUUID32(String input) {
        if (input == null) {
            return false;
        }
        return UUID_WITH_NO_HYPHEN_PATTERN.matcher(input.toLowerCase(Locale.ROOT)).matches();
    }

    /**
     * 生成36位UUID
     *
     * @return UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取32位UUID
     *
     * @return UUID
     */
    public static String uuid32() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
