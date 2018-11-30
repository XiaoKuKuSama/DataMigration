package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by xinyi.chang on 18-11-23.
 */
@Data
@TableName("pos")
public class YDXPos extends BaseModel{

    private static final long serialVersionUID = 1L;

    private Integer notifyOptimistic=0; //通知版本号
    private String posSn; //POS机具序列号
    private String posCati; //终端号
    private String posType; //型号
    private String posOwner; //采购机，自备机
    private String agentNo; //一级编号
    private String customerNo; //商户编号
    private String shopNo; //网点编号
    private String status; //状态
    private String useType; //使用方式
    private String purchaseAgentNo; //采购服务商编号
    private String productBrand; //POS所属机构
    private Date createTime; //创建时间
    private Date updateTime; //更新时间
}
