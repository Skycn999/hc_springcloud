/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.controller.tx;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.quote.Depth;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import com.mi.hundsun.oxchains.base.core.po.tpl.TradeFee;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.util.TxUtils;
import com.mi.hundsun.oxchains.consumer.web.config.WebGenericController;
import com.mi.hundsun.oxchains.consumer.web.controller.user.AccountCentralController;
import com.mi.hundsun.oxchains.consumer.web.rabbitmq.TxHandlerProducer;
import com.mi.hundsun.oxchains.consumer.web.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.web.service.tx.QuoteHuoBiInterface;
import com.mi.hundsun.oxchains.consumer.web.service.tx.TxInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.ExchangeInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.UserInterface;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 枫亭
 * @date 2018-04-13 15:31.
 */
@Slf4j
@RestController
@RequestMapping("/api/web/tx")
public class TxController extends WebGenericController {

    @Autowired
    protected UserInterface userInterface;
    @Autowired
    private TxInterface txInterface;
    @Autowired
    private AccountInterface accountInterface;
    @Autowired
    private QuoteHuoBiInterface huoBiInterface;
    @Autowired
    private ExchangeInterface exchangeInterface;

    @Autowired
    private TxHandlerProducer txHandlerProducer;

    @Autowired
    AccountCentralController accountCentralController;

    @ApiOperation(value = "限价买入", notes = "发起限价买入货币操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "price", value = "买入价", required = true, dataType = "String")
            , @ApiImplicitParam(name = "amount", value = "买入量", dataType = "String")
            , @ApiImplicitParam(name = "currencyPair", value = "交易对", dataType = "String")
            , @ApiImplicitParam(name = "exchangeNo", value = "交易所编号", dataType = "String")
    })
    @RequestMapping("/limitedBuyIn")
    public ResultEntity limitedBuyIn(@RequestParam String price, @RequestParam String amount,
                                     @RequestParam String currencyPair, @RequestParam String exchangeNo) {

        if (StrUtil.isBlank(price)) {
            throw new BussinessException("[买入价]不能为空");
        }
        if (StrUtil.isBlank(amount)) {
            throw new BussinessException("[买入量]不能为空");
        }
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[交易对]不能为空");
        }
        if (!currencyPair.contains("_")) {
            throw new BussinessException("[交易对]不合法");
        }

        //如果分母不是主流币种，则exchangeNo参数不能为空
        if (!TxUtils.isMainCoinCode(currencyPair.split("_")[1])) {
            if (exchangeNo.equalsIgnoreCase("all")) {
                throw new BussinessException("非主流币种");
            }
        }
        //校验交易对是否符合要求
        this.checkCurrencyPair(currencyPair, amount, MainDelegation.DIRECTION.BUYIN.code, MainDelegation.STYLE.LIMITED.code);

        Integer userId = getLoginUserId();
        //获取当前用户总资产
        BigDecimal netWorth = accountCentralController.computeNetWorth();
        if (netWorth.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BussinessException("无可用净资产或净资产计算出错");
        }
        //user模块校验 - 1.用户信息 2.用户风控信息
        ResultEntity resultEntity = userInterface.preValidUserInfoToTx(userId, netWorth);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            throw new BussinessException(resultEntity.getMessage());
        }
        ResultEntity tplEntity = userInterface.getTradeFeeTpl(currencyPair);
        TradeFee tradeFee;
        if (tplEntity.getCode() == ResultEntity.SUCCESS) {
            tradeFee = JSON.parseObject(tplEntity.getData().toString(), TradeFee.class);
        } else {
            throw new BussinessException(tplEntity.getMessage());
        }
        //交易模块校验 - 资产可用校验、主委托生成、资金冻结等
        ResultEntity delegationResult = accountInterface.handleAccountByLimitedBuyIn(
                userId,
                new BigDecimal(price),
                new BigDecimal(amount),
                tradeFee.getBuyFee(),
                currencyPair, exchangeNo);

        if (delegationResult.getCode() == ResultEntity.SUCCESS) {
            MainDelegation delegation = JSON.parseObject(delegationResult.getData().toString(), MainDelegation.class);
            //发送到买入交易队列
            txHandlerProducer.sendBuyInTxTask(exchangeNo, currencyPair, delegation);
            return ok(delegation.getDelegateNo(), "限价买入委托成功");
        }
        return fail(delegationResult.getMessage());
    }

    @ApiOperation(value = "限价卖出", notes = "发起限价卖出货币操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "price", value = "卖出价", required = true, dataType = "String")
            , @ApiImplicitParam(name = "amount", value = "卖出量", dataType = "String")
            , @ApiImplicitParam(name = "currencyPair", value = "交易对", dataType = "String")
            , @ApiImplicitParam(name = "exchangeNo", value = "交易所编号", dataType = "String")
    })
    @RequestMapping("/limitedSellOut")
    public ResultEntity limitedSellOut(@RequestParam String price,
                                       @RequestParam String amount,
                                       @RequestParam String currencyPair,
                                       @RequestParam String exchangeNo) {

        if (StrUtil.isBlank(price)) {
            throw new BussinessException("[卖出价]不能为空");
        }
        if (StrUtil.isBlank(amount)) {
            throw new BussinessException("[卖出量]不能为空");
        }
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[交易对]不能为空");
        }
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[交易所编号]不能为空");
        }
        if (!currencyPair.contains("_")) {
            throw new BussinessException("[交易对]不合法");
        }
        //如果分母不是主流币种，则exchangeNo参数不能为空
        if (!TxUtils.isMainCoinCode(currencyPair.split("_")[0])) {
            if (exchangeNo.equalsIgnoreCase("all")) {
                throw new BussinessException("非主流币种");
            }
        }
        //校验交易对是否符合要求
        this.checkCurrencyPair(currencyPair, amount, MainDelegation.DIRECTION.SELLOUT.code, MainDelegation.STYLE.LIMITED.code);
        //1.判断卖出的币种是否是主流币种
        //1.1 主流币种会拆单卖出
        //1.2 非主流币种会按照持仓交易所卖出 不存在拆单。
        Integer userId = getLoginUserId();
        //获取当前用户总资产
        BigDecimal netWorth = accountCentralController.computeNetWorth();
        //user模块校验 - 1.用户信息 2.用户风控信息
        ResultEntity resultEntity = userInterface.preValidUserInfoToTx(userId, netWorth);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            throw new BussinessException(resultEntity.getMessage());
        }
        ResultEntity tplEntity = userInterface.getTradeFeeTpl(currencyPair);
        TradeFee tradeFee;
        if (tplEntity.getCode() == ResultEntity.SUCCESS) {
            tradeFee = JSON.parseObject(tplEntity.getData().toString(), TradeFee.class);
        } else {
            throw new BussinessException(tplEntity.getMessage());
        }
        //交易模块校验 - 资产可用校验并生成卖出主委托订单
        ResultEntity delegationResult = accountInterface.handleAccountByLimitedSellOut(userId,
                new BigDecimal(price),
                new BigDecimal(amount),
                tradeFee.getSellFee(),
                currencyPair,
                exchangeNo);

        if (delegationResult.getCode() == ResultEntity.SUCCESS) {
            MainDelegation delegation = JSON.parseObject(delegationResult.getData().toString(), MainDelegation.class);
            //发送到买入交易队列
            txHandlerProducer.sendSellOutTxTask(exchangeNo, currencyPair, delegation);
            return ok(delegation.getDelegateNo(), "限价卖出委托成功");
        }
        return fail(delegationResult.getMessage());
    }

    @ApiOperation(value = "市价买入", notes = "发起市价买入操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gmv", value = "交易额", dataType = "String")
            , @ApiImplicitParam(name = "currencyPair", value = "交易对", dataType = "String")
            , @ApiImplicitParam(name = "exchangeNo", value = "交易所编号", dataType = "String")
    })
    @RequestMapping("/marketBuyIn")
    public ResultEntity marketBuyIn(@RequestParam String gmv, @RequestParam String currencyPair,
                                    @RequestParam String exchangeNo) {
        if (StrUtil.isBlank(gmv)) {
            throw new BussinessException("[交易额]不能为空");
        }
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[交易对]不能为空");
        }
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[交易所信息]不能为空");
        }
        if (!currencyPair.contains("_")) {
            throw new BussinessException("[交易对]不合法");
        }
        //校验交易对是否符合要求
        this.checkCurrencyPair(currencyPair, gmv, MainDelegation.DIRECTION.BUYIN.code, MainDelegation.STYLE.MARKET.code);
        Integer userId = getLoginUserId();
        //获取当前用户总资产
        BigDecimal netWorth = accountCentralController.computeNetWorth();
        //user模块校验 - 1.用户信息 2.用户风控信息
        ResultEntity resultEntity = userInterface.preValidUserInfoToTx(userId, netWorth);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            throw new BussinessException(resultEntity.getMessage());
        }
        ResultEntity tplEntity = userInterface.getTradeFeeTpl(currencyPair);
        TradeFee tradeFee;
        if (tplEntity.getCode() == ResultEntity.SUCCESS) {
            tradeFee = JSON.parseObject(tplEntity.getData().toString(), TradeFee.class);
        } else {
            throw new BussinessException(tplEntity.getMessage());
        }
        //交易模块校验 - 资产可用校验
        ResultEntity delegationResult = accountInterface.handleAccountByMarketBuyIn(userId, new BigDecimal(gmv),
                tradeFee.getBuyFee(), currencyPair, exchangeNo);
        if (delegationResult.getCode() == ResultEntity.SUCCESS) {
            MainDelegation delegation = JSON.parseObject(delegationResult.getData().toString(), MainDelegation.class);
            //发送到买入交易队列
            txHandlerProducer.sendBuyInTxTask(exchangeNo, currencyPair, delegation);
            return ok(delegation.getDelegateNo(), "市价买入委托成功");
        }
        return fail(delegationResult.getMessage());
    }

    @ApiOperation(value = "市价卖出", notes = "发起市价卖出请求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gmv", value = "卖出量", dataType = "String")
            , @ApiImplicitParam(name = "currencyPair", value = "交易对", dataType = "String")
            , @ApiImplicitParam(name = "exchangeNo", value = "交易所编号", dataType = "String")
    })
    @RequestMapping("/marketSellOut")
    public ResultEntity marketSellOut(@RequestParam String gmv, @RequestParam String currencyPair,
                                      @RequestParam String exchangeNo) {
        //卖出量 在卖出操作中对应买入操作中的交易额
        if (StrUtil.isBlank(gmv)) {
            throw new BussinessException("[卖出量]不能为空");
        }
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[交易对]不能为空");
        }
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[交易所信息]不能为空");
        }

        if (!currencyPair.contains("_")) {
            throw new BussinessException("[交易对]不合法");
        }
        //校验交易对是否符合要求
        this.checkCurrencyPair(currencyPair, gmv, MainDelegation.DIRECTION.SELLOUT.code, MainDelegation.STYLE.MARKET.code);
        //1.判断卖出的币种是否是主流币种
        //1.1 主流币种会拆单卖出
        //1.2 非主流币种会按照持仓交易所卖出 不存在拆单。
        Integer userId = getLoginUserId();
        //获取当前用户总资产
        BigDecimal netWorth = accountCentralController.computeNetWorth();
        //user模块校验 - 1.用户信息 2.用户风控信息
        ResultEntity resultEntity = userInterface.preValidUserInfoToTx(userId, netWorth);
        if (resultEntity.getCode() != ResultEntity.SUCCESS) {
            throw new BussinessException(resultEntity.getMessage());
        }
        ResultEntity tplEntity = userInterface.getTradeFeeTpl(currencyPair);
        TradeFee tradeFee;
        if (tplEntity.getCode() == ResultEntity.SUCCESS) {
            tradeFee = JSON.parseObject(tplEntity.getData().toString(), TradeFee.class);
        } else {
            throw new BussinessException(tplEntity.getMessage());
        }
        //交易模块校验 - 资产可用校验并生成卖出主委托订单
        ResultEntity delegationResult = accountInterface.handleAccountByMarketSellOut(userId,
                new BigDecimal(gmv),
                tradeFee.getSellFee(),
                currencyPair,
                exchangeNo,
                "");
        if (delegationResult.getCode() == ResultEntity.SUCCESS) {
            MainDelegation delegation = JSON.parseObject(delegationResult.getData().toString(), MainDelegation.class);
            //发送到买入交易队列
            txHandlerProducer.sendSellOutTxTask(exchangeNo, currencyPair, delegation);
            return ok(delegation.getDelegateNo(), "市价卖出委托成功");
        }
        return fail(delegationResult.getMessage());
    }

    @ApiOperation(value = "查询交易币种可用资产", notes = "查询交易币种可用资产")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对", dataType = "String")
    })
    @RequestMapping("/findAvailAccount")
    public ResultEntity findAvailAccount(@RequestParam String symbol) {
        if (StrUtil.isBlank(symbol)) {
            throw new BussinessException("[交易对]不能为空");
        }
        if (!symbol.contains("_")) {
            throw new BussinessException("[交易对]不合法");
        }

        Integer userId = getLoginUserId();
        ResultEntity accountResult = accountInterface.findAvailAccount(userId, symbol);
        if (accountResult.getCode() == ResultEntity.SUCCESS) {
            Map<String, Object> map = JSON.parseObject(accountResult.getData().toString(), Map.class);
            //查找10档行情
            String json = huoBiInterface.depth(symbol);
            Depth depth = JSON.parseObject(JSON.parseObject(json).get("data").toString(), Depth.class);
            map.put("ask_price", TxUtils.removeRedundanceZeroString(depth.getAsk_price1()));
            map.put("bid_price", TxUtils.removeRedundanceZeroString(depth.getBid_price1()));
            return ok(map);
        }

        return fail("未查询到可用资产");
    }


    @ApiOperation(value = "撤单", notes = "对委托中的订单进行撤回操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "delegateNo", value = "主委托编号", dataType = "String"),
            @ApiImplicitParam(name = "symbol", value = "交易对", dataType = "String")
    })
    @RequestMapping("/revoke")
    public ResultEntity revoke(@RequestParam String delegateNo, @RequestParam String symbol) {
        if (StrUtil.isBlank(delegateNo)) {
            throw new BussinessException("[委托编号]不能为空");
        }
        if (StrUtil.isBlank(symbol)) {
            throw new BussinessException("[交易对]不能为空");
        }
        ResultEntity revoke = txInterface.revoke(delegateNo, symbol);
        if (revoke.getCode() == ResultEntity.SUCCESS) {
            return result(revoke.getMessage(), revoke.getCode());
        }
        return fail(revoke.getMessage());
    }

    @ApiOperation(value = "当前委托", notes = "我的当前委托")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "direction", value = "委托方向 1买入 2卖出，0全部", required = true, dataType = "Integer")
    })
    @RequestMapping("/myCurrDelegates")
    public ResultEntity myCurrDelegates(@RequestParam Integer direction) {
        Integer loginUserId = getLoginUserId();
        ResultEntity resultEntity = txInterface.myCurrDelegates(loginUserId, direction);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return fail(resultEntity.getMessage());
    }

    @ApiOperation(value = "主委托列表", notes = "我的主委托列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "direction", value = "委托方向 1买入 2卖出，0全部", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
    })
    @RequestMapping("/myDelegateList")
    public ResultEntity myDelegateList(@RequestParam Integer direction, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        Integer loginUserId = getLoginUserId();
        ResultEntity resultEntity = txInterface.myDelegateList(loginUserId, direction, pageNumber, pageSize);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return fail(resultEntity.getMessage());
    }

    @ApiOperation(value = "成交列表", notes = "我的成交列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "direction", value = "委托方向 1买入 2卖出,0全部", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @RequestMapping("/myDealList")
    public ResultEntity myDealList(@RequestParam Integer direction, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        Integer loginUserId = getLoginUserId();
        ResultEntity resultEntity = txInterface.myDealList(loginUserId, direction, pageNumber, pageSize);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return fail(resultEntity.getMessage());
    }

    @ApiOperation(value = "子委托列表", notes = "我的子委托列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "direction", value = "委托方向 1买入 2卖出，0全部", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer")

    })
    @RequestMapping("/mySubDelegateList")
    public ResultEntity mySubDelegateList(@RequestParam Integer direction, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        Integer loginUserId = getLoginUserId();
        ResultEntity resultEntity = txInterface.mySubDelegateList(loginUserId, direction, pageNumber, pageSize);
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return fail(resultEntity.getMessage());
    }


    @ApiOperation(value = "主委托订单明细", notes = "根据主委托订单uuid查询子委托列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "mainUuid", value = "主委托uuid", required = true, dataType = "String")
    })
    @RequestMapping("/mySubDelegateListByMainUuid")
    public ResultEntity mySubDelegateListByMainUuid(@RequestParam String mainUuid) {
        ResultEntity resultEntity = txInterface.mySubDelegateListByMainUuid(mainUuid, getLoginUserId());
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            return ok(resultEntity.getData());
        }
        return fail(resultEntity.getMessage());
    }


    @ApiOperation(value = "快捷键功能,撤销全部委托时间最近的一个订单", notes = "快捷键功能,撤销全部委托时间最近的一个订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/shortcutFunctionOfRevokeLastOne")
    public ResultEntity shortcutFunctionOfRevokeLastOne() {

        return ok();
    }

    @ApiOperation(value = "快捷键功能,撤销全部委托中的订单", notes = "快捷键功能,撤销全部委托中的订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
    })
    @RequestMapping("/shortcutFunctionOfRevokeAll")
    public ResultEntity shortcutFunctionOfRevokeAll() {

        return ok();
    }

    /**
     * 校验下单code配置
     */
    private void checkCurrencyPair(String currencyPair, String amount, Integer direction, Integer style) {
        ResultEntity codePairConfigResult = exchangeInterface.getCodePairConfig(currencyPair);
        if (codePairConfigResult.getCode() == ResultEntity.SUCCESS) {
            BigDecimal bigDecimal = new BigDecimal(amount);
            CodePairConfig config = JSON.parseObject(JSON.toJSONString(codePairConfigResult.getData()), CodePairConfig.class);
            if (style == MainDelegation.STYLE.LIMITED.code) {
                //限价买入卖出

                if (bigDecimal.compareTo(new BigDecimal(config.getLimitMinAmount())) < 0
                        || bigDecimal.compareTo(new BigDecimal(config.getLimitMaxAmount())) > 0) {
                    throw new BussinessException("[" + currencyPair + "]限价下单量需在[" + config.getLimitMinAmount()
                            + "," + config.getLimitMaxAmount() + "]之间");
                }

            } else if (style == MainDelegation.STYLE.MARKET.code) {
                if (direction == MainDelegation.DIRECTION.BUYIN.code) {
                    if (bigDecimal.compareTo(new BigDecimal(config.getMarketMinBuyAmount())) < 0
                            || bigDecimal.compareTo(new BigDecimal(config.getMarketMaxBuyAmount())) > 0) {
                        throw new BussinessException("[" + currencyPair + "]市价买入量需在[" + config.getMarketMinBuyAmount()
                                + "," + config.getMarketMaxBuyAmount() + "]之间");
                    }
                } else if (direction == MainDelegation.DIRECTION.SELLOUT.code) {
                    if (bigDecimal.compareTo(new BigDecimal(config.getMarketMinSellAmount())) < 0
                            || bigDecimal.compareTo(new BigDecimal(config.getMarketMaxSellAmount())) > 0) {
                        throw new BussinessException("[" + currencyPair + "]市价卖出量需在[" + config.getMarketMinSellAmount()
                                + "," + config.getMarketMaxSellAmount() + "]之间");
                    }
                }
            }
        } else {
            throw new BussinessException("未查询到币种配置");
        }

    }

}
