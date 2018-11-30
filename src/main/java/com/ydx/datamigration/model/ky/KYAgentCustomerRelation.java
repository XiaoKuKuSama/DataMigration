package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 卡友posp_boss库 服务商商户关系
 */
@Data
@TableName("posp_boss.agent_customer_relation")
public class KYAgentCustomerRelation extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String selfAgentNo; //直属服务商编号
    private String customerNo;  //商户编号
    private Date createTime;  //创建时间
    private Date updateTime;  //修改时间
    private String createUser;  //创建人
    private String ownerSys;  //所属系统
    private String agentNo;  //一代服务商编号

    @TableField(exist = false)
    private KYCustomer kyCustomer;

}
