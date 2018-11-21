package com.mi.hundsun.oxchains.base.core.mapper.exchange;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccountInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 交易所母账号管理相关Dao接口<br>
 *
 * @author bin
 * @date 2018-04-16 05:59:15
 */
public interface MotherAccountInfoMapper extends GenericMapper<MotherAccountInfo, Integer> {


    @Select("SELECT t.*,ex.name AS 'ex.name' FROM ex_mother_account_info t LEFT JOIN exchange ex  ON ex.ex_no = t.ex_no WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize} ")
    List<MotherAccountInfoModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM ex_mother_account_info t LEFT JOIN exchange ex  ON ex.ex_no = t.ex_no WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    @Select(" SELECT MAX(a.ex_no) AS ex_no, MAX(a.account_email) AS account_email, MAX(a.api_secret) AS api_secret, " +
            " MAX(a.api_key) AS api_key, MAX(a.account_name) AS account_name, MAX(a.account_id) AS account_id, " +
            " MAX(t.coin_currency) AS coin_currency, MAX(t.available) AS available, MAX(t.freeze) AS freeze, MAX(t.total) AS total" +
            " FROM ex_mother_account_info t,ex_mother_account a " +
            " WHERE t.mother_account_id = a.id AND t.del_flag = 0 AND a.state = 1 AND t.coin_currency = #{code} " +
            " GROUP BY a.ex_no " +
            " ORDER BY available DESC")
    List<MotherAccountInfoModel> groupByCoinCode(@Param("code") String code);

    @Select(" SELECT a.ex_no,a.account_email,a.api_secret,a.api_key,a.account_name,a.account_id,a.account_no,t.coin_currency,t.available,t.freeze,t.total " +
            " FROM ex_mother_account_info t,ex_mother_account a " +
            " WHERE t.mother_account_id = a.id AND t.del_flag = 0 AND a.state = 1 AND a.ex_no = #{exchangeNo} " +
            " ORDER BY t.available DESC limit 1")
    MotherAccountInfoModel findByExchangeNo(@Param("exchangeNo") String exchangeNo);

    @Select(" SELECT a.ex_no,a.account_email,a.api_secret,a.api_key,a.account_name,a.account_no,a.account_id,t.coin_currency,t.available,t.freeze,t.total " +
            " FROM ex_mother_account_info t,ex_mother_account a " +
            " WHERE t.mother_account_id = a.id AND t.del_flag = 0 AND a.state = 1 AND t.coin_currency = #{code} AND a.ex_no = #{exchangeNo} " +
            " ORDER BY t.available DESC limit 1")
    MotherAccountInfoModel findByExchangeNoAndCoinCode(@Param("exchangeNo") String exchangeNo, @Param("code") String code);

    @Select(" SELECT a.ex_no,a.account_email,a.api_secret,a.api_key,a.account_name,a.account_no,a.account_id,t.coin_currency,t.available,t.freeze,t.total " +
            " FROM ex_mother_account_info t,ex_mother_account a " +
            " WHERE t.mother_account_id = a.id AND t.del_flag = 0 AND a.state = 1 AND a.account_name = #{accountName} " +
            " AND a.ex_no = #{exchangeNo} AND t.coin_currency = #{code}")
    MotherAccountInfoModel findByAccountNo(@Param("exchangeNo") String exchangeNo, @Param("accountName") String accountName, @Param("code") String code );

    @Select("SELECT a.ex_no,a.account_email,a.api_secret,a.api_key,a.account_name,a.account_no,a.account_id " +
            "FROM ex_mother_account a " +
            "WHERE a.state = 1 AND a.ex_no = #{exchangeNo} AND a.account_name = #{accountName}")
    List<MotherAccountInfoModel> findByAccountNameAndExNo(@Param("exchangeNo") String exchangeNo, @Param("accountName") String accountName);

    @Select("SELECT api_secret,a.api_key,a.ex_no FROM ex_mother_account a WHERE a.ex_no = #{exchangeNo} AND a.state = 1 AND a.del_flag = 0")
    List<MotherAccount> getApiInfoByExchange(@Param("exchangeNo") String exchangeNo);
}
