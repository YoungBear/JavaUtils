package com.ysx.utils.file;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2020/3/1 17:41
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 文件操作异常
 */
public class FileException extends Exception {

    public FileException() {
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
