package com.ysx.utils.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-02-19 23:19
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 参考：https://docs.oracle.com/javase/tutorial/essential/regex/test_harness.html
 */
public class RegexTestHarness {

    public static void main(String[] args) {
        String regex = "(\\d\\d)";
        Pattern pattern = Pattern.compile(regex);
        String input = "121212";

        Matcher matcher = pattern.matcher(input);
        System.out.println("groupCount: " + matcher.groupCount());
        boolean found = false;
        while (matcher.find()) {
            String output = String.format("I found the text" +
                            " \"%s\" starting at " +
                            "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
            System.out.println(output);
            found = true;
        }
        if (!found) {
            System.out.println("No match found.%n");
        }
    }
}
