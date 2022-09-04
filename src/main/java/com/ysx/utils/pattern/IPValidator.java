package com.ysx.utils.pattern;

import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-09-04 17:54
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description IP校验
 */
public class IPValidator {

    private static final String IP_V4 = "^(((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$";
    // 下边这个正则也ok
    // private static final String IP_V4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
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
