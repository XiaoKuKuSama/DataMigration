package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by xinyi.chang on 18-11-24.
 */
@Data
@TableName("posp_boss.customer")
public class KYCustomer {

    private String mcc; //商户类型
    private String dockingOrganization; //所属机构,甲方机构
    private String fullName; //商户名称
    private String phoneNo; //商户手机号
    private String status; //商户状态
    private String linkman; //联系人
    private Date openTime; //开通时间
    private String customerType ; //商户报单类型
    private String legalPerson;


}
