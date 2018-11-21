package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.entity.export.ExportUtils;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UserFreezeInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户冻结
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserFreezeJsonController extends GenericController<Integer,UserFreeze> {

    @Autowired
    UserFreezeInterface userFreezeInterface;

    @ResponseBody
    @RequestMapping("userFreeze/json/list")
    @RequiresPermissions("sys:user:freeze:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return userFreezeInterface.getDtGridList(dtGridPager);
    }


    /**
     * 编辑用户信息
     *
     * @param userFreeze
     * @return
     */
    @ResponseBody
    @RequestMapping("user/freeze/json/set")
    @RequiresPermissions("sys:user:freeze:set")
    public ResultEntity editJson(UserFreeze userFreeze) throws Exception {
        userFreezeInterface.updateByPrimaryKeySelective(userFreeze);
        return ok();
    }


    /**
     * 列表导出
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "userFreeze/json/export")
    @RequiresPermissions("sys:userFreeze:export")
    public ResultEntity export(String dtGridPager,HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 执行导出
        try {
            DtGrid dtGrid  = userFreezeInterface.getDtGridListExport(dtGridPager);
            ExportUtils.export(request, response, dtGrid);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("导出失败");
        }
    }

}
