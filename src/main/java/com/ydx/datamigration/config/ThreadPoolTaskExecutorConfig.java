package com.ydx.datamigration.config;

import com.ydx.datamigration.enums.BooleanEnum;
import com.ydx.datamigration.properties.ThreadPoolConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置 DataMigrationThreadPool
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {

    private static final Logger logger=LoggerFactory.getLogger(ThreadPoolTaskExecutorConfig.class);

    @Autowired
    private ThreadPoolConfigProperties properties;

    @Bean
    public Executor DataMigrationThreadPool() {
        logger.info("---------------------线程池初始化:{}",properties);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置线程池参数
        executor.setThreadNamePrefix("DataMigrationThreadPool-");
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaximumPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        //当调度器shutdown被调用时等待当前被调度的任务完成
        executor.setWaitForTasksToCompleteOnShutdown(BooleanEnum.TRUE.isValue());
        //线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池
        executor.initialize();
        logger.info("---------------------线程池初始化完成---------------------");
        return executor;
    }

}
