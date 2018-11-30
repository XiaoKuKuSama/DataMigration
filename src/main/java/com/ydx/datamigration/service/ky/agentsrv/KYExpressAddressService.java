package com.ydx.datamigration.service.ky.agentsrv;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydx.datamigration.dao.ky.agentsrv.KYExpressAddressMapper;
import com.ydx.datamigration.model.ky.KYExpressAddress;
import org.springframework.stereotype.Service;

/**
 * 卡友 agent_srv数据源 收货地址信息操作service
 */
@Service
public class KYExpressAddressService extends ServiceImpl<KYExpressAddressMapper,KYExpressAddress> {
}
