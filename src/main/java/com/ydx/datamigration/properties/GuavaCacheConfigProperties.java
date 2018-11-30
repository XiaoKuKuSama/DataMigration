package com.ydx.datamigration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by xinyi.chang on 18-11-24.
 */
@Data
@Component
@ConfigurationProperties(prefix = "guava.cache" ,ignoreInvalidFields = true)
public class GuavaCacheConfigProperties {

    private long maximumSize;

    private long maximumWeight;

    private long expireAfterWriteDuration;

    private long expireAfterAccessDuration;

    private long refreshDuration;

    private int initialCapacity;

    private int concurrencyLevel;

}
