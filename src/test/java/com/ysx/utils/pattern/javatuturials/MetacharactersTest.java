package com.ysx.utils.pattern.javatuturials;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-03-05 10:36
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class MetacharactersTest {
    @Test
    public void metacharacterTest() {
        String regex = "cat.";
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("cats").matches());
        assertTrue(pattern.matcher("cata").matches());
    }

    @Test
    public void escapingMetacharacter_using_backslash_Test() {
        String regex = "cat\\.";
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("cat.").matches());
        assertFalse(pattern.matcher("cata").matches());
    }


    @Test
    public void escapingMetacharacter_using_QE_Test() {
        String regex = "\\Qcat.\\E";
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("cat.").matches());
        assertFalse(pattern.matcher("cat").matches());
    }

    @Test
    public void escapingMetacharacter_using_flag_Test() {
        String regex = "cat.";
        Pattern pattern = Pattern.compile(regex, Pattern.LITERAL);
        assertTrue(pattern.matcher("cat.").matches());
        assertFalse(pattern.matcher("cata").matches());
    }

    @Test
    public void escapingMetacharacter_using_quote_Test() {
        // \Qcat.\E
        String regex = Pattern.quote("cat.");
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("cat.").matches());
        assertFalse(pattern.matcher("cata").matches());
    }
}
