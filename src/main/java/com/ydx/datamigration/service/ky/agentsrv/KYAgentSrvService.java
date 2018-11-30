package com.ydx.datamigration.service.ky.agentsrv;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.ydx.datamigration.config.KYAgentSrvDataSourceConfig;
import com.ydx.datamigration.enums.ConstantEnum;
import com.ydx.datamigration.enums.KYAgentInfoRoleType;
import com.ydx.datamigration.enums.OperatorTypeEnum;
import com.ydx.datamigration.model.ky.*;
import com.ydx.datamigration.model.ydx.MigrationInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理业务service（卡友agent_srv数据源事物在此开启）
 */
@Service
@Transactional(value = KYAgentSrvDataSourceConfig.TRANSACTION_MANAGER, rollbackFor = Exception.class)
public class KYAgentSrvService {

    @Autowired
    private KYAgentInfoService agentInfoService;

    @Autowired
    private KYEnterpriseInfoService enterpriseInfoService;

    @Autowired
    private KYExpressAddressService expressAddressService;

    @Autowired
    private KYOperatorService operatorService;

    @Autowired
    private KYAgentPosActivityFeeService agentPosActivityFeeService;

    /**
     * 查询迁移的一代服务商在卡友agent_srv库需要迁移的相关信息数量
     *
     * @param migrationInfo 迁移信息
     * @return
     */
    public void queryMigrationInfoByAgentNo(MigrationInfo migrationInfo) {
        String agentNo = migrationInfo.getAgentNo();
        int kyAgentInfo = agentInfoService.count(new LambdaQueryWrapper<KYAgentInfo>().eq(KYAgentInfo::getAgentNo, agentNo));
        Optional.of(kyAgentInfo).filter(x -> x > 0).map(
                y -> {
                    //查询一代服务商有多少个要迁移的下级 status为TRUE roleType为AGENT_SUB分支机构 并且父链包含一代编号
                    int agents = agentInfoService.count(new LambdaQueryWrapper<KYAgentInfo>()
                            .eq(KYAgentInfo::getStatus, ConstantEnum.TRUE.getValue())
                            .eq(KYAgentInfo::getRoleType, KYAgentInfoRoleType.AGENT_SUB.getCode())
//                            .in(KYAgentInfo::getRoleType, Lists.newArrayList(KYAgentInfoRoleType.AGENT_SUB.getCode(), KYAgentInfoRoleType.SALE.getCode()))
                            .likeRight(KYAgentInfo::getParentAgentLine, "-" + agentNo + "-"));
                    //查询一代服务商及其下级有多少个要迁移的账号信息 agentNo为一代编号 类型不为RECOMMENDER 状态不为DELETE
                    int operas = operatorService.count(new LambdaQueryWrapper<KYOperator>()
                            .eq(KYOperator::getAgentNo, agentNo)
                            .ne(KYOperator::getOperatorType, OperatorTypeEnum.RECOMMENDER.getCode())
                            .ne(KYOperator::getStatus, ConstantEnum.DELETE.getValue()));
                    //填写每个查询结果
                    migrationInfo.setSubAgentSum(agents);
                    migrationInfo.setSubAgentSuccSum(0);
                    migrationInfo.setAccountSum(operas);
                    migrationInfo.setAccountSuccSum(0);
                    return y;
                }).orElseThrow(() -> new RuntimeException("-----迁移服务商不存在-----"));
    }

    /**
     * 根据一代服务商编号 查询出要迁移的下级服务商业务对象集合，服务商对象中关联着账号信息
     *
     * @param agentNo 一代服务商编号
     * @return
     */
    public List<KYAgentInfo> queryMigrationSubAgentList(String agentNo) {
        return agentInfoService.queryAgentInfoListLikeParentAgentLine("-" + agentNo + "-");
    }

    /**
     * 根据一代服务商编号 查询出全部下级
     *
     * @param agentNo 一代服务商编号
     * @return
     */
    public List<KYAgentInfo> queryMigrationSubAgents(String agentNo) {
        return agentInfoService.list(new LambdaQueryWrapper<KYAgentInfo>()
                .eq(KYAgentInfo::getStatus, ConstantEnum.TRUE.getValue())
                .in(KYAgentInfo::getRoleType, Lists.newArrayList(KYAgentInfoRoleType.AGENT_SUB.getCode(), KYAgentInfoRoleType.SALE.getCode()))
                .likeRight(KYAgentInfo::getParentAgentLine, "-" + agentNo + "-"));
    }


    /**
     * 查询迁移 卡友一代服务商信息
     */
    public KYAgentInfo queryKyAgentInfoByAgentNo(String agentNo) {
        return agentInfoService.getOne(new LambdaQueryWrapper<KYAgentInfo>().eq(KYAgentInfo::getAgentNo, agentNo));
    }

    /**
     * 查询迁移 卡友一代服务商企业信息
     */
    public KYEnterpriseInfo queryKYEnterpriseInfoByAgentNo(String agentNo) {
        return enterpriseInfoService.getOne(new LambdaQueryWrapper<KYEnterpriseInfo>().eq(KYEnterpriseInfo::getAgentNo, agentNo));
    }

    /**
     * 查询迁移 卡友一代服务商收货地址信息 不包含删除的
     */
    public List<KYExpressAddress> queryKYExpressAddressByAgentNo(String agentNo) {
        return expressAddressService.list(new LambdaQueryWrapper<KYExpressAddress>().eq(KYExpressAddress::getAgentNo, agentNo)
                .eq(KYExpressAddress::getStatus, ConstantEnum.TRUE.getValue()));
    }

    /**
     * 查询迁移 卡友一代服务商的账号信息 结果集包含 一代自己的超级管理员和一代的操作员 不包含删除的账号 不包含合伙人帐号
     */
    public List<KYOperator> queryKYOperatorByAgentNo(String agentNo) {
        return operatorService.list(new LambdaQueryWrapper<KYOperator>().eq(KYOperator::getSelfAgentNo, agentNo)
                .ne(KYOperator::getOperatorType, OperatorTypeEnum.RECOMMENDER.getCode())
                .ne(KYOperator::getStatus, ConstantEnum.DELETE.getValue()));
    }


    /**
     * 查询卡友服务商库 服务商的业务迁移信息
     *
     * @param migrationInfo
     * @param queryList
     */
    public void queryMigrationInfoByAgentNoBusiness(MigrationInfo migrationInfo, List<KYAgentInfo> queryList) {
        //查询服务商的费率设置迁移数量
        int acticityFeeSum = Optional.ofNullable(queryList).filter(CollectionUtils::isNotEmpty).map(
                list -> {
                    List<Integer> ints = Lists.newArrayList();
                    list.stream().forEach(
                            info -> {
                                int res = agentPosActivityFeeService.count(
                                        new LambdaQueryWrapper<KYAgentPosActivityFee>()
                                                .eq(KYAgentPosActivityFee::getAgentNo, info.getAgentNo()));
                                ints.add(res);
                            }
                    );
                    return Optional.ofNullable(ints).filter(CollectionUtils::isNotEmpty)
//                            .map(x -> {
                                //INT集合元素求和
//                                .map(x->x.stream().mapToInt(Integer::intValue).sum()).orElse(0);
                                .map(x->x.stream().reduce(0,Integer::sum)).orElse(0);
//                                AtomicInteger result = new AtomicInteger(0);
//                                x.stream().forEach(y -> result.addAndGet(y));
//                                return result.intValue();
//                            }).orElse(0);
                }
        ).orElse(0);
        migrationInfo.setActicityFeeSum(acticityFeeSum);
        migrationInfo.setActicityFeeSuccSum(0);

    }

    /**
     * 查询需要迁移的服务商费率配置信息
     *
     * @param queryList
     * @return
     */
    public List<KYAgentPosActivityFee> queryKYAgentPosActivityFeeList(List<KYAgentInfo> queryList) {
        return Optional.ofNullable(queryList).filter(CollectionUtils::isNotEmpty).map(
                list -> {
                    List<KYAgentPosActivityFee> result = Lists.newArrayList();
                    list.stream().forEach(
                            info -> {
                                List<KYAgentPosActivityFee> fees=agentPosActivityFeeService.list(
                                        new LambdaQueryWrapper<KYAgentPosActivityFee>()
                                                .eq(KYAgentPosActivityFee::getAgentNo, info.getAgentNo()));
                                Optional.ofNullable(fees).filter(CollectionUtils::isNotEmpty)
                                        .map(x->result.addAll(x));
                            }
                    );
                    return result;
                }
        ).orElse(null);
    }
}
