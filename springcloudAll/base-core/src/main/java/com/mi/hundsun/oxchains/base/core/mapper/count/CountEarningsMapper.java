package com.mi.hundsun.oxchains.base.core.mapper.count;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.count.CountEarningsModel;
import com.mi.hundsun.oxchains.base.core.po.count.CountEarnings;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 收益汇总相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-24 10:49:34
 */
public interface CountEarningsMapper extends GenericMapper<CountEarnings,Integer> {

    @Select("SELECT t.* FROM count_earnings t WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<CountEarningsModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM count_earnings t WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);
}
