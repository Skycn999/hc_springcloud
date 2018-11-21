package com.mi.hundsun.oxchains.consumer.admin.service.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.TradeFee;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface TradeFeeInterface {

    /**
     * 交易手续费查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/tpl/tradeFee/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 列表查询
     *
     * @return
     */
    @PostMapping(value = "/tpl/tradeFee/select")
    List<TradeFee> select(@RequestBody TradeFee tradeFee) throws BussinessException;

    /**
     * 查询用户
     *
     * @param tradeFee
     * @return
     */
    @PostMapping(value = "/tpl/tradeFee/selectOne")
    TradeFee selectOne(@RequestBody TradeFee tradeFee);

    /**
     * 插入
     * @param tradeFee
     */
    @PostMapping(value = "/tpl/tradeFee/insert")
    void insert(@RequestBody TradeFee tradeFee);

    /**
     * 编辑
     *
     * @param tradeFee
     * @return
     */
    @PostMapping(value = "/tpl/tradeFee/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody TradeFee tradeFee);
}
