package com.ysx.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2020/3/1 17:24
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class HelloJavaUtilsTest {

    private final HelloJavaUtils helloJavaUtils = new HelloJavaUtils();

    @Test
    @DisplayName("hello test")
    public void helloTest() {
        Assertions.assertEquals("hello", helloJavaUtils.hello());
    }
}
