# Java UUID 正则表达式

UUID，即通用唯一识别码（Universally Unique Identifier）。

UUID的介绍(来自[百度百科](<https://baike.baidu.com/item/UUID/5921266?fr=aladdin>))

UUID是指在一台机器上生成的数字，它保证对在同一时空中的所有机器都是唯一的。

UUID由以下几部分的组合：

（1）当前日期和时间，UUID的第一个部分与时间有关，如果你在生成一个UUID之后，过几秒又生成一个UUID，则第一个部分不同，其余相同。

（2）时钟序列。

（3）全局唯一的IEEE机器识别号，如果有网卡，从网卡MAC地址获得，没有网卡以其他方式获得。

**标准的UUID的格式：**

`xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` (8-4-4-4-12)

其中每个x表示一个16进制的数字(小写)。即共32的16进制的数字，使用分隔符，分割成8-4-4-4-12五部分。

用正则表达式表示为：

```java
/**
 * 带中划线的UUID共36位
 */
String UUID = "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}";

/**
 * 不带中划线的UUDI共32位
 */
String UUID_WITH_NO_HYPHEN = "[0-9a-f]{32}";
```

对应可以编写Java 工具类：

```java
package com.ysx.utils.pattern;

import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-08-28 12:10
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class UUIDPattern {

    private static final Pattern UUID_PATTERN = Pattern.compile(PatternConstant.UUID);

    private static final Pattern UUID_WITH_NO_HYPHEN_PATTERN = Pattern.compile(PatternConstant.UUID_WITH_NO_HYPHEN);


    /**
     * 判断是否是有效的UUID（忽略大小写）
     *
     * @param input 入参字符串
     * @return 是否是有效的UUID
     */
    public static boolean isValidUUID(String input) {
        if (input == null) {
            return false;
        }
        return UUID_PATTERN.matcher(input.toLowerCase(Locale.ROOT)).matches();
    }

    /**
     * 判断是否是有效的32位UUID（忽略大小写）
     *
     * @param input 入参字符串
     * @return 是否是有效的32位UUID
     */
    public static boolean isValidUUID32(String input) {
        if (input == null) {
            return false;
        }
        return UUID_WITH_NO_HYPHEN_PATTERN.matcher(input.toLowerCase(Locale.ROOT)).matches();
    }

    /**
     * 生成36位UUID
     *
     * @return UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取32位UUID
     *
     * @return UUID
     */
    public static String uuid32() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
```



## 源代码地址

- [github](https://github.com/YoungBear/JavaUtils)
- [gitee](https://gitee.com/YoungBear2023/JavaUtils)

