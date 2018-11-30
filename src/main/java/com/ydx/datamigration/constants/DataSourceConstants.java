package com.ydx.datamigration.constants;

/**
 * 数据源配置相关常量
 */
public class DataSourceConstants {

    //卡友服务商库数据源名称
    public static final String KY_AGENT_SRV_DATASOURCE_NAME="KY_AGENT_SRV";
    //卡友服务商库数据源包扫描路径
    public static final String KY_AGENT_SRV_DATASOURCE_PACKAGE="com.ydx.datamigration.dao.ky.agentsrv";

    //卡友运营库数据源名称
    public static final String KY_POSP_BOSS_DATASOURCE_NAME="KY_POSP_BOSS";
    //卡友运营库数据源包扫描路径
    public static final String KY_POSP_BOSS_DATASOURCE_PACKAGE="com.ydx.datamigration.dao.ky.pospboss";

    //易多销服务商库数据源名称
    public static final String YDX_AGENT_SRV_DATASOURCE_NAME="YDX_AGENT_SRV";
    //易多销服务商库数据源包扫描路径
    public static final String YDX_AGENT_SRV_DATASOURCE_PACKAGE="com.ydx.datamigration.dao.ydx.agentsrv";

    //易多销服务商产品库数据源名称
    public static final String YDX_AGENT_PRODUCT_DATASOURCE_NAME="YDX_AGENT_PRODUCT";
    //易多销服务商产品库数据源包扫描路径
    public static final String YDX_AGENT_PRODUCT_DATASOURCE_PACKAGE="com.ydx.datamigration.dao.ydx.agentproduct";
}
