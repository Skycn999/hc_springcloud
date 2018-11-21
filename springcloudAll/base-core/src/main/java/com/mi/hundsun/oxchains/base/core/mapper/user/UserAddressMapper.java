package com.mi.hundsun.oxchains.base.core.mapper.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.user.UserAddressModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 用户提币地址表相关Dao接口<br>
 *
 * @author lzj
 * @date 2018-04-14 10:22:36
 */
public interface UserAddressMapper extends GenericMapper<UserAddress, Integer> {

    @Select("SELECT t.*,u.mobile AS 'u.mobile',u.id_type AS 'u.idType',u.id_no AS 'u.idNo',u.email AS 'u.email',u.realname AS 'u.realname' FROM user_address t LEFT JOIN users u ON u.id = t.user_id WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<UserAddressModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM user_address t LEFT JOIN users u ON u.id = t.user_id  WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);


    @Select("SELECT t.* FROM user_address t WHERE 1=1 ${whereSql} ${sortSql}")
    List<UserAddress> findUserAddressOrderBy(Map<String, Object> params);


    @Select("SELECT count(1) FROM user_address WHERE 1=1 ${whereSql} ${sortSql}")
    long getUserAddressCount(Map<String, Object> params);


    @Select("SELECT * FROM user_address WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<UserAddress> getUserAddressList(Map<String, Object> params);


    @Update("UPDATE user_address SET is_default = #{isDefault1} WHERE is_default = #{isDefault2} AND user_id = #{userId} AND coin_currency = #{coinCurrency} AND del_flag = #{delFlag}")
    void updateBySql(@Param(value = "isDefault1") Integer isDefault1, @Param(value = "isDefault2") Integer isDefault2, @Param(value = "userId") Integer userId, @Param(value = "coinCurrency") String coinCurrency, @Param(value = "delFlag") Integer delFlag);

    @Update("UPDATE user_address SET del_flag = #{delFlag} WHERE id = #{id} AND user_id = #{userId}")
    int delUserAddress(@Param(value = "delFlag") Integer delFlag, @Param(value = "userId") Integer userId, @Param(value = "id") Integer id);

    @Select("SELECT count(1) FROM user_address t WHERE 1=1 AND t.user_id = #{userId} AND t.coin_currency = #{coinCode} AND t.del_flag = 0")
    long getUserAddressSize(@Param(value = "userId") Integer userId, @Param(value = "coinCode") String coinCode);

    @Update("UPDATE user_address t SET t.del_flag = 1 WHERE 1=1 ${whereSql}")
    int delUserAddressForApp(Map<String, Object> params);


    @Select("SELECT * FROM user_address a WHERE a.user_id = #{userId} AND a.coin_currency = #{code} AND a.state = 1 AND a.del_flag = 0 ORDER BY a.is_default = 1 DESC")
    List<UserAddress> findMyMcAddressByCode(@Param("userId") Integer userId, @Param("code") String code);
}
