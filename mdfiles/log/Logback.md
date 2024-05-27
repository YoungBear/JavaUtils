# 普通 Maven 工程集成logback日志框架

## 1. 设置pom依赖

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.13</version>
</dependency>
```

## 2. 添加日志配置

显示在控制台，指定显示格式，并且指定存储在本地路径，并按照时间分割。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="false">

    <property name="LOG_HOME" value="/Users/youngbear/logs"/>
    <property name="PROJECT_NAME" value="LogPractise"/>
    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd'T'HH:mm:ss.SSS zXXX} [%thread] %-5level %logger{50}:%L - %msg%n</pattern>
        </encoder>
    </appender>
    <!-- 输出到文件 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- file 标签用来表示当前日志的文件，如果没有改标签的话，则使用FileNamePattern中的配置 -->
        <file>${LOG_HOME}/${PROJECT_NAME}/${PROJECT_NAME}.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/${PROJECT_NAME}/${PROJECT_NAME}.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>15</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd'T'HH:mm:ss.SSS zXXX} [%thread] %-5level %logger{50}:%L - %msg%n</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>50MB</MaxFileSize>
        </triggeringPolicy>
    </appender>

    <!-- 日志输出级别 默认DEBUG 不区分大小写-->
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

## 3. 写日志

```java
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
```



## 4. 自定义日志字段

参考：https://logback.qos.ch/manual/mdc.html

### 4.1 设置logback.xml

使用 `%{<字段名>}` 来表示打印自定义的字段，如： `%X{remoteIp}`

```
<pattern>%d{yyyy-MM-dd'T'HH:mm:ss.SSS zXXX} [%thread] %X{remoteIp} %-5level %logger{50}:%L - %msg%n</pattern>
```

### 4.2 调用MDC写入自定义字段的值

**tips：**

- SpringBoot工程可以使用拦截器写入，在线程开始时put，结束时remote。



java代码 main方法调用：

```java
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
```



日志打印效果：

```shell
2022-08-28T13:14:30.355 CST+08:00 [main]  INFO  com.ysx.utils.log.SimpleMDC:22 - first log: before set mdc.
2022-08-28T13:14:30.362 CST+08:00 [main] 192.168.0.12 INFO  com.ysx.utils.log.SimpleMDC:25 - second log: after set mdc.
2022-08-28T13:14:30.362 CST+08:00 [main] 192.168.0.234 INFO  com.ysx.utils.log.SimpleMDC:28 - third log: after set mdc2.
2022-08-28T13:14:30.362 CST+08:00 [main]  INFO  com.ysx.utils.log.SimpleMDC:31 - fourth log: after remove mdc key.
```



## [源代码地址](https://github.com/YoungBear/JavaUtils)

