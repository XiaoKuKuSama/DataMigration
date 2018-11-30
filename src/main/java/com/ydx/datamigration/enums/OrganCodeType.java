package com.ydx.datamigration.enums;

import lombok.Getter;

/**
 * Created by xinyi.chang on 18-11-20.
 */
@Getter
public enum OrganCodeType {

    ROOT("ROOT", "根节点", "根路径模块，不允许变更"),
    AGENT_ROOT("AGENT_ROOT", "代理根节点", "代理根节点"),
    AGENT_DIRECT("DIRECT_ROOT", "直销团队", "代理直销团队根节点"),
    AGENT_DISTRIB("DISTRIB_ROOT", "分销团队", "代理分销团队根节点"),
    AGENT_BUSI("BUSI_ROOT", "业务团队", "代理业务团队根节点");

    private String code;
    private String name;
    private String description;

    private OrganCodeType(String code, String name, String description){
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static OrganCodeType codeOf(String code){
        for(OrganCodeType type:OrganCodeType.values()){
            if(code.equalsIgnoreCase(type.code)){
                return type;
            }
        }
        return null;
    }
}
