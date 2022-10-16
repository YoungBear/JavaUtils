package com.ysx.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-08-28 12:10
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description HelloLog
 */
public class HelloLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloLog.class);

    public static void main(String[] args) {
        LOGGER.trace("trace log");
        LOGGER.debug("debug log");
        LOGGER.info("info log");
        LOGGER.warn("warn log");
        LOGGER.error("error log");

    }
}
