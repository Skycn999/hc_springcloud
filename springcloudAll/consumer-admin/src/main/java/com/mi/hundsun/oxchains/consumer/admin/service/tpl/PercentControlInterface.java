package com.mi.hundsun.oxchains.consumer.admin.service.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface PercentControlInterface {

    /**
     * 列表查询
     *
     * @return
     */
    @PostMapping(value = "/tpl/percentControl/select")
    List<PercentControl> select(PercentControl percentControl) throws BussinessException;

    /**
     * 用户地址查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/tpl/percentControl/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 插入
     * @param percentControl
     */
    @PostMapping(value = "/tpl/percentControl/insert")
    void insert(@RequestBody PercentControl percentControl);

    /**
     * 编辑用户
     *
     * @param percentControl
     * @return
     */
    @PostMapping(value = "/tpl/percentControl/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody PercentControl percentControl);
}
