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
