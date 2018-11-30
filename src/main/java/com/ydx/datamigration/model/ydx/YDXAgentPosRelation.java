package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by xinyi.chang on 18-11-23.
 */
@Data
@TableName("agent_pos_relation")
public class YDXAgentPosRelation extends BaseModel{

    private static final long serialVersionUID = 1L;

    /**
     * POS机具序列号
     */
    private String posSn;

    /**
     * 代理商编号
     */
    private String agentNo;

    /**
     * 所属服务商编号
     */
    private String directAgentNo;

    /**
     * 所属服务商链
     */
    private String agentLine;

    /**
     * 所属销售编号
     */
    private String saleNo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     *创建人
     */
    private String createUser;
}
