# Mockito 中的 Answer 参数

在使用Mockito中的mock方法时，有一个重载方法，支持传入Answer类型的参数，表示没有打桩的方法的处理策略(default answer for unstubbed methods)。常见的选项如下：

| 参数值              | 描述                                                         |
| ------------------- | ------------------------------------------------------------ |
| CALLS_REAL_METHODS  | 调用真实方法                                                 |
| RETURNS_DEEP_STUBS  | 允许深度模拟。即允许在mock对象中使用链式调用                 |
| RETURNS_DEFAULTS    | 默认值。表示返回默认值，如0，空集合，null等。                |
| RETURNS_MOCKS       | first tries to return ordinary values (zeros, empty collections, empty string, etc.) then it tries to return mocks. If the return type cannot be mocked (e.g. is final) then plain null is returned. |
| RETURNS_SELF        | 在build模式下返回本身的方法，直接返回自身。                  |
| RETURNS_SMART_NULLS | 智能null，不会报空指针                                       |



## CALLS_REAL_METHODS

参考代码：

```java
package com.ysx.utils.datetime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-21 23:27
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 日期工具类
 */
public class DateUtils {

    /**
     * 默认格式
     */
    private static final DateTimeFormatter YEAR_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取前一天的起始时间 00:00:00
     * 如 2024-07-21 00:00:00
     *
     * @return 格式化日期时间
     */
    public static String getLastDayStartTimePattern() {
        return LocalDate.now().minusDays(1).atTime(LocalTime.MIN).format(YEAR_DATE_TIME_FORMATTER);

    }
}

```



单元测试：

```Java
package com.ysx.utils.datetime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-21 23:34
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description unit test for {@link DateUtils}
 */
public class DateUtilsTest {

    @Test
    @DisplayName("不跨月 getLastDayStartTimePattern test")
    public void getLastDayStartTimePatternTest1() {
        LocalDate mockedNow = LocalDate.of(2024, 7, 21);
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(mockedNow);
            Assertions.assertEquals("2024-07-20 00:00:00", DateUtils.getLastDayStartTimePattern());
        }
    }

    @Test
    @DisplayName("跨月 getLastDayStartTimePattern test")
    public void getLastDayStartTimePatternTest2() {
        LocalDate mockedNow = LocalDate.of(2024, 7, 1);
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(mockedNow);
            Assertions.assertEquals("2024-06-30 00:00:00", DateUtils.getLastDayStartTimePattern());
        }

    }

    @Test
    @DisplayName("跨年 getLastDayStartTimePattern test")
    public void getLastDayStartTimePatternTest3() {
        LocalDate mockedNow = LocalDate.of(2024, 1, 1);
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(mockedNow);
            Assertions.assertEquals("2023-12-31 00:00:00", DateUtils.getLastDayStartTimePattern());
        }
    }
}

```



**说明：**

这里仅mock静态方法LocalDate::now，所以对于minusDays等其他非静态方法，会调用其真实方法。因为minusDays内部也会调用静态方法LocalDate::ofEpochDay，所以如果不加CALLS_REAL_METHODS参数的话，就会报空指针异常。



## 其他选项



ClassA:

```java
package com.ysx.utils.mockito;

import java.util.List;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-22 23:26
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class ClassA {

    private String name;

    private int age;

    private List<String> list;

    private ClassC classC;

    public String method1() {
        return "method1";
    }

    public String method2() {
        return "method2";
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<String> getList() {
        return list;
    }

    public ClassC getClassC() {
        return classC;
    }
}

```





ClassB:

```java
package com.ysx.utils.mockito;

import java.util.List;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-22 23:29
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class ClassB {
    private ClassA classA;

    public ClassB(ClassA classA) {
        this.classA = classA;
    }

    public String method1() {
        return classA.method1();
    }

    public String method2() {
        return classA.method2();
    }

    public String getName() {
        return classA.getName();
    }

    public int getAge() {
        return classA.getAge();
    }
    public List<String> getList() {
        return classA.getList();
    }
    public ClassC getClassC() {
        return classA.getClassC();
    }
}

```





ClassC:

```java
package com.ysx.utils.mockito;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-07-23 0:08
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
public class ClassC {
    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

```



验证程序

ClassBTest:

```java
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
```





## 参考

- [Mockito官方文档](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Mockito static_mocks](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#static_mocks)
- [CALLS_REAL_METHODS](https://javadoc.io/static/org.mockito/mockito-core/5.12.0/org/mockito/Mockito.html#CALLS_REAL_METHODS)



## 源代码

- [github-JavaUtils](https://github.com/YoungBear/JavaUtils)
- [gitee-JavaUtils](https://gitee.com/YoungBear2023/JavaUtils)

