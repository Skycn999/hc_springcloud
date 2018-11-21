package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.ExchangeInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class ExchangeJsonController extends GenericController<Integer, Exchange> {
    @Autowired
    ExchangeInterface exchangeInterface;

    /**
     * 交易所分页列表
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("exchange/json/list")
    @RequiresPermissions("sys:exchange:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return exchangeInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加交易所
     *
     * @param exchange
     * @return
     */
    @ResponseBody
    @RequestMapping("/exchange/json/add")
    @RequiresPermissions("sys:serviceFee:save")
    public ResultEntity addJson(Exchange exchange) throws Exception {
        exchange.setUuid(RandomUtils.randomCustomUUID().toString());

        String exchangeNo;
        while (true) {
            String exNo = "E" + OrderNoUtils.getExNo();
            List<Exchange> list = exchangeInterface.select(new Exchange(t -> t.setExNo(exNo)));
            if (CollectionUtils.isEmpty(list)) {
                exchangeNo = exNo;
                break;
            }
        }
        exchange.setExNo(exchangeNo);
        String result = checkModel(exchange);
        if (result != null) {
            throw new BussinessException(result);
        }
        if (exchange.getState() == Exchange.STATE.ENABLE.code) {
            exchange.setEnableTime(new Date());
        } else {
            exchange.setStopTime(new Date());
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        exchange.setCreateTime(new Date());
        exchange.setCreator(admin.getName());
        exchange.setDelFlag(GenericPo.DELFLAG.NO.code);
        return exchangeInterface.insert(exchange);
    }

    /**
     * 校验
     *
     * @param exchange
     * @return
     */
    private String checkModel(Exchange exchange) {
        if (StringUtils.isBlank(exchange.getName())) {
            return "交易所名称不能为空";
        } else if (exchange.getName().length() > 60) {
            return "交易所名称不能超过60个字符";
        }
        if (StringUtils.isBlank(exchange.getQuoteUrl())) {
            return "行情地址不能为空";
        } else if (exchange.getQuoteUrl().length() > 80) {
            return "行情地址长度不能超过80位";
        }
        if (StringUtils.isBlank(exchange.getTxUrl())) {
            return "交易地址不能为空";
        } else if (exchange.getTxUrl().length() > 80) {
            return "交易地址长度不能超过80位";
        }
        if (StringUtils.isBlank(exchange.getState())) {
            return "请选择状态";
        }
        return null;
    }

    /**
     * 编辑交易所
     *
     * @param exchange
     * @return
     */
    @ResponseBody
    @RequestMapping("/exchange/json/update")
    @RequiresPermissions("sys:exchange:edit")
    public ResultEntity editJson(Exchange exchange) throws Exception {
        String result = checkModel(exchange);
        if (result != null) {
            throw new BussinessException(result);
        }
        if (exchange.getState() == Exchange.STATE.ENABLE.code) {
            exchange.setEnableTime(new Date());
        } else {
            exchange.setStopTime(new Date());
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        exchange.setUpdateTime(new Date());
        exchange.setUpdator(admin.getName());
        return exchangeInterface.updateExchange(exchange);
    }

}
