package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



/**
 * Created by xinyi.chang on 18-11-20.
 */
@Data
@TableName("sys_agent")
public class YDXSysAgent {

    @TableId
    private Long id;

    private String agentNo;

    private String agentName;

    private String level;
}
