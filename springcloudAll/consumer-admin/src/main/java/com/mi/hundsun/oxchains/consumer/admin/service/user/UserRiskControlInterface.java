package com.mi.hundsun.oxchains.consumer.admin.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.UserRiskControl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface UserRiskControlInterface {

    /**
     * 用户风控设置查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/user/riskControl/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 新增风控
     *
     * @param userRiskControl
     */
    @PostMapping(value = "/user/riskControl/insert")
    ResultEntity insert(@RequestBody UserRiskControl userRiskControl)throws BussinessException;

    /**
     * 设置模板
     *
     * @param userRiskControl
     */
    @PostMapping(value = "/user/riskControl/updateSettingTpl")
    ResultEntity updateSettingTpl(@RequestBody UserRiskControl userRiskControl)throws BussinessException;

}
