package com.mi.hundsun.oxchains.provider.admin.controller.cms;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.cms.Announcement;
import com.mi.hundsun.oxchains.base.core.service.cms.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("公告")
@Slf4j
@RestController
@RequestMapping("/cms/announcement")
public class AnnouncementController {

    @Autowired
    AnnouncementService announcementService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return announcementService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<Announcement> select(Announcement announcement) throws Exception {
        return announcementService.select(announcement);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insertAnnouncement(@RequestBody Announcement announcement) throws Exception {
        announcementService.insert(announcement);
    }

    @ApiOperation(value = "修改状态")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Announcement nnouncement) throws BussinessException {
        announcementService.updateByPrimaryKeySelective(nnouncement);
    }

    @ApiOperation(value = "查询单个公告信息")
    @PostMapping(value = "/selectOne")
    public Announcement selectOne(@RequestBody Announcement announcement) throws BussinessException {
        return announcementService.selectOne(announcement);
    }
}
