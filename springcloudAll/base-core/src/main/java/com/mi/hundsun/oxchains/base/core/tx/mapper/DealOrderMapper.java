package com.mi.hundsun.oxchains.base.core.tx.mapper;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.tx.model.CountDealOrderModel;
import com.mi.hundsun.oxchains.base.core.tx.model.DealOrderModel;
import com.mi.hundsun.oxchains.base.core.tx.po.DealOrder;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 交易管理-成交订单相关Dao接口<br>
 *
 * @author fengting
 * @date 2018-04-13 09:54:58
 */
public interface DealOrderMapper extends GenericMapper<DealOrder, Integer> {

    @Select("SELECT t.*,subDel.main_delegate_no AS 'subDel.mainDelegateNo',subDel.state AS 'subDel.state',subDel.amount AS 'subDel.amount',subDel.price AS 'subDel.price',subDel.exchange AS 'subDel.exchange',subDel.gmv AS 'subDel.gmv',subDel.style AS 'subDel.style',subDel.create_time AS 'subDel.createTime',mainDel.origin AS 'mainDel.origin'" +
            " FROM tx_deal_order t LEFT JOIN tx_sub_delegation subDel ON subDel.entrust_no = t.entrust_no LEFT JOIN tx_main_delegation mainDel ON mainDel.delegate_no = subDel.main_delegate_no WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<DealOrderModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM tx_deal_order t LEFT JOIN tx_sub_delegation subDel ON subDel.entrust_no = t.entrust_no LEFT JOIN tx_main_delegation mainDel ON mainDel.delegate_no = subDel.main_delegate_no WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    @Select("SELECT * FROM tx_deal_order WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<DealOrder> selectByPage(Map<String, Object> params);

    @Select("SELECT COUNT(1) FROM tx_deal_order WHERE 1=1 ${whereSql}")
    long countBySqlPage(Map<String, Object> params);

    @Select(" SELECT count(1) as totalTransactions, SUM(w.amount) AS amount, SUM(w.gmv) AS gmv, w.exchange, " +
            "DATE_FORMAT(w.create_time,'%y-%m-%d') AS create_time, w.coin_code,w.coin_currency " +
            " FROM tx_deal_order w " +
            " WHERE DATE_FORMAT(w.create_time,'%y-%m-%d') = DATE_FORMAT(NOW(),'%y-%m-%d') " +
            " GROUP BY w.exchange")
    List<CountDealOrderModel> countGroupByExchange();

    @Select(" SELECT count(1) as totalTransactions, SUM(w.amount) AS amount, SUM(w.gmv) AS gmv, w.exchange, " +
            "DATE_FORMAT(w.create_time,'%y-%m-%d') AS create_time, w.coin_code, w.coin_currency " +
            " FROM tx_deal_order w " +
            " WHERE w.del_flag = 0 AND DATE_FORMAT(w.create_time,'%y-%m-%d') = DATE_FORMAT(NOW(),'%y-%m-%d') " +
            " GROUP BY w.coin_code,coin_currency")
    List<CountDealOrderModel> countGroupByCode();

    @Select(" SELECT SUM(w.service_fee) AS serviceFee, SUM(w.plat_fee) AS platFee, SUM(w.gmv) AS gmv, " +
            " DATE_FORMAT(w.create_time,'%y-%m-%d') AS create_time " +
            " FROM tx_deal_order w " +
            " WHERE DATE_FORMAT(w.create_time,'%y-%m-%d') = DATE_FORMAT(NOW(),'%y-%m-%d')")
    List<CountDealOrderModel> countServiceFeeByCreateTime();

    @Select("SELECT count(1) as numbers, w.direction FROM tx_deal_order w WHERE w.del_flag = 0 AND " +
            "DATE_FORMAT(w.create_time,'%y-%m-%d') = DATE_FORMAT(NOW(),'%y-%m-%d') GROUP BY w.direction")
    List<CountDealOrderModel> countDirection();

}
