package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 卡友agent_srv库 账号信息
 */
@Data
@TableName("operator")
public class KYOperator extends BaseModel {
    private static final long serialVersionUID = 1L;

    private String username; //操作员的登录的名称
    private String status; //状态TRUE,FALSE,DELETE
    private String password; //密码
    private Integer maxErrorTimes; //最大登录失败次数
    private Date lastLoginErrTime; //最后登陆错误时间
    private Integer modifyPasswdCycle; //修改密码期限
    private Date passwdModifyTime; //密码修改时间
    private String allowBeginTime; //登录许可起始时间
    private String allowEndTime; //登录许可截止时间
    private String operatorType; //操作员类型
    private String realname; //操作员真实姓名
    private Date createTime; //操作员创建时间
    private String createOperator; //创建人
    private String reloginFlag; //重复登录标志
    private Date passwdExpireTime; //密码到期时间
    private Date passwdEffectTime; //密码生效时间
    private Integer tryTimes;  //同时登录的数量
    private String orgCode; //机构编码
    private String agentNo; //服务商编号
    private String publicPassword; //公用密码
    private String isSend; //短信是否发送
    private Date lastSendTime; //最后发送短信时间
    private String selectScope; //查询范围
//    private String secondAgentId; //数据库已删除
    private String changePassword; //是否修改过密码或用于存放前三次密码
    private String selfAgentNo; //操作员所属服务商编号
}
