package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by xinyi.chang on 18-11-23.
 */
@Data
@TableName("agent_customer_relation")
public class YDXAgentCustomerRelation extends BaseModel{

    private static final long serialVersionUID = 1L;
    private String agentNo; //一级服务商编号
    private String customerNo; //商户编号
    private String directAgentNo; //所属服务商编号
    private String agentLine; //所属服务商链
    private String saleNo; //所属销售编号
    private Date createTime; //创建时间
    private Date updateTime; //更新时间
    private String createUser;
    private Integer notifyOptimistic=0; //通知版本号

    private String customerType; //商户类型
    private String productBrand; //所属机构,甲方机构
    private String fullName; //商户名称
    private String phoneNo; //商户手机号
    private String status; //商户状态
    private String linkman; //联系人
    private String legalPerson; //企业法人
    private Date openTime; //开通时间
    private String userSalesType; //操作时的销售类型
    private String targetType; //报单时选择的类型：直销或者下级
    private String customerSource ; //商户报单类型
    private String newFlag ; //Y:新增商户，N迁移商户

}
