package com.ydx.datamigration.properties.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据源配置父类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class BaseDataSourceConfigProperties {
    @Value("${spring.application.name}")
    private String applicationName;
    private String dataSourceName;
    private int dsConfPort;
    private String dsConfIp;
    private boolean logEnabled;
    private long timeBetweenLogStatsMillis = 10000L;
    private LogFilter logFilter = new LogFilter();
}
