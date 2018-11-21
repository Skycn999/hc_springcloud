package com.mi.hundsun.oxchains.consumer.admin.service.fn;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface RechargeCoinInterface {

    /**
     * 充币管理分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/fn/rechargeCoin/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     *  查询充币记录
     * @param rechargeCoin
     * @return
     * @throws BussinessException
     */
    @PostMapping(value = "/fn/rechargeCoin/selectOne")
    RechargeCoin selectOne(@RequestBody RechargeCoin rechargeCoin) throws BussinessException;

    /**
     * 充币记录审核
     */
    @PostMapping(value = "/fn/rechargeCoin/audit")
    ResultEntity audit(@RequestBody RechargeCoin rechargeCoin) throws BussinessException;

    /**
     * 充币记录添加
     */
    @PostMapping(value = "/fn/rechargeCoin/insert")
    ResultEntity insert(@RequestBody RechargeCoin rechargeCoin) throws BussinessException;

//    /**
//     * 导出excel
//     *
//     * @param dtGridPager
//     * @return
//     */
//    @PostMapping(value = "/fn/rechargeCoin/getDtGridListExport")
//    DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws BussinessException;
}
