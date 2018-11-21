package com.mi.hundsun.oxchains.consumer.admin.service.user;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface UserFreezeInterface {

    /**
     * 用户冻结列表查询
     *
     * @param dtGridPager
     * @return
     */
    //@PostMapping(value = "/user/freeze/getDtGridList")
    @PostMapping(value = "/user/userFreeze/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 查询用户
     *
     * @param userFreeze
     * @return
     */
    @PostMapping(value = "/user/userFreeze/selectOne")
    UserFreeze selectOne(@RequestBody UserFreeze userFreeze);

    /**
     * 编辑用户
     *
     * @param userFreeze
     * @return
     */
    @PostMapping(value = "/user/userFreeze/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody UserFreeze userFreeze);



    /**
     * 导出excel
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/user/userFreeze/getDtGridListExport")
    DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws BussinessException;
}
