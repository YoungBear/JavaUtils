package com.ysx.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-08-28 12:10
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 自定义日志字段
 * MDC：参考 <a href="https://logback.qos.ch/manual/mdc.html">...</a>
 * 在logback.xml中使用%X{remoteIp}引用
 */
public class SimpleMDC {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMDC.class);
    private static final String MDC_KEY_REMOTE_IP = "remoteIp";

    public static void main(String[] args) {
        LOGGER.info("first log: before set mdc.");

        MDC.put(MDC_KEY_REMOTE_IP, "192.168.0.12");
        LOGGER.info("second log: after set mdc.");

        MDC.put(MDC_KEY_REMOTE_IP, "192.168.0.234");
        LOGGER.info("third log: after set mdc2.");

        MDC.remove(MDC_KEY_REMOTE_IP);
        LOGGER.info("fourth log: after remove mdc key.");

    }
}
