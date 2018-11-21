package com.mi.hundsun.oxchains.consumer.admin.service.tx;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import org.omg.CORBA.Request;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-trade-${feignSuffix}")
public interface MainDelegationInterface {

    /**
     * 主委托管理分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/tx/mainDelegation/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws Exception;

    /**
     * 主委托管理分页查询
     *
     * @param dtGrid
     * @return
     */
    @PostMapping(value = "/tx/mainDelegation/getDtGridLists")
    DtGrid getDtGridLists(@RequestBody DtGrid dtGrid) throws Exception;

    /**
     * 撤单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/tx/mainDelegation/revoke")
    ResultEntity revoke(@RequestParam("id") Integer id)throws Exception;

    /**
     * 导出excel
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/tx/mainDelegation/getDtGridListExport")
    DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws BussinessException;
}
