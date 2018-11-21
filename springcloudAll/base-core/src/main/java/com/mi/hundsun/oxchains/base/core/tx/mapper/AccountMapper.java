package com.mi.hundsun.oxchains.base.core.tx.mapper;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户资产持仓表相关Dao接口<br>
 *
 * @author fengting
 * @date 2018-04-13 09:54:58
 */
public interface AccountMapper extends GenericMapper<Account, Integer> {

    @Select("SELECT * FROM tx_account t WHERE 1=1 ${whereSql} ${sortSql} ")
    List<Account> getAvailMentionCurrencyNoMerge(Map<String, Object> params);

    @Select(" SELECT t.total,t.available, t.freeze , t.coin_code , t.exchange_no" +
            " FROM tx_account t WHERE 1=1 ${whereSql} ${sortSql} ")
    List<Account> getAvailMentionCurrencyMerge(Map<String, Object> params);

    @Select(" SELECT SUM(t.total) AS total, SUM(t.available) AS available, SUM(t.freeze) AS freeze " +
            " FROM tx_account t " +
            " WHERE t.coin_code = #{coinCode} AND t.user_id = #{userId} AND t.del_flag = 0 " +
            " GROUP BY t.coin_code")
    Account getSumByCoinCode(@Param("userId") Integer userId, @Param("coinCode") String coinCode);

    @Update(" UPDATE tx_account t SET t.available = t.available - #{amount}," +
            " t.freeze = t.freeze + #{amount},t.update_time = now() " +
            " WHERE t.coin_code = #{coinCode} AND t.user_id = #{userId} AND t.total - #{amount} >= 0 AND t.available - #{amount} >= 0")
    int freeze(@Param("amount") BigDecimal amount, @Param("coinCode") String coinCode, @Param("userId") Integer userId);

    @Update(" UPDATE tx_account t SET t.freeze = t.freeze - #{amount}, t.total = t.total - #{amount}, t.update_time = now() " +
            " WHERE t.coin_code = #{coinCode} AND t.user_id = #{userId} AND t.freeze - #{amount} >=0 ")
    int deductFreezen(@Param("amount") BigDecimal amount, @Param("coinCode") String coinCode, @Param("userId") Integer userId);

    @Update(" UPDATE tx_account t SET t.total = t.total - #{amount}, t.freeze = t.freeze - #{amount}, t.update_time = now() " +
            " WHERE t.coin_code = #{coinCode} AND t.user_id = #{userId} AND t.freeze - #{amount} >=0 AND t.exchange_no = #{exchangeNo} ")
    int deductFreezenByExchangeNo(@Param("amount") BigDecimal amount, @Param("coinCode") String coinCode, @Param("userId") Integer userId,
                                  @Param("exchangeNo") String exchangeNo);

    @Update(" UPDATE tx_account t SET t.total = t.total + #{amount}, t.available = t.available + #{amount}, t.update_time = now() " +
            " WHERE t.coin_code = #{coinCode} AND t.user_id = #{userId} ")
    int addPositionNoExchange(@Param("amount") BigDecimal amount, @Param("coinCode") String coinCode, @Param("userId") Integer userId);

    @Update(" UPDATE tx_account t SET t.total = t.total + #{amount}, t.available = t.available + #{amount}, t.update_time = now() " +
            " WHERE t.coin_code = #{coinCode} AND t.user_id = #{userId} AND t.exchange_no = #{exchangeNo} ")
    int addPosition(@Param("amount") BigDecimal amount, @Param("coinCode") String coinCode, @Param("userId") Integer userId, @Param("exchangeNo") String exchangeNo);

    @Update(" UPDATE tx_account t SET t.available = t.available - #{amount},t.freeze = t.freeze + #{amount},t.update_time = now() " +
            " WHERE t.coin_code = #{coinCode} AND t.user_id = #{userId} AND t.total >= #{amount} AND t.available >= #{amount}")
    int doMentionCoin(@Param("amount") BigDecimal amount, @Param("coinCode") String coinCode, @Param("userId") Integer userId);

    @Update(" UPDATE tx_account t SET t.available = t.available - #{amount}, t.freeze = t.freeze + #{amount},t.update_time = now() " +
            " WHERE t.coin_code = #{coinCode} AND t.user_id = #{userId} AND t.exchange_no = #{exchangeNo} AND t.total - #{amount} >=0 " +
            " AND t.available - #{amount} >= 0")
    int freezeForSmallCurrency(@Param("amount") BigDecimal amount, @Param("coinCode") String coinCode,
                               @Param("userId") Integer userId, @Param("exchangeNo") String exchangeNo);
}
