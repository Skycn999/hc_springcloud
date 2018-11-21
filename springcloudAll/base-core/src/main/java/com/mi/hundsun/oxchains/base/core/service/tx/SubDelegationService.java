package com.mi.hundsun.oxchains.base.core.service.tx;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交易管理-子委托管理业务相关Service接口<br>
 *
 * @author bin
 * @date 2018-04-13 09:54:58
 */
public interface SubDelegationService extends GenericService<Integer, SubDelegation> {
    /**
     * 根据主委托编号查询子委托信息
     *
     * @param delegateNo 主委托编号
     * @param userId     用户ID
     * @return 子委托记录
     */
    List<SubDelegation> getMySubsByMainNo(String delegateNo, Integer userId);


    /**
     * 查询子委托记录
     *
     * @param userId 用户id
     * @return 委托列表
     */
    Map<String, Object> getMySubDelegates(Integer userId, Integer direction, Integer pageNumber, Integer pageSize);

    /**
     * 限价委托 生成子委托
     * @param delegation  主委托
     * @param amount      委托量
     * @param price       委托价
     * @param exchangeNo  交易所
     * @param accountNo   母账号
     * @return 子委托
     */
    SubDelegation generateForLimited(MainDelegation delegation, BigDecimal amount, BigDecimal price, String exchangeNo,
                                     String accountNo);

    /**
     * 市价买入委托生成子委托
     * @param delegation  主委托
     * @param gmv         交易额
     * @param exchangeNo  交易所
     * @param accountNo   母账号信息
     * @return 子委托
     */
    SubDelegation generateForMarketBuyIn(MainDelegation delegation, BigDecimal gmv, String exchangeNo, String accountNo);

    /**
     * 市价卖出委托生成子委托
     * @param delegation  主委托
     * @param gmv         交易额
     * @param exchangeNo  交易所
     * @param accountNo   母账号信息
     * @return 子委托
     */
    SubDelegation generateForMarketSellOut(MainDelegation delegation, BigDecimal gmv, String exchangeNo, String accountNo);

    /**
     * 根据子委托编号查询
     *
     * @param entrustNo 子委托
     * @return SubDelegation
     */
    SubDelegation getByEntrustNo(String entrustNo);

    /**
     * 根据子委托系统编号查询
     *
     * @param billNo 子委托系统订单号
     * @return SubDelegation
     */
    SubDelegation getByBillNo(String billNo);

    /**
     * 获取交易中的子委托订单列表
     */
    List<SubDelegation> getTradingSubDelegates();

    int updateByStateForFailure(SubDelegation sub);

    /**
     * 查找子委托 通过主委托编号和用户id
     * @param delegateNo  主委托编号
     * @param userId      用户id
     * @return  List<SubDelegation>
     */
    List<SubDelegation> getSubDelegatesByMainDelegateNo(String delegateNo, Integer userId);
}
