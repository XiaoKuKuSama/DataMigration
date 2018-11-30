package com.ydx.datamigration.enums;

import lombok.Getter;

/**
 * 易多销SysUser 系统用户类别枚举
 */
@Getter
public enum SysUserTypeEnum {

    DISTRIB_SALE("DISTRIB_SALE", "分销销售"),
    DIRECT_SALE("DIRECT_SALE", "直销销售"),
    ADMIN("ADMIN", "管理员"),
    UNSALE("UNSALE", "非销售"), // 超级管理员，普通管理员（UA-UB-UC）
    UNDEFINED("UNDEFINED", "未定义");

    private String code;
    private String text;

    SysUserTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

}
