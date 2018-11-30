package com.ydx.datamigration.service.ky.agentsrv;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydx.datamigration.dao.ky.agentsrv.KYAgentInfoMapper;
import com.ydx.datamigration.model.ky.KYAgentInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卡友 agent_srv数据源 服务商信息操作service
 */
@Service
public class KYAgentInfoService extends ServiceImpl<KYAgentInfoMapper, KYAgentInfo> {


    /**
     * 查询卡友一代服务商要迁移的下级信息  迁移对象:状态为TRUE 类型为销售、分支机构  父链中是一代服务商编号-agentno-开头
     *
     * @param parentAgentLine 在链后添加上% 模糊查询
     * @return
     */
    public List<KYAgentInfo> queryAgentInfoListLikeParentAgentLine(String parentAgentLine) {
        return super.baseMapper.queryAgentInfoListLikeParentAgentLine(parentAgentLine + "%");
    }
}