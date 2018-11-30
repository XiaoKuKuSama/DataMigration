package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by xinyi.chang on 18-11-20.
 */
@Data
@TableName("sys_organ")
public class YDXSysOrgan extends BaseModel{

    private static final long serialVersionUID = 1L;

    private String agentNo;    //服务商编号

    private String typeCode;  //识别标识

    private String parentCode;  //所属标识

    private String parentPath;  //所属路径

    private String name;    //名称

    /**
     * AGENT_ROOT - 根节点
     * DIRECT_ROOT - 直销团队
     * DISTRIB_ROOT - 分销团队
     * BUSI_ROOT - 业务团队
     */
    private String code;    //标识

    protected Integer orders;

    protected String status;

    protected String description;

    protected String createUser;

    protected Date createTime;

    protected String updateUser;

    protected Date updateTime;
}
