package com.ydx.datamigration.config;


import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.DruidDataSourceStatLoggerImpl;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.google.common.collect.Lists;
import com.ydx.datamigration.config.base.DruidPooledDataSource;
import com.ydx.datamigration.constants.DataSourceConstants;
import com.ydx.datamigration.enums.ConstantEnum;
import com.ydx.datamigration.properties.KYAgentSrvDataSourceConfigProperties;
import com.ydx.datamigration.properties.base.LogFilter;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * 卡友服务商库 agent_srv 数据源配置
 */

@Configuration
@MapperScan(basePackages = KYAgentSrvDataSourceConfig.PACKAGE, sqlSessionFactoryRef = KYAgentSrvDataSourceConfig.SQL_SESSION_FACTORY)
public class KYAgentSrvDataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(KYAgentSrvDataSourceConfig.class);

    //项目定义的数据源名称
    protected final static String DATA_SOURCE = DataSourceConstants.KY_AGENT_SRV_DATASOURCE_NAME;
    //数据源配置扫描包路径
    protected final static String PACKAGE = DataSourceConstants.KY_AGENT_SRV_DATASOURCE_PACKAGE;
    //项目定义SessionFactory别名
    protected final static String SQL_SESSION_FACTORY = DATA_SOURCE + "_SqlSessionFactory";
    //Transaction_manager别名
    public final static String TRANSACTION_MANAGER = DATA_SOURCE + "_TransactionManager";

    @Autowired
    private KYAgentSrvDataSourceConfigProperties properties;

    @Primary
    @Bean(name = DATA_SOURCE)
    public DataSource dataSource() {
        this.logger.info("init DruidDataSourceProperties. {}", this.properties);
        DruidPooledDataSource druidPooledDataSource = new DruidPooledDataSource(
                this.properties.getDsConfIp(),
                this.properties.getDsConfPort(),
                this.properties.getApplicationName(),
                this.properties.getDataSourceName());
        druidPooledDataSource.setTimeBetweenLogStatsMillis(properties.getTimeBetweenLogStatsMillis());
        if (this.properties.isLogEnabled()) {
            DruidDataSourceStatLogger statLogger = new DruidDataSourceStatLoggerImpl();
            statLogger.setLoggerName(ConstantEnum.DATASOURCE_LOGGER_NAME.getValue());
            druidPooledDataSource.setStatLogger(statLogger);
            List<Filter> filters = Lists.newArrayList();
            filters.add(this.logFilter());
            druidPooledDataSource.setProxyFilters(filters);
        }
        return druidPooledDataSource;
    }

    @Primary
    @Bean(name = TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager(
            @Qualifier(DATA_SOURCE) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier(DATA_SOURCE) DataSource dataSource) throws Exception {
        final MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setCacheEnabled(false);

        //分页插件paginationInterceptor  乐观锁插件optimisticLockerInterceptor
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setPlugins(new Interceptor[]{paginationInterceptor(), optimisticLockerInterceptor()});
        sqlSessionFactory.setGlobalConfig(globalConfiguration());

        return sqlSessionFactory.getObject();
    }

    /**
     * Mybatis Plus 全局配置
     */
    private GlobalConfig globalConfiguration() {
        GlobalConfig conf = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.AUTO);
        conf.setDbConfig(dbConfig);
        conf.setRefresh(true);
        return conf;
    }

    /**
     * 分页插件
     */
    private PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }

    /**
     * 乐观锁插件
     */
    private OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    /**
     * druid日志过滤器
     */
    private Slf4jLogFilter logFilter() {
        Slf4jLogFilter filter = new Slf4jLogFilter();
        LogFilter logFilter = this.properties.getLogFilter();
        filter.setResultSetLogEnabled(logFilter.isResultSetLogEnabled());
        filter.setConnectionLogEnabled(logFilter.isConnectionLogEnabled());
        filter.setStatementCreateAfterLogEnabled(logFilter.isStatementCreateAfterLogEnabled());
        filter.setStatementCloseAfterLogEnabled(logFilter.isStatementCloseAfterLogEnabled());
        filter.setStatementPrepareAfterLogEnabled(logFilter.isStatementPrepareAfterLogEnabled());
        filter.setStatementParameterClearLogEnable(logFilter.isStatementCloseAfterLogEnabled());
        filter.setStatementParameterSetLogEnabled(logFilter.isStatementParameterSetLogEnabled());
        return filter;
    }


}
