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
