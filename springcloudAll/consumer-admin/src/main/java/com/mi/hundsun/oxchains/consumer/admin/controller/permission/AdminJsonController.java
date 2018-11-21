package com.mi.hundsun.oxchains.consumer.admin.controller.permission;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.AdminInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * 后台管理员
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class AdminJsonController extends GenericPo {

    @Resource
    AdminInterface adminInterface;

    /**
     * 管理员列表json数据
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping("admin/json/list")
    @RequiresPermissions("sys:admin:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return adminInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加管理员
     *
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("admin/json/add")
    @RequiresPermissions("sys:admin:save")
    public ResultEntity addJson(Admin admin) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        adminInterface.saveAdmin(admin);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑管理员
     *
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("admin/json/edit")
    @RequiresPermissions("sys:admin:edit")
    public ResultEntity editJson(Admin admin) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        adminInterface.updateAdmin(admin);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 管理员编辑资料
     *
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("admin/json/profile")
    public ResultEntity profileJson(Admin admin) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        Admin curr = AdminSessionHelper.getCurrAdmin();
        if (StrUtil.isNotBlank(admin.getAvatarUrl())) {
            curr.setAvatarUrl(admin.getAvatarUrl());
        }
        if (StrUtil.isNotBlank(admin.getPassword())) {
            curr.setPassword(DigestUtil.md5Hex(admin.getPassword()));
        }
        if (StrUtil.isNotBlank(admin.getAvatarUrl()) || StrUtil.isNotBlank(admin.getPassword())) {
            adminInterface.updateByPrimaryKeySelective(curr);
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 删除管理员
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("admin/json/del")
    @RequiresPermissions("sys:admin:del")
    public ResultEntity delJson(int id) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        adminInterface.deleteByPrimaryKey(id);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
}