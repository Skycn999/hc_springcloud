package com.mi.hundsun.oxchains.provider.admin.controller.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Dict;
import com.mi.hundsun.oxchains.base.core.service.system.DictService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典表业务相关Service接口<br>
 *
 * @ClassName: DictController
 * @author donfy
 * @date   2017-08-16 01:59:00
 */
@Slf4j
@RestController
@RequestMapping("/sys/dict")
public class DictController {

    @Autowired
    DictService dictService;

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody Dict dict) throws BussinessException {
        dictService.insert(dict);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Dict dict) throws BussinessException {
        dictService.updateByPrimaryKeySelective(dict);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        dictService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody Dict dict) throws BussinessException {
        dictService.removeById(dict);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public Dict getNormalModelById(@RequestBody Dict dict) throws BussinessException {
        return dictService.getNormalModelById(dict);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public Dict selectOne(@RequestBody Dict dict) throws BussinessException {
        return dictService.selectOne(dict);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<Dict> select(@RequestBody Dict dict) throws BussinessException {
        return dictService.select(dict);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<Dict> selectAll() throws BussinessException {
        return dictService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return dictService.getDtGridList(dtGridPager);
    }
}
