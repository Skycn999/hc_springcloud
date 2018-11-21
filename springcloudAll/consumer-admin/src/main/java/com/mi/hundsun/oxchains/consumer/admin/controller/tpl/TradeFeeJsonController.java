package com.mi.hundsun.oxchains.consumer.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import com.mi.hundsun.oxchains.base.core.po.tpl.TradeFee;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.TradeFeeInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(BaseController.BASE_URI)
public class TradeFeeJsonController extends GenericController<Integer, PercentControl> {

    @Autowired
    TradeFeeInterface tradeFeeInterface;

    @ResponseBody
    @RequestMapping("tradeFee/json/list")
    @RequiresPermissions("sys:tradeFee:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return tradeFeeInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加交易手续费模板
     *
     * @param tradeFee
     * @return
     */
    @ResponseBody
    @RequestMapping("/tradeFee/json/add")
    @RequiresPermissions("sys:tradeFee:save")
    public ResultEntity addJson(TradeFee tradeFee) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        tradeFee.setUuid(RandomUtils.randomCustomUUID().toString());
        tradeFee.setTplNo(OrderNoUtils.getSerialNumber());
        String result = checkModel(tradeFee);
        if (result != null) {
            throw new BussinessException(result);
        }
        List<TradeFee> tradeFeeList = tradeFeeInterface.select(new TradeFee(t -> {
            t.setSymbol(tradeFee.getSymbol());
        }));
        if (tradeFeeList.size()>0){
            tradeFeeList.forEach(t -> {
                t.setState(TradeFee.STATE.DISABLE.code);
                tradeFeeInterface.updateByPrimaryKeySelective(t);
            });
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        tradeFee.setCreateTime(new Date());
        tradeFee.setCreator(admin.getName());
        tradeFee.setUpdator(admin.getName());
        tradeFeeInterface.insert(tradeFee);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 校验
     *
     * @param tradeFee
     * @return
     */
    private String checkModel(TradeFee tradeFee) {
        String result = null;
        BigDecimal bigDecimal = new BigDecimal("100");
        if (StringUtils.isBlank(tradeFee.getTplName())) {
            result = "模版名称不能为空";
        } else if (tradeFee.getTplName().length() > 50) {
            result = "模版名称不能超过50个字符";
        }
        if (StringUtils.isBlank(tradeFee.getSymbol())) {
            result = "请输入交易币种";
        } else if (tradeFee.getSymbol().length() > 20) {
            result = "交易币种的长度不能超过20位";
        } else if (!tradeFee.getSymbol().contains("_")) {
            result = "输入交易币种格式不正确,中间含有_";
        }
        if (null == tradeFee.getBuyFee()) {
            result = "买入交易手续费率不能为空";
        } else if (!ValidateUtils.isPrice(tradeFee.getBuyFee().toString())) {
            result = "输入格式不正确，请输入整数或小数，小数点前最多保留两位,小数点后最多四位";
        } else if(tradeFee.getBuyFee().compareTo(bigDecimal)>0 || tradeFee.getBuyFee().compareTo(BigDecimal.ZERO)<=0){
            result = "买入交易手续费率应该在0~100之间";
        }
        if (null == tradeFee.getSellFee()) {
            result = "卖出交易手续费率不能为空";
        } else if (!ValidateUtils.isPrice(tradeFee.getSellFee().toString())) {
            result = "输入格式不正确，请输入整数或小数，小数点前最多保留两位,小数点后最多四位";
        }else if(tradeFee.getSellFee().compareTo(bigDecimal)>0 || tradeFee.getSellFee().compareTo(BigDecimal.ZERO)<=0){
            result = "卖出交易手续费率应该在0~100之间";
        }
        if (null == tradeFee.getState()) {
            result = "请选择状态";
        } else if (tradeFee.getState() != TradeFee.STATE.ENABLE.code && tradeFee.getState() != TradeFee.STATE.DISABLE.code) {
            result = "状态选择不正确";
        }
        return result;
    }

    /**
     * 编辑手续费模板
     *
     * @param tradeFee
     * @return
     */
    @ResponseBody
    @RequestMapping("/tradeFee/json/update")
    @RequiresPermissions("sys:tradeFee:edit")
    public ResultEntity editJson(TradeFee tradeFee) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        String result = checkModel(tradeFee);
        if (result != null) {
            throw new BussinessException(result);
        }
        List<TradeFee> tradeFeeList = tradeFeeInterface.select(new TradeFee(t -> {
            t.setSymbol(tradeFee.getSymbol());
        }));
        if (tradeFeeList.size()>0){
            tradeFeeList.forEach(t -> {
                t.setState(TradeFee.STATE.DISABLE.code);
                tradeFeeInterface.updateByPrimaryKeySelective(t);
            });
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        tradeFee.setUpdateTime(new Date());
        tradeFee.setUpdator(admin.getName());
        tradeFeeInterface.updateByPrimaryKeySelective(tradeFee);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
}
