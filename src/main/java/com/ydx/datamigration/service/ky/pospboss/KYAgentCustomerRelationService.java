package com.ydx.datamigration.service.ky.pospboss;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydx.datamigration.dao.ky.pospboss.KYAgentCustomerRelationMapper;
import com.ydx.datamigration.model.ky.KYAgentCustomerRelation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卡友 posp_boss数据源 服务商商户关系操作service
 */
@Service
public class KYAgentCustomerRelationService extends ServiceImpl<KYAgentCustomerRelationMapper,KYAgentCustomerRelation> {

    public List<KYAgentCustomerRelation> queryKYAgentCustomerRelationWithKYCustomer(String agentNo){
        return super.baseMapper.queryKYAgentCustomerRelationWithKYCustomer(agentNo);
    }
}
