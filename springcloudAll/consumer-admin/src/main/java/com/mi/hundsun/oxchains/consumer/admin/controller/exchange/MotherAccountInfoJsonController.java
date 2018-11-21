package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.entity.export.ExportUtils;
import com.mi.hundsun.oxchains.base.common.enums.ExchangeEnum;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountModel;
import com.mi.hundsun.oxchains.base.core.model.quote.model.AccountBalance;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccountInfo;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.MotherAccountInfoInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.MotherAccountInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.TradeBianInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.TradeBitFinexInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.TradeHuoBiInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.TradeOkexInterface;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 交易所母账号资产信息controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Slf4j
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MotherAccountInfoJsonController extends GenericController<Integer, MotherAccountInfo> {
    @Autowired
    MotherAccountInfoInterface motherAccountInfoInterface;
    @Autowired
    MotherAccountInterface motherAccountInterface;
    @Autowired
    TradeBianInterface tradeBianInterface;
    @Autowired
    TradeBitFinexInterface tradeBitFinexInterface;
    @Autowired
    TradeHuoBiInterface tradeHuoBiInterface;
    @Autowired
    TradeOkexInterface tradeOkexInterface;

    /**
     * 交易所母账号资产信息分页列表
     */
    @ResponseBody
    @RequestMapping("motherAccountInfo/json/list")
    @RequiresPermissions("sys:motherAccountInfo:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return motherAccountInfoInterface.getDtGridList(dtGridPager);
    }


    /**
     * 列表导出
     */
    @ResponseBody
    @RequestMapping(value = "motherAccountInfo/json/export")
    @RequiresPermissions("sys:motherAccountInfo:export")
    public ResultEntity export(String dtGridPager, HttpServletRequest request, HttpServletResponse response) {
        // 执行导出
        try {
            DtGrid dtGrid = motherAccountInfoInterface.getDtGridListExport(dtGridPager);
            ExportUtils.export(request, response, dtGrid);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("导出失败");
        }
    }


    /**
     * 交易所母账号资产同步
     */
    @ResponseBody
    @RequestMapping("motherAccountInfo/json/synchron")
//    @RequiresPermissions("sys:motherAccountInfo:list")
    public ResultEntity synchronJson() throws Exception {
        //查询所有交易母账号
        List<MotherAccount> motherAccounts = motherAccountInterface.select(new MotherAccount(m -> {
            m.setDelFlag(GenericPo.DELFLAG.NO.code);
            m.setState(MotherAccount.STATE.ENABLE.code);
        }));
        StringBuilder msg = new StringBuilder();
        if (null != motherAccounts && motherAccounts.size() > 0) {
            for (MotherAccount motherAccount : motherAccounts) {
                String json;
                if (motherAccount.getExNo().equals(ExchangeEnum.BIAN.getCode())) {
                    json = tradeBianInterface.account(motherAccount.getApiKey(), motherAccount.getApiSecret());
                } else if (motherAccount.getExNo().equals(ExchangeEnum.HUOBI.getCode())) {
                    json = tradeHuoBiInterface.account(motherAccount.getApiKey(), motherAccount.getApiSecret());
                } else if (motherAccount.getExNo().equals(ExchangeEnum.BITFINEX.getCode())) {
                    json = tradeBitFinexInterface.account(motherAccount.getApiKey(), motherAccount.getApiSecret());
                } else if (motherAccount.getExNo().equals(ExchangeEnum.OKEX.getCode())) {
                    json = tradeOkexInterface.account(motherAccount.getApiKey(), motherAccount.getApiSecret());
                } else {
                    return fail("同步错误, 交易所不存在");
                }
                log.info(motherAccount.getAccountName() + "交易所返回数据: {}", json);
                if (StrUtil.isNotBlank(json)) {
                    JSONObject jsonObject = JSON.parseObject(json);
                    if (jsonObject.getString("code").equals("200")) {
                        //持仓信息
                        List<AccountBalance> accountBalances = JSON.parseArray(jsonObject.getString("data"), AccountBalance.class);
                        MotherAccountModel motherAccountModel = new MotherAccountModel();
                        BeanUtils.copyProperties(motherAccount, motherAccountModel);
                        motherAccountModel.setABalances(accountBalances);
                        ResultEntity resultEntity = motherAccountInfoInterface.synchronMotherAccount(motherAccountModel);
                        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
                            msg.append("母账号[").append(motherAccount.getAccountName()).append("],").append(jsonObject.getString("msg"));
                        }
                    }
                } else {
                    msg.append("母账号[").append(motherAccount.getAccountName()).append("],未返回任何信息");
                }
            }
        }
        if (!msg.toString().equals("")) {
            return fail("同步失败,错误信息:" + msg);
        }
        return ok();
    }


//    for (MotherAccount motherAccount : motherAccounts) {
//        if (motherAccount.getExNo().equals(ExchangeEnum.BIAN.getCode())) {
//            String json = tradeBianInterface.account(motherAccount.getApiKey(), motherAccount.getApiSecret());
//            log.info("Bian交易所返回数据: {}" ,json);
//            if (StrUtil.isNotBlank(json)) {
//                JSONObject jsonObject = JSON.parseObject(json);
//                if (jsonObject.getString("code").equals("200")) {
//                    List<AccountBalance> accountBalances = JSON.parseArray(jsonObject.getString("data"), AccountBalance.class);//持仓信息
//                    MotherAccountModel motherAccountModel = new MotherAccountModel();
//                    BeanUtils.copyProperties(motherAccount, motherAccountModel);
//                    motherAccountModel.setABalances(accountBalances);
//                    ResultEntity resultEntity = motherAccountInfoInterface.synchronMotherAccount(motherAccountModel);
//                    if (resultEntity.getCode() != ResultEntity.SUCCESS) {
//                        msg.append("[").append(motherAccount.getAccountName()).append("]");
//                    }
//                }
//            } else {
//                msg.append("[").append(motherAccount.getAccountName()).append("]");
//            }
//        } else if (motherAccount.getExNo().equals(ExchangeEnum.HUOBI.getCode())) {
//            String json = tradeHuoBiInterface.account(motherAccount.getApiKey(), motherAccount.getApiSecret());
//            log.info("HuoBi交易所返回数据: {}" ,json);
//            if (StrUtil.isNotBlank(json)) {
//                JSONObject jsonObject = JSON.parseObject(json);
//                if (jsonObject.getString("code").equals("200")) {
//                    List<AccountBalance> accountBalances = JSON.parseArray(jsonObject.getString("data"), AccountBalance.class);//持仓信息
//                    MotherAccountModel motherAccountModel = new MotherAccountModel();
//                    BeanUtils.copyProperties(motherAccount, motherAccountModel);
//                    motherAccountModel.setABalances(accountBalances);
//                    ResultEntity resultEntity = motherAccountInfoInterface.synchronMotherAccount(motherAccountModel);
//                    if (resultEntity.getCode() != ResultEntity.SUCCESS) {
//                        msg.append("[").append(motherAccount.getAccountName()).append("]");
//                    }
//                }
//            } else {
//                msg.append("[").append(motherAccount.getAccountName()).append("]");
//            }
//        } else if (motherAccount.getExNo().equals(ExchangeEnum.BITFINEX.getCode())) {
//            String json = tradeBitFinexInterface.account(motherAccount.getApiKey(), motherAccount.getApiSecret());
//            log.info("BitFinex交易所返回数据: {}" ,json);
//            if (StrUtil.isNotBlank(json)) {
//                JSONObject jsonObject = JSON.parseObject(json);
//                if (jsonObject.getString("code").equals("200")) {
//                    List<AccountBalance> accountBalances = JSON.parseArray(jsonObject.getString("data"), AccountBalance.class);//持仓信息
//                    MotherAccountModel motherAccountModel = new MotherAccountModel();
//                    BeanUtils.copyProperties(motherAccount, motherAccountModel);
//                    motherAccountModel.setABalances(accountBalances);
//                    ResultEntity resultEntity = motherAccountInfoInterface.synchronMotherAccount(motherAccountModel);
//                    if (resultEntity.getCode() != ResultEntity.SUCCESS) {
//                        msg.append("[").append(motherAccount.getAccountName()).append("]");
//                    }
//                }
//            } else {
//                msg.append("[").append(motherAccount.getAccountName()).append("]");
//            }
//        }  else if (motherAccount.getExNo().equals(ExchangeEnum.OKEX.getCode())) {
//            String json = tradeOkexInterface.account(motherAccount.getApiKey(), motherAccount.getApiSecret());
//            log.info("BitFinex交易所返回数据: {}" ,json);
//            if (StrUtil.isNotBlank(json)) {
//                JSONObject jsonObject = JSON.parseObject(json);
//                if (jsonObject.getString("code").equals("200")) {
//                    List<AccountBalance> accountBalances = JSON.parseArray(jsonObject.getString("data"), AccountBalance.class);//持仓信息
//                    MotherAccountModel motherAccountModel = new MotherAccountModel();
//                    BeanUtils.copyProperties(motherAccount, motherAccountModel);
//                    motherAccountModel.setABalances(accountBalances);
//                    ResultEntity resultEntity = motherAccountInfoInterface.synchronMotherAccount(motherAccountModel);
//                    if (resultEntity.getCode() != ResultEntity.SUCCESS) {
//                        msg.append("母账号[").append(motherAccount.getAccountName()).append("],").append(jsonObject.getString("msg"));
//                    }
//                }
//            } else {
//                msg.append("[").append(motherAccount.getAccountName()).append("]");
//            }
//        } else {
//            return fail("同步错误, 交易所不存在");
//        }
//    }
}
