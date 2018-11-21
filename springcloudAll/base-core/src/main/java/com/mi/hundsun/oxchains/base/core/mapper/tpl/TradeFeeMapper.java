package com.mi.hundsun.oxchains.base.core.mapper.tpl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.tpl.TradeFeeModel;
import com.mi.hundsun.oxchains.base.core.po.tpl.TradeFee;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 交易手续费率模板相关Dao接口<br>
 *
 * @author bin
 * @date   2018-05-16 03:26:27
 */
public interface TradeFeeMapper extends GenericMapper<TradeFee,Integer> {

    @Select("SELECT t.* FROM tpl_trade_fee t WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<TradeFeeModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM tpl_trade_fee t WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);
}
