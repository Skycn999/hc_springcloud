/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.enums.ExchangeEnum;
import com.mi.hundsun.oxchains.base.core.common.SubOrderModel;
import com.mi.hundsun.oxchains.base.core.common.VoDetail;
import com.mi.hundsun.oxchains.base.core.common.VoPrModel;
import com.mi.hundsun.oxchains.base.core.common.VolumeComparator;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.model.quote.Depth;
import com.mi.hundsun.oxchains.base.core.model.quote.model.OrderQryRes;
import com.mi.hundsun.oxchains.base.core.service.tx.MainDelegationService;
import com.mi.hundsun.oxchains.base.core.service.tx.SubDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.mi.hundsun.oxchains.base.core.util.TxUtils;
import com.mi.hundsun.oxchains.provider.trade.rabbitmq.TxSyncMessageProducer;
import com.mi.hundsun.oxchains.provider.trade.service.*;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 枫亭
 * @description 交易相关
 * @date 2018-05-04 11:42.
 */
@Slf4j
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class TxServiceImpl implements TxService {

    @Resource
    private ExchangeInterface exchangeInterface;
    @Resource
    private TradeHuoBiInterface tradeHuoBiInterface;
    @Resource
    private TradeBitFinexInterface tradeBitFinexInterface;
    @Resource
    private TradeBianInterface tradeBianInterface;
    @Resource
    private SubDelegationService subDelegationService;
    @Resource
    private MainDelegationService mainDelegationService;
    @Autowired
    TxSyncMessageProducer txSyncMessageProducer;

    @Override
    public String doOrderByLimited(MainDelegation delegate, String exchangeNo, BigDecimal price, BigDecimal amount, String currencyPair,
                                   MotherAccountInfoModel account, Integer style) {
        price = TxUtils.removeRedundanceZeroString(price);
        amount = TxUtils.removeRedundanceZeroString(amount);
        //判断委托类型 市价还是限价
        String entrustProp;
        if (style == MainDelegation.STYLE.MARKET.code) {
            entrustProp = ENTRUST_PROP_1;
        } else if (style == MainDelegation.STYLE.LIMITED.code) {
            entrustProp = ENTRUST_PROP_2;
        } else {
            throw new BussinessException("委托类型错误");
        }

        //判断交易方向 买入还是卖出
        String entrustBs;
        if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
            entrustBs = ENTRUST_BS_1;
        } else if (delegate.getDirection() == MainDelegation.DIRECTION.SELLOUT.code) {
            entrustBs = ENTRUST_BS_2;
        } else {
            throw new BussinessException("委托类型错误");
        }
        SubDelegation subDelegation = null;
        String json = null;
        try {
            subDelegation = subDelegationService.generateForLimited(delegate, amount, price, exchangeNo, account.getAccountName());
            //获取数量最大的卖1挡交易所发起下单请求。
            if (exchangeNo.equals(ExchangeEnum.BIAN.getCode())) {
                //调用币安接口
                json = tradeBianInterface.order(account.getApiKey(), account.getApiSecret(),
                        currencyPair, entrustBs, entrustProp, price.toString(), amount.toString());
            } else if (exchangeNo.equals(ExchangeEnum.HUOBI.getCode())) {
                //调用火币接口
                json = tradeHuoBiInterface.order(account.getApiKey(), account.getApiSecret(),
                        currencyPair, entrustBs, entrustProp, price.toString(), amount.toPlainString());
            } else if (exchangeNo.equals(ExchangeEnum.BITFINEX.getCode())) {
                //调用bitfinex接口
                json = tradeBitFinexInterface.order(account.getApiKey(), account.getApiSecret(),
                        currencyPair, entrustBs, entrustProp, price.toString(), amount.toString());
            } else {
                log.error("没有支持的交易所");
            }
        } catch (Exception e) {
            log.error("发起委托请求失败,错误信息:{}", e.getMessage());
            txSyncMessageProducer.sendSubDelegateFailureTask(subDelegation, e.getMessage());
            return "";
        }

        if (StrUtil.isNotBlank(json)) {
            JSONObject jsonObject = JSON.parseObject(json);
            if (jsonObject.getString("code").equals("200")) {
                String entrustNo = JSON.parseObject(jsonObject.getString("data")).getString("entrust_no");
                subDelegation.setEntrustNo(entrustNo);
                subDelegation.setState(SubDelegation.STATE.TRADING.code);
                subDelegation.setUpdateTime(new Date());
                subDelegationService.updateByPrimaryKeySelective(subDelegation);
                return entrustNo;
                //后续操作详见同步定时器 - 增加持仓等信息需要通过定时器查询委托编号 根据返回的信息判断是否增加持仓还是解冻资产
            } else {
                log.error("限价委托失败:{}", json);
                // 没有返回委托编号 可能是由于网络等原因导致请求失败 需要解冻资产，结束此次交易流程。
                txSyncMessageProducer.sendSubDelegateFailureTask(subDelegation, jsonObject.getString("msg"));
            }
        }
        return "";
        // 同步模块 后面需要根据委托订单号进行查询  判断交易真实状态，进行资金操作
    }

    @Override
    public String doOrderByMarket(MainDelegation delegation, String exchangeNo, BigDecimal gmv, String currencyPair,
                                  MotherAccountInfoModel account, Integer style) {
        gmv = TxUtils.removeRedundanceZeroString(gmv);
        //判断委托类型 市价还是限价
        String entrustProp;
        if (style == MainDelegation.STYLE.MARKET.code) {
            entrustProp = ENTRUST_PROP_1;
        } else if (style == MainDelegation.STYLE.LIMITED.code) {
            entrustProp = ENTRUST_PROP_2;
        } else {
            throw new BussinessException("委托类型错误");
        }

        //判断交易方向 买入还是卖出
        String entrustBs;
        if (delegation.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
            entrustBs = ENTRUST_BS_1;
        } else if (delegation.getDirection() == MainDelegation.DIRECTION.SELLOUT.code) {
            entrustBs = ENTRUST_BS_2;
        } else {
            throw new BussinessException("委托类型错误");
        }
        SubDelegation subDelegation;
        if (delegation.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
            subDelegation = subDelegationService.generateForMarketBuyIn(delegation, gmv, exchangeNo, account.getAccountName());
        } else {
            subDelegation = subDelegationService.generateForMarketSellOut(delegation, gmv, exchangeNo, account.getAccountName());
        }
        String json = null;
        try {
            //获取数量最大的卖1挡交易所发起下单请求。
            if (exchangeNo.equals(ExchangeEnum.BIAN.getCode())) {
                //调用币安接口
                json = tradeBianInterface.order(account.getApiKey(), account.getApiSecret(),
                        currencyPair, entrustBs, entrustProp, "", TxUtils.removeRedundanceZeroString(gmv).toString());
            } else if (exchangeNo.equals(ExchangeEnum.HUOBI.getCode())) {
                //调用火币接口
                json = tradeHuoBiInterface.order(account.getApiKey(), account.getApiSecret(),
                        currencyPair, entrustBs, entrustProp, "", TxUtils.removeRedundanceZeroString(gmv).toString());
            } else if (exchangeNo.equals(ExchangeEnum.BITFINEX.getCode())) {
                //调用bitfinex接口
                json = tradeBitFinexInterface.order(account.getApiKey(), account.getApiSecret(),
                        currencyPair, entrustBs, entrustProp, "", TxUtils.removeRedundanceZeroString(gmv).toString());
            } else {
                log.error("没有支持的交易所");
            }
        } catch (Exception e) {
            log.error("发起委托请求失败,错误信息:{}", e.getMessage());
            String msg;
            if (e instanceof BussinessException) {
                msg = e.getMessage();
            } else {
                msg = "系统异常";
            }
            txSyncMessageProducer.sendSubDelegateFailureTask(subDelegation, msg);
            return "";
        }

        if (StrUtil.isNotBlank(json)) {
            JSONObject jsonObject = JSON.parseObject(json);
            if (jsonObject.getString("code").equals("200")) {
                String entrustNo = JSON.parseObject(jsonObject.getString("data")).getString("entrust_no");
                subDelegation.setEntrustNo(entrustNo);
                subDelegation.setState(SubDelegation.STATE.TRADING.code);
                subDelegation.setUpdateTime(new Date());
                subDelegationService.updateByPrimaryKeySelective(subDelegation);
                return entrustNo;
                //同步模块 增加持仓等信息需要通过定时器查询委托编号 根据返回的信息判断是否增加持仓还是解冻资产
            } else {
                log.error("市价委托失败:{}" + json);
                //失败操作
                txSyncMessageProducer.sendSubDelegateFailureTask(subDelegation, jsonObject.getString("msg"));
            }
        }
        return "";
        // 同步模块 后面需要根据委托订单号进行查询  判断交易真实状态，进行资金操作
    }

    @Override
    public void separateOrderByMarketPriceOfBuyIn(Depth depth, MainDelegation delegate, List<MotherAccountInfoModel> accountInfoModels) {
        //市价买入时根据交易额来拆单
        List<VoPrModel> voPrModels;
        if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
            //买入 - 解析卖档数据
            voPrModels = this.resolveDepth(depth, "ask");
        } else {
            //卖出 - 解析买档数据
            voPrModels = this.resolveDepth(depth, "bid");
        }

        String symbol = delegate.getCoinCode() + "_" + delegate.getCoinCurrency().toLowerCase();
        Map<String, SubOrderModel> subOrderModels = new HashMap<>();
        List<VoDetail> voDetails;
        //剩余数量
        BigDecimal remainGmv = delegate.getGmv();
        for (VoPrModel prModel : voPrModels) {
            voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
            //是否跳出循环标记
            int c = 0;
            for (VoDetail voDetail : voDetails) {
                //声明本次下单数量
                BigDecimal thisGmv;
                //卖挡数据提供的交易额
                BigDecimal depthGmv = voDetail.getVolume().multiply(prModel.getPrice());
                //剩余数量小于等于0 表明已全部满足用户的委托数量 则跳出当前循环
                if (remainGmv.subtract(depthGmv).compareTo(BigDecimal.ZERO) <= 0) {
                    thisGmv = remainGmv;
                    c = 1;
                } else {
                    thisGmv = depthGmv;
                }
                remainGmv = remainGmv.subtract(depthGmv);
                SubOrderModel model = new SubOrderModel();
                model.setExchangeNo(voDetail.getExchangeNo());
                model.setVolume(thisGmv.setScale(10, BigDecimal.ROUND_HALF_UP));
                model.setPrice(delegate.getPrice());
                //判断集合中是否存在相同的交易所 如果存在则把数量相加
                int has = 0;
                for (String key : subOrderModels.keySet()) {
                    SubOrderModel m = subOrderModels.get(key);
                    if (key.equals(model.getExchangeNo())) {
                        //四舍五入
                        m.setVolume(m.getVolume().add(thisGmv).setScale(10, BigDecimal.ROUND_HALF_UP));
                        has = 1;
                    }
                }
                //不存在 则put
                if (has == 0) {
                    subOrderModels.put(voDetail.getExchangeNo(), model);
                }

                if (c == 1) {
                    break;
                }
            }
            if (c == 1) {
                break;
            }
        }
        //执行下单操作
        this.doOrders(subOrderModels, delegate, symbol, accountInfoModels);
    }

    @Override
    public void separateOrderByMarketPriceOfSellOut(Depth depth, MainDelegation delegate, List<MotherAccountInfoModel> accountInfoModels) {
        //市价买入时根据交易额来拆单
        List<VoPrModel> voPrModels;
        if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
            //买入 - 解析卖档数据
            voPrModels = this.resolveDepth(depth, "ask");
        } else {
            //卖出 - 解析买档数据
            voPrModels = this.resolveDepth(depth, "bid");
        }

        String symbol = delegate.getCoinCode() + "_" + delegate.getCoinCurrency().toLowerCase();
        Map<String, SubOrderModel> subOrderModels = new HashMap<>();
        List<VoDetail> voDetails;
        //剩余数量
        BigDecimal remainAmount = delegate.getAmount();
        for (VoPrModel prModel : voPrModels) {
            voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
            //是否跳出循环标记
            int c = 0;
            for (VoDetail voDetail : voDetails) {
                //声明本次下单数量
                BigDecimal thisAmount;
                //剩余数量小于等于0 表明已全部满足用户的委托数量 则跳出当前循环
                if (remainAmount.subtract(voDetail.getVolume()).compareTo(BigDecimal.ZERO) <= 0) {
                    thisAmount = remainAmount;
                    c = 1;
                } else {
                    thisAmount = voDetail.getVolume();
                }
                remainAmount = remainAmount.subtract(voDetail.getVolume());
                SubOrderModel model = new SubOrderModel();
                model.setExchangeNo(voDetail.getExchangeNo());
                model.setVolume(thisAmount.setScale(10, BigDecimal.ROUND_HALF_UP));
                model.setPrice(delegate.getPrice());
                //判断集合中是否存在相同的交易所 如果存在则把数量相加
                int has = 0;
                for (String key : subOrderModels.keySet()) {
                    SubOrderModel m = subOrderModels.get(key);
                    if (key.equals(model.getExchangeNo())) {
                        //四舍五入
                        m.setVolume(m.getVolume().add(thisAmount).setScale(10, BigDecimal.ROUND_HALF_UP));
                        has = 1;
                    }
                }
                //不存在 则put
                if (has == 0) {
                    subOrderModels.put(voDetail.getExchangeNo(), model);
                }

                if (c == 1) {
                    break;
                }
            }
            if (c == 1) {
                break;
            }
        }
        //执行下单操作
        this.doOrders(subOrderModels, delegate, symbol, accountInfoModels);
    }

    public void separateOrderByLimitPriceOfBuyIn(Depth depth, MainDelegation delegate, List<MotherAccountInfoModel> accountInfoModels) {
        //判断委托方向
        List<VoPrModel> voPrModels;
        if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
            //买入 - 解析卖档数据
            voPrModels = this.resolveDepth(depth, "ask");
        } else {
            //卖出 - 解析买档数据
            voPrModels = this.resolveDepth(depth, "bid");
        }
        //卖挡深度
        int d = 0;
        //通过价格判断需要取卖几档的数据
        for (VoPrModel prModel : voPrModels) {
            if (delegate.getPrice().compareTo(prModel.getPrice()) >= 0) {
                d++;
            }
        }
        List<VoDetail> voDetails;
        String symbol = delegate.getCoinCode() + "_" + delegate.getCoinCurrency().toLowerCase();
        Map<String, SubOrderModel> subOrderModels = new HashMap<>();
        //剩余数量
        BigDecimal remainVolume = delegate.getAmount();
        //没有满足的卖挡数据 则直接生成一笔订单 发给卖1挡数量最高的那个交易所
        if (d == 0) {
            VoPrModel prModel = voPrModels.get(0);
            voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
            VoDetail voDetail = voDetails.get(0);
            SubOrderModel model = new SubOrderModel();
            model.setExchangeNo(voDetail.getExchangeNo());
            model.setVolume(remainVolume);
            model.setPrice(delegate.getPrice());
            subOrderModels.put(voDetail.getExchangeNo(), model);
        } else {
            for (int i = 0; i < d; i++) {
                VoPrModel prModel = voPrModels.get(i);
                voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
                //是否跳出循环标记 1跳出 0继续
                int c = 0;
                for (VoDetail voDetail : voDetails) {
                    //声明本次下单数量
                    BigDecimal thisVolume;
                    //剩余数量小于等于0 表明已全部满足用户的委托数量 则跳出当前循环
                    if (remainVolume.subtract(voDetail.getVolume()).compareTo(BigDecimal.ZERO) <= 0) {
                        thisVolume = remainVolume;
                        c = 1;
                    } else {
                        thisVolume = voDetail.getVolume();
                    }
                    remainVolume = remainVolume.subtract(voDetail.getVolume());
                    SubOrderModel model = new SubOrderModel();
                    model.setExchangeNo(voDetail.getExchangeNo());
                    model.setVolume(thisVolume);
                    model.setPrice(delegate.getPrice());
                    //判断集合中是否存在相同的交易所 如果存在则把数量相加
                    int has = 0;
                    for (String key : subOrderModels.keySet()) {
                        SubOrderModel m = subOrderModels.get(key);
                        if (key.equals(model.getExchangeNo())) {
                            m.setVolume(m.getVolume().add(thisVolume));
                            has = 1;
                        }
                    }
                    //不存在 则put
                    if (has == 0) {
                        subOrderModels.put(voDetail.getExchangeNo(), model);
                    }
                    if (c == 1) {
                        break;
                    }
                }
                if (c == 1) {
                    break;
                }
            }
            //全部吃完 发现还有剩余数量，则取最高档卖挡中最高数量的交易所 加上剩余数量
            if (remainVolume.compareTo(BigDecimal.ZERO) > 0) {
                VoPrModel prModel = voPrModels.get(d - 1);
                voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
                String exchangeNo = voDetails.get(0).getExchangeNo();
                for (String key : subOrderModels.keySet()) {
                    SubOrderModel m = subOrderModels.get(key);
                    if (key.equals(exchangeNo)) {
                        m.setVolume(m.getVolume().add(remainVolume));
                        break;
                    }
                }
            }
        }

        //设置是否拆单标记
        if (subOrderModels.size() == 1) {
            delegate.setIsSplit(MainDelegation.DELFLAG.NO.code);
        } else {
            delegate.setIsSplit(MainDelegation.DELFLAG.YES.code);
        }
        mainDelegationService.updateIsSplitByMainNo(delegate.getIsSplit(), delegate.getDelegateNo());
        //执行下单操作
        this.doOrders(subOrderModels, delegate, symbol, accountInfoModels);
    }

    public void separateOrderByLimitPriceOfSellOut(Depth depth, MainDelegation delegate, List<MotherAccountInfoModel> accountInfoModels) {
        //判断委托方向
        List<VoPrModel> voPrModels;
        if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
            //买入 - 解析卖档数据
            voPrModels = this.resolveDepth(depth, "ask");
        } else {
            //卖出 - 解析买档数据
            voPrModels = this.resolveDepth(depth, "bid");
        }
        //卖挡深度
        int d = 0;
        //通过价格判断需要取卖几档的数据
        for (VoPrModel prModel : voPrModels) {
            if (delegate.getPrice().compareTo(prModel.getPrice()) <= 0) {
                d++;
            }
        }
        String symbol = delegate.getCoinCode() + "_" + delegate.getCoinCurrency().toLowerCase();
        Map<String, SubOrderModel> subOrderModels = new HashMap<>();
        List<VoDetail> voDetails;
        //剩余数量
        BigDecimal remainVolume = delegate.getAmount();
        if (d == 0) {
            VoPrModel prModel = voPrModels.get(0);
            voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
            VoDetail voDetail = voDetails.get(0);
            SubOrderModel model = new SubOrderModel();
            model.setExchangeNo(voDetail.getExchangeNo());
            model.setVolume(remainVolume);
            model.setPrice(delegate.getPrice());
            subOrderModels.put(voDetail.getExchangeNo(), model);
        } else {
            for (int i = 0; i < d; i++) {
                VoPrModel prModel = voPrModels.get(i);
                voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
                //是否跳出循环标记 1跳出 0继续
                int c = 0;
                for (VoDetail voDetail : voDetails) {
                    //声明本次下单数量
                    BigDecimal thisVolume;
                    //剩余数量小于等于0 表明已全部满足用户的委托数量 则跳出当前循环
                    if (remainVolume.subtract(voDetail.getVolume()).compareTo(BigDecimal.ZERO) <= 0) {
                        thisVolume = remainVolume;
                        c = 1;
                    } else {
                        thisVolume = voDetail.getVolume();
                    }
                    remainVolume = remainVolume.subtract(voDetail.getVolume());
                    SubOrderModel model = new SubOrderModel();
                    model.setExchangeNo(voDetail.getExchangeNo());
                    model.setVolume(thisVolume);
                    model.setPrice(TxUtils.removeRedundanceZeroString(delegate.getPrice()));
                    //判断集合中是否存在相同的交易所 如果存在则把数量相加
                    int has = 0;
                    for (String key : subOrderModels.keySet()) {
                        SubOrderModel m = subOrderModels.get(key);
                        if (key.equals(model.getExchangeNo())) {
                            m.setVolume(m.getVolume().add(thisVolume));
                            has = 1;
                        }
                    }
                    //不存在 则put
                    if (has == 0) {
                        subOrderModels.put(voDetail.getExchangeNo(), model);
                    }
                    if (c == 1) {
                        break;
                    }
                }
                if (c == 1) {
                    break;
                }
            }
            //全部吃完 发现还有剩余数量，则取最高档卖挡中最高数量的交易所 加上剩余数量
            if (remainVolume.compareTo(BigDecimal.ZERO) > 0) {
                VoPrModel prModel = voPrModels.get(d - 1);
                voDetails = this.getVolumeDetailByName(prModel.getName(), depth);
                String exchangeNo = voDetails.get(0).getExchangeNo();
                for (String key : subOrderModels.keySet()) {
                    SubOrderModel m = subOrderModels.get(key);
                    if (key.equals(exchangeNo)) {
                        m.setVolume(m.getVolume().add(remainVolume));
                        break;
                    }
                }
            }
        }

        //执行下单操作
        this.doOrders(subOrderModels, delegate, symbol, accountInfoModels);
    }

    /**
     * 循环调用接口下单
     *
     * @param subOrderModels    待下单信息
     * @param delegate          主委托
     * @param symbol            交易对
     * @param accountInfoModels 母账号列表
     */
    private void doOrders(Map<String, SubOrderModel> subOrderModels,
                          MainDelegation delegate,
                          String symbol,
                          List<MotherAccountInfoModel> accountInfoModels) throws BussinessException {
        int succeed = 0;
        if (subOrderModels.size() > 0) {
            delegate.setIsSplit(MainDelegation.DELFLAG.YES.code);
        } else {
            delegate.setIsSplit(MainDelegation.DELFLAG.NO.code);
        }
        for (String key : subOrderModels.keySet()) {
            SubOrderModel model = subOrderModels.get(key);
            //获取交易所母账号信息 这里可能发生异常 未获取到交易所母账号
            MotherAccountInfoModel account;
            try {
                account = this.getAccount(accountInfoModels, key);
            } catch (Exception e) {
                log.error("查找母账号[" + key + "]异常:{}", e.getMessage());
                delegate.setRemark(e.getMessage() + ":[" + key + "]");
                continue;
            }
            model.setAccount(account);
            String s;
            if (delegate.getStyle() == MainDelegation.STYLE.LIMITED.code) {
                //调用下单接口
                s = this.doOrderByLimited(delegate, model.getExchangeNo(), delegate.getPrice(), model.getVolume(),
                        symbol, account, delegate.getStyle());
            } else {
                s = this.doOrderByMarket(delegate, model.getExchangeNo(),
                        model.getVolume(), symbol, account, delegate.getStyle());

            }
            if (!"".equalsIgnoreCase(s)) {
                succeed++;
            }
        }
        //子委托成功单数=子委托委托单数 则更新主委托信息
        if (succeed == 0) {
            delegate.setState(MainDelegation.STATE.FAILED.code);
            txSyncMessageProducer.sendMainDelegateFailureTask(delegate);
            return;
        } else if (succeed == subOrderModels.size()) {
            delegate.setRemark("子委托全部委托成功");
        } else {
            delegate.setRemark("子委托部分委托成功");
        }

        delegate.setState(MainDelegation.STATE.COMMISSIONED_IN.code);
        delegate.setUpdator("用户:" + delegate.getUserId());
        delegate.setUpdateTime(new Date());
        mainDelegationService.updateByPrimaryKeySelective(delegate);
    }

    @Override
    public void singleQryOrder(MotherAccountInfoModel account, String symbol, String billNo) {
        //根据委托编号查询子委托信息 从缓存中获取
        SubDelegation sub = subDelegationService.getByBillNo(billNo);
        if (null == sub) {
            log.error("singleQryOrder No Record...");
            return;
        }
        if (sub.getState() == SubDelegation.STATE.REVOKED.code || sub.getState() == SubDelegation.STATE.FAILED.code ||
                sub.getState() == SubDelegation.STATE.DEAL.code || sub.getState() == SubDelegation.STATE.PART_OF_REVOKE.code) {
            log.error(sub.getBillNo() + ", The Record Was Handled");
            return;
        }
        String entrustNo = sub.getEntrustNo();
        if (StrUtil.isNotBlank(entrustNo)) {
            String json = "";
            //子委托状态是【交易中】 执行查询 否则不查询
            String exchangeNo = sub.getExchange();
            //获取子委托的交易所编号
            if (exchangeNo.equals(ExchangeEnum.BIAN.getCode())) {
                //调用币安接口、生成子委托
                json = tradeBianInterface.orderQry(account.getApiKey(), account.getApiSecret(), symbol, entrustNo);
                log.info("BIAN 查询响应信息:{}" ,json);
            } else if (exchangeNo.equals(ExchangeEnum.HUOBI.getCode())) {
                //调用火币接口、生成子委托
                json = tradeHuoBiInterface.orderQry(account.getApiKey(), account.getApiSecret(),
                        symbol, entrustNo);
                log.info("HUOBI查询响应信息:{}" ,json);
            } else if (exchangeNo.equals(ExchangeEnum.BITFINEX.getCode())) {
                //调用bitfinex接口、生成子委托
                json = tradeBitFinexInterface.orderQry(account.getApiKey(), account.getApiSecret(),
                        symbol, entrustNo);
                log.info("BitFinex查询响应信息:{}" ,json);
            }
            JSONObject jsonObject = JSON.parseObject(json);
            if (Integer.parseInt(jsonObject.getString("code")) == ResultEntity.SUCCESS) {
                OrderQryRes result = JSON.parseObject(jsonObject.getString("data"), OrderQryRes.class);
                String status = result.getEntrust_status();
                //部成待撤-4 //部成-7 //已成交-8  这几种状态都有成交的情况所以按照同一种逻辑处理
                if ("4".equalsIgnoreCase(status) || "7".equalsIgnoreCase(status) || "8".equalsIgnoreCase(status)) {
                    //放入队列
                    txSyncMessageProducer.sendSubDelegateSuccessTask(sub, result, entrustNo);

                    //撤单成功  //部撤-5  已撤-6
                } else if ("5".equalsIgnoreCase(status) || "6".equalsIgnoreCase(status)) {
                    if ("5".equalsIgnoreCase(status)) {
                        sub.setState(SubDelegation.STATE.PART_OF_REVOKE.code);
                    } else if ("6".equalsIgnoreCase(status)) {
                        sub.setState(SubDelegation.STATE.REVOKED.code);
                    }
                    txSyncMessageProducer.sendRevokeDelegateFailureTask(sub,"用户撤单");
                } else if ("9".equalsIgnoreCase(status)) {
                    //1.子委托信息更新 2.主委托信息更新 3.解冻资产
                    String msg = "交易所返回废单";
                    txSyncMessageProducer.sendSubDelegateFailureTask(sub, msg);
                }
            } else {
                String msg = jsonObject.getString("msg");
                txSyncMessageProducer.sendSubDelegateFailureTask(sub, msg);
            }
        }
    }

    @Override
    public String revoke(List<SubDelegation> subDelegations, String symbol) {
        int count = 0;
        String resultMsg;
        for (SubDelegation sub : subDelegations) {
            String json = null;
            String exchangeNo = sub.getExchange();
            //获取母账号信息
            MotherAccountInfoModel account = exchangeInterface.findMotherAccount(exchangeNo, sub.getCoinCurrency());
            //获取子委托的交易所编号
            if (exchangeNo.equals(ExchangeEnum.BIAN.getCode())) {
                //调用币安接口、撤单操作
                json = tradeBianInterface.withdraw(account.getApiKey(), account.getApiSecret(), symbol, sub.getEntrustNo());
            } else if (exchangeNo.equals(ExchangeEnum.HUOBI.getCode())) {
                //调用火币接口、撤单操作
                json = tradeHuoBiInterface.withdraw(account.getApiKey(), account.getApiSecret(), symbol, sub.getEntrustNo());
            } else if (exchangeNo.equals(ExchangeEnum.BITFINEX.getCode())) {
                //调用bitfinex接口、撤单操作
                json = tradeBitFinexInterface.withdraw(account.getApiKey(), account.getApiSecret(), symbol, sub.getEntrustNo());
            }
            JSONObject jsonObject = JSON.parseObject(json);
            //撤单请求成功(具体撤单结果 需要通过同步模块才能知道结果)
            if (Integer.parseInt(jsonObject.getString("code")) == ResultEntity.SUCCESS) {
                //更新子委托状态
                sub.setState(SubDelegation.STATE.REVOKING.code);
                sub.setInfo("用户手动撤单中...");
                subDelegationService.updateByPrimaryKeySelective(sub);
                count++;
            } else {
                //更新子委托状态
                sub.setInfo("用户手动撤单失败,失败原因:" + jsonObject.getString("msg"));
                subDelegationService.updateByPrimaryKeySelective(sub);
//                resultMsg = jsonObject.getString("msg");
            }
        }
        if (count == subDelegations.size()) {
            resultMsg = "全部撤回请求成功";
        } else {
            resultMsg = "部分撤回请求成功";
        }
        mainDelegationService.updateStateByMainNo(MainDelegation.STATE.COMMISSIONED_IN.code, resultMsg, subDelegations.get(0).getMainDelegateNo());
        return resultMsg;
    }

    private MotherAccountInfoModel getAccount(List<MotherAccountInfoModel> accountInfoModels, String exNo) {
        MotherAccountInfoModel model = null;
        for (MotherAccountInfoModel m : accountInfoModels) {
            if (exNo.equals(m.getExNo())) {
                model = m;
            }
        }
        if (null == model) {
            throw new BussinessException("未获取到满足条件的母账号");
        }
        return model;
    }

    /**
     * 通过档位名称获取买卖量明细 并按照降序排列
     *
     * @param name  档位名称 如bid_volume1、ask_volume2等
     * @param depth 行情数据
     * @return List<VoDetail>
     */
    private List<VoDetail> getVolumeDetailByName(String name, Depth depth) {
        String volume_detail = depth.getVolume_detail();
        List<VoDetail> voDetails = new ArrayList<>();
        Map<String, JSONObject> detailMap = JSON.parseObject(volume_detail, HashMap.class);
        for (Map.Entry<String, JSONObject> objectEntry : detailMap.entrySet()) {
            if (objectEntry.getKey().equals(name)) {
                JSONObject bid_volume11 = objectEntry.getValue();
                for (Map.Entry<String, Object> entry : bid_volume11.entrySet()) {
                    VoDetail voDetail = new VoDetail();
                    voDetail.setExchangeNo(entry.getKey());
                    voDetail.setVolume(String.valueOf(entry.getValue()));
                    //判断数量=0的交易所
                    if (new BigDecimal(String.valueOf(entry.getValue())).compareTo(BigDecimal.ZERO) > 0) {
                        voDetails.add(voDetail);
                    }
                }
                voDetails.sort(new VolumeComparator());
            }
        }
        return voDetails;
    }

    /**
     * 解析买卖档数据
     *
     * @param depth 数据
     * @param type  买or卖
     * @return List<VoPrModel>
     */
    private List<VoPrModel> resolveDepth(Depth depth, String type) {
        List<VoPrModel> voPrModels = new ArrayList<>();
        switch (type) {
            case "ask":
                VoPrModel model1 = new VoPrModel();
                model1.setName("ask_volume1");
                model1.setPrice(depth.getAsk_price1());
                model1.setVolume(depth.getAsk_volume1());
                voPrModels.add(model1);
                VoPrModel model2 = new VoPrModel();
                model2.setName("ask_volume2");
                model2.setPrice(depth.getAsk_price2());
                model2.setVolume(depth.getAsk_volume2());
                voPrModels.add(model2);
                VoPrModel model3 = new VoPrModel();
                model3.setName("ask_volume3");
                model3.setPrice(depth.getAsk_price3());
                model3.setVolume(depth.getAsk_volume3());
                voPrModels.add(model3);
                VoPrModel model4 = new VoPrModel();
                model4.setName("ask_volume4");
                model4.setPrice(depth.getAsk_price4());
                model4.setVolume(depth.getAsk_volume4());
                voPrModels.add(model4);
                VoPrModel model5 = new VoPrModel();
                model5.setName("ask_volume5");
                model5.setPrice(depth.getAsk_price5());
                model5.setVolume(depth.getAsk_volume5());
                voPrModels.add(model5);
                VoPrModel model6 = new VoPrModel();
                model6.setName("ask_volume6");
                model6.setPrice(depth.getAsk_price6());
                model6.setVolume(depth.getAsk_volume6());
                voPrModels.add(model6);
                VoPrModel model7 = new VoPrModel();
                model7.setName("ask_volume7");
                model7.setPrice(depth.getAsk_price7());
                model7.setVolume(depth.getAsk_volume7());
                voPrModels.add(model7);
                VoPrModel model8 = new VoPrModel();
                model8.setName("ask_volume8");
                model8.setPrice(depth.getAsk_price8());
                model8.setVolume(depth.getAsk_volume8());
                voPrModels.add(model8);
                VoPrModel model9 = new VoPrModel();
                model9.setName("ask_volume9");
                model9.setPrice(depth.getAsk_price9());
                model9.setVolume(depth.getAsk_volume9());
                voPrModels.add(model9);
                VoPrModel model10 = new VoPrModel();
                model10.setName("ask_volume10");
                model10.setPrice(depth.getAsk_price10());
                model10.setVolume(depth.getAsk_volume10());
                voPrModels.add(model10);
                break;
            case "bid":
                VoPrModel bidModel1 = new VoPrModel();
                bidModel1.setName("bid_volume1");
                bidModel1.setPrice(depth.getBid_price1());
                bidModel1.setVolume(depth.getBid_volume1());
                voPrModels.add(bidModel1);
                VoPrModel bidModel2 = new VoPrModel();
                bidModel2.setName("bid_volume2");
                bidModel2.setPrice(depth.getBid_price2());
                bidModel2.setVolume(depth.getBid_volume2());
                voPrModels.add(bidModel2);
                VoPrModel bidModel3 = new VoPrModel();
                bidModel3.setName("bid_volume3");
                bidModel3.setPrice(depth.getBid_price3());
                bidModel3.setVolume(depth.getBid_volume3());
                voPrModels.add(bidModel3);
                VoPrModel bidModel4 = new VoPrModel();
                bidModel4.setName("bid_volume4");
                bidModel4.setPrice(depth.getBid_price4());
                bidModel4.setVolume(depth.getBid_volume4());
                voPrModels.add(bidModel4);
                VoPrModel bidModel5 = new VoPrModel();
                bidModel5.setName("bid_volume5");
                bidModel5.setPrice(depth.getBid_price5());
                bidModel5.setVolume(depth.getBid_volume5());
                voPrModels.add(bidModel5);
                VoPrModel bidModel6 = new VoPrModel();
                bidModel6.setName("bid_volume6");
                bidModel6.setPrice(depth.getBid_price6());
                bidModel6.setVolume(depth.getBid_volume6());
                voPrModels.add(bidModel6);
                VoPrModel bidModel7 = new VoPrModel();
                bidModel7.setName("bid_volume7");
                bidModel7.setPrice(depth.getBid_price7());
                bidModel7.setVolume(depth.getBid_volume7());
                voPrModels.add(bidModel7);
                VoPrModel bidModel8 = new VoPrModel();
                bidModel8.setName("bid_volume8");
                bidModel8.setPrice(depth.getBid_price8());
                bidModel8.setVolume(depth.getBid_volume8());
                voPrModels.add(bidModel8);
                VoPrModel bidModel9 = new VoPrModel();
                bidModel9.setName("bid_volume9");
                bidModel9.setPrice(depth.getBid_price9());
                bidModel9.setVolume(depth.getBid_volume9());
                voPrModels.add(bidModel9);
                VoPrModel bidModel10 = new VoPrModel();
                bidModel10.setName("bid_volume10");
                bidModel10.setPrice(depth.getBid_price10());
                bidModel10.setVolume(depth.getBid_volume10());
                voPrModels.add(bidModel10);
                break;
        }
        return voPrModels;
    }
}
