package com.mi.hundsun.oxchains.consumer.admin.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface UserIdentifyInterface {

    /**
     * 用户认证查询
     *
     * @param dtGridPager
     * @return
     */
    //@PostMapping(value = "/user/identify/getDtGridList")
    @PostMapping(value = "/user/userIdentify/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 查询用户
     *
     * @param userIdentify
     * @return
     */
    @PostMapping(value = "/user/userIdentify/selectOne")
    UserIdentify selectOne(@RequestBody UserIdentify userIdentify);

    /**
     * 编辑用户
     *
     * @param userIdentify
     * @return
     */
    @PostMapping(value = "/user/userIdentify/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody UserIdentify userIdentify);

    /**
     * 导出excel
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/user/userIdentify/getDtGridListExport")
    DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws BussinessException;


}
