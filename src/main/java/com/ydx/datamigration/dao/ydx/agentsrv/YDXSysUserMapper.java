package com.ydx.datamigration.dao.ydx.agentsrv;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydx.datamigration.model.ydx.YDXSysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface YDXSysUserMapper extends BaseMapper<YDXSysUser> {

    @Select("select max(user_no) from sys_user where agent_no=#{agentNo};")
    int querySysUserMaxUserNo(String agentNo);
}
