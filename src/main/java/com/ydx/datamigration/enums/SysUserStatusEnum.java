package com.ydx.datamigration.enums;

import lombok.Getter;

/**
 * 易多销易多销SysUser 系统用户状态枚举
 */
@Getter
public enum SysUserStatusEnum {

    NORMAL("NORMAL", "正常"),
    UNREVIEW("UNREVIEW", "未审核"),
    REVIEWING("REVIEWING", "审核中"),
    UNAPPROVED("UNAPPROVED", "未审核通过"),
    LOCKED("LOCKED", "锁定"),
    DISABLED("DISABLED", "禁用"),
    EXPIRED("EXPIRED", "失效（过期"),
    DELETED("DELETED", "删除");

    private String name;
    private String text;

    private SysUserStatusEnum(String name, String text) {
        this.name = name;
        this.text = text;
    }
}
