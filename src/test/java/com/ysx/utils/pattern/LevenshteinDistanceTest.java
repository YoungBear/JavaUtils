package com.ysx.utils.pattern;

import org.junit.jupiter.api.Test;


import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-04-03 22:42
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class LevenshteinDistanceTest {

    @Test
    public void test() {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        int distance = levenshteinDistance.apply("kitten", "sitting");
        System.out.println("Levenshtein Distance: " + distance);
    }
}
