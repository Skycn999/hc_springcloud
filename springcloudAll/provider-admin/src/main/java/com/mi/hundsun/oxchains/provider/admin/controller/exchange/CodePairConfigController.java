package com.mi.hundsun.oxchains.provider.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import com.mi.hundsun.oxchains.base.core.service.quote.CodePairConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("代码对管理服务")
@Slf4j
@RestController
public class CodePairConfigController extends GenericController {
    @Autowired
    CodePairConfigService codePairConfigService;


    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/exchange/codePairConfig/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return codePairConfigService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/exchange/codePairConfig/insert")
    public ResultEntity insert(@RequestBody CodePairConfig codePairConfig) throws Exception {
        try {
            codePairConfigService.insert(codePairConfig);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/exchange/codePairConfig/updateCodePacirConfig")
    public ResultEntity updateCodePacirConfig(@RequestBody CodePairConfig codePairConfig) throws Exception {
        try {
            codePairConfigService.updateByPrimaryKeySelective(codePairConfig);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

}
