package com.mi.hundsun.oxchains.consumer.admin.service.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Configure;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface ServiceFeeInterface {


    /**
     * 手续费模板分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/tpl/serviceFee/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 列表查询
     *
     * @return
     */
    @PostMapping(value = "/tpl/serviceFee/select")
    List<ServiceFee> select(@RequestBody ServiceFee serviceFee) throws BussinessException;

    /**
     * 保存手续费模板
     * @param serviceFee
     */
    @PostMapping(value = "/tpl/serviceFee/insertFee")
    void insertFee(@RequestBody ServiceFee serviceFee);

    /**
     * 编辑更新手续费模板
     *
     * @param serviceFee
     */
    @PostMapping(value = "/tpl/serviceFee/updateServiceFeee")
    void updateServiceFeee(@RequestBody ServiceFee serviceFee);

}
