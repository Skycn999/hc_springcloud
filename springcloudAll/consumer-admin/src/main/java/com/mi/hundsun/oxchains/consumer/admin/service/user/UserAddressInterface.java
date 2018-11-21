package com.mi.hundsun.oxchains.consumer.admin.service.user;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface UserAddressInterface {

    /**
     * 用户地址查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/user/userAddress/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 查询用户
     *
     * @param userAddress
     * @return
     */
    @PostMapping(value = "/user/userAddress/selectOne")
    UserAddress selectOne(@RequestBody UserAddress userAddress);

//    /**
//     * 查询用户
//     *
//     * @param userId
//     * @return
//     */
//    @PostMapping(value = "/user/userAddress/selectByPrimaryKey")
//    UserAddress selectByPrimaryKey(@RequestBody Integer userId);

    /**
     * 导出excel
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/user/userAddress/getDtGridListExport")
    DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws BussinessException;
}
