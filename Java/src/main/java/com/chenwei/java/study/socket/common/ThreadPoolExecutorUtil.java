package com.chenwei.java.study.socket.common;

import java.util.concurrent.*;

/**
 * @author chenwei
 * @date 2019-01-19 10:13
 **/
public class ThreadPoolExecutorUtil {
    private static int threadNam = 0;
    private static ExecutorService poolExecutor = new ThreadPoolExecutor(10, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), r -> {
        threadNam++;
        return new Thread(r, "SocketServer-Handler-Thread-" + threadNam);
    }, new ThreadPoolExecutor.AbortPolicy());

    public static Future submit(Runnable task) {
        return poolExecutor.submit(task);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return poolExecutor.submit(task);
    }

    public static <T> Future<T> submit(Runnable task, T result) {
        return poolExecutor.submit(task, result);
    }
}
