package com.mi.hundsun.oxchains.base.core.mapper.quote;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 交易代码对配置管理相关Dao接口<br>
 *
 * @author db
 * @date   2018-06-10 03:23:44
 */
public interface CodePairConfigMapper extends GenericMapper< CodePairConfig,Integer> {


    @Select("SELECT * FROM ex_code_pair_config e WHERE e.ex_numbers like '%${exchangeNo}%' AND e.del_flag = 0 AND " +
            "e.state = 1 ORDER BY e.type = 1 DESC")
    List<CodePairConfig> selectByExchange(Map<String, Object> params);

    @Select("SELECT * FROM ex_code_pair_config e WHERE e.base_code = '${baseCode}' AND e.code like '%${code}%' AND state = 1 AND " +
            "e.del_flag = 0 ORDER BY e.${orderColumn} ${orderType} LIMIT ${startPos}, ${pageSize}")
    List<CodePairConfig> pageByCodeAndBaseCode(Map<String, Object> params);

    @Select("SELECT * FROM ex_code_pair_config e WHERE e.base_code = '${baseCode}' AND e.code like '%${code}%' AND e.del_flag = 0")
    List<CodePairConfig> findByCodeAndBaseCode(Map<String, Object> params);

    @Select("SELECT * FROM ex_code_pair_config e WHERE (e.code like '%${code}%' OR e.base_code like '%${code}%') AND e.ex_numbers like '%huobi%' AND " +
            "e.del_flag = 0 AND e.state = 1 ORDER BY e.type = 1")
    List<CodePairConfig> searchForApp(Map<String, Object> params);

    @Select("SELECT * FROM ex_code_pair_config e WHERE e.is_display_on_app = 1 AND e.state = 1 AND e.del_flag = 0 ")
    List<CodePairConfig> getDisplayOnAppCodes();

    @Select("SELECT * FROM ex_code_pair_config e WHERE e.state = 1 AND e.del_flag = 0 ")
    List<CodePairConfig> getAllValidCodes();

    @Select("SELECT * FROM ex_code_pair_config e WHERE e.code = #{code} AND e.base_code = #{baseCode} AND e.del_flag = 0")
    CodePairConfig getCodePairConfig(@Param("code") String code, @Param("baseCode") String baseCode);
}
