/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.controller;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.common.CommodityModel;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.constant.CoinCode;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.service.tx.AccountService;
import com.mi.hundsun.oxchains.base.core.service.tx.MainDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.util.TxUtils;
import com.mi.hundsun.oxchains.provider.trade.service.QuoteHuoBiInterface;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易服务控制器
 *
 * @author 枫亭
 * @description
 * @date 2018-04-08 20:49.
 */
@Api("用户资产持仓管理服务")
@Slf4j
@RestController
public class AccountController extends GenericController {

    @Autowired
    AccountService accountService;
    @Autowired
    MainDelegationService mainDelegationService;
    @Autowired
    QuoteHuoBiInterface huoBiInterface;


    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/tx/account/getDtGridList")
    public DtGrid getDtGridList(@RequestBody DtGrid dtGrid) throws Exception {
        return accountService.getDtGridList(dtGrid);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/tx/account/insert")
    public void insert(@RequestBody Account account) throws BussinessException {
        accountService.insert(account);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/tx/account/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Account account) throws BussinessException {
        accountService.updateByPrimaryKeySelective(account);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/tx/account/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        accountService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/tx/account/removeById")
    public void removeById(@RequestBody Account account) throws BussinessException {
        accountService.removeById(account);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/tx/account/getNormalModelById")
    public Account getNormalModelById(@RequestBody Account account) throws BussinessException {
        return accountService.getNormalModelById(account);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/tx/account/selectOne")
    public Account selectOne(@RequestBody Account account) throws BussinessException {
        return accountService.selectOne(account);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/tx/account/select")
    public List<Account> select(@RequestBody Account account) throws BussinessException {
        return accountService.select(account);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/tx/account/selectAll")
    public List<Account> selectAll() throws BussinessException {
        return accountService.selectAll();
    }

    @ApiOperation(value = "充币更新资产记录")
    @PostMapping(value = "/tx/account/updateByRecharge")
    public ResultEntity updateByRecharge(@RequestBody Account account) {
        try {
            accountService.updateByRecharge(account);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation(value = "提币不通过解冻资产")
    @PostMapping(value = "/tx/account/mentionCoinNoPass")
    public ResultEntity mentionCoinNoPass(@RequestBody MentionCoin mentionCoin) {
        try {
            accountService.mentionCoinNoPass(mentionCoin);
            return ResultEntity.success();
        } catch (BussinessException e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation(value = "提币录入成功扣除资产")
    @PostMapping(value = "/tx/account/mentionCoinSuccess")
    public ResultEntity mentionCoinSuccess(@RequestBody MentionCoin mentionCoin) {
        try {
            accountService.mentionCoinSuccess(mentionCoin);
            return ResultEntity.success();
        } catch (BussinessException e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }


    @ApiOperation(value = "查找我的资产持仓记录")
    @PostMapping(value = "/tx/account/selectByUserId")
    public ResultEntity selectByUserId(@RequestParam Integer userId) {
        try {
            Account account = new Account();
            account.setUserId(userId);
            List<Account> list = accountService.select(account);
            for(Account a: list) {
                a.setTotal(TxUtils.removeRedundanceZeroString(a.getTotal()));
                a.setAvailable(TxUtils.removeRedundanceZeroString(a.getAvailable()));
                a.setFreeze(TxUtils.removeRedundanceZeroString(a.getFreeze()));
            }
            return ok(JSON.toJSONString(list));
        } catch (BussinessException e) {
            return fail(e.getMessage());
        } catch (Exception e) {
            return fail();
        }
    }

    @ApiOperation(value = "获取用户可提币的币种列表", notes = "获取用户可提币的币种列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "isMerge", value = "是否合并同一品种", required = true, dataType = "Integer")
    })
    @PostMapping("/tx/account/getAvailMentionCurrency")
    public ResultEntity getAvailMentionCurrency(@RequestParam Integer userId, @RequestParam Integer isMerge) {
        if (null == isMerge) {
            return fail("[isMerge]不能为空");
        }
        if (null == userId || 0 == userId) {
            return fail("[userId]不能为空");
        }
        List<Account> list = accountService.getAvailMentionCurrency(userId, isMerge);
        if (null != list && list.size() > 0) {
            return ok(JSON.toJSONString(list));
        }
        return fail("没有数据");
    }


    @ApiOperation(value = "处理限价买入资金账户", notes = "处理市价买入资金账户,正常情况下返回主委托记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "buyPrice", value = "委托价", required = true, dataType = "String"),
            @ApiImplicitParam(name = "buyAmount", value = "卖出价", required = true, dataType = "String"),
            @ApiImplicitParam(name = "buyFeeScale", value = "手续费", required = true, dataType = "String"),
            @ApiImplicitParam(name = "currencyPair", value = "交易对", required = true, dataType = "String"),
            @ApiImplicitParam(name = "exchangeNo", value = "交易所", required = true, dataType = "String")
    })
    @PostMapping("/tx/account/handleAccountByLimitedBuyIn")
    public ResultEntity handleAccountByLimitedBuyIn(@RequestParam Integer userId,
                                                    @RequestParam BigDecimal price,
                                                    @RequestParam BigDecimal amount,
                                                    @RequestParam BigDecimal buyFeeScale,
                                                    @RequestParam String currencyPair,
                                                    @RequestParam String exchangeNo) {

        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (null == buyFeeScale || buyFeeScale.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[buyFeeScale]不能为空");
        }
        if (null == price || price.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[price]不能为空");
        }
        if (null == amount || amount.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[amount]不能为空");
        }
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[currencyPair]不能为空");
        }
        if (!currencyPair.contains("_")) {
            throw new BussinessException("交易对格式不合法");
        }
        MainDelegation delegation = accountService.handleAccountByLimitedBuyIn(userId, price, amount, buyFeeScale,
                currencyPair, exchangeNo);
        MainDelegation main = new MainDelegation();
        main.setDelegateNo(delegation.getDelegateNo());
        main = mainDelegationService.selectOne(main);
        if (null != main && null != main.getDelegateNo()) {
            return ok(JSON.toJSONString(main));
        }
        return fail("生成主委托记录失败");
    }

    @ApiOperation(value = "处理限价买入资金账户", notes = "处理市价买入资金账户,正常情况下返回主委托记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "price", value = "委托价", required = true, dataType = "String"),
            @ApiImplicitParam(name = "amount", value = "卖出量", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sellFeeScale", value = "手续费", required = true, dataType = "String"),
            @ApiImplicitParam(name = "currencyPair", value = "交易对", required = true, dataType = "String"),
            @ApiImplicitParam(name = "exchangeNo", value = "交易所编号", required = true, dataType = "String")
    })
    @PostMapping("/tx/account/handleAccountByLimitedSellOut")
    public ResultEntity handleAccountByLimitedSellOut(@RequestParam Integer userId,
                                                      @RequestParam BigDecimal price,
                                                      @RequestParam BigDecimal amount,
                                                      @RequestParam BigDecimal sellFeeScale,
                                                      @RequestParam String currencyPair,
                                                      @RequestParam String exchangeNo) {

        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (null == sellFeeScale || sellFeeScale.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[buyFeeScale]不能为空");
        }
        if (null == price || price.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[price]不能为空");
        }
        if (null == amount || amount.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[amount]不能为空");
        }
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[currencyPair]不能为空");
        }
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        if (!currencyPair.contains("_")) {
            throw new BussinessException("交易对格式不合法");
        }
        MainDelegation delegation = accountService.handleAccountByLimitedSellOut(userId, price, amount, sellFeeScale, currencyPair, exchangeNo);
        MainDelegation main = new MainDelegation();
        main.setDelegateNo(delegation.getDelegateNo());
        main = mainDelegationService.selectOne(main);
        if (null != main && null != main.getDelegateNo()) {
            return ok(JSON.toJSONString(main));
        }
        return fail("生成主委托记录失败");
    }

    @ApiOperation(value = "处理市价买入资金账户", notes = "处理市价买入资金账户,正常情况下返回主委托记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "gmv", value = "卖出价", required = true, dataType = "String"),
            @ApiImplicitParam(name = "buyFeeScale", value = "买入手续费率", required = true, dataType = "String"),
            @ApiImplicitParam(name = "currencyPair", value = "交易对", required = true, dataType = "String"),
            @ApiImplicitParam(name = "exchangeNo", value = "交易对", required = true, dataType = "String")
    })
    @PostMapping("/tx/account/handleAccountByMarketBuyIn")
    public ResultEntity handleAccountByMarketBuyIn(@RequestParam Integer userId,
                                                   @RequestParam BigDecimal gmv,
                                                   @RequestParam BigDecimal buyFeeScale,
                                                   @RequestParam String currencyPair,
                                                   @RequestParam String exchangeNo) {

        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (null == buyFeeScale || buyFeeScale.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[buyFeeScale]不能为空");
        }

        if (null == gmv || gmv.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[gmv]不能为空");
        }
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[currencyPair]不能为空");
        }
        if (!currencyPair.contains("_")) {
            throw new BussinessException("交易对格式不合法");
        }
        MainDelegation delegation = accountService.handleAccountByMarketBuyIn(userId, gmv, buyFeeScale, currencyPair, exchangeNo);
        MainDelegation main = new MainDelegation();
        main.setDelegateNo(delegation.getDelegateNo());
        main = mainDelegationService.selectOne(main);
        if (null != main && null != main.getDelegateNo()) {
            return ok(JSON.toJSONString(main));
        }
        return fail("生成主委托记录失败");
    }

    @ApiOperation(value = "处理市价卖出资金账户", notes = "处理市价买入资金账户,正常情况下返回主委托记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "gmv", value = "卖出价", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sellFeeScale", value = "卖出手续费率", required = true, dataType = "String"),
            @ApiImplicitParam(name = "currencyPair", value = "交易对", required = true, dataType = "String"),
            @ApiImplicitParam(name = "exchangeNo", value = "交易所编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accountNo", value = "交易所母账号", required = true, dataType = "String")
    })
    @PostMapping("/tx/account/handleAccountByMarketSellOut")
    public ResultEntity handleAccountByMarketSellOut(@RequestParam Integer userId,
                                                     @RequestParam BigDecimal gmv,
                                                     @RequestParam BigDecimal sellFeeScale,
                                                     @RequestParam String currencyPair,
                                                     @RequestParam String exchangeNo,
                                                     @RequestParam String accountNo) {

        if (null == userId || 0 == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (null == sellFeeScale || sellFeeScale.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[sellFeeScale]不能为空");
        }

        if (null == gmv || gmv.equals(BigDecimal.ZERO)) {
            throw new BussinessException("[gmv]不能为空");
        }
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[currencyPair]不能为空");
        }
        if (!currencyPair.contains("_")) {
            throw new BussinessException("交易对格式不合法");
        }
        MainDelegation delegation = accountService.handleAccountByMarketSellOut(userId, gmv, sellFeeScale, currencyPair, exchangeNo, accountNo);
        MainDelegation main = new MainDelegation();
        main.setDelegateNo(delegation.getDelegateNo());
        main = mainDelegationService.selectOne(main);
        if (null != main && null != main.getDelegateNo()) {
            return ok(JSON.toJSONString(main));
        }
        return fail("生成主委托记录失败");
    }

    @ApiOperation(value = "校验用户输入的提币数量", notes = "校验用户输入的提币数量是否小于用户的可用数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "提币币种", required = true, dataType = "String"),
            @ApiImplicitParam(name = "amount", value = "用户提币数量", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceFee", value = "提币手续费", required = true, dataType = "String"),
    })
    @PostMapping("/tx/account/checkAvailAmount")
    public boolean checkAvailAmount(@RequestParam Integer userId, @RequestParam String code, @RequestParam String amount, @RequestParam String serviceFee) {
        //获取用户账户可用数量
        Account account = accountService.getSumByCoinCode(userId, code);
        return account.getAvailable().compareTo(new BigDecimal(amount).add(new BigDecimal(serviceFee))) >= 0;
    }

    @ApiOperation(value = "执行提币资金操作", notes = "执行提币资金操作(冻结资产、新增资产变更流水)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "提币币种", required = true, dataType = "String"),
            @ApiImplicitParam(name = "amount", value = "用户提币数量", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceFee", value = "提币手续费", required = true, dataType = "String"),
    })
    @PostMapping("/tx/account/doMentionCoin")
    public ResultEntity doMentionCoin(@RequestParam Integer userId, @RequestParam String code, @RequestParam String amount, @RequestParam String serviceFee) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(code) || StringUtils.isBlank(amount) || StringUtils.isBlank(serviceFee)) {
            return fail("参数错误");
        }
        try {
            accountService.doMentionCoin(userId, code, amount, serviceFee);
            return ok();
        } catch (BussinessException e) {
            e.printStackTrace();
            return fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @PostMapping(value = "/tx/account/findAvailAccount")
    public ResultEntity findAvailAccount(@RequestParam Integer userId,
                                                @RequestParam String symbol) {
        if (null == userId) {
            throw new BussinessException("[userId]不能为空");
        }
        if (StrUtil.isBlank(symbol)) {
            throw new BussinessException("[code]不能为空");
        }
        //分隔交易对
        String[] split = symbol.split("_");
        String muCode = split[1];
        String ziCode = split[0];
        Map<String, Object> map = new LinkedHashMap<>();
        //查询币种持仓
        Account a = new Account();
        a.setUserId(userId);
        a.setCoinCode(muCode);
        a.setDelFlag(Account.DELFLAG.NO.code);
        List<Account> muCodeAccounts = accountService.select(a);
        if (null != muCodeAccounts && muCodeAccounts.size() > 0) {
            //主流币种
            if (TxUtils.isMainCoinCode(muCode)) {
                map.put(muCode.toUpperCase(), TxUtils.removeRedundanceZeroString(muCodeAccounts.get(0).getAvailable()).toString());
            } else {
                Map<String, Object> map2 = new LinkedHashMap<>();
                for (Account account : muCodeAccounts) {
                    map2.put(account.getExchangeNo(), TxUtils.removeRedundanceZeroString(account.getAvailable()).toString());
                }
                map.put(muCode.toUpperCase(), map2);
            }
        } else {
            map.put(muCode.toUpperCase(), "0.00000");
        }

        Account a2 = new Account();
        //查询币种持仓
        a2.setUserId(userId);
        a2.setCoinCode(ziCode);
        a2.setDelFlag(Account.DELFLAG.NO.code);
        List<Account> accounts = accountService.select(a2);

        if (null != accounts && accounts.size() > 0) {
            //主流币种
            if (TxUtils.isMainCoinCode(ziCode)) {
                map.put(ziCode.toUpperCase(), TxUtils.removeRedundanceZeroString(accounts.get(0).getAvailable()).toString());
            } else {
                Map<String, Object> map2 = new LinkedHashMap<>();
                for (Account account : accounts) {
                    map2.put(account.getExchangeNo(), TxUtils.removeRedundanceZeroString(account.getAvailable()).toString());
                }
                map.put(ziCode.toUpperCase(), map2);
            }
        } else {
            map.put(ziCode.toUpperCase(), "0.00000");
        }
        return ok(JSON.toJSONString(map));
    }

    /**
     * 计算账户当前净资产
     *
     * @param userId 待计算用户ID
     * @return 净资产
     */
    @PostMapping(value = "/tx/account/getNetWorth")
    public BigDecimal getNetWorth(@RequestParam Integer userId) {
        ResultEntity resultEntity = this.selectByUserId(userId);
        BigDecimal netWorth = BigDecimal.ZERO;
        if (resultEntity.getCode() == ResultEntity.SUCCESS) {
            List<Account> accounts = JSON.parseArray(resultEntity.getData().toString(), Account.class);
            //记录不能直接转换成BTC的币种
            List<Account> unableDirectConvertBtcAccounts = new ArrayList<>();
            //以BTC为分母进行转换
            for (Account account : accounts) {
                //有uSDT持仓 单独计算
                if (account.getCoinCode().equalsIgnoreCase(CoinCode.USDT)) {
                    String symbol = CoinCode.BTC + "_" + CoinCode.USDT;
                    ResultEntity jsonObject = JSON.parseObject(huoBiInterface.price(symbol), ResultEntity.class);
                    List<CommodityModel> commodityModels = JSON.parseArray(jsonObject.getData().toString(), CommodityModel.class);
                    if (null != commodityModels && commodityModels.size() > 0) {
                        CommodityModel model = commodityModels.get(0);
                        String lastPrice = model.getLastPrice();
                        String plainString = account.getTotal().
                                divide(new BigDecimal(lastPrice), 10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
                                .toPlainString();
                        netWorth = netWorth.add(new BigDecimal(plainString));
                    }
                    continue;
                }
                if (!account.getCoinCode().equalsIgnoreCase(CoinCode.BTC)) {
                    String symbol = account.getCoinCode() + "_" + CoinCode.BTC;
                    ResultEntity jsonObject = JSON.parseObject(huoBiInterface.price(symbol), ResultEntity.class);
                    List<CommodityModel> commodityModels = JSON.parseArray(jsonObject.getData().toString(), CommodityModel.class);
                    if (null != commodityModels && commodityModels.size() > 0) {
                        CommodityModel model = commodityModels.get(0);
                        String lastPrice = model.getLastPrice();
                        if (StrUtil.isNotBlank(lastPrice)) {
                            netWorth = netWorth.add(new BigDecimal(model.getLastPrice()).multiply(account.getTotal()));
                        } else {
                            unableDirectConvertBtcAccounts.add(account);
                        }
                    }
                } else {
                    //资产列表中是否包含btc 包含的话 净资产直接加
                    netWorth = netWorth.add(account.getTotal());
                }
            }

            //记录不能直接转换成ETH的币种
            List<Account> unableDirectConvertEthAccounts = new ArrayList<>();
            //以ETH为分母进行转换
            for (Account a : unableDirectConvertBtcAccounts) {
                if (!a.getCoinCode().equalsIgnoreCase(CoinCode.ETH)) {
                    String symbol = a.getCoinCode() + "_" + CoinCode.ETH;
                    ResultEntity jsonObject = JSON.parseObject(huoBiInterface.price(symbol), ResultEntity.class);
                    List<CommodityModel> commodityModels = JSON.parseArray(jsonObject.getData().toString(), CommodityModel.class);
                    if (null != commodityModels && commodityModels.size() > 0) {
                        CommodityModel model = commodityModels.get(0);
                        String lastPrice = model.getLastPrice();
                        if (StrUtil.isNotBlank(lastPrice)) {
                            netWorth = netWorth.add(new BigDecimal(lastPrice).multiply(a.getTotal()));
                        } else {
                            unableDirectConvertEthAccounts.add(a);
                        }
                    }
                }
            }

            //以USDT为分母进行转换
            for (Account a : unableDirectConvertEthAccounts) {
                if (!a.getCoinCode().equalsIgnoreCase(CoinCode.USDT)) {
                    String symbol = a.getCoinCode() + "_" + CoinCode.USDT;
                    ResultEntity jsonObject = JSON.parseObject(huoBiInterface.price(symbol), ResultEntity.class);
                    List<CommodityModel> commodityModels = JSON.parseArray(jsonObject.getData().toString(), CommodityModel.class);
                    if (null != commodityModels && commodityModels.size() > 0) {
                        CommodityModel model = commodityModels.get(0);
                        netWorth = netWorth.add(new BigDecimal(model.getLastPrice()).multiply(a.getTotal()));
                    }
                }
            }
        }
        return new BigDecimal(netWorth.stripTrailingZeros().toPlainString());
    }


}
