package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 易多销agent_srv库  企业信息
 */
@Data
@TableName("enterprise_info")
public class YDXEnterpriseInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String agentNo; //代理商编号
    private String enterpriseName;//企业名称
    private String licenseNo;//营业执照编号
    private String address;//注册地址
    private String registCapital;//注册资本
    private Date registDate; //注册日期
    private Date tradeBegin; //营业起始时间
    private Date tradeEnd;//营业截止时间
    private String tradeRange;  //营业范围
    private String legalName; //法人姓名
    private String legalCard; //法人身份证号码
    private String legalCardAnnexFront; //法人身份证附件(正面)url
    private String legalCardAnnexObverse; //法人身份证附件(反面)url
    private String legalAddress; //法人身份证地址
    private String bank;//开户行
    private String bankAccount; //账号
    private String license;//营业执照url
    private String permitLicense;//开户许可证url
    private String status; //状态
    private Date createTime;//创建时间
    private Date updateTime; //更新时间
    private String wordSizeName;//字号
    private String permitNumber; //核准号

}
