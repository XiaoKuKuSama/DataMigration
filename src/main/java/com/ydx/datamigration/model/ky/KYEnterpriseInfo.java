package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 卡友agent_srv库 企业信息
 */
@Data
@TableName("enterprise_info")
public class KYEnterpriseInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String enterpriseName;              //企业名称
    private String address;                     //企业注册地址
    private String businessLicenseNo;           //证照编号
    private String businessScope;               //经营范围
    private String enterpriseType;              //企业类型
    private Date creationDate;                  //成立日期
    private String legalPerson;                 //法人名称
    private String identityNo;                  //法人身份证号
    private String identityAddress;             //法人身份证地址
    private String idCardType;                  //证件类型
    private String organizationCode;            //组织机构代码
    private String registeredCapital;           //注册资本
    private String registrationNo;              //注册编号
    private String actualCapital;               //实收资本
    private String taxRegistNo;                 //税务登记证号
    private String validityDate;                //营业期限
    private String remark;                      //备注
    private String realAddress;                 //实际经营地址
    private String webUrl;                      //企业信息注册查询网址
    private String bussinessStatus;             //营业状态
    private String agentNo;                     //代理商编号
    private Date   createTime;                  //创建时间
    private Date   updateTime;                  //更新时间
}
