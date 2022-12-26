package io.debc.nft.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.debc.nft.config.ConfigurableConstants.*;

/**
 * @description: thread pool
 * @author: Jalivv
 * @create: 2022-12-08 11:33
 **/
public class Pool extends ThreadPoolExecutor {

    /**
     * this(POOL_CORE_SIZE, POOL_QUEUE_SIZE)
     */
    public Pool() {
        this(PRODUCE_CORE_SIZE, PRODUCE_QUEUE_SIZE);
    }

    public Pool(int coreSize, int queueSize) {
        this(coreSize, queueSize, null);
    }

    public Pool(int coreSize, int queueSize, String name) {
        super(coreSize, coreSize, 0L, TimeUnit.MILLISECONDS, new ProductBlockQueue<>(queueSize),
                namedThreadFactory(name), new AbortPolicy());
    }


    /**
     * The default thread factory
     */
    public static class NamedThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        NamedThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + checkPrefix(name) +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

        private static String checkPrefix(String prefix) {
            return prefix == null || prefix.length() == 0 ? "debc" : prefix;
        }

    }

    public static ThreadFactory namedThreadFactory(String name) {
        return new NamedThreadFactory(name);
    }

    /**
     * product thread will block when pool queue is full
     *
     * @param <T>
     */
    public static class ProductBlockQueue<T> extends LinkedBlockingQueue<T> {
        public ProductBlockQueue(int capacity) {
            super(capacity);
        }

        @Override
        public boolean offer(T t) {
            try {
                put(t);
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }


}
