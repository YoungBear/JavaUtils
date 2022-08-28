package com.ysx.utils.pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-08-28 11:59
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 常用正则表达式
 */
public interface PatternConstant {

    /**
     * 带中划线的UUID共36位
     */
    String UUID = "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}";

    /**
     * 不带中划线的UUDI共32位
     */
    String UUID_WITH_NO_HYPHEN = "[0-9a-f]{32}";

}
