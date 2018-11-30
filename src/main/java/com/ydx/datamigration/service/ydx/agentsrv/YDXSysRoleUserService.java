package com.ydx.datamigration.service.ydx.agentsrv;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydx.datamigration.dao.ydx.agentsrv.YDXSysRoleUserMapper;
import com.ydx.datamigration.model.ydx.YDXSysRoleUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 易多销 用户角色对应关系操作service
 */
@Service
public class YDXSysRoleUserService extends ServiceImpl<YDXSysRoleUserMapper, YDXSysRoleUser> {


    public List<String> queryRoleList() {
        return super.baseMapper.queryRoleList();
    }

}
