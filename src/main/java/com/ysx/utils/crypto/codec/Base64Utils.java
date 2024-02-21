package com.ysx.utils.crypto.codec;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-02-08 7:40
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description Base64编码解码
 */
public class Base64Utils {

    /**
     * 编码
     *
     * @param dataString 待编码字符串
     * @return 编码后字符串
     */
    public static String encode(String dataString) {
        return new String(Base64.getEncoder().encode(dataString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    /**
     * 解码
     *
     * @param codeString 待解码字符串
     * @return 解码后的字符串
     */
    public static String decode(String codeString) {
        return new String(Base64.getDecoder().decode(codeString), StandardCharsets.UTF_8);
    }
}
