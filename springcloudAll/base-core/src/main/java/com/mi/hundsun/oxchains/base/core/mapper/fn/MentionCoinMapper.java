package com.mi.hundsun.oxchains.base.core.mapper.fn;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.fn.MentionCoinModel;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 提币管理相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-15 06:13:26
 */
public interface MentionCoinMapper extends GenericMapper< MentionCoin,Integer> {

    @Select("SELECT t.*,u.email AS 'u.email',u.realname AS 'u.realname',u.mobile AS 'u.mobile',u.id_no AS 'u.idNo',u.id_type AS 'u.idType' FROM fn_mention_coin t LEFT JOIN users u ON u.id = t.user_id WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<MentionCoinModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM fn_mention_coin t LEFT JOIN users u  ON u.id = t.user_id WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM fn_mention_coin WHERE 1=1 ${whereSql} ${sortSql}")
    long myMentionCoinLogCount(Map<String, Object> params);

    @Select("SELECT * FROM fn_mention_coin WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<MentionCoin> myMentionCoinLog(Map<String, Object> params);

    @Select("SELECT SUM(t.amount) FROM fn_mention_coin t WHERE t.user_id = #{userId} AND t.coin_currency = #{code} AND t.state != #{state} AND t.del_flag = 0 AND  t.create_time like #{todayStr} ")
    BigDecimal getMentionCoinByToday(@Param(value = "userId")Integer userId, @Param(value = "code")String code, @Param(value = "state")Integer state,@Param(value = "todayStr")String todayStr);

}
