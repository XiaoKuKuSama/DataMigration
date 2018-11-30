package com.ydx.datamigration.model.ydx;

import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 易多销agent_srv库 迁移记录 记录每个一代服务商迁移数据
 */
@Data
public class MigrationInfo extends BaseModel {
    private static final long serialVersionUID = 1L;

    private String agentNo;
    private Integer subAgentSum;    //需要迁移的下级数量(分支机构+销售)
    private Integer subAgentSuccSum; //成功迁移的下级数量(分支机构+销售)
    private Integer accountSum;     //需要迁移的总账号数(一代+一代操作员+分支机构+销售)
    private Integer accountSuccSum; //成功迁移的总账号数(一代+一代操作员+分支机构+销售)
    private Integer acticityFeeSum;  //需要迁移的服务商活动费率设置数量
    private Integer acticityFeeSuccSum; //成功迁移的服务商活动费率设置数量
    private Integer posSum;  //需要迁移的终端信息数量
    private Integer posSuccSum; //成功迁移的终端信息数量
    private Integer posRelationSum; //需要迁移的终端关系数量
    private Integer posRelationSuccSum; // 成功迁移的终端关系数量
    private Integer customerRelationSum; //需要迁移的商户关系数量
    private Integer customerRelationSuccSum; //成功迁移的商户关系数量
    private String status;          //迁移状态 SUCCESS成功  FAIL失败  INIT初始化
    private String failNode;        //失败节点
    private Integer failSum;        //失败次数
    private Date successTime;       //成功时间
    private Date createTime;        //创建时间
    private Date updateTime;        //修改时间
    private Date businessSuccessTime; //业务迁移成功时间
}
