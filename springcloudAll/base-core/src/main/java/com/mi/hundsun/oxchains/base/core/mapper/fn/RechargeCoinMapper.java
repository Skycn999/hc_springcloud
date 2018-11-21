package com.mi.hundsun.oxchains.base.core.mapper.fn;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.fn.RechargeCoinModel;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 充币管理相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-15 03:52:26
 */
public interface RechargeCoinMapper extends GenericMapper< RechargeCoin,Integer> {

    @Select("SELECT t.*,u.email AS 'u.email',u.realname AS 'u.realname',u.mobile AS 'u.mobile',u.id_no AS 'u.idNo',u.id_type AS 'u.idType' FROM fn_recharge_coin t LEFT JOIN users u ON u.id = t.user_id WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<RechargeCoinModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM fn_recharge_coin t LEFT JOIN users u  ON u.id = t.user_id WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM fn_recharge_coin WHERE 1=1 ${whereSql} ${sortSql}")
    long getRechargeCoinCountByUserId(Map<String, Object> params);

    @Select("SELECT * FROM fn_recharge_coin WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<RechargeCoin> getRechargeCoinListByUserId(Map<String, Object> params);


}
