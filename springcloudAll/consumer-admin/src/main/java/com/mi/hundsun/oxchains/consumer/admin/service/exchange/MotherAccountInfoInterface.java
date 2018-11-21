package com.mi.hundsun.oxchains.consumer.admin.service.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountModel;
import com.mi.hundsun.oxchains.base.core.model.quote.model.AccountBalance;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface MotherAccountInfoInterface {

    /**
     * 交易所母账号资产信息分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/exchange/motherAccountInfo/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws Exception;


    /**
     * 交易所母账号资产同步
     *
     * @param motherAccountModel 母账户信息
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/exchange/motherAccountInfo/synchronMotherAccount")
    ResultEntity synchronMotherAccount(@RequestBody MotherAccountModel motherAccountModel);

    /**
     * 导出excel
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/exchange/motherAccountInfo/getDtGridListExport")
    DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws BussinessException;
}
