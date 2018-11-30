package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 卡友agent_srv库 服务商信息
 */
@Data
@TableName("agent_info")
public class KYAgentInfo extends BaseModel {
    private static final long serialVersionUID = 1L;

    private String agentNo;                    //代理商编号
    private String fullName;                //全称
    private String shortName;                //简称
    private String orgCode;                    //所属地区
    private String salesNo;                 //销售Id
    private Date createTime;                //创建时间
    private Date updateTime;                //代理商修改时间
    private String status;                    //状态
    private String phoneNo;                    //手机号
    private String linkMan;                    //联系人
    private String address;                 //地址
    private String email;                    //电子邮箱
    private String empowerMan;              //授权代表
    private String telephone;               //固定电话
    private String zipCode;                 //邮编
    private String commitCustStatus;        //提交商户状态
    private String supportLinkman;          //客服联系人
    private String supportLinkphone;        //客服联系电话
    private String riskLinkman;             //风险联系人
    private String riskLinkphone;           //风险联系电话
    private String bankAgentNo;             //收单外包服务机构
    private String roleType;                //1：服务商，2：操作员 3：销售 4：合伙人
    private String organizationType;        //服务商机构：1：乐富，2：掌易通
    private String qualificationsType;      //资质类型 Q企业W小微
    private String parentAgentNo;           //上级服务商编号
    private String canRecommend;            //是否为招商中心 Y 是
    private String recommendAgentNo;        //服务商的招商中心编号
    private String agentLevel;              //服务商级别
    private String parentAgentLine;         //父服务商链
    private Integer qsfCount;               //服务商入网申请轻松付个数，默认0
    private String dockingOrganization;    //三方机构编号
    private String riskCtrlDesc;           //风控描述
    private Date completeTime;           //审核完成时间
    private String contractNo;                //协议编号
    private Date uploadContractTime;     //合同上传时间
    private String isUploadContract;        //是否上传了电子合同
    private Date expTime;                //合同终止时间

    @TableField(exist = false)
    private KYOperator kyOperator;
}
