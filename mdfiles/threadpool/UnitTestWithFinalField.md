# final字段单元测试

## 背景

通常在配置线程池时，需要获取当前JVM的CPU数量，并和用户的配置（如10）取最小值，作为核心线程数，即用如下的方法:

```java
    // 获取虚拟机（JVM）运行时环境下的可用 CPU 核心数量
    private static final int CPU_CORE_COUNT = Runtime.getRuntime().availableProcessors();


    /**
     * 计算核心线程数
     *
     * @return core pool count
     */
    public static int calculateCorePoolCount() {
        return Math.min(CPU_CORE_COUNT, 10);
    }
```

这种情况下，用于CPU_CORE_COUNT是在final字段定义的，所以无法进行mock，所以需要通过设置final字段的值来进行单元测试。

## 解决方法

在单元测试方法中修改final字段的值（即用来mock不同的数据），达到单元测试的效果。参考代码如下：

```java
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

```



