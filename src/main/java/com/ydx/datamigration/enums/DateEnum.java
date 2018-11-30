package com.ydx.datamigration.enums;


import lombok.Getter;

/**
 * 日期相关枚举
 */
@Getter
public enum DateEnum {

    YMD("年月日","yyyy-MM-dd"),
    HMS("时分秒","HH:mm:ss"),
    YMDHMS("年月日时分秒","yyyy-MM-dd HH:mm:ss");

    private String name;
    private String value;

    DateEnum(String name, String value){
        this.name=name;
        this.value=value;
    }


}
