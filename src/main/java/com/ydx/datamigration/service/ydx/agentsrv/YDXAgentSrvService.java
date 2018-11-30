package com.ydx.datamigration.service.ydx.agentsrv;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.ydx.datamigration.config.YDXAgentSrvDataSourceConfig;
import com.ydx.datamigration.enums.BooleanEnum;
import com.ydx.datamigration.enums.ConstantEnum;
import com.ydx.datamigration.model.ydx.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 处理业务service（易多销agent_srv数据源事物在此开启）
 */
@Service
@Transactional(value = YDXAgentSrvDataSourceConfig.TRANSACTION_MANAGER, rollbackFor = Exception.class)
public class YDXAgentSrvService {

    private static final Logger logger = LoggerFactory.getLogger(YDXAgentSrvService.class);

    @Autowired
    private MigrationInfoService migrationInfoService;

    @Autowired
    private YDXAgentInfoService agentInfoService;

    @Autowired
    private YDXAgentInfoExtService agentInfoExtService;

    @Autowired
    private YDXEnterpriseInfoService enterpriseInfoService;

    @Autowired
    private YDXSysUserService sysUserService;

    @Autowired
    private  YDXSysRoleUserService roleUserService;

    @Autowired
    private YDXSysAgentService sysAgentService;

    @Autowired
    private YDXSysOrganService sysOrganService;

    @Autowired
    private YDXSysOrganUserService sysOrganUserService;

    /**
     * 批量保存服务商迁移信息
     *
     * @param list
     * @return
     */
    public boolean saveMigrationInfoList(List<MigrationInfo> list) {
        return migrationInfoService.saveOrUpdateBatch(list);
    }

    /**
     * 批量更新服务商迁移信息
     */
    public boolean updateMigrationInfoList(List<MigrationInfo> list){
        return migrationInfoService.updateBatchById(list);
    }

    /**
     * 根据服务商编号查询服务商迁移信息
     */
    public MigrationInfo queryMigrationInfoByAgentNo(String agentNo){
        return migrationInfoService.getOne(new LambdaQueryWrapper<MigrationInfo>().eq(MigrationInfo::getAgentNo,agentNo));
    }


    /**
     * 更新服务商迁移信息
     */
    public boolean updateMigrationInfo(MigrationInfo migrationInfo) {
        return migrationInfoService.updateById(migrationInfo);
    }


    /**
     * 查询需要迁移的服务商迁移信息  状态为INIT 或 FAIL
     *
     * @return
     */
    public List<MigrationInfo> queryMigrationInfoList() {
        return migrationInfoService.list(
                new LambdaQueryWrapper<MigrationInfo>()
                        .in(MigrationInfo::getStatus, Lists.newArrayList(ConstantEnum.INIT.getValue(), ConstantEnum.FAIL.getValue())));
    }


    /**
     * 保存易多销一代迁移信息 统一事物
     *
     * @param agentInfo
     * @param enterpriseInfo
     * @param sysUsers
     * @param agentInfoExt
     * @param ydxSysRoleUsers
     */
    public void saveMigrationAgent(YDXAgentInfo agentInfo,
                                   YDXEnterpriseInfo enterpriseInfo,
                                   List<YDXSysUser> sysUsers,
                                   YDXAgentInfoExt agentInfoExt,
                                   List<YDXSysRoleUser> ydxSysRoleUsers,
                                   YDXSysAgent ydxSysAgent,
                                   List<YDXSysOrgan> ydxSysOrgans,
                                   List<YDXSysOrganUser> sysOrganUsers) {
        boolean res = agentInfoService.save(agentInfo);
        boolean res1 = Optional.ofNullable(agentInfoExt).map(x->agentInfoExtService.save(x)).orElse(BooleanEnum.TRUE.isValue());
        boolean res2 = enterpriseInfoService.save(enterpriseInfo);
        boolean res3 = sysUserService.saveBatch(sysUsers);
        boolean res4 = roleUserService.saveBatch(ydxSysRoleUsers);
        boolean res5 = sysAgentService.save(ydxSysAgent);
        boolean res6 = sysOrganService.saveBatch(ydxSysOrgans);
        boolean res7 = sysOrganUserService.saveBatch(sysOrganUsers);
        if (!(res && res1 && res2 && res3 && res4 && res5 && res6 && res7)) throw new RuntimeException("保存易多销一代迁移信息失败");
    }

    /**
     * 保存易多销一代迁移的  下级和账号信息  统一事物
     *
     * @param agentInfos
     * @param sysUsers
     */
    public void saveMigrationSubAgent(List<YDXAgentInfo> agentInfos,
                                      List<YDXSysUser> sysUsers,
                                      List<YDXSysRoleUser> subSysRoleUsers,
                                      List<YDXSysAgent> ydxSysAgents,
                                      List<YDXSysOrgan> ydxSysOrgansSub,
                                      List<YDXSysOrganUser> sysOrganUsersSub
                                      ) {
        boolean res = agentInfoService.saveBatch(agentInfos);
        boolean res1 = Optional.ofNullable(sysUsers).map(x->sysUserService.saveBatch(x)).orElse(BooleanEnum.TRUE.isValue());
        boolean res2 = Optional.ofNullable(subSysRoleUsers).map(x->roleUserService.saveBatch(x)).orElse(BooleanEnum.TRUE.isValue());
        boolean res3 = sysAgentService.saveBatch(ydxSysAgents);
        boolean res4 = sysOrganService.saveBatch(ydxSysOrgansSub);
        boolean res5 = Optional.ofNullable(sysOrganUsersSub).map(x->sysOrganUserService.saveBatch(x)).orElse(BooleanEnum.TRUE.isValue());
        if (!(res && res1 && res2 && res3 && res4 && res5)) throw new RuntimeException("保存易多销下级迁移信息失败");

    }

    /**
     * 根据服务商编号 查询对应账号集合中userNo的最大值
     * @param agentNo
     * @return
     */
    public int querySysUserMaxUserNo(String agentNo) {
        return sysUserService.querySysUserMaxUserNo(agentNo);
    }


    /**
     * 查询易多销的所有角色code集合
     * @return
     */
    public List<String> queryYDXRoles(){
        return roleUserService.queryRoleList();
    }


    @Cacheable(value = "ydxAgentInfoCatch",sync = true)
    public YDXAgentInfo queryYDXAgentInfoByAgentNo(String ownerAgentNo) {
        return Optional.ofNullable(ownerAgentNo).filter(StringUtils::isNotEmpty)
                .map(x->agentInfoService.getOne(new LambdaQueryWrapper<YDXAgentInfo>().eq(YDXAgentInfo::getAgentNo,ownerAgentNo)))
                .orElse(null);
    }

    /**
     * 根据卡友agentno 查询sysuser
     * @param ownerAgentNo
     * @return
     */
    @Cacheable(value = "ydxSysUserCatch",sync = true)
    public YDXSysUser queryYDXSysUserByKYAgentNo(String ownerAgentNo) {
        return Optional.ofNullable(ownerAgentNo).filter(StringUtils::isNotEmpty)
                .map(x->sysUserService.getOne(new LambdaQueryWrapper<YDXSysUser>().eq(YDXSysUser::getKyAgentNo,ownerAgentNo)))
                .orElse(null);
    }
}
