package com.ydx.datamigration.properties;


import com.ydx.datamigration.properties.base.BaseDataSourceConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 卡友服务商库 agent_srv 数据源配置properties
 */
@Component
@ConfigurationProperties(prefix = "kyagentsrvds", ignoreInvalidFields = true)
public class KYAgentSrvDataSourceConfigProperties extends BaseDataSourceConfigProperties {

    @Override
    public String toString() {
        return KYAgentSrvDataSourceConfigProperties.class.getSimpleName()+":"+super.toString();
    }
}