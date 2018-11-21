package com.mi.hundsun.oxchains.consumer.admin.controller.log;

import com.mi.hundsun.oxchains.base.core.po.system.ServiceLog;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.ServiceLogInterface;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class ServiceLogJsonController extends GenericController<Integer,ServiceLog> {

    @Autowired
    private ServiceLogInterface logInterface;

    /**
     * 操作日志json数据
     */
    @ResponseBody
    @RequestMapping("log/json/list")
    @RequiresPermissions("sys:log:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return logInterface.getDtGridList(dtGridPager);
    }

}
