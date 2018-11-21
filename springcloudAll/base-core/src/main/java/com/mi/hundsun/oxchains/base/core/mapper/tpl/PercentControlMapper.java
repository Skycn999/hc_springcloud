package com.mi.hundsun.oxchains.base.core.mapper.tpl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.tpl.PercentControlModel;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 百分比风控模板相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-13 10:28:11
 */
public interface PercentControlMapper extends GenericMapper< PercentControl,Integer> {

    @Select("SELECT t.* FROM tpl_percent_control t WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<PercentControlModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM tpl_percent_control t WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);
}
