package com.mi.hundsun.oxchains.provider.user.controller;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.po.cms.Announcement;
import com.mi.hundsun.oxchains.base.core.po.cms.Banner;
import com.mi.hundsun.oxchains.base.core.po.user.UserAttachment;
import com.mi.hundsun.oxchains.base.core.service.cms.AnnouncementService;
import com.mi.hundsun.oxchains.base.core.service.cms.BannerService;
import com.mi.hundsun.oxchains.base.core.service.user.UserAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(value = "首页相关服务", description = "RegistLoginController Created By db at 2018-04-08 20:58")
@RestController
@RequestMapping("/prod/index")
public class IndexController extends GenericController {

    @Autowired
    BannerService bannerService;
    @Autowired
    UserAttachmentService userAttachmentService;
    @Autowired
    AnnouncementService announcementService;


    @ApiOperation(value = "首页banner列表", notes = "首页banner列表")
    @ApiImplicitParams({
    })
    @PostMapping(value = "/bannerList")
    public ResultEntity bannerList() {
        try {
            Integer size = 3;
            List<Banner> bannerList = bannerService.getIndexList(size);
            return ok(bannerList);
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "获取图片真实地址", notes = "获取图片真实地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "picUuid", value = "图片UUID", required = true, dataType = "String"),
    })
    @PostMapping(value = "/getRealPath")
    public String getRealPath(String picUuid) {
        if (StringUtils.isBlank(picUuid)){
            return null;
        }
        UserAttachment userAttachment= userAttachmentService.selectOne(new UserAttachment(a->{
            a.setDelFlag(GenericPo.DELFLAG.NO.code);
            a.setUuid(picUuid);
        }));
        if (null != userAttachment){
            return  userAttachment.getRealPath();
        }
        return null;
    }

    @ApiOperation(value = "首页公告", notes = "首页公告")
    @ApiImplicitParams({
    })
    @PostMapping(value = "/getNewAnnouncement")
    public ResultEntity getNewAnnouncement() {
        Announcement announcement = announcementService.getNewAnnouncement();
        return ok(announcement);
    }

    @ApiOperation(value = "首页公告", notes = "首页公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ancUuid", value = "公告uuid", required = true, dataType = "String")
    })
    @PostMapping(value = "/announcementDetail")
    public ResultEntity announcementDetail(String ancUuid) {
        try {
            Announcement announcement = announcementService.selectOne(new Announcement(a->{
                a.setUuid(ancUuid);
                a.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            return ok(announcement);
        } catch (Exception e) {
            e.printStackTrace();
            return fail("获取失败");
        }
    }

    @ApiOperation(value = "公告列表", notes = "公告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @PostMapping(value = "/announcementList")
    public ResultEntity announcementList(Integer pageSize,Integer pageNumber) {
        if(StringUtils.isBlank(pageSize)){
            return fail("pageSize不能为空");
        }
        if(StringUtils.isBlank(pageNumber)){
            return fail("pageNumber不能为空");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            long recordCount = announcementService.announcementListCount(Announcement.STATE.PUBLISHED.code);
            //总分页数
            int pageNum = (int) recordCount / pageSize + (recordCount % pageSize > 0 ? 1 : 0);
            List<Announcement> list = announcementService.getAnnouncementList(Announcement.STATE.PUBLISHED.code,pageSize, pageNumber);
            map.put("recordCount", recordCount);
            map.put("pageNum", pageNum);
            map.put("data", list);
            return ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            return fail("查询失败");
        }
    }

}
