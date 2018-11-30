package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 卡友posp_boss库 优质服务商信息
 */
@Data
@TableName("posp_boss.agent_quality")
public class KYAgentQuality extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String branchName; //所属非公司
    private String agentNo;  //服务商编码
    private String agentName; //服务商名称
    private Double paidDeposit; //保证金
    private String signingTime; //签约时间
    private String monthProfit; //月分润
    private String isTransfer; //是否权限转移
    private String remark; //备注
    private Date updatetime; //修改时间
}
