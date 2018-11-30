package com.ydx.datamigration.properties;

import com.ydx.datamigration.properties.base.BaseDataSourceConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 易多销服务商库 agent_srv 数据源配置properties
 */
@Component
@ConfigurationProperties(prefix = "ydxagentsrvds", ignoreInvalidFields = true)
public class YDXAgentSrvDataSourceConfigProperties extends BaseDataSourceConfigProperties {
    @Override
    public String toString() {
        return YDXAgentSrvDataSourceConfigProperties.class.getSimpleName() + ":" + super.toString();
    }
}
