package com.mi.hundsun.oxchains.provider.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.ServiceLog;
import com.mi.hundsun.oxchains.base.core.service.system.ServiceLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台接口调用日志业务相关Service接口<br>
 *
 * @author xxw
 * @ClassName: LogController
 * @date 2017-09-18 11:56:52
 */
@Slf4j
@RestController
@RequestMapping("/sys/log")
public class ServiceLogController {

    @Autowired
    ServiceLogService serviceLogService;

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody ServiceLog serviceLog) throws BussinessException {
        serviceLogService.insert(serviceLog);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody ServiceLog log) throws BussinessException {
        serviceLogService.updateByPrimaryKeySelective(log);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        serviceLogService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody ServiceLog log) throws BussinessException {
        serviceLogService.removeById(log);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public ServiceLog getNormalModelById(@RequestBody ServiceLog log) throws BussinessException {
        return serviceLogService.getNormalModelById(log);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public ServiceLog selectOne(@RequestBody ServiceLog log) throws BussinessException {
        return serviceLogService.selectOne(log);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<ServiceLog> select(@RequestBody ServiceLog log) throws BussinessException {
        return serviceLogService.select(log);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<ServiceLog> selectAll() throws BussinessException {
        return serviceLogService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")

    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return serviceLogService.getDtGridList(dtGridPager);
    }
}
