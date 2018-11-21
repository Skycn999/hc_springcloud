/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.service.impl;

import com.mi.hundsun.oxchains.base.core.constant.AccountLogType;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.quote.model.OrderQryRes;
import com.mi.hundsun.oxchains.base.core.service.tx.*;
import com.mi.hundsun.oxchains.base.core.tx.po.*;
import com.mi.hundsun.oxchains.base.core.util.TxUtils;
import com.mi.hundsun.oxchains.provider.trade.service.TxOrderHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 枫亭
 * @description 交易相关
 * @date 2018-05-04 11:42.
 */
@Slf4j
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class TxOrderHandlerServiceImpl implements TxOrderHandlerService {

    @Resource
    private DealOrderService dealOrderService;
    @Resource
    private SubDelegationService subDelegationService;
    @Resource
    private MainDelegationService mainDelegationService;
    @Resource
    private AccountService accountService;
    @Resource
    private AccountLogService accountLogService;

    @Override
    public void handleSucceedOrderOfMarketSellOut(SubDelegation sub, OrderQryRes result, String entrustNo) {
        Integer userId = sub.getUserId();
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        // 处理委托成交的子订单 - 变更资产持仓记录、添加资产变更记录、更新子委托信息、更新主委托信息(成交数量累计等操作)
        List<DealOrder> orders = dealOrderService.generateForMarket(sub, result, entrustNo);
        //计算总成交量
        BigDecimal totalDealAmount = BigDecimal.ZERO;
        BigDecimal totalDealGmv = BigDecimal.ZERO;
        BigDecimal totalServiceFee = BigDecimal.ZERO;
        for (DealOrder order : orders) {
            totalDealAmount = totalDealAmount.add(order.getAmount());
            totalDealGmv = totalDealGmv.add(order.getGmv());
            totalServiceFee = totalServiceFee.add(order.getPlatFee());
        }
        //totalDealAmount 本次返回的总成交量等于0 则不继续处理
        if (totalDealAmount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        //更新子委托
        sub.setGmv(sub.getGmv().add(totalDealGmv));
        sub.setDealAmount(sub.getDealAmount().add(totalDealAmount));
        sub.setDealPrice(orders.get(0).getPrice());
        sub.setUpdateTime(new Date());

        //部撤
        if ("5".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.PART_OF_REVOKE.code);
            sub.setInfo("子委托部分成交");
            //部成
        } else if ("7".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.PART_OF_DEAL.code);
            sub.setInfo("子委托部分成交");
            //已成交
        } else if ("8".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.DEAL.code);
            sub.setInfo("子委托全部成交");
        }
        subDelegationService.updateByPrimaryKeySelective(sub);

        //扣除冻结
        Account a = new Account();
        a.setCoinCode(ziCode);
        if (!TxUtils.isMainCoinCode(ziCode)) {
            a.setMotherAccount(sub.getMotherAccount());
            a.setExchangeNo(sub.getExchange());
        }
        a.setUserId(userId);
        a.setDelFlag(Account.DELFLAG.NO.code);
        Account account = accountService.selectOne(a);
        if (null == account) {
            throw new BussinessException("市价卖出,未查询到资产账户");
        }
        //更新主委托
        int i;
        if (!TxUtils.isMainCoinCode(ziCode)) {
            i = accountService.deductFreezen(totalDealAmount, ziCode, userId, sub.getExchange());
        } else {
            i = accountService.deductFreezen(totalDealAmount, ziCode, userId);
        }
        if (i < 1) {
            throw new BussinessException("市价卖出-扣除冻结资产失败,资产代码:[" + muCode + "]");
        }
        //生成扣除记录

        accountLogService.createLog(userId, ziCode, totalDealAmount, account.getAvailable(),
                account.getFreeze().subtract(totalDealAmount), AccountLog.APPROACH.DEDUCT.code, "市价卖出订单成交，扣除冻结数量", AccountLogType.UNCHANGE);

        //增加持仓 首先判断用户是否存在该持仓，如果存在则更新 如果不存在则增加
        BigDecimal remainDealGmv = totalDealGmv.subtract(totalServiceFee);
        Account a2 = new Account();
        a2.setUserId(userId);
        a2.setCoinCode(muCode);
        if (!TxUtils.isMainCoinCode(muCode)) {
            a2.setMotherAccount(sub.getMotherAccount());
            a2.setExchangeNo(sub.getExchange());
        }
        Account account2 = accountService.selectOne(a2);
        if (null == account2) {
            a2.setAvailable(remainDealGmv);
            a2.setTotal(remainDealGmv);
            a2.setExchangeName(Account.getExName(a2.getExchangeNo()));
            accountService.insert(a2);
            //生成增加持仓记录
            accountLogService.createLog(userId, muCode, remainDealGmv, BigDecimal.ZERO,
                    BigDecimal.ZERO, AccountLog.APPROACH.TX_GET.code, "委托订单成交,增加资产持仓", AccountLogType.ADD);
        } else {
            accountService.addPosition(remainDealGmv, muCode, userId, sub.getExchange());
            //生成增加持仓记录
            accountLogService.createLog(userId, muCode, remainDealGmv, account2.getAvailable(),
                    a2.getFreeze(), AccountLog.APPROACH.TX_GET.code, "委托订单成交,增加资产持仓", AccountLogType.ADD);
        }
    }

    @Override
    public void handleSucceedOrderOfMarketBuyIn(SubDelegation sub, OrderQryRes result, String entrustNo) {
        Integer userId = sub.getUserId();
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        // 处理委托成交的子订单 - 变更资产持仓记录、添加资产变更记录、更新子委托信息、更新主委托信息(成交数量累计等操作)
        List<DealOrder> orders = dealOrderService.generateForMarket(sub, result, entrustNo);
        //计算总成交量
        BigDecimal totalDealAmount = BigDecimal.ZERO;
        BigDecimal totalDealGmv = BigDecimal.ZERO;
        BigDecimal totalServiceFee = BigDecimal.ZERO;
        for (DealOrder order : orders) {
            totalDealAmount = totalDealAmount.add(order.getAmount());
            totalDealGmv = totalDealGmv.add(order.getGmv());
            totalServiceFee = totalServiceFee.add(order.getPlatFee());
        }
        //totalDealAmount 本次返回的总成交量等于0 则不继续处理
        if (totalDealAmount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        //更新子委托
        sub.setDealAmount(sub.getDealAmount().add(totalDealAmount));
        sub.setDealPrice(orders.get(0).getPrice());
        sub.setUpdateTime(new Date());
        //部撤
        if ("5".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.PART_OF_REVOKE.code);
            sub.setInfo("子委托部分成交");
            //部成
        } else if ("7".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.PART_OF_DEAL.code);
            sub.setInfo("子委托部分成交");
            //已成交
        } else if ("8".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.DEAL.code);
            sub.setInfo("子委托全部成交");
        }
        subDelegationService.updateByPrimaryKeySelective(sub);

        Account a = new Account();
        a.setCoinCode(muCode);
        a.setUserId(userId);
        a.setDelFlag(Account.DELFLAG.NO.code);
        Account account = accountService.selectOne(a);
        if (null == account) {
            throw new BussinessException("市价买入,未查询到资产账户");
        }
        int i;
        if (!TxUtils.isMainCoinCode(muCode)) {
            i = accountService.deductFreezen(totalDealGmv, muCode, userId, sub.getExchange());
        } else {
            i = accountService.deductFreezen(totalDealGmv, muCode, userId);
        }
        if (i < 1) {
            throw new BussinessException("市价买入-扣除冻结资产失败,资产代码:[" + muCode + "]");
        }
        //生成扣除记录
        accountLogService.createLog(userId, muCode, totalDealGmv, account.getAvailable(),
                account.getFreeze().subtract(totalDealGmv), AccountLog.APPROACH.DEDUCT.code, "市价买入订单成交，扣除冻结资产", AccountLogType.UNCHANGE);

        //增加持仓 首先判断用户是否存在该持仓，如果存在则更新 如果不存在则增加
        //本次增加的持仓应该减去手续费
        BigDecimal subtract = totalDealAmount.subtract(totalServiceFee);
        Account a2 = new Account();
        a2.setUserId(userId);
        a2.setCoinCode(ziCode);
        if (!TxUtils.isMainCoinCode(ziCode)) {
            a2.setExchangeNo(sub.getExchange());
            a2.setMotherAccount(sub.getMotherAccount());
        }
        Account account2 = accountService.selectOne(a2);
        if (null == account2) {
            a2.setAvailable(subtract);
            a2.setTotal(subtract);
            a2.setExchangeName(Account.getExName(a2.getExchangeNo()));
            accountService.insert(a2);
            //生成增加持仓记录
            accountLogService.createLog(userId, ziCode, subtract, BigDecimal.ZERO,
                    BigDecimal.ZERO, AccountLog.APPROACH.TX_GET.code, "市价买入订单成交,新增资产持仓", AccountLogType.ADD);
        } else {
            accountService.addPosition(subtract, ziCode, userId, sub.getExchange());
            //生成增加持仓记录
            accountLogService.createLog(userId, ziCode, subtract, account2.getAvailable(),
                    a2.getFreeze(), AccountLog.APPROACH.TX_GET.code, "市价买入订单成交,增加资产持仓", AccountLogType.ADD);
        }
    }

    @Override
    public void handleSucceedOrderOfLimitSellOut(SubDelegation sub, OrderQryRes result, String entrustNo) {
        //小币种限价卖出全部成交: 1.扣除冻结小币种数量 2.增加大币种持仓。
        Integer userId = sub.getUserId();
//        String mainDelegateNo = sub.getMainDelegateNo();
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        // 处理委托成交的子订单 - 变更资产持仓记录、添加资产变更记录、更新子委托信息、更新主委托信息(成交数量累计等操作)
        List<DealOrder> orders = dealOrderService.generateForLimited(sub, result, entrustNo);
        //计算总成交量
        BigDecimal totalDealAmount = BigDecimal.ZERO;
        BigDecimal totalDealGmv = BigDecimal.ZERO;
        BigDecimal totalServiceFee = BigDecimal.ZERO;
        for (DealOrder order : orders) {
            totalDealAmount = totalDealAmount.add(order.getAmount());
            totalDealGmv = totalDealGmv.add(TxUtils.removeRedundanceZeroString(order.getAmount().multiply(order.getPrice())));
            totalServiceFee = totalServiceFee.add(order.getPlatFee());
        }
        //totalDealAmount 本次返回的总成交量等于0 则不继续处理
        if (totalDealAmount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        //更新子委托
        sub.setDealAmount(sub.getDealAmount().add(totalDealAmount));
        sub.setDealPrice(orders.get(0).getPrice());
        sub.setUpdateTime(new Date());
        if ("5".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.PART_OF_REVOKE.code);
            //部成
        } else if ("7".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.PART_OF_DEAL.code);
            //已成交
        } else if ("8".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.DEAL.code);
        }
        subDelegationService.updateByPrimaryKeySelective(sub);
        //扣除冻结 卖出量
        Account a = new Account();
        a.setCoinCode(ziCode);
        a.setUserId(userId);
        if (!TxUtils.isMainCoinCode(ziCode)) {
            a.setMotherAccount(sub.getMotherAccount());
        }
        a.setDelFlag(Account.DELFLAG.NO.code);
        Account account = accountService.selectOne(a);
        if (null == account) {
            throw new BussinessException("限价卖出,未查询到资产账户");
        }
        int i;
        if (!TxUtils.isMainCoinCode(ziCode)) {
            i = accountService.deductFreezen(totalDealAmount, ziCode, userId, sub.getExchange());
        } else {
            i = accountService.deductFreezen(totalDealAmount, ziCode, userId);
        }
        if (i < 1) {
            throw new BussinessException("扣除冻结资产失败,资产代码:[" + ziCode + "]");
        }
        //生成扣除记录
        accountLogService.createLog(userId, ziCode, totalDealAmount, account.getAvailable(),
                account.getFreeze().subtract(totalDealAmount), AccountLog.APPROACH.DEDUCT.code, "限价卖出订单成交，扣除冻结数量", AccountLogType.UNCHANGE);

        //增加持仓 首先判断用户是否存在该持仓，如果存在则更新 如果不存在则增加
        //本次增加的持仓应该减去手续费
        BigDecimal remainAmount = totalDealGmv.subtract(totalServiceFee);
        Account a2 = new Account();
        a2.setUserId(userId);
        a2.setCoinCode(muCode);
        if (!TxUtils.isMainCoinCode(muCode)) {
            a2.setExchangeNo(sub.getExchange());
            a2.setMotherAccount(sub.getMotherAccount());
        }
        Account account2 = accountService.selectOne(a2);
        if (null == account2) {
            a2.setAvailable(remainAmount);
            a2.setTotal(remainAmount);
            a2.setExchangeName(Account.getExName(a2.getExchangeNo()));
            accountService.insert(a2);
            //生成增加持仓记录
            accountLogService.createLog(userId, muCode, remainAmount, BigDecimal.ZERO,
                    BigDecimal.ZERO, AccountLog.APPROACH.TX_GET.code, "限价卖出订单成交,新增资产持仓", AccountLogType.ADD);
        } else {
            accountService.addPosition(remainAmount, muCode, userId, sub.getExchange());
            //生成增加持仓记录
            accountLogService.createLog(userId, muCode, remainAmount, account2.getAvailable(),
                    account2.getFreeze(), AccountLog.APPROACH.TX_GET.code, "限价卖出订单成交,增加资产持仓", AccountLogType.ADD);
        }
    }

    @Override
    public void handleSucceedOrderOfLimitBuyIn(SubDelegation sub, OrderQryRes result, String entrustNo) {
        // 处理委托成交的子订单 - 变更资产持仓记录、添加资产变更记录、更新子委托信息、更新主委托信息(成交数量累计等操作)
        Integer userId = sub.getUserId();
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //生成成交订单 - result 中是全量返回成交的明细
        List<DealOrder> orders;
        orders = dealOrderService.generateForLimited(sub, result, entrustNo);
        //计算本次返回的总成交量
        BigDecimal totalDealAmount = BigDecimal.ZERO;
        BigDecimal totalDealGmv = BigDecimal.ZERO;
        BigDecimal totalServiceFee = BigDecimal.ZERO;
        for (DealOrder order : orders) {
            totalDealAmount = totalDealAmount.add(order.getAmount());
            totalDealGmv = totalDealGmv.add(order.getAmount().multiply(order.getPrice()));
            totalServiceFee = totalServiceFee.add(order.getPlatFee());
        }
        //totalDealAmount 本次返回的总成交量等于0 则不继续处理
        if (totalDealAmount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        //更新子委托
        sub.setDealAmount(sub.getDealAmount().add(totalDealAmount));
        sub.setDealPrice(sub.getPrice());
        sub.setUpdateTime(new Date());
        if ("5".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.PART_OF_REVOKE.code);
            //部成
        } else if ("7".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.PART_OF_DEAL.code);
            //已成交
        } else if ("8".equalsIgnoreCase(result.getEntrust_status())) {
            sub.setState(SubDelegation.STATE.DEAL.code);
        }
        subDelegationService.updateByPrimaryKeySelective(sub);
        Account a = new Account();
        a.setCoinCode(muCode);
        a.setUserId(userId);
        a.setDelFlag(Account.DELFLAG.NO.code);
        if (!TxUtils.isMainCoinCode(muCode)) {
            a.setMotherAccount(sub.getMotherAccount());
            a.setExchangeNo(sub.getExchange());
        }
        Account account = accountService.selectOne(a);
        if (null == account) {
            throw new BussinessException("限价买入,未查询到资产账户");
        }
        //扣除冻结
        int i;
        if (!TxUtils.isMainCoinCode(muCode)) {
            i = accountService.deductFreezen(totalDealGmv, muCode, userId, sub.getExchange());
        } else {
            i = accountService.deductFreezen(totalDealGmv, muCode, userId);
        }
        if (i < 1) {
            throw new BussinessException("限价买入-扣除冻结资产失败,资产代码:[" + muCode + "]");
        }
        //生成扣除记录
        accountLogService.createLog(userId, muCode, totalDealGmv, account.getAvailable(),
                account.getFreeze().subtract(totalDealGmv), AccountLog.APPROACH.DEDUCT.code, "限价买入订单成交，扣除冻结数量", AccountLogType.UNCHANGE);

        //增加持仓 首先判断用户是否存在该持仓，如果存在则更新 如果不存在则增加
        //减去手续费
        BigDecimal remainAmount = totalDealAmount.subtract(totalServiceFee);
        Account a2 = new Account();
        a2.setUserId(userId);
        a2.setCoinCode(ziCode);
        if (!TxUtils.isMainCoinCode(ziCode)) {
            a2.setExchangeNo(sub.getExchange());
            a2.setMotherAccount(sub.getMotherAccount());
        }
        Account account2 = accountService.selectOne(a2);
        if (null == account2) {
            a2.setAvailable(remainAmount);
            a2.setTotal(remainAmount);
            a2.setExchangeName(Account.getExName(a2.getExchangeNo()));
            accountService.insert(a2);
            //生成增加持仓记录
            accountLogService.createLog(userId, ziCode, remainAmount, BigDecimal.ZERO,
                    BigDecimal.ZERO, AccountLog.APPROACH.TX_GET.code, "限价买入订单成交,新增资产持仓", AccountLogType.ADD);
        } else {
            accountService.addPosition(remainAmount, ziCode, userId, sub.getExchange());
            //生成增加持仓记录
            accountLogService.createLog(userId, ziCode, remainAmount, account2.getAvailable(),
                    a2.getFreeze(), AccountLog.APPROACH.TX_GET.code, "限价买入单成交,增加资产持仓", AccountLogType.ADD);
        }


    }

    @Override
    public void handlerFailedOrderOfLimitedBuyIn(SubDelegation sub, String msg) {
        //子委托信息更新
        sub.setState(SubDelegation.STATE.FAILED.code);
        sub.setInfo(msg);
        sub.setUpdateTime(new Date());
        int update = subDelegationService.updateByStateForFailure(sub);
        if (update < 1) {
            log.error("更新子委托失败");
            return;
        }
        //解冻资产
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(sub.getUserId());
        account.setCoinCode(muCode);
        if (!TxUtils.isMainCoinCode(muCode)) {
            account.setMotherAccount(sub.getMotherAccount());
            account.setExchangeNo(sub.getExchange());
        }
        account.setDelFlag(Account.DELFLAG.NO.code);
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("Limited BuyIn Failure Handle,Query Account[" + muCode + "] Is Null");
        }
        //更新资产账户
        BigDecimal optGmv = sub.getGmv();
        int freeze;
        try {
            if (!TxUtils.isMainCoinCode(muCode)) {
                freeze = accountService.freeze(optGmv.negate(), muCode, sub.getUserId(), sub.getExchange());
            } else {
                freeze = accountService.freeze(optGmv.negate(), muCode, sub.getUserId());
            }
        } catch (Exception e) {
            throw new BussinessException("Limited BuyIn Failure Handle, Account Unfreeze occurred exception.");
        }

        if (freeze < 1) {
            throw new BussinessException("Limited BuyIn Failure Handle, Account Unfreeze Failure.");
        }

        //添加资产变更记录
        String desc = "使用[" + muCode + "]买入[" + ziCode + "],买入数量:" + sub.getAmount() + ",消耗:" + optGmv +
                "个" + muCode + ",申请买入失败,解冻用户资产.子委托uuid:" + sub.getUuid();
        int log = accountLogService.createLog(sub.getUserId(), muCode, optGmv, account.getAvailable(),
                account.getFreeze().subtract(optGmv),
                AccountLog.APPROACH.UNFREEZE.code, desc, AccountLogType.ADD);
        if (log < 1) {
            throw new BussinessException("资金记录保存失败");
        }
    }

    @Override
    public void handlerFailedOrderOfLimitedSellOut(SubDelegation sub, String msg) {
        //子委托信息更新
        sub.setState(SubDelegation.STATE.FAILED.code);
        sub.setInfo(msg);
        sub.setUpdateTime(new Date());
        int update = subDelegationService.updateByStateForFailure(sub);
        if (update < 1) {
            log.error("更新子委托失败");
            return;
        }
        //解冻资产
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(sub.getUserId());
        account.setCoinCode(ziCode);
        account.setDelFlag(Account.DELFLAG.NO.code);
        //卖出操作需要区分是否是主流币种
        if (!TxUtils.isMainCoinCode(ziCode)) {
            //非主流币 需要区分交易所和母账号
            account.setMotherAccount(sub.getMotherAccount());
            account.setExchangeNo(sub.getExchange());
        }
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("Limited SellOut Failure Handler,Query Account[" + ziCode + "] Is Null");
        }
        BigDecimal opt = sub.getAmount();
        //更新资产账户
        int freeze;
        try {
            if (!TxUtils.isMainCoinCode(ziCode)) {
                freeze = accountService.freeze(opt.negate(), ziCode, sub.getUserId(), sub.getExchange());
            } else {
                freeze = accountService.freeze(opt.negate(), ziCode, sub.getUserId());
            }
        } catch (Exception e) {
            log.error("资产解冻失败,{}", e.getMessage());
            throw new BussinessException("资产解冻失败");
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录
        String desc = "使用[" + ziCode + "]限价卖出[" + muCode +
                "],卖出数量:" +opt + ",卖出失败-失败原因: " + msg + "解冻用户资产.子委托uuid:" + sub.getUuid();
        int log = accountLogService.createLog(sub.getUserId(), muCode, opt, account.getAvailable(),
                account.getFreeze().subtract(opt),
                AccountLog.APPROACH.UNFREEZE.code, desc, AccountLogType.ADD);
        if (log < 1) {
            throw new BussinessException("资金记录保存失败");
        }
    }

    @Override
    public void handlerFailedOrderOfMarketBuyIn(SubDelegation sub, String msg) {
        //子委托信息更新
        sub.setState(SubDelegation.STATE.FAILED.code);
        sub.setInfo(msg);
        sub.setUpdateTime(new Date());
        int update = subDelegationService.updateByStateForFailure(sub);
        if (update < 1) {
            log.error("更新子委托失败");
            return;
        }
        //处理委托失败-或委托异常的子委托 - 更新子委托信息、解冻资产、添加资产变更记录
        //1.如果没有拆单 则同步更新主委托

        //解冻资产
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(sub.getUserId());
        account.setCoinCode(muCode);
        account.setDelFlag(Account.DELFLAG.NO.code);
        if (!TxUtils.isMainCoinCode(muCode)) {
            //非主流币 需要区分交易所和母账号
            account.setMotherAccount(sub.getMotherAccount());
            account.setExchangeNo(sub.getExchange());
        }
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("Market BuyIn Failure Handler,Query Account[" + muCode + "] Is Null");
        }
        BigDecimal optGmv = sub.getGmv();
        int freeze;
        try {
            if (!TxUtils.isMainCoinCode(muCode)) {
                freeze = accountService.freeze(optGmv.negate(), muCode, sub.getUserId(), sub.getExchange());
            } else {
                freeze = accountService.freeze(optGmv.negate(), muCode, sub.getUserId());
            }
        } catch (Exception e) {
            log.error("资产解冻失败,{}", e.getMessage());
            throw new BussinessException("资产解冻失败");
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录 - 解冻资产记录
        String txDesc = "使用[" + muCode + "]市价买入[" + ziCode +
                "],交易额:" + optGmv + ". 买入失败-失败原因: " + msg + ",子委托uuid:" + sub.getUuid();
        int txLog = accountLogService.createLog(sub.getUserId(), muCode, optGmv, account.getAvailable(),
                account.getFreeze().subtract(optGmv),
                AccountLog.APPROACH.UNFREEZE.code, txDesc, AccountLogType.ADD);
        if (txLog < 1) {
            throw new BussinessException("资金记录保存失败");
        }

//        //更新主委托
//        //没有拆单
//        if (sub.getHasBrother() == SubDelegation.DELFLAG.NO.code) {
//            int mainState = MainDelegation.STATE.FINISHED.code;
//            mainDelegationService.updateStateByMainNo(mainState,"已完成,子委托:" + sub.getStateFormatter(), sub.getMainDelegateNo());
//        }
    }

    @Override
    public void handlerFailedOrderOfMarketSellOut(SubDelegation sub, String msg) {
        //子委托信息更新
        sub.setState(SubDelegation.STATE.FAILED.code);
        sub.setInfo(msg);
        sub.setUpdateTime(new Date());
        int update = subDelegationService.updateByStateForFailure(sub);
        if (update < 1) {
            log.error("更新子委托失败");
            return;
        }
        //解冻资产
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(sub.getUserId());
        account.setCoinCode(ziCode);
        account.setDelFlag(Account.DELFLAG.NO.code);
        //卖出操作需要区分是否是主流币种
        if (!TxUtils.isMainCoinCode(ziCode)) {
            //非主流币 需要区分交易所和母账号
            account.setMotherAccount(sub.getMotherAccount());
            account.setExchangeNo(sub.getExchange());
        }
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("Market SellOut Failure Handler,Query Account[" + ziCode + "] Is Null");
        }
        //更新资产账户
        BigDecimal optAmount = sub.getAmount();
        int freeze;
        try {
            if (!TxUtils.isMainCoinCode(ziCode)) {
                freeze = accountService.freeze(optAmount.negate(), ziCode, sub.getUserId(), sub.getExchange());
            } else {
                freeze = accountService.freeze(optAmount.negate(), ziCode, sub.getUserId());
            }
        } catch (Exception e) {
            log.error("资产解冻失败,{}", e.getMessage());

            throw new BussinessException("资产解冻失败");
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录
        String desc = "使用[" + muCode + "]市价卖出[" + ziCode +
                "],买入数量:" + optAmount + ",买入失败-失败原因: " + msg + ",子委托uuid:" + sub.getUuid();
        int log = accountLogService.createLog(sub.getUserId(), ziCode, optAmount, account.getAvailable(),
                account.getFreeze().subtract(optAmount),
                AccountLog.APPROACH.UNFREEZE.code, desc, AccountLogType.ADD);
        if (log < 1) {
            throw new BussinessException("资金记录保存失败");
        }

//        //没有拆单
//        if (sub.getHasBrother() == SubDelegation.DELFLAG.NO.code) {
//            int mainState = MainDelegation.STATE.FINISHED.code;
//            mainDelegationService.updateStateByMainNo(mainState,"已完成,子委托:" + sub.getStateFormatter(), sub.getMainDelegateNo());
//        }
    }

    @Override
    public void handleMainDelegateOfFailureByLimitedBuyIn(MainDelegation delegation) {
        String logInfo = "Main Delegate Limited BuyIn Failure Handle,";
        //主委托信息更新
        delegation.setState(MainDelegation.STATE.FAILED.code);
        delegation.setUpdateTime(new Date());
        mainDelegationService.updateByPrimaryKeySelective(delegation);
        //解冻资产
        String muCode = delegation.getCoinCurrency();
        String ziCode = delegation.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(delegation.getUserId());
        account.setCoinCode(muCode);
        if (!TxUtils.isMainCoinCode(muCode)) {
            account.setExchangeNo(delegation.getExchangeNo());
        }
        account.setDelFlag(Account.DELFLAG.NO.code);
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException(logInfo + ",Query Account[" + muCode + "] Is Null");
        }
        int freeze;
        if (!TxUtils.isMainCoinCode(muCode)) {
            freeze = accountService.freeze(delegation.getGmv().negate(), muCode, delegation.getUserId(), delegation.getExchangeNo());
        } else {
            freeze = accountService.freeze(delegation.getGmv().negate(), muCode, delegation.getUserId());
        }
        if (freeze < 1) {
            throw new BussinessException(logInfo + ",Unfreeze Amount[" + muCode + "] Failed");
        }

        //添加资产变更记录
        String desc = "使用[" + muCode + "]买入[" + ziCode + "],买入数量:" + delegation.getAmount() + ",消耗:" + delegation.getGmv() +
                "个" + muCode + ",申请买入失败,解冻用户资产.主委托uuid:" + delegation.getUuid();
        int log = accountLogService.createLog(delegation.getUserId(), muCode, delegation.getGmv(), account.getAvailable(),
                account.getFreeze().subtract(delegation.getGmv()),
                AccountLog.APPROACH.UNFREEZE.code, desc, AccountLogType.ADD);
        if (log < 1) {
            throw new BussinessException(logInfo + ",Unfreeze Account Log saved Failed");
        }
    }

    @Override
    public void handleMainDelegateOfFailureByLimitedSellout(MainDelegation delegation) {
        //主委托信息更新
        delegation.setState(MainDelegation.STATE.FAILED.code);
        delegation.setUpdateTime(new Date());
        mainDelegationService.updateByPrimaryKeySelective(delegation);
        //解冻资产
        String muCode = delegation.getCoinCurrency();
        String ziCode = delegation.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(delegation.getUserId());
        account.setCoinCode(ziCode);
        if (!TxUtils.isMainCoinCode(ziCode)) {
            account.setExchangeNo(delegation.getExchangeNo());
        }
        account.setDelFlag(Account.DELFLAG.NO.code);
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("资产账户查询失败");
        }
        //解冻用户资产账户
        int freeze;
        if (!TxUtils.isMainCoinCode(ziCode)) {
            freeze = accountService.freeze(delegation.getAmount().negate(), ziCode, delegation.getUserId(), delegation.getExchangeNo());
        } else {
            freeze = accountService.freeze(delegation.getAmount().negate(), ziCode, delegation.getUserId());
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录
        String desc1 = "交易货币对[" + ziCode + "_" + muCode + "],卖出[" + ziCode + "],卖出数量:" + delegation.getAmount() +
                ",卖出失败[" + delegation.getRemark() + "], 解冻用户资产.主委托uuid:" + delegation.getUuid();
        int log1 = accountLogService.createLog(delegation.getUserId(), ziCode, delegation.getAmount(), account.getAvailable(),
                account.getFreeze().subtract(delegation.getAmount()),
                AccountLog.APPROACH.UNFREEZE.code, desc1, AccountLogType.ADD);
        if (log1 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
    }

    @Override
    public void handleMainDelegateOfFailureByMarketBuyIn(MainDelegation delegation) {
        //主委托信息更新
        delegation.setState(MainDelegation.STATE.FAILED.code);
        delegation.setUpdateTime(new Date());
        mainDelegationService.updateByPrimaryKeySelective(delegation);
        //解冻资产
        String muCode = delegation.getCoinCurrency();
        String ziCode = delegation.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(delegation.getUserId());
        account.setCoinCode(muCode);
        if (!TxUtils.isMainCoinCode(muCode)) {
            account.setExchangeNo(delegation.getExchangeNo());
        }
        account.setDelFlag(Account.DELFLAG.NO.code);
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("资产账户查询失败");
        }
        //解冻用户资产账户
        int freeze;
        if (!TxUtils.isMainCoinCode(muCode)) {
            freeze = accountService.freeze(delegation.getGmv().negate(), muCode, delegation.getUserId(), delegation.getExchangeNo());
        } else {
            freeze = accountService.freeze(delegation.getGmv().negate(), muCode, delegation.getUserId());
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录
        String desc1 = "使用[" + muCode + "]市价买入[" + ziCode + "],买入交易额:" + delegation.getGmv() +
                ",买入失败[" + delegation.getRemark() + "], 解冻用户资产.主委托uuid:" + delegation.getUuid();
        int log1 = accountLogService.createLog(delegation.getUserId(), muCode, delegation.getGmv(), account.getAvailable(),
                account.getFreeze().subtract(delegation.getGmv()),
                AccountLog.APPROACH.UNFREEZE.code, desc1, AccountLogType.ADD);
        if (log1 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
    }

    @Override
    public void handleMainDelegateOfFailureByMarketSellout(MainDelegation delegation) {
        //主委托信息更新
        delegation.setState(MainDelegation.STATE.FAILED.code);
        delegation.setUpdateTime(new Date());
        mainDelegationService.updateByPrimaryKeySelective(delegation);
        //解冻资产
        String muCode = delegation.getCoinCurrency();
        String ziCode = delegation.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(delegation.getUserId());
        account.setCoinCode(ziCode);
        if (!TxUtils.isMainCoinCode(ziCode)) {
            account.setExchangeNo(delegation.getExchangeNo());
        }
        account.setDelFlag(Account.DELFLAG.NO.code);
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("资产账户查询失败");
        }
        //解冻用户资产账户
        int freeze;
        if (!TxUtils.isMainCoinCode(ziCode)) {
            freeze = accountService.freeze(delegation.getAmount().negate(), ziCode, delegation.getUserId(), delegation.getExchangeNo());
        } else {
            freeze = accountService.freeze(delegation.getAmount().negate(), ziCode, delegation.getUserId());
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }
        //添加资产变更记录
        String desc1 = "市价卖出交易货币对[" + ziCode + "_" + muCode + "],卖出[" + ziCode + "],卖出数量:" + delegation.getAmount() +
                ",卖出失败[" + delegation.getRemark() + "], 解冻用户资产.主委托uuid:" + delegation.getUuid();
        int log1 = accountLogService.createLog(delegation.getUserId(), ziCode, delegation.getAmount(), account.getAvailable(),
                account.getFreeze().subtract(delegation.getAmount()),
                AccountLog.APPROACH.UNFREEZE.code, desc1, AccountLogType.ADD);
        if (log1 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
    }

    @Override
    public void handlerRevokeOrderOfLimitedBuyIn(SubDelegation sub, String msg) {

        //子委托信息更新
        sub.setInfo(msg);
        sub.setUpdateTime(new Date());
        int update = subDelegationService.updateByStateForFailure(sub);
        if (update < 1) {
            log.error("更新子委托失败");
            return;
        }
        //解冻资产
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(sub.getUserId());
        account.setCoinCode(muCode);
        if (!TxUtils.isMainCoinCode(muCode)) {
            account.setExchangeNo(sub.getExchange());
        }
        account.setDelFlag(Account.DELFLAG.NO.code);
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("资产账户查询失败");
        }
        //更新资产账户 本次解冻数量 = 总交易额 - 成交额
        BigDecimal remainGmv = TxUtils.removeRedundanceZeroString(sub.getGmv().subtract(sub.getDealAmount().
                multiply(sub.getDealPrice()).setScale(10, BigDecimal.ROUND_HALF_UP)));
        int freeze;

        try {
            if (!TxUtils.isMainCoinCode(muCode)) {
                freeze = accountService.freeze(remainGmv.negate(), muCode, sub.getUserId(), sub.getExchange());
            } else {
                freeze = accountService.freeze(remainGmv.negate(), muCode, sub.getUserId());
            }
        } catch (Exception e) {
            log.error("资产解冻失败,{}", e.getMessage());
            throw new BussinessException("资产解冻失败");
        }

        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录
        String desc = "使用[" + muCode + "]限价买入[" + ziCode + "],买入数量:" + sub.getAmount() + ",消耗:" + sub.getGmv() +
                "个" + muCode + ",用户撤回,解冻用户资产.子委托uuid:" + sub.getUuid();
        int log = accountLogService.createLog(sub.getUserId(), muCode, remainGmv, account.getAvailable(),
                account.getFreeze().subtract(remainGmv),
                AccountLog.APPROACH.UNFREEZE.code, desc, AccountLogType.ADD);
        if (log < 1) {
            throw new BussinessException("资金记录保存失败");
        }

//        //没有拆单
//        if (sub.getHasBrother() == SubDelegation.DELFLAG.NO.code) {
//            int mainState = MainDelegation.STATE.FINISHED.code;
//            mainDelegationService.updateStateByMainNo(mainState,"已完成,子委托:" + sub.getStateFormatter(), sub.getMainDelegateNo());
//        }
    }

    @Override
    public void handlerRevokeOrderOfLimitedSellOut(SubDelegation sub, String msg) {
        //子委托信息更新
        sub.setState(SubDelegation.STATE.REVOKED.code);
        sub.setInfo(msg);
        sub.setUpdateTime(new Date());
        int update = subDelegationService.updateByStateForFailure(sub);
        if (update < 1) {
            log.error("更新子委托失败");
            return;
        }
        //解冻资产
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(sub.getUserId());
        account.setCoinCode(ziCode);
        account.setDelFlag(Account.DELFLAG.NO.code);
        //卖出操作需要区分是否是主流币种
        if (!TxUtils.isMainCoinCode(ziCode)) {
            //非主流币 需要区分交易所和母账号
            account.setExchangeNo(sub.getExchange());
        }
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("资产账户查询失败");
        }
        //更新资产账户 解冻资产 = 总委托数量 - 成交数量
        BigDecimal freezeAmount = TxUtils.removeRedundanceZeroString(sub.getAmount().subtract(sub.getDealAmount()));
        int freeze;
        if (!TxUtils.isMainCoinCode(ziCode)) {
            freeze = accountService.freeze(freezeAmount.negate(), ziCode, sub.getUserId(), sub.getExchange());
        } else {
            freeze = accountService.freeze(freezeAmount.negate(), ziCode, sub.getUserId());
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录
        String desc = "使用[" + ziCode + "]限价卖出[" + muCode +
                "],执行撤回操作,解冻数量:" + freezeAmount + ", 撤回子委托uuid:" + sub.getUuid();
        int log = accountLogService.createLog(sub.getUserId(), muCode, freezeAmount, account.getAvailable(),
                account.getFreeze().subtract(freezeAmount),
                AccountLog.APPROACH.UNFREEZE.code, desc, AccountLogType.ADD);
        if (log < 1) {
            throw new BussinessException("资金记录保存失败");
        }

//        //没有拆单
//        if (sub.getHasBrother() == SubDelegation.DELFLAG.NO.code) {
//            int mainState = MainDelegation.STATE.FINISHED.code;
//            mainDelegationService.updateStateByMainNo(mainState,"已完成,子委托:" + sub.getStateFormatter(), sub.getMainDelegateNo());
//        }
    }

    @Override
    public void handlerRevokeOrderOfMarketBuyIn(SubDelegation sub, String msg) {
        //子委托信息更新
        sub.setState(SubDelegation.STATE.REVOKED.code);
        sub.setInfo(msg);
        sub.setUpdateTime(new Date());
        int update = subDelegationService.updateByStateForFailure(sub);
        if (update < 1) {
            log.error("更新子委托失败");
            return;
        }
        //撤回 - 更新子委托信息、解冻资产、添加资产变更记录
        //1.如果没有拆单 则同步更新主委托

        //解冻资产
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(sub.getUserId());
        account.setCoinCode(muCode);
        account.setDelFlag(Account.DELFLAG.NO.code);
        if (!TxUtils.isMainCoinCode(muCode)) {
            //非主流币 需要区分交易所和母账号
            account.setExchangeNo(sub.getExchange());
        }
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("资产账户查询失败");
        }
        //更新资产账户 本次解冻数量 = 总交易额 - 成交额
        BigDecimal remainGmv = TxUtils.removeRedundanceZeroString(sub.getGmv().subtract(sub.getDealAmount().
                multiply(sub.getDealPrice()).setScale(10, BigDecimal.ROUND_HALF_UP)));
        int freeze;
        if (!TxUtils.isMainCoinCode(muCode)) {
            freeze = accountService.freeze(remainGmv.negate(), muCode, sub.getUserId(), sub.getExchange());
        } else {
            freeze = accountService.freeze(remainGmv.negate(), muCode, sub.getUserId());
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录 - 解冻资产记录
        String txDesc = "使用[" + muCode + "]市价买入[" + ziCode + "],买入交易额:" + sub.getGmv() +
                muCode + ",用户撤回,解冻用户资产" + remainGmv + ". 子委托uuid:" + sub.getUuid();
        int txLog = accountLogService.createLog(sub.getUserId(), muCode, remainGmv, account.getAvailable(),
                account.getFreeze().subtract(remainGmv),
                AccountLog.APPROACH.UNFREEZE.code, txDesc, AccountLogType.ADD);
        if (txLog < 1) {
            throw new BussinessException("资金记录保存失败");
        }

//        //没有拆单
//        if (sub.getHasBrother() == SubDelegation.DELFLAG.NO.code) {
//            int mainState = MainDelegation.STATE.FINISHED.code;
//            mainDelegationService.updateStateByMainNo(mainState,"已完成,子委托:" + sub.getStateFormatter(), sub.getMainDelegateNo());
//        }
    }

    @Override
    public void handlerRevokeOrderOfMarketSellOut(SubDelegation sub, String msg) {
        //子委托信息更新
        sub.setState(SubDelegation.STATE.REVOKED.code);
        sub.setInfo(msg);
        sub.setUpdateTime(new Date());
        int update = subDelegationService.updateByStateForFailure(sub);
        if (update < 1) {
            log.error("更新子委托失败");
            return;
        }
        //解冻资产
        String muCode = sub.getCoinCurrency();
        String ziCode = sub.getCoinCode();
        //查询资产账号
        Account account = new Account();
        account.setUserId(sub.getUserId());
        account.setCoinCode(ziCode);
        account.setDelFlag(Account.DELFLAG.NO.code);
        //卖出操作需要区分是否是主流币种
        if (!TxUtils.isMainCoinCode(ziCode)) {
            //非主流币 需要区分交易所和母账号
            account.setExchangeNo(sub.getExchange());
        }
        account = accountService.selectOne(account);
        if (null == account) {
            throw new BussinessException("资产账户查询失败");
        }
        //更新资产账户 解冻资产 = 总委托数量 - 成交数量
        BigDecimal freezeAmount = TxUtils.removeRedundanceZeroString(sub.getAmount().subtract(sub.getDealAmount()));
        int freeze;
        if (!TxUtils.isMainCoinCode(ziCode)) {
            freeze = accountService.freeze(freezeAmount.negate(), ziCode, sub.getUserId(), sub.getExchange());
        } else {
            freeze = accountService.freeze(freezeAmount.negate(), ziCode, sub.getUserId());
        }
        if (freeze < 1) {
            throw new BussinessException("资产解冻失败");
        }

        //添加资产变更记录
        String desc = "使用[" + ziCode + "]市价卖出[" + muCode +
                "],执行撤回操作,解冻数量:" + freezeAmount + ", 撤回子委托uuid:" + sub.getUuid();
        int log = accountLogService.createLog(sub.getUserId(), ziCode, freezeAmount, account.getAvailable(), account.getFreeze().subtract(freezeAmount),
                AccountLog.APPROACH.UNFREEZE.code, desc, AccountLogType.ADD);
        if (log < 1) {
            throw new BussinessException("资金记录保存失败");
        }

//        //没有拆单
//        if (sub.getHasBrother() == SubDelegation.DELFLAG.NO.code) {
//            int mainState = MainDelegation.STATE.FINISHED.code;
//            mainDelegationService.updateStateByMainNo(mainState,"已完成,子委托:" + sub.getStateFormatter(), sub.getMainDelegateNo());
//        }
    }
}
