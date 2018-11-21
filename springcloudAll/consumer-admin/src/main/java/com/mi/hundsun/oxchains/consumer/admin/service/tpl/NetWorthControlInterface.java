package com.mi.hundsun.oxchains.consumer.admin.service.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface NetWorthControlInterface {

    /**
     * 列表查询
     *
     * @return
     */
    @PostMapping(value = "/tpl/netWorth/select")
    List<NetWorthControl> select(NetWorthControl netWorthControl) throws BussinessException;

    /**
     * 用户地址查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/tpl/netWorth/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 插入
     * @param netWorthControl
     */
    @PostMapping(value = "/tpl/netWorth/insert")
    void insertNetWorthControl(@RequestBody NetWorthControl netWorthControl);

    /**
     * 编辑用户
     *
     * @param netWorthControl
     * @return
     */
    @PostMapping(value = "/tpl/netWorth/updateNetWorthControl")
    void updateNetWorthControl(@RequestBody NetWorthControl netWorthControl);

//    /**
//     * 查询用户
//     *
//     * @param netWorthControl
//     * @return
//     */
//    @PostMapping(value = "/tpl/valueRisk/selectOne")
//    NetWorthControl selectOne(@RequestBody NetWorthControl netWorthControl);
}
