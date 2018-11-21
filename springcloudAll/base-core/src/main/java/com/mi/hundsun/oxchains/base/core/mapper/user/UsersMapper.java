package com.mi.hundsun.oxchains.base.core.mapper.user;


import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.user.UserCountModel;
import com.mi.hundsun.oxchains.base.core.model.user.UsersModel;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-12 03:56:01
 */
public interface UsersMapper extends GenericMapper< Users,Integer> {

    @Select("SELECT t.*,a.realname_state AS 'realname_state',a.id_positive_pic AS 'a.idPositivePic' FROM users t LEFT JOIN user_identify a ON a.user_id= t.id WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<UsersModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM users t LEFT JOIN user_identify a ON a.user_id= t.id WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM users t WHERE 1=1 AND t.id <> ${id} AND t.mobile = ${mobile} AND t.del_flag = ${delFlag}")
    long mobileRepeatCheck(Map<String, Object> params);

    @Select("SELECT count(1) FROM users t WHERE 1=1 AND t.del_flag = ${delFlag} AND t.id <> ${id} AND t.id_type = ${idType} AND t.id_no = ${idNo}")
    long idNoRepeatCheck(Map<String, Object> params);

    @Select("SELECT count(1) FROM users t WHERE 1=1 AND t.del_flag = ${delFlag} AND t.id <> ${id} AND t.email = '${email}'")
    long emailRepeatCheck(Map<String, Object> params);

    @Select("SELECT t.* FROM users t WHERE 1=1 ${whereSql}")
    List<Users> getUserListByUserId(Map<String, Object> params);

    /**
     * 平台注册用户统计
     */
    @Select(" SELECT count(1) FROM users t WHERE 1=1 ${whereSql}")
    long findUserSum(Map<String, Object> params);

    @Select(" SELECT count(1) AS userRegisterCount, DATE_FORMAT(w.create_time,'%y-%m-%d') AS create_time " +
            " FROM users w " +
            " WHERE DATE_FORMAT(w.create_time,'%y-%m-%d') = DATE_FORMAT(NOW(),'%y-%m-%d')")
    UserCountModel countUserRegisterInfo();

    @Select(" SELECT count(1) AS userRegisterCount, DATE_FORMAT(w.last_login_date,'%y-%m-%d') AS last_login_date " +
            " FROM users w " +
            " WHERE DATE_FORMAT(w.last_login_date,'%y-%m-%d') = DATE_FORMAT(NOW(),'%y-%m-%d')")
    UserCountModel countUserLoginInfo();

    @Select("SELECT count(1) FROM users t WHERE DATE_FORMAT(t.create_time,'%y-%m-%d') = DATE_FORMAT(NOW(),'%y-%m-%d')")
    long countTodayRegisterUser();

    @Select("SELECT count(1) FROM users t WHERE DATE_FORMAT(t.last_login_date,'%y-%m-%d') = DATE_FORMAT(NOW(),'%y-%m-%d')")
    long countTodayLoginUser();

    @Select("SELECT count(1) FROM users t WHERE 1=1")
    long countHistoryRegisterUser();
}
