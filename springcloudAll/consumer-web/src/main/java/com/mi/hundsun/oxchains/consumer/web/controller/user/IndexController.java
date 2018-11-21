package com.mi.hundsun.oxchains.consumer.web.controller.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.consumer.web.config.WebGenericController;
import com.mi.hundsun.oxchains.consumer.web.service.user.IndexInterface;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 首页controller
 */
@RestController
@RequestMapping("/api/web/index")
public class IndexController extends WebGenericController {
    @Autowired
    IndexInterface indexInterface;

    @ApiImplicitParams({
    })
    @ApiOperation(value = "首页banner列表-APP", notes = "首页banner列表-APP")
    @RequestMapping("/bannerList")
    public ResultEntity bannerList() {
        return indexInterface.bannerList();
    }

    @ApiImplicitParams({
    })
    @ApiOperation(value = "首页公告", notes = "首页最新公告")
    @RequestMapping("/getNewAnnouncement")
    public ResultEntity getNewAnnouncement() {
        return indexInterface.getNewAnnouncement();
    }

    @ApiImplicitParams({
           @ApiImplicitParam(name = "ancUuid", value = "公告uuid", required = true, dataType = "String")
    })
    @ApiOperation(value = "公告详情", notes = "公告详情")
    @RequestMapping("/announcementDetail")
    public ResultEntity announcementDetail(String ancUuid){
        if(StringUtils.isBlank(ancUuid)){
            return fail("参数错误");
        }
        return indexInterface.announcementDetail(ancUuid);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @ApiOperation(value = "公告列表", notes = "公告列表")
    @RequestMapping("/announcementList")
    public ResultEntity announcementList(Integer pageSize,Integer pageNumber){
        if(StringUtils.isBlank(pageSize)||StringUtils.isBlank(pageNumber)){
            return fail("参数错误");
        }
        return indexInterface.announcementList(pageSize,pageNumber);
    }
}
