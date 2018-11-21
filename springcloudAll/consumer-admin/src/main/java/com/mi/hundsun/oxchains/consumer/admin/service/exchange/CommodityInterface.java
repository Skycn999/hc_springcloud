package com.mi.hundsun.oxchains.consumer.admin.service.exchange;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface CommodityInterface {
    /**
     * 资料代码查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/exchange/commodity/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws Exception;

    /**
     * 查询代码资料
     * @param commodity
     * @return
     */
    @PostMapping(value = "/exchange/commodity/select")
    List<Commodity> select(@RequestBody Commodity commodity);

    /**
     * 查询代码资料
     * @param commodity
     * @return
     */
    @PostMapping(value = "/exchange/commodity/selectOne")
    Commodity selectOne(@RequestBody Commodity commodity);

    /**
     * 插入
     * @param commodity
     */
    @PostMapping(value = "/exchange/commodity/insert")
    void insert(@RequestBody Commodity commodity);

    /**
     * 编辑用户
     *
     * @param commodity
     * @return
     */
    @PostMapping(value = "/exchange/commodity/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Commodity commodity);

    /**
     * 校验code
     * @param code
     * @return
     */
    @PostMapping(value = "/exchange/commodity/checkCode")
    boolean checkCode(@RequestParam("code") String code);
}
