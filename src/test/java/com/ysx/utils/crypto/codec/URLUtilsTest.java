package com.ysx.utils.crypto.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-02-21 23:40
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description unit test for {@link URLUtils}
 */
public class URLUtilsTest {

    @Test
    @DisplayName("URL 编码")
    void encodeTest() throws UnsupportedEncodingException {
        String text = "http://example.com/path?query=北京2008";
        String excepted = "http%3A%2F%2Fexample.com%2Fpath%3Fquery%3D%E5%8C%97%E4%BA%AC2008";
        assertEquals(excepted, URLUtils.encode(text));
    }

    @Test
    @DisplayName("URL 解码")
    void decodeTest() throws UnsupportedEncodingException {
        String text = "http%3A%2F%2Fexample.com%2Fpath%3Fquery%3D%E5%8C%97%E4%BA%AC2008";
        String excepted = "http://example.com/path?query=北京2008";
        assertEquals(excepted, URLUtils.decode(text));
    }
}
