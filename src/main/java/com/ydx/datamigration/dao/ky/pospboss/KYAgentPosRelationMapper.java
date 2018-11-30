package com.ydx.datamigration.dao.ky.pospboss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydx.datamigration.model.ky.KYAgentPosRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface KYAgentPosRelationMapper extends BaseMapper<KYAgentPosRelation> {

    @Options(timeout = 10000, flushCache = Options.FlushCachePolicy.TRUE)
    @Select("select * from posp_boss.agent_pos_relation where id=#{id}")
    KYAgentPosRelation queryAgentPosRelationById(String id);
}
