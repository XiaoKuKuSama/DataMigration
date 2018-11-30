package com.ydx.datamigration.dao.ky.agentsrv;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydx.datamigration.model.ky.KYAgentInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KYAgentInfoMapper extends BaseMapper<KYAgentInfo> {

    /**
     * 去除一级缓存 根据id查询服务商信息
     * @param id
     * @return
     */
    @Options(timeout = 10000,  flushCache = Options.FlushCachePolicy.TRUE)
    @Select("select * from agent_info where id=#{id}")
    KYAgentInfo queryAgentInfoById(String id);


    /**
     * 查询卡友一代服务商要迁移的下级信息  迁移对象:状态为TRUE 类型为销售、分支机构  父链中是一代服务商编号-agentno-开头
     * @param parentAgentLine
     * @return
     */
    @Select("select * from agent_info where status='TRUE' and role_type in ('SALE','AGENT_SUB') and parent_agent_line like #{parentAgentLine}")
    @Results({
            @Result(property = "agentNo" ,column = "agent_no"),
            @Result(property = "kyOperator",
                    column = "agent_no",
                    one=@One(select = "com.ydx.datamigration.dao.ky.agentsrv.KYOperatorMapper.queryKYOperatorBySelfAgentNo"))})
    List<KYAgentInfo> queryAgentInfoListLikeParentAgentLine(String parentAgentLine);



}
