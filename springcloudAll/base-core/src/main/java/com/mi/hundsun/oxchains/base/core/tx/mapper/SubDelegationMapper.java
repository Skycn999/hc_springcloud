package com.mi.hundsun.oxchains.base.core.tx.mapper;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 交易管理-子委托管理相关Dao接口<br>
 *
 * @author fengting
 * @date 2018-04-13 09:54:58
 */
public interface SubDelegationMapper extends GenericMapper<SubDelegation, Integer> {


    @Select("SELECT * FROM tx_sub_delegation t WHERE t.bill_no = #{billNo}")
    SubDelegation selectByBillNo(@Param("billNo") String billNo);

    @Select("SELECT * FROM tx_sub_delegation WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<SubDelegation> selectByPage(Map<String, Object> params);

    @Select("SELECT COUNT(1) FROM tx_sub_delegation WHERE 1=1 ${whereSql}")
    long countBySqlPage(Map<String, Object> params);

    /**
     * TRADING("交易中",2),
     * REVOKING("撤单中",3),
     * PART_OF_REVOKE("部分成交",6)
     */
    @Select("SELECT * FROM tx_sub_delegation t WHERE t.state in (1,2,3,6) AND t.del_flag = 0")
    List<SubDelegation> getTradingSubDelegates();

    @Update(" UPDATE tx_sub_delegation t SET t.state = #{state}, t.update_time = now(),t.info = #{info} " +
            " WHERE t.user_id = #{userId} AND t.id = #{id} AND t.state <> #{state}")
    int updateByStateForFailure(@Param("id") Integer id, @Param("userId") Integer userId, @Param("info") String info, @Param("state") Integer state);
}
