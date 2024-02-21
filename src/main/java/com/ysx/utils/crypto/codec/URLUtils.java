package com.ysx.utils.crypto.codec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-02-21 23:20
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class URLUtils {
    /**
     * 编码
     *
     * @param dataString 待编码字符串
     * @return 编码后字符串
     */
    public static String encode(String dataString) throws UnsupportedEncodingException {
        return URLEncoder.encode(dataString, StandardCharsets.UTF_8.toString());
    }

    /**
     * 解码
     *
     * @param codeString 待解码字符串
     * @return 解码后的字符串
     */
    public static String decode(String codeString) throws UnsupportedEncodingException {
        return URLDecoder.decode(codeString, StandardCharsets.UTF_8.toString());
    }
}
