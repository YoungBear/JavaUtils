package com.ysx.utils.crypto.aes;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/8/8 18:20
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description AES加解密异常
 */
public class AESException extends Exception {
    public AESException() {
    }

    public AESException(String message) {
        super(message);
    }

    public AESException(String message, Throwable cause) {
        super(message, cause);
    }

    public AESException(Throwable cause) {
        super(cause);
    }

    public AESException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
