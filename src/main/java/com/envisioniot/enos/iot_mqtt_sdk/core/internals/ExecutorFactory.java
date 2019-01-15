package com.envisioniot.enos.iot_mqtt_sdk.core.internals;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author zhensheng.cai
 * @date 2019/1/8.
 */
public class ExecutorFactory {

    /**
     * 用于处理一般的publish任务请求。
     */
    private ExecutorService internalExecutor =  new ThreadPoolExecutor(10, 20, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1000),
            new ThreadFactoryBuilder().setNameFormat("internal-executor-%d").build());

    /**
     * 用于处理单线程顺序执行的任务，如连接和订阅，这些任务不需要并发，也保证了不会干扰订阅的结果。
     * 防止在连接的过程中有订阅发生，导致订阅失败。
     */
    private ExecutorService connectExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("connect-executor-%d").build());


    /**
     * callback timeout pool
     */
    private ScheduledExecutorService timeoutScheduler = new ScheduledThreadPoolExecutor(1,
            new ThreadFactoryBuilder().setNameFormat("callback-timeout-pool-%d").build());

    /**
     * 多线程池
     * @return
     */
    public ExecutorService getPublishExecutor(){
        return internalExecutor;
    }

    /**
     * 单个顺序执行的线程池
     * @return
     */
    public ExecutorService getConnectExecutor(){
        return connectExecutor;
    }

    public ScheduledExecutorService getTimeoutScheduler(){
        return timeoutScheduler;
    }
}
