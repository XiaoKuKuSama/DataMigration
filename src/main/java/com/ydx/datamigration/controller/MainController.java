package com.ydx.datamigration.controller;


import com.ydx.datamigration.service.MainService;
import com.ydx.datamigration.service.ydx.agentsrv.YDXAgentSrvService;
import com.ydx.datamigration.utils.POIUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 迁移系统Controller
 */
@RestController
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MainService mainService;


    /**
     * 上传迁移服务商列表，初始化迁移服务商基本信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uploadExcel")
    public String uploadExcel(@RequestParam(value = "excelFile") MultipartFile excelFile,
                              @RequestParam(value = "agents") String agents,
                              HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("-----上传Excel解析迁移列表开始-----");
            //读取上传excel 第一个sheet 第一列从第二行开始 读取服务商编号
            //返回String数组的集合 每个数组的第一个元素 为服务商编号
            List<String[]> listStrArr = POIUtil.readExcel(excelFile);
            String inits = Optional.ofNullable(listStrArr)
                    .filter(CollectionUtils::isNotEmpty)
                    .map(x -> x.size() + "")
                    .orElse("0");
            logger.info("-----读取excel文件{}行-----", inits);
            //检查页面传入的预计迁移服务商数和excel读取出来的服务商数是否匹配
            Optional.ofNullable(agents).filter(StringUtils::isNotEmpty)
                    .filter(x -> x.equals(inits))
                    .orElseThrow(() -> new RuntimeException("上传的excel中服务商迁移数量与预计迁移服务商数量不符"));
            //调用初始化迁移服务商方法
            int result = mainService.initMigrationAgents(listStrArr);
            logger.info("-----成功初始化{}个服务商迁移信息-----", result);
            return "-----成功初始化" + result + "个服务商迁移信息-----";
        } catch (IOException e) {
            logger.error("-----读取excel文件失败-----", e);
            return "初始化失败";
        }
    }

    /**
     * 基本信息迁移入口
     */
    @RequestMapping("/migration")
    public String migration() {
        logger.info("-----服务商迁移开始-----");
        int result = mainService.migration();
        logger.info("-----成功迁移{}个服务商-----", result);
        return "成功迁移" + result + "个服务商";
    }


    /**
     * 上传迁移服务商列表，初始化迁移服务商业务信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uploadExcelBussiness")
    public String uploadExcelBussiness(@RequestParam(value = "excelFile") MultipartFile excelFile,
                              @RequestParam(value = "agents") String agents,
                              HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("-----上传Excel解析业务迁移列表开始-----");
            //读取上传excel 第一个sheet 第一列从第二行开始 读取服务商编号
            //返回String数组的集合 每个数组的第一个元素 为服务商编号
            List<String[]> listStrArr = POIUtil.readExcel(excelFile);
            String inits = Optional.ofNullable(listStrArr)
                    .filter(CollectionUtils::isNotEmpty)
                    .map(x -> x.size() + "")
                    .orElse("0");
            logger.info("-----读取excel文件{}行-----", inits);
            //检查页面传入的预计迁移服务商数和excel读取出来的服务商数是否匹配
            Optional.ofNullable(agents).filter(StringUtils::isNotEmpty)
                    .filter(x -> x.equals(inits))
                    .orElseThrow(() -> new RuntimeException("上传的excel中服务商业务迁移数量与预计迁移服务商数量不符"));
            //调用初始化迁移服务商业务信息方法
            int result = mainService.initMigrationAgentsBussiness(listStrArr);
            logger.info("-----成功初始化{}个服务商业务迁移信息-----", result);
            return "-----成功初始化" + result + "个服务商业务迁移信息-----";
        } catch (IOException e) {
            logger.error("-----读取excel文件失败-----", e);
            return "初始化失败";
        }
    }

    /**
     * 业务信息迁移入口
     */
    @RequestMapping("/migration_business")
    public String migrationBusiness() {
        logger.info("-----服务商业务迁移开始-----");
        int result = mainService.migrationBusiness();
        logger.info("-----成功业务迁移{}个服务商-----", result);
        return "成功业务迁移" + result + "个服务商";
    }

}
