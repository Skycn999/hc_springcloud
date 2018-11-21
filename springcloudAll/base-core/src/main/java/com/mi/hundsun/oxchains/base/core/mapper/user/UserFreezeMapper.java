package com.mi.hundsun.oxchains.base.core.mapper.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.user.UserFreezeModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 冻结功能表相关Dao接口<br>
 *
 * @author lzj
 * @date   2018-04-14 10:22:36
 */
public interface UserFreezeMapper extends GenericMapper< UserFreeze,Integer> {

    @Select("SELECT t.*,u.mobile AS 'u.mobile',u.email AS 'u.email',u.realname AS 'u.realname',u.country AS 'u.country',u.id_type AS 'u.idType',u.id_no AS 'u.idNo' FROM user_freeze t LEFT JOIN users u ON u.id = t.user_id WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<UserFreezeModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM user_freeze t LEFT JOIN users u ON u.id = t.user_id  WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);
}
