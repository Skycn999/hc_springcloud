package com.mi.hundsun.oxchains.base.core.mapper.cms;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.cms.BannerModel;
import com.mi.hundsun.oxchains.base.core.po.cms.Banner;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * banner管理相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-23 09:43:28
 */
public interface BannerMapper extends GenericMapper< Banner,Integer> {

    @Select("SELECT t.* FROM cms_banner t WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<BannerModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM cms_banner t WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    @Select("SELECT t.title,t.pc_path,t.app_path FROM cms_banner t WHERE 1=1 AND t.state = #{state} AND t.del_flag = 0  ORDER BY t.create_time DESC LIMIT 0,#{size} " )
    List<Banner> selectIndexList(@Param("state") Integer state,@Param("size") Integer size);
}
