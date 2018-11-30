package com.ydx.datamigration.service.ydx.agentsrv;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydx.datamigration.dao.ydx.agentsrv.YDXSysUserMapper;
import com.ydx.datamigration.model.ydx.YDXSysUser;
import org.springframework.stereotype.Service;

/**
 * 易多销 agent_srv数据源 账户信息操作service
 */
@Service
public class YDXSysUserService extends ServiceImpl<YDXSysUserMapper, YDXSysUser> {


    public int querySysUserMaxUserNo(String agentNo) {
        return super.baseMapper.querySysUserMaxUserNo(agentNo);
    }
}
