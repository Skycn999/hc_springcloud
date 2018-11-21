package com.mi.hundsun.oxchains.consumer.admin.service.cms;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.cms.Banner;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface BannerInterface {

    /**
     * 列表查询
     *
     * @return
     */
    @PostMapping(value = "/cms/banner/select")
    List<Banner> select(Banner banner) throws BussinessException;

    /**
     * 分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/cms/banner/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 插入
     * @param banner
     */
    @PostMapping(value = "/cms/banner/insert")
    void insert(@RequestBody Banner banner);

    /**
     * 编辑用户
     *
     * @param banner
     * @return
     */
    @PostMapping(value = "/cms/banner/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Banner banner);

    /**
     * 查询单个对象
     *
     * @param banner
     * @return
     */
    @PostMapping(value = "/cms/banner/selectOne")
    Banner selectOne(@RequestBody Banner banner);

}
