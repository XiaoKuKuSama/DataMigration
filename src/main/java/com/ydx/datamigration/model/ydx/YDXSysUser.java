package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 易多销agent_srv库 系统用户信息
 */
@Data
@TableName("sys_user")
public class YDXSysUser extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String userTypeCode;    //用户类别标志（SysUserTypeEnum）
    private String agentNo;        //服务商编号
    private String organCode;    //机构标志
    private String userNo;        //用户编号
    private String username;    //用户名
    private String password;    //密码
    private String realname;    //真实姓名
    private String email;        //电子邮箱
    private String telephone;    //电话
    private String cellphone;    //用户手机号码
    private String sex;            //性别
    private Integer age;        //年龄
    private String address;        //地址
    private String photo;        //照片
    private String userStatus;    //用户状态(SysUserStatusEnum)

    private Integer orders; //排序
    private String description; //描述
    private String status; //数据状态
    private String createUser; //创建人
    private Date createTime; //创建时间
    private String updateUser; //修改人
    private Date updateTime;  //修改时间

    private String kyAgentNo; //原卡友服务商编号

}
