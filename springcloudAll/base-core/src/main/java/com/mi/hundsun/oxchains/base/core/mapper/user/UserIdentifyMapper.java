package com.mi.hundsun.oxchains.base.core.mapper.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.user.UserIdentifyModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户认证相关Dao接口<br>
 *
 * @author lzj
 * @date   2018-04-12 03:40:50
 */
public interface UserIdentifyMapper extends GenericMapper< UserIdentify,Integer> {

    //@Select("SELECT t.*,u.user_name AS 'u.userName',u.mobile AS 'u.mobile' FROM biz_user_freeze t LEFT JOIN biz_user u ON u.id = t.user_id  WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize} ")
    @Select("SELECT t.*,u.email AS 'u.email',u.realname AS 'u.realname',u.mobile AS 'u.mobile',u.id_no AS 'u.idNo',u.id_type AS 'u.idType',u.country AS 'u.country' FROM user_identify t LEFT JOIN users u ON u.id = t.user_id WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<UserIdentifyModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM user_identify t LEFT JOIN users u ON u.id = t.user_id  WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    @Update("UPDATE user_identify t SET t.mobile_state = #{mobileState}, t.mobile_time = #{mobileTime} WHERE t.user_id = #{userId}")
    boolean updateBindMobile(@Param(value = "userId")Integer userId,@Param(value = "mobileState") Integer mobileState,@Param(value = "mobileTime") Date mobileTime);

    @Update("UPDATE user_identify t SET t.email_state = #{emailState}, t.email_time = #{emailTime} WHERE t.user_id = #{userId}")
    boolean updateBindEmail(@Param(value = "userId")Integer userId,@Param(value = "emailState") Integer emailState,@Param(value = "emailTime") Date emailTime);

    //后台修改用户时更新
    @Update("UPDATE user_identify t SET t.realname_state = #{realnameState}, t.realname_time = #{realnameTime} WHERE t.user_id = #{userId}")
    boolean updateRealname(@Param(value = "userId")Integer userId,@Param(value = "realnameState") Integer realnameState,@Param(value = "realnameTime") Date realnameTime);

}
