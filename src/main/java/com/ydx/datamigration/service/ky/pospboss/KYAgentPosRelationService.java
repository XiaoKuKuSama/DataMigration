package com.ydx.datamigration.service.ky.pospboss;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydx.datamigration.dao.ky.pospboss.KYAgentPosRelationMapper;
import com.ydx.datamigration.model.ky.KYAgentPosRelation;
import org.springframework.stereotype.Service;

/**
 * 卡友 posp_boss数据源 服务商终端关系操作service
 */
@Service
public class KYAgentPosRelationService extends ServiceImpl<KYAgentPosRelationMapper, KYAgentPosRelation> {


    public KYAgentPosRelation queryAgentPosRelationById(String id) {
        return super.baseMapper.queryAgentPosRelationById(id);
    }
}
