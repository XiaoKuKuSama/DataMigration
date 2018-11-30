package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 卡友agent_srv库  服务商收货地址
 */
@Data
@TableName("express_address")
public class KYExpressAddress extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String agentNo; //代理商编号
    private String receiveAddress; //收货的地址
    private String isDefaultAddress; //是否是默认地址
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
    private String phoneNo; //手机号
    private String linkMan; //联系人
    private String remark;  //备注
    private String status; //服务商收货地址表中的状态
    private String organizationCode; //组织机构码
    private String email; //服务商地址表的邮箱地址
    private String cPOrgCode; //县乡组织机构码 countyPreferentialOrgCode

}
