package com.ysx.utils.threadpool;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
    public void testCalculateCorePoolCount_whenCpuCoreCountIsLessThan10() throws Exception {
        setCpuCoreCount(4);
        int result = ThreadPoolConfig.calculateCorePoolCount();
        assertEquals(4, result);
    }

    @Test
    public void testCalculateCorePoolCount_whenCpuCoreCountIsGreaterThan10() throws Exception {
        setCpuCoreCount(16);
        int result = ThreadPoolConfig.calculateCorePoolCount();
        assertEquals(10, result);
    }

    @Test
    public void testCalculateCorePoolCount_whenCpuCoreCountIs10() throws Exception {
        setCpuCoreCount(10);
        int result = ThreadPoolConfig.calculateCorePoolCount();
        assertEquals(10, result);
    }


    /**
     * 通过反射设置final字段的值
     *
     * @param value 待设置的值
     * @throws Exception 异常
     */
    private void setCpuCoreCount(int value) throws Exception {
        Field field = ThreadPoolConfig.class.getDeclaredField("CPU_CORE_COUNT");
        field.setAccessible(true);

        // 移除final修饰符
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, value);
    }


}
