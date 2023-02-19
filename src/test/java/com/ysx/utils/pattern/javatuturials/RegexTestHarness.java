package com.ysx.utils.pattern.javatuturials;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-03-05 9:58
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 测试公共方法
 */
public class RegexTestHarness {

    /**
     * 正则表达式和input匹配情况
     *
     * @param regex 正则表达式
     * @param input 输入字符串
     */
    public static void runTest(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        boolean found = false;
        while (matcher.find()) {
            String format = String.format("I found the text" + " \"%s\" starting at " + "index %d and ending at index %d.",
                    matcher.group(), matcher.start(), matcher.end());
            System.out.println(format);
            found = true;
        }
        if (!found) {
            System.out.println("No match found.");
        }
    }
}
