package com.ysx.utils.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.SmartNullPointerException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-22 23:30
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
class ClassBTest {

    @Test
    @DisplayName("不指定Answers")
    void defaultAnswerTest() {
        ClassA mockA = mock(ClassA.class);
        doReturn("method1_X").when(mockA).method1();
        ClassB classB = new ClassB(mockA);
        // 打桩的函数返回打桩的值
        assertEquals("method1_X", classB.method1());
        // 未打桩的函数，返回默认值：String默认为null，int默认为0，集合默认为空集合
        assertNull(classB.method2());
        assertNull(classB.getName());
        assertEquals(0, classB.getAge());
        assertEquals(0, classB.getList().size());
        assertNull(classB.getClassC());
    }

    @Test
    @DisplayName("指定默认Answers：Mockito.RETURNS_DEFAULTS")
    void useDefaultAnswerTest() {
        ClassA mockA = mock(ClassA.class, Mockito.RETURNS_DEFAULTS);
        doReturn("method1_X").when(mockA).method1();
        ClassB classB = new ClassB(mockA);
        // 打桩的函数返回打桩的值
        assertEquals("method1_X", classB.method1());
        // 未打桩的函数，返回默认值：String默认为null，int默认为0，集合默认为空集合
        assertNull(classB.method2());
        assertNull(classB.getName());
        assertEquals(0, classB.getAge());
        assertEquals(0, classB.getList().size());
        assertNull(classB.getClassC());
    }

    @Test
    @DisplayName("指定智能空值Answers：Mockito.RETURNS_SMART_NULLS")
    void useReturnsSmartNullsAnswerTest() {
        ClassA mockA = mock(ClassA.class, Mockito.RETURNS_SMART_NULLS);
        doReturn("method1_X").when(mockA).method1();
        ClassB classB = new ClassB(mockA);
        // 打桩的函数返回打桩的值
        assertEquals("method1_X", classB.method1());
        // 未打桩的函数，返回smart null：String默认为空字符串，int默认为0，集合默认为空集合
        assertEquals("", classB.method2());
        assertEquals("", classB.getName());
        assertEquals(0, classB.getAge());
        assertEquals(0, classB.getList().size());
        assertThrows(SmartNullPointerException.class, () -> classB.getClassC().getName());
        assertThrows(SmartNullPointerException.class, () -> classB.getClassC().getAge());
    }

    @Test
    @DisplayName("指定Answers：Mockito.RETURNS_MOCKS")
    void useReturnsMocksAnswerTest() {
        ClassA mockA = mock(ClassA.class, Mockito.RETURNS_MOCKS);
        doReturn("method1_X").when(mockA).method1();
        ClassB classB = new ClassB(mockA);
        // 打桩的函数返回打桩的值
        assertEquals("method1_X", classB.method1());
        // 未打桩的函数，返回mock对象
        assertEquals("", classB.method2());
        assertEquals("", classB.getName());
        assertEquals(0, classB.getAge());
        assertEquals(0, classB.getList().size());
        assertNotNull(classB.getClassC());
        assertEquals("", classB.getClassC().getName());
        assertEquals(0, classB.getClassC().getAge());
    }

    @Test
    @DisplayName("指定Answers：Mockito.RETURNS_DEEP_STUBS")
    void useReturnsDeepStubsAnswerTest() {
        ClassA mockA = mock(ClassA.class, Mockito.RETURNS_DEEP_STUBS);
        // 在创建模拟（mock）对象时，自动模拟（stub）该对象及其返回的任何嵌套对象
        when(mockA.getClassC().getName()).thenReturn("Name1");
        ClassB classB = new ClassB(mockA);
        // 打桩的函数返回打桩的值
        assertEquals("Name1", classB.getClassC().getName());
    }

    @Test
    @DisplayName("指定Answers：Mockito.CALLS_REAL_METHODS")
    void useCallRealMethodsAnswerTest() {
        ClassA mockA = mock(ClassA.class, Mockito.CALLS_REAL_METHODS);
        doReturn("method1_X").when(mockA).method1();
        ClassB classB = new ClassB(mockA);
        // 打桩的函数返回打桩的值
        assertEquals("method1_X", classB.method1());
        // 未打桩的函数，调用真实方法
        assertEquals("method2", classB.method2());
        assertNull(classB.getName());
        assertEquals(0, classB.getAge());
        assertNull(classB.getList());
        assertNull(classB.getClassC());
    }

    @Test
    @DisplayName("使用spy,类似于：Mockito.CALLS_REAL_METHODS")
    void useSpyTest() {
        ClassA mockA = spy(ClassA.class);
        doReturn("method1_X").when(mockA).method1();
        ClassB classB = new ClassB(mockA);
        // 打桩的函数返回打桩的值
        assertEquals("method1_X", classB.method1());
        // 未打桩的函数，调用真实方法
        assertEquals("method2", classB.method2());
        assertNull(classB.getName());
        assertEquals(0, classB.getAge());
        assertNull(classB.getList());
        assertNull(classB.getClassC());
    }
}