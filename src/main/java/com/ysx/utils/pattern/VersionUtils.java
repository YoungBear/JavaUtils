package com.ysx.utils.pattern;

import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-11-20 22:20
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 版本号校验
 * 这里仅考虑三位版本号，即 主版本号.次版本号.修订号
 * MAJOR.MINOR.PATCH
 * 参考：<a href="https://semver.org/lang/zh-CN/">...</a>
 */
public class VersionUtils {

    private static final String PATTERN_STR = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)$";

    private static final Pattern VERSION_PATTERN = Pattern.compile(PATTERN_STR);

    /**
     * 是否是有效的版本号
     *
     * @param input 字符串
     * @return 是否是有效的版本号
     */
    public static boolean isValidVersion(String input) {
        if (input == null) {
            return false;
        }
        return VERSION_PATTERN.matcher(input).matches();
    }

    /**
     * 比较两个版本号大小
     * version1比version2大返回1
     * version1比version2小返回-1
     * version1与version2相同返回0
     *
     * @param version1 version1
     * @param version2 version2
     * @return 比较结果
     */
    public static int compare(String version1, String version2) {
        if (!isValidVersion(version1) || !isValidVersion(version2)) {
            throw new RuntimeException("invalid version.");
        }
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Codes = version1.split("\\.");
        String[] version2Codes = version2.split("\\.");
        for (int i = 0; i < version1Codes.length; i++) {
            int code1 = Integer.parseInt(version1Codes[i]);
            int code2 = Integer.parseInt(version2Codes[i]);
            if (code1 > code2) {
                return 1;
            } else if (code1 < code2) {
                return -1;
            }
        }
        return 0;
    }

}
