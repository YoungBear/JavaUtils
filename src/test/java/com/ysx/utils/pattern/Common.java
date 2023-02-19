package com.ysx.utils.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-02-28 22:10
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class Common {

    public static int runTestBaeldung(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int matches = 0;
        while (matcher.find()) {
            matches++;
        }
        return matches;
    }

    public static void runTest(String regex, String input) {
        Pattern pattern =
                Pattern.compile(regex);

        Matcher matcher =
                pattern.matcher(input);

        boolean found = false;
        while (matcher.find()) {
            String format = String.format("I found the text" +
                            " \"%s\" starting at " +
                            "index %d and ending at index %d.",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
            System.out.println(format);
            found = true;
        }
        if (!found) {
            System.out.println("No match found.");
        }
    }
}
