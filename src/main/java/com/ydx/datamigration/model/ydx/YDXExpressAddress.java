package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 易多销agent_product库  收货地址信息
 */
@Data
@TableName("express_address")
public class YDXExpressAddress extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String ownerNo; //持有者编号
    private String ownerRole;//持有者角色
    private String phoneNo;//手机号
    private String linkMan;//联系人
    private String receiveAddress;//收货的地址
    private String isDefaultAddress;//是否是默认地址
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private String remark;//备注
    private String orgCode;//地区编号
    private String orgName;//地区名称
    private String telephone;//固定电话
    private String email;//电子邮件

}
