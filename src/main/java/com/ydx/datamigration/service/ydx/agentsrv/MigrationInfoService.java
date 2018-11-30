package com.ydx.datamigration.service.ydx.agentsrv;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydx.datamigration.dao.ydx.agentsrv.MigrationInfoMapper;
import com.ydx.datamigration.model.ydx.MigrationInfo;
import org.springframework.stereotype.Service;

/**
 * 易多销 agent_srv数据源 迁移信息操作service
 */
@Service
public class MigrationInfoService extends ServiceImpl<MigrationInfoMapper,MigrationInfo> {
}
