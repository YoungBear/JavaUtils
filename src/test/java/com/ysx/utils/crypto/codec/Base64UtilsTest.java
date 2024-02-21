package com.ysx.utils.crypto.codec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-02-08 7:46
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description unit test for {@link Base64Utils}
 */
class Base64UtilsTest {

    @Test
    @DisplayName("base64 编码")
    void encodeTest() {
        String text = "Hello@123";
        String excepted = "SGVsbG9AMTIz";
        assertEquals(excepted, Base64Utils.encode(text));
    }

    @Test
    @DisplayName("base64 解码")
    void decodeTest() {
        String text = "SGVsbG9AMTIz";
        String excepted = "Hello@123";
        assertEquals(excepted, Base64Utils.decode(text));
    }
}