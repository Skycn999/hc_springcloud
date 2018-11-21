package com.mi.hundsun.oxchains.base.core.mapper.tpl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.tpl.NetWorthControlModel;
import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 净值风控模板相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-12 11:36:33
 */
public interface NetWorthControlMapper extends GenericMapper< NetWorthControl,Integer> {

    @Select("SELECT t.* FROM tpl_net_worth_control t WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<NetWorthControlModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM tpl_net_worth_control t WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    /**
     * 更新是否默认（添加时）
     *
     * @param isDefault1
     * @param isDefault2
     * @param delFlag
     */
    @Update("UPDATE tpl_net_worth_control SET is_default = #{isDefault1} WHERE is_default = #{isDefault2} AND del_flag = #{delFlag}")
    void updateBySql(@Param(value = "isDefault1") Integer isDefault1, @Param(value = "isDefault2") Integer isDefault2 , @Param(value = "delFlag") Integer delFlag);


    /**
     * 更新是否默认（编辑时）
     *
     * @param isDefault1
     * @param isDefault2
     * @param delFlag
     */
    @Update("UPDATE tpl_net_worth_control SET is_default = #{isDefault1} WHERE is_default = #{isDefault2} AND del_flag = #{delFlag} AND id != #{id}")
    void updateBySqlEdit(@Param(value = "isDefault1") Integer isDefault1, @Param(value = "isDefault2") Integer isDefault2 ,@Param(value = "delFlag") Integer delFlag,@Param(value = "id") Integer id);

    @Select("SELECT t.* FROM tpl_net_worth_control t WHERE t.state = #{state} AND t.del_flag = #{delFlag} ORDER BY t.is_default = #{isDefault} DESC, t.create_time DESC")
    List<NetWorthControl> findNetWorthControlList(@Param(value = "state") Integer state,@Param(value = "delFlag") Integer delFlag,@Param(value = "isDefault") Integer isDefault);
}
