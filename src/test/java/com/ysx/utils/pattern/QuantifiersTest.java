package com.ysx.utils.pattern;

import org.junit.jupiter.api.Test;

import static com.ysx.utils.pattern.Common.runTest;
import static com.ysx.utils.pattern.Common.runTestBaeldung;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-02-28 22:10
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 量词
 * Greedy 贪婪
 * Reluctant 勉强
 * Possessive 占有
 */
public class QuantifiersTest {

    @Test
    public void test1() {

    }

    @Test
    // I found the text "xfooxxxxxxfoo" starting at index 0 and ending at index 13.
    public void test_Greedy1() {
        runTest(".*foo", "xfooxxxxxxfoo");
    }

    @Test
    // I found the text "xfoo" starting at index 0 and ending at index 4.
    // I found the text "xxxxxxfoo" starting at index 4 and ending at index 13.
    public void test_Reluctant1() {
        runTest(".*?foo", "xfooxxxxxxfoo");
    }

    @Test
    // No match found.
    public void test_Possessive1() {
        runTest(".*+foo", "xfooxxxxxxfoo");
    }
}
