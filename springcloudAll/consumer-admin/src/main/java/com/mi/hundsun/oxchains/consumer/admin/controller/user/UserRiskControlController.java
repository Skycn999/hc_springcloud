package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.NetWorthControlInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.PercentControlInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.ServiceFeeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 用户风控设置
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserRiskControlController extends BaseController {
    @Autowired
    ServiceFeeInterface serviceFeeInterface;
    @Autowired
    NetWorthControlInterface netWorthControlInterface;
    @Autowired
    PercentControlInterface percentControlInterface;


    /**
     * 用户风控设置列表
     * @param modelMap
     * @return
     */
    @RequestMapping("riskControl/list")
    public String list(ModelMap modelMap) {
        List<NetWorthControl>  netWorths = netWorthControlInterface.select(new NetWorthControl(c->{
            c.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        List<PercentControl>  percents = percentControlInterface.select(new PercentControl(p->{
            p.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        modelMap.addAttribute("netWorths", netWorths);
        modelMap.addAttribute("percents", percents);
        return ok("user/riskControl/list");
    }

}
