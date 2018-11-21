package com.mi.hundsun.oxchains.base.core.service.tx;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户资产持仓表业务相关Service接口<br>
 *
 * @author bin
 * @ClassName: AccountService
 * @date 2018-04-13 09:54:58
 */
public interface AccountService extends GenericService<Integer, Account> {

    /**
     * 充币更新资产记录
     *
     * @param account 资产
     * @throws Exception ex
     */
    void updateByRecharge(Account account) throws Exception;

    /**
     * 提币不通过解冻资产
     *
     * @param mentionCoin 提币记录
     * @throws BussinessException ex
     */
    void mentionCoinNoPass(MentionCoin mentionCoin) throws BussinessException;


    /**
     * 提币录入成功扣除资产
     *
     * @param mentionCoin 提币记录
     * @throws BussinessException ex
     */
    void mentionCoinSuccess(MentionCoin mentionCoin) throws BussinessException;

    /**
     * 获取用户账户可用资产列表
     *
     * @param userId  用户ID
     * @param isMerge 是否合并
     * @return List<Account>
     */
    List<Account> getAvailMentionCurrency(Integer userId, Integer isMerge);

    /**
     * 通过币种获取和用户id获取资产信息
     *
     * @param userId   用户id
     * @param coinCode 币种代码
     * @return Account
     */
    Account getSumByCoinCode(Integer userId, String coinCode);

    int insertList(List<Account> accounts);

    /**
     * 执行提币资金操作(冻结资产、新增资产变更流水)
     *
     * @param userId     用户id
     * @param code       币种
     * @param amount     数量
     * @param serviceFee 手续费
     */
    void doMentionCoin(Integer userId, String code, String amount, String serviceFee) throws BussinessException;

    /**
     * 冻结可用
     *
     * @param gmv          冻结数量
     * @param coinCurrency 币种
     * @param userId       用户ID
     */
    int freeze(BigDecimal gmv, String coinCurrency, Integer userId);

    /**
     * 冻结可用区分交易所
     * @param gmv          冻结数量
     * @param coinCurrency 币种
     * @param userId       用户ID
     * @param exchangeNo   交易所编号
     */
    int freeze(BigDecimal gmv, String coinCurrency, Integer userId, String exchangeNo);

    /**
     * 扣除冻结
     * @param amount   扣除数量
     * @param coinCode 币种
     * @param userId   用户id
     */
    int deductFreezen(BigDecimal amount, String coinCode, Integer userId);

    /**
     * 扣除冻结 区分交易所
     * @param amount     扣除数量
     * @param coinCode   币种
     * @param userId     用户id
     * @param exchangeNo 交易所编号
     */
    int deductFreezen(BigDecimal amount, String coinCode, Integer userId, String exchangeNo);

    /**
     * 增加持仓 区分交易所
     * @param amount     数量
     * @param coinCode   币种
     * @param userId     用户id
     * @param exchangeNo 交易所编号
     */
    int addPosition(BigDecimal amount, String coinCode, Integer userId, String exchangeNo);

    /**
     * 市价买入处理资金并生成委托记录
     * @param userId       用户id
     * @param gmv          交易额
     * @param buyFeeScale  手续费比例
     * @param currencyPair 交易对
     * @return 主委托
     */
    MainDelegation handleAccountByMarketBuyIn(Integer userId, BigDecimal gmv, BigDecimal buyFeeScale,
                                              String currencyPair, String exchangeNo);

    /**
     * 市价买入处理资金并生成委托记录
     * @param userId       用户id
     * @param gmv          交易额
     * @param sellFeeScale 手续费比例
     * @param currencyPair 交易对
     * @param exchangeNo   交易所编号
     * @param accountNo    交易所母账号
     * @return 主委托
     */
    MainDelegation handleAccountByMarketSellOut(Integer userId, BigDecimal gmv, BigDecimal sellFeeScale,
                                                String currencyPair, String exchangeNo, String accountNo);

    /**
     * 限价买入处理资金并生成主委托记录
     *
     * @param userId       用户id
     * @param buyPrice     买入价
     * @param buyAmount    买入量
     * @param feeScale     手续费比例
     * @param currencyPair 交易对
     * @return 主委托记录
     */
    MainDelegation handleAccountByLimitedBuyIn(Integer userId, BigDecimal buyPrice, BigDecimal buyAmount,
                                               BigDecimal feeScale, String currencyPair, String exchangeNo);

    /**
     *
     * @param userId       用户id
     * @param buyPrice     委托价
     * @param buyAmount    卖出量
     * @param sellFeeScale 手续费费率
     * @param currencyPair 交易对
     * @param exchangeNo   选择的交易所编号
     * @return 主委托
     */
    MainDelegation handleAccountByLimitedSellOut(Integer userId, BigDecimal buyPrice, BigDecimal buyAmount,
                                                 BigDecimal sellFeeScale, String currencyPair, String exchangeNo);

}
