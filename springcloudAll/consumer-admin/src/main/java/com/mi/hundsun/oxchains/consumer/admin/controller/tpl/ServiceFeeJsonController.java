package com.mi.hundsun.oxchains.consumer.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.ServiceFeeInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 手续费模板controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class ServiceFeeJsonController extends GenericController<Integer, ServiceFee> {
    @Autowired
    ServiceFeeInterface serviceFeeInterface;

    @ResponseBody
    @RequestMapping("serviceFee/json/list")
    @RequiresPermissions("sys:serviceFee:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return serviceFeeInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加手续费模板
     *
     * @param serviceFee
     * @return
     */
    @ResponseBody
    @RequestMapping("/serviceFee/json/add")
    @RequiresPermissions("sys:serviceFee:save")
    public ResultEntity addJson(ServiceFee serviceFee) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        serviceFee.setUuid(RandomUtils.randomCustomUUID().toString());
        serviceFee.setTplNo(OrderNoUtils.getSerialNumber());
        String result =  checkModel(serviceFee);
        if(result != null){
            throw new BussinessException(result);
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        serviceFee.setCreateTime(new Date());
        serviceFee.setCreator(admin.getName());
        serviceFee.setUpdator(admin.getName());
        serviceFeeInterface.insertFee(serviceFee);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 校验
     * @param serviceFee
     * @return
     */
    private String checkModel(ServiceFee serviceFee){
        String result = null;
        if(StringUtils.isBlank(serviceFee.getTplName())){
            result = "模版名称不能为空";
        }else if(serviceFee.getTplName().length()>50){
            result = "模版名称不能超过50个字符";
        }
        if(StringUtils.isBlank(serviceFee.getCoinCurrency())){
            result = "请选择币种";
        }
        if(null == serviceFee.getServiceFee()){
            result = "提现手续费不能为空";
        }else if (!ValidateUtils.isPrice3(serviceFee.getServiceFee().toString())) {
            result = "手续费格式不正确,可整数可小数,小数点前后最大可为10位";
        }
        if(null == serviceFee.getTodayMaxAmount()){
            result = "当日累计提币最大限额不能为空";
        }else if (!ValidateUtils.isPrice3(serviceFee.getTodayMaxAmount().toString())) {
            result = "当日累计提币最大限额格式不正确,可整数可小数,小数点前后最大可为10位";
        }
        if(null == serviceFee.getOnceMinAmount()){
            result = "单次提币最小限额不能为空";
        }else if (!ValidateUtils.isPrice3(serviceFee.getOnceMinAmount().toString())) {
            result = "单次提币最小限额格式不正确,可整数可小数,小数点前后最大可为10位";
        }
        if(null == serviceFee.getState()){
            result = "请选择状态";
        }else if(serviceFee.getState() != ServiceFee.STATE.ENABLE.code && serviceFee.getState() != ServiceFee.STATE.DISABLE.code){
            result = "状态选择不正确";
        }
        if(null == serviceFee.getIsDefault()){
            result = "请选择是否默认";
        }else if(serviceFee.getIsDefault() != ServiceFee.ISDEFAULT.YES.code && serviceFee.getIsDefault() != ServiceFee.ISDEFAULT.NO.code){
            result = "是否默认不正确";
        }
        if(serviceFee.getOnceMinAmount().compareTo(serviceFee.getTodayMaxAmount())>0){
            result = "单次提币最小限额不能大于当日累计提币最大限额";
        }else if(serviceFee.getServiceFee().compareTo(serviceFee.getOnceMinAmount())>=0){
            result = "提币手续费不能大于单次提币最小限额";
        }
        return result;
    }

    /**
     * 编辑手续费模板
     *
     * @param serviceFee
     * @return
     */
    @ResponseBody
    @RequestMapping("/serviceFee/json/update")
    @RequiresPermissions("sys:serviceFee:edit")
    public ResultEntity editJson(ServiceFee serviceFee) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        String result =  checkModel(serviceFee);
        if(result!=null){
            throw new BussinessException(result);
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        serviceFee.setUpdateTime(new Date());
        serviceFee.setUpdator(admin.getName());
        serviceFeeInterface.updateServiceFeee(serviceFee);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }





}
