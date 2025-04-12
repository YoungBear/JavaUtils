# CyclicBarrier 基本用法

## 简介

 CyclicBarrier 是 Java 并发包（java.util.concurrent）中的一个同步辅助类。它允许一组线程相互等待，直到到达某个公共屏障点（common barrier point）。只有当所有参与的线程都到达屏障点时，这些线程才能继续执行。这与CountDownLatch不同，后者是一次性的，而CyclicBarrier可以在等待的线程被释放后重新使用。

## 使用场景

多线程任务中，让多个线程在某个特定的点上同步，确保完成所有线程都完成某个阶段后，再一起进入下一阶段。

比如：一个计算任务被分成了多个部分，每个部分由不同的线程进行处理，处理完成后需要汇总结果，这时候需要所有线程都完成计算后，才能进行汇总。

## 基本用法

### 创建 CyclicBarrier

使用 new CyclicBarrier(int parties) 创建一个新的 CyclicBarrier，其中 parties 参数指定了必须调用 await() 方法以使所有线程都能通过屏障的线程数量。

也可以提供第二个参数，这是一个 Runnable，当最后一个线程到达屏障时会执行这个Runnable，这可以用于在所有线程到达屏障之前执行一些特定的操作。

### 线程中使用 await() 方法

每个线程在需要等待其他线程的地方调用 await() 方法。该方法会挂起当前线程，直到所有线程都调用了 await() 方法。

如果当前线程不是最后一个到达屏障的线程，await() 将阻塞直到所有线程都到达。如果当前线程是最后一个到达的，则所有线程都会被唤醒继续执行。

### 重置屏障

可以通过调用 reset() 方法来重置 CyclicBarrier，这将导致所有等待的线程收到一个 BrokenBarrierException 异常，并且屏障计数会被重置为其初始状态。



## 示例

### 说明：

使用休眠函数模拟实际业务执行的耗时，使用线程池多线程的执行任务，所有任务完成后，汇总计算结果。

### 公共代码

给出模拟函数，以及线程池定义等前置条件代码。



```java
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
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CyclicBarrierTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicBarrierTest.class);

    // 使用map模拟所有的数据，每个线程占据一个key
    private final Map<Integer, Integer> countMap = new ConcurrentHashMap<>();

    // 核心线程数
    private static final int CORE_POOL_SIZE = 5;

    // 线程池
    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE * 2,
            10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000));

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

}

```



### 场景1 基本用法

任务数量不超过线程池的核心线程数

```java
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
```

运行后日志打印参考：(日志打印忽略时间戳)

```shell
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:105 - waiting all thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-3> index: 2, randomSleepSeconds: 7.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-1> index: 0, randomSleepSeconds: 7.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-2> index: 1, randomSleepSeconds: 7.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-2> index: 1, waiting other thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-3> index: 2, waiting other thread.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-1> index: 0, waiting other thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-3> index: 2, has passed barrier.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:108 - <main> has passed barrier.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-1> index: 0, has passed barrier.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:116 - finish calculate.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-2> index: 1, has passed barrier.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:118 - sum: 21.
```

日志说明：

- 有3个任务，分别用3个子线程执行，主线程调用 ```cyclicBarrier.await(1, TimeUnit.MINUTES);``` 方法，表示等待子线程任务执行完成，之后汇总结果。



### 场景2 构造函数中指定Runnable

构造函数Runnable的用法：该Runnable优先级高

```java
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
```



打印日志参考：

```shell
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:139 - waiting all thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-3> index: 2, randomSleepSeconds: 9.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-2> index: 1, randomSleepSeconds: 7.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-1> index: 0, randomSleepSeconds: 6.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-1> index: 0, waiting other thread.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-2> index: 1, waiting other thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-3> index: 2, waiting other thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:130 - <pool-2-thread-3> is first priority.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-1> index: 0, has passed barrier.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-2> index: 1, has passed barrier.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-3> index: 2, has passed barrier.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:142 - <main> has passed barrier.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:150 - finish calculate.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:152 - sum: 22.
```

日志说明：

- 即构造函数的Runnable的优先级最高，在其他所有到达屏障点之后，这个线程的代码最先执行。



### 场景3 通用场景：分批处理

使用线程池处理的场景，由于线程池数量是有限的，所以一次并发执行的线程数是有上限的，比如为5，当总任务数为13时，就需要把任务分批处理，以线程池核心线程数量分批，13个任务和分为3批，每个批次分别为5,5,3个任务。在所有批次执行完成之后，再汇总所有的数据。

分批场景需要考虑最后一批的执行，最后一批的数量可能存在小于核心线程数量的情况，要以实际的数量为准。

参考代码如下：

```java
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
```



参考日志：

```shell
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:168 - batch calculate, batchIndex: 0, threadNum: 5.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:178 - waiting all thread in this batch, batchIndex: 0.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-1> index: 0, randomSleepSeconds: 3.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-3> index: 2, randomSleepSeconds: 8.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-5> index: 4, randomSleepSeconds: 8.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-4> index: 3, randomSleepSeconds: 8.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-2> index: 1, randomSleepSeconds: 7.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-1> index: 0, waiting other thread.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-2> index: 1, waiting other thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-3> index: 2, waiting other thread.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-5> index: 4, waiting other thread.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-4> index: 3, waiting other thread.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:181 - <main> has passed barrier in this batch, batchIndex: 0.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:168 - batch calculate, batchIndex: 1, threadNum: 5.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:178 - waiting all thread in this batch, batchIndex: 1.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-1> index: 0, has passed barrier.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-1> index: 5, randomSleepSeconds: 8.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-2> index: 1, has passed barrier.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-3> index: 2, has passed barrier.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-3> index: 7, randomSleepSeconds: 8.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-2> index: 6, randomSleepSeconds: 8.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-5> index: 4, has passed barrier.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-5> index: 8, randomSleepSeconds: 5.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-4> index: 3, has passed barrier.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-4> index: 9, randomSleepSeconds: 3.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-4> index: 9, waiting other thread.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-5> index: 8, waiting other thread.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-2> index: 6, waiting other thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-3> index: 7, waiting other thread.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-1> index: 5, waiting other thread.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:181 - <main> has passed barrier in this batch, batchIndex: 1.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-4> index: 9, has passed barrier.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:168 - batch calculate, batchIndex: 2, threadNum: 3.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:178 - waiting all thread in this batch, batchIndex: 2.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-5> index: 8, has passed barrier.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-5> index: 10, randomSleepSeconds: 2.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-3> index: 7, has passed barrier.
[pool-2-thread-2]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-2> index: 6, has passed barrier.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-3> index: 12, randomSleepSeconds: 5.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:67 - <pool-2-thread-4> index: 11, randomSleepSeconds: 9.
[pool-2-thread-1]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-1> index: 5, has passed barrier.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-5> index: 10, waiting other thread.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-3> index: 12, waiting other thread.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:79 - <pool-2-thread-4> index: 11, waiting other thread.
[pool-2-thread-5]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-5> index: 10, has passed barrier.
[pool-2-thread-3]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-3> index: 12, has passed barrier.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:181 - <main> has passed barrier in this batch, batchIndex: 2.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:191 - finish calculate.
[pool-2-thread-4]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:88 - <pool-2-thread-4> index: 11, has passed barrier.
[main]  INFO  com.ysx.utils.concurrent.CyclicBarrierTest:193 - sum: 82.
```



日志说明：

- 分批处理，每个批次内部是并发执行的。
- 在所有批次执行完成之后，再汇总计算结果。



## 源代码地址

- [github](https://github.com/YoungBear/JavaUtils/blob/master/src/test/java/com/ysx/utils/concurrent/CyclicBarrierTest.java)
- [gitee](https://gitee.com/YoungBear2023/JavaUtils/blob/master/src/test/java/com/ysx/utils/concurrent/CyclicBarrierTest.java)

