package com.ydx.datamigration.service.ydx.agentproduct;

import com.ydx.datamigration.config.YDXAgentProductDataSourceConfig;
import com.ydx.datamigration.enums.BooleanEnum;
import com.ydx.datamigration.model.ydx.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 *  处理业务service（易多销agent_product数据源事物在此开启）
 */
@Service
@Transactional(value = YDXAgentProductDataSourceConfig.TRANSACTION_MANAGER,rollbackFor=Exception.class)
public class YDXAgentProductService {

    private static final Logger logger = LoggerFactory.getLogger(YDXAgentProductService.class);

    @Autowired
    private YDXExpressAddressService expressAddressService;

    @Autowired
    private YDXPosService posService;

    @Autowired
    private YDXAgentPosActivityFeeService agentPosActivityFeeService;

    @Autowired
    private YDXAgentPosRelationService posRelationService;

    @Autowired
    private YDXAgentCustomerRelationService customerRelationService;


    public void saveYDXExpressAddress(List<YDXExpressAddress> list){
        boolean res = Optional.ofNullable(list)
                .filter(CollectionUtils::isNotEmpty)
                .map(x->expressAddressService.saveBatch(x))
                .orElse(BooleanEnum.TRUE.isValue());
        if(!res) throw new RuntimeException("保存易多销一代地址信息失败");
    }


    public void saveYDXAgentPosActivityFeeList(List<YDXAgentPosActivityFee> ydxAgentPosActivityFeeList) {
        boolean res = Optional.ofNullable(ydxAgentPosActivityFeeList)
                .filter(CollectionUtils::isNotEmpty)
                .map(x->agentPosActivityFeeService.saveBatch(x))
                .orElse(BooleanEnum.TRUE.isValue());
        if(!res) throw new RuntimeException("保存易多销服务商活动费率信息失败");
    }

    public void saveYDXPosList(List<YDXPos> ydxPosList) {
        boolean res = Optional.ofNullable(ydxPosList)
                .filter(CollectionUtils::isNotEmpty)
                .map(x->posService.saveBatch(x))
                .orElse(BooleanEnum.TRUE.isValue());
        if(!res) throw new RuntimeException("保存易多销服务商终端信息失败");
    }


    public void saveYDXAgentPosRelationList(List<YDXAgentPosRelation> ydxAgentPosRelationList) {
        boolean res = Optional.ofNullable(ydxAgentPosRelationList)
                .filter(CollectionUtils::isNotEmpty)
                .map(x->posRelationService.saveBatch(x))
                .orElse(BooleanEnum.TRUE.isValue());
        if(!res) throw new RuntimeException("保存易多销服务商终端关系失败");
    }

    public void saveYDXAgentCustomerRelationList(List<YDXAgentCustomerRelation> ydxAgentCustomerRelationList) {
        boolean res = Optional.ofNullable(ydxAgentCustomerRelationList)
                .filter(CollectionUtils::isNotEmpty)
                .map(x->customerRelationService.saveBatch(x))
                .orElse(BooleanEnum.TRUE.isValue());
        if(!res) throw new RuntimeException("保存易多销服务商商户关系失败");
    }
}
