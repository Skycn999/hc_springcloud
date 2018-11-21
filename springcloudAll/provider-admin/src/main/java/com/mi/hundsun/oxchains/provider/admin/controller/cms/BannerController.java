package com.mi.hundsun.oxchains.provider.admin.controller.cms;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.cms.Banner;
import com.mi.hundsun.oxchains.base.core.service.cms.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("banner")
@Slf4j
@RestController
@RequestMapping("/cms/banner")
public class BannerController {

    @Autowired
    BannerService bannerService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return bannerService.getDtGridList(dtGridPager);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<Banner> select(Banner banner) throws Exception {
        return bannerService.select(banner);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insertBanner(@RequestBody Banner banner) throws Exception {
        bannerService.insert(banner);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Banner banner) throws BussinessException {
        bannerService.updateByPrimaryKeySelective(banner);
    }

    @ApiOperation(value = "查询单个banner信息")
    @PostMapping(value = "/selectOne")
    public Banner selectOne(@RequestBody Banner banner) throws BussinessException {
        return bannerService.selectOne(banner);
    }
}
