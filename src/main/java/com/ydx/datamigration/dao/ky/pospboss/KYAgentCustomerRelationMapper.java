package com.ydx.datamigration.dao.ky.pospboss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydx.datamigration.model.ky.KYAgentCustomerRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KYAgentCustomerRelationMapper extends BaseMapper<KYAgentCustomerRelation> {


    @Select("select * from posp_boss.agent_customer_relation where agent_no = #{agentNo}")
    @Results({
            @Result(property = "customerNo" ,column = "customer_no"),
            @Result(property = "kyCustomer",
                    column = "customer_no",
                    one=@One(select = "com.ydx.datamigration.dao.ky.pospboss.KYCustomerMapper.queryKYCustomerByNo"))})
    List<KYAgentCustomerRelation> queryKYAgentCustomerRelationWithKYCustomer(String agentNo);
}
