package com.mi.hundsun.oxchains.base.core.mapper.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.po.user.UserInLetter;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户站内信表相关Dao接口<br>
 *
 * @author lzj
 * @date   2018-04-14 10:22:36
 */
public interface UserInLetterMapper extends GenericMapper< UserInLetter,Integer> {


    @Update("UPDATE user_in_letter t SET t.del_flag = 1 WHERE 1=1 ${whereSql}")
    boolean clearMyMsg(Map<String, Object> params);


    @Select("SELECT count(1) FROM user_in_letter  WHERE 1=1 ${whereSql} ${sortSql}")
    long userInLetterCount(Map<String, Object> params);


    @Select("SELECT * FROM user_in_letter WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<UserInLetter> userInLetterList(Map<String, Object> params);
}
