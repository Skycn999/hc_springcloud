package com.mi.hundsun.oxchains.consumer.admin.controller.tx;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.SubDelegationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 子委托管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class SubDelegationJsonController extends GenericController<Integer, SubDelegation> {

    @Autowired
    SubDelegationInterface subDelegationInterface;

    /**
     * 分页查询
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("subDelegation/json/list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return  subDelegationInterface.getDtGridList(dtGridPager);
    }

}
