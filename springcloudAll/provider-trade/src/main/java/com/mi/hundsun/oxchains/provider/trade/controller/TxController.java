/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.controller;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.enums.ExchangeEnum;
import com.mi.hundsun.oxchains.base.common.utils.LogUtil;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.model.quote.Depth;
import com.mi.hundsun.oxchains.base.core.service.tx.AccountService;
import com.mi.hundsun.oxchains.base.core.service.tx.DealOrderService;
import com.mi.hundsun.oxchains.base.core.service.tx.MainDelegationService;
import com.mi.hundsun.oxchains.base.core.service.tx.SubDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.mi.hundsun.oxchains.provider.trade.rabbitmq.TxSyncMessageProducer;
import com.mi.hundsun.oxchains.provider.trade.service.ExchangeInterface;
import com.mi.hundsun.oxchains.provider.trade.service.QuoteHuoBiInterface;
import com.mi.hundsun.oxchains.provider.trade.service.TxService;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author 枫亭
 * @desc 交易服务控制器
 * @date 2018-04-13 13:44.
 */
@Slf4j
@RestController
@RequestMapping("/prod/tx")
public class TxController extends GenericController {


    @Autowired
    private MainDelegationService mainDelegationService;
    @Autowired
    private DealOrderService dealOrderService;
    @Autowired
    private SubDelegationService subDelegationService;
    @Autowired
    private QuoteHuoBiInterface huoBiInterface;
    @Autowired
    private TxService txService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ExchangeInterface exchangeInterface;
    @Autowired
    private TxSyncMessageProducer txSyncMessageProducer;


    @ApiOperation(value = "下单委托服务，买入卖出-限价和市价")
    @PostMapping(value = "/doTx")
    public ResultEntity doTx(@RequestParam String params) {
        Map<String, Object> map = JSON.parseObject(params, HashMap.class);
        //获取参数
        MainDelegation delegate = JSON.parseObject(map.get("mainDelegation").toString(), MainDelegation.class);
        List<MotherAccountInfoModel> accountInfoModels = JSON.parseArray(map.get("accountInfoModels").toString(), MotherAccountInfoModel.class);
        String exchangeNo = map.get("exchangeNo").toString();
        String symbol = delegate.getCoinCode() + "_" + delegate.getCoinCurrency();
        //判断用户选择的交易所 目前只支持三家交易所 分别火币 币安 bitfinex
        if (exchangeNo.equals(ExchangeEnum.HUOBI.getCode())
                || exchangeNo.equals(ExchangeEnum.BITFINEX.getCode())
                || exchangeNo.equals(ExchangeEnum.BIAN.getCode())) {
            //单个交易所，直接执行下单操作，不需要拆单
            if (null == accountInfoModels || accountInfoModels.size() < 1) {
                throw new BussinessException("没有找到合适的母账号");
            }
            MotherAccountInfoModel account = accountInfoModels.get(0);
            //设置是否拆单标记
            delegate.setIsSplit(MainDelegation.DELFLAG.NO.code);
            //调用接口、生成子委托
            if (delegate.getStyle() == MainDelegation.STYLE.LIMITED.code) {
                txService.doOrderByLimited(delegate, exchangeNo, delegate.getPrice(), delegate.getAmount(), symbol, account, delegate.getStyle());
            } else {
                if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
                    txService.doOrderByMarket(delegate, exchangeNo, delegate.getGmv(), symbol, account, delegate.getStyle());
                } else {
                    txService.doOrderByMarket(delegate, exchangeNo, delegate.getAmount(), symbol, account, delegate.getStyle());
                }

            }
            delegate.setState(MainDelegation.STATE.COMMISSIONED_IN.code);
            delegate.setRemark("子委托全部委托成功");
            delegate.setUpdator(delegate.getUserId() + ":用户");
            delegate.setUpdateTime(new Date());
            mainDelegationService.updateByPrimaryKeySelective(delegate);
        } else if (exchangeNo.equals(ExchangeEnum.ALL.getCode())) {
            //用户选择的是全部交易所，则按照10档行情进行拆单 分别下发交易请求到各个交易所
            //调用接口获取买卖10挡行情数据
            String json = huoBiInterface.depth(symbol);
            Depth depth = JSON.parseObject(JSON.parseObject(json).get("data").toString(), Depth.class);
            //1.判断委托类型
            if (delegate.getStyle() == MainDelegation.STYLE.LIMITED.code) {
                if (delegate.getDirection() == MainDelegation.DIRECTION.SELLOUT.code) {
                    txService.separateOrderByLimitPriceOfSellOut(depth, delegate, accountInfoModels);
                } else {
                    txService.separateOrderByLimitPriceOfBuyIn(depth, delegate, accountInfoModels);
                }
            } else if (delegate.getStyle() == MainDelegation.STYLE.MARKET.code) {
                if (delegate.getDirection() == MainDelegation.DIRECTION.SELLOUT.code) {
                    txService.separateOrderByMarketPriceOfSellOut(depth, delegate, accountInfoModels);
                } else {
                    txService.separateOrderByMarketPriceOfBuyIn(depth, delegate, accountInfoModels);
                }

            } else {
                return fail("买入方式错误");
            }

        } else {
            throw new BussinessException("未找到支持的交易所");
        }
        return ok();
    }

    @ApiOperation(value = "撤单", notes = "对委托中的订单进行撤回操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "delegateNo", value = "主委托编号", dataType = "String")
    })
    @RequestMapping("/revoke")
    public ResultEntity revoke(@RequestParam String delegateNo, @RequestParam String symbol) {
        if (StrUtil.isBlank(delegateNo)) {
            throw new BussinessException("[delegateNo]不能为空");
        }
        if (StrUtil.isBlank(symbol)) {
            throw new BussinessException("[symbol]不能为空");
        }
        //1.查询主委托的子委托明细
        //2.子委托状态在[交易中]和[下单申请]委托中的可以撤回
        //主委托撤单
        List<SubDelegation> subDelegations = new ArrayList<>();
        if (delegateNo.startsWith("M")) {
            SubDelegation delegation = new SubDelegation();
            delegation.setMainDelegateNo(delegateNo);
            delegation.setDelFlag(SubDelegation.DELFLAG.NO.code);
            subDelegations = subDelegationService.select(delegation);
        } else {
            //子委托撤单
            SubDelegation delegation = new SubDelegation();
            delegation.setBillNo(delegateNo);
            delegation.setDelFlag(SubDelegation.DELFLAG.NO.code);
            SubDelegation s = subDelegationService.selectOne(delegation);
            subDelegations.add(s);
        }

        if (null != subDelegations && subDelegations.size() > 0) {
            String msg = txService.revoke(subDelegations, symbol);
            return result(msg, ResultEntity.SUCCESS);
        } else {
            //处理主委托
            MainDelegation mainDelegation = new MainDelegation();
            mainDelegation.setDelegateNo(delegateNo);
            mainDelegation.setDelFlag(MainDelegation.DELFLAG.NO.code);
            this.handleMainDelegateOfFailure(mainDelegationService.selectOne(mainDelegation));
            return fail("未找到对应子委托,撤回主委托");
        }
    }


    @ApiOperation(value = "查询用户当前的主委托订单")
    @PostMapping(value = "/myCurrDelegates")
    public ResultEntity<List<MainDelegation>> myCurrDelegates(@RequestParam Integer userId, @RequestParam Integer direction) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (direction != 0 && direction != 1 && direction != 2) {
            direction = 0;
        }
        List<MainDelegation> mainDelegations = mainDelegationService.getMyCurrMainDelegates(userId, direction);
        return ok(mainDelegations);
    }

    @ApiOperation(value = "用户主委托历史列表")
    @PostMapping(value = "/myDelegateList")
    public ResultEntity myDelegateList(@RequestParam Integer userId, @RequestParam Integer direction,
                                       @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (direction != 0 && direction != 1 && direction != 2) {
            direction = 0;
        }
        Map<String, Object> map = mainDelegationService.getMyMainDelegates(userId, direction, pageNumber, pageSize);
        return ok(map);
    }

    @ApiOperation(value = "用户子委托历史列表")
    @PostMapping(value = "/mySubDelegateList")
    public ResultEntity mySubDelegateList(@RequestParam Integer userId, @RequestParam Integer direction,
                                          @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (direction != 0 && direction != 1 && direction != 2) {
            direction = 0;
        }
        Map<String, Object> map = subDelegationService.getMySubDelegates(userId, direction, pageNumber, pageSize);
        return ok(map);
    }

    @ApiOperation(value = "用户成交列表")
    @PostMapping(value = "/myDealList")
    public ResultEntity myDealList(@RequestParam Integer userId, @RequestParam Integer direction,
                                   @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (direction != 0 && direction != 1 && direction != 2) {
            direction = 0;
        }
        Map<String, Object> map = dealOrderService.getMyDealOrders(userId, direction, pageNumber, pageSize);
        return ok(map);
    }

    @ApiOperation(value = "子委托明细")
    @PostMapping(value = "/mySubDelegateListByMainUuid")
    public ResultEntity mySubDelegateListByMainUuid(@RequestParam String uuid, @RequestParam Integer userId) {
        if (StrUtil.isBlank(uuid)) {
            throw new BussinessException("[uuid]不能为空");
        }
        MainDelegation delegation = new MainDelegation();
        delegation.setUuid(uuid);
        delegation.setUserId(userId);
        delegation.setDelFlag(MainDelegation.DELFLAG.NO.code);
        MainDelegation d = mainDelegationService.selectOne(delegation);
        if (null == d) {
            throw new BussinessException("主委托记录不存在");
        }
        List<SubDelegation> subDelegationOrders = subDelegationService.getMySubsByMainNo(d.getDelegateNo(), userId);
        if (null != subDelegationOrders && subDelegationOrders.size() > 0) {
            return ok(subDelegationOrders);
        }
        return fail("未查询到子委托记录");
    }

    @ApiOperation(value = "用户注册初始账户持仓")
    @PostMapping(value = "/registerInit")
    public ResultEntity registerInit(@RequestParam Integer userId) {
        if (null == userId) {
            return fail("用户id参数有误");
        }
        try {
            List<Account> accounts = new ArrayList<>();
            Account account = new Account();
            account.setUuid(RandomUtils.randomCustomUUID());
            account.setUserId(userId);
            account.setCoinCode(CoinCode.BTC);
            account.setTotal(BigDecimal.ZERO);
            account.setAvailable(BigDecimal.ZERO);
            account.setFreeze(BigDecimal.ZERO);
            account.setCreateTime(new Date());
            account.setDelFlag(GenericPo.DELFLAG.NO.code);
            accounts.add(account);
            account = new Account();
            account.setUuid(RandomUtils.randomCustomUUID());
            account.setUserId(userId);
            account.setCoinCode(CoinCode.ETH);
            account.setTotal(BigDecimal.ZERO);
            account.setAvailable(BigDecimal.ZERO);
            account.setFreeze(BigDecimal.ZERO);
            account.setCreateTime(new Date());
            account.setDelFlag(GenericPo.DELFLAG.NO.code);
            accounts.add(account);
            accountService.insertList(accounts);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }


    @ApiOperation(value = "主委托拆单之前发生异常的后续处理-解冻资产")
    @PostMapping(value = "/handleMainDelegateOfFailure")
    public void handleMainDelegateOfFailure(@RequestBody MainDelegation delegation) {
        if (null == delegation) {
            log.error("主委托不存在,无需解冻资产");
            return;
        }
        txSyncMessageProducer.sendMainDelegateFailureTask(delegation);
//        //判断主委托类型和委托方向 执行不同的退还逻辑
//        if (delegation.getStyle() == MainDelegation.STYLE.LIMITED.code) {
//            if (delegation.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
//                txOrderHandlerService.handleMainDelegateOfFailureByLimitedBuyIn(delegation);
//            } else if (delegation.getDirection() == MainDelegation.DIRECTION.SELLOUT.code) {
//                txOrderHandlerService.handleMainDelegateOfFailureByLimitedSellout(delegation);
//            }
//        } else if (delegation.getStyle() == MainDelegation.STYLE.MARKET.code) {
//            if (delegation.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
//                txOrderHandlerService.handleMainDelegateOfFailureByMarketBuyIn(delegation);
//            } else if (delegation.getDirection() == MainDelegation.DIRECTION.SELLOUT.code) {
//                txOrderHandlerService.handleMainDelegateOfFailureByMarketSellout(delegation);
//            }
//        }

    }


    @ApiOperation(value = "查找委托中的子委托")
    @PostMapping(value = "/findTradingSubDelegateList")
    public ResultEntity findTradingSubDelegateList() {
        List<SubDelegation> tradingSubDelegateList = subDelegationService.getTradingSubDelegates();
        return ok(JSON.toJSONString(tradingSubDelegateList));
    }

    @ApiOperation(value = "查找委托中的主委托")
    @PostMapping(value = "/findTradingMainDelegateList")
    public ResultEntity findTradingMainDelegateList() {
        List<MainDelegation> tradingSubDelegateList = mainDelegationService.findTradingMainDelegateList();
        return ok(JSON.toJSONString(tradingSubDelegateList));
    }

    @ApiOperation(value = "子委托查询")
    @PostMapping(value = "/singleQryOrder")
    public ResultEntity singleQryOrder(@RequestParam String map) {
        Map<String, Object> params = JSON.parseObject(map, HashMap.class);
        MotherAccountInfoModel account = JSON.parseObject(params.get("account").toString(), MotherAccountInfoModel.class);
        String symbol = params.get("symbol").toString();
        String billNo = params.get("billNo").toString();

        txService.singleQryOrder(account, symbol, billNo);
        return ok();
    }

    @ApiOperation(value = "子委托同步")
    @PostMapping(value = "/syncSubDelegation")
    public void syncSubDelegation() {
        List<SubDelegation> tradingSubDelegateList = subDelegationService.getTradingSubDelegates();
        for (SubDelegation sub : tradingSubDelegateList) {
            try {
                ResultEntity motherAccountByEac = exchangeInterface.findMotherAccountByExNoAndAccountName(sub.getExchange(), sub.getMotherAccount());
                if (motherAccountByEac.getCode() == ResultEntity.SUCCESS) {
                    MotherAccountInfoModel account = JSON.parseObject(motherAccountByEac.getData().toString(), MotherAccountInfoModel.class);
                    txService.singleQryOrder(account, sub.getCoinCode() + "_" + sub.getCoinCurrency(), sub.getBillNo());
                } else {
                    log.error("Query Exchange's Account was Failed,The Reason Is:{}", motherAccountByEac.getMessage());
                }
            } catch (Exception e) {
                LogUtil.error(e, TxController.class);
            }

        }
    }

}
