package com.ysx.utils.pattern.javatuturials;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-03-07 7:42
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 捕获组
 */
public class CapturingGroupTest {
    @Test
    public void capturing_group_test() {
        Pattern pattern = Pattern.compile("((A)(B(C)))");
        String input = "ABC";
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            Assertions.assertEquals("ABC", matcher.group(0));
            Assertions.assertEquals("ABC", matcher.group(1));
            Assertions.assertEquals("A", matcher.group(2));
            Assertions.assertEquals("BC", matcher.group(3));
            Assertions.assertEquals("C", matcher.group(4));
        }
    }

    @Test
    public void named_capturing_group_test() {
        Pattern pattern = Pattern.compile("(?<name1>(<?name2>A)(<?name3>B(<?name4>C)))");
        String input = "ABC";
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            Assertions.assertEquals("ABC", matcher.group("name1"));
            Assertions.assertEquals("A", matcher.group("name2"));
            Assertions.assertEquals("BC", matcher.group("name3"));
            Assertions.assertEquals("C", matcher.group("name4"));
        }
    }

    @Test
    public void non_capturing_group_test() {
        Pattern pattern = Pattern.compile("(ABC)+");
        String input = "ABCABC";
        Matcher matcher = pattern.matcher(input);
        Assertions.assertTrue(matcher.matches());
        Assertions.assertEquals(1, matcher.groupCount());

        pattern = Pattern.compile("(?:ABC)+");
        matcher = pattern.matcher(input);
        Assertions.assertTrue(matcher.matches());
        Assertions.assertEquals(0, matcher.groupCount());
    }

    @Test
    public void non_capturing_group_test_flags() {
        Pattern pattern = Pattern.compile("(abc)(ABC)+");
        String input = "abcabcabc";
        Matcher matcher = pattern.matcher(input);
        Assertions.assertFalse(matcher.matches());

        pattern = Pattern.compile("(abc)(?i:ABC)+");
        matcher = pattern.matcher(input);
        Assertions.assertTrue(matcher.matches());
    }

    @Test
    public void non_capturing_group_test_flags_negation() {
        Pattern pattern = Pattern.compile("(abc)(ABC)+", Pattern.CASE_INSENSITIVE);
        String input = "ABCabcabc";
        Matcher matcher = pattern.matcher(input);
        Assertions.assertTrue(matcher.matches());

        pattern = Pattern.compile("(abc)(?-i:ABC)+", Pattern.CASE_INSENSITIVE);
        Assertions.assertFalse(pattern.matcher("ABCabcabc").matches());
        Assertions.assertTrue(pattern.matcher("ABCABCABC").matches());
    }
}
