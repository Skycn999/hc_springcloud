package com.mi.hundsun.oxchains.consumer.admin.controller.index;

import com.mi.hundsun.oxchains.base.core.model.user.UserFormModel;
import com.mi.hundsun.oxchains.base.core.tx.model.CountDealOrderModel;
import com.mi.hundsun.oxchains.base.core.tx.po.DealOrder;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.DealOrderInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UsersInterface;
import com.xiaoleilu.hutool.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private DealOrderInterface dealOrderInterface;
    @Autowired
    private UsersInterface usersInterface;

    @RequestMapping(value = BASE_URI, method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        modelMap.put("statTime", DateUtil.now());
        modelMap.put("update", "");
        //实时统计数据
        List<CountDealOrderModel> countDirections = dealOrderInterface.countDirection();
        for (CountDealOrderModel model : countDirections) {
            if (model.getDirection() == DealOrder.DIRECTION.BUYIN.code) {
                modelMap.put("txBuyInCount", model.getNumbers());
            } else {
                modelMap.put("txSellOutCount", model.getNumbers());
            }
        }
        UserFormModel model = usersInterface.countByMgrIndex();
        modelMap.put("loginUserCount", model.getLoginNum());
        modelMap.put("totalUserCount", model.getTotalNum());
        modelMap.put("onlineUserCount", model.getOnlineNum());
        modelMap.put("newAddUserCount", model.getRegNum());

        return ok("index");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("redirect:" + BASE_URI);
    }


}