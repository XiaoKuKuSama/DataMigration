package com.ydx.datamigration.enums;


import lombok.Getter;

/**
 * 常量枚举
 */
@Getter
public enum ConstantEnum {
    //基础
    SUCCESS("成功","SUCCESS"),
    FAIL("失败","FAIL"),

    INIT("初始化","INIT"),
    TRUE("正确","TRUE"),
    FALSE("错误","FALSE"),
    DELETE("删除","DELETE"),


    //数据源配置相关
    ERROR_DATASOURCE_APPLICATION_NAME("错误的APP名称","ERRORApplicationName"),
    ERROR_DATASOURCE_PORT("数据源配置服务PORT不能为空","ERRORPort"),
    ERROR_DATASOURCE_HOST("数据源配置服务HOST不能为空","ERRORHost"),
    ERROR_DATASOURCE_DATASOURCE_NAME("数据源配置服务dataSourceName不能为空","ERRORDataSourceName"),
    FAILED_DATASOURCE_RESPONSE("数据源配置服务响应失败","Failed"),
    DATASOURCE_LOGGER_NAME("druid数据源日志名称","druid.statlog"),

    //公共
    DEFAULT_ERROR_VIEW_NAME("全局异常捕获跳转默认错误页面","error")

    ;

    private String name;
    private String value;


    ConstantEnum() {
    }

    ConstantEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
