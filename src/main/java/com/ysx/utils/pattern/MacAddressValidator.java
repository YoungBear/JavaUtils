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
