package com.mi.hundsun.oxchains.consumer.admin.controller.cms;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.cms.Banner;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.cms.BannerInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping(BaseController.BASE_URI)
public class BannerJsonController extends GenericController<Integer, Banner> {

    @Resource
    BannerInterface bannerInterface;

    @RequestMapping(value="banner/json/list", method= RequestMethod.POST)
    @RequiresPermissions("sys:banner:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return bannerInterface.getDtGridList(dtGridPager);
    }

    /**
     * 新增保存
     *
     * @param banner
     * @return
     * @throws Exception
     */
    @RequestMapping("banner/json/add")
    @RequiresPermissions("sys:banner:add")
    public ResultEntity addJson(Banner banner) throws Exception {
        checkBanner(banner, "add");
        ResultEntity resultEntity = new ResultEntity();
        Admin admin = AdminSessionHelper.getCurrAdmin();
        banner.setBannerNo(OrderNoUtils.getSerialNumber());
        banner.setCreator(admin.getName());
        banner.setCreateTime(new Date());
        banner.setUuid(RandomUtils.randomCustomUUID().toString());
        bannerInterface.insert(banner);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑用户信息
     *
     * @param banner
     * @return
     */
    @ResponseBody
    @RequestMapping("banner/json/edit")
    @RequiresPermissions("sys:banner:edit")
    public ResultEntity editJson(Banner banner) throws Exception {
        checkBanner(banner, "edit");
        bannerInterface.updateByPrimaryKeySelective(banner);
        return ok();
    }

    private void checkBanner(Banner banner, String action) {
        if (null == banner) {
            throw new BussinessException("参数有误");
        }
        if ("edit".equals(action)) {
            if (null == banner.getId()) {
                throw new BussinessException("参数有误");
            }
        }
        if(StringUtils.isBlank(banner.getTitle())){
            throw new BussinessException("请输入标题");
        } else if(banner.getTitle().length()>200){
            throw new BussinessException("输入长度不能大于200个字符");
        }
        if(null == banner.getAppPath()){
            throw new BussinessException("请添加app端banner");
        } else if(banner.getAppPath().length()>200){
            throw new BussinessException("app长度不能大于200个字符");
        }
        if(StringUtils.isBlank(banner.getPcPath())){
            throw new BussinessException("请添加pc端地址");
        } else if(banner.getAppPath().length()>200){
            throw new BussinessException("pc地址长度不能大于200个字符");
        }
        if(null == banner.getState()){
            throw new BussinessException("请选择状态");
        } else if( banner.getState() != Banner.STATE.UNPUBLISHED.code && banner.getState() !=Banner.STATE.PUBLISHED.code ){
            throw new BussinessException("状态不正确");
        }
    }

    /**
     * 发布
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "banner/json/publish")
    @RequiresPermissions("sys:banner:publish")
    public ResultEntity publish(Integer id) throws Exception {
        if(id==null){
            throw new BussinessException("参数有误");
        }
        Banner banner = bannerInterface.selectOne(new Banner(a -> {
            a.setId(id);
            a.setDelFlag(Banner.DELFLAG.NO.code);
        }));
        if (null == banner) {
            throw new BussinessException("您要发布的内容不存在");
        } else if (banner.getState() == Banner.STATE.PUBLISHED.code) {
            throw new BussinessException("当前状态下不能操作");
        }
        bannerInterface.updateByPrimaryKeySelective(new Banner(a -> {
            a.setId(banner.getId());
            a.setState(Banner.STATE.PUBLISHED.code);
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
    @RequestMapping(value = "banner/json/back")
    @RequiresPermissions("sys:banner:back")
    public ResultEntity back(Integer id) throws Exception {
        if(id==null){
            throw new BussinessException("参数有误");
        }
        Banner banner = bannerInterface.selectOne(new Banner(a -> {
            a.setId(id);
            a.setDelFlag(Banner.DELFLAG.NO.code);
        }));
        if (null == banner) {
            throw new BussinessException("您要撤回的内容不存在");
        } else if (banner.getState() != Banner.STATE.PUBLISHED.code) {
            throw new BussinessException("当前状态下不能操作");
        }
        bannerInterface.updateByPrimaryKeySelective(new Banner(a -> {
            a.setId(banner.getId());
            a.setState(Banner.STATE.REVOKE.code);
        }));
        return ok();
    }
}
