package com.mi.hundsun.oxchains.provider.admin.controller.fn;


import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.model.fn.RechargeCoinModel;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.service.fn.RechargeCoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("充币管理服务")
@Slf4j
@RestController
public class RechargeCoinController extends GenericController {
    @Autowired
    RechargeCoinService rechargeCoinService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/fn/rechargeCoin/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return rechargeCoinService.getDtGridList(dtGridPager,RechargeCoinModel.class);
    }

    @ApiOperation(value = "查询单个")
    @PostMapping(value = "/fn/rechargeCoin/selectOne")
    public RechargeCoin selectOne(@RequestBody RechargeCoin rechargeCoin) throws Exception {
        return rechargeCoinService.selectOne(rechargeCoin);
    }

    @ApiOperation(value = "记录审核修改")
    @PostMapping(value = "/fn/rechargeCoin/audit")
    public ResultEntity audit(@RequestBody RechargeCoin rechargeCoin) throws Exception {
        try {
            rechargeCoinService.audit(rechargeCoin);
            return  ok();
        }catch (Exception e){
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "充值记录添加")
    @PostMapping(value = "/fn/rechargeCoin/insert")
    public ResultEntity insert(@RequestBody RechargeCoin rechargeCoin) throws Exception {
        try {
            rechargeCoinService.insert(rechargeCoin);
            return  ok();
        }catch (Exception e){
            e.printStackTrace();
            return fail();
        }
    }

//    @ApiOperation(value = "导出excel")
//    @PostMapping(value = "/fn/rechargeCoin/getDtGridListExport")
//    public DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws Exception {
//        return rechargeCoinService.getDtGridListExport(dtGridPager,RechargeCoinModel.class);
//    }
}
