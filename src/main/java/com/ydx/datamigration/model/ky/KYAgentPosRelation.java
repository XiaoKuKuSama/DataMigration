package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 卡友posp_boss库 服务商终端关系
 */
@Data
@TableName("posp_boss.agent_pos_relation")
public class KYAgentPosRelation extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String posSn;  //机具编号
    private String agentNo; //一级服务商编号
    private String ownerAgentNo; //所属服务商编号
    private String saleAgentNo;//采购服务商编号
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
    private String createUser; //创建人
    private String ownerSys;  //所属系统
}
