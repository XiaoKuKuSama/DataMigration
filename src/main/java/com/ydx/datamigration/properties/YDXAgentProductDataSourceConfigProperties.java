package com.ydx.datamigration.properties;

import com.ydx.datamigration.properties.base.BaseDataSourceConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 易多销服务商产品库 agent_product 数据源配置properties
 */
@Component
@ConfigurationProperties(prefix = "ydxagentproductds", ignoreInvalidFields = true)
public class YDXAgentProductDataSourceConfigProperties extends BaseDataSourceConfigProperties {
    @Override
    public String toString() {
        return YDXAgentProductDataSourceConfigProperties.class.getSimpleName() + ":" + super.toString();
    }
}
