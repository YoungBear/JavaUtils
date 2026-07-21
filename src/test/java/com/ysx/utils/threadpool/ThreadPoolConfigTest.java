package com.ysx.utils.threadpool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-10-01 7:54
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description unit test for {@link ThreadPoolConfig}
 */
public class ThreadPoolConfigTest {

    @Test
    public void testCalculateCorePoolCount_whenCpuCoreCountIsLessThan10() {
        int result = ThreadPoolConfig.calculateCorePoolCount(4);
        assertEquals(4, result);
    }

    @Test
    public void testCalculateCorePoolCount_whenCpuCoreCountIsGreaterThan10() {
        int result = ThreadPoolConfig.calculateCorePoolCount(16);
        assertEquals(10, result);
    }

    @Test
    public void testCalculateCorePoolCount_whenCpuCoreCountIs10() {
        int result = ThreadPoolConfig.calculateCorePoolCount(10);
        assertEquals(10, result);
    }
}
