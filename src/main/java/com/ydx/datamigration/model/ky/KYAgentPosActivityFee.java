package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by xinyi.chang on 18-11-23.
 */
@Data
@TableName("agent_pos_activity_fee")
public class KYAgentPosActivityFee{
    private static final long serialVersionUID = 1L;

    private String agentNo; //所属直接服务商编号
    private String status="TRUE"; //状态
    private Double rate; //费率
    private Date createTime;
    private Date modifyTime;
    private String activityCode;
    private String prodCode;
}
