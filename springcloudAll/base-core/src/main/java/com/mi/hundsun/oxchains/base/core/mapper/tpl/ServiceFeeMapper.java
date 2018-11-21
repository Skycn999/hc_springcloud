package com.mi.hundsun.oxchains.base.core.mapper.tpl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 手续费模板相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-12 11:00:23
 */
public interface ServiceFeeMapper extends GenericMapper< ServiceFee,Integer> {


    /**
     * 更新是否默认（添加时）
     *
     * @param isDefault1
     * @param isDefault2
     * @param delFlag
     */
    @Update("UPDATE tpl_service_fee SET is_default = #{isDefault1} WHERE is_default = #{isDefault2} AND coin_currency = #{coinCurrency} AND del_flag = #{delFlag}")
    void updateBySql(@Param(value = "isDefault1") Integer isDefault1, @Param(value = "isDefault2") Integer isDefault2 ,@Param(value = "coinCurrency") String coinCurrency,@Param(value = "delFlag") Integer delFlag);


    /**
     * 更新是否默认（编辑时）
     *
     * @param isDefault1
     * @param isDefault2
     * @param delFlag
     */
    @Update("UPDATE tpl_service_fee SET is_default = #{isDefault1} WHERE is_default = #{isDefault2} AND coin_currency = #{coinCurrency} AND del_flag = #{delFlag} AND id != #{id}")
    void updateBySqlEdit(@Param(value = "isDefault1") Integer isDefault1, @Param(value = "isDefault2") Integer isDefault2 ,@Param(value = "coinCurrency") String coinCurrency,@Param(value = "delFlag") Integer delFlag,@Param(value = "id") Integer id);


    @Select("SELECT t.* FROM tpl_service_fee t WHERE  t.coin_currency = #{coinCode} AND t.state = #{state} AND t.del_flag = #{delFlag} ORDER BY t.is_default = #{isDefault} DESC, t.create_time DESC")
    List<ServiceFee> findServiceFeeList(@Param(value = "coinCode") String coinCode, @Param(value = "state") Integer state,@Param(value = "delFlag") Integer delFlag,@Param(value = "isDefault") Integer isDefault);

    @Select("SELECT t.* FROM tpl_service_fee t WHERE t.coin_currency = #{coinCurrency} AND t.state = 0 AND t.del_flag = #{delFlag} ORDER BY t.is_default = 1 DESC")
    List<ServiceFee> findServiceFeeByCoin(@Param(value = "coinCurrency")String coinCurrency,@Param(value = "delFlag") Integer delFlag);
}
