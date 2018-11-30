package com.ydx.datamigration.enums;

import lombok.Getter;

/**
 * 布尔枚举
 */
@Getter
public enum BooleanEnum {

    TRUE("正确",true),FALSE("错误",false),
    YES("是",true),NO("否",false);

    private String name;
    private boolean value;

    BooleanEnum(String name, boolean value) {
        this.name = name;
        this.value = value;
    }
}
