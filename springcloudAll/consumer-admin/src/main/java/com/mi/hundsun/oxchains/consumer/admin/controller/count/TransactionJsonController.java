package com.mi.hundsun.oxchains.consumer.admin.controller.count;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.count.TransactionInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class TransactionJsonController extends GenericPo {

    @Resource
    TransactionInterface transactionInterface;

    @ResponseBody
    @RequestMapping("transaction/json/list")
    @RequiresPermissions("sys:transaction:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return transactionInterface.getDtGridList(dtGridPager);
    }
}
