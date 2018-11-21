package com.mi.hundsun.oxchains.provider.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.MsgTemplate;
import com.mi.hundsun.oxchains.base.core.service.system.MsgTemplateService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消息模板表业务相关Service接口<br>
 *
 * @ClassName: MsgTemplateController
 * @author donfy
 * @date   2017-08-16 01:52:00
 */
@Slf4j
@RestController
@RequestMapping("/sys/messageTemplate")
public class MsgTemplateController {

    @Autowired
    MsgTemplateService msgTemplateService;

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody MsgTemplate msgTemplate) throws BussinessException {
        msgTemplateService.insert(msgTemplate);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody MsgTemplate msgTemplate) throws BussinessException {
        msgTemplateService.updateByPrimaryKeySelective(msgTemplate);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        msgTemplateService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody MsgTemplate msgTemplate) throws BussinessException {
        msgTemplateService.removeById(msgTemplate);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public MsgTemplate getNormalModelById(@RequestBody MsgTemplate msgTemplate) throws BussinessException {
        return msgTemplateService.getNormalModelById(msgTemplate);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public MsgTemplate selectOne(@RequestBody MsgTemplate msgTemplate) throws BussinessException {
        return msgTemplateService.selectOne(msgTemplate);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<MsgTemplate> select(@RequestBody MsgTemplate msgTemplate) throws BussinessException {
        return msgTemplateService.select(msgTemplate);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<MsgTemplate> selectAll() throws BussinessException {
        return msgTemplateService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return msgTemplateService.getDtGridList(dtGridPager);
    }
}
