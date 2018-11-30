package com.ydx.datamigration.model.ky;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydx.datamigration.model.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 卡友posp_boss库 机具信息
 */
@Data
@TableName("posp_boss.pos")
public class KYPos extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String posCati; 			// POS终端号
    private String posSn; 				// POS机具序列号
    private Long agentId; 				// 所属代理商
    private Long customerId; 			// 所属商户
    private Long shopId; 				// 所属网点
    private String supplierCode; 		// 供货商
    private String type; 				// 型号
    private String commuType; 			// 通讯类型
    private String useType; 			// 使用方式
    private String locationCode; 		// 库位编码
    private String status; 				// 状态
    private String softVersion; 		// 当前软件版本号
    private String paramVersion; 		// 当前参数版本号
//    private String mKey; 				// 主密钥(密文)
//    private Date mKeyLastModifyTime; 	// 主密钥最后更新时间
//    private String managePassword; 		// 管理密码
//    private String managePasswordBak; 	// 原管理密码
//    private Date pwdLastmodifyTime; 	// 管理密码最后更新时间
    private Date createTime; 			// 创建时间
    private String posOwner; 			// pos所属
//    private String recommenderId; 		// 推荐人编号
//    private Integer isLocked; 			// 是否锁定
    private String posUuid;				// pos机mac地址
    private String agentNo;             // 一代服务商编号
    private String customerNo;          // 绑定的商户编号
    private String shopNo; 				// 网点编号
    private String posSeq;				// uuid，与bank_terminal表关联
    private Integer isStock;			// is_stock 是否是存量 1是0否
    private Integer canEncrypt;			// can_encrypt 支持加密 1是0否
    private String dockingOrganization;
}
