package com.ydx.datamigration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "threadpool", ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {
    private int corePoolSize;
    private int maximumPoolSize;
    private int keepAliveSeconds;
    private int queueCapacity;
}
