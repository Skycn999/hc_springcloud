package com.mi.hundsun.oxchains.consumer.admin.controller.fn;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.ImportExcelUntil;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.fn.PlatUserAddressInterface;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 平台地址管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class PlatUserAddressJsonController extends GenericController<Integer, PlatUserAddress> {
    @Autowired
    PlatUserAddressInterface platUserAddressInterface;

    @ResponseBody
    @RequestMapping("platUserAddress/json/list")
    @RequiresPermissions("sys:platUserAddress:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return platUserAddressInterface.getDtGridList(dtGridPager);
    }


    @ResponseBody
    @RequestMapping("platUserAddress/json/importExcel")
    @RequiresPermissions("sys:platUserAddress:import")
    public ResultEntity importExcel(MultipartFile file) throws Exception {
        // 解析Excel数据
        List<PlatUserAddress> list = ImportExcelUntil.importExcel(file, PlatUserAddress.class);
        int successCount = 0;
        int failedCount = 0;
        int repeadCount = 0;

        if (CollectionUtils.isNotEmpty(list)) {
            for (PlatUserAddress address : list) {
                ResultEntity res = platUserAddressInterface.selectCount(new PlatUserAddress(t -> t.setAddress(address.getAddress())));
                if (res.getCode() == 200 && Integer.parseInt(res.getData().toString()) == 0) {
                    ResultEntity result = platUserAddressInterface.insert(address);
                    if (result.getCode() == 200) {
                        successCount++;
                    } else {
                        failedCount++;
                    }
                } else {
                    repeadCount++;
                }
            }
        }
        String message = "处理地址总量：[" + list.size() + "]，" + "成功：[" + successCount + "]，" + "失败：[" + failedCount + "]，" + "重复未处理：[" + repeadCount + "]。";
        return ok(null, message);
    }
}
