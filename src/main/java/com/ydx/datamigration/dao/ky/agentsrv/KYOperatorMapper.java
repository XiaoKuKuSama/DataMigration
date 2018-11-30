package com.ydx.datamigration.dao.ky.agentsrv;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydx.datamigration.model.ky.KYOperator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface KYOperatorMapper extends BaseMapper<KYOperator> {

    @Select("select * from operator where self_agent_no=#{selfAgentNo} and status<>'DELETE'")
    KYOperator queryKYOperatorBySelfAgentNo(String selfAgentNo);
}
