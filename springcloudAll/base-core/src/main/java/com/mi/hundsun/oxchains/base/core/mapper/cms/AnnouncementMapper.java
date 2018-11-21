package com.mi.hundsun.oxchains.base.core.mapper.cms;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.cms.AnnouncementModel;
import com.mi.hundsun.oxchains.base.core.po.cms.Announcement;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 系统公告相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-23 09:43:28
 */
public interface AnnouncementMapper extends GenericMapper< Announcement,Integer> {

    @Select("SELECT t.* FROM cms_announcement t WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<AnnouncementModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1) FROM cms_announcement t WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

    @Select("SELECT t.* FROM cms_announcement t WHERE 1=1 AND t.state = #{state} AND t.del_flag = 0")
    List<Announcement> getNewAnnouncement(@Param("state") Integer state);

    @Select("SELECT count(1) FROM cms_announcement WHERE 1=1 ${whereSql} ${sortSql}")
    long announcementListCount(Map<String, Object> params);

    @Select("SELECT * FROM cms_announcement WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize}")
    List<Announcement> getAnnouncementList(Map<String, Object> params);
}
