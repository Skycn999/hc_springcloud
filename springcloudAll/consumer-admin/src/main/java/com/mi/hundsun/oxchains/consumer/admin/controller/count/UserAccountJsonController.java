package com.mi.hundsun.oxchains.consumer.admin.controller.count;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.count.UserAccountInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserAccountJsonController extends GenericPo {

    @Resource
    UserAccountInterface userAccountInterface;

    @ResponseBody
    @RequestMapping("userAccount/json/list")
    @RequiresPermissions("sys:userAccount:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return userAccountInterface.getDtGridList(dtGridPager);
    }
}
