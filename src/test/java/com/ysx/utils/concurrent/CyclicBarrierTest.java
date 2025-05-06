package com.ysx.utils.concurrent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-04-12 10:07
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description CyclicBarrier
 * CyclicBarrier 是 Java 并发包（java.util.concurrent）中的一个同步辅助类。它允许一组线程相互等待，直到到达某个公共屏障点（common barrier point）。
 * 只有当所有参与的线程都到达屏障点时，这些线程才能继续执行。这与CountDownLatch不同，后者是一次性的，而CyclicBarrier可以在等待的线程被释放后重新使用。
 * 使用场景：多线程任务中，让多个线程在某个特定的点上同步，确保完成所有线程都完成某个阶段后，再一起进入下一阶段。
 * 比如：一个计算任务被分成了多个部分，每个部分由不同的线程进行处理，处理完成后需要汇总结果，这时候需要所有线程都完成计算后，才能进行汇总
 * <p>
 * 基本用法：
 * 创建 CyclicBarrier：
 * 使用 new CyclicBarrier(int parties) 创建一个新的 CyclicBarrier，其中 parties 参数指定了必须调用 await() 方法以使所有线程都能通过屏障的线程数量。
 * 也可以提供第二个参数，这是一个 Runnable，当最后一个线程到达屏障时会执行这个Runnable，这可以用于在所有线程到达屏障之前执行一些特定的操作。
 * <p>
 * 线程中使用 await() 方法：
 * 每个线程在需要等待其他线程的地方调用 await() 方法。该方法会挂起当前线程，直到所有线程都调用了 await() 方法。
 * 如果当前线程不是最后一个到达屏障的线程，await() 将阻塞直到所有线程都到达。如果当前线程是最后一个到达的，则所有线程都会被唤醒继续执行。
 * <p>
 * 重置屏障：
 * 可以通过调用 reset() 方法来重置 CyclicBarrier，这将导致所有等待的线程收到一个 BrokenBarrierException 异常，并且屏障计数会被重置为其初始状态。
 */
public class CyclicBarrierTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicBarrierTest.class);

    // 使用map模拟所有的数据，每个线程占据一个key
    private final Map<Integer, Integer> countMap = new ConcurrentHashMap<>();

    // 核心线程数
    private static final int CORE_POOL_SIZE = 5;

    // 线程池
    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, // 核心线程数（长期保持的线程数）
            CORE_POOL_SIZE * 2, // 最大线程数（允许同时运行的最大线程数）
            10, // 非核心线程空闲时的存活时间
            TimeUnit.SECONDS, // 时间单位（如 TimeUnit.SECONDS）
            new LinkedBlockingDeque<>(1000), // 任务队列（任务等待执行的队列）
            Executors.defaultThreadFactory(), // 线程工厂（可自定义线程名称等）
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略（任务无法执行时的处理方式）
    );

    /**
     * 使用一个休眠函数，模拟实际业务的计算，并且等待其他线程的结束
     *
     * @param cyclicBarrier 屏障独显
     * @param index 数据的key，这里用index表示
     */
    private void calculate(CyclicBarrier cyclicBarrier, int index) {
        SecureRandom secureRandom = new SecureRandom();
        // 随机休眠时间，并作为数据值
        int randomSleepSeconds = secureRandom.nextInt(10);
        LOGGER.info("<{}> index: {}, randomSleepSeconds: {}.", Thread.currentThread().getName(), index, randomSleepSeconds);

        // 休眠，模拟线程计算耗时
        try {
            Thread.sleep(randomSleepSeconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 记录计算结果
        countMap.put(index, randomSleepSeconds);

        // 等待其他线程到达这个点
        LOGGER.info("<{}> index: {}, waiting other thread.", Thread.currentThread().getName(), index);
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        // 通过了这个屏障
        LOGGER.info("<{}> index: {}, has passed barrier.", Thread.currentThread().getName(), index);

    }

    @Test
    @DisplayName("基本用法：任务数量不超过线程池的核心线程数")
    public void test1() {
        int taskNum = 3;
        int threadNum = Math.min(CORE_POOL_SIZE, taskNum);
        // 这里 CyclicBarrier 的构造函数的参数 parties 表示需要拦截的线程的数量，示例中有3个子线程，1个主线程
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum + 1);
        for (int i = 0; i < threadNum; i++) {
            final int index = i;
            THREAD_POOL_EXECUTOR.execute(() -> calculate(cyclicBarrier, index));
        }
        try {
            // 等待所有子线程任务执行完成，超时时间为1分钟
            LOGGER.info("waiting all thread.");
            cyclicBarrier.await(1, TimeUnit.MINUTES);

            LOGGER.info("<{}> has passed barrier.", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("finish calculate.");
        int sum = countMap.values().stream().mapToInt(Integer::intValue).sum();
        LOGGER.info("sum: {}.", sum);
    }

    @Test
    @DisplayName("构造函数Runnable的用法：该Runnable优先级高")
    public void test2() {
        int taskNum = 3;
        int threadNum = Math.min(CORE_POOL_SIZE, taskNum);
        // 这里 CyclicBarrier 的构造函数的参数 parties 表示需要拦截的线程的数量，示例中有3个子线程，1个主线程
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum + 1, new Runnable() {
            @Override
            public void run() {
                LOGGER.info("<{}> is first priority.", Thread.currentThread().getName());
            }
        });
        for (int i = 0; i < threadNum; i++) {
            final int index = i;
            THREAD_POOL_EXECUTOR.execute(() -> calculate(cyclicBarrier, index));
        }
        try {
            // 等待所有子线程任务执行完成，超时时间为1分钟
            LOGGER.info("waiting all thread.");
            cyclicBarrier.await(1, TimeUnit.MINUTES);

            LOGGER.info("<{}> has passed barrier.", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("finish calculate.");
        int sum = countMap.values().stream().mapToInt(Integer::intValue).sum();
        LOGGER.info("sum: {}.", sum);
    }

    @Test
    @DisplayName("通用用法：任务数量存在超过线程池的核心线程数的场景，需要分批处理")
    public void test3() {
        int taskNum = 13;
        // 计算分批计算的批次：任务数/核心线程数，并向上取整
        int batchNum = (int) Math.ceil((double) taskNum / CORE_POOL_SIZE);

        for (int batchIndex = 0; batchIndex < batchNum; batchIndex++) {
            int threadNum = CORE_POOL_SIZE;
            if (batchIndex == batchNum - 1) {
                // 如果是最后一批，需要以实际剩余的任务数作为线程数
                threadNum = taskNum - batchIndex * CORE_POOL_SIZE;
            }
            LOGGER.info("batch calculate, batchIndex: {}, threadNum: {}.", batchIndex, threadNum);
            // 这里 CyclicBarrier 的构造函数的参数 parties 表示需要拦截的线程的数量，这里有threadNum个子线程，1个主线程
            CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum + 1);
            for (int threadIndex = 0; threadIndex < threadNum; threadIndex++) {
                // 当前数据的index需要单独计数
                final int index = threadIndex + batchIndex * CORE_POOL_SIZE;
                THREAD_POOL_EXECUTOR.execute(() -> calculate(cyclicBarrier, index));
            }
            try {
                // 当前批次，等待所有子线程任务执行完成，超时时间为1分钟
                LOGGER.info("waiting all thread in this batch, batchIndex: {}.", batchIndex);
                cyclicBarrier.await(1, TimeUnit.MINUTES);

                LOGGER.info("<{}> has passed barrier in this batch, batchIndex: {}.", Thread.currentThread().getName(), batchIndex);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }

        LOGGER.info("finish calculate.");
        int sum = countMap.values().stream().mapToInt(Integer::intValue).sum();
        LOGGER.info("sum: {}.", sum);
    }

}
