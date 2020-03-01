package com.ysx.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2020/3/1 17:23
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 测试代码
 */
public class HelloJavaUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloJavaUtils.class);

    public String hello() {
        LOGGER.info("hello");
        return "hello";
    }
}
