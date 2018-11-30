package com.ydx.datamigration.properties;

import com.ydx.datamigration.properties.base.BaseDataSourceConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 卡友运营库 posp_boss 数据源配置properties
 */
@Component
@ConfigurationProperties(prefix = "kypospbossds", ignoreInvalidFields = true)
public class KYPospBossDataSourceConfigProperties extends BaseDataSourceConfigProperties {
    @Override
    public String toString() {
        return KYPospBossDataSourceConfigProperties.class.getSimpleName() + ":" + super.toString();
    }
}
