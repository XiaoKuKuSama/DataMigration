package com.ydx.datamigration.dao.ydx.agentsrv;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydx.datamigration.model.ydx.YDXSysRoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface YDXSysRoleUserMapper extends BaseMapper<YDXSysRoleUser> {


    @Select("select code from sys_role where level_type<>'YDX_TEST' ")
    List<String> queryRoleList();
}
