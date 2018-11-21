package com.mi.hundsun.oxchains.consumer.admin.service.fn;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;

import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface PlatUserAddressInterface {

    /**
     * 平台地址管理分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/fn/platUserAddress/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 平台地址管理列表查询
     *
     * @param platUserAddress
     * @return
     */
    @PostMapping(value = "/fn/platUserAddress/select")
    List<PlatUserAddress> select(@RequestBody PlatUserAddress platUserAddress) throws Exception;


    /**
     * 新增平台地址
     *
     * @param platUserAddress
     * @return
     */
    @PostMapping(value = "/fn/platUserAddress/insert")
    ResultEntity insert(@RequestBody PlatUserAddress platUserAddress);

    /**
     * 查询统计
     *
     * @param platUserAddress
     * @return
     */
    @PostMapping(value = "/fn/platUserAddress/selectCount")
    ResultEntity selectCount(@RequestBody PlatUserAddress platUserAddress);

    /**
     * 获取平台地址信息
     *
     * @param platUserAddress
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/fn/platUserAddress/selectOne")
    PlatUserAddress selectOne(@RequestBody PlatUserAddress platUserAddress) throws Exception;

}
