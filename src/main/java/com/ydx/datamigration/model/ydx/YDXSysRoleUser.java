package com.ydx.datamigration.model.ydx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 易多销 用户角色对应好关系
 */
@Data
@TableName("sys_role_user")
public class YDXSysRoleUser extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String username; //sysUser 的 username
    private String roleCode; //角色code
    private String createUser; //创建人
    private Date createTime; //创建时间
    private String updateUser; //修改人
    private Date updateTime;  //修改时间


    public YDXSysRoleUser(String username,String roleCode,String createUser,Date createTime){
        this.username=username;
        this.roleCode=roleCode;
        this.createUser=createUser;
        this.createTime=createTime;
    }
}
