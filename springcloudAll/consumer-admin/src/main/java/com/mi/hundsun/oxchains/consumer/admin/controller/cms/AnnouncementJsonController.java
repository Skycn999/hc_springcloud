package com.mi.hundsun.oxchains.consumer.admin.controller.cms;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.cms.Announcement;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.cms.AnnouncementInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping(BaseController.BASE_URI)
public class AnnouncementJsonController extends GenericController<Integer, Announcement> {

    @Resource
    AnnouncementInterface announcementInterface;

    @RequestMapping("announcement/json/list")
    @RequiresPermissions("sys:announcement:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return announcementInterface.getDtGridList(dtGridPager);
    }

    /**
     * 新增保存
     *
     * @param announcement
     * @return
     * @throws Exception
     */

    @RequestMapping("announcement/json/add")
    @RequiresPermissions("sys:announcement:add")
    public ResultEntity addJson(Announcement announcement) throws Exception {
        checkannouncement(announcement, "add");
        ResultEntity resultEntity = new ResultEntity();
        Admin admin = AdminSessionHelper.getCurrAdmin();
        announcement.setAnNo(OrderNoUtils.getSerialNumber());
        announcement.setCreator(admin.getName());
        announcement.setCreateTime(new Date());
        announcement.setUuid(RandomUtils.randomCustomUUID().toString());
        announcementInterface.insert(announcement);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    private void checkannouncement(Announcement announcement, String action) {
        if (null == announcement) {
            throw new BussinessException("参数有误");
        }
        if ("edit".equals(action)) {
            if (null == announcement.getId()) {
                throw new BussinessException("参数有误");
            }
        }
        if(StringUtils.isBlank(announcement.getTitle())){
            throw new BussinessException("公告标题不能为空");
        } else if(announcement.getTitle().length()>500){
            throw new BussinessException("输入长度不能超过500位");
        }
//        if(null == announcement.getTplNo()){
//            throw new BussinessException("协议编号不能为空");
//        } else if(announcement.getTplNo().length()>50){
//            throw new BussinessException("输入长度不能超过50位");
//        }
        if(StringUtils.isBlank(announcement.getCnContent())){
            throw new BussinessException("内容不能为空");
        } else if(!ValidateUtils.isChinese(announcement.getCnContent())){
            throw new BussinessException("请输入中文");
        }
        if(StringUtils.isBlank(announcement.getEnContent())){
            throw new BussinessException("内容不能为空");
        } else if(ValidateUtils.isContainChinese(announcement.getEnContent())){
            throw new BussinessException("请输入英文");
        }
        if(null == announcement.getState()){
            throw new BussinessException("请选择状态");
        } else if(announcement.getState() !=Announcement.STATE.UNPUBLISHED.code && announcement.getState() !=Announcement.STATE.PUBLISHED.code ){
            throw new BussinessException("状态不正确");
        }
    }

    /**
     * 发布
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "announcement/json/publish")
    @RequiresPermissions("sys:announcement:publish")
    public ResultEntity publish(Integer id) throws Exception {
        if(id==null){
            throw new BussinessException("参数有误");
        }
        Announcement announcement = announcementInterface.selectOne(new Announcement(a -> {
            a.setId(id);
            a.setDelFlag(Announcement.DELFLAG.NO.code);
        }));
        if (null == announcement) {
            throw new BussinessException("您要发布的内容不存在");
        } else if (announcement.getState() == Announcement.STATE.PUBLISHED.code) {
            throw new BussinessException("当前状态下不能操作");
        }
        announcementInterface.updateByPrimaryKeySelective(new Announcement(a -> {
            a.setId(announcement.getId());
            a.setState(Announcement.STATE.PUBLISHED.code);
            a.setCreateTime(new Date());
        }));
        return ok();
    }

    /**
     * 撤回
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "announcement/json/back")
    @RequiresPermissions("sys:announcement:back")
    public ResultEntity back(Integer id) throws Exception {
        if(id==null){
            throw new BussinessException("参数有误");
        }
        Announcement announcement = announcementInterface.selectOne(new Announcement(a -> {
            a.setId(id);
            a.setDelFlag(Announcement.DELFLAG.NO.code);
        }));
        if (null == announcement) {
            throw new BussinessException("您要撤回的内容不存在");
        } else if (announcement.getState() != Announcement.STATE.PUBLISHED.code) {
            throw new BussinessException("当前状态下不能操作");
        }
        announcementInterface.updateByPrimaryKeySelective(new Announcement(a -> {
            a.setId(announcement.getId());
            a.setState(Announcement.STATE.REVOKE.code);
        }));
        return ok();
    }
}

