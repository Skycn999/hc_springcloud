package com.mi.hundsun.oxchains.consumer.admin.service.cms;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.cms.Announcement;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface AnnouncementInterface {

    /**
     * 列表查询
     *
     * @return
     */
    @PostMapping(value = "/cms/announcement/select")
    List<Announcement> select(Announcement announcement) throws BussinessException;

    /**
     * 用户列表查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/cms/announcement/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 插入
     * @param announcement
     */
    @PostMapping(value = "/cms/announcement/insert")
    void insert(@RequestBody Announcement announcement);

    /**
     * 查询单个对象
     *
     * @param announcement
     * @return
     */
    @PostMapping(value = "/cms/announcement/selectOne")
    Announcement selectOne(@RequestBody Announcement announcement);

    /**
     * 修改状态
     *
     * @param announcement
     * @return
     */
    @PostMapping(value = "/cms/announcement/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Announcement announcement);
}
