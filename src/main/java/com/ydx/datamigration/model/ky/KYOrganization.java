package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

/**
 * 卡友posp_boss库  地址映射信息
 */
@Data
@TableName("posp_boss.organization")
public class KYOrganization {

    private static final long serialVersionUID = 1L;
    private String code;  //编码
    private String name;  //名称
    private String parent_code; //父编码
}
