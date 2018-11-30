package com.ydx.datamigration.enums;


import lombok.Getter;

/**
 * 卡友服务商角色类型枚举
 */
@Getter
public enum KYAgentInfoRoleType {
    AGENT("AGENT", "服务商"),
    SALE("SALE", "销售"),
    AGENT_SUB("AGENT_SUB", "下级服务商"),
    KY_PARTNER("KY_PARTNER", "新合伙人"),
    RECOMMENDER("RECOMMENDER", "老合伙人"),
    HB_PARTNER("HB_PARTNER", "翰宝合伙人"),
    CXF("CXF", "磁炫付");

    private String code;
    private String text;

    KYAgentInfoRoleType(String code, String text) {
        this.code = code;
        this.text = text;
    }
}
