package com.mi.hundsun.oxchains.consumer.admin.controller.user;



import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.entity.export.ExportUtils;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UserAddressInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 *用户地址
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserAddressJsonController extends GenericController<Integer,UserAddress> {

    @Resource
    UserAddressInterface userAddressInterface;

    @ResponseBody
    @RequestMapping("userAddress/json/list")
    @RequiresPermissions("sys:user:address:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return userAddressInterface.getDtGridList(dtGridPager);
    }

    /**
     * 列表导出
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "userAddress/json/export")
    @RequiresPermissions("sys:userAddress:export")
    public ResultEntity export(String dtGridPager, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 执行导出
        try {
        DtGrid dtGrid  = userAddressInterface.getDtGridListExport(dtGridPager);
        ExportUtils.export(request, response, dtGrid);
        return ok();
    } catch (Exception e) {
        e.printStackTrace();
        return fail("导出失败");
    }
   }
}
