package com.ysx.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2020/3/1 17:24
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class HelloJavaUtilsTest {

    private HelloJavaUtils helloJavaUtils = new HelloJavaUtils();

    @Test
    public void helloTest() {
        Assert.assertEquals("hello", helloJavaUtils.hello());
    }
}
