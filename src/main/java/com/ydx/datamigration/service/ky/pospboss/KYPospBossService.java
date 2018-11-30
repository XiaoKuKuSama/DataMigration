package com.ydx.datamigration.service.ky.pospboss;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ydx.datamigration.config.KYPospBossDataSourceConfig;
import com.ydx.datamigration.dao.ky.pospboss.KYOrganizationMapper;
import com.ydx.datamigration.enums.ConstantEnum;
import com.ydx.datamigration.model.ky.*;
import com.ydx.datamigration.model.ydx.MigrationInfo;
import com.ydx.datamigration.model.ydx.YDXExpressAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 处理业务service（卡友posp_boss数据源事物在此开启）
 */
@Service
@Transactional(value = KYPospBossDataSourceConfig.TRANSACTION_MANAGER, rollbackFor = Exception.class)
public class KYPospBossService {

    @Autowired
    private KYAgentPosRelationService posRelationService;

    @Autowired
    private KYAgentCustomerRelationService customerRelationService;

    @Autowired
    private KYAgentQualityService agentQualityService;

    @Autowired
    private KYPosService posService;

    @Autowired
    private KYOrganizationService organizationService;


    /**
     * 查询迁移的一代服务商在卡友posp_boss库需要迁移的相关信息数量
     *
     * @param migrationInfo 迁移信息
     * @return
     */
    public void queryMigrationInfoByAgentNo(MigrationInfo migrationInfo) {


    }

    /**
     * 查询一代服务商是否在优质服务商列表里
     *
     * @param agentNo
     * @return 1为在  0为不在
     */
    public int queryAgentQualityByAgentNo(String agentNo) {
        return agentQualityService.count(new LambdaQueryWrapper<KYAgentQuality>().eq(KYAgentQuality::getAgentNo, agentNo));
    }

    /**
     * 卡友地址信息 转 易多销地址信息 处理orgname
     */
    public void KY2YDXExpressAddressForOrgName(YDXExpressAddress to) {
        Optional.ofNullable(to.getOrgCode()).filter(StringUtils::isNotEmpty).ifPresent(
                code -> {
                    String s1 = "";
                    String s2 = "";
                    String s3 = "";
                    if (code.length() == 6) {
                        s3 = Optional.ofNullable(organizationService.getOne(new LambdaQueryWrapper<KYOrganization>().eq(KYOrganization::getCode, code)))
                                .map(x -> x.getName()).orElse("");
                    }
                    s1 = Optional.ofNullable(organizationService.getOne(new LambdaQueryWrapper<KYOrganization>().eq(KYOrganization::getCode, code.substring(0, 2))))
                            .map(x -> x.getName()).orElse("");
                    if (code.length() >= 4) {
                        s2 = Optional.ofNullable(organizationService.getOne(new LambdaQueryWrapper<KYOrganization>().eq(KYOrganization::getCode, code.substring(0, 4))))
                                .map(x -> x.getName()).orElse("");
                    }
                    to.setOrgName(s1 + s2 + s3);
                }
        );
    }

    /**
     * 查询卡友运营库 服务商的业务迁移信息
     *
     * @param migrationInfo
     */
    public void queryMigrationInfoByAgentNoBusiness(MigrationInfo migrationInfo) {
        //查询服务商需要迁移的pos信息
        int posSum = posService.count(new LambdaQueryWrapper<KYPos>().eq(KYPos::getAgentNo, migrationInfo.getAgentNo())
                                .ne(KYPos::getStatus,ConstantEnum.DELETE.getValue()));
        migrationInfo.setPosSum(posSum);
        migrationInfo.setPosSuccSum(0);
        //查询服务商需要迁移的商户关系信息
        int customerRelationSum = customerRelationService.count(new LambdaQueryWrapper<KYAgentCustomerRelation>()
                .eq(KYAgentCustomerRelation::getAgentNo, migrationInfo.getAgentNo()));
        migrationInfo.setCustomerRelationSum(customerRelationSum);
        migrationInfo.setCustomerRelationSuccSum(0);
        //查询服务商需要迁移的机具关系信息
        int posRelationSum = posRelationService.count(new LambdaQueryWrapper<KYAgentPosRelation>()
                .eq(KYAgentPosRelation::getAgentNo, migrationInfo.getAgentNo()));
        migrationInfo.setPosRelationSum(posRelationSum);
        migrationInfo.setPosRelationSuccSum(0);
    }


    /**
     * 根据一代服务商编号 查询迁移的pos信息 不包含DELETE状态
     * @param agentNo
     * @return
     */
    public List<KYPos> queryKYPosListByAgentNo(String agentNo) {
        return Optional.ofNullable(agentNo).filter(StringUtils::isNotEmpty)
                .map(no->posService.list(new LambdaQueryWrapper<KYPos>()
                        .eq(KYPos::getAgentNo,agentNo)
                        .ne(KYPos::getStatus, ConstantEnum.DELETE.getValue())))
                .orElse(null);
    }

    /**
     * 根据一代服务商编号  查询迁移的终端关系信息
     * @param agentNo
     * @return
     */
    public List<KYAgentPosRelation> queryKYAgentPosRelationList(String agentNo) {
        return Optional.ofNullable(agentNo).filter(StringUtils::isNotEmpty)
                .map(no->posRelationService.list(new LambdaQueryWrapper<KYAgentPosRelation>()
                        .eq(KYAgentPosRelation::getAgentNo,agentNo)))
                .orElse(null);
    }

    public List<KYAgentCustomerRelation> querykyAgentCustomerRelationList(String agentNo) {
        return customerRelationService.queryKYAgentCustomerRelationWithKYCustomer(agentNo);
    }
}
