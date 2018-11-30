package com.ydx.datamigration.enums;

import lombok.Getter;

/**
 * 卡友 账号表类型枚举
 */
@Getter
public enum OperatorTypeEnum {

    SUPERADMIN("SUPERADMIN", "超级管理员"),
    RECOMMENDER("RECOMMENDER", "合伙人"),
    AGENT_SUB("AGENT_SUB", "分支机构"),
    SALE("SALE", "销售"),
    AGENTADMIN("AGENTADMIN", "服务商管理员")
    ;
    private String code;
    private String text;

    OperatorTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

}
