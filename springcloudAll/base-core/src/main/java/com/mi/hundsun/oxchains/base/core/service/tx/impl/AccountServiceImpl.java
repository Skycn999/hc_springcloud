package com.mi.hundsun.oxchains.base.core.service.tx.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.constant.AccountLogType;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.service.tx.AccountLogService;
import com.mi.hundsun.oxchains.base.core.service.tx.AccountService;
import com.mi.hundsun.oxchains.base.core.tx.mapper.AccountMapper;
import com.mi.hundsun.oxchains.base.core.tx.mapper.MainDelegationMapper;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.base.core.tx.po.AccountLog;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.util.TxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资产持仓表业务相关Service接口实现<br>
 *
 * @author bin
 * @date 2018-04-13 09:54:58
 */
@Slf4j
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private AccountLogService accountLogService;
    @Resource
    private MainDelegationMapper mainDelegationMapper;

    @Override
    public GenericMapper<Account, Integer> _getMapper() {
        return accountMapper;
    }

    @Override
    public void updateByRecharge(Account account) {
        //先根据userId和资产代码查询用户资产持仓
        List<Account> accounts = accountMapper.select(new Account(a -> {
            a.setUserId(account.getUserId());
            a.setCoinCode(account.getCoinCode());
            a.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        //若已存在资产持仓
        String desc1 = "用户充入:" + account.getCoinCode() + "货币" + account.getTotal() + "个";
        if (accounts != null && accounts.size() > 0) {
            Account account1 = accounts.get(0);
            Account acc = new Account();
            acc.setId(account1.getId());
            acc.setTotal(account1.getTotal().add(account.getTotal()));
            acc.setAvailable(account1.getAvailable().add(account.getTotal()));
            acc.setUpdateTime(account.getUpdateTime());
            acc.setUpdator(account.getUpdator());
            accountMapper.updateByPrimaryKeySelective(acc);
            //资产记录变更
            int log1 = accountLogService.createLog(account.getUserId(), account.getCoinCode(), account.getTotal(), account1.getAvailable(),
                    account1.getFreeze(), AccountLog.APPROACH.RECHARGE.code, desc1, AccountLogType.ADD);
            if (log1 < 1) {
                throw new BussinessException("资金记录保存失败");
            }
        } else {
            account.setUuid(RandomUtils.randomCustomUUID());
            account.setFreeze(BigDecimal.ZERO);
            account.setCreateTime(new Date());
            account.setCreator(account.getUpdator());
            account.setUpdateTime(null);
            account.setUpdator(null);
            account.setDelFlag(GenericPo.DELFLAG.NO.code);
            accountMapper.insert(account);
            //资产记录变更
            int log1 = accountLogService.createLog(account.getUserId(), account.getCoinCode(), account.getTotal(), BigDecimal.ZERO,
                    BigDecimal.ZERO, AccountLog.APPROACH.RECHARGE.code, desc1, AccountLogType.ADD);
            if (log1 < 1) {
                throw new BussinessException("资金记录保存失败");
            }
        }
    }

    @Override
    public void mentionCoinNoPass(MentionCoin mentionCoin) throws BussinessException {
        //先根据userId和资产代码查询用户资产持仓
        List<Account> accounts = accountMapper.select(new Account(a -> {
            a.setUserId(mentionCoin.getUserId());
            a.setCoinCode(mentionCoin.getCoinCurrency());
            a.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        Account account = accounts.get(0);
        //提币不通过则解冻
        Account acc = new Account();
        acc.setId(account.getId());
        acc.setFreeze(account.getFreeze().subtract(mentionCoin.getAmount().add(mentionCoin.getServiceFee())));
        acc.setAvailable(account.getAvailable().add(mentionCoin.getAmount().add(mentionCoin.getServiceFee())));
        acc.setUpdateTime(new Date());
        acc.setUpdator(mentionCoin.getConfirmor());
        accountMapper.updateByPrimaryKeySelective(acc);
        //记录资产变更
        String desc1 = "用户提币解冻:" + mentionCoin.getCoinCurrency() + "货币" + mentionCoin.getAmount() + "个";
        int log1 = accountLogService.createLog(mentionCoin.getUserId(), mentionCoin.getCoinCurrency(), mentionCoin.getAmount(), account.getAvailable(),
                account.getFreeze().subtract(mentionCoin.getAmount()), AccountLog.APPROACH.MENTION_UNFREEZE.code, desc1, AccountLogType.ADD);
        if (log1 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
        String desc2 = "用户提币手续费解冻:" + mentionCoin.getCoinCurrency() + "货币" + mentionCoin.getServiceFee() + "个";
        int log2 = accountLogService.createLog(mentionCoin.getUserId(), mentionCoin.getCoinCurrency(), mentionCoin.getServiceFee(), account.getAvailable().add(mentionCoin.getAmount()),
                account.getFreeze().subtract(mentionCoin.getAmount().add(mentionCoin.getServiceFee())), AccountLog.APPROACH.MENTION_UNFREEZE.code, desc2, AccountLogType.ADD);
        if (log2 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
    }


    @Override
    public void mentionCoinSuccess(MentionCoin mentionCoin) throws BussinessException {
        //先根据userId和资产代码查询用户资产持仓
        List<Account> accounts = accountMapper.select(new Account(a -> {
            a.setUserId(mentionCoin.getUserId());
            a.setCoinCode(mentionCoin.getCoinCurrency());
            a.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        Account account = accounts.get(0);
        //扣除总额和冻结
        Account acc = new Account();
        acc.setId(account.getId());
        acc.setTotal(account.getTotal().subtract(mentionCoin.getAmount().add(mentionCoin.getServiceFee())));
        acc.setFreeze(account.getFreeze().subtract(mentionCoin.getAmount().add(mentionCoin.getServiceFee())));
        acc.setUpdateTime(new Date());
        acc.setUpdator(mentionCoin.getConfirmor());
        accountMapper.updateByPrimaryKeySelective(acc);
        //记录资产变更
        String desc1 = "用户提币扣除:" + mentionCoin.getCoinCurrency() + "货币" + mentionCoin.getAmount() + "个";
        int log1 = accountLogService.createLog(mentionCoin.getUserId(), mentionCoin.getCoinCurrency(), mentionCoin.getAmount(), account.getAvailable(),
                account.getFreeze().subtract(mentionCoin.getAmount()), AccountLog.APPROACH.DEDUCT.code, desc1, AccountLogType.UNCHANGE);
        if (log1 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
        String desc2 = "用户提币手续费扣除:" + mentionCoin.getCoinCurrency() + "货币" + mentionCoin.getServiceFee() + "个";
        int log2 = accountLogService.createLog(mentionCoin.getUserId(), mentionCoin.getCoinCurrency(), mentionCoin.getServiceFee(), account.getAvailable(),
                account.getFreeze().subtract(mentionCoin.getAmount().add(mentionCoin.getServiceFee())), AccountLog.APPROACH.DEDUCT.code, desc2, AccountLogType.UNCHANGE);
        if (log2 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
   }

    @Override
    public List<Account> getAvailMentionCurrency(Integer userId, Integer isMerge) {
        Map<String, Object> params = new HashMap<>();
        List<Account> accounts;
        //是否合并相同币种 0否 1是
        if (isMerge == 0) {
            //不合并
            params.put("whereSql", " AND t.user_id = " + userId + " AND t.coin_code in('BTC','ETH') AND t.del_flag = 0 ");
            accounts = accountMapper.getAvailMentionCurrencyNoMerge(params);
        } else {
            //合并
            params.put("whereSql", "AND t.user_id = " + userId + " AND t.coin_code in('BTC','ETH') AND t.del_flag = 0 ");
            params.put("sortSql", "ORDER BY t.update_time DESC ");
            accounts = accountMapper.getAvailMentionCurrencyMerge(params);
        }

        return accounts;
    }

    public Account getSumByCoinCode(Integer userId, String coinCode) {

        return accountMapper.getSumByCoinCode(userId, coinCode);
    }

    @Override
    public int insertList(List<Account> accounts) {
        return accountMapper.insertList(accounts);
    }

    @Override
    public void doMentionCoin(Integer userId, String code, String amount, String serviceFee) throws BussinessException {
        //冻结资产
        Account account = accountMapper.selectOne(new Account(a -> {
            a.setUserId(userId);
            a.setCoinCode(code);
            a.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        BigDecimal amount2 = new BigDecimal(amount).add(new BigDecimal(serviceFee));
        accountMapper.doMentionCoin(amount2, code, userId);

        String desc1 = "用户提币冻结:" + code + "货币" + amount + "个";
        int log1 = accountLogService.createLog(userId, code, new BigDecimal(amount), account.getAvailable(), account.getFreeze().add(new BigDecimal(amount)), AccountLog.APPROACH.MENTION_FREEZE.code, desc1, AccountLogType.REDUCE);
        if (log1 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
        String desc2 = "用户提币冻结:" + code + "货币" + amount + "个";
        int log2 = accountLogService.createLog(userId, code, new BigDecimal(serviceFee), account.getAvailable().subtract(new BigDecimal(amount)), account.getFreeze().add(new BigDecimal(amount)).add(new BigDecimal(serviceFee)), AccountLog.APPROACH.MENTION_FREEZE.code, desc2, AccountLogType.REDUCE);
        if (log2 < 1) {
            throw new BussinessException("资金记录保存失败");
        }
    }

    @Override
    public int freeze(BigDecimal gmv, String coinCurrency, Integer userId) {
        return accountMapper.freeze(gmv, coinCurrency, userId);
    }

    @Override
    public int freeze(BigDecimal gmv, String coinCurrency, Integer userId, String exchangeNo) {
        return accountMapper.freezeForSmallCurrency(gmv, coinCurrency, userId, exchangeNo);
    }

    @Override
    public int deductFreezen(BigDecimal amount, String coinCode, Integer userId) {
        return accountMapper.deductFreezen(amount, coinCode, userId);
    }

    @Override
    public int deductFreezen(BigDecimal amount, String coinCode, Integer userId, String exchangeNo) {
        return accountMapper.deductFreezenByExchangeNo(amount, coinCode, userId, exchangeNo);
    }

    @Override
    public int addPosition(BigDecimal amount, String coinCode, Integer userId, String exchangeNo) {
        //主流币种 主流币种没有交易所之分.
        if (coinCode.equals(CoinCode.BTC) || coinCode.equals(CoinCode.ETH)) {
            return accountMapper.addPositionNoExchange(amount, coinCode, userId);
        } else {
            return accountMapper.addPosition(amount, coinCode, userId, exchangeNo);
        }
    }


    @Override
    public MainDelegation handleAccountByMarketBuyIn(Integer userId, BigDecimal gmv, BigDecimal buyFeeScale,
                                                     String currencyPair, String exchangeNo) {
        String ziCode = currencyPair.split("_")[0];
        String muCode = currencyPair.split("_")[1];
        //查询当前用户对应币种的资产
        Account account = this.getSumByCoinCode(userId, muCode);
        //判断可用资产是否足够
        if (account.getAvailable().compareTo(gmv) < 0) {
            throw new BussinessException("您的可用资产不足");
        }
        //足够: 冻结本次交易资产和手续费 生成资金流水
        //更新资产账户
        try {
            int freeze;
            if (!TxUtils.isMainCoinCode(muCode)) {
                freeze = accountMapper.freezeForSmallCurrency(gmv, muCode, userId, exchangeNo);
            } else {
                freeze = accountMapper.freeze(gmv, muCode, userId);
            }
            if (freeze < 1) {
                throw new BussinessException("您的可用资产不足");
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BussinessException("您的可用资产不足");
        }
        //生成两条资金流水 交易一条 手续费一条
        String tradeLogDesc = "使用[" + muCode + "]市价买入[" + ziCode + "]. 交易额:" + gmv + "个" + muCode;
        int tradeLog = accountLogService.createLog(userId, muCode, gmv, account.getTotal(),
                account.getFreeze().add(gmv), AccountLog.APPROACH.FREEZE.code, tradeLogDesc, AccountLogType.REDUCE);
        if (tradeLog < 1) {
            throw new BussinessException("交易资金记录保存失败");
        }

        //生成主委托记录
        MainDelegation main = new MainDelegation();
        main.setUuid(RandomUtils.randomCustomUUID());
        main.setUserId(userId);
        main.setDelegateNo(OrderNoUtils.getMainDelegateOrderNo());
        main.setStyle(MainDelegation.STYLE.MARKET.code);
        main.setOrigin(MainDelegation.ORIGIN.PC.code);
        main.setDirection(MainDelegation.DIRECTION.BUYIN.code);
        main.setCoinCurrency(muCode);
        main.setCoinCode(ziCode);
        main.setAmount(BigDecimal.ZERO);
        main.setPrice(BigDecimal.ZERO);
        main.setGmv(gmv);
        main.setServiceFeeScale(buyFeeScale);
        main.setState(MainDelegation.STATE.REPORTED.code);
        main.setCreator("用户" + userId);
        int insert = mainDelegationMapper.insert(main);
        if (insert < 1) {
            throw new BussinessException("主委托记录保存失败");
        }
        return main;
    }

    @Override
    public MainDelegation handleAccountByMarketSellOut(Integer userId, BigDecimal sellAmount, BigDecimal sellFeeScale,
                                                       String currencyPair, String exchangeNo, String accountNo) {
        String ziCode = currencyPair.split("_")[0];
        String muCode = currencyPair.split("_")[1];
        //查询当前用户对应币种的资产
        Account account = new Account();
        account.setUserId(userId);
        account.setCoinCode(ziCode);
        account.setDelFlag(Account.DELFLAG.NO.code);
        //卖出操作需要区分是否是主流币种
        if (!TxUtils.isMainCoinCode(ziCode)) {
            //非主流币 需要区分交易所和母账号
            account.setExchangeNo(exchangeNo);
        }
        account = this.selectOne(account);
        if(null == account) {
            throw new BussinessException("未查询到可用持仓");
        }
        //判断加上手续费可用资产是否足够
        if (account.getAvailable().compareTo(sellAmount) < 0) {
            throw new BussinessException("您的可用资产不足");
        }
        //足够: 冻结本次交易资产和手续费 生成资金流水
        //更新资产账户
        try {
            int freeze;
            if (!TxUtils.isMainCoinCode(ziCode)) {
                freeze = accountMapper.freezeForSmallCurrency(sellAmount, ziCode, userId, exchangeNo);
            } else {
                freeze = accountMapper.freeze(sellAmount, ziCode, userId);
            }
            if (freeze < 1) {
                throw new BussinessException("您的可用资产不足");
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BussinessException("您的可用资产不足");
        }

        //生成两条资金流水 交易一条 手续费一条
        String tradeDesc = "使用[" + ziCode + "]市价卖出,以获得[" + muCode + "].卖出量:" + sellAmount;
        int tradeLog = accountLogService.createLog(userId, ziCode, sellAmount, account.getTotal(),
                account.getFreeze().add(sellAmount), AccountLog.APPROACH.FREEZE.code, tradeDesc, AccountLogType.REDUCE);
        if (tradeLog < 1) {
            throw new BussinessException("资金记录保存失败");
        }
        //生成主委托记录
        MainDelegation main = new MainDelegation();
        main.setUuid(RandomUtils.randomCustomUUID());
        main.setUserId(userId);
        main.setDelegateNo(OrderNoUtils.getMainDelegateOrderNo());
        main.setStyle(MainDelegation.STYLE.MARKET.code);
        main.setOrigin(MainDelegation.ORIGIN.PC.code);
        main.setDirection(MainDelegation.DIRECTION.SELLOUT.code);
        main.setCoinCurrency(muCode);
        main.setCoinCode(ziCode);
        main.setAmount(sellAmount);
        main.setPrice(BigDecimal.ZERO);
        main.setGmv(BigDecimal.ZERO);
        main.setServiceFeeScale(sellFeeScale);
        main.setState(MainDelegation.STATE.REPORTED.code);
        main.setCreator("用户" + userId);
        int insert = mainDelegationMapper.insert(main);
        if (insert < 1) {
            throw new BussinessException("主委托记录保存失败");
        }
        return main;
    }

    @Override
    public MainDelegation handleAccountByLimitedBuyIn(Integer userId, BigDecimal buyPrice, BigDecimal buyAmount,
                                                      BigDecimal feeScale, String currencyPair, String exchangeNo) {
        String ziCode = currencyPair.split("_")[0];
        String muCode = currencyPair.split("_")[1];
        //查询当前用户对应币种的资产
        Account account = this.getSumByCoinCode(userId, muCode);
        //计算交易额
        BigDecimal gmv = buyPrice.multiply(buyAmount);
        //判断加上手续费可用资产是否足够
        if (account.getAvailable().compareTo(gmv) < 0) {
            throw new BussinessException("您的可用资产不足");
        }
        //足够: 冻结本次交易资产和手续费 生成资金流水
        //更新资产账户
        try {
            int freeze ;
            if (!TxUtils.isMainCoinCode(muCode)) {
                freeze = accountMapper.freezeForSmallCurrency(gmv, muCode, userId, exchangeNo);
            } else {
                freeze = accountMapper.freeze(gmv, muCode, userId);
            }
            if (freeze < 1) {
                throw new BussinessException("您的可用资产不足");
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BussinessException("您的可用资产不足");
        }

        //生成两条资金流水 交易一条 手续费一条
        String tradeDesc = "使用[" + muCode + "]限价买入,以获得[" + ziCode + "]. 买入数量:" + buyAmount + ", 买入价格:" + buyPrice + "," +
                "消耗:" + gmv + "个[" + muCode + "]";
        int tradeLog = accountLogService.createLog(userId, muCode, gmv, account.getTotal(),
                account.getFreeze().add(gmv), AccountLog.APPROACH.FREEZE.code, tradeDesc, AccountLogType.REDUCE);
        if (tradeLog < 1) {
            throw new BussinessException("资金记录保存失败");
        }

        //生成主委托记录
        MainDelegation main = new MainDelegation();
        main.setUuid(RandomUtils.randomCustomUUID());
        main.setUserId(userId);
        main.setDelegateNo(OrderNoUtils.getMainDelegateOrderNo());
        main.setStyle(MainDelegation.STYLE.LIMITED.code);
        main.setOrigin(MainDelegation.ORIGIN.PC.code);
        main.setDirection(MainDelegation.DIRECTION.BUYIN.code);
        main.setCoinCurrency(muCode);
        main.setCoinCode(ziCode);
        main.setAmount(buyAmount);
        main.setPrice(buyPrice);
        main.setGmv(gmv);
        main.setServiceFeeScale(feeScale);
        main.setState(MainDelegation.STATE.REPORTED.code);
        main.setCreator("用户" + userId);
        int insert = mainDelegationMapper.insert(main);
        if (insert < 1) {
            throw new BussinessException("主委托记录保存失败");
        }
        return main;
    }

    @Override
    public MainDelegation handleAccountByLimitedSellOut(Integer userId, BigDecimal sellPrice, BigDecimal sellAmount,
                                                        BigDecimal feeScale, String currencyPair, String exchangeNo) {
        String ziCode = currencyPair.split("_")[0];
        String muCode = currencyPair.split("_")[1];
        //卖出交易对  查询分子对应的可用资产
        //查询当前用户对应币种的资产
        Account account = new Account();
        account.setUserId(userId);
        account.setCoinCode(ziCode);
        account.setDelFlag(Account.DELFLAG.NO.code);
        //卖出操作需要区分是否是主流币种
        if (!TxUtils.isMainCoinCode(ziCode)) {
            //非主流币 需要区分交易所和母账号
            account.setExchangeNo(exchangeNo);
        }
        account = this.selectOne(account);
        //判断可用资产是否足够 限价卖出 直接判断卖出量,不用判断交易额
        if (account.getAvailable().compareTo(sellAmount) < 0) {
            throw new BussinessException("您的可用资产不足");
        }
        //足够: 冻结本次交易资产和手续费 生成资金流水
        //更新资产账户
        try {
            int freeze;
            if (!TxUtils.isMainCoinCode(ziCode)) {
                freeze = accountMapper.freezeForSmallCurrency(sellAmount, ziCode, userId, exchangeNo);
            } else {
                freeze = accountMapper.freeze(sellAmount, ziCode, userId);
            }
            if (freeze < 1) {
                throw new BussinessException("您的可用资产不足");
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            throw new BussinessException("您的可用资产不足");
        }
        //生成1条资金流水
        String desc1 = "使用[" + ziCode + "]限价卖出,以获得[" + muCode + "].卖出量:" + sellAmount + ", 卖出价格:" + sellPrice;
        int log1 = accountLogService.createLog(userId, ziCode, sellAmount, account.getTotal(),
                account.getFreeze().add(sellAmount), AccountLog.APPROACH.FREEZE.code, desc1, AccountLogType.REDUCE);
        if (log1 < 1) {
            throw new BussinessException("交易资金记录保存失败");
        }
        //生成主委托记录
        MainDelegation main = new MainDelegation();
        main.setUuid(RandomUtils.randomCustomUUID());
        main.setUserId(userId);
        main.setDelegateNo(OrderNoUtils.getMainDelegateOrderNo());
        main.setStyle(MainDelegation.STYLE.LIMITED.code);
        main.setOrigin(MainDelegation.ORIGIN.PC.code);
        main.setDirection(MainDelegation.DIRECTION.SELLOUT.code);
        main.setCoinCurrency(muCode);
        main.setCoinCode(ziCode);
        main.setAmount(sellAmount);
        main.setPrice(sellPrice);
        main.setGmv(sellPrice.multiply(sellAmount));
        main.setServiceFeeScale(feeScale);
        main.setCreateTime(new Date());
        main.setState(MainDelegation.STATE.REPORTED.code);
        main.setCreator("用户" + userId);
        int insert = mainDelegationMapper.insert(main);
        if (insert < 1) {
            throw new BussinessException("主委托记录保存失败");
        }
        return main;
    }


}
