package com.mi.hundsun.oxchains.base.core.mapper.exchange;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountModel;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
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
public interface MotherAccountMapper extends GenericMapper<MotherAccount, Integer> {

    @Select("SELECT t.*,ex.name AS 'ex.name' FROM ex_mother_account t LEFT JOIN exchange ex  ON ex.ex_no = t.ex_no WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize} ")
    List<MotherAccountModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1)  FROM ex_mother_account t LEFT JOIN exchange ex  ON ex.ex_no = t.ex_no WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);


}
