package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.CodePairConfigInteface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class CodePairConfigJsonController extends GenericController<Integer, CodePairConfig> {

    @Autowired
    CodePairConfigInteface codePairConfigInteface;

    @ResponseBody
    @RequestMapping("codePairConfig/json/list")
    @RequiresPermissions("sys:codePairConfig:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return codePairConfigInteface.getDtGridList(dtGridPager);
    }

    /**
     * 新增保存
     *
     * @param codePairConfig
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("codePairConfig/json/add")
    @RequiresPermissions("sys:codePairConfig:add")
    public ResultEntity addJson(CodePairConfig codePairConfig) throws Exception {
       String  result = checkCodePairConfig(codePairConfig,"add");
       if (null != result){
           return fail(result);
       }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        codePairConfig.setCreator(admin.getName());
        codePairConfig.setCreateTime(new Date());
        codePairConfig.setUuid(RandomUtils.randomCustomUUID().toString());
        return  codePairConfigInteface.insert(codePairConfig);
    }

    /**
     * 编辑保存
     *
     * @param codePairConfig
     * @return
     */
    @ResponseBody
    @RequestMapping("codePairConfig/json/edit")
    @RequiresPermissions("sys:codePairConfig:edit")
    public ResultEntity editJson(CodePairConfig codePairConfig) throws Exception {
        checkCodePairConfig(codePairConfig, "edit");
        Admin admin = AdminSessionHelper.getCurrAdmin();
        codePairConfig.setUpdator(admin.getName());
        codePairConfig.setUpdateTime(new Date());
        return codePairConfigInteface.updateCodePacirConfig(codePairConfig);
    }

    private String checkCodePairConfig(CodePairConfig codePairConfig, String action) {
        String reslult = null;
        if (null == codePairConfig) {
            reslult = "参数有误";
        }
        if ("edit".equals(action)) {
            if (null == codePairConfig.getId()) {
                reslult = "参数有误";
            }
        }
        if (StringUtils.isBlank(codePairConfig.getCode())) {
            reslult = "请选择资产代码";
        }else if(StringUtils.isBlank(codePairConfig.getExNumbers())){
            reslult = "请选择交易所";
        }else if(StringUtils.isBlank(codePairConfig.getBaseCode())){
            reslult = "请选择分区";
        }else if(StringUtils.isBlank(codePairConfig.getState())){
            reslult = "请选择状态";
        }else if(codePairConfig.getState().intValue() != CodePairConfig.STATE.DISABLE.code
                && codePairConfig.getState().intValue() != CodePairConfig.STATE.ENABLE.code){
            reslult = "状态不正确";
        }else if(StringUtils.isBlank(codePairConfig.getType())){
            reslult = "请选择类型";
        }else if(codePairConfig.getType().intValue() != CodePairConfig.TYPE.MAIN.code
                && codePairConfig.getType().intValue() != CodePairConfig.TYPE.NON_MAIN.code){
            reslult = "类型不正确";
        }else if(StringUtils.isBlank(codePairConfig.getIsDisplayOnApp())){
            reslult = "请选择是否在APP显示";
        }else if(codePairConfig.getIsDisplayOnApp().intValue() != CodePairConfig.ISDISPLAYONAPP.NO.code
                && codePairConfig.getIsDisplayOnApp().intValue() != CodePairConfig.ISDISPLAYONAPP.YES.code){
            reslult = "是否在APP显示不正确";
        }else if(StringUtils.isBlank(codePairConfig.getLimitMinAmount())){
            reslult = "限价最小下单数量不能为空";
        }else if(StringUtils.isBlank(codePairConfig.getLimitMaxAmount())){
            reslult = "限价最大下单数量不能为空";
        }else if(new BigDecimal(codePairConfig.getLimitMaxAmount()).compareTo(new BigDecimal(codePairConfig.getLimitMinAmount())) < 0){
            reslult = "限价最小下单数量不能大于限价最大下单数量";
        }else if(StringUtils.isBlank(codePairConfig.getMarketMinBuyAmount())){
            reslult = "市价最小买入量不能为空";
        }else if(StringUtils.isBlank(codePairConfig.getMarketMaxBuyAmount())){
            reslult = "市价最大买入量不能为空";
        }else if(new BigDecimal(codePairConfig.getMarketMaxBuyAmount()).compareTo(new BigDecimal(codePairConfig.getMarketMinBuyAmount())) < 0){
            reslult = "市价最小买入量不能大于市价最大买入量";
        }else if(StringUtils.isBlank(codePairConfig.getMarketMinSellAmount())){
            reslult = "市价最小卖出量不能为空";
        }else if(StringUtils.isBlank(codePairConfig.getMarketMaxSellAmount())){
            reslult = "市价最大卖出量不能为空";
        }else if(new BigDecimal(codePairConfig.getMarketMaxSellAmount()).compareTo(new BigDecimal(codePairConfig.getMarketMinSellAmount())) < 0){
            reslult = "市价最小卖出量不能大于市价最大卖出量";
        }else if(StringUtils.isBlank(codePairConfig.getMinQuotePoint())){
            reslult = "最小行情点位不能为空";
        }
        return reslult;
    }

}
