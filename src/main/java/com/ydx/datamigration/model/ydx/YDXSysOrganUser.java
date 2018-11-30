package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by xinyi.chang on 18-11-20.
 */
@Data
@TableName("sys_organ_user")
public class YDXSysOrganUser {
    private static final long serialVersionUID = 1L;

    @TableId
    protected Long id;

    protected String createUser;  //创建用户

    protected Date createTime;    //创建时间

    protected String updateUser;  //修改用户

    protected Date updateTime;    //修改时间

    private String username;  //用户标识

    private String agentNo;    //服务商编号

    private String organCode;  //团队标识

    private Date inTime;    //加入团队时间

    private Date outTime;    //脱离团队时间

    private String status;     //与团队关系， Y:有效；N:脱离

    /**
     * 加入本团队之前的关系信息
     */
    private Long   beforeId;

    private String beforeAgentNo;

    private String beforeOrganCode;

    private Date beforeInTime;

    private Date beforeOutTime;
}
