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

    /**
     * 计算核心线程数
     *
     * @return core pool count
     */
    public static int calculateCorePoolCount() {
        return calculateCorePoolCount(Runtime.getRuntime().availableProcessors());
    }

    /**
     * 计算核心线程数
     *
     * @param cpuCoreCount CPU核心数
     * @return core pool count
     */
    static int calculateCorePoolCount(int cpuCoreCount) {
        return Math.min(cpuCoreCount, 10);
    }
}
