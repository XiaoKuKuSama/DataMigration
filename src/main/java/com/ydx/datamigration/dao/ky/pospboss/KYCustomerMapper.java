package com.ydx.datamigration.dao.ky.pospboss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydx.datamigration.model.ky.KYCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by xinyi.chang on 18-11-24.
 */
@Mapper
public interface KYCustomerMapper extends BaseMapper<KYCustomer>{


    @Select("select c.mcc,c.docking_organization,c.full_name,c.phone_no,c.status,c.linkman,c.open_time,c.customer_type,i.legal_person from posp_boss.customer c left join posp_boss.enterprise_info i on c.enterpriseinfo_id=i.id where customer_no = #{customerNo}")
    KYCustomer queryKYCustomerByNo(String customerNo);
}
