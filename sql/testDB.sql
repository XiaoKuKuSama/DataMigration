select * from ds_appli where appli_info_id=2080;

select * from ds_info WHERE id in (500,501,502,503);

select * from db_info where id in (3,254,332,333);

INSERT INTO `datasource_db`.`ds_info` (`id`, `min_pool_size`, `max_pool_size`, `max_idle_time`, `acquire_increment`, `max_statements`, `initial_pool_size`, `idle_connection_test_period`, `acquire_retry_attempts`, `break_after_acquire_failure`, `test_connection_on_checkout`, `db_info_id`, `ds_name`) VALUES ('500', '5', '300', '1800', '2', '0', '1', '10800', '0', 'false', 'false', '3', 'ds_odb_datamigration_posp_boss_r');
INSERT INTO `datasource_db`.`ds_info` (`id`, `min_pool_size`, `max_pool_size`, `max_idle_time`, `acquire_increment`, `max_statements`, `initial_pool_size`, `idle_connection_test_period`, `acquire_retry_attempts`, `break_after_acquire_failure`, `test_connection_on_checkout`, `db_info_id`, `ds_name`) VALUES ('501', '1', '20', '1800', '2', '0', '1', '10800', '0', 'false', 'false', '254', 'ds_mdb_datamigration_ky_agent_srv_r');
INSERT INTO `datasource_db`.`ds_info` (`id`, `min_pool_size`, `max_pool_size`, `max_idle_time`, `acquire_increment`, `max_statements`, `initial_pool_size`, `idle_connection_test_period`, `acquire_retry_attempts`, `break_after_acquire_failure`, `test_connection_on_checkout`, `db_info_id`, `ds_name`) VALUES ('502', '10', '50', '1800', '2', '0', '1', '10800', '0', 'false', 'false', '332', 'ds_mdb_datamigration_ydx_agent_srv_rw');
INSERT INTO `datasource_db`.`ds_info` (`id`, `min_pool_size`, `max_pool_size`, `max_idle_time`, `acquire_increment`, `max_statements`, `initial_pool_size`, `idle_connection_test_period`, `acquire_retry_attempts`, `break_after_acquire_failure`, `test_connection_on_checkout`, `db_info_id`, `ds_name`) VALUES ('503', '10', '50', '1800', '2', '0', '1', '10800', '0', 'false', 'false', '333', 'ds_mdb_datamigration_ydx_agent_product_rw');



DROP TABLE IF EXISTS `migration_info`;
CREATE TABLE `migration_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `optimistic` int(11) DEFAULT '0' COMMENT '数据版本',
  `agent_no` varchar(20) NOT NULL COMMENT '迁移代理商编号',
  `sub_agent_sum` int(6) DEFAULT '0' COMMENT '需要迁移的下级数量(分支机构+销售)',
  `sub_agent_succ_sum` int(6) DEFAULT '0' COMMENT '成功迁移的下级数量(分支机构+销售)',
  `account_sum` int(6) DEFAULT '0' COMMENT '需要迁移的总账号数(一代+一代操作员+分支机构+销售)',
  `account_succ_sum` int(6) DEFAULT '0' COMMENT '成功迁移的下级数量(分支机构+销售)',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `fail_node` varchar(20) DEFAULT NULL COMMENT '迁移失败节点',
  `fail_sum` int(6) DEFAULT '0' COMMENT '迁移失败次数',
  `success_time` datetime DEFAULT NULL COMMENT '迁移成功时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `agent_no` (`agent_no`),
  KEY `index_migration_agent_no` (`agent_no`) COMMENT '服务商编号索引',
  KEY `index_migration_status` (`status`) COMMENT '迁移状态索引'
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='代理商迁移信息表';



ALTER TABLE `migration_info`
ADD COLUMN `acticity_fee_sum`  int(6) NULL COMMENT '需要迁移的服务商活动费率设置数量',
ADD COLUMN `acticity_fee_succ_sum`  int(6) NULL COMMENT '成功迁移的服务商活动费率设置数量',
ADD COLUMN `pos_sum`  int(6) NULL COMMENT '需要迁移的终端信息数',
ADD COLUMN `pos_succ_sum`  int(6) NULL COMMENT '成功迁移的终端信息数量',
ADD COLUMN `pos_relation_sum`  int(6) NULL COMMENT '需要迁移的终端关系数量',
ADD COLUMN `pos_relation_succ_sum`  int(6) NULL COMMENT '成功迁移的终端关系数量',
ADD COLUMN `customer_relation_sum`  int(6) NULL COMMENT '需要迁移的商户关系数量',
ADD COLUMN `customer_relation_succ_sum`  int(6) NULL COMMENT '成功迁移的商户关系数量',
ADD COLUMN `business_success_time`  datetime NULL COMMENT '业务迁移成功时间';