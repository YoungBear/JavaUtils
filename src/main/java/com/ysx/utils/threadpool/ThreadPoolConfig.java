package com.ysx.utils.threadpool;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-10-01 7:51
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description 线程池配置
 */
public class ThreadPoolConfig {
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
}
