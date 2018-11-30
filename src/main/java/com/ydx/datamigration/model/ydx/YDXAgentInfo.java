package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;


/***
 * 易多销agent_srv库 销售机构信息
 */
@Data
@TableName("agent_info")
public class YDXAgentInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String agentNo;// 代理商编号
    private String saleNo;// 销售员编号
    private Integer agentLevel; // 服务商级别
    private String parentAgentNo;// 上级服务商编号
    private String parentAgentLine;// 父服务商链
    private String agentType;// 代理商类型 company/公司、selfEmployed/个体户、person/个人
    private String fullName;// 代理商全名
    private String shortName;// 代理商简称
    private String qualificationsType;// 资质类型 q企业w小微
    private String orgCode;// 代理商所属地区
    private String address;// 详细地址
    private String protocolNo;// 协议编号
    private Date protocolExpireyTime;// 协议失效时间
    private String protocolAnnex;// 协议扫描件url
    private String managerName;// 管理者名称
    private String managerPhone;// 管理者手机号
    private String managerEmail;// 管理者邮箱
    private String bussPrincipalName;// 业务负责人名称
    private String bussPrincipalPhone;// 业务负责人电话
    private String bussPrincipalFixedPhone;// 负责人固定电话
    private String status;// 状态
    private Date createTime;// 代理商创建时间
    private Date updateTime;// 代理商更新时间


}
