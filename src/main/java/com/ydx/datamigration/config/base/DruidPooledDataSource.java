package com.ydx.datamigration.config.base;


import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.ydx.datamigration.enums.ConstantEnum;
import com.ydx.datamigration.utils.DesUtil;
import com.ydx.datamigration.utils.SocketClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * druid数据库连接池对象封装 用于请求数据源管理dsm初始化数据源
 */
@Data
public class DruidPooledDataSource extends DruidDataSource {
    private static final long serialVersionUID = 1L;
    public static final Logger logger = LoggerFactory.getLogger(DruidPooledDataSource.class);
    private String dataSourceName;
    private String filter;
    private String applicationName;
    private String dsConfIp;
    private int dsConfPort;


    public DruidPooledDataSource(String dsConfIp, int dsConfPort, String applicationName, String dataSourceName) {
        this.dsConfIp = dsConfIp;
        this.dsConfPort = dsConfPort;
        this.applicationName = applicationName;
        this.setDataSourceName(dataSourceName);
    }

    private void setDataSourceName(String dataSourceName) {
        try {
            this.dataSourceName = dataSourceName;
            String data = SocketClient.requestDataSourceInfo(this.applicationName, dataSourceName, this.dsConfIp, this.dsConfPort);
            if (ConstantEnum.FAILED_DATASOURCE_RESPONSE.getValue().equals(data)) {
                logger.info("{applicationName=" + this.applicationName + "&" + "dataSourceName" + "=" + dataSourceName + "}");
                throw new RuntimeException("Sorry，you can not use the dataSource.");
            }

            if (ConstantEnum.ERROR_DATASOURCE_APPLICATION_NAME.getValue().equals(data)) {
                logger.info("method=setDataSourceName,{applicationName=" + this.applicationName + "}");
                throw new RuntimeException("The configuration of the applicationName at client is wrong or this application is not given any dataSource.");
            }
            if (!Strings.isNullOrEmpty(data)) {
                String[] datas = data.split("\n");
                this.dataInjectDataSource(datas);
            }
        } catch (NullPointerException e) {
            logger.info("method=setDataSourceName, {}", Throwables.getStackTraceAsString(e));
        }

    }

    private void dataInjectDataSource(String[] datas) {
        try {
            String[] var2 = datas;
            int var3 = datas.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String data = var2[var4];
                String[] dataArray = data.split("-");
                String var7 = dataArray[0];
                byte var8 = -1;
                switch (var7.hashCode()) {
                    case -2135890647:
                        if (var7.equals("max_pool_size")) {
                            var8 = 1;
                        }
                        break;
                    case -1677367426:
                        if (var7.equals("break_after_acquire_failure")) {
                            var8 = 7;
                        }
                        break;
                    case -1323526104:
                        if (var7.equals("driver")) {
                            var8 = 9;
                        }
                        break;
                    case -313584489:
                        if (var7.equals("min_pool_size")) {
                            var8 = 0;
                        }
                        break;
                    case -265713450:
                        if (var7.equals("username")) {
                            var8 = 11;
                        }
                        break;
                    case 116079:
                        if (var7.equals("url")) {
                            var8 = 10;
                        }
                        break;
                    case 29605641:
                        if (var7.equals("initial_pool_size")) {
                            var8 = 4;
                        }
                        break;
                    case 215325062:
                        if (var7.equals("acquire_retry_attempts")) {
                            var8 = 6;
                        }
                        break;
                    case 797225490:
                        if (var7.equals("test_connection_on_checkout")) {
                            var8 = 8;
                        }
                        break;
                    case 1216985755:
                        if (var7.equals("password")) {
                            var8 = 12;
                        }
                        break;
                    case 1367393085:
                        if (var7.equals("max_idle_time")) {
                            var8 = 2;
                        }
                        break;
                    case 1505875704:
                        if (var7.equals("idle_connection_test_period")) {
                            var8 = 5;
                        }
                        break;
                    case 1516646047:
                        if (var7.equals("max_statements")) {
                            var8 = 3;
                        }
                }

                switch (var8) {
                    case 0:
                        this.setMinIdle(Integer.parseInt(dataArray[1]));
                        break;
                    case 1:
                        this.setMaxActive(Integer.parseInt(dataArray[1]));
                        break;
                    case 2:
                        this.setMaxEvictableIdleTimeMillis((long) Integer.parseInt(dataArray[1]) * 1000L);
                        break;
                    case 3:
                        this.setMaxPoolPreparedStatementPerConnectionSize(0);
                        break;
                    case 4:
                        this.setInitialSize(Integer.parseInt(dataArray[1]));
                        break;
                    case 5:
                        this.setTimeBetweenEvictionRunsMillis((long) Integer.parseInt(dataArray[1]) * 1000L);
                        break;
                    case 6:
                        this.setConnectionErrorRetryAttempts(Integer.parseInt(dataArray[1]));
                        break;
                    case 7:
                        if ("false".equals(dataArray[1])) {
                            this.setBreakAfterAcquireFailure(false);
                        } else {
                            this.setBreakAfterAcquireFailure(true);
                        }
                        break;
                    case 8:
                        if ("false".equals(dataArray[1])) {
                            this.setTestOnBorrow(false);
                        } else {
                            this.setTestOnBorrow(true);
                        }
                        break;
                    case 9:
                        this.setDriverClassName(dataArray[1]);
                        break;
                    case 10:
                        this.setUrl(dataArray[1]);
                        break;
                    case 11:
                        this.setUsername(dataArray[1]);
                        break;
                    case 12:
                        byte[] pdTempArray = Base64.getDecoder().decode(dataArray[1]);
                        byte[] dataNew = DesUtil.desDecrypt(DesUtil.initKey, pdTempArray, DesUtil.keyArray2);
                        String realPassword = new String(dataNew, 0, dataNew.length);
                        int location = realPassword.indexOf("*");
                        if (location != -1) {
                            realPassword = realPassword.substring(0, location);
                        }

                        logger.info("3Q ==> ", realPassword);
                        this.setPassword(realPassword);
                }
            }

            if (this.getTimeBetweenConnectErrorMillis() == 500L) {
                this.setTimeBetweenConnectErrorMillis(1000L);
            }

            if (null != this.filter && !"".equals(this.filter)) {
                this.setFilters(this.filter);
            }
        } catch (Exception var13) {
            logger.error(Throwables.getStackTraceAsString(var13));
        }

    }
}
