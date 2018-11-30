package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 易多销agent_srv库  销售机构扩展信息
 */
@Data
@TableName("agent_info_ext")
public class YDXAgentInfoExt extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String agentNo; //服务商编号
    private String extType; //扩展属性类型
    private String extName; //扩展属性名称
    private String extValue; //扩展属性值
    private Date createTime; //创建时间
    private Date updateTime; //更新时间
}
