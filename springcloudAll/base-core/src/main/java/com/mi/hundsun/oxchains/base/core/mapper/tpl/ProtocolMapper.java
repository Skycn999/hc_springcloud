package com.mi.hundsun.oxchains.base.core.mapper.tpl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.tpl.ProtocolModel;
import com.mi.hundsun.oxchains.base.core.po.tpl.Protocol;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 各种协议模板相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-23 09:50:40
 */
public interface ProtocolMapper extends GenericMapper< Protocol,Integer> {

    @Select("SELECT t.* FROM tpl_protocol t WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<ProtocolModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM tpl_protocol t WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);
}
