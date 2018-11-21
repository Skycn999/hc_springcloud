package com.mi.hundsun.oxchains.base.core.tx.mapper;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 交易管理-主委托管理相关Dao接口<br>
 *
 * @author fengting
 * @date 2018-04-13 09:54:58
 */
public interface MainDelegationMapper extends GenericMapper<MainDelegation, Integer> {


    @Select("UPDATE tx_main_delegation t SET t.state = #{state},t.remark = #{remark},t.update_time = now(),t.updator = 'system' where t.delegate_no = #{delegateNo} AND t.del_flag = 0")
    void updateStateByMainNo(@Param("state") int state, @Param("remark") String remark, @Param("delegateNo") String delegateNo);

    @Select("UPDATE tx_main_delegation t SET t.is_split = #{isSplit},t.update_time = now(),t.updator = 'system' where t.delegate_no = #{delegateNo} AND t.del_flag = 0")
    void updateIsSplitByMainNo(@Param("isSplit") int isSplit, @Param("delegateNo") String delegateNo);

    @Select("SELECT * FROM tx_main_delegation WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<MainDelegation> selectByPage(Map<String, Object> params);

    @Select("SELECT COUNT(1) FROM tx_main_delegation WHERE 1=1 ${whereSql}")
    long countBySqlPage(Map<String, Object> params);

    @Select("SELECT * FROM tx_main_delegation WHERE 1=1 AND state in (1,2) AND user_id = #{userId} order BY create_time DESC ")
    List<MainDelegation> getMyCurrDelegates(@Param("userId") Integer userId);

    @Select("SELECT * FROM tx_main_delegation WHERE 1=1 AND state in (1,2) AND user_id = #{userId} and direction = #{direction} order BY create_time DESC")
    List<MainDelegation> getMyCurrDelegatesByDirection(@Param("userId") Integer userId, @Param("direction") Integer direction);

    @Select("SELECT * FROM tx_main_delegation t WHERE t.state in (1,2) AND t.del_flag = 0")
    List<MainDelegation> findTradingMainDelegateList();
}
