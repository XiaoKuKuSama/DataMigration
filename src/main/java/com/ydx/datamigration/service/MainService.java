package com.ydx.datamigration.service;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ydx.datamigration.enums.*;
import com.ydx.datamigration.model.ky.*;
import com.ydx.datamigration.model.ydx.*;
import com.ydx.datamigration.service.ky.agentsrv.KYAgentSrvService;
import com.ydx.datamigration.service.ky.pospboss.KYPospBossService;
import com.ydx.datamigration.service.ydx.agentproduct.YDXAgentProductService;
import com.ydx.datamigration.service.ydx.agentsrv.YDXAgentSrvService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 迁移系统主service  引用4个数据源对应的事物servcie 处理迁移逻辑
 */
@Component
public class MainService {

    private static final Logger logger = LoggerFactory.getLogger(MainService.class);

    @Autowired
    private KYAgentSrvService kyAgentSrvService;

    @Autowired
    private KYPospBossService kyPospBossService;

    @Autowired
    private YDXAgentSrvService ydxAgentSrvService;

    @Autowired
    private YDXAgentProductService ydxAgentProductService;


    /**
     * 初始化迁移服务商业务信息列表
     *
     * @param listStrArr excel解析的上传列表 每个String数组的第一个元素为服务商编号
     * @return 初始化服务商数量
     */
    public int initMigrationAgentsBussiness(List<String[]> listStrArr) {
        logger.info("-----初始化服务商业务迁移信息开始,迁移数量={}个!-----", listStrArr.size());
        //初始化迁移信息集合
        List<MigrationInfo> migrationList = Collections.synchronizedList(Lists.newLinkedList());
        //验证传入参数listStrArr 多线程初始化每一个服务商的迁移信息
        Optional.ofNullable(listStrArr).filter(CollectionUtils::isNotEmpty)
                .map(x -> {
                    x.parallelStream().forEach(
                            strArr -> {
                                MigrationInfo info = initMigrationByAgentNoBusiness(strArr[0]);
                                Optional.ofNullable(info).map(y -> migrationList.add(y));
                            });
                    return null;
                });
        logger.info("-----查询服务商业务迁移信息完成,个数={}个!-----", migrationList.size());
        //保存服务商迁移信息
        Optional.of(migrationList).filter(CollectionUtils::isNotEmpty)
                .map(x -> {
                    boolean res = ydxAgentSrvService.updateMigrationInfoList(x);
                    Optional.ofNullable(res).filter(bo -> bo == BooleanEnum.TRUE.isValue())
                            .orElseThrow(() -> {
                                logger.error("-----保存服务商业务迁移信息失败-----");
                                return new RuntimeException("保存服务商业务迁移信息失败");
                            });
                    return null;
                });
        logger.info("-----初始化服务商业务迁移信息结束,初始化成功={}个!-----", migrationList.size());
        return migrationList.size();
    }

    /**
     * 初始化每个服务商的业务迁移信息
     *
     * @return MigrationInfo
     */
    private MigrationInfo initMigrationByAgentNoBusiness(String agentNo) {
        logger.info("-----初始化服务商业务迁移信息开始,agentNo={}-----", agentNo);
        MigrationInfo migrationInfo;
        try {
            migrationInfo = Optional.ofNullable(agentNo).filter(StringUtils::isNotEmpty)
                    .map(no -> ydxAgentSrvService.queryMigrationInfoByAgentNo(no))
                    .orElseThrow(() -> new RuntimeException("-----迁移服务商编号为空-----"));
            Optional.ofNullable(migrationInfo).map(
                    info -> {
                        //查询迁移服务商
                        KYAgentInfo agent = kyAgentSrvService.queryKyAgentInfoByAgentNo(migrationInfo.getAgentNo());
                        //查询迁移服务商的全部下级
                        List<KYAgentInfo> agentSubList = kyAgentSrvService.queryMigrationSubAgents(migrationInfo.getAgentNo());
                        List<KYAgentInfo> queryList = Lists.newArrayList(agent);
                        queryList.addAll(agentSubList);
                        //查询卡友服务商库 服务商的业务迁移信息
                        kyAgentSrvService.queryMigrationInfoByAgentNoBusiness(migrationInfo, queryList);
                        //查询卡友运营库 服务商的业务迁移信息
                        kyPospBossService.queryMigrationInfoByAgentNoBusiness(migrationInfo);
                        migrationInfo.setStatus(ConstantEnum.INIT.getValue());
                        migrationInfo.setUpdateTime(new Date());
                        return info;
                    }
            ).orElseThrow(() -> new RuntimeException("-----迁移服务商不存在-----"));
        } catch (Exception e) {
            logger.error("-----初始化服务商业务迁移信息失败,agentNo={}-----error={}", agentNo,
                    Throwables.getStackTraceAsString(e));
            return null;
        }
        logger.info("-----初始化服务商业务迁移信息成功,agentNo={}-----", agentNo);
        return migrationInfo;
    }


    /**
     * 业务信息迁移方法
     *
     * @return 迁移服务商数量
     */
    public int migrationBusiness() {
        //查询迁移信息为INIT和FAIL的服务商迁移信息
        List<MigrationInfo> migrationInfos = ydxAgentSrvService.queryMigrationInfoList();
        AtomicInteger index = new AtomicInteger(0);
        Optional.ofNullable(migrationInfos).filter(CollectionUtils::isNotEmpty)
                .map(list -> {
                    logger.info("-----查询出需业务迁移的服务商{}个-----", migrationInfos.size());
                    list.parallelStream().forEach(
                            x -> {
                                boolean res = migrationAgentBusiness(x);
                                if (res) {
                                    index.incrementAndGet();
                                }
                            }
                    );
                    return null;
                });
        logger.info("-----迁移成功{}个服务商-----", index.intValue());
        return index.intValue();
    }

    /**
     * 业务迁移每个服务商业务信息
     *
     * @param migrationInfo
     * @return
     */
    private boolean migrationAgentBusiness(MigrationInfo migrationInfo) {
        //初始化迁移信息对象
        String caseNode = "";
        String agentNo = "";
        KYAgentInfo agentInfo;
        List<KYAgentInfo> agentSubList;
        List<KYAgentInfo> queryList;
        List<KYAgentPosActivityFee> kyAgentPosActivityFeeList;
        List<KYPos> kyPosList;
        List<KYAgentPosRelation> kyAgentPosRelationList;
        List<KYAgentCustomerRelation> kyAgentCustomerRelationList;
        try {
            //获取迁移服务商的FailNode 如果没有则默认为INIT
            caseNode = Optional.ofNullable(migrationInfo)
                    .map(info -> Optional.ofNullable(info.getFailNode()).filter(StringUtils::isNotEmpty).orElse("INIT"))
                    .orElse("NULL");
            //迁移一代服务商编号
            agentNo = migrationInfo.getAgentNo();
            //查询迁移服务商
            agentInfo = kyAgentSrvService.queryKyAgentInfoByAgentNo(agentNo);
            //查询迁移服务商的全部下级
            agentSubList = kyAgentSrvService.queryMigrationSubAgents(migrationInfo.getAgentNo());
            queryList = Lists.newArrayList(agentInfo);
            queryList.addAll(agentSubList);
            //根据caseNode选择从哪步开始迁移
            switch (caseNode) {
                case "INIT":
                    logger.info("-----agentNo={},从头开始迁移-----", agentNo);
                case "ACTICITYFEE":
                    logger.info("-----agentNo={},服务商费率配置信息开始迁移-----", agentNo);
                    caseNode = "ACTICITYFEE";

                    kyAgentPosActivityFeeList = kyAgentSrvService.queryKYAgentPosActivityFeeList(queryList);
                    List<YDXAgentPosActivityFee> ydxAgentPosActivityFeeList = KY2YDXAgentPosActivityFeeList(kyAgentPosActivityFeeList);
                    ydxAgentProductService.saveYDXAgentPosActivityFeeList(ydxAgentPosActivityFeeList);

                    migrationInfo.setActicityFeeSuccSum(ydxAgentPosActivityFeeList.size());
                    logger.info("-----agentNo={},服务商费率配置信息迁移成功-----", agentNo);
                case "POS":
                    logger.info("-----agentNo={},服务商终端信息开始迁移-----", agentNo);
                    caseNode = "POS";

                    kyPosList = kyPospBossService.queryKYPosListByAgentNo(agentNo);
                    List<YDXPos> ydxPosList = KY2YDXPosList(kyPosList);
                    ydxAgentProductService.saveYDXPosList(ydxPosList);

                    migrationInfo.setPosSuccSum(ydxPosList.size());
                    logger.info("-----agentNo={},服务商终端信息迁移成功-----", agentNo);
                case "POSRELATION":
                    logger.info("-----agentNo={},服务商终端关系信息开始迁移-----", agentNo);
                    caseNode = "POSRELATION";

                    kyAgentPosRelationList = kyPospBossService.queryKYAgentPosRelationList(agentNo);
                    List<YDXAgentPosRelation> ydxAgentPosRelationList
                            = KY2YDXAgentPosRelationList(kyAgentPosRelationList);

                    ydxAgentProductService.saveYDXAgentPosRelationList(ydxAgentPosRelationList);

                    migrationInfo.setPosRelationSuccSum(ydxAgentPosRelationList.size());
                    logger.info("-----agentNo={},服务商终端关系信息迁移成功-----", agentNo);
                case "CUSTOMERRELATION":
                    logger.info("-----agentNo={},服务商商户关系信息开始迁移-----", agentNo);
                    caseNode = "CUSTOMERRELATION";

                    kyAgentCustomerRelationList = kyPospBossService.querykyAgentCustomerRelationList(agentNo);
                    List<YDXAgentCustomerRelation> ydxAgentCustomerRelationList
                            = KY2YDXYDXAgentCustomerRelationList(kyAgentCustomerRelationList);

                    ydxAgentProductService.saveYDXAgentCustomerRelationList(ydxAgentCustomerRelationList);

                    migrationInfo.setCustomerRelationSuccSum(ydxAgentCustomerRelationList.size());
                    logger.info("-----agentNo={},服务商商户关系信息迁移成功-----", agentNo);
                    break;
                case "NULL":
                    logger.error("-----传入的服务商迁移信息为空-----");
                    return BooleanEnum.FALSE.isValue();
            }
        } catch (Exception e) {
            logger.error("-----服务商={}在{}步迁移失败-----", agentNo, caseNode);
            logger.error("-----错误信息{}-----", Throwables.getStackTraceAsString(e));
            migrationInfo.setStatus(ConstantEnum.FAIL.getValue());
            migrationInfo.setUpdateTime(new Date());
            migrationInfo.setFailNode(caseNode);
            migrationInfo.setFailSum(migrationInfo.getFailSum() + 1);
            ydxAgentSrvService.updateMigrationInfo(migrationInfo);
            return BooleanEnum.FALSE.isValue();
        }
        logger.info("-----服务商={}整体业务迁移成功-----", agentNo);
        migrationInfo.setStatus(ConstantEnum.SUCCESS.getValue());
        migrationInfo.setUpdateTime(new Date());
        migrationInfo.setBusinessSuccessTime(new Date());
        ydxAgentSrvService.updateMigrationInfo(migrationInfo);
        return BooleanEnum.TRUE.isValue();
    }

    private List<YDXAgentCustomerRelation> KY2YDXYDXAgentCustomerRelationList(
            List<KYAgentCustomerRelation> kyAgentCustomerRelationList) {
        return Optional.ofNullable(kyAgentCustomerRelationList).filter(CollectionUtils::isNotEmpty)
                .map(kyList -> {
                    List<YDXAgentCustomerRelation> toList = Lists.newArrayList();
                    kyList.stream().forEach(
                            from -> {
                                YDXAgentInfo ydxAgentInfo = ydxAgentSrvService.queryYDXAgentInfoByAgentNo(from.getSelfAgentNo());
                                KYCustomer customer = from.getKyCustomer();
                                YDXAgentCustomerRelation to = new YDXAgentCustomerRelation();
                                to.setAgentNo(from.getAgentNo());
                                to.setCustomerNo(from.getCustomerNo());
                                if (ydxAgentInfo == null) {
                                    YDXSysUser user = ydxAgentSrvService.queryYDXSysUserByKYAgentNo(from.getSelfAgentNo());
                                    YDXAgentInfo parentYDXAgentInfo =
                                            ydxAgentSrvService.queryYDXAgentInfoByAgentNo(user.getAgentNo());
                                    to.setDirectAgentNo(user.getAgentNo());
                                    to.setAgentLine(parentYDXAgentInfo.getParentAgentLine());
                                    to.setSaleNo(user.getUserNo());
                                } else {
                                    to.setDirectAgentNo(from.getSelfAgentNo());
                                    to.setAgentLine(ydxAgentInfo.getParentAgentLine());
                                    to.setSaleNo("000000");
                                }
                                to.setCreateTime(from.getCreateTime());
                                to.setUpdateTime(from.getUpdateTime());
                                to.setCreateUser("migration");
                                Optional.ofNullable(customer).ifPresent(
                                        c -> {
                                            String type=Optional.ofNullable(c.getMcc()).filter(StringUtils::isNotEmpty)
                                                    .map(mcc -> {
                                                        String res;
                                                        if (mcc.startsWith("999")) {
                                                            res="MPOS";
                                                        } else {
                                                            res="DPOS";
                                                        }
                                                        return res;
                                                    }).orElse("");
                                            to.setCustomerType(type);
                                            to.setProductBrand(c.getDockingOrganization());
                                            to.setFullName(c.getFullName());
                                            to.setPhoneNo(c.getPhoneNo());
                                            to.setStatus(c.getStatus());
                                            to.setLinkman(c.getLinkman());
                                            to.setLegalPerson(c.getLegalPerson());
                                            to.setOpenTime(c.getOpenTime());
                                            to.setUserSalesType(null);
                                            to.setTargetType(null);
                                            to.setCustomerSource(c.getCustomerType());
                                            to.setNewFlag("Y");
                                        }
                                );
                                toList.add(to);
                            }
                    );
                    return toList;
                }).orElse(null);

    }

    private List<YDXAgentPosRelation> KY2YDXAgentPosRelationList(List<KYAgentPosRelation> kyAgentPosRelationList) {
        return Optional.ofNullable(kyAgentPosRelationList).filter(CollectionUtils::isNotEmpty)
                .map(kyList -> {
                    List<YDXAgentPosRelation> toList = Lists.newArrayList();
                    kyList.stream().forEach(
                            from -> {
                                YDXAgentInfo ydxAgentInfo = ydxAgentSrvService.queryYDXAgentInfoByAgentNo(from.getOwnerAgentNo());
                                YDXAgentPosRelation to = new YDXAgentPosRelation();
                                to.setPosSn(from.getPosSn());
                                to.setAgentNo(from.getAgentNo());
                                if (ydxAgentInfo == null) {
                                    YDXSysUser user = ydxAgentSrvService.queryYDXSysUserByKYAgentNo(from.getOwnerAgentNo());
                                    YDXAgentInfo parentYDXAgentInfo =
                                            ydxAgentSrvService.queryYDXAgentInfoByAgentNo(user.getAgentNo());
                                    to.setDirectAgentNo(user.getAgentNo());
                                    to.setAgentLine(parentYDXAgentInfo.getParentAgentLine());
                                    to.setSaleNo(user.getUserNo());
                                } else {
                                    to.setDirectAgentNo(from.getOwnerAgentNo());
                                    to.setAgentLine(ydxAgentInfo.getParentAgentLine());
                                    to.setSaleNo(null);
                                }
                                to.setCreateTime(from.getCreateTime());
                                to.setUpdateTime(from.getUpdateTime());
                                to.setCreateUser("migration");
                                toList.add(to);
                            }
                    );
                    return toList;
                }).orElse(null);

    }

    /**
     * 卡友->易多销 服务商终端信息转换
     *
     * @param kyPosList
     * @return
     */
    private List<YDXPos> KY2YDXPosList(List<KYPos> kyPosList) {
        return Optional.ofNullable(kyPosList).filter(CollectionUtils::isNotEmpty)
                .map(list -> {
                    List<YDXPos> ydxPosList = Lists.newArrayList();
                    list.stream().forEach(
                            from -> {
                                YDXPos to = new YDXPos();
                                to.setPosSn(from.getPosSn());
                                to.setPosCati(from.getPosCati());
                                to.setPosType(from.getType());
                                to.setPosOwner(from.getPosOwner());
                                to.setAgentNo(from.getAgentNo());
                                to.setCustomerNo(from.getCustomerNo());
                                to.setShopNo(from.getShopNo());
                                to.setStatus(from.getStatus());
                                to.setUseType(from.getUseType());
                                to.setPurchaseAgentNo(from.getAgentNo()); //采购服务商编号
                                to.setProductBrand(Optional.ofNullable(from.getDockingOrganization()).orElse("29000000")); //所属机构
                                to.setCreateTime(from.getCreateTime());
                                to.setUpdateTime(null);
                                ydxPosList.add(to);
                            });
                    return ydxPosList;
                })
                .orElse(null);
    }

    /**
     * 卡友->易多销 服务商活动费率配置转换
     *
     * @param kyAgentPosActivityFeeList
     * @return
     */
    private List<YDXAgentPosActivityFee> KY2YDXAgentPosActivityFeeList(List<KYAgentPosActivityFee> kyAgentPosActivityFeeList) {
        return Optional.ofNullable(kyAgentPosActivityFeeList)
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> {
                    List<YDXAgentPosActivityFee> result = Lists.newArrayList();
                    list.stream().forEach(fee -> result.add(KY2YDXAgentPosActivityFee(fee)));
                    return result;
                }).orElse(null);
    }

    private YDXAgentPosActivityFee KY2YDXAgentPosActivityFee(KYAgentPosActivityFee fee) {
        return Optional.ofNullable(fee).map(
                from -> {
                    YDXAgentPosActivityFee to = new YDXAgentPosActivityFee();
                    to.setAgentNo(from.getAgentNo());
                    to.setRate(from.getRate());
                    to.setCreateTime(from.getCreateTime());
                    to.setUpdateTime(from.getModifyTime());
                    to.setStatus(from.getStatus());
                    to.setActivityCode(from.getActivityCode());
                    to.setActivityName(getActivityName(to.getActivityCode()));
                    return to;
                }
        ).orElseThrow(() -> new RuntimeException("------服务商活动费率配置为空------"));
    }

    private String getActivityName(String activityCode) {
        return Optional.ofNullable(activityCode).filter(StringUtils::isNotEmpty)
                .map(code -> {
                    String name = "";
                    switch (code) {
                        case "ISHUA_ACTIVITY_V2":
                            name = "友刷2.0活动";
                            break;
                        case "ISHUA_ACTIVITY_V1":
                            name = "友刷1.0活动";
                            break;
                        case "ISHUA170511_POS_FWSHK":
                            name = "友刷MPOS回馈";
                            break;
                        case "KY_ISHUA_20170901_MPOS":
                            name = "2017Q4、2018Q1Mpos政策";
                            break;
                        case "LFKC-201803070001":
                            name = "LF库存机具活动";
                            break;
                        case "HDBH-20180302170934":
                            name = "18Q1营销活动";
                            break;
                        case "ISHUA170615_POS_GT5W_V1_2":
                            name = "友刷1.5活动";
                            break;
                    }
                    return name;
                }).orElse(null);
    }


    /**
     * 初始化迁移服务商列表
     *
     * @param listStrArr excel解析的上传列表 每个String数组的第一个元素为服务商编号
     * @return 初始化服务商数量
     */
    public int initMigrationAgents(List<String[]> listStrArr) {
        logger.info("-----初始化服务商迁移信息开始,迁移数量={}个!-----", listStrArr.size());
        //初始化迁移信息集合
        List<MigrationInfo> migrationList = Collections.synchronizedList(Lists.newLinkedList());
        //验证传入参数listStrArr 多线程初始化每一个服务商的迁移信息
        Optional.ofNullable(listStrArr).filter(CollectionUtils::isNotEmpty)
                .map(x -> {
                    x.parallelStream().forEach(
                            strArr -> {
                                MigrationInfo info = initMigrationByAgentNo(strArr[0]);
                                Optional.ofNullable(info).map(y -> migrationList.add(y));
                            });
                    return null;
                });
        logger.info("-----查询服务商迁移信息完成,个数={}个!-----", migrationList.size());
        //保存服务商迁移信息
        Optional.of(migrationList).filter(CollectionUtils::isNotEmpty)
                .map(x -> {
                    boolean res = ydxAgentSrvService.saveMigrationInfoList(x);
                    Optional.ofNullable(res).filter(bo -> bo == BooleanEnum.TRUE.isValue())
                            .orElseThrow(() -> {
                                logger.error("-----保存服务商迁移信息失败,保存迁移信息失败-----");
                                return new RuntimeException("保存服务商迁移信息失败,保存迁移信息失败");
                            });
                    return null;
                });
        logger.info("-----初始化服务商迁移信息结束,初始化成功={}个!-----", migrationList.size());
        return migrationList.size();
    }

    /**
     * 初始化每个服务商的迁移信息
     *
     * @return MigrationInfo
     */
    private MigrationInfo initMigrationByAgentNo(String agentNo) {
        logger.info("-----初始化服务商迁移信息开始,agentNo={}-----", agentNo);
        MigrationInfo migrationInfo = new MigrationInfo();
        try {
            Optional.ofNullable(agentNo).filter(StringUtils::isNotEmpty)
                    .map(x -> {
                                migrationInfo.setAgentNo(agentNo);
                                //查询卡友服务商库 服务商的迁移信息
                                kyAgentSrvService.queryMigrationInfoByAgentNo(migrationInfo);
                                //查询卡友运营库 服务商的迁移信息
                                kyPospBossService.queryMigrationInfoByAgentNo(migrationInfo);
                                //添加初始化数据
                                migrationInfo.setStatus(ConstantEnum.INIT.getValue());
                                migrationInfo.setCreateTime(new Date());
                                return x;
                            }
                    ).orElseThrow(() -> new RuntimeException("-----迁移服务商编号为空-----"));
        } catch (Exception e) {
            logger.error("-----初始化服务商迁移信息失败,agentNo={}-----error={}", agentNo,
                    Throwables.getStackTraceAsString(e));
            return null;
        }
        logger.info("-----初始化服务商迁移信息成功,agentNo={}-----", agentNo);
        return migrationInfo;
    }


    /**
     * 基本信息迁移方法
     *
     * @return 迁移服务商数量
     */
    public int migration() {
        //查询迁移信息为INIT和FAIL的服务商迁移信息
        List<MigrationInfo> migrationInfos = ydxAgentSrvService.queryMigrationInfoList();
        AtomicInteger index = new AtomicInteger(0);
        Optional.ofNullable(migrationInfos).filter(CollectionUtils::isNotEmpty)
                .map(list -> {
                    logger.info("-----查询出需迁移的服务商{}个-----", migrationInfos.size());
                    list.parallelStream().forEach(
                            x -> {
                                boolean res = migrationAgent(x);
                                if (res) {
                                    index.incrementAndGet();
                                }
                            }
                    );
                    return null;
                });
        logger.info("-----迁移成功{}个服务商-----", index.intValue());
        return index.intValue();
    }


    /**
     * 迁移每个服务商相关信息
     */
    private boolean migrationAgent(MigrationInfo migrationInfo) {
        //初始化迁移信息对象
        String caseNode = "";
        String agentNo = "";
        KYAgentInfo kyAgentInfo;
        KYEnterpriseInfo kyEnterpriseInfo;
        List<KYExpressAddress> kyExpressAddressList;
        List<KYOperator> kyOperatorList;
        int agentQualityCount;
        List<KYAgentInfo> kySubAgents;
        try {
            //获取迁移服务商的FailNode 如果没有则默认为INIT
            caseNode = Optional.ofNullable(migrationInfo)
                    .map(info -> Optional.ofNullable(info.getFailNode()).orElse("INIT"))
                    .orElse("NULL");
            //迁移一代服务商编号
            agentNo = migrationInfo.getAgentNo();
            //根据caseNode选择从哪步开始迁移
            switch (caseNode) {
                case "INIT":
                    logger.info("-----agentNo={},从头开始迁移-----", agentNo);
                case "AGENT":
                    logger.info("-----agentNo={},服务商信息开始迁移-----", agentNo);
                    caseNode = "AGENT";
                    //查询卡友迁移的相关信息  FROM kyAgentInfo一代信息 kyEnterpriseInfo企业信息 kyOperatorList一代账号和操作员账号信息 agentQualityCount是不是优质服务商
                    kyAgentInfo = kyAgentSrvService.queryKyAgentInfoByAgentNo(agentNo);
                    kyEnterpriseInfo = kyAgentSrvService.queryKYEnterpriseInfoByAgentNo(agentNo);
                    kyOperatorList = kyAgentSrvService.queryKYOperatorByAgentNo(agentNo);
                    agentQualityCount = kyPospBossService.queryAgentQualityByAgentNo(agentNo);

                    //组装成易多销入库的对象信息  TO
                    YDXAgentInfo ydxAgentInfo = KY2YDXAgentInfo(kyAgentInfo);
                    YDXAgentInfoExt ydxAgentInfoExt = KY2YDXAgentInfoExt(agentQualityCount, agentNo);
                    YDXEnterpriseInfo ydxEnterpriseInfo = KY2YDXEnterpriseInfo(kyEnterpriseInfo);
                    List<YDXSysUser> ydxSysUsers = KY2YDXSysUserAgent(kyOperatorList, kyAgentInfo);
                    YDXSysAgent ydxSysAgent = initYDXSysAgent(kyAgentInfo);
                    List<YDXSysOrganUser> sysOrganUsers = initYDXSysOrganUsers(ydxSysUsers);
                    //初始化YDX团队信息
                    List<YDXSysOrgan> ydxSysOrgans = initYDXSysOrgans(kyAgentInfo);

                    //初始化一代账号的权限信息
                    List<YDXSysRoleUser> ydxSysRoleUsers = getMigrationSysRoleUserList(ydxSysUsers);


                    //统一保存一代相关迁移信息
                    ydxAgentSrvService.saveMigrationAgent(ydxAgentInfo, ydxEnterpriseInfo, ydxSysUsers, ydxAgentInfoExt, ydxSysRoleUsers, ydxSysAgent, ydxSysOrgans, sysOrganUsers);
                    //成功迁移账号数量 增加
                    migrationInfo.setAccountSuccSum(migrationInfo.getAccountSuccSum() + ydxSysUsers.size());
                    logger.info("-----agentNo={},服务商信息迁移成功-----", agentNo);
                case "EXPADD":
                    logger.info("-----agentNo={},地址信息开始迁移-----", agentNo);
                    caseNode = "EXPADD";
                    //查询卡友迁移的相关信息  FROM
                    kyExpressAddressList = kyAgentSrvService.queryKYExpressAddressByAgentNo(agentNo);

                    //组装成易多销入库的对象信息   TO
                    List<YDXExpressAddress> ydxExpressAddress = KY2YDXExpressAddress(kyExpressAddressList);

                    //保存地址信息
                    ydxAgentProductService.saveYDXExpressAddress(ydxExpressAddress);
                    logger.info("-----agentNo={},地址信息迁移成功-----", agentNo);
                case "SUBAGENT":
                    logger.info("-----agentNo={},从下级服务商信息开始迁移-----", migrationInfo.getAgentNo());
                    caseNode = "SUBAGENT";
                    //查询卡友迁移的相关信息  FROM
                    //查询此一代服务商需要迁移的下级信息 关联 账号 信息   下级信息-账号 一对一
                    kySubAgents = kyAgentSrvService.queryMigrationSubAgentList(agentNo);

                    //组装成易多销入库对象信息 TO
                    List<YDXAgentInfo> ydxSubAgentInfos = Collections.synchronizedList(Lists.newLinkedList());
                    List<YDXSysUser> ydxSysUsersSub = Collections.synchronizedList(Lists.newLinkedList());
                    List<YDXSysAgent> ydxSysAgents = Collections.synchronizedList(Lists.newLinkedList());
                    List<YDXSysOrgan> ydxSysOrgansSub = Collections.synchronizedList(Lists.newLinkedList());
                    KY2YDXSubAgentAndSysUser(kySubAgents, ydxSubAgentInfos, ydxSysUsersSub, ydxSysAgents, ydxSysOrgansSub);

                    //处理所有节点的销售账号的userNo 一代需要查出最大的userno然后继续加 下级的直接从1开始
                    List<YDXSysUser> resultYDXSysUsersSub = ydxSysUserNoTransform(ydxSysUsersSub, agentNo);

                    //初始化所有迁移下级账号的权限信息
                    List<YDXSysRoleUser> subSysRoleUsers = getMigrationSysRoleUserList(resultYDXSysUsersSub);

                    //初始化sysOrganUser信息
                    List<YDXSysOrganUser> sysOrganUsersSub = initYDXSysOrganUsers(resultYDXSysUsersSub);

                    //保存易多销下级信息和下级账号信息
                    ydxAgentSrvService.saveMigrationSubAgent(ydxSubAgentInfos, resultYDXSysUsersSub, subSysRoleUsers, ydxSysAgents, ydxSysOrgansSub, sysOrganUsersSub);
                    //成功迁移下级和账号数量 增加
                    migrationInfo.setSubAgentSuccSum(ydxSubAgentInfos.size());
                    migrationInfo.setAccountSuccSum(migrationInfo.getAccountSuccSum() + ydxSysUsersSub.size());
                    logger.info("-----agentNo={},下级和下级账号信息迁移成功-----", agentNo);
                    break;
                case "NULL":
                    logger.error("-----传入的服务商迁移信息为空-----");
                    return BooleanEnum.FALSE.isValue();
            }
        } catch (Exception e) {
            logger.error("-----服务商={}在{}步迁移失败-----", agentNo, caseNode);
            logger.error("-----错误信息{}-----", Throwables.getStackTraceAsString(e));
            migrationInfo.setStatus(ConstantEnum.FAIL.getValue());
            migrationInfo.setUpdateTime(new Date());
            migrationInfo.setFailNode(caseNode);
            migrationInfo.setFailSum(migrationInfo.getFailSum() + 1);
            ydxAgentSrvService.updateMigrationInfo(migrationInfo);
            return BooleanEnum.FALSE.isValue();
        }
        logger.info("-----服务商={}整体迁移成功-----", agentNo);
        migrationInfo.setStatus(ConstantEnum.SUCCESS.getValue());
        migrationInfo.setUpdateTime(new Date());
        migrationInfo.setSuccessTime(new Date());
        ydxAgentSrvService.updateMigrationInfo(migrationInfo);
        return BooleanEnum.TRUE.isValue();
    }

    private List<YDXSysOrganUser> initYDXSysOrganUsers(List<YDXSysUser> ydxSysUsers) {
        return Optional.ofNullable(ydxSysUsers)
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> {
                    List<YDXSysOrganUser> sysOrganUsers = Lists.newArrayList();
                    list.stream().forEach(
                            sysUser -> {
                                YDXSysOrganUser organUser = new YDXSysOrganUser();
                                organUser.setAgentNo(sysUser.getAgentNo());
                                organUser.setUsername(sysUser.getUsername());
                                organUser.setOrganCode(sysUser.getOrganCode());
                                organUser.setStatus("Y");
                                organUser.setCreateTime(new Date());
                                organUser.setInTime(new Date());
                                organUser.setCreateUser("migration");
                                sysOrganUsers.add(organUser);
                            }
                    );
                    return sysOrganUsers;
                })
                .orElse(null);
    }

    /**
     * 初始化某个agentinfo节点的三个团队信息
     *
     * @param kyAgentInfo
     * @return
     */
    private List<YDXSysOrgan> initYDXSysOrgans(KYAgentInfo kyAgentInfo) {
        return Optional.ofNullable(kyAgentInfo).map(
                x -> {
                    //ROOT团队
                    YDXSysOrgan rootOrgan = this.createRootOrgan(kyAgentInfo.getAgentNo(), new Date(), "migration");
                    // 直销团队
                    YDXSysOrgan directOrgan = this.createOrgan(rootOrgan, OrganCodeType.AGENT_DIRECT, new Date(), "migration", 1);
                    // 分销团队
                    YDXSysOrgan distribOrgan = this.createOrgan(rootOrgan, OrganCodeType.AGENT_DISTRIB, new Date(), "migration", 2);

                    List<YDXSysOrgan> sysOrgans = Lists.newArrayList(rootOrgan, directOrgan, distribOrgan);

                    return sysOrgans;
                }
        ).orElse(null);
    }

    private YDXSysOrgan createRootOrgan(String agentNo, Date createTime, String crateUser) {
        YDXSysOrgan organ = new YDXSysOrgan();
        organ.setAgentNo(agentNo);
        organ.setTypeCode(null);
        organ.setParentPath("-" + OrganCodeType.ROOT.getCode());
        organ.setParentCode(OrganCodeType.ROOT.getCode());
        OrganCodeType agentRoot = OrganCodeType.AGENT_ROOT;
        organ.setCode(agentRoot.getCode());
        organ.setName(agentRoot.getName());
        organ.setStatus("NORMAL");
        organ.setOrders(1);
        organ.setDescription(agentRoot.getDescription());
        organ.setCreateUser(crateUser);
        organ.setCreateTime(createTime);
        return organ;
    }

    /**
     * 创建ROOT机构下级
     *
     * @param rootOrgan
     * @param organType
     * @param createTime
     * @param crateUser
     * @param slot
     * @return
     */
    private YDXSysOrgan createOrgan(YDXSysOrgan rootOrgan,
                                    OrganCodeType organType,
                                    Date createTime, String crateUser, Integer slot) {
        YDXSysOrgan organ = new YDXSysOrgan();
        organ.setAgentNo(rootOrgan.getAgentNo());
        organ.setTypeCode(null);
        organ.setParentPath(rootOrgan.getParentPath() + "-" + rootOrgan.getCode());
        organ.setParentCode(rootOrgan.getCode());
        organ.setCode(organType.getCode());
        organ.setName(organType.getName());
        organ.setDescription(organType.getDescription());
        organ.setStatus("NORMAL");
        organ.setOrders(slot);
        organ.setCreateUser(crateUser);
        organ.setCreateTime(createTime);
        return organ;
    }


    /**
     * 初始化YDX的权限数据
     *
     * @param kyAgentInfo
     * @return
     */
    private YDXSysAgent initYDXSysAgent(KYAgentInfo kyAgentInfo) {
        return Optional.ofNullable(kyAgentInfo).map(
                x -> {
                    YDXSysAgent sysAgent = new YDXSysAgent();
                    sysAgent.setAgentNo(x.getAgentNo());
                    sysAgent.setLevel(x.getAgentLevel());
                    sysAgent.setAgentName(x.getFullName());
                    return sysAgent;
                }
        ).orElse(null);
    }


    /**
     * 获取迁移账号 需要的用户账号角色应对关系集合  默认迁移的账号初始化拥有全部角色
     *
     * @param ydxSysUserList
     * @return
     */
    private List<YDXSysRoleUser> getMigrationSysRoleUserList(List<YDXSysUser> ydxSysUserList) {
        return Optional.ofNullable(ydxSysUserList).filter(CollectionUtils::isNotEmpty).map(
                userList -> {
                    List<YDXSysRoleUser> roleUsers = Collections.synchronizedList(Lists.newLinkedList());
                    //获取易多销所有的角色code
                    List<String> roleCodes = ydxAgentSrvService.queryYDXRoles();
                    userList.parallelStream().forEach(
                            user -> {
                                if (user.getUserTypeCode().equals(SysUserTypeEnum.DIRECT_SALE.getCode())) {
                                    roleUsers.add(new YDXSysRoleUser(user.getUsername(), "DIRECT_SALE", "migration", new Date()));
                                    roleUsers.add(new YDXSysRoleUser(user.getUsername(), "DEFULT", "migration", new Date()));
                                    return;
                                }
                                roleCodes.stream().forEach(
                                        code -> {
                                            if (user.getUserTypeCode().equals(SysUserTypeEnum.UNSALE.getCode()) && code.equals("ADMIN"))
                                                return;
                                            roleUsers.add(new YDXSysRoleUser(user.getUsername(), code, "migration", new Date()));
                                        }
                                );
                            }

                    );
                    return roleUsers;
                }
        ).orElse(null);
    }

    /**
     * 补全迁移账号的userNo
     *
     * @param ydxSysUsersSub
     * @param agentNo
     * @return
     */
    private List<YDXSysUser> ydxSysUserNoTransform(List<YDXSysUser> ydxSysUsersSub, String agentNo) {
        //处理所有节点的销售账号的userNo 一代需要查出最大的userno然后继续加 下级的直接从1开始
        List<YDXSysUser> resultList = Lists.newArrayList();
        Optional.ofNullable(ydxSysUsersSub)
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> {
                    //已经处理过的分支机构账号直接添加到resultList中
                    resultList.addAll(list.stream().filter(x -> StringUtils.isNotEmpty(x.getUserNo())).collect(Collectors.toList()));
                    //处理userNo为空的 销售账号 按照agentNo分组
                    List<YDXSysUser> saleSysUserList = list.stream().filter(x -> StringUtils.isEmpty(x.getUserNo())).collect(Collectors.toList());
                    Map<String, List<YDXSysUser>> userMap = Maps.newHashMap();
                    Optional.ofNullable(saleSysUserList)
                            .filter(CollectionUtils::isNotEmpty)
                            .map(
                                    users -> {
                                        users.stream().forEach(
                                                user -> {
                                                    List<YDXSysUser> uList;
                                                    String subAgentNo = user.getAgentNo();
                                                    if (!userMap.containsKey(subAgentNo)) {
                                                        uList = Lists.newArrayList();
                                                        uList.add(user);
                                                        userMap.put(subAgentNo, uList);
                                                    } else {
                                                        uList = userMap.get(subAgentNo);
                                                        uList.add(user);
                                                    }
                                                }
                                        );
                                        return null;
                                    }
                            );
                    //得到了key为agentNo value为List<YDXSysUser>的map
                    //循环处理每个list添加userNo
                    userMap.forEach(
                            (no, lists) -> {
                                //如果处理的是一代的销售账户集合 查询数据库看当前最大的userNo+1，不是一代的则直接为1
                                AtomicInteger index = new AtomicInteger(Optional.of(no).filter(x -> x.equals(agentNo))
                                        .map(y -> ydxAgentSrvService.querySysUserMaxUserNo(no) + 1)
                                        .orElse(1));
                                System.out.println("----------" + index.intValue());
                                lists.stream().forEach(
                                        user -> {
                                            user.setUserNo(String.format("%06d", index.intValue()));
                                            user.setUsername(user.getAgentNo() + user.getUserNo());
                                            resultList.add(user);
                                            index.incrementAndGet();
                                        }
                                );
                            }
                    );
                    return null;
                });
        return resultList;
    }


    /**
     * 卡友一代服务商信息 转 易多销服务商信息
     */
    private YDXAgentInfo KY2YDXAgentInfo(KYAgentInfo kyAgentInfo) {
        return Optional.of(kyAgentInfo).map(
                from -> {
                    YDXAgentInfo to = new YDXAgentInfo();
                    to.setAgentNo(from.getAgentNo());
                    to.setSaleNo(from.getSalesNo()); //一代存入卡友销售编号
                    to.setAgentLevel(Integer.parseInt(from.getAgentLevel()));
                    to.setParentAgentNo(from.getParentAgentNo());
                    to.setParentAgentLine(to.getAgentNo());//易多销父服务商链一代包含自己原卡友为null 易多销一代服务商链结构为 no-no-no
                    to.setAgentType("company");// 代理商类型 company/公司、selfEmployed/个体户、person/个人
                    to.setFullName(from.getFullName());
                    to.setShortName(from.getShortName());
                    to.setQualificationsType(from.getQualificationsType());
                    to.setOrgCode(from.getOrgCode());
                    to.setAddress(from.getAddress());
                    //合同相关 易多销需要重新签合同 迁移时不迁旧合同数据
//                    to.setProtocolNo();to.setProtocolAnnex();to.setProtocolExpireytime();
                    //管理者 负责人 迁移 //原联系人->管理者（手机号为登陆账号）    原客服联系人->业务负责人
                    to.setManagerName(from.getLinkMan());
                    to.setManagerPhone(from.getPhoneNo());
                    to.setManagerEmail(from.getEmail());
                    to.setBussPrincipalName(from.getSupportLinkman());
                    to.setBussPrincipalPhone(from.getSupportLinkphone());
                    to.setBussPrincipalFixedPhone(from.getTelephone());
                    to.setStatus("1");
                    to.setCreateTime(from.getCreateTime());
                    return to;
                }).orElseThrow(() -> new RuntimeException("卡友一代服务商信息转化错误,agent对象为空"));
    }

    /**
     * 返回易多销服务商扩展信息 目前只根据是否在卡友存在优质服务商 创建一条扩展信息
     */
    private YDXAgentInfoExt KY2YDXAgentInfoExt(int agentQualityCount, String agentNo) {
        return Optional.of(agentQualityCount).filter(x -> x >= 1).map(
                y -> {
                    YDXAgentInfoExt ext = new YDXAgentInfoExt();
                    ext.setAgentNo(agentNo);
                    ext.setExtType("PAY");
                    ext.setExtName("ISHIGHQUALITY");
                    ext.setExtValue(ConstantEnum.TRUE.getValue());
                    ext.setCreateTime(new Date());
                    return ext;
                }).orElse(null);
    }

    /**
     * 卡友企业信息 转 易多销企业信息
     */
    private YDXEnterpriseInfo KY2YDXEnterpriseInfo(KYEnterpriseInfo kyEnterpriseInfo) {
        return Optional.ofNullable(kyEnterpriseInfo).map(
                from -> {
                    YDXEnterpriseInfo to = new YDXEnterpriseInfo();
                    to.setAgentNo(from.getAgentNo());
                    to.setEnterpriseName(from.getEnterpriseName());
                    to.setLicenseNo(from.getBusinessLicenseNo());
                    to.setAddress(from.getRealAddress());
                    to.setRegistCapital(from.getRegisteredCapital());
                    to.setRegistDate(from.getCreationDate());
                    to.setTradeBegin(null);//营业起始时间
                    to.setTradeEnd(null);//营业截止时间
                    to.setTradeRange(from.getBusinessScope());
                    to.setLegalName(from.getLegalPerson());
                    to.setLegalCard(from.getIdentityNo());
                    to.setLegalCardAnnexFront(from.getAgentNo() + "/4.jpg");
                    to.setLegalCardAnnexObverse(from.getAgentNo() + "/4.jpg");
                    to.setLegalAddress(from.getIdentityAddress());
//                    to.setBank();//开户行   卡友没有存
//                    to.setBankAccount();//账号  卡友没有存
                    to.setLicense(from.getAgentNo() + "/1.jpg");//营业执照url
                    to.setPermitLicense(from.getAgentNo() + "/5.jpg");//开户许可证url
                    to.setStatus("1");
                    to.setCreateTime(from.getCreateTime());
                    to.setWordSizeName("");//字号
                    to.setPermitNumber("");//核准号
                    return to;
                }).orElseThrow(() -> new RuntimeException("卡友企业信息转化错误,agent对象为空"));
    }

    /**
     * 卡友地址信息 转 易多销地址信息
     */
    private List<YDXExpressAddress> KY2YDXExpressAddress(List<KYExpressAddress> kyExpressAddresses) {
        return Optional.ofNullable(kyExpressAddresses).filter(CollectionUtils::isNotEmpty).map(
                fromList -> {
                    List<YDXExpressAddress> toList = Lists.newArrayList();
                    fromList.stream().forEach(
                            from -> {
                                YDXExpressAddress to = new YDXExpressAddress();
                                to.setOwnerNo(from.getAgentNo());
                                to.setOwnerRole("AGENT");
                                to.setPhoneNo(from.getPhoneNo());
                                to.setLinkMan(from.getLinkMan());
                                to.setReceiveAddress(from.getReceiveAddress());
                                to.setIsDefaultAddress(from.getIsDefaultAddress());
                                to.setCreateTime(from.getCreateTime());
                                to.setUpdateTime(from.getUpdateTime());
                                to.setRemark(from.getRemark());
                                //如果6位地址code不为空 就填入这个值
                                if (StringUtils.isNotEmpty(from.getCPOrgCode())) to.setOrgCode(from.getCPOrgCode());
                                //如果4位地址code不为空 检查to的地址code是否有值 有不填 没有填入4位地址code
                                if (StringUtils.isNotEmpty(from.getOrganizationCode())
                                        && StringUtils.isEmpty(to.getOrgCode()))
                                    to.setOrgCode(from.getOrganizationCode());
                                to.setTelephone(from.getPhoneNo());
                                to.setEmail(from.getEmail());
                                kyPospBossService.KY2YDXExpressAddressForOrgName(to);
                                toList.add(to);
                            }
                    );
                    return toList;
                }).orElse(null);
    }


    /**
     * 卡友一代系统用户信息 转 易多销用户信息
     */
    private List<YDXSysUser> KY2YDXSysUserAgent(List<KYOperator> fromOperator,
                                                KYAgentInfo fromAgentInfo) {
        return Optional.ofNullable(fromOperator).filter(CollectionUtils::isNotEmpty).map(
                fromOperatorList -> {
                    List<YDXSysUser> toList = Lists.newArrayList();
                    AtomicInteger index = new AtomicInteger(1);
                    List<String> flags = Collections.synchronizedList(Lists.newArrayList());
                    fromOperatorList.stream().forEach(
                            from -> {
                                YDXSysUser to = new YDXSysUser();
                                if (from.getOperatorType().equals(OperatorTypeEnum.SUPERADMIN.getCode())) {
                                    if (flags.size() > 0) {
                                        to.setUserTypeCode(SysUserTypeEnum.UNSALE.getCode());
                                        to.setUserNo(String.format("%06d", index.intValue()));
                                        index.incrementAndGet();
                                    } else {
                                        to.setUserTypeCode(SysUserTypeEnum.ADMIN.getCode());
                                        to.setUserNo("000000");
                                        flags.add("1");
                                    }
                                } else {
                                    to.setUserTypeCode(SysUserTypeEnum.UNSALE.getCode());
                                    to.setUserNo(String.format("%06d", index.intValue()));
                                    index.incrementAndGet();
                                }
                                to.setAgentNo(from.getSelfAgentNo());
                                to.setOrganCode("AGENT_ROOT");
                                to.setUsername(to.getAgentNo() + to.getUserNo());
                                to.setPassword(from.getPassword());
                                to.setRealname(from.getRealname());
                                to.setEmail(null);
                                to.setTelephone(null);
                                to.setCellphone(from.getUsername());
                                to.setSex(null);
                                to.setAge(null);
                                to.setAddress(null);
                                to.setPhoto(null);
                                if (from.getStatus().equals(ConstantEnum.TRUE.getValue())) {
                                    to.setUserStatus(SysUserStatusEnum.NORMAL.getName());
                                } else {
                                    to.setUserStatus(SysUserStatusEnum.DISABLED.getName());
                                }
                                to.setOrders(null);
                                to.setDescription("");
                                to.setStatus(SysUserStatusEnum.NORMAL.getName());
                                to.setCreateUser("migration");
                                to.setCreateTime(new Date());
                                to.setUpdateUser(null);
                                to.setUpdateTime(null);
                                to.setKyAgentNo(from.getSelfAgentNo());
                                toList.add(to);
                            }
                    );
                    return toList;
                }).orElseThrow(() -> new NullPointerException("-----一代服务商用户信息集合为空-----"));
    }

    /**
     * 卡友 一代的下级信息和账号  转 易多销 下级 和 账号
     *
     * @param kyAgentInfos 卡友下级信息集合
     * @param toAgentInfos 易多销下级集合
     * @param toSysUsers   易多销下级账号集合
     */
    private void KY2YDXSubAgentAndSysUser(List<KYAgentInfo> kyAgentInfos,
                                          List<YDXAgentInfo> toAgentInfos,
                                          List<YDXSysUser> toSysUsers,
                                          List<YDXSysAgent> ydxSysAgents,
                                          List<YDXSysOrgan> ydxSysOrgansSub) {
        Optional.ofNullable(kyAgentInfos).filter(CollectionUtils::isNotEmpty).map(
                fromKYAgentInfos -> {
                    fromKYAgentInfos.parallelStream().forEach(
                            fromKYAgentInfo -> {
                                //如果是分支机构
                                if (fromKYAgentInfo.getRoleType().equals(KYAgentInfoRoleType.AGENT_SUB.getCode())) {
                                    //转换易多销 销售机构信息
                                    YDXAgentInfo to = new YDXAgentInfo();
                                    to.setAgentNo(fromKYAgentInfo.getAgentNo());
                                    to.setSaleNo(null); //分支机构的销售编号为空
                                    to.setAgentLevel(Integer.parseInt(fromKYAgentInfo.getAgentLevel()));
                                    to.setParentAgentNo(fromKYAgentInfo.getParentAgentNo());
                                    //易多销多级的父链 C为自己 存储为 A-B-C 最后为自己  卡友为 -A-B- 没有自己
                                    to.setParentAgentLine(
                                            (fromKYAgentInfo.getParentAgentLine() + to.getAgentNo()).substring(1));
                                    to.setAgentType("company");// 代理商类型 company/公司、selfEmployed/个体户、person/个人
                                    to.setFullName(fromKYAgentInfo.getFullName());
                                    to.setShortName(fromKYAgentInfo.getShortName());
                                    to.setQualificationsType("Q");
                                    to.setOrgCode(fromKYAgentInfo.getOrgCode());
                                    to.setAddress(fromKYAgentInfo.getAddress());
                                    //合同相关 易多销需要重新签合同 迁移时不迁旧合同数据
//                                   to.setProtocolNo();to.setProtocolAnnex();to.setProtocolExpireytime();
                                    //管理者 负责人 迁移 //原联系人->管理者（手机号为登陆账号）    原客服联系人->业务负责人
                                    to.setManagerName(fromKYAgentInfo.getLinkMan());
                                    to.setManagerPhone(fromKYAgentInfo.getPhoneNo());
                                    to.setManagerEmail(fromKYAgentInfo.getEmail());
                                    to.setBussPrincipalName(fromKYAgentInfo.getSupportLinkman());
                                    to.setBussPrincipalPhone(fromKYAgentInfo.getSupportLinkphone());
                                    to.setBussPrincipalFixedPhone(fromKYAgentInfo.getTelephone());
                                    to.setStatus("1");
                                    to.setCreateTime(fromKYAgentInfo.getCreateTime());
                                    toAgentInfos.add(to);

                                    //初始化权限数据
                                    ydxSysAgents.add(initYDXSysAgent(fromKYAgentInfo));

                                    //初始化团队数据
                                    ydxSysOrgansSub.addAll(initYDXSysOrgans(fromKYAgentInfo));

                                    //转换易多销账户信息
                                    KYOperator fromOperator = fromKYAgentInfo.getKyOperator();
                                    YDXSysUser toSysUser;
                                    if (fromOperator != null) {
                                        toSysUser = new YDXSysUser();
                                        toSysUser.setUserTypeCode(SysUserTypeEnum.ADMIN.getCode());
                                        toSysUser.setAgentNo(fromKYAgentInfo.getAgentNo());
                                        toSysUser.setOrganCode("AGENT_ROOT");
                                        toSysUser.setUserNo("000000");
                                        toSysUser.setUsername(toSysUser.getAgentNo() + toSysUser.getUserNo());
                                        toSysUser.setPassword(fromOperator.getPassword());
                                        toSysUser.setRealname(fromOperator.getRealname());
                                        toSysUser.setEmail(fromKYAgentInfo.getEmail());
                                        toSysUser.setTelephone(fromKYAgentInfo.getTelephone());
                                        toSysUser.setCellphone(fromOperator.getUsername());
                                        toSysUser.setSex(null);
                                        toSysUser.setAge(null);
                                        toSysUser.setAddress(fromKYAgentInfo.getAddress());
                                        toSysUser.setPhoto(null);
                                        if (fromOperator.getStatus().equals(ConstantEnum.TRUE.getValue())) {
                                            toSysUser.setUserStatus(SysUserStatusEnum.NORMAL.getName());
                                        } else {
                                            toSysUser.setUserStatus(SysUserStatusEnum.DISABLED.getName());
                                        }
                                        toSysUser.setOrders(null);
                                        toSysUser.setDescription("迁移分支机构账号");
                                        toSysUser.setStatus(SysUserStatusEnum.NORMAL.getName());
                                        toSysUser.setCreateUser("migration");
                                        toSysUser.setCreateTime(new Date());
                                        toSysUser.setUpdateUser(null);
                                        toSysUser.setUpdateTime(null);
                                        toSysUser.setKyAgentNo(fromKYAgentInfo.getAgentNo());
                                        toSysUsers.add(toSysUser);
                                    }
                                } else {
                                    //如果不是分支机构 就是销售 只迁移成Sysuser
                                    //转换易多销账户信息
                                    KYOperator fromOperator = fromKYAgentInfo.getKyOperator();
                                    YDXSysUser toSysUser;
                                    if (fromOperator != null) {
                                        toSysUser = new YDXSysUser();
                                        toSysUser.setUserTypeCode(SysUserTypeEnum.DIRECT_SALE.getCode());
                                        toSysUser.setAgentNo(fromKYAgentInfo.getParentAgentNo());
                                        toSysUser.setOrganCode("DIRECT_ROOT");
                                        toSysUser.setPassword(fromOperator.getPassword());
                                        toSysUser.setRealname(fromOperator.getRealname());
                                        toSysUser.setEmail(fromKYAgentInfo.getEmail());
                                        toSysUser.setTelephone(fromKYAgentInfo.getTelephone());
                                        toSysUser.setCellphone(fromOperator.getUsername());
                                        toSysUser.setSex(null);
                                        toSysUser.setAge(null);
                                        toSysUser.setAddress(fromKYAgentInfo.getAddress());
                                        toSysUser.setPhoto(null);
                                        if (fromOperator.getStatus().equals(ConstantEnum.TRUE.getValue())) {
                                            toSysUser.setUserStatus(SysUserStatusEnum.NORMAL.getName());
                                        } else {
                                            toSysUser.setUserStatus(SysUserStatusEnum.DISABLED.getName());
                                        }
                                        toSysUser.setOrders(null);
                                        toSysUser.setDescription("迁移销售账号");
                                        toSysUser.setStatus(SysUserStatusEnum.NORMAL.getName());
                                        toSysUser.setCreateUser("migration");
                                        toSysUser.setCreateTime(new Date());
                                        toSysUser.setUpdateUser(null);
                                        toSysUser.setUpdateTime(null);
                                        toSysUser.setKyAgentNo(fromKYAgentInfo.getAgentNo());
                                        toSysUsers.add(toSysUser);
                                    }
                                }
                            });
                    return fromKYAgentInfos;
                }).orElseThrow(() -> new NullPointerException("-----一代服务商用户信息集合为空-----"));

    }
}
